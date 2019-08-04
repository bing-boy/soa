package com.registry;

import java.util.List;

import org.springframework.context.ApplicationContext;

public interface BaseRegistry {
	public boolean registry(String param, ApplicationContext applicationContext);

	public List<String> getRegistry(String id, ApplicationContext applicationContext);
}
