<%@page import="com.lwr.software.reporter.DashboardConstants"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Query2Report</title>
	<link rel="stylesheet" type="text/css" href="CSS/lwr.css">
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
	<style>
	.center{
	    position:absolute;
	    display:block;
	    left:35%;
	    top:35%;
	}
	</style>
</head>
<body class="lwr_main_body">
<nav style="margin-bottom: 0px;border-radius:0px" class="navbar navbar-inverse">
	<div class="container-fluid">
		<div class="navbar-header">
			<img style="float:left;position:relative" src="images/q2r.png" alt=""></img>
			<a style="color:#f1f1f1;margin-left:10px" class="navbar-brand" href="#">
			</a>
		</div>
	</div>
</nav>
<%
	request.getSession().removeAttribute(DashboardConstants.SECURITY_CONTEXT);
	request.getSession().removeAttribute("errmsg");
	Cookie username = new Cookie("username", null);
    username.setMaxAge(0);
    response.addCookie(username);
%>
<h1>You are successfully logged out!!</h1>
<h2>Click <a href="/lwr/login">Here</a> to login!!</h2>
</body>
</html>