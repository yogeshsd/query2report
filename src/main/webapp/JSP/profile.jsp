<%@page import="com.lwr.software.reporter.admin.usermgmt.User"%>
<%@page import="com.lwr.software.reporter.admin.usermgmt.UserManager"%>
<%@page import="java.util.List"%>
<%@page import="com.lwr.software.reporter.security.UserSecurityContext"%>
<%@page import="com.lwr.software.reporter.DashboardConstants"%>
<%@page import="javax.swing.text.StyledEditorKit.ForegroundAction"%>
<%@page import="java.io.File"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@include file="header.jsp" %>
<section id="mainsection">
	<br>
		<h4>User Preferences</h4>
			<table style="width:50%">
				<tr>
					<td style="min-width:200px">
						User Display Name
					</td>
					<td>
						<input name="displayname" id="displayname" type="text" value="<%=user.getDisplayName()%>"></input>
					</td>
				</tr>
				<tr>
					<td>
						User Name
					</td>
					<td>
						<input name="username" id="username" type="text" value=<%=user.getUsername() %>></input>
					</td>
				</tr>
				<tr>
					<td>
						User Role
					</td>
					<td>
						<input name="role" id="role" type="text" value=<%=user.getRole() %> readonly></input>
					</td>
				</tr>
			</table>
		<hr>
		<h4>Report Preferences</h4>			
			<table style="width:50%">
				<tr>
					<td style="min-width:200px">
						Chart Preference
					</td>
					<td>
						<select id="chartoption">
							<%
								if(user.getChartType()==null || user.getChartType().equalsIgnoreCase(DashboardConstants.HTML_GOOGLE)){
									%>
										<option value="<%=DashboardConstants.HTML_GOOGLE %>" selected>Google Charts</option>
										<option value="<%=DashboardConstants.HTML_JFREE %>" >JFree Charts</option>
									<%
								}else{
									%>
										<option value="<%=DashboardConstants.HTML_GOOGLE %>" >Google Charts</option>
										<option value="<%=DashboardConstants.HTML_JFREE %>" selected>JFree Charts</option>
									<%
								}
							%>
						</select>
					</td>
				</tr>
				<tr>
					<td>
						Refresh Interval
					</td>
					<td>
						<input name="refreshInterval" id="refreshInterval" type="text" value="<%=user.getRefreshInterval()%>"></input>
					</td>
				</tr>
			</table>
		<hr>
		<input name="password" id="password" type="hidden" value=<%=user.getPassword()%>></input>
	<br>
	<button type="button" onclick="saveProfile()" class="btn btn-primary" style="background:#679BB7">Submit</button>
	<button type="reset" class="btn btn-primary" style="background:#679BB7">Reset</button>
	<h5 id="savetext" class="alerttext"></h5>
</section>
<%@include file="footer.jsp" %>