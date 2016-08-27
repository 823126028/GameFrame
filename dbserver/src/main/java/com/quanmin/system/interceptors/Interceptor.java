package com.quanmin.system.interceptors;

import java.util.Iterator;

import com.quanmin.netty.Request;
import com.quanmin.netty.Response;
import com.quanmin.system.action.RouteWay;
import com.quanmin.system.result.Result;

public interface Interceptor {
	public Result<?> interceptor(RouteWay routeWay, Iterator<Interceptor> interceptors, Request request, Response response);
}
