package com.lwr.software.reporter.restservices;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.lwr.software.reporter.DashboardConstants.Role;
import com.lwr.software.reporter.admin.usermgmt.User;
import com.lwr.software.reporter.admin.usermgmt.UserList;
import com.lwr.software.reporter.admin.usermgmt.UserManager;

@Path("/users/")
public class UserManagementService {
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public UserList getUsers(){
		UserList userList = new UserList();
		Set<User> users = UserManager.getUserManager().getUsers();
		userList.setUserList(users);
		return userList;
	}

	@Path("/{param1}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public UserList getUser(@PathParam("param1") String userName){
		UserList userList = new UserList();
		User user = UserManager.getUserManager().getUser(userName);
		Set<User> ul = new HashSet<User>();
		ul.add(user);
		userList.setUserList(ul);
		return userList;
	}

	@Path("/remove/{param1}")
	@POST
	public Response removeUser(@PathParam("param1") String userName){
		boolean status = UserManager.getUserManager().removeUser(userName);
		if(status)
			return Response.ok("User '"+userName+"' Deleted.").build();
		else
			return Response.serverError().build();
	}
	
	@Path("/save")
	@POST
	public Response updateUser(
			@QueryParam("displayname") String displayName,
			@QueryParam("username") String userName,
			@QueryParam("password") String password,
			@QueryParam("role") Role role,
			@QueryParam("charttype") String chartType,
			@QueryParam("refreshInterval") Long refreshInterval){
		User user = new User(displayName,userName,password,role);
		if(refreshInterval != null)
			user.setRefreshInterval(refreshInterval);
		if(chartType != null)
			user.setChartType(chartType);
		boolean status = UserManager.getUserManager().saveUser(user);
		if(status)
			return Response.ok("User '"+userName+"' Saved.").build();
		else
			return Response.ok("Unable to save user '"+userName+"'.").build();
	}
}
