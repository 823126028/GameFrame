package com.quanmin.spring.wrapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.UnsatisfiedDependencyException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

public class SpringObjectFactory extends ObjectFactory implements ApplicationContextAware {
	/** log */
	private static final Log log = LogFactory.getLog(SpringObjectFactory.class);
	
	protected ApplicationContext context;
	protected AutowireCapableBeanFactory autowireCapableBeanFactory;
	protected int autowireStrategy = AutowireCapableBeanFactory.AUTOWIRE_BY_NAME;
	protected boolean alwaysRespectAutowireStrategy = false;
	
	
	/**
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(ApplicationContext context )
			throws BeansException {
		this.context = context;
		this.autowireCapableBeanFactory = findAutoWiringBeanFactory(this.context);
	}
	
	
	/**
	 * @see com.reign.framework.netty.mvc.will.framework.socket.mvc.ObjectFactory#buildBean(java.lang.Class)
	 */
	@Override
	public Object buildBean(Class<?> clazz) throws InstantiationException,
			IllegalAccessException {
		Object o = null;
		try {
			o = context.getBean(clazz.getName());
		} catch (NoSuchBeanDefinitionException e) {
			o = _buildBean(clazz);
		}
		return o;
	}

	private Object _buildBean(Class<?> clazz) throws InstantiationException, IllegalAccessException {
		Object bean;
		try {
			if (alwaysRespectAutowireStrategy) {
				bean = autowireCapableBeanFactory.createBean(clazz, autowireStrategy, false);
				return bean;
			} else {
				bean = autowireCapableBeanFactory.autowire(clazz, AutowireCapableBeanFactory.AUTOWIRE_CONSTRUCTOR, false);
				bean = autowireCapableBeanFactory.applyBeanPostProcessorsBeforeInitialization(bean, bean.getClass().getName());
				bean = autowireCapableBeanFactory.applyBeanPostProcessorsAfterInitialization(bean, bean.getClass().getName());
				
				return autoWireBean(bean, autowireStrategy);
			}
		} catch (UnsatisfiedDependencyException e) {
			log.error("error build bean", e);
			
			return autoWireBean(clazz.newInstance(), autowireStrategy);
		}
	}

	private Object autoWireBean(Object bean, int autowireStrategy) {
		if (autowireCapableBeanFactory != null) {
			autowireCapableBeanFactory.autowireBeanProperties(bean, autowireStrategy, false);
		}
		injectApplicationContext(bean);
		return bean;
	}

	private void injectApplicationContext(Object bean) {
		if (bean instanceof ApplicationContextAware) {
			((ApplicationContextAware)bean).setApplicationContext(context);
		}
	} 

	public void setAutowireStrategy(int autowireStrategy) {
		switch (autowireStrategy) {
		case AutowireCapableBeanFactory.AUTOWIRE_BY_NAME:
			log.info("Setting autowire strategy to name");
			this.autowireStrategy = autowireStrategy;
			break;
		case AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE:
			log.info("Setting autowire strategy to type");
			this.autowireStrategy = autowireStrategy;
			break;
		case AutowireCapableBeanFactory.AUTOWIRE_CONSTRUCTOR:
			log.info("Setting autowire strategy to constructor");
			this.autowireStrategy = autowireStrategy;
			break;
		case AutowireCapableBeanFactory.AUTOWIRE_NO:
			log.info("Setting autowire strategy to none");
			this.autowireStrategy = autowireStrategy;
			break;
		default:
			throw new IllegalStateException("invalid autowire type set, [type:" + autowireStrategy + "]");
		}
	}
	
	/**
	 * @return the autowireStrategy
	 */
	public int getAutowireStrategy() {
		return autowireStrategy;
	}

	private AutowireCapableBeanFactory findAutoWiringBeanFactory(
			ApplicationContext context) {
		if (context instanceof AutowireCapableBeanFactory) {
			return (AutowireCapableBeanFactory) context;
		} else if (context instanceof ConfigurableApplicationContext) {
			return ((ConfigurableApplicationContext)context).getBeanFactory();
		} else if (context.getParent() != null) {
			return findAutoWiringBeanFactory(context.getParent());
		}
		return null;
	}

}
