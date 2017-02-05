/**
 *
 */
	function deleteConnection(alias){
		var x = confirm("You are deleting connection '"+alias+"'!");
		if(x){
			var request = $.ajax({
				url: "rest/connections/remove/"+alias,
				type: "POST",
				success: function(resp){
					$('table#admintable tr#'+alias).remove();
					$("#savetext").html(resp);
				},
				error: function(e){
					$("#savetext").html(e.responseText);
				}
			});
		}
	}

	function selectConnection(row){
		var index = 0;
		$("#admintable tr").css('background','#FFFFFF');
		$(row).css('background','#8ec252')
		$(row).find('td').each(function(){
			var column = $(this);
			if(index == 0){
				var n = column.html().indexOf("<img");
				var a = column.html();
				$("#isdefault").prop('checked', false);
				if(n !== -1){
					a = column.html().substring(0,n);
					$("#isdefault").prop('checked', true);
				}
				$("#alias").val(a);
				index++;
			}else if(index == 1){
				$("#username").val(column.html());
				index++;
			}else if(index == 2){
				$("#password").val(column.html());
				index++;
			}else if(index == 3){
				$("#driver").val(column.html());
				index++;
			}else if(index == 4){
				$("#url").val(column.html());
				index++;
			}
		});
	}

	function testConnection(){
		var alias = $("#alias").val();
		var isdefault = $("#isdefault").prop('checked');
		var username = $("#username").val();
		var password = $("#password").val();
		var driver = $("#driver").val();
		var url = $("#url").val();
		if( alias == ''){
			alert("Alias cannot be null!");
		}else if (username ==''){
			alert("Username cannot be null!");
		}else if (driver ==''){
			alert("Driver cannot be null!");
		}else if (url ==''){
			alert("url cannot be null!");
		}else{
			$("#savetext").html("<img src=\"images/loading.gif\" style=\"width:75px;height:75px\"></img>");
			var request = $.ajax({
				url: "rest/connections/test?username="+username+"&password="+password+"&alias="+alias+"&url="+url+"&isdefault="+isdefault+"&driver="+driver,
				type: "POST",
				success: function(resp){
							var row = $("#"+alias+"_conn");
							var img = "<span class=\"label label-success\">Success</span>";
							row.html(img);
							$("#savetext").html(resp);
						},
				error: function(e){
							var row = $("#"+alias+"_conn");
							var img = "<span class=\"label label-danger\">Failed</span>";
							row.html(img);
							$("#savetext").html("<h6>"+e.responseText+"</h6>");
					}
				});
		}
	}

	function saveConnection(){
		var alias = $("#alias").val();
		var isdefault = $("#isdefault").prop('checked');
		var username = $("#username").val();
		var password = $("#password").val();
		var driver = $("#driver").val();
		var url = $("#url").val();
		if( alias == ''){
			alert("Alias cannot be null!");
		}else if (username ==''){
			alert("Username cannot be null!");
		}else if (driver ==''){
			alert("Driver cannot be null!");
		}else if (url ==''){
			alert("url cannot be null!");
		}else{
			var request = $.ajax({
				url: "rest/connections/save?username="+username+"&password="+password+"&alias="+alias+"&url="+url+"&isdefault="+isdefault+"&driver="+driver,
				type: "POST",
				success: function(resp){
							var row = $("#"+alias);
							if( row.length == 0){
								//
							}else{
								$('table#admintable tr#'+alias).remove();
							}
							var row ="";
							if(isdefault){
								row = "<tr style=\"min-width:30px\" onclick=\"selectConnection(this)\" id=\""+alias+"\"><td>"+alias+"<img alt=\"Default Connection\" src=\"images/sign-greentick.png\"/></td><td>"+username+"</td><td>"+password+"</td><td>"+driver+"</td><td>"+url+"</td><td style=\"min-width:30px\"><img src=\"images/sign-delete.png\" onclick=\"deleteRow(\'"+alias+"\')\"></img></td><td style=\"min-width:30px\" id=\""+alias+"_conn\"><span class=\"label label-danger\">Failed</span></td></tr>";
							}else{
								row = "<tr style=\"min-width:30px\" onclick=\"selectConnection(this)\" id=\""+alias+"\"><td>"+alias+"</td><td>"+username+"</td><td>"+password+"</td><td>"+driver+"</td><td>"+url+"</td><td style=\"min-width:30px\"><img src=\"images/sign-delete.png\" onclick=\"deleteRow(\'"+alias+"\')\"></img></td><td style=\"min-width:30px\" id=\""+alias+"_conn\"><span class=\"label label-danger\">Failed</span></td></tr>";
							}
							$("#admintable").append(row);
							$("#savetext").html(resp);
						},
				error: function(e){
						$("#savetext").html(e.responseText);
					}
				});
		}
	}

