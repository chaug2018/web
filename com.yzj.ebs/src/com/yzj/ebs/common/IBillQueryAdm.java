package com.yzj.ebs.common;

import java.util.List;
import java.util.Map;

import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.database.DocLog;
import com.yzj.ebs.database.DocSet;

public interface IBillQueryAdm {
	
	/** 分页查询票据信息列表  ebs_docset
	 * @throws XDocProcException */
	public List<DocSet> queryBillListByPage(Map<String, String> queryMap,PageParam queryParam) throws XDocProcException;
	/** 分页查询票据信息列表  ebs_docset
	 * @throws XDocProcException */
	public List<DocSet> queryBillList(Map<String, String> queryMap) throws XDocProcException;
	
	/** 根据账单编号获取账单明细信息 
	 * @throws XDocProcException */
	public DocSet queryDocSetByVoucherNo(String voucherNo) throws XDocProcException;
	
	/** 根据无账单编号获取账单明细信息 
	 * @throws XDocProcException */
	public DocSet queryDocSetByDocId(String docId) throws XDocProcException;
	
	/** 获取日志列表 
	 * @throws XDocProcException */
	public List<DocLog> queryDocLogList(String docId) throws XDocProcException;
	
	/** 执行hql语句 */
	public void executeHql(String hql);
}