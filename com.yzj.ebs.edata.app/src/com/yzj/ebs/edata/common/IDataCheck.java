package com.yzj.ebs.edata.common;

/**
 *创建于:2012-9-25<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 数据校验接口
 * @author Lif
 * @version 1.0.0
 */
public interface IDataCheck {
	/**
	 * 设置面板
	 * @param display
	 */
	void setDisplay(IDisplay display);
	
	/**
	 * 校验规则
	 * @param method　 校验策略
	 */
	void addCheckMethod(ICheckMethod method);
	
	/**
	 * 校验文件目录是否存在
	 */
	boolean checkFilePath();
	
	/**
	 * 校验文件内容
	 * @param fileName 文件
	 * @return 成功返回行数
	 */
	CheckImportResult checkFileContent(String fileName);
	
}
