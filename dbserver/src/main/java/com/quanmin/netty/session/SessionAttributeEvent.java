package com.quanmin.netty.session;


public class SessionAttributeEvent extends SessionEvent {

	public SessionAttributeEvent(String key, Object value, Session session) {
		super(session);
		
		this.key = key;
		this.value = value;
	}

	public String key;

	public Object value;
}
