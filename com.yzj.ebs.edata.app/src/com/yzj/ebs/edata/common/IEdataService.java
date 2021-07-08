package com.yzj.ebs.edata.common;

/**
 *创建于:2012-9-25<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 数据校验、导入Service层主接口
 * @author Lif
 * @version 1.0.0
 */
public interface IEdataService {
	
	/**
	 * 设置面板
	 * @param display
	 */
	void setDisplay(IDisplay display);
	
	/**
	 * 数据校验
	 * @return
	 */
	boolean checkEdata();
	
	/**
	 * 数据导入
	 * @return
	 */
	CheckImportResult importEdata();
	
	/**
	 * 数据处理
	 * @return
	 */
	boolean dataDispose();
	
}
