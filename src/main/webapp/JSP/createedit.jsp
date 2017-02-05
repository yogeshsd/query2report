<%@page import="com.lwr.software.reporter.reportmgmt.Report"%>
<%@page import="com.lwr.software.reporter.reportmgmt.ReportManager"%>
<%@page import="java.util.List"%>
<%@page import="com.lwr.software.reporter.security.UserSecurityContext"%>
<%@page import="com.lwr.software.reporter.DashboardConstants"%>
<%@page import="com.lwr.software.reporter.admin.connmgmt.ConnectionParams"%>
<%@page import="java.util.Set"%>
<%@page import="com.lwr.software.reporter.admin.connmgmt.ConnectionManager"%>
<%@page import="org.json.simple.JSONArray"%>
<%@page import="org.json.simple.JSONObject"%>
<%@page import="org.json.simple.parser.JSONParser"%>
<%@page import="java.io.File"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="java.io.FileReader"%>
<%@ include file="header.jsp"%>
<script type="text/javascript">
$(document).ready(function() {
	$("#savepersonalmenu").show();
	$("#savepublicmenu").show();
});
</script>
	<%
		Set<ConnectionParams> params = ConnectionManager.getConnectionManager().getConnectionParams();
		StringBuffer jsonData = new StringBuffer();
		JSONArray jsData=null;
		String description="";
		String reportName = name;
		String userName = (String)request.getParameter("userName");
		if(name!=null){
			BufferedReader buffReader;
			if(userName.equalsIgnoreCase(DashboardConstants.PUBLIC_USER))
				buffReader = new BufferedReader(new FileReader(DashboardConstants.PUBLIC_REPORT_DIR+File.separatorChar+reportName));
			else
				buffReader = new BufferedReader(new FileReader(DashboardConstants.PRIVATE_REPORT_DIR+File.separatorChar+userName+File.separatorChar+reportName));
			while(true){
				String line = buffReader.readLine();
				if(line == null)
					break;
				jsonData.append(line);
			}
			System.out.println(jsonData.toString());
			JSONParser parser = new JSONParser();
			jsData = (JSONArray)parser.parse(jsonData.toString());
			Report report = ReportManager.getReportManager().getReport(reportName,userName);
			description = report.getDescription();
		}else{
			name="";
		}
	%>
	
	<script type="text/javascript">
	function buildCell(index,rowIndex, columnIndex, title,sql){
		var id = index+"_"+rowIndex+"_"+columnIndex;
		html="<div style=\"background:white;border-radius:5px;border:1px solid black;padding:5px;margin:5px\" >";
		html=html+"<label>Title</label><br>";
		html=html+"<input class=\"input_withbottomborder\" type=\"text\" id=\""+id+"_title\"/>";
		html=html+"<label>Query</label><br>";
		html=html+"<input class=\"input_withbottomborder\"  id=\""+id+"_sql\" name=\"query\"/>";
		html=html+"<label>Chart Type</label>";
		html=html+"				<div>";
		html=html+"					<select id=\""+id+"_select\">";
		html=html+"						<option value=\"pie\">Pie Chart</option>";
		html=html+"						<option value=\"bar\">Bar Chart</option>";
		html=html+"						<option value=\"barstack\">Bar Stack Chart</option>";
		html=html+"						<option value=\"line\">Line Chart</option>";
		html=html+"						<option value=\"column\">Column Chart</option>";
		html=html+"						<option value=\"columnstack\">Column Stack Chart</option>";
		html=html+"						<option value=\"table\">Table Chart</option>";
		html=html+"					</select>";
		html=html+"				</div>";
		html=html+"<label>Connection</label><br>";
		html=html+"				<div>";
		html=html+"				<select id=\""+id+"_conn\">";
		html=html+"					<option value=\"default\">default</option>";
		<%
			for(ConnectionParams param : params){
				String alias = param.getAlias();
				%>
				html=html+"					<option value=\"<%=alias%>\"><%=alias%></option>";
				<%
			}
		%>
		html=html+"					</select>";
		html=html+"				</div>";
		html=html+"				<br>";
		html=html+"				<button onclick=\"runQuery("+index+","+rowIndex+","+columnIndex+")\" type=\"button\" class=\"btn btn-primary btn-xs\">Test Query</button>";			
		html=html+"</div>";
		return html;
	}
	</script>
	<script type="text/javascript">
			var jsData = <%=jsonData%>;
			$("#mainsection").ready(function(){
				buildPage(jsData);
			});
	</script>
	<section id="mainsection">
		<label style="font-size:12px">Report Name</label> 
		<input class="input_withbottomborder" type="text" id="dashname" name="dash-name" value="<%=name%>"></input>
		<label style="font-size:12px">Report Description</label>
		<input class="input_withbottomborder" type="text" id="description" name="description" value="<%=description %>"></input>
		<table id="mytable">
				<tr id="row_0" class="outerrow">
					<td>
						<table id="innertable_0" class="innertable_withborder">
							<tr class="cellrow">
								<td class="cellcolumn" id="0_0_0_cell">
									<img src="images/sign-edit.png" alt="Edit Report Cell" title="Edit Report Cell" onclick="populateCell(0,0,0,'','','')">
								</td>
							</tr>
						</table>
					</td>
				<td>
					<img align="middle" alt="Add Report Column" title="Add Report Column" src="images/sign-add.png" onclick="addColumn(0)"></img>
				</td>
				</tr>
			</table>
			<div style="position:relative"><img align="right" alt="Add Report Row" title="Add Report Row" src="images/sign-add.png" onclick="addRow()"></img>
			<img align="right" alt="Delete Report Column" title="Delete Report Column" src="images/sign-remove.png" onclick="removeRow()"></img></div>
		</section>
<%@ include file="footer.jsp"%>