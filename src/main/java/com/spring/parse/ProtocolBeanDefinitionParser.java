package com.spring.parse;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public class ProtocolBeanDefinitionParser implements BeanDefinitionParser {

	private Class<?> beanClass;
	
	public ProtocolBeanDefinitionParser(Class<?> beanClass) {
		this.beanClass = beanClass;
	}
	
	@Override
	public BeanDefinition parse(Element element, ParserContext parserContext) {
		RootBeanDefinition beanDefinition = new RootBeanDefinition();
		beanDefinition.setBeanClass(beanClass);
		beanDefinition.setLazyInit(false);
		String name = element.getAttribute("name");
		String host = element.getAttribute("host");
		String port = element.getAttribute("port");
		String contextpath = element.getAttribute("contextpath");
		
		if(name == null || "".equals(name)) {
			throw new RuntimeException("Protocol name 不能为空");
		}
		if(host == null || "".equals(host)) {
			throw new RuntimeException("Protocol host 不能为空");
		}
		if(port == null || "".equals(port)) {
			throw new RuntimeException("Protocol port 不能为空");
		}
		
		beanDefinition.getPropertyValues().add("name", name);
		beanDefinition.getPropertyValues().add("host", host);
		beanDefinition.getPropertyValues().add("port", port);
		beanDefinition.getPropertyValues().add("contextpath", contextpath);
		parserContext.getRegistry().registerBeanDefinition("Protocol" + name, beanDefinition);
		return beanDefinition;
	}

}
