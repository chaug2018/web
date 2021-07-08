package com.yzj.ebs.back.notmatch.biz;

import java.util.List;
import java.util.Map;

import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.database.AccNoMainData;
import com.yzj.ebs.database.DocSet;
import com.yzj.ebs.database.NotMatchTable;

/**
 * 创建于:2013-04-01<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 未达业务逻辑操作接口
 * 
 * @author jiangzhengqiu
 * @version 1.0
 */
public interface INotMatchBiz {

	/**
	 * 根据流水号查询单个票据
	 * 
	 * @param DocSet
	 *            票据对象
	 * 
	 */
	public DocSet queryOneByID(Long docId) throws XDocProcException;

	/**
	 * 根据流水号查询未达列表
	 * 
	 * @param List
	 *            <NotMatchTable> 未达列表
	 * 
	 */
	public List<NotMatchTable> getNotMatchListByDocId(String docId)
			throws XDocProcException;

	/**
	 * 更新未达列表
	 * 
	 * 
	 * 
	 */
	public void updateNotMatchList(List<NotMatchTable> notMatchList)
			throws XDocProcException;

	/**
	 * 根据账单编号获取余额状态列表
	 * 
	 * @param voucherno
	 *            凭证号 return 账户信息列表
	 */
	public void updateAccnoMainData(List<NotMatchTable> notMatchList,
			DocSet docset) throws XDocProcException;

	/**
	 * 删除未达列表
	 * 
	 * 
	 * 
	 */
	List<AccNoMainData> getAccnoMainDataByVoucherNo(String voucherno)
			throws XDocProcException;

	/**
	 * 根据条件查询未达列表
	 * 
	 * @param queryMap
	 *            查询条件 param 分页信息 return 未达列表
	 */
	void deleteNotMatchList(List<NotMatchTable> notMatchList)
			throws XDocProcException;

	/**
	 * 保存未达录入信息
	 * 
	 * @param notMatchList
	 *            未达列表
	 * 
	 */
	void saveNotMatchItems(List<NotMatchTable> notMatchList)
			throws XDocProcException;

	/**
	 * 根据条件查询未达列表
	 * 
	 * @param queryMap
	 *            查询条件 param 分页信息 return 未达列表
	 */
	List<NotMatchTable> getNotMatchTableData(Map<String, String> queryMap,
			PageParam param) throws XDocProcException;

	/**
	 * 取得所有的未达信息
	 * 
	 * 
	 * return 未达列表
	 */
	List<NotMatchTable> getAllNotMatchData(Map<String, String> queryMap)
			throws XDocProcException;
}
