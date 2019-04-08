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

public class RowElement {
	
	private List<Element> elements;
	
	private int numCols = 0;
	
	private int rowSpan = 1;
	
	public RowElement(){
	}
	
	public int getRowSpan() {
		return rowSpan;
	}

	public void setRowSpan(int rowSpan) {
		this.rowSpan = rowSpan;
	}

	public List<Element> getElements() {
		return elements;
	}
	
	public int getNumCols() {
		return numCols;
	}

	public void setNumCols(int numCols) {
		this.numCols = numCols;
	}

	public void setElements(List<Element> elements) {
		this.elements = elements;
	}

	public RowElement newInstance() {
		RowElement newInstance = new RowElement();
		this.numCols=0;
		newInstance.elements = new ArrayList<Element>();
		if(this.elements != null){
			for (Element element : elements) {
				numCols+=element.colSpan;
				newInstance.elements.add(element.newInstance());
			}
		}
		newInstance.numCols=numCols;
		newInstance.rowSpan=this.rowSpan;
		return newInstance;
	}
}
