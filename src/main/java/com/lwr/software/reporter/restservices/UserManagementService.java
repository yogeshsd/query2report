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

package com.lwr.software.reporter.restservices;


import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

import com.lwr.software.reporter.DashboardConstants;
import com.lwr.software.reporter.admin.usermgmt.User;
import com.lwr.software.reporter.admin.usermgmt.UserManager;

@Path("/users/")
public class UserManagementService {
	
	private static Logger logger = LogManager.getLogger(UserManagementService.class);
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUsers(){
		logger.info("Getting all users");
		Set<User> users = UserManager.getUserManager().getUsers();
		JSONObject userList = new JSONObject();
		userList.put("users", users);
		logger.info("Returning "+(users==null?0:users.size())+" users");
		return Response.ok(userList).build();
	}

	@Path("/{param1}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUser(@PathParam("param1") String userName){
		if(userName == null){
			logger.error("User name cannot be null");
			return Response.serverError().entity("User name cannot be null.").build();
		}
		logger.info("Getting user details for - "+userName);
		User user = UserManager.getUserManager().getUser(userName);
		Set<User> ul = new HashSet<User>();
		ul.add(user);
		JSONObject userList = new JSONObject();
		userList.put("users", ul);
		logger.info("Returning "+(user==null?null:user.getUsername())+" details");
		return Response.ok(userList).build();
	}

	
	@Path("/{param1}/role")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserRole(@PathParam("param1") String userName){
		if(userName == null){
			logger.error("User name cannot be null");
			return Response.serverError().entity("User name cannot be null.").build();
		}
		logger.info("Getting user role details for user - "+userName);		
		User user = UserManager.getUserManager().getUser(userName);
		JSONObject userList = new JSONObject();
		userList.put("userRole", user.getRole());
		logger.info("Returning "+(user==null?null:user.getUsername())+" with "+user.getRole()+" ");
		return Response.ok(userList).build();
	}

	@Path("/{param1}/remove")
	@DELETE
	public Response removeUser(@PathParam("param1") String userName){
		if(userName == null){
			logger.error("User name cannot be null");
			return Response.serverError().entity("User name cannot be null.").build();
		}
		logger.info("Removing user "+userName);
		if(userName.equals(DashboardConstants.ADMIN_USER)){
			logger.error("Cannot delete built in admin user");
			return Response.serverError().entity("Cannot delete user '"+userName+"'").build();
		}
		boolean status = UserManager.getUserManager().removeUser(userName);
		if(status){
			logger.info("User "+userName+" deleted");
			return Response.ok("User '"+userName+"' deleted.").build();
		}
		else{
			logger.error("Unable to delete user "+userName);
			return Response.serverError().entity("Unable to delete user '"+userName+"'").build();
		}
	}
	
	@Path("/save")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateUser(User user){
		if(user == null){
			logger.error("User name cannot be null");
			return Response.serverError().entity("User name cannot be null.").build();
		}
		logger.info("Saving user "+user.getDisplayName());
		boolean status = UserManager.getUserManager().saveUser(user);
		if(status){
			logger.info("User "+user.getDisplayName()+" saved.");
			return Response.ok("User '"+user.getDisplayName()+"' saved.").build();
		}else{
			logger.error("Unable to save user "+user.getDisplayName());
			return Response.serverError().entity("Unable to save user '"+user.getDisplayName()+"'.").build();
		}
	}
}
