var lwrApp = angular.module('LWR', [ 'ngRoute', 'ui.router', 'ui.bootstrap','ngCookies','ngSanitize','ngMaterial']);

lwrApp.config(function($stateProvider,$urlRouterProvider){
	$stateProvider
	.state('list', {
		url: "/list/:mode",
		templateUrl: "html/reportlist.html",
		params: {
			mode:null
		},
		controller: 'ReportListController'
	})
 	.state('openreport',{
	      url: "/openreport/:title?mode&type",
	      templateUrl: "html/openreport.html",
	      params: {
	    	    title: null,
	    	    mode:null,
	    	    type:null
	    	  },
	      controller: 'ReportController'
	})
    .state('usermgmt', {
		url: '/usermgmt',
		templateUrl: "html/usermgmt.html",
		params: {
			title: null,
			mode:null
		},
		controller: 'UserController'
	})
	.state('connmgmt', {
		url: '/connmgmt',
		templateUrl: "html/connmgmt.html",
		params: {
		    title: null,
		    mode:null
		},
		controller: 'ConnectionController'
	})
	.state('getstarted', {
			url : '/getstarted',
			templateUrl: "html/getstarted.html",
			params: {
				title: null,
				mode:null
			}			
	})
	.state('example', {
		url: '/example',
		templateUrl: "html/example.html",
		params: {
			title: null,
			mode:null
		}
	})
});


var controllers = {};

controllers.ApplicationController = function($scope,$mdDialog, $cookies){
	$scope.userRole = $cookies.get("username").split("_0_")[2];
	
	$scope.showDialog = function(evt, id) {
        $mdDialog.show({
             targetEvent: evt,
             scope: $scope.$new(),
             clickOutsideToClose: true, 
             templateUrl: id
        });
   }
}

/************************************************** User Controller ********************************************************/
controllers.UserController = function($scope, $http) {
	$http.get('rest/users').then(function(response) {
		$scope.users = response.data.users;
	});

	$scope.addUser = function() {
		var user = {
			username : $scope.newUser.userName,
			displayName : $scope.newUser.displayName,
			password : $scope.newUser.password,
			chartType : $scope.newUser.chartType,
			role : $scope.newUser.role
		};
		var request = $.ajax({
			url : "rest/users/save",
			type : "PUT",
			data : JSON.stringify(user),
			dataType : "json",
			contentType : "application/json",
			success : function(resp) {
				alert('Success');
			},
			error : function(e) {
				alert('Failed');
			}
		});
		var found = false;
		for (index = 0; index < $scope.users.length; index++) {
			if ($scope.users[index].username == user.username) {
				found = true;
				$scope.users.splice(index, 1);
				$scope.users.splice(index, 0, user);
				break;
			}
		}
		;
		if (!found) {
			$scope.users.push(user);
		}
	};

	$scope.removeUser = function() {
		var index = $scope.users.findIndex(function(user, i) {
			return user.username === $scope.newUser.userName;
		});
		$scope.users.splice(index, 1);
		var request = $.ajax({
			url : "rest/users/" + $scope.newUser.userName+"/remove",
			type : "DELETE",
			success : function(resp) {
				alert('Success');
			},
			error : function(e) {
				alert('Failed');
			}
		});
	};

	$scope.setUser = function($user) {
		$scope.newUser = [];
		$scope.newUser.displayName = $user.displayName;
		$scope.newUser.userName = $user.username;
		$scope.newUser.password = $user.password;
		$scope.newUser.role = $user.role.toLowerCase();
		$scope.newUser.chartType = $user.chartType;
	};

};


/************************************************** Connection Controller ********************************************************/
controllers.ConnectionController = function($scope, $http, $q,$mdDialog) {
	$http.get('rest/connections').then(
			function(response) {
				$scope.connections = response.data.connections;
			});

	$scope.addConnection = function() {
		var connection = {
			alias : $scope.newConnection.alias,
			username : $scope.newConnection.userName,
			password : $scope.newConnection.password,
			driver : $scope.newConnection.driver,
			isDefault : $scope.newConnection.isDefault,
			url : $scope.newConnection.url
		};
		var request = $.ajax({
			url : "rest/connections/save",
			type : "PUT",
			data : JSON.stringify(connection),
			dataType : "json",
			contentType : "application/json",
			success : function(resp) {
				 $mdDialog.show(
				      $mdDialog.alert()
				        .clickOutsideToClose(true)
				        .title('Save/Update of alias \''+connection.alias+'\'  Succeeded')
				        .ok('Ok')
				    );
			},
			error : function(e) {
				 $mdDialog.show(
					      $mdDialog.alert()
					        .clickOutsideToClose(true)
					        .title('Save/Update of alias \''+connection.alias+'\'  Failed')
					        .ok('Ok')
					    );
			}
		});
		var found = false;
		for (index = 0; index < $scope.connections.length; index++) {
			if ($scope.connections[index].alias == connection.alias) {
				found = true;
				$scope.connections.splice(index, 1);
				$scope.connections.splice(index, 0, connection);
				break;
			}
		}
		;

		if (!found) {
			$scope.connections.push(connection);
		}
	};

	$scope.removeConnection = function() {
		var index = $scope.connections.findIndex(function(connection, i) {
			return connection.alias === $scope.newConnection.alias;
		});
		$scope.connections.splice(index, 1);
		var request = $.ajax({
			url : "rest/connections/" + $scope.newConnection.alias+"/remove",
			type : "DELETE",
			success : function(resp) {
				alert('Success');
			},
			error : function(e) {
				alert('Failed');
			}
		});
	};

	$scope.testConnection = function() {
		var connection = {
			alias : $scope.newConnection.alias,
			username : $scope.newConnection.userName,
			password : $scope.newConnection.password,
			driver : $scope.newConnection.driver,
			isDefault : $scope.newConnection.isDefault,
			url : $scope.newConnection.url
		};
		var request = $.ajax({
			url : "rest/connections/test",
			type : "POST",
			data : JSON.stringify(connection),
			dataType : "json",
			contentType : "application/json",
			success : function(resp) {
				 $mdDialog.show(
					      $mdDialog.alert()
					        .clickOutsideToClose(true)
					        .title('Connection to alias \''+connection.alias+'\'  Succeeded')
					        .ok('Ok')
					    );
			},
			error : function(e, status, error) {
				 $mdDialog.show(
					      $mdDialog.alert()
					        .clickOutsideToClose(true)
					        .title('Connection to alias \''+connection.alias+'\'  Failed')
					        .textContent("Response = "+e.responseText+". Error = "+error+". Status = "+e.status)
					        .ok('Ok')
					    );
			}
		});
	};

	$scope.setConnection = function($conn) {
		$scope.newConnection = [];
		$scope.newConnection.alias = $conn.alias;
		$scope.newConnection.userName = $conn.username;
		$scope.newConnection.password = $conn.password;
		if ($conn.isDefault == 'true') {
			$scope.newConnection.isDefault = true;
		}
		$scope.newConnection.driver = $conn.driver;
		$scope.newConnection.url = $conn.url;
	};

};

/************************************************** ReportList Controller ********************************************************/
controllers.ReportListController = function($scope,$cookies,$stateParams, $http,$q) {
	var userName = $cookies.get("username").split("_0_")[0];
	$scope.userRole = $cookies.get("username").split("_0_")[2];
	var mode = $stateParams.mode;

	if(mode=='public'){
		userName='public';
		$scope.reportMode = 'public';
	}else{
		$scope.reportMode = 'personal';
	}
	$http.get('rest/reports/personal/'+userName).then(function(response) {
		$scope.reports = response.data.reports;
	});

	$scope.deleteReports = function(){
		for (index = 0; index < $scope.reports.length; index++) {
			if ($scope.reports[index].isDeleted == true) {
				var reportTitle = $scope.reports[index].title;
				alert("Deleting report "+reportTitle);
				var getReport = function(){
					var deferred = $q.defer();
					$http.delete('rest/reports/'+userName+'/'+reportTitle+'/delete').then(function(response) {
						deferred.resolve(response);
					});
					return deferred.promise;
				};
				var promise=getReport();
				promise.then(function(response){
					if(response.status == 200){
						var reportIndex=0;
						alert("Deleted report "+response.data+" successfully.");
						for (reportIndex = 0; reportIndex < $scope.reports.length; reportIndex++) {
							if ($scope.reports[reportIndex].title==response.data){
								$scope.reports.splice(reportIndex, 1);		
							}
						}
					}else{
						alert('Unable to delete report '+status.title);
					}
				});
			}
		}
	}
};

/************************************************** Report Controller ********************************************************/
controllers.ReportController = function($scope,$interval,$q,$stateParams,$cookies,$http, $compile,$mdDialog){
	var userName = $cookies.get("username").split("_0_")[0];
	$scope.userRole = $cookies.get("username").split("_0_")[2];
	$scope.userName=userName;
	
	var getConnections = function(){
		var deferred = $q.defer();
		$http.get('rest/connections').then(function(response){
			deferred.resolve(response.data.connections);
		});
		return deferred.promise;
	}
	var promise=getConnections();
	$scope.aliases=["default"];
	promise.then(function(connections){
		for(connection of connections)
			$scope.aliases.push(connection.alias);
	});
	
	if($stateParams.type=='editreport'){
		$scope.reportOpenType='editreport';
	}else if($stateParams.type=='openreport'){
		$scope.reportOpenType='openreport';
	}else if($stateParams.type=='newreport'){
		$scope.reportOpenType='editreport';
	}
	
	if($stateParams.title != null && $stateParams.mode != null){
		$scope.title=$stateParams.title;
		$scope.reportMode=$stateParams.mode;
		var getReport = function(){
			var deferred = $q.defer();
			if($stateParams.mode=='public'){
				$http.get('rest/reports/public/'+$scope.title).then(function(response) {
					deferred.resolve(response.data.reports);
				});
			}else{
				$http.get('rest/reports/'+userName+'/'+$scope.title).then(function(response) {
					deferred.resolve(response.data.reports);
				});
			}
			return deferred.promise;
		};
		var promise=getReport();
		promise.then(function(reports){
			$scope.reports=reports;	
		});
	}else{
		$scope.reports = [];
		var report = {
			title : "",
			description : "",
			rows : [{
					elements:[{
						title:"",
						query:"",
						chartType:"",
						dbalias:"default",
						refreshinterval:"-1"
					}]
				}
			]
		};
		$scope.reports.push(report);
	}
	
	
	var intervalPromises = [];
	var ind = 0;
	
	$scope.export=function(type){
		if(type=='PDF'){
			html2canvas(document.getElementById("reportdiv"), {
				  onrendered: function(canvas) {
				  var extra_canvas = document.createElement("canvas");
			        extra_canvas.setAttribute('width', 1200);
			        extra_canvas.setAttribute('height', 800);
			        var ctx = extra_canvas.getContext('2d');
			        ctx.drawImage(canvas, 0, 0, 1200, 800);
				    var pdf = new jsPDF('l');
				    var marginLeft=0;
				    var marginRight=0;
				    pdf.addImage(extra_canvas.toDataURL("image/jpeg"),"jpeg",marginLeft,marginRight);
				    pdf.save('report.pdf');
				  }
				});
		}
	}
	
	$scope.loadElement = function(element,chartType){
		if(element.title && element.query){
			var id = element.title+"_cell";
			var request = $.ajax({
				url: "rest/reports/element/query",
				type: "POST",
				data: {
					"sqlQuery":element.query,
					"databaseAlias":element.dbalias,
					"chartType":element.chartType},
					success: function(data) {
						if(chartType){
							drawChart(data,id,chartType,element.title);
						}else{
							drawChart(data,id,element.chartType,element.title);
						}
					},
					error: function(e,status,error){
						document.getElementById(id).innerHTML = "Response = "+e.responseText+". Error = "+error+". Status = "+e.status;
					}
			});
		}
	};	

	$scope.$on('$destroy', function() {
		for(i = 0;i<intervalPromises.length;i++){
			var intervalPromise = intervalPromises[i];
			$interval.cancel(intervalPromise);
		}
	});
	
	$scope.addColumn=function(rowId){
		var index = $scope.reports[0].rows[rowId].elements.length;
		var element = {
				title:"Untitled "+rowId+index,
				query:"",
				chartType:"",
				dbalias:"default"
		};
		$scope.reports[0].rows[rowId].elements.push(element);
		editElement(element);
	};
	
	$scope.addRow=function(){
		var rowId = $scope.reports[0].rows.length;
		var row={
				elements:[{
					title:"Untitled "+rowId+"0",
					query:"",
					chartType:"",
					dbalias:"default"
			}]
		};
		$scope.reports[0].rows.push(row);
	};
	
	
	$scope.deleteColumn=function(rowId,colId){
		$scope.reports[0].rows[rowId].elements.splice(colId,1);
		if($scope.reports[0].rows[rowId].elements.length==0){
			$scope.reports[0].rows.splice(rowId,1);
		}
	};

	$scope.deleteLastRow=function(){
		$scope.reports[0].rows.splice(-1,1);
	}
	
	$scope.formatNumber = function(i) {
	    return Math.floor(i); 
	}
	
	$scope.save=function(mode){
		var uName = $cookies.get("username").split("_0_")[0];
		var rName = $scope.reports[0].title;
		if(mode=='public'){
			uName = 'public';
		}		
		var request = $.ajax({
			url: "rest/reports/"+uName+"/"+rName+"/save",
			type : "POST",
			data : JSON.stringify($scope.reports),
			dataType : "json",
			contentType : "application/json",
			success : function(resp) {
				alert('Report '+rName+' Saved!');
			},
			error : function(e) {
				alert('Report '+rName+' Failed');
			}
		});
	};

    $scope.editElement = function(ev,id,element) {
        $mdDialog.show({
            targetEvent: ev,
            locals:{param: element,param2: $scope.aliases},
            clickOutsideToClose: true, 
            scope: $scope.$new(),
            controller:EditElementController,
            templateUrl: id
       }).then(function(modElement) {
    	   element.title=modElement.title;
    	   element.query=modElement.query;
    	   element.chartType=modElement.chartType;
    	   element.refreshinterval=modElement.refreshinterval;
    	   element.dbalias=modElement.dbalias;
    	   $scope.loadElement(element);
       }, function() {
       });
    };

    var EditElementController = function ($scope, param, param2, $mdDialog) {
    	$scope.modElement={};
    	$scope.modElement.title = param.title;
    	$scope.modElement.query = param.query;
    	$scope.modElement.chartType = param.chartType;
    	$scope.modElement.refreshinterval = param.refreshinterval;
    	$scope.modElement.dbalias = param.dbalias;
    	$scope.aliases = param2;

    	$scope.saveElement = function(){
	    	$mdDialog.hide($scope.modElement);
	    }
	    
	    
		$scope.testElement = function(render){
			$scope.render = render;
			var request = $.ajax({
				url: "rest/reports/element/query",
				type: "POST",
				data: {
					"sqlQuery":$scope.modElement.query,
					"databaseAlias":$scope.modElement.dbalias,
					"chartType":$scope.modElement.chartType},
				success: function(data) {
						if($scope.render){
							drawChart(data,'testdata',$scope.modElement.chartType,$scope.modElement.title);
						}else{
							drawChart(data,'testdata','table',$scope.modElement.title);
						}
					},
				error: function(e,status,error){
						document.getElementById('#testresult').innerHTML = "Response = "+e.responseText+". Error = "+error+". Status = "+e.status;
					}
			});
		};
	} 
    
    
    $scope.editTitle = function(ev,id,report) {
        $mdDialog.show({
            targetEvent: ev,
            locals:{param: report},
            clickOutsideToClose: true, 
            scope: $scope.$new(),
            controller:EditReportController,
            templateUrl: id
       }).then(function(modRep) {
    	   $scope.reports[0].title=modRep.title;
    	   $scope.reports[0].description=modRep.description;
       }, function() {
       });
    };
    
    var EditReportController = function ($scope, param, $mdDialog) {
    	$scope.modRep={};
    	$scope.modRep.title = param.title;
    	$scope.modRep.description = param.description;
	    $scope.saveEdit = function(){
	    	$mdDialog.hide($scope.modRep);
	    }
	}  
}
lwrApp.controller(controllers);
