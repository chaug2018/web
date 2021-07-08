package com.yzj.ebs.common;

import java.util.List;

import com.yzj.ebs.database.BasicInfoLog;


/**
 * 创建于:2012-10-25<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * BasicInfoLog 表操作访问服务接口定义
 * 
 * @author 陈林江
 * @version 1.0.0
 */
public interface IBasicInfoLogAdm extends IBaseService<BasicInfoLog>  {

	/**
	 * 创建basicInfo操作日志
	 * @param basicInfoLog 日志对象
	 * @return
	 * @throws XDocProcException
	 */
	BasicInfoLog createBasicInfoLog(BasicInfoLog basicInfoLog)throws XDocProcException;
	
	/**
	 * 查询basicInfo操作日志
	 * @param  param 查询参数
	 * @param  isPaged 是否分页
	 * @return  basicInfoLog 日志对象
	 * @throws XDocProcException
	 */
	List<BasicInfoLog> queryBasicInfoLog(OperLogQueryParam param,boolean isPaged)throws XDocProcException;
	
	/**
	 * 更新
	 * @param basicInfoLog
	 * @throws XDocProcException
	 */
	void updateBasicInfoLog(BasicInfoLog basicInfoLog) throws XDocProcException;
	
	/**
	 * 查找
	 * @param basicInfoLog
	 * @return
	 */
	List<BasicInfoLog> findBasicInfoLog(BasicInfoLog basicInfoLog);
	
}
