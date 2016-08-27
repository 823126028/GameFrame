package com.quanmin.servlet;
import org.springframework.context.ApplicationContext;
import com.quanmin.netty.Request;
import com.quanmin.netty.Response;
import com.quanmin.system.action.RouteWay;
import com.quanmin.system.interceptors.InterceptorGetter;

public class DispatchServlet implements Servlet{
	private static final long serialVersionUID = 1L;
	private ServletConfig config;
	private ServletContext context;
	
	@Override
	public void init(ServletConfig config, ServletContext context) {
		this.config = config;
		this.context = context;
	}

	@Override
	public ServletConfig getServletConfig() {
		return this.config;
	}

	@Override
	public ServletContext getServletContext() {
		return this.context;
	}

	@Override
	public void destroy() {
	}

	@Override
	public void service(Request request, Response response) {
		ApplicationContext applicationContext = (ApplicationContext)getServletContext().getAttribute(ServletContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
		RouteWay routeWay = applicationContext.getBean(RouteWay.class);
		routeWay.invocate(routeWay,InterceptorGetter.getInterceptorList().iterator(), request, response);
	}

}
