package com.yzj.ebs.accoper.action;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
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
import com.yzj.ebs.common.BankParam;
import com.yzj.ebs.common.IPublicTools;
import com.yzj.ebs.common.RefTableTools;
import com.yzj.ebs.common.SimpleOrg;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.database.BasicInfo;
import com.yzj.ebs.query.action.queryparam.AcountInfoQueryParam;
import com.yzj.wf.am.security.common.AMSecurityDefine;
import com.yzj.wf.common.WFLogger;
import com.yzj.wf.core.model.po.wrapper.XPeopleInfo;
import com.yzj.wf.export.common.ExportEntity;
import com.yzj.wf.export.service.DataExporterImpl;

/**
 * 创建于:2012-10-26 版权所有(C) 2012 深圳市银之杰科技股份有限公司
 * 
 * 账户签约维护action
 * 
 * @author 陈林江
 * @version 1.0.0
 */
public class AccSignAction extends ActionSupport {

	private static final long serialVersionUID = 6164668642550717876L;
	private static WFLogger logger = WFLogger.getLogger(AccSignAction.class);
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private TreeMap<String, String> refAccCycleMap = new TreeMap<String, String>();
	private TreeMap<String, String> refAccTypeMap = new TreeMap<String, String>();
	private TreeMap<String, String> sendTypeMap = new TreeMap<String, String>();
	private TreeMap<String, String> faceTypeMap = new TreeMap<String, String>();
	private TreeMap<String, String> contactFlagMap = new TreeMap<String, String>();
	private TreeMap<String, String> sealModeMap = new TreeMap<String, String>();
	private TreeMap<String, String> modifyTypeMap = new TreeMap<String, String>();
	List<BasicInfo> queryList = new ArrayList<BasicInfo>();
	private AcountInfoQueryParam acountQueryParam;
	private String idCenter;

	private String idBank;
	// private IBasicInfoAdm basicInfoAdm;
	// private IBasicInfoLogAdm basicInfoLogAdm;
	private String errMsg = null;
	private String orgTree;
	private IPublicTools tools;
	private String accNo;
	private String accName;
	private String orgId;
	private String phone;
	private String zip;
	private String linkMan;
	private String address;
	private String sealNo;
	private String faceType;
	private String sendType;
	private String index;
	private String sealMode;
	private String accCycle;
	private String modifyType;
	// 合同号
	private String contractNo;
	// private INewSealAdm newSealAdm;
	private IAccModifyBiz accModifyBiz;

	/**
	 * 初始化获取页面需要数据
	 */
	public String init() {
		this.clean();
		HttpServletRequest request = ServletActionContext.getRequest();
		XPeopleInfo people = (XPeopleInfo) request.getSession().getAttribute(
				AMSecurityDefine.XPEOPLEINFO);
		acountQueryParam = new AcountInfoQueryParam();
		SimpleOrg org = null;
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
	 * 查询所有账户信息
	 * 
	 */
	public String queryAccData() throws IOException {
		this.clean();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		try {
			queryList = this.queryAcountInfo(acountQueryParam);
		} catch (XDocProcException e) {
			errMsg = "查询账户信息出现错误";
			logger.error("查询账户信息出现错误", e);
		}
		return "initSuccess";
	}

	/**
	 * 
	 * 账户签约修改
	 * 
	 */
	public String modify() throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		XPeopleInfo people = (XPeopleInfo) request.getSession().getAttribute(
				AMSecurityDefine.XPEOPLEINFO);
		response.setCharacterEncoding("UTF-8");

		BasicInfo info = null;
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
		if (info == null) {
			logger.error("未找到需要修改的数据,可能是用户开启两个窗口所导致");
			return null;
		}
		// 判断验印账号的客户号必须跟签约账号一致
		try {
			accModifyBiz.isTheSame(info.getAccNo(), sealNo);
		} catch (XDocProcException e) {
			logger.error(e.getMessage());
			response.getWriter().write(e.getMessage());
			return null;
		}
		// 如果一致的话判断验印账号在印鉴库是否存在
		try {
			accModifyBiz.isExistSeal(sealNo);
		} catch (XDocProcException e) {
			logger.error(e.getMessage());
			response.getWriter().write(e.getMessage());
			return null;
		}
		String desc = this.updateBasicInfo(info);
		// 更新数据库
		try {
			accModifyBiz.modify(info, desc);
		} catch (XDocProcException e) {
			logger.error(e.getMessage());
			response.getWriter().write(e.getMessage());
			return null;
		}
		return null;
	}

	/***
	 * 根据索引取的对应的账户信息
	 * 
	 * @return 账户信息对象
	 * @throws IOException
	 */
	public String getBasicInfoByIndex() throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		BasicInfo info = null;
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
	 * 
	 * 新增账户签约
	 * 
	 */
	public String add() throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		XPeopleInfo people = (XPeopleInfo) request.getSession().getAttribute(
				AMSecurityDefine.XPEOPLEINFO);
		response.setCharacterEncoding("UTF-8");
		BasicInfo info = null;
		try {
			info = accModifyBiz.getOneByAccNo(accNo);
		} catch (XDocProcException e1) {
			logger.error("查询改账号是否存在时出现错误", e1);
			response.getWriter().write("查询改账号是否存在时出现错误");
			return null;
		}
		BankParam bankParam = null;
		try {
			bankParam = tools.getBankParam(orgId);
			if (bankParam == null) {
				response.getWriter().write("输入的机构号不正确，请重新输入");
				return null;
			}
		} catch (XDocProcException e1) {
			response.getWriter().write("查询所属机构信息出现错误，请重试");
			return null;
		}
		if (info != null) {
			response.getWriter().write("该账户已经存在于系统中，无法新增");
			return null;
		}
		info = new BasicInfo();
		String desc = this.updateBasicInfo(info);
		info.setAccNo(accNo);
		info.setAccName(accName);
		info.setIdBank(bankParam.getIdBank());
		info.setIdBranch(bankParam.getIdBranch());
		info.setIdCenter(bankParam.getIdCenter());
		info.setSignContractNo(contractNo);
		try {
			accModifyBiz.create(info, desc);
		} catch (XDocProcException e) {
			logger.error("新增签约失败", e);
			response.getWriter().write("新增签约失败:" + e.getMessage());
			return null;
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
				logger.error("签约账户信息导出时查询数据失败：" + e1.getMessage());
				response.getWriter().write("导出列表失败：" + e1.getMessage());
			}
			String tableName = "签约账户信息列表";
			ExportEntity entity = new ExportEntity();
			entity.setFileName(tableName);
			entity.setTitle(tableName);
			entity.setDataList(exportList);
			Map<String, String> pro_desc = new LinkedHashMap<String, String>();
			Map<String, Map<String, String>> paramsMap = new HashMap<String, Map<String, String>>();
			pro_desc.put("accNo", "账号");
			pro_desc.put("idBank", "机构号");
			pro_desc.put("accName", "户名");
			pro_desc.put("address", "地址");
			pro_desc.put("zip", "邮编");
			pro_desc.put("linkMan", "联系人");
			pro_desc.put("phone", "联系电话");
			pro_desc.put("custId", "客户号");
			pro_desc.put("sealAccNo", "验印账号");
			pro_desc.put("signContractNo", "合同号");
			pro_desc.put("signFlag", "签约状态");
			pro_desc.put("sealMode", "验印模式");
			pro_desc.put("accCycle", "对账周期");

			paramsMap.put("signFlag", contactFlagMap);
			paramsMap.put("sealMode", sealModeMap);
			paramsMap.put("accCycle", refAccCycleMap);

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
	 * 将页面传入的查询条件封装成一个map，用于查询时方便拼装查询条件
	 * 
	 * @return 条件map
	 */
	private Map<String, String> createQueryMap() {
		Map<String, String> conditions = new HashMap<String, String>();
		String accNo = acountQueryParam.getAccNo();
		String signContractNo = acountQueryParam.getContractNo();
		String custId = acountQueryParam.getCustId();
		String accCycle = acountQueryParam.getAccCycle();
		String accName = acountQueryParam.getAccName();
		String idBank = acountQueryParam.getIdBank();
	
		String signFlag = acountQueryParam.getSignFlag();
		String sealMode = acountQueryParam.getSealMode();
		
		
		this.idCenter = idCenter;
		this.idBank = idBank;
		if (accNo != null && accNo.length() > 0) {
			conditions.put("accNo", accNo);
		}
		if (signContractNo != null && signContractNo.length() > 0) {
			conditions.put("signContractNo", signContractNo);
		}
		if (signFlag != null && signFlag.length() > 0) {
			conditions.put("signFlag", signFlag);
		}
		if (sealMode != null && sealMode.length() > 0) {
			conditions.put("sealMode", sealMode);
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
		return conditions;
	}

	private String updateBasicInfo(BasicInfo info) {
		String desc = "签约维护成功!";
		if (phone != null && !phone.equals(info.getPhone())) {
			desc += "联系电话:" + info.getPhone() + "-->" + phone;
		}
		if (zip != null && !zip.equals(info.getZip())) {
			desc += "，邮编:" + info.getZip() + "-->" + zip;
		}
		if (linkMan != null && !linkMan.equals(info.getLinkMan())) {
			desc += "，联系人:" + info.getLinkMan() + "-->" + linkMan;
		}
		if (address != null && !address.equals(info.getAddress())) {
			desc += "，地址:" + info.getAddress() + "-->" + address;
		}
		if (sealNo != null && !sealNo.equals(info.getSealAccNo())) {
			desc += "，验印账号:" + info.getSealAccNo() + "-->" + sealNo;
		}
		if (sealMode != null && !sealMode.equals(info.getSealMode())) {
			desc += "，验印模式:"
					+ sealModeMap.get(info.getSealMode() == null ? "" : info
							.getSealMode()) + "-->" + sealModeMap.get(sealMode);
		}
		if (accCycle != null && !accCycle.equals(info.getAccCycle())) {
			desc += "，对账周期:"
					+ (info.getAccCycle() == null ? null : refAccCycleMap
							.get(info.getAccCycle())) + "-->"
					+ refAccCycleMap.get(accCycle);
		}
		if (contractNo != null && !contractNo.equals(info.getSignContractNo())) {
			desc += "，合同号:" + info.getSignContractNo() + "-->" + contractNo;
		}
		desc += "，维护方式:" + modifyTypeMap.get(modifyType);

		info.setPhone(phone);
		info.setZip(zip);
		info.setLinkMan(linkMan);
		info.setAddress(address);
		info.setSealAccNo(sealNo);
		// info.setFaceFlag(faceType);
		info.setSendMode(sendType);
		info.setSealMode(sealMode);
		info.setAccCycle(accCycle);
		info.setSignFlag("1"); // 标志为已签约
		info.setModifyType(modifyType);
		// 合同号
		info.setSignContractNo(contractNo);

		return desc;
	}

	/***
	 * 情况成员变量值
	 */
	private void clean() {
		queryList.clear();
		errMsg = null;
		accName = null;
		accNo = null;
		orgId = null;
		phone = null;
		zip = null;
		linkMan = null;
		address = null;
		sealNo = null;
		faceType = null;
		sendType = null;
		index = null;
		idCenter = null;
	
		idBank = null;
		modifyType = null;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String adress) {
		this.address = adress;
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

	public TreeMap<String, String> getContactFlagMap() {
		if (contactFlagMap == null || contactFlagMap.size() == 0) {
			contactFlagMap = RefTableTools.ValRefSignflagMap;
		}
		return contactFlagMap;
	}

	public void setContactFlagMap(TreeMap<String, String> contactFlagMap) {
		this.contactFlagMap = contactFlagMap;
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

	public static WFLogger getLogger() {
		return logger;
	}

	public static void setLogger(WFLogger logger) {
		AccSignAction.logger = logger;
	}

	public static SimpleDateFormat getSdf() {
		return sdf;
	}

	public static void setSdf(SimpleDateFormat sdf) {
		AccSignAction.sdf = sdf;
	}

	public String getSealMode() {
		return sealMode;
	}

	public void setSealMode(String sealMode) {
		this.sealMode = sealMode;
	}

	public String getAccCycle() {
		return accCycle;
	}

	public void setAccCycle(String accCycle) {
		this.accCycle = accCycle;
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

	public String getIdBank() {
		return idBank;
	}

	public void setIdBank(String idBank) {
		this.idBank = idBank;
	}

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
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
}
