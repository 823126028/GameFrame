package com.quanmin.system.listener;

import org.springframework.context.ApplicationContext;

import com.quanmin.netty.parser.ParserGetter;
import com.quanmin.servlet.ServletConfig;
import com.quanmin.servlet.ServletContext;
import com.quanmin.servlet.ServletContextListener;
import com.quanmin.spring.wrapper.ObjectFactory;
import com.quanmin.spring.wrapper.SpringObjectFactory;
import com.quanmin.system.service.ServerInitManager;

public class InitServerListener implements ServletContextListener {
	protected ServletContext context;
	protected static ObjectFactory objectFactory;

	public ServletContext getServletContext() {
		return this.context;
	}

	private ObjectFactory getObjectFactory() {
		if (objectFactory == null) {
			SpringObjectFactory factory = new SpringObjectFactory();
			ApplicationContext applicationContext = (ApplicationContext) getServletContext()
					.getAttribute(ServletContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
			if (applicationContext == null) {
				objectFactory = new ObjectFactory();
			} else {
				factory.setApplicationContext(applicationContext);
				objectFactory = factory;
			}
		}
		return objectFactory;
	}

	public void init(ServletContext context,ServletConfig servletConfig) {
		this.context = context;
		try {
			ParserGetter.getInstance().init(servletConfig);
			ServerInitManager serverInitManager = (ServerInitManager) getObjectFactory()
					.buildBean(ServerInitManager.class);
			serverInitManager.init();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void contextInitialized(ServletContext context,ServletConfig servletConfig) {
		init(context,servletConfig);
	}

	@Override
	public void contextDestoryed() {
	}
}
