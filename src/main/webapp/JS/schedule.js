/**
 *
 */
	function deleteSchedule(schedulename){
		var x = confirm("You are deleting schedule '"+schedulename+"'!");
		if(x){
			var username = "public";
			username=$("#usernamehidden").val();
			var request = $.ajax({
				url: "rest/schedules/remove/"+schedulename+"/"+username,
				type: "POST",
				success: function(resp){
					$('table#admintable tr#'+schedulename).remove();
					$("#savetext").html(resp);
				},
				error: function(e){
					$("#savetext").html(e.responseText);
				}
			});
		}
	}
	function selectSchedule(row){
		var index = 0;
		var recurrence="HOURLY";
		$("#admintable tr").css('background','#FFFFFF');
		$(row).css('background','#8ec252')
		$(row).find('td').each(function(){
			var column = $(this);
			if(index == 0 ){
				$("#schedulename").val(column.html());
				index++;
			}else if(index == 1){
				$("#reportname").val(column.html());
				index++;
			}else if(index == 2){
				$("#format_select").val(column.html());
				index++;
			}else if(index == 3){
				index++;
				var dest = column.html();
				$("#"+dest).prop("checked",true);
				if(dest == "EMAIL"){
					$("#smtpconfig").show();
					$("#PersonalFolders").prop("checked",false);
				}else if(dest == "PersonalFolders"){
					$("#smtpconfig").hide();
					$("#EMAIL").prop("checked",false);

				}
			}else if(index == 4){
				$("#start_date").val(column.html().replace(" ","T"));
				index++;
			}else if(index == 5){
				index++;
				recurrence = column.html();
				$("#"+recurrence).prop("checked",true);
				if(recurrence == "HOURLY"){
					$("#DAILY").prop("checked",false);
					$("#MONTHLY").prop("checked",false);
					$("#hourly_div").show();
					$("#daily_div").hide();
				}else if(recurrence == "DAILY"){
					$("#HOURLY").prop("checked",false);
					$("#MONTHLY").prop("checked",false);
					$("#daily_div").show();
					$("#hourly_div").hide();
				}else if(recurrence == "MONTHLY"){
					$("#DAILY").prop("checked",false);
					$("#HOURLY").prop("checked",false);
					$("#daily_div").hide();
					$("#hourly_div").hide();
				}
			}else if(index == 6){
				index++;
				var interval = column.html();
				$("#"+recurrence.toLowerCase()+"_frequency").val(interval);
			}else if(index == 7){
				index++;
				var foldername = column.html();
				$("#foldername").val(foldername);
			}else if(index == 8){
				index++;
				var smtphost = column.html();
				$("#smtphost").val(smtphost);
			}else if(index == 9){
				index++;
				var smtpport = column.html();
				$("#smtpport").val(smtpport);
			}else if(index == 10){
				index++;
				var receiveremail = column.html();
				$("#receiveremail").val(receiveremail);
			}else if(index == 11){
				index++;
				var senderemail = column.html();
				$("#senderemail").val(senderemail);
			}
		});
	}
	function saveSchedule(){
		var email = $("#EMAIL").prop('checked');
		var inbox = $("#PersonalFolders").prop('checked');
		var hourly = $("#HOURLY").prop('checked');
		var daily = $("#DAILY").prop('checked');
		var monthly = $("#MONTHLY").prop('checked');
		var hourlyfrequency = $("#hourly_frequency").val();
		var dailyfrequency = $("#daily_frequency").val();
		var startdate = $("#start_date").val();
		startdate=startdate.replace("T"," ");
		var destination = "EMAIL";
		var recurrence="HOURLY";
		var smtphost="NA";
		var smtpport="NA";
		var senderemail="NA";
		var receiveremail="NA";
		var foldername="NA";
		var frequency="-1";
		if(email == true){
			var smtphost = $("#smtphost").val();
			var smtpport = $("#smtpport").val();
			var senderemail = $("#senderemail").val();
			var receiveremail = $("#receiveremail").val();
		}else{
			destination="PersonalFolders"
		}

		if(daily){
			recurrence="DAILY";
			frequency=dailyfrequency;
		}else if(monthly){
			recurrence="MONTHLY";
			frequency="30";
		}else if(hourly){
			frequency=hourlyfrequency;
		}
		var schedulename = $("#schedulename").val();
		var reportname = $("#reportname").val();
		var format = $("#format_select").val();

		var schedules=[];
		var schedule={};
		schedule["scheduleName"]=schedulename;
		schedule["reportName"]=reportname;
		schedule["format"]=format;
		schedule["destination"]=destination;
		schedule["recurrence"]=recurrence;
		schedule["interval"]=frequency;
		schedule["smtpHost"]=smtphost;
		schedule["smtpPort"]=smtpport;
		schedule["senderEmail"]=senderemail;
		schedule["receiverEmail"]=receiveremail;
		schedule["folderName"]="NA";
		schedule["startDate"]=startdate;
		schedules[0]=schedule;
		var username = "public";
		username=$("#usernamehidden").val();
		var request = $.ajax({
			url: "rest/schedules/save?schedules="+JSON.stringify(schedules)+"&userName="+username,
			type: "POST",
			success: function(resp){
						var row = $("#"+schedulename);
						if( row.length == 0){
							var row = "<tr onclick=\"selectSchedule(this)\" id=\""+schedulename+"\"><td>"+schedulename+"</td><td>"+reportname+"</td><td>"+format+"</td><td>"+destination+"</td><td>"+startdate+"</td><td>"+recurrence+"</td><td>"+frequency+"</td><td style=\"display:none\">"+foldername+"</td><td style=\"display:none\">"+smtphost+"</td><td style=\"display:none\">"+smtpport+"</td><td style=\"display:none\">"+receiveremail+"</td><td style=\"display:none\">"+senderemail+"</td><td style=\"width:50px\"><img src=\"images/sign-delete.png\" onclick=\"deleteSchedule(\'"+schedulename+"\')\"></img></td></tr>";
							$("#admintable").append(row);
							$("#savetext").html(resp);
						}
					},
			error: function(e){
					$("#savetext").html(e.responseText);
				}
			});

	}
	function selectDestination(ele){
		var dest=ele;
		if(dest == 'EMAIL' ){
			$("#smtpconfig").show();
			$('#PersonalFolders').prop("checked",false);
		}else if(dest == 'PersonalFolders'){
			$("#smtpconfig").hide();
			$('#EMAIL').prop("checked",false)
		}
	}
	function selectRecurrence(value){
		if(value == 'HOURLY'){
			$('#hourly_div').show();
			$('#daily_div').hide();
			$('#monthly_div').hide();
			$('#DAILY').prop("checked",false);
			$('#MONTHLY').prop("checked",false);
		}else if(value == 'DAILY'){
			$('#daily_div').show();
			$('#hourly_div').hide();
			$('#monthly_div').hide();
			$('#HOURLY').prop("checked",false);
			$('#MONTHLY').prop("checked",false);
		}else if(value == 'MONTHLY'){
			$('#monthly_div').show();
			$('#daily_div').hide();
			$('#hourly_div').hide();
			$('#HOURLY').prop("checked",false);
			$('#DAILY').prop("checked",false);
		}
	}
