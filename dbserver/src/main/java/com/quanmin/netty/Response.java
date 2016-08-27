package com.quanmin.netty;

import java.io.IOException;
import java.util.Map;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.handler.codec.http.cookie.Cookie;

public interface Response {

	public Channel getChannel();
	
	public boolean isWritable();
	
	public ChannelFuture write(Object obj) throws IOException;
	
	public void addCookie(Cookie cookie);
	
	public Map<String, Cookie> getCookies();
	
	public Map<String, String> getHeaders();
	
	public void addHeader(String name, String value);
	
	public byte[] getContent();
}
