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

package com.lwr.software.reporter.admin.connmgmt;

public class ConnectionParams {

	private String username;
	
	private String password;
	
	private String driver;
	
	private String url;
	
	private String alias;
	
	private String isDefault="false"; 
	
	private String isConnectionSuccess="false";

	public ConnectionParams(){
		
	}
	
	public ConnectionParams(String username, String password, String driver, String url, String alias){
		this.username=username;
		this.password=password;
		this.driver=driver;
		this.url=url;
		this.alias=alias;
	}

	public String getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}

	public String getIsConnectionSuccess() {
		return isConnectionSuccess;
	}

	public void setIsConnectionSuccess(String isConnectionSuccess) {
		this.isConnectionSuccess = isConnectionSuccess;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
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

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	@Override
	public boolean equals(Object obj) {
		ConnectionParams invar = (ConnectionParams)obj;
		return getAlias().equalsIgnoreCase(invar.getAlias());
	}
	
	@Override
	public int hashCode() {
		return getAlias().hashCode();
	}
	
	@Override
	public String toString() {
		return "alias: "+this.alias+", username: "+this.username+", driver: "+this.driver+", url: "+this.url;
	}
}
