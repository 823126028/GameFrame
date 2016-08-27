package com.quanmin.netty.http;



import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;
import org.jboss.netty.handler.execution.ExecutionHandler;
import org.jboss.netty.handler.execution.OrderedMemoryAwareThreadPoolExecutor;
import org.jboss.netty.handler.stream.ChunkedWriteHandler;

import com.quanmin.servlet.Servlet;
import com.quanmin.servlet.ServletContext;

public class HttpServletPipelineFactory implements ChannelPipelineFactory {
	
	/** ExecutionHandler */
	private final ExecutionHandler executionHandler;
	
	/** servlet */
	private final Servlet servlet;
	
	/** ServletContext */
	private final ServletContext sc;
	
	public HttpServletPipelineFactory(Servlet servlet, ServletContext sc) {
		this.servlet = servlet;
		this.sc = sc;
		this.executionHandler = new ExecutionHandler(new OrderedMemoryAwareThreadPoolExecutor(10, 0, 0));
	}

	/**
	 * @see org.jboss.netty.channel.ChannelPipelineFactory#getPipeline()
	 */
	@Override
	public ChannelPipeline getPipeline() throws Exception {
		HttpDefaultHandler defaultHandler = new HttpDefaultHandler(servlet, sc);
		
		ChannelPipeline pipeline = Channels.pipeline();
		pipeline.addLast("decoder",new HttpRequestDecoder());
		pipeline.addLast("encoder", new HttpResponseEncoder());
		pipeline.addLast("thread", executionHandler);
		pipeline.addLast("chunkedWriter", new ChunkedWriteHandler());
		pipeline.addLast("handler", defaultHandler);
		return pipeline;
	}
}
