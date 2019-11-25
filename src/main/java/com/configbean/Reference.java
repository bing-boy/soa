package com.configbean;

import java.io.Serializable;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.cluster.Cluster;
import com.cluster.FailFastCluster;
import com.cluster.FailOverCluster;
import com.cluster.FailSafeCluster;
import com.loadbalance.LoadBalance;
import com.loadbalance.RandomLoadBalance;
import com.loadbalance.RoundRobinLoadBalance;
import com.proxy.advice.InvokeInvocationHandler;
import com.proxy.invoke.HttpInvoke;
import com.proxy.invoke.Invoke;
import com.proxy.invoke.NettyInvoke;
import com.proxy.invoke.RmiInvoke;
import com.registry.BaseRegistryDelegate;

/**
 * @author bing
 * Reference中实现FactoryBean,是因为有intf接口的定义
 * 在任何类中都可以实现 FactoryBean都可以调得到getObject,都可以返回一个接口类型的对象，
 * 但是在Reference类中实现该接口是因为该类中有intf接口的字符串，有了字符串就可以反射出该接口的类型（getObjectType）
 * 于是就可以在getObject中对该接口进行代理，然后返回该接口的代理对象
 * 
 * 生产者服务启动时需要往注册中心注册信息，实现InitializingBean，在bean实例化完成的时候调用afterPropertiesSet
 * 
 * 要拿到Spring上下文，看是否有配置Protocol，通过实现ApplicationContextAware接口
 */
public class Reference extends BaseConfigBean
		implements Serializable, FactoryBean, InitializingBean, ApplicationContextAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3274923789L;
	private String intf;
	private String protocol;
	private String loadbalance;
	private String cluster;
	private String retries;

	private Invoke invoke; // 表明该Reference的调用者是哪个

	private static Map<String, Invoke> invokes = new HashMap<>();

	//生产者多个服务列表
	private List<String> registryInfo = new ArrayList<>();

	//负载均衡
	private static Map<String, LoadBalance> loadBalances = new HashMap<>();

	//集群容错
	private static Map<String, Cluster> clusters = new HashMap<>();

	static {
		invokes.put("http", new HttpInvoke());
		invokes.put("rmi", new RmiInvoke());
		invokes.put("netty", new NettyInvoke());

		loadBalances.put("random", new RandomLoadBalance());
		loadBalances.put("roundrob", new RoundRobinLoadBalance());

		clusters.put("failover", new FailOverCluster());
		clusters.put("failfast", new FailFastCluster());
		clusters.put("failsafe", new FailSafeCluster());
	}

	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	/*
	 * getObject方法会拿到一个实例，这个方法是Spring调用的，spring初始化的时候调用的，具体是getBean方法调用的
	 * 
	 * ApplicationContext.getBean("id");这个过程就会调用getObject,只要涉及到getBean的调用，
	 * 就会调到getObject，不管是哪个类实现了FactoryBean， 调用getBean,就会调用到getObject
	 * 
	 * 这里在getObject方法里面，返回的就是intf这个接口的代理
	 */
	@Override
	public Object getObject() throws Exception {
		// 返回intf的代理对象
		if (protocol != null && !"".equals(protocol)) {
			invoke = invokes.get(protocol);
		} else {
			// 如果Reference配置中没有配置protocol属性，就看有没有配置protocol
			// Protocol这个实例实在Spring容器中，应该从上下文中拿实例，所以要拿到Spring上下文，通过实现ApplicationContextAware接口
			Protocol protocol = applicationContext.getBean(Protocol.class); // Protocol 没有id,这里根据类型拿而不是根据id拿
			if(protocol != null) {
				invoke = invokes.get(protocol.getName());
			} else {
				// 如果两个地方都没配置，给一个默认的协议http
				invoke = invokes.get("http");
			}
		}
		// 返回代理
		return Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class<?>[] { Class.forName(intf) },
				new InvokeInvocationHandler(invoke, this));
	}

	@Override
	public Class getObjectType() {
		try {
			if (intf != null && !"".equals(intf)) {
				return Class.forName(intf);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean isSingleton() {
		return true; // 这里表示返回的这个代理是个单例
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		registryInfo = BaseRegistryDelegate.getRegistry(id, applicationContext);
		System.out.println("registryInfo: " + registryInfo);
	}

	public String getIntf() {
		return intf;
	}

	public void setIntf(String intf) {
		this.intf = intf;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getLoadbalance() {
		return loadbalance;
	}

	public void setLoadbalance(String loadbalance) {
		this.loadbalance = loadbalance;
	}

	public List<String> getRegistryInfo() {
		return registryInfo;
	}

	public void setRegistryInfo(List<String> registryInfo) {
		this.registryInfo = registryInfo;
	}

	public static Map<String, LoadBalance> getLoadBalances() {
		return loadBalances;
	}

	public static void setLoadBalances(Map<String, LoadBalance> loadBalances) {
		Reference.loadBalances = loadBalances;
	}

	public String getCluster() {
		return cluster;
	}

	public void setCluster(String cluster) {
		this.cluster = cluster;
	}

	public String getRetries() {
		return retries;
	}

	public void setRetries(String retries) {
		this.retries = retries;
	}

	public static Map<String, Cluster> getClusters() {
		return clusters;
	}

	public static void setClusters(Map<String, Cluster> clusters) {
		Reference.clusters = clusters;
	}

}
