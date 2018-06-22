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

package com.lwr.software.reporter.admin.usermgmt;

import com.lwr.software.reporter.DashboardConstants;

public class User {

	private String username;
	
	private String password;
	
	private String displayName;
	
	private String chartType = DashboardConstants.HTML_JFREE;
	
	private String role = DashboardConstants.ADMIN_USER;
	
	private long refreshInterval = DashboardConstants.DEFAULT_REFRESH_INTERVAL_MILLIS;
	
	private int sessionTimeout = DashboardConstants.DEFAULT_SESSION_TIMEOUT;
	
	public User(){
		
	}
	
	public int getSessionTimeout() {
		return sessionTimeout;
	}

	public void setSessionTimeout(int sessionTimeout) {
		this.sessionTimeout = sessionTimeout;
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
		return "[username: "+this.username+", displayName: "+this.displayName+", role: "+this.role+", chartType: "+this.chartType+", refreshInterval: "+this.refreshInterval+"]";
	}
}
