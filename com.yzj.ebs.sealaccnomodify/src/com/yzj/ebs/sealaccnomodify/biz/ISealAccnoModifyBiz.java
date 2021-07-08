package com.yzj.ebs.sealaccnomodify.biz;

import java.util.List;
import java.util.Map;

import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.database.BasicInfo;


/**
 * 创建于:2013-04-01<br>
 * 版权所有(C) 2013 深圳市银之杰科技股份有限公司<br>
 * 账户信息操作业务接口
 * 
 * @author jiangzhengqiu
 * @version 1.0
 */
public interface ISealAccnoModifyBiz  {

	/***
	 * 删除账户信息业务逻辑
	 * @param info 账户信息对象
	 * @throws XDocProcException
	 */
	public void delete(BasicInfo info) throws XDocProcException;
	
	/***
	 * 判断验印账号与签约账号的客户号是否一致
	 * @return 是否一致
	 */
	public boolean isTheSame(String accNo,String sealAccno) throws XDocProcException;
	
	/***
	 * 判断账号是否存在印鉴
	 * @return 是否存在印鉴
	 * @throws XDocProcException 
	 */
	public boolean isExistSeal(String accNo) throws XDocProcException;
	
	/***
	 * 修改账户信息
	 * @return  void
	 * @throws XDocProcException 
	 */
	public void modify(BasicInfo info,String desc) throws XDocProcException;
	
	/***
	 * 查询账户信息
	 * @return 账户信息列表
	 */
	public List<BasicInfo> getBasicinfoData(Map<String, String> queryMap,
			PageParam param) throws XDocProcException;
	
	/***
	 * 查询所有账户
	 * @return 账户信息列表
	 */
	public List<BasicInfo> getAllBasicInfo(Map<String, String> queryMap) throws XDocProcException;
	
	/***
	 * 新增一个客户信息
	 * @throws XDocProcException 
	 */
	public void create(BasicInfo info,String desc) throws XDocProcException;
	
	/***
     * 根據帳號查詢帳戶信息
     * @throws XDocProcException 
     */
	public BasicInfo getOneByAccNo(String accNo) throws XDocProcException;

}
