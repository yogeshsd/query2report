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
