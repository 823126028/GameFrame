package com.quanmin.netty.session;


public interface SessionAttributeListener {
	/** Notification that an attribute has been added to a session. Called after the attribute is added.*/
    public void attributeAdded ( SessionAttributeEvent se );
	/** Notification that an attribute has been removed from a session. Called after the attribute is removed. */
    public void attributeRemoved ( SessionAttributeEvent se );
	/** Notification that an attribute has been replaced in a session. Called after the attribute is replaced. */
    public void attributeReplaced ( SessionAttributeEvent se );
}
