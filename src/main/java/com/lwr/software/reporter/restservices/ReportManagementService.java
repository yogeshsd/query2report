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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.lwr.software.reporter.DashboardConstants;
import com.lwr.software.reporter.reportmgmt.Element;
import com.lwr.software.reporter.reportmgmt.Report;
import com.lwr.software.reporter.reportmgmt.ReportManager;
import com.lwr.software.reporter.reportmgmt.RowElement;

@Path("/reports/")
public class ReportManagementService {
	
	private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	@Path("/personal/{userName}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPersonalReports(
			@PathParam("userName") String userName
			){
		JSONObject repToReturn = new JSONObject();
		JSONArray reports = new JSONArray();
		Map<String,Map<String,Report>> userToReport = ReportManager.getReportManager().getReports(userName);
		Set<String> keys = userToReport.keySet();
		for (String key : keys) {
			Map<String, Report> value = userToReport.get(key);
			Collection<Report> reps = value.values();
			for (Report report : reps){ 
				try {
					 reports.add(report);
				} catch (Exception e) {
					return Response.serverError().entity("Unable to load reports. Error "+e.getMessage()).build();
				}
			}
		}
		repToReturn.put("reports", reports);
		return Response.ok(repToReturn).build();
	}

	@Path("/{userName}/{reportName}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getReport(
			@PathParam("reportName") String reportName,
			@PathParam("userName") String userName
			){
		ObjectMapper objectMapper = new ObjectMapper();
		JSONArray reports = new JSONArray();
		JSONObject toReturn = new JSONObject();
		Map<String,Map<String,Report>> userToReport = ReportManager.getReportManager().getReports(userName);
		Set<String> keys = userToReport.keySet();
		for (String key : keys) {
			Map<String, Report> value = userToReport.get(key);
			Collection<Report> reps = value.values();
			for (Report report : reps){ 
				try {
					if(reportName.equalsIgnoreCase(report.getTitle())){
						reports.add(report);
						break;
					}
				} catch (Exception e) {
					return Response.serverError().entity("Unable to load report '"+reportName+"'. Error "+e.getMessage()).build();
				}
			}
		}
		toReturn.put("reports", reports);
		return Response.ok(toReturn).build();
	}

	@Path("/{userName}/{reportName}/delete")
	@DELETE
	public Response deleteReport(
			@PathParam("reportName") String reportName,
			@PathParam("userName") String userName
			){
		boolean isDeleted = ReportManager.getReportManager().deleteReport(userName,reportName);
		if(isDeleted)
			return Response.ok(reportName).build();
		else
			return Response.serverError().build();
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
			reportName = reportName.trim();
			boolean status = ReportManager.getReportManager().saveReport(reports,reportName,userName);
			if(status)
				return Response.ok("Report Saved.").build();
			else
				return Response.ok("Unable to save Report.").build();
	}
	
	@Path("/{userName}/{reportName}/{elementName}/")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response executeQuery(
			@PathParam("userName") String userName,
			@PathParam("reportName") String reportName,
			@PathParam("elementName") String elementName
			){
 		Report report = ReportManager.getReportManager().getReport(reportName,userName);
		System.out.println(formatter.format(System.currentTimeMillis())+"\t Report Name = "+reportName+", Element Name = "+elementName);
		List<RowElement> reportElements = report.getRows();
		for (RowElement rowElement : reportElements) {
			List<Element> elements = rowElement.getElements();
			for (Element element : elements) {
				if(element.getTitle().equalsIgnoreCase(elementName)){
					try {
						element.init();
					} catch (Exception e) {
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
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response testQuery(
			@FormParam("sqlQuery") String sqlQuery,
			@FormParam("databaseAlias") String databaseAlias,
			@FormParam("chartType") String chartType
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
