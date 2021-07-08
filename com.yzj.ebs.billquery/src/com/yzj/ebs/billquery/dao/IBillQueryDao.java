package com.yzj.ebs.billquery.dao;

import java.util.List;

import com.yzj.ebs.billquery.dao.pojo.QueryParam;
import com.yzj.ebs.database.DocLog;
import com.yzj.ebs.database.DocSet;

public interface IBillQueryDao {
	
	/** 分页查询票据信息列表  ebs_docset*/
	public List<DocSet> queryBillListByPage(QueryParam queryParam);
	/** 分页查询票据信息列表  ebs_docset*/
	public List<DocSet> queryBillList(QueryParam queryParam);
	
	/** 根据账单编号获取账单明细信息 */
	public DocSet queryDocSetByVoucherNo(String voucherNo);
	
	/** 根据无账单编号获取账单明细信息 */
	public DocSet queryDocSetByDocId(String docId);
	
	/** 获取日志列表 */
	public List<DocLog> queryDocLogList(String docId);
	
	/** 执行hql语句 */
	public void executeHql(String hql);
}