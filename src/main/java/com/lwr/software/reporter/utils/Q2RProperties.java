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
