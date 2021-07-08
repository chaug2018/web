package com.yzj.ebs.netquery.action;

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
import com.yzj.ebs.netquery.biz.INetQueryBiz;
import com.yzj.ebs.netquery.param.NetQueryParam;
import com.yzj.ebs.netquery.param.NetResultParam;
import com.yzj.wf.am.security.common.AMSecurityDefine;
import com.yzj.wf.common.WFLogger;
import com.yzj.wf.core.model.po.wrapper.XPeopleInfo;
import com.yzj.wf.export.common.ExportEntity;
import com.yzj.wf.export.service.DataExporterImpl;

/**
 * 查询网银对账情况
 * @author SWL
 * @todo 
 * 
 */
public class NetQueryAction extends ActionSupport {

	/**
	 * 页面定义常量
	 */
	private static final long serialVersionUID = -6939200931420142243L;
	private static WFLogger logger = WFLogger.getLogger(NetQueryAction.class);
	private TreeMap<String, String> refCheckflagMap = new TreeMap<String, String>();//余额ref表
	private TreeMap<String, String> refSendModeMap = new TreeMap<String, String>();//发送方式ref表
	private Map<String,String> currTypeMap = new HashMap<String,String>();    // 币种
    List<NetResultParam> resultList = new ArrayList<NetResultParam>();//查询结果集	 
    List<NetResultParam> exportList = new ArrayList<NetResultParam>();//查询结果集
	private NetResultParam result;
	private NetQueryParam netQueryParam;

	private String errMsg = null;
	private String orgTree;
	private IPublicTools tools;

	public INetQueryBiz netQueryBiz;

	private String idCenter = "";
	private String idBranch = "";
	private String idBank = "";

	private String entityName = "";

	/**
	 * 初始化获取页面需要数据
	 */

	public String init() {
		this.clean();
		this.idCenter = "";
		this.idBranch = "";
		this.idBank = "";
		SimpleOrg org = null;
		HttpServletRequest request = ServletActionContext.getRequest();
		XPeopleInfo people = (XPeopleInfo) request.getSession().getAttribute(
				AMSecurityDefine.XPEOPLEINFO);//获取当前登录柜员信息
		try {
			org = tools.getCurOrgTree(people.getOrgNo());
		} catch (XDocProcException e) {
			errMsg = "获取当前机构信息列表出现错误";
			logger.error("获取当前机构信息列表出现错误", e);
		}
		getCurrTypeMap();//币种map
		JSONObject json = JSONObject.fromObject(org);
		orgTree = json.toString();

		return "initSuccess";
	}

	/**
	 * 查询所有对账单信息
	 * 
	 * @return
	 * @throws IOException
	 * @throws XDocProcException
	 */
	public String queryNetData() throws IOException, XDocProcException {

		resultList.clear();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
	
		resultList = this.queryData(true);//分页查询

		return "initSuccess";
	}

	/**
	 * 导出数据
	 * 
	 * @return
	 * @throws IOException
	 */
	public String exportData() throws IOException, XDocProcException {

		exportList.clear();
        //导出全量数据查询
		exportList = this.queryData(false);
		ExportEntity entity=new ExportEntity();
		entity.setFileName("网银对账信息表");
		entity.setTitle("网银对账信息表");
		entity.setDataList(exportList);
		Map<String,String> pro_desc=new LinkedHashMap<String, String>();
		Map<String,Map<String,String>> paramsMap=new HashMap<String,Map<String,String>>();
		pro_desc.put("idCenter", "分行");
		pro_desc.put("idBank", "网点");
		pro_desc.put("bankName", "网点名称");
		pro_desc.put("accNo", "账号");
		pro_desc.put("accName", "账户名称");
		pro_desc.put("docDate", "对账日期");
		pro_desc.put("strcredit", "余额");
		pro_desc.put("billcount", "对账次数");
		pro_desc.put("sendMode","对账方式" );
		pro_desc.put("checkFlag", "对账结果");
		paramsMap.put("sendMode", refSendModeMap);
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

	/***
	 * 查询数据库
	 * @param isPaged
	 * @return
	 * @throws XDocProcException 
	 */
	private List<NetResultParam> queryData(boolean isPaged) throws XDocProcException {
		// TODO Auto-generated method stub
		// 根据对账日期月份确定查询的分表
		String docdateTmp = netQueryParam.getDocDate();
		String monthTmp = docdateTmp.substring(5, 7);
		entityName = "ebs_AccnoDetailData_".trim()+monthTmp.trim();
		//得到查询页面元素的map
		Map<String, String> queryMap = new HashMap<String, String>();
		queryMap = createMap();
		if (isPaged) {
			return netQueryBiz.getNetQueryData(queryMap, netQueryParam, entityName, true);//分页返回数据
		} else {
			return netQueryBiz.getNetQueryData(queryMap, netQueryParam, entityName, false);//分页返回数据
		}

	}
   /***
    * 查询条件构造map
    * @return queryMap
    */
	private Map<String, String> createMap() {
		// TODO Auto-generated method stub
		Map<String, String> queryMap = new HashMap<String, String>();
		String idBank = netQueryParam.getIdBank();
		String idCenter = netQueryParam.getIdCenter();
		String idBranch = netQueryParam.getIdBranch();
		String idBank1 = netQueryParam.getIdBank1();

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

		String docDateTmp = netQueryParam.getDocDate();
		String DocDate = docDateTmp.substring(0, 4).trim()
				+ docDateTmp.substring(5, 7).trim()
				+ docDateTmp.substring(8).trim();
		queryMap.put("DocDate", DocDate);

		String AccSon = netQueryParam.getAccNo();
		queryMap.put("AccNo", AccSon);

		String finalcheckflag = netQueryParam.getCheckFlag();
		queryMap.put("finalcheckflag", finalcheckflag);

		return queryMap;
	}

	/**
	 * 初始化clean方法
	 * @return
	 */
	public void clean(){
		errMsg = null;
		netQueryParam = new NetQueryParam();
		exportList = new ArrayList<NetResultParam>();
		resultList = new ArrayList<NetResultParam>();
		result = null;
		
	}
	public static WFLogger getLogger() {
		return logger;
	}

	public static void setLogger(WFLogger logger) {
		NetQueryAction.logger = logger;
	}

	public TreeMap<String, String> getRefCheckflagMap() {
		if (null == refCheckflagMap || refCheckflagMap.size() == 0) {
			refCheckflagMap = RefTableTools.ValRefCheckflagMap;
		}
		return refCheckflagMap;
	}

	public void setRefCheckflagMap(TreeMap<String, String> refCheckflagMap) {
		this.refCheckflagMap = refCheckflagMap;
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

	public List<NetResultParam> getResultList() {
		return resultList;
	}

	public void setResultList(List<NetResultParam> resultList) {
		this.resultList = resultList;
	}

	public NetResultParam getResult() {
		return result;
	}

	public void setResult(NetResultParam result) {
		this.result = result;
	}

	public NetQueryParam getNetQueryParam() {
		return netQueryParam;
	}

	public void setNetQueryParam(NetQueryParam netQueryParam) {
		this.netQueryParam = netQueryParam;
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

	

	public INetQueryBiz getNetQueryBiz() {
		return netQueryBiz;
	}

	public void setNetQueryBiz(INetQueryBiz netQueryBiz) {
		this.netQueryBiz = netQueryBiz;
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

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public List<NetResultParam> getExportList() {
		return exportList;
	}

	public void setExportList(List<NetResultParam> exportList) {
		this.exportList = exportList;
	}

	public Map<String, String> getCurrTypeMap() {
		if (null == currTypeMap || currTypeMap.size() == 0) {
			currTypeMap = RefTableTools.valParamCurrtypeMap;
		}
		return currTypeMap;
	}

	public void setCurrTypeMap(Map<String, String> currTypeMap) {
		this.currTypeMap = currTypeMap;
	}



}
