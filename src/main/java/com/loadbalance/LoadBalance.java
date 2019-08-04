package com.loadbalance;

import java.util.List;

public interface LoadBalance {
	NodeInfo doSelect(List<String> registryInfo);
}
