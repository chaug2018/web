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
import com.yzj.ebs.report.biz.IEbillMatchReportBiz;
import com.yzj.ebs.report.pojo.EbillMatchQueryParam;
import com.yzj.ebs.report.pojo.EbillMatchResult;
import com.yzj.ebs.util.UtilBase;
import com.yzj.wf.common.WFLogger;
import com.yzj.wf.core.model.po.wrapper.XPeopleInfo;
import com.yzj.wf.export.common.ExportEntity;
import com.yzj.wf.export.service.DataExporterImpl;

/**
 * 创建于:2012-11-22<br>
 * 半年机构对账有效率统计action
 * @param 
 * @author swl
 * @version 1.0.0
 */
public class EbillMatchReportAction extends ActionSupport {

	private static final long serialVersionUID = 1L;
	private EbillMatchQueryParam queryParam;
	private EbillMatchResult result;
	private List<EbillMatchResult> resultList;
	private TreeMap<String, String> refAccCycleMap = new TreeMap<String, String>();
	private List<String> showList;
	private String errMsg = "";
	private IPublicTools tools;
	private String orgTree;
	private static WFLogger logger = WFLogger.getLogger(EbillMatchReportAction.class);

	private IEbillMatchReportBiz ebillMatchReportBizImpl;

	private Map<String, String> refAccTypeMap = new TreeMap<String, String>();
	private Map<String, String> refSendModeMap = new TreeMap<String, String>();
	// 查询下拉框的值
	private String idCenter = "";
	private String idBranch = "";
	private String idBank = "";
	private ArrayList<EbillMatchResult> exportList;
	private String selectCount = "";
	/*
	 * 初始化界面
	 */
	public String init() throws XDocProcException {
		errMsg = null;
		queryParam = new EbillMatchQueryParam();
		resultList = new ArrayList<EbillMatchResult>();
		showList=new ArrayList<String>();
		result = null;
		SimpleOrg org = null;
		idCenter = "";
		idBranch = "";
		idBank = "";
		selectCount = "countIdBank";
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
		resultList.clear();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		String docDateTmp = queryParam.getDocDate();
		String docDate = docDateTmp.substring(0, 4).trim()
				+ docDateTmp.substring(5, 7).trim()
				+ docDateTmp.substring(8).trim();
		UtilBase u = new UtilBase();
		String docDateStr = docDate + "对账情况";
		showList.clear();
		showList.add(docDateStr);
		for (int i = 5; i > 0; i--) {
			docDate=u.getMothLastDate(docDate);
			docDateStr =  docDate + "对账情况";
			showList.add(docDateStr);
		}
		resultList = this.queryData(true);
		return "initSuccess";
	}

	/*
	 * 导出数据
	 */
	public String exportData() throws IOException, XDocProcException {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		List<EbillMatchResult> exportList;
		this.exportList= new ArrayList<EbillMatchResult>();		
			exportList = this.queryData(false);
			ExportEntity entity=new ExportEntity();
			entity.setFileName("半年机构对账有效率统计");
			entity.setTitle("半年机构对账有效率统计");
			entity.setDataList(exportList);
			Map<String,String> pro_desc= new LinkedHashMap<String,String>();
			
			String docDateTmp = queryParam.getDocDate();
			String docDate = docDateTmp.substring(0, 4).trim()
					+ docDateTmp.substring(5, 7).trim()
					+ docDateTmp.substring(8).trim();
		    String docDateChange=docDate;//将页面docDate赋值给变化
			UtilBase u= new UtilBase();
			String docDateStr=docDate+"账户数"+","+docDate+"成功数"+","+docDate+"有效率";
			for(int i=5 ; i>0 ;i--){
				docDateChange=u.getMothLastDate(docDateChange);//传入一个docDate，返回上一个月最后一天的日期yyyymmdd
				docDateStr=docDateChange+"账户数"+","+docDateChange+"成功数"+","+docDateChange+"有效率" +","+ docDateStr;
			}
			String tableHeaderCN = "网点,网点名称,";
			tableHeaderCN=tableHeaderCN+docDateStr;
			String[] tableHeaderName=tableHeaderCN.split(",");
			pro_desc.put("idCenter", "支行");
			pro_desc.put("idBank", "网点");
			pro_desc.put("cname", "网点名称");
			pro_desc.put("SendCount1", tableHeaderName[2]);
			pro_desc.put("MatchCount1", tableHeaderName[3]);
			pro_desc.put("fstMatchPercent", tableHeaderName[4]);
			pro_desc.put("SendCount2", tableHeaderName[5]);
			pro_desc.put("MatchCount2", tableHeaderName[6]);
			pro_desc.put("secMatchPercent", tableHeaderName[7]);
			pro_desc.put("SendCount3", tableHeaderName[8]);
			pro_desc.put("MatchCount3", tableHeaderName[9]);
			pro_desc.put("thrMatchPercent", tableHeaderName[10]);
			pro_desc.put("SendCount4", tableHeaderName[11]);
			pro_desc.put("MatchCount4", tableHeaderName[12]);
			pro_desc.put("fouMatchPercent", tableHeaderName[13]);
			pro_desc.put("SendCount5", tableHeaderName[14]);
			pro_desc.put("MatchCount5", tableHeaderName[15]);
			pro_desc.put("fifMatchPercent", tableHeaderName[16]);
			pro_desc.put("SendCount6", tableHeaderName[17]);
			pro_desc.put("MatchCount6", tableHeaderName[18]);
			pro_desc.put("sixMatchPercent", tableHeaderName[19]);
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
	private List<EbillMatchResult> queryData(boolean isPaged) throws XDocProcException {
		this.resultList = new ArrayList<EbillMatchResult>();
		String docDateTmp = queryParam.getDocDate();
		String docDate = docDateTmp.substring(0, 4).trim()
				+ docDateTmp.substring(5, 7).trim()
				+ docDateTmp.substring(8).trim();
		Map<String, String> queryMap = new HashMap<String, String>();
		queryMap = createMap();
		
		// true为按条件分页查询，否则为查询所有（导出时）
		if (isPaged) {
			return ebillMatchReportBizImpl.getEbillMatchReportResult(queryMap,
					queryParam, docDate,true,selectCount);
		} else {
			return ebillMatchReportBizImpl.getEbillMatchReportResult(queryMap,
					queryParam, docDate,false,selectCount);
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
		String idBank1 = queryParam.getIdBank1();

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
		
		String subjectNo = queryParam.getAccType();
		if(subjectNo != null && subjectNo != ""){
			queryMap.put("subjectNo", subjectNo);
		}
		String accCycle = queryParam.getAccCycle();
		if(accCycle != null && accCycle != ""){
			queryMap.put("accCycle", accCycle);
		}
		String sendMode = queryParam.getSendMode();
		if(sendMode != null && sendMode != ""){
			queryMap.put("sendMode", sendMode);
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

	public ArrayList<EbillMatchResult> getExportList() {
		return exportList;
	}

	public void setExportList(ArrayList<EbillMatchResult> exportList) {
		this.exportList = exportList;
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

	
	public EbillMatchQueryParam getQueryParam() {
		return queryParam;
	}

	public void setQueryParam(EbillMatchQueryParam queryParam) {
		this.queryParam = queryParam;
	}

	public EbillMatchResult getResult() {
		return result;
	}

	public void setResult(EbillMatchResult result) {
		this.result = result;
	}

	public List<EbillMatchResult> getResultList() {
		return resultList;
	}

	public void setResultList(List<EbillMatchResult> resultList) {
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
		EbillMatchReportAction.logger = logger;
	}

	public IEbillMatchReportBiz getEbillMatchReportBizImpl() {
		return ebillMatchReportBizImpl;
	}

	public void setEbillMatchReportBizImpl(
			IEbillMatchReportBiz ebillMatchReportBizImpl) {
		this.ebillMatchReportBizImpl = ebillMatchReportBizImpl;
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
	/**
	 * @return the refAccCycleMap
	 */
	public TreeMap<String, String> getRefAccCycleMap() {
		if (null == refAccCycleMap || refAccCycleMap.size() == 0) {
			refAccCycleMap = RefTableTools.ValRefAccCycleMap;
		}
		return refAccCycleMap;
	}

	public void setRefAccCycleMap(TreeMap<String, String> refAccCycleMap) {
		this.refAccCycleMap = refAccCycleMap;
	}

	public String getSelectCount() {
		return selectCount;
	}

	public void setSelectCount(String selectCount) {
		this.selectCount = selectCount;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
