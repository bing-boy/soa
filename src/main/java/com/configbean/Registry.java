package com.configbean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import com.registry.BaseRegistry;
import com.registry.RedisRegistry;

public class Registry extends BaseConfigBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 452093858023L;
	private String protocol;
	private String address;

	private static ApplicationContext applicationContext;
	
	private static Map<String, BaseRegistry> registryMap = new HashMap<>();
	
	static {
		registryMap.put("redis", new RedisRegistry());
	}

	public static void setApplicationContext(ApplicationContext applicationContext) {
		Registry.applicationContext = applicationContext;
	}

	public static Map<String, BaseRegistry> getRegistryMap() {
		return registryMap;
	}

	public static void setRegistryMap(Map<String, BaseRegistry> registryMap) {
		Registry.registryMap = registryMap;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
