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
