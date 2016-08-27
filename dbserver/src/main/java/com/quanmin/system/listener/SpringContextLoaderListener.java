package com.quanmin.system.listener;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.quanmin.servlet.ServletConfig;
import com.quanmin.servlet.ServletContext;
import com.quanmin.servlet.ServletContextListener;

public class SpringContextLoaderListener implements ServletContextListener {

	public void contextInitialized(ServletContext sc,ServletConfig servletConfig) {
		init(sc,servletConfig);
	}

	private void init(ServletContext sc,ServletConfig servletConfig) {
		if (sc.getAttribute(ServletContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE) != null) {
			throw new IllegalStateException("Can not initialize context because there is already exists");
		}
		ApplicationContext ac = createApplicationContext(sc);
		sc.setAttribute(ServletContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, ac);
	}

	private ApplicationContext createApplicationContext(ServletContext sc) {
		ClassPathXmlApplicationContext cpxac = new ClassPathXmlApplicationContext("applicationContext.xml");
		return cpxac;
	}

	@Override
	public void contextDestoryed() {
	}
}
