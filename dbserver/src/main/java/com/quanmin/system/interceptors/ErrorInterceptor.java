package com.quanmin.system.interceptors;

import java.util.Iterator;

import com.quanmin.netty.Request;
import com.quanmin.netty.Response;
import com.quanmin.system.action.RouteWay;
import com.quanmin.system.result.ByteResult;
import com.quanmin.system.result.Result;

public class ErrorInterceptor implements Interceptor{
	public static  ByteResult E0010_BYTERESULT = new ByteResult("error_result".getBytes());
	
	@Override
	public Result<?> interceptor(RouteWay routeWay, Iterator<Interceptor> iterator, Request request,
			Response response)  {
		try{
			return routeWay.invocate(routeWay, iterator, request, response);
		}catch(Exception e){
			System.out.println("error exception catch");
			e.printStackTrace();
			return E0010_BYTERESULT;
		}
	}
}
