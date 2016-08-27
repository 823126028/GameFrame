package com.quanmin.spring.wrapper;


public class ObjectFactory
{
  public Object buildBean(Class<?> clazz)
    throws InstantiationException, IllegalAccessException
  {
    Object o = clazz.newInstance();
    return o;
  }
}
