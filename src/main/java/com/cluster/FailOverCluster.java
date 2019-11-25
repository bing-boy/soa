package com.cluster;

import com.proxy.invoke.Invocation;
import com.proxy.invoke.Invoke;

/** 如果调用节点失败，就自动切换到其他集群节点
 * @author bing
 *
 */
public class FailOverCluster implements Cluster {

	@Override
	public String invoke(Invocation invocation) throws Exception {
		String retries = invocation.getReference().getRetries();
		Integer retrint = Integer.parseInt(retries);

		Invoke invoke = invocation.getInvoke();

		for (int i = 0; i < retrint; i++) {
			try {
				String result = invoke.invoke(invocation);
				return result;
			} catch (Exception e) {
				System.out.println("失败第" + i + "次");
				e.printStackTrace();
				continue;
			}

		}

		//重试配置的次数均失败
		throw new RuntimeException("尝试" + retries + "次全部失败");
	}

}
