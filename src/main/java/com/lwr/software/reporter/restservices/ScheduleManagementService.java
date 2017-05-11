package com.lwr.software.reporter.restservices;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.CollectionType;
import org.codehaus.jackson.map.type.TypeFactory;
import org.json.simple.JSONArray;

import com.lwr.software.reporter.admin.schedmgmt.Schedule;
import com.lwr.software.reporter.admin.schedmgmt.ScheduleList;
import com.lwr.software.reporter.admin.schedmgmt.ScheduleManager;
import com.lwr.software.reporter.admin.schedmgmt.ScheduleRunAudit;

@Path("/schedules/")
public class ScheduleManagementService {
	@Path("/instances/{userName}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getScheduleHistory(@PathParam("userName") String userName){
		ObjectMapper objectMapper = new ObjectMapper();
		JSONArray ins = new JSONArray();
		Map<String, Set<ScheduleRunAudit>> schedules = ScheduleManager.getScheduleManager().getScheduleHistory(userName);
		
		if(schedules == null)
			return ins.toJSONString();
		
		Set<String> keys = schedules.keySet();
		for (String key : keys) {
			Set<ScheduleRunAudit> instances = schedules.get(key);
			for (ScheduleRunAudit instance : instances) {
				 String schedString;
				try {
					schedString = objectMapper.writeValueAsString(instance);
					ins.add(schedString);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return ins.toJSONString();
	}
	
	@Path("/{userName}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ScheduleList getSchedules(@PathParam("userName") String userName){
		ScheduleList scheduleList = new ScheduleList();
		Set<Schedule> schedules = ScheduleManager.getScheduleManager().getSchedules(userName);
		scheduleList.setScheduleList(schedules);
		return scheduleList;
	}

	@Path("/remove/{scheduleName}/{userName}")
	@POST
	public Response removeSchedule(			
			@PathParam("scheduleName") String scheduleName,
			@PathParam("userName") String userName){
		boolean status = ScheduleManager.getScheduleManager().removeSchedule(scheduleName,userName);
		if(status)
			return Response.ok("Schedule '"+scheduleName+"' Deleted.").build();
		else
			return Response.serverError().entity("Schedule '"+scheduleName+"' cannot be deleted").build();
	}
	
	@Path("/save")
	@POST
	public Response updateSchedule(@QueryParam("schedules") String scheduleJson,
			@QueryParam("userName") String userName){
    	ObjectMapper objectMapper = new ObjectMapper();
    	objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm"));
    	TypeFactory typeFactory = objectMapper.getTypeFactory();
    	CollectionType collectionType = typeFactory.constructCollectionType(Set.class, Schedule.class);
    	try {
			Set<Schedule> scheds =  objectMapper.readValue(scheduleJson, collectionType);
			Iterator<Schedule> iterator = scheds.iterator();
			while(iterator.hasNext()){
				Schedule schedule = iterator.next();
				boolean status = ScheduleManager.getScheduleManager().saveSchedule(schedule,userName);
				if(status)
					return Response.ok("Schedule '"+schedule.getScheduleName()+"' Saved.").build();
				else
					return Response.serverError().entity("Unable to save schedule '"+schedule.getScheduleName()+"'.").build();
			}
		} catch (IOException e) {
			e.printStackTrace();
			return Response.serverError().entity("Unable to save schedule."+e.getMessage()).build();
		}
    	return Response.serverError().entity("Unable to save schedule.").build();
	}
}
