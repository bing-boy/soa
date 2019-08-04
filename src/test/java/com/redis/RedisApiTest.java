package com.redis;

import org.junit.Test;

public class RedisApiTest {

	@Test
	public void connecTest() {
		RedisApi ra = new RedisApi();
		//System.out.println(ra.exists("redispass"));
		System.out.println(ra.get("redispass"));
	}
}
