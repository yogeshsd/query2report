package com.lwr.software.reporter.admin.drivermgmt;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.CollectionType;
import org.codehaus.jackson.map.type.TypeFactory;

import com.lwr.software.reporter.DashboardConstants;

public class DriverManager {

	private static volatile DriverManager manager;
	
	private Set<DriverParams> driverParams = new HashSet<DriverParams>();
	
	private static Logger logger = LogManager.getLogger(DriverManager.class);
	
	static{
		File configDir = new File(DashboardConstants.CONFIG_PATH);
		System.out.println("Creating directory "+configDir);
		configDir.mkdirs();
	}
	
	private String fileName = DashboardConstants.CONFIG_PATH+"drivers.json";
	
	public static DriverManager getDriverManager(){
		if(manager == null){
			synchronized (DriverManager.class) {
				if(manager == null){
					manager = new DriverManager();
				}
			}
		}
		return manager;
	}
	
	private DriverManager(){
		init();
	}
	
	private void init(){
	    try {
	    	ObjectMapper objectMapper = new ObjectMapper();
	        TypeFactory typeFactory = objectMapper.getTypeFactory();
	        CollectionType collectionType = typeFactory.constructCollectionType(Set.class, DriverParams.class);
	        driverParams =  objectMapper.readValue(new File(fileName), collectionType);
	        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(driverParams));
	    } catch (IOException e) {
	    	logger.error("Unable to initialize connection manager",e);
	    }
	}
	
	public boolean saveDriver(DriverParams params){
		logger.info("Saving driver "+params.getAlias());
		try{
			boolean found = false;
			Iterator<DriverParams> iterator = driverParams.iterator();
			while(iterator.hasNext()){
				DriverParams param = iterator.next();
				if(param.getAlias().equals(params.getAlias())){
					found = true;
					param.setClassName(params.getClassName());
					if(params.getJarFile() != null)
						param.setJarFile(params.getJarFile());
				}
			}
			if(!found)
				driverParams.add(params);
			serializeDriverParams();
			return true;
		}catch(Exception e){
			logger.error("Unable to save driver "+params.getAlias(),e);
			return false;
		}
		
	}
	
	public Set<DriverParams> getDrivers(){
		return this.driverParams;
	}
	
	public DriverParams getDriver(String alias){
		for (DriverParams driver : driverParams) {
			if(driver.getAlias().equalsIgnoreCase(alias))
				return driver;
		}
		return null;
	}

	public boolean removeDriver(String alias) {
		logger.info("Deleting driver "+alias);
		DriverParams paramToDelete = null;
		for (DriverParams param : driverParams) {
			if(param.getAlias().equalsIgnoreCase(alias)){
				paramToDelete = param;
				break;
			}
		}
		if(paramToDelete == null)
			return false;
		driverParams.remove(paramToDelete);
		serializeDriverParams();
		return true;
	}
	
	private void serializeDriverParams(){
		try{
	    	ObjectMapper objectMapper = new ObjectMapper();
	        String dataToRight = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(driverParams);
	        logger.info("Drivers file name is "+new File(fileName).getAbsolutePath());
	        FileWriter writer = new FileWriter(fileName);
	        writer.write(dataToRight);
	        writer.flush();
	        writer.close();
		}catch(Exception e){
			e.printStackTrace();
			logger.error("Unable to seralize connection manager ",e);
		}
	}

}
