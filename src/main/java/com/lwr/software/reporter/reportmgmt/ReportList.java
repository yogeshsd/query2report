package com.lwr.software.reporter.reportmgmt;

import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="ReportList")
@XmlAccessorType(XmlAccessType.FIELD)
public class ReportList {
	
	@XmlElement(name="Reports")
	private Set<Report> reportList;
	
	public Set<Report> getReportList() {
		return reportList;
	}

	public void setReportList(Set<Report> reportList) {
		this.reportList = reportList;
	}
}
