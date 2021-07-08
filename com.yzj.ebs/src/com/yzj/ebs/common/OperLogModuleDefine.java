package com.yzj.ebs.common;

/**
 *创建于:2013-1-7<br>
 *版权所有(C) 2013 深圳市银之杰科技股份有限公司<br>
 * 操作日志模块名称定义
 * @author chender
 * @version 1.0
 */
public class OperLogModuleDefine {
	
	/**
	 * 账户维护
	 */
	public static final int accModify=0;
	/**
	 * 账户签约
	 */
	public static final int accSign=1;
	/**
	 * 特殊账户维护
	 */
	public static final int specialAcc=2;
	/**
	 * 退信登记
	 */
	public static final int reTreatInput=3;
	/**
	 * 退信处理
	 */
	public static final int reTreatProcess=4;
	/**
	 * 催收处理
	 */
	public static final int rushProcess=5;
	/**
	 * 自动OCR
	 */
	public static final int autoOCR=6;
	/**
	 * 自动验印
	 */
	public static final int autoYj=7;
	/**
	 * 自动记账
	 */
	public static final int autojz=8;
	/**
	 * 数据处理
	 */
	public static final int dataprocess=9;
	
	//未达上传模块（上传）
	public static final String  NOTMATCH="10";
	//对账单模块（上传）
	public static final String  DATAPAPER="11";
	//对账结果模块（下载）
	public static final String  DATARESULT="12";
	
	//网银签约查询
	public static final String IBANKTRACE = "13";
	
	
	public static final String DATAPROFLAG="数据正在处理中";
	public static final String DATAIMPSUCCESS="数据导入完成";
	public static final String DATAMOVESUCCESS="数据迁移完成";
	public static final String DATAPROSUCCESS="数据处理完成";
	public static final String ACCEPTSUCCESS="下载网银对账结果成功!";
	public static final String SENDSUCCESS="上传未达结果成功!";
	
	public static String getModuleName(int key){
		return RefTableTools.ValRefOperLogModuleMap.get(key+"");
	}

	
}
