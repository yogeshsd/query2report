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
		var analyticsPossible=false;
		var forecastPossible=false;
		if(rows.length==0){
			document.getElementById(id).innerHTML = "<h4> - No Data for <font style=\"color:red\">'"+chartTitle+"'</font> -</h4>";
			return;
		}

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
		
		if(metricCount == 2 && ( keyCount + timeCount ) == 0 ) {
			forecastPossible=true;
		}

		if(metricCount >= 1 && ( keyCount + timeCount) <= 1 ) {
			analyticsPossible=true;
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
	    		interval: { 
	    			'mean': { 'style':'line', 'color':'black','lineWidth': 2},
	    			'fit1': { 'style':'line', 'color':'orange','lineWidth': 2},
	    			'fit2': { 'style':'line', 'color':'green','lineWidth': 2},
	    			'fit4': { 'style':'line', 'color':'brown','lineWidth': 2},
	    			'stddev': { 'style':'area', 'color':'#4374E0','lineWidth': 2,'fillOpacity': 0.3},
	    			'normaldist': { 'style':'area', 'color':'#4374E0','lineWidth': 2,'fillOpacity': 0.3}
	    		},
	    		chart : { title: chartTitle }, 
	    		chartArea: {
	    			backgroundColor:{
	    				stroke:'grey',
	    				strokeWidth:1
	    			},
	    			width:'90%',
	    			height:'70%'
	    		},
	    		legend: {
    				position: 'bottom', 
    				textStyle: {color: 'black', fontSize: 14,style:'bold'}
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
			if(!analyticsPossible){
				alert('Statistics is supported only for one measure charts');
				return;
			}
			var stats=getAnalytics(rows);
			var numColsOrig = dataTableToPlot.getNumberOfColumns();
			dataTableToPlot.addColumn({id:'mean',type:'number',role:'interval',label:'Mean'});
			for (i = 0; i < rows.length; i++){
				dataTableToPlot.setCell(i,numColsOrig,stats.mean);
			}
			wrapper.draw();
		});

		google.visualization.events.addListener(wrapper.getChart(), 'stddev', function(){
			if(!analyticsPossible){
				alert('Statistics is supported only for one measure charts');
				return;
			}
			var stats=getAnalytics(rows);
			var dataTableToPlot = wrapper.getDataTable();
			var numColsOrig = dataTableToPlot.getNumberOfColumns();
			dataTableToPlot.addColumn({id:'stddev',type:'number',role:'interval',label:'Standard Deviation'});
			dataTableToPlot.addColumn({id:'stddev',type:'number',role:'interval',label:'Standard Deviation'});
			for (i = 0; i < rows.length; i++){
				dataTableToPlot.setCell(i,numColsOrig,stats.mean+stats.stddev);
				dataTableToPlot.setCell(i,numColsOrig+1,stats.mean-stats.stddev);
			}
			wrapper.draw();
		});
		
		google.visualization.events.addListener(wrapper.getChart(), 'polyfit', function(obj){
			if(!forecastPossible){
				alert('Forecasting is supported only when both the axis data types are numeric');
				return;
			}
			var n = obj.n;
			var stats=getPolyFit(rows,n);
			var numColsOrig = dataTableToPlot.getNumberOfColumns();
			dataTableToPlot.addColumn({id:'fit'+n,type:'number',role:'interval',label:n+' Order Regression','pointSize': 10});
			var keys = Object.keys(rows[0]);
			for (i = 0; i < rows.length+30; i++){
				if(i > rows.length-1){
					var x = rows[rows.length-1][keys[0]]+(i-rows.length*1);
				}else{
					var x = rows[i][keys[0]];
				}
				var y = 0;
				for (j = 0; j <= n; j++){
					y = y+stats[j]*Math.pow(x,j);
				}
				console.log('Y = '+y+', X = '+x);
				if(i>rows.length-1){
					dataTableToPlot.addRow();
					dataTableToPlot.setCell(i,0,x);
					dataTableToPlot.setCell(i,1,y);
				}
				dataTableToPlot.setCell(i,numColsOrig,y);
			}
			wrapper.setDataTable(dataTableToPlot);
			wrapper.draw();
		});
	});
}

function getAnalytics(rows){
	var sumy=0;
	var count=0;
	var mean=0;
	var variance=0;
	var stddev = 0;
	var keys = Object.keys(rows[0]);
	for (i = 0; i < rows.length; i++){
		var y = rows[i][keys[1]];
		count++;
		sumy=sumy+y;
	}
	mean = sumy/count;
	var total=0;
	for (i = 0; i < rows.length; i++){
		var y = rows[i][keys[1]];
		var diffvalue = ((y-mean)*(y-mean));
		total = total + diffvalue;
	}
	variance = total/(count);
	stddev = Math.sqrt(variance);
	var obj = {};
	obj.mean = Number(Number(mean).toFixed(2));
	obj.stddev = Number(Number(stddev).toFixed(2));
	obj.variance = Number(Number(variance).toFixed(2));
	console.log('Mean = '+obj.mean+', Variance = '+obj.variance+', Standard Deviation = '+obj.stddev);
	return obj;
}

function getPolyFit(rows,n){
	var keys = Object.keys(rows[0]);
	var N = rows.length;
    var X=[2*n+1];
    for(i=0;i<=2*n;i++){
        X[i]=0;
        for(j=0;j<N;j++){
    		var x = rows[j][keys[0]];
        	X[i]=X[i]+Math.pow(x,i);
        }
    }	
    var B=[n+1];  
    var Y=[n+1];      
    for(i=0;i<=n;i++){
        Y[i]=0;
        for(j=0;j<N;j++){
        	var y = rows[j][keys[1]];
        	var x = rows[j][keys[0]];
            Y[i]=Y[i]+Math.pow(x,i)*y;
        }
    }
    for(i=0;i<=n;i++){
    	B[i]=[n+2];
        for(j=0;j<=n;j++){
            B[i][j]=X[i+j]; 
        }
    }
    for(i=0;i<=n;i++){
        B[i][n+1]=Y[i];
    }
    var A=[n+1];
    gaussEliminationLS(n+1,n+2,B,A);
    for(i=0;i<=n;i++){
    	console.log('X'+n+' = '+A[i]);
    }
	return A;
}

function gaussEliminationLS(m,n,a,x){
    var i,j,k;
    for(i=0;i<m-1;i++){
        for(k=i+1;k<m;k++){
            if(Math.abs(a[i][i])<Math.abs(a[k][i])){
                for(j=0;j<n;j++){                
                    var temp;
                    temp=a[i][j];
                    a[i][j]=a[k][j];
                    a[k][j]=temp;
                }
            }
        }
        for(k=i+1;k<m;k++){
            var term=a[k][i]/ a[i][i];
            for(j=0;j<n;j++){
                a[k][j]=a[k][j]-term*a[i][j];
            }
        }
         
    }
    for(i=m-1;i>=0;i--){
        x[i]=a[i][n-1];
        for(j=i+1;j<n-1;j++){
            x[i]=x[i]-a[i][j]*x[j];
        }
        x[i]=x[i]/a[i][i];
    } 
}
