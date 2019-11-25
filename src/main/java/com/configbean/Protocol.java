package com.configbean;

import java.io.Serializable;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.netty.NettyUtil;
import com.rmi.RmiUtil;

public class Protocol extends BaseConfigBean
		implements Serializable, InitializingBean, ApplicationContextAware, ApplicationListener<ContextRefreshedEvent> {
	//ContextRefreshedEvent 是Spring启动成功以后才会触发的事件

	/**
	 * 
	 */
	private static final long serialVersionUID = 1324543543254L;
	private String name;
	private String host;
	private String port;
	private String contextpath;

	private static ApplicationContext application;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getContextpath() {
		return contextpath;
	}

	public void setContextpath(String contextpath) {
		this.contextpath = contextpath;
	}


	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.application = application;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if ("rmi".equalsIgnoreCase(name)) {
			RmiUtil rmi = new RmiUtil();
			rmi.startRmiServer(host, port, "bingrmi");
		}
		/*if ("netty".equalsIgnoreCase(name)) {
			try {
				NettyUtil.startServer(port);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}*/
	}


	//
	/* ContextRefreshedEvent 是Spring启动完成时触发的事件
	 * @see 没有这个会导致应用启动报错，请求消费端无法请求，怀疑spring没有启动成功,故将上面的netty启动放到spring启动之后
	 */
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (!ContextRefreshedEvent.class.getName().equals(event.getClass().getName())) {
			return;
		}
		//启动不成功是由于阻塞在了NettyUtil.class的这行代码这里  f.channel().closeFuture().sync();
		/*if ("netty".equalsIgnoreCase(name)) {
			try {
				NettyUtil.startServer(port);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}*/
		if(!"netty".equalsIgnoreCase(name)) {
			return;
		}
		
		//丢到另一个线程启动netty（为保证tomcat和netty顺利启动）
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					NettyUtil.startServer(port);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		}).start();
		;
	}


}
