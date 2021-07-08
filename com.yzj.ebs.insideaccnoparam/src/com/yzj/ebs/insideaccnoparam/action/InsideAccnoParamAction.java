package com.yzj.ebs.insideaccnoparam.action;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.yzj.ebs.common.IPublicTools;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.database.InsideAccnoParam;
import com.yzj.ebs.insideaccnoparam.biz.InsideAccnoParamBiz;
import com.yzj.ebs.insideaccnoparam.pojo.QueryParam;
import com.yzj.wf.common.WFLogger;

/**
 * 内部账户关联
 * @author Administrator
 *
 */
public class InsideAccnoParamAction extends ActionSupport{
	
	private static final long serialVersionUID = 1L;
	private IPublicTools tools;
	private static WFLogger logger = WFLogger.getLogger(InsideAccnoParamAction.class);
	private InsideAccnoParamBiz insideAccnoParamImpl;   
	private List<InsideAccnoParam> queryList = new ArrayList<InsideAccnoParam>();
	private Map<String, String> queryMap;
	private QueryParam queryParam;
	
	public String init(){
		queryParam=new QueryParam();
		queryList.clear();
		
		return "initSuccess";
	}

	/**
	 *   查询内部账户对账配置信息
	 * @return
	 */
	public String queryData(){
		queryMap = new TreeMap<String,String>();
		String accno = queryParam.getAccno();
		String custId = queryParam.getCustId();
		String recheckCustId = queryParam.getRecheckCustId();
		
		if(accno!=null && accno.trim().length()>0){
			queryMap.put("insideaccno", accno.trim());
		}
		if(custId!=null && custId.length()>0){
			queryMap.put("insideuser", custId);
		}
		if(recheckCustId!=null && recheckCustId.length()>0){
			queryMap.put("insiderecheckuser", recheckCustId);
		}
		try {
			queryList = insideAccnoParamImpl.queryInsideInfor(queryMap, queryParam);
		} catch (XDocProcException e) {
			logger.error("数据库出错！",e);
		}
		return "initSuccess";
	}
	
	
	/**
	 *   删除内部账户对账配置信息
	 * @return
	 * @throws IOException 
	 */
	public String deleteInside() throws IOException{
		HttpServletResponse response = ServletActionContext.getResponse();
		String accNo = tools.getParameter("accNo");
		String custId = tools.getParameter("custId");
		String recheckCustId = tools.getParameter("recheckCustId");
		String msg = "success";
		try {
			insideAccnoParamImpl.deleteInsideInfor(accNo, custId,recheckCustId);
		} catch (SQLException e) {
			logger.error("数据库出错！",e);
			msg = "false";
		}
		response.getWriter().write(msg);
		return null;
	}
	
	/**
	 * 增加内部账户配置信息
	 * @return  返回 -1 数据库出错  1 添加成功  0 账号不存在
	 * @throws IOException 
	 */
	public void addOrModifyInseide() throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		//修改后的accno
		String accNo1 = tools.getParameter("accNo_div1");
		String custId = tools.getParameter("custId");
		String recheckCustId = tools.getParameter("recheckCustId");
		// 操作标示
		String flog_div = tools.getParameter("flog_div");
		int flog=1;
		try {
			//1:成功 2:该条记录已存在  0:账号不存在  -1:数据库出错  4 custId不存在  5 recheckCustId不存在   6 custId和recheckCustId不是同一部门
			if(flog_div.equals("add")){
				//增加操作  1 成功  0 记录已存在
				flog = insideAccnoParamImpl.inputInsideInfor(accNo1, custId,recheckCustId);
			}else{//已废除
				//原来的 accNo
				String accNo2 = tools.getParameter("accNo_div2");
				//编辑操作  1 成功  0 记录已存
				flog = insideAccnoParamImpl.changeInsideInfor(accNo1, custId,accNo2);
			}
		} catch (Exception e) {
			flog =-1;
			logger.error("数据库出错！",e);
		}	
		response.getWriter().write(String.valueOf(flog));
		
	}
	
	
	public IPublicTools getTools() {
		return tools;
	}
	
	public void setTools(IPublicTools tools) {
		this.tools = tools;
	}
	
	public InsideAccnoParamBiz getInsideAccnoParamImpl() {
		return insideAccnoParamImpl;
	}

	public void setInsideAccnoParamImpl(InsideAccnoParamBiz insideAccnoParamImpl) {
		this.insideAccnoParamImpl = insideAccnoParamImpl;
	}
	
	public List<InsideAccnoParam> getQueryList() {
		return queryList;
	}

	public void setQueryList(List<InsideAccnoParam> queryList) {
		this.queryList = queryList;
	}

	public QueryParam getQueryParam() {
		return queryParam;
	}

	public void setQueryParam(QueryParam queryParam) {
		this.queryParam = queryParam;
	}
	
}
