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
