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

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.lwr.software.reporter.DashboardConstants;
import com.lwr.software.reporter.reportmgmt.ReportParameter;

public class DWHUtility {

	private Connection connection;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy HH:mm:ss");

	public DWHUtility(Connection connection) {
		this.connection = connection;
	}
	
	public List<List<Object>> executeQuery(String sql,Set<ReportParameter> reportParams) throws Exception {
		if(sql == null)
			return new ArrayList<>();
		List<List<Object>> rows = new ArrayList<List<Object>>();
		Map<String,List<Integer>> paramToIndexMap = new HashMap<String,List<Integer>>();
		Map<String,ReportParameter> paramMap = new HashMap<String,ReportParameter>();
		
		if(reportParams != null){
			for (ReportParameter reportParam : reportParams) 
				paramMap.put(reportParam.getName(), reportParam);
		}
		
		int index=1;
		String toMatch = "\\{[_a-z0-9]+:[_a-z0-9]+\\}|\\{[_a-z0-9]+\\}";
		Pattern pattern = Pattern.compile(toMatch);
		Matcher matcher = pattern.matcher(sql);
		while(matcher.find()){
			int startIndex = matcher.start();
			int endIndex = matcher.end();
			String paramName = sql.substring(startIndex+1, endIndex-1);
		
			String[] ps = paramName.split(":");
			String key=ps[0];
			if(ps.length==2)
				key=ps[1];
			
			List<Integer> indices = paramToIndexMap.get(key);
			if(indices == null){
				indices = new ArrayList<Integer>();
				paramToIndexMap.put(key, indices);
			}
			
			ReportParameter reportParam = paramMap.get(key);
			if(reportParam.getDataType().equals("list")){
				String value = reportParam.getValue();
				String patterns[] = value.split(",");
				String bindString="";
				for( int i=0;i<patterns.length;i++){
					indices.add(index++);
					bindString=bindString+"?,";
				}
				bindString = bindString.substring(0,bindString.lastIndexOf(","));
				sql = sql.replace("{list:"+reportParam.getName()+"}","("+bindString+")");

			}else{
				indices.add(index++);
				sql = sql.replaceAll("\\{"+reportParam.getDataType()+":"+reportParam.getName()+"\\}","?");
				sql = sql.replaceAll("\\{"+reportParam.getName()+"\\}","?");

			}
			matcher = pattern.matcher(sql);
		}
		
		try {
			PreparedStatement stmt = this.connection.prepareStatement(sql);
			stmt.setFetchSize(1000);
			if(reportParams != null){
				for (ReportParameter reportParam : reportParams) {
					String paramName = reportParam.getName();
					String paramType = reportParam.getDataType();
					List<Integer> indices = paramToIndexMap.get(paramName);
					if(indices == null)
						continue;
					String value = reportParam.getValue();
					String patterns[] = value.split(",");
					int i=0;
					for (Integer ind : indices) {
						if(paramType.equals("string"))
							stmt.setString(ind, reportParam.getValue());
						else if(paramType.equals("numeric"))
							stmt.setDouble(ind, Double.parseDouble(reportParam.getValue()));
						else if(paramType.equals("date"))
							stmt.setDate(ind, new Date(sdf.parse(reportParam.getValue()).getTime()));
						else if(paramType.equals("datetime"))
							stmt.setTimestamp(ind,new Timestamp(sdf.parse(reportParam.getValue()).getTime()));
						else if(paramType.equals("list")){
							stmt.setObject(ind, patterns[i++]);
						}
					}
				}
			}
			ResultSet res = stmt.executeQuery();
			int columns = res.getMetaData().getColumnCount();
			List<Object> header = new ArrayList<Object>();
			Map<Integer,Integer> dataTypeMap = new HashMap<Integer,Integer>();
			for (int i = 0; i < columns; i++) {
				String type = DashboardConstants.STRING;
				int dataType = res.getMetaData().getColumnType(i+1);
				if( dataType == Types.CHAR || dataType == Types.VARCHAR || dataType == Types.LONGNVARCHAR || dataType == Types.NVARCHAR)
					type = DashboardConstants.STRING;
				else if ( dataType == Types.BIGINT || dataType == Types.DECIMAL || dataType == Types.DOUBLE || dataType == Types.FLOAT || dataType == Types.INTEGER || dataType == Types.NUMERIC || dataType == Types.SMALLINT || dataType == Types.TINYINT || dataType == Types.REAL)
					type = DashboardConstants.NUMBER;
				else if ( dataType == Types.TIME || dataType == Types.TIMESTAMP || dataType == Types.DATE)
					type = DashboardConstants.DATETIME;
				else if ( dataType == Types.BOOLEAN)
					type = DashboardConstants.BOOLEAN;
				header.add(type+":"+res.getMetaData().getColumnLabel(i+1));
				dataTypeMap.put((i+1), dataType);
			}
			rows.add(header);
			while(res.next()){
				List<Object> row = new ArrayList<Object>();
				for (int i = 0; i < columns; i++) {
					Object obj  = res.getObject(i+1);
					int dataType = dataTypeMap.get((i+1));
					if ( dataType == Types.TIME || dataType == Types.TIMESTAMP || dataType == Types.DATE){
						Timestamp ts = (Timestamp)obj;
						String value = sdf.format(ts);
						row.add(value);
					}else{
						row.add(obj);
					}
				}
				rows.add(row);
			}
		} catch (Exception e) {
			throw e;
		}
		return rows;
	}
	
	public long executeCountQuery(String sql) {
		long toReturn = -1;
		try {
			Statement stmt = this.connection.createStatement();
			ResultSet res = stmt.executeQuery(sql);
			if(res.next())
				return res.getLong(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return toReturn;
	}	
}
