<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ include file="header.jsp"%>
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
	<br>
	<br>
	<table>
			<tr>
				<td style="color:black"><h1>Version 3.1</h1></td>
			</tr>
			<tr>
				<td style="color:black"><h4>Copyright <%=DashboardConstants.PRODUCT_NAME %> @ 2016</h4></td>
			</tr>
			<tr>
				<td style="color:darkblue"><h4>Share your feedback and wish list lwrreporter@gmail.com</h4></td>
			</tr>
			<tr>
				<td style="color:darkblue"><h4>Latest version can be found at <a href="https://sourceforge.net/projects/light-weight-reporter/?source=navbar">here</a></h4></td>
			</tr>
			
	</table>
</section>
<%@ include file="footer.jsp"%>