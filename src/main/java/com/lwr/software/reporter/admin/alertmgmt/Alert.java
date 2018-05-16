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

package com.lwr.software.reporter.admin.alertmgmt;

public class Alert {

	private String message;
	
	private String resolutionMessage;
	
	private Status alertStatus = Status.WARNING;
	
	public static enum Status {WARNING,ERROR}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Status getAlertStatus() {
		return alertStatus;
	}

	public void setAlertStatus(Status alertStatus) {
		this.alertStatus = alertStatus;
	}

	public String getResolutionMessage() {
		return resolutionMessage;
	}

	public void setResolutionMessage(String resolutionMessage) {
		this.resolutionMessage = resolutionMessage;
	};
}
