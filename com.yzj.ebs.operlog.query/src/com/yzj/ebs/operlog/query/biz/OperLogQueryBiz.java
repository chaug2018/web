package com.yzj.ebs.operlog.query.biz;

import java.util.ArrayList;
import java.util.List;

import com.yzj.ebs.common.IBasicInfoLogAdm;
import com.yzj.ebs.common.ICheckMainDataLogAdm;
import com.yzj.ebs.common.IPublicTools;
import com.yzj.ebs.common.OperLogQueryParam;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.database.BasicInfoLog;
import com.yzj.wf.core.model.po.wrapper.XPeopleInfo;

/**
 * 创建于:2013-04-01<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 日志查询 业务实现
 * 
 * @author 单伟龙
 * @version 1.0.0
 */
public class OperLogQueryBiz implements IOperLogQueryBiz {

	private IBasicInfoLogAdm basicInfoLogAdm;
	private ICheckMainDataLogAdm checkMainDataLogAdm;
	private IPublicTools tools;
	@SuppressWarnings("rawtypes")
	List<?> queryList = new ArrayList();

	/**
	 * 查询对账单日志表
	 * 
	 * @throws XDocProcException
	 */
	@Override
	public List<?> queryCheckMainDataLog(OperLogQueryParam param,
			boolean isPaged) throws XDocProcException {
		// TODO Auto-generated method stub
		try {
			queryList.clear();
			XPeopleInfo people = tools.getCurrLoginPeople();
			if (people == null) {
				throw new XDocProcException("会话超时，请重新登录！");
			}

			queryList = checkMainDataLogAdm.queryCheckMainDataLog(param,
					isPaged);
			return queryList;
		} catch (XDocProcException e) {
			// TODO Auto-generated catch block
			throw new XDocProcException(e.getMessage());
		}
	}

	/**
	 * 查询账户信息日志表
	 * 
	 * @throws XDocProcException
	 */
	@Override
	public List<?> queryBasicInfoLog(OperLogQueryParam param, boolean isPaged)
			throws XDocProcException {
		// TODO Auto-generated method stub
		queryList.clear();
		queryList = basicInfoLogAdm.queryBasicInfoLog(param, isPaged);
		return queryList;
	}

	public IBasicInfoLogAdm getBasicInfoLogAdm() {
		return basicInfoLogAdm;
	}

	public void setBasicInfoLogAdm(IBasicInfoLogAdm basicInfoLogAdm) {
		this.basicInfoLogAdm = basicInfoLogAdm;
	}

	public ICheckMainDataLogAdm getCheckMainDataLogAdm() {
		return checkMainDataLogAdm;
	}

	public void setCheckMainDataLogAdm(ICheckMainDataLogAdm checkMainDataLogAdm) {
		this.checkMainDataLogAdm = checkMainDataLogAdm;
	}

	public IPublicTools getTools() {
		return tools;
	}

	public void setTools(IPublicTools tools) {
		this.tools = tools;
	}
	
	
	@Override
	public List<BasicInfoLog> findBasicInfoLog(BasicInfoLog basicInfoLog) {
		return basicInfoLogAdm.findBasicInfoLog(basicInfoLog);
	}

	@Override
	public void updateBasicInfoLog(BasicInfoLog basicInfoLog)
			throws XDocProcException {
		basicInfoLogAdm.updateBasicInfoLog(basicInfoLog);
		
	}

	@Override
	public BasicInfoLog createBasicInfoLog(BasicInfoLog basicInfoLog)
			throws XDocProcException {
		return basicInfoLogAdm.createBasicInfoLog(basicInfoLog);
	}

}
