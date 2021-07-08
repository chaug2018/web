package com.yzj.ebs.ebill.analyse.action;

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
import com.yzj.ebs.common.RefTableTools;
import com.yzj.ebs.common.SimpleOrg;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.ebill.analyse.biz.IEbillAnalyseBiz;
import com.yzj.ebs.ebill.analyse.pojo.AnalyseResult;
import com.yzj.ebs.ebill.analyse.pojo.QueryParam;
import com.yzj.wf.am.security.common.AMSecurityDefine;
import com.yzj.wf.common.WFLogger;
import com.yzj.wf.core.model.po.wrapper.XPeopleInfo;
import com.yzj.wf.export.common.ExportEntity;
import com.yzj.wf.export.service.DataExporterImpl;

/**
 * 
 * 创建于:2012-11-22<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 机构对账率统计
 * 
 * @author 施江敏 swl
 * @version 1.0.0
 */
public class EbillAnalyse extends ActionSupport {

	private static final long serialVersionUID = 5380188291736604821L;
	private static final java.text.DecimalFormat df = new java.text.DecimalFormat(
			"#.##");
	private QueryParam queryParam;
	private AnalyseResult result; // 统计结果类
	private List<AnalyseResult> resultList; // 统计结果类集合，为了前台显示方便
	private String errMsg = "";
	private IPublicTools tools;
	private String orgTree;
	private static WFLogger logger = WFLogger.getLogger(EbillAnalyse.class);
	private IEbillAnalyseBiz ebillAnalyseBizImpl;
	private Map<String, String> orgTypeMap = new TreeMap<String, String>();

	private static String ORGTYPE_BYIDBRANCH = "0";
	private static String ORGTYPE_BYIDBANK = "1";

	// 查询下拉框的值
	private String idCenter = "";
	private String idBranch = "";
	private String idBank = "";
	private String selectCount = "";
	
	private TreeMap<String, String> refAccCycleMap = new TreeMap<String, String>();
	private Map<String, String> refSendModeMap = new TreeMap<String, String>();

	/**
	 * 初始化界面
	 * 
	 */
	public String init() {
		errMsg = null;
		queryParam = new QueryParam();
		getOrgTypeMap();
		resultList = new ArrayList<AnalyseResult>();
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

		resultList = ebillAnalyseBizImpl.getEbillAnalyseList(queryMap,
				queryParam, true,selectCount);

		return "initSuccess";
	}

	/**
	 * 导出统计结果
	 * 
	 */
	public String exportData() throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		List<AnalyseResult> exportList = new ArrayList<AnalyseResult>();
		Map<String, String> queryMap = createQueryMap();
		//得到输出的全量数据
		exportList = ebillAnalyseBizImpl.getEbillAnalyseList(queryMap,
				queryParam, false,selectCount);
		ExportEntity entity=new ExportEntity();
		entity.setFileName("对账率统计");
		entity.setTitle("对账率统计");
		entity.setDataList(exportList);
		Map<String ,String> pro_desc=new LinkedHashMap<String,String>();
		
		pro_desc.put("idCenter", "分行");
		pro_desc.put("idBank", "网点");
		pro_desc.put("bankName", "网点名称");
		pro_desc.put("docDate", "对账日期");
		pro_desc.put("sendCount", "发出的账户数");
		pro_desc.put("backCount", "回收的账户数");
		pro_desc.put("backPercent", "回收率");
		pro_desc.put("retreatCount", "退信数");
		pro_desc.put("proveMatchCount", "验印成功的账户数");
		pro_desc.put("proveNotMatchCount", "验印不符的账户数");
		pro_desc.put("proveMatchPercent", "验印成功率");
		pro_desc.put("proveNotMatchPercent", "验印不符率");
		pro_desc.put("wjkCount", "未建库的账户数");
		pro_desc.put("checkSuccessCount", "余额相符的账户数");
		pro_desc.put("checkFailCount", "余额不符的账户数");
		pro_desc.put("ebillSuccessCount", "对账成功的账户数");
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
		String docDateTmp = queryParam.getDocDate();
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
		if(docDateTmp != null && docDateTmp.length()>0){
			String docDate = docDateTmp.substring(0, 4)
					+ docDateTmp.substring(5, 7) + docDateTmp.substring(8);
			queryMap.put("docDate", docDate);
		}
		if(accCycle!=null && accCycle.length()>0){
			queryMap.put("accCycle", accCycle);
		}
		if(sendMode!=null && sendMode.length()>0){
			queryMap.put("sendMode", sendMode);
		}
		
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
		EbillAnalyse.logger = logger;
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

	public Map<String, String> getOrgTypeMap() {
		orgTypeMap = new TreeMap<String, String>();
		orgTypeMap.put(ORGTYPE_BYIDBANK, "按网点");
		orgTypeMap.put(ORGTYPE_BYIDBRANCH, "按支行");

		return orgTypeMap;
	}

	public void setOrgTypeMap(Map<String, String> orgTypeMap) {
		this.orgTypeMap = orgTypeMap;
	}

	public IEbillAnalyseBiz getEbillAnalyseBizImpl() {
		return ebillAnalyseBizImpl;
	}

	public void setEbillAnalyseBizImpl(IEbillAnalyseBiz ebillAnalyseBizImpl) {
		this.ebillAnalyseBizImpl = ebillAnalyseBizImpl;
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
}
