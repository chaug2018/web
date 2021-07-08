package com.yzj.ebs.blackwhite.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URLEncoder;
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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.opensymphony.xwork2.ActionSupport;
import com.yzj.ebs.blackwhite.biz.IBlackWhiteBiz;
import com.yzj.ebs.blackwhite.queryparam.BWQueryParam;
import com.yzj.ebs.common.IPublicTools;
import com.yzj.ebs.common.RefTableTools;
import com.yzj.ebs.common.SimpleOrg;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.database.BasicInfo;
import com.yzj.wf.am.security.common.AMSecurityDefine;
import com.yzj.wf.common.WFLogger;
import com.yzj.wf.core.model.po.wrapper.XPeopleInfo;
import com.yzj.wf.export.common.ExportEntity;
import com.yzj.wf.export.service.DataExporterImpl;

/**
 * 
 * 创建于:2012-10-17<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 特殊账户维护维护
 * 
 * @author ShiJiangmin
 * @version 1.0.0
 */
public class BlackWhiteAction extends ActionSupport {

	private static final long serialVersionUID = -6798089937398488203L;
	private static SimpleDateFormat matter1 = new SimpleDateFormat("yyyyMMdd");
	private static SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	private List<BasicInfo> queryList = new ArrayList<BasicInfo>();
	private BWQueryParam bwQueryParam;
	private Map<String, String> queryMap;
	private Map<String, String> refIsCheck =new  HashMap<String, String>();
	private TreeMap<String, String> refAccCycleMap = new TreeMap<String, String>();
	private static WFLogger logger = WFLogger.getLogger(BlackWhiteAction.class);
	private String errMsg=null;
	private String orgTree;
	private IPublicTools tools;
	private IBlackWhiteBiz blackWhiteBizImpl;
	
	//以下是维护特殊账户时的输入项
	private String batchSpecialFlag;
	private String selectIds;	//批量修改id，对应行号
	private String accCycle;
	private String index;
	private String accNo;
	
	//查询下拉框的值
	private String idCenter;
	private String idBank;
	//批量上传文件的路径
	private File upFile;
	private String msg="";
	
	private TreeMap<String, String> sealModeMap = new TreeMap<String, String>();
	private TreeMap<String, String> sendTypeMap = new TreeMap<String, String>();
	
	
	
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
	private String modifyType;
	private String isCheck;
	


	/**
	 * 
	 * 初始化界面
	 * 
	 * @return
	 */
	public String init() {
		
		queryList.clear();
	    idCenter = null;
	    idBank = null;
		errMsg=null;
		bwQueryParam = new BWQueryParam();
		refIsCheck.clear();
		SimpleOrg org=null;
		HttpServletRequest request = ServletActionContext.getRequest();
		XPeopleInfo people = (XPeopleInfo) request.getSession().getAttribute(
				AMSecurityDefine.XPEOPLEINFO);
		try {
			// 获取机构信息树
			org = tools.getCurOrgTree(people.getOrgNo());
		} catch (XDocProcException e) {
			errMsg="获取当前机构信息列表出现错误";
			logger.error("获取当前机构信息列表出现错误",e);
		}
		refIsCheck = tools.getRefIsCheck();
		JSONObject json = JSONObject.fromObject(org);
		orgTree=json.toString();
		getRefAccCycleMap();
		return "initSuccess";
	}

	/**
	 * 查询特殊账户
	 * @return
	 * @throws IOException
	 */
	public String getQueryData(){
		errMsg=null;
		queryList.clear();
		queryMap = createQueryMap();
	    idCenter = bwQueryParam.getidCenter();
	    idBank =  bwQueryParam.getIdBank();
		try {
			queryList = blackWhiteBizImpl.getBasicinfoData(queryMap,(PageParam)bwQueryParam,true);
			for(int i=0;i<queryList.size();i++){
				BasicInfo info = queryList.get(i);
				String datetmp = info.getSignTime();
				if(datetmp!=null&&datetmp.trim().length()>0){
					String date = datetmp.substring(0,4)+"-"+datetmp.substring(4,6)+"-"+datetmp.substring(6);
					info.setSignTime(date);
				}
			}
		} catch (Exception e) {
			logger.error("特殊账户查询失败", e);
			errMsg="特殊账户查询失败";
		}
		return "initSuccess";
	}

	// 根据索引获取账户信息
	public String getBasicInfoByIndex(){
		try {
			BasicInfo info = null;
			if (queryList.get(Integer.valueOf(index) - 1).getAccNo().equals(accNo.trim())) {
				info = (queryList.get(Integer.valueOf(index) - 1)); // 直接根据下标获取
			} else { // 若数据顺序出现错乱,则循环遍历
				for (int i = 0; i < queryList.size(); i++) {
					if (queryList.get(i).getAccNo().equals(accNo.trim())) {
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
			tools.ajaxResult(json.toString());
		} catch (Exception e) {
			logger.error("获取json对象出现错误", e);
		}
		return null;
	}


	/**
	 * 导出数据
	 * @return
	 * @throws IOException 
	 */
	public void exportData(){
		try {
			List<BasicInfo> exportList = new ArrayList<BasicInfo>();
			queryMap = createQueryMap();
			exportList =  blackWhiteBizImpl.getBasicinfoData(queryMap,(PageParam)bwQueryParam,false);	//全量查询
			if(exportList.size()==0||exportList==null){
				tools.ajaxResult("查询列表为空");
			}else{
				ExportEntity entity=new ExportEntity();
				entity.setFileName("特殊账户列表");
				entity.setTitle("特殊账户列表");
				entity.setDataList(exportList);
				Map<String,String> pro_desc=new LinkedHashMap<String,String>();
				Map<String,Map<String,String>> paramsMap=new HashMap<String,Map<String,String>>();
				pro_desc.put("accNo", "签约账号");
				pro_desc.put("idBank", "机构号");
				pro_desc.put("accName", "账户名称");
				pro_desc.put("sendAddress", "邮寄地址");
				pro_desc.put("zip", "邮编");
				pro_desc.put("phone", "联系电话");
				pro_desc.put("linkMan", "联系人");
				pro_desc.put("signTime", "维护时间");
				pro_desc.put("custId", "客户号");	
				//	pro_desc.put("accType", "账户类型");
				//	paramsMap.put("specialFlag", refBlackWhiteFlagMap);
				entity.setPro_desc_map(pro_desc);
				entity.setParamsMap(paramsMap);
			    new DataExporterImpl().export(entity);
		    }
		} catch (Exception e) {
			logger.error("导出列表失败：" + e.getMessage());
			tools.ajaxResult("导出列表失败：" + e.getMessage());
		}
	}

	/**
	 * 逐笔维护特殊账户，更新BasicInfo及日志
	 * @return
	 * @throws IOException
	 * @throws XDocProcException 
	 */
	public String submitModify()throws IOException, XDocProcException{
		
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
			logger.error("未找到需要修改的数据,可能是用户开启两个窗口所导致");
			response.getWriter().write("修改失败，未找到对应的数据,请尝试刷新页面");
		}
		
		String desc = this.updateBasicInfo(info);
		// 更新数据库
		try {
			blackWhiteBizImpl.modifyByOne(info, desc);
		} catch (Exception e) {
			logger.error(e.getMessage());
			response.getWriter().write(e.getMessage());		
		}
		return null;
	}
	
	
	private String updateBasicInfo(BasicInfo info) {
		
		//1.封装日志信息
		String desc = "账号编辑维护成功!";
		if(phone!=null&&!phone.equals(info.getPhone())){
			desc+="联系电话:"+info.getPhone()+"-->"+phone;
		}
		if(zip!=null&&!zip.equals(info.getZip())){
			desc+="，邮编:"+info.getZip()+"-->"+zip;
		}
		if(linkMan!=null&&!linkMan.equals(info.getLinkMan())){
			desc+="，联系人:"+info.getLinkMan()+"-->"+linkMan;
		}
		if(sendAddress!=null&&!sendAddress.equals(info.getAddress())){
			desc+="，地址:"+info.getAddress()+"-->"+sendAddress;
		}
		if(sealNo!=null&&!sealNo.equals(info.getSealAccNo())){
			desc+="，验印账号:"+info.getSealAccNo()+"-->"+sealNo;
		}
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
		
		//设置页面传来的值
		if(phone!=null){
			info.setPhone(phone); //联系电话
		}
		if(zip!=null){
			info.setZip(zip); //邮编
		}
		if(linkMan!=null){
			info.setLinkMan(linkMan);//联系人
		}
		if(sendAddress!=null){
			info.setSendAddress(sendAddress); //邮寄地址
			info.setAddress(sendAddress);//地址
		}
		if(sendType!=null){
			info.setSendMode(sendType); //发送方式
			if("3".equals(sendType)){
				info.setSignFlag("0");//柜面签约网银
			}
		}
		if(sealMode!=null){
			info.setSealMode(sealMode); //验印模式
		}
		if(accCycle!=null){
			info.setAccCycle(accCycle); //账户类型
		}
		if(isCheck!=null){
			info.setIsCheck(isCheck);//是否对账
		}
		if(sealNo!=null){
			info.setSealAccNo(sealNo);//验印账号
		}
		return desc;
	}
	
	
	
	/**
	 * 批量维护特殊账户。更新BasicInfo及日志
	 * @return
	 * @throws IOException
	 * @throws XDocProcException 
	 */
	public String batchModify()throws IOException, XDocProcException{
		String[] selectIdList = selectIds.split(",");
		for(int i = 0;i<selectIdList.length;i++){
			String index = selectIdList[i];	
			if(index!=""&&index.trim().length()>0){
				this.modifyByOne(index,batchSpecialFlag);
			}else{
				continue;
			}
		}
		return null;
	}
	
	/**
	 * 修改单个账号的特殊账户
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws XDocProcException 
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public String modifyByOne(String index,String accCycle) throws IOException, XDocProcException{
		BasicInfo info=new BasicInfo();
		info=queryList.get(Integer.valueOf(index.trim())-1);
		if(info==null){
			logger.error("未找到需要修改的数据,可能是用户开启两个窗口所导致");
			return null;
		}
		info.setAccCycle(accCycle);
		blackWhiteBizImpl.modifyByOne(info, index);
		return null;
	}


	/**
	 * 根据BWQueryParam生成查询Map
	 * @return
	 */
	public Map<String,String> createQueryMap(){
		queryMap = new TreeMap<String,String>();
		String idBank = bwQueryParam.getIdBank();
		String idCenter = bwQueryParam.getidCenter();
		this.idCenter=idCenter;
		this.idBank=idBank;
		if (idBank != null && idBank.length() > 0){
			queryMap.put("idBank", idBank);
		}
		 if (idCenter != null && idCenter.length() > 0){
			queryMap.put("idCenter", idCenter);
		}
		String signTimeTmp = bwQueryParam.getSignTime();
		String signTime = "";
		if(signTimeTmp.trim().length()>0){
			signTime = signTimeTmp.substring(0, 4)+signTimeTmp.substring(5, 7)+signTimeTmp.substring(8);
		}else{
		}
		queryMap.put("signTime", signTime);
		queryMap.put("custId", bwQueryParam.getCustId());
		queryMap.put("accNo", bwQueryParam.getAccNo());
		queryMap.put("accName", bwQueryParam.getAccName());
		queryMap.put("accCycle", bwQueryParam.getAccCycle());
		queryMap.put("ischeck", bwQueryParam.getIscheck());
		queryMap.put("isspecile", "1");
		return queryMap;
	}
	/**
	 * 读取excel文件  批量维护特殊账户
	 * @return
	 */
	public void excelBatchInput(){
		String flag = "";
		try {
			flag = blackWhiteBizImpl.completeBatchInput(upFile);
		} catch (XDocProcException e) {
			// 读取 excel 失败
			flag = "特殊账户维护模块上传失败";
			logger.error(flag,e);
		}
		if(flag.equals("")){
			tools.ajaxResult("导入成功！");
		}else{
			tools.ajaxResult(flag);
		}
	}
	/**
	 * 删除操作  
	 * @return
	 */
	public void deleteInfor() throws IOException{
	
		HttpServletRequest request = ServletActionContext.getRequest();
		String deleteaccNo = request.getParameter("postData");
		try {
			blackWhiteBizImpl.deleteInfor(deleteaccNo);
		}catch (XDocProcException e) {
			//删除失败
			msg="特殊帐户信息删除失败！";
			logger.error(msg,e);
			tools.ajaxResult("false");
		}
		//删除成功，返回前页面
		tools.ajaxResult("deleteSuccess");
	}
	
	
	/**
	 * 下载批量导入模板
	 * 
	 * @return
	 * @throws IOException
	 */
	public String downloadTemplete() throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		String path;
		try {
			path = this.getClass().getClassLoader().getResource("").toURI().getPath();
		} catch (URISyntaxException e1) {
			path="template";
		}
		if(path.contains("classes")){
			path = path.replace("classes", "template");
		}
		String fileName = "特殊账户维护模板 .xls";
		String filenamedownload = path + fileName;
		// 设置输出的格式
		fileName = URLEncoder.encode(fileName, "UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.reset();
		response.setContentType("application/x-download");
		response.addHeader("Content-Disposition", "attachment;filename=\""
				+ fileName);
		// 循环取出流中的数据
		OutputStream output = null;
		FileInputStream fis = null;
		try {
			output = response.getOutputStream();
			fis = new FileInputStream(filenamedownload);
			byte[] b = new byte[1024];
			int i = 0;
			while ((i = fis.read(b)) > 0) {
				output.write(b, 0, i);
			}
			output.flush();
		} catch (Exception e) {
			msg = "特殊账户维护模板 下载失败";
			logger.error(msg,e);
			return null;
		} finally {
			if (fis != null) {
				fis.close();
				fis = null;
			}
			if (output != null) {
				output.close();
				output = null;
			}
		}
		return null;
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
	
	public List<BasicInfo> getQueryList() {
		return queryList;
	}

	public void setQueryList(List<BasicInfo> queryList) {
		this.queryList = queryList;
	}

	public BWQueryParam getBwQueryParam() {
		return bwQueryParam;
	}

	public void setBwQueryParam(BWQueryParam bwQueryParam) {
		this.bwQueryParam = bwQueryParam;
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


	
	public String getAccCycle() {
		return accCycle;
	}

	public void setAccCycle(String accCycle) {
		this.accCycle = accCycle;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}



	public String getSelectIds() {
		return selectIds;
	}

	public void setSelectIds(String selectIds) {
		this.selectIds = selectIds;
	}

	public String getBatchSpecialFlag() {
		return batchSpecialFlag;
	}

	public void setBatchSpecialFlag(String batchSpecialFlag) {
		this.batchSpecialFlag = batchSpecialFlag;
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

	public String getAccNo() {
		return accNo;
	}

	public void setAccNo(String accNo) {
		this.accNo = accNo;
	}

	public static SimpleDateFormat getMatter1() {
		return matter1;
	}

	public static void setMatter1(SimpleDateFormat matter1) {
		BlackWhiteAction.matter1 = matter1;
	}

	public static SimpleDateFormat getSdf() {
		return sdf;
	}

	public static void setSdf(SimpleDateFormat sdf) {
		BlackWhiteAction.sdf = sdf;
	}

	public IBlackWhiteBiz getBlackWhiteBizImpl() {
		return blackWhiteBizImpl;
	}

	public void setBlackWhiteBizImpl(IBlackWhiteBiz blackWhiteBizImpl) {
		this.blackWhiteBizImpl = blackWhiteBizImpl;
	}
	public File getUpFile() {
		return upFile;
	}

	public void setUpFile(File upFile) {
		this.upFile = upFile;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
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

	public TreeMap<String, String> getSealModeMap() {
		if (sealModeMap == null || sealModeMap.size() == 0) {
			sealModeMap = RefTableTools.ValRefSealModeMap;
		}
		return sealModeMap;
	}

	public void setSealModeMap(TreeMap<String, String> sealModeMap) {
		this.sealModeMap = sealModeMap;
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
	
	public String getIsCheck() {
		return isCheck;
	}

	public void setIsCheck(String isCheck) {
		this.isCheck = isCheck;
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

	public String getSealMode() {
		return sealMode;
	}

	public void setSealMode(String sealMode) {
		this.sealMode = sealMode;
	}

	public String getModifyType() {
		return modifyType;
	}

	public void setModifyType(String modifyType) {
		this.modifyType = modifyType;
	}
}
