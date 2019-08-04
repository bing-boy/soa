package com.spring.parse;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public class ServiceBeanDefinitionParser implements BeanDefinitionParser {

	private Class<?> beanClass;
	
	public ServiceBeanDefinitionParser(Class<?> beanClass) {
		this.beanClass = beanClass;
	}
	
	@Override
	public BeanDefinition parse(Element element, ParserContext parserContext) {
		RootBeanDefinition beanDefinition = new RootBeanDefinition();
		beanDefinition.setBeanClass(beanClass);
		beanDefinition.setLazyInit(false);
		String intf = element.getAttribute("interface");
		String ref = element.getAttribute("ref");
		String protocol = element.getAttribute("protocol");
		
		if(intf == null || "".equals(intf)) {
			throw new RuntimeException("Service interface不能为空");
		}
		if(ref == null || "".equals(ref)) {
			throw new RuntimeException("Service ref 不能为空");
		}
		/*
		 * if(protocol == null || "".equals(protocol)) { throw new
		 * RuntimeException("Service protocol 不能为空"); }
		 */
		
		MutablePropertyValues propertyValues = beanDefinition.getPropertyValues();
		beanDefinition.getPropertyValues().add("intf", intf);
		beanDefinition.getPropertyValues().add("ref", ref);
		beanDefinition.getPropertyValues().add("protocol", protocol);
		parserContext.getRegistry().registerBeanDefinition("Service" + ref + intf, beanDefinition);
		return beanDefinition;
	}

}
