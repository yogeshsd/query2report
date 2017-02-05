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

import com.lwr.software.reporter.admin.connmgmt.ConnectionFactory;
import com.lwr.software.reporter.admin.connmgmt.ConnectionList;
import com.lwr.software.reporter.admin.connmgmt.ConnectionManager;
import com.lwr.software.reporter.admin.connmgmt.ConnectionParams;

@Path("/connections/")
public class ConnectionManagementService {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ConnectionList getConnectionList(){
		Set<ConnectionParams> connections = ConnectionManager.getConnectionManager().getConnectionParams();
		ConnectionList connectionList = new ConnectionList();
		connectionList.setConnectionList(connections);
		return connectionList;
	}
	
	@Path("/{param1}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ConnectionList getUser(@PathParam("param1") String alias){
		ConnectionList connectionList = new ConnectionList();
		ConnectionParams connection = ConnectionManager.getConnectionManager().getConnectionParams(alias);
		Set<ConnectionParams> cl = new HashSet<ConnectionParams>();
		cl.add(connection);
		connectionList.setConnectionList(cl);
		return connectionList;
	}

	@Path("/remove/{param1}")
	@POST
	public Response removeUser(@PathParam("param1") String alias){
		boolean status = ConnectionManager.getConnectionManager().removeConnection(alias);
		if(status)
			return Response.ok("Connection '"+alias+"' Deleted.").build();
		else
			return Response.ok("Unable to delete connection '"+alias+"'.").build();
	}
	
	@Path("/save")
	@POST
	public Response updateUser(
			@QueryParam("alias") String alias,
			@QueryParam("isdefault") String isdefault,
			@QueryParam("username") String userName,
			@QueryParam("password") String password,
			@QueryParam("url") String url,
			@QueryParam("driver") String driver,
			@QueryParam("isconnectionsuccess") String isconnectionsuccess){
		if(isdefault == null)
			isdefault="false";
		else
			isdefault="true";
		ConnectionParams connParams = new ConnectionParams(userName,password,driver,url,alias,isdefault,isconnectionsuccess);
		boolean status = ConnectionManager.getConnectionManager().saveConnectionParams(connParams);
		if(status)
			return Response.ok("Connection '"+alias+"' Saved.").build();
		else
			return Response.ok("Unable to save connection '"+alias+"'.").build();
	}

	@Path("/test")
	@POST
	public Response testConnection(
			@QueryParam("alias") String alias,
			@QueryParam("isdefault") String isdefault,
			@QueryParam("username") String userName,
			@QueryParam("password") String password,
			@QueryParam("url") String url,
			@QueryParam("driver") String driver){
		String message="";
		
		if(isdefault == null)
			isdefault="false";
		else
			isdefault="true";
		ConnectionParams connParams = new ConnectionParams(userName,password,driver,url,alias,isdefault,"false");
		boolean status=false;
		try{
			status = ConnectionFactory.testConnection(connParams);
		}catch (ClassNotFoundException e){
			message = "Driver Class "+driver+" not found. Check if the jar file is copied to {CATALINA_HOME}/webapps/lib folder";
			status=false;
		}catch (Exception e){
			message = e.getMessage();
			status=false;
		}
		ConnectionManager.getConnectionManager().saveConnectionParams(connParams);
		if(status)
			return Response.ok("Connection to '"+alias+"' successful.").build();
		else{
			return Response.serverError().entity("Connection to '"+alias+"' failed. Error "+message).build();
		}
	}
}
