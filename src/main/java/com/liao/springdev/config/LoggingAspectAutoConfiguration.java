package com.liao.springdev.config;

import com.liao.springdev.aspect.LoggingAspect;
import com.liao.springdev.listener.StartupApplicationListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

public class LoggingAspectAutoConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspectAutoConfiguration.class);
    @Value("${print.profiles.first}")
    private String printProfilesActive;
    @Bean
    public LoggingAspect loggingAspect() {
        logger.info("\n\n" +
                " __        ______   ______    ______         _______   ________  __     __        ________   ______    ______   __       \n" +
                "|  \\      |      \\ /      \\  /      \\       |       \\ |        \\|  \\   |  \\      |        \\ /      \\  /      \\ |  \\      \n" +
                "| $$       \\$$$$$$|  $$$$$$\\|  $$$$$$\\      | $$$$$$$\\| $$$$$$$$| $$   | $$       \\$$$$$$$$|  $$$$$$\\|  $$$$$$\\| $$      \n" +
                "| $$        | $$  | $$__| $$| $$  | $$      | $$  | $$| $$__    | $$   | $$         | $$   | $$  | $$| $$  | $$| $$      \n" +
                "| $$        | $$  | $$    $$| $$  | $$      | $$  | $$| $$  \\    \\$$\\ /  $$         | $$   | $$  | $$| $$  | $$| $$      \n" +
                "| $$        | $$  | $$$$$$$$| $$  | $$      | $$  | $$| $$$$$     \\$$\\  $$          | $$   | $$  | $$| $$  | $$| $$      \n" +
                "| $$_____  _| $$_ | $$  | $$| $$__/ $$      | $$__/ $$| $$_____    \\$$ $$           | $$   | $$__/ $$| $$__/ $$| $$_____ \n" +
                "| $$     \\|   $$ \\| $$  | $$ \\$$    $$      | $$    $$| $$     \\    \\$$$            | $$    \\$$    $$ \\$$    $$| $$     \\\n" +
                " \\$$$$$$$$ \\$$$$$$ \\$$   \\$$  \\$$$$$$        \\$$$$$$$  \\$$$$$$$$     \\$              \\$$     \\$$$$$$   \\$$$$$$  \\$$$$$$$$\n" +
                "\n" +
                "\n" +
                "\n");
        return new LoggingAspect();
    }


    // 检查是否加载成功
    @PostConstruct
    public void check() {
        logger.info("print.profiles.active:{}", printProfilesActive);
    }

}