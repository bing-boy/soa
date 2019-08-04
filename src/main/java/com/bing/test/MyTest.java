package com.bing.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MyTest {

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("mytest.xml");

		TestService testService = context.getBean(TestService.class);
		testService.eat("");
	}

}
