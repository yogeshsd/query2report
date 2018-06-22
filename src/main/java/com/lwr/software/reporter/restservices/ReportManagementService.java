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

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.lwr.software.reporter.reportmgmt.Element;
import com.lwr.software.reporter.reportmgmt.Report;
import com.lwr.software.reporter.reportmgmt.ReportManager;
import com.lwr.software.reporter.reportmgmt.RowElement;

@Path("/reports/")
public class ReportManagementService {
	
	private static Logger logger = LogManager.getLogger(ReportManagementService.class);
	
	private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	@Path("/personal/{userName}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPersonalReports(
			@PathParam("userName") String userName
			){
		if(userName == null){
			logger.error("User name cannot be null");
			return Response.serverError().entity("User name cannot be null").build();
		}	
		logger.info("Getting reports for "+userName);
		JSONObject repToReturn = new JSONObject();
		JSONArray reports = new JSONArray();
		Map<String,Map<String,Report>> userToReport = ReportManager.getReportManager().getReports(userName);
		Set<String> keys = userToReport.keySet();
		for (String key : keys) {
			Map<String, Report> value = userToReport.get(key);
			Collection<Report> reps = value.values();
			for (Report report : reps){ 
				logger.debug("Adding '"+report.getTitle()+"' report for user "+userName);
				reports.add(report);
			}
		}
		repToReturn.put("reports", reports);
		logger.info("Returning "+reports==null?0:reports.size()+" reports for user "+userName);
		return Response.ok(repToReturn).build();
	}

	@Path("/{userName}/{reportName}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getReport(
			@PathParam("reportName") String reportName,
			@PathParam("userName") String userName
			){
		if(userName == null || reportName == null){
			logger.error("User name or report name cannot be null");
			return Response.serverError().entity("User name or report name cannot be null").build();
		}
		logger.info("Getting report "+reportName+" for user "+userName);
		ObjectMapper objectMapper = new ObjectMapper();
		JSONArray reports = new JSONArray();
		JSONObject toReturn = new JSONObject();
		Map<String,Map<String,Report>> userToReport = ReportManager.getReportManager().getReports(userName);
		Set<String> keys = userToReport.keySet();
		
		boolean reportFound=false;
		for (String key : keys) {
			Map<String, Report> value = userToReport.get(key);
			Collection<Report> reps = value.values();
			for (Report report : reps){ 
				if(reportName.equalsIgnoreCase(report.getTitle())){
					reportFound=true;
					reports.add(report);
					break;
				}
			}
		}
		toReturn.put("reports", reports);
		
		if(reportFound){
			logger.info("Found report "+reportName+" for user "+userName+" in database");
		}else{
			logger.error("Not found report "+reportName+" for user "+userName+" in database");
		}
		return Response.ok(toReturn).build();
	}

	@Path("/{userName}/{reportName}/delete")
	@DELETE
	public Response deleteReport(
			@PathParam("reportName") String reportName,
			@PathParam("userName") String userName
			){
		if(userName == null || reportName == null){
			logger.error("User name or report name cannot be null");
			return Response.serverError().entity("User name or report name cannot be null").build();
		}
		logger.info("Removing report '"+reportName+"' for user "+userName);
		boolean isDeleted = ReportManager.getReportManager().deleteReport(userName,reportName);
		if(isDeleted){
			logger.info("Report '"+reportName+"' for user "+userName+" deleted");
			return Response.ok(reportName).build();
		}
		else{
			logger.error("Unable to delete '"+reportName+"' report for user "+userName);
			return Response.serverError().entity("Unable to delete '"+reportName+"' report for user "+userName).build();
		}
	}

	
	@Path("/{userName}/{reportName}/save")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response saveReport(
			@PathParam("userName") String userName,
			@PathParam("reportName") String reportName,
			Report reports[]
			)
	{
			if(userName == null || reportName == null){
				logger.error("User name or report name cannot be null");
				return Response.serverError().entity("User name or report name cannot be null").build();
			}
			logger.info("Saving report '"+reportName+"' for user "+userName);
			reportName = reportName.trim();
			boolean status = ReportManager.getReportManager().saveReport(reports,reportName,userName);
			if(status){
				logger.info("Report '"+reportName+"' for user "+userName+" saved");
				return Response.ok().build();
			}
			else{
				logger.error("Unable to save  report '"+reportName+"' for user "+userName);
				return Response.ok("Unable to save  report '"+reportName+"' for user "+userName).build();
			}
	}
	
	@Path("/{userName}/{reportName}/{elementName}/")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response executeQuery(
			@PathParam("userName") String userName,
			@PathParam("reportName") String reportName,
			@PathParam("elementName") String elementName
			){
		if(userName == null || reportName == null || elementName == null){
			logger.error("User name or report name or element name cannot be null");
			return Response.serverError().entity("User name or report name or element name cannot be null").build();			
		}
		logger.info("Getting element "+elementName+" in report "+reportName+" for user "+userName);
 		Report report = ReportManager.getReportManager().getReport(reportName,userName);
 		if(report != null){
 			logger.debug("Report "+reportName+" for user "+userName+" found");
 			List<RowElement> reportElements = report.getRows();
 			for (RowElement rowElement : reportElements) {
 				List<Element> elements = rowElement.getElements();
 				for (Element element : elements) {
 					if(element.getTitle().equalsIgnoreCase(elementName)){
 						logger.debug("Element "+elementName+" in report "+reportName+" for user "+userName+" found");
 						try {
 							element.init();
 						} catch (Exception e) {
 							e.printStackTrace();
 							logger.error("Unable to load element "+elementName+" in report "+reportName+" for user "+userName+" Error "+e.getMessage(),e);
 							return Response.serverError().entity("Unable to load element "+elementName+" in report "+reportName+" for user "+userName+" Error "+e.getMessage()).build();
 						}
 						JSONArray data = element.getJsonData();
 						logger.debug("Element "+elementName+" in report "+reportName+" for user "+userName+" initialized successfully");
 						return Response.ok(data.toJSONString()).build();
 					}
 				}
 			}
 			logger.error("Element "+elementName+" in report "+reportName+" for user "+userName+" not found");
 		}else{
 			logger.error("Report "+reportName+" for user "+userName+" not found");
 		}
 		return Response.serverError().entity("Element "+elementName+" in report "+reportName+" for user "+userName+" not found").build();
	}
	
	@Path("/element/query")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response testQuery(
			@FormParam("sqlQuery") String sqlQuery,
			@FormParam("databaseAlias") String databaseAlias,
			@FormParam("chartType") String chartType
			){
		if(sqlQuery == null || databaseAlias == null || chartType == null){
			logger.error("User name or report name or element name cannot be null");
			return Response.serverError().entity("User name or report name or element name cannot be null").build();	
		}
		logger.info("Executing sql query on "+databaseAlias+" with chart type "+chartType);
		logger.debug("Executing sql query "+sqlQuery);
		if(sqlQuery == null || sqlQuery.isEmpty()){
			Response.ok().build();
		}
		Element element = new Element(sqlQuery,chartType,databaseAlias);
		try {
			element.init();
		} catch (SQLException e) {
			logger.error("Unable to verify "+sqlQuery+" on "+databaseAlias+" with chart type "+chartType+". Error "+e.getMessage(),e);
			return Response.serverError().entity("Unable to verify "+sqlQuery+" on "+databaseAlias+" with chart type "+chartType+". Error "+e.getMessage()).build();
		}
		JSONArray data = element.getJsonData();
		logger.info("Returning sql query out from "+databaseAlias+" with chart type "+chartType);
		return Response.ok(data.toJSONString()).build();
	}
}
