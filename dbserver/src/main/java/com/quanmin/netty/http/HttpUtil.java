package com.quanmin.netty.http;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.cookie.Cookie;
import org.jboss.netty.handler.codec.http.cookie.DefaultCookie;
import org.jboss.netty.handler.codec.http.cookie.ServerCookieDecoder;

import com.quanmin.structure.Tuple;

public final class HttpUtil {
	private HttpUtil() {};

	public static Map<String, Cookie> getCookies(HttpRequest httpRequest) {
		Map<String, Cookie> cookies = new HashMap<String, Cookie>(16);
		String value = httpRequest.headers().get(HttpHeaders.Names.COOKIE);
		if (value != null) {
			Set<Cookie> cookieSet = ServerCookieDecoder.LAX.decode(value);
			if (cookieSet != null) {
				for (Cookie cookie : cookieSet) {
					Cookie temp = new DefaultCookie(cookie.name(), cookie.value());
					temp.setPath(cookie.path());
					temp.setDomain(cookie.domain());
					temp.setSecure(cookie.isSecure());
					temp.setHttpOnly(cookie.isHttpOnly());
					cookies.put(temp.name(), temp);
				}
			}
		}
		return cookies;
	}
	
	public static String decode(String str, String encode) throws UnsupportedEncodingException {
    	try {
			return URLDecoder.decode(str, encode);
		} catch (UnsupportedEncodingException e) {
			throw e;
		} catch (Throwable t) {
			return str;
		}
    }
	

	public static Map<String, String> getHeaders(HttpRequest httpRequest) {
		Map<String, String> headers = new HashMap<String, String>(16);

		for (String key : httpRequest.headers().names()) {
			headers.put(key, httpRequest.headers().get(key));
		}

		return headers;
	}
	

	public static Tuple<Boolean, byte[]> getRequestContent(HttpRequest httpRequest) {
		Tuple<Boolean, byte[]> tuple = new Tuple<Boolean, byte[]>();
		if(httpRequest.getMethod() == HttpMethod.POST){
			tuple.left = true;
			ChannelBuffer channelBuffer = httpRequest.getContent();
			byte[] body = new byte[channelBuffer.readableBytes()];
			httpRequest.getContent().getBytes(channelBuffer.readerIndex(), body, 0, body.length);
			tuple.right = body;
			return tuple;
		}else{
			tuple.left = false;
			String uri = httpRequest.getUri();
			String params = uri.substring(uri.indexOf("?") + 1);
			tuple.right = params.getBytes();
			return tuple;
		}
	}
	
}
