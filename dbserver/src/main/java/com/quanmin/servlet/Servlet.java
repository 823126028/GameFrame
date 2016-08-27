package com.quanmin.servlet;

import java.io.Serializable;

import com.quanmin.netty.Request;
import com.quanmin.netty.Response;

public interface Servlet extends Serializable {

	void init(ServletConfig config, ServletContext context);

	ServletConfig getServletConfig();
	
	ServletContext getServletContext();

	void service(Request request,Response response);

	void destroy();
	
}
