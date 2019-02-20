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

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnore;

import com.lwr.software.reporter.DashboardConstants;
import com.lwr.software.reporter.utils.EncryptionUtil;

public class User {

	@JsonIgnore
	private static Logger logger = LogManager.getLogger(User.class);
			
	private String username;
	
	private String password;
	
	private String displayName;
	
	private String chartType = DashboardConstants.HTML_JFREE;
	
	private String role = DashboardConstants.ADMIN_USER;
	
	private long refreshInterval = DashboardConstants.DEFAULT_REFRESH_INTERVAL_MILLIS;
	
	private int sessionTimeout = DashboardConstants.DEFAULT_SESSION_TIMEOUT;
	
	private String authToken = "";

	public User(){
		
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
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
	
	
	public String loginUser(String inPassword){
		String encPassword = getPassword();
		String inEncPassword = EncryptionUtil.encrypt(inPassword);
		if(encPassword.equals(inEncPassword)){
			return generateToken();
		}
		throw new RuntimeException("Unauthorized user. Invalid username or password.");
	}

	
	@JsonIgnore
	public String generateToken(){
		SecureRandom randomGenerator = new SecureRandom();
		Integer randomNumber = randomGenerator.nextInt(10000);
		double currNanoTime = System.nanoTime();
		double durationNanoTime = ((double)this.sessionTimeout)*(1000000000);
		double expNanoTime = currNanoTime+durationNanoTime;
		String key = this.username+":"+randomNumber.toString()+":"+expNanoTime;
		byte[] dataBytes = key.getBytes(StandardCharsets.UTF_8);
		authToken = username+"_0_"+Base64.getEncoder().encodeToString(dataBytes)+"_0_"+role;
		System.out.println(authToken);
		logger.info("Token for user "+this.username+" is "+this.authToken);
//		authToken = username+"_0_"+key+"_0_"+role;
		return authToken;
	}

	@JsonIgnore
	public boolean isValidToken(String token){
		logger.info("For user "+this.username+" in token is "+token+" stored token is "+this.authToken);
		if(authToken!=null && !authToken.equals(token)){
			logger.error("Corrupt token");
			return false;
		}
		
		String tokenPatterns[] = token.split("_0_");
		if(tokenPatterns == null || tokenPatterns.length!=3){
			return false;
		}
		
		byte[] data = Base64.getDecoder().decode(tokenPatterns[1]);
		String decodedToken = new String(data, StandardCharsets.UTF_8);
		tokenPatterns = decodedToken.split(":");
		if(tokenPatterns.length != 3){
			logger.error("Corrupt token");
			return false;
		}
		double expNanoTime = Double.parseDouble(tokenPatterns[2]);
		double currNanoTime = System.nanoTime();
		logger.info("User "+this.username+" token will expire in "+((expNanoTime-currNanoTime)/10E9)+" seconds.");
		System.out.println("User "+this.username+" token will expire in "+((expNanoTime-currNanoTime)/1E9)+" seconds.");
		if(currNanoTime < expNanoTime ){
			return true;
		}else{
			logger.error("Token expired");
			return false;
		}
	}

	public void logoutUser() {
		this.authToken="";
		logger.info("For user "+this.username+" logging out and setting authToken to null");
	}
}
