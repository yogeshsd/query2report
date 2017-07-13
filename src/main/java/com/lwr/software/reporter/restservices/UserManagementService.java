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

import org.json.simple.JSONObject;

import com.lwr.software.reporter.admin.usermgmt.User;
import com.lwr.software.reporter.admin.usermgmt.UserManager;

@Path("/users/")
public class UserManagementService {
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUsers(){
		System.out.println("UserManagementService : getUsers ");
		Set<User> users = UserManager.getUserManager().getUsers();
		JSONObject userList = new JSONObject();
		userList.put("users", users);
		return Response.ok(userList).build();
	}

	@Path("/{param1}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUser(@PathParam("param1") String userName){
		System.out.println("UserManagementService : getUser : "+userName);
		User user = UserManager.getUserManager().getUser(userName);
		Set<User> ul = new HashSet<User>();
		ul.add(user);
		JSONObject userList = new JSONObject();
		userList.put("users", ul);
		return Response.ok(userList).build();
	}

	
	@Path("/{param1}/role")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserRole(@PathParam("param1") String userName){
		System.out.println("UserManagementService : getUserRole : "+userName);
		User user = UserManager.getUserManager().getUser(userName);
		JSONObject userList = new JSONObject();
		userList.put("userRole", user.getRole());
		return Response.ok(userList).build();
	}

	
	@Path("/{param1}/remove")
	@DELETE
	public Response removeUser(@PathParam("param1") String userName){
		System.out.println("UserManagementService : removeUser : "+userName);
		boolean status = UserManager.getUserManager().removeUser(userName);
		if(status)
			return Response.ok("User '"+userName+"' Deleted.").build();
		else
			return Response.serverError().entity("Unable to delete user '"+userName+"'").build();
	}
	
	@Path("/save")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateUser(User user){
		System.out.println("UserManagementService : updateUser : "+user);
		boolean status = UserManager.getUserManager().saveUser(user);
		if(status)
			return Response.ok("User '"+user.getDisplayName()+"' Saved.").build();
		else
			return Response.serverError().entity("Unable to save user '"+user.getDisplayName()+"'.").build();
	}
}
