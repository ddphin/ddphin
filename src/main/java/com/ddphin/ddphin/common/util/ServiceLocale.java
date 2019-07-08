package com.ddphin.ddphin.common.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

public class ServiceLocale {
	private static ApplicationContext context;
	
    public static ApplicationContext getContext() {
       return context;
    }
    public static void setContext(ApplicationContext ctx) {  
       context = ctx;  
    }

    public static Object findService(String beanId) { 
    	Object bean = null;
    	try {
    		bean = context.getBean(beanId);
    	}
    	catch (BeansException e) {
    		bean = null;
    	}
    	return bean;
    }
    
    public static <T> Object findService(Class<T> clazz) { 
    	Object bean = null;
    	try {
    		bean = context.getBean(clazz);
    	}
    	catch (BeansException e) {
    		bean = null;
    	}
    	return bean;
    }
}
