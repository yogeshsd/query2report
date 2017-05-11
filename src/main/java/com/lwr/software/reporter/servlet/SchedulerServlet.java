package com.lwr.software.reporter.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.lwr.software.reporter.admin.schedmgmt.ScheduleManager;

public class SchedulerServlet extends HttpServlet{

	@Override
	public void init() throws ServletException {
		ScheduleManager.getScheduleManager();
	}
}
