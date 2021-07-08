/**
 * 创建于:2012-09-24<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 参数定义
 * 
 * @author lif
 * @version 1.0
 */
package com.yzj.ebs.edata.common;

public class PublicDefine {
	//http://localhost:8080/windforce/EDataAction_edataProcess.action
	public static String dataServerAdress = "http://localhost:8080/windforce/EDataAction_edataProcess.action"; // 数据处理服务地址
	public static String columnSeparator=""; //列之间的分隔符
	//public static String columnSeparator="\\|";
	public static int maindataSeparatorlen=20; //主数据分隔符个数
	public static int dephistSeparatorlen=10; //明细分隔符个数
	public static int utblbrcdSeparatorlen=4;//机构分隔符个数
	public static int userSeparatorlen=2; //人员信息分隔符个数
	public static int exrtSeparatorlen=4; //汇率分隔符个数
	public static int atchusSeparatorlen=2; //客户经理分隔符个数
	public static String szmonth="01";//1-12月份
	
	/**
	 * 自动导数
	 */
	public static int okSeparatorlen=1; //导数状态分隔符个数
	
}
