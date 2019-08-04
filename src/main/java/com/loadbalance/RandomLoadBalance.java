package com.loadbalance;

import java.util.Collection;
import java.util.List;
import java.util.Random;

import com.alibaba.fastjson.JSONObject;

public class RandomLoadBalance implements LoadBalance {

	@Override
	public NodeInfo doSelect(List<String> registryInfo) {
		Random random = new Random();
		int index = random.nextInt(registryInfo.size());
		String registry = registryInfo.get(index);

		JSONObject registryJo = JSONObject.parseObject(registry);
		Collection<Object> values = registryJo.values();
		JSONObject node = new JSONObject();
		for (Object value : values) {
			node = JSONObject.parseObject(value.toString());
		}

		JSONObject protocol = node.getJSONObject("protocol");
		NodeInfo nodeInfo = new NodeInfo();
		nodeInfo.setHost(protocol.get("host") != null ? protocol.getString("host") : "");
		nodeInfo.setProt(protocol.get("port") != null ? protocol.getString("port") : "");
		nodeInfo.setContextPath(protocol.get("context") != null ? protocol.getString("context") : "");
		return nodeInfo;
	}

}
