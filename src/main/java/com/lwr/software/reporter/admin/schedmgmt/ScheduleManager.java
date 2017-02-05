package com.lwr.software.reporter.admin.schedmgmt;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.CollectionType;
import org.codehaus.jackson.map.type.TypeFactory;
import org.quartz.SchedulerException;

import com.lwr.software.reporter.DashboardConstants;
import com.lwr.software.reporter.admin.usermgmt.User;
import com.lwr.software.reporter.admin.usermgmt.UserManager;
import com.lwr.software.reporter.reportmgmt.ReportManager;

public class ScheduleManager {

	private static volatile ScheduleManager manager;
	
	private static Logger logger = LogManager.getLogger(ScheduleManager.class);
	
	private LWRSchedular schedular;
	
	private Map<String,Set<Schedule>> userToScheduleMap = new HashMap<String,Set<Schedule>>();
	
	private Map<String,Map<String,Set<ScheduleRunAudit>>> userScheduleHistroy = new HashMap<String,Map<String,Set<ScheduleRunAudit>>>();
	
	public static ScheduleManager getScheduleManager(){
		if(manager == null){
			synchronized (ScheduleManager.class) {
				if(manager == null){
					manager = new ScheduleManager();
				}
			}
		}
		return manager;
	}
	
	private ScheduleManager(){
		init();
	}
	
	private void init(){
	    try {
	    	logger.info("Starting schedular...");
	    	schedular = LWRSchedular.getInstance();
	    	schedular.startSchedular();
	    } catch (Throwable e) {
	    	e.printStackTrace();
	    	logger.error("Unable to initialize schedule manager.",e);
	    	return;
	    }
    	logger.info("Initializing schedules for all users.");
    	Set<User> users = UserManager.getUserManager().getUsers();
    	for (User user : users) {
    		ReportManager.getReportManager().reload(user.getUsername());
    		loadSchedule(user);
		}
	}
	
	private void loadSchedule(User user) {
		String userName = user.getUsername();
		Set<Schedule> schedules = new HashSet<Schedule>();
		logger.info("Loading schedules for user "+userName);
        try {
			String userScheduleFile = DashboardConstants.PRIVATE_REPORT_DIR+File.separatorChar+userName+File.separatorChar+"schedule"+File.separatorChar+"schedules.json";
			File file = new File(userScheduleFile);
			if(file.exists()){
				ObjectMapper objectMapper = new ObjectMapper();
				TypeFactory typeFactory = objectMapper.getTypeFactory();
				CollectionType collectionType = typeFactory.constructCollectionType(Set.class, Schedule.class);
				schedules =  objectMapper.readValue(file, collectionType);
			}else{
				logger.info("No schedules found for user "+userName);
			}
			userToScheduleMap.put(userName, schedules);
		} catch (IOException e) {
	    	e.printStackTrace();
	    	logger.error("Unable to initialize schedules for user "+userName,e);
	    	return;
		}
        
        logger.info("Scheduling the jobs for user "+userName);
        for (Schedule schedule : schedules) {
        	try {
				schedular.addJob(schedule,user.getUsername());
			} catch (SchedulerException e) {
				logger.error("Unable to initialize schedules for user "+userName+" and schedule "+schedule.getScheduleName(),e);
			}
		}
	}

	public boolean saveSchedule(Schedule schedule,String userName){
		logger.info("Saving schedule "+schedule.getScheduleName()+" for user "+userName);
		Set<Schedule> schedules = userToScheduleMap.get(userName);
		boolean isSaved=true;
		try{
			if(schedules != null && schedules.contains(schedule)){
				schedules.remove(schedule);
				schedules.add(schedule);
				schedular.updateJob(schedule, userName);
			} else{
				if(schedules == null){
					schedules = new HashSet<Schedule>();
				}
				schedules.add(schedule);
				userToScheduleMap.put(userName, schedules);
				schedular.addJob(schedule, userName);
			}
			serialize();
		} catch (SchedulerException e) {
			logger.error("Unable to save/update schedule"+schedule.getScheduleName(),e);
			isSaved=false;
		}
		return isSaved;
	}
	
	public Schedule getSchedule(String name,String userName){
		Set<Schedule> schedules = userToScheduleMap.get(userName);
		for (Schedule schedule : schedules) {
			if(schedule.getScheduleName().equalsIgnoreCase(name))
				return schedule;
		}
		return null;
	}
	
	public boolean removeSchedule(String name,String userName){
		logger.info("Deleting schedule "+name+" for user "+userName);
		Set<Schedule> schedules = userToScheduleMap.get(userName);
		boolean toReturn = false;
		try {
			for (Schedule schedule : schedules) {
				if(schedule.getScheduleName().equalsIgnoreCase(name)){
					toReturn = schedules.remove(schedule);
					schedular.deleteJob(schedule,userName);
					serialize();
					break;
				}
			}
		} catch (SchedulerException e) {
			logger.error("Unable to delete schedule "+name,e);
		}
		return toReturn;
	}
	
	private void serialize(){
			logger.info("Serializing schedules...");
			Set<String> userNames = userToScheduleMap.keySet();
			for (String userName : userNames) {
				try{
				logger.info("Serializing schedule for user "+userName);
				Set<Schedule> schedules = userToScheduleMap.get(userName);
				ObjectMapper objectMapper = new ObjectMapper();
				String dataToRight = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(schedules);
				
				String path = DashboardConstants.PRIVATE_REPORT_DIR+File.separatorChar+userName+File.separatorChar+"schedule";
				File dir = new File(path);
				dir.mkdirs();
				
				String userScheduleFile = path+File.separatorChar+"schedules.json";
				FileWriter writer = new FileWriter(userScheduleFile);
				writer.write(dataToRight);
				writer.flush();
				writer.close();
				}catch(Exception e){
					logger.error("Unable to serialize schedule manager.",e);
				}
			}
	}

	public Set<Schedule> getSchedules(String userName) {
		Set<Schedule> schedules = userToScheduleMap.get(userName);
		return schedules;
	}
	
	public synchronized void updateScheduleRunAudit(ScheduleRunAudit audit){
		String userName = audit.getUserName();
		Map<String, Set<ScheduleRunAudit>> scheds = this.userScheduleHistroy.get(userName);
		
		if(scheds == null){
			scheds = new HashMap<String,Set<ScheduleRunAudit>>();
			this.userScheduleHistroy.put(userName, scheds);
		}
		
		Set<ScheduleRunAudit> instances = scheds.get(audit.getScheduleName());
		if(instances == null){
			instances = new HashSet<ScheduleRunAudit>();
			scheds.put(audit.getScheduleName(), instances);
		}
		instances.add(audit);
	}
	
	public Map<String, Set<ScheduleRunAudit>> getScheduleHistory(String userName){
		return this.userScheduleHistroy.get(userName);
	}
}