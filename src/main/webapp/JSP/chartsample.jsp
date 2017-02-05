<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ include file="header.jsp"%>
<meta name="viewport" content="width=device-width, initial-scale=1">
<script type="text/javascript">
$(document).ready(function() {
	$("#editmenu").hide();
	$("#savepersonalmenu").hide();
	$("#savepublicmenu").hide();
	$("#pdfmenu").hide();
	$("#csvmenu").hide();
	$("#htmlmenu").hide();
});
</script>
<style>
.carousel-inner>.item>img, .carousel-inner>.item>a>img,.carousel-inner>.item>h3,.carousel-inner>.item>h4,.carousel-inner>.item>h5 {
	width: 70%;
	margin: auto;
}
.carousel-indicators{
	background-color:#679BB7;
}
</style>
<section id="mainsection">
	<div id="myCarousel" class="carousel slide" data-ride="carousel" style="height:100%">
		<!-- Wrapper for slides -->
		<div class="carousel-inner" role="listbox">
			<div class="item active">
				<br>
				<h3>Bar Chart Example</h3>
				<br>
				<h4>SQL Query</h4>
				<br>
				<h5>select timestamp,cpu_util,mem_util,swap_util from sysper.system_perf where (timestamp between '2016-10-11 19:30:00' and '2016-10-11 20:30:00' ) and  (hostname like 'host1.mydomain.com')</h5>
				<br>
				<h4>Data</h4>
				<img src="./images/data_table.JPG" alt="Data Table">
				<h4>Chart</h4>
				<img src="./images/bar_sample.JPG" alt="Bar Chart">
			</div>
			<div class="item">
				<br>
				<h3>Stacked Bar Chart Example</h3>
				<br>
				<h4>SQL Query</h4>
				<br>
				<h5>select timestamp,cpu_util,mem_util,swap_util from sysper.system_perf where (timestamp between '2016-10-11 19:30:00' and '2016-10-11 20:30:00' ) and  (hostname like 'host1.mydomain.com')</h5>
				<br>
				<h4>Data</h4>
				<img src="./images/data_table.JPG" alt="Data Table">
				<h4>Chart</h4>
				<img src="./images/stack_bar_sample.JPG" alt="Stack Bar Chart">
			</div>
			<div class="item">
				<br>
				<h3>Column Chart Example</h3>
				<br>
				<h4>SQL Query</h4>
				<br>
				<h5>select timestamp,cpu_util,mem_util,swap_util from sysper.system_perf where (timestamp between '2016-10-11 19:30:00' and '2016-10-11 20:30:00' ) and  (hostname like 'host1.mydomain.com')</h5>
				<br>
				<h4>Data</h4>
				<img src="./images/data_table.JPG" alt="Data Table">
				<h4>Chart</h4>
				<img src="./images/column_sample.JPG" alt="Column Chart">
			</div>
			<div class="item">
				<br>
				<h3>Stacked Column Chart Example</h3>
				<br>
				<h4>SQL Query</h4>
				<br>
				<h5>select timestamp,cpu_util,mem_util,swap_util from sysper.system_perf where (timestamp between '2016-10-11 19:30:00' and '2016-10-11 20:30:00' ) and  (hostname like 'host1.mydomain.com')</h5>
				<br>
				<h4>Data</h4>
				<img src="./images/data_table.JPG" alt="Data Table">
				<h4>Chart</h4>
				<img src="./images/stack_column_sample.JPG" alt="Stack Column Chart">
			</div>
			<div class="item">
				<br>
				<h3>Line Chart Example</h3>
				<br>
				<h4>SQL Query</h4>
				<br>
				<h5>select timestamp,cpu_util,mem_util,swap_util from sysper.system_perf where (timestamp between '2016-10-11 19:30:00' and '2016-10-11 20:30:00' ) and  (hostname like 'host1.mydomain.com')</h5>
				<br>
				<h4>Data</h4>
				<img src="./images/data_table.JPG" alt="Data Table">
				<h4>Chart</h4>
				<img src="./images/line_sample.JPG" alt="Line Chart">
			</div>
		</div>
		<!-- Left and right controls -->
		<a class="left carousel-control" href="#myCarousel" role="button" data-slide="prev" style="background:#f7f7f7"> 
			<span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span> 
			<span class="sr-only">Previous</span>
		</a> 
		<a class="right carousel-control" href="#myCarousel" role="button" data-slide="next" style="background:#f7f7f7">
			<span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
			<span class="sr-only">Next</span>
		</a>
				<!-- Indicators -->
		<ol class="carousel-indicators">
			<li data-target="#myCarousel" data-slide-to="0" class="active"></li>
			<li data-target="#myCarousel" data-slide-to="1"></li>
			<li data-target="#myCarousel" data-slide-to="2"></li>
			<li data-target="#myCarousel" data-slide-to="3"></li>
			<li data-target="#myCarousel" data-slide-to="4"></li>
		</ol>
		
	</div>
</section>
<br>
<br>
<%@ include file="footer.jsp"%>