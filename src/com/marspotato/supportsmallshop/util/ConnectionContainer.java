package com.marspotato.supportsmallshop.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public final class ConnectionContainer {

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
}
