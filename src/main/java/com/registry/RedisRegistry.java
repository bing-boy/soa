package com.registry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

import com.alibaba.fastjson.JSONObject;
import com.configbean.Protocol;
import com.configbean.Registry;
import com.configbean.Service;
import com.redis.RedisApi;

public class RedisRegistry implements BaseRegistry {

	@Override
	public boolean registry(String ref, ApplicationContext applicationContext) {
		try {
			Protocol protocol = applicationContext.getBean(Protocol.class);
			Map<String, Service> services = applicationContext.getBeansOfType(Service.class);
			Registry registry = applicationContext.getBean(Registry.class);

			RedisApi.createJedisPool(registry.getAddress());

			for (Map.Entry<String, Service> entry : services.entrySet()) {
				if (entry.getValue().getRef().equals(ref)) {
					JSONObject jo = new JSONObject();
					jo.put("protocol", JSONObject.toJSONString(protocol));
					jo.put("service", JSONObject.toJSONString(entry.getValue()));

					JSONObject jsonObject = new JSONObject();
					jsonObject.put(protocol.getHost() + ":" + protocol.getPort(), jo);

					lpush(jsonObject, ref);
				}
			}
			return true;
		} catch (BeansException e) {
			e.printStackTrace();
		}
		return false;
	}

	//去重，避免同个服务注册多次，比如多次启动同个service
	private void lpush(JSONObject jsonObject, String key) {
		if (RedisApi.exists(key)) {
			Set<String> keys = jsonObject.keySet();
			String keyStr = "";
			for (String kk : keys) {
				keyStr = kk;
			}

			//拿redis对应key里面的内容
			List<String> registryInfo = RedisApi.lrange(key);
			List<String> newRegistry = new ArrayList<>();

			boolean isold = false;

			for (String node : registryInfo) {
				JSONObject jo = jsonObject.parseObject(node);
				if (jo.containsKey(keyStr)) {
					newRegistry.add(jsonObject.toJSONString());
					isold = true;
				} else {
					newRegistry.add(node);
				}
			}

			if (isold) {
				//老机器去重
				if (newRegistry.size() > 0) {
					RedisApi.del(key);
					String[] newReStr = new String[newRegistry.size()];
					for (int i = 0; i < newRegistry.size(); i++) {
						newReStr[i] = newRegistry.get(i);
					}
					RedisApi.lpush(key, newReStr);
				}
			} else {
				//加入新启动的机器
				RedisApi.lpush(key, jsonObject.toJSONString());
			}
		} else {
			//所有的都是第一次启动
			RedisApi.lpush(key, jsonObject.toJSONString());
		}
	}

	@Override
	public List<String> getRegistry(String id, ApplicationContext applicationContext) {
		try {
			Registry registry = applicationContext.getBean(Registry.class);
			RedisApi.createJedisPool(registry.getAddress());
			if (RedisApi.exists(id)) {
				//拿key对应的list
				return RedisApi.lrange(id);
			}
		} catch (BeansException e) {
			e.printStackTrace();
		}
		return null;
	}

}
