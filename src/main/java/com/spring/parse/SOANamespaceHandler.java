package com.spring.parse;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

import com.configbean.Protocol;
import com.configbean.Reference;
import com.configbean.Registry;
import com.configbean.Service;

public class SOANamespaceHandler extends NamespaceHandlerSupport {

	@Override
	public void init() {
		this.registerBeanDefinitionParser("registry", new RegistryBeanDefinitionParser(Registry.class));
		this.registerBeanDefinitionParser("protocol", new ProtocolBeanDefinitionParser(Protocol.class));
		this.registerBeanDefinitionParser("service", new ServiceBeanDefinitionParser(Service.class));
		this.registerBeanDefinitionParser("reference", new ReferenceBeanDefinitionParser(Reference.class));
	}

}
