package com.lwr.software.test.selenium;

import java.util.concurrent.TimeUnit;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import junit.framework.Assert;

public class ReportLayoutTests {
	private static WebDriver driver;
	
	
	@BeforeClass
	public static void init(){
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(30,TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(30,TimeUnit.SECONDS);
		try{
			driver.get("http://localhost:8080/q2r/login.html");
			Thread.sleep(1000);
			driver.findElement(By.id("username")).sendKeys("admin");
			driver.findElement(By.id("password")).sendKeys("admin");
			driver.findElement(By.id("loginButton")).click();
			Thread.sleep(1000);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@AfterClass
	public static void destroy(){
		try{
			driver.close();
			Thread.sleep(1000);
			driver.findElement(By.id("usericon")).click();
			Thread.sleep(1000);
			driver.findElement(By.id("logoutRef")).click();
			Thread.sleep(1000);
		}catch (Exception e) {
		e.printStackTrace();
		}
	}
	
	@Test
	public void columnAddDelete() throws InterruptedException {
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
		driver.findElement(By.id("addColumn0")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("addColumn0")).click();
		Thread.sleep(1000);

		driver.findElement(By.id("addColumn1")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("addColumn1")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("addColumn1")).click();
		Thread.sleep(1000);
		
		
		driver.findElement(By.id("deleteRef00")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("deleteRef01")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("deleteRef03")).click();
		Thread.sleep(1000);		
		driver.findElement(By.id("deleteRef10")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("deleteRef12")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("deleteRef13")).click();
		Thread.sleep(1000);		
		Assert.assertNotNull(driver.findElement(By.id("deleteRef02")));
		Assert.assertNotNull(driver.findElement(By.id("deleteRef11")));
		Thread.sleep(10000);
	}
	@Test
	public void rowAddDelete() throws InterruptedException {
		driver.findElement(By.id("publicmgmt")).click();
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
		driver.findElement(By.id("addColumn0")).click();
		Thread.sleep(1000);

		driver.findElement(By.id("addColumn1")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("addColumn1")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("addColumn1")).click();
		Thread.sleep(1000);

		driver.findElement(By.id("addRow")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("addColumn2")).click();
		Thread.sleep(1000);

		
		driver.findElement(By.id("deleteRef10")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("deleteRef11")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("deleteRef12")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("deleteRef13")).click();
		Thread.sleep(5000);

		
		driver.findElement(By.id("deleteRef00")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("deleteRef01")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("deleteRef02")).click();
		Thread.sleep(5000);
		
		driver.findElement(By.id("addRow")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("addColumn1")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("addColumn1")).click();
		Thread.sleep(5000);
		
		
	}
}
