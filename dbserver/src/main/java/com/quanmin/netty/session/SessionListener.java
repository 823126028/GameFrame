package com.quanmin.netty.session;


import java.util.EventListener;

public interface SessionListener extends EventListener {
	/** 
	* Notification that a session was created.
	* @param se the notification event
	*/
    public void sessionCreated ( SessionEvent se );
    
	/** 
	* Notification that a session is about to be invalidated.
	* @param se the notification event
	*/
    public void sessionDestroyed ( SessionEvent se );
}
