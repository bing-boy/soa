package com.spring.parse;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public class ReferenceBeanDefinitionParser implements BeanDefinitionParser {

	private Class<?> beanClass;
	
	public ReferenceBeanDefinitionParser(Class<?> beanClass) {
		this.beanClass = beanClass;
	}
	
	@Override
	public BeanDefinition parse(Element element, ParserContext parserContext) {
		RootBeanDefinition beanDefinition = new RootBeanDefinition();
		beanDefinition.setBeanClass(beanClass);
		beanDefinition.setLazyInit(false);
		String id = element.getAttribute("id");
		String intf = element.getAttribute("interface");
		String protocol = element.getAttribute("protocol");
		String loadbalance = element.getAttribute("loadbalance");
		String cluster = element.getAttribute("cluster");
		String retries = element.getAttribute("retries");
		
		if(id == null || "".equals(id)) {
			throw new RuntimeException("Reference id 不能为空");
		}
		if(intf == null || "".equals(intf)) {
			throw new RuntimeException("Reference interface 不能为空");
		}
		if(protocol == null || "".equals(protocol)) {
			throw new RuntimeException("Reference protocol 不能为空");
		}
		if (loadbalance == null || "".equals(loadbalance)) {
			throw new RuntimeException("Reference loadbalance 不能为空");
		}
		if (cluster == null || "".equals(cluster)) {
			throw new RuntimeException("Reference cluster 不能为空");
		}
		if (retries == null || "".equals(retries)) {
			throw new RuntimeException("Reference retries 不能为空");
		}
		
		MutablePropertyValues propertyValues = beanDefinition.getPropertyValues();
		beanDefinition.getPropertyValues().add("id", id);
		beanDefinition.getPropertyValues().add("intf", intf);
		beanDefinition.getPropertyValues().add("protocol", protocol);
		beanDefinition.getPropertyValues().add("loadbalance", loadbalance);
		beanDefinition.getPropertyValues().add("cluster", cluster);
		beanDefinition.getPropertyValues().add("retries", retries);
		parserContext.getRegistry().registerBeanDefinition("Reference" + id, beanDefinition);
		return beanDefinition;
	}

}
