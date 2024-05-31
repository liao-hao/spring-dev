package com.liao.springdev.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import javax.annotation.PostConstruct;

@Configuration
@PropertySources({@PropertySource(value = "classpath:dev-application.yml", factory = YamlPropertySourceFactory.class)})
public class PluginConfiguration {

    @Value("${test.test}")
    private String url;


    @PostConstruct
    public void init() {
        System.out.println("PluginConfiguration init");
        System.out.println("url: " + url);
    }

}