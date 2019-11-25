package com.cluster;

import com.proxy.invoke.Invocation;
import com.proxy.invoke.Invoke;

/** 调用节点异常，直接失败
 * @author bing
 *
 */
public class FailFastCluster implements Cluster {

	@Override
	public String invoke(Invocation invocation) throws Exception {
		Invoke invoke = invocation.getInvoke();

		try {
			return invoke.invoke(invocation);
		} catch (Exception e) {
			throw e;
		}
	}

}
