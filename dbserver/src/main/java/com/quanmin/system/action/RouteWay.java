package com.quanmin.system.action;
import java.util.Iterator;
import org.springframework.stereotype.Component;
import com.quanmin.netty.Request;
import com.quanmin.netty.Response;
import com.quanmin.system.interceptors.Interceptor;
import com.quanmin.system.result.ByteResult;
import com.quanmin.system.result.Result;

@Component("routeWay")
public class RouteWay {
	public Result<?> invocate(RouteWay routeWay,Iterator<Interceptor> iterator,Request request,Response response){
		if(iterator != null && iterator.hasNext()){
			return iterator.next().interceptor(routeWay, iterator, request, response);
		}else{
			System.out.println("success");
			throw new RuntimeException("error");
			//return new ByteResult("abc".getBytes());
		}
	}
}