package com.lwr.software.reporter.admin.schedmgmt;

import java.util.Date;

import com.lwr.software.reporter.DashboardConstants.Destination;
import com.lwr.software.reporter.DashboardConstants.OutputFormat;
import com.lwr.software.reporter.DashboardConstants.Recurrence;
import com.lwr.software.reporter.DashboardConstants.Status;

public class Schedule {

	private String scheduleName;
	
	private String reportName;
	
	private OutputFormat format = OutputFormat.HTML;
	
	private Destination destination = Destination.EMAIL;
	
	private long lastRunTime;
	
	private Status status = Status.UNKNOWN;
	
	private Recurrence recurrence = Recurrence.HOURLY;
	
	private int interval;
	
	private String smtpHost;
	
	private String smtpPort;
	
	private String senderEmail;
	
	private String receiverEmail;
	
	private String folderName;
	
	private Date startDate;
	
	public Schedule(){
		
	}

	public Schedule(String jsonString){
		
	}
	
	public String getScheduleName() {
		return scheduleName;
	}

	public void setScheduleName(String name) {
		this.scheduleName = name;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public OutputFormat getFormat() {
		return format;
	}

	public void setFormat(OutputFormat format) {
		this.format = format;
	}

	public Destination getDestination() {
		return destination;
	}

	public void setDestination(Destination destination) {
		this.destination = destination;
	}

	public long getLastRunTime() {
		return lastRunTime;
	}

	public void setLastRunTime(long lastRunTime) {
		this.lastRunTime = lastRunTime;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public String getSmtpHost() {
		return smtpHost;
	}

	public void setSmtpHost(String smtpHost) {
		this.smtpHost = smtpHost;
	}

	public String getSmtpPort() {
		return smtpPort;
	}

	public void setSmtpPort(String smtpPort) {
		this.smtpPort = smtpPort;
	}

	public String getSenderEmail() {
		return senderEmail;
	}

	public void setSenderEmail(String senderEmail) {
		this.senderEmail = senderEmail;
	}

	public String getReceiverEmail() {
		return receiverEmail;
	}

	public void setReceiverEmail(String receiverEmail) {
		this.receiverEmail = receiverEmail;
	}

	
	public Recurrence getRecurrence() {
		return recurrence;
	}

	public void setRecurrence(Recurrence recurrence) {
		this.recurrence = recurrence;
	}

	public String getFolderName() {
		return folderName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null)
			return false;
		Schedule inSched = (Schedule)obj;
		return inSched.getScheduleName().equalsIgnoreCase(this.getScheduleName());
	}
	
	@Override
	public int hashCode() {
		return this.getScheduleName().hashCode();
	}
}
