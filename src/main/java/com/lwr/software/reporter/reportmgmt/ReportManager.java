package com.lwr.software.reporter.reportmgmt;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.CollectionType;
import org.codehaus.jackson.map.type.TypeFactory;

import com.lwr.software.reporter.DashboardConstants;

public class ReportManager {
	
	private static ReportManager manager;
	
	private Map<String,Map<String,Report>> userReportMap = new LinkedHashMap<String,Map<String,Report>>();

	static{
		File dir = new File(DashboardConstants.PUBLIC_REPORT_DIR);
		dir.mkdirs();
	}
	
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
		Map<String,Report> reportMap = new LinkedHashMap<String,Report>();
		File dir = new File(dirName);
		dir.mkdirs();
		String reportFiles[] = dir.list();
		for(String reportFile : reportFiles){
			File f = new File(reportFile);
			if(f.isDirectory() || reportFile.equalsIgnoreCase("schedule"))
				continue;
		    try {
		    	ObjectMapper objectMapper = new ObjectMapper();
		        TypeFactory typeFactory = objectMapper.getTypeFactory();
		        CollectionType collectionType = typeFactory.constructCollectionType(Set.class, Report.class);
		        Set<Report> reports =  objectMapper.readValue(new File(dir.getAbsolutePath()+File.separatorChar+reportFile), collectionType);
		        for (Report report : reports) 
		        	reportMap.put(report.getTitle(), report);
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}
		userReportMap.put(userName, reportMap);
	}

	public Report getReport(String reportTitle,String userName) {
		Map<String, Report> map = userReportMap.get(userName);
		if(map == null || map.isEmpty())
			return null;
		return map.get(reportTitle)==null?null:map.get(reportTitle).newInstance();
	}

	private boolean serializeReport(Report reports[],String dashboardname,String userName){
		try{
	    	ObjectMapper objectMapper = new ObjectMapper();
	    	for (Report report : reports) {
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
	        if(userName.equalsIgnoreCase(DashboardConstants.PUBLIC_USER))
	        	writer = new FileWriter(DashboardConstants.PUBLIC_REPORT_DIR+dashboardname);
	        else{
	        	File path = new File(DashboardConstants.PRIVATE_REPORT_DIR+File.separatorChar+userName+File.separatorChar);
	        	path.mkdirs();
	        	writer = new FileWriter(DashboardConstants.PRIVATE_REPORT_DIR+File.separatorChar+userName+File.separatorChar+dashboardname);
	        }
	        writer.write(dataToRight);
	        writer.flush();
	        writer.close();
	        return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean saveReport(Report reports[],String dashboardname,String userName) {
		return serializeReport(reports,dashboardname,userName);
	}

	public Map<String,Map<String,Report>> getReports(String userName) {
		Map<String,Map<String,Report>> reps = new HashMap<String,Map<String,Report>>();
		if(userName==null)
			return reps;
		init(userName);
		Map<String, Report> privateReports = userReportMap.get(userName);
		if(privateReports != null){
			reps.put(userName,privateReports);
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
		Map<String, Report> reports = userReportMap.get(userName);
		reports.remove(reportName);
		File path = new File(DashboardConstants.PRIVATE_REPORT_DIR+File.separatorChar+userName+File.separatorChar+reportName);
		if(userName.equals(DashboardConstants.PUBLIC_USER))
			path = new File(DashboardConstants.PUBLIC_REPORT_DIR+File.separatorChar+reportName);
		System.out.println("Deleting file "+path.getAbsoluteFile());
		boolean isDeleted= path.delete();
		if(isDeleted)
			System.out.println("File is successfully deleted!!");
		else
			System.out.println("Unable to delete the file!!");
		return true;
	}
}
