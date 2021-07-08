package com.yzj.ebs.common;

import java.util.List;
import java.util.Map;

import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.database.NotMatchTable;

/**
 * 
 *创建于:2012-11-12<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 未达项数据操作类
 * @author 施江敏
 * @version 1.0.0
 */
public interface INotMatchTableAdm extends IBaseService<NotMatchTable>{

	/**
	 * 根据DocId查询NotMatchTable
	 * 
	 * @param docId
	 *            主键docid
	 * @return 查询结果
	 * @throws XDocProcException
	 *             异常
	 */
	public List<NotMatchTable> getNotMatchListByDocId(String docId) throws XDocProcException;
	/**
     * 批量添加未达项记录
     * @param notMatchList 
     * 			    需要增加的对象集合
     * @throws XDocProcException
     */
	public void saveNotMatchItems(List<NotMatchTable> notMatchList) throws XDocProcException;
	
	/**
     * 批量删除未达项记录
     * @param notMatchList 
     * 			    需要删除的对象集合
     * @throws XDocProcException
     */
	public void deleteNotMatchList(List<NotMatchTable> notMatchList) throws XDocProcException;
	
	/**
     * 根据条件分页查询未达项记录，查询条件存放于一个Map中
     * @param queryMap 
     * 			    查询参数
     * @param param
     * 			  分页参数
     * @throws XDocProcException
     */
	public List<NotMatchTable> getNotMatchTableData(Map<String, String> queryMap,
			PageParam param) throws XDocProcException;
	/**
     * 根据条件查询所有未达项记录，查询条件存放于一个Map中
     * @param queryMap 
     * 			    查询参数
     * @throws XDocProcException
     */
	public List<NotMatchTable> getAllNotMatchData(Map<String, String> queryMap) throws XDocProcException;
	/**
     * 批量更新未达项记录
     * @param notMatchList 
     * 			    需要更新的记录
     * @throws XDocProcException
     */
	public void updateNotMatchList(List<NotMatchTable> notMatchList) throws XDocProcException;

	/**
	 * 未达账情况统计
	 * @param queryMap
	 * @return
	 * @throws XDocProcException
	 */
	@SuppressWarnings("rawtypes")
	public List getAnalyseresults(Map<String, String> queryMap,PageParam queryParam,boolean isPaged) throws XDocProcException;
	
	/**
	 * 未达账情况统计(按单位统计)
	 * @param queryMap
	 * @return
	 * @throws XDocProcException
	 */
	@SuppressWarnings("rawtypes")
	public List getAnalyseresultsCount(Map<String, String> queryMap,PageParam queryParam,
			boolean isPaged,String selectCount) throws XDocProcException;
	
	/**
	 * 获得所有对账中心的名字
	 * @return
	 * @throws XDocProcException
	 */
	@SuppressWarnings("rawtypes")
	public List getAllIdBranchName() throws XDocProcException;
	
	/**
	 * 更新accnomaindata的余额状态
	 * @param accno 账号
	 * 		  checkfalg 余额结果
	 * 		  voucherno 账单编号
	 * @return
	 * @throws XDocProcException
	 */
	//@SuppressWarnings("rawtypes")
	public void updateAccnomaindata(String accno,String checkfalg,String voucherno) throws XDocProcException;
	
}
