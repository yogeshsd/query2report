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
