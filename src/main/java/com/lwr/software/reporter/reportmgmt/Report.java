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

import java.util.ArrayList;
import java.util.List;

public class Report {

	private String title;
	
	private String description;
	
	private String aurthor;
	
	private long creationDate;
	
	private int maxrows=0;
	
	private List<RowElement> rows;

	private long modifiedDate;
	
	private List<ReportParameter> params;
	
	private List<LinkedElementParameter> linkedParams;
	
	public List<LinkedElementParameter> getLinkedParams() {
		return linkedParams;
	}

	public void setLinkedParams(List<LinkedElementParameter> linkedParams) {
		this.linkedParams = linkedParams;
	}

	public String getAurthor() {
		return aurthor;
	}

	public List<ReportParameter> getParams() {
		return params;
	}

	public void setParams(List<ReportParameter> params) {
		this.params = params;
	}

	public void setAurthor(String aurthor) {
		this.aurthor = aurthor;
	}

	public long getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(long creationDate) {
		this.creationDate = creationDate;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getmaxrows() {
		return maxrows;
	}

	public void setmaxrows(int maxrows) {
		this.maxrows = maxrows;
	}

	public List<RowElement> getRows() {
		return rows;
	}

	public void setRows(List<RowElement> rows) {
		this.rows = rows;
	}
	
	public synchronized Report newInstance(){
		Report newReport = new Report();
		newReport.title=this.title;
		newReport.description=this.description;
		newReport.maxrows=this.maxrows;
		newReport.rows= new ArrayList<>();
		if(this.rows!= null){
			for (RowElement rowElement : this.rows) {
				newReport.rows.add(rowElement.newInstance());
			}
		}
		newReport.params=this.params;
		return newReport;
	}

	public void setModifiedDate(long modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	
	public long getModifiedDate() {
		return modifiedDate;
	}
}
