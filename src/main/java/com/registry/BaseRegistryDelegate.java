package com.registry;

import java.util.List;

import org.springframework.context.ApplicationContext;

import com.configbean.Registry;

public class BaseRegistryDelegate {
	public static void registry(String ref, ApplicationContext applicationContext) {
		Registry registry = applicationContext.getBean(Registry.class);
		String protocol = registry.getProtocol();
		BaseRegistry registryBean = registry.getRegistryMap().get(protocol);
		registryBean.registry(ref, applicationContext);
	}

	public static List<String> getRegistry(String id, ApplicationContext applicationContext) {
		Registry registry = applicationContext.getBean(Registry.class);
		String protocol = registry.getProtocol();
		BaseRegistry registryBean = registry.getRegistryMap().get(protocol);
		return registryBean.getRegistry(id, applicationContext);
	}
}
