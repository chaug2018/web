package com.yzj.ebs.common;

import java.util.List;
import java.util.Map;

import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.database.AccNoMainData;
import com.yzj.ebs.database.CheckMainData;
import com.yzj.ebs.database.DocSet;

/**
 * 创建于:2012-9-29<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * CheckMainData表操作访问服务接口定义
 * 
 * @author qinjingfeng,ShiJiangmin
 * @version 1.0.0
 */
public interface ICheckMainDataAdm extends IBaseService<CheckMainData> {

	//报表二期开发begin
	 
	/*
	@SuppressWarnings("rawtypes")
	List getProveReportList(Map<String, String> queryMap, PageParam pageParam,boolean isPaged)
			throws XDocProcException;
	*/
	
	/**
	 * 按统计单位查询验印统计结果
	 * 
	 * @param queryMap
	 * @param pageParam
	 * @return 统计结果集
	 * @throws XDocProcException
	 */
	@SuppressWarnings("rawtypes")
	List getProveReportListCount(Map<String, String> queryMap, PageParam pageParam,boolean isPaged,String selectCount)
			throws XDocProcException;
	
	/**
	 * 按统计单位查询验印统计结果
	 * 
	 * @param queryMap
	 * @param pageParam
	 * @return 统计结果集
	 * @throws XDocProcException
	 */
	@SuppressWarnings("rawtypes")
	List getProveAccDetailReportList(Map<String, String> queryMap, PageParam pageParam,boolean isPaged)
			throws XDocProcException;
	
	
	/**
	 * 按单位进行机构对账率统计
	 * 
	 * @param queryMap
	 * @param pageParam
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	List getEbillReportList(Map<String, String> queryMap, PageParam pageParam,boolean isPaged,String selectCount)
			throws XDocProcException;
	
	//报表二期开发end
	
	
	/**
	 * 查询checkmaindata主表的一些方法，包括统计方法
	 * 
	 * @param queryMap
	 *            查询条件Map
	 * @return 查询结果集
	 * @throws XDocProcException
	 *             执行时发生异常则抛出
	 */
	List<CheckMainData> getCheckMainData(Map<String, String> queryMap,
			PageParam param) throws XDocProcException;
	/**
	 * 查询checkMainData表和accNoMainData
	 * 
	 * @param queryMap
	 *            查询条件Map
	 * @return 查询结果集
	 * @throws XDocProcException
	 *             执行时发生异常则抛出
	 */
	List<AccNoMainData> getCheckAccMainData(Map<String, String> queryMap,
			PageParam param,String accNo) throws XDocProcException;
	
	 List<AccNoMainData> getAllAccCheckMainData(Map<String, String> queryMap,String accNo) throws XDocProcException;

	/**
	 * 自动记账时，根据docset对象更新checkmaindata记录
	 * 
	 * @param docSet
	 *            docset对账
	 * @return 查询结果集
	 * @throws XDocProcException
	 *             执行时发生异常则抛出
	 */
	boolean upDateCheckmaindata(DocSet docSet) throws XDocProcException;

	/**
	 * 根据对账单编号查询CheckMainData
	 * 
	 * @param voucherno
	 * @return
	 * @throws XDocProcException
	 */
	CheckMainData getOneByVoucherNo(String voucherno) throws XDocProcException;

	/**
	 * 更新一条CheckMainData记录
	 * 
	 * @param checkMainData
	 *            对象
	 * @return
	 * @throws XDocProcException
	 */
	CheckMainData updateCheckMainData(CheckMainData checkMainData)
			throws XDocProcException;

	/**
	 * 查询所有符合条件的CheckMainData记录
	 * 
	 * @param checkMainData
	 *            对象
	 * @return
	 * @throws XDocProcException
	 */
	List<CheckMainData> getAllCheckMainData(Map<String, String> queryMap) throws XDocProcException;

	/**
	 * 退信情况统计
	 * 
	 * @param docDate
	 * @param pageParam
	 * 
	 * @return
	 * @throws XDocProcException
	 */
	public List<?> getUrgeStatisticsResult(Map<String, String> queryMap, PageParam pageParam,
			boolean isPaged) throws XDocProcException;
	
	/**
	 * 退信情况统计(按单位统计)
	 * 
	 * @param docDate
	 * @param pageParam
	 * 
	 * @return
	 * @throws XDocProcException
	 */
	public List<?> getUrgeStatisticsResultCount(Map<String, String> queryMap, PageParam pageParam,
			boolean isPaged,String selectCount) throws XDocProcException;
	
	/**
	 * 根据对账日期查询退信情况,并做分页处理
	 * 
	 * @param docDate
	 * @param pageParam
	 * 
	 * @return
	 * @throws XDocProcException
	 */
	public List<?> getAllUrgeStatisticsResult(Map<String, String> queryMap)
			throws XDocProcException;
	/**
	 * 催收情况统计
	 * 
	 * @param docDate
	 * @param pageParam
	 * 
	 * @return
	 * @throws XDocProcException
	 */
	public List<?> getRushStatisticsResult(Map<String, String> queryMap, PageParam pageParam
			,boolean isPaged) throws XDocProcException;
	
	/**
	 * 催收情况统计(按单位统计)
	 * 
	 * @param docDate
	 * @param pageParam
	 * 
	 * @return
	 * @throws XDocProcException
	 */
	public List<?> getRushStatisticsResultCount(Map<String, String> queryMap, PageParam pageParam
			,boolean isPaged,String selectCount) throws XDocProcException;
	
	/**
	 * 根据对账日期查询催收情况,并做分页处理
	 * 
	 * @param docDate
	 * @param pageParam
	 * 
	 * @return
	 * @throws XDocProcException
	 */
	public List<?> getAllRushStatisticsResult(Map<String, String> queryMap)
			throws XDocProcException;
	
	/**
	 * 根据对账日期查询对账中心业务量,并做分页处理
	 * 
	 * @param docDate
	 * @param pageParam
	 * 
	 * @return
	 * @throws XDocProcException
	 */
	public List<?> getBusinessStatisticsResult(Map<String, String> queryMap, PageParam pageParam,String docDate,boolean isPaged)
			throws XDocProcException;
	
	/**
	 * 根据对账日期查询对账中心业务量,并做分页处理
	 * 
	 * @param docDate
	 * @param pageParam
	 * 
	 * @return
	 * @throws XDocProcException
	 */
	public List<?> getBusinessStatisticsResultCount(Map<String, String> queryMap, PageParam pageParam,
			String docDate,boolean isPaged,String selectCount)
			throws XDocProcException;
	
	/**
	 * 根据对账日期查询对账中心业务量,并做分页处理
	 * 
	 * @param docDate
	 * @param pageParam
	 * 
	 * @return
	 * @throws XDocProcException
	 */
	public List<?> getAllBusinessStatisticsResult(Map<String, String> queryMap,String docDate)
			throws XDocProcException;
	/**
	 * 按账号获取对账单发出数
	 * 
	 * @return
	 */
	long getAccSendCount(Map<String, String> queryMap) throws XDocProcException;

	/**
	 * 按对账单获取对账单发出数
	 * 
	 * @return
	 */
	long getBillSendCount(Map<String, String> queryMap)
			throws XDocProcException;

	/**
	 * 查询验印统计结果
	 * 
	 * @param queryMap
	 * @param pageParam
	 * @return 统计结果集
	 * @throws XDocProcException
	 */
	@SuppressWarnings("rawtypes")
	List getProveAnalyseList(Map<String, String> queryMap, PageParam pageParam,boolean isPaged)
			throws XDocProcException;
	
	/**
	 * 按统计单位查询验印统计结果
	 * 
	 * @param queryMap
	 * @param pageParam
	 * @return 统计结果集
	 * @throws XDocProcException
	 */
	@SuppressWarnings("rawtypes")
	List getProveAnalyseListCount(Map<String, String> queryMap, PageParam pageParam,boolean isPaged,String selectCount)
			throws XDocProcException;
	
	/**
	 * 获得所有上级机构的名字
	 * 
	 * @param queryMap
	 * @param pageParam
	 * @return 统计结果集
	 * @throws XDocProcException
	 */
	@SuppressWarnings("rawtypes")
	List getAllIdBranchName() throws XDocProcException;

	/**
	 * 机构对账率统计
	 * 
	 * @param queryMap
	 * @param pageParam
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	List getEbillAnalyseList(Map<String, String> queryMap, PageParam pageParam,boolean isPaged,String selectCount)
			throws XDocProcException;
	
	/**
	 * 按单位进行机构对账率统计
	 * 
	 * @param queryMap
	 * @param pageParam
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	List getEbillAnalyseListCount(Map<String, String> queryMap, PageParam pageParam,boolean isPaged,String selectCount)
			throws XDocProcException;


	/**
	 * 对账率统计
	 * 
	 * @param queryMap
	 * @param pageParam
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	List getAllEbillAnalyseList(Map<String, String> queryMap,String orgType,String accNo,String custManager)
			throws XDocProcException;
	/**
	 * 增量对账情况统计
	 * 
	 * 
	 */
	@SuppressWarnings("rawtypes")
	List getPartEbillAnalyseList(Map<String, String> queryMap,String docDateStart,String docDateEnd,boolean isPaged)
			throws XDocProcException;
	
	/**
	 * 
	 * 根据账单编号查询BasicInfo,供自动验印使用
	 * 
	 * @param voucherno
	 *            voucherno账单编号
	 * @return 查询结果
	 * @throws XDocProcException
	 *             自定义异常
	 */
	public List<Object[]> getSealAccno(String voucherno) throws XDocProcException;
	
	/**
	 * 打印账单后更改账单状态为等待回收 docstate=2
	 * @param voucherno
	 * @param printTimes
	 * @throws XDocProcException
	 */
	public void updateCheckmaindataByVoucherno(String voucherno,int printTimes) throws XDocProcException;
	/**
	 * 根据对账单编号查询checkmaindata表
	 * @param voucherNo
	 * @return
	 * @throws XDocProcException
	 */
	public CheckMainData queryOneByVoucherNo(String voucherNo) throws XDocProcException;
	
	/**
	 * 根据页面查询条件，查询checkmaindata 和accnomaindata
	 * 
	 */
	
	public List<CheckMainData> getBillinfoQueryData(
			Map<String, String> queryMap, final String queryType,PageParam pageParam)
			throws XDocProcException;
	
	/**
	 * 根据页面查询条件，查询checkmaindata 和accnomaindata重载 
	 * 
	 */
	public List<Object[]> getCheckMaindata(Map<String, String> queryMap,
			PageParam pageParam, Map<String, String> accQueryMap)
			throws XDocProcException;
	/**
	 * 在账单导出模块 ，根据页面查询的条件，导出对应的对账单 sunjian
	 * @param queryMap
	 * @return
	 * @throws XDocProcException
	 */
	public List<CheckMainData> getExportCheckMainData(Map<String, String> queryMap)throws XDocProcException;
	/**
	 * 获得当月活期的明细
	 */
	public List<Object[]> getDeatil(Map<String,String> mapData,String month,boolean isPaged,int firstNum,int pageNum)throws XDocProcException;
	
	/**
	 * 得到 所有需要打印对账单的 对账单编号
	 */
	public List<Object[]> getAllVoucherNo(Map<String,String> queryMap)throws XDocProcException;
}
