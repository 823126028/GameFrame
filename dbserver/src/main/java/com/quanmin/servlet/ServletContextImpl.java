package com.quanmin.servlet;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ServletContextImpl
  implements ServletContext
{
  private ConcurrentMap<String, Object> map = new ConcurrentHashMap<String,Object>();
  private ServletConfig config;
  
  public ServletContextImpl() {}
  
  public ServletContextImpl(ServletConfig config)
  {
    this.config = config;
  }
  
  public Object getAttribute(String key)
  {
    return this.map.get(key);
  }
  
  public Object setAttribute(String key, Object value)
  {
    return this.map.put(key, value);
  }
  
  public boolean removeAttribute(String key)
  {
    return this.map.remove(key) != null;
  }
  
  public void invalidate()
  {
    this.map.clear();
  }
  
  public Object getInitParam(String paramName)
  {
    if (this.config == null) {
      return null;
    }
    return this.config.getInitParam(paramName);
  }
}
