package com.yzj.ebs.rush.action;

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
import com.yzj.ebs.rush.biz.IRushBiz;
import com.yzj.ebs.rush.queryparam.RushQueryParam;
import com.yzj.wf.common.WFLogger;
import com.yzj.wf.core.model.po.wrapper.XPeopleInfo;
import com.yzj.wf.export.common.ExportEntity;
import com.yzj.wf.export.service.DataExporterImpl;

/**
 * 
 * 创建于:2012-10-29<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 对账催收
 * 
 * @author 施江敏
 * @version 1.0.0
 */
public class RushAction extends ActionSupport {

	private static final long serialVersionUID = -4329277568064087038L;
	private static String RUSHFLAG_NOTPROCESS = "0";//未处理
	private static String RUSHFLAG_PROCESSED = "1";//已处理
	private static String RUSHSTATE_NEED = "1";//需催收
	private RushQueryParam rushQueryParam ;
	private static WFLogger logger = WFLogger.getLogger(RushAction.class);
	private IPublicTools tools;
	private IRushBiz rushBizImpl;
	
	private Map<String, String> refRushFlagMap = new TreeMap<String, String>();		//催收标志  0：未处理  1：已处理
	private Map<String,String> refRushMethodMap = new TreeMap<String,String>();	//催收方式 0：电话 1：邮件 2：面对面
	private Map<String,String> refRushResultMap = new TreeMap<String,String>();	//处理意见  0：等待回收 1：再次发出 2：放弃放出
	private TreeMap<String, String> refSendMode = new TreeMap<String, String>();// 发送方式
	private TreeMap<String,String> refCustomResponseMap=new TreeMap<String, String>();//客户回应
	private TreeMap<String,String> refDocStateMap=new TreeMap<String, String>();//账单状态
	private Map<String, String> queryMap;
	private String errMsg = null;
	private String orgTree;
	private List<CheckMainData> queryList ;

	// 以下是维护催收信息时的输入项
	private String voucherNo;
	private String rushMethod;
	private String rushResult;
	private String rushDesc;
	private String index;
	private String selectIds="";
	private String customResponse;

	//查询下拉框的值
	private String idCenter="";
	private String idBank="";
	/**
	 * 初始化界面
	 * 
	 * @return
	 * @throws XDocProcException 
	 */
	public String init() throws XDocProcException {
		rushQueryParam = new RushQueryParam();
		queryList= new ArrayList<CheckMainData>();
		errMsg = null;
		SimpleOrg org = null;
	    idCenter="";
	    idBank="";
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
	 * 查询催收列表
	 * 
	 * @return
	 */
	public String queryRushData() {
		this.queryList= new ArrayList<CheckMainData>();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		idCenter = rushQueryParam.getidCenter();
		idBank = rushQueryParam.getIdBank();
		
		queryMap = createQueryMap();
		try {
			queryList = rushBizImpl.getCheckMainData(queryMap,rushQueryParam);
		} catch (XDocProcException e) {
			errMsg = "催收信息查询失败:" + e.getMessage();
			logger.error("催收信息查询失败", e);
		}
		return "initSuccess";
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
			exportList = rushBizImpl.getAllCheckMainData(queryMap);
			String tableName = "催收信息列表";
			ExportEntity entity=new ExportEntity();
			entity.setFileName(tableName);
			entity.setTitle(tableName);
			entity.setDataList(exportList);
			Map<String,String> pro_desc=new LinkedHashMap<String,String>();
			Map<String,Map<String,String>> paramsMap=new HashMap<String,Map<String,String>>();
			
			pro_desc.put("idCenter", "机构号");
			pro_desc.put("voucherNo", "账单编号");
			pro_desc.put("accName", "账户名称");
			pro_desc.put("docState", "账单状态");
			pro_desc.put("rushDate", "催收日期");
			pro_desc.put("rushOperCode", "催收柜员");
			pro_desc.put("customResponse", "客户回应");
			pro_desc.put("rushMethod", "催收方式");
			pro_desc.put("rushResult", "处理意见");
			pro_desc.put("rushDesc", "备注");
			pro_desc.put("rushFlag", "处理标志");
			pro_desc.put("sendMode", "发送渠道");
			
			paramsMap.put("docState", refDocStateMap);
			paramsMap.put("customResponse", refCustomResponseMap);
			paramsMap.put("rushMethod", refRushMethodMap);
			paramsMap.put("rushResult", refRushResultMap);
			paramsMap.put("rushFlag", refRushFlagMap);
			paramsMap.put("sendMode", refSendMode);
			entity.setParamsMap(paramsMap);
			entity.setPro_desc_map(pro_desc);
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
	 * 批量处理
	 * @return
	 * @throws IOException
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
	/**
	 * 通过下标获取表单记录
	 * @return
	 * @throws IOException
	 */
	public String getCheckMainDataByIndex() throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		CheckMainData info = null;
		if (queryList.get(Integer.valueOf(index) - 1).getVoucherNo().equals(voucherNo)) {
			info = (queryList.get(Integer.valueOf(index) - 1)); // 直接根据下标获取
		} else { // 若数据顺序出现错乱,则循环遍历
			for (int i = 0; i < queryList.size(); i++) {
				if (queryList.get(i).getVoucherNo().equals(voucherNo.trim())) {
					info = queryList.get(i);
					break;
				}
			}
		}
		if (info == null) {
			logger.error("未在服务端缓存中找到需要修改的数据,可能是用户开启两个窗口所导致");
			return null;
		}
		TempCheckMainData temp=new TempCheckMainData();
		temp.setAutoId(info.getAutoId());
		temp.setIdBank(info.getIdBank());
		temp.setAccName(info.getAccName());
		temp.setVoucherNo(info.getVoucherNo());
		temp.setRushMethod(info.getRushMethod());
		temp.setRushResult(info.getRushResult());
		temp.setRushDesc(info.getRushDesc());
		temp.setCustomResponse(info.getCustomResponse());
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
	 * 修改催收信息
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
		}
		return null;
	}
	
	private void modifyOne(String index) throws Exception{
		
		if ((Integer.valueOf(index)-1)>=queryList.size()) {
			throw new Exception("未找到需要修改的数据");
		}
		CheckMainData checkMainData=queryList.get(Integer.valueOf(index)-1);
	    if(checkMainData!=null){
	    	checkMainData.setRushMethod(rushMethod);
	    	checkMainData.setRushDesc(rushDesc);
	    	checkMainData.setRushResult(rushResult);
	    	checkMainData.setCustomResponse(customResponse);
	    }
		rushBizImpl.modifyOne(checkMainData, index);
	}

	
	/**
	 * 构造查询参数map
	 * 
	 * @return
	 */
	private Map<String, String> createQueryMap() {
		queryMap = new TreeMap<String, String>();
		String idBank = rushQueryParam.getIdBank();
		String idBank1 = rushQueryParam.getIdBank1();
		String idCenter = rushQueryParam.getidCenter();
		this.idCenter=idCenter;
		this.idBank=idBank;
		if (idBank != null && idBank.length() > 0){
			queryMap.put("idBank", idBank);
		}
		if (idBank1 != null && idBank1.length() > 0) {
			queryMap.put("idBank", idBank1);
		} 
		  if (idCenter != null && idCenter.length() > 0){
			queryMap.put("idCenter", idCenter);
		}  
		queryMap.put("rushState", RUSHSTATE_NEED);
		queryMap.put("rushMethod", rushQueryParam.getRushMethod());
		queryMap.put("rushFlag", rushQueryParam.getRushFlag());
		queryMap.put("voucherNo", rushQueryParam.getVoucherNo());
		//将页面获取的对账日期转换
		String docDateTmp = rushQueryParam.getDocDate();
		String docDate = docDateTmp.substring(0,4)+docDateTmp.substring(5, 7)+docDateTmp.substring(8);
		queryMap.put("docDate", docDate);
		queryMap.put("sendMode", rushQueryParam.getSendModeFlag()); // 发送方式
		return queryMap;
	}

	public RushQueryParam getRushQueryParam() {
		return rushQueryParam;
	}

	public void setRushQueryParam(RushQueryParam rushQueryParam) {
		this.rushQueryParam = rushQueryParam;
	}

	public static WFLogger getLogger() {
		return logger;
	}

	public static void setLogger(WFLogger logger) {
		RushAction.logger = logger;
	}


	public IPublicTools getTools() {
		return tools;
	}

	public void setTools(IPublicTools tools) {
		this.tools = tools;
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

	public List<CheckMainData> getQueryList() {
		return queryList;
	}

	public void setQueryList(List<CheckMainData> queryList) {
		this.queryList = queryList;
	}


	public Map<String, String> getRefRushFlagMap() {
		refRushFlagMap = new TreeMap<String, String>();
		refRushFlagMap.put(RUSHFLAG_NOTPROCESS, "未处理");
		refRushFlagMap.put(RUSHFLAG_PROCESSED, "已处理");
		return refRushFlagMap;
	}

	public void setRefRushFlagMap(Map<String, String> refRushFlagMap) {
		this.refRushFlagMap = refRushFlagMap;
	}

	public String getVoucherNo() {
		return voucherNo;
	}

	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}


	public String getRushMethod() {
		return rushMethod;
	}

	public void setRushMethod(String rushMethod) {
		this.rushMethod = rushMethod;
	}

	public String getRushResult() {
		return rushResult;
	}

	public void setRushResult(String rushResult) {
		this.rushResult = rushResult;
	}

	public String getRushDesc() {
		return rushDesc;
	}

	public void setRushDesc(String rushDesc) {
		this.rushDesc = rushDesc;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}


	public Map<String, String> getRefRushMethodMap() {
		if (null == refRushMethodMap || refRushMethodMap.size() == 0) {
			refRushMethodMap = RefTableTools.ValRushMethodMap;
		}
		return refRushMethodMap;
	}

	public void setRefRushMethodMap(Map<String, String> refRushMethodMap) {
		this.refRushMethodMap = refRushMethodMap;
	}

	public Map<String, String> getRefRushResultMap() {
		if (null == refRushResultMap || refRushResultMap.size() == 0) {
			refRushResultMap = RefTableTools.ValRushResultMap;
		}
		return refRushResultMap;
	}

	public void setRefRushResultMap(Map<String, String> refRushResultMap) {
		this.refRushResultMap = refRushResultMap;
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

	public String getSelectIds() {
		return selectIds;
	}

	public void setSelectIds(String selectIds) {
		this.selectIds = selectIds;
	}
	public TreeMap<String, String> getRefSendMode() {
		if (null == refSendMode || refSendMode.size() == 0) {
			refSendMode = RefTableTools.ValRefSendModeMap;
		}
		return refSendMode;
	}
	

	public TreeMap<String, String> getRefCustomResponseMap() {
		if(refCustomResponseMap==null||refCustomResponseMap.size()==0){
			refCustomResponseMap=RefTableTools.ValRefCustomResponseMap;
		}
		return refCustomResponseMap;
	}
	

	public TreeMap<String, String> getRefDocStateMap() {
		if(refDocStateMap==null||refDocStateMap.size()==0){
			refDocStateMap=RefTableTools.ValRefDocstateMap;
		}
		return refDocStateMap;
	}

	public void setRefDocStateMap(TreeMap<String, String> refDocStateMap) {
		this.refDocStateMap = refDocStateMap;
	}

	public void setRefCustomResponseMap(TreeMap<String, String> refCustomResponseMap) {
		this.refCustomResponseMap = refCustomResponseMap;
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
		private String rushMethod;
		private String rushResult;
		private String rushDesc;
		private String rushFlag;
		private String rushDate;
		private String rushOperCode;
		private String customResponse;
		
		
		public String getRushFlag() {
			return rushFlag;
		}
		public void setRushFlag(String rushFlag) {
			this.rushFlag = rushFlag;
		}
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
		public String getRushMethod() {
			return rushMethod;
		}
		public void setRushMethod(String rushMethod) {
			this.rushMethod = rushMethod;
		}
		public String getRushResult() {
			return rushResult;
		}
		public void setRushResult(String rushResult) {
			this.rushResult = rushResult;
		}
		public String getRushDesc() {
			return rushDesc;
		}
		public void setRushDesc(String rushDesc) {
			this.rushDesc = rushDesc;
		}
		public String getRushDate() {
			return rushDate;
		}
		public void setRushDate(String rushDate) {
			this.rushDate = rushDate;
		}
		public String getRushOperCode() {
			return rushOperCode;
		}
		public void setRushOperCode(String rushOperCode) {
			this.rushOperCode = rushOperCode;
		}
		public String getCustomResponse() {
			return customResponse;
		}
		public void setCustomResponse(String customResponse) {
			this.customResponse = customResponse;
		}
	}


	public String getCustomResponse() {
		return customResponse;
	}

	public void setCustomResponse(String customResponse) {
		this.customResponse = customResponse;
	}

	public IRushBiz getRushBizImpl() {
		return rushBizImpl;
	}

	public void setRushBizImpl(IRushBiz rushBizImpl) {
		this.rushBizImpl = rushBizImpl;
	}

	public String getIdCenter() {
		return idCenter;
	}

	public void setIdCenter(String idCenter) {
		this.idCenter = idCenter;
	}

}
