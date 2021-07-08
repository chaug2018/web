package com.yzj.ebs.ebillmatch.param.action;

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
import com.yzj.ebs.ebillmatch.param.biz.IEbillMatchParamBiz;
import com.yzj.ebs.ebillmatch.param.param.MatchResultParam;
import com.yzj.ebs.ebillmatch.param.param.QueryParam;
import com.yzj.ebs.util.UtilBase;
import com.yzj.wf.common.WFLogger;
import com.yzj.wf.core.model.po.wrapper.XPeopleInfo;
import com.yzj.wf.export.common.ExportEntity;
import com.yzj.wf.export.service.DataExporterImpl;

/*
 * 半年机构对账有效率明细统计action
 * @param 
 * @author swl
 * @version 1.0.0
 */
public class EbillMatchParamAction extends ActionSupport {

	private static final long serialVersionUID = 2900661980854042005L;
	private static final java.text.DecimalFormat df = new java.text.DecimalFormat(
			"#.##");
	/*
	 * 
	 */
	private QueryParam queryParam;
	private MatchResultParam result;
	private List<MatchResultParam> resultList;
	private List<String> showList;
	private String errMsg = "";
	private IPublicTools tools;
	private String orgTree;
	private static WFLogger logger = WFLogger
			.getLogger(EbillMatchParamAction.class);

	private IEbillMatchParamBiz ebillMatchParamBizImpl;

	private Map<String, String> refAccTypeMap = new TreeMap<String, String>();
	private Map<String, String> refSendModeMap = new TreeMap<String, String>();
	// 查询下拉框的值
	private String idCenter = "";
	private String idBranch = "";
	private String idBank = "";
	private ArrayList<MatchResultParam> exportList;

	/*
	 * 初始化界面
	 */
	public String init() throws XDocProcException {
		errMsg = null;
		queryParam = new QueryParam();
		resultList = new ArrayList<MatchResultParam>();
		showList = new ArrayList<String>();
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
		this.resultList = new ArrayList<MatchResultParam>();
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
			docDate = u.getMothLastDate(docDate);
			docDateStr = docDate + "对账情况";
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
		List<MatchResultParam> exportList;
		this.exportList = new ArrayList<MatchResultParam>();

		exportList = this.queryData(false);
		ExportEntity entity=new ExportEntity();
		entity.setFileName("半年有效对账明细");
		entity.setTitle("半年有效对账明细");
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
	private List<MatchResultParam> queryData(boolean isPaged) {
		this.resultList = new ArrayList<MatchResultParam>();
		String docDateTmp = queryParam.getDocDate();
		String docDate = docDateTmp.substring(0, 4).trim()
				+ docDateTmp.substring(5, 7).trim()
				+ docDateTmp.substring(8).trim();
		Map<String, String> queryMap = new HashMap<String, String>();
		queryMap = createMap();
		
		//true为分页查询
		if (isPaged) {
			return ebillMatchParamBizImpl.getEbillMatchParam(queryMap,
					(PageParam) queryParam, docDate,true);
		}else{
			return ebillMatchParamBizImpl
					.getEbillMatchParam(queryMap,(PageParam) queryParam, docDate,false);
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
		if (subjectNo!=null&&subjectNo!="") {
			queryMap.put("subjectNo", subjectNo);
		}
		String sendMode = queryParam.getSendMode();
		if(sendMode!=null&&sendMode!=""){
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

	public QueryParam getQueryParam() {
		return queryParam;
	}

	public void setQueryParam(QueryParam queryParam) {
		this.queryParam = queryParam;
	}

	public MatchResultParam getResult() {
		return result;
	}

	public void setResult(MatchResultParam result) {
		this.result = result;
	}

	public List<MatchResultParam> getResultList() {
		return resultList;
	}

	public void setResultList(List<MatchResultParam> resultList) {
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
		EbillMatchParamAction.logger = logger;
	}

	
	public IEbillMatchParamBiz getEbillMatchParamBizImpl() {
		return ebillMatchParamBizImpl;
	}

	public void setEbillMatchParamBizImpl(IEbillMatchParamBiz ebillMatchParamBizImpl) {
		this.ebillMatchParamBizImpl = ebillMatchParamBizImpl;
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

	public ArrayList<MatchResultParam> getExportList() {
		return exportList;
	}

	public void setExportList(ArrayList<MatchResultParam> exportList) {
		this.exportList = exportList;
	}

	public List<String> getShowList() {
		return showList;
	}

	public void setShowList(List<String> showList) {
		this.showList = showList;
	}

}
