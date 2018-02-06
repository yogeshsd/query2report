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
    .state('drivermgmt', {
		url: '/drivermgmt',
		templateUrl: "html/drivermgmt.html",
		params: {
			title: null,
			mode:null
		},
		controller: 'DriverController'
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

controllers.ApplicationController = function($scope,$mdDialog, $cookies,$http){
	$scope.userRole = $cookies.get("username").split("_0_")[2];
	$scope.alerts = [];
	
	$http.get('rest/connections').then(
			function(response) {
				$scope.connections = response.data.connections;
				$scope.alerts[0]="No connections defined.";
			}
		);	

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
controllers.UserController = function($scope, $http,$mdDialog) {
	$scope.modifiedUser={};
	$http.get('rest/users').then(function(response) {
		$scope.users = response.data.users;
	});

    $scope.editUser = function(ev,id,user) {
    	if(user){
        	$scope.modifiedUser=user;
    	}else{
    		$scope.modifiedUser={};
    	}
        $mdDialog.show({
            targetEvent: ev,
            locals:{param: user},
            clickOutsideToClose: true, 
            scope: $scope.$new(),
            templateUrl: id
       }).then(function(modifiedUser) {
       }, function() {
       });
    };

	$scope.addUser = function() {
		var user = {
			username : $scope.modifiedUser.username,
			displayName : $scope.modifiedUser.displayName,
			password : $scope.modifiedUser.password,
			chartType : $scope.modifiedUser.chartType,
			role : $scope.modifiedUser.role
		};
		var request = $.ajax({
			url : "rest/users/save",
			type : "PUT",
			data : JSON.stringify(user),
			dataType : "json",
			contentType : "application/json",
			success : function(resp) {
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
				
				$mdDialog.show(
   				      $mdDialog.alert()
   				        .clickOutsideToClose(true)
   				        .title('Save of user \''+user.username+'\'  Succeeded')
   				        .ok('Ok')
   				    );
			},
			error : function(e) {
				 $mdDialog.show(
   				      $mdDialog.alert()
   				        .clickOutsideToClose(true)
   				        .title('Save of user \''+user.username+'\'  Failed')
   				        .textContent("Response = "+e.responseText+". Error = "+error+". Status = "+e.status)
   				        .ok('Ok')
   				    );
			}
		});
	};

	$scope.removeUser = function(modifiedUser) {
		var request = $.ajax({
			url : "rest/users/" + modifiedUser.username+"/remove",
			type : "DELETE",
			success : function(resp) {
				var index = $scope.users.findIndex(function(user, i) {
					return user.username === modifiedUser.username;
				});
				$scope.users.splice(index, 1);				
  				 $mdDialog.show(
  	   				      $mdDialog.alert()
  	   				        .clickOutsideToClose(true)
  	   				        .title('Delete of user \''+modifiedUser.username+'\'  Succeeded')
  	   				        .ok('Ok')
  	   				    );    				
			},
			error : function(e) {
  				 $mdDialog.show(
  	   				      $mdDialog.alert()
  	   				        .clickOutsideToClose(true)
  	   				        .title('Delete of user \''+modifiedUser.username+'\'  Failed')
  	   				        .textContent("Response = "+e.responseText+". Error = "+error+". Status = "+e.status)
  	   				        .ok('Ok')
  	   				    );    				
			}
		});
	};
};

/************************************************** Connection Controller ********************************************************/
controllers.DriverController = function($scope, $http, $q,$mdDialog) {
	$scope.modifiedDriver={};
	$http.get('rest/drivers').then(
			function(response) {
				$scope.drivers = response.data.drivers;
			});
	
	$scope.editDriver = function(ev,id,driver) {
		if(driver){
			$scope.modifiedDriver=driver;
		}else{
			$scope.modifiedDriver={};
		}
        $mdDialog.show({
            targetEvent: ev,
            locals:{param: driver},
            clickOutsideToClose: true, 
            scope: $scope.$new(),
            templateUrl: id
       }).then(function(modifiedDriver) {
       }, function() {
       });
    };
    
	$scope.addDriver = function() {
		var driver = {
			alias : $scope.modifiedDriver.alias,
			className : $scope.modifiedDriver.className,
			jarFile : $scope.jarFile.name
		};
        var file = $scope.jarFile;
        var uploadUrl = "rest/drivers/save";
        var fd = new FormData();
        fd.append('jarFile', file);
        fd.append('className',driver.className);
        fd.append('alias',driver.alias);
        
		$http.post(uploadUrl, fd, {
		   transformRequest: angular.identity,
		   headers: {'Content-Type': undefined}
		})
		.success(function(){
			var found = false;
			for (index = 0; index < $scope.drivers.length; index++) {
				if ($scope.drivers[index].alias == driver.alias) {
					found = true;
					$scope.drivers.splice(index, 1);
					$scope.drivers.splice(index, 0, driver);
					break;
				}
			}
			;
			if (!found) {
				$scope.drivers.push(driver);
			}			
			$mdDialog.show(
					      $mdDialog.alert()
					        .clickOutsideToClose(true)
					        .title('File upload \''+file.name+'\'  Succeeded.')
					        .ok('Ok')
					    );  
		})
		.error(function(e){
			 $mdDialog.show(
					      $mdDialog.alert()
					        .clickOutsideToClose(true)
					        .title('File upload \''+file.name+'\' Unsuccessful.')
					        .textContent("Response = "+e.responseText+". Error = "+error+". Status = "+e.status)
					        .ok('Ok')
					    );  
		});
	}
	
	$scope.removeDriver = function(modifiedDriver) {
		var request = $.ajax({
			url : "rest/drivers/" + modifiedDriver.alias+"/remove",
			type : "DELETE",
			success : function(resp) {
				var index = $scope.drivers.findIndex(function(driver, i) {
					return driver.alias === modifiedDriver.alias;
				});
				$scope.drivers.splice(index, 1);				
  				 $mdDialog.show(
  	   				      $mdDialog.alert()
  	   				        .clickOutsideToClose(true)
  	   				        .title('Delete of user \''+modifiedDriver.alias+'\'  Succeeded')
  	   				        .ok('Ok')
  	   				    );    				
			},
			error : function(e) {
  				 $mdDialog.show(
  	   				      $mdDialog.alert()
  	   				        .clickOutsideToClose(true)
  	   				        .title('Delete of user \''+modifiedDriver.alias+'\'  Failed')
  	   				        .textContent("Response = "+e.responseText+". Error = "+error+". Status = "+e.status)
  	   				        .ok('Ok')
  	   				    );    				
			}
		});
	};	
}


/************************************************** Connection Controller ********************************************************/
controllers.ConnectionController = function($scope, $http, $q,$mdDialog) {
	$scope.modifiedConnection={};
	$http.get('rest/connections').then(
			function(response) {
				$scope.connections = response.data.connections;
			});

	$http.get('rest/drivers').then(
			function(response) {
				$scope.drivers = response.data.drivers;
			});
	
    $scope.editConnection = function(ev,id,connection) {
    	if(connection){
    		$scope.modifiedConnection=connection;
    		for (index = 0; index < $scope.drivers.length; index++) {
    			if ($scope.drivers[index].className == connection.driver) {
    				$scope.selectedDriver=$scope.drivers[index];
    			}
    		}
    	}else{
    		$scope.modifiedConnection={};
    	}
        $mdDialog.show({
            targetEvent: ev,
            locals:{param: connection},
            clickOutsideToClose: true, 
            scope: $scope.$new(),
            templateUrl: id
       }).then(function(modifiedConnection) {
       }, function() {
       });
    };

	$scope.addConnection = function() {
		var connection = {
			alias : $scope.modifiedConnection.alias,
			username : $scope.modifiedConnection.username,
			password : $scope.modifiedConnection.password,
			driver : $scope.selectedDriver.className,
			isDefault : $scope.modifiedConnection.isDefault,
			url : $scope.modifiedConnection.url
		};
		var request = $.ajax({
			url : "rest/connections/save",
			type : "PUT",
			data : JSON.stringify(connection),
			dataType : "json",
			contentType : "application/json",
			success : function(resp) {
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
				 $mdDialog.show(
				      $mdDialog.alert()
				        .clickOutsideToClose(true)
				        .title('Save of alias \''+connection.alias+'\'  Succeeded')
				        .ok('Ok')
				    );
			},
			error : function(e) {
				 $mdDialog.show(
					      $mdDialog.alert()
					        .clickOutsideToClose(true)
					        .title('Save of alias \''+connection.alias+'\'  Failed')
					        .textContent("Response = "+e.responseText+". Error = "+error+". Status = "+e.status)
					        .ok('Ok')
					    );
			}
		});
	};

	$scope.removeConnection = function(modifiedConnection) {
		var request = $.ajax({
			url : "rest/connections/" + modifiedConnection.alias+"/remove",
			type : "DELETE",
			success : function(resp) {
				var index = $scope.connections.findIndex(function(connection, i) {
					return connection.alias === modifiedConnection.alias;
				});
				$scope.connections.splice(index, 1);
				
				 $mdDialog.show(
				      $mdDialog.alert()
				        .clickOutsideToClose(true)
				        .title('Delete of alias \''+modifiedConnection.alias+'\'  Succeeded')
				        .ok('Ok')
				    );    				
			},
			error : function(e) {
				 $mdDialog.show(
				      $mdDialog.alert()
				        .clickOutsideToClose(true)
				        .title('Delete of alias \''+modifiedConnection.alias+'\'  Failed')
				        .textContent("Response = "+e.responseText+". Error = "+error+". Status = "+e.status)
				        .ok('Ok')
				    );    				
			}
		});
	};

	$scope.testConnection = function(modifiedConnection) {
		var connection = {
			alias : modifiedConnection.alias,
			username : modifiedConnection.username,
			password : modifiedConnection.password,
			driver : modifiedConnection.driver,
			isDefault : modifiedConnection.isDefault,
			url : modifiedConnection.url
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
		$scope.reports[0].aurthor=uName;
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



lwrApp.directive('fileModel', ['$parse', function ($parse) {
return {
   restrict: 'A',
   link: function(scope, element, attrs) {
      var model = $parse(attrs.fileModel);
      var modelSetter = model.assign;
      element.bind('change', function(){
             scope.$apply(function(){
                modelSetter(scope.$parent, element[0].files[0]);
             });
          });
       }
    };
}]);

lwrApp.directive('loading', function () {
	return {
		restrict: 'E',
		replace:true,
		template: '<div><img style="display:block;margin-left:auto;margin-right:auto;padding-top:10%" src="images/loading.gif"/></div>',
		link: function (scope, element, attr) {
		scope.$watch(attr.id, function (val) {
			$(element).show();
		});
		}
	}
})