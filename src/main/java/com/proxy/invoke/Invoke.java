package com.proxy.invoke;


/**
 * @author bing 
 * 为了方便，不封装成对象了，返回String，用json的方式进行通信
 */
public interface Invoke {
	public String invoke(Invocation invocation);
}
