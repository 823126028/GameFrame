package com.quanmin.servlet;

public interface ServletContextListener {

	void contextInitialized(ServletContext sc,ServletConfig servletConfig);
	
	void contextDestoryed();
}
