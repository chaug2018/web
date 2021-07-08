package com.yzj.ebs.accoper.action;

import java.io.IOException;
import java.io.OutputStream;
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
import com.yzj.ebs.biz.IAccModifyBiz;
import com.yzj.ebs.common.IPublicTools;
import com.yzj.ebs.common.RefTableTools;
import com.yzj.ebs.common.SimpleOrg;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.database.BasicInfo;
import com.yzj.ebs.query.action.queryparam.AcountInfoQueryParam;
import com.yzj.wf.common.WFLogger;
import com.yzj.wf.core.model.po.wrapper.XPeopleInfo;
import com.yzj.wf.export.common.ExportEntity;
import com.yzj.wf.export.service.DataExporterImpl;

/**
 * 创建于:2012-10-24 版权所有(C) 2012 深圳市银之杰科技股份有限公司
 * 
 * 账户修改action
 * 
 * @author 陈林江
 * @version 1.0.0
 */
public class AccModifyAction extends ActionSupport {

	private static final long serialVersionUID = -1742330773852472864L;
	private static WFLogger logger = WFLogger.getLogger(AccModifyAction.class);
	private TreeMap<String, String> refAccCycleMap = new TreeMap<String, String>();
	private TreeMap<String, String> refAccTypeMap = new TreeMap<String, String>();
	private TreeMap<String, String> sendTypeMap = new TreeMap<String, String>();
	private TreeMap<String, String> faceTypeMap = new TreeMap<String, String>();
	private TreeMap<String, String> modifyTypeMap = new TreeMap<String, String>();
	private TreeMap<String, String> sealModeMap = new TreeMap<String, String>();
	List<BasicInfo> queryList = new ArrayList<BasicInfo>();
	private Map<String, String> refIsCheck =new HashMap<String, String>();
	private AcountInfoQueryParam acountQueryParam;
	private String idCenter;
	private String idBank;
	private String errMsg = null;
	private String orgTree;
	private IPublicTools tools;
	private String accNo;
	private String accName;
	private String orgId;
	private String phone;
	private String zip;
	private String linkMan;
	private String sendAddress;
	private String sealNo;
	private String faceType;
	private String sendType;
	private String sealMode;
	private String index;
	private String modifyType;
	private IAccModifyBiz accModifyBiz;


	/**
	 * 初始化获取页面需要数据
	 */
	public String init() {
		this.clean();
		try {
			XPeopleInfo people = tools.getCurrLoginPeople();
			SimpleOrg org = null;
			try {
				org = tools.getCurOrgTree(people.getOrgNo());
			} catch (XDocProcException e) {
				errMsg = "获取当前机构信息列表出现错误";
				logger.error("获取当前机构信息列表出现错误", e);
			}
			refIsCheck = tools.getRefIsCheck();
			JSONObject json = JSONObject.fromObject(org);
			orgTree = json.toString();
		} catch (XDocProcException e1) {
			errMsg = "会话超时，请重新登录再试！";
			logger.error("会话超时，请重新登录再试！", e1);
		}
		getRefAccCycleMap();
		return "initSuccess";
	}

	/**
	 * 查询所有账户信息
	 * 
	 * @return
	 * @throws IOException
	 * @throws XDocProcException
	 */
	public String queryAccData() throws IOException {
		try {
			queryList = this.queryAcountInfo(acountQueryParam);
		} catch (XDocProcException e) {
			errMsg = "查询账户信息出现错误";
			logger.error("查询账户信息出现错误", e);
		}
		return "initSuccess";
	}

	/***
	 * 删除账户信息
	 */
	public void delete() throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		BasicInfo info = null;
		if (queryList.get(Integer.valueOf(index) - 1).getAccNo().equals(accNo)) {
			info = (queryList.get(Integer.valueOf(index) - 1)); // 直接根据下标获取
		} else { // 若数据顺序出现错乱,则循环遍历
			for (int i = 0; i < queryList.size(); i++) {
				if (queryList.get(i).getAccNo().equals(6)) {
					info = queryList.get(i);
					break;
				}
			}
		}
		if (info == null) {
			logger.error("未找到需要修改的数据,可能是用户开启两个窗口所导致");
		}
		try {
			// 执行更新数据库
			accModifyBiz.delete(info);
		} catch (XDocProcException e) {
			logger.error(e.getMessage(), e);
			response.getWriter().write(e.getMessage());
		}
	}

	/***
	 * 账户信息修改
	 */
	@SuppressWarnings("unused")
	public void modify() throws Exception {
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		response.setCharacterEncoding("UTF-8");
		BasicInfo info = null;
		//从前台界面的到 修改后的 账户类型， 发送方式 ， 验印模式,是否参与对账
		String accCycle =request.getParameter("accnoFlag");
		String sealMode =request.getParameter("sealMode");
		String sendType =request.getParameter("sendType");
		String isCheck  =request.getParameter("isCheck");
		String address = tools.getParameter("sendAddress");
		if (queryList.get(Integer.valueOf(index) - 1).getAccNo().equals(accNo)) {
			info = (queryList.get(Integer.valueOf(index) - 1)); // 直接根据下标获取
		} else { // 若数据顺序出现错乱,则循环遍历
			for (int i = 0; i < queryList.size(); i++) {
				if (queryList.get(i).getAccNo().equals(accNo)) {
					info = queryList.get(i);
					break;
				}
			}
		}
		//如果账户类型发生变化 则账户设置为特殊帐户	
			if(!accCycle.equals("")&&!accCycle.equals(info.getAccCycle())){
				info.setAccCycle(accCycle);
				info.setIsSpecile("1");
			}	
			//info.setSealMode(sealMode);	
			//info.setSendMode(sendType);
			if("3".equals(sendType)){
				info.setSignFlag("0");//柜面签约网银
			}
			
		if(!isCheck.equals(info.getIsCheck())){
			info.setIsCheck(isCheck);
			//info.setIsSpecile("1");
		}
			//邮寄地址修改
			info.setSendAddress(address);
			info.setAddress(address);
		if (info == null) {
			logger.error("未找到需要修改的数据,可能是用户开启两个窗口所导致");
			response.getWriter().write("修改失败，未找到对应的数据,请尝试刷新页面");
		}
		// 判断验印账号的客户号必须跟签约账号一致
		try {
			accModifyBiz.isTheSame(info.getAccNo(), sealNo);
		} catch (XDocProcException e) {
			logger.error(e.getMessage());
			response.getWriter().write(e.getMessage());
		}

		String desc = this.updateBasicInfo(info);
		// 更新数据库
		try {
			accModifyBiz.modify(info, desc);
		} catch (Exception e) {
			logger.error(e.getMessage());
			response.getWriter().write(e.getMessage());		
		}
	}

	/***
	 * 根据索引获取账户信息
	 */
	public String getBasicInfoByIndex() throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		BasicInfo info = null;
		BasicInfo basicinfo = queryList.get(Integer.valueOf(index) - 1);
		if(basicinfo!=null){
			if (basicinfo.getAccNo().equals(accNo)) {
				info = basicinfo; // 直接根据下标获取
			} else { // 若数据顺序出现错乱,则循环遍历
				for (int i = 0; i < queryList.size(); i++) {
					if (queryList.get(i).getAccNo().equals(accNo)) {
						info = queryList.get(i);
						break;
					}
				}
			}
		}
		if (info == null) {
			logger.error("未在服务端缓存中找到需要修改的数据,可能是用户开启两个窗口所导致");
			return null;
		}
		JSONObject json = JSONObject.fromObject(info);
		OutputStream out = null;
		try {
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
				exportList = accModifyBiz.getAllBasicInfo(queryMap);
			} catch (XDocProcException e1) {
				logger.error("账户信息导出时查询数据失败：" + e1.getMessage());
				response.getWriter().write("导出列表失败：" + e1.getMessage());
				return null;
			}
			ExportEntity entity = new ExportEntity();

			String tableName = "账户信息列表";
			entity.setFileName(tableName);
			entity.setTitle(tableName);
			entity.setDataList(exportList);
			Map<String, String> pro_desc = new LinkedHashMap<String, String>();
			Map<String, Map<String, String>> paramsMap = new HashMap<String, Map<String, String>>();
			pro_desc.put("accNo", "账号");
			pro_desc.put("idBank", "机构号");
			pro_desc.put("accName", "户名");
			pro_desc.put("sendAddress", "地址");
			pro_desc.put("zip", "邮编");
			pro_desc.put("linkMan", "联系人");
			pro_desc.put("phone", "联系电话");
			//pro_desc.put("accType", "账户类型");
			pro_desc.put("custId", "客户号");
			pro_desc.put("sealAccNo", "验印账号");
			pro_desc.put("sealMode", "验印模式");
			pro_desc.put("sendMode", "发送方式");

			paramsMap.put("accType", refAccTypeMap);
			paramsMap.put("sealMode", sealModeMap);
			paramsMap.put("sendMode", sendTypeMap);
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

	private String updateBasicInfo(BasicInfo info) {
		String desc = "账号编辑维护成功!";
//		if(phone!=null&&!phone.equals(info.getPhone())){
//			desc+="联系电话:"+info.getPhone()+"-->"+phone;
//		}
//		if(zip!=null&&!zip.equals(info.getZip())){
//			desc+="，邮编:"+info.getZip()+"-->"+zip;
//		}
//		if(linkMan!=null&&!linkMan.equals(info.getLinkMan())){
//			desc+="，联系人:"+info.getLinkMan()+"-->"+linkMan;
//		}
//		if(address!=null&&!address.equals(info.getAddress())){
//			desc+="，地址:"+info.getAddress()+"-->"+address;
//		}
//		if(sealNo!=null&&!sealNo.equals(info.getSealAccNo())){
//			desc+="，验印账号:"+info.getSealAccNo()+"-->"+sealNo;
//		}
		if(sealMode!=null&&!sealMode.equals(info.getSealMode())){
			desc+="，验印模式:"+sealModeMap.get(info.getSealMode() == null  ? "" : info.getSealMode())+"-->"+sealModeMap.get(sealMode);
		}
		if(sendType!=null&&!sendType.equals(info.getSendMode())){
			desc+="，发送方式:"+sendTypeMap.get(info.getSendMode() == null ? "" : info.getSendMode())+"-->"+sendTypeMap.get(sendType);
		}
		if(info.getAccCycle()!=null){
			desc+="，维护账户类型:"+info.getAccCycle()+refAccCycleMap.get(info.getAccCycle());
		}
		
		if(info.getIsCheck()!=null && !"".equals(info.getIsCheck())){
			desc+= " 账户为:"+refIsCheck.get(info.getIsCheck());
		}
		
		info.setPhone(phone);
		info.setZip(zip);
		info.setLinkMan(linkMan);
		info.setSendAddress(sendAddress);
		info.setSealAccNo(sealNo);
		info.setSealMode(sealMode);
		info.setSendMode(sendType);
		return desc;
	}

	// 清空成员变量值
	private void clean() {
		queryList.clear();
		errMsg = null;
		accName = null;
		accNo = null;
		orgId = null;
		phone = null;
		zip = null;
		linkMan = null;
		sendAddress = null;
		sealNo = null;
		faceType = null;
		sendType = null;
		index = null;
		idCenter = null;
		idBank = null;
		modifyType = null;
		acountQueryParam = new AcountInfoQueryParam();
		refIsCheck.clear();

	}

	/**
	 * 用来查询所有的账户信息
	 * 
	 * @throws XDocProcException
	 */
	private List<BasicInfo> queryAcountInfo(
			AcountInfoQueryParam acountQueryParam) throws XDocProcException {
		Map<String, String> conditions = new HashMap<String, String>();
		conditions = createQueryMap();
		// 分页查询。
		List<BasicInfo> resultList = accModifyBiz.getBasicinfoData(conditions,
				(PageParam) acountQueryParam);
		return resultList;
	}

	/**
	 * 
	 * 创建查询参数map
	 * 
	 * @return
	 */
	public Map<String, String> createQueryMap() {
		Map<String, String> conditions = new HashMap<String, String>();
		String accNo = acountQueryParam.getAccNo();
		String sendAddress = acountQueryParam.getSendAddress();
		String isCheck = acountQueryParam.getIsCheck();
		String sealMode = acountQueryParam.getSealMode();
		String sendMode = acountQueryParam.getSendMode();
		String custId = acountQueryParam.getCustId();
		String accCycle = acountQueryParam.getAccCycle();
		String accName = acountQueryParam.getAccName();
		String idBank = acountQueryParam.getIdBank();
		String idCenter = acountQueryParam.getidCenter();
		this.idCenter = idCenter;
		this.idBank = idBank;
		if (accNo != null && accNo.length() > 0) {
			conditions.put("accNo", accNo);
		}
		if (sendAddress != null && sendAddress.length() > 0) {
			conditions.put("sendAddress", sendAddress);
		}
		if (custId != null && custId.length() > 0) {
			conditions.put("custId", custId);
		}
		if (accCycle != null && accCycle.length() > 0) {
			conditions.put("accCycle", accCycle);
		}
		if (accName != null && accName.length() > 0) {
			conditions.put("accName", accName);
		}
		if (idBank != null && idBank.length() > 0) {
			conditions.put("idBank", idBank);
		}
		if (idCenter != null && idCenter.length() > 0) {
			conditions.put("idCenter", idCenter);
		}
		if (sendMode != null && sendMode.length() > 0) {
			conditions.put("sendMode", sendMode);
		}
		if (sealMode != null && sealMode.length() > 0) {
			conditions.put("sealMode", sealMode);
		}
		if (isCheck != null && isCheck.length() > 0) {
			conditions.put("isCheck", isCheck);
		}
		// 只查询普通账户 
		conditions.put("isspecile","0");
//		conditions.put("isCheck","0");
		return conditions;
	}
	


	/**
	 * @param refAccCycleMap  账户类型 1234 不对账
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

	public TreeMap<String, String> getSealModeMap() {
		if (sealModeMap == null || sealModeMap.size() == 0) {
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

	public TreeMap<String, String> getSendTypeMap() {
		if (sendTypeMap == null || sendTypeMap.size() == 0) {
			sendTypeMap = RefTableTools.ValRefSendModeMap;
		}
		return sendTypeMap;
	}

	public void setSendTypeMap(TreeMap<String, String> sendTypeMap) {
		this.sendTypeMap = sendTypeMap;
	}

	public TreeMap<String, String> getFaceTypeMap() {
		if (faceTypeMap == null || faceTypeMap.size() == 0) {
			faceTypeMap = RefTableTools.ValRefFaceflagMap;
		}
		return faceTypeMap;
	}

	public void setFaceTypeMap(TreeMap<String, String> faceTypeMap) {
		this.faceTypeMap = faceTypeMap;
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

	/**
	 * @return the refAccCycleMap
	 */
	public TreeMap<String, String> getRefAccCycleMap() {
		if (null == refAccCycleMap || refAccCycleMap.size() == 0) {
			refAccCycleMap = RefTableTools.ValRefAccCycleMap;
		}
		return refAccCycleMap;
	}

	public String getAccNo() {
		return accNo;
	}

	public void setAccNo(String accNo) {
		this.accNo = accNo.trim();
	}

	public String getAccName() {
		return accName;
	}

	public void setAccName(String accName) {
		this.accName = accName;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getLinkMan() {
		return linkMan;
	}

	public void setLinkMan(String linkMan) {
		this.linkMan = linkMan;
	}

	public String getSendAddress() {
		return sendAddress;
	}

	public void setSendAddress(String sendAddress) {
		this.sendAddress = sendAddress;
	}

	public String getSealNo() {
		return sealNo;
	}

	public void setSealNo(String sealNo) {
		this.sealNo = sealNo;
	}

	public String getFaceType() {
		return faceType;
	}

	public void setFaceType(String faceType) {
		this.faceType = faceType;
	}

	public String getSendType() {
		return sendType;
	}

	public void setSendType(String sendType) {
		this.sendType = sendType;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
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

	public String getSealMode() {
		return sealMode;
	}

	public void setSealMode(String sealMode) {
		this.sealMode = sealMode;
	}

	public IAccModifyBiz getAccModifyBiz() {
		return accModifyBiz;
	}

	public void setAccModifyBiz(IAccModifyBiz accModifyBiz) {
		this.accModifyBiz = accModifyBiz;
	}

	public TreeMap<String, String> getModifyTypeMap() {
		if (modifyTypeMap.size() == 0) {
			modifyTypeMap.put("0", "按客户号维护");
			modifyTypeMap.put("1", "按账号维护");
		}
		return modifyTypeMap;
	}

	public void setModifyTypeMap(TreeMap<String, String> modifyTypeMap) {
		this.modifyTypeMap = modifyTypeMap;
	}

	public String getModifyType() {
		return modifyType;
	}

	public void setModifyType(String modifyType) {
		this.modifyType = modifyType;
	}

	public Map<String, String> getRefIsCheck() {
		return refIsCheck;
	}

	public void setRefIsCheck(Map<String, String> refIsCheck) {
		this.refIsCheck = refIsCheck;
	}

	public String getIdCenter() {
		return idCenter;
	}

	public void setIdCenter(String idCenter) {
		this.idCenter = idCenter;
	}


	
}
