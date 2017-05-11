function addRow() {
	var rowIndex = $("#mytable .outerrow").last().index()+1;
	var rowId = rowIndex+"_0_0_cell";
	var row = "<tr class=\"outerrow\" id=\"row_"+rowIndex+"\"><td><table id=\"innertable_"+rowIndex+"\" class=\"innertable_withborder\"><tr class=\"cellrow\"><td class=\"cellcolumn\" id=\""+rowId+"\"><img src=\"images/sign-edit.png\" alt=\"Edit Report Cell\" onclick=\"populateCell('"+rowIndex+"_0_0','','','')\"></td></tr></table></td><td><img src=\"images/sign-add.png\" alt=\"Add Report Column\" title=\"Add Report Column\" onclick=\"addColumn("+rowIndex+")\"></img></td></tr>";
	$("#mytable").append(row);
}

function removeRow() {
	var rowIndex = $("#mytable").find(".outerrow").last().index();
	var x = confirm("You are deleting row "+rowIndex);
	if(x){
		$('#row_'+rowIndex).remove();
	}
}

function addColumn(inRowIndex){
	var rows = $("#innertable_"+inRowIndex).find(".cellrow");
	$(rows).each(function() {
		var row = $(this);
		var columns = $(row).find(".cellcolumn");
		var columnIndex = $(columns).last().index()+1;
		var rowIndex = 0;
		var column = "<td class=\"cellcolumn\" id=\""+inRowIndex+"_"+rowIndex+"_"+columnIndex+"_cell\"><img src=\"images/sign-edit.png\" alt=\"Edit Report Cell\" title=\"Edit Report Cell\" onclick=\"populateCell('"+inRowIndex+"_"+rowIndex+"_"+columnIndex+"','','','')\"></img></td>";
		row.append(column);
	});
}

function populateCell(index,title,sql,ctype,dbalias){
	var cell = buildCell(index);
	var id = "#"+index;
	$(id+"_cell").append(cell);
	$(id+"_title").val(title);
	$(id+"_sql").val(sql);
	$(id+"_conn").val(dbalias);
	$(id+"_select").val(ctype);
}

function buildCell(index){
	var id = index;
	var html="";
	html=html+"<div style=\"width:100%;display:table;border:1px solid #ccc;\">";
	html=html+"	<div style=\"display: table-row\">";
	html=html+"		<div style=\"display: table-cell;background-color: #eee;color:#555;padding: 6px 12px;\">";
	html=html+"			Element Title";
	html=html+"		</div>";
	html=html+"		<div style=\"display: table-cell;padding-left:5px\">";
	html=html+"			<input style=\"width:95%;vertical-align: bottom;border:0px;border-bottom: 1px solid black\" input=\"text\" id=\""+id+"_title\"></input>";
	html=html+"		</div>";
	html=html+"	</div>";
	html=html+"	<div style=\"display: table-row\">";
	html=html+"		<div style=\"display: table-cell;background-color: #eee;color:#555;padding: 6px 12px;\">";
	html=html+"			SQL Query";
	html=html+"		</div>";
	html=html+"		<div style=\"display: table-cell;padding-left:5px\">";
	html=html+"			<input style=\"width:95%;vertical-align: bottom;border:0px;border-bottom: 1px solid black\" input=\"text\" id=\""+id+"_sql\"></input>";
	html=html+"		</div>";
	html=html+"	</div>";
	html=html+"	<div style=\"display: table-row\">";
	html=html+"		<div style=\"display: table-cell;background-color: #eee;color:#555;padding: 6px 12px;\">";
	html=html+"			Chart Type";
	html=html+"		</div>";
	html=html+"		<div style=\"display: table-cell;padding-left:5px\">";
	html=html+"			<select id=\""+id+"_select\">";
	html=html+"				<option value=\"pie\">Pie Chart</option>";
	html=html+"				<option value=\"bar\">Bar Chart</option>";
	html=html+"				<option value=\"barstack\">Bar Stack Chart</option>";
	html=html+"				<option value=\"line\">Line Chart</option>";
	html=html+"				<option value=\"column\">Column Chart</option>";
	html=html+"				<option value=\"columnstack\">Column Stack Chart</option>";
	html=html+"				<option value=\"table\">Table Chart</option>";
	html=html+"			</select>";
	html=html+"		</div>";
	html=html+"	</div>";
	html=html+"	<div style=\"display: table-row\">";
	html=html+"		<div style=\"display: table-cell;background-color: #eee;color:#555;padding: 6px 12px;\">";
	html=html+"			Connection";
	html=html+"		</div>";
	html=html+"		<div style=\"display: table-cell;padding-left:5px\">";
	html=html+"			<select id=\""+id+"_conn\">";
	html=html+"				<option value=\"default\">Default</option>";
	html=html+"			</select>";
	html=html+"		</div>";
	html=html+"	</div>";
	html=html+"	<div style=\"display: table-row\">";
	html=html+"		<div style=\"display: table-cell\"></div>";
	html=html+"		<div style=\"display: table-cell;padding-left:5px\">";
	html=html+"				<button type=\"button\" class=\"btn btn-primary btn-xs\">Test Query</button>";			
	html=html+"		</div>";
	html=html+"	</div>";
	html=html+"</div>";
	return html;
}

function save(mode){
	var reportName = $("#title").val();
	var description = $("#descr").val();
	var userName = $.cookie("username").split("_0_")[0];
	alert('Saving report '+reportName+' to '+userName);
	if(reportName !== ''){
		var rowNumber=0;
		var rows = $("#mytable .outerrow");
		var rowsJson=[];
		$(rows).each(function() {
			var row = $(this);
			if(row !== null){
				var innerrow = $("#innertable_"+rowNumber+" .cellrow");
				var elementsJson={};
				var innercolumns = $(innerrow).find("td.cellcolumn");
				var columnIndex=0;
				var rowJson=[];
				$(innercolumns).each(function(){
					var cell = $(this);
					var id = $(cell).attr("id");
					var title = $("#"+rowNumber+"_0_"+columnIndex+"_title").val();
					var query = $("#"+rowNumber+"_0_"+columnIndex+"_sql").val();
					var ctype = $("#"+rowNumber+"_0_"+columnIndex+"_select").val();
					var dbalias = $("#"+rowNumber+"_0_"+columnIndex+"_conn").val();
					var data={};
					data["title"]=title;
					data["query"]=query;
					data["chartType"]=ctype;
					data["id"]=id;
					data["rownumber"]=rowNumber;
					data["row"]=0;
					data["column"]=columnIndex;
					data["dbalias"]=dbalias;
					rowJson[columnIndex]=data;
					columnIndex++;
				})
			}
			elementsJson["elements"]=rowJson;
			rowsJson[rowNumber]=elementsJson;
			rowNumber++;
		});
		var dashboard={};
		dashboard["title"]=reportName;
		dashboard["description"]=description;
		dashboard["maxrows"]=rowNumber;
		dashboard["rows"]=rowsJson;
		dashboard["aurthor"]=userName;
		var root=[];
		root[0]=dashboard;
		if(mode=='public'){
			userName=mode;
		}
		var request = $.ajax({
			url: "rest/reports/"+userName+"/"+reportName+"/save",
			type: "POST",
			dataType: "html",
			data: {"components":JSON.stringify(root),"dashboardname":reportName},
			success: function(resp){
					alert(resp+". Go to Home Page to view the report");
				},
			error: function(e,status,error){
				    alert("Error Saving "+error+status+e);
				}
			});
	}else{
		alert('Name cannot be null!');
	}
}

function deleteReport(reportName,userName){
	var x = confirm('Are you sure to delete report \''+reportName+'\' for user '+userName+'?');
	if(x){
		var request = $.ajax({
			url: "rest/reports/report/delete",
			type: "GET",
			data: {
				"reportName":reportName,
				"userName":userName},
			success: function(data) {
					alert('Deleted report \''+reportName+'\' for user '+userName);
					$('#'+reportName.replace(/\ /g,"")).remove();
				},
			error: function(e,status,error){
					alert('Unable to delete report \''+reportName+'\' for user '+userName);
				}
		});
	}
}

function runQuery(row,rowIndex,columnIndex){
	var id = row+"_"+rowIndex+"_"+columnIndex;
	var sqlQuery =$("#"+id+"_sql").val();
	var databaseAlias = $("#"+id+"_conn").val();
	var chartType = $("#"+id+"_select").val();
	var userName=$("#usernamehidden").val();
	var request = $.ajax({
		url: "rest/reports/element/test",
		type: "GET",
		data: {
			"sqlQuery":sqlQuery,
			"databaseAlias":databaseAlias,
			"chartType":chartType},
		success: function(data) {
				drawTable(data);
			},
		error: function(e,status,error){
			    $("#"+id).html("Response = "+e.responseText+". Error = "+error+". Status = "+e.status);
			}
	});
}

function loadElement(id,userName,reportName,elementName,chartType){
	var element = $(id);
	var height = 100;
	var width = 100;
	$(id).html("<table style=\"width:"+width+";height:"+height+";border:0px;vertical-align:center;text-align:center\"><tr><td style=\"vertical-align:middle;text-align:center\"><img src=\"images/loading.gif\" style=\"width:75px;height:75px\"></img></td></tr></table>");
	var request = $.ajax({
		url: "rest/reports/"+userName+"/"+reportName+"/"+elementName,
		type: "GET",
		success: function(data) {
				drawChart(data,id,chartType,elementName);
			},
		error: function(e,status,error){
			    $("#"+id).html("Response = "+e.responseText+". Error = "+error+". Status = "+e.status);
			}
	});
}

function drawChart(data,id,chartType,chartTitle){
	var inData = JSON.parse(data);
	var headers = inData[0].headers;
	var rows = inData[0].data;
	var dataTables = {};
	var keyIndex=-1;
	var timeIndex=-1;
	var keyCount=0;
	var metricCount=0;
	var timeCount=0;
	var dataTableToPlot;
	var keyColumnNames=[];
	var timeColumnNames=[];
	var metricColumnNames=[];

	for(i = 0 ; i < headers.length ; i++){
		var h = headers[i].split(":");
		if(h[0] == 'string'){
			keyIndex=i;
			keyColumnNames[keyCount]=h[1];
			keyCount++;
		}else if(h[0] == 'datetime'){
			timeIndex=i;
			timeColumnNames[timeCount]=h[1];
			timeCount++;
		}else{
			metricColumnNames[metricCount]=h[1];
			metricCount++;
		}
	}

	if( ( timeCount>1 || keyCount>1 || metricCount==0 ) && chartType!='table'){
		$("#"+id).html("<h5>Element has key columns ["+Object.values(keyColumnNames)+"], time columns ["+Object.values(timeColumnNames)+"] and metrics columns ["+Object.values(metricColumnNames)+"].</h5><br><h5>Graph is not supported</h5>");
	}else if(timeCount==1 && keyCount==1 && chartType!='table'){
		var keyCol = headers[keyIndex].split(":")[1];
		for (i = 0; i < rows.length; i++){
			var colIndex=0;
			var keyVal = rows[i][keyCol];
			var data = dataTables[keyVal];
			if(data==undefined){
				data = new google.visualization.DataTable();
				for(j = 0 ; j < headers.length ; j++){
					var h = headers[j].split(":");
					if(h[0] == 'datetime'){
						data.addColumn(h[0],h[1]);
					}else if(h[0] == 'number'){
						data.addColumn(h[0],keyVal+"_"+h[1]);
					}
				}
				dataTables[keyVal]=data;
			}
			var index = data.addRows(1);
			for( j = 0;j < headers.length;j++){
				var h = headers[j].split(":");
				if(h[0] == 'datetime'){
					var date = new Date(rows[i][h[1]]);
					data.setCell(index,colIndex,date);
					colIndex++;
				}else if(h[0] == 'number'){
					data.setCell(index,colIndex,rows[i][h[1]]);
					colIndex++;
				}
			}
		}
		var keys = Object.keys(dataTables);
		dataTableToPlot=dataTables[keys[0]];
		var indexes = [];
		for(i=1;i<keys.length;i++){
			var key = keys[i];
			indexes[i-1]=i;
			var dataTable = dataTables[key];
			var numCols = dataTable.getNumberOfColumns();
			var numColsJoin = dataTableToPlot.getNumberOfColumns();
			var index=[];
			var indexJoin=[];
			for(k=1;k<numCols;k++){
				index[k-1]=k;
			}
			for(k=1;k<numColsJoin;k++){
				indexJoin[k-1]=k;
			}
			dataTableToPlot = google.visualization.data.join(dataTableToPlot, dataTable, 'full', [[0, 0]],indexJoin ,index );
		}
	}else{
		var headers = inData[0].headers;
		var rows = inData[0].data;
		dataTableToPlot = new google.visualization.DataTable();
		for(i = 0 ; i < headers.length ; i++){
			var h = headers[i].split(":");
			dataTableToPlot.addColumn(h[0],h[1]);
		}
		for (i = 0; i < rows.length; i++){
			dataTableToPlot.insertRows(i,1)
			for( j = 0;j < headers.length;j++){
				var h = headers[j].split(":");
				if(h[0] == 'datetime'){
					var date = new Date(rows[i][h[1]]);
					dataTableToPlot.setCell(i,j,date);
				}else{
					dataTableToPlot.setCell(i,j,rows[i][h[1]]);
				}
			}
		}
	}

	var chart;
	var element = document.getElementById(id);
	if(chartType=='line'){
		chart = new google.visualization.LineChart(element);
	}	else if(chartType=='pie'){
		chart = new google.visualization.PieChart(element);
	}	else if(chartType=='bar'){
		chart = new google.visualization.BarChart(element);
	}	else if(chartType=='barstack'){
		chart = new google.visualization.BarChart(element);
	}	else if(chartType=='column'){
		chart = new google.visualization.ColumnChart(element);
	}	else if(chartType=='columnstack'){
		chart = new google.visualization.ColumnChart(element);
	}   else if(chartType=='table'){
		chart = new google.visualization.Table(element);
	}
    var cssClassNames = {headerRow: 'celltable'};
    var options = {legend: {position: 'bottom', textStyle: {color: 'blue', fontSize: 12}},width:'100%',height:'100%',title:chartTitle,cssClassNames:cssClassNames,allowHtml:true};
    if( chartType=='barstack' || chartType=='columnstack'){
    	options[isStacked]=true;
    }
	var view = new google.visualization.DataView(dataTableToPlot);
	chart.draw(view, options);
}

function drawTable(inData){
	var headers = inData[0].headers;
	var rows = inData[0].data;
	var dataTables = {};
	var keyIndex=-1;
	var timeIndex=-1;
	var keyCount=0;
	var metricCount=0;
	var timeCount=0;
	var dataTableToPlot;
	var keyColumnNames=[];
	var timeColumnNames=[];
	var metricColumnNames=[];

	var headerHtml="<tr>";
	for(i = 0 ; i < headers.length ; i++){
		var h = headers[i].split(":");
		if(h[0] == 'string'){
			keyIndex=i;
			keyColumnNames[keyCount]=h[1];
			keyCount++;
		}else if(h[0] == 'datetime'){
			timeIndex=i;
			timeColumnNames[timeCount]=h[1];
			timeCount++;
		}else{
			metricColumnNames[metricCount]=h[1];
			metricCount++;
		}
		headerHtml=headerHtml+"<th style=\"border:1px solid black;background-color: #679BB7;color:#ffffff\">"+h[1]+"</th>";
	}
	headerHtml=headerHtml+"</tr>";

	var htmlData="";
	for (i = 0; i < rows.length; i++){
		htmlData=htmlData+"<tr>";
		for( j = 0;j < headers.length;j++){
			var h = headers[j].split(":");
			htmlData=htmlData+"<td style=\"border:1px solid black;\">"+rows[i][h[1]]+"</td>";
		}
		htmlData=htmlData+"</tr>";
	}
	var html="";
	html=html+"<div><h5>Key columns [<span style=\"blue\">"+Object.values(keyColumnNames)+"</span>]</h5><h5>Time columns [<span style=\"blue\">"+Object.values(timeColumnNames)+"</span>]</h5><h5>Metrics columns [<span style=\"blue\">"+Object.values(metricColumnNames)+"</span>]</h5>";
	if( ( timeCount>1 || keyCount>1 || metricCount==0 ) ){
		html=html+"<h5>Graph is not supported</h5></div>";
	}
	html=html+"<div>" + "<table style=\"border:1px solid black;vertical-align:center;text-align:center;border-collapse: collapse;\">" +
			headerHtml +
			htmlData + "</table>" +
			"</div>";
	var x=window.open("","","directories=0,titlebar=0,toolbar=0,location=0,status=0,menubar=0,scrollbars=no,resizable=no,width=400,height=350");
	x.document.open();
	x.document.write(html);
	x.document.close();
}
