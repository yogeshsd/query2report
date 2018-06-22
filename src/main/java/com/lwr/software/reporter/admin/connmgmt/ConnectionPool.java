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
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.lwr.software.reporter.DashboardConstants;

public class ConnectionPool {

	private Map<String,Stack<Connection>> connectionPool = new HashMap<String,Stack<Connection>>();

	private Map<String,Integer> connectionCount = new HashMap<String,Integer>();
	
	private static ConnectionPool _instance;
	
	private static Logger logger = LogManager.getLogger(ConnectionPool.class);
	
	public static ConnectionPool getInstance(){
		if(_instance == null){
			synchronized(ConnectionPool.class){
				if(_instance == null){
					_instance = new ConnectionPool();
				}
			}
		}
		return _instance;
	}
	
	private ConnectionPool(){
	}
	
	public Connection getConnection(String alias){
		logger.info("Getting connection for alias "+alias);
		Stack<Connection> connections = connectionPool.get(alias);
		if(connections == null){
			logger.info("Connection pool for alias "+alias+" is empty. Creating new pool.");
			synchronized(connectionPool){
				if(connections == null){
					connections = new Stack<Connection>();
					connectionPool.put(alias, connections);
				}
			}
		}
		
		synchronized(connections){
			Integer usedConnections = connectionCount.get(alias);
			if(usedConnections == null){
				usedConnections=0;
			}
			if(connections.isEmpty() && usedConnections >= DashboardConstants.MAX_CONNECTIONS){
					try {
						logger.warn("Pool is empty. All "+DashboardConstants.MAX_CONNECTIONS+" allowed connections in pool are already in use. Waiting for other threads to release connection.");
						while(connections.isEmpty())
							connections.wait();
						logger.info("Done waiting for other threads to release connection. This thread will continue.");
						return connections.pop();
					} catch (InterruptedException e) {
						logger.error("Error during wait for connection for alias "+alias,e);
						e.printStackTrace();
					}
			}else if(connections.isEmpty() && usedConnections< DashboardConstants.MAX_CONNECTIONS){
				logger.info("Pool is empty. Creating a new connection for alias "+alias+" as pool limit "+DashboardConstants.MAX_CONNECTIONS+" is not reached.");
				Connection connection = ConnectionFactory.getConnection(alias);
				if(connection!=null){
					usedConnections++;
					connectionCount.put(alias, usedConnections);
				}
				return connection;
			}else{
				logger.info("Returning connection from the pool for alias "+alias);
				return connections.pop();
			}
		}
		return null;
	}
	
	public void releaseConnection(Connection connection,String alias){
		logger.info("Releasing the connection back to pool for alias "+alias);
		try {
			connection.rollback();
		} catch (SQLException e) {
			logger.error("Error during roll back of connection while releasing for alias "+alias);
			e.printStackTrace();
		}
		Stack<Connection> connections = connectionPool.get(alias);
		synchronized(connections){
			connections.push(connection);
			connections.notifyAll();
		}
	}

	public void removeConnection(String alias){
		connectionPool.remove(alias);
	}

}
