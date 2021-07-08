package com.yzj.ebs.insideaccnoparam.action;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.yzj.ebs.common.IPublicTools;
import com.yzj.ebs.database.InnerAccnoMaindata;
import com.yzj.ebs.insideaccnoparam.biz.InsideAccnoRecheckBiz;
import com.yzj.ebs.insideaccnoparam.pojo.AccnoCheckParam;
import com.yzj.ebs.util.FinalConstant;
import com.yzj.wf.am.security.common.AMSecurityDefine;
import com.yzj.wf.common.WFLogger;
import com.yzj.wf.core.model.po.wrapper.XPeopleInfo;
/**
 * 创建于:2015-12-09 
 * 版权所有(C) 2015 深圳市银之杰科技股份有限公司
 * 内部账户对账单核对
 * 
 * @author 
 * @version 1.0.0
 */

public class InsideAccnoRecheckAction extends ActionSupport{
	
	private static final long serialVersionUID = 1L;
	private String custId;
	private IPublicTools tools;
	private static WFLogger logger = WFLogger.getLogger(InsideAccnoRecheckAction.class);
	private InsideAccnoRecheckBiz insideAccnoRecheckImpl;
	private AccnoCheckParam innerparam;
	private List<InnerAccnoMaindata> queryList = new ArrayList<InnerAccnoMaindata>();
	private Map<String,String> accordName = new HashMap<String,String>();
	private Map<String,String> recheckMap = new HashMap<String,String>();
	
	public String init(){
		innerparam = new AccnoCheckParam();
		queryList.clear();
		
		accordName= FinalConstant.accordName;
		HttpServletRequest request = ServletActionContext.getRequest();
		XPeopleInfo people = (XPeopleInfo) request.getSession().getAttribute(
				AMSecurityDefine.XPEOPLEINFO);
		custId = people.getPeopleCode(); 
		
		return "initSuccess";
	}
	
	/**
	 *  查询 内部账户对账单信息
	 * @return
	 */
	public String queryInnerAccno(){
		Map<String,String> queryMap = new HashMap<String,String>();
		String accno = innerparam.getAccno();
		String dataDate = innerparam.getDatadate();
		
		if (accno != null && accno.trim().length() > 0) {
			queryMap.put("accno", accno.trim());
		}
		if (dataDate != null && dataDate.length() > 0) {
			queryMap.put("datadate", dataDate.replace("-", ""));
		}
		
		queryMap.put("recheckpeoplecode",custId );
		
		try {
			queryList = insideAccnoRecheckImpl.queryAccnoRecheck(queryMap,innerparam);
		} catch (SQLException e) {
			logger.error("查询内部账户核对情况出错！",e );
			e.printStackTrace();
		}
		
		return "initSuccess";
	}
	
	
	/**
	 * 核对 内部账户对账单
	 * @return
	 * @throws IOException 
	 */
	public void checkInnerAccno() throws IOException{
		Map<String,String> updateMap = new HashMap<String,String>();
		updateMap.put("recheck", tools.getParameter("recheck_div"));
		updateMap.put("datadate", tools.getParameter("datadate_div"));
		updateMap.put("accno", tools.getParameter("accno_div"));
		updateMap.put("custId", custId);
		String msg="";
			 try {
				insideAccnoRecheckImpl.updateRecheck(updateMap);
				msg="modifySuccess";
			} catch (SQLException e) {
				logger.error("查询内部账户核对情况修改出错！" ,e);
				msg="false";
			}
		
		tools.ajaxResult(msg);
	}
	
	
	
	/* set get*/
	public String getCustId() {
		return custId;
	}
	public void setCustId(String custId) {
		this.custId = custId;
	}
	public IPublicTools getTools() {
		return tools;
	}
	public void setTools(IPublicTools tools) {
		this.tools = tools;
	}

	public InsideAccnoRecheckBiz getInsideAccnoRecheckImpl() {
		return insideAccnoRecheckImpl;
	}

	public void setInsideAccnoRecheckImpl(
			InsideAccnoRecheckBiz insideAccnoRecheckImpl) {
		this.insideAccnoRecheckImpl = insideAccnoRecheckImpl;
	}

	public AccnoCheckParam getInnerparam() {
		return innerparam;
	}

	public void setInnerparam(AccnoCheckParam innerparam) {
		this.innerparam = innerparam;
	}

	public List<InnerAccnoMaindata> getQueryList() {
		return queryList;
	}

	public void setQueryList(List<InnerAccnoMaindata> queryList) {
		this.queryList = queryList;
	}

	public Map<String, String> getAccordName() {
		return accordName;
	}

	public void setAccordName(Map<String, String> accordName) {
		this.accordName = accordName;
	}

	public Map<String, String> getRecheckMap() {
		if (null == recheckMap || recheckMap.size() == 0) {
			recheckMap.put("0", "复核通过");
			recheckMap.put("1", "复核不通过");
		}
		return recheckMap;
	}

	public void setRecheckMap(Map<String, String> recheckMap) {
		this.recheckMap = recheckMap;
	}
	

}
