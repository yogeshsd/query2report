package com.lwr.software.reporter;

import java.io.File;

public interface DashboardConstants {

	public static final String PATH="..";
	public static final String PRIVATE_REPORT_DIR = DashboardConstants.PATH+File.separatorChar+"dashboard"+File.separatorChar+"reports"+File.separatorChar+"private"+File.separatorChar;
	public static final String PUBLIC_REPORT_DIR = DashboardConstants.PATH+File.separatorChar+"dashboard"+File.separatorChar+"reports"+File.separatorChar+"public"+File.separatorChar;
	public static final String CONFIG_PATH = DashboardConstants.PATH+File.separatorChar+"dashboard"+File.separatorChar+"config"+File.separatorChar;
	public static final String TEMP_PATH = DashboardConstants.PATH+File.separatorChar+"dashboard"+File.separatorChar+"temp"+File.separatorChar;
	public static final String PRODUCT_NAME="Lite Weight Reporter";
	public static final String PIE_CHART_TYPE = "pie";
	public static final String BAR_CHART_TYPE = "bar";
	public static final String BAR_STACK_CHART_TYPE = "barstack";
	public static final String ELEMENT_TYPE = "element";
	public static final String CELL_TYPE = "cell";
	public static final String DASHBOARD_DIR=File.separator+"dashboard"+File.separator;
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
}
