package com.quanmin.netty.parser;

import com.quanmin.servlet.ServletConfig;

public class ParserGetter {
	private static final ParserGetter parserGetter = new ParserGetter();
	private ParserGetter(){};
	private IParser httpPostParser;
	private IParser httpGetParser;
	
	public static ParserGetter getInstance(){
		return parserGetter;
	}
	
	public void init(ServletConfig servletConfig) throws InstantiationException, IllegalAccessException{
		Class<?> httpPostParserClazz = (Class<?>)servletConfig.getInitParam(ServletConfig.HTTP_POST_PARSER);
		
		Class<?> httpGetParserClazz = (Class<?>)servletConfig.getInitParam(ServletConfig.HTTP_GET_PARSER);
		
		httpPostParser = (IParser) httpPostParserClazz.newInstance();
		
		httpGetParser = (IParser) httpGetParserClazz.newInstance(); 
	}
	
	public IParser getHttpPostParser(){
		return httpPostParser;
	}
	
	public IParser getHttpGetParser(){
		return httpGetParser;
	}
}
