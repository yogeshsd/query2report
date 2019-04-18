package com.lwr.software.test.selenium;

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
public class SalesDemo {
	
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
	
	@Before
	public void login() throws InterruptedException{
	}
	
	@After
	public void logout() throws InterruptedException{
	}
	
	@Test
	public void step1CreateFixedReport() throws InterruptedException {
		createReport("Revenue and Sales Summary","The report shows the revenue and sales numbers of an orginization.");
		openReport("Revenue and Sales Summary",30000);
	}

	private void openReport(String title, long sleepTime) throws InterruptedException {
		Thread.sleep(1000);
		driver.findElement(By.id("publicmgmt")).click();
		driver.findElement(By.id(title+"OpenRef")).click();
		Thread.sleep(sleepTime);
	}

	@Test
	public void step2CreateFixedReport() throws InterruptedException {
		createReport("Revenue and Sales Summary - Demo","The report shows the revenue and sales numbers of an organization.");
		openReport("Revenue and Sales Summary - Demo",10000);
	}
	
	private void createReport(String title, String descr) throws InterruptedException {
		Thread.sleep(1000);
		driver.findElement(By.id("newreport")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("editReportRef")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("title")).sendKeys(title);
		driver.findElement(By.id("description")).sendKeys(descr);
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
		driver.findElement(By.id("editRef00")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("titleInput")).clear();
		Thread.sleep(1000);
		driver.findElement(By.id("titleInput")).sendKeys("Revenue By Product");
		Thread.sleep(1000);
		driver.findElement(By.id("queryInput")).sendKeys("select product_line,sum(revenue) from sales group by product_line");
		Thread.sleep(1000);
		driver.findElement(By.id("chartSelect")).sendKeys("Pie Chart");
		Thread.sleep(1000);
		driver.findElement(By.id("databaseSelect")).sendKeys("MySQL");
		Thread.sleep(1000);
		driver.findElement(By.id("colSpanInput")).clear();
		Thread.sleep(1000);
		driver.findElement(By.id("colSpanInput")).sendKeys("1");
		Thread.sleep(1000);
		driver.findElement(By.id("refreshButton")).click();
		Thread.sleep(5000);
		driver.findElement(By.id("saveButton")).click();

		Thread.sleep(1000);
		driver.findElement(By.id("editRef01")).click();
		Thread.sleep(400);
		driver.findElement(By.id("titleInput")).clear();
		Thread.sleep(400);
		driver.findElement(By.id("titleInput")).sendKeys("Revenue By Region");
		Thread.sleep(400);
		driver.findElement(By.id("queryInput")).sendKeys("select region,sum(revenue) from sales group by region");
		Thread.sleep(400);
		driver.findElement(By.id("chartSelect")).sendKeys("Pie Chart");
		Thread.sleep(400);
		driver.findElement(By.id("databaseSelect")).sendKeys("MySQL");
		Thread.sleep(400);
		driver.findElement(By.id("refreshButton")).click();
		Thread.sleep(400);
		driver.findElement(By.id("saveButton")).click();

		Thread.sleep(1000);
		driver.findElement(By.id("editRef02")).click();
		driver.findElement(By.id("titleInput")).clear();
		Thread.sleep(400);
		driver.findElement(By.id("titleInput")).sendKeys("Revenue year over year");
		Thread.sleep(400);
		driver.findElement(By.id("queryInput")).sendKeys("select quarter,sum(revenue) from sales group by quarter order by year");
		Thread.sleep(400);
		driver.findElement(By.id("chartSelect")).sendKeys("Column Chart");
		Thread.sleep(400);
		driver.findElement(By.id("databaseSelect")).sendKeys("MySQL");
		Thread.sleep(400);
		driver.findElement(By.id("colSpanInput")).clear();
		Thread.sleep(400);
		driver.findElement(By.id("colSpanInput")).sendKeys("2");
		Thread.sleep(400);
		driver.findElement(By.id("refreshButton")).click();
		Thread.sleep(400);
		driver.findElement(By.id("saveButton")).click();

		Thread.sleep(1000);
		driver.findElement(By.id("editRef10")).click();
		Thread.sleep(400);
		driver.findElement(By.id("titleInput")).clear();
		Thread.sleep(400);
		driver.findElement(By.id("titleInput")).sendKeys("Sales By Product");
		Thread.sleep(400);
		driver.findElement(By.id("queryInput")).sendKeys("select product_line,sum(quantity) from sales group by product_line");
		Thread.sleep(400);
		driver.findElement(By.id("chartSelect")).sendKeys("Pie Chart");
		Thread.sleep(400);
		driver.findElement(By.id("databaseSelect")).sendKeys("MySQL");
		Thread.sleep(400);
		driver.findElement(By.id("colSpanInput")).clear();
		Thread.sleep(400);
		driver.findElement(By.id("colSpanInput")).sendKeys("1");
		Thread.sleep(400);
		driver.findElement(By.id("refreshButton")).click();
		Thread.sleep(400);
		driver.findElement(By.id("saveButton")).click();

		Thread.sleep(1000);
		driver.findElement(By.id("editRef11")).click();
		Thread.sleep(400);
		driver.findElement(By.id("titleInput")).clear();
		Thread.sleep(400);
		driver.findElement(By.id("titleInput")).sendKeys("Sales By Region");
		Thread.sleep(400);
		driver.findElement(By.id("queryInput")).sendKeys("select region,sum(quantity) from sales group by region");
		Thread.sleep(400);
		driver.findElement(By.id("chartSelect")).sendKeys("Pie Chart");
		Thread.sleep(400);
		driver.findElement(By.id("databaseSelect")).sendKeys("MySQL");
		Thread.sleep(400);
		driver.findElement(By.id("colSpanInput")).clear();
		Thread.sleep(400);
		driver.findElement(By.id("colSpanInput")).sendKeys("1");
		Thread.sleep(400);
		driver.findElement(By.id("refreshButton")).click();
		Thread.sleep(400);
		driver.findElement(By.id("saveButton")).click();

		Thread.sleep(1000);
		driver.findElement(By.id("editRef12")).click();
		driver.findElement(By.id("titleInput")).clear();
		Thread.sleep(400);
		driver.findElement(By.id("titleInput")).sendKeys("Sales year over year");
		Thread.sleep(400);
		driver.findElement(By.id("queryInput")).sendKeys("select quarter,sum(quantity) from sales group by quarter order by year");
		Thread.sleep(400);
		driver.findElement(By.id("chartSelect")).sendKeys("Column Chart");
		Thread.sleep(400);
		driver.findElement(By.id("databaseSelect")).sendKeys("MySQL");
		Thread.sleep(400);
		driver.findElement(By.id("colSpanInput")).clear();
		Thread.sleep(400);
		driver.findElement(By.id("colSpanInput")).sendKeys("2");
		Thread.sleep(400);
		driver.findElement(By.id("refreshButton")).click();
		Thread.sleep(400);
		driver.findElement(By.id("saveButton")).click();

		Thread.sleep(1000);
		driver.findElement(By.id("saveReportButton")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("savePublicRef")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//button[contains(.,'Ok')]")).click();
		Thread.sleep(1000);
	}
}
