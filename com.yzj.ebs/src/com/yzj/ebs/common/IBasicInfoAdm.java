package com.yzj.ebs.common;

import java.util.List;
import java.util.Map;

import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.database.BasicInfo;


/**
 * 创建于:2012-9-29<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * Basicinf表操作访问服务接口定义
 * 
 * @author 秦靖锋
 * @version 1.0.0
 */
public interface IBasicInfoAdm extends IBaseService<BasicInfo>{

	//---------------------------------二期报表开发begin-----------------------------------
	/**
	 * 对账集中情况统计
	 */
	@SuppressWarnings("rawtypes")
	List getFocusReportList(Map<String, String> queryMap,
			PageParam pageParam, boolean isPaged, String selectCount) throws XDocProcException;
	
	/**
	 * 网银对账签约率统计
	 */
	@SuppressWarnings("rawtypes")
	List getNetSignReportList(Map<String, String> queryMap,
			PageParam pageParam, boolean isPaged, String selectCount) throws XDocProcException;
	
	/**
	 * 网银对账率统计
	 */
	@SuppressWarnings("rawtypes")
	List getNetCheckReportList(Map<String, String> queryMap,
			PageParam pageParam, boolean isPaged, String selectCount) throws XDocProcException;
	//---------------------------------二期报表开发end-----------------------------------
	
	/**
	 * 分页查询Basicinfo表的一些方法，包括统计方法
	 * 
	 * @param queryMap
	 *            查询条件Map
	 * @param param
	 * 			  分页查询相关条件
	 * @return 查询结果集
	 * @throws XDocProcException
	 *             执行时发生异常则抛出
	 */
	List<BasicInfo> getBasicinfoData(Map<String,String> queryMap,PageParam param) throws XDocProcException;

	/**
	 * 查询Basicinfo表
	 * 
	 * @param queryMap
	 *            查询条件Map
	 * @return 查询结果集
	 * @throws XDocProcException
	 *             执行时发生异常则抛出
	 */
	List<BasicInfo> getAllBasicInfo(Map<String, String> queryMap) throws XDocProcException;
	
	/**
	 * 查询Basicinfo与acctList表
	 * 
	 * @param queryMap
	 *            查询条件Map
	 * @return 查询结果集
	 * @throws XDocProcException
	 *             执行时发生异常则抛出
	 */
	List<?> getAcctListData(Map<String,String> queryMap,PageParam param) throws XDocProcException;
	
	/**
	 * 查询Basicinfo与acctList表
	 * 
	 * @param queryMap
	 *            查询条件Map
	 * @return 查询结果集
	 * @throws XDocProcException
	 *             执行时发生异常则抛出
	 */
	List<?> getAllAcctList(Map<String, String> queryMap) throws XDocProcException;

	/**
	 * 根据账号删除一条记录
	 * @param accNo
	 * @return
	 * @throws XDocProcException
	 */
	int deleteBasicInfoByAccNo(String accNo) throws XDocProcException;
	
	/**
	 * 根据账号获取一条BasicInfo记录
	 * @param accNo
	 * @return
	 * @throws XDocProcException
	 */
	BasicInfo getOneByAccNo(String accNo) throws XDocProcException;
	
	
	/**
	 * 根据对账日期和账号获取  网银对账信息
	 * @param accNo 关联checkMainData表
	 * 
	 * @return
	 * @throws XDocProcException
	 */
	 List<?> getAllData(Map<String, String> queryMap,final String entityName,PageParam param) throws XDocProcException;
	 
	 /**
		 * 根据对账日期和账号获取  网银对账信息
		 * @param accNo 关联checkMainData表
		 * 
		 * @return
		 * @throws XDocProcException
		 */
	 List<?> getOneData(Map<String, String> queryMap,final String entityName) throws XDocProcException;
	 
	 /**
		 * 统计黑白名单
		 * 
		 * @return
		 * @throws XDocProcException
		 */
	 public List<?> analyseBlackWhite(Map<String, String> queryMap,PageParam pageParam,
			 boolean isPaged) throws XDocProcException;
	 
	 /**
		 * 统计黑白名单(按单位统计)
		 * 
		 * @return
		 * @throws XDocProcException
		 */
	 public List<?> analyseBlackWhiteCount(Map<String, String> queryMap,PageParam pageParam,
			 boolean isPaged,String selectCount) throws XDocProcException;
	 
	 /**
		 * 获得所有对账中心的名字
		 * 
		 * @return
		 * @throws XDocProcException
		 */
	 public List<?> getAllIdBranchName() throws XDocProcException;
	 
	 /**
		 * 导出黑白名单
		 * 
		 * @return
		 * @throws XDocProcException
		 */
	 public List<?> exportanalyseBlackWhite(Map<String, String> queryMap,PageParam pageParam) throws XDocProcException;
	 /**
		 * 把账户设置为正常账户
		 * 
		 * @return
		 * @throws XDocProcException
		 */
	 public void changeToNormal(String accNo)throws XDocProcException;
	 
	 /**
	  *   单个 账户跟新
	  */
	 public void updateBasicInfo(BasicInfo basicInfo)throws XDocProcException;
	 /**
	  * 面对面 柜台发送地址维护
	  */
//	 public void updateAddress(Map<String,String>updateMap,String sendType,String sendAddress)throws XDocProcException;
}
