package com.yzj.ebs.report.action;

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
import com.yzj.ebs.report.biz.ICoverReportBiz;
import com.yzj.ebs.report.pojo.CoverQueryParam;
import com.yzj.ebs.report.pojo.CoverResult;
import com.yzj.wf.am.security.common.AMSecurityDefine;
import com.yzj.wf.common.WFLogger;
import com.yzj.wf.core.model.po.wrapper.XPeopleInfo;
import com.yzj.wf.export.common.ExportEntity;
import com.yzj.wf.export.service.DataExporterImpl;

/**
 * 
 * 创建于:2015-10-17<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 覆盖率统计
 * 
 * @author chenzg
 * @version 1.0.0
 */
public class CoverReportAction extends ActionSupport {

	private static final long serialVersionUID = 1L;
	private static final java.text.DecimalFormat df = new java.text.DecimalFormat("#.##");
	private CoverQueryParam queryParam;
	private CoverResult result; // 统计结果类
	private List<CoverResult> resultList; // 统计结果类集合，为了前台显示方便
	private String errMsg = "";
	private IPublicTools tools;
	private String orgTree;
	private static WFLogger logger = WFLogger.getLogger(CoverReportAction.class);
	private ICoverReportBiz coverReportBizImpl;

	// 查询下拉框的值
	private String idCenter = "";
	private String idBranch = "";
	private String idBank = "";
	private String selectCount = "";
	
	private TreeMap<String, String> refAccCycleMap = new TreeMap<String, String>();
	private Map<String, String> refSendModeMap = new TreeMap<String, String>();

	private String isThree;//选择日期区间是否是三个月  1:是  0:不是
	/**
	 * 初始化界面
	 * 
	 */
	public String init() {
		errMsg = null;
		queryParam = new CoverQueryParam();
		resultList = new ArrayList<CoverResult>();
		result = null;
		SimpleOrg org = null;
		idCenter = "";
		idBranch = "";
		idBank = "";
		selectCount = "countIdBank";
		isThree="0";
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

		resultList = coverReportBizImpl.getCoverReportList(queryMap,
				queryParam, true,selectCount,isThree);

		return "initSuccess";
	}

	/**
	 * 导出统计结果
	 * 
	 */
	public String exportData() throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		List<CoverResult> exportList = new ArrayList<CoverResult>();
		Map<String, String> queryMap = createQueryMap();
		//得到输出的全量数据
		exportList = coverReportBizImpl.getCoverReportList(queryMap,
				queryParam, false,selectCount,isThree);
		ExportEntity entity=new ExportEntity();
		entity.setFileName("覆盖率统计");
		entity.setTitle("覆盖率统计");
		entity.setDataList(exportList);
		Map<String ,String> pro_desc=new LinkedHashMap<String,String>();
		
		pro_desc.put("idCenter", "分行");
		pro_desc.put("idBank", "网点");
		pro_desc.put("bankName", "网点名称");
		pro_desc.put("sendCount", "发出的账户数");
		
		pro_desc.put("successCount", "有过成功对账数");
		pro_desc.put("failCount", "都未成功对账数");
		if("1".equals(isThree)){
			pro_desc.put("oneFailCount", "一次未成功对账数");
			pro_desc.put("twoFailCount", "两次未成功对账数");
			pro_desc.put("threeFailCount", "三次未成功对账数");
		}
		pro_desc.put("coverPercent", "覆盖率");
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
		
//		if(beginDocDateTmp != null && beginDocDateTmp.length()>0){
//			String docDate = beginDocDateTmp.substring(0, 4)
//					+ beginDocDateTmp.substring(5, 7) + beginDocDateTmp.substring(8);
//			queryMap.put("docDate", docDate);
//		}
		
		String beginDocDate = beginDocDateTmp.substring(0, 4)
				+ beginDocDateTmp.substring(5, 7) + beginDocDateTmp.substring(8);
		String endDocDate = endDocDateTmp.substring(0, 4)
				+ endDocDateTmp.substring(5, 7) + endDocDateTmp.substring(8);
		
		int monthSum = Integer.parseInt(endDocDateTmp.substring(5, 7)) - 
							Integer.parseInt(beginDocDateTmp.substring(5, 7));
		isThree = (monthSum==2||monthSum==-10)?"1":"0";
		queryMap.put("docDate", "a.docDate >='"+beginDocDate+"' and a.docDate <='"+endDocDate+"'");
		
		if(accCycle!=null && accCycle.length()>0){
			queryMap.put("accCycle", accCycle);
		}
		if(sendMode!=null && sendMode.length()>0){
			queryMap.put("sendMode", sendMode);
		}
		
		return queryMap;
	}

	public CoverQueryParam getQueryParam() {
		return queryParam;
	}

	public void setQueryParam(CoverQueryParam queryParam) {
		this.queryParam = queryParam;
	}

	public CoverResult getResult() {
		return result;
	}

	public void setResult(CoverResult result) {
		this.result = result;
	}

	public List<CoverResult> getResultList() {
		return resultList;
	}

	public void setResultList(List<CoverResult> resultList) {
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
		CoverReportAction.logger = logger;
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

	public ICoverReportBiz getCoverReportBizImpl() {
		return coverReportBizImpl;
	}

	public void setCoverReportBizImpl(ICoverReportBiz coverReportBizImpl) {
		this.coverReportBizImpl = coverReportBizImpl;
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

	public String getIsThree() {
		return isThree;
	}

	public void setIsThree(String isThree) {
		this.isThree = isThree;
	}

	
}
