<%@page import="java.util.List"%>
<%@page import="com.lwr.software.reporter.security.UserSecurityContext"%>
<%@page import="com.lwr.software.reporter.DashboardConstants.Role"%>
<%@page import="com.lwr.software.reporter.DashboardConstants"%>
<%@page import="com.lwr.software.reporter.admin.usermgmt.User"%>
<%@page import="com.lwr.software.reporter.admin.usermgmt.UserManager"%>
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
});
</script>
<section id="mainsection">
	<div class="topnav_adminpages">
		<h4 style="padding-top:5px">User(s) List</h4>
		<table class="admincelltable" id="admintable">
			<tr>
				<th>Display Name</th>
				<th>Username</th>
				<th>Password</th>
				<th>Role</th>
				<th>Delete</th>
			</tr>
			<%
				UserManager manager = UserManager.getUserManager();
				Set<User> users = manager.getUsers();
				for(User u : users){
					%>
					<tr onclick="selectUser(this)" id="<%=u.getUsername()%>">
						<td><%=u.getDisplayName() %></td>
						<td><%=u.getUsername() %></td>
						<td style="visbility: hidden"><%=u.getPassword() %></td>
						<td><%=u.getRole().toString() %></td>
						<td style="min-width:30px"><img src="images/sign-delete.png" onclick="deleteUser('<%=u.getUsername()%>')"></img></td>
					</tr>
					<%
				}
			%>
		</table>
	</div>
	<br>
	<hr>
	<div class="bottomnav_adminpages">
		<h4>Create/Edit User</h4>
		<table style="width:50%" class="admincellinputtable">
			<tr>
				<td>Display Name</td>
				<td>
					<input name="displayname" id="displayname" type="text"></input>
				</td>
			</tr>
			<tr>
				<td>User Name</td>
				<td>
					<input name="username" id="username" type="text"></input>
				</td>
			</tr>
			<tr>
				<td>Password</td>
				<td>
					<input name="password" id="password" type="password"></input>
				</td>
			</tr>
			<tr>
				<td>Role</td>
				<td>
					<select id="role_select">
					<%
						Role[] roles = Role.values();
						for(int i=0;i<roles.length;i++){
							%>
								<option value="<%=roles[i] %>"><%=roles[i] %></option>
							<%							
						}
					%>
					</select>
				</td>
			</tr>
		</table>
	</div>
	<br>
	<button type="button" onclick="saveUser()" class="btn btn-primary" style="background:#679BB7">Submit</button>
	<button type="reset" class="btn btn-primary" style="background:#679BB7">Reset</button>
	<br>
	<br>
	<hr>
	<br>
	<h5 id="savetext" class="alerttext"></h5>
</section>
<%@include file="../JSP/footer.jsp" %>