package com.yzj.ebs.edata.common;


/**
 *创建于:2012-9-25<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 数据导入接口
 * @author Lif
 * @version 1.0.0
 */
public interface IDataImport {
	/**
	 * 显示提示信息
	 * @param display
	 */
	void setDisplay(IDisplay display);
	
	
	/**
	 * 检查之前导入的行数
	 * 
	 * @param dataDate  数据日期
	 * @return 如果未导入，返回０，如果需则返回已经导入的行数
	 */
	long checkLastImportLine(String tablename);
	
	/**
	 * 数据导入起始行
	 * @param fileName
	 * @param tableName
	 * @param startLine
	 */
	CheckImportResult importData(String fileName,String tableName,int columnNum,long startLine);
	
	/**
	 * 导入数据导入SQL
	 * @param SQL
	 */
	CheckImportResult importData(String SQL);
	
	
	/**
	 * 查找文件的总行数
	 * @param fileName
	 * @return
	 */
	long checkTxtTotalLine(String fileName);
	
	/**
	 * 发请求删除表数据
	 * @param tableName
	 */
	void deleteTable(String tableName);
	
	/**
	 * 导入数据前先按照数据日期查主表，如果查不到说明这期是第一次导 先清空下表
	 * @param datadate
	 * @return
	 */
	long queryTempTable(String tableName,String datadate);
	
	/**
	 * 根据数据日期删除表数据
	 * @param tableName
	 * @param dataDate
	 * @param dayOrMouth
	 */
	void deleteTableByDataDate(String tableName, String dataDate,String dayOrMouth);

	/**
	 * 查询数据状态表中的数据
	 * @param tableName
	 * @param dataDate
	 * @param dayOrMouth
	 */
	long queryDataStateCount(String strWhere);
}
