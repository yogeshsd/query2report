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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.CollectionType;
import org.codehaus.jackson.map.type.TypeFactory;

import com.lwr.software.reporter.DashboardConstants;
import com.lwr.software.reporter.utils.EncryptionUtil;

public class ConnectionManager {

	private static volatile ConnectionManager manager;
	
	private Set<ConnectionParams> connParams = new HashSet<ConnectionParams>();
	
	private static Logger logger = LogManager.getLogger(ConnectionManager.class);
	
	private String fileName = DashboardConstants.CONFIG_PATH+"connections.json";
	
	public static ConnectionManager getConnectionManager(){
		if(manager == null){
			synchronized (ConnectionManager.class) {
				if(manager == null){
					manager = new ConnectionManager();
				}
			}
		}
		return manager;
	}
	
	private ConnectionManager(){
		init();
	}
	
	private void init(){
		logger.info("Initializing connection manager from "+new File(fileName).getAbsolutePath());
	    try {
	    	ObjectMapper objectMapper = new ObjectMapper();
	        TypeFactory typeFactory = objectMapper.getTypeFactory();
	        CollectionType collectionType = typeFactory.constructCollectionType(Set.class, ConnectionParams.class);
	        connParams =  objectMapper.readValue(new File(fileName), collectionType);
	    } catch (IOException e) {
	    	logger.error("Unable to initialize connection manager",e);
	    }
	}
	
	public Set<ConnectionParams> getConnectionParams(){
		return this.connParams;
	}
	
	public ConnectionParams getConnectionParams(String alias){
		logger.info("Getting connection params for alias "+alias);
		ConnectionParams defConnParam = null;
		boolean isFirst = true;
		for (ConnectionParams connectionParams : connParams) {
			if(isFirst){
				defConnParam = connectionParams;
				isFirst = false;
			}
			if(connectionParams.getAlias().equalsIgnoreCase(alias) || (connectionParams.getIsDefault().equalsIgnoreCase("true") && alias.equalsIgnoreCase("default"))){
				logger.info("Returning connection params ["+connectionParams+"] for alias "+alias);				
				return connectionParams;
			}
		}
		if(alias.equalsIgnoreCase("default"))
			return defConnParam;
		logger.warn("No connection params defined against alias "+alias);
		return null;
	}

	public boolean saveConnectionParams(ConnectionParams params){
		logger.info("Saving connection "+params);
		try{
			if(connParams.contains(params)){
				if(params.getPassword() == null){
					ConnectionParams prevConn = null;
					for (ConnectionParams cp : connParams) {
						if(cp.getAlias().equals(params.getAlias()))
							prevConn = cp;
					}
					if(prevConn!=null)
						params.setPassword(prevConn.getPassword());
				}else{
					String password = params.getPassword();
					String encPassword = EncryptionUtil.encrypt(password);
					params.setPassword(encPassword);
				}
				connParams.remove(params);
				connParams.add(params);
			}else{
				String password = params.getPassword();
				String encPassword = EncryptionUtil.encrypt(password);
				params.setPassword(encPassword);
				connParams.add(params);
			}
			if(params.getIsDefault().equalsIgnoreCase("true")){
				for (ConnectionParams connParam : connParams) {
					if(connParam.getIsDefault().equalsIgnoreCase("true") && !connParam.equals(params))
						connParam.setIsDefault("false");
				}
			}
			serializeConnectionParams();
			return true;
		}catch(Exception e){
			logger.error("Unable to save connection "+params.getAlias(),e);
			return false;
		}
		
	}

	public boolean removeConnection(String alias) {
		logger.info("Deleting connection params for alias "+alias);
		ConnectionParams paramToDelete = null;
		for (ConnectionParams param : connParams) {
			if(param.getAlias().equalsIgnoreCase(alias)){
				paramToDelete = param;
				break;
			}
		}
		if(paramToDelete == null){
			logger.error("No connection params found for alias "+alias);
			return false;
		}
		logger.info("Deleting connection params ["+paramToDelete+"] for alias "+alias);
		connParams.remove(paramToDelete);
		serializeConnectionParams();
		ConnectionPool.getInstance().removeConnection(alias);
		return true;
	}
	
	private void serializeConnectionParams(){
		try{
			logger.info("Seralizing drivers to file "+new File(fileName).getAbsolutePath());
	    	ObjectMapper objectMapper = new ObjectMapper();
	        String dataToRight = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(connParams);
	        FileWriter writer = new FileWriter(fileName);
	        writer.write(dataToRight);
	        writer.flush();
	        writer.close();
		}catch(Exception e){
			logger.error("Unable to seralize connection manager ",e);
		}
	}

}
