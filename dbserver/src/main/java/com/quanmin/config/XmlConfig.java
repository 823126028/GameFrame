package com.quanmin.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.quanmin.servlet.ServletConfig;

public class XmlConfig {
	/** listenerList */
	private List<Class<?>> listenerList = new ArrayList<Class<?>>();
	
	private Map<String, Object> servletParamsMap = new HashMap<String, Object>();
	
	public ServletConfig getServletConfig(){
		return new ServletConfig() {
			
			@Override
			public List<Class<?>> getListeners() {
				return listenerList;
			}
			
			@Override
			public Map<String, Object> getInitParams() {
				return servletParamsMap;
			}
			
			@Override
			public Object getInitParam(String paramName) {
				return servletParamsMap.get(paramName);
			}
		};
	}

	public XmlConfig(String path) {
		parse(path);
	}
	
	private void parseNorma(Document doc,String key,Class<?> type) throws ClassNotFoundException, DOMException{
		Element servletElement = (Element) doc.getElementsByTagName(key).item(0);
		if (null == servletElement) {
			throw new RuntimeException("can't parse servlet config, can't found[" + key + "]element");
		}
		Object param = null;
		if(type == Integer.class){
			param = Integer.parseInt(servletElement.getTextContent().trim());
		}else if(type == Class.class){
			param = Class.forName(servletElement.getTextContent().trim());
		}else if(type == String.class){
			param = servletElement.getTextContent().trim();
		}
		this.servletParamsMap.put(key,param);
	}
	
	private void parseListeners(Document doc) throws ClassNotFoundException, DOMException{
		NodeList nodeList = doc.getElementsByTagName("listener-class");
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (node instanceof Element) {
				Element element = (Element) node;
				listenerList.add((Class<?>)Class.forName(element.getTextContent()));
			}
		}
	}
	
	
	@SuppressWarnings("unchecked")
	private void parse(String path) {
		if (StringUtils.isBlank(path)) {
			throw new RuntimeException("can't parse servlet config, path must not be null");
		}
				
		DocumentBuilder db;
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setValidating(false);
			dbf.setNamespaceAware(false);
			
			db = dbf.newDocumentBuilder();
			
			db.setErrorHandler(new ErrorHandler() {				
				@Override
				public void warning(SAXParseException arg0) throws SAXException {
					throw arg0;
				}				
				@Override
				public void fatalError(SAXParseException arg0) throws SAXException {
					throw arg0;
				}				
				@Override
				public void error(SAXParseException arg0) throws SAXException {
					throw arg0;
				}
			}); 
			Document doc = db.parse(XmlConfig.class.getClassLoader().getResourceAsStream(path));
			parseListeners(doc);
			parseNorma(doc, ServletConfig.HTTP_GET_PARSER,Class.class);
			parseNorma(doc, ServletConfig.HTTP_POST_PARSER,Class.class);
			parseNorma(doc, ServletConfig.SESSION_HTTP_PORT,Integer.class);
			parseNorma(doc, ServletConfig.SESSION_TCP_PORT,Integer.class);
			parseNorma(doc, ServletConfig.SESSION_TICK_INTERVAL,Integer.class);
			parseNorma(doc, ServletConfig.SESSION_TIME_OUT,Integer.class);
		} catch (Exception e) {
			throw new RuntimeException("can't parse servlet config, have a exception", e);
		}
	}

	public static void main(String[] args) {
		XmlConfig config = new XmlConfig("conf.xml");
	}

}
