package com.lwr.software.reporter.admin.connmgmt;

import java.sql.Connection;
import java.sql.Driver;
import java.util.Properties;

import com.lwr.software.reporter.admin.drivermgmt.DriverManager;
import com.lwr.software.reporter.admin.drivermgmt.DriverParams;
import com.lwr.software.reporter.utils.EncryptionUtil;

public class ConnectionFactory {

	public static Connection getConnection(String alias) {
		try{
			ConnectionParams params = ConnectionManager.getConnectionManager().getConnectionParams(alias);
			String url = params.getUrl();
			DriverParams driverParams = DriverManager.getDriverManager().getDriver(params.getDriver());
			String driverClass = driverParams.getClassName();
			String username = params.getUsername();
			String password = params.getPassword();
			String decPassword = EncryptionUtil.decrypt(password);
			System.out.println("Trying to get connection to DB [ " + url + " ] for user [ " + username + " ] and driver class [" + driverClass + "]");
			Driver driver = (Driver) Class.forName(driverClass).newInstance();
			Properties props = new Properties();
			props.put("user", username);
			props.put("password", decPassword);
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
			DriverParams driverParams = DriverManager.getDriverManager().getDriver(params.getDriver());
			String driverClass = driverParams.getClassName();
			String username = params.getUsername();
			String password = params.getPassword();
			String decPassword = EncryptionUtil.decrypt(password);
			System.out.println("Trying to get connection to DB [ " + url + " ] for user [ " + username + " ] and driver class [" + driverClass + "]");
			Driver driver = (Driver) Class.forName(driverClass).newInstance();
			Properties props = new Properties();
			props.put("user", username);
			props.put("password", decPassword);
			if(driver.acceptsURL(url)){
				Connection connection = driver.connect(url, props);
				connection.setAutoCommit(false);
				System.out.println("Got new connection to DB [ " + url + " ] for user [ " + username + "] " + connection);
				status=true;
				params.setIsConnectionSuccess(Boolean.toString(status));
				connection.close();
			}else{
				throw new RuntimeException("Driver "+params.getDriver()+" is not suitable for URL "+url);
			}
		}catch (Exception e){
			throw e;
		}
		return status;
	}
}
