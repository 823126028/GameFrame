//package com.quanmin.system.interceptors;
//
//import java.util.Iterator;
//
//import com.quanmin.netty.Request;
//import com.quanmin.netty.Response;
//import com.quanmin.system.result.Result;
//
//public class AuthInterceptor {
//	public Result<?> interceptor(ActionInvocation invocation, Iterator<Interceptor> interceptors, Request request, Response response)throws Exception{
//		  Result<?> obj = invocation.invoke(interceptors, request, response);
//		  return obj;
//	}
//}
