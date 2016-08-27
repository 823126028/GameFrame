package com.quanmin.netty.parser;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Map.Entry;

import com.alibaba.fastjson.JSON;

public class OneLevelJsonParser implements IParser{
	@Override
	public void parseParam(byte[] content, Map<String, String[]> paramMap) throws UnsupportedEncodingException {
		Map<String, Object> map = JSON.parseObject(new String(content));
		for (Entry<String,Object> entry: map.entrySet()) {
			paramMap.put(entry.getKey(), new String[]{entry.getValue().toString()});
		}
	}
}
