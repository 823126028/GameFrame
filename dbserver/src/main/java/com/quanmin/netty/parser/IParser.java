package com.quanmin.netty.parser;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public interface IParser{
	void parseParam(byte[] content,Map<String,String[]> paramMap) throws UnsupportedEncodingException;
}
