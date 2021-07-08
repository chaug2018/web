package com.yzj.ebs.billinfoquery.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.servlet.http.HttpServletRequest;
import net.sf.json.JSONObject;
import org.apache.struts2.ServletActionContext;

import com.infotech.publiclib.Exception.DaoException;
import com.opensymphony.xwork2.ActionSupport;
import com.yzj.ebs.billinfoquery.biz.IBillinfoQueryBiz;
import com.yzj.ebs.billinfoquery.queryparam.BillinfoQueryParam;
import com.yzj.ebs.common.IPublicTools;
import com.yzj.ebs.common.IdCenterParam;
import com.yzj.ebs.common.RefTableTools;
import com.yzj.ebs.common.SimpleOrg;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.database.AccNoMainData;
import com.yzj.ebs.database.CheckMainData;
import com.yzj.ebs.util.DataExporter;
import com.yzj.wf.am.security.common.AMSecurityDefine;
import com.yzj.wf.common.WFLogger;
import com.yzj.wf.core.model.po.wrapper.XPeopleInfo;
import com.yzj.wf.export.common.ExportEntity;
import com.yzj.wf.export.service.DataExporterImpl;

/**
 * 对账单信息查询
 * 
 * @author SWL
 * @version 1.0.0
 * 
 * 
 */
public class BillinfoQueryAction extends ActionSupport {

	private static final long serialVersionUID = -3783560582604032835L;
	private static WFLogger logger = WFLogger
			.getLogger(BillinfoQueryAction.class);
	private TreeMap<String, String> refCheckflagMap = new TreeMap<String, String>();
	private TreeMap<String, String> refProveflagMap = new TreeMap<String, String>();
	private TreeMap<String, String> refDocStateMap = new TreeMap<String, String>();
	private Map<String, String> refQueryTypeMap = new TreeMap<String, String>();
	private TreeMap<String, String> refAccCycleMap = new TreeMap<String, String>();
	private TreeMap<String, String> refAccTypeMap = new TreeMap<String, String>();
	private TreeMap<String, String> refSendModeMap = new TreeMap<String, String>();
	private TreeMap<String, String> checkResultMap = new TreeMap<String, String>();
	private TreeMap<String, String> faceFlagMap = new TreeMap<String, String>();
	
	private List<CheckMainData> queryBillList;
	private List<AccNoMainData> resultList;
	private BillinfoQueryParam billinfoQueryParam;

	private String errMsg = null;
	private String orgTree;
	private IPublicTools tools;

	public final static String VOUCHERNO_QUERYTYPE = "1";// 按账单编号查询，只有一张表，checkmaindata
	public final static String ACCNOINFO_QUERYTYPE = "2";// 按账号查询，查询俩张表，checkmaindata和accnomaindata表

	private String idCenter = "";
	private String idBank = "";

	private List<String> imgList = new ArrayList<String>();
	public IBillinfoQueryBiz billinfoQueryBizImpl;
	private CheckMainData checkMainData;
	private Map<String,String> currTypeMap = new HashMap<String,String>();    // 币种
	private RefTableTools refTableTools;

	/**
	 * 初始化获取页面需要数据
	 */
	public String init() {
		errMsg = null;
		billinfoQueryParam = new BillinfoQueryParam();
		queryBillList = new ArrayList<CheckMainData>();
		resultList = new ArrayList<AccNoMainData>();
		checkMainData = null;
		SimpleOrg org = null;
		idCenter = "";
		idBank = "";
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
		try {
			currTypeMap = refTableTools.getParamSub("param_currtype");
		} catch (DaoException e) {
			e.printStackTrace();
		}
		return "initSuccess";
	}

	/**
	 * 查询所有对账单信息
	 * 
	 */
	@SuppressWarnings("unchecked")
	public String queryBillinfoData() throws IOException, XDocProcException {
		this.queryBillList = new ArrayList<CheckMainData>();
		idCenter = billinfoQueryParam.getIdCenter();
		idBank = billinfoQueryParam.getIdBank();
		
		Map<String, String> queryMap = new HashMap<String, String>();
		queryMap = creatMap();
		String accNo = billinfoQueryParam.getAccNo();
		String queryType = billinfoQueryParam.getQueryType();
		
		if (queryType.equals("2")) {
			//按账号查询
			resultList = (List<AccNoMainData>)billinfoQueryBizImpl.queryData(queryMap, billinfoQueryParam, accNo, queryType);
		} else {
			//按账单查询
			queryBillList = (List<CheckMainData>) billinfoQueryBizImpl.queryData(queryMap, billinfoQueryParam, accNo, queryType);
		}
		return "initSuccess";
	}

	/*
	 * 完成查询条件的queryMap
	 */
	private Map<String, String> creatMap() {
		Map<String, String> queryMap = new HashMap<String, String>();
		String idBank = billinfoQueryParam.getIdBank();
		String idCenter = billinfoQueryParam.getIdCenter();
		String sendMode = billinfoQueryParam.getSendMode();
		
		this.idCenter = idCenter;
		this.idBank = idBank;
		if (idBank != null && idBank.length() > 0) {
			queryMap.put("idBank", idBank);
		}
		if (idCenter != null && idCenter.length() > 0) {
			queryMap.put("idCenter", idCenter);
		}
		if (sendMode != null && sendMode.length() > 0) {
			queryMap.put("sendMode", sendMode);
		}
		String docDateTmp = billinfoQueryParam.getDocDate();
		String DocDate = docDateTmp.substring(0, 4).trim()
				+ docDateTmp.substring(5, 7).trim()
				+ docDateTmp.substring(8).trim();
		queryMap.put("docDate", DocDate);
		String VoucherNo = billinfoQueryParam.getVoucherNo();
		queryMap.put("voucherNo", VoucherNo);
		String AccName = billinfoQueryParam.getAccName();
		queryMap.put("accName", AccName);
		String finalCheckFlag = billinfoQueryParam.getFinalCheckFlag();
		queryMap.put("finalCheckFlag", finalCheckFlag);
		String ProveFlag = billinfoQueryParam.getProveFlag();
		queryMap.put("proveFlag", ProveFlag);
		String AccType = billinfoQueryParam.getAccType();
		queryMap.put("accType", AccType);
		
		String checkResult = billinfoQueryParam.getCheckResult();
		if (checkResult != null && checkResult.length() > 0) {
			queryMap.put("checkResult", checkResult);
		}
		
		String faceFlag = billinfoQueryParam.getFaceFlag();
		if (faceFlag != null && faceFlag.length() > 0) {
			queryMap.put("faceFlag", faceFlag);
		}
		
		return queryMap;
	}

	/**
	 * 导出数据
	 * 
	 * @return
	 * @throws IOException
	 * @throws XDocProcException
	 */
	public String exportData() throws IOException, XDocProcException {

		String queryType = billinfoQueryParam.getQueryType();
		String accNo = billinfoQueryParam.getAccNo(); 
		if (queryType.equals("2")) {
			List<AccNoMainData> exportList = new ArrayList<AccNoMainData>();
			exportList.clear();
			exportList = (List<AccNoMainData>) billinfoQueryBizImpl.queryAllData(creatMap(), accNo, queryType);	
			ExportEntity entity=new ExportEntity();
			entity.setFileName("对账单账号信息查询");
			entity.setTitle("对账单账号信息查询");
			entity.setDataList(exportList);
			Map<String,String> pro_desc=new LinkedHashMap<String,String>();
			Map<String,Map<String,String>> paramsMap=new HashMap<String,Map<String,String>>();
			LinkedHashMap<String, Map<String, String>> tableHeader = new LinkedHashMap<String, Map<String, String>>();
			pro_desc.put("myvoucherNo", "账单编号");
			pro_desc.put("idCenter", "分行号");
			pro_desc.put("idBank", "机构号");
			pro_desc.put("accNo", "账号");
			pro_desc.put("accNoSon", "子账号");
			pro_desc.put("myaccName", "户名");
			pro_desc.put("strcredit", "余额");
			pro_desc.put("mydocDate", "对账日期");
			pro_desc.put("currency", "币种");
			pro_desc.put("myproveFlag", "验印状态");
			pro_desc.put("finalCheckFlag", "余额结果");
			pro_desc.put("mydocState", "账单状态");
			pro_desc.put("accCycle", "账户类型");
			//pro_desc.put("accType", "账户类型");
			pro_desc.put("myworkDate", "回收日期");
			pro_desc.put("result", "对账成功");
			pro_desc.put("sendMode", "发送方式");
			pro_desc.put("faceFlag", "是否特殊面对面");
			
			paramsMap.put("myproveFlag", refProveflagMap);
			paramsMap.put("finalCheckFlag", refCheckflagMap);
			paramsMap.put("mydocState", refDocStateMap);
			paramsMap.put("accCycle", refAccCycleMap);
			//paramsMap.put("accType", refAccTypeMap);
			paramsMap.put("sendMode", refSendModeMap);
			paramsMap.put("faceFlag", faceFlagMap);
			entity.setPro_desc_map(pro_desc);
			entity.setParamsMap(paramsMap);
			try {
				new DataExporterImpl().export(entity);
				return null;
			} catch (Throwable e) {
				logger.error("导出列表失败：" + e.getMessage());
			}

			return null;
		} else {
			List<CheckMainData> exportList1 = new ArrayList<CheckMainData>();
			exportList1.clear();
			exportList1 = (List<CheckMainData>) billinfoQueryBizImpl.queryAllData(creatMap(), accNo, queryType);	
			String tableName = "对账单信息查询";
			LinkedHashMap<String, Map<String, String>> tableHeader = new LinkedHashMap<String, Map<String, String>>();
			tableHeader.put("voucherNo", null);
			tableHeader.put("idCenter", null);
			tableHeader.put("idBank", null);
			tableHeader.put("accName", null);
			tableHeader.put("docDate", null);
			tableHeader.put("proveFlag", refProveflagMap);
			tableHeader.put("docState", refDocStateMap);
			tableHeader.put("workDate", null);
			tableHeader.put("sendMode", refSendModeMap);
			String tableHeaderCN = "对账单编号,分行号,机构号,户名,对账日期,验印状态,账单状态,回收日期,发送方式";
			DataExporter dataExporter = new DataExporter(tableName,
					exportList1, tableHeader, tableHeaderCN);
			try {
				dataExporter.createExcel();
				dataExporter.downloadLocal(dataExporter.getFileName());
				exportList1.clear();

			} catch (Throwable e) {
				logger.error("导出列表失败：" + e.getMessage());
				errMsg = "导出列表失败：" + e.getMessage();
			}

			return null;
		}
	}

	/***
	 * 查看详细信息的方法
	 * 
	 * @return
	 * @throws XDocProcException
	 */
	public String viewBillinfoDetail() throws XDocProcException {

		HttpServletRequest request = ServletActionContext.getRequest();
		String voucherNo = request.getParameter("voucherNo");
		checkMainData = billinfoQueryBizImpl.queryOneByVoucherNo(voucherNo);
		imgList.clear();
		try {
			IdCenterParam idCenter = tools.getParamIdcenter(checkMainData
					.getIdCenter());
			if (idCenter == null) {
				throw new XDocProcException("无法获取到"
						+ checkMainData.getIdCenter() + "的机构信息");
			}
			String frontimagepath = idCenter.getImageUrl()
					+ checkMainData.getFrontImagePath();

			String backimagepath = idCenter.getImageUrl()
					+ checkMainData.getBackImagePath();
			if (checkMainData.getFrontImagePath() != null
					&& checkMainData.getFrontImagePath().trim().length() > 0) {
				imgList.add(frontimagepath);
			} else if (checkMainData.getFrontImagePath() == null
					|| checkMainData.getFrontImagePath() == "") {

				String beforeimagepath = request.getRequestURL().toString()
						.trim().replace("viewBillinfoDetail.action", "")
						+ "xiangxi.jpg";

				imgList.add(beforeimagepath);
			}
			if (checkMainData.getBackImagePath() != null
					&& checkMainData.getBackImagePath().trim().length() > 0) {
				imgList.add(backimagepath);
			} else if (checkMainData.getBackImagePath() == null
					|| checkMainData.getBackImagePath() == "") {
				String nextimagepath = request.getRequestURL().toString()
						.trim().replace("viewBillinfoDetail.action", "")
						+ "xiangxi.jpg";
				imgList.add(nextimagepath);
			}
		} catch (XDocProcException e) {
			e.printStackTrace();
		}
		return "initSuccess";
	}

	public static WFLogger getLogger() {
		return logger;
	}

	public static void setLogger(WFLogger logger) {
		BillinfoQueryAction.logger = logger;
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

	public TreeMap<String, String> getRefProveflagMap() {
		if (null == refProveflagMap || refProveflagMap.size() == 0) {
			refProveflagMap = RefTableTools.ValRefProveflagMap;
		}
		return refProveflagMap;
	}

	public void setRefProveflagMap(TreeMap<String, String> refProveflagMap) {
		this.refProveflagMap = refProveflagMap;
	}

	public TreeMap<String, String> getRefSendModeMap() {
		if (null == refSendModeMap || refSendModeMap.size() == 0) {
			refSendModeMap = RefTableTools.ValRefSendModeMap;
		}
		return refSendModeMap;
	}

	public void setRefSendModeMap(TreeMap<String, String> refSendModeMap) {
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

	public TreeMap<String, String> getRefAccTypeMap() {
		if (null == refAccTypeMap || refAccTypeMap.size() == 0) {
			refAccTypeMap = RefTableTools.ValRefAcctypeMap;
		}
		return refAccTypeMap;
	}

	public void setRefAccTypeMap(TreeMap<String, String> refAccTypeMap) {
		this.refAccTypeMap = refAccTypeMap;
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

	public BillinfoQueryParam getBillinfoQueryParam() {
		return billinfoQueryParam;
	}

	public void setBillinfoQueryParam(BillinfoQueryParam billinfoQueryParam) {
		this.billinfoQueryParam = billinfoQueryParam;
	}

	public Map<String, String> getRefQueryTypeMap() {
		refQueryTypeMap.put(VOUCHERNO_QUERYTYPE, "按对账单信息查询");
		refQueryTypeMap.put(ACCNOINFO_QUERYTYPE, "按账号信息查询");
		return refQueryTypeMap;
	}

	public void setRefQueryTypeMap(Map<String, String> refQueryTypeMap) {
		this.refQueryTypeMap = refQueryTypeMap;
	}

	public TreeMap<String, String> getCheckResultMap() {
		if (null == checkResultMap || checkResultMap.size() == 0) {
			checkResultMap.put("1", "对账成功");
			checkResultMap.put("2", "对账不成功");
		}
		return checkResultMap;
	}

	public void setCheckResultMap(TreeMap<String, String> checkResultMap) {
		this.checkResultMap = checkResultMap;
	}
	
	public TreeMap<String, String> getFaceFlagMap() {
		if (faceFlagMap == null || faceFlagMap.size() == 0) {
			//faceFlagMap = RefTableTools.ValRefFaceflagMap;
			faceFlagMap.put("1", "是");
		}
		return faceFlagMap;
	}

	public void setFaceFlagMap(TreeMap<String, String> faceFlagMap) {
		this.faceFlagMap = faceFlagMap;
	}
	
	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public String getOrgTree() {
		return orgTree;
	}

	public void setOrgTree(String orgTree) {
		this.orgTree = orgTree;
	}

	public IPublicTools getTools() {
		return tools;
	}

	public void setTools(IPublicTools tools) {
		this.tools = tools;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<CheckMainData> getQueryBillList() {
		return queryBillList;
	}

	public void setQueryBillList(List<CheckMainData> queryBillList) {
		this.queryBillList = queryBillList;
	}

	public String getidCenter() {
		return idCenter;
	}

	public void setidCenter(String idCenter) {
		this.idCenter = idCenter;
	}

	public String getIdBank() {
		return idBank;
	}

	public void setIdBank(String idBank) {
		this.idBank = idBank;
	}

	public static String getVouchernoQuerytype() {
		return VOUCHERNO_QUERYTYPE;
	}

	public static String getAccnoinfoQuerytype() {
		return ACCNOINFO_QUERYTYPE;
	}

	public List<String> getImgList() {
		return imgList;
	}

	public void setImgList(List<String> imgList) {
		this.imgList = imgList;
	}

	public CheckMainData getCheckMainData() {
		return checkMainData;
	}

	public void setCheckMainData(CheckMainData checkMainData) {
		this.checkMainData = checkMainData;
	}

	public List<AccNoMainData> getResultList() {
		return resultList;
	}

	public void setResultList(List<AccNoMainData> resultList) {
		this.resultList = resultList;
	}

	public IBillinfoQueryBiz getBillinfoQueryBizImpl() {
		return billinfoQueryBizImpl;
	}

	public void setBillinfoQueryBizImpl(IBillinfoQueryBiz billinfoQueryBizImpl) {
		this.billinfoQueryBizImpl = billinfoQueryBizImpl;
	}

	public String getIdCenter() {
		return idCenter;
	}

	public void setIdCenter(String idCenter) {
		this.idCenter = idCenter;
	}

	public Map<String, String> getCurrTypeMap() {
		return currTypeMap;
	}

	public void setCurrTypeMap(Map<String, String> currTypeMap) {
		this.currTypeMap = currTypeMap;
	}

	public RefTableTools getRefTableTools() {
		return refTableTools;
	}

	public void setRefTableTools(RefTableTools refTableTools) {
		this.refTableTools = refTableTools;
	}
	
}
