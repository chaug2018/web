package com.yzj.ebs.edata.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

import com.yzj.ebs.edata.common.CheckImportResult;
import com.yzj.ebs.edata.common.CheckInformation;
import com.yzj.ebs.edata.common.IDataImport;
import com.yzj.ebs.edata.common.IDisplay;
import com.yzj.ebs.edata.common.PublicDefine;
import com.yzj.ebs.edata.common.CheckInformation.InfoType;
import com.yzj.ebs.edata.tran.DataHttpService;
import com.yzj.wf.common.WFLogger;

//import dbutil.EDataDao;

/**
 * 创建于:2012-9-25<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 数据导入默认实现类
 * 
 * @author Lif
 * @version 1.0.0
 */
public class DataImportDefault implements IDataImport {
	private WFLogger logger = WFLogger.getLogger(DataImportDefault.class);
	DataHttpService dhs = new DataHttpService();
	private IDisplay display;
	private String dataDate;
	CheckImportResult result = new CheckImportResult();
	//EDataDao dao=new EDataDao();
	
	public DataImportDefault(String dataDate){
		super();
		this.dataDate=dataDate;
	}
	@Override
	public void setDisplay(IDisplay display) {
		this.display = display;
	}

	@Override
	public long checkLastImportLine(String tablename) {
		long lastImportNo = 0;
		try {
			lastImportNo = dhs.queryTableDataHttpRequest(tablename);
//			lastImportNo=dao.queryTableCount(tablename);
		} catch (Exception e) {
			logger.error("查询数据时发生异常", e);
			e.printStackTrace();
		}
		return lastImportNo;
	}

	public CheckImportResult importData(String strSql) {
		try {
			result.totalLine = (long) dhs.executeSql(strSql);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("执行数据导入语句发生异常", e);
		}
		return result;
	}
	
	
	@Override
	public CheckImportResult importData(String fileName, String tableName,int columnNum,
			long startLine) {
		result.totalLine = 0;
		long LastImportLine = startLine+1; // 临时存放开始行，为了续导
		String[] fileLineData; // 每行数据存在数组中
		StringBuffer fileValues = new StringBuffer();
		List<String> fileDateValuesList = new ArrayList<String>();
		File file = new File(fileName);
		BufferedReader reader = null;
		try {
			System.out.println("以行为单位读取文件内容，一次读一整行：");
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(file), "gbk"));
			String tempString = null;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				result.totalLine++;
				// 如果startLine=0说明是第一次导入，如果>0说明之前导过，这里续导是读到startLine行+1时再处理，没找到直接跳过多少行的方法，只能这样
				if (LastImportLine != 0) {
					if (LastImportLine == result.totalLine) {
						LastImportLine++;
						fileLineData = tempString.split(PublicDefine.columnSeparator,
								tempString.length());
						for (int i = 0; i < columnNum+1; i++) {
							fileValues.append("'" + fileLineData[i].trim()
									+ "',");
						}
						fileDateValuesList.add(fileValues.toString().substring(
								0, fileValues.toString().length() - 1));
						fileValues.setLength(0);
						// 每700条插入一次  TODO  计算多少条导一次 需要测试多少数量合适
						if (fileDateValuesList.size() / 800 == 1) {
							try {
								 dhs.addTableDataHttpRequest(tableName,
								 fileDateValuesList,dataDate);
//								dao.ExecuteSql(tableName,fileDateValuesList,docdate);
								fileDateValuesList.clear();
							} catch (Exception e) {
								logger.error("数据导入时发生异常", e);
								for (int i = 0; i < fileDateValuesList.size(); i++) {
									result.failLine++;
									CheckInformation checkInformation = new CheckInformation(
											InfoType.ERROR, "导数失败，发生异常:"+fileDateValuesList.get(i));
									this.display.showMsg(checkInformation);
								}
								e.printStackTrace();
							}
						}
					}
				} else {
					if (columnNum >0) {
						fileLineData = tempString.split(PublicDefine.columnSeparator,
								tempString.length());
					} else {
						fileLineData = new String[] { tempString };
					}
					for (int i = 0; i < columnNum+1; i++) {
						fileValues.append("'" + fileLineData[i].trim() + "',");
					}
					fileDateValuesList.add(fileValues.toString().substring(0,
							fileValues.toString().length() - 1));
					fileValues.setLength(0);
					// 每700条插入一次
					if (fileDateValuesList.size() / 800 == 1) {
						try {
							// 向后台发请求
							 dhs.addTableDataHttpRequest(tableName,
							 fileDateValuesList,dataDate);
//							dao.ExecuteSql(tableName,fileDateValuesList,docdate);
							fileDateValuesList.clear();
						} catch (Exception e) {
							logger.error("数据导入时发生异常", e);
							for (int i = 0; i < fileDateValuesList.size(); i++) {
								result.failLine++;
								CheckInformation checkInformation = new CheckInformation(
										InfoType.ERROR, "导数失败，发生异常:"+fileDateValuesList.get(i));
								this.display.showMsg(checkInformation);
							}
							e.printStackTrace();
						}
					}
				}
			}
			// 循环到最后不够700条或一开始就没700条，再插
			if (fileDateValuesList != null && fileDateValuesList.size() > 0) {
				try {
					 dhs.addTableDataHttpRequest(tableName,
					 fileDateValuesList,dataDate);
//					dao.ExecuteSql(tableName,fileDateValuesList,docdate);
					fileDateValuesList.clear();
				} catch (Exception e) {
					logger.error("数据导入时发生异常", e);
					for (int i = 0; i < fileDateValuesList.size(); i++) {
						result.failLine++;
						CheckInformation checkInformation = new CheckInformation(
								InfoType.ERROR, "导数失败，发生异常:"+fileDateValuesList.get(i));
						this.display.showMsg(checkInformation);
					}
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			logger.error("导入数据时发生异常", e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return result;
	}

	@Override
	public long checkTxtTotalLine(String fileName) {
		long sumCount = 0;
		File file = new File(fileName);
		BufferedReader reader = null;
		LineNumberReader lineNumberReader = null;
		try {
			System.out.println("以行为单位读取文件内容，一次读一整行：");
			long fileLength = file.length();
			lineNumberReader = new LineNumberReader(new FileReader(file));
			lineNumberReader.skip(fileLength);
			sumCount = lineNumberReader.getLineNumber();

		} catch (IOException e) {
			logger.error("计算文件共有多少行发生异常", e);
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

	@Override
	public void deleteTable(String tableName) {
		try {
			dhs.deleteTableDataHttpRequest(tableName);
//			dao.delTable(tableName);
		} catch (Exception e) {
			logger.error("删除数据时发生异常", e);
			e.printStackTrace();
		}
	}

	@Override
	public long queryTempTable(String tableName,String dataDate) {
		long cou=0;
		try {
			return dhs.queryTempTableDataHttpRequest(tableName,dataDate);
//			cou=dao.queryTableCountByDocdate(docdate);
		} catch (Exception e) {
			logger.error("删除数据时发生异常", e);
			e.printStackTrace();
		}
		return cou;
	}
	
	@Override
	public long queryDataStateCount(String strWhere) {
		long cou=0;
		try {
			return dhs.queryDataStateCountHttpRequest(strWhere);
		} catch (Exception e) {
			logger.error("删除数据时发生异常", e);
			e.printStackTrace();
		}
		return cou;
	}
	
	
	@Override
	public void deleteTableByDataDate(String tableName, String dataDate,String dayOrMouth) {
		try {
			dhs.deleteTableDataByDataDateHttpRequest(tableName,dataDate,dayOrMouth);
		} catch (Exception e) {
			logger.error("删除数据时发生异常", e);
			e.printStackTrace();
		}
	}
}
