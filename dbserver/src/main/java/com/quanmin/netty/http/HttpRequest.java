package com.quanmin.netty.http;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.http.cookie.Cookie;
import org.jboss.netty.handler.codec.http.cookie.DefaultCookie;

import com.quanmin.ServerConstants;
import com.quanmin.netty.Request;
import com.quanmin.netty.Response;
import com.quanmin.netty.parser.IParser;
import com.quanmin.netty.parser.ParserGetter;
import com.quanmin.netty.session.Session;
import com.quanmin.netty.session.SessionManager;
import com.quanmin.servlet.ServletConfig;
import com.quanmin.servlet.ServletContext;


public class HttpRequest implements Request{
	//用户登录的Id
	private String sessionId;
	//解析出来的参数
	private Map<String,String[]> paramMap = new HashMap<String,String[]>();
	
	private ChannelHandlerContext ctx;

	private Response response;
	
	private Map<String, Cookie> cookies;
	
	/** headers */
	private Map<String, String> headers;
	
	private String url;
	
	private String command;
	
	//用于解析参数顺序
	private int requestId;
	
	private IParser parser;
	
	public HttpRequest(ChannelHandlerContext ctx,
			boolean isPost,
			byte[] content,
			String command, 
			Map<String, Cookie> cookies,
			Map<String, String> headers,
			Response response)  {
		this.ctx = ctx;
		this.response = response;
		this.cookies = cookies;
		this.headers = headers;
		
		if(isPost){
			parser = ParserGetter.getInstance().getHttpPostParser();
		}else{
			parser = ParserGetter.getInstance().getHttpGetParser();
		}
		try {
			parser.parseParam(content, paramMap);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String[] value = getParamterValues(ServerConstants.COMMAND);
		this.command = null == value ? null : value[0];
		
		sessionId = getCookieValue(ServerConstants.JSESSIONID);
		SessionManager.getInstance().access(sessionId);
	    Session session = getSession(false);
	     if (session != null) {
	        session.setResponse(response);
	     }
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public Map<String, String[]> getParamterMap() {
		if(paramMap == null){
			paramMap = new HashMap<String,String[]>();
		}
		return paramMap;
	}

	@Override
	public String[] getParamterValues(String key) {
		if(paramMap == null){
			paramMap = new HashMap<String,String[]>();
		}
		return paramMap.get(key);
	}

	@Override
	public Session getSession() {
		return getSession(true);
	}

	@Override
	public Session getSession(boolean allowCreate) {
		Session session = SessionManager.getInstance().getSession(sessionId, allowCreate);
		if (allowCreate && (null != session && !session.getId().equals(sessionId))) {
			sessionId = session.getId();
			response.addCookie(new DefaultCookie(ServerConstants.JSESSIONID, session.getId()));
		}
		if (null != session) {
			session.access();
		}
		return session;
	}

	@Override
	public Session getNewSession() {
		Session session = SessionManager.getInstance().getSession(null, true);
		sessionId = session.getId();
		response.addCookie(new DefaultCookie(ServerConstants.JSESSIONID, session.getId()));
		session.access();
		return session;
	}
	@Override
	public String getCommand() {
		return command;
	}

	@Override
	public int getRequestId() {
		return this.getRequestId();
	}

	@Override
	public Object getAttachment() {
		return ctx.getAttachment();
	}


	@Override
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
		
	}

	@Override
	public void setAttachment(Object obj) {
		ctx.setAttachment(obj);	
	}

	@Override
	public String getHeader(String key) {
		return headers.get(key);
	}

	@Override
	public String getCookieValue(String key) {
		Cookie cookie = cookies.get(key);
		if(cookie == null){
			return null;
		}else{
			return cookie.value();
		}
	}

	@Override
	public Collection<Cookie> getCookies() {
		return cookies.values();
	}

	@Override
	public InetSocketAddress getRemoteAddress() {
		return (InetSocketAddress) ctx.getChannel().getRemoteAddress();
	}

	@Override
	public void pushAndClose(ChannelBuffer buffer) {
		throw new UnsupportedOperationException("http request can't offer this operation");
	}
}
