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

package com.lwr.software.reporter.restservices;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.lwr.software.reporter.reportmgmt.Element;
import com.lwr.software.reporter.reportmgmt.Report;
import com.lwr.software.reporter.reportmgmt.ReportManager;
import com.lwr.software.reporter.reportmgmt.ReportParameter;
import com.lwr.software.reporter.reportmgmt.RowElement;

@Path("/export/")
public class ReportExportService {
	
	private static Logger logger = LogManager.getLogger(ReportExportService.class);


	@Path("/csv/{userName}/{reportName}")
	@POST
	@Produces("text/csv")
	public Response exportCsv(
			@PathParam("reportName") String reportName,
			@PathParam("userName") String userName,
			Set<ReportParameter> reportParams
			){
		Map<String,Map<String,Report>> userToReport = ReportManager.getReportManager().getReports(userName);
		Set<String> keys = userToReport.keySet();
		Report toExport = null;
		boolean reportFound=false;
		for (String key : keys) {
			Map<String, Report> value = userToReport.get(key);
			Collection<Report> reps = value.values();
			for (Report report : reps){ 
				if(reportName.equalsIgnoreCase(report.getTitle())){
					reportFound=true;
					toExport=report;
					break;
				}
			}
		}
		if(reportFound && toExport != null){
			return exportCsv(toExport,reportParams);
		}else{
			return Response.serverError().entity("Unable to export '"+reportName+"' report for user "+userName).build();
		}
	}
	
	private Response exportCsv(Report toExport, Set<ReportParameter> reportParams) {
		try {
			File file = new File(toExport.getTitle());
			System.out.println(file.getAbsoluteFile());
			FileWriter writer = new FileWriter(file.getAbsoluteFile());
			writer.write("Report : "+toExport.getTitle()+"\n");
			List<RowElement> rows = toExport.getRows();
			for (RowElement rowElement : rows) {
				List<Element> elements = rowElement.getElements();
				for (Element element : elements) {
					writer.write("\nElement : "+element.getTitle()+"\n");
					try {
						element.setParams(reportParams);
						element.init();
						List<List<Object>> data = element.getData();
						for (List<Object> row : data) {
							StringBuffer toWrite = new StringBuffer();
							for (Object cell : row) {
								toWrite.append(cell.toString()+",");
							}
							writer.write(toWrite.toString()+"\n");
						}
					} catch (Exception e) {
						logger.error("Unable to init '"+element.getTitle()+"' element of report '"+toExport.getTitle()+"' Error "+e.getMessage(),e);
						return Response.serverError().entity("Unable to init '"+element.getTitle()+"' element of report '"+toExport.getTitle()+"' Error "+e.getMessage()).build();
					}
				}
			}
			writer.flush();
			writer.close();
			ResponseBuilder responseBuilder = Response.ok((Object) file);
			responseBuilder.header("Access-Control-Allow-Origin","*");
			responseBuilder.header("Content-Type", "text/csv");
			responseBuilder.header("Content-Disposition", "attachment;filename="+file.getName()+".csv");
			responseBuilder.header("Content-Length", file.length());
			return responseBuilder.build();
		} catch (IOException e1) {
			logger.error("Unable to export '"+toExport.getTitle()+"' report ",e1);
			return Response.serverError().entity("Unable to export '"+toExport.getTitle()+"' report "+e1.getMessage()).build();
		}
	}

	@Path("/excel/{userName}/{reportName}")
	@POST
	public Response exportExcel(
			@PathParam("reportName") String reportName,
			@PathParam("userName") String userName,
			Set<ReportParameter> reportParams
			){
		Map<String,Map<String,Report>> userToReport = ReportManager.getReportManager().getReports(userName);
		Set<String> keys = userToReport.keySet();
		Report toExport = null;
		boolean reportFound=false;
		for (String key : keys) {
			Map<String, Report> value = userToReport.get(key);
			Collection<Report> reps = value.values();
			for (Report report : reps){ 
				if(reportName.equalsIgnoreCase(report.getTitle())){
					reportFound=true;
					toExport=report;
					break;
				}
			}
		}
		if(reportFound && toExport != null){
			return exportExcel(toExport,reportParams);
		}else{
			return Response.serverError().entity("Unable to export '"+reportName+"' report for user "+userName).build();
		}
	}

	public Response exportExcel(Report toExport,Set<ReportParameter> reportParams){
        Workbook wb = new XSSFWorkbook();
        
        Font boldFont = wb.createFont();
        boldFont.setBold(true);
        
        CellStyle headerStyle = wb.createCellStyle();
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setFillBackgroundColor(IndexedColors.BLUE.getIndex());
        headerStyle.setFont(boldFont);
        
        
        
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);


        CellStyle titleStyle = wb.createCellStyle();
        titleStyle.setBorderBottom(BorderStyle.THIN);
        titleStyle.setBorderTop(BorderStyle.THIN);
        titleStyle.setBorderRight(BorderStyle.THIN);
        titleStyle.setBorderLeft(BorderStyle.THIN);
        
		List<RowElement> rows = toExport.getRows();
		int sheetIndex=0;
		for (RowElement rowElement : rows) {
			List<Element> elements = rowElement.getElements();
			for (Element element : elements) {
				try {
					element.setParams(reportParams);
					element.init();
				} catch (Exception e) {
					logger.error("Unable to init '"+element.getTitle()+"' element of report '"+toExport.getTitle()+"' Error "+e.getMessage(),e);
					return Response.serverError().entity("Unable to init '"+element.getTitle()+"' element of report '"+toExport.getTitle()+"' Error "+e.getMessage()).build();
				}
				String sheetName = element.getTitle().substring(0,element.getTitle().length()>30?30:element.getTitle().length())+(sheetIndex++);
				Sheet sheet = wb.createSheet(sheetName);
				
				Row reportTitleRow = sheet.createRow(0);
				Cell reportTitleHeader = reportTitleRow.createCell(0);
				reportTitleHeader.setCellStyle(headerStyle);
				reportTitleHeader.setCellValue("Report Title:");
				
				Cell reportTitleCell = reportTitleRow.createCell(1);
				reportTitleCell.setCellStyle(titleStyle);
				reportTitleCell.setCellValue(toExport.getTitle());

				Row elementTitleRow = sheet.createRow(1);
				Cell elementTitleHeader = elementTitleRow.createCell(0);
				elementTitleHeader.setCellStyle(headerStyle);
				elementTitleHeader.setCellValue("Element Title:");

				Cell elementTitleCell = elementTitleRow.createCell(1);
				elementTitleCell.setCellStyle(titleStyle);
				elementTitleCell.setCellValue(element.getTitle());
				
				List<List<Object>> dataToExport = element.getData();
				
				int rowIndex = 3;
				Row headerRow = sheet.createRow(rowIndex++);
				List<Object> unifiedHeaderRow = element.getHeader();
				for (int i = 0; i < unifiedHeaderRow.size(); i++) {
					Cell headerCell = headerRow.createCell(i);
					String headerCellValue = unifiedHeaderRow.get(i).toString();
					headerCell.setCellValue(headerCellValue);
					headerCell.setCellStyle(headerStyle);
				}
				for (int i = 0 ; i < dataToExport.size() ; i ++) {
					Row row = sheet.createRow(rowIndex++);
					List<Object> unifiedRow = dataToExport.get(i);
					int cellIndex = 0;
					for (Object cellValue : unifiedRow) {
						Cell cell = row.createCell(cellIndex);
						cell.setCellStyle(cellStyle);
						try{
							double val = Double.parseDouble(cellValue.toString());
							cell.setCellValue(val);
						}catch(NumberFormatException e){
							cell.setCellValue(cellValue.toString());
						}
						cellIndex++;
					}
				}
			}
		}
		try {
			String fileName = System.getProperty("java.io.tmpdir")+File.pathSeparator+toExport.getTitle()+".xlsx";
			File file = new File(fileName);
			wb.write(new FileOutputStream(file));
			wb.close();
			ResponseBuilder responseBuilder = Response.ok((Object) file);
			responseBuilder.header("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			responseBuilder.header("Content-Transfer-Encoding","binary");
			responseBuilder.header("Content-Disposition", "attachment;filename="+fileName);
			responseBuilder.header("Content-Length", file.length());
			return responseBuilder.build();
		} catch (Exception e1) {
			return Response.serverError().entity("Unable to export "+toExport.getTitle()+" report "+e1.getMessage()).build();
		}

	}
}
