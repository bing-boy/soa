package com.redis;

import java.util.Properties;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class JedisPoolTest {
	public static String Host = "192.168.1.15";
	public static int Port = 6379;
	public static String Password = "redis";
	//public static JedisPool jedisPool = new JedisPool(Host, Port);
	public static Properties props = new Properties();
	public static GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
	public static JedisPool jedisPool = new JedisPool(poolConfig, Host, Port, 10000, Password);

	//new JedisPool(poolConfig, Host, Port, 0, Password);
	@Test
	public void connectTest() {
		Jedis jedis = jedisPool.getResource();
		//jedis.auth(Password);
		//jedis.connect();
		String redispass = jedis.get("redispass");
		System.out.println(redispass);
	}
}
