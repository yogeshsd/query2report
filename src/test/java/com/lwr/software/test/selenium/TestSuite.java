package com.lwr.software.test.selenium;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)				
@Suite.SuiteClasses({				
	SetupConfiguration.class,
	BuildingReportTestCases.class,
	UserMgmtNewUserTest.class,
	UnSupportedGraphTest.class
})		

public class TestSuite {				
}