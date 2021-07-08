package com.yzj.ebs.insideaccnoparam.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.yzj.ebs.common.IPublicTools;
import com.yzj.ebs.database.InnerAccno;
import com.yzj.ebs.insideaccnoparam.biz.InnerAccnoBiz;
import com.yzj.ebs.insideaccnoparam.pojo.QueryParam;
import com.yzj.wf.am.security.common.AMSecurityDefine;
import com.yzj.wf.common.WFLogger;
import com.yzj.wf.core.model.po.wrapper.XPeopleInfo;
import com.yzj.wf.export.common.ExportEntity;
import com.yzj.wf.export.service.DataExporterImpl;

/**
 * 
 * 创建于:2015-12-31<br>
 * 版权所有(C) 2015 深圳市银之杰科技股份有限公司<br>
 * 
 * 内部账户维护<br>
 * 
 * @author chenzg
 * @version 1.0.0
 */
public class InnerAccnoAction extends ActionSupport {

	private static final long serialVersionUID = 1L;
	private List<InnerAccno> queryList = new ArrayList<InnerAccno>();
	private QueryParam queryParam;
	private Map<String, String> queryMap;
	
	private static WFLogger logger = WFLogger.getLogger(InnerAccnoAction.class);
	private String errMsg=null;
	
	private IPublicTools tools;
	private InnerAccnoBiz innerAccnoImpl;

	

	public String init() {
		queryParam = new QueryParam();
		queryList.clear();
		errMsg=null;
		
		return "initSuccess";
	}

	/**
	 * 查询账户信息
	 * @return
	 * @throws IOException
	 */
	public String getQueryData(){
		errMsg=null;
		queryList.clear();
		queryMap = createQueryMap();
		try {
			queryList = innerAccnoImpl.queryAccnolist(queryMap,queryParam,true);
			
		} catch (Exception e) {
			logger.error("内部账户查询失败", e);
			errMsg="内部账户查询失败";
		}
		return "initSuccess";
	}

	public Map<String,String> createQueryMap(){
		queryMap = new TreeMap<String,String>();
		String accno = queryParam.getAccno();
		
		if (accno != null && accno.trim().length() > 0) {
			queryMap.put("accno", accno.trim());
		}
		
		return queryMap;
	}

	/**
	 * 导出数据
	 * @return
	 * @throws IOException 
	 */
	public void exportData(){
		try {
			List<InnerAccno> exportList = new ArrayList<InnerAccno>();
			queryMap = createQueryMap();
			exportList =  innerAccnoImpl.queryAccnolist(queryMap,queryParam,false);	//全量查询
			if(exportList.size()==0||exportList==null){
				tools.ajaxResult("查询列表为空");
			}else{
				ExportEntity entity=new ExportEntity();
				entity.setFileName("内部账户列表");
				entity.setTitle("内部账户列表");
				entity.setDataList(exportList);
				Map<String,String> pro_desc=new LinkedHashMap<String,String>();
				pro_desc.put("accNo", "账号");
				pro_desc.put("inputDate", "录入日期");
				pro_desc.put("inputPeopleCode", "录入柜员");
				
				entity.setPro_desc_map(pro_desc);
			    new DataExporterImpl().export(entity);
		    }
		} catch (Exception e) {
			logger.error("导出列表失败：" + e.getMessage());
			tools.ajaxResult("导出列表失败：" + e.getMessage());
		}
	}

	/**
	 * 增加内部账户
	 * @return  返回 -1 数据库出错  1 添加成功  2 该条记录已存在
	 * @throws IOException 
	 */
	public void addAccno() throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		XPeopleInfo people = (XPeopleInfo) request.getSession().getAttribute(
				AMSecurityDefine.XPEOPLEINFO);
		String peopleCode = people.getPeopleCode();
		
		String accNo = tools.getParameter("accNo_div");
		int flog=1;//1:成功 2:该条记录已存在    -1:数据库出错 
		try {
			flog = innerAccnoImpl.addInnerAccno(accNo,peopleCode);
		} catch (Exception e) {
			flog =-1;
			logger.error("数据库出错！",e);
		}	
		response.getWriter().write(String.valueOf(flog));
		
	}
	
	/**
	 * 删除操作  
	 * @return
	 */
	public void deleteInfo() throws IOException{
	
		HttpServletRequest request = ServletActionContext.getRequest();
		String deleteAccno = request.getParameter("accno");
		try {
			innerAccnoImpl.deleteInnerAccno(deleteAccno);
		}catch (Exception e) {
			logger.error("内部帐户删除失败！",e);
			tools.ajaxResult("false");
		}
		
		tools.ajaxResult("deleteSuccess");
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

	public Map<String, String> getQueryMap() {
		return queryMap;
	}

	public void setQueryMap(Map<String, String> queryMap) {
		this.queryMap = queryMap;
	}

	public InnerAccnoBiz getInnerAccnoImpl() {
		return innerAccnoImpl;
	}

	public void setInnerAccnoImpl(InnerAccnoBiz innerAccnoImpl) {
		this.innerAccnoImpl = innerAccnoImpl;
	}

	public List<InnerAccno> getQueryList() {
		return queryList;
	}

	public void setQueryList(List<InnerAccno> queryList) {
		this.queryList = queryList;
	}

	public QueryParam getQueryParam() {
		return queryParam;
	}

	public void setQueryParam(QueryParam queryParam) {
		this.queryParam = queryParam;
	}

	public static WFLogger getLogger() {
		return logger;
	}

	public static void setLogger(WFLogger logger) {
		InnerAccnoAction.logger = logger;
	}
	
	
}
