package com.rmi;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.configbean.Service;

/** rmi的实现类，负责rmi的调用，这个是生产者端使用的类
 * @author bing
 *
 */
public class SoaRmiImpl extends UnicastRemoteObject implements SoaRmi {

	protected SoaRmiImpl() throws RemoteException {
		super();
	}

	private static final long serialVer = 14327972938080L;

	@Override
	public String invoke(String param) throws RemoteException {
		JSONObject requestParam = JSONObject.parseObject(param);
		//要从远程的生产者的spring容器中拿到对应的serviceid实例
		String serviceId = requestParam.getString("serviceId");
		String methodName = requestParam.getString("methodName");
		JSONArray paramTypes = requestParam.getJSONArray("paramTypes");
		//这个是对应的方法参数
		JSONArray methodParamJa = requestParam.getJSONArray("methodParams");
		//这个是反射的参数
		Object[] objs = null;
		if (methodParamJa != null) {
			objs = new Object[methodParamJa.size()];
			int i = 0;
			for (Object o : methodParamJa) {
				objs[i++] = o;
			}
		}

		//拿到spring的上下文
		ApplicationContext applicationContext = Service.getApplicationContext();
		//服务层的实例
		Object serviceBean = applicationContext.getBean(serviceId);
		//这个方法的获取，要考虑到方法的重载
		Method method = getMethod(serviceBean, methodName, paramTypes);

		if (method != null) {
			Object result;
			try {
				result = method.invoke(serviceBean, objs);
				return result.toString();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		} else {
			return "-------no such method-----";
		}

		return null;
	}

	private Method getMethod(Object bean, String methodName, JSONArray paramTypes) {
		Method[] methods = bean.getClass().getMethods();
		List<Method> retMethod = new ArrayList<>();
		for (Method method : methods) {
			if (methodName.trim().equals(method.getName())) {
				retMethod.add(method);
			}
		}
		//如果大小是1，说明只有方法只有一个
		if (retMethod.size() == 1) {
			return retMethod.get(0);
		}

		boolean isSameSize = false;
		boolean isSameType = false;
		bing: for (Method method : retMethod) {
			Class<?>[] types = method.getParameterTypes();
			if (types.length == paramTypes.size()) {
				isSameSize = true;
			}
			if (!isSameSize) {
				continue;
			}
			for (int i = 0; i < types.length; i++) {
				if (types[i].toString().contains(paramTypes.getString(i))) {
					isSameType = true;
				} else {
					isSameType = false;
				}
				if (!isSameType) {
					continue bing;
				}
			}
			if (isSameType) {
				return method;
			}
		}

		return null;
	}
}
