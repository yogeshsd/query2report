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
