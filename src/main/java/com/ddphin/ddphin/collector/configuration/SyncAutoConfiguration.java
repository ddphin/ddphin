package com.ddphin.ddphin.collector.configuration;
/**
 * ClassName: SyncAutoConfiguration
 * Function:  SyncAutoConfiguration
 * Date:      2019/7/2 下午8:39
 * Author     DaintyDolphin
 * Version    V1.0
 */

import com.ddphin.ddphin.collector.interceptor.SynchronizerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@AutoConfigureAfter(CollectorAutoConfiguration.class)
public class SyncAutoConfiguration implements WebMvcConfigurer {
    @Autowired
    private CollectorProperties properties;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (null != properties.getApi() && !properties.getApi().isEmpty()) {
            registry.addInterceptor(new SynchronizerInterceptor()).addPathPatterns(properties.getApi());
        }
    }
}