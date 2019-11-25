package com.cluster;

import com.proxy.invoke.Invocation;

public interface Cluster {
	public String invoke(Invocation invocation) throws Exception;
}
