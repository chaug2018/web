package com.yzj.ebs.edata.common;

/**
 *创建于:2012-9-25<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 *  对每一行内容进行校验的策略、方法
 * @author Lif
 * @version 1.0.0
 */
public interface ICheckMethod {
	/**
	 * 校验方法
	 * @param content
	 * @param fileName
	 * @return
	 */
	CheckInformation check(String content,String fileName);
}
