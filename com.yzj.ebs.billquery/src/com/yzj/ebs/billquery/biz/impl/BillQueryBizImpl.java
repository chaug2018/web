package com.yzj.ebs.billquery.biz.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yzj.ebs.billquery.biz.IBillQueryBiz;
import com.yzj.ebs.billquery.queryparam.QueryParam;
import com.yzj.ebs.common.IBillQueryAdm;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.database.DocLog;
import com.yzj.ebs.database.DocSet;

public class BillQueryBizImpl implements IBillQueryBiz{
	IBillQueryAdm billQueryDao;
	

	/** 分页查询票据信息列表  ebs_docset
	 * @throws XDocProcException */
	public List<DocSet> queryBillListByPage(QueryParam queryParam) throws XDocProcException {
		Map<String, String> queryMap = new HashMap<String,String>();
		// 设置输入的参数
		queryMap.put("docId", queryParam.getDocId());
		queryMap.put("voucherNo", queryParam.getVoucherNo());
		queryMap.put("docFlag", queryParam.getDocFlag());
		queryMap.put("idCenter", queryParam.getIdCenter());
		queryMap.put("idBranch", queryParam.getIdBranch());
		queryMap.put("idBank", queryParam.getIdBank());
		queryMap.put("startDate", queryParam.getStartDate());
		queryMap.put("endDate", queryParam.getEndDate());
		queryMap.put("checkDate", queryParam.getCheckDate());
		PageParam pageparam = new PageParam();
		pageparam.setPageSize(queryParam.getPageSize());
		pageparam.setCurPage(queryParam.getCurPage());
		List<DocSet> list = billQueryDao.queryBillListByPage(queryMap, pageparam);
		// 设置输出的参数
		queryParam.setFirstResult(pageparam.getFirstResult());
		queryParam.setTotal(pageparam.getTotal());
		queryParam.setTotalPage(pageparam.getTotalPage());
		queryParam.setLastResult(pageparam.getFirstResult() + list.size());// 当前页显示的最后一条记录
		return list;
	}
	/** 查询票据信息列表  ebs_docset
	 * @throws XDocProcException */
	public List<DocSet> queryBillList(QueryParam queryParam) throws XDocProcException {
		Map<String, String> queryMap = new HashMap<String,String>();
		// 设置输入的参数
		queryMap.put("docId", queryParam.getDocId());
		queryMap.put("voucherNo", queryParam.getVoucherNo());
		queryMap.put("docFlag", queryParam.getDocFlag());
		queryMap.put("idCenter", queryParam.getIdCenter());
		queryMap.put("idBranch", queryParam.getIdBranch());
		queryMap.put("idBank", queryParam.getIdBank());
		queryMap.put("startDate", queryParam.getStartDate());
		queryMap.put("endDate", queryParam.getEndDate());
		queryMap.put("checkDate", queryParam.getCheckDate());
		List<DocSet> list = billQueryDao.queryBillList(queryMap);
		return list;
	}
	/** 根据docId获取账单明细信息 
	 * @throws XDocProcException */
	public DocSet queryDocSetByVoucherNo(String docId) throws XDocProcException {
		DocSet docSet = billQueryDao.queryDocSetByVoucherNo(docId);
		return docSet;
	}
	
	/** 获取日志列表 
	 * @throws XDocProcException */
	public List<DocLog> queryDocLogList(String docId) throws XDocProcException {
		List<DocLog> docLogList = billQueryDao.queryDocLogList(docId);
		return docLogList;
	}
	
	/** 发起删除，更新账单表状态为-2 */
	public void deleteBill(String docId) {
		String hql = "update DocSet set docflag = -1 where docid =" + docId;
		billQueryDao.executeHql(hql);
	}
	public IBillQueryAdm getBillQueryDao() {
		return billQueryDao;
	}
	public void setBillQueryDao(IBillQueryAdm billQueryDao) {
		this.billQueryDao = billQueryDao;
	}
}