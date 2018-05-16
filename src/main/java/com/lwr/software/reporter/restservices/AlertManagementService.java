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

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.simple.JSONObject;

import com.lwr.software.reporter.admin.alertmgmt.Alert;
import com.lwr.software.reporter.admin.alertmgmt.Alert.Status;
import com.lwr.software.reporter.admin.connmgmt.ConnectionManager;
import com.lwr.software.reporter.admin.connmgmt.ConnectionParams;
import com.lwr.software.reporter.admin.drivermgmt.DriverManager;
import com.lwr.software.reporter.admin.drivermgmt.DriverParams;

@Path("/alerts/")
public class AlertManagementService {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getConnectionList(){
		System.out.println("AlertManagementService : get");
		Set<Alert> alerts = new HashSet<Alert>();
		
		Set<DriverParams> drivers = DriverManager.getDriverManager().getDrivers();
		if(drivers == null || drivers.isEmpty()){
			Alert alert = new Alert();
			alert.setMessage("No JDBC drivers are defined.");
			alert.setAlertStatus(Status.ERROR);
			alert.setResolutionMessage("Define a new driver <a href=\"#/drivermgmt\">here</a>.");
			alerts.add(alert);
		}
		
		Set<ConnectionParams> connections = ConnectionManager.getConnectionManager().getConnectionParams();
		if(connections == null || connections.isEmpty()){
			Alert alert = new Alert();
			alert.setMessage("No JDBC connections are defined.");
			alert.setAlertStatus(Status.ERROR);
			alert.setResolutionMessage("Define a new connection <a href=\"#/connmgmt\">here</a>.");
			alerts.add(alert);
		}
		
		JSONObject alertList = new JSONObject();
		alertList.put("alerts", alerts);
		return Response.ok(alertList).build();
	}
}
