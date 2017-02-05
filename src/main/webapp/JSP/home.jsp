<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Collection"%>
<%@page import="java.util.Map"%>
<%@page import="com.lwr.software.reporter.reportmgmt.Report"%>
<%@page import="java.util.Set"%>
<%@page import="com.lwr.software.reporter.reportmgmt.ReportManager"%>
<%@page import="java.util.List"%>
<%@page import="com.lwr.software.reporter.security.UserSecurityContext"%>
<%@page import="com.lwr.software.reporter.DashboardConstants"%>
<%@page import="java.io.File"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ include file="header.jsp"%>
<script>
	$(document).ready(function(){
		showTab('public');
	})
	function search(){
		var text = $("#searchtext").val().toLowerCase();
		var rows = $(".repotlisttable");
		$(rows).each(function() {
			var link = $(this).html();
			if(link.toLowerCase().indexOf(text)>=0){
				$(this).parent().show();							
			}else{
				var description = $(this).parent().find(".reportdescr").text().toLowerCase();
				if(description.indexOf(text)>=0){
					$(this).parent().show();							
				}else{
					$(this).parent().hide();
				}
			}
		});
	}
</script>
<section id="mainsection">
</section>
<%@ include file="footer.jsp"%>