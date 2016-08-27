package com.quanmin.netty.http;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.codec.http.HttpVersion;
import org.jboss.netty.handler.codec.http.cookie.Cookie;

public class HttpRespone implements com.quanmin.netty.Response {	
	private Channel channel;
	
	private HttpResponse httpResponse; 

	private Map<String, String> headers; 

	private Map<String, Cookie> cookies; 

	private ByteArrayOutputStream outPutStream;

	public HttpRespone(Channel channel) {
		this.channel = channel;
	}

	@Override
	public Channel getChannel() {
		return channel;
	}

	@Override
	public boolean isWritable() {
		return channel.isWritable();
	}


	@Override
	public ChannelFuture write(Object obj) throws IOException {
		if (channel.isWritable()) {
			getOutPutStream().write((byte[]) obj);
		}
		return null;
	}

	@Override
	public void addCookie(Cookie cookie) {
		getInternalCookies().put(cookie.name(), cookie);
	}	
	
	@Override
	public void addHeader(String name, String value) {
		getHeads().put(name, value);
	}

	@Override
	public Map<String, Cookie> getCookies() {
		return cookies;
	}

	@Override
	public Map<String, String> getHeaders() {
		return headers;
	}

	@Override
	public byte[] getContent() {
		return getOutPutStream().toByteArray();
	}

	private synchronized Map<String, Cookie> getInternalCookies() {
		if (null == cookies) {
			cookies = new HashMap<String, Cookie>(16);
		}
		return cookies;
	}
	

	private synchronized Map<String, String> getHeads() {
		if (null == headers) {
			headers = new HashMap<String, String>(16);
		}
		return headers;
	}


	public synchronized HttpResponse getHttpResponse() {
		if (httpResponse == null) {
			httpResponse = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
		}
						
		return httpResponse;
	}

	public synchronized ByteArrayOutputStream getOutPutStream() {
		if (outPutStream == null) {
			outPutStream = new ByteArrayOutputStream();
		}
		return outPutStream;
	}
	

	public synchronized void setStatus(HttpResponseStatus status) {
		if (httpResponse == null) {
			httpResponse = new DefaultHttpResponse(HttpVersion.HTTP_1_1, status);
			return;
		}
		httpResponse.setStatus(status);
	}

	public synchronized HttpResponseStatus getStatus() {
		if (httpResponse == null) {
			return HttpResponseStatus.OK;
		}
		return httpResponse.getStatus();
	}
}
