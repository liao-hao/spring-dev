package com.liao.springdev.config;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

import java.io.IOException;
import java.util.Properties;

public class YamlPropertySourceFactory implements PropertySourceFactory {
    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {
        YamlPropertiesFactoryBean factoryBean = new YamlPropertiesFactoryBean();
        factoryBean.setResources(resource.getResource());

        Properties properties = factoryBean.getObject();
        String propertySourceName = (name != null) ? name : "YamlPropertySource";

        PropertiesPropertySource propertiesPropertySource = new PropertiesPropertySource(propertySourceName, properties);
        return propertiesPropertySource;
    }
}