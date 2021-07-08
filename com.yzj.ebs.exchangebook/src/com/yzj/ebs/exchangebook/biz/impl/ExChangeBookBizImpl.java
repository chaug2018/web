/**
 * AccoperBizImpl.java
 * 版权所有(C) 2013 深圳市银之杰科技股份有限公司
 * 创建:Jiangzhengqiu 2013-03-29
 */
package com.yzj.ebs.exchangebook.biz.impl;

import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;

import com.yzj.ebs.common.BankParam;
import com.yzj.ebs.common.IExChangeBookAdm;
import com.yzj.ebs.common.IPublicTools;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.database.CheckMainData;
import com.yzj.ebs.database.ExChangeBook;
import com.yzj.ebs.exchangebook.action.QueryParam;
import com.yzj.ebs.exchangebook.biz.IExChangeBookBiz;
import com.yzj.wf.am.security.common.AMSecurityDefine;
import com.yzj.wf.core.model.po.wrapper.XPeopleInfo;

/**
 * 创建于:2013-03-29 版权所有(C) 2013 深圳市银之杰科技股份有限公司
 * 
 * 账户信息业务逻辑统一实现
 * 
 * @author jiangzhengqiu
 * @version 1.0.0
 */
public class ExChangeBookBizImpl implements IExChangeBookBiz {
	private IPublicTools tools;
	private IExChangeBookAdm exChangeBookAdm;
	
	/**
	 * 查询账单交接列表,按分页查询
	 * 
	 * @return List<ExChangeBook>
	 * @throws XDocProcException
	 */
	@Override
	public List<ExChangeBook> getExChangeBook(Map<String, String> queryMap,
			PageParam param, BankParam bankparam) throws XDocProcException {
		return exChangeBookAdm.getExChangeBook(queryMap, param, bankparam);
	}
	/**
	 * 查询账单交接列表,查询所有
	 * 
	 * @return List<ExChangeBook>
	 * @throws XDocProcException
	 */
	@Override
	public List<ExChangeBook> getAllExChangeBook(Map<String, String> queryMap,
			BankParam bankparam) {
		return exChangeBookAdm.getAllExChangeBook(queryMap, bankparam);
	}

	@Override
	/**
	 * 确认当前登录柜员是否有权限查看录入账号的明细
	 * 
	 * @return
	 * @throws XDocProcException
	 */
	public boolean isQueryAllowed(QueryParam queryParam) throws XDocProcException {
		CheckMainData checkMainData = new CheckMainData();
		String hql = "from CheckMainData where voucherNo='"
				+ queryParam.getVoucherNo() + "'";
		List<?> list = exChangeBookAdm.findByHql(hql);
		if (list == null || list.size() < 1) {
			return false; // 若accno不存在则返回false
		} else {
			checkMainData = (CheckMainData) list.get(0);
			XPeopleInfo userInfo = (XPeopleInfo) ServletActionContext
					.getRequest().getSession()
					.getAttribute(AMSecurityDefine.XPEOPLEINFO); // 获取当前登录柜员所在机构的的机构信息
			String idBank = userInfo.getOrgNo();
			BankParam bankParam = tools.getBankParam(idBank);
			Short level = bankParam.getLevel();
			if (level == 1) {// 总行
				return true;
			} else if (level == 2) {// 分行
				if (checkMainData.getIdCenter().equals(idBank)) {
					return true;
				} else {
					return false;
				}
			} else if (level == 3) {// 支行
				if (checkMainData.getIdBranch().equals(idBank)
						|| checkMainData.getIdBank().equals(idBank)) {
					return true;
				} else {
					return false;
				}
			} else if (level == 4) {// 网点
				if (checkMainData.getIdBank().equals(idBank)) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
	}

	@Override
	public ExChangeBook create(ExChangeBook book) throws XDocProcException {
		/***
		 * 控制
		 */
		XPeopleInfo people = (XPeopleInfo) tools.getCurrLoginPeople();
		BankParam param = tools.getBankParam(people.getOrgNo());
		Short level = param.getLevel();
		if (level == 1) {// 总行
		// 对账中心
			book.setIdBranch(param.getIdCenter());
			book.setIdBank(param.getIdCenter());
			book.setIdCenter(param.getIdCenter());
		} else if (level == 2) {// 分行
			book.setIdBranch(param.getIdCenter());
			book.setIdBank(param.getIdCenter());
			// 对账中心
			book.setIdCenter(param.getIdBank());
		} else if (level == 3) {// 支行
		// 对账中心
			book.setIdBank(param.getIdBank());
			book.setIdCenter(param.getIdCenter());
			// 支行
			book.setIdBranch(param.getIdBank());
		} else if (level == 4) {// 网点
		// 对账中心
			book.setIdCenter(param.getIdCenter());
			// 支行
			book.setIdBranch(param.getIdBranch());
			// 网点
			book.setIdBank(param.getIdBank());
		}
		return exChangeBookAdm.create(book);
	}

	public IExChangeBookAdm getExChangeBookAdm() {
		return exChangeBookAdm;
	}

	public void setExChangeBookAdm(IExChangeBookAdm exChangeBookAdm) {
		this.exChangeBookAdm = exChangeBookAdm;
	}
	public IPublicTools getTools() {
		return tools;
	}

	public void setTools(IPublicTools tools) {
		this.tools = tools;
	}
}
