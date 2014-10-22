package com.marspotato.supportsmallshop.util;

import java.io.IOException;
import java.io.InputStream;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public final class ConnectionContainer {

	private static JedisPool pool = null;
	private static SqlSessionFactory sqlSessionFactory = null;

	// seal the class
	private ConnectionContainer() {
	}

	public static SqlSession getDBConnection() {
		if (sqlSessionFactory == null) {
			synchronized (ConnectionContainer.class) {
				if (sqlSessionFactory == null) {
					String resource = "mybatis-config.xml";
					InputStream inputStream;
					try {
						inputStream = Resources.getResourceAsStream(resource);
						sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
					} catch (IOException e) {
						e.printStackTrace();
						return null;
					}
				}
			}
		}
		return sqlSessionFactory.openSession();
	}
	
	public static Jedis getRedisConnection() {
		if (pool == null) {
			// first time
			synchronized (ConnectionContainer.class) {
				if (pool == null) {
					try {
						Context ctx = new InitialContext();
						ctx = (Context) ctx.lookup("java:comp/env");
						String redisPath = (String) ctx.lookup("supportsmallshop-redisPath");

						//not use sentinal as there is only one redis instance
						pool = new JedisPool(new JedisPoolConfig(), redisPath);

					} catch (NamingException e) {
						e.printStackTrace();
					}
				}
			}

		}
		return pool.getResource();
	}

	public static void returnRedisConnection(Jedis connection) {
		pool.returnResource(connection);
	}
}
