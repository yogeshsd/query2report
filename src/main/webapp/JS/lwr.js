function loadElement(id,userName,reportName,elementName,chartType){
	var request = $.ajax({
		url: "rest/reports/"+userName+"/"+reportName+"/"+elementName,
		type: "GET",
		success: function(data) {
				drawChart(data,id,chartType,elementName,document);
			},
		error: function(e,status,error){
				document.getElementById(id).innerHTML="Response = "+e.responseText+". Error = "+error+". Status = "+e.status;
			}
	});
}

function drawChart(data,id,chartType,chartTitle,doc){
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
		doc.getElementById(id).innerHTML = "<h4> - No Data for <font style=\"color:red\">'"+chartTitle+"'</font> -</h4>";
		return;
	}

	if( ( timeCount>1 || keyCount>1 || metricCount==0 ) && chartType!='table'){
		doc.getElementById(id).innerHTML = "<h5>Element has key columns ["+Object.values(keyColumnNames)+"], time columns ["+Object.values(timeColumnNames)+"] and metrics columns ["+Object.values(metricColumnNames)+"].</h5><br><h5>Graph is not supported</h5>";
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
	var element = doc.getElementById(id);
	var cType='';
	if(chartType=='line'){
		chart = new google.visualization.LineChart(element);
		cType='LineChart';
	}	else if(chartType=='pie'){
		chart = new google.visualization.PieChart(element);
		cType='PieChart';
	}	else if(chartType=='bar'){
		chart = new google.visualization.BarChart(element);
		cType='BarChart';
	}	else if(chartType=='barstack'){
		chart = new google.visualization.BarChart(element);
		cType='BarChart';
	}	else if(chartType=='column'){
		chart = new google.visualization.ColumnChart(element);
		cType='ColumnChart';
	}	else if(chartType=='columnstack'){
		chart = new google.visualization.ColumnChart(element);
		cType='ColumnChart';
	}   else if(chartType=='table'){
		chart = new google.visualization.Table(element);
		cType='Table';
	}	else if(chartType=='annotate_line'){
		chart = new google.visualization.AnnotatedTimeLine(element);
		cType='LineChart';
	}
    var cssClassNames = {headerRow: 'celltable'};
    var options = {legend: {position: 'bottom', textStyle: {color: 'blue', fontSize: 12}},cssClassNames:{headerRow: 'gTableHeaderRow',headerCell: 'gTableHeaderCell'},allowHtml:true};
    if( chartType=='barstack' || chartType=='columnstack'){
    	options["isStacked"]=true;
    }
    if(chartType=='table'){
    	options["page"]='enable';
    	options["width"]='100%';
    }
    var wrapper = new google.visualization.ChartWrapper({
		chartType: cType,
		dataTable: dataTableToPlot,
		options: options,
		containerId: id
	});
	wrapper.draw();
}

function drawTable(data){
	var win = window.open('', '', 'width=500,height=500,top=200,left=200');
	var doc = win.document;
	doc.write("<body><div id='datatable'><body>");
	drawChart(data,'datatable','table','Test Data',doc)
	doc.close();
}

function editElement(element,aliases){
	var html="";
	html=html+"	<div class=\"holderdiv\">";
	html=html+"		<div class=\"titlediv\">";
	html=html+"			Element Title";
	html=html+"		</div>";
	html=html+"		<div class=\"inputdiv\">";
	html=html+"			<input style=\"width:95%;vertical-align: bottom;border:0px;border-bottom: 1px solid black\" placeholder=\"Element Title\" data-ng-value=\"element.title\" data-ng-model=\"element.title\"></input>";
	html=html+"		</div>";
	html=html+"	</div>";
	html=html+"	<div class=\"holderdiv\">";
	html=html+"		<div class=\"titlediv\">";
	html=html+"			SQL Query";
	html=html+"		</div>";
	html=html+"		<div class=\"inputdiv\">";
	html=html+"			<input style=\"width:95%;vertical-align: bottom;border:0px;border-bottom: 1px solid black\" placeholder=\"SQL Query\" data-ng-value=\"element.query\" data-ng-model=\"element.query\"></input>";
	html=html+"		</div>";
	html=html+"	</div>";
	html=html+"	<div  class=\"holderdiv\">";
	html=html+"		<div class=\"titlediv\">";
	html=html+"			Chart Type";
	html=html+"		</div>";
	html=html+"		<div class=\"inputdiv\">";
	html=html+"			<select data-ng-value=\""+element.chartType+"\" data-ng-model=\"element.chartType\">";
	html=html+"				<option value=\"pie\">Pie Chart</option>";
	html=html+"				<option value=\"bar\">Bar Chart</option>";
	html=html+"				<option value=\"barstack\">Bar Stack Chart</option>";
	html=html+"				<option value=\"line\">Line Chart</option>";
	html=html+"				<option value=\"annotate_line\">Annoted Line Chart</option>";
	html=html+"				<option value=\"column\">Column Chart</option>";
	html=html+"				<option value=\"columnstack\">Column Stack Chart</option>";
	html=html+"				<option value=\"table\">Table Chart</option>";
	html=html+"			</select>";
	html=html+"		</div>";
	html=html+"	</div>";
	html=html+"	<div  class=\"holderdiv\">";
	html=html+"		<div class=\"titlediv\">";
	html=html+"			Connection";
	html=html+"		</div>";
	html=html+"		<div class=\"inputdiv\">";
	html=html+"			<select data-ng-value=\""+element.dbalias+"\" data-ng-model=\"element.dbalias\">";
	for(i = 0 ; i < aliases.length ; i++){
		if(aliases[i] == element.dbalias){
			html=html+"				<option value=\""+aliases[i]+"\" selected>"+aliases[i]+"</option>";
		}else{
			html=html+"				<option value=\""+aliases[i]+"\">"+aliases[i]+"</option>";
		}
	}
	html=html+"			</select>";
	html=html+"		</div>";
	html=html+"		</div>";
	html=html+"	<div><div style=\"display:inline\"><button style=\"background:#337ab7;color:#fff\" data-ng-click=\"testElement(element)\">Test Element</button></div><div style=\"display:inline\"><button style=\"background:#337ab7;color:#fff\" data-ng-click=\"loadElement(report.title,element.title,element.chartType)\">Close Edit</button></div></div>";	
	return html;
}
