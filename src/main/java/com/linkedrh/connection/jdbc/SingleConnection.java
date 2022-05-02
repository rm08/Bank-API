package com.linkedrh.connection.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;

import org.springframework.stereotype.Component;

@Component
public class SingleConnection {

	private static String url = "jdbc:postgresql://localhost:5432/linkedrh";
	private static String password = "admin";
	private static String user = "postgres";
	private static Connection connection = null;

	public SingleConnection() {
		try {
			if (connection == null) {
				Class.forName("org.postgresql.Driver");
				connection = DriverManager.getConnection(url, user, password);
				connection.setAutoCommit(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Connection get() {
		return connection;
	}


}
