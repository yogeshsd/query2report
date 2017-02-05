package com.lwr.software.reporter.admin.schedmgmt;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="ScheduleList")
@XmlAccessorType(XmlAccessType.FIELD)
public class ScheduleList {
	
	@XmlElement(name="Schedule")
	private Set<Schedule> scheduleList;
	
	public Set<Schedule> getScheduleList() {
		return scheduleList;
	}

	public void setScheduleList(Set<Schedule> userList) {
		this.scheduleList = userList;
	}
}