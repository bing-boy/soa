package com.redis;

import org.junit.Test;

import redis.clients.jedis.Jedis;

public class JedisTest {
	public static String Host = "192.168.1.15";
	public static int Port = 6379;
	public static String Password = "redis";
	public static Jedis jedis = new Jedis(Host, Port);

	@Test
	public void connectTest() {
		jedis.auth(Password);
		//jedis.connect();
		String redispass = jedis.get("redispass");
		System.out.println(redispass);
	}

	@Test
	public void lpushTest() {
		jedis.auth(Password);
		System.out.println(jedis.lpush("bing", "name:bing", "age:22"));
	}

}
