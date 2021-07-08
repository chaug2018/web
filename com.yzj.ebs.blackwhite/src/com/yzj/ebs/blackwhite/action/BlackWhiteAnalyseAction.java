package com.yzj.ebs.blackwhite.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.yzj.ebs.blackwhite.biz.IBlackWhiteBiz;
import com.yzj.ebs.blackwhite.queryparam.BlackWhiteResult;
import com.yzj.ebs.blackwhite.queryparam.QueryParam;
import com.yzj.ebs.common.IBasicInfoAdm;
import com.yzj.ebs.common.IPublicTools;
import com.yzj.ebs.common.SimpleOrg;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.wf.am.security.common.AMSecurityDefine;
import com.yzj.wf.common.WFLogger;
import com.yzj.wf.core.model.po.wrapper.XPeopleInfo;
import com.yzj.wf.export.common.ExportEntity;
import com.yzj.wf.export.service.DataExporterImpl;

/**
 * 
 * 创建于:2013-01-15<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 特殊账户维护统计
 * 
 * @author jiangzhengqiu
 * @version 1.0.0
 */
public class BlackWhiteAnalyseAction extends ActionSupport {
	private static WFLogger logger = WFLogger
			.getLogger(BlackWhiteAnalyseAction.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 8387168546702069817L;
	private IBasicInfoAdm basicinfoAdm;
	// 查询下拉框的值
	private String idCenter = "";
	private String idBranch = "";
	private String idBank = "";
	private QueryParam queryParam;
	List<BlackWhiteResult> resultList;
	private String errMsg = "";
	private IPublicTools tools;
	private String orgTree;
	private IBlackWhiteBiz blackWhiteBizImpl;
	private String selectCount = "";

	/**
	 * 初始化界面
	 * 
	 * @return
	 */
	public String init() {
		idCenter = "";
		idBranch = "";
		idBank = "";
		errMsg = null;
		queryParam = new QueryParam();
		resultList = new ArrayList<BlackWhiteResult>();
		SimpleOrg org = null;
		selectCount = "countIdBank";
		HttpServletRequest request = ServletActionContext.getRequest();
		XPeopleInfo people = (XPeopleInfo) request.getSession().getAttribute(
				AMSecurityDefine.XPEOPLEINFO);
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

	/**
	 * 逐笔维护特殊账户，更新BasicInfo及日志
	 * 
	 * @return
	 * @throws IOException
	 */
	public String analyse() throws IOException {
		resultList = new ArrayList<BlackWhiteResult>();
		try {
			Map<String, String> queryMap = new HashMap<String, String>();
			String idBank = queryParam.getIdBank();
			String idBank1 = queryParam.getIdBank1();
			String idCenter = queryParam.getIdCenter();
			String idBranch = queryParam.getIdBranch();
			this.idCenter = idCenter;
			this.idBranch = idBranch;
			this.idBank = idBank;
			if (idBank != null && idBank.length() > 0) {
				queryMap.put("idBank", idBank);
			}
			if (idBranch != null && idBranch.length() > 0) {
				queryMap.put("idBranch", idBranch);
			}
			if (idCenter != null && idCenter.length() > 0) {
				queryMap.put("idCenter", idCenter);
			}
			if (idBank1 != null && idBank1.length() > 0) {
				queryMap.put("idBank", idBank1);
			}
			resultList = blackWhiteBizImpl.getAnalyseResult(queryParam,
					queryMap, true, selectCount);
		} catch (XDocProcException e) {
			e.printStackTrace();
		}
		return "initSuccess";
	}

	/**
	 * 导出数据
	 * 
	 * @return
	 * @throws IOException
	 * @throws XDocProcException
	 */
	public String exportData() throws IOException, XDocProcException {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");

		List<BlackWhiteResult> exportList = new ArrayList<BlackWhiteResult>();

		Map<String, String> queryMap = new HashMap<String, String>();
		String idBank = queryParam.getIdBank();
		String idBank1 = queryParam.getIdBank1();
		String idCenter = queryParam.getIdCenter();
		String idBranch = queryParam.getIdBranch();
		this.idCenter = idCenter;
		this.idBranch = idBranch;
		this.idBank = idBank;
		if (idBank != null && idBank.length() > 0) {
			queryMap.put("idBank", idBank);
		}
		if (idBranch != null && idBranch.length() > 0) {
			queryMap.put("idBranch", idBranch);
		}
		if (idCenter != null && idCenter.length() > 0) {
			queryMap.put("idCenter", idCenter);
		}
		if (idBank1 != null && idBank1.length() > 0) {
			queryMap.put("idBank", idBank1);
		}
		exportList = blackWhiteBizImpl.getAnalyseResult(queryParam, queryMap,
				false,selectCount);

		ExportEntity entity=new ExportEntity();
		entity.setFileName("特殊帐户统计列表");
		entity.setTitle("特殊帐户统计列表");
		entity.setDataList(exportList);
		Map<String,String> pro_desc=new LinkedHashMap<String, String>();
		pro_desc.put("idCenter", "分行");
		pro_desc.put("idBank", "网点");
		pro_desc.put("bankName", "网点名称");
		pro_desc.put("blackCount", "不对账数");
		pro_desc.put("whiteCount", "对账数");
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

	public IBasicInfoAdm getBasicinfoAdm() {
		return basicinfoAdm;
	}

	public void setBasicinfoAdm(IBasicInfoAdm basicinfoAdm) {
		this.basicinfoAdm = basicinfoAdm;
	}

	public String getIdCenter() {
		return idCenter;
	}

	public void setIdCenter(String idCenter) {
		this.idCenter = idCenter;
	}

	public String getIdBranch() {
		return idBranch;
	}

	public void setIdBranch(String idBranch) {
		this.idBranch = idBranch;
	}

	public String getIdBank() {
		return idBank;
	}

	public void setIdBank(String idBank) {
		this.idBank = idBank;
	}

	public QueryParam getQueryParam() {
		return queryParam;
	}

	public void setQueryParam(QueryParam queryParam) {
		this.queryParam = queryParam;
	}

	public List<BlackWhiteResult> getResultList() {
		return resultList;
	}

	public void setResultList(List<BlackWhiteResult> resultList) {
		this.resultList = resultList;
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
		BlackWhiteAnalyseAction.logger = logger;
	}

	public IBlackWhiteBiz getBlackWhiteBizImpl() {
		return blackWhiteBizImpl;
	}

	public void setBlackWhiteBizImpl(IBlackWhiteBiz blackWhiteBizImpl) {
		this.blackWhiteBizImpl = blackWhiteBizImpl;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getSelectCount() {
		return selectCount;
	}

	public void setSelectCount(String selectCount) {
		this.selectCount = selectCount;
	}
	
}
