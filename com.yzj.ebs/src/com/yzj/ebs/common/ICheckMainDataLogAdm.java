package com.yzj.ebs.common;

import java.util.List;

import com.yzj.ebs.database.CheckMainDataLog;


/**
 * 创建于:2013-1-12<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * CheckMainDataLog 表操作访问服务接口定义
 * 
 * @author 陈林江
 * @version 1.0.0
 */
public interface ICheckMainDataLogAdm extends IBaseService<CheckMainDataLog>{

	/**
	 * 创建CheckMainData操作日志
	 * @param checkMainDataLog 日志对象
	 * @return
	 * @throws XDocProcException
	 */
	CheckMainDataLog createCheckMainDataLog(CheckMainDataLog checkMainDataLog)throws XDocProcException;
	
	/**
	 * 查询checkMainData操作日志
	 * @param  param 查询参数
	 * @param isPaged 是否分页
	 * @return  checkMainData 日志对象
	 * @throws XDocProcException
	 */
	List<CheckMainDataLog> queryCheckMainDataLog(OperLogQueryParam param,boolean isPaged)throws XDocProcException;
}
