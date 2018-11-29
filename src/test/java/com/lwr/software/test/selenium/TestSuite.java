package com.lwr.software.test.selenium;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)				
@Suite.SuiteClasses({				
	UserMgmtChangeAdminPwdTest.class,
	Q2RE2ETestSuite.class,
	UserMgmtNewUserTest.class,
	UnSupportedGraphTest.class
})		

public class TestSuite {				
}