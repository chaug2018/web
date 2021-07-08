package com.yzj.ebs.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.format.Border;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.struts2.ServletActionContext;

/**
 * 
 * 创建于:2012-10-23<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 用于生根据提供的表头调用相应方法获取值
 * 
 * @author ShiJiangmin
 * @version 1.0.0
 */
public class DataExporter {
	private String tableName;
	private List<?> exportData;
	private LinkedHashMap<String,Map<String,String>> tableHeader; // 要求以“,”分隔
	private String tableHeaderCN; // 中文表头
	private String fileName;

	public DataExporter(String tableName, List<?> exportData,
			LinkedHashMap<String,Map<String,String>> tableHeader, String tableHeaderCN) {
		this.tableName = tableName;
		this.exportData = exportData;
		this.tableHeader = tableHeader;
		this.tableHeaderCN = tableHeaderCN;
		String uuid = UUID.randomUUID().toString();
		this.fileName = new SimpleDateFormat("yyyyMMdd").format(new Date())
				+ tableName + uuid;
	}

	/**
	 * 根据表头动态调用get方法
	 * 
	 * @return list 对应于表格中的一条记录的所有值List
	 * @throws Throwable
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List getDataValues() throws Throwable {

		Set<String> headers = tableHeader.keySet();
		List list = new ArrayList();
		for (Object o : exportData) {
			for (Iterator it = headers.iterator();it.hasNext();) {
				// 动态获取方法名
				String str = (String) it.next();
				String str1 =  str.substring(0, 1).toUpperCase();
				String str2 = str1 + str.substring(1);
				String methodName = "get" + str2;
				// 调用方法
				String dataValue;
				Method method = o.getClass().getMethod(methodName, null);
				if (method.invoke(o, null) == null) {
					dataValue = "";
				}else{
					dataValue = method.invoke(o, null) + "";
				}
				
				//判断是否是Map,如果是Map则遍历匹配
				if(tableHeader.get(str)!=null){
					Map<String,String> refMap = tableHeader.get(str);
					Set<String> refKey = refMap.keySet();
					for (Iterator it1 = refKey.iterator(); it1.hasNext();) {
			            String s = (String) it1.next();
			            if(dataValue.equals(s)){
			            	dataValue = refMap.get(s);
			            	break;
			            }
			            
					}
				}
				list.add(dataValue);
			}
		}
		return list;
	}

	/**
	 * 构造表格内容
	 * 
	 * @throws Throwable
	 */
	@SuppressWarnings({ "rawtypes", "deprecation" })
	public void createExcel() throws Throwable {
		File path = new File("exportTmp");
		if(!path.exists()){
			path.mkdir();
		}
		File file = new File("exportTmp",fileName + ".xls");
		WritableWorkbook book = Workbook.createWorkbook(file);
		WritableSheet sheet = book.createSheet("sheet1", 0);

		//设置格式
		jxl.write.WritableFont font1 = new jxl.write.WritableFont(
				jxl.write.WritableFont.TIMES, 16, jxl.write.WritableFont.BOLD);
		jxl.write.WritableFont font3 = new jxl.write.WritableFont(
				jxl.write.WritableFont.TIMES, 10, jxl.write.WritableFont.BOLD);

		jxl.write.WritableCellFormat CBwcfF1 = new jxl.write.WritableCellFormat(
				font1);
		jxl.write.WritableCellFormat CBwcfF2 = new jxl.write.WritableCellFormat(
				font3);
		jxl.write.WritableCellFormat CBwcfF3 = new jxl.write.WritableCellFormat();
		
		CBwcfF1.setAlignment(jxl.write.Alignment.CENTRE);
		CBwcfF2.setAlignment(jxl.write.Alignment.CENTRE);
		CBwcfF2.setBorder(Border.ALL, jxl.format.BorderLineStyle.THIN,
				Colour.BLACK);
		CBwcfF3.setBorder(Border.ALL, jxl.format.BorderLineStyle.THIN,
				Colour.BLACK);

		//填充内容
		Label label = new Label(0, 0, tableName,CBwcfF1);
		Set<String> headers = tableHeader.keySet();
		String[] headersCN = tableHeaderCN.split(",");
		int columnCount = headers.size();
		sheet.addCell(label);
		sheet.mergeCells(0, 0, columnCount - 1, 0);

		for (int i = 0; i < columnCount; i++) {
			Label headLabel = new Label(i, 1, headersCN[i],CBwcfF2);
			sheet.addCell(headLabel);
		}
		
		int recordCount = exportData.size();
		List list = new ArrayList();
		list = getDataValues();
		
		for (int lineCount = 1; lineCount <= recordCount; lineCount++) {
			for (int j = 0; j < columnCount; j++) {
				Label valueLabel = new Label(j, lineCount + 1,
						(String) list.get((lineCount - 1) * columnCount + j),CBwcfF3);
				sheet.addCell(valueLabel);
			}
		}
		book.write();	
		book.close();	
	}


	
	/**
	 * 下载并删除文件
	 * 
	 * @param response
	 * @throws IOException
	 */
	public void downloadLocal(String fileName)throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		File exportFile = new File("exportTmp",fileName + ".xls");
		String filenamedisplay = fileName + ".xls";
		FileInputStream inStream = null;
		OutputStream output = null;
		try{
			inStream = new FileInputStream(exportFile.getPath()); // 文件的存放路径
			// 设置输出的格式
			filenamedisplay = URLEncoder.encode(filenamedisplay, "UTF-8");
			response.setContentType("application/x-download");
			response.setHeader("Content-Disposition", "attachment;filename=\""
					+ filenamedisplay);
			response.setContentLength((int) exportFile.length());
			// 循环取出流中的数据
			byte[] b=new byte[1024];
			int len;
			 output = response.getOutputStream();
			while ((len = inStream.read(b)) != -1) {
				output.write(b,0,len);
			}
		}catch(IOException e){
			throw e;
		}finally{
			try {
				inStream.close();
			} catch (IOException e1) {
			}
			output.close();
			// 删除服务器临时文件
			if (exportFile != null && exportFile.exists()) {
				exportFile.delete();
			}
		}		
	}
	
	/**
	 * 导出数据文件
	 * @param fileName
	 * @throws IOException
	 */
	public void downloadTxtDataToLocal(String fileName,String name) throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		File exportFile = new File(fileName);
		InputStream inStream = new FileInputStream(exportFile.getPath()); // 文件的存放路径
		// 设置输出的格式
		name = URLEncoder.encode(name, "GBK");
		response.reset();
		response.setContentType("application/x-download");
		response.addHeader("Content-Disposition", "attachment;filename=\""
				+ name);
		response.setContentLength((int) exportFile.length());
		// 循环取出流中的数据
		int len;
		PrintWriter output = response.getWriter();
		while ((len = inStream.read()) != -1) {
			output.write(len);
		}
		inStream.close();
		// 删除服务器临时文件
		if (exportFile != null && exportFile.exists()) {
			exportFile.delete();
		}
	}
	

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public List<?> getExportData() {
		return exportData;
	}

	public void setExportData(List<?> exportData) {
		this.exportData = exportData;
	}

	public LinkedHashMap<String, Map<String, String>> getTableHeader() {
		return tableHeader;
	}

	public void setTableHeader(
			LinkedHashMap<String, Map<String, String>> tableHeader) {
		this.tableHeader = tableHeader;
	}

	public String getTableHeaderCN() {
		return tableHeaderCN;
	}

	public void setTableHeaderCN(String tableHeaderCN) {
		this.tableHeaderCN = tableHeaderCN;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
}
