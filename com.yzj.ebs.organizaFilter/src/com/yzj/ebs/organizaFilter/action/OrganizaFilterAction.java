package com.yzj.ebs.organizaFilter.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import net.sf.json.JSONObject;
import org.apache.struts2.ServletActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.yzj.ebs.common.SimpleOrg;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.database.OrganizaFilter;
import com.yzj.ebs.impl.PublicToolsImpl;
import com.yzj.ebs.organizaFilter.param.OrganizaFilterParam;
import com.yzj.ebs.organizaFilter.server.OrganizaFilterServer;
import com.yzj.wf.am.security.common.AMSecurityDefine;
import com.yzj.wf.common.WFLogger;
import com.yzj.wf.core.model.po.wrapper.XPeopleInfo;

public class OrganizaFilterAction extends ActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static WFLogger logger = WFLogger.getLogger(OrganizaFilterAction.class);
	private OrganizaFilterServer organizaFilterServer;
	private PublicToolsImpl tools;
	private List<OrganizaFilter> queryList;
	private OrganizaFilter organizaFilter;
	private OrganizaFilterParam param;
	private String orgTree;
	/**
	 * 模块初始化
	 * @return
	 */
	public String init(){
		queryList=new ArrayList<OrganizaFilter>();
		param=new OrganizaFilterParam();
		HttpServletRequest request = ServletActionContext.getRequest();
		XPeopleInfo people = (XPeopleInfo) request.getSession().getAttribute(
				AMSecurityDefine.XPEOPLEINFO);
		SimpleOrg org = null;
		try {
			org = tools.getCurOrgTree(people.getOrgNo());
		} catch (XDocProcException e) {
			logger.error("获取当前机构信息列表出现错误", e);
		}
		JSONObject json = JSONObject.fromObject(org);
		orgTree = json.toString();
		return "goOrganizaFilter";
	}
	
	/**
	 * 分页查询数据
	 * @return
	 */
	public String queryData(){
		Map<String,String>queryMap=new HashMap<String, String>();
		String idBank =tools.getParameter("param.idBank");
		String idCenter =tools.getParameter("param.idCenter");
		param.setIdBank(idBank);
		param.setIdCenter(idCenter);
		if(idBank!=null&&idBank!=""){
			queryMap.put("idBank", idBank);
		}
		if(idCenter!=null&&idCenter!=""){
			queryMap.put("idCenter", idCenter);
		}
		
		int total = organizaFilterServer.selectCount(queryMap);
		queryList=organizaFilterServer.selectRecords(queryMap, param);
		//总页数
		int totalPage=0;
		//当前页数
		int curPage = param.getCurPage();
		//每页显示多少条
		int pageSize = param.getPageSize();
		if(total%pageSize==0){
			totalPage=total/pageSize;
		}else{
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
		return "goOrganizaFilter";
	}
	
	/**
	 * 检查机构是否已经维护
	 */
	public void checkBankIsExist(){
		String editIdBank=tools.getParameter("editIdBank");
		OrganizaFilter OrganizaFilter=organizaFilterServer.findOrgFilterInfoByIdBank(editIdBank);
		if(OrganizaFilter!=null){
			tools.ajaxResult("exist");
		}else{
			Map<String,String> bankInfoMap =organizaFilterServer.findBankInfoByIdBank(editIdBank);
			if(bankInfoMap.size()==0){
				tools.ajaxResult("notFind");
			}else{
				tools.ajaxResult("checkSucess");
			}
		}
	}
	
	/**
	 * 通过机构号得到机构的信息
	 */
	public void findBankInfoByIdBank(){
		String editIdBank=tools.getParameter("editIdBank");
		Map<String,String> map=new HashMap<String,String>();
		Map<String,String> bankInfoMap =organizaFilterServer.findBankInfoByIdBank(editIdBank);
		if(bankInfoMap.size()>0){
			map.put("editBankName", bankInfoMap.get("bankName"));
			map.put("editIdBranch", bankInfoMap.get("idBranch"));
			map.put("editIdCenter", bankInfoMap.get("idCenter"));
			JSONObject json= JSONObject.fromObject(map);
			tools.ajaxResult(json.toString());
		}
	}
	
	
	/**
	 * 删除数据
	 * @return
	 */
	public void deleteData(){
		String editIdBank=tools.getParameter("idBank").trim();
		OrganizaFilter organizaFilter=organizaFilterServer.findOrgFilterInfoByIdBank(editIdBank);
		if(organizaFilter!=null){
			organizaFilterServer.deleteOrganizaFilter(organizaFilter);
			tools.ajaxResult("deleteSucess");
		}else{
			tools.ajaxResult("删除异常");
		}
	}
	
	/**
	 * 保存数据
	 */
	public void saveData(){
		String addOrEdit=tools.getParameter("addOrEdit");
		String editIdBank=tools.getParameter("editIdBank");
		String editBankName=tools.getParameter("editBankName");
		String editIdBranch=tools.getParameter("editIdBranch");
		String editIdCenter=tools.getParameter("editIdCenter");
		OrganizaFilter organizaFilter=new OrganizaFilter();
		if(addOrEdit.equals("edit")){
			organizaFilter=organizaFilterServer.findOrgFilterInfoByIdBank(editIdBank);
		}
		organizaFilter.setIdBank(editIdBank);
		organizaFilter.setBankName(editBankName);
		organizaFilter.setIdBranch(editIdBranch);
		organizaFilter.setIdCenter(editIdCenter);
		organizaFilterServer.saveOrUpdate(organizaFilter);
		tools.ajaxResult("saveSucess");
	}
	
	
	public OrganizaFilterServer getOrganizaFilterServer() {
		return organizaFilterServer;
	}

	public void setOrganizaFilterServer(OrganizaFilterServer organizaFilterServer) {
		this.organizaFilterServer = organizaFilterServer;
	}

	public PublicToolsImpl getTools() {
		return tools;
	}

	public void setTools(PublicToolsImpl tools) {
		this.tools = tools;
	}

	public List<OrganizaFilter> getQueryList() {
		return queryList;
	}

	public void setQueryList(List<OrganizaFilter> queryList) {
		this.queryList = queryList;
	}

	public OrganizaFilter getOrganizaFilter() {
		return organizaFilter;
	}

	public void setOrganizaFilter(OrganizaFilter organizaFilter) {
		this.organizaFilter = organizaFilter;
	}
	
	public OrganizaFilterParam getParam() {
		return param;
	}

	public void setParam(OrganizaFilterParam param) {
		this.param = param;
	}

	public String getOrgTree() {
		return orgTree;
	}

	public void setOrgTree(String orgTree) {
		this.orgTree = orgTree;
	}
	
	
}
