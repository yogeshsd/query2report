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
	
	static{
		File configDir = new File(DashboardConstants.CONFIG_PATH);
		configDir.mkdirs();
	}
	
	private String fileName = DashboardConstants.CONFIG_PATH+"drivers.json";
	
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
	    try {
	    	ObjectMapper objectMapper = new ObjectMapper();
	        TypeFactory typeFactory = objectMapper.getTypeFactory();
	        CollectionType collectionType = typeFactory.constructCollectionType(Set.class, ConnectionParams.class);
	        connParams =  objectMapper.readValue(new File(fileName), collectionType);
	        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(connParams));
	    } catch (IOException e) {
	    	logger.error("Unable to initialize connection manager",e);
	    }
	}
	
	public boolean saveConnectionParams(ConnectionParams params){
		logger.info("Saving connection "+params.getAlias());
		try{
			if(connParams.contains(params)){
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
	
	public Set<ConnectionParams> getConnectionParams(){
		return this.connParams;
	}
	
	public ConnectionParams getConnectionParams(String alias){
		ConnectionParams defConnParam = null;
		boolean isFirst = true;
		for (ConnectionParams connectionParams : connParams) {
			if(isFirst){
				defConnParam = connectionParams;
				isFirst = false;
			}
			if(connectionParams.getAlias().equalsIgnoreCase(alias) || (connectionParams.getIsDefault().equalsIgnoreCase("true") && alias.equalsIgnoreCase("default")))
				return connectionParams;
		}
		if(alias.equalsIgnoreCase("default"))
			return defConnParam;
		return null;
	}

	
	private void serializeConnectionParams(){
		try{
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

	public boolean removeConnection(String alias) {
		logger.info("Deleting connection "+alias);
		ConnectionParams paramToDelete = null;
		for (ConnectionParams param : connParams) {
			if(param.getAlias().equalsIgnoreCase(alias)){
				paramToDelete = param;
				break;
			}
		}
		if(paramToDelete == null)
			return false;
		connParams.remove(paramToDelete);
		serializeConnectionParams();
		return true;
	}
}
