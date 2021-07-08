package com.yzj.ebs.peoplerole.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import net.sf.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.yzj.ebs.common.IPublicTools;
import com.yzj.ebs.common.SimpleOrg;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.database.BasicInfo;
import com.yzj.ebs.peoplerole.biz.IQueryPeopleRoleBiz;
import com.yzj.ebs.peoplerole.param.PeopleRole;
import com.yzj.ebs.peoplerole.param.QueryPeopleRoleParam;
import com.yzj.ebs.util.FinalConstant;
import com.yzj.wf.common.WFLogger;
import com.yzj.wf.core.model.po.wrapper.XPeopleInfo;
import com.yzj.wf.export.common.ExportEntity;
import com.yzj.wf.export.service.DataExporterImpl;

/**
 * 创建于:2014-01-02<br>
 * 版权所有(C) 2014 深圳市银之杰科技股份有限公司<br>
 * 人员岗位查询
 * 
 * @author dengwu
 * @version 1.0.0
 */

public class QueryPeopleRoleAction extends ActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8106668273287742300L;
	private String errMsg = "";
	private IPublicTools tools;
	private String orgTree;
	private static WFLogger logger = WFLogger
			.getLogger(QueryPeopleRoleAction.class);

	// 查询值
	private String idCenter;
	private String idBank;
	private String peopleName;
	private String peopleCode;
	private String organizeInfo; // 机构号
	// 查询List
	private List<PeopleRole> resultList = new ArrayList<PeopleRole>();
	private QueryPeopleRoleParam queryParam;

	private IQueryPeopleRoleBiz queryPeopleRoleBiz;

	/**
	 * 初始化界面
	 * 
	 * @return
	 * @throws XDocProcException
	 */
	public String init() throws XDocProcException {
		errMsg = null;
		idCenter = "";
		idBank = "";
		peopleName = "";
		peopleCode = "";
		orgTree = "";
		queryParam = new QueryPeopleRoleParam();
		SimpleOrg org = null;
		resultList.clear();
		organizeInfo = "";
		XPeopleInfo people = tools.getCurrLoginPeople();
		try {
			org = tools.getCurOrgTree(people.getOrgNo());
		} catch (XDocProcException e) {
			errMsg = "获取当前机构信息列表出现错误";
			logger.error("获取当前机构信息列表出现错误", e);
		}
		JSONObject json = JSONObject.fromObject(org);
		orgTree = json.toString();
		return "initSuccess";
	}

	public String query() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		Map<String, String> queryMap = null;
		queryMap = this.getQueryMap();
		resultList.clear();
		try {
			resultList = queryPeopleRoleBiz.getPeopleRoleList(queryMap,queryParam,true);
		} catch (XDocProcException e) {
			e.printStackTrace();
		}

		return "initSuccess";
	}

	public Map<String, String> getQueryMap() {
		this.idCenter = queryParam.getIdCenter();
		this.idBank = queryParam.getIdBank();
		this.peopleName = queryParam.getPeopleName();
		this.peopleCode = queryParam.getPeopleCode();
		String organizeInfo = this.organizeInfo;
		Map<String, String> queryMap = new HashMap<String, String>();
		if (idCenter != null && idCenter.length() > 0 && idBank.equals("")) {
			queryMap.put("organizeinfo", idCenter);
		}
		if (organizeInfo != null && organizeInfo.length() > 0) {
			queryMap.put("organizeinfo", organizeInfo);
		}
		if (peopleName != null && peopleName.length() > 0) {
			queryMap.put("peopleName", peopleName);
		}
		if (peopleCode != null && peopleCode.length() > 0) {
			queryMap.put("peopleCode", peopleCode);
		}
		return queryMap;

	}

	/**
	 * 导出数据
	 * 
	 * @return 账户信息列表
	 * @throws IOException 
	 */
	public String exportData() throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		List<PeopleRole> exportList = new ArrayList<PeopleRole>();
		
		Map<String, String> queryMap = getQueryMap();
		
		try {
			exportList = queryPeopleRoleBiz.getPeopleRoleList(queryMap,queryParam,false);
			
			if (exportList.size() == 0 || exportList == null) {
				response.getWriter().write("查询列表为空");
				return null;
			}
		} catch (XDocProcException e1) {
			logger.error("账户信息导出时查询数据失败：" + e1.getMessage());
			response.getWriter().write("导出列表失败：" + e1.getMessage());
		}
		ExportEntity entity=new ExportEntity();
		entity.setFileName("人员岗位明细");
		entity.setTitle("人员岗位列表");
		entity.setDataList(exportList);
		Map<String,String> pro_desc=new LinkedHashMap<String, String>();
		
		pro_desc.put("orgid", "机构号");
		pro_desc.put("peopleName", "姓名");
		pro_desc.put("peopleCode", "账号");
		pro_desc.put("roleGroupName", "岗位名称");		

		entity.setPro_desc_map(pro_desc);
		try {
			new DataExporterImpl().export(entity);
			return null;
		} catch (Throwable e) {
			logger.error("导出列表失败：" + e.getMessage());
			response.getWriter().write("导出列表失败：" + e.getMessage());
		}
		return null;
		
	}
	
	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public IPublicTools getTools() {
		return tools;
	}

	public void setTools(IPublicTools tools) {
		this.tools = tools;
	}

	public String getOrgTree() {
		return orgTree;
	}

	public void setOrgTree(String orgTree) {
		this.orgTree = orgTree;
	}

	public static WFLogger getLogger() {
		return logger;
	}

	public static void setLogger(WFLogger logger) {
		QueryPeopleRoleAction.logger = logger;
	}

	public String getIdCenter() {
		return idCenter;
	}

	public void setIdCenter(String idCenter) {
		this.idCenter = idCenter;
	}

	public String getIdBank() {
		return idBank;
	}

	public void setIdBank(String idBank) {
		this.idBank = idBank;
	}

	public String getPeopleName() {
		return peopleName;
	}

	public void setPeopleName(String peopleName) {
		this.peopleName = peopleName;
	}

	public String getPeopleCode() {
		return peopleCode;
	}

	public void setPeopleCode(String peopleCode) {
		this.peopleCode = peopleCode;
	}

	public List<PeopleRole> getResultList() {
		return resultList;
	}

	public void setResultList(List<PeopleRole> resultList) {
		this.resultList = resultList;
	}

	public QueryPeopleRoleParam getQueryParam() {
		return queryParam;
	}

	public void setQueryParam(QueryPeopleRoleParam queryParam) {
		this.queryParam = queryParam;
	}

	public String getOrganizeInfo() {
		return organizeInfo;
	}

	public void setOrganizeInfo(String organizeInfo) {
		this.organizeInfo = organizeInfo;
	}

	public IQueryPeopleRoleBiz getQueryPeopleRoleBiz() {
		return queryPeopleRoleBiz;
	}

	public void setQueryPeopleRoleBiz(IQueryPeopleRoleBiz queryPeopleRoleBiz) {
		this.queryPeopleRoleBiz = queryPeopleRoleBiz;
	}

}
