package com.lwr.software.test.selenium;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
//import org.monte.screenrecorder.ScreenRecorder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import junit.framework.Assert;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Q2RE2ETestSuite {
	
	private static WebDriver driver;
	
//	static ScreenRecorder screenRecorder;
	
	@BeforeClass
	public static void init(){
		System.setProperty("webdriver.chrome.driver","D:\\LWR\\ChromeDriver\\chromedriver_win32\\chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(30,TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(30,TimeUnit.SECONDS);
//		try {
//		GraphicsConfiguration gc = GraphicsEnvironment
//			      .getLocalGraphicsEnvironment()
//			      .getDefaultScreenDevice()
//			      .getDefaultConfiguration();
//			screenRecorder = new ScreenRecorder(gc);
//			screenRecorder.start();
//			driver.get("http://localhost:8080/q2r/login");
//			Thread.sleep(5000);
//			driver.findElement(By.id("username")).sendKeys("admin");
//			driver.findElement(By.id("password")).sendKeys("admin");
//			driver.findElement(By.id("loginButton")).click();
//			Thread.sleep(5000);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		} 
	}
	
	@AfterClass
	public static void destroy(){
		try{
			driver.close();
//			screenRecorder.stop();
		}catch (Exception e) {
		e.printStackTrace();
		}
	}
	
	@Before
	public void login() throws InterruptedException{
		driver.get("http://localhost:8080/q2r/login");
		Thread.sleep(1000);
		driver.findElement(By.id("username")).sendKeys("admin");
		driver.findElement(By.id("password")).sendKeys("admin");
		driver.findElement(By.id("loginButton")).click();
		Thread.sleep(1000);
	}
	
	@After
	public void logout() throws InterruptedException{
		Thread.sleep(1000);
		driver.findElement(By.id("usericon")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("logoutRef")).click();
		Thread.sleep(1000);
	}
	
	@Test
	public void step1CreateDriver() throws InterruptedException {
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
		WebElement elem = driver.findElement(By.tagName("h2"));
		String textToAssert = elem.getText();
		Assert.assertEquals(true, textToAssert.contains("JDBC Driver 'MySQL' upload Succeeded. Restart the application server."));
		Thread.sleep(15000);
	}

	@Test
	public void step2CreateConnection() throws InterruptedException {
		driver.findElement(By.id("connmgmt")).click();
		Thread.sleep(1000);
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
		WebElement elem = driver.findElement(By.tagName("h2"));
		String textToAssert = elem.getText();
		Assert.assertEquals(true, textToAssert.contains("Save of alias 'MySQL' Succeeded"));
		
		Thread.sleep(1000);
		driver.findElement(By.id("MySQLTestConnectionButton")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//button[contains(.,'Ok')]")).click();
		elem = driver.findElement(By.tagName("h2"));
		textToAssert = elem.getText();
		Assert.assertEquals(true, textToAssert.contains("Connection to alias 'MySQL' Succeeded"));
	}

	@Test
	public void step3CreateFixedReport() throws InterruptedException {
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
		Thread.sleep(2000);
	}


	@Test
	public void step4CreateParameterizedReport() throws InterruptedException {
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
		driver.findElement(By.id("hostname")).sendKeys("myhost1.mydomain.com,myhost2.mydomain.com");
		Thread.sleep(1000);
		driver.findElement(By.id("startdate")).sendKeys("10/04/2018");
		Thread.sleep(1000);
		driver.findElement(By.id("enddate")).sendKeys("10/06/2018");
		Thread.sleep(1000);
		driver.findElement(By.id("applyButton")).click();
		Thread.sleep(2000);
	}

	@Test
	public void step5EditParameterizedReport() throws InterruptedException {
		Thread.sleep(1000);
		driver.findElement(By.id("publicmgmt")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("Server Performance - ParameterizedEditRef")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("hostname")).sendKeys("myhost1.mydomain.com,myhost2.mydomain.com");
		Thread.sleep(1000);
		driver.findElement(By.id("startdate")).sendKeys("10/04/2018");
		Thread.sleep(1000);
		driver.findElement(By.id("enddate")).sendKeys("10/06/2018");
		Thread.sleep(1000);
		driver.findElement(By.id("applyButton")).click();
		Thread.sleep(4000);
		driver.findElement(By.id("editRef00")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("chartSelect")).sendKeys("Stepped Area Chart");
		Thread.sleep(1000);
		driver.findElement(By.id("saveButton")).click();
		
		Thread.sleep(2000);
		driver.findElement(By.id("editRef01")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("chartSelect")).sendKeys("Annoted Line");
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
		driver.findElement(By.id("hostname")).sendKeys("myhost1.mydomain.com,myhost2.mydomain.com");
		Thread.sleep(1000);
		driver.findElement(By.id("startdate")).sendKeys("10/04/2018");
		Thread.sleep(1000);
		driver.findElement(By.id("enddate")).sendKeys("10/06/2018");
		Thread.sleep(1000);
		driver.findElement(By.id("applyButton")).click();
		Thread.sleep(2000);
	}

	@Test
	public void step6ChangeColSpan() throws InterruptedException {
		Thread.sleep(1000);
		driver.findElement(By.id("publicmgmt")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("Server Performance - ParameterizedEditRef")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("hostname")).sendKeys("myhost1.mydomain.com,myhost2.mydomain.com");
		Thread.sleep(1000);
		driver.findElement(By.id("startdate")).sendKeys("10/04/2018");
		Thread.sleep(1000);
		driver.findElement(By.id("enddate")).sendKeys("10/06/2018");
		Thread.sleep(1000);
		driver.findElement(By.id("applyButton")).click();
		Thread.sleep(4000);
		driver.findElement(By.id("editRef00")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("colSpanInput")).clear();
		driver.findElement(By.id("colSpanInput")).sendKeys("4");
		
		Thread.sleep(1000);
		driver.findElement(By.id("saveButton")).click();
		
		Thread.sleep(2000);
		driver.findElement(By.id("editRef01")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("colSpanInput")).clear();
		driver.findElement(By.id("colSpanInput")).sendKeys("12");
		driver.findElement(By.id("chartSelect")).sendKeys("Column Chart");
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
		driver.findElement(By.id("hostname")).sendKeys("myhost1.mydomain.com,myhost2.mydomain.com");
		Thread.sleep(1000);
		driver.findElement(By.id("startdate")).sendKeys("10/04/2018");
		Thread.sleep(1000);
		driver.findElement(By.id("enddate")).sendKeys("10/06/2018");
		Thread.sleep(1000);
		driver.findElement(By.id("applyButton")).click();
		Thread.sleep(2000);
	}
}
