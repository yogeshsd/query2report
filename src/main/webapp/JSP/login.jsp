<%@page import="com.lwr.software.reporter.DashboardConstants"%>
<%@page import="javax.swing.text.StyledEditorKit.ForegroundAction"%>
<%@page import="java.io.File"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE>
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
<script type="text/javascript" src="JS/angular.js"></script>
<script type="text/javascript" src="JS/bootstrap.min.js"></script>
<link rel="shortcut icon" href="images/lwr_logo.png">
</head>
<body class="lwr_main_body">
<table>
	<tr>
		<td width="5%">
			<img src="images/lwr_logo.png" alt="" height="60">
		</td>
		<td style="vertical-align:middle;padding-left:50px" width="92%">
			<font size="6" color="white"><%=DashboardConstants.PRODUCT_NAME %></font>
		</td>
		<td width="3%" style="vertical-align:middle">
		<%
			Object loginName = request.getSession().getAttribute(DashboardConstants.SECURITY_CONTEXT);
			if(loginName == null){
				loginName = "";
			}
			
		%>
		</td>
	</tr>
</table>
<form action="doLogin" method="post">
	<table style="table-layout : fixed;height:80%;width:100%" >
		<tr>
			<td></td>
			<td>
			</td>
			<td>
				<table class="login_table">
					<tr>
						<td style="color:black">User Name:</td>
						<td><input id="username" name="username" type="text"></input></td>
					</tr>
					<tr>
						<td style="color:black">Password:</td>
						<td><input id="password" name="password" type="password"></input></td>
					</tr>
					<tr></tr>
					<%
					String errmsg = (String)request.getSession().getAttribute("errmsg");
					if(errmsg == null || errmsg.isEmpty()){
						%><tr></tr><%
					}else{
						%><tr><%=errmsg%></tr><%
					}
					%>
					<tr></tr>
					<tr>
						<td><button name="login" type="submit">Login</button></td>
						<td><button name="reset">Reset</button></td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</form>
<%@ include file="footer.jsp" %>