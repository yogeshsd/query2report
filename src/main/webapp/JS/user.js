/**
 *
 */
	function deleteUser(username){
		var x = confirm("You are deleting user '"+username+"'!");
		if(x){
			var request = $.ajax({
				url: "rest/users/remove/"+username,
				type: "POST",
				success: function(resp){
					$('table#admintable tr#'+username).remove();
					$("#savetext").html(resp);
				},
				error: function(e){
					$("#savetext").html(e.responseText);
				}
			});
		}
	}
	function selectUser(row){
		var index = 0;
		$("#admintable tr").css('background','#FFFFFF');
		$(row).css('background','#8ec252')
		$(row).find('td').each(function(){
			var column = $(this);
			if(index == 0 ){
				$("#displayname").val(column.html());
				index++;
			}else if(index == 1){
				$("#username").val(column.html());
				index++;
			}else if(index == 2){
				$("#password").val(column.html());
				index++;
			}else if(index == 3){
				$("#role_select").val(column.html());
				index++;
			}
		});
	}
	function saveUser(){
		var root=[];
		var users={};
		var displayName = $("#displayname").val();
		var username = $("#username").val();
		var password = $("#password").val();
		var role = $("#role_select").val();
		if( role == ''){
			alert("Role cannot be null!");
		}else if (username ==''){
			alert("Username cannot be null!");
		}else if (password ==''){
			alert("Driver cannot be null!");
		}else{
			users["displayname"]=displayName;
			users["username"]=username;
			users["password"]=password;
			users["role"]=role;
			var request = $.ajax({
				url: "rest/users/save?displayname="+displayName+"&username="+username+"&password="+password+"&role="+role,
				type: "POST",
				success: function(resp){
							var row = $("#"+username);
							if( row.length == 0){
								var row = "<tr onclick=\"selectUser(this)\" id=\""+username+"\"><td>"+displayName+"</td><td>"+username+"</td><td>"+password+"</td><td>"+role+"</td><td><img src=\"images/sign-delete.png\" onclick=\"deleteUser(\'"+username+"\')\"></img></td></tr>";
								$("#admintable").append(row);
								$("#savetext").html(resp);
							}
						},
				error: function(e){
						$("#savetext").html(e.responseText);
					}
				});
		}
	}


	function saveProfile(){
		var users={};
		var displayName = $("#displayname").val();
		var username = $("#username").val();
		var password = $("#password").val();
		var role = $("#role").val();
		var chartType = $("#chartoption").val();
		var refreshInterval = $("#refreshInterval").val();
		users["displayname"]=displayName;
		users["username"]=username;
		users["password"]=password;
		users["role"]=role;
		users["charttype"]=chartType;
		users["refreshInterval"]=refreshInterval;
		var request = $.ajax({
			url: "rest/users/save?displayname="+displayName+"&username="+username+"&password="+password+"&role="+role+"&charttype="+chartType+"&refreshInterval="+refreshInterval,
			type: "POST",
			success: function(resp){
						$("#savetext").html(resp);
					},
			error: function(e){
					$("#savetext").html(e.responseText);
				}
			});
	}