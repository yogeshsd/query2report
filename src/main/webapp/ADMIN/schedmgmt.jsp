<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.lwr.software.reporter.DashboardConstants.Recurrence"%>
<%@page import="com.lwr.software.reporter.DashboardConstants.OutputFormat"%>
<%@page import="com.lwr.software.reporter.DashboardConstants.Destination"%>
<%@page import="com.lwr.software.reporter.admin.schedmgmt.Schedule"%>
<%@page import="com.lwr.software.reporter.admin.schedmgmt.ScheduleManager"%>
<%@page import="java.util.List"%>
<%@page import="com.lwr.software.reporter.security.UserSecurityContext"%>
<%@page import="com.lwr.software.reporter.DashboardConstants.Role"%>
<%@page import="com.lwr.software.reporter.DashboardConstants"%>
<%@page import="java.util.Set"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@include file="../JSP/header.jsp" %>
<script type="text/javascript">
	$(document).ready(function() {
		$("#editmenu").hide();
		$("#savepersonalmenu").hide();
		$("#savepublicmenu").hide();
		$("#pdfmenu").hide();
		$("#csvmenu").hide();
		$("#htmlmenu").hide();
	  $('input.typeahead').typeahead({
	    source: function (query, process) {
		var username = "public";
		username=$("#usernamehidden").val();
			$.ajax({
			  url: 'rest/reports',
			  type: 'GET',
			  dataType: 'JSON',
			  data: {"userName":username},
			  success: function(data) {
					var htmlData=[];
					for (i = 0; i < data.length; i++) { 
						var report = JSON.parse(data[i]);
						htmlData[i]=report.title;
					}
					process(htmlData);
			  }
			});
		 }
	  });
	});
</script>
<section id="mainsection">
	<div class="topnav_adminpages">
		<h4 style="padding-top:5px">Schedule(s) List</h4>
		<table class="admincelltable" id="admintable">
			<tr>
				<th>Schedule Name</th>
				<th>Report Name</th>
				<th>Output Format</th>
				<th>Destination</th>
				<th>Start Date</th>
				<th>Recurrence</th>
				<th>Interval</th>
				<th style="display:none">Folder Name</th>
				<th style="display:none">SMTP Host Name</th>
				<th style="display:none">SMTP Port</th>
				<th style="display:none">Receiver(s) Email</th>
				<th style="display:none">Sender(s) Email</th>
				<th style="width:50px">Delete</th>
			</tr>
			<%
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				Set<Schedule> schedules = ScheduleManager.getScheduleManager().getSchedules(user.getUsername());
				if(schedules != null){
					for(Schedule s : schedules){
						%>
						<tr onclick="selectSchedule(this)" id="<%=s.getScheduleName()%>">
							<td><%=s.getScheduleName() %></td>
							<td><%=s.getReportName() %></td>
							<td><%=s.getFormat() %></td>
							<td><%=s.getDestination() %></td>
							<td><%=format.format(s.getStartDate())%></td>
							<td><%=s.getRecurrence() %></td>
							<td><%=s.getInterval()%></td>
							<td style="display:none"><%=s.getFolderName()%></td>
							<td style="display:none"><%=s.getSmtpHost()%></td>
							<td style="display:none"><%=s.getSmtpPort()%></td>
							<td style="display:none"><%=s.getReceiverEmail()%></td>
							<td style="display:none"><%=s.getSenderEmail()%></td>
							<td style="min-width:30px"><img src="images/sign-delete.png" onclick="deleteSchedule('<%=s.getScheduleName()%>')"></img></td>
						</tr>
						<%
					}
				}
			%>
		</table>
	</div>
	<hr>
	<div class="bottomnav_adminpages">
		<h4>Create/Edit Schedule</h4>
		<table style="width:50%" class="admincellinputtable">
			<tr>
				<td style="min-width:200px">Schedule Name</td>
				<td>
					<input name="schedulename" id="schedulename" type="text"></input>
				</td>
			</tr>
			<tr>
				<td>Report Name</td>
				<td>
					<input name="reportname" id="reportname" type="text" class="typeahead tt-query" autocomplete="off" spellcheck="false">
				</td>
			</tr>
			<tr>
				<td>Output Format</td>
				<td>
					<select id="format_select">
					<%
						OutputFormat[] formats = OutputFormat.values();
						for(int i=0;i<formats.length;i++){
							%>
								<option value="<%=formats[i] %>"><%=formats[i] %></option>
							<%							
						}
					%>
					</select>
				</td>
			</tr>
		</table>
		<hr>
		<table>
			<tr>
			<td><h4>Recurrence</h4></td>
			<%
				Recurrence[] frequencies = Recurrence.values();
				for(int i=0;i<frequencies.length;i++){
					if(frequencies[i].equals(Recurrence.HOURLY)){
						%>
						<td style="min-width:50px">
							<input style="margin:0px"type="radio" id="<%=frequencies[i] %>"  onchange="selectRecurrence('<%=frequencies[i]%>')" checked>
						</td>
						<td><%=frequencies[i] %></td>
						<%							
					}else{
						%>
						<td style="min-width:50px">
							<input style="margin:0px"type="radio" id="<%=frequencies[i] %>" onchange="selectRecurrence('<%=frequencies[i]%>')">
						</td>
						<td><%=frequencies[i] %></td>
						<%							
					}
				}
			%>
			</tr>
		</table>
		Start Time	<input style="width:200px" type="datetime-local" id="start_date">
			<div id="hourly_div" style="display:block;padding-top:20px;padding-bottom:20px">
			<table>
				<tr>
					<td style="padding-right:5px">Every</td>
					<td style="padding:5px"><input style="width:40px;height:25px" type="text" id="hourly_frequency"></input><td>
					<td style="padding:5px">Hour(s)</td>
			</table>
			</div>
			<div id="daily_div" style="display:none;padding-top:20px;padding-bottom:20px">
			<table>
				<tr>
				<td style="padding-right:5px">Every</td> 
				<td style="padding:5px"><input style="width:40px;height:25px" type="text" id="daily_frequency"></input><td>
				<td style="padding:5px">Day(s)</td>
			</table>
			</div>
			<div id="monthly_div" style="display:none;padding-top:20px;padding-bottom:20px">
			</div>
		<hr>
		<table>
			<tr>
			<td><h4>Destination</h4></td>
			<%
				Destination[] destinations = Destination.values();
				for(int i=0;i<destinations.length;i++){
					if(destinations[i].equals(Destination.EMAIL)){
						%>
						<td style="min-width:50px">
							<input style="margin:0px" type="radio" id="<%=destinations[i] %>" value="<%=destinations[i] %>" onchange="selectDestination('<%=destinations[i]%>')" checked>
						</td>
						<td><%=destinations[i]%></td>
						<%							
					}else{
						%>
						<td style="min-width:50px">
							<input style="margin:0px" type="radio" id="<%=destinations[i] %>" value="<%=destinations[i] %>" onchange="selectDestination('<%=destinations[i]%>')">
						</td>
						<td><%=destinations[i]%></td>
						<%							
					}
				}
			%>
			</tr>
		</table>
		<div id="smtpconfig" style="display:block">
			<table style="width:50%" class="admincellinputtable">
				<tr>
					<td style="min-width:200px">SMTP Host Name</td>
					<td>
						<input name="smtphost" id="smtphost" type="text"></input>
					</td>
				</tr>
				<tr>
					<td>SMTP Port</td>
					<td>
						<input name="smtpport" id="smtpport" type="text"></input>
					</td>
				</tr>
				<tr>
					<td>Sender Email Address</td>
					<td>
						<input name="senderemail" id="senderemail" type="text"></input>
					</td>
				</tr>
				<tr>
					<td>Receivers Email Address(s)</td>
					<td>
						<input name="receiveremail" id="receiveremail" type="text"></input>
					</td>
				</tr>
			</table>
		</div>
	</div>
	<hr>
	<button type="button" onclick="saveSchedule()" class="btn btn-primary" style="background:#679BB7">Submit</button>
	<button type="reset" class="btn btn-primary" style="background:#679BB7">Reset</button>
	<hr>
	<h5 id="savetext" class="alerttext"></h5>
</section>
<%@include file="../JSP/footer.jsp" %>