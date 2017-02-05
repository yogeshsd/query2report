package com.lwr.software.reporter.restservices;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONArray;

import com.lwr.software.reporter.DashboardConstants;
import com.lwr.software.reporter.reportmgmt.Element;
import com.lwr.software.reporter.reportmgmt.Report;
import com.lwr.software.reporter.reportmgmt.ReportManager;
import com.lwr.software.reporter.reportmgmt.RowElement;

@Path("/reports/")
public class ReportManagementService {
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getReportNames(
			@QueryParam("userName") String userName
			){
		ObjectMapper objectMapper = new ObjectMapper();
		JSONArray reports = new JSONArray();
		Map<String,Map<String,Report>> userToReport = ReportManager.getReportManager().getReports(userName);
		Set<String> keys = userToReport.keySet();
		for (String key : keys) {
			Map<String, Report> value = userToReport.get(key);
			Collection<Report> reps = value.values();
			for (Report report : reps){ 
				try {
					 String reportString =  objectMapper.writeValueAsString(report);
					 reports.add(reportString);
				} catch (Exception e) {
					return Response.serverError().entity("Unable to load reports. Error "+e.getMessage()).build();
				}
			}
		}
		return Response.ok(reports.toJSONString()).build();
	}

	@Path("/report")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getReport(
			@QueryParam("reportName") String reportName,
			@QueryParam("userName") String userName
			){
		ObjectMapper objectMapper = new ObjectMapper();
		JSONArray reports = new JSONArray();
		Map<String,Map<String,Report>> userToReport = ReportManager.getReportManager().getReports(userName);
		Set<String> keys = userToReport.keySet();
		for (String key : keys) {
			Map<String, Report> value = userToReport.get(key);
			Collection<Report> reps = value.values();
			for (Report report : reps){ 
				try {
					if(reportName.equalsIgnoreCase(report.getTitle())){
						String reportString =  objectMapper.writeValueAsString(report);
						reports.add(reportString);
					}
				} catch (Exception e) {
					return Response.serverError().entity("Unable to load report '"+reportName+"'. Error "+e.getMessage()).build();
				}
			}
		}
		return Response.ok(reports.toJSONString()).build();
	}

	@Path("/report/delete")
	@GET
	public Response deleteReport(
			@QueryParam("reportName") String reportName,
			@QueryParam("userName") String userName
			){
		boolean isDeleted = ReportManager.getReportManager().deleteReport(userName,reportName);
		if(isDeleted)
			return Response.ok("Successfully Deleted report "+userName+":"+reportName).build();
		else
			return Response.serverError().build();
	}

	
	@Path("/save")
	@POST
	public Response saveReport(
			@FormParam("components") String components,
			@FormParam("dashboardname") String rName)
	{
			String patterns[] = rName.split(":");
			String userName = DashboardConstants.PUBLIC_USER;
			String reportName = rName;
			if(patterns.length==2){
				userName = patterns[0];
				reportName = patterns[1];
			}
			boolean status = ReportManager.getReportManager().saveReport(components,reportName,userName);
			if(status)
				return Response.ok("Report Saved.").build();
			else
				return Response.ok("Unable to save Report.").build();
	}
	
	@Path("/element/")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response executeQuery(
			@QueryParam("reportName") String reportName,
			@QueryParam("elementName") String elementName,
			@QueryParam("userName") String userName
			){
 		Report report = ReportManager.getReportManager().getReport(reportName,userName);
		System.out.println("Report Name = "+reportName+", Element Name = "+elementName);
		List<RowElement> reportElements = report.getRows();
		for (RowElement rowElement : reportElements) {
			List<Element> elements = rowElement.getElements();
			for (Element element : elements) {
				if(element.getTitle().equalsIgnoreCase(elementName)){
					try {
						element.init();
					} catch (SQLException e) {
						e.printStackTrace();
						return Response.serverError().entity("Unable to load element "+elementName+". Error "+e.getMessage()).build();
					}
					JSONArray data = element.getJsonData();
					return Response.ok(data.toJSONString()).build();
				}
			}
		}
		return Response.ok("[]").build();
	}
	
	@Path("/element/test")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response testQuery(
			@QueryParam("sqlQuery") String sqlQuery,
			@QueryParam("databaseAlias") String databaseAlias,
			@QueryParam("chartType") String chartType
			){
		Element element = new Element(sqlQuery,chartType,databaseAlias);
		try {
			element.init();
		} catch (SQLException e) {
			e.printStackTrace();
			return Response.serverError().entity("Unable to verify "+sqlQuery+". Error "+e.getMessage()).build();
		}
		JSONArray data = element.getJsonData();
		return Response.ok(data.toJSONString()).build();
	}
}
