/**
 * AccoperBizImpl.java
 * 版权所有(C) 2013 深圳市银之杰科技股份有限公司
 * 创建:Jiangzhengqiu 2013-03-29
 */
package com.yzj.ebs.deleteauth.biz.impl;

import java.util.List;

import com.yzj.ebs.common.IAccnoMainDataAdm;
import com.yzj.ebs.common.IDocSetAdm;
import com.yzj.ebs.common.INotMatchTableAdm;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.database.AccNoMainData;
import com.yzj.ebs.database.DocSet;
import com.yzj.ebs.database.NotMatchTable;
import com.yzj.ebs.deleteauth.biz.IDeleteAuthBiz;
import com.yzj.ebs.deleteauth.service.BillInfo;

/**
 * 创建于:2013-03-29 版权所有(C) 2013 深圳市银之杰科技股份有限公司
 * 
 * 删除审核业务逻辑统一实现
 * 
 * @author jiangzhengqiu
 * @version 1.0.0
 */
public class DeleteAuthBizImpl implements IDeleteAuthBiz {
	private IDocSetAdm docSetAdm;
	private IAccnoMainDataAdm accnoMainDataAdm;
	private INotMatchTableAdm notMatchAdm;
	
	/**
	 * 查询单个票据
	 * 
	 * @return DocSet
	 * @throws XDocProcException
	 */
	@Override
	public DocSet queryOneByID(Long docId) throws XDocProcException
	{
		return docSetAdm.queryOneByID(docId);
	}
	

	/**
	 * 对象类型装换,并设置docSet的credit字段
	 * 
	 * @param list
	 * @return 票据对象
	 * @throws XDocProcException 
	 */
	@Override
	 public BillInfo getManualInputInfo(DocSet docSet) throws XDocProcException
	 {
		 BillInfo info = new BillInfo();
		 List<AccNoMainData> accNolist = accnoMainDataAdm
				.getAccnoMainDataByVoucherNo(docSet.getVoucherNo());
		 double totalCredit=0;
			for (int i = 0; i < accNolist.size(); i++) {
				AccNoMainData data = accNolist.get(i);
				totalCredit+=data.getCredit();
				docSet.setCredit(totalCredit);
				if (i == 0) {
					info.setAccNo1(data.getAccNo());
					info.setAccNo_1(data.getAccNoSon());
					info.setResult1(data.getCheckFlag());
				} else if (i == 1) {
					info.setAccNo2(data.getAccNo());
					info.setAccNo_2(data.getAccNoSon());
					info.setResult2(data.getCheckFlag());
				} else if (i == 2) {
					info.setAccNo3(data.getAccNo());
					info.setAccNo_3(data.getAccNoSon());
					info.setResult3(data.getCheckFlag());
				} else if (i == 3) {
					info.setAccNo4(data.getAccNo());
					info.setAccNo_4(data.getAccNoSon());
					info.setResult4(data.getCheckFlag());
				} else if (i == 4) {
					info.setAccNo5(data.getAccNo());
					info.setAccNo_5(data.getAccNoSon());
					info.setResult5(data.getCheckFlag());
				}
			}
			return info;
	 }
	/***
	 * 根据流水ID 删除未达信息
	 * @param docId
	 * @throws XDocProcException
	 */
	@Override
	public void deleteNotMatchList(String docId) throws XDocProcException
	{
		List<NotMatchTable> result=notMatchAdm.getNotMatchListByDocId(docId);
		notMatchAdm.deleteNotMatchList(result);
	}
	
	public IDocSetAdm getDocSetAdm() {
		return docSetAdm;
	}
	public void setDocSetAdm(IDocSetAdm docSetAdm) {
		this.docSetAdm = docSetAdm;
	}
	public IAccnoMainDataAdm getAccnoMainDataAdm() {
		return accnoMainDataAdm;
	}
	public void setAccnoMainDataAdm(IAccnoMainDataAdm accnoMainDataAdm) {
		this.accnoMainDataAdm = accnoMainDataAdm;
	}
	public INotMatchTableAdm getNotMatchAdm() {
		return notMatchAdm;
	}
	public void setNotMatchAdm(INotMatchTableAdm notMatchAdm) {
		this.notMatchAdm = notMatchAdm;
	}
}
