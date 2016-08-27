package com.quanmin.netty.parser;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.quanmin.netty.http.HttpUtil;

public class AndParser implements IParser{

	public static String[] getValue(String[] values, String value) {
        if (null == values || values.length == 0) {
            return new String[] { value };
        }
        String[] result = new String[values.length + 1];
        System.arraycopy(values, 0, result, 0, values.length);
        result[values.length] = value;
        return result;
    }
	
	@Override
	public void parseParam(byte[] bytes, Map<String, String[]> paramMap) throws UnsupportedEncodingException {
		String content = new String(bytes);
		if (StringUtils.isBlank(content)) {
			return;
		}
		String str = content.trim();
		String[] strs = str.split("&");
		for (String value : strs) {
			String[] values = value.split("=");
			String k = HttpUtil.decode(values[0], "utf-8");
			if (values.length == 1) {
				paramMap.put(k, null);
			} else {
				String v = HttpUtil.decode(values[1], "utf-8");
				for (int i = 2; i < values.length; i++) {
					v = v + "=" + HttpUtil.decode(values[i], "utf-8");
				}
				if (paramMap.containsKey(k)) {
					paramMap.put(k, getValue(paramMap.get(k), v));
				} else {
					paramMap.put(k, new String[] { v });
				}
			}
		}
	}

}
