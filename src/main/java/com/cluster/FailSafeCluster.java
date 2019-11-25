package com.cluster;

import com.proxy.invoke.Invocation;
import com.proxy.invoke.Invoke;

/**	调用节点失败，直接忽略
 * @author bing
 *
 */
public class FailSafeCluster implements Cluster {

	@Override
	public String invoke(Invocation invocation) throws Exception {
		Invoke invoke = invocation.getInvoke();

		try {
			return invoke.invoke(invocation);
		} catch (Exception e) {
			System.out.println("调用失败，忽略");
			e.printStackTrace(); //打印异常，吃掉错误直接返回
			return "调用失败，忽略";
		}
	}

}
