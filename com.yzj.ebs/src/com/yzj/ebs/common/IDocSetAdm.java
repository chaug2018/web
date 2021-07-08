package com.yzj.ebs.common;

import java.util.List;

import com.yzj.ebs.database.DocSet;

/**
 * 创建于:2012-8-3<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * DocSet表操作访问服务接口定义
 * 
 * @author WangXue
 * @version 1.0.0
 */
public interface IDocSetAdm extends IBaseService<DocSet> {

	/**
	 * 根据DocId查询docset
	 * 
	 * @param docId
	 *            主键docid
	 * @return 查询结果
	 * @throws XDocProcException
	 *             异常
	 */
	DocSet queryOneByID(Long docId) throws XDocProcException;
	
	
	/**
	 * 根据提入行号和工作日期查询DocSet
	 * 
	 * @param branchNo
	 *            提入行号
	 * @param workdate
	 *            工作日期
	 * @return 查询结果，当查询结果为空时返回null
	 * @throws XDocProcException
	 *             异常
	 */
	List<DocSet> findByBranchNoAndWorkDate(String branchNo, String workdate)
			throws XDocProcException;

	/**
	 * 根据对账单号查找从网银对账结果创建的DocSet记录。
	 * 注：网银结果只有在余额不符的情况下才会创建DocSet，且初始状态docFlag=3而不是0
	 * @param voucherNo
	 * 				对账单号
	 * @return
	 * 				查询结果
	 * @throws 
	 * 			XDocProcException
	 *             异常
	 */
	DocSet findEBankDocSetByVoucherNo(String voucherNo) throws XDocProcException;
}
