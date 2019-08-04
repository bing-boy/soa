package com.loadbalance;

public class NodeInfo {

	private String host;

	private String prot;

	private String contextPath; //http上下文

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getProt() {
		return prot;
	}

	public void setProt(String prot) {
		this.prot = prot;
	}

	public String getContextPath() {
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}
}
