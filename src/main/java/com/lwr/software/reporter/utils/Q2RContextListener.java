/* 
	Query2Report Copyright (C) 2018  Yogesh Deshpande
	
	This file is part of Query2Report.
	
	Query2Report is free software: you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation, either version 3 of the License, or
	(at your option) any later version.
	
	Query2Report is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.
	
	You should have received a copy of the GNU General Public License
	along with Query2Report.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.lwr.software.reporter.utils;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.ws.rs.core.Response;

import org.apache.commons.compress.compressors.FileNameUtil;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.google.common.io.Files;
import com.lwr.software.reporter.DashboardConstants;

import jdk.nashorn.internal.runtime.Context;

public class Q2RContextListener implements ServletContextListener {
	
	private static Logger logger = LogManager.getLogger(Q2RContextListener.class);

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		try {
			Q2RProperties.getInstance().saveProperties();
		} catch (IOException e) {
			logger.error("Configuration save failed",e);
		}
	}

	@Override
	public void contextInitialized(ServletContextEvent contextEvent) {
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

		logger.info("Creating private report directory tree "+DashboardConstants.APPLN_TEMP_DIR);
		dir = new File(DashboardConstants.APPLN_TEMP_DIR);
		if(dir.exists()){
			logger.info("Private report directory tree already exists");
		}else{			
			boolean dirCreated = dir.mkdirs();
			if(dirCreated){
				logger.info("Private report directory tree created successfully");
			}else{
				logger.error("Private report directory tree creation failed, please check for file permission on "+DashboardConstants.APPLN_TEMP_DIR);
			}
		}

		File logoFile = new File(DashboardConstants.APPLN_LOGO_FILE);
		if(logoFile.exists()){
			String appLogo = contextEvent.getServletContext().getRealPath("/")+File.separatorChar+"images"+File.separatorChar+"q2r.png"; 
			File appLogoFile = new File(appLogo);
			logger.info("Coping of custom logo file "+logoFile.getAbsolutePath()+" to folder "+appLogoFile.getAbsolutePath());
			try {
				Files.copy(logoFile, appLogoFile);
				logger.info("Coping of custom logo file "+logoFile.getAbsolutePath()+" to folder "+appLogoFile.getAbsolutePath()+" -- Copied");
			} catch (IOException e) {
				logger.error("Coping of custom logo file "+logoFile.getAbsolutePath()+" to folder "+appLogoFile.getAbsolutePath()+" -- Failed",e);
			}
		}
		Q2RProperties.getInstance();
	}

}
