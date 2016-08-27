package com.quanmin.netty;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Map;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.handler.codec.http.cookie.Cookie;
import com.quanmin.netty.session.Session;

public interface Request {
	public Map<String, String[]> getParamterMap();

	public String[] getParamterValues(String key);

	public Session getSession();
	
	public Session getSession(boolean allowCreate);
	
	public Session getNewSession();
	
	public String getCommand();
	
	public int getRequestId();

	public Object getAttachment();
	
	public void setSessionId(String sessionId);
	
	public void setAttachment(Object obj);
	
	public String getHeader(String key);
	
	public String getCookieValue(String key);
	
	public Collection<Cookie> getCookies();
	
	public InetSocketAddress getRemoteAddress();

	public void pushAndClose(ChannelBuffer buffer);
}
