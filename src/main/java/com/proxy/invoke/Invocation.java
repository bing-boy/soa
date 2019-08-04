package com.proxy.invoke;

import java.lang.reflect.Method;

import com.configbean.Reference;

public class Invocation {

	private Method method;

	private Object[] objects;

	private Reference reference; //消费者要从多个reference中选择一个provider进行调用，这些是封装在reference中的，所以这里塞一个reference,在invokeInvocationHandler的invoke中，就可以设置调用setReference了

	public Invocation() {
		// TODO Auto-generated constructor stub
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public Object[] getObjects() {
		return objects;
	}

	public void setObjects(Object[] objects) {
		this.objects = objects;
	}

	public Reference getReference() {
		return reference;
	}

	public void setReference(Reference reference) {
		this.reference = reference;
	}
}
