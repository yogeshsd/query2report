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
