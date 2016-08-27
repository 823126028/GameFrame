package com.quanmin.netty.http;
import java.nio.channels.ClosedChannelException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.CACHE_CONTROL;
import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.EXPIRES;
import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.SET_COOKIE;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpMessage;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.codec.http.HttpVersion;
import org.jboss.netty.handler.codec.http.cookie.Cookie;
import org.jboss.netty.handler.codec.http.cookie.ServerCookieEncoder;
import org.jboss.netty.handler.stream.ChunkedWriteHandler;

import com.quanmin.ServerConstants;
import com.quanmin.netty.Request;
import com.quanmin.netty.Response;
import com.quanmin.servlet.Servlet;
import com.quanmin.servlet.ServletContext;
import com.quanmin.structure.Tuple;

public class HttpDefaultHandler extends SimpleChannelUpstreamHandler {
	/** log */
	private static final Log log = LogFactory.getLog(HttpDefaultHandler.class);

	private final Servlet servlet;
	
	private final ServletContext sc;

	/** Chunk Response */
	public ChunkedWriteHandler chunkedWriteHandler = new ChunkedWriteHandler();
	
	private static final Pattern pattern = Pattern.compile("quanmin[\\s\\S]*");
	
	public HttpDefaultHandler(Servlet servlet, ServletContext sc) {
		this.sc = sc;
		this.servlet = servlet;
	}
	
	/**
	 * @see org.jboss.netty.channel.SimpleChannelUpstreamHandler#exceptionCaught(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.ExceptionEvent)
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
		if (!(e.getCause() instanceof ClosedChannelException)) {
			log.error("http channel error, channel[id:" + e.getChannel().getId() + 
			          ", interestOps:" + e.getChannel().getInterestOps() + 
			          ", bound:" + e.getChannel().isBound() +  
			          ", connected:" + e.getChannel().isConnected() +  
			          ", open:" + e.getChannel().isOpen() + 
			          ", readable:" + e.getChannel().isReadable() +  
			          ", writable:" + e.getChannel().isWritable() + "]",
			          e.getCause());
		}
	}
	
	private boolean checkUrlRight(Channel channel,HttpRequest httpRequest){
		String uri = httpRequest.getUri();
		Matcher matcher = pattern.matcher(uri);
		if (!matcher.find()) {
			HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_GATEWAY);
			channel.write(response).addListener(ChannelFutureListener.CLOSE);
			return false;
		}
		return true;
	}
	
	
	private String getCommandFromUrl(HttpRequest httpRequest){
		//TODO
		//unfinish=============
		return "";
	}
	
	@Override
	public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e)
			    throws Exception{
	}

	/**
	 * @see org.jboss.netty.channel.SimpleChannelUpstreamHandler#messageReceived(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.MessageEvent)
	 */
	@Override
	public void messageReceived(final ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		final Object msg = e.getMessage();
		try{
			if (msg instanceof HttpRequest) {
				try {
					HttpRequest httpRequest = (HttpRequest)msg;
					//检查url是否符合标准
					if (!checkUrlRight(ctx.getChannel(), httpRequest)) {
						return;
					}
					String command = getCommandFromUrl(httpRequest);
					Tuple<Boolean, byte[]> requestContent = HttpUtil.getRequestContent(httpRequest);
				    Map<String, Cookie> cookies = HttpUtil.getCookies(httpRequest);    
				    Map<String, String> headers = HttpUtil.getHeaders(httpRequest);
				    
				    
					final HttpRespone response = new HttpRespone(e.getChannel());
					final Request request = new com.quanmin.netty.http.HttpRequest(ctx, requestContent.left,
							requestContent.right, command, cookies, headers, response);
					
					servlet.service(request,response);
					doResponse(ctx, response, httpRequest);
				} catch (Exception ex) {
					ex.printStackTrace();
					log.error("handle http request error", ex);
					HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.INTERNAL_SERVER_ERROR);
					e.getChannel().write(response).addListener(ChannelFutureListener.CLOSE);
					return;
				}
			}
		}catch(Exception exe){
			exe.printStackTrace();
		}
	}


	protected static void writeResponse(ChannelHandlerContext ctx,
			Response response,
			HttpResponse httpResponse,
			HttpRequest httpRequest) {
		
		byte[] content = null;
		if (httpRequest.getMethod().equals(HttpMethod.HEAD)) {
			content = new byte[0];
		} else {
			content = response.getContent();
		}
		final boolean keepAlive = isKeepAlive(httpRequest);
		ChannelBuffer buf = ChannelBuffers.wrappedBuffer(content);
		httpResponse.setContent(buf);
		setContentLength(httpResponse, content.length);
		ChannelFuture f = ctx.getChannel().write(httpResponse);

		if (!keepAlive) {
			f.addListener(ChannelFutureListener.CLOSE);
		}
	}

	private static void setContentLength(HttpResponse httpResponse, int contentLength) {
		httpResponse.headers().add(HttpHeaders.Names.CONTENT_LENGTH, String.valueOf(contentLength));
	}
	
	protected static void addHeadAndCookieToResponse(Response response, HttpResponse nettyResponse) {
		Map<String, String> headers = response.getHeaders();
		if (null != headers) {
			for (Map.Entry<String, String> entry : headers.entrySet()) {
				nettyResponse.headers().add(entry.getKey(), entry.getValue());
			}
		}
		Map<String, Cookie> cookies = response.getCookies();
		if (null != cookies) {
			for (Map.Entry<String, Cookie> entry : cookies.entrySet()) {
				nettyResponse.headers().add(SET_COOKIE, ServerCookieEncoder.LAX.encode(entry.getValue()));
			}
		}
		if (null != headers) {
			if (!headers.containsKey(CACHE_CONTROL) && !headers.containsKey(EXPIRES)) {
				nettyResponse.headers().add(CACHE_CONTROL, "no-cache");
			}
		}
		nettyResponse.setStatus(HttpResponseStatus.OK);
    }
	
	
	private void doResponse(ChannelHandlerContext ctx, com.quanmin.netty.http.HttpRespone response, HttpRequest httpRequest) throws Exception {
		HttpResponse nettyResponse = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
		nettyResponse.headers().add(CONTENT_TYPE, ServerConstants.CONTENT_TYPE_JSON);
        final boolean keepAlive = isKeepAlive(httpRequest);        
		if (keepAlive && httpRequest.getProtocolVersion().equals(HttpVersion.HTTP_1_0)) {
			nettyResponse.headers().add(CONNECTION, "Keep-Alive");
		}
        addHeadAndCookieToResponse(response, nettyResponse);

        writeResponse(ctx, response, nettyResponse, httpRequest);
	}
	
	public static boolean isKeepAlive(HttpMessage message) {
        return HttpHeaders.isKeepAlive(message);
    }
	
}
