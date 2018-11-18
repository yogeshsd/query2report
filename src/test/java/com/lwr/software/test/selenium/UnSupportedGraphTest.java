package com.lwr.software.test.selenium;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import junit.framework.Assert;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UnSupportedGraphTest{

	
	private static WebDriver driver;
	
	@BeforeClass
	public static void init(){
		System.setProperty("webdriver.chrome.driver","D:\\LWR\\ChromeDriver\\chromedriver_win32\\chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(30,TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(30,TimeUnit.SECONDS);
	}
	
	@AfterClass
	public static void destroy(){
		driver.close();
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
	public void createThreeMeasureReport() throws InterruptedException {
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
		driver.findElement(By.id("editRef00")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("titleInput")).clear();
		Thread.sleep(1000);
		driver.findElement(By.id("titleInput")).sendKeys("CPU Utilization");
		Thread.sleep(1000);
		driver.findElement(By.id("queryInput")).sendKeys("select ts,hostname,hostname,cpu from sysperf where hostname in ('myhost1.mydomain.com') and ts>='2018-10-05 10:00:00' and ts<='2018-10-06 12:00:00' order by ts");
		Thread.sleep(1000);
		driver.findElement(By.id("chartSelect")).sendKeys("Line Chart");
		Thread.sleep(1000);
		driver.findElement(By.id("databaseSelect")).sendKeys("MySQL");
		Thread.sleep(1000);
		driver.findElement(By.id("refreshButton")).click();
		Thread.sleep(1000);
		
		WebElement elem = driver.findElement(By.id("chartdata"));
		String textToAssert = elem.findElement(By.tagName("h5")).getText();
		Assert.assertEquals(true, textToAssert.contains("Element has key columns [hostname,hostname], time columns [ts] and metrics columns [cpu]."));


		driver.findElement(By.id("queryInput")).clear();
		Thread.sleep(1000);
		driver.findElement(By.id("queryInput")).sendKeys("select ts,ts,hostname,cpu from sysperf where hostname in ('myhost1.mydomain.com') and ts>='2018-10-05 10:00:00' and ts<='2018-10-06 12:00:00' order by ts");
		driver.findElement(By.id("refreshButton")).click();
		Thread.sleep(1000);
		elem = driver.findElement(By.id("chartdata"));
		textToAssert = elem.findElement(By.tagName("h5")).getText();
		Assert.assertEquals(true, textToAssert.contains("Element has key columns [hostname], time columns [ts,ts] and metrics columns [cpu]."));

		driver.findElement(By.id("queryInput")).clear();
		Thread.sleep(1000);
		driver.findElement(By.id("queryInput")).sendKeys("select ts,ts,cpu from sysperf where hostname in ('myhost1.mydomain.com') and ts>='2018-10-05 10:00:00' and ts<='2018-10-06 12:00:00' order by ts");
		driver.findElement(By.id("refreshButton")).click();
		Thread.sleep(1000);
		elem = driver.findElement(By.id("chartdata"));
		textToAssert = elem.findElement(By.tagName("h5")).getText();
		Assert.assertEquals(true, textToAssert.contains("Element has key columns [], time columns [ts,ts] and metrics columns [cpu]."));

		driver.findElement(By.id("queryInput")).clear();
		Thread.sleep(1000);
		driver.findElement(By.id("queryInput")).sendKeys("select hostname,hostname,hostname,cpu from sysperf where hostname in ('myhost1.mydomain.com') and ts>='2018-10-05 10:00:00' and ts<='2018-10-06 12:00:00' order by ts");
		driver.findElement(By.id("refreshButton")).click();
		Thread.sleep(1000);
		elem = driver.findElement(By.id("chartdata"));
		textToAssert = elem.findElement(By.tagName("h5")).getText();
		Assert.assertEquals(true, textToAssert.contains("Element has key columns [hostname,hostname,hostname], time columns [] and metrics columns [cpu]."));
		
		driver.findElement(By.id("cancelButton")).click();

	}
}
