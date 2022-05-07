package com.stefbured.oncallserver.config;

import lombok.NonNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContextHolder implements ApplicationContextAware {
    private static SpringContextHolder instance;
    private ApplicationContext applicationContext;

    private SpringContextHolder() {
    }

    public static ApplicationContext getContext() {
        return instance.applicationContext;
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        SpringContextHolder.setInstance(this);
        this.applicationContext = applicationContext;
    }

    private static void setInstance(SpringContextHolder springContextHolder) {
        instance = springContextHolder;
    }
}
