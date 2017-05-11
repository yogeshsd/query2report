package com.lwr.software.reporter.restservices;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.lwr.software.reporter.admin.usermgmt.User;
import com.lwr.software.reporter.admin.usermgmt.UserList;
import com.lwr.software.reporter.admin.usermgmt.UserManager;

@Path("/users/")
public class UserManagementService {
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public UserList getUsers(){
		System.out.println("UserManagementService : get ");
		UserList userList = new UserList();
		Set<User> users = UserManager.getUserManager().getUsers();
		userList.setUserList(users);
		return userList;
	}

	@Path("/{param1}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public UserList getUser(@PathParam("param1") String userName){
		System.out.println("UserManagementService : get : "+userName);
		UserList userList = new UserList();
		User user = UserManager.getUserManager().getUser(userName);
		Set<User> ul = new HashSet<User>();
		ul.add(user);
		userList.setUserList(ul);
		return userList;
	}

	@Path("/{param1}/remove")
	@DELETE
	public Response removeUser(@PathParam("param1") String userName){
		System.out.println("UserManagementService : remove : "+userName);
		boolean status = UserManager.getUserManager().removeUser(userName);
		if(status)
			return Response.ok("User '"+userName+"' Deleted.").build();
		else
			return Response.serverError().build();
	}
	
	@Path("/save")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateUser(User user){
		System.out.println("UserManagementService : save : "+user);
		boolean status = UserManager.getUserManager().saveUser(user);
		if(status)
			return Response.ok("User '"+user.getDisplayName()+"' Saved.").build();
		else
			return Response.ok("Unable to save user '"+user.getDisplayName()+"'.").build();
	}
}
