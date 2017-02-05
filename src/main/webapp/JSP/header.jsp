<%@page import="com.lwr.software.reporter.admin.usermgmt.UserManager"%>
<%@page import="com.lwr.software.reporter.admin.usermgmt.User"%>
<%@page import="java.util.List"%>
<%@page import="com.lwr.software.reporter.security.UserSecurityContext"%>
<%@page import="com.lwr.software.reporter.DashboardConstants"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title><%=DashboardConstants.PRODUCT_NAME %></title>
<link rel="stylesheet" type="text/css" href="CSS/lwr-dash.css">
<link rel="stylesheet" href="CSS/bootstrap.min.css">
<link rel="stylesheet" href="CSS/bootstrap-theme.min.css">
<script type="text/javascript" src="JS/jquery.min.js"></script>
<script type="text/javascript" src="JS/lwr.js"></script>
<script type="text/javascript" src="JS/connection.js"></script>
<script type="text/javascript" src="JS/user.js"></script>
<script type="text/javascript" src="JS/schedule.js"></script>
<script type="text/javascript" src="JS/angular.js"></script>
<script type="text/javascript" src="JS/bootstrap.min.js"></script>
<script type="text/javascript" src="JS/bootstrap-typeahead.js"></script>
<script type="text/javascript" src="JS/jspdf.min.js"></script>
<script type="text/javascript" src="JS/html2canvas.js"></script>
<link rel="shortcut icon" href="images/lwr_logo.png">
</head>
<body class="no-js">
<script type="text/javascript" src="https://www.google.com/jsapi"></script>
<script type="text/javascript">
	google.load('visualization','1', {'packages':['corechart,table']});
</script>
<header>
	<table>
		<tr>
			<td width="5%">
				<img src="images/lwr_logo.png" alt="" height="60">
			</td>
			<td style="vertical-align:middle;padding-left:50px" width="92%">
				<font size="6" color="white"><%=DashboardConstants.PRODUCT_NAME %></font>
			</td>
			<%
				Object loginName = request.getSession().getAttribute(DashboardConstants.SECURITY_CONTEXT);
				if(loginName == null){
					loginName = "";
				}
				
			%>
		</tr>
	</table>
</header>
<nav>
<div>
	<%
		String name = (String)request.getParameter("name");
		UserSecurityContext context = (UserSecurityContext) request.getSession().getAttribute(DashboardConstants.SECURITY_CONTEXT);
		User user = null;
		if(context != null){
			user = UserManager.getUserManager().getUser(context.getUserName());
			%>
				<table id="menuheader">
					<tr>
						<th></th>
						<th></th>
						<th></th>
						<th></th>
						<th></th>
						<th><a class="menu" id="newmenu" href="createedit"><img alt="New Report" title="New Report" src="images/new.png" onmouseenter="focusMenu(this)" onmouseleave="unfocusMenu(this)" onmouseenter="focusMenu(this)" onmouseleave="unfocusMenu(this)"></img></a></th>
						<th><a class="menu" id="editmenu" style="display:none" href="#"><img alt="Edit Report" title="Edit Report" src="images/edit.png" onclick="javascript:editReport()" onmouseenter="focusMenu(this)" onmouseleave="unfocusMenu(this)"></img></a></th>
						<th><a class="menu" id="savepersonalmenu" style="display:none" href="javascript:save('personal')"><img alt="Save Personal Folders" title="Save Personal Folders" src="images/save-personal.png" onmouseenter="focusMenu(this)" onmouseleave="unfocusMenu(this)"></img></a></th>
						<th><a class="menu"id="savepublicmenu" style="display:none" href="javascript:save('public')"><img alt="Save Public Folders" title="Save Public Folders" src="images/save-public.png" onmouseenter="focusMenu(this)" onmouseleave="unfocusMenu(this)"></img></a></th>
						<th><a class="menu" id="pdfmenu" style="display:none" href="javascript:exportPDF()"><img alt="Export Report as PDF" title="Export Report as PDF" src="images/pdf.png" onmouseenter="focusMenu(this)" onmouseleave="unfocusMenu(this)"></img></a></th>
						<th><a class="menu" id="csvmenu" style="display:none" href="javascript:exportCSV()"><img alt="Export Report as CSV" title="Export Report as CSV" src="images/csv.png" onmouseenter="focusMenu(this)" onmouseleave="unfocusMenu(this)"></img></a></th>
						<th><a class="menu" id="htmlmenu" style="display:none" href="javascript:exportHTML()"><img alt="Export Report as HTML" title="Export Report as HTML" src="images/html.png" onmouseenter="focusMenu(this)" onmouseleave="unfocusMenu(this)"></img></a></th>
						<th></th>
						<th></th>
						<th><a class="menu" id="usermgmtmenu" href="usermgmt"><img alt="User Management" title="User Management" src="images/usermgmt.png" onmouseenter="focusMenu(this)" onmouseleave="unfocusMenu(this)"></img></a></th>
						<th><a class="menu" id="connmgmtmenu" href="connmgmt"><img alt="Connection Management" title="Connection Management"  src="images/connmgmt.png" onmouseenter="focusMenu(this)" onmouseleave="unfocusMenu(this)"></img></a></th>
						<th><a class="menu" id="schedmenu" href="schedmgmt"><img alt="Schedule Management" title="Schedule Management"  src="images/schedmgmt.png" onmouseenter="focusMenu(this)" onmouseleave="unfocusMenu(this)"></img></a></th>
						<th><a class="menu" id="previewmenu" href="chartsample"><img alt="Example Reports" title="Example Reports"  src="images/preview.png" onmouseenter="focusMenu(this)" onmouseleave="unfocusMenu(this)"></img></a></th>
						<th><a class="menu" id="getstartedmenu" href="getstarted"><img alt="Getting Started" title="Getting Started"  src="images/getstarted.png" onmouseenter="focusMenu(this)" onmouseleave="unfocusMenu(this)"></img></a></th>
						<th><a class="menu" id="aboutmenu" href="about"><img alt="About" title="About"  src="images/about.png" onmouseenter="focusMenu(this)" onmouseleave="unfocusMenu(this)"></img></a></th>
					<%
						if(user != null){
							%>
								<th><a class="menu" id="settings" href="profile"><img alt="Settings" title="Settings"  src="images/settings.png" onmouseenter="focusMenu(this)" onmouseleave="unfocusMenu(this)"></img></a></th>
								<th><a class="menu" id="logout" href="logout"><img alt="Logout" title="Logout"  src="images/logout.png" onmouseenter="focusMenu(this)" onmouseleave="unfocusMenu(this)"></img></a></th>
							<%		
						}

					%>
					</tr>
				</table>
			<%
			}
		%>
		<input id="usernamehidden" type="hidden" name="<%=user.getUsername()%>" value="<%=user.getUsername()%>"></input>
</div>
</nav>
<aside>
	<div class="list-group">
		<a id="public_li_id" href="javascript:showTab('public');" class="list-group-item active">Public Reports</a>
		<a id="personal_li_id" href="javascript:showTab('personal');" class="list-group-item">Personal Reports</a>
		<a id="schedule_li_id" href="javascript:showTab('schedule');" class="list-group-item">Scheduled Reports</a>
	</div>
</aside>
