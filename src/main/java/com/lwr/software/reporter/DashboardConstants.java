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

package com.lwr.software.reporter;

import java.io.File;

public interface DashboardConstants {

	public static final String PATH=System.getProperty("catalina.base");
	public static final String PRIVATE_REPORT_DIR = DashboardConstants.PATH+File.separatorChar+"q2rapp"+File.separatorChar+"reports"+File.separatorChar+"private"+File.separatorChar;
	public static final String PUBLIC_REPORT_DIR = DashboardConstants.PATH+File.separatorChar+"q2rapp"+File.separatorChar+"reports"+File.separatorChar+"public"+File.separatorChar;
	public static final String CONFIG_PATH = DashboardConstants.PATH+File.separatorChar+"q2rapp"+File.separatorChar+"config"+File.separatorChar;
	public static final String PRODUCT_NAME="Query2Report";
	public static final String PIE_CHART_TYPE = "pie";
	public static final String BAR_CHART_TYPE = "bar";
	public static final String BAR_STACK_CHART_TYPE = "barstack";
	public static final String ELEMENT_TYPE = "element";
	public static final String CELL_TYPE = "cell";
	public static final String COLUMN_CHART_TYPE = "column";
	public static final String LINE_CHART_TYPE = "line";
	public static final String TABLE_TYPE = "table";
	public static final String HTML_GOOGLE="html_google";
	public static final String CHART_OPTIONS = "chart";
	public static final String TABLE_OPTIONS = "table";
	public static final String STRING = "string";
	public static final String NUMBER = "number";
	public static final String BOOLEAN = "boolean";	
	public static final String USERNAME = "username";
	public static final String DATETIME = "datetime";
	public static final String PASSWORD = "password";
	public static final String CSV = "CSV";
	public static final String ADMIN_USER = "admin";
	public static final String COLUMN_STACK_CHART_TYPE = "columnstack";
	public static final String CHART_STACK_OPTIONS = "columnstack";
	public static final String SECURITY_CONTEXT = "SECURITY_CONTEXT";
	public static final String ANNOTATED_TYPE = "annotated";
	public static final String HTML_JFREE = "html_jfree";
	public static final String PDF="pdf";
	public static final String ALL_PAGES = "ALL_PAGES";
	public static final String HTML = "html";
	public static final long DEFAULT_REFRESH_INTERVAL_MILLIS = 300000;
	public static final Integer MAX_CONNECTIONS = 5;
	public static final String PUBLIC_USER = "public";
	public static final String LWR_SCHEDULE_JOB = "LWR_SCHEDULE_JOB";
	public static final String LWR_SCHEDULE_GROUP = "LWR_SCHEDULE_GROUP";
	public static enum Destination  {EMAIL,PersonalFolders};
	public static enum OutputFormat  {HTML,PDF,CSV};
	public static enum Status { SUCCESS,ERROR,RUNNING, UNKNOWN};
	public static enum Recurrence { HOURLY,DAILY,MONTHLY};
	public static final String ALGORITHM = "AES";
	public static final String ENCODING = "UTF-8";
	public static final String INIT_VECTOR = "liteweightreport";
	public static final String INIT_KEY = "liteweightreport";
	public static final String ADMIN = "admin";
	public static final String VIEW = "view";
	public static final String GUEST = "guest";
	public static final int DEFAULT_SESSION_TIMEOUT = 600;
}
