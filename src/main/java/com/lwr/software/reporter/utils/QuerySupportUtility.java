package com.lwr.software.reporter.utils;

import java.util.HashSet;
import java.util.Set;

import com.lwr.software.reporter.DashboardConstants;

public class QuerySupportUtility {
	
	
	public static Set<String> testSupport(int dimCount,int metricCount, int stringCount, int numRows, String chartType) {
		Set<String> messages = new HashSet<String>();
		if(chartType.equalsIgnoreCase(DashboardConstants.PIE_CHART_TYPE)){
			if(dimCount != 1 ){
				messages.add("Chart needs only 1 dimension but the query has "+dimCount+" dimension columns. Consider rewriting the query.");
			}
			if(metricCount != 1){
				messages.add("Chart needs only one metric by the query has "+metricCount+" metric columns. Consider limiting the rows.");
			}
			if(numRows>50){
				messages.add("Too many data point. Chart is best viewed with < 50 points but the query has "+numRows+" metric columns.");
			}
		}else if(
					chartType.equalsIgnoreCase(DashboardConstants.BAR_CHART_TYPE) 
					|| chartType.equalsIgnoreCase(DashboardConstants.BAR_STACK_CHART_TYPE)
					|| chartType.equalsIgnoreCase(DashboardConstants.COLUMN_CHART_TYPE)
					|| chartType.equalsIgnoreCase(DashboardConstants.COLUMN_STACK_CHART_TYPE)
					|| chartType.equalsIgnoreCase(DashboardConstants.LINE_CHART_TYPE)
				){
			if(dimCount > 2 || stringCount < 1){
				messages.add("Chart needs not more than 2 dimension but the query has "+dimCount+" dimension columns. Consider rewriting the query");
			}
			if(metricCount <1  || metricCount>4){
				messages.add("Chart needs atleast 1 and at max 4 metrics by the query has "+metricCount+" metric columns. Consider rewriting the query");
			}
			if(numRows>255 && !chartType.equalsIgnoreCase(DashboardConstants.LINE_CHART_TYPE)){
				messages.add("Too many data point. Chart is best viewed with < 255 points but the query has "+numRows+" metric columns. Consider limiting the rows");
			}
		}
		return messages;
	}
}
