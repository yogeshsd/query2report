package com.lwr.software.reporter.admin.connmgmt;

import java.sql.Connection;
import java.sql.Driver;
import java.util.Properties;

public class ConnectionFactory {

	public static Connection getConnection(String alias) {
		try{
			ConnectionParams params = ConnectionManager.getConnectionManager().getConnectionParams(alias);
			String url = params.getUrl();
			String driverClass = params.getDriver();
			String username = params.getUsername();
			String password = params.getPassword();
			System.out.println("Trying to get connection to DB [ " + url + " ] for user [ " + username + " ] and driver class [" + driverClass + "]");
			Driver driver = (Driver) Class.forName(driverClass).newInstance();
			Properties props = new Properties();
			props.put("user", username);
			props.put("password", password);
			Connection connection = driver.connect(url, props);
			connection.setAutoCommit(false);
			System.out.println("Got new connection to DB [ " + url + " ] for user [ " + username + "] " + connection);
			return connection;
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public static boolean testConnection(ConnectionParams params) throws Exception {
		boolean status = false;
		try{
			String url = params.getUrl();
			String driverClass = params.getDriver();
			String username = params.getUsername();
			String password = params.getPassword();
			System.out.println("Trying to get connection to DB [ " + url + " ] for user [ " + username + " ] and driver class [" + driverClass + "]");
			Driver driver = (Driver) Class.forName(driverClass).newInstance();
			Properties props = new Properties();
			props.put("user", username);
			props.put("password", password);
			Connection connection = driver.connect(url, props);
			connection.setAutoCommit(false);
			System.out.println("Got new connection to DB [ " + url + " ] for user [ " + username + "] " + connection);
			status=true;
			params.setIsConnectionSuccess(Boolean.toString(status));
			connection.close();
		}catch (Exception e){
			throw e;
		}
		return status;
	}
}
