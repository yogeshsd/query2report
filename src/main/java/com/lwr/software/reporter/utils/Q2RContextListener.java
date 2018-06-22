package com.lwr.software.reporter.utils;

import java.io.File;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.lwr.software.reporter.DashboardConstants;

public class Q2RContextListener implements ServletContextListener {
	
	private static Logger logger = LogManager.getLogger(Q2RContextListener.class);

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		logger.info("Creating directories if already created");
		
		logger.info("Creating config directory tree "+DashboardConstants.CONFIG_PATH);
		File dir = new File(DashboardConstants.CONFIG_PATH);
		if(dir.exists()){
			logger.info("Config directory tree already exists");
		}else{			
			boolean dirCreated = dir.mkdirs();
			if(dirCreated){
				logger.info("Config directory tree created successfully");
			}else{
				logger.error("Config directory tree creation failed, please check for file permission on "+DashboardConstants.CONFIG_PATH);
			}
		}
		
		
		logger.info("Creating public report directory tree "+DashboardConstants.PUBLIC_REPORT_DIR);
		dir = new File(DashboardConstants.PUBLIC_REPORT_DIR);
		if(dir.exists()){
			logger.info("Public report directory tree already exists");
		}else{			
			boolean dirCreated = dir.mkdirs();
			if(dirCreated){
				logger.info("Public report directory tree created successfully");
			}else{
				logger.error("Public report directory tree creation failed, please check for file permission on "+DashboardConstants.PUBLIC_REPORT_DIR);
			}
		}
		
		logger.info("Creating private report directory tree "+DashboardConstants.PRIVATE_REPORT_DIR);
		dir = new File(DashboardConstants.PRIVATE_REPORT_DIR);
		if(dir.exists()){
			logger.info("Private report directory tree already exists");
		}else{			
			boolean dirCreated = dir.mkdirs();
			if(dirCreated){
				logger.info("Private report directory tree created successfully");
			}else{
				logger.error("Private report directory tree creation failed, please check for file permission on "+DashboardConstants.PRIVATE_REPORT_DIR);
			}
		}
	}

}
