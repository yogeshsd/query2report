<%@page import="com.lwr.software.reporter.DashboardConstants"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<%
	request.getSession().removeAttribute(DashboardConstants.SECURITY_CONTEXT);
	request.getSession().removeAttribute("errmsg");
%>
<h1>You are successfully logged out!!</h1>
<h2>Click <a href="/login">Here</a> to login!!</h2>
</body>
</html>