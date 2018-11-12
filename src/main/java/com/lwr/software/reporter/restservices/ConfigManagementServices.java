package com.lwr.software.reporter.restservices;

import java.io.IOException;
import java.util.Properties;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.lwr.software.reporter.utils.Q2RProperties;

@Path("/props/")
public class ConfigManagementServices {
	
	private static Logger logger = LogManager.getLogger(ConfigManagementServices.class);
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getConfigProperties(){
		Q2RProperties props = Q2RProperties.getInstance();
		return Response.ok(props).build();
	}

	@PUT
	public Response saveConfigProperties(Properties properties){
		Q2RProperties props = Q2RProperties.getInstance();
		props.setProperties(properties);
		try {
			props.saveProperties();
			return Response.ok("Configuration Saved Successfully").build();
		} catch (IOException e) {
			logger.error("Configuration save failed ",e);
			return Response.serverError().entity("Configuration save failed "+e.getMessage()).build();
		}
	}
}
