function loadElement(id,userName,reportName,elementName,chartType){
	var height = 100;
	var width = 100;
	var request = $.ajax({
		url: "rest/reports/"+userName+"/"+reportName+"/"+elementName,
		type: "GET",
		success: function(data) {
				drawChart(data,id,chartType,elementName);
			},
		error: function(e,status,error){
				document.getElementById(id).innerHTML="Response = "+e.responseText+". Error = "+error+". Status = "+e.status;
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
	
	if(rows.length==0){
		document.getElementById(id).innerHTML = "<h4> - No Data for <font style=\"color:red\">'"+chartTitle+"'</font> -</h4>";
		return;
	}

	if( ( timeCount>1 || keyCount>1 || metricCount==0 ) && chartType!='table'){
		document.getElementById(id).innerHTML = "<h5>Element has key columns ["+Object.values(keyColumnNames)+"], time columns ["+Object.values(timeColumnNames)+"] and metrics columns ["+Object.values(metricColumnNames)+"].</h5><br><h5>Graph is not supported</h5>";
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
	}	else if(chartType=='annotate_line'){
		chart = new google.visualization.AnnotatedTimeLine(element);
	}
    var cssClassNames = {headerRow: 'celltable'};
    var options = {legend: {position: 'bottom', textStyle: {color: 'blue', fontSize: 12}},width:'100%',height:'100%',cssClassNames:{headerRow: 'gTableHeaderRow',headerCell: 'gTableHeaderCell'},allowHtml:true};
    if(chartType=='annotate_line'){
    	options = {legend: {position: 'bottom', textStyle: {color: 'blue', fontSize: 12}},width:'80%',height:'100%',cssClassNames:cssClassNames,allowHtml:true};
    }
    if( chartType=='barstack' || chartType=='columnstack'){
    	options["isStacked"]=true;
    }
	var view = new google.visualization.DataView(dataTableToPlot);
	chart.draw(view, options);
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

function drawTable(d){
	var inData = JSON.parse(d);
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
		headerHtml=headerHtml+"<th style=\"border:1px solid black;background-color: #333;color:#ffffff\">"+h[1]+"</th>";
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
