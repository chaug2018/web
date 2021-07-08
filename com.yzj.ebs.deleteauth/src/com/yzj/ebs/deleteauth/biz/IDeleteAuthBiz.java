package com.yzj.ebs.deleteauth.biz;

import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.database.DocSet;
import com.yzj.ebs.deleteauth.service.BillInfo;


/**
 * 创建于:2013-04-11<br>
 * 版权所有(C) 2013 深圳市银之杰科技股份有限公司<br>
 * 删除审核业务层实现
 * 
 * @author jiangzhengqiu
 * @version 1.0.0
 */
public interface IDeleteAuthBiz {
	
	/**
	 * 查询单个票据
	 * 
	 * @return DocSet
	 * @throws XDocProcException
	 */
	public DocSet queryOneByID(Long docId) throws XDocProcException;
	/**
	 * 对象类型装换,并设置docSet的credit字段
	 * 
	 * @param list
	 * @return 票据对象
	 * @throws XDocProcException 
	 */
	public BillInfo getManualInputInfo(DocSet docSet) throws XDocProcException;
	/***
	 * 根据流水ID 删除未达信息
	 * @param docId
	 * @throws XDocProcException
	 */
	void deleteNotMatchList(String docId) throws XDocProcException;
}
