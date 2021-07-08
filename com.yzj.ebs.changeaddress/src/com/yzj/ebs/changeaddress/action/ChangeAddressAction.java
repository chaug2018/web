package com.yzj.ebs.changeaddress.action;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import net.sf.json.JSONObject;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.yzj.ebs.changeaddress.biz.ChangeAddressServer;
import com.yzj.ebs.changeaddress.param.DataParam;
import com.yzj.ebs.changeaddress.param.DetailParam;
import com.yzj.ebs.common.IPublicTools;
import com.yzj.ebs.common.SimpleOrg;
import com.yzj.ebs.database.BasicInfo;
import com.yzj.ebs.database.FaceFlug;
import com.yzj.ebs.util.FinalConstant;
import com.yzj.wf.am.security.common.AMSecurityDefine;
import com.yzj.wf.common.WFLogger;
import com.yzj.wf.core.model.po.wrapper.XPeopleInfo;

/**
 * 
 *创建于:2013-8-29<br>
 *版权所有(C) 2013 深圳市银之杰科技股份有限公司<br>
 * 面对面 柜台 发送地址修改 action
 * @author 贺伟龙
 * @version 1.0.0
 */
/**
 * @author Administrator
 *
 */
/**
 * @author Administrator
 *
 */
public class ChangeAddressAction extends ActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static WFLogger logger = WFLogger.getLogger(ChangeAddressAction.class);
	private ChangeAddressServer changeAddressServer;
	private IPublicTools tools;
	private DataParam param;
	
	private String errMsg=null;

	private String orgTree;
	
	//分行ValueMap<分行行号，分行名字>	---供新增时前台下拉菜单用
	private Map<String,String> idCenterValueMap;
	//分行NameMap<分行名字，分行行号>		---供新增时后台获取前台分行名转化为分行行号用
	private Map<String,String> idCenterNameMap;
	
	//发送方式Map<发送方式编号，发送方式名称>
	private Map<String, String> sendModeMap;	
	//投递地址Map<投递地址编号，投递地址名称>
	private Map<String,String> addressMap;
	
	private Map<String,String> queryMap;
	//接收返回结果集
	private List<FaceFlug> faceFlugList;
	
	//BasicInfo容器，供查看明细时使用
	private List<BasicInfo> basicInfoList ;
	//明细页面的分页参数
	private DetailParam detailParam;
	//明细分页时用到的每页显示Map
	private Map<String, String> detailPageSizeMap;

	public String init() {
		SimpleOrg org=null;
		param= new DataParam();
		detailParam = new DetailParam();
		queryMap = new HashMap<String,String>();
		idCenterValueMap = new HashMap<String,String>();
		idCenterNameMap = new HashMap<String,String>();
		detailPageSizeMap=new LinkedHashMap<String,String>();
		faceFlugList = new ArrayList<FaceFlug>();
		basicInfoList = new ArrayList<BasicInfo>();
		sendModeMap = findSendModeMap();
		addressMap = findAddressMap();
		HttpServletRequest request = ServletActionContext.getRequest();
		XPeopleInfo people = (XPeopleInfo) request.getSession().getAttribute(
				AMSecurityDefine.XPEOPLEINFO);
		try {
			// 获取机构信息树
			org = tools.getCurOrgTree(people.getOrgNo());
			int level = org.getLevel();
			if(level == 1){//总行级别
				List<SimpleOrg> childOrgList = org.getChildren();
				for(SimpleOrg  childOrg : childOrgList){
					idCenterValueMap.put(childOrg.getOrgId(), childOrg.getOrgName());
					idCenterNameMap.put(childOrg.getOrgName(), childOrg.getOrgId());
				}
			}
		} catch (Exception e) {
			errMsg="获取当前机构信息列表出现错误";
			logger.error("获取当前机构信息列表出现错误",e);
		}
		JSONObject json = JSONObject.fromObject(org);
		orgTree=json.toString();
		return "initSuccess";
	}

	/**
	 * 新增发送地址
	 * @return	：成功与失败的标志
	 */
	public void addAddressData(){
		FaceFlug faceFlug = new FaceFlug();
		String idCenter = tools.getParameter("idCenter");
		String sendMode = tools.getParameter("sendMode");
		String addressFlug = tools.getParameter("sendAddress");
		//将获取到的中文信息转化为编号
		faceFlug.setIdCenter(idCenter);
		faceFlug.setSendMode(sendMode);
		faceFlug.setAddressFlug(addressFlug);
		changeAddressServer.addInfo(faceFlug);
		tools.ajaxResult("addSuccess");
	}
	
	/**
	 * 删除指定的投递地址
	 */
	public void deleteData(){
		FaceFlug faceFlug = new FaceFlug();
		String idCenter = tools.getParameter("idCenter");
		String sendMode = tools.getParameter("sendMode");
		String sendAddress = tools.getParameter("sendAddress");
		
		faceFlug.setIdCenter(idCenter);
		
		if(sendMode.equals("柜台")){
			faceFlug.setSendMode("1");
		}else if(sendMode.equals("面对面")){
			faceFlug.setSendMode("4");
		}
		if(sendAddress.equals("分行")){
			faceFlug.setAddressFlug("0");
		}else if(sendAddress.equals("网点")){
			faceFlug.setSendMode("1");
		}
		
		changeAddressServer.deleteFaceFlug(faceFlug);
		tools.ajaxResult("deleteSucess");
	}
	/**
	 * 查询要添加的投递地址是否存在
	 * @return
	 */
	public void ifExist(){
		String result = "";
		
		//获取前台查询参数，参数名需要和前台传过来的一致
		Map<String,String> queryMapTemp = new HashMap<String,String>();
		//获取前台传送的新增与修改的标签
		String idCenter = tools.getParameter("idCenter");
		String sendMode = tools.getParameter("sendMode");
		if(idCenter != null && !idCenter.equals("") ){
			queryMapTemp.put("idCenter", idCenter);
		}
		if(sendMode != null && !sendMode.equals("")){
			queryMapTemp.put("sendMode", sendMode);
		}
		int total = changeAddressServer.selectFaceFlugCount(queryMapTemp).intValue();
		//存在的记录数大于0
		if(total>0){	
			result = "exist";
		}else{
			result = "notExist";
		}
		
		tools.ajaxResult(result);
	}
	
	/**
	 * 得到 更新用的 MAP
	 * @param param
	 * @return
	 */
	public Map<String ,String> getMap(DataParam param ){
		Map<String,String> updateMap = new HashMap<String,String>();
		updateMap.put("idCenter", param.getIdCenter());
		updateMap.put("sendMode", param.getSendMode());
		updateMap.put("addressFlug", param.getSendAddress());
		return updateMap;
	}
	
	/**
	 * 分页查询投递地址
	 * @param param	对账中心、发送方式、投递地址等信息；
	 * @return
	 */
	public String queryData(){
		try {
			queryMap.clear();
			faceFlugList = new ArrayList<FaceFlug>();
			
			String idCenter = tools.getParameter("param.idCenter");
			String sendMode = tools.getParameter("param.sendMode");
			//保存前台的下拉菜单选择
			param.setIdCenter(idCenter);
			param.setSendMode(sendMode);
			if(idCenter != null && !idCenter.equals("") ){
				queryMap.put("idCenter", idCenter);
			}
			if(sendMode != null && !sendMode.equals("")){
				queryMap.put("sendMode", sendMode);
			}
			int total = changeAddressServer.selectFaceFlugCount(queryMap).intValue();
			faceFlugList = changeAddressServer.selectFaceFlugRecords(queryMap,param);
			//总页数
			int totalPage=0;
			//当前页数
			int curPage = param.getCurPage();
			//每页显示多少条
			int pageSize = param.getPageSize();
			if(total%pageSize==0){	
				//能整除，则页数刚好可以显示完
				totalPage=total/pageSize;
			}else{					
				//不能整除，说明还要多一页才能显示完
				totalPage=total/pageSize+1;
			}
			
			//当前页第一条数据
			int firstResult =0;
			//当前页最后一条数据
			int lastResult=0;
			if(totalPage>0){
				firstResult = (curPage-1)*pageSize+1;
				if(curPage==totalPage){
					lastResult=total;
				}else{
					lastResult=firstResult+pageSize-1;
				}
			}
			
			//从新设置分页参数
			param.setTotal(total);
			param.setFirstResult(firstResult);
			param.setLastResult(lastResult);
			param.setTotalPage(totalPage);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "initSuccess";
	}
	
	/**
	 * 
	 * @return	跳转页面的标记，配置在struts-ebs.xml中：<result name="queryDetailInfo">/ebs/address/changeaddress_DetailInfo.jsp</result>
	 */
	public String queryBasicInfo(){
		/*
		 * 处理逻辑：
		 * 1、获取前台传过来的对账中心、发送方式2个参数，并将他们存储到分页参数对象中的idCenter和sendMode这2个属性中
		 * 2、将这2个参数组装成queryMap；
		 * 3、修改前台获取到的分页参数，并进行处理
		 * 4、调用后台分页查询方法changeAddressServer.selectBasicInfoRecords(tempQueryMap,detailParam)查询数据并返回；
		 * 5、分页处理：修改分页参数
		 * 5、返回跳转页面的标签；
		 */
		String idCenter = tools.getParameter("idCenter");
		String sendMode = tools.getParameter("sendMode");
		//获取主页面和二层页面传过来的是否初始化分页参数的标签
		String flag = tools.getParameter("flag");
		if(flag.equals("true")){
			detailParam = new DetailParam();
		}else if(flag.equals("false")){
			detailParam=this.getDetailParam();
		}
		
		Map<String,String> tempQueryMap = new HashMap<String,String>();
		if(idCenter == null || idCenter.equals("")){
			idCenter = detailParam.getDetailIDCenter();
			tempQueryMap.put("idCenter", idCenter);
		}else{	
			//不为空的情况为第一次"查看"，弹出二层页面，此时，将对账中心和发送方式2个参数存储在detailParam对象中
			detailParam.setDetailIDCenter(idCenter);
			tempQueryMap.put("idCenter", idCenter);
		}
		if(sendMode == null || sendMode.equals("")){
			sendMode = detailParam.getDetailSendMode();
			tempQueryMap.put("sendMode", sendMode);
		}else{
			//不为空的情况为第一次"查看"，弹出二层页面，此时，将对账中心和发送方式2个参数存储在detailParam对象中
			if(sendMode.equals("1")){
				tempQueryMap.put("sendMode", "1");
				detailParam.setDetailSendMode("1");
			}else if(sendMode.equals("4")){
				tempQueryMap.put("sendMode", "4");
				detailParam.setDetailSendMode("4");
			}
		}
		
		/*
		 *1、初始的分页参数：
		 *	1.1、每页显示数：pageSize=10
		 *	1.2、总记录数：total=0
		 *	1.3、当前页：curPage=1
		 *	1.4、总页数：totalPage=1
		 *	1.5、当前页最后一条记录：lastResult=0
		 */
		int total = changeAddressServer.selectBasicInfoCount(tempQueryMap).intValue();
		basicInfoList.clear();
		basicInfoList = changeAddressServer.selectBasicInfoRecords(tempQueryMap,detailParam);
		//总页数
		int totalPage=0;
		//当前页数
		int curPage = detailParam.getCurPage();
		//每页显示多少条
		int pageSize = detailParam.getPageSize();
		if(total%pageSize==0){	//能整除，则页数刚好可以显示完
			totalPage=total/pageSize;
		}else{					//不能整除，说明还要多一页才能显示完
			totalPage=total/pageSize+1;
		}
		
		//当前页第一条数据
		int firstResult =0;
		//当前页最后一条数据
		int lastResult=0;
		if(totalPage>0){
			firstResult = (curPage-1)*pageSize+1;
			if(curPage==totalPage){
				lastResult=total;
			}else{
				lastResult=firstResult+pageSize-1;
			}
		}
		
		//从新设置分页参数
		detailParam.setTotal(total);
		detailParam.setFirstResult(firstResult);
		detailParam.setLastResult(lastResult);
		detailParam.setTotalPage(totalPage);
	
		return "queryDetailInfo";
	}
	
	public ChangeAddressServer getChangeAddressServer() {
		return changeAddressServer;
	}

	public void setChangeAddressServer(ChangeAddressServer changeAddressServer) {
		this.changeAddressServer = changeAddressServer;
	}

	/**
	 * 查找发送方式的键值对，去除不需要的邮寄和网银
	 * @return
	 */
	public Map<String,String> findSendModeMap(){
		Map<String, String> sendModeMapTemp=new TreeMap<String, String>();
		sendModeMapTemp=FinalConstant.sendModelName;
		sendModeMapTemp.remove("2");
		sendModeMapTemp.remove("3");
		return sendModeMapTemp;
	}

	
	/**
	 *初始化投递地址Map，用于将编号转化为中文名称
	 * @return
	 */
	public Map<String,String> findAddressMap(){
		Map<String, String> addressMapTemp=new TreeMap<String, String>();
		addressMapTemp.put("0", "分行");
		addressMapTemp.put("1", "网点");
		return addressMapTemp;
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
	public ChangeAddressServer getchangeAddressServer() {
		return changeAddressServer;
	}
	public void setchangeAddressServer(ChangeAddressServer changeAddressServer) {
		this.changeAddressServer = changeAddressServer;
	}
	public DataParam getParam() {
		return param;
	}
	public void setParam(DataParam param) {
		this.param = param;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public Map<String, String> getQueryMap() {
		return queryMap;
	}

	public void setQueryMap(Map<String, String> queryMap) {
		this.queryMap = queryMap;
	}

	public List<FaceFlug> getFaceFlugList() {
		return faceFlugList;
	}

	public void setFaceFlugList(List<FaceFlug> faceFlugList) {
		this.faceFlugList = faceFlugList;
	}

	public Map<String, String> getSendModeMap() {
		return sendModeMap;
	}

	public void setSendModeMap(Map<String, String> sendModeMap) {
		this.sendModeMap = sendModeMap;
	}

	public Map<String, String> getAddressMap() {
		return addressMap;
	}

	public void setAddressMap(Map<String, String> addressMap) {
		this.addressMap = addressMap;
	}

	public Map<String, String> getIdCenterValueMap() {
		return idCenterValueMap;
	}

	public void setIdCenterValueMap(Map<String, String> idCenterValueMap) {
		this.idCenterValueMap = idCenterValueMap;
	}

	public Map<String, String> getIdCenterNameMap() {
		return idCenterNameMap;
	}

	public void setIdCenterNameMap(Map<String, String> idCenterNameMap) {
		this.idCenterNameMap = idCenterNameMap;
	}

	public List<BasicInfo> getBasicInfoList() {
		return basicInfoList;
	}

	public void setBasicInfoList(List<BasicInfo> basicInfoList) {
		this.basicInfoList = basicInfoList;
	}
	
	public DetailParam getDetailParam() {
		return detailParam;
	}

	public void setDetailParam(DetailParam detailParam) {
		this.detailParam = detailParam;
	}

	public Map<String, String> getDetailPageSizeMap() {
		return detailPageSizeMap;
	}

	public void setDetailPageSizeMap(Map<String, String> detailPageSizeMap) {
		this.detailPageSizeMap = detailPageSizeMap;
	}

//	public String getIfInitDetailParam() {
//		return ifInitDetailParam;
//	}
//
//	public void setIfInitDetailParam(String ifInitDetailParam) {
//		this.ifInitDetailParam = ifInitDetailParam;
//	}
	
}
