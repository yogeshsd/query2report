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
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import junit.framework.Assert;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CurrencyConversion {
	
	private static WebDriver driver;
	
	@BeforeClass
	public static void init(){
		driver = new ChromeDriver();
		driver.get("chrome://settings/");
		driver.manage().window().maximize();
		driver.manage().window().fullscreen();
		driver.manage().timeouts().implicitlyWait(30,TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(30,TimeUnit.SECONDS);
		try{
			driver.get("http://localhost:8080/q2r/login.html");
			Thread.sleep(1000);
			driver.findElement(By.id("username")).sendKeys("admin");
			driver.findElement(By.id("password")).sendKeys("admin");
			driver.findElement(By.id("loginButton")).click();
			Thread.sleep(1000);
		}catch(Throwable e){
			e.printStackTrace();
		}
	}
	
	@AfterClass
	public static void destroy(){
		try{
			Thread.sleep(1000);
			driver.findElement(By.id("usericon")).click();
			Thread.sleep(1000);
			driver.findElement(By.id("logoutRef")).click();
			Thread.sleep(1000);
			driver.close();
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
		createReport("Indian Rupee Conversion Rate","Conversion rate of Indian rupee against different currencies of the world like USD, GBP and EUR. The data displayed is over the years value day by day.");
		openReport("Indian Rupee Conversion Rate",10000);
	}

	private void openReport(String title, long sleepTime) throws InterruptedException {
		Thread.sleep(1000);
		driver.findElement(By.id("publicmgmt")).click();
		driver.findElement(By.id(title+"OpenRef")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("currency")).sendKeys("USD");
		driver.findElement(By.id("applyButton")).click();
		Thread.sleep(sleepTime);
		driver.findElement(By.id("addStats00")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("showMean00")).click();
		Thread.sleep(5000);
		driver.findElement(By.id("addStats00")).click();
		Thread.sleep(1000);		
		driver.findElement(By.id("showSd00")).click();
		Thread.sleep(5000);
		Thread.sleep(sleepTime);
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
		driver.findElement(By.id("editRef00")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("titleInput")).clear();
		Thread.sleep(1000);
		driver.findElement(By.id("titleInput")).sendKeys("Conversion rate of Indian Rupee ( INR ) over the years");
		Thread.sleep(1000);
		driver.findElement(By.id("queryInput")).sendKeys("select date,price from q2r.currency_conversion where from_cur={string:currency}");
		Thread.sleep(1000);
		driver.findElement(By.id("chartSelect")).sendKeys("Area Chart");
		Thread.sleep(1000);
		driver.findElement(By.id("databaseSelect")).sendKeys("MySQL");
		Thread.sleep(1000);
		driver.findElement(By.id("colSpanInput")).clear();
		Thread.sleep(1000);
		driver.findElement(By.id("colSpanInput")).sendKeys("1");
		Thread.sleep(1000);
		driver.findElement(By.id("refreshButton")).click();
		
		Thread.sleep(1000);
		driver.findElement(By.id("refreshButton")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("currency")).sendKeys("USD");
		Thread.sleep(1000);
		driver.findElement(By.id("refreshButton")).click();

		Thread.sleep(5000);
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
