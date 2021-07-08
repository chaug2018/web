package com.yzj.ebs.common;

import java.util.List;
import java.util.Map;

import com.yzj.ebs.database.AccNoDetailData;
import com.yzj.ebs.database.temp.hbm.EbsMarginData;
import com.yzj.ebs.common.param.PageParam;

public interface IAccnoDetailAdm {
	/**
	 * 分页查询AccnoDetailData表的一些方法，包括统计方法
	 * 
	 * @param queryMap
	 *            查询条件Map
	 * @param param
	 *            分页查询相关条件
	 * @return 查询结果集
	 * @throws XDocProcException
	 *             执行时发生异常则抛出
	 */
	List<AccNoDetailData> getAccnoDetailData(Map<String, String> queryMap,
			PageParam param, String entityName) throws XDocProcException;

	/**
	 * 查询AccnoDetailData表的一些方法，包括统计方法
	 * 
	 * @param queryMap
	 *            查询条件Map
	 * @param param
	 *            分页查询相关条件
	 * @return 查询结果集
	 * @throws XDocProcException
	 *             执行时发生异常则抛出
	 */
	List<AccNoDetailData> getAllAccnoDetailData(Map<String, String> queryMap,
			String entityName) throws XDocProcException;

	/**
	 * 分页查询AccnoDetailData表的一些方法，包括统计方法
	 * 
	 * @param queryMap
	 *            查询条件Map
	 * @param param
	 *            分页查询相关条件
	 * @return 查询结果集
	 * @throws XDocProcException
	 *             执行时发生异常则抛出
	 */
	List<Object[]> getAccnoDetailDataByDocDate(
			Map<String, String> queryMap, PageParam param, String entityName,
			String docDate, boolean isPaged) throws XDocProcException;

	/**
	 * 根据 账单编号查询发生额明细数据
	 * 
	 * @param voucherno
	 *            账单编号
	 * @return 查询结果集
	 * @throws XDocProcException
	 *             执行时发生异常则抛出
	 */
	List<AccNoDetailData> getAccnoDetailDataByVoucherno(String voucherno, String docdate) throws XDocProcException;
	
	/**
	 * 根据 账单编号查询保证金及定期明细
	 * 
	 * @param voucherno
	 *            账单编号
	 * @return 查询结果集
	 * @throws XDocProcException
	 *             执行时发生异常则抛出
	 */
	List<EbsMarginData> getMarginDataByVoucherno(String voucherno)throws XDocProcException;

	
	/**
	 * 根据查询条件查询发生额明细数据
	 * 
	 * @param where
	 *            查询条件
	 *  @param docdate
	 *            对账日期 
	 * @return 查询结果集
	 * @throws XDocProcException
	 *             执行时发生异常则抛出
	 */
	List<AccNoDetailData> getAccnoDetailDataByWhere(String where, String docdate)
			throws XDocProcException;
	
}
