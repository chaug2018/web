package com.yzj.ebs.ruleofacccycle.action;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
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
import com.yzj.ebs.database.RuleOfAccCycle;
import com.yzj.ebs.ruleofacccycle.biz.IRuleOfAccCycleBiz;
import com.yzj.ebs.ruleofacccycle.queryparam.QueryParam;
import com.yzj.ebs.util.FinalConstant;
import com.yzj.wf.am.security.common.AMSecurityDefine;
import com.yzj.wf.common.WFLogger;
import com.yzj.wf.core.model.po.wrapper.XPeopleInfo;
/**
 * 
 *创建于:2013-8-23<br>
 *版权所有(C) 2013 深圳市银之杰科技股份有限公司<br>
 * 账户类型定制 action
 * @author j_sun
 * @version 1.0.0
 */
public class RuleOfAccCycleAction extends ActionSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String errMsg=null;
	private String orgTree;
	private IPublicTools tools;
	private static WFLogger logger = WFLogger.getLogger(RuleOfAccCycleAction.class);
	private Map<String,String> refAccCycleMap;       //账户类型
	private IRuleOfAccCycleBiz ruleOfAccCycleImpl;   
	private Map<String,String>ValParamSubnocMap;		//科目号
	private QueryParam queryParam;		
	private Map<String, String> queryMap;	
	private List<RuleOfAccCycle> resultList = new ArrayList<RuleOfAccCycle>();

	
	public String init() {
		SimpleOrg org=null;
		cleanAll();
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
		getValParamSubnocMap();
		JSONObject json = JSONObject.fromObject(org);
		orgTree=json.toString();
		return "initSuccess";
	}
	/**
	 * 查询 定制规则 
	 * @return
	 */
	public String queryData(){
		errMsg=null;
		createQueryMap();   //查找的MAP
		try {
			resultList = ruleOfAccCycleImpl.queryRule(queryMap,queryParam,false);
		} catch (XDocProcException e) {
			errMsg="账户类型规则查询失败";
			logger.error(errMsg + e.getMessage());
		}
		return "initSuccess";
	}

	/**
	 * 增加账户
	 * ExecuteFlog 0 使用  1废弃 
	 * @throws IOException 
	 */
	public void addOrModifyData () throws IOException{
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		errMsg="";
		String modifyOrAdd = request.getParameter("modifyOrAdd");
		String index = request.getParameter("rowIndex");
		//1 为增加 规则   2 为修改规则
		
			RuleOfAccCycle rule = new RuleOfAccCycle();
			rule.setAccCycle(request.getParameter("accCycle"));
			rule.setSubjectNo(request.getParameter("subjectNo"));
			rule.setMinBal(request.getParameter("minBal"));
			rule.setMaxBal(request.getParameter("maxBal"));
			rule.setOneMinAccrual(request.getParameter("oneMinAccrual"));
			rule.setOneMaxAccrual(request.getParameter("oneMaxAccrual"));
			rule.setTotalMinAccrual(request.getParameter("totalMinAccrual"));
			rule.setTotalMaxAccrual(request.getParameter("otalMaxAccrual"));  
			
		if("1".equals(modifyOrAdd)){
			//增加规则
			try {
				ruleOfAccCycleImpl.addRule(rule);
				errMsg="addormodify";
			} catch (XDocProcException e) {
				errMsg="false";
				logger.error("账户类型配置规则 增加失败！" + e.getMessage());
			}
			
		}else{
			// 修改规则
			RuleOfAccCycle modifyRule =null;
			try{
				modifyRule= getNowRule(index);
				errMsg="addormodify";
			}catch(Exception e){
				logger.error("未在服务端缓存中找到需要修改的数据,可能是用户开启两个窗口所导致"+e.getMessage());
				errMsg="false";
			}
			rule.setAutoId(modifyRule.getAutoId());
			
			try {
				ruleOfAccCycleImpl.reviseRule(rule);
				errMsg="addormodify";
			} catch (XDocProcException e) {
				errMsg="账户类型配置规则 修改失败！";
				logger.error(errMsg + e.getMessage());
				errMsg="false";
			}	
		}	
		response.getWriter().write(errMsg);
	}
	/**
	 * 删除规则
	 * @throws IOException 
	 */
	public void deleteData() throws IOException{
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		errMsg="";
		String num1 =request.getParameter("rowNumber");
		RuleOfAccCycle rule= new RuleOfAccCycle();
		try{
			rule = getNowRule(num1);  
		}catch(Exception e){
			logger.error("未在服务端缓存中找到需要修改的数据,可能是用户开启两个窗口所导致",e.getMessage());
			errMsg="false";
		}
		try {
			ruleOfAccCycleImpl.deleteRule(rule);
			errMsg="deleteSuccess";
		} catch (XDocProcException e) {
			errMsg="删除失败";
			logger.error(errMsg + e.getMessage());
			errMsg="false";
		}
		response.getWriter().write(errMsg);
	}
	
	/**
	 *编辑界面反显 信息 
	 * @throws IOException 
	 */
	public void ruleModify() throws IOException{
		errMsg="";
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		String index = request.getParameter("index");
		RuleOfAccCycle rule= null;
		try{
			rule = getNowRule(index);  
			
			// 传回界面
			JSONObject json = JSONObject.fromObject(rule);
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
		}catch(Exception e){
			logger.error("未在服务端缓存中找到需要修改的数据,可能是用户开启两个窗口所导致");
			response.getWriter().write("false");
		}
	
	}
	
	
	/**
	 * 查询用的 queryMap
	 */
	public void createQueryMap(){
		queryMap = new TreeMap<String,String>();
		queryMap.put("acccycle", queryParam.getAccCycle());
		queryMap.put("subjectno", queryParam.getSubjectNo());	
	}
	
	/**
	 * 清空 数据
	 */
	public void cleanAll(){
		queryParam= new QueryParam();
		resultList.clear();
		
	}
	
	/**
	 * 得到 点击 编辑 或者 删除的 信息
	 * @return
	 */
	public RuleOfAccCycle getNowRule(String index){
		int num =Integer.parseInt(index.trim());
		int pageSize =queryParam.getPageSize();
		int curPage =queryParam.getCurPage()-1;
		int nowRule = num-pageSize*curPage;
		RuleOfAccCycle rule= new RuleOfAccCycle();
		rule =resultList.get(nowRule-1);
		return rule;	
	}
	

	public Map<String, String> getRefAccCycleMap() {
		if (null == refAccCycleMap || refAccCycleMap.size() == 0) {
			refAccCycleMap = RefTableTools.ValRefAccCycleMap;
		}
		return refAccCycleMap;
	}
	public Map<String, String> getValParamSubnocMap() {
		if (null == ValParamSubnocMap || ValParamSubnocMap.size() == 0) {
			ValParamSubnocMap = RefTableTools.ValParamSubnocMap;
		}
		return ValParamSubnocMap;
	}

	
	/**
	 * set  .. get..
	 */
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
	
	public void setValParamSubnocMap(Map<String, String> valParamSubnocMap) {
		ValParamSubnocMap = valParamSubnocMap;
	}
	public IRuleOfAccCycleBiz getRuleOfAccCycleImpl() {
		return ruleOfAccCycleImpl;
	}
	public void setRuleOfAccCycleImpl(IRuleOfAccCycleBiz ruleOfAccCycleImpl) {
		this.ruleOfAccCycleImpl = ruleOfAccCycleImpl;
	}
	public void setRefAccCycleMap(Map<String, String> refAccCycleMap) {
		this.refAccCycleMap = refAccCycleMap;
	}
	public QueryParam getQueryParam() {
		return queryParam;
	}
	public void setQueryParam(QueryParam queryParam) {
		this.queryParam = queryParam;
	}
	public List<RuleOfAccCycle> getResultList() {
		return resultList;
	}
	public void setResultList(List<RuleOfAccCycle> resultList) {
		this.resultList = resultList;
	}	
}
