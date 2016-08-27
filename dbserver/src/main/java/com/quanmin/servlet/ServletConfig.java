package com.quanmin.servlet;


import java.util.List;
import java.util.Map;

public interface ServletConfig {
	public static final String SESSION_TICK_INTERVAL = "session-tick-time"; 
	
	public static final String SESSION_TIME_OUT = "session-time-out";
	
	public static final String SESSION_TCP_PORT = "tcp-port";
	
	public static final String SESSION_HTTP_PORT = "http-port";

	public static final String HTTP_POST_PARSER = "http-post-parser";
	
	public static final String HTTP_GET_PARSER = "http-get-parser";
	
	List<Class<?>> getListeners();

	Object getInitParam(String paramName);

	Map<String, Object> getInitParams();
}
