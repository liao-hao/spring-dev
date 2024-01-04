spring boot项目开发调试插件

安装
```mvn clean install```

使用
```     
        <dependency>
            <groupId>com.liao</groupId>
            <artifactId>spring-dev</artifactId>
            <version>1.0.0</version>
        </dependency>
```
[LoggingAspect.java](src%2Fmain%2Fjava%2Fcom%2Fliao%2Fspringdev%2Faspect%2FLoggingAspect.java)

LogAspect功能介绍:
1. 打印请求参数
2. 打印AOP代码行号, 在idea中可以直接跳转