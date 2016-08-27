package com.quanmin;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.Executors;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import com.quanmin.config.XmlConfig;
import com.quanmin.netty.session.SessionManager;
import com.quanmin.servlet.DispatchServlet;
import com.quanmin.servlet.Servlet;
import com.quanmin.servlet.ServletConfig;
import com.quanmin.servlet.ServletContext;
import com.quanmin.servlet.ServletContextImpl;
import com.quanmin.servlet.ServletContextListener;


public class ServletBootstrap { 
	/** 应用执行器*/
	private Servlet servlet;
	
	/** 应用执行器的设置 */
	private ServletConfig servletConfig;
	
	/** 应用的上下文存放，主要存放Spring的beanContainer */
	private ServletContext servletContext;
	
	/** CONFIG_FILE_NAME */
	private static final String CONFIG_FILE_NAME = "conf.xml";

	public ServletBootstrap() {
	}

	public void startup() throws Exception {
		XmlConfig config = new XmlConfig(CONFIG_FILE_NAME);
		servletConfig = config.getServletConfig();
		servletContext = new ServletContextImpl(servletConfig);
		//初始化监听器,监听器用于去耦合初始化
		initLisenters(servletConfig, servletContext);
		//初始化服务工具
		servlet = new DispatchServlet();
		servlet.init(servletConfig,servletContext);
		buildHttpServer(servletConfig, servletContext);
	    SessionManager.getInstance().setServletConfig(servletConfig);
	    SessionManager.getInstance().startSessionCheckThread();
	    
	    System.out.println("===SERVER==OPEN=========");
	}
	
	private void initLisenters(ServletConfig servletConfig, ServletContext context) throws InstantiationException, IllegalAccessException{
		List<Class<?>> listenerList = servletConfig.getListeners();
		for (Class<?> clazz : listenerList) {
			if (ServletContextListener.class.isAssignableFrom(clazz)) {
				ServletContextListener listener = (ServletContextListener) clazz.newInstance();
				listener.contextInitialized(servletContext,servletConfig);
			}
		}
	}
	
	private void buildHttpServer(ServletConfig servletConfig, ServletContext servletContext){
		NioServerSocketChannelFactory httpChannelFactory = new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool());
		ServerBootstrap httpsb = new ServerBootstrap();
		httpsb.setFactory(httpChannelFactory);
		httpsb.setPipelineFactory(new com.quanmin.netty.http.HttpServletPipelineFactory(servlet, servletContext));
		httpsb.setOption("tcpNoDelay", true);    		
		httpsb.bind(new InetSocketAddress((Integer)servletConfig.getInitParam(ServletConfig.SESSION_HTTP_PORT)));
	}
	
	private void buildTcpServer(){
		//NioServerSocketChannelFactory tcpChannelFactory = new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool());
	}
	
	
	public static void main(String[] args) throws Exception {
		ServletBootstrap sbs = new ServletBootstrap();
		sbs.startup();
	}
}
