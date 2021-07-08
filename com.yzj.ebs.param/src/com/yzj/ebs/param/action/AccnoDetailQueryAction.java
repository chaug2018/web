package com.yzj.ebs.param.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.sf.json.JSONObject;
import com.opensymphony.xwork2.ActionSupport;
import com.yzj.ebs.common.IPublicTools;
import com.yzj.ebs.common.RefTableTools;
import com.yzj.ebs.common.SimpleOrg;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.database.AccNoDetailData;
import com.yzj.ebs.param.biz.IParamQueryBiz;
import com.yzj.ebs.param.queryparam.AccnoDetailQueryParam;
import com.yzj.ebs.util.FinalConstant;
import com.yzj.wf.common.WFLogger;
import com.yzj.wf.core.model.po.wrapper.XPeopleInfo;
import com.yzj.wf.export.common.ExportEntity;
import com.yzj.wf.export.service.DataExporterImpl;

/**
 * 
 * 创建于:2012-9-28<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 发生额明细查询
 * 
 * @author ShiJiangmin 单伟龙
 * @version 1.0.0
 */
public class AccnoDetailQueryAction extends ActionSupport {

	private static final long serialVersionUID = 8682825639383690254L;
	private WFLogger logger = WFLogger.getLogger(AccnoDetailQueryAction.class);
	private AccnoDetailQueryParam accnoDetailQueryParam = new AccnoDetailQueryParam();
	List<AccNoDetailData> queryList = new ArrayList<AccNoDetailData>();
	List<AccNoDetailData> exportList = new ArrayList<AccNoDetailData>();
	private String entityName = "";
	private List<String> idbanks;

	private Map<String, String> refDcFlagMap = new TreeMap<String, String>();// 任务流节点名称
	private Map<String, String> refCheckflagMap = new TreeMap<String, String>();// 对账结果

	private Map<String,String> docflagMap = new HashMap<String,String>();

	private String errMsg = null;
	private String orgTree;
	private IPublicTools tools;
	private IParamQueryBiz paramQueryBiz;

	private String idCenter = "";
	private String idBranch = "";
	private String idBank = "";

	/**
	 * 初始化页面
	 * 
	 * @return
	 * @throws XDocProcException
	 */
	public String init() throws XDocProcException {
		queryList.clear();
		exportList.clear();
		accnoDetailQueryParam = new AccnoDetailQueryParam();
		SimpleOrg org = null;
		idCenter = "";
		idBranch = "";
		idBank = "";
		docflagMap = FinalConstant.dcflagName;
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

	/**
	 * 查询发生额明细
	 * 
	 * @return
	 * @throws IOException
	 * @throws XDocProcException
	 */
	public String queryAccnoDetailData() throws IOException, XDocProcException {
		queryList = this.queryData(true);// 分页查询
		return "initSuccess";
	}

	/**
	 * 导出数据
	 * 
	 * @return
	 * @throws IOException
	 * @throws XDocProcException
	 */
	public String exportData() throws XDocProcException {

		exportList = this.queryData(false);// 全量查询
		String tableName = "发生额明细查询";
		ExportEntity entity=new ExportEntity();
		entity.setFileName(tableName);
		entity.setTitle(tableName);
		entity.setDataList(exportList);
		Map<String,String> pro_desc=new LinkedHashMap<String,String>();
		Map<String,Map<String,String>> paramsMap=new HashMap<String,Map<String,String>>();
		pro_desc.put("workDate", "发生日期");
		pro_desc.put("strtraceBal", "发生额");
		pro_desc.put("dcFlag", "借贷方向");
		pro_desc.put("to_Accno", "对方账号");
		pro_desc.put("to_Accname", "对方户名");
		pro_desc.put("strcredit", "余额");
		pro_desc.put("idBank", "开户行");
		pro_desc.put("traceNo", "交易序号");
//		pro_desc.put("checkFlag", "核对结果");
		paramsMap.put("dcFlag", refDcFlagMap);
		paramsMap.put("checkFlag", refCheckflagMap);
		
		entity.setPro_desc_map(pro_desc);
		entity.setParamsMap(paramsMap);
		try {
			new DataExporterImpl().export(entity);
			return null;
		} catch (Throwable e) {
			logger.error("导出列表失败：" + e.getMessage());
		}
		return null;
	}

	/*
	 * 查询数据
	 */
	private List<AccNoDetailData> queryData(boolean isPaged)
			throws XDocProcException {
		queryList = new ArrayList<AccNoDetailData>();
		exportList = new ArrayList<AccNoDetailData>();// 导出的列表
		// 根据对账日期月份确定查询的分表
		String workdate = accnoDetailQueryParam.getWorkDate();
		String monthTmp = workdate.substring(5,7);
		entityName = "ebs_AccnoDetailData_".trim() + monthTmp.trim();
		return paramQueryBiz.getAccnoDetailDataByDocDate(createQueryMap(),
				accnoDetailQueryParam, entityName, workdate, isPaged);
	}

	/**
	 * 通过accnoDetailQueryParam拼装查询参数Map，作为分页查询方法的参数
	 * 
	 * @return 用于查询的Map
	 */
	private Map<String, String> createQueryMap() {
		Map<String, String> queryMap = new HashMap<String, String>();
		String idBank = accnoDetailQueryParam.getIdBank();
		String idCenter = accnoDetailQueryParam.getIdCenter();
		String idBranch = accnoDetailQueryParam.getIdBranch();

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

		String accNo = accnoDetailQueryParam.getAccNo();
		String checkFlag = accnoDetailQueryParam.getCheckFlag();
		queryMap.put("accNo", accNo);
		//queryMap.put("workDate", accnoDetailQueryParam.getWorkDate());
		queryMap.put("checkFlag", checkFlag);
		return queryMap;
	}

	
	public Map<String, String> getRefCheckflagMap() {
		if (null == refCheckflagMap || refCheckflagMap.size() == 0) {
			refCheckflagMap = RefTableTools.ValRefCheckflagMap;
		}
		return refCheckflagMap;
	}

	public void setRefCheckflagMap(Map<String, String> refCheckflagMap) {
		this.refCheckflagMap = refCheckflagMap;
	}

	public AccnoDetailQueryParam getAccnoDetailQueryParam() {
		return accnoDetailQueryParam;
	}

	public void setAccnoDetailQueryParam(
			AccnoDetailQueryParam accnoDetailQueryParam) {
		this.accnoDetailQueryParam = accnoDetailQueryParam;
	}

	public List<AccNoDetailData> getQueryList() {
		return queryList;
	}

	public void setQueryList(List<AccNoDetailData> queryList) {
		this.queryList = queryList;
	}

	public WFLogger getLogger() {
		return logger;
	}

	public void setLogger(WFLogger logger) {
		this.logger = logger;
	}
	
	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public List<String> getIdbanks() {
		return idbanks;
	}

	public void setIdbanks(List<String> idbanks) {
		this.idbanks = idbanks;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
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

	public IParamQueryBiz getParamQueryBiz() {
		return paramQueryBiz;
	}

	public void setParamQueryBiz(IParamQueryBiz paramQueryBiz) {
		this.paramQueryBiz = paramQueryBiz;
	}

	public Map<String, String> getDocflagMap() {
		return docflagMap;
	}

	public void setDocflagMap(Map<String, String> docflagMap) {
		this.docflagMap = docflagMap;
	}

}
