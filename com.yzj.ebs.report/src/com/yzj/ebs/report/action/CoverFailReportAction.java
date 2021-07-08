package com.yzj.ebs.report.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
import com.yzj.ebs.common.RefTableTools;
import com.yzj.ebs.common.SimpleOrg;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.report.biz.ICoverFailReportBiz;
import com.yzj.ebs.report.pojo.CoverFailQueryParam;
import com.yzj.ebs.report.pojo.CoverFailResult;
import com.yzj.wf.am.security.common.AMSecurityDefine;
import com.yzj.wf.common.WFLogger;
import com.yzj.wf.core.model.po.wrapper.XPeopleInfo;
import com.yzj.wf.export.common.ExportEntity;
import com.yzj.wf.export.service.DataExporterImpl;

/**
 * 
 * 创建于:2015-10-17<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 连续对账未成功账户明细
 * 
 * @author chenzg
 * @version 1.0.0
 */
public class CoverFailReportAction extends ActionSupport {

	private static final long serialVersionUID = 1L;
	private static final java.text.DecimalFormat df = new java.text.DecimalFormat("#.##");
	private CoverFailQueryParam queryParam;
	private CoverFailResult result; // 统计结果类
	private List<CoverFailResult> resultList; // 统计结果类集合，为了前台显示方便
	private String errMsg = "";
	private IPublicTools tools;
	private String orgTree;
	private static WFLogger logger = WFLogger.getLogger(CoverFailReportAction.class);
	private ICoverFailReportBiz coverFailReportBizImpl;

	// 查询下拉框的值
	private String idCenter = "";
	private String idBranch = "";
	private String idBank = "";
	private String selectCount = "";
	
	private TreeMap<String, String> refAccCycleMap = new TreeMap<String, String>();
	private Map<String, String> refSendModeMap = new TreeMap<String, String>();

	private TreeMap<String, String> refCheckflagMap = new TreeMap<String, String>();
	private TreeMap<String, String> refDocStateMap = new TreeMap<String, String>();
	private TreeMap<String, String> refAccStateMap = new TreeMap<String, String>();
	private Map<String, String> failCountMap = new TreeMap<String, String>();//未成功次数
	
	public String init() {
		errMsg = null;
		queryParam = new CoverFailQueryParam();
		resultList = new ArrayList<CoverFailResult>();
		result = null;
		SimpleOrg org = null;
		idCenter = "";
		idBranch = "";
		idBank = "";
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
	 * 统计数据
	 * 
	 */
	public String analyse() throws IOException {
		resultList.clear();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		idCenter = queryParam.getIdCenter();
		idBranch = queryParam.getIdBranch();
		idBank = queryParam.getIdBank();
		Map<String, String> queryMap = createQueryMap();

		resultList = coverFailReportBizImpl.getCoverFailReportList(queryMap,
				queryParam, true);

		return "initSuccess";
	}

	/**
	 * 导出统计结果
	 * 
	 */
	public String exportData() throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		List<CoverFailResult> exportList = new ArrayList<CoverFailResult>();
		Map<String, String> queryMap = createQueryMap();
		//得到输出的全量数据
		exportList = coverFailReportBizImpl.getCoverFailReportList(queryMap,
				queryParam, false);
		ExportEntity entity=new ExportEntity();
		entity.setFileName("连续对账未成功账户明细");
		entity.setTitle("连续对账未成功账户明细");
		entity.setDataList(exportList);
		Map<String,Map<String,String>> paramsMap=new HashMap<String,Map<String,String>>();
		Map<String ,String> pro_desc=new LinkedHashMap<String,String>();
		
		pro_desc.put("idCenter", "分行");
		pro_desc.put("idBank", "网点");
		pro_desc.put("bankName", "网点名称");
		pro_desc.put("accno", "账号");
		pro_desc.put("accname", "户名");
		pro_desc.put("docState", "账单状态");
		pro_desc.put("docdate", "对账日期");
		pro_desc.put("checkFlag", "对账结果");
		pro_desc.put("sendmode", "发送方式");
		pro_desc.put("accState", "账户状态");
		pro_desc.put("notCheckCount", "未对账次数");
		
		paramsMap.put("docState", refDocStateMap);
		//paramsMap.put("checkFlag", refCheckflagMap);
		paramsMap.put("sendmode", refSendModeMap);
		paramsMap.put("accState", refAccStateMap);
		
		entity.setPro_desc_map(pro_desc);
		entity.setParamsMap(paramsMap);
		try {
			new DataExporterImpl().export(entity);
			return null;
		} catch (Throwable e) {
			logger.error("导出列表失败：" + e.getMessage());
			response.getWriter().write("导出列表失败：" + e.getMessage());
		}

		return null;
	}

	/**
	 * 生成查询Map
	 * 
	 * @return
	 */
	public Map<String, String> createQueryMap() {
		Map<String, String> queryMap = new TreeMap<String, String>();
		String idBank = queryParam.getIdBank();
		String idBank1 = queryParam.getIdBank1();
		String idCenter = queryParam.getIdCenter();
		String idBranch = queryParam.getIdBranch();
		String beginDocDateTmp = queryParam.getBeginDocDate();
		String endDocDateTmp = queryParam.getEndDocDate();
		
		String accCycle = queryParam.getAccCycle();
		String sendMode = queryParam.getSendMode();
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
		
		String beginDocDate = beginDocDateTmp.substring(0, 4)
				+ beginDocDateTmp.substring(5, 7) + beginDocDateTmp.substring(8);
		String endDocDate = endDocDateTmp.substring(0, 4)
				+ endDocDateTmp.substring(5, 7) + endDocDateTmp.substring(8);
		
		
		queryMap.put("docDate", "a.docDate >='"+beginDocDate+"' and a.docDate <='"+endDocDate+"'");
		
		if(accCycle!=null && accCycle.length()>0){
			queryMap.put("accCycle", accCycle);
		}
		if(sendMode!=null && sendMode.length()>0){
			queryMap.put("sendMode", sendMode);
		}
		String failCount = queryParam.getFailCount();
		if (failCount!=null&&failCount!="") {
			queryMap.put("failCount", failCount);
		}
		return queryMap;
	}

	

	public CoverFailQueryParam getQueryParam() {
		return queryParam;
	}

	public void setQueryParam(CoverFailQueryParam queryParam) {
		this.queryParam = queryParam;
	}

	public CoverFailResult getResult() {
		return result;
	}

	public void setResult(CoverFailResult result) {
		this.result = result;
	}

	public List<CoverFailResult> getResultList() {
		return resultList;
	}

	public void setResultList(List<CoverFailResult> resultList) {
		this.resultList = resultList;
	}

	public ICoverFailReportBiz getCoverFailReportBizImpl() {
		return coverFailReportBizImpl;
	}

	public void setCoverFailReportBizImpl(ICoverFailReportBiz coverFailReportBizImpl) {
		this.coverFailReportBizImpl = coverFailReportBizImpl;
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
		CoverFailReportAction.logger = logger;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
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

	public static java.text.DecimalFormat getDf() {
		return df;
	}

	public String getSelectCount() {
		return selectCount;
	}

	public void setSelectCount(String selectCount) {
		this.selectCount = selectCount;
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
	
	public TreeMap<String, String> getRefAccCycleMap() {
		if (null == refAccCycleMap || refAccCycleMap.size() == 0) {
			refAccCycleMap = RefTableTools.ValRefAccCycleMap;
		}
		return refAccCycleMap;
	}

	public void setRefAccCycleMap(TreeMap<String, String> refAccCycleMap) {
		this.refAccCycleMap = refAccCycleMap;
	}
	
	public Map<String, String> getRefCheckflagMap() {
		if (null == refCheckflagMap || refCheckflagMap.size() == 0) {
			refCheckflagMap = RefTableTools.ValRefCheckflagMap;
		}
		return refCheckflagMap;
	}

	public void setRefCheckflagMap(TreeMap<String, String> refCheckflagMap) {
		this.refCheckflagMap = refCheckflagMap;
	}
	
	public TreeMap<String, String> getRefDocStateMap() {
		if (null == refDocStateMap || refDocStateMap.size() == 0) {
			refDocStateMap = RefTableTools.ValRefDocstateMap;
		}
		return refDocStateMap;
	}

	public void setRefDocStateMap(TreeMap<String, String> refDocStateMap) {
		this.refDocStateMap = refDocStateMap;
	}

	public TreeMap<String, String> getRefAccStateMap() {
		if (null == refAccStateMap || refAccStateMap.size() == 0) {
			refAccStateMap.put("0","正常");
			refAccStateMap.put("1","销户");
			refAccStateMap.put("2","长期不动户");
			refAccStateMap.put("3","不动转收益");
		}
		return refAccStateMap;
	}

	public void setRefAccStateMap(TreeMap<String, String> refAccStateMap) {
		this.refAccStateMap = refAccStateMap;
	}
	
	public Map<String, String> getFailCountMap() {
		if (null == failCountMap || failCountMap.size() == 0) {
			failCountMap.put("1", "1");
			failCountMap.put("2", "2");
			failCountMap.put("3", "3");
			failCountMap.put("4", "4");
			failCountMap.put("5", "5");
			failCountMap.put("6", "6");
//			failCountMap.put("7", "7");
//			failCountMap.put("8", "8");
//			failCountMap.put("9", "9");
//			failCountMap.put("10", "10");
//			failCountMap.put("11", "11");
//			failCountMap.put("12", "12");
		}
		return failCountMap;
	}

	public void setFailCountMap(Map<String, String> failCountMap) {
		this.failCountMap = failCountMap;
	}
}
