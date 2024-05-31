package com.liao.springdev.listener;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

public class StartupApplicationListener implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger logger = LoggerFactory.getLogger(StartupApplicationListener.class);

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        logger.info("引用插件成功");
    }
}