/*  
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
*/

function loadGoogle(){
	console.log("Loaded google charts")
}

function drawChart(data,id,chartType,chartTitle){
	google.charts.load('current', {'packages':['corechart','annotatedtimeline','charteditor']});
	google.charts.load('visualization', {'packages':['table']});
	var loaded = google.charts.setOnLoadCallback(loadGoogle);
	loaded.then(function(value) {
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

		if( ( ( timeCount + keyCount ) > 2 || timeCount >1 || keyCount > 2 || metricCount==0 ) && chartType!='table'){
			document.getElementById(id).innerHTML = "<h5 id=\"notsupportedelem\">Element has key columns ["+Object.values(keyColumnNames)+"], time columns ["+Object.values(timeColumnNames)+"] and metrics columns ["+Object.values(metricColumnNames)+"].</h5><br><h5>Graph is not supported</h5>";
			return;
		}else if( (timeCount==1 && keyCount==1 && chartType!='table' ) || keyCount==2){
			var keyCol = headers[keyIndex].split(":")[1];
			for (i = 0; i < rows.length; i++){
				var colIndex=0;
				var keyVal = rows[i][keyCol];
				var gdata = dataTables[keyVal];
				if(gdata==undefined){
					var gdata = new google.visualization.DataTable();
					for(j = 0 ; j < headers.length ; j++){
						if(j == keyIndex)
							continue;
						var h = headers[j].split(":");
						if(h[0] == 'datetime' || h[0] == 'string'){
							gdata.addColumn(h[0],h[1]);
						}else if(h[0] == 'number'){
							gdata.addColumn(h[0],keyVal+"_"+h[1]);
						}
					}
					dataTables[keyVal]=gdata;
				}
				var index = gdata.addRows(1);
				for( j = 0;j < headers.length;j++){
					if(j == keyIndex)
						continue;					
					var h = headers[j].split(":");
					if(h[0] == 'datetime'){
						var date = new Date(rows[i][h[1]]);
						gdata.setCell(index,colIndex,date);
						colIndex++;
					}else if(h[0] == 'string' || h[0] == 'number'){
						gdata.setCell(index,colIndex,rows[i][h[1]]);
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
		var cType='';
		if(chartType=='line'){
			chart = new google.visualization.LineChart(element);
			cType='LineChart';
		}	else if(chartType=='pie' || chartType=='donut'){
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
		}   else if(chartType=='area'){
			chart = new google.visualization.AreaChart(element);
			cType='AreaChart';
		}    else if(chartType=='steppedarea'){
			chart = new google.visualization.SteppedAreaChart(element);
			cType='SteppedAreaChart';
		}   else if(chartType=='table'){
			chart = new google.visualization.Table(element);
			cType='Table';
		}	else if(chartType=='scatter'){
			chart = new google.visualization.ScatterChart(element);
			cType='ScatterChart';
		}   else if(chartType=='annotate_line'){
			chart = new google.visualization.AnnotatedTimeLine(element);
			cType='AnnotatedTimeLine';
		}
	    var cssClassNames = {headerRow: 'celltable'};
	    var options = { chart : { title: chartTitle }, chartArea: { left:'10%',top:'5%',width:'80%',height:'75%'},legend: {position: 'bottom', textStyle: {color: 'blue', fontSize: 12}},cssClassNames:{headerRow: 'gTableHeaderRow',headerCell: 'gTableHeaderCell'},allowHtml:true,hAxis:{textPosition:'out',showTextEvery:1}};
//	    if(chartType=='annotate_line'){
//	    	var options = { chart : { title: chartTitle }, chartArea: { left:'0%',top:'0%',width:'80%',height:'75%'},legend: {position: 'bottom', textStyle: {color: 'blue', fontSize: 12}},cssClassNames:{headerRow: 'gTableHeaderRow',headerCell: 'gTableHeaderCell'},allowHtml:true,hAxis:{textPosition:'out',showTextEvery:1}};
//	    }
	    if( chartType=='barstack' || chartType=='columnstack'){
	    	options["isStacked"]=true;
	    }
	    if(chartType=='table'){
	    	options["page"]='enable';
	    	options["width"]='100%';
	    	options["height"]='100%';
	    }
	    if(chartType=='donut'){
	    	options["pieHole"]='0.4';
	    }
	    
	    var wrapper = new google.visualization.ChartWrapper({
			chartType: cType,
			dataTable: dataTableToPlot,
			options: options,
			container: document.getElementById(id)
		});
		wrapper.draw();
	});
}
