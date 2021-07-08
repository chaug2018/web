package com.yzj.ebs.partebill.action;

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
import com.yzj.ebs.partebill.biz.IPartEbillAnalyseBiz;
import com.yzj.ebs.partebill.param.QueryParam;
import com.yzj.ebs.partebill.param.AnalyseResult;
import com.yzj.wf.common.WFLogger;
import com.yzj.wf.core.model.po.wrapper.XPeopleInfo;
import com.yzj.wf.export.common.ExportEntity;
import com.yzj.wf.export.service.DataExporterImpl;

/**
 * 创建于:2013-7-22<br>
 * 增量对账情况统计action
 * @param 
 * @author jw
 * @version 1.0.0
 */
public class PartEbillAnalyseAction extends ActionSupport {

	private static final long serialVersionUID = 3810537058968630574L;
	private QueryParam queryParam;
	private AnalyseResult result;
	private List<AnalyseResult> resultList;
	private List<String> showList;
	private String errMsg = "";
	private IPublicTools tools;
	private String orgTree;
	private static WFLogger logger = WFLogger.getLogger(PartEbillAnalyseAction.class);

	private IPartEbillAnalyseBiz partEbillAnalyseBizImpl;

	private Map<String, String> refAccTypeMap = new TreeMap<String, String>();
	private Map<String, String> refSendModeMap = new TreeMap<String, String>();
	// 查询下拉框的值
	private String idCenter = "";
	private String idBranch = "";
	private String idBank = "";
	private ArrayList<AnalyseResult> exportList;

	/*
	 * 初始化界面
	 */
	public String init() throws XDocProcException {
		errMsg = null;
		queryParam = new QueryParam();
		resultList = new ArrayList<AnalyseResult>();
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

	/*
	 * 
	 * 查询数据
	 */
	public String analyse() throws XDocProcException {
		this.resultList = new ArrayList<AnalyseResult>();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		resultList = this.queryData(true);
		return "initSuccess";
	}

	/*
	 * 导出数据
	 */
	public String exportData() throws IOException, XDocProcException {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		List<AnalyseResult> exportList;
		this.exportList= new ArrayList<AnalyseResult>();		
			exportList = this.queryData(false);
			ExportEntity entity=new ExportEntity();
			entity.setFileName("增量对账率统计");
			entity.setTitle("增量对账率统计");
			entity.setDataList(exportList);
			Map<String ,String> pro_desc=new LinkedHashMap<String,String>();
			
			pro_desc.put("idBank", "机构号");
			pro_desc.put("bankName", "机构名称");
			pro_desc.put("docDate", "对账日期");
			pro_desc.put("sendCount", "对账单发出数");
			pro_desc.put("backCount", "回收数");
			pro_desc.put("backPercent", "回收率");
			pro_desc.put("retreatCount", "退信数");
			pro_desc.put("proveMatchCount", "验印成功数");
			pro_desc.put("proveNotMatchCount", "验印不符数");
			pro_desc.put("proveMatchPercent", "验印成功率");
			pro_desc.put("proveNotMatchPercent", "验印不符率");
			pro_desc.put("wjkCount", "未建库数");
			pro_desc.put("checkSuccessCount", "余额相符数");
			pro_desc.put("checkFailCount", "余额不符数");
			pro_desc.put("ebillSuccessCount", "对账成功数");
			pro_desc.put("checkSuccessPercent", "对账成功率");
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
	 * 解析查询结果
	 */
	private List<AnalyseResult> queryData(boolean isPaged) throws XDocProcException {
		this.resultList = new ArrayList<AnalyseResult>();
		String docDateStart = queryParam.getDocDateStart();
		String docDateEnd = queryParam.getDocDateEnd();
		Map<String, String> queryMap = new HashMap<String, String>();
		queryMap = createMap();
		// true为按条件分页查询，否则为查询所有（导出时）
		if (isPaged) {
			return partEbillAnalyseBizImpl.getPartEbillAnalyseResult(queryMap,
					(PageParam) queryParam, docDateStart,docDateEnd,true);
		} else {
			return partEbillAnalyseBizImpl.getPartEbillAnalyseResult(queryMap,
					(PageParam) queryParam, docDateStart,docDateEnd,false);
		}
	}

	/*
	 * 构造查询Map
	 */
	private Map<String, String> createMap() {
		Map<String, String> queryMap = new HashMap<String, String>();
		String idBank = queryParam.getIdBank();
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

	public ArrayList<AnalyseResult> getExportList() {
		return exportList;
	}

	public void setExportList(ArrayList<AnalyseResult> exportList) {
		this.exportList = exportList;
	}

	public Map<String, String> getRefAccTypeMap() {
		if (null == refAccTypeMap || refAccTypeMap.size() == 0) {
			refAccTypeMap = RefTableTools.ValRefAcctypeMap;
		}
		return refAccTypeMap;
	}

	public void setRefAccTypeMap(Map<String, String> refAccTypeMap) {
		this.refAccTypeMap = refAccTypeMap;
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

	public List<AnalyseResult> getResultList() {
		return resultList;
	}

	public void setResultList(List<AnalyseResult> resultList) {
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
		PartEbillAnalyseAction.logger = logger;
	}
	
	
	public IPartEbillAnalyseBiz getPartEbillAnalyseBizImpl() {
		return partEbillAnalyseBizImpl;
	}

	public void setPartEbillAnalyseBizImpl(
			IPartEbillAnalyseBiz partEbillAnalyseBizImpl) {
		this.partEbillAnalyseBizImpl = partEbillAnalyseBizImpl;
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
	
	public List<String> getShowList() {
		return showList;
	}

	public void setShowList(List<String> showList) {
		this.showList = showList;
	}
	
}
