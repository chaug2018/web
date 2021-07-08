package com.yzj.ebs.common;

import com.yzj.ebs.database.DocSet;



/**
 * 创建于:2012-8-21<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 账务接口，与行内核心系统的接口定义
 * 
 * @author WangXue
 * @version 1.0.0
 */
public interface ITallyService {

	/**
	 * 查询户名
	 * 
	 * @param docSet
	 *            业务对象
	 * @return 户名
	 * @throws XDocProcException
	 *             异常
	 */
	String getAccName(DocSet docSet,StringBuffer errorMsg) throws XDocProcException;

	/**
	 * 提入借记记账
	 * 
	 * @param docSet
	 *            业务对象
	 * @return 交易结果，成功返回true
	 * @throws XDocProcException
	 *             异常
	 */
	boolean tallyDebit(DocSet docSet) throws XDocProcException;

	/**
	 * 提入借记退票
	 * 
	 * @param docSet
	 *            业务对象
	 * @return 交易结果，成功返回true
	 * @throws XDocProcException
	 *             异常
	 */
	boolean untreadDebit(DocSet docSet) throws XDocProcException;

	/**
	 * 借记记账撤销
	 * 
	 * @param docSet
	 *            业务对象
	 * @return 交易结果，成功返回true
	 * @throws XDocProcException
	 *             异常
	 */
	boolean undoTallyDebit(DocSet docSet,StringBuffer errorMsg) throws XDocProcException;

	/**
	 * 密码核验
	 * 
	 * @param docSet
	 *            业务对象
	 * @return 交易结果，成功返回true
	 * @throws XDocProcException
	 *             异常
	 */
	boolean checkPayPwd(DocSet docSet,StringBuffer errorMsg) throws XDocProcException;
}
