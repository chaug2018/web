package com.yzj.ebs.common;

import java.util.List;
import java.util.Map;

import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.database.AccNoMainData;

/**
 * 创建于:2012-10-10<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 账号明细主表dao操作接口
 * 
 * @author 陈林江，李飞
 * @version 1.0
 */
public interface IAccnoMainDataAdm extends IBaseService<AccNoMainData> {

	//---------------------------------二期报表开发begin-----------------------------------
	/**
	 * 根据对账日期查询机构对账有效率
	 * 
	 * @param queryMap,queryParam,docDate,isPaged,selectCount
	 * @return list
	 * @throws XDocProcException
	 */
	List<?> getEbillMatchReportResult(Map<String, String> queryMap,PageParam queryParam, 
			String docDate,boolean isPaged,String selectCount) throws XDocProcException;
	
	/**
	 * 根据对账日期，查询账户对账有效明细
	 * 
	 * @param queryMap 查询条件Map
	 * @return
	 * @throws XDocProcException
	 */
	List<?> getEbillMatchDetail(Map<String, String> queryMap, PageParam param,
			String docDate,boolean isPaged) throws XDocProcException;
	
	/**
	 * 账户有效对账结果展示
	 * 
	 * @param queryMap 查询条件Map
	 * @return
	 * @throws XDocProcException
	 */
	List<?> getEbillMatchResult(Map<String, String> queryMap, PageParam param,
			boolean isPaged) throws XDocProcException;
	
	/**
	 * 覆盖率统计结果展示
	 * 
	 * @param queryMap 查询条件Map
	 * @return
	 * @throws XDocProcException
	 */
	List<?> getCoverReportList(Map<String, String> queryMap, PageParam param,
			boolean isPaged,String selectCount,String isThree) throws XDocProcException;
	
	/**
	 * 获取连续未对账 次数
	 * @param selectCount
	 * @param idBank
	 * @return
	 * @throws XDocProcException
	 */
	List<?> getNotCheckCount(String selectCount,String idBank) throws XDocProcException;
	
	/**
	 * 连续对账未成功账户明细
	 * 
	 * @param queryMap 查询条件Map
	 * @return
	 * @throws XDocProcException
	 */
	List<?> getCoverFailReportList(Map<String, String> queryMap, PageParam param,
			boolean isPaged) throws XDocProcException;
	
	/**
	 * 活跃账户情况统计
	 * 
	 * @param queryMap 查询条件Map
	 * @return
	 * @throws XDocProcException
	 */
	List<?> getActiveAccReportList(Map<String, String> queryMap, PageParam param,
			boolean isPaged,String tableName,String workDate) throws XDocProcException;
	
	//---------------------------------二期报表开发end-----------------------------------
	
	/**
	 * 根据accno查询basicinfo表的sealmode
	 * @param accno
	 * @return
	 * @throws XDocProcException
	 */
	String getSealmodeByAccNo(String accno) throws XDocProcException;
	
	/**
	 * 根据账单编号获取账号明细
	 * 
	 * @param voucherno
	 *            账单编号
	 * @return
	 * @throws XDocProcException
	 */
	List<AccNoMainData> getAccnoMainDataByVoucherNo(String voucherno)
			throws XDocProcException;

	/**
	 * 批量更新账号明细
	 * 
	 * @param list
	 *            需要更新的对象集合
	 * @return
	 * @throws XDocProcException
	 */
	List<AccNoMainData> batchUpdate(List<AccNoMainData> list)
			throws XDocProcException;

	/**
	 * 根据对账日期查找账户信息
	 * 
	 * @param queryMap
	 *            查询条件Map
	 * @return
	 * @throws XDocProcException
	 */
	List<AccNoMainData> getAccnoMainDataByDocDate(Map<String, String> queryMap,
			PageParam param) throws XDocProcException;

	/*
	 * 根据对账日期查询机构对账有效率
	 * 
	 * @param queryMap
	 * 
	 * @return
	 * 
	 * @throws XDocProcException
	 */
	List<?> getEbillMatchAnalyseResult(Map<String, String> queryMap,
			PageParam queryParam, String docDate) throws XDocProcException;
	
	/*
	 * 根据对账日期查询机构对账有效率
	 * 
	 * @param queryMap
	 * 
	 * @return
	 * 
	 * @throws XDocProcException
	 */
	List<?> getEbillMatchAnalyseResultCount(Map<String, String> queryMap,
			PageParam queryParam, String docDate,String selectCount) throws XDocProcException;
	
	/*
	 * 获得所有上级机构号的名字
	 * 
	 * @param queryMap
	 * 
	 * @return
	 * 
	 * @throws XDocProcException
	 */
	List<?> getAllIdBranchName() throws XDocProcException;

	/*
	 * 根据对账日期查询机构对账有效率
	 * 
	 * @param queryMap
	 * 
	 * @return
	 * 
	 * @throws XDocProcException
	 */
	List<?> getAllEbillMatchAnalyseResult(Map<String, String> queryMap,
			String docDate) throws XDocProcException;
	
	/*
	 * 根据对账日期查询机构对账有效率
	 * 
	 * @param queryMap
	 * 
	 * @return
	 * 
	 * @throws XDocProcException
	 */
	List<?> getAllEbillMatchAnalyseResultCount(Map<String, String> queryMap,
			String docDate,String selectCount ) throws XDocProcException;

	/**
	 * 根据对账日期，查询账户对账有效明细
	 * 
	 * @param queryMap
	 *            查询条件Map
	 * @return
	 * @throws XDocProcException
	 */
	List<?> getEbillMatchParam(Map<String, String> queryMap, PageParam param,
			String docDate) throws XDocProcException;

	List<?> getAllEbillMatchParam(Map<String, String> queryMap, String docDate)
			throws XDocProcException;

	/**
	 * 根据账号查找对应的AccNoMainData实体
	 * @param accNo
	 * @return 
	 */
	AccNoMainData getAccnoMainDataByAccno(String accNo)throws XDocProcException;
}
