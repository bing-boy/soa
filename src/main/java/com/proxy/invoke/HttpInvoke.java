package com.proxy.invoke;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.configbean.Reference;
import com.loadbalance.LoadBalance;
import com.loadbalance.NodeInfo;
import com.rpc.http.HttpRequest;

/**
 * http的调用过程
 */

public class HttpInvoke implements Invoke {

	@Override
	public String invoke(Invocation invocation) {
		List<String> registryInfo = invocation.getReference().getRegistryInfo();
		//负载均衡算法
		String loadBalence = invocation.getReference().getLoadbalance();
		Reference reference = invocation.getReference();
		LoadBalance loadBalanceBean = reference.getLoadBalances().get(loadBalence);
		
		NodeInfo nodeInfo = loadBalanceBean.doSelect(registryInfo);
		
		//调用远程方法，需要传递一些参数给远程的生产者；调用远程的生产者是传输的json字符串
		JSONObject sendParam = new JSONObject();
		sendParam.put("methodName", invocation.getMethod().getName()); //要调用的远程方法
		sendParam.put("methodParams", invocation.getObjects());
		sendParam.put("serviId", reference.getId()); //从远程生产者spring容器中拿到serviceId对应的服务层的实例，通过方法的名称和方法的类型，可以找到方法的method对象，就可以通过反射调到方法了 
		sendParam.put("paramTypes", invocation.getMethod().getParameterTypes());

		String url = "http://" + nodeInfo.getHost() + ":" + nodeInfo.getProt() + nodeInfo.getContextPath();
		
		//调用对端的生产者服务
		String result = HttpRequest.sendPost(url, sendParam.toJSONString());
		return result;
	}

}
