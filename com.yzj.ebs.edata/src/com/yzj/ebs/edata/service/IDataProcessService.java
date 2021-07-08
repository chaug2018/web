package com.yzj.ebs.edata.service;

import java.util.List;

import com.infotech.publiclib.Exception.DaoException;
import com.yzj.ebs.edata.bean.BillBean;

/**
 * 创建于:2012-10-10<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 数据导入、处理service接口
 * 
 * @author Lif
 * @version 1.0.0
 */
public interface IDataProcessService {

	/**
	 * 批量插入数据
	 * 
	 * @param tableName
	 * @param dataValues
	 * @param dataDate
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void batchInsertDate(String tableName, List dataValues,
			String dataDate) throws Exception;
	
	/**
	 * 查找表总数
	 * 
	 * @param tableName
	 * @return
	 * @throws DaoException
	 */
	public long queryTableCount(String tableName) throws DaoException;
	
	/**
	 * 数据导入时先把机构信息处理好，供插入到wf框架中
	 * 
	 * @throws Exception
	 */
	public boolean processParamBankData() throws Exception;

	/**
	 * 同用执行的sql
	 * @param sql
	 * @throws DaoException
	 */
	public void insertOrUpdateDate(String sql) throws DaoException;

	/**
	 * 查询临时账单信息
	 * @return
	 * @throws DaoException
	 */
	public List<BillBean> queryUsers() throws DaoException;

	
	/**
	 * 查询ParamSysbase表中的数据
	 * @param parma
	 * @return
	 */
	public String findSysbaseParam(String parma);
	
}
