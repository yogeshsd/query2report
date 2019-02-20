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
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

import com.lwr.software.reporter.admin.connmgmt.ConnectionFactory;
import com.lwr.software.reporter.admin.connmgmt.ConnectionManager;
import com.lwr.software.reporter.admin.connmgmt.ConnectionParams;
import com.lwr.software.reporter.admin.drivermgmt.DriverManager;
import com.lwr.software.reporter.admin.drivermgmt.DriverParams;

@Path("/connections/")
public class ConnectionManagementService {
	
	private static Logger logger = LogManager.getLogger(ConnectionManagementService.class);

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getConnectionList(){
		logger.info("Getting all connections");
		Set<ConnectionParams> connections = ConnectionManager.getConnectionManager().getConnectionParams();
		JSONObject connectionList = new JSONObject();
		connectionList.put("connections", connections);
		logger.info("Returning "+(connections==null?0:connections.size())+" connections");
		return Response.ok(connectionList).build();
	}
	
	@Path("/{alias}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getConnection(@PathParam("alias") String alias){
		if(alias == null){
			logger.error("Connection alias cannot be null");
			return Response.serverError().entity("Connection alias cannot be null").build();
		}		
		logger.info("Getting connection details for "+alias);
		ConnectionParams connection = ConnectionManager.getConnectionManager().getConnectionParams(alias);
		Set<ConnectionParams> cl = new HashSet<ConnectionParams>();
		cl.add(connection);
		JSONObject connectionList = new JSONObject();
		connectionList.put("connections", cl);
		logger.info("Returning connection details for "+(connection==null?null:connection.getAlias()));
		return Response.ok(connectionList).build();
	}

	@Path("/{alias}/remove")
	@DELETE
	public Response removeConnection(@PathParam("alias") String alias){
		if(alias == null){
			logger.error("Connection alias cannot be null");
			return Response.serverError().entity("Connection alias cannot be null").build();
		}
		logger.info("Removing connection details for "+alias);
		boolean status = ConnectionManager.getConnectionManager().removeConnection(alias);
		if(status){
			logger.info("Connection "+alias+" deleted.");
			return Response.ok("Connection '"+alias+"' deleted").build();
		}
		else{
			logger.error("Unable to delete connection '"+alias);
			return Response.serverError().entity("Unable to delete connection '"+alias).build();
		}
	}
	
	@Path("/save")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateConnection(ConnectionParams connParams){
		if(connParams == null){
			logger.error("Connection alias cannot be null");
			return Response.serverError().entity("Connection alias cannot be null").build();
		}
		logger.info("Saving connection details for "+connParams.getAlias());
		boolean status = ConnectionManager.getConnectionManager().saveConnectionParams(connParams);
		if(status){
			logger.info("Connection '"+connParams.getAlias()+"' saved");
			return Response.ok("Connection '"+connParams.getAlias()+"' saved").build();
		}
		else{
			logger.error("Unable to save connection '"+connParams.getAlias());
			return Response.serverError().entity("Unable to save connection '"+connParams.getAlias()).build();
		}
	}

	@Path("/test")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response testConnection(ConnectionParams connParams){
		if(connParams == null){
			logger.error("Connection alias cannot be null");
			return Response.serverError().entity("Connection alias cannot be null").build();
		}
		logger.info("Testing connection details for "+connParams.getAlias());
		String message="";
		boolean status=false;
		DriverParams driverParams = DriverManager.getDriverManager().getDriver(connParams.getDriver());
		ConnectionParams pms = ConnectionManager.getConnectionManager().getConnectionParams(connParams.getAlias());
		try{
			status = ConnectionFactory.testConnection(pms);
		}catch (ClassNotFoundException e){
			message = "Driver class "+driverParams.getClassName()+" for "+driverParams.getAlias()+" driver alias not found. Check if the jar file "+driverParams.getJarFile()+" is copied to {CATALINA_HOME}/webapps/lib folder";
			status=false;
		}catch (Exception e){
			message = e.getMessage();
			status=false;
		}
		if(status){
			logger.info("Connection to '"+connParams.getAlias()+"' successful");
			return Response.ok("Connection to '"+connParams.getAlias()+"' successful").build();
		}
		else{
			logger.error("Connection to '"+connParams.getAlias()+"' failed. Error "+message);
			return Response.serverError().entity("Connection to '"+connParams.getAlias()+"' failed. Error "+message).build();
		}
	}
}
