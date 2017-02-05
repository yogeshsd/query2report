package com.lwr.software.reporter.reportmgmt;

import java.util.ArrayList;
import java.util.List;

public class RowElement {
	
	private List<Element> elements;
	
	public RowElement(){
	}
	
	public List<Element> getElements() {
		return elements;
	}
	
	public void setElements(List<Element> elements) {
		this.elements = elements;
	}

	public RowElement newInstance() {
		RowElement newInstance = new RowElement();
		newInstance.elements = new ArrayList<Element>();
		if(this.elements != null){
			for (Element element : elements) {
				newInstance.elements.add(element.newInstance());
			}
		}
		return newInstance;
	}
}
