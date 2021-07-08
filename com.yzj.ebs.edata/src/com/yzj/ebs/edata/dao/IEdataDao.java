/**
 *创建于:2012-10-10<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 数据导入，处理dao接口
 * @author Lif
 * @version 1.0.0
 */
package com.yzj.ebs.edata.dao;

import java.util.List;
import java.util.Map;

import com.infotech.publiclib.Exception.DaoException;
import com.yzj.ebs.edata.bean.BaseParamBean;
import com.yzj.ebs.edata.bean.BillBean;

public interface IEdataDao {

	/************************ 数据导入 *******************************/
	/**
	 * 批量插入数据
	 * 
	 * @param tableName
	 * @param dataValues
	 * @param docdate
	 * @param flag
	 *            自动还是手动导数标识 auto 自动；hand 手动导
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void executeSql(String tableName, List dataValues, String docdate)
			throws Exception;

	/**
	 * 查找表总数
	 * 
	 * @param tableName
	 * @return
	 * @throws DaoException
	 */
	public long queryTableCount(String tableName) throws DaoException;


	/************************ 数据处理 *******************************/
	/**
	 * 通用sql
	 * 
	 * @param sql
	 * @throws Exception
	 */
	public void insertOrUpdateDate(String sql) throws DaoException;

	/**
	 * 查找临时账单信息
	 * 
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public List<BillBean> queryBillTable(String sql) throws DaoException;

	/**
	 * 查询对账中心参数表
	 * 
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public List<BaseParamBean> findParamTable(String sql) throws DaoException;
	
	/**
	 * 数据导入时先把机构信息处理好，供插入到wf框架中
	 * 
	 * @throws Exception
	 */
	public void processParamBankData() throws DaoException;

	/**
	 * 关闭数据处理的连接
	 */
	public void closeConnProcess();
	
	/**
	 * 查找临时用户表
	 * 
	 * @throws DaoException
	 */
	public List<BillBean> queryKubUser() throws DaoException;
	
	/**
	 * 查找科目号
	 * 
	 * @param sql
	 * @throws DaoException
	 */
	public List<BillBean> querySubNoc(String sql) throws DaoException;
	
	/**
	 * lixinagjun
	 * 数据库查询 obj为需要查询的字段，sql为sql语句，返回值为List<Map>  一个map存放的为一条数据
	 * @param obj
	 * @param sql
	 * @return List<Map<String, Object>> 
	 */
	public List<Map<String, Object>> queryObjectList(String[] resultPar, String sql);
	
	/**
	 *  lixiangjun
	 * 查询数据总条目数 
	 * @param sql
	 * @return
	 */
	public long queryDataCount(String sql);
	
	
	/**
	 * lixiangjun
	 * 查询 PARAM_SYSBASE 表中的参数参数值
	 * @param parma
	 * @return
	 */
	public String findSysbaseParam(String parma);
	
	
}
