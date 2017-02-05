/**
 *
 */

function focusMenu(ele){
	$(ele).css("zoom","100%");
}
function unfocusMenu(ele){
	$(ele).css("zoom","100%");
}
function editReport(){
	var reportName = $("#reportName").val();
	var userName = $("#userName").val();
	location.href = "createedit?name="+reportName+"&userName="+userName;
}

function addRow() {
	var rowIndex = $("#mytable .outerrow").last().index()+1;
	var rowId = rowIndex+"_0_0_cell";
	var row = "<tr class=\"outerrow\" id=\"row_"+rowIndex+"\"><td><table id=\"innertable_"+rowIndex+"\" class=\"innertable_withborder\"><tr class=\"cellrow\"><td class=\"cellcolumn\" id=\""+rowId+"\"><img src=\"images/sign-edit.png\" alt=\"Edit Report Cell\" onclick=\"populateCell("+rowIndex+",0,0,'','','')\"></td></tr></table></td><td><img src=\"images/sign-add.png\" alt=\"Add Report Column\" title=\"Add Report Column\" onclick=\"addColumn("+rowIndex+")\"></img></td></tr>";
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
		var column = "<td class=\"cellcolumn\" id=\""+inRowIndex+"_"+rowIndex+"_"+columnIndex+"_cell\"><img src=\"images/sign-edit.png\" alt=\"Edit Report Cell\" title=\"Edit Report Cell\" onclick=\"populateCell("+inRowIndex+","+rowIndex+","+columnIndex+",'','','')\"></img></td>";
		row.append(column);
	});
}

function removeColumn(inRowIndex){
	var rows = $("#innertable_"+inRowIndex+" tr");
	$(rows).each(function() {
		var row = $(this);
		var columns = $(row).find("td");
		var columnIndex = $(columns).index();
		var rowIndex = $(row).index();
		$('#row_'+rowIndex+'_column_'+columnIndex).remove();
	});
}

function populateCell(index,rowIndex,columnIndex,title,sql,ctype,dbalias){
	var cell = buildCell(index,rowIndex,columnIndex,title,sql);
	var id = "#"+index+"_"+rowIndex+"_"+columnIndex;
	$(id+"_cell").html(cell);
	$(id+"_title").val(title);
	$(id+"_sql").val(sql);
	$(id+"_conn").val(dbalias);
	$(id+"_select").val(ctype);
}

function buildPage(json){
	var table = $("#mytable");
	table.empty();
	var myArr = JSON.parse(JSON.stringify(json));
	var maxrows = myArr[0].maxrows;
	for(var i=0;i<maxrows;i++){
		var row = myArr[0].rows[i];
		addRow();
		var numcols = row.elements.length;
		for(var j=0;j<numcols;j++){
			if(j>0)
				addColumn(i);
			var column = row.elements[j];
			var rowNum = column.rownumber;
			var rowId = column.row;
			var columnId = column.column;
			populateCell(rowNum,rowId,columnId,column.title,column.query,column.chartType,column.dbalias);
		}
	}
}

function save(mode){
	var username = "public";
	if(mode == "personal"){
		username=$("#usernamehidden").val();
	}
	var dashname = $("#dashname").val();
	var description = $("#description").val();
	if(dashname !== ''){
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
		dashboard["title"]=dashname;
		dashboard["description"]=description;
		dashboard["maxrows"]=rowNumber;
		dashboard["rows"]=rowsJson;
		var reportName = username+":"+dashname;
		var root=[];
		root[0]=dashboard;
		var request = $.ajax({
			url: "rest/reports/save",
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

function loadReport(reportName,userName){
	$("#editmenu").show();
	$("#pdfmenu").show();
	$("#csvmenu").show();
	$("#htmlmenu").show();
	var section = $("#mainsection");
	var request = $.ajax({
		url: "rest/reports/report",
		type: "GET",
		data: {
			"reportName":reportName,
			"userName":userName},
		success: function(data) {
			var jsonData = JSON.parse(data[0]);
				section.empty();
				section.append("<input id=\"userName\" type=\"hidden\" value=\""+userName+"\">");
				section.append("<input id=\"reportName\" type=\"hidden\" value=\""+reportName+"\">");
				section.append("<div id=\"reporttitle_div\">");
				$("#reporttitle_div").append("<ul id=\"reporttitle_ul\" class=\"list-group\">");
				$("#reporttitle_ul").append("<li id=\"reporttitle_ul_li\" class=\"list-group-item\" style=\"padding:0px\">");
				$("#reporttitle_ul_li").append("<h4 style=\"padding-left:10px;\">"+jsonData.title+"</h4>");
				$("#reporttitle_ul_li").append("<span class=\"reportdescr\" style=\"color:black;padding-left:10px\">"+jsonData.description+"</span>");
				section.append("<div id=\"reportmain_div\">");
				var numRows = jsonData.maxrows;
				var rowHeight = 85/numRows;
				if(rowHeight<25){
					rowHeight=25;
				}
				for(i=0;i<numRows;i++){
					var row = jsonData.rows[i];
					var numCols = row.elements.length;
					var colWidth = 99/numCols;
					$("#reportmain_div").append("<div id=\"divrow_"+i+"\" style=\"padding:5px;height:"+rowHeight+"% !important;overflow:auto\">");
					for(j=0;j<numCols;j++){
						var column = row.elements[j];
						var id = column.id;
						$("#divrow_"+i).append("<div class=\"divchart\" id=\"div_"+id+"\" style=\"float:left;height:100%;width:"+colWidth+"%\" onclick=\"refreshElement(this,'"+jsonData.title+"','"+column.title+"',300000,'"+userName+"','"+column.chartType+"')\">");
						$("#div_"+id).html("<table style=\"width:"+colWidth+";border:0px;vertical-align:center;text-align:center\"><tr><td style=\"vertical-align:middle;text-align:center\"><img src=\"images/loading.gif\" style=\"width:75px;height:75px\"></img></td></tr></table>");
						$("#div_"+id).load(loadElement("div_"+id,jsonData.title,column.title,userName,column.chartType));
					}
				}
			},
		error: function(e,status,error){
				$("#reportmain_div").html("Response = "+e.responseText+". Error = "+error+". Status ="+e.status);
			}
	});
}

function refreshElement(id,reportName,elementName,timeout,userName,chartType){
	setInterval(function() {
		var height = $(id).height();
		var width = $(id).width();
		$(id).html("<table style=\"width:"+width+";height:"+height+";border:0px;vertical-align:center;text-align:center\"><tr><td style=\"vertical-align:middle;text-align:center\"><img src=\"images/loading.gif\" style=\"width:75px;height:75px\"></img></td></tr></table>");
		var request = $.ajax({
			url: "rest/reports/element",
			type: "GET",
			data: {
				"reportName":reportName,
				"elementName":elementName,
				"userName":userName},
			success: function(data) {
					drawChart(data,id,chartType,elementName);
				},
			error: function(e,status,error){
					$("#"+id).html("Response = "+e.responseText+". Error = "+error+". Status ="+e.status);
				}
			});
	},timeout);
}

function loadElement(id,reportName,elementName,userName,chartType){
	var element = $(id);
	var height = 100;
	var width = 100;
	$(id).html("<table style=\"width:"+width+";height:"+height+";border:0px;vertical-align:center;text-align:center\"><tr><td style=\"vertical-align:middle;text-align:center\"><img src=\"images/loading.gif\" style=\"width:75px;height:75px\"></img></td></tr></table>");
	var request = $.ajax({
		url: "rest/reports/element",
		type: "GET",
		data: {
			"reportName":reportName,
			"elementName":elementName,
			"userName":userName},
		success: function(data) {
				drawChart(data,id,chartType,elementName);
			},
		error: function(e,status,error){
			    $("#"+id).html("Response = "+e.responseText+". Error = "+error+". Status = "+e.status);
			}
	});
}

function drawChart(inData,id,chartType,chartTitle){
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


function showTab(tabName){
	if(tabName == "public"){
		$("#public_li_id").attr('class', 'list-group-item active');
		$("#personal_li_id").attr('class', 'list-group-item');
		$("#schedule_li_id").attr('class', 'list-group-item');
	}else if(tabName == "personal"){
		$("#personal_li_id").attr('class', 'list-group-item active');
		$("#public_li_id").attr('class', 'list-group-item');
		$("#schedule_li_id").attr('class', 'list-group-item');
	}else if(tabName == "schedule"){
		$("#schedule_li_id").attr('class', 'list-group-item active');
		$("#public_li_id").attr('class', 'list-group-item');
		$("#personal_li_id").attr('class', 'list-group-item');
	}
	$("#editmenu").hide();
	$("#savepersonalmenu").hide();
	$("#savepublicmenu").hide();
	$("#pdfmenu").hide();
	$("#csvmenu").hide();
	$("#htmlmenu").hide();
	$("#mainsection").empty();
	$("#mainsection").html("<table style=\"width:100%;height:50%;border:0px;vertical-align:center;text-align:center\"><tr><td style=\"vertical-align:middle;text-align:center\"><img src=\"images/loading.gif\" style=\"width:75px;height:75px\"></img></td></tr></table>");
	var userName = "public";
	if(tabName == "personal" || tabName == "schedule"){
		userName=$("#usernamehidden").val();
	}
	$.ajax({
		  url: 'rest/reports',
		  type: 'GET',
		  dataType: 'JSON',
		  data: {"userName":userName},
		  success: function(data) {
			  var htmlData="<table id=\"reportlisttable\"><tr><th width=\"25%\">Report Name</th><th width=\"75%\">Description</th></tr>";
				for (i = 0; i < data.length; i++) {
					var report = JSON.parse(data[i]);
					htmlData = htmlData+"<tr id=\""+report.title.replace(/\ /g,"")+"\">";
					htmlData = htmlData+"<td>";
					htmlData = htmlData+"<a title=\"Click to run report\" alt=\"Click to run report\" href=\"javascript:loadReport('"+report.title+"','"+userName+"')\">";
					htmlData = htmlData+report.title;
					htmlData = htmlData+"</a>";
					htmlData = htmlData+"<a href=\"createedit?name="+report.title+"&userName="+userName+"\"><img style=\"padding:5px\" src=\"images/report-edit.png\"/></a>";
					htmlData = htmlData+"<a href=\"javascript:deleteReport('"+report.title+"','"+userName+"')\"><img style=\"padding:5px\" src=\"images/report-delete.png\"/></a>";
					htmlData = htmlData+"</td>";
					htmlData = htmlData+"<td>"+report.description+"</td>";
					htmlData = htmlData+"</tr>";
				}
				htmlData=htmlData+"</table>";
				$("#mainsection").html(htmlData);
		  },
		  error: function(e,status,error){
			  $("#mainsection").html("Response = "+e.responseText+". Error = "+error+". Status = "+e.status);
			}
		});
}

function exportPDF(){
	var reportName = $("#reportName").val();
	var pdf = new jsPDF('landscape');
    var options = { pagesplit: true };
    pdf.addHTML($("#mainsection"), options, function(){
    	pdf.save(reportName+".pdf");
    });
}

function exportHTML(){
	var reportName = $("#reportName").val()+".html";
	var element = document.createElement('a');
	var html = $("#mainsection").html();
	element.setAttribute('href', 'data:text/plain;charset=utf-8,' + encodeURIComponent(html));
	element.setAttribute('download', reportName);
	document.body.appendChild(element);
	element.click();
	document.body.removeChild(element);
}