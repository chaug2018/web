package com.yzj.ebs.common;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.yzj.ebs.util.DataLogUtils;
import com.yzj.wf.core.model.po.wrapper.XPeopleInfo;
import com.yzj.wf.pam.db.BaseParam;
import com.yzj.wf.pam.query.ParamQuery;

/**
 *创建于:2012-10-8<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 常用方法接口
 * @author 陈林江
 * @version 1.0
 */
public interface IPublicTools {

	/**
	 * 获取当前工作日
	 * @return
	 */
	String getCurWorkDate()throws XDocProcException;

	/**
	 * 获取对账中心额度信息
	 * @param id 对账中心id号
	 * @return 额度信息实体类
	 * @throws XDocProcException
	 */
	CreditParam getCreditParam(String id)throws XDocProcException;


	/**
	 * 通过对账中心号，获取对账中心信息
	 * @param idCenter 对账中心号
	 * @return 对账中心实体类
	 * @throws XDocProcException
	 */
	IdCenterParam getParamIdcenter(String idCenter)throws XDocProcException;

	/**
	 * 通过机构号获取机构参数对象
	 * @param idBank 机构号
	 * @return 机构对象
	 * @throws XDocProcException
	 */
	BankParam getBankParam(String idBank)throws XDocProcException;

	/**
	 * 获取验印不过理由参数表
	 */
	Map<String,String> getUReason() throws XDocProcException;


	/**
	 * 从param_bank表中获取机构表列表参数
	 * @param
	 * @return Map<String,BankParam>
	 * @throws XDocProcException
	 */
	Map<String,BankParam> getAllBankParam()throws XDocProcException;


	/**
	 * 从param_sysbase表中获取参数
	 * @param sysbaseid
	 * @return sysbasevalue
	 * @throws XDocProcException
	 */
	String getSysbaseParam(String sysbaseId) throws XDocProcException;

	/**
	 * 获取删除理由列表
	 * @return 删除理由列表
	 * @throws XDocProcException
	 */
	List<String> getDeleteReason()throws XDocProcException;

	/**
	 * 获取重录理由列表
	 * @return 重录理由列表
	 * @throws XDocProcException
	 */
	List<String> getReInputReason()throws XDocProcException;

	/**
	 * 根据结构号获取当前可以访问的机构树列表
	 * @param orgID 机构号
	 * @return
	 * @throws XDocProcException
	 */
	SimpleOrg getCurOrgTree(String orgID)throws XDocProcException;

	/***
	 * 获取当前登录用户对象
	 */
	XPeopleInfo getCurrLoginPeople() throws XDocProcException;
	

	/**
	 * 
	 * 通过查询条件从参数表中获取参数对象
	 * 
	 * @author 20130509 guoyj
	 * 
	 * @param pq
	 *            查询条件ParamQuery对象
	 * @param tableName
	 *            参数表对应实体类名称
	 * @return 返回获取参数对象
	 * @throws XDocProcException
	 */
	List<BaseParam> getBaseParamsByCondition(ParamQuery pq, String tableName)
			throws XDocProcException;

	/**
	 * @param currtype
	 *            币种类型
	 * @return 对人人民币的汇率
	 * @throws XDocProcException
	 */
	double getExchangeRate(String currtype) throws XDocProcException;
	
	/**
	 * 账户是否对账map
	 * 0 对账  1不对账
	 * @return
	 */
	Map<String, String> getRefIsCheck ();
	


	/**
	 * 是不是特殊帐户map
	 * 0 特殊账户  1普通账户
	 * @return
	 */
	Map<String, String> getRefIsSpecile ();
	
	/**
	 * 账户状态map
	 * 0 正常  1 销户 2 长期不动户 3 不动转收益
	 * @return
	 */
	Map<String, String> getAccState ();
	
	
	List<BankParam> getBankParamList();
	
	/**
	 * 删除文件
	 */
	public void deleteFile(File file);
	
	/**
	 * ajax方式返回信息
	 * @param result
	 */
	void ajaxResult(String result);
	
	/**
	 * 获取前台页面传递的参数
	 * @return
	 */
	String getParameter(String param);
	
	/**
	 * 获取日志类
	 * @return
	 */
	 DataLogUtils getDataLogUtils();
}
