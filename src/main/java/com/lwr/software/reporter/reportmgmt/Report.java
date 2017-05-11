package com.lwr.software.reporter.reportmgmt;

import java.util.ArrayList;
import java.util.List;

public class Report {

	private String title;
	
	private String description;
	
	private String aurthor;
	
	private long creationDate;
	
	private int maxrows;
	
	private List<RowElement> rows;

	private long modifiedDate;
	
	public String getAurthor() {
		return aurthor;
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
		return newReport;
	}

	public void setModifiedDate(long modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	
	public long getModifiedDate() {
		return modifiedDate;
	}
}
