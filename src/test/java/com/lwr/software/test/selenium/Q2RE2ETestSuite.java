package com.lwr.software.test.selenium;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class Q2RE2ETestSuite {
	
	private WebDriver driver;

	public static void main(String[] args) throws InterruptedException {
		System.setProperty("webdriver.chrome.driver","D:\\LWR\\ChromeDriver\\chromedriver_win32\\chromedriver.exe");
		
		Q2RE2ETestSuite test = new Q2RE2ETestSuite();
		test.createDriver();
		Thread.sleep(5000);
		test.createConnection();
		Thread.sleep(5000);
		test.createFixedReport();
		Thread.sleep(5000);
		test.createParameterizedReport();
		Thread.sleep(5000);
	}
	
	public Q2RE2ETestSuite(){
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(30,TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(30,TimeUnit.SECONDS);
	}

	private void createDriver() throws InterruptedException {
		driver.get("http://localhost:8080/q2r/login");
		driver.findElement(By.id("username")).sendKeys("admin");
		driver.findElement(By.id("password")).sendKeys("admin");
		driver.findElement(By.id("loginButton")).click();

		Thread.sleep(1000);
		driver.findElement(By.id("drivermgmt")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("addDriverButton")).click();
		driver.findElement(By.id("aliasInput")).sendKeys("MySQL");
		Thread.sleep(1000);
		driver.findElement(By.id("classNameInput")).sendKeys("com.mysql.jdbc.Driver");
		Thread.sleep(1000);
		driver.findElement(By.id("jarFileUploadSelectInput")).sendKeys("D:\\Work\\jars\\mysql-connector-java-5.0.8-bin.jar");
		Thread.sleep(1000);
		driver.findElement(By.id("saveDriverButton")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//button[contains(.,'Ok')]")).click();
		Thread.sleep(5000);
		
		driver.findElement(By.id("usericon")).click();
		driver.findElement(By.id("logoutRef")).click();
	}
	
	private void createConnection() throws InterruptedException {
		driver.get("http://localhost:8080/q2r/login");
		driver.findElement(By.id("username")).sendKeys("admin");
		driver.findElement(By.id("password")).sendKeys("admin");
		driver.findElement(By.id("loginButton")).click();

		Thread.sleep(1000);
		driver.findElement(By.id("connmgmt")).click();
		Thread.sleep(5000);
		driver.findElement(By.id("addConnectionButton")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("aliasInput")).sendKeys("MySQL");
		Thread.sleep(1000);
		driver.findElement(By.id("driverSelect")).sendKeys("MySQL");
		Thread.sleep(1000);
		driver.findElement(By.id("urlInput")).sendKeys("jdbc:mysql://localhost:3306/sys");
		Thread.sleep(1000);
		driver.findElement(By.id("usernameInput")).sendKeys("root");
		Thread.sleep(1000);
		driver.findElement(By.id("passwordInput")).sendKeys("root");
		Thread.sleep(1000);
		driver.findElement(By.id("saveConnectionButton")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//button[contains(.,'Ok')]")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("MySQLTestConnectionButton")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//button[contains(.,'Ok')]")).click();
		Thread.sleep(5000);

		driver.findElement(By.id("usericon")).click();
		driver.findElement(By.id("logoutRef")).click();

		
	}


	private void createFixedReport() throws InterruptedException {
		driver.get("http://localhost:8080/q2r/login");
		Thread.sleep(1000);
		driver.findElement(By.id("username")).sendKeys("admin");
		Thread.sleep(1000);
		driver.findElement(By.id("password")).sendKeys("admin");
		Thread.sleep(1000);
		driver.findElement(By.id("loginButton")).click();
		
		Thread.sleep(1000);
		driver.findElement(By.id("newreport")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("editReportRef")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("title")).sendKeys("Server Performance - Fixed");
		Thread.sleep(1000);
		driver.findElement(By.id("description")).sendKeys("Performance of server in terms of CPU, Run Q, Memory and Swap Utilization");
		
		Thread.sleep(1000);
		driver.findElement(By.id("saveButton")).click();

		Thread.sleep(1000);
		driver.findElement(By.id("addRow")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("addColumn0")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("addColumn1")).click();
		
		Thread.sleep(1000);
		driver.findElement(By.id("editRef00")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("titleInput")).clear();
		Thread.sleep(1000);
		driver.findElement(By.id("titleInput")).sendKeys("CPU Utilization");
		Thread.sleep(1000);
		driver.findElement(By.id("queryInput")).sendKeys("select ts,hostname,cpu from sysperf where hostname in ('myhost1.mydomain.com') and ts>='2018-10-05 10:00:00' and ts<='2018-10-06 12:00:00' order by ts");
		Thread.sleep(1000);
		driver.findElement(By.id("chartSelect")).sendKeys("Line Chart");
		Thread.sleep(1000);
		driver.findElement(By.id("databaseSelect")).sendKeys("MySQL");
		Thread.sleep(1000);
		driver.findElement(By.id("refreshButton")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("saveButton")).click();

		Thread.sleep(1000);
		driver.findElement(By.id("editRef01")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("titleInput")).clear();
		Thread.sleep(1000);
		driver.findElement(By.id("titleInput")).sendKeys("Run Queue");
		Thread.sleep(1000);
		driver.findElement(By.id("queryInput")).sendKeys("select ts,hostname,runq from sysperf where hostname in ('myhost1.mydomain.com') and ts>='2018-10-05 10:00:00' and ts<='2018-10-06 12:00:00' order by ts");
		Thread.sleep(1000);
		driver.findElement(By.id("chartSelect")).sendKeys("Scatter Chart");
		Thread.sleep(1000);
		driver.findElement(By.id("databaseSelect")).sendKeys("MySQL");
		Thread.sleep(1000);
		driver.findElement(By.id("refreshButton")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("saveButton")).click();

		Thread.sleep(1000);
		driver.findElement(By.id("editRef10")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("titleInput")).clear();
		Thread.sleep(1000);
		driver.findElement(By.id("titleInput")).sendKeys("Memory Queue");
		Thread.sleep(1000);
		driver.findElement(By.id("queryInput")).sendKeys("select ts,hostname,mem from sysperf where hostname in ('myhost1.mydomain.com') and ts>='2018-10-05 10:00:00' and ts<='2018-10-06 12:00:00' order by ts");
		Thread.sleep(1000);
		driver.findElement(By.id("chartSelect")).sendKeys("Column Chart");
		Thread.sleep(1000);
		driver.findElement(By.id("databaseSelect")).sendKeys("MySQL");
		Thread.sleep(1000);
		driver.findElement(By.id("refreshButton")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("saveButton")).click();

		Thread.sleep(1000);
		driver.findElement(By.id("editRef11")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("titleInput")).clear();
		Thread.sleep(1000);
		driver.findElement(By.id("titleInput")).sendKeys("Swap Utilization");
		Thread.sleep(1000);
		driver.findElement(By.id("queryInput")).sendKeys("select ts,hostname,swap from sysperf where hostname in ('myhost1.mydomain.com') and ts>='2018-10-05 10:00:00' and ts<='2018-10-06 12:00:00' order by ts");
		Thread.sleep(1000);
		driver.findElement(By.id("chartSelect")).sendKeys("Area Chart");
		Thread.sleep(1000);
		driver.findElement(By.id("databaseSelect")).sendKeys("MySQL");
		Thread.sleep(1000);
		driver.findElement(By.id("refreshButton")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("saveButton")).click();

		Thread.sleep(1000);
		driver.findElement(By.id("saveReportButton")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("savePublicRef")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//button[contains(.,'Ok')]")).click();
		Thread.sleep(1000);
		
		Thread.sleep(1000);
		driver.findElement(By.id("publicmgmt")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("Server Performance - FixedOpenRef")).click();

		driver.findElement(By.id("usericon")).click();
		driver.findElement(By.id("logoutRef")).click();
		
		
	}



	private void createParameterizedReport() throws InterruptedException {
		driver.get("http://localhost:8080/q2r/login");
		Thread.sleep(1000);
		driver.findElement(By.id("username")).sendKeys("admin");
		Thread.sleep(1000);
		driver.findElement(By.id("password")).sendKeys("admin");
		Thread.sleep(1000);
		driver.findElement(By.id("loginButton")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("newreport")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("editReportRef")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("title")).sendKeys("Server Performance - Parameterized");
		Thread.sleep(1000);
		driver.findElement(By.id("description")).sendKeys("Performance of server in terms of CPU, Run Q, Memory and Swap Utilization");
		Thread.sleep(1000);
		driver.findElement(By.id("saveButton")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("addRow")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("addColumn0")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("addColumn1")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("editRef00")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("titleInput")).clear();
		Thread.sleep(1000);
		driver.findElement(By.id("titleInput")).sendKeys("CPU Utilization");
		Thread.sleep(1000);
		driver.findElement(By.id("queryInput")).sendKeys("select ts,hostname,cpu from sysperf where hostname in {list:hostname} and ts>={date:startdate} and ts<={date:enddate} order by ts");
		Thread.sleep(1000);
		driver.findElement(By.id("chartSelect")).sendKeys("Line Chart");
		Thread.sleep(1000);
		driver.findElement(By.id("databaseSelect")).sendKeys("MySQL");

		Thread.sleep(1000);
		driver.findElement(By.id("refreshButton")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("hostname")).sendKeys("myhost1.mydomain.com");
		Thread.sleep(1000);
		driver.findElement(By.id("startdate")).sendKeys("10/04/2018");
		Thread.sleep(1000);
		driver.findElement(By.id("enddate")).sendKeys("10/06/2018");
		Thread.sleep(1000);
		driver.findElement(By.id("refreshButton")).click();

		Thread.sleep(1000);
		driver.findElement(By.id("saveButton")).click();

		Thread.sleep(1000);
		driver.findElement(By.id("editRef01")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("titleInput")).clear();
		Thread.sleep(1000);
		driver.findElement(By.id("titleInput")).sendKeys("Run Queue");
		Thread.sleep(1000);
		driver.findElement(By.id("queryInput")).sendKeys("select ts,hostname,runq from sysperf where hostname in {list:hostname} and ts>={date:startdate} and ts<={date:enddate} order by ts");
		Thread.sleep(1000);
		driver.findElement(By.id("chartSelect")).sendKeys("Scatter Chart");
		Thread.sleep(1000);
		driver.findElement(By.id("databaseSelect")).sendKeys("MySQL");

		Thread.sleep(1000);
		driver.findElement(By.id("refreshButton")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("hostname")).sendKeys("myhost1.mydomain.com");
		Thread.sleep(1000);
		driver.findElement(By.id("startdate")).sendKeys("10/04/2018");
		Thread.sleep(1000);
		driver.findElement(By.id("enddate")).sendKeys("10/06/2018");
		Thread.sleep(1000);
		driver.findElement(By.id("refreshButton")).click();

		
		Thread.sleep(1000);
		driver.findElement(By.id("saveButton")).click();

		Thread.sleep(1000);
		driver.findElement(By.id("editRef10")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("titleInput")).clear();
		Thread.sleep(1000);
		driver.findElement(By.id("titleInput")).sendKeys("Memory Queue");
		Thread.sleep(1000);
		driver.findElement(By.id("queryInput")).sendKeys("select ts,hostname,mem from sysperf where hostname in {list:hostname} and ts>={date:startdate} and ts<={date:enddate} order by ts");
		Thread.sleep(1000);
		driver.findElement(By.id("chartSelect")).sendKeys("Column Chart");
		Thread.sleep(1000);
		driver.findElement(By.id("databaseSelect")).sendKeys("MySQL");
		Thread.sleep(1000);
		driver.findElement(By.id("refreshButton")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("hostname")).sendKeys("myhost1.mydomain.com");
		Thread.sleep(1000);
		driver.findElement(By.id("startdate")).sendKeys("10/04/2018");
		Thread.sleep(1000);
		driver.findElement(By.id("enddate")).sendKeys("10/06/2018");
		Thread.sleep(1000);
		driver.findElement(By.id("refreshButton")).click();

		Thread.sleep(1000);
		driver.findElement(By.id("saveButton")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("editRef11")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("titleInput")).clear();
		Thread.sleep(1000);
		driver.findElement(By.id("titleInput")).sendKeys("Swap Utilization");
		Thread.sleep(1000);
		driver.findElement(By.id("queryInput")).sendKeys("select ts,hostname,swap from sysperf where hostname in {list:hostname} and ts>={date:startdate} and ts<={date:enddate} order by ts");
		Thread.sleep(1000);
		driver.findElement(By.id("chartSelect")).sendKeys("Area Chart");
		Thread.sleep(1000);
		driver.findElement(By.id("databaseSelect")).sendKeys("MySQL");

		Thread.sleep(1000);
		driver.findElement(By.id("refreshButton")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("hostname")).sendKeys("myhost1.mydomain.com");
		Thread.sleep(1000);
		driver.findElement(By.id("startdate")).sendKeys("10/04/2018");
		Thread.sleep(1000);
		driver.findElement(By.id("enddate")).sendKeys("10/06/2018");
		Thread.sleep(1000);
		driver.findElement(By.id("refreshButton")).click();
		Thread.sleep(1000);
		
		
		Thread.sleep(1000);
		driver.findElement(By.id("saveButton")).click();

		Thread.sleep(1000);
		driver.findElement(By.id("saveReportButton")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("savePublicRef")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//button[contains(.,'Ok')]")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("publicmgmt")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("Server Performance - ParameterizedOpenRef")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("hostname")).sendKeys("myhost1.mydomain.com");
		Thread.sleep(1000);
		driver.findElement(By.id("startdate")).sendKeys("10/04/2018");
		Thread.sleep(1000);
		driver.findElement(By.id("enddate")).sendKeys("10/06/2018");
		Thread.sleep(1000);
		driver.findElement(By.id("applyButton")).click();

		driver.findElement(By.id("usericon")).click();
		driver.findElement(By.id("logoutRef")).click();
	
		
	}

}
