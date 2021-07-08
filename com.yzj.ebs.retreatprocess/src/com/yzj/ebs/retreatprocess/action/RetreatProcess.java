package com.yzj.ebs.retreatprocess.action;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
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
import com.yzj.ebs.database.CheckMainData;
import com.yzj.ebs.retreatprocess.biz.IRetreatProcessBiz;
import com.yzj.ebs.retreatprocess.queryparam.RetreatQueryParam;
import com.yzj.wf.common.WFLogger;
import com.yzj.wf.core.model.po.wrapper.XPeopleInfo;
import com.yzj.wf.export.common.ExportEntity;
import com.yzj.wf.export.service.DataExporterImpl;
/**
 * 
 *创建于:2012-11-15<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 退信处理控制类
 * @author 施江敏 单伟龙
 * @version 1.0.0
 */
public class RetreatProcess extends ActionSupport {
	private static final long serialVersionUID = -8346377099690270046L;
	private static WFLogger logger = WFLogger.getLogger(RetreatProcess.class);
	private IPublicTools tools;

	private IRetreatProcessBiz retreatProcessBizImpl;
	
	private RetreatQueryParam retreatQueryParam;
	private XPeopleInfo userInfo; // 人员信息
	private String userId; // 人员编号
	private Map<String, String> refUrgeTypeMap = new TreeMap<String, String>();	//处理类型
	private Map<String, String> refUrgeFlagMap = new TreeMap<String, String>(); // 处理标志 0:未处理 1:已处理
	private Map<String,String> refUrgeResultMap = new TreeMap<String,String>();	//处理意见  0：再次发出 1：放弃放出
	private TreeMap<String, String> refSendMode = new TreeMap<String, String>();// 发送方式
	private Map<String, String> queryMap;
	private String errMsg = null;
	private String orgTree;
	List<CheckMainData> queryList;

	private static String URGEFLAG_NOTPROCESS = "0"; // 退信未处理
	private static String URGEFLAG_PROCESSED = "1"; // 退信已处理
	//private static String URGESTATE_NOTINPUT = "0"; // 退信未登记
	private static String URGESTATE_INPUTED = "1"; // 退信已登记

	// 以下是处理意见所需要的字段
	private String voucherNo;
	private String urgeResult;
	private String urgeDesc;
	private String index;
	private Integer urgeTimes=0;
	// 选择的IDS
	private String selectIds="";

	//查询下拉框的值
	private String idCenter;
	private String idBranch;
	private String idBank;
	/**
	 * 初始化界面要素
	 * 
	 * @return
	 * @throws XDocProcException 
	 */
	public String init() throws XDocProcException {
		retreatQueryParam = new RetreatQueryParam();
		this.queryList = new ArrayList<CheckMainData>();
		errMsg = null;
		SimpleOrg org = null;
		idCenter = "";
		idBank = "";
		idBranch = "";
		XPeopleInfo people = tools.getCurrLoginPeople();//获取登录人员机构树
		try {
			org = tools.getCurOrgTree(people.getOrgNo());
		} catch (XDocProcException e) {
			errMsg = "获取当前机构信息列表出现错误";
			logger.error("获取当前机构信息列表出现错误", e);
		}
		JSONObject json = JSONObject.fromObject(org);
		orgTree = json.toString();
		userInfo = tools.getCurrLoginPeople();
		userId = userInfo.getPeopleCode();

		return "initSuccess";
	}

	/**
	 * 查询退信列表
	 * 
	 * @return
	 * @throws IOException
	 * @throws XDocProcException 
	 */
	public String queryRetreatData() throws IOException, XDocProcException {
		errMsg = null;
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		this.queryList = new ArrayList<CheckMainData>();
		idCenter = retreatQueryParam.getIdCenter();
		idBranch = retreatQueryParam.getIdBranch();
		idBank = retreatQueryParam.getIdBank();
		queryList = retreatProcessBizImpl.getCheckMainData(createQueryMap(),
				retreatQueryParam);
		return "initSuccess";
	}
	
	/**
	 * 根据下标批量处理
	 * @return
	 * @throws IOException
	 */
	public String getCheckMainDataByIndex() throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		CheckMainData info = null;
		if (queryList.get(Integer.valueOf(index) - 1).getVoucherNo().equals(voucherNo.trim())) {
			info = (queryList.get(Integer.valueOf(index) - 1)); // 直接根据下标获取
		} else { // 若数据顺序出现错乱,则循环遍历
			for (int i = 0; i < queryList.size(); i++) {
				if (queryList.get(i).getVoucherNo().equals(voucherNo.trim())) {
					info = queryList.get(i);
					break;
				}
			}
		}
		TempCheckMainData temp=new TempCheckMainData();
		if (info == null) {
			logger.error("未在服务端缓存中找到需要修改的数据,可能是用户开启两个窗口所导致");
			return null;
		}
		temp.setAutoId(info.getAutoId());
		temp.setIdBank(info.getIdBank());
		temp.setAccName(info.getAccName());
		temp.setUrgeDate(info.getUrgeDate());
		temp.setVoucherNo(info.getVoucherNo());
		temp.setUrgeTimes(info.getUrgeTimes());
		temp.setUrgeType(refUrgeTypeMap.get(info.getUrgeType()));
		temp.setUrgeDesc(info.getUrgeDesc());
		temp.setUrgeResult(info.getUrgeResult());
		OutputStream out = null;
		try {
		   JSONObject json = JSONObject.fromObject(temp);		
			out = response.getOutputStream();
			response.setContentType("text/html; charset=UTF-8");
			out.write(json.toString().getBytes("UTF-8"));
			out.flush();
		} catch (Exception e) {
			logger.error("获取json对象出现错误", e);
		} finally {
			if (out != null) {
				out.close();
			}
		}
		return null;
	}

	/**
	 * 导出数据
	 * 
	 * @return
	 * @throws IOException
	 * @throws XDocProcException 
	 */
	public String exportData() throws IOException, XDocProcException {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		List<CheckMainData> exportList = new ArrayList<CheckMainData>();

		if (queryList.size() == 0 || queryList == null) {
			response.getWriter().write("查询列表为空");
			return null;
		} else {
			queryMap = createQueryMap();
			exportList = retreatProcessBizImpl.getAllCheckMainData(queryMap);
			String tableName = "退信信息列表";
			ExportEntity entity=new ExportEntity();
			entity.setFileName(tableName);
			entity.setTitle(tableName);
			entity.setDataList(exportList);
			Map<String,String> pro_desc=new LinkedHashMap<String,String>();
			Map<String,Map<String,String>> paramsMap=new HashMap<String,Map<String,String>>();
			LinkedHashMap<String, Map<String, String>> tableHeader = new LinkedHashMap<String, Map<String, String>>();
			pro_desc.put("idCenter", "机构号");
			pro_desc.put("voucherNo", "账单编号");
			pro_desc.put("accName", "账户名称");
			pro_desc.put("urgeDate", "登记日期");
			pro_desc.put("urgeType", "登记类型");
			pro_desc.put("urgeNote", "摘要");
			pro_desc.put("urgeResult", "处理意见");
			pro_desc.put("urgeDesc", "处理备注");
			pro_desc.put("urgeFlag", "处理状态");
			pro_desc.put("urgeTimes", "已退信次数");
			pro_desc.put("urgePeople1","处理柜员");
			pro_desc.put("urgeDate1", "处理日期");
			pro_desc.put("sendMode", "发送渠道");
			
			paramsMap.put("urgeType", refUrgeTypeMap);
			paramsMap.put("urgeResult", refUrgeResultMap);
			paramsMap.put("urgeResult", refUrgeResultMap);
			paramsMap.put("sendMode", refSendMode);
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

	/**
	 * 退信处理。更新checkmaindata相关字段
	 * 
	 * @return
	 * @throws IOException
	 */
	public String modify() throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		
		try {
			this.modifyOne(index);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			response.getWriter().write(e.getMessage());
			return null;
		}
		return null;
	}

	
	/**
	 * 批量处理
	 * 
	 * @return
	 */
	public String batchModify()throws IOException{
		HttpServletResponse response = ServletActionContext.getResponse();		
		response.setCharacterEncoding("UTF-8");
		String[] indexs=selectIds.split(",");
		if(indexs==null||indexs.length==0){
			response.getWriter().write("未获取到勾选的复选框的下标");
			return null;
		}
		for(int i=0;i<indexs.length;i++){
			try {
				this.modifyOne(indexs[i]);
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
				response.getWriter().write(e.getMessage()+("(已成功"+i+"个)"));
				return null;
			}
		}
		
		return null;
	}
	public void modifyOne(String index) throws Exception
	{
		
		CheckMainData checkMainData = null;
		if ((Integer.valueOf(index)-1)>=queryList.size()) {
			throw new Exception("未找到需要修改的数据");
		}
		 checkMainData=queryList.get(Integer.valueOf(index)-1);
		 checkMainData.setUrgeDesc(urgeDesc);
		 checkMainData.setUrgeResult(urgeResult);
		 if("0".equals(urgeResult)){
			 checkMainData.setDocState("5");
			 checkMainData.setDocStateCN(RefTableTools.ValRefDocstateMap.get("5"));
		 }else if("1".equals(urgeResult)){
			 checkMainData.setDocState("6");
			 checkMainData.setDocStateCN(RefTableTools.ValRefDocstateMap.get("6"));
		 }
		 retreatProcessBizImpl.modifyOne(checkMainData, index);
	}
	/**
	 * 构造查询参数
	 * 
	 * @return
	 */
	private Map<String, String> createQueryMap() {
		queryMap = new TreeMap<String, String>();
		String idBank = retreatQueryParam.getIdBank();
		String idBank1 = retreatQueryParam.getIdBank1();
		String idCenter = retreatQueryParam.getIdCenter();
		String idBranch = retreatQueryParam.getIdBranch();
		this.idCenter=idCenter;
		this.idBranch=idBranch;
		this.idBank=idBank;
		if (idBank != null && idBank.length() > 0){
			queryMap.put("idBank", idBank);
		}
		if (idBank1 != null && idBank1.length() > 0) {
			queryMap.put("idBank", idBank1);
		} 
		 if (idBranch != null && idBranch.length() > 0){
			queryMap.put("idBranch", idBranch);
		} if (idCenter != null && idCenter.length() > 0){
			queryMap.put("idCenter", idCenter);
		}
	
		String docDateTmp = retreatQueryParam.getDocDate();
		if(docDateTmp!=null&&docDateTmp.trim().length()>0){
			String docDate = docDateTmp.substring(0, 4)+docDateTmp.substring(5, 7)+docDateTmp.substring(8);
			queryMap.put("docDate", docDate);	
		}
	
		
		String urgeDateTmp = retreatQueryParam.getUrgeDate();
		String urgeDate="";
		if(urgeDateTmp!=null&&urgeDateTmp.trim().length()>0){
			urgeDate = urgeDateTmp.substring(0, 4)+urgeDateTmp.substring(5, 7)+urgeDateTmp.substring(8);
		}
		queryMap.put("urgeDate", urgeDate);
		queryMap.put("urgeType", retreatQueryParam.getUrgeType());//退信类型
		queryMap.put("voucherNo", retreatQueryParam.getVoucherNo());
		queryMap.put("urgeFlag", retreatQueryParam.getUrgeFlag());//退信标志
		queryMap.put("sendMode", retreatQueryParam.getSendModeFlag()); // 发送方式
		queryMap.put("urgeState", URGESTATE_INPUTED); // 登记状态查询“已登记”
		return queryMap;
	}

	public RetreatQueryParam getRetreatQueryParam() {
		return retreatQueryParam;
	}

	public void setRetreatQueryParam(RetreatQueryParam retreatQueryParam) {
		this.retreatQueryParam = retreatQueryParam;
	}


	public Map<String, String> getRefUrgeTypeMap() {
		if(null==refUrgeTypeMap || refUrgeTypeMap.size()==0){
			refUrgeTypeMap = RefTableTools.ValRefUrgeTypeMap;
		}
		return refUrgeTypeMap;
	}

	public void setRefUrgeTypeMap(Map<String, String> refUrgeTypeMap) {
		this.refUrgeTypeMap = refUrgeTypeMap;
	}

	public Map<String, String> getRefUrgeFlagMap() {
		refUrgeFlagMap.put(URGEFLAG_NOTPROCESS, "未处理");
		refUrgeFlagMap.put(URGEFLAG_PROCESSED, "已处理");
		return refUrgeFlagMap;
	}

	public void setRefUrgeFlagMap(Map<String, String> refUrgeFlagMap) {
		this.refUrgeFlagMap = refUrgeFlagMap;
	}

	public Map<String, String> getQueryMap() {
		return queryMap;
	}

	public void setQueryMap(Map<String, String> queryMap) {
		this.queryMap = queryMap;
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

	public List<CheckMainData> getQueryList() {
		return queryList;
	}

	public void setQueryList(List<CheckMainData> queryList) {
		this.queryList = queryList;
	}


	public XPeopleInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(XPeopleInfo userInfo) {
		this.userInfo = userInfo;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getVoucherNo() {
		return voucherNo;
	}

	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}



	public String getUrgeResult() {
		return urgeResult;
	}

	public void setUrgeResult(String urgeResult) {
		this.urgeResult = urgeResult;
	}

	public String getUrgeDesc() {
		return urgeDesc;
	}

	public void setUrgeDesc(String urgeDesc) {
		this.urgeDesc = urgeDesc;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public Map<String, String> getRefUrgeResultMap() {
		refUrgeResultMap = new TreeMap<String,String>();
		refUrgeResultMap.put("0", "再次发出");
		refUrgeResultMap.put("1", "放弃发出");
		return refUrgeResultMap;
	}

	public void setRefUrgeResultMap(Map<String, String> refUrgeResultMap) {
		this.refUrgeResultMap = refUrgeResultMap;
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

	public Integer getUrgeTimes() {
		return urgeTimes;
	}
	
	public void setUrgeTimes(Integer urgeTimes) {
		this.urgeTimes = urgeTimes;
	}
	public TreeMap<String, String> getRefSendMode() {
		if (null == refSendMode || refSendMode.size() == 0) {
			refSendMode = RefTableTools.ValRefSendModeMap;
		}
		return refSendMode;
	}

	public void setRefSendMode(TreeMap<String, String> refSendMode) {
		this.refSendMode = refSendMode;
	}
	
	/**
	 * 
	 *创建于:2013-1-15<br>
	 *版权所有(C) 2013 深圳市银之杰科技股份有限公司<br>
	 *  临时checkMaindata对象，做界面返回（因为json在处理checkMainData对象的时候会报错，所以搞了个替身）
	 * @author chender
	 * @version 1.0
	 */
	public class TempCheckMainData implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 5384783571032051615L;
		private Long autoId;
		private String idBank;
		private String accName;
		private String voucherNo;
		private String urgeDate;
		private Integer urgeTimes;
		private String urgeResult;
		private String urgeDesc;
		private String urgeType;
		
		
	
		public Long getAutoId() {
			return autoId;
		}
		public void setAutoId(Long autoId) {
			this.autoId = autoId;
		}
		public String getIdBank() {
			return idBank;
		}
		public void setIdBank(String idBank) {
			this.idBank = idBank;
		}
		public String getAccName() {
			return accName;
		}
		public void setAccName(String accName) {
			this.accName = accName;
		}
		public String getVoucherNo() {
			return voucherNo;
		}
		public void setVoucherNo(String voucherNo) {
			this.voucherNo = voucherNo;
		}
		public String getUrgeDate() {
			return urgeDate;
		}
		public void setUrgeDate(String urgeDate) {
			this.urgeDate = urgeDate;
		}
		public String getUrgeResult() {
			return urgeResult;
		}
		public void setUrgeResult(String urgeResult) {
			this.urgeResult = urgeResult;
		}
		public String getUrgeDesc() {
			return urgeDesc;
		}
		public void setUrgeDesc(String urgeDesc) {
			this.urgeDesc = urgeDesc;
		}
		public Integer getUrgeTimes() {
			return urgeTimes;
		}
		public void setUrgeTimes(Integer urgeTimes) {
			this.urgeTimes = urgeTimes;
		}
		public String getUrgeType() {
			return urgeType;
		}
		public void setUrgeType(String urgeType) {
			this.urgeType = urgeType;
		}
		
		
	}
	public String getSelectIds() {
		return selectIds;
	}

	public void setSelectIds(String selectIds) {
		this.selectIds = selectIds;
	}

	public IRetreatProcessBiz getRetreatProcessBizImpl() {
		return retreatProcessBizImpl;
	}

	public void setRetreatProcessBizImpl(IRetreatProcessBiz retreatProcessBizImpl) {
		this.retreatProcessBizImpl = retreatProcessBizImpl;
	}

}
