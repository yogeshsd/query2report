var lwrApp = angular.module('LWR', [ 'ngRoute', 'ui.router', 'ui.bootstrap','ngCookies','ngSanitize']);

lwrApp.config(function($stateProvider,$urlRouterProvider){
	$urlRouterProvider.otherwise("/reports");
	$stateProvider
	.state('reports',{
	      url: "/reports",
	      templateUrl: "html/reports.html",
	})
	.state('reports.newreport', {
		url: "/newreport",
		templateUrl: "html/editreport.html",
		controller: 'ReportController'
	})
    .state('reports.list', {
	      url: "/list/:mode",
	      templateUrl: "html/reportlist.html",
	      params: {
	    	    mode:null
	    	  },
	      controller: 'ReportListController'
    })
 	.state('reports.list.openreport',{
	      url: "/openreport/:title?mode",
	      views: {
	    	    '@reports' :{
	    	      templateUrl: "html/openreport.html",
	    	      params: {
	    	    	    title: null,
	    	    	    mode:null
	    	    	  },
	    	      controller: 'ReportController'
	    	    }
	      }
	})
	.state('reports.list.editreport',{
	      url: "/editreport/:title?mode",
	      views: {
	    	    '@reports' :{
	    	      templateUrl: "html/editreport.html",
	    	      params: {
	    	    	    title: null,
	    	    	    mode:null
	    	    	  },
	    	      controller: 'ReportController'
	    	    }
	      }
	})
    .state('reports.usermgmt', {
		url: '/usermgmt',
		views: {
			'@reports' :{
			  templateUrl: "html/usermgmt.html",
			  params: {
				    title: null,
				    mode:null
				  },
			  controller: 'UserController'
			}
		}
	})
	.state('reports.connmgmt', {
			url: '/connmgmt',
			views: {
				'@reports' :{
				  templateUrl: "html/connmgmt.html",
				  params: {
					    title: null,
					    mode:null
					  },
				  controller: 'ConnectionController'
				}
			}			
	})
	.state('reports.getstarted', {
			url : '/getstarted',
			views: {
				'@reports' :{
				  templateUrl: "html/getstarted.html",
				  params: {
					    title: null,
					    mode:null
					  },
				  controller: 'ConnectionController'
				}
			}			
	})
	.state('reports.example', {
			url: '/example',
			views: {
				'@reports' :{
				  templateUrl: "html/example.html",
				  params: {
					    title: null,
					    mode:null
					  },
				  controller: 'ConnectionController'
				}
			}				
	})
});


var controllers = {};
controllers.UserController = function($scope, $http) {
	$http.get('rest/users').then(function(response) {
		$scope.users = response.data.User;
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
			type : "POST",
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
			url : "rest/users/remove/" + $scope.newUser.userName,
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

controllers.ConnectionController = function($scope, $http, $q) {
	$http.get('rest/connections').then(
			function(response) {
				$scope.connections = response.data.Connection;
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
			type : "POST",
			data : JSON.stringify(connection),
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
				alert('Success');
			},
			error : function(xhr, status, error) {
				alert(xhr.responseText);
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

controllers.ReportListController = function($scope,$cookies,$stateParams, $http,$q) {
	var userName = $cookies.get("username").split("_0_")[0];
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
					$http.get('rest/reports/'+userName+'/'+reportTitle+'/delete').then(function(response) {
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

controllers.ReportController = function($scope,$interval,$q,$stateParams,$cookies,$http){
	var userName = $cookies.get("username").split("_0_")[0];
	$scope.userName=userName;
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
						dbalias:"default"
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
			html2canvas(document.body, {
				  onrendered: function(canvas) {
				    var pdf = new jsPDF();
				    var marginLeft=20;
				    var marginRight=20
				    pdf.addImage(canvas.toDataURL("image/jpeg"),"jpeg",marginLeft,marginRight)
				    window.location=pdf.output("datauristring")
				  }
				});
		}
	}
	
	
	$scope.loadElement = function(idParent,idChild,userName,reportName,elementName,chartType){
		var id = reportName+"_"+idParent+"_"+idChild+"_cell";
		if($scope.reportMode=='public'){
			loadElement(id,'public',reportName,elementName,chartType);
		}else{
			loadElement(id,userName,reportName,elementName,chartType);
		}
		intervalPromise = $interval(function(){
			if($scope.reportMode=='public'){
				loadElement(id,'public',reportName,elementName,chartType);
			}
			else{
				loadElement(id,userName,reportName,elementName,chartType);
			}
		},300000);
		
		intervalPromises[ind]=intervalPromise;
		ind++;
	};
	
	$scope.$on('$destroy', function() {
		for(i = 0;i<intervalPromises.length;i++){
			var intervalPromise = intervalPromises[i];
			$interval.cancel(intervalPromise);
		}
	});
	
	$scope.testElement = function($element){
		var request = $.ajax({
			url: "rest/reports/element/test",
			type: "GET",
			data: {
				"sqlQuery":$element.query,
				"databaseAlias":$element.dbalias,
				"chartType":$element.chartType},
			success: function(data) {
					drawTable(data);
				},
			error: function(e,status,error){
				    $("#"+id).html("Response = "+e.responseText+". Error = "+error+". Status = "+e.status);
				}
		});

	};

	$scope.addColumn=function(rowId){
		var element = {
				title:"",
				query:"",
				chartType:"",
				dbalias:"default"
		};
		$scope.reports[0].rows[rowId].elements.push(element);
	};
	
	$scope.addRow=function(rowId){
		var row={
				elements:[{
					title:"",
					query:"",
					chartType:"",
					dbalias:"default"
			}]
		};
		$scope.reports[0].rows.push(row);
	};

	$scope.deleteLastRow=function(){
		$scope.reports[0].rows.splice(-1,1);
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
}

lwrApp.controller(controllers);