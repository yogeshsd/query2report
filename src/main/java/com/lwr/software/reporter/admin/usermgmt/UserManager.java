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

package com.lwr.software.reporter.admin.usermgmt;

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

public class UserManager {

	private static volatile UserManager manager;
	
	private Set<User> users = new HashSet<User>();
	
	private static Logger logger = LogManager.getLogger(UserManager.class);
	
	private String fileName = DashboardConstants.CONFIG_PATH+"users.json";
	
	public static UserManager getUserManager(){
		if(manager == null){
			synchronized (UserManager.class) {
				if(manager == null){
					manager = new UserManager();
				}
			}
		}
		return manager;
	}
	
	private UserManager(){
		init();
	}
	
	private void init(){
		logger.info("Initializing user manager from "+new File(fileName).getAbsolutePath());
	    try {
	    	ObjectMapper objectMapper = new ObjectMapper();
	        TypeFactory typeFactory = objectMapper.getTypeFactory();
	        CollectionType collectionType = typeFactory.constructCollectionType(Set.class, User.class);
	        users =  objectMapper.readValue(new File(fileName), collectionType);
	    } catch (IOException e) {
	    	logger.error("Unable to initialize user manager",e);
	    }
		if(users == null || users.isEmpty()){
			logger.info("Adding default admin user");
			String encPassword = EncryptionUtil.encrypt("admin");
			User adminUser = new User("Administrator","admin",encPassword,"admin",DashboardConstants.HTML_GOOGLE);
			users.add(adminUser);
		}
	}
	
	public boolean authUser(String userName, String inPassword) {
		for (User user : users) {
			if(user.getUsername().equalsIgnoreCase(userName)){
				String encPassword = user.getPassword();
				String decPassword = EncryptionUtil.decrypt(encPassword);
				String inDecPassword = EncryptionUtil.decrypt(inPassword);
				if(inDecPassword.equals(decPassword)){
					return true;
				}
			}
		}
		logger.error("User "+userName+" authentication unsuccessful.");
		return false;
	}

	public Set<User> getUsers(){
		return this.users;
	}
	
	public User getUser(String username){
		logger.debug("Getting details for user "+username);
		for (User user : users) {
			if(user.getUsername().equalsIgnoreCase(username)){
				logger.debug("Returning details "+user+" for user "+username);				
				return user;
			}
		}
		logger.warn("Unable to find details for user "+username);
		return null;
	}
	
	public boolean saveUser(User user){
		logger.info("Saving user "+user);
		try{
			if(user.getUsername().equals(DashboardConstants.ADMIN_USER) && !user.getRole().equals(DashboardConstants.ADMIN))
				user.setRole(DashboardConstants.ADMIN);
			User prevUser = null;
			if(users.contains(user)){
				if(user.getPassword() == null){
					for (User u : users) {
						if(u.getUsername().equals(user.getUsername()))
							prevUser = u;
					}
					if(prevUser!=null)
						user.setPassword(prevUser.getPassword());
				}else{
					String password = user.getPassword();
					String encPassword = EncryptionUtil.encrypt(password);
					user.setPassword(encPassword);
				}
				users.remove(user);
				users.add(user);
			}else{
				String password = user.getPassword();
				String encPassword = EncryptionUtil.encrypt(password);
				user.setPassword(encPassword);
				users.add(user);
			}
			seralize();
			return true;
		}catch(Exception e){
			logger.error("Unable to save user "+user.getUsername(),e);
			return false;
		}
	}

	public boolean removeUser(String userName) {
		logger.info("Deleting user "+userName);
		if(userName.equals(DashboardConstants.ADMIN_USER))
			return false;
		User userToDelete = null;
		for (User user : users) {
			if(user.getUsername().equalsIgnoreCase(userName)){
				userToDelete = user;
				break;
			}
		}
		if(userToDelete == null){
			logger.warn("Unable to find details for user "+userName);
			return false;
		}
		logger.info("Deleting details "+userToDelete+" for user "+userName);
		users.remove(userToDelete);
		seralize();
		return true;
	}

	private void seralize(){
		try{
			logger.info("Seralizing users to file "+new File(fileName).getAbsolutePath());
	    	ObjectMapper objectMapper = new ObjectMapper();
	        String dataToRight = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(users);
	        FileWriter writer = new FileWriter(fileName);
	        writer.write(dataToRight);
	        writer.flush();
	        writer.close();
		}catch(Exception e){
			logger.error("Unable to serialize user manager",e);
		}
	}

}
