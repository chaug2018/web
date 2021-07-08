package com.yzj.ebs.common;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.database.InsideAccnoParam;

/**
 * 创建于:2013-8-16<br>
 * 版权所有(C) 2013 深圳市银之杰科技股份有限公司<br>
 * InsideAccnoParam表操作访问服务接口定义
 * 
 * @author j_sun
 * @version 1.0.0
 */

public interface IInsideAccnoParam {
	
	/**
	 * 查询InsideAccnoParam
	 * 
	 * @param queryMap
	 *            查询条件Map
	 * @return 查询结果集
	 * @throws XDocProcException
	 *             执行时发生异常则抛出
	 */
	public List<InsideAccnoParam> getInfor(Map<String, String> queryMap,
			PageParam param)throws XDocProcException;
	/**
	 * 修改InsideAccnoParam
	 * 如果 新的 账号不存在 返回0 ， 存在返回1  
	 */
	public int changeInfor(String accNo1, String custId, String accNo2)throws SQLException;
	
	/**
	 * 增加InsideAccnoParam
	 * 
	 * @param queryMap
	 *            查询条件Map
	 * @return 查询结果集
	 * @throws XDocProcException
	 *             执行时发生异常则抛出
	 */
	public void putInfor(InsideAccnoParam inside
			)throws XDocProcException;
	
	/**
	 * 删除InsideAccnoParam
	 * 
	 * @param queryMap
	 *            查询条件Map
	 * @return 查询结果集
	 * @throws XDocProcException
	 *             执行时发生异常则抛出
	 */
	public void deleteInfor(String accNo, String custId,String recheckCustId
			)throws SQLException;
	
	/**
	 * 根据 输入的内部账号 查询 该账号是否存在于ebs_inneraccnomaindata 中
	 * 不存在返回 0 , 存在 返回 大于0的数
	 */
	public int checkInnerAccNo(String accNo)throws XDocProcException;
	
	/**
	 * 根据 输入的custId 查询 该custId是否存在于po_peopleinfo 中(peoplestate=0)
	 *  不存在返回 0 , 存在 返回 大于0的数
	 */
	public int checkCustId(String custId)throws XDocProcException;
	
	
	/**
	 * 检测custId和recheckCustId是否同一部门
	 * @param custId
	 * @param recheckCustId
	 * @return 是返回1，不是返回0
	 * @throws XDocProcException
	 */
	public int checkCustIdIDBank(String custId,String recheckCustId) throws XDocProcException;
	
	/**
	 * 内部账单 核对 信息查询
	 * 
	 */
	public List<Map<String,Object>> queryInnerCheckResult(Map<String,String> queryMap,PageParam param) throws SQLException;
	/**
	 * 内部账单 核对信息 修改
	 */
	public void innerAccnoCheck (Map<String,String> updateMap) throws SQLException;
	 
	/**
	 *  确认关联表中 accno 的唯一性
	 *  如果 已存在 返回 1 不存在 0
	 * @throws SQLException 
	*/
	public int ifExistAccnoInParam(String accno) throws SQLException;
	
	 
	public List<Map<String,Object>> queryInnerRecheckResult(Map<String,String> queryMap,PageParam param) throws SQLException;
	
	public void updateRecheck (Map<String,String> updateMap) throws SQLException;
	
	public List<Map<String,Object>> queryInnerList(Map<String,String> queryMap,PageParam param,boolean isPage) throws SQLException;
	
	public List<Map<String,Object>> queryInnerAccnoList(Map<String,String> queryMap,PageParam param,boolean isPage) throws SQLException;
	
	public int ifExistAccno(String accno) throws SQLException;
	
	public void addInnerAccno(String accNo,String inputDate,String peopleCode) throws XDocProcException;
	
	public void deleteInnerAccno(String accNo) throws XDocProcException;
	
	public List<Map<String,Object>> getInnerAccnoDetailData(Map<String, String> queryMap,PageParam param,boolean isPaged) throws SQLException;
}
