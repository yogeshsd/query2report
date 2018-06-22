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

package com.lwr.software.reporter.reportmgmt;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.CollectionType;
import org.codehaus.jackson.map.type.TypeFactory;

import com.lwr.software.reporter.DashboardConstants;

public class ReportManager {
	
	private static ReportManager manager;
	
	private Map<String,Map<String,Report>> userReportMap = new LinkedHashMap<String,Map<String,Report>>();
	
	private static Logger logger = LogManager.getLogger(ReportManager.class);

	public static ReportManager getReportManager(){
		if(manager == null){
			synchronized (ReportManager.class) {
				if(manager == null){
					manager = new ReportManager();
				}
			}
		}
		return manager;
	}
	
	private ReportManager(){
		init(DashboardConstants.PUBLIC_USER);
	}

	private void init(String userName) {
		String dirName = DashboardConstants.PUBLIC_REPORT_DIR;
		if(!userName.equalsIgnoreCase(DashboardConstants.PUBLIC_USER))
			dirName = DashboardConstants.PRIVATE_REPORT_DIR+userName;
		logger.debug("Initializing report manager for user "+userName+" from "+new File(dirName).getAbsolutePath());
		Map<String,Report> reportMap = new LinkedHashMap<String,Report>();
		File dir = new File(dirName);
		dir.mkdirs();
		String reportFiles[] = dir.list();
		if(reportFiles == null || reportFiles.length==0){
			logger.warn("Got 0 reports for user "+userName);
			return;
		}
		logger.debug("Got "+reportFiles.length+" reports for user "+userName);
		for(String reportFile : reportFiles){
			File f = new File(reportFile);
			if(f.isDirectory() || reportFile.equalsIgnoreCase("schedule"))
				continue;
		    try {
		    	File fn = new File(dir.getAbsolutePath()+File.separatorChar+reportFile);
		    	logger.info("Loading report template from file '"+fn.getAbsolutePath()+"'");
		    	ObjectMapper objectMapper = new ObjectMapper();
		        TypeFactory typeFactory = objectMapper.getTypeFactory();
		        CollectionType collectionType = typeFactory.constructCollectionType(Set.class, Report.class);
		        Set<Report> reports =  objectMapper.readValue(fn, collectionType);
		        for (Report report : reports) 
		        	reportMap.put(report.getTitle(), report);
		    } catch (IOException e) {
		    	logger.error("Error while loading report from template "+dir.getAbsoluteFile(),e);
		    }
		}
		userReportMap.put(userName, reportMap);
	}

	public boolean saveReport(Report reports[],String dashboardname,String userName) {
		return serializeReport(reports,dashboardname,userName);
	}

	public Report getReport(String reportTitle,String userName) {
		logger.info("Getting report "+reportTitle+" for user "+userName);
		Map<String, Report> map = userReportMap.get(userName);
		if(map == null || map.isEmpty()){
			logger.error("Unable to find report "+reportTitle+" for user "+userName);
			return null;
		}
		Report report = map.get(reportTitle)==null?null:map.get(reportTitle).newInstance();
		if(report == null){
			logger.error("Unable to find report "+reportTitle+" for user "+userName);
		}else{
			logger.info("Returning report "+report+" for user "+userName);
		}
		return report; 
	}

	public Map<String,Map<String,Report>> getReports(String userName) {
		Map<String,Map<String,Report>> reps = new HashMap<String,Map<String,Report>>();
		if(userName==null)
			return reps;
		logger.debug("Getting report list for user "+userName);
		init(userName);
		Map<String, Report> privateReports = userReportMap.get(userName);
		if(privateReports != null){
			logger.debug("Returning "+privateReports.size()+" reports for user "+userName);
			reps.put(userName,privateReports);
		}else{
			logger.warn("Returning 0 reports for user "+userName);
		}
		return reps;
	}
	
	public void reload(){
		init(DashboardConstants.PUBLIC_REPORT_DIR);
		Set<String> userNames = userReportMap.keySet();
		if(userNames == null || userNames.isEmpty())
			return;
		for (String userName : userNames) {
			if(userName.equalsIgnoreCase(DashboardConstants.PUBLIC_USER))
				continue;
			init(userName);
		}
	}
	
	public void reload(String userName){
		init(userName);
	}

	public boolean deleteReport(String userName, String reportName) {
		logger.info("Deleting report "+reportName+" for user "+userName);
		Map<String, Report> reports = userReportMap.get(userName);
		reports.remove(reportName);
		File path = new File(DashboardConstants.PRIVATE_REPORT_DIR+File.separatorChar+userName+File.separatorChar+reportName);
		if(userName.equals(DashboardConstants.PUBLIC_USER))
			path = new File(DashboardConstants.PUBLIC_REPORT_DIR+File.separatorChar+reportName);
		logger.info("Deleting report template file "+path.getAbsoluteFile());
		boolean isDeleted= path.delete();
		if(isDeleted){
			logger.info("Report "+reportName+" and template "+path+" file is successfully deleted!!");
			return true;
		}
		else{
			logger.info("Unable to delete report "+reportName+" and template "+path+" file");
			return false;
		}
	}

	private boolean serializeReport(Report reports[],String dashboardname,String userName){
		try{
	    	ObjectMapper objectMapper = new ObjectMapper();
	    	for (Report report : reports) {
	    		logger.info("Seralizing report "+report.getTitle()+" for user "+userName);
	        	Map<String, Report> map = userReportMap.get(userName);
	        	if(map == null || map.isEmpty()){
	        		map = new LinkedHashMap<String,Report>();
	        		userReportMap.put(userName, map);
	        	}
	        	if(map.get(report.getTitle())==null){
	        		report.setCreationDate(System.currentTimeMillis());
	        	}else{
	        		report.setCreationDate(map.get(report.getTitle()).getCreationDate());
	        	}
	        	report.setModifiedDate(System.currentTimeMillis());
				map.put(report.getTitle(), report);
	    	}
	        String dataToRight = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(reports);
	        FileWriter writer = null;
	        if(userName.equalsIgnoreCase(DashboardConstants.PUBLIC_USER)){
	        	String fName = DashboardConstants.PUBLIC_REPORT_DIR+dashboardname;
	        	logger.info("File name to persist the report template is "+fName);
	        	writer = new FileWriter(fName);
	        }
	        else{
	        	File path = new File(DashboardConstants.PRIVATE_REPORT_DIR+File.separatorChar+userName+File.separatorChar);
	        	logger.info("Creating path "+path.getAbsoluteFile()+" to seralize report template ");
	        	path.mkdirs();
	        	String fName = DashboardConstants.PRIVATE_REPORT_DIR+File.separatorChar+userName+File.separatorChar+dashboardname;
	        	logger.info("File name to persist the report template is "+fName);
	        	writer = new FileWriter(fName);
	        }
	        writer.write(dataToRight);
	        writer.flush();
	        writer.close();
	        return true;
		}catch(Exception e){
			logger.error("Error seralizing report template files for user "+userName,e);
			return false;
		}
	}
}
