/**
 * 创建于:2013-9-20
 * 版权所有(C) 2013 深圳市银之杰科技股份有限公司
 * @author lixiangjun
 * @version 1.1
 */
package com.yzj.ebs.edata.service.impl;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.yzj.ebs.common.IPublicTools;
import com.yzj.ebs.common.OperLogModuleDefine;
import com.yzj.ebs.edata.action.EDataAction;
import com.yzj.ebs.edata.dao.impl.WFDataDao;
import com.yzj.ebs.edata.service.DataProcessUtil;
import com.yzj.wf.core.service.po.IOrganizeInfoAdm;
import com.yzj.wf.core.service.po.IPeopleInfoAdm;

public class DataImportAuto {

	private IPublicTools tools;
	
	private IPeopleInfoAdm peopleInfoAdm; // 人员处理

	private IOrganizeInfoAdm biz;
	
	private DataProcessServiceImpl dataProcess; // 数据处理服务
	
	public DataProcessUtil dataUtil=new DataProcessUtil();
	
	private WFDataDao wfDataDao;
	
	public WFDataDao getWfDataDao() {
		return wfDataDao;
	}


	public void setWfDataDao(WFDataDao wfDataDao) {
		this.wfDataDao = wfDataDao;
	}


	public void insertOrgAndPerpleToWf(String tableName){
		dataUtil.insertOrgAndPerpleToWf(dataProcess, tools, biz, peopleInfoAdm, tableName,wfDataDao);
	}
	
	
	/**
	 * 湘江银行的数据文件的导入：将数据推送过来的数据文件导入到数据的临时表中
	 * @param fileName 文件名
	 * @param tableName 表名称
	 * @param startLine  已经导入的条目数
	 * fileName = 表名   如  MAINDATA.dat
	 * 分隔符 str.split("");
	 * @return
	 */
	public void insertToOracle(String dataDate,String path,String fileName,
			long startLine,String splitchar,int paramNum,String okFlag)throws Exception{
		String tableName = "EBS_"+fileName;
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(new File(path+fileName+".dat")), "UTF-8"));
			String readLine;
			long lineTemp = 0; //临时存储行数
			boolean isImport = false; //是否导入
			List<String> paramValuesList = new ArrayList<String>();
			while((readLine = reader.readLine())!=null){
				//每10000条向数据库插入一次，并用startLine记录以及插入到数据库的条目数,startLine为700的整数倍
				if(paramValuesList.size()==10000){
					dataProcess.batchInsertDate(tableName,
							paramValuesList, dataDate);
					paramValuesList.clear();
					startLine = lineTemp;
				}
				//如果已经导入的条目数和文件读取的行数相同的话，则执行导入。这样做的效果是支持续传
				if(lineTemp == startLine){
					isImport = true;
				}
				if(isImport){
					if((readLine.trim()).length()>0){
						StringBuffer paramValues = new StringBuffer();
						List<String> dataStr=dataProcess.analyzeDate(readLine,splitchar,paramNum);
						for(String str:dataStr){
							paramValues.append(" '"+str.trim()+"',");
						}
						paramValuesList.add(paramValues.toString().substring(
								0, paramValues.toString().length() - 1));
					}
				}
				lineTemp++;
			}
			/**
			 * 到这里，数据读取已经完成了，如果paramValuesList中依然有数据，说明paramValuesList的数据不足1000条，并且为最后的数据，
			 * 将这不足10000的条的最后的数据插入数据库中，则此文件的数据导入操作完成
			 */
			if(paramValuesList.size()>0){
				dataProcess.batchInsertDate(tableName,
						paramValuesList, dataDate);
				paramValuesList.clear();
				startLine = lineTemp;
			}
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String nowTime = sf.format(new Date());
			EDataAction.MSGINFO="数据日期："+dataDate+", 信息：文件:"+fileName+"导入成功，共导入"+startLine+"条";
			tools.getDataLogUtils().info("操作完成时间:"+nowTime+",数据日期："+dataDate+", 信息：文件:"+fileName+"导入成功，共导入"+startLine+"条",String.valueOf(OperLogModuleDefine.dataprocess),dataDate);
		} catch (Exception e) {
			EDataAction.MSGINFO="数据日期："+dataDate+", 信息：文件:"+fileName+"数据导入时发生异常";
			tools.getDataLogUtils().error(EDataAction.MSGINFO, e,String.valueOf(OperLogModuleDefine.dataprocess),dataDate);
			//如果数据导入发生了异常，则在ok文件中记录当前文件已经导入完成了多少条，避免数据重复导入。减少数据导入的时间
			importSchedule(path+"/"+okFlag,tableName+"="+startLine);
			throw new Exception("文件:"+fileName+"数据导入时发生异常" + e.getMessage());
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (Exception e) {
					tools.getDataLogUtils().error("关闭数据流时发生异常", e,String.valueOf(OperLogModuleDefine.dataprocess));
				}
			}
		}
	}
	
	/*
	 * 如果数据导发生异常，记录下当前数据文件数据导入的进度 
	 */
	public void importSchedule(String fileName,String errorLine){
		BufferedOutputStream out = null;
		try{
			out = new BufferedOutputStream(new FileOutputStream(new File(fileName)));
			out.write(errorLine.getBytes());
			out.flush();
			out.close();
		}catch(Exception e){
			tools.getDataLogUtils().error("保存数据导入进度出错", e,String.valueOf(OperLogModuleDefine.dataprocess));
		}finally {
			if (out != null) {
				try {
					out.close();
				} catch (Exception e) {
					tools.getDataLogUtils().error("关闭数据流时发生异常", e,String.valueOf(OperLogModuleDefine.dataprocess));
				}
			}
		}	
	}
	
	
	public long checkTxtTotalLine(String fileName) {
		long sumCount = 0;
		File file = new File(fileName);
		BufferedReader reader = null;
		LineNumberReader lineNumberReader = null;
		try {
			long fileLength = file.length();
			lineNumberReader = new LineNumberReader(new FileReader(file));
			lineNumberReader.skip(fileLength);
			sumCount = lineNumberReader.getLineNumber();
		} catch (IOException e) {
			tools.getDataLogUtils().error("计算文件共有多少行发生异常", e,String.valueOf(OperLogModuleDefine.dataprocess));
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return sumCount;
	}
	
	
	public IPublicTools getTools() {
		return tools;
	}

	public void setTools(IPublicTools tools) {
		this.tools = tools;
	}

	public IPeopleInfoAdm getPeopleInfoAdm() {
		return peopleInfoAdm;
	}

	public void setPeopleInfoAdm(IPeopleInfoAdm peopleInfoAdm) {
		this.peopleInfoAdm = peopleInfoAdm;
	}

	public IOrganizeInfoAdm getBiz() {
		return biz;
	}

	public void setBiz(IOrganizeInfoAdm biz) {
		this.biz = biz;
	}

	public DataProcessServiceImpl getDataProcess() {
		return dataProcess;
	}

	public void setDataProcess(DataProcessServiceImpl dataProcess) {
		this.dataProcess = dataProcess;
	}
	
}
