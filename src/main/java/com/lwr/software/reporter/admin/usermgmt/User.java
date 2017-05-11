package com.lwr.software.reporter.admin.usermgmt;

import com.lwr.software.reporter.DashboardConstants;

public class User {

	private String username;
	
	private String password;
	
	private String displayName;
	
	private String chartType = DashboardConstants.HTML_JFREE;
	
	private String role = DashboardConstants.ADMIN_USER;
	
	private long refreshInterval = DashboardConstants.DEFAULT_REFRESH_INTERVAL_MILLIS;
	
	public User(){
		
	}
	
	public long getRefreshInterval() {
		return refreshInterval;
	}
	
	public void setRefreshInterval(long refreshInterval) {
		this.refreshInterval = refreshInterval;
	}
	
	public User(String displayName,String username,String password, String role, String chartType){
		this.displayName=displayName;
		this.username=username;
		this.password=password;
		this.role=role;
		this.chartType=chartType;
	}

	public String getChartType() {
		return chartType;
	}

	public void setChartType(String chartType) {
		this.chartType = chartType;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj==null)
			return false;
		User user = (User)obj;
		return getUsername().equalsIgnoreCase(user.getUsername());
	}
	
	@Override
	public int hashCode() {
		return getUsername().hashCode();
	}
	@Override
	public String toString() {
		return "username: "+this.username+", displayName: "+this.displayName+", role: "+this.role+", chartType: "+this.chartType+", refreshInterval: "+this.refreshInterval;
	}
}
