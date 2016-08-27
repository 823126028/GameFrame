package com.quanmin.system.interceptors;
import java.util.Iterator;

import com.quanmin.netty.Request;
import com.quanmin.netty.Response;
import com.quanmin.system.action.RouteWay;
import com.quanmin.system.result.Result;

public class TimeLogInterceptor implements Interceptor{
	@Override
	public Result<?> interceptor(RouteWay routeWay, Iterator<Interceptor> iterator, Request request,
			Response response){
		long currentTime = System.currentTimeMillis();
		Result<?> result = routeWay.invocate(routeWay, iterator, request, response);
		System.out.println("time ===:" + (System.currentTimeMillis() - currentTime));
		return result;
	}
}
