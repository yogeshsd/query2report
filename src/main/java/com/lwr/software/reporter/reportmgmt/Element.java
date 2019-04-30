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

package com.lwr.software.reporter.reportmgmt;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.lwr.software.reporter.DashboardConstants;
import com.lwr.software.reporter.admin.connmgmt.ConnectionPool;
import com.lwr.software.reporter.utils.DWHUtility;

public class Element {

    protected String id;

    protected String dbalias;

    protected String title;

    protected String query;

    protected String chartType;

    protected long refreshInterval = -1;

    protected boolean hasParams;

    protected boolean hasDependency;

    protected int colSpan = 1;

    protected int rowSpan = 1;

    private Set<ReportParameter> params;

    private Set<LinkedElementParameter> linkedParams;

    public int getRowSpan() {
		return rowSpan;
	}

	public void setRowSpan(int rowSpan) {
		this.rowSpan = rowSpan;
	}

	public int getColSpan() {
		return colSpan;
	}

	public void setColSpan(int colSpan) {
		this.colSpan = colSpan;
	}

	public Set<LinkedElementParameter> getLinkedParams() {
		return linkedParams;
	}

	public void setLinkedParams(Set<LinkedElementParameter> linkedParams) {
		this.linkedParams = linkedParams;
	}

    public boolean isHasDependency() {
		return hasDependency;
	}

	public void setHasDependency(boolean hasDependency) {
		this.hasDependency = hasDependency;
	}

	public Set<ReportParameter> getParams() {
		return params;
	}

	public void setParams(Set<ReportParameter> params) {
		this.params = params;
	}

	public boolean isHasParams() {
		return hasParams;
	}

	public void setHasParams(boolean hasParams) {
		this.hasParams = hasParams;
	}

	@JsonIgnore
    protected List<Object> headers;

    @JsonIgnore
    protected List<List<Object>> data;

    @JsonIgnore
    protected int dimCount=0;

    @JsonIgnore
    protected int metricCount=0;

    @JsonIgnore
    protected int stringCount=0;

    @JsonIgnore
    protected int dateCount=0;

    @JsonIgnore
    protected int booleanCount=0;

	@JsonIgnore
    protected List<List<Object>> processedData = new ArrayList<List<Object>>();

    @JsonIgnore
    protected Map<Integer,String> indexToDataTypeMap = new HashMap<Integer,String>();

    @JsonIgnore
    protected Map<Integer,String> indexToColNameMap = new HashMap<Integer,String>();

    @JsonIgnore
    protected Map<String,List<Integer>> dataTypeToIndex = new HashMap<String,List<Integer>>();

    @JsonIgnore
    protected Map<String,Set<String>> dataTypeToColumnNames = new HashMap<String,Set<String>>();

	private JSONArray jsonData;

	public long getRefreshInterval() {
		return refreshInterval;
	}

	public void setRefreshInterval(long refreshInterval) {
		this.refreshInterval = refreshInterval;
	}

	public List<List<Object>> getProcessedData() {
		return processedData;
	}

	public void setProcessedData(List<List<Object>> processedData) {
		this.processedData = processedData;
	}

	public Map<Integer, String> getIndexToDataTypeMap() {
		return indexToDataTypeMap;
	}

	public void setIndexToDataTypeMap(Map<Integer, String> indexToDataTypeMap) {
		this.indexToDataTypeMap = indexToDataTypeMap;
	}

	public Map<String, List<Integer>> getDataTypeToIndex() {
		return dataTypeToIndex;
	}

	public void setDataTypeToIndex(Map<String, List<Integer>> dataTypeToIndex) {
		this.dataTypeToIndex = dataTypeToIndex;
	}

	public List<Object> getHeader() {
		return headers;
	}

	public void setHeader(List<Object> header) {
		this.headers = header;
	}

	public List<List<Object>> getData() {
		return data;
	}

	public void setData(List<List<Object>> data) {
		if(data == null)
			return;
		this.data = data;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDbalias() {
		return dbalias;
	}

	public void setDbalias(String dbalias) {
		this.dbalias = dbalias;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getChartType() {
		return chartType;
	}

	public void setChartType(String chartType) {
		this.chartType = chartType;
	}

	public int getDimCount() {
		return dimCount;
	}

	public void setDimCount(int dimCount) {
		this.dimCount = dimCount;
	}

	public int getMetricCount() {
		return metricCount;
	}

	public void setMetricCount(int metricCount) {
		this.metricCount = metricCount;
	}

	public int getStringCount() {
		return stringCount;
	}

	public void setStringCount(int stringCount) {
		this.stringCount = stringCount;
	}

	public int getDateCount() {
		return dateCount;
	}

	public void setDateCount(int dateCount) {
		this.dateCount = dateCount;
	}

	public int getBooleanCount() {
		return booleanCount;
	}

	public void setBooleanCount(int booleanCount) {
		this.booleanCount = booleanCount;
	}

	public void clear(){
		if(this.headers!=null)
			this.headers.clear();
		if(this.data!=null)
			this.data.clear();
		if(this.indexToDataTypeMap!=null)
			this.indexToDataTypeMap.clear();
		if(this.dataTypeToIndex!=null)
			this.dataTypeToIndex.clear();
		if(this.processedData!=null)
			this.processedData.clear();
	}

	public void init() throws Exception{
		if (this.getQuery() == null)
			return;

		String sql = this.getQuery();
		String dbalias = this.getDbalias();
		if (dbalias == null){
			dbalias="default";
		}
		Connection connection = ConnectionPool.getInstance().getConnection(dbalias);
		if (connection == null){
			throw new SQLException("Unable to get connection to alias '"+dbalias+"'");
		}

		List<List<Object>> rows = new ArrayList<>();
		DWHUtility utility = new DWHUtility(connection);

		try {
			rows = utility.executeQuery(sql,this.params);
		} catch (Exception e) {
			throw e;
		} finally{
			ConnectionPool.getInstance().releaseConnection(connection, dbalias);
		}

		if (rows == null || rows.isEmpty())
			return;

		List<Object> headers = rows.get(0);
		this.setHeader(headers);

		rows.remove(0);

		jsonData = new JSONArray();
		this.setData(rows);

		processMetaData();
		processData();

		JSONObject fullJson = new JSONObject();
		JSONArray jd = new JSONArray();
		for (List<Object> row : rows) {
			JSONObject obj = new JSONObject();
			int i = 0;
			for (Object col : row) {
				obj.put(indexToColNameMap.get(i++), col);
			}
			jd.add(obj);
		}
		fullJson.put("headers", headers);
		fullJson.put("data", jd);
		jsonData.add(fullJson);
	}

	public void setJsonData(JSONArray jsonData) {
		this.jsonData = jsonData;
	}

	public JSONArray getJsonData() {
		return jsonData;
	}



	private void processMetaData() {
		int index=0;
		for (Object column : headers) {
			String head = ((String)column).replaceAll("'", "");
			String hs[] = head.split(":");

			List<Integer> indices = dataTypeToIndex.get(hs[0]);
			if(indices == null){
				indices = new ArrayList<Integer>();
				dataTypeToIndex.put(hs[0], indices);
			}
			indices.add(index);

			Set<String> colNames = dataTypeToColumnNames.get(hs[0]);
			if(colNames == null){
				colNames = new HashSet<String>();
				dataTypeToColumnNames.put(hs[0],colNames );
			}
			colNames.add(hs[1]);

			indexToDataTypeMap.put(index, hs[0]);
			indexToColNameMap.put(index, hs[1]);
			index++;
		}

		List<Integer> dateIndices = dataTypeToIndex.get(DashboardConstants.DATETIME);
		List<Integer> stringIndices = dataTypeToIndex.get(DashboardConstants.STRING);
		List<Integer> metricIndices = dataTypeToIndex.get(DashboardConstants.NUMBER);

		dateCount = dateIndices==null?0:dateIndices.size();
		stringCount = stringIndices==null?0:stringIndices.size();
		metricCount = metricIndices==null?0:metricIndices.size();
		dimCount = stringCount+dateCount;
	}

	private void processData() {
		for (List<Object> row : data) {
			List<Object> modifiedRow = new ArrayList<Object>();

			List<Integer> indices = dataTypeToIndex.get(DashboardConstants.DATETIME);
			if(indices != null){
				for (Integer ind : indices)
					modifiedRow.add(row.get(ind));
			}

			indices = dataTypeToIndex.get(DashboardConstants.STRING);
			if(indices != null){
				for (Integer ind : indices)
					modifiedRow.add(row.get(ind));
			}

			indices = dataTypeToIndex.get(DashboardConstants.BOOLEAN);
			if(indices != null){
				for (Integer ind : indices)
					modifiedRow.add(row.get(ind));
			}

			indices = dataTypeToIndex.get(DashboardConstants.NUMBER);
			if(indices != null){
				for (Integer ind : indices)
					modifiedRow.add(row.get(ind));
			}
			processedData.add(modifiedRow);
		}
	}

	@JsonIgnore
	public Set<String> getDimColNames() {
		Set<String> toReturn = new HashSet<String>();
		Set<String> dateTimeCols = this.dataTypeToColumnNames.get(DashboardConstants.DATETIME);
		if(dateTimeCols!=null)
			toReturn.addAll(dateTimeCols);

		Set<String> stringCols = this.dataTypeToColumnNames.get(DashboardConstants.STRING);
		if(stringCols!=null)
			toReturn.addAll(stringCols);

		return toReturn;
	}

	@JsonIgnore
	public Set<String> getMetricColNames() {
		Set<String> toReturn = new HashSet<String>();
		Set<String> metricCols = this.dataTypeToColumnNames.get(DashboardConstants.NUMBER);
		if(metricCols!=null)
			toReturn.addAll(metricCols);
		return toReturn;

	}

	public Element(String query,String chartType,String dbalias) {
		this.query=query;
		this.chartType=chartType;
		this.dbalias=dbalias;
	}

	public Element(){
	}

	public Element newInstance() {
		Element newInstance = new Element();
		newInstance.id=this.id;
		newInstance.dbalias=this.dbalias;
		newInstance.title=this.title;
		newInstance.query=this.query;
		newInstance.chartType=this.chartType;
		newInstance.hasParams=this.hasParams;
		newInstance.colSpan=this.colSpan;
		newInstance.rowSpan=this.rowSpan;
		newInstance.refreshInterval=this.refreshInterval;
		return newInstance;
	}

	public List<Object> getHeaders() {
		return headers;
	}

	public void setHeaders(List<Object> header) {
		this.headers = header;
	}

}
