package com.yzj.ebs.common;

import java.util.List;

import com.yzj.ebs.database.DocLog;

/**
 * 创建于:2012-9-29<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * DocSet表操作访问服务接口定义
 * 
 * @author 秦靖锋
 * @version 1.0.0
 */
public interface IDocLogAdm extends IBaseService<DocLog>{

	/**
	 * 查询指定票据的相关日志信息
	 * 
	 * @param docid
	 *            票据流水号
	 * @return 所有与指定票据的相关日志信息,按增加时的顺序排列
	 * @throws XDocProcException
	 *             执行时发生异常则抛出
	 */
	List<DocLog> queryListByDocID(long docID) throws XDocProcException;

}
