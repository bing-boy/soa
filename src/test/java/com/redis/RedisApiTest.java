package com.redis;

public class RedisApiTest {

	public void connecTest() {
		RedisApi ra = new RedisApi();
		//System.out.println(ra.exists("redispass"));
		System.out.println(ra.get("redispass"));
	}
}
