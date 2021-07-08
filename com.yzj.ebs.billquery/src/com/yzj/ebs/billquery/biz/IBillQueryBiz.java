package com.yzj.ebs.billquery.biz;

import java.util.List;
import java.util.Map;

import com.yzj.ebs.billquery.queryparam.QueryParam;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.database.DocLog;
import com.yzj.ebs.database.DocSet;

public interface IBillQueryBiz {
	
	/** 分页查询票据信息列表  ebs_docset
	 * @throws XDocProcException */
	public List<DocSet> queryBillListByPage(QueryParam queryParam) throws XDocProcException;
	/** 分页查询票据信息列表  ebs_docset
	 * @throws XDocProcException */
	public List<DocSet> queryBillList(QueryParam queryParam) throws XDocProcException;
	
	/** 根据账单编号获取账单明细信息 
	 * @throws XDocProcException */
	public DocSet queryDocSetByVoucherNo(String docId) throws XDocProcException;
	
	/** 获取日志列表 
	 * @throws XDocProcException */
	public List<DocLog> queryDocLogList(String docId) throws XDocProcException;
	
	
	
	/** 发起删除，更新账单表状态为-2 */
	public void deleteBill(String docId);
}