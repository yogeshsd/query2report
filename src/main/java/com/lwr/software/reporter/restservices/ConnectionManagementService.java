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
		System.out.println("ConnectionManagementService : get");
		Set<ConnectionParams> connections = ConnectionManager.getConnectionManager().getConnectionParams();
		ConnectionList connectionList = new ConnectionList();
		connectionList.setConnectionList(connections);
		return connectionList;
	}
	
	@Path("/{alias}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ConnectionList getUser(@PathParam("alias") String alias){
		System.out.println("ConnectionManagementService : get : "+alias);
		ConnectionList connectionList = new ConnectionList();
		ConnectionParams connection = ConnectionManager.getConnectionManager().getConnectionParams(alias);
		Set<ConnectionParams> cl = new HashSet<ConnectionParams>();
		cl.add(connection);
		connectionList.setConnectionList(cl);
		return connectionList;
	}

	@Path("/{alias}/remove")
	@DELETE
	public Response removeUser(@PathParam("alias") String alias){
		System.out.println("ConnectionManagementService : remove : "+alias);
		boolean status = ConnectionManager.getConnectionManager().removeConnection(alias);
		if(status)
			return Response.ok("Connection '"+alias+"' Deleted.").build();
		else
			return Response.ok("Unable to delete connection '"+alias+"'.").build();
	}
	
	@Path("/save")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateUser(ConnectionParams connParams){
		System.out.println("ConnectionManagementService : save : "+connParams);
		boolean status = ConnectionManager.getConnectionManager().saveConnectionParams(connParams);
		if(status)
			return Response.ok("Connection '"+connParams.getAlias()+"' Saved.").build();
		else
			return Response.ok("Unable to save connection '"+connParams.getAlias()+"'.").build();
	}

	@Path("/test")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response testConnection(ConnectionParams connParams){
		System.out.println("ConnectionManagementService : test : "+connParams);
		String message="";
		boolean status=false;
		try{
			status = ConnectionFactory.testConnection(connParams);
		}catch (ClassNotFoundException e){
			message = "Driver Class "+connParams.getDriver()+" not found. Check if the jar file is copied to {CATALINA_HOME}/webapps/lib folder";
			status=false;
		}catch (Exception e){
			message = e.getMessage();
			status=false;
		}
		ConnectionManager.getConnectionManager().saveConnectionParams(connParams);
		if(status)
			return Response.ok("Connection to '"+connParams.getAlias()+"' successful.").build();
		else{
			return Response.serverError().entity("Connection to '"+connParams.getAlias()+"' failed. Error "+message).build();
		}
	}
}
