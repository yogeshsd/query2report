/* 
	Query2Report Copyright (C) 2018  Yogesh Deshpande
	
	This file is part of Query2Report.
	
	Query2Report is free software: you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation, either version 3 of the License, or
	(at your option) any later version.
	
	Query2Report is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.
	
	You should have received a copy of the GNU General Public License
	along with Query2Report.  If not, see <http://www.gnu.org/licenses/>.
*/

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
		}catch (Throwable e){
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
		}catch (Throwable e){
			throw e;
		}
		return status;
	}
}
