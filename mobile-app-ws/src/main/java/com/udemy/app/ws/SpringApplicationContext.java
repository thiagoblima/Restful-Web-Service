package com.udemy.app.ws;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringApplicationContext implements ApplicationContextAware {

	private static ApplicationContext CONTEXT;
	
	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		CONTEXT = context;
	}
	
	// We have access to all beans from the ApplicationContext
	public static Object getBean(String beanName) {
		return CONTEXT.getBean(beanName);
	}
}
