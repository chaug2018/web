package com.yzj.ebs.accoper.action;

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

import com.infotech.publiclib.Exception.DaoException;
import com.opensymphony.xwork2.ActionSupport;
import com.yzj.ebs.biz.IAccModifyBiz;
import com.yzj.ebs.common.IPublicTools;
import com.yzj.ebs.common.RefTableTools;
import com.yzj.ebs.common.SimpleOrg;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.database.BasicInfo;
import com.yzj.ebs.query.action.queryparam.AcountInfoQueryParam;
import com.yzj.ebs.util.FinalConstant;
import com.yzj.wf.common.WFLogger;
import com.yzj.wf.core.model.po.wrapper.XPeopleInfo;
import com.yzj.wf.export.common.ExportEntity;
import com.yzj.wf.export.service.DataExporterImpl;

/**
 * 创建于:2012-10-24 版权所有(C) 2012 深圳市银之杰科技股份有限公司
 * 
 * 账户查询action
 * 
 * @author 陈林江
 * @version 1.0.0
 */
public class AccQueryAction extends ActionSupport {

	private static final long serialVersionUID = 2423474133466780987L;
	private static WFLogger logger = WFLogger.getLogger(AccQueryAction.class);
	private TreeMap<String, String> refAccCycleMap = new TreeMap<String, String>();
	private TreeMap<String, String> refAccTypeMap = new TreeMap<String, String>();
	private TreeMap<String, String> sealModeMap = new TreeMap<String, String>();
	private TreeMap<String, String> sendModeMap = new TreeMap<String, String>();
	private Map<String, String> refIsCheck = new HashMap<String, String>();
	private Map<String, String> refIsSpecile = new HashMap<String, String>();
	List<BasicInfo> queryList = new ArrayList<BasicInfo>();
	private AcountInfoQueryParam acountQueryParam = new AcountInfoQueryParam();
	private String idCenter;
	private String idBank;
	private String errMsg = null;
	//是否查询网银账户判断
	private String accT;
	private String orgTree;
	private IPublicTools tools;
	private IAccModifyBiz accModifyBiz;
	private Map<String,String> currTypeMap = new HashMap<String,String>();    // 币种
	
	//科目号
	private TreeMap<String, String> ValParamSubnocMap = new TreeMap<String, String>();
	private RefTableTools refTableTools;

	/**
	 * 初始化获取页面需要数据
	 * @throws XDocProcException 
	 */
	public String init() throws XDocProcException {
		
		this.clean();
		XPeopleInfo people = tools.getCurrLoginPeople();
		SimpleOrg org = null;
		try {
			org = tools.getCurOrgTree(people.getOrgNo());
		} catch (XDocProcException e) {
			errMsg = "获取当前机构信息列表出现错误";
			logger.error("获取当前机构信息列表出现错误", e);
		}
		refIsCheck = tools.getRefIsCheck();
		refIsSpecile = tools.getRefIsSpecile();
		JSONObject json = JSONObject.fromObject(org);
		orgTree = json.toString();
		accT="false";
		try {
			currTypeMap = refTableTools.getParamSub("param_currtype");
		} catch (DaoException e) {
			e.printStackTrace();
		}
		return "initSuccess";
	}

	/**
	 * 查询所有账户信息
	 * 
	 * @return 账户信息列表
	 */
	public String queryAccData() throws IOException {
		errMsg = null;
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		queryList.clear();
		try {
			queryList = this.queryAcountInfo();
		} catch (XDocProcException e) {
			errMsg = "查询账户信息出现错误";
			logger.error("查询账户信息出现错误", e);
		}
		return "initSuccess";
	}

	/**
	 * 导出数据
	 * 
	 * @return 账户信息列表
	 */
	public String exportData() throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		List<BasicInfo> exportList = new ArrayList<BasicInfo>();
		if (queryList.size() == 0 || queryList == null) {
			response.getWriter().write("查询列表为空");
			return null;
		} else {
			Map<String, String> queryMap = new HashMap<String, String>();
			queryMap = createQueryMap();
			try {
				if(accT.equals("false")){
					exportList = accModifyBiz.getAllBasicInfo(queryMap);
				}else{
					exportList = accModifyBiz.getAllAcctList(queryMap);
				}
			} catch (XDocProcException e1) {
				logger.error("账户信息导出时查询数据失败：" + e1.getMessage());
				response.getWriter().write("导出列表失败：" + e1.getMessage());
			}
			ExportEntity entity=new ExportEntity();
			entity.setFileName("账户信息列表");
			entity.setTitle("账户信息列表");
			entity.setDataList(exportList);
			Map<String,String> pro_desc=new LinkedHashMap<String, String>();
			Map<String,Map<String,String>> paramsMap=new HashMap<String,Map<String,String>>();
			pro_desc.put("accNo", "账号");
			pro_desc.put("idBank", "机构号");
			pro_desc.put("bankName", "机构名称");
			pro_desc.put("accName", "账户");
			
			pro_desc.put("subjectNo", "科目号");
			pro_desc.put("accState", "用户状态");
			
			pro_desc.put("sendAddress", "地址");
			pro_desc.put("zip", "邮编");
			pro_desc.put("linkMan", "联系人");
			pro_desc.put("phone", "联系电话");
			pro_desc.put("accCycle", "对账周期");
			pro_desc.put("custId", "客户号");
			pro_desc.put("sealMode", "验印模式");
			pro_desc.put("openDate", "开户日期");
			pro_desc.put("sendMode", "对账渠道");
			paramsMap.put("accType", refAccTypeMap);
			paramsMap.put("accCycle", refAccCycleMap);
			paramsMap.put("sealMode", sealModeMap);
			paramsMap.put("sendMode", sendModeMap);
			paramsMap.put("accState",FinalConstant.accStateName);
			entity.setPro_desc_map(pro_desc);
			entity.setParamsMap(paramsMap);
			try {
				new DataExporterImpl().export(entity);
				return null;
			} catch (Throwable e) {
				logger.error("导出列表失败：" + e.getMessage());
				response.getWriter().write("导出列表失败：" + e.getMessage());
			}
		}
		return null;
	}

	/***
	 * 将页面的查询条件封装成一个map方便查询的时候拼装查询条件
	 * @return 条件map
	 */
	private Map<String, String> createQueryMap() {
		Map<String, String> conditions = new HashMap<String, String>();
		String accNo = acountQueryParam.getAccNo();
		String address = acountQueryParam.getSendAddress();
		String custId = acountQueryParam.getCustId();
		String accCycle = acountQueryParam.getAccCycle();
		String accName = acountQueryParam.getAccName();
		String sealMode = acountQueryParam.getSealMode();
		String sendMode = acountQueryParam.getSendMode();
		String accType = acountQueryParam.getAccType();
		String idBank = acountQueryParam.getIdBank();
		String idCenter = acountQueryParam.getidCenter();
		String subjectNo = acountQueryParam.getSubjectNo();
		String accState = acountQueryParam.getAccState();
		String currency = acountQueryParam.getCurrency();
		
		this.idCenter = idCenter;
		this.idBank = idBank;
		conditions.put("accNo", accNo);
		conditions.put("sendAddress", address);
		conditions.put("custId", custId);
		conditions.put("accCycle", accCycle);
		conditions.put("accName", accName);
		conditions.put("sealMode", sealMode);
		conditions.put("sendMode", sendMode);
		conditions.put("accType", accType);
		conditions.put("subjectNo", subjectNo);
		conditions.put("accState", accState);
		
		if (idBank != null && idBank.length() > 0) {
			conditions.put("idBank", idBank);
		}
		if (idCenter != null && idCenter.length() > 0) {
			conditions.put("idCenter", idCenter);
		}
		String isCheck = acountQueryParam.getIsCheck();
		String isSpecile = acountQueryParam.getIsSpecile();
		if(!isCheck.equals("")){
			conditions.put("isCheck", isCheck);
		}
		if(!isSpecile.equals("")){
			conditions.put("isSpecile",isSpecile );
		}
		if(currency!=null && !"".equals(currency)){
			conditions.put("currency", currency);
		}
			
		return conditions;
	}

	private void clean() {
		queryList.clear();
		acountQueryParam = null;
		errMsg = null;
		idCenter = null;
		idBank = null;
		refIsCheck.clear();
		refIsSpecile.clear();
	}
	
	/**
	 * 用来查询所有的账户信息
	 * 
	 * @throws XDocProcException
	 */
	private List<BasicInfo> queryAcountInfo() throws XDocProcException {
		Map<String, String> conditions = new HashMap<String, String>();
		conditions = createQueryMap();
		List<BasicInfo> resultList =null;
		// 分页查询。
		if(accT.equals("false")){
			resultList = accModifyBiz.getBasicinfoData(conditions,
				(PageParam) acountQueryParam);
		}else{
			resultList=accModifyBiz.getAcctListData(conditions, (PageParam) acountQueryParam);
		}
		return resultList;
	}

	/**
	 * @return the queryList
	 */
	public List<BasicInfo> getQueryList() {
		return queryList;
	}

	/**
	 * @param queryList
	 *            the queryList to set
	 */
	public void setQueryList(List<BasicInfo> queryList) {
		this.queryList = queryList;
	}

		
	
	public TreeMap<String, String> getSealModeMap() {
		if (null == sealModeMap || sealModeMap.size() == 0) {
			sealModeMap = RefTableTools.ValRefSealModeMap;
		}
		return sealModeMap;
	}

	public void setSealModeMap(TreeMap<String, String> sealModeMap) {
		this.sealModeMap = sealModeMap;
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

	/**
	 * @return the refAccCycleMap
	 */
	public TreeMap<String, String> getRefAccCycleMap() {
		if (null == refAccCycleMap || refAccCycleMap.size() == 0) {
			refAccCycleMap = RefTableTools.ValRefAccCycleMap;
		}
		return refAccCycleMap;
	}
	
	public TreeMap<String, String> getValParamSubnocMap() {
		if (null == ValParamSubnocMap || ValParamSubnocMap.size() == 0) {
			ValParamSubnocMap = RefTableTools.ValParamSubnocMap;
		}
		return ValParamSubnocMap;
	}

	public void setValParamSubnocMap(TreeMap<String, String> valParamSubnocMap) {
		ValParamSubnocMap = valParamSubnocMap;
	}

	/**
	 * @param refAccCycleMap
	 *            the refAccCycleMap to set
	 */
	public void setRefAccCycleMap(TreeMap<String, String> refAccCycleMap) {
		this.refAccCycleMap = refAccCycleMap;
	}

	public AcountInfoQueryParam getAcountQueryParam() {
		return acountQueryParam;
	}

	public void setAcountQueryParam(AcountInfoQueryParam acountQueryParam) {
		this.acountQueryParam = acountQueryParam;
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
	public TreeMap<String, String> getSendModeMap() {
		if (null == sendModeMap || sendModeMap.size() == 0) {
			sendModeMap = RefTableTools.ValRefSendModeMap;
		}
		return sendModeMap;
	}

	public void setSendModeMap(TreeMap<String, String> sendModeMap) {
		this.sendModeMap = sendModeMap;
	}
	public IAccModifyBiz getAccModifyBiz() {
		return accModifyBiz;
	}

	public void setAccModifyBiz(IAccModifyBiz accModifyBiz) {
		this.accModifyBiz = accModifyBiz;
	}

	public Map<String, String> getRefIsCheck() {
		return refIsCheck;
	}

	public void setRefIsCheck(Map<String, String> refIsCheck) {
		this.refIsCheck = refIsCheck;
	}

	public Map<String, String> getRefIsSpecile() {
		return refIsSpecile;
	}

	public void setRefIsSpecile(Map<String, String> refIsSpecile) {
		this.refIsSpecile = refIsSpecile;
	}

	public String getIdCenter() {
		return idCenter;
	}

	public void setIdCenter(String idCenter) {
		this.idCenter = idCenter;
	}

	public String getAccT() {
		return accT;
	}

	public void setAccT(String accT) {
		this.accT = accT;
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
