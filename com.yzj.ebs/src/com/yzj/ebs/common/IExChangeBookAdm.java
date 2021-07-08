package com.yzj.ebs.common;

import java.util.List;
import java.util.Map;

import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.database.ExChangeBook;

/**
 * 创建于:2012-9-29<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * ExChangeBook表操作访问服务接口定义
 * 
 * @author qinjingfeng
 * @version 1.0.0
 */
public interface IExChangeBookAdm extends IBaseService<ExChangeBook>{
	
	/**
	 * 查询ExChangeBook主表的一些方法，包括统计方法
	 * 
	 * @param queryMap
	 *            查询条件Map
	 * @return 查询结果集
	 * @throws XDocProcException
	 *             执行时发生异常则抛出
	 */
	List<ExChangeBook> getExChangeBook(Map<String, String> queryMap,
			PageParam param,BankParam bankparam) throws XDocProcException;

	
	/**
	 * 查询所有符合条件的ExChangeBook记录
	 * 
	 *@param queryMap
	 *            查询条件Map
	 * @return 查询结果集
	 * @throws XDocProcException
	 */
	List<ExChangeBook> getAllExChangeBook(Map<String, String> queryMap,BankParam bankparam);
}
