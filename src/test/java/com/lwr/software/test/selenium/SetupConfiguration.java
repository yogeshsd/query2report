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
public class SetupConfiguration {
	
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
		driver.get("http://localhost:8080/q2r/login.html");
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
		driver.findElement(By.id("jarFileUploadSelectInput")).sendKeys("D:\\Work\\jars\\mysql-connector-java-8.0.13.jar");
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
}
