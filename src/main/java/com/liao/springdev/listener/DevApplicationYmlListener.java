package com.liao.springdev.listener;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.*;
import org.springframework.core.io.ClassPathResource;

import java.util.Properties;

/**
 * 这个类的主要作用是在Spring Boot应用启动时，将dev-application.yml配置文件加载到环境的PropertySources中，并确保其优先级最高。
 * <p>
 * 1. 该类实现了ApplicationListener接口，并监听了ApplicationEnvironmentPreparedEvent事件。 <br>
 * 2. ApplicationEnvironmentPreparedEvent事件是Spring Boot在准备环境但在加载应用上下文之前发送的事件。<br>
 * 3. onApplicationEvent方法，该方法会在ApplicationEnvironmentPreparedEvent事件发生时被调用。<br>
 * 3. 在onApplicationEvent方法中，我们手动加载了dev-application.yml配置文件，并将其添加到环境的PropertySources中。
 * </p>
 *
 * <p>
 * 最早触发的事件是ApplicationStartingEvent。这个事件在Spring Boot应用启动开始时，但在任何处理之前发送。  然后，依次触发的事件有：
 * <li> ApplicationEnvironmentPreparedEvent：当环境已经准备好，但在创建ApplicationContext之前发送。
 * <li>  ApplicationContextInitializedEvent：当ApplicationContext被创建并准备初始化，但在bean定义被加载之前发送。
 * <li> ApplicationPreparedEvent：当ApplicationContext已经准备好，但在刷新之前发送。
 * <li>ApplicationStartedEvent：当ApplicationContext已经刷新，但在调用ApplicationRunner和CommandLineRunner之前发送。
 * <li>ApplicationReadyEvent：当所有ApplicationRunner和CommandLineRunner已经被调用后发送。
 * <li>ApplicationFailedEvent：如果在启动过程中有异常，会发送这个事件。
 * <li>所以，ApplicationEnvironmentPreparedEvent并不是Spring Boot最早的事件，而是在环境准备好之后触发的事件。
 * </p>
 */
public class DevApplicationYmlListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(DevApplicationYmlListener.class);

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        addAfterRamon(event, "_dev-application.yml"); // 开发用的优化配置
        addAfterRamon(event, "first.yml"); // 可以用main覆盖dev

    }

    private void addAfterRamon(ApplicationEnvironmentPreparedEvent event, String path) {
        MutablePropertySources propertySources = event.getEnvironment().getPropertySources();
        YamlPropertiesFactoryBean factoryBean = new YamlPropertiesFactoryBean();
        ClassPathResource classPathResource = new ClassPathResource(path);
        if (classPathResource.exists()) {
            factoryBean.setResources(classPathResource);
            Properties properties = factoryBean.getObject();
            PropertySource<?> propertySource = new PropertiesPropertySource("devApplicationYmlPropertySource", properties);
            logger.info("addFirst {} to PropertySources", path);
            propertySources.addAfter("random", propertySource);
            logger.info("propertySources 顺序 {}", propertySources);
        }
    }


/*
    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
     // 3 放在ramdon后面
        MutablePropertySources propertySources = event.getEnvironment().getPropertySources();

        YamlPropertiesFactoryBean factoryBean = new YamlPropertiesFactoryBean();
        factoryBean.setResources(new ClassPathResource("dev-application.yml"));
        Properties properties = factoryBean.getObject();
        PropertySource<?> propertySource = new PropertiesPropertySource("devApplicationYmlPropertySource", properties);
        logger.info("addFirst dev-application.yml to PropertySources");
        propertySources.addAfter("random",propertySource);
        logger.info("propertySources 顺序 {}", propertySources);
    }
*/


//
//    @Override
//    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
//        logger.info("Added dev-application.yml into system properties");
//        YamlPropertiesFactoryBean factoryBean = new YamlPropertiesFactoryBean();
//        factoryBean.setResources(new ClassPathResource("dev-application.yml"));
//        Properties properties = factoryBean.getObject();
//        Enumeration<?> enumeration = properties.propertyNames();
//        StringBuilder sb = new StringBuilder();
//        while (enumeration.hasMoreElements()) {
//            String key = (String) enumeration.nextElement();
//            String value = properties.getProperty(key);
//            if (System.getProperty(key) == null) {
//                System.setProperty(key, value);
//                sb.append(key).append("=");
//                sb.append(value).append("\n");
//            }
//        }
//        logger.info("set system property {} \n", sb);
//    }
//
//
//
//    @Override
//    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
//
//        MutablePropertySources propertySources = event.getEnvironment().getPropertySources();
//        YamlPropertiesFactoryBean factoryBean = new YamlPropertiesFactoryBean();
//        factoryBean.setResources(new ClassPathResource("dev-application.yml"));
//        Properties properties = factoryBean.getObject();
//        PropertySource<?> propertySource = new PropertiesPropertySource("devApplicationYmlPropertySource", properties);
//        logger.info("addFirst dev-application.yml to PropertySources");
//        propertySources.addFirst(propertySource);
//        logger.info("propertySources 顺序 {}", propertySources);
//    }


}