<!--   
	Query2Report Copyright (C) 2018  Yogesh Deshpande
	
	This file is part of Query2Report.
	
	Query2Report is free software: you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation, either version 3 of the License, or
	(at your option) any later version.
	
	Query2Report is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.
	
	You should have received a copy of the GNU General Public License
	along with Query2Report.  If not, see <http://www.gnu.org/licenses/>.
-->

<%@page import="javax.swing.text.StyledEditorKit.ForegroundAction"%>
<%@page import="java.io.File"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Query2Report</title>
	<link rel="stylesheet" type="text/css" href="CSS/lwr.css">
	<script type="text/javascript" src="JS/jquery.min.js"></script>
	<link rel="stylesheet" type="text/css" href="CSS/bootstrap.min.css">
	<script type="text/javascript" src="JS/bootstrap.min.js"></script>
	<style>
	.center{
	    position:absolute;
	    display:block;
	    left:40%;
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
	<div class="container-fluid">
		<div class="row" style="height:92vh">
			<div style="padding:5px;width:10%;height:100%;float: left;">
				<div style="overflow:auto;height:50%;border: 1px solid #f1f1f1;background-color:#f1f1f1;box-shadow: 0 2px 5px 0 rgba(0,0,0,0.16), 0 2px 10px 0 rgba(0,0,0,0.12);">
				</div>
				<div style="overflow:auto;height:50%;border: 1px solid #f1f1f1;background-color:#f1f1f1;box-shadow: 0 2px 5px 0 rgba(0,0,0,0.16), 0 2px 10px 0 rgba(0,0,0,0.12);">
					<ul class="nav nav-pills nav-stacked">
						<li><span style="font-size:14px;font-weight:bold;vertical-align: bottom;padding-right:50px">Video Guides  </span></li>
						<li>
							<a class="topbar" target="_blank" href="https://youtu.be/p9jcBZJ1-SU"><img style="height:20px;width:20px;float:left" src="./images/youtube.png"><span style="margin-left:2px"> Concepts Guide</span></a>
						</li>
						<li>
							<a class="topbar" target="_blank" href="https://youtu.be/gxlEGq5iSm8"><img style="height:20px;width:20px;float:left" src="./images/youtube.png"/><span  style="margin-left:2px"> Report Showcase</span></a>
						</li>
						<li>
							<a class="topbar" target="_blank" href="https://youtu.be/vyU7BUE5rbs"><img style="height:20px;width:20px;float:left" src="./images/youtube.png"/><span style="margin-left:2px"> Getting Started</span></a>
						</li>
						<li>
							<a class="topbar" target="_blank" href="https://youtu.be/MZm6rhf2_Ts"><img style="height:20px;width:20px;float:left" src="./images/youtube.png"/><span style="margin-left:2px"> Building Report</span></a>
						</li>
					</ul>
				</div>
			</div>
			<div style="auto;padding:5px;width:90%;height:100%;float: left;">
				<div class="center">
					<form action="doLogin" method="post">
						<div style="width:100%;;padding:50px;background-color: #f1f1f1;border-radius:5px;border-style:outset;">
							<table>
								<tr style="padding:10px">
									<td style="color:black"><span class="glyphicon glyphicon-user"></span><span> User Name </span></td>
									<td><input id="username" class="form-control" type="text" name="username"></td>
								</tr>
								<tr style="padding:10px">
									<td style="color:black"><span class="glyphicon glyphicon-lock"></span><span> Password </span></td>
									<td><input id="password" class="form-control" type="password" name="password"></td>
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
								<tr></tr>
								<tr>
									<td style="padding:10px">
									</td>
									<td style="padding:10px">
										<button id="loginButton" class="btn btn-primary" name="login" type="submit">Login</button>
										<button id="resetButton" class="btn btn-primary" name="reset">Reset</button>
									</td>
								</tr>
							</table>
						</div>
					</form>
				</div>
			</div>
		</div>
		<div class="row" style="position: absolute;bottom: 0; width: 100%;">
			<div class="col-md-7" style="text-align:left;border: 1px solid #f1f1f1;background-color:#f1f1f1;">
					<span style="font-size:14px;font-weight:bold;vertical-align: bottom;padding-right:30px">Rate Q2R On </span>
			  			<img style="height:20px;width:20px;" src="./images/github.png">
		  				<a href="https://github.com/yogeshsd/lwr-maven" target="_blank" style="margin-right:20px;margin-left:10px">Github</a>
						<img style="height:20px;width:20px;" src="./images/sourceforge.png"></img>
						<a href="https://sourceforge.net/projects/query2report/" target="_blank" style="margin-left:10px">Sourceforge</a>
			</div>
			<div class="col-md-5" style="text-align:right;border: 1px solid #f1f1f1;background-color:#f1f1f1;">
					<a data-ng-click="showDialog($event, 'html/aboutDialog.html')")><span style="font-size:14px;font-weight:bold;vertical-align: bottom;"> Query2Report Copyright (C) 2018 </span></a>
			</div>
		</div>
	</div>
</body>