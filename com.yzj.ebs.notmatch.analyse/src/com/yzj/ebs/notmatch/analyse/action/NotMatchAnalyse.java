package com.yzj.ebs.notmatch.analyse.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.yzj.ebs.common.IPublicTools;
import com.yzj.ebs.common.SimpleOrg;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.notmatch.analyse.biz.INotMatchAnalyseBiz;
import com.yzj.ebs.notmatch.analyse.pojo.AnalyseResult;
import com.yzj.ebs.notmatch.analyse.pojo.QueryParam;
import com.yzj.wf.am.security.common.AMSecurityDefine;
import com.yzj.wf.common.WFLogger;
import com.yzj.wf.core.model.po.wrapper.XPeopleInfo;
import com.yzj.wf.export.common.ExportEntity;
import com.yzj.wf.export.service.DataExporterImpl;

/**
 * 
 * 创建于:2012-11-12<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 未达账情况统计类
 * 
 * @author 单伟龙 施江敏
 * @version 1.0.0
 */
public class NotMatchAnalyse extends ActionSupport {

	private static final long serialVersionUID = 6774891577373350756L;

	private QueryParam queryParam;// 查询参数bean
	private List<AnalyseResult> resultList = new ArrayList<AnalyseResult>();// 查询结果集
	private List<AnalyseResult> exportList = new ArrayList<AnalyseResult>();// 导出结果集
	private AnalyseResult result;
	private String errMsg = "";
	private IPublicTools tools;// 工具类
	private String orgTree;
	private INotMatchAnalyseBiz notMatchAnalyseBiz;// 未达统计统计业务实现
	private static WFLogger logger = WFLogger.getLogger(NotMatchAnalyse.class);

	// 查询下拉框的值
	private String idCenter;
	private String idBranch;
	private String idBank;
	private String selectCount = "";

	/**
	 * 初始化界面
	 * 
	 * @return
	 */
	public String init() {
		errMsg = null;
		queryParam = new QueryParam();
		resultList = new ArrayList<AnalyseResult>();
		result = null;
		idCenter = "";
		idBranch = "";
		idBank = "";
		SimpleOrg org = null;
		selectCount = "countIdBank";
		HttpServletRequest request = ServletActionContext.getRequest();
		XPeopleInfo people = (XPeopleInfo) request.getSession().getAttribute(
				AMSecurityDefine.XPEOPLEINFO);// 获取当前登录柜员信息
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
	 * 统计数据
	 * 
	 * @return
	 * @throws IOException
	 * @throws XDocProcException
	 */
	public String analyse() throws IOException, XDocProcException {
		this.resultList = new ArrayList<AnalyseResult>();
		// 查询全量统计数据
		resultList = notMatchAnalyseBiz.getAnalyseResult(createQueryMap(),
				queryParam, true,selectCount);
		return "initSuccess";
	}

	/**
	 * 导出统计结果
	 * 
	 * @return
	 * @throws IOException
	 * @throws XDocProcException
	 */
	public String exportData() throws IOException, XDocProcException {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		exportList.clear();
		exportList = notMatchAnalyseBiz.getAnalyseResult(createQueryMap(),
				queryParam, false,selectCount);
		ExportEntity entity=new ExportEntity();
		entity.setFileName("未达账情况统计");
		entity.setTitle("未达账情况统计");
		entity.setDataList(exportList);
		
		Map<String,String> pro_desc=new LinkedHashMap<String, String>();
		pro_desc.put("idCenter", "分行");
		pro_desc.put("idBank", "网点");
		pro_desc.put("bankName", "网点名称");
		pro_desc.put("docDate", "对账日期");
		pro_desc.put("sendCount", "发出数");
		pro_desc.put("notMatchCount", "未达账数");
		pro_desc.put("checkMatchCount", "调平相符数");
		pro_desc.put("checkNotMatchCount", "核对不符数");
		pro_desc.put("notMatchPercent", "未达账率");
		pro_desc.put("tunePercent", "调平率");
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

	public Map<String, String> createQueryMap() {
		Map<String, String> queryMap = new TreeMap<String, String>();
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
		if (idBank1 != null && idBank1.length() > 0) {
			queryMap.put("idBank", idBank1);
		}
		if (idBranch != null && idBranch.length() > 0) {
			queryMap.put("idBranch", idBranch);
		}
		if (idCenter != null && idCenter.length() > 0) {
			queryMap.put("idCenter", idCenter);
		}

		String docDateTmp = queryParam.getDocDate();
		String docDate = docDateTmp.substring(0, 4)
				+ docDateTmp.substring(5, 7) + docDateTmp.substring(8);
		queryMap.put("docDate", docDate);
		return queryMap;
	}

	public QueryParam getQueryParam() {
		return queryParam;
	}

	public void setQueryParam(QueryParam queryParam) {
		this.queryParam = queryParam;
	}

	public AnalyseResult getResult() {
		return result;
	}

	public void setResult(AnalyseResult result) {
		this.result = result;
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

	public List<AnalyseResult> getResultList() {
		return resultList;
	}

	public void setResultList(List<AnalyseResult> resultList) {
		this.resultList = resultList;
	}

	public List<AnalyseResult> getExportList() {
		return exportList;
	}

	public void setExportList(List<AnalyseResult> exportList) {
		this.exportList = exportList;
	}

	public INotMatchAnalyseBiz getNotMatchAnalyseBiz() {
		return notMatchAnalyseBiz;
	}

	public void setNotMatchAnalyseBiz(INotMatchAnalyseBiz notMatchAnalyseBiz) {
		this.notMatchAnalyseBiz = notMatchAnalyseBiz;
	}

	public static WFLogger getLogger() {
		return logger;
	}

	public static void setLogger(WFLogger logger) {
		NotMatchAnalyse.logger = logger;
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
