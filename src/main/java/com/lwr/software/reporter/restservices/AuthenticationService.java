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

import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.lwr.software.reporter.admin.usermgmt.UserManager;
import com.sun.jersey.multipart.FormDataParam;

@Path("/auth/")
public class AuthenticationService {

	private static Logger logger = LogManager.getLogger(AlertManagementService.class);
	
	@Path("/login")
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response authUser(@FormDataParam("username") String userName,
			@FormDataParam("password") String password){
		logger.info("Authenticating user "+userName);
		try{
			String token = UserManager.getUserManager().authUser(userName, password);
			return Response.ok(token).build();
		}catch(Exception e){
			return Response.serverError().entity(e.getMessage()).status(Response.Status.UNAUTHORIZED).build();
		}
	}

	@Path("/logout")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response logoutUser(@CookieParam("Q2R_AUTH_INFO") Cookie cookie){
		String cookieValue = cookie.getValue();
		String tokenPatterns[] = cookieValue.split("_0_");
		
		if(tokenPatterns.length!=3)
			return Response.serverError().entity("Corrupt Token").build();
		
		logger.info("Logging out user "+tokenPatterns[0]);
		try{
			boolean validToken = UserManager.getUserManager().validateToken(tokenPatterns[0], cookieValue);
			if(validToken){
				UserManager.getUserManager().logoutUser(tokenPatterns[0]);
				return Response.ok("User "+tokenPatterns[0]+" logged out.").build();
			}else{
				return Response.serverError().entity("Logout failed").status(Response.Status.UNAUTHORIZED).build();
			}
		}catch(Exception e){
			return Response.serverError().entity("Logout failed").build();
		}
	}

}