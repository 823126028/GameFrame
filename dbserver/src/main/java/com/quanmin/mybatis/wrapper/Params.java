package com.quanmin.mybatis.wrapper;


import java.util.HashMap;
import java.util.Map;

public class Params extends HashMap<String, Object> {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 9109040637831222502L;

	/** EMPTY_PARAM */
	public static final Map<String, Object> EMPTY_PARAM = new HashMap<String, Object>(0);

	public Params() {
		super();
	}

	public Params(int initialCapacity) {
		super(initialCapacity);
	}
	
	public Params addParam(String key, Object value) {
		super.put(key, value);
		return this;
	}
}
