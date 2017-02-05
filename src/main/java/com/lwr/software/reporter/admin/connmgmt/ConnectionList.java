package com.lwr.software.reporter.admin.connmgmt;

import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="ConnectionList")
@XmlAccessorType(XmlAccessType.FIELD)

public class ConnectionList {
	@XmlElement(name="Connection")
	private Set<ConnectionParams> connectionList;
	
	public Set<ConnectionParams> getConnectionList() {
		return connectionList;
	}
	
	public void setConnectionList(Set<ConnectionParams> connectionList) {
		this.connectionList = connectionList;
	}
}
