package com.yzj.ebs.blackwhite.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;


import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.yzj.ebs.blackwhite.biz.IInputQueryBiz;
import com.yzj.ebs.blackwhite.queryparam.InputQueryParam;
import com.yzj.ebs.common.IPublicTools;
import com.yzj.ebs.common.RefTableTools;
import com.yzj.ebs.common.SimpleOrg;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.database.ImportSpecile;
import com.yzj.ebs.util.FinalConstant;
import com.yzj.wf.am.security.common.AMSecurityDefine;
import com.yzj.wf.common.WFLogger;
import com.yzj.wf.core.model.po.wrapper.XPeopleInfo;
/**
 * 
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 特殊账户导入表 查询
 * 
 * @author j_sun
 * @version 1.0.0
 */
public class InputQueryAction extends ActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static WFLogger logger = WFLogger.getLogger(InputQueryAction.class);
	private IPublicTools tools;
	private List<ImportSpecile> result = new ArrayList<ImportSpecile>();
	private InputQueryParam inputParam ;
	private IInputQueryBiz inputQueryImpl;
	private TreeMap<String, String> refAccCycleMap = new TreeMap<String, String>();
	private Map<String, String> refImportName = new HashMap<String, String>();
	private TreeMap<String, String> sendTypeMap = new TreeMap<String, String>();
	private TreeMap<String, String> sealModeMap = new TreeMap<String, String>();
	
	public String init() {
		result.clear();
		inputParam = new InputQueryParam();
		SimpleOrg org=null;
		HttpServletRequest request = ServletActionContext.getRequest();
		XPeopleInfo people = (XPeopleInfo) request.getSession().getAttribute(
				AMSecurityDefine.XPEOPLEINFO);
		try {
			// 获取机构信息树
			org = tools.getCurOrgTree(people.getOrgNo());
		} catch (XDocProcException e) {
			logger.error("获取当前机构信息列表出现错误",e);
		}
		getRefAccCycleMap();
		getSendTypeMap();
		getSealModeMap();
		refImportName = FinalConstant.refImportName;
		return "initSuccess";
	}

	public String queryData(){
		try {
			result = inputQueryImpl.queryDate(getQueryMap(), inputParam);
		} catch (XDocProcException e) {
			logger.error("特殊账户过滤表查询出错！");
			e.printStackTrace();
		}
		return  "initSuccess";
	}

	/**
	 * 拼凑查询的Map
	 * @return
	 */
	public Map<String,String> getQueryMap(){
		Map<String,String> queryMap = new HashMap<String,String>();
		queryMap.put("accno", inputParam.getAccno());
		queryMap.put("accCycle", inputParam.getAccCycle());
		queryMap.put("isImport", inputParam.getFlag());
		queryMap.put("docdate", inputParam.getDate().replace("-", ""));
		return queryMap;
	}
	
		/* set get*/
	public IPublicTools getTools() {
		return tools;
	}
	
	public void setTools(IPublicTools tools) {
		this.tools = tools;
	}
	
	public List<ImportSpecile> getResult() {
		return result;
	}
	
	public void setResult(List<ImportSpecile> result) {
		this.result = result;
	}

	public InputQueryParam getInputParam() {
		return inputParam;
	}

	public void setInputParam(InputQueryParam inputParam) {
		this.inputParam = inputParam;
	}

	public IInputQueryBiz getInputQueryImpl() {
		return inputQueryImpl;
	}

	public void setInputQueryImpl(IInputQueryBiz inputQueryImpl) {
		this.inputQueryImpl = inputQueryImpl;
	}

	public TreeMap<String, String> getRefAccCycleMap() {
		if (null == refAccCycleMap || refAccCycleMap.size() == 0) {
			refAccCycleMap = RefTableTools.ValRefAccCycleMap;
		}
		return refAccCycleMap;
	}

	public void setRefAccCycleMap(TreeMap<String, String> refAccCycleMap) {
		this.refAccCycleMap = refAccCycleMap;
	}

	public Map<String, String> getRefImportName() {
		return refImportName;
	}

	public void setRefImportName(Map<String, String> refImportName) {
		this.refImportName = refImportName;
	}

	public TreeMap<String, String> getSendTypeMap() {
		if (sendTypeMap == null || sendTypeMap.size() == 0) {
			sendTypeMap = RefTableTools.ValRefSendModeMap;
		}
		return sendTypeMap;
	}

	public void setSendTypeMap(TreeMap<String, String> sendTypeMap) {
		this.sendTypeMap = sendTypeMap;
	}

	public TreeMap<String, String> getSealModeMap() {
		if (sealModeMap == null || sealModeMap.size() == 0) {
			sealModeMap = RefTableTools.ValRefSealModeMap;
		}
		return sealModeMap;
	}
	public void setSealModeMap(TreeMap<String, String> sealModeMap) {
		this.sealModeMap = sealModeMap;
	}

	
}
