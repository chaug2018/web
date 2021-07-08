package com.yzj.ebs.operlog.query.biz;

import java.util.List;

import com.yzj.ebs.common.OperLogQueryParam;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.database.BasicInfoLog;

/**
 * 创建于:2013-04-01<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 日志查询模块 业务实现接口
 * 
 * @author 单伟龙
 * @version 1.0.0
 */
public interface IOperLogQueryBiz {
	/**
	 * 查詢账单日志表
	 * @throws XDocProcException 
	 */
	public List<?> queryCheckMainDataLog(OperLogQueryParam param, boolean isPaged) throws XDocProcException;

	/**
	 * 查询账户信息日志表
	 * @throws XDocProcException 
	 */
	public List<?> queryBasicInfoLog(OperLogQueryParam param, boolean isPaged) throws XDocProcException;
	
	public List<BasicInfoLog> findBasicInfoLog(BasicInfoLog basicInfoLog);
	public void updateBasicInfoLog(BasicInfoLog basicInfoLog) throws XDocProcException;
	public BasicInfoLog createBasicInfoLog(BasicInfoLog basicInfoLog)throws XDocProcException;
}
