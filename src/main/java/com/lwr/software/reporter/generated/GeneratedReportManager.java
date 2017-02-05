package com.lwr.software.reporter.generated;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import com.lwr.software.reporter.DashboardConstants;

public class GeneratedReportManager {
	
	private Set<String> reportFileNames = new HashSet<String>();
	
	private String userName;

	public GeneratedReportManager(String userName) {
		this.userName = userName;
	}
	
	public void buildList(){
		File dir = new File(DashboardConstants.PRIVATE_REPORT_DIR+File.separatorChar+userName+File.separatorChar+"schedule"+File.separatorChar);
		String[] files = dir.list();
		for (String file : files) {
			if(file.equalsIgnoreCase("schedules.json"))
				continue;
			reportFileNames.add(file);
		}
	}
	
	public Set<String> getGeneratedReports(){
		return this.reportFileNames;
	}
}
