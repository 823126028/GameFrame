package com.quanmin.netty.session;
import org.jboss.netty.channel.Channel;

import com.quanmin.netty.Response;

public interface Session {

    public String getId();

	public Object getAttribute(String key);

	public void setAttribute(String key, Object value);

	public boolean removeAttribute(String key);

	public void invalidate();

	public void markDiscard();

	public void access();

	public void setValid(boolean isValid);
	
	public boolean isValid();

	public void expire();

	public void setChannel(Channel channel);

	public void setResponse(Response response);
	
	public Channel getChannel();
	
	public Response getResponse();
}
