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

function drawChart(data,id,chartType,chartTitle,inElement){
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

		if(chartType=='cell'){
			var html_data="<style>.cellclass{text-align:center;color:blue;font-weight:bold}</style><div style=\"text-align:center;top:30%\">";
			for (i = 0; i < rows.length; i++){
				for( j = 0;j < headers.length;j++){
					var h = headers[j].split(":");
					var val = rows[i][h[1]];
					if(h[1]=='h1'){
						html_data=html_data+"<h1>"+val+"</h1>";
					}else if(h[1]=='h2'){
						html_data=html_data+"<h2>"+val+"</h2>";
					}else if(h[1]=='h3'){
						html_data=html_data+"<h3>"+val+"</h3>";
					}else if(h[1]=='h4'){
						html_data=html_data+"<h4>"+val+"</h4>";
					}
				}
			}
			document.getElementById(id).innerHTML=html_data+"</div>";
			return;
		}else if( ( ( timeCount + keyCount ) > 2 || timeCount >1 || keyCount > 2 || metricCount==0 ) && chartType!='table'){
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
						var value = rows[i][h[1]];
						dataTableToPlot.setCell(i,j,value);
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
	    var options = { 
	    		interval: { 'mean': { 'style':'line', 'color':'black','lineWidth': 2},'stddev': { 'style':'area', 'color':'orange','lineWidth': 2,'fillOpacity': 0.3} },
	    		chart : { title: chartTitle }, 
	    		chartArea: {
	    			backgroundColor:{
	    				stroke:'grey',
	    				strokeWidth:1
	    			}
	    		},
	    		legend: {
    				position: 'bottom', 
    				textStyle: {color: 'blue', fontSize: 12}
	    		},
	    		cssClassNames:{headerRow: 'gTableHeaderRow',headerCell: 'gTableHeaderCell'},
	    		allowHtml:true,
	    		hAxis:{textPosition:'out',showTextEvery:1}
	    };
	    if( chartType=='barstack' || chartType=='columnstack'){
	    	options["isStacked"]=true;
	    }
	    if(chartType=='table'){
	    	options["page"]='enable';
	    	options["width"]='100%';

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
		inElement.chartWrapper =  wrapper;
		
		google.visualization.events.addListener(wrapper.getChart(), 'mean', function(){
			var stats=getAnalytics(rows,headers);
			var numColsOrig = dataTableToPlot.getNumberOfColumns();
			if(headers.length>2){
				alert('Statistics is supported only for one measure charts');
				return;
			}
			dataTableToPlot.addColumn({id:'mean',type:'number',role:'interval'});
			dataTableToPlot.addColumn({id:'mean',type:'number',role:'interval'});
			for (i = 0; i < rows.length; i++){
				dataTableToPlot.setCell(i,numColsOrig,stats.mean);
				dataTableToPlot.setCell(i,numColsOrig+1,stats.mean);
			}
			wrapper.draw();
		});
		
		google.visualization.events.addListener(wrapper.getChart(), 'stddev', function(){
			var stats=getAnalytics(rows,headers);
			var dataTableToPlot = wrapper.getDataTable();
			var numColsOrig = dataTableToPlot.getNumberOfColumns();
			if(headers.length>2){
				alert('Statistics is supported only for one measure charts');
				return;
			}
			dataTableToPlot.addColumn({id:'stddev',type:'number',role:'interval'});
			dataTableToPlot.addColumn({id:'stddev',type:'number',role:'interval'});
			for (i = 0; i < rows.length; i++){
				dataTableToPlot.setCell(i,numColsOrig,stats.mean+stats.stddev);
				dataTableToPlot.setCell(i,numColsOrig+1,stats.mean-stats.stddev);
			}
			wrapper.draw();
		});
	});
}

function getAnalytics(rows,headers){
	var sum=0;
	var count=0;
	var mean=0;
	var variance=0;
	var stddev = 0;
	var min=Number.MAX_SAFE_INTEGER;;
	var max=Number.MIN_SAFE_INTEGER;
	for (i = 0; i < rows.length; i++){
		for( j = 1;j < headers.length;j++){
			var h = headers[j].split(":");
			if(h[0] == 'number'){
				var value = rows[i][h[1]];
				if(value<min){
					min=value;
				}
				if(value>max){
					max=value;
				}
				count++;
				sum=sum+value;
			}
		}
	}
	mean = sum/count;
	var total=0;
	for (i = 0; i < rows.length; i++){
		for( j = 1;j < headers.length;j++){
			var h = headers[j].split(":");
			if(h[0] == 'number'){
				var value = rows[i][h[1]];
					var diffvalue = ((value-mean)*(value-mean));
					total = total + diffvalue;
			}
		}
	}
	variance = total/(count);
	stddev = Math.sqrt(variance);
	var obj = {};
	obj.mean = mean;
	obj.stddev = stddev;
	obj.variance = variance;
	obj.n = rows.length;
	obj.min=min;
	obj.max=max;
	return obj;
}
