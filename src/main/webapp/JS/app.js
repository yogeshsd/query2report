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

var lwrApp = angular.module('LWR', [ 'ngRoute', 'ui.router', 'ui.bootstrap','ngCookies','ngSanitize','ngMaterial']);


lwrApp.config(function($stateProvider,$urlRouterProvider){
	$stateProvider
	.state('logout', {
		url: "/logout",
		templateUrl: "logout.html",
		controller: 'AuthenticationController'
	})
	.state('login', {
		url: "/login",
		templateUrl: "login.html",
		controller: 'AuthenticationController'
	})
	.state('home', {
		url: "/home",
		templateUrl: "index.html",
		controller: 'ApplicationController'
	})
	.state('list', {
		url: "/list/:mode",
		templateUrl: "html/reportlist.html",
		params: {
			mode:'public'
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
    .state('alertmgmt', {
		url: '/alertmgmt',
		templateUrl: "html/alertmgmt.html"
    })
    .state('configmgmt', {
		url: '/configmgmt',
		templateUrl: "html/configmgmt.html",
		controller: 'ConfigurationController'
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
	.state('video', {
		url: '/video/:video_source',
		templateUrl: "html/video_player.html",
		params: {
			video_source: null
		},
		controller:'VideoController'
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

lwrApp.service('$configProps', function($http) {
	this.getConfigProperties = function(){
		$http.get('rest/props/').then(
			function(response) {
				return response.data;
			}
		);	
	}
});

var controllers = {};

/************************************************** Authentication Controller ********************************************************/


controllers.AuthenticationController = function($scope,$mdDialog, $cookies,$http,$state,$window){
	console.log("AuthenticationController..");
	$scope.authenticated = false;
	$scope.errorMessage = "";
	
	$scope.reset = function(){
		$scope.username="";
		$scope.password="";
	}
	
	$scope.showDialog = function(evt, id) {
        $mdDialog.show({
             targetEvent: evt,
             scope: $scope.$new(),
             clickOutsideToClose: true, 
             templateUrl: id
        });
    }
	
	$scope.login = function(){
		console.log("AuthenticationController..login called");
		var fd = new FormData();
		fd.append("username",$scope.username);
		fd.append("password",$scope.password);
		console.log("Login request "+$scope.username);
		var request = $.ajax({
			url : "rest/auth/login",
			type : "POST",
			data : fd,
			processData : false,
			contentType : false,			
			success : function(response) {
				console.log("Login request "+$scope.username+" is successful. Redirecting to index.html");
				$cookies.put("Q2R_AUTH_INFO",response);
				$window.location.href = '/q2r/index.html'
			},
			error : function(e) {
				console.log("Login request "+$scope.username+" is failed. Error "+e.responseText);
				$scope.errorMessage = e.responseText;
			}
		});		
	}
	
	$scope.logout = function(){
		console.log("AuthenticationController..logout called");
		var request = $.ajax({
			url : "rest/auth/logout",
			type : "POST",
			processData : false,
			contentType : false,			
			success : function(response) {
				console.log("Successfully logged out.")
			},
			error : function(e) {
				console.log("Logout failed.")
			}
		});		
	}
}

/************************************************** Application Controller ********************************************************/

controllers.ApplicationController = function($scope, $cookies,$http,$state,$mdDialog){
	console.log("ApplicationController: opening home page");
	$scope.userRole = $cookies.get("Q2R_AUTH_INFO").split("_0_")[2];
	$scope.userName = $cookies.get("Q2R_AUTH_INFO").split("_0_")[0];
	$scope.alerts = [];
	
	$http.get('rest/users/'+$scope.userName).then(
			function(response) {
				$scope.user = response.data.users[0];
			}
		);	

	$http.get('rest/props/').then(
			function(response) {
				$scope.props = response.data;
			}
		);	

	$http.get('rest/alerts').then(
			function(response) {
				$scope.alerts = response.data.alerts;
			}
		);	

	$scope.showDialog = function(evt, id) {
		$(".menuitemref").each(function() {
			$(this).removeClass('active');
			$(this).addClass('inactive');
		});
		$(".menuitem").each(function() {
			$(this).removeClass('active');
			$(this).addClass('inactive');
		});
		
		$("#helpmgmt").removeClass("inactive");
		$("#helpmgmtref").removeClass("inactive");
		$("#helpmgmt").addClass("active");
		$("#helpmgmtref").addClass("active");
		
        $mdDialog.show({
             targetEvent: evt,
             scope: $scope.$new(),
             clickOutsideToClose: true, 
             templateUrl: id
        });
    }

	$state.go('list','');
}

/************************************************** Configuration Controller ********************************************************/
controllers.ConfigurationController = function($scope, $http,$stateParams,$mdDialog,$configProps){

	$(".menuitemref").each(function() {
		$(this).removeClass('active');
		$(this).addClass('inactive');
	});
	$(".menuitem").each(function() {
		$(this).removeClass('active');
		$(this).addClass('inactive');
	});
	
	$("#usericon").removeClass("inactive");
	$("#usericonref").removeClass("inactive");
	$("#usericon").addClass("active");
	$("#usericonref").addClass("active");

	
 	$http.get('rest/props/').then(
		function(response) {
			$scope.props = response.data;
		}
	);	
	
	$scope.saveConfig = function(){
		angular.forEach($scope.props, function (value, key) {
            if ($scope.configForm[key].$dirty) {
              $scope.props[key] = $scope.configForm[key].$viewValue;
            }
        });
		var params = JSON.stringify($scope.props);
		var request = $.ajax({
			url : "rest/props",
			type : "PUT",
			data : params,
			dataType : "json",
			contentType : "application/json",			
			success : function(resp) {
  				 $mdDialog.show(
   				      $mdDialog.alert()
   				        .clickOutsideToClose(true)
   				        .title(resp)
   				        .ok('Ok')
   				    );    				
			},
			error : function(e) {
 				 $mdDialog.show(
   				      $mdDialog.alert()
   				        .clickOutsideToClose(true)
   				        .title(e.responseText)
   				        .ok('Ok')
   				    );    				
			}
		});		
	}
}
/************************************************** User Controller ********************************************************/
controllers.UserController = function($scope, $http,$mdDialog) {
	$(".menuitemref").each(function() {
		$(this).removeClass('active');
		$(this).addClass('inactive');
	});
	$(".menuitem").each(function() {
		$(this).removeClass('active');
		$(this).addClass('inactive');
	});

	$("#usermgmt").removeClass("inactive");
	$("#usermgmtref").removeClass("inactive");
	$("#usermgmt").addClass("active");
	$("#usermgmtref").addClass("active");
	
	$scope.modifiedUser={};
	$http.get('rest/users').then(function(response) {
		$scope.users = response.data.users;
	});
	
	$scope.isAddUser=false;

    $scope.editUser = function(ev,id,user,mode) {
    	$scope.isAddUser=mode;
    	if(user && !mode){
        	$scope.modifiedUser={};
        	$scope.modifiedUser.username=user.username;
        	$scope.modifiedUser.displayName=user.displayName;
        	$scope.modifiedUser.chartType=user.chartType;
        	$scope.modifiedUser.role=user.role;
        	$scope.modifiedUser.sessionTimeout=user.sessionTimeout;
    	}else{
    		$scope.modifiedUser={};
    	}
        $mdDialog.show({
            targetEvent: ev,
            locals:{param: user},
            clickOutsideToClose: true, 
            scope: $scope.$new(),
            templateUrl: id
       }).then(function() {
       }, function() {
       });
    };

	$scope.addUser = function() {
		var user = {
			username : $scope.modifiedUser.username,
			displayName : $scope.modifiedUser.displayName,
			password : $scope.modifiedUser.newpassword,
			chartType : $scope.modifiedUser.chartType,
			role : $scope.modifiedUser.role,
			sessionTimeout : $scope.modifiedUser.sessionTimeout
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
   				        .textContent("Response = "+e.responseText+". Status = "+e.status)
   				        .ok('Ok')
   				    );
			}
		});
	};

	$scope.removeUser = function(modifiedUser) {
		var confirm = $mdDialog.confirm()
					.title('Delete User Confirmation')
					.textContent('Do you really want to delete?')
					.ariaLabel('Lucky day')
					.ok('Ok')
					.cancel('Cancel');
		$mdDialog.show(confirm).then(function() {
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
	  	   				        .textContent("Response = "+e.responseText+". Status = "+e.status)
	  	   				        .ok('Ok')
	  	   				    );    				
				}
			});
		});
	};
};

/************************************************** Driver Controller ********************************************************/
controllers.DriverController = function($scope, $http, $q,$mdDialog) {
	$(".menuitemref").each(function() {
		$(this).removeClass('active');
		$(this).addClass('inactive');
	});
	$(".menuitem").each(function() {
		$(this).removeClass('active');
		$(this).addClass('inactive');
	});
	$("#drivermgmt").removeClass("inactive");
	$("#drivermgmtref").removeClass("inactive");
	$("#drivermgmt").addClass("active");
	$("#drivermgmtref").addClass("active");
	
	$scope.modifiedDriver={};
	$http.get('rest/drivers').then(
			function(response) {
				$scope.drivers = response.data.drivers;
			});
	
	
	$scope.isAddDriver=false;
	
	$scope.editDriver = function(ev,id,driver,mode) {
		$scope.isAddDriver=mode;
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
        var file = $scope.jarFile;
        var uploadUrl = "rest/drivers/save";
        var fd = new FormData();
        fd.append('jarFile', file);
        fd.append('className',$scope.modifiedDriver.className);
        fd.append('alias',$scope.modifiedDriver.alias);
        
		$http.post(uploadUrl, fd, {
		   transformRequest: angular.identity,
		   headers: {'Content-Type': undefined}
		})
		.success(function(){
			var found = false;
			if(file){
				$scope.modifiedDriver.jarFile=file.name;
			}
			for (index = 0; index < $scope.drivers.length; index++) {
				if ($scope.drivers[index].alias == $scope.modifiedDriver.alias) {
					found = true;
					$scope.drivers.splice(index, 1);
					$scope.drivers.splice(index, 0, $scope.modifiedDriver);
					break;
				}
			}
			;
			if (!found) {
				$scope.drivers.push($scope.modifiedDriver);
			}			
			$mdDialog.show(
					      $mdDialog.alert()
					        .clickOutsideToClose(true)
					        .title('JDBC Driver \''+$scope.modifiedDriver.alias+'\'  upload Succeeded. Restart the application server.')
					        .ok('Ok')
					    );  
		})
		.error(function(e){
			 $mdDialog.show(
					      $mdDialog.alert()
					        .clickOutsideToClose(true)
					        .title('JDBC Driver \''+$scope.modifiedDriver.alias+'\' Save Unsuccessful.')
					        .textContent("Response = "+e.responseText+". Status = "+e.status)
					        .ok('Ok')
					    );  
		});
	}
	
	$scope.removeDriver = function(modifiedDriver) {
		var confirm = $mdDialog.confirm()
		.title('Delete Driver Confirmation')
		.textContent('Do you really want to delete?')
		.ariaLabel('Lucky day')
		.ok('Ok')
		.cancel('Cancel');
		$mdDialog.show(confirm).then(function() {
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
	  	   				        .textContent("Response = "+e.responseText+". Status = "+e.status)
	  	   				        .ok('Ok')
	  	   				    );    				
				}
			});
		});
	};	
}


/************************************************** Connection Controller ********************************************************/
controllers.ConnectionController = function($scope, $http, $q,$mdDialog) {
	$(".menuitemref").each(function() {
		$(this).removeClass('active');
		$(this).addClass('inactive');
	});
	$(".menuitem").each(function() {
		$(this).removeClass('active');
		$(this).addClass('inactive');
	});
	$("#connmgmt").removeClass("inactive");
	$("#connmgmtref").removeClass("inactive");
	$("#connmgmt").addClass("active");
	$("#connmgmtref").addClass("active");

	$scope.modifiedConnection={};
	$http.get('rest/connections').then(
			function(response) {
				$scope.connections = response.data.connections;
			});

	$http.get('rest/drivers').then(
			function(response) {
				$scope.drivers = response.data.drivers;
			});
	
	$scope.isAddConnection=false;
	
    $scope.editConnection = function(ev,id,connection,mode) {
    	$scope.isAddConnection=mode;
    	if(connection){
    		$scope.modifiedConnection={};
    		$scope.modifiedConnection.alias=connection.alias;
    		$scope.modifiedConnection.username=connection.username;
    		$scope.modifiedConnection.password=connection.password;
    		$scope.modifiedConnection.driver=connection.driver;
    		$scope.modifiedConnection.isDefault=connection.isDefault;
    		$scope.modifiedConnection.url=connection.url;
    		for (index = 0; index < $scope.drivers.length; index++) {
    			if ($scope.drivers[index].alias == connection.driver) {
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
			password : $scope.modifiedConnection.newpassword,
			driver : $scope.selectedDriver.alias,
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
					        .textContent("Response = "+e.responseText+". Status = "+e.status)
					        .ok('Ok')
					    );
			}
		});
	};

	$scope.removeConnection = function(modifiedConnection) {
		var confirm = $mdDialog.confirm()
		.title('Delete Connection Confirmation')
		.textContent('Do you really want to delete?')
		.ariaLabel('Lucky day')
		.ok('Ok')
		.cancel('Cancel');
		$mdDialog.show(confirm).then(function() {
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
					        .textContent("Response = "+e.responseText+". Status = "+e.status)
					        .ok('Ok')
					    );    				
				}
			});
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
					        .textContent("Response = "+e.responseText+". Status = "+e.status)
					        .ok('Ok')
					    );
			}
		});
	};
};

/************************************************** ReportList Controller ********************************************************/
controllers.ReportListController = function($scope,$cookies,$stateParams, $http,$q,$mdDialog) {
	var userName = $cookies.get("Q2R_AUTH_INFO").split("_0_")[0];
	$scope.userRole = $cookies.get("Q2R_AUTH_INFO").split("_0_")[2];
	var mode = $stateParams.mode;

	$(".menuitemref").each(function() {
		$(this).removeClass('active');
		$(this).addClass('inactive');
	});
	$(".menuitem").each(function() {
		$(this).removeClass('active');
		$(this).addClass('inactive');
	});
	
	if(mode=='public'){
		userName='public';
		$scope.reportMode = 'public';
		$("#publicmgmt").removeClass("inactive");
		$("#publicmgmtref").removeClass("inactive");
		$("#publicmgmt").addClass('active');
		$("#publicmgmtref").addClass('active');
	}else{
		$scope.reportMode = 'personal';
		$("#personalmgmt").removeClass("inactive");
		$("#personalmgmtref").removeClass("inactive");
		$("#personalmgmt").addClass('active');
		$("#personalmgmtref").addClass('active');
	}
	$http.get('rest/reports/personal/'+userName).then(function(response) {
		$scope.reports = response.data.reports;
	});

	$scope.deleteReports = function(){
		var confirm = $mdDialog.confirm()
		.title('Delete Report Confirmation')
		.textContent('Do you really want to delete?')
		.ariaLabel('Lucky day')
		.ok('Ok')
		.cancel('Cancel');
		$mdDialog.show(confirm).then(function() {
			for (index = 0; index < $scope.reports.length; index++) {
				if ($scope.reports[index].isDeleted == true) {
					var reportTitle = $scope.reports[index].title;
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
							 $mdDialog.show(
								      $mdDialog.alert()
								        .clickOutsideToClose(true)
								        .title('Deletion of report \''+response.data+'\'  Succeeded')
								        .ok('Ok')
								    );
							for (reportIndex = 0; reportIndex < $scope.reports.length; reportIndex++) {
								if ($scope.reports[reportIndex].title==response.data){
									$scope.reports.splice(reportIndex, 1);		
								}
							}
						}else{
							 $mdDialog.show(
								      $mdDialog.alert()
								        .clickOutsideToClose(true)
								        .title('Deletion of report \''+status.title+'\'  failed')
								        .ok('Ok')
								    );
						}
					});
				}
			}
		});
	}
};

/************************************************** Report Controller ********************************************************/
controllers.ReportController = function($scope,$interval,$q,$stateParams,$cookies,$http, $compile,$mdDialog){
	var userName = $cookies.get("Q2R_AUTH_INFO").split("_0_")[0];
	$scope.userRole = $cookies.get("Q2R_AUTH_INFO").split("_0_")[2];
	$scope.userName=userName;

	$(".menuitemref").each(function() {
		$(this).removeClass('active');
		$(this).addClass('inactive');
	});
	$(".menuitem").each(function() {
		$(this).removeClass('active');
		$(this).addClass('inactive');
	});
	
	if($stateParams.mode=='public'){
		$("#publicmgmt").removeClass("inactive");
		$("#publicmgmtref").removeClass("inactive");
		$("#publicmgmt").addClass('active');
		$("#publicmgmtref").addClass('active');
	}else if($stateParams.mode=='personal'){
		$("#personalmgmt").removeClass("inactive");
		$("#personalmgmtref").removeClass("inactive");
		$("#personalmgmt").addClass('active');
		$("#personalmgmtref").addClass('active');
	}
	
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
		$("#newreport").removeClass("inactive");
		$("#newreportref").removeClass("inactive");
		$("#newreport").addClass('active');
		$("#newreportref").addClass('active');
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
			if(reports[0]){
				$scope.reportParams = reports[0].params;
				if($scope.reportParams && $scope.reportParams.length>0){
					$mdDialog.show({
						locals:{params: $scope.reportParams},
						clickOutsideToClose: true, 
						scope: $scope.$new(),
						templateUrl: 'html/params_dialog.html'
					}).then(function(reportParams) {
					}, function() {
						var rows = $scope.reports[0].rows;
						for(var index=0;index<rows.length;index++){
							var cols = rows[index];
							for(var colIndex = 0; colIndex<cols.elements.length;colIndex++){
								var col = cols.elements[colIndex];
								var id = col.title+"_cell";
								var html = "<div style=\"margin:5px\"><span>Following report parameters are not applied.</span>";
								html=html+"<ul>";
								for(var paramIndex=0;paramIndex<$scope.reportParams.length;paramIndex++){
									html=html+"<li>"+$scope.reportParams[paramIndex].name+"</li>";
								}
								html=html+"</ul></div>";
								document.getElementById(id).innerHTML=html;
							}
						}
					});
				}
			}
		});
	}else{
		$scope.reports = [];
		var report = {
			title : "",
			description : "",
			rows : [{
					elements:[{
						title:"Untitled 00",
						query:"",
						chartType:"",
						dbalias:"default",
						refreshInterval:"-1",
						colSpan:"1"
					}]
				}
			]
		};
		$scope.reports.push(report);
	}
	
	$scope.applyParams = function() {
		$mdDialog.hide();
		for(var paramIndex=0;paramIndex<$scope.reportParams.length;paramIndex++){
			if( $scope.reportParams[paramIndex].dataType=='date' ){
				var d = new Date($scope.reportParams[paramIndex].value);
				var mm = d.getMonth()+1;
				var dd = d.getDate();
				var yy = d.getFullYear();
				if(mm<10)
					mm="0"+mm;
				if(dd<10)
					dd="0"+dd;
				var formattedDate = mm+"/"+dd+"/"+yy+" 00:00:00";
				$scope.reportParams[paramIndex].value=formattedDate;
			}else if( $scope.reportParams[paramIndex].dataType=='datetime' ){
				var d = new Date($scope.reportParams[paramIndex].value);
				var mm = d.getMonth()+1;
				var dd = d.getDate();
				var yy = d.getFullYear();
				var hh = d.getHours();
				var mi = d.getMinutes();
				var ss = d.getSeconds();
				if(mm<10)
					mm="0"+mm;
				if(dd<10)
					dd="0"+dd;
				if(hh<10)
					hh="0"+hh;
				if(mi<10)
					mi="0"+mi;
				if(ss<10)
					ss="0"+ss;
				var formattedDate = mm+"/"+dd+"/"+yy+" "+hh+":"+mi+":"+ss;
				$scope.reportParams[paramIndex].value=formattedDate;
			}
		}
		
		var rows = $scope.reports[0].rows;
		var params = [];
		for(var index=0;index<rows.length;index++){
			var cols = rows[index];
			for(var colIndex = 0; colIndex<cols.elements.length;colIndex++){
				var col = cols.elements[colIndex];
				col.params = $scope.reportParams;
				col.paramsApplied=true;
				$scope.loadElement(col,col.chartType);
			}
		}
	}
	
	var intervalPromises = [];
	var ind = 0;
	
	$scope.export=function(type){
		if(type=='PDF'){
			var element = document.getElementById("root");
			html2canvas(element).then(function(canvas) {
				var imgData = canvas.toDataURL('image/png');
				var doc = new jsPDF('landscape','in','A2');
				doc.addImage(imgData, 'PNG', 1, 1);
				doc.save('sample-file.pdf');
			});
		}else if(type=='CSV'){
			var url='';
			if($stateParams.mode=='public'){
				url = 'rest/export/csv/public/'+$scope.reports[0].title;
			}else{
				 url = 'rest/export/csv/'+$scope.userName+'/'+$scope.reports[0].title;
			}
			$http({
			 	url: url,
		        method: "POST",
		        data: JSON.stringify($scope.reportParams)
			}).then(function(response) {
                var file = new Blob([ response.data ], {
                    type:'application/csv'
                });
                var fileURL = URL.createObjectURL(file);
                var a = document.createElement('a');
                a.href = fileURL;
                a.target  = '_blank';
                a.download  = $scope.reports[0].title+'.csv';
                document.body.appendChild(a);
                a.click();
                $scope.isDownloading=false;
			});				
		}else if(type=='EXCEL'){
			var url='';
			if($stateParams.mode=='public'){
				url = 'rest/export/excel/public/'+$scope.reports[0].title;
			}else{
				url = 'rest/export/excel/'+$scope.userName+'/'+$scope.reports[0].title;	
			}
			$http({
			 	url: url,
		        method: "POST",
		        responseType: 'arraybuffer',
		        data: JSON.stringify($scope.reportParams)
			}).then(function(resposne) {
                var file = new Blob([ resposne.data ], {
                    type:'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
                });
                var fileURL = URL.createObjectURL(file);
                var a = document.createElement('a');
                a.href = fileURL;
                a.target  = '_blank';
                a.download  = $scope.reports[0].title+'.xlsx';
                document.body.appendChild(a);
                a.click();
                $scope.isDownloading=false;
			});				
		}
	}
	
	function base64toBlob(base64Data, contentType) {
	    contentType = contentType || '';
	    var sliceSize = 1024;
	    var byteCharacters = atob(base64Data);
	    var bytesLength = byteCharacters.length;
	    var slicesCount = Math.ceil(bytesLength / sliceSize);
	    var byteArrays = new Array(slicesCount);

	    for (var sliceIndex = 0; sliceIndex < slicesCount; ++sliceIndex) {
	        var begin = sliceIndex * sliceSize;
	        var end = Math.min(begin + sliceSize, bytesLength);

	        var bytes = new Array(end - begin);
	        for (var offset = begin, i = 0; offset < end; ++i, ++offset) {
	            bytes[i] = byteCharacters[offset].charCodeAt(0);
	        }
	        byteArrays[sliceIndex] = new Uint8Array(bytes);
	    }
	    return new Blob(byteArrays, { type: contentType });
	}
	
	$scope.loadElement = function(element,chartType){
		if(element.title && element.query && ( !element.hasParams || element.paramsApplied ) ){
			loadData(element,chartType);
			if(element.refreshInterval > 0){
				setInterval(function() {
					loadData(element,chartType);
				},element.refreshInterval*1000);
			}
		}
	};
	
	function loadData(element,chartType){
		var id = element.title+"_cell";
		var request = $.ajax({
			url: "rest/reports/element/query",
			type: "POST",
			dataType:"json",
			contentType: 'application/json',
			data: JSON.stringify(element),
				success: function(data) {
					if(element.chartType){
						drawChart(data,id,chartType,element.title);
					}else{
						drawChart(data,id,chartType,element.title);
					}
				},
				error: function(e,status,error){
					document.getElementById(id).innerHTML = "Response = "+e.responseText+". Error = "+error+". Status = "+e.status;
				}
		});
	}

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
				dbalias:"default",
				colSpan:1
		};
		if(!$scope.reports[0].rows[rowId].numCols){
			$scope.reports[0].rows[rowId].numCols=1;
		}
		$scope.reports[0].rows[rowId].numCols=$scope.reports[0].rows[rowId].numCols+element.colSpan;
		$scope.reports[0].rows[rowId].elements.push(element);
		editElement(element);
	};
	
	$scope.addRow=function(){
		var rowId = $scope.reports[0].rows.length;
		var row={
				numCols:1,
				elements:[{
					title:"Untitled "+rowId+"0",
					query:"",
					chartType:"",
					dbalias:"default",
					colSpan:1
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
		var uName = $cookies.get("Q2R_AUTH_INFO").split("_0_")[0];
		var rName = $scope.reports[0].title;
		$scope.reports[0].aurthor=uName;
		var rows = $scope.reports[0].rows;
		var params = [];
		for(var index=0;index<rows.length;index++){
			var cols = rows[index];
			for(var colIndex = 0; colIndex<cols.elements.length;colIndex++){
				var col = cols.elements[colIndex];
				
				// Parse Report Parameters
				var patterns = col.query.match(/[^{}]+(?=\})/g);
				if(patterns){
					col.hasParams=true;
					for(var i = 0; i<patterns.length;i++){
						var param = {};
						var subPatterns = patterns[i].split(':');
						if(subPatterns.length==2){
							param.name=subPatterns[1];
							param.dataType=subPatterns[0];
						}else{
							param.name=patterns[i];
							param.dataType='string';
						}
						param.value='';
						found=false;
						for( var j = 0;j<params.length;j++){
							if(params[j].name == param.name && params[j].dataType==param.dataType)
								found=true;
						}
						if(!found)
							params.push(param);
					}
				}else{
					col.hasParams=false;
				}
			}
		}
		$scope.reports[0].params = params;
		if(mode=='public'){
			uName = 'public';
		}
		var request = $.ajax({
			url: "rest/reports/"+uName+"/"+rName+"/save",
			type : "POST",
			data : JSON.stringify($scope.reports),
			contentType : "application/json",
			success : function(resp) {
				$mdDialog.show(
	   				      $mdDialog.alert()
	   				        .clickOutsideToClose(true)
	   				        .title('Save of report \''+rName+'\'  Succeeded')
	   				        .ok('Ok')
	   				    );
			},
			error : function(e) {
				$mdDialog.show(
	   				      $mdDialog.alert()
	   				        .clickOutsideToClose(true)
	   				        .title('Save of report \''+rName+'\'  failed with response '+e.responseText+' status : '+e.status)
	   				        .ok('Ok')
	   				    );
			}
		});
	};

    $scope.editElement = function(ev,id,element,row) {
    	element.params = $scope.reportParams;
    	var origColSpan = element.colSpan;
        $mdDialog.show({
            targetEvent: ev,
            locals:{element: element,alias: $scope.aliases,row: row},
            clickOutsideToClose: true, 
            scope: $scope.$new(),
            controller:EditElementController,
            templateUrl: id
       }).then(function(modElement) {
    	   element.title=modElement.title;
    	   element.query=modElement.query;
    	   element.chartType=modElement.chartType;
    	   element.refreshInterval=modElement.refreshInterval;
    	   element.dbalias=modElement.dbalias;
    	   element.params = modElement.params;
    	   element.paramsApplied = modElement.paramsApplied;
    	   element.colSpan = modElement.colSpan;
    	   if(origColSpan!=modElement.colSpan){
				for(var colIndex = 0; colIndex<row.elements.length;colIndex++){
					var col = row.elements[colIndex];
					col.params = $scope.reportParams;
					col.paramsApplied=true;
					$scope.loadElement(col,col.chartType);
				}
    	   }else{
    		   $scope.loadElement(element,element.chartType);    		   
    	   }
       }, function() {
       });
    };

    var EditElementController = function ($scope, element, alias, row,$mdDialog) {
    	$scope.modElement={};
    	$scope.modElement=element;
    	$scope.origColSpan = element.colSpan;
    	$scope.aliases = alias;
    	$scope.row=row;

    	if(!$scope.modElement.params){
    		$scope.modElement.params=[];
    	}
		for(var paramIndex=0;paramIndex<$scope.modElement.params.length;paramIndex++){
			if( $scope.modElement.params[paramIndex].dataType=='date' ||  $scope.modElement.params[paramIndex].dataType=='datetime'){
				if(!$scope.modElement.params[paramIndex].value){
//					$scope.modElement.params[paramIndex].raw=new Date();
				}else{
					$scope.modElement.params[paramIndex].raw=$scope.modElement.params[paramIndex].value;
				}
			}
		}
    	$scope.saveElement = function(){
	    	$mdDialog.hide($scope.modElement);
	    }
    	
    	$scope.changeElementSpan = function(){
    		if($scope.modElement.colSpan!=''){
    				$scope.row.numCols=$scope.row.numCols+($scope.modElement.colSpan-$scope.origColSpan);
    		}
    	}
    	
		$scope.refreshElement = function(){
			if(!$scope.modElement.query) {
				return;
			}
			
			var patterns = $scope.modElement.query.match(/[^{}]+(?=\})/g);
			if(patterns){
				$scope.modElement.hasParams=true;
				for(var i = 0; i<patterns.length;i++){
					var param = {};
					var subPatterns = patterns[i].split(':');
					if(subPatterns.length==2){
						param.name=subPatterns[1];
						param.dataType=subPatterns[0];
					}else{
						param.name=patterns[i];
						param.dataType='string';
					}
					param.value='';
					found=false;
					for( var j = 0;j<$scope.modElement.params.length;j++){
						if($scope.modElement.params[j].name == param.name && $scope.modElement.params[j].dataType==param.dataType)
							found=true;
					}
					if(!found){
						if( param.dataType=='date' ||  param.dataType=='datetime'){
							//param.raw=new Date();
						}
						
						$scope.modElement.params.push(param);
					}
				}
			}else{
				$scope.modElement.hasParams=false;
			}
			
			
			for(var paramIndex=0;paramIndex<$scope.modElement.params.length;paramIndex++){
				if( $scope.modElement.params[paramIndex].dataType=='date' ){
					var d = new Date($scope.modElement.params[paramIndex].raw);
					var mm = d.getMonth()+1;
					var dd = d.getDate();
					var yy = d.getFullYear();
					if(mm<10)
						mm="0"+mm;
					if(dd<10)
						dd="0"+dd;
					var formattedDate = mm+"/"+dd+"/"+yy+" 00:00:00";
//					$scope.modElement.params[paramIndex].raw= new Date($scope.modElement.params[paramIndex].value);
					$scope.modElement.params[paramIndex].value=formattedDate;
				}else if( $scope.modElement.params[paramIndex].dataType=='datetime' ){
					var d = new Date($scope.modElement.params[paramIndex].raw);
					var mm = d.getMonth()+1;
					var dd = d.getDate();
					var yy = d.getFullYear();
					var hh = d.getHours();
					var mi = d.getMinutes();
					var ss = d.getSeconds();
					if(mm<10)
						mm="0"+mm;
					if(dd<10)
						dd="0"+dd;
					if(hh<10)
						hh="0"+hh;
					if(mi<10)
						mi="0"+mi;
					if(ss<10)
						ss="0"+ss;
					var formattedDate = mm+"/"+dd+"/"+yy+" "+hh+":"+mi+":"+ss;
//					$scope.modElement.params[paramIndex].raw= new Date($scope.modElement.params[paramIndex].value);
					$scope.modElement.params[paramIndex].value=formattedDate;
				}
			}
			
			var noParams = [];
			if($scope.modElement.params && $scope.modElement.params.length>0){
				for(var paramIndex=0;paramIndex<$scope.modElement.params.length;paramIndex++){
					if(!$scope.modElement.params[paramIndex].value || $scope.modElement.params[paramIndex].value.includes("NaN")){
						noParams.push($scope.modElement.params[paramIndex].name);
					}
				}
			}
			if(noParams.length>0){
				var html = "<div style=\"margin:5px\"><span>Following report parameters are not applied.</span>";
				html=html+"<ul>";
				for(var paramIndex=0;paramIndex<noParams.length;paramIndex++){
					html=html+"<li>"+noParams[paramIndex]+"</li>";
				}
				html=html+"</ul></div>";
				document.getElementById('tabledata').innerHTML=html;
				document.getElementById('chartdata').innerHTML=html;

			}else{
				$scope.tabledata=false;
				$scope.chartdata=false;
				$scope.modElement.paramsApplied = true;
				var request = $.ajax({
					url: "rest/reports/element/query",
					type: "POST",
					dataType:"json",
					contentType: 'application/json',
					data: JSON.stringify($scope.modElement),
					success: function(data) {
						drawChart(data,'chartdata',$scope.modElement.chartType,$scope.modElement.title);
						$scope.chartdata=false;
						drawChart(data,'tabledata','table',$scope.modElement.title);
						$scope.tabledata=false;
					},
					error: function(e,status,error){
						document.getElementById('tabledata').innerHTML = "Response = "+e.responseText+". Error = "+error+". Status = "+e.status;
					}
				});
			}
		};
	} 

    $scope.cancel = function(){
    	$mdDialog.hide();
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


/*************** Video Controller ******************************/

controllers.VideoController = function($scope,$stateParams, $sce ){
	var url = "";
	$scope.video_descr="";
	
	$(".menuitemref").each(function() {
		$(this).removeClass('active');
		$(this).addClass('inactive');
	});
	$(".menuitem").each(function() {
		$(this).removeClass('active');
		$(this).addClass('inactive');
	});
	
	$("#"+$stateParams.video_source).removeClass("inactive");
	$("#"+$stateParams.video_source+"ref").removeClass("inactive");
	$("#"+$stateParams.video_source).addClass('active');
	$("#"+$stateParams.video_source+"ref").addClass('active');

	
	if($stateParams.video_source == 'concepts'){
		url = "https://www.youtube.com/embed/NdEUZ2suiv8";
		$scope.video_descr="Concepts Guide";
	}else if($stateParams.video_source == 'reportshowcase'){
		url = "https://www.youtube.com/embed/gxlEGq5iSm8";
		$scope.video_descr="Reports Showcase";
	}else if($stateParams.video_source == 'gettingstarted'){
		url = "https://www.youtube.com/embed/vyU7BUE5rbs";
		$scope.video_descr="Getting Started";
	}else if($stateParams.video_source == 'buildingreport'){
		url = "https://www.youtube.com/embed/MZm6rhf2_Ts";
		$scope.video_descr="Building Report";
	}
	$scope.video_source = $sce.trustAsResourceUrl(url);
}


/***************************************************************/

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

