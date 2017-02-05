package com.lwr.software.reporter.utils;

import java.sql.Connection;
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

import com.lwr.software.reporter.DashboardConstants;

public class DWHUtility {

	private Connection connection;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");

	public DWHUtility(Connection connection) {
		this.connection = connection;
	}
	
	public List<List<Object>> executeQuery(String sql) throws SQLException {
		if(sql == null)
			return new ArrayList<>();
		List<List<Object>> rows = new ArrayList<List<Object>>();
		try {
			Statement stmt = this.connection.createStatement();
			ResultSet res = stmt.executeQuery(sql);
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
		} catch (SQLException e) {
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
