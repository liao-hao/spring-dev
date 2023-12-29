package com.liao.springdev.config;

import com.liao.springdev.aspect.LoggingAspect;
import com.liao.springdev.listener.StartupApplicationListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggingAspectAutoConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspectAutoConfiguration.class);

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
}