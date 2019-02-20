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
import org.openqa.selenium.chrome.ChromeDriver;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserMgmtChangeAdminPwdTest {
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
	public void step1ChangePassword() throws InterruptedException {
		login("admin","admin");
		Thread.sleep(1000);
		driver.findElement(By.id("usericon")).click();
		Thread.sleep(500);
		driver.findElement(By.id("updateProfileRef")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("passwordInput")).sendKeys("admin1234");
		driver.findElement(By.id("confirmPasswordInput")).sendKeys("admin1234");
		driver.findElement(By.id("saveUserButton")).click();
		Thread.sleep(1000);
		logout();
		Thread.sleep(1000);
		login("admin","admin1234");
		Thread.sleep(1000);
		logout();
	}
	
	@Test
	public void step2ResetToDefault() throws InterruptedException {
		login("admin","admin1234");
		Thread.sleep(1000);
		driver.findElement(By.id("usericon")).click();
		Thread.sleep(500);
		driver.findElement(By.id("updateProfileRef")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("passwordInput")).sendKeys("admin");
		driver.findElement(By.id("confirmPasswordInput")).sendKeys("admin");
		driver.findElement(By.id("saveUserButton")).click();
		Thread.sleep(1000);
		logout();
		Thread.sleep(1000);
		login("admin","admin");
		Thread.sleep(1000);
		logout();
	}
}
