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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.simple.JSONObject;

import com.lwr.software.reporter.admin.drivermgmt.DriverManager;
import com.lwr.software.reporter.admin.drivermgmt.DriverParams;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

@Path("/drivers/")
public class DriverManagementService {

	@Context
	private ServletContext application;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDrivers(){
		System.out.println("DriverManagementService : get");
		Set<DriverParams> connections = DriverManager.getDriverManager().getDrivers();
		JSONObject drivers = new JSONObject();
		drivers.put("drivers", connections);
		return Response.ok(drivers).build();
	}
	
	@Path("/{alias}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDriver(@PathParam("alias") String alias){
		System.out.println("DriverManagementService : get : "+alias);
		DriverParams driver = DriverManager.getDriverManager().getDriver(alias);
		Set<DriverParams> drivers = new HashSet<DriverParams>();
		drivers.add(driver);
		return Response.ok(drivers).build();
	}

	@Path("/{alias}/remove")
	@DELETE
	public Response removeDriver(@PathParam("alias") String alias){
		System.out.println("DriverManagementService : remove : "+alias);
		boolean status = DriverManager.getDriverManager().removeDriver(alias);
		if(status)
			return Response.ok("Driver '"+alias+"' Deleted.").build();
		else
			return Response.serverError().entity("Unable to delete driver '"+alias+"'.").build();
	}
	
	@Path("/save")
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response updateDriver(
			@FormDataParam("alias") String alias,
			@FormDataParam("className") String className,
			@FormDataParam("jarFile") InputStream uploadedInputStream,
			@FormDataParam("jarFile") FormDataContentDisposition fileDetails){
		String path = application.getRealPath("/");
		String fileLocation = path+File.separatorChar+"WEB-INF"+File.separatorChar+"lib"+File.separatorChar+fileDetails.getFileName(); 
		try {  
			if(fileDetails.getFileName() != null){
				FileOutputStream out = new FileOutputStream(new File(fileLocation));  
				int read = 0;  
				byte[] bytes = new byte[1024];  
				out = new FileOutputStream(new File(fileLocation));  
				while ((read = uploadedInputStream.read(bytes)) != -1) {  
					out.write(bytes, 0, read);  
				}  
				out.flush();  
				out.close();
			}
            DriverParams params = new DriverParams();
            params.setAlias(alias);
            params.setClassName(className);
            params.setJarFile(fileDetails.getFileName());
    		boolean status = DriverManager.getDriverManager().saveDriver(params);
    		if(status)
    			return Response.ok("Driver '"+params.getAlias()+"' Saved.").build();
    		else
    			return Response.serverError().entity("Unable to save driver '"+params.getAlias()+"'.").build();
        } catch (IOException e) {
        	e.printStackTrace();
        	return Response.serverError().entity("File Upload failed with error "+e.getMessage()).build();
        }  
	}
}
