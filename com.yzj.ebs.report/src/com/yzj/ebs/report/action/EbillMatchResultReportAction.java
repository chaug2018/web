package com.yzj.ebs.report.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.yzj.ebs.common.IPublicTools;
import com.yzj.ebs.common.RefTableTools;
import com.yzj.ebs.common.SimpleOrg;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.report.biz.IEbillMatchResultReportBiz;
import com.yzj.ebs.report.pojo.EbillMatchResultQueryParam;
import com.yzj.ebs.report.pojo.EbillMatchResultResult;
import com.yzj.ebs.util.UtilBase;
import com.yzj.wf.common.WFLogger;
import com.yzj.wf.core.model.po.wrapper.XPeopleInfo;
import com.yzj.wf.export.common.ExportEntity;
import com.yzj.wf.export.service.DataExporterImpl;

/**
 * 账户有效对账结果展示action
 * @param 
 * @author chenzg
 * @version 1.0.0
 */
public class EbillMatchResultReportAction extends ActionSupport {

	private static final long serialVersionUID = 1L;
	private static final java.text.DecimalFormat df = new java.text.DecimalFormat("#.##");
	
	private EbillMatchResultQueryParam queryParam;
	private EbillMatchResultResult result;
	private List<EbillMatchResultResult> resultList;
	
	private String errMsg = "";
	private IPublicTools tools;
	private String orgTree;
	private static WFLogger logger = WFLogger
			.getLogger(EbillMatchResultReportAction.class);

	private IEbillMatchResultReportBiz EbillMatchResultReportBizImpl;

	private Map<String, String> refAccTypeMap = new TreeMap<String, String>();
	private Map<String, String> refSendModeMap = new TreeMap<String, String>();
	private Map<String, String> refCheckFlagMap = new TreeMap<String, String>();
	private Map<String, String> refProveFlagMap = new TreeMap<String, String>();
	private Map<String, String> succFlagMap = new TreeMap<String, String>();//成功与否标志
	
	// 查询下拉框的值
	private String idCenter = "";
	private String idBranch = "";
	private String idBank = "";
	private ArrayList<EbillMatchResultResult> exportList;

	/*
	 * 初始化界面
	 */
	public String init() throws XDocProcException {
		errMsg = null;
		queryParam = new EbillMatchResultQueryParam();
		resultList = new ArrayList<EbillMatchResultResult>();
		
		result = null;
		SimpleOrg org = null;
		idCenter = "";
		idBranch = "";
		idBank = "";
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

	
	public String analyse() throws XDocProcException {
		this.resultList = new ArrayList<EbillMatchResultResult>();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		
		resultList = this.queryData(true);
		return "initSuccess";
	}

	/**
	 * 导出数据
	 */

	public String exportData() throws IOException, XDocProcException {

		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		List<EbillMatchResultResult> exportList;
		this.exportList = new ArrayList<EbillMatchResultResult>();

		exportList = this.queryData(false);
		ExportEntity entity=new ExportEntity();
		entity.setFileName("账户有效对账结果展示");
		entity.setTitle("账户有效对账结果展示");
		entity.setDataList(exportList);
		Map<String,String> pro_desc=new LinkedHashMap<String,String>();
		

		String docDateTmp = queryParam.getDocDate();
		String docDate = docDateTmp.substring(0, 4).trim()
				+ docDateTmp.substring(5, 7).trim()
				+ docDateTmp.substring(8).trim();
		UtilBase u = new UtilBase();
		//根据当前日期，往前推半年
		String docDateStr = docDate + "对账情况";
		for (int i = 5; i > 0; i--) {
			docDate = u.getMothLastDate(docDate);
			docDateStr = docDate + "对账情况" + "," + docDateStr;
		}
		
		String tableHeaderCN = "机构号,机构名称,账号,子账号,账户名,";
		tableHeaderCN = tableHeaderCN + docDateStr;
		String[] tableHanderName=tableHeaderCN.split(",");
		pro_desc.put("idCenter", "分行");
		pro_desc.put("idBank", "网点");
		pro_desc.put("bankName", "网点名称");
		pro_desc.put("accNo", "账号");
		pro_desc.put("accName", "账户名");
		pro_desc.put("MatchCount1", tableHanderName[5]);
		pro_desc.put("MatchCount2", tableHanderName[6]);
		pro_desc.put("MatchCount3", tableHanderName[7]);
		pro_desc.put("MatchCount4", tableHanderName[8]);
		pro_desc.put("MatchCount5", tableHanderName[9]);
		pro_desc.put("MatchCount6", tableHanderName[10]);
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

	/*
	 * 查询解析数据
	 */
	private List<EbillMatchResultResult> queryData(boolean isPaged) {
		this.resultList = new ArrayList<EbillMatchResultResult>();
		
		//true为分页查询
		if (isPaged) {
			return EbillMatchResultReportBizImpl.getEbillMatchResult(createMap(),
					(PageParam) queryParam,true);
		}else{
			return EbillMatchResultReportBizImpl.getEbillMatchResult(createMap(),
					(PageParam) queryParam,false);
		}
		
	}

	// 创建查询条件
	private Map<String, String> createMap() {
		Map<String, String> queryMap = new HashMap<String, String>();
		String idBank = queryParam.getIdBank();
		String idCenter = queryParam.getIdCenter();
		String idBranch = queryParam.getIdBranch();
		String idBank1 = queryParam.getIdBank1();

		this.idCenter = idCenter;
		this.idBranch = idBranch;
		this.idBank = idBank;
		if (idBank != null && idBank.length() > 0) {
			queryMap.put("idBank", idBank);
		}
		if (idBank1 != null && idBank1.trim().length() > 0) {
			queryMap.put("idBank", idBank1.trim());
		}
		if (idBranch != null && idBranch.length() > 0) {
			queryMap.put("idBranch", idBranch);
		}
		if (idCenter != null && idCenter.length() > 0) {
			queryMap.put("idCenter", idCenter);
		}

		String subjectNo = queryParam.getAccType();
		if (subjectNo!=null&&subjectNo!="") {
			queryMap.put("subjectNo", subjectNo);
		}
		String sendMode = queryParam.getSendMode();
		if(sendMode!=null&&sendMode!=""){
			queryMap.put("sendMode", sendMode);
		}
		String accno = queryParam.getAccno();
		if (accno!=null&&accno.trim()!="") {
			queryMap.put("accno", accno.trim());
		}
		String succFlag = queryParam.getSuccFlag();
		if (succFlag!=null&&succFlag!="") {
			queryMap.put("succFlag", succFlag);
		}

		String docdateTmp = queryParam.getDocDate();
		String docdate = docdateTmp.substring(0, 4)
				+ docdateTmp.substring(5, 7) + docdateTmp.substring(8);
		queryMap.put("docdate", docdate);
				
		return queryMap;
	}

	public Map<String, String> getRefSendModeMap() {
		if (null == refSendModeMap || refSendModeMap.size() == 0) {
			refSendModeMap = RefTableTools.ValRefSendModeMap;
		}
		return refSendModeMap;
	}

	public void setRefSendModeMap(Map<String, String> refSendModeMap) {
		this.refSendModeMap = refSendModeMap;
	}
	
	public Map<String, String> getRefCheckFlagMap() {
		if (null == refCheckFlagMap || refCheckFlagMap.size() == 0) {
			refCheckFlagMap = RefTableTools.ValRefCheckflagMap;
		}
		return refCheckFlagMap;
	}


	public void setRefCheckFlagMap(Map<String, String> refCheckFlagMap) {
		this.refCheckFlagMap = refCheckFlagMap;
	}


	public Map<String, String> getRefProveFlagMap() {
		if (null == refProveFlagMap || refProveFlagMap.size() == 0) {
			refProveFlagMap = RefTableTools.ValRefProveflagMap;
		}
		return refProveFlagMap;
	}


	public void setRefProveFlagMap(Map<String, String> refProveFlagMap) {
		this.refProveFlagMap = refProveFlagMap;
	}


	public Map<String, String> getSuccFlagMap() {
		if (null == succFlagMap || succFlagMap.size() == 0) {
			succFlagMap.put("1", "成功");
			succFlagMap.put("0", "失败");
		}
		return succFlagMap;
	}
	
	public void setSuccFlagMap(Map<String, String> succFlagMap) {
		this.succFlagMap = succFlagMap;
	}

	public EbillMatchResultQueryParam getQueryParam() {
		return queryParam;
	}

	public void setQueryParam(EbillMatchResultQueryParam queryParam) {
		this.queryParam = queryParam;
	}

	public EbillMatchResultResult getResult() {
		return result;
	}

	public void setResult(EbillMatchResultResult result) {
		this.result = result;
	}

	public List<EbillMatchResultResult> getResultList() {
		return resultList;
	}

	public void setResultList(List<EbillMatchResultResult> resultList) {
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
		EbillMatchResultReportAction.logger = logger;
	}

	public IEbillMatchResultReportBiz getEbillMatchResultReportBizImpl() {
		return EbillMatchResultReportBizImpl;
	}

	public void setEbillMatchResultReportBizImpl(
			IEbillMatchResultReportBiz EbillMatchResultReportBizImpl) {
		this.EbillMatchResultReportBizImpl = EbillMatchResultReportBizImpl;
	}

	public Map<String, String> getRefAccTypeMap() {
		if (null == refAccTypeMap || refAccTypeMap.size() == 0) {
			refAccTypeMap = RefTableTools.ValParamSubnocMap;
		}
		return refAccTypeMap;
	}

	public void setRefAccTypeMap(Map<String, String> refAccTypeMap) {
		this.refAccTypeMap = refAccTypeMap;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static java.text.DecimalFormat getDf() {
		return df;
	}

	public ArrayList<EbillMatchResultResult> getExportList() {
		return exportList;
	}

	public void setExportList(ArrayList<EbillMatchResultResult> exportList) {
		this.exportList = exportList;
	}

}
