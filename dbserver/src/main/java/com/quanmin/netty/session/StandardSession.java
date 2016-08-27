package com.quanmin.netty.session;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.jboss.netty.channel.Channel;

import com.quanmin.TimeConstants;
import com.quanmin.netty.Response;
import com.quanmin.servlet.ServletConfig;

public class StandardSession implements Session {

	private static enum Type {
		CREATE, DESTORY, ADD, REPLACE, REMOVE
	}
	/** map */
	public ConcurrentMap<String, Object> map = new ConcurrentHashMap<String, Object>();

	/** sessionId */
	public String id;
	public long createTime;

	public long lastAccessTime;

	public boolean isValid = true;

	public boolean expire = false;

	/** Channel */
	public Channel channel;

	public Response response = null;

	public List<SessionListener> sessionListeners;

	public List<SessionAttributeListener> sessionAttributeListeners;

	public ServletConfig servletConfig;
	
	private boolean discard = false;

	public StandardSession() {
	}
	
	public StandardSession(String id,
			List<SessionListener> sessionListeners,
			List<SessionAttributeListener> sessionAttributeListeners,
			ServletConfig servletConfig) {
		this.id = id;
		this.createTime = System.currentTimeMillis();
		this.lastAccessTime = System.currentTimeMillis();
		this.sessionListeners = sessionListeners;
		this.sessionAttributeListeners = sessionAttributeListeners;
		this.servletConfig = servletConfig;
		notifyListener(sessionListeners, Type.CREATE);
	}


	@Override
	public Object getAttribute(String key) {
		return map.get(key);
	}


	@Override
	public void setAttribute(String key, Object value) {
		Object obj = map.put(key, value);
		if (null == obj) {
			notifyListener(sessionAttributeListeners, new SessionAttributeEvent(key, value, this), Type.ADD);
		} else {
			notifyListener(sessionAttributeListeners, new SessionAttributeEvent(key, obj, this), Type.REPLACE);
		}
	}

	@Override
	public boolean removeAttribute(String key) {
		Object obj = map.remove(key);
		notifyListener(sessionAttributeListeners, new SessionAttributeEvent(key, obj, this), Type.REMOVE);
		return null != obj;
	}
	

	@Override
	public void markDiscard() {
		this.discard = true;
	}


	@Override
	public void invalidate() {
		if (discard) {
			discard();
			return;
		}
		
		try {
			notifyListener(sessionListeners, Type.DESTORY);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (null != channel) {
			try {
				channel.close();
			} catch (Exception e) {
			}
		}
		if (null != response && null != response.getChannel()) {
			try {
				response.getChannel().close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		map.clear();
		response = null;
		channel = null;
		map = null;
		SessionManager.getInstance().sessions.remove(id); 
	}

	@Override
	public String getId() {
		return id;
	}


	@Override
	public void access() {
		this.lastAccessTime = System.currentTimeMillis();
	}


	@Override
	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

	@Override
	public boolean isValid() {
		if (expire) {
			return false;
		}

		if (!isValid) {
			return false;
		}
		
		if (discard) {
			return false;
		}

		if ((System.currentTimeMillis() - lastAccessTime) < (Integer)servletConfig.getInitParam(ServletConfig.SESSION_TIME_OUT) * TimeConstants.SECOND_MILLIONS) {
			return true;
		} else {
			isValid = false;
			expire = true;
		}

		return false;
	}


	@Override
	public void expire() {
		invalidate();
	}


	@Override
	public void setChannel(Channel channel) {
		if (null != this.channel 
				&& this.channel.isOpen() 
				&& !this.channel.getId().equals(channel.getId())) {
			this.channel.close();
		}
		safeCloseResponse();
		this.channel = null;
		this.channel = channel;
	}


	@Override
	public Response getResponse() {
		return response;
	}


	@Override
	public Channel getChannel() {
		return channel;
	}

    /**
	 * @see com.reign.framework.netty.servlet.Session#setResponse(com.reign.framework.netty.servlet.Response)
	 */
	@Override
	public void setResponse(Response response) {
		if (null != this.response
				&& null != this.response.getChannel()
				&& this.response.getChannel().isOpen()
				&& !this.response.getChannel().getId().equals(response.getChannel().getId())) {
			this.response.getChannel().close();
		}
		safeCloseChannel();
		this.response = null;
		this.response = response;
	}
	

	private void safeCloseChannel() {
		if (null != this.channel 
				&& this.channel.isOpen()) {
			this.channel.close();
		}
		this.channel = null;
	}
	

	private void safeCloseResponse() {
		if (null != this.response
				&& null != this.response.getChannel()
				&& this.response.getChannel().isOpen()) {
			this.response.getChannel().close();
		}
		this.response = null;
	}
	

	private void notifyListener(List<SessionListener> sessionListeners, Type type) {
		for (SessionListener listener : sessionListeners) {
			switch (type) {
			case CREATE:
				listener.sessionCreated(new SessionEvent(this));
				break;
			case DESTORY:
				listener.sessionDestroyed(new SessionEvent(this));
				break;
			default:
				break;
			}
		}
	}

	private void notifyListener(List<SessionAttributeListener> sessionAttributeListeners, SessionEvent event, Type type) {
		for (SessionAttributeListener listener : sessionAttributeListeners) {
			switch (type) {
			case ADD:
				listener.attributeAdded((SessionAttributeEvent) event);
				break;
			case REMOVE:
				listener.attributeRemoved((SessionAttributeEvent) event);
				break;
			case REPLACE:
				listener.attributeReplaced((SessionAttributeEvent) event);
				break;
			default:
				break;
			}
		}

	}

	
	private void discard() {
		try {
			notifyListener(sessionListeners, Type.DESTORY);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (null != response && null != response.getChannel()) {
			try {
				response.getChannel().close();
			} catch (Exception e) {
			}
		}
		map.clear();
		response = null;
		channel = null;
		map = null;
		SessionManager.getInstance().sessions.remove(id); 
	}
	
}
