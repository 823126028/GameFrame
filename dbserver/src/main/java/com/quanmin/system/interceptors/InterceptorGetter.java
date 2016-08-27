package com.quanmin.system.interceptors;

import java.util.ArrayList;
import java.util.List;

public class InterceptorGetter {
	private static final ArrayList<Interceptor> arrayListInterceptor = new ArrayList<Interceptor>();
	
	private static final ErrorInterceptor errorInterceptor = new ErrorInterceptor();
	
	private static final TimeLogInterceptor timeLogInterceptor = new TimeLogInterceptor();
	
	static{
		arrayListInterceptor.add(errorInterceptor);
		arrayListInterceptor.add(timeLogInterceptor);
	}
	
	public static List<Interceptor> getInterceptorList(){
		return arrayListInterceptor;
	}
}
