package com.quanmin.servlet;

public abstract interface ServletContext
{
  public static final String ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE = ServletContext.class.getName() + ".Spring.Root";
  public static final String ROOT_WEB_APPLICATION_SERVLET_ATTRIBUTE = ServletContext.class.getName() + ".Servlet.Root";
  
  public abstract Object getAttribute(String paramString);
  
  public abstract Object setAttribute(String paramString, Object paramObject);
  
  public abstract boolean removeAttribute(String paramString);
  
  public abstract void invalidate();
  
  public abstract Object getInitParam(String paramString);
}
