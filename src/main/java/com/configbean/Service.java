package com.configbean;

import java.io.Serializable;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.registry.BaseRegistryDelegate;

public class Service extends BaseConfigBean implements Serializable, InitializingBean, ApplicationContextAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = 98508205840L;
	private String intf;
	private String ref;
	private String protocol;

	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		//启动生产者时将服务注册到注册中心
		//这里使用了委托者模式解耦，在该Service中没有与注册中心的耦合，如果将注册中心的代码写在这里
		//那么换了注册中心（redis,zookeeper）时，这里也要跟着修改，
		//而此处使用委托者模式，如果换了注册中心，只需要修改委托者BaseRegistryDelegate而不需要修改Service类
		BaseRegistryDelegate.registry(ref, applicationContext);
	}

	public String getIntf() {
		return intf;
	}

	public void setIntf(String intf) {
		this.intf = intf;
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
}
