package com.lwr.software.reporter.admin.usermgmt;

import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="UserList")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserList {
	
	@XmlElement(name="User")
	private Set<User> userList;
	
	public Set<User> getUserList() {
		return userList;
	}

	public void setUserList(Set<User> userList) {
		this.userList = userList;
	}
}
