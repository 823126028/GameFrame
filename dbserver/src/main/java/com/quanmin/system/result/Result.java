package com.quanmin.system.result;

public interface Result<T> {
	String getViewName();

	T getResult();
}
