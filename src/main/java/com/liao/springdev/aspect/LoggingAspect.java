package com.liao.springdev.aspect;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 打印请求日志
 * // LIAO TODO 2023/12/29:没有过滤request / 资源等
 */
@Aspect
@Component
public class LoggingAspect {
    private static final List<Class<?>> NON_PRINTABLE_TYPES = Arrays.asList(Closeable.class, File.class, MultipartFile.class, byte[].class, HttpServletResponse.class, HttpServletRequest.class
            // Add other non-printable types here
    );

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
    private static ObjectMapper objectMapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
    private static ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();

    private static final int MAX_CACHE_SIZE = 1000;

    private static final Map<String, Integer> lineNumberCache = new LinkedHashMap<String, Integer>(MAX_CACHE_SIZE, 0.75f, true) {
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, Integer> eldest) {
            return size() > MAX_CACHE_SIZE;
        }
    };

    @Pointcut("execution(public * com..*Controller.*(..))")
    public void webLog() {
    }

    @Around("webLog()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        String logMessage = getLogMessage(joinPoint);
        logger.info(logMessage);
        printArgs(joinPoint);
        Object result = joinPoint.proceed();

        logger.info("RESPONSE : " + objectMapper.writeValueAsString(result));
        logger.info("SPEND TIME : " + (System.currentTimeMillis() - startTime));
        return result;
    }

    private String getLogMessage(ProceedingJoinPoint joinPoint) throws JsonProcessingException {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // 获取当前执行的方法名、行号和文件名

        // 获取被切的方法的信息
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        String fileName = className.substring(className.lastIndexOf(".") + 1) + ".java";
        int lineNumber = 1;
        try {
            lineNumber = findLineNumber(className, methodName);
        } catch (IOException ignored) {
        }

        String logMessage = String.format("CLASS_METHOD : %s.%s(%s:%s)\nURL : %s %s | IP : %s \nPARAMETER: %s ARGS : %s",
                className,
                methodName,
                fileName,
                lineNumber,
                request.getMethod(),
                request.getRequestURL().toString(),
                request.getRemoteAddr(),
                getParameter(joinPoint),
                printArgs(joinPoint));
        return logMessage;
    }



    private static int findLineNumber(String className, String methodName) throws IOException {
        if (lineNumberCache.containsKey(className + methodName)) {
            return lineNumberCache.get(className + methodName);
        }
        int lineNumber = findLineNumber0(findJavaFilePath(className), methodName);
        lineNumberCache.put(className + methodName, lineNumber);
        return lineNumber;
    }

    private static int findLineNumber0(String filePath, String methodName) throws IOException {
        Pattern pattern = Pattern.compile("\\s*" + methodName + "\\s*\\(");
        Matcher matcher = pattern.matcher("");
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath))) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                matcher.reset(line);
                if (matcher.find()) {
                    return lineNumber;
                }
            }
        }
        return 1; // Method not found
    }

    private static String findJavaFilePath(String className) {
        String classFilePath = ClassLoader.getSystemResource("").getPath();
        String classPathRoot = classFilePath.substring(1, classFilePath.indexOf("/target/classes/"));
        String javaFilePath = classPathRoot + "/src/main/java/" + className.replace('.', '/') + ".java";
        return javaFilePath;
    }

    private String getParameter(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if (args == null) {
            return "";
        }
        StringBuilder argClassNames = new StringBuilder();
        for (Object arg : args) {
            if (arg != null) {
                argClassNames.append(arg.getClass().getSimpleName()).append(", ");
            }
        }
        // Remove the last comma and space
        if (argClassNames.length() > 0) {
            argClassNames.setLength(argClassNames.length() - 2);
        }
        return argClassNames.toString();
    }

    private String printArgs(ProceedingJoinPoint joinPoint) throws JsonProcessingException {
        Object[] args = joinPoint.getArgs();
        if (args == null) {
            return null;
        }
        for (Object arg : args) {
            if (NON_PRINTABLE_TYPES.stream().anyMatch(type -> type.isInstance(arg))) {
                return null;
            }
        }
        return objectWriter.writeValueAsString(args);
    }
}