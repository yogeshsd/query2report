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
public class UserMgmtNewUserTest{

	
	private static WebDriver driver;
	
	@BeforeClass
	public static void init(){
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(30,TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(30,TimeUnit.SECONDS);
	}
	
	@AfterClass
	public static void destroy(){
		driver.close();
	}
	
	public void login(String username, String password) throws InterruptedException{
		driver.get("http://localhost:8080/q2r/login.html");
		Thread.sleep(1000);
		driver.findElement(By.id("username")).sendKeys(username);
		driver.findElement(By.id("password")).sendKeys(password);
		driver.findElement(By.id("loginButton")).click();
		Thread.sleep(1000);
	}
	
	public void logout() throws InterruptedException{
		Thread.sleep(1000);
		driver.findElement(By.id("usericon")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("logoutRef")).click();
		Thread.sleep(1000);
	}

	@Test
	public void step1CreateUser() throws InterruptedException {
		login("admin","admin");
		Thread.sleep(1000);
		driver.findElement(By.id("usermgmt")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("addUserButton")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("displayNameInput")).sendKeys("user1");
		driver.findElement(By.id("userNameInput")).sendKeys("user1");
		driver.findElement(By.id("passwordInput")).sendKeys("user1");
		driver.findElement(By.id("confirmPasswordInput")).sendKeys("user1");
		driver.findElement(By.id("roleSelect")).sendKeys("view");
		driver.findElement(By.id("saveUserButton")).click();
		Thread.sleep(1000);
		WebElement elem = driver.findElement(By.tagName("h2"));
		driver.findElement(By.xpath("//button[contains(.,'Ok')]")).click();
		String textToAssert = elem.getText();
		Assert.assertEquals(true, textToAssert.contains("Save of user 'user1' Succeeded"));
		Thread.sleep(1000);
		logout();
		Thread.sleep(1000);
		login("user1","user1");
		Thread.sleep(1000);
		logout();
	}
	
	@Test
	public void step2CreatePersonalReport() throws InterruptedException {
		login("user1","user1");
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
		driver.findElement(By.id("paramsref")).click();
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
		driver.findElement(By.id("paramsref")).click();
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
		driver.findElement(By.id("paramsref")).click();
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
		driver.findElement(By.id("paramsref")).click();
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
		driver.findElement(By.id("savePersonalRef")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//button[contains(.,'Ok')]")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("personalmgmt")).click();
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
		
		logout();
	}
	
	@Test
	public void step3EditPersonalReport() throws InterruptedException {
		login("user1","user1");
		Thread.sleep(1000);
		driver.findElement(By.id("personalmgmt")).click();
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
		driver.findElement(By.id("savePersonalRef")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//button[contains(.,'Ok')]")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("personalmgmt")).click();
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
		
		logout();
	}
}
