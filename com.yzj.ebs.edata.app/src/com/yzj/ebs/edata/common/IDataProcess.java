package com.yzj.ebs.edata.common;


/**
 *创建于:2012-9-28<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 数据处理接口
 * @author Lif
 * @version 1.0.0
 */
public interface IDataProcess {
	
	/**
	 * 显示提示信息
	 * @param display
	 */
	void setDisplay(IDisplay display);
	/**
	 * 数据处理
	 * @param docdate 对账日期
	 * @param szmonth 1-12月份
	 * @return
	 */
	boolean dataDispose(String docdate,String szmonth);
	
}
