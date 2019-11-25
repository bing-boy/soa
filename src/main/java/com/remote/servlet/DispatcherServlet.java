package com.remote.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.springframework.context.ApplicationContext;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.configbean.Service;

public class DispatcherServlet extends HttpServlet {

	/**
	 * soa框架中给生产者接收请求用的servlet,必须采用http协议才能调用得到
	 */
	private static final long serialVersionUID = 2032504385032L;
	private static final Logger logger = org.slf4j.LoggerFactory.getLogger(DispatcherServlet.class);
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			JSONObject requestParam = httpProcess(req, resp);
			//要从远程的生产者的spring容器中拿到对应的serviceid实例
			String serviceId = requestParam.getString("serviceId");
			String methodName = requestParam.getString("methodName");
			JSONArray paramTypes = requestParam.getJSONArray("paramTypes");
			//这个是对应的方法参数
			JSONArray methodParamJa = requestParam.getJSONArray("methodParams");
			//这个是反射的参数
			Object[] objs = null;
			if(methodParamJa != null) {
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
			PrintWriter pw = resp.getWriter();
			if(method != null) {
				Object result = method.invoke(serviceBean, objs);
				pw.write(result.toString());
			} else {
				pw.write("-------no such method-----");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
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
			if(!isSameSize) {
				continue;
			}
			for (int i = 0; i < types.length; i++) {
				if (types[i].toString().contains(paramTypes.getString(i))) {
					isSameType = true;
				} else {
					isSameType = false;
				}
				if(!isSameType) {
					continue bing;
				}
			}
			if (isSameType) {
				return method;
			}
		}
		
		return null;
	}

	public static JSONObject httpProcess(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		StringBuffer sb = new StringBuffer();
		InputStream is = req.getInputStream();

		BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
		String s = "";
		while ((s = br.readLine()) != null) {
			sb.append(s);
		}
		if (sb.toString().length() <= 0) {
			return null;
		} else {
			logger.debug("解析http：" + sb.toString());
			return JSONObject.parseObject(sb.toString());
		}
	}
}
