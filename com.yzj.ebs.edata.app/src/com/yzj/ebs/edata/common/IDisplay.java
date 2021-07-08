package com.yzj.ebs.edata.common;

/**
 *创建于:2012-9-25<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 显示信息接口
 * @author Lif
 * @version 1.0.0
 */
public interface IDisplay {
	/**
	 * 显示信息
	 * @param checkInformation　 显示提示信息内容
	 */
	void showMsg(CheckInformation checkInformation);
}
