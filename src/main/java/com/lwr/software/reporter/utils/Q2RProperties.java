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
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.lwr.software.reporter.DashboardConstants;

public class Q2RProperties extends Properties{
	
	private static Logger logger = LogManager.getLogger(Q2RProperties.class);
	
	private static Q2RProperties properties = new Q2RProperties();
	
	public static Q2RProperties getInstance(){
		return properties;
	}
	
	private Q2RProperties() {
		File file = new File(DashboardConstants.PROP_FILE_PATH);
		
		if(!file.exists()){
			put("show_video_links", "true");
			put("show_feedback_links", "true");
			put("show_copyright_links", "true");
			put("send_sql_to_ui", "false");
			try {
				saveProperties();
			} catch (IOException e) {
				logger.error("Config properties file "+DashboardConstants.PROP_FILE_PATH+" write error.",e);
			}
		}else{
			if(get("show_video_links") == null)
				put("show_video_links", "true");	
			if(get("show_feedback_links") == null)
				put("show_feedback_links", "true");
			if(get("show_copyright_links") == null)
				put("show_copyright_links", "true");
			if(get("send_sql_to_ui") == null)
				put("send_sql_to_ui", "false");		
		}
		try {
			this.load(new FileReader(DashboardConstants.PROP_FILE_PATH));
		} catch (IOException e) {
			logger.error("Unable to load properties from "+DashboardConstants.PROP_FILE_PATH);
		}
	}
	
	public void saveProperties() throws IOException{
		this.store(new FileWriter(DashboardConstants.PROP_FILE_PATH), "Writing from Q2RProperties constructor");
	}

	public void setProperties(Properties properties) {
		this.putAll(properties);
	}
}
