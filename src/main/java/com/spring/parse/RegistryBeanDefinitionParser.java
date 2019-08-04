package com.spring.parse;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public class RegistryBeanDefinitionParser implements BeanDefinitionParser {

	private Class<?> beanClass;
	
	public RegistryBeanDefinitionParser(Class<?> beanClass) {
		this.beanClass = beanClass;
	}
	
	@Override
	public BeanDefinition parse(Element element, ParserContext parserContext) {
		RootBeanDefinition beanDefinition = new RootBeanDefinition();
		beanDefinition.setBeanClass(beanClass);
		beanDefinition.setLazyInit(false);
		String protocol = element.getAttribute("protocol");
		String address = element.getAttribute("address");
		
		if(protocol == null || "".equals(protocol)) {
			throw new RuntimeException("Registry protocol 不能为空");
		}
		if(address == null || "".equals(address)) {
			throw new RuntimeException("Registry address 不能为空");
		}
		
		MutablePropertyValues propertyValues = beanDefinition.getPropertyValues();
		beanDefinition.getPropertyValues().add("protocol", protocol);
		beanDefinition.getPropertyValues().add("address", address);
		parserContext.getRegistry().registerBeanDefinition("Register" + address, beanDefinition);
		return beanDefinition;
	}

}
