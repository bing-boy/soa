package com.proxy.advice;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import com.configbean.Reference;
import com.proxy.invoke.Invocation;
import com.proxy.invoke.Invoke;

/**
 * @author bing
 * 这是一个advice,在这个advice里面就进行了rpc的远程调用 
 * rpc:http,rmi,netty
 */
public class InvokeInvocationHandler implements InvocationHandler {

	private Invoke invoke;

	private Reference reference;

	public InvokeInvocationHandler(Invoke invoke, Reference reference) {
		this.invoke = invoke;
		this.reference = reference;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		//在这个invoke里面最终要调用多个远程provider
		System.out.println("已经获取到了代理实例，已经调到了InvokeInvocationHandler.invoke");
		Invocation invocation = new Invocation();
		invocation.setMethod(method);
		invocation.setObjects(args);
		invocation.setReference(reference);
		String result = invoke.invoke(invocation);
		return result;
	}

}
