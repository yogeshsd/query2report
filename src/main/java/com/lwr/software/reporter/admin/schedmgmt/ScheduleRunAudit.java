package com.lwr.software.reporter.admin.schedmgmt;

import com.lwr.software.reporter.DashboardConstants.Status;

public class ScheduleRunAudit {

	private String reportName;
	
	private String scheduleName;
	
	private String userName;
	
	private Status status;
	
	private long nextRunTime;
	
	private long lastRunTime;

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public String getScheduleName() {
		return scheduleName;
	}

	public void setScheduleName(String scheduleName) {
		this.scheduleName = scheduleName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public long getNextRunTime() {
		return nextRunTime;
	}

	public void setNextRunTime(long nextRunTime) {
		this.nextRunTime = nextRunTime;
	}

	public long getLastRunTime() {
		return lastRunTime;
	}

	public void setLastRunTime(long lastRunTime) {
		this.lastRunTime = lastRunTime;
	}
}
