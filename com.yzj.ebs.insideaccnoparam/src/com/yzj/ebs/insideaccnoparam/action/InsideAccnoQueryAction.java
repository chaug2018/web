package com.yzj.ebs.insideaccnoparam.action;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.yzj.ebs.common.IPublicTools;
import com.yzj.ebs.database.InnerAccnoMaindata;
import com.yzj.ebs.insideaccnoparam.biz.InsideAccnoQueryBiz;
import com.yzj.ebs.insideaccnoparam.pojo.AccnoCheckParam;
import com.yzj.ebs.util.FinalConstant;
import com.yzj.wf.am.security.common.AMSecurityDefine;
import com.yzj.wf.common.WFLogger;
import com.yzj.wf.core.model.po.wrapper.XPeopleInfo;
import com.yzj.wf.export.common.ExportEntity;
import com.yzj.wf.export.service.DataExporterImpl;
/**
 * 创建于:2015-12-09 
 * 版权所有(C) 2015 深圳市银之杰科技股份有限公司
 * 内部账户对账单查询
 * 
 * @author 
 * @version 1.0.0
 */

public class InsideAccnoQueryAction extends ActionSupport{
	
	private static final long serialVersionUID = 1L;
	private String custId;
	private String orginfo;//机构号
	private IPublicTools tools;
	private static WFLogger logger = WFLogger.getLogger(InsideAccnoQueryAction.class);
	private InsideAccnoQueryBiz insideAccnoQueryImpl;
	private AccnoCheckParam innerparam;
	private List<InnerAccnoMaindata> queryList = new ArrayList<InnerAccnoMaindata>();
	private Map<String,String> accordName = new HashMap<String,String>();
	private Map<String,String> recheckMap = new HashMap<String,String>();
	private TreeMap<String, String> idcenterMap = new TreeMap<String, String>();
	
	public String init(){
		innerparam = new AccnoCheckParam();
		queryList.clear();
		
		accordName= FinalConstant.accordName;
		HttpServletRequest request = ServletActionContext.getRequest();
		XPeopleInfo people = (XPeopleInfo) request.getSession().getAttribute(
				AMSecurityDefine.XPEOPLEINFO);
		custId = people.getPeopleCode(); 
		orginfo = people.getOrgNo();
		if("1000000000".equals(orginfo)){
			
		}else{
			idcenterMap.clear();
			idcenterMap.put(orginfo, orginfo);
		}
		return "initSuccess";
	}
	
	/**
	 *  查询 内部账户对账单信息
	 * @return
	 */
	public String queryInnerAccno(){
		
		try {
			queryList = insideAccnoQueryImpl.queryAccnolist(createQueryMap(),innerparam,true);
		} catch (SQLException e) {
			logger.error("查询内部账户情况出错！",e );
			e.printStackTrace();
		}
		
		return "initSuccess";
	}
	
	public Map<String, String> createQueryMap() {
		Map<String,String> queryMap = new HashMap<String,String>();
		
		String accno = innerparam.getAccno();
		String begindatadate = innerparam.getBegindatadate();
		String enddatadate = innerparam.getEnddatadate();
		String result = innerparam.getResult();
		String recheck = innerparam.getRecheck();
		String idcenter = innerparam.getIdcenter();
		
		if(accno!=null && accno.trim().length()>0){
			queryMap.put("accno", accno.trim());
		}
		if(begindatadate!=null && begindatadate.length()>0){
			queryMap.put("begindatadate", " t.datadate>='"+begindatadate.replace("-", "")+"'");
		}
		if(enddatadate!=null && enddatadate.length()>0){
			queryMap.put("enddatadate", " t.datadate<='"+enddatadate.replace("-", "")+"'");
		}
		if (result!=null&&result!="") {
			queryMap.put("result", result);
		}
		if (recheck!=null&&recheck!="") {
			queryMap.put("recheck", recheck);
		}
		if (idcenter!=null&&idcenter!="") {
			queryMap.put("idcenter", idcenter);
		}
		
		//非总行(1000000000)用户只能查自己的
		if(!"1000000000".equals(orginfo)){
			queryMap.put("custId", custId);
		}		
		
		return queryMap;
	}

	
	public String exportData() throws IOException,SQLException {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		List<InnerAccnoMaindata> exportList = new ArrayList<InnerAccnoMaindata>();
		//得到输出的全量数据
		exportList = insideAccnoQueryImpl.queryAccnolist(createQueryMap(),innerparam,false);
		
		ExportEntity entity=new ExportEntity();
		entity.setFileName("内部账户对账单查询");
		entity.setTitle("内部账户对账单查询");
		entity.setDataList(exportList);
		Map<String ,String> pro_desc=new LinkedHashMap<String,String>();
		Map<String,Map<String,String>> paramsMap=new HashMap<String,Map<String,String>>();
		
		pro_desc.put("idCenter", "对账中心号");
		pro_desc.put("accNo", "账号");
		pro_desc.put("bal", "余额");
		pro_desc.put("dataDate", "账单日期");
		pro_desc.put("result", "勾兑结果");
		pro_desc.put("resultPeopleCode", "核对柜员号");
		pro_desc.put("resultDate", "核对日期");
		pro_desc.put("abs", "备注");
		pro_desc.put("reCheck", "复核结果");
		pro_desc.put("reCheckPeopleCode", "复核柜员号");
		pro_desc.put("reCheckDate", "复核日期");
		
		paramsMap.put("result", accordName);
		paramsMap.put("reCheck", recheckMap);
		entity.setPro_desc_map(pro_desc);
		entity.setParamsMap(paramsMap);
		
		try {
			new DataExporterImpl().export(entity);
			return null;
		} catch (Throwable e) {
			logger.error("导出列表失败：" + e.getMessage());
			response.getWriter().write("导出列表失败：" + e.getMessage());
		}

		return null;
	}
	
	
	
	public String getCustId() {
		return custId;
	}
	public void setCustId(String custId) {
		this.custId = custId;
	}
	
	public String getOrginfo() {
		return orginfo;
	}

	public void setOrginfo(String orginfo) {
		this.orginfo = orginfo;
	}

	public IPublicTools getTools() {
		return tools;
	}
	public void setTools(IPublicTools tools) {
		this.tools = tools;
	}

	public InsideAccnoQueryBiz getInsideAccnoQueryImpl() {
		return insideAccnoQueryImpl;
	}

	public void setInsideAccnoQueryImpl(InsideAccnoQueryBiz insideAccnoQueryImpl) {
		this.insideAccnoQueryImpl = insideAccnoQueryImpl;
	}

	public AccnoCheckParam getInnerparam() {
		return innerparam;
	}

	public void setInnerparam(AccnoCheckParam innerparam) {
		this.innerparam = innerparam;
	}

	public List<InnerAccnoMaindata> getQueryList() {
		return queryList;
	}

	public void setQueryList(List<InnerAccnoMaindata> queryList) {
		this.queryList = queryList;
	}

	public Map<String, String> getAccordName() {
		return accordName;
	}

	public void setAccordName(Map<String, String> accordName) {
		this.accordName = accordName;
	}

	public Map<String, String> getRecheckMap() {
		if (null == recheckMap || recheckMap.size() == 0) {
			recheckMap.put("0", "复核通过");
			recheckMap.put("1", "复核不通过");
		}
		return recheckMap;
	}

	public void setRecheckMap(Map<String, String> recheckMap) {
		this.recheckMap = recheckMap;
	}
	
	public TreeMap<String, String> getIdcenterMap() {
		if (null == idcenterMap || idcenterMap.size() == 0) {
			idcenterMap.put("1000000000", "1000000000-总行");
			idcenterMap.put("7099", "7099-怀化分行");
			idcenterMap.put("7199", "7199-郴州分行");
			idcenterMap.put("7299", "7299-永州分行");
			idcenterMap.put("7699", "7699-娄底分行");
			idcenterMap.put("7799", "7799-张家界分行");
			idcenterMap.put("7899", "7899-湘西分行");
			idcenterMap.put("8099", "8099-岳阳分行");
			idcenterMap.put("8199", "8199-长沙分行");
			idcenterMap.put("8299", "8299-株洲分行");
			idcenterMap.put("8399", "8399-衡阳分行");
			idcenterMap.put("8599", "8599-邵阳分行");
			idcenterMap.put("8699", "8699-常德分行");
			idcenterMap.put("8799", "8799-益阳分行");
			idcenterMap.put("8899", "8899-湘潭分行");
			idcenterMap.put("8999", "8999-总行营业部");
			idcenterMap.put("9999", "9999-总行清算中心");
		}
		return idcenterMap;
	}

	public void setIdcenterMap(TreeMap<String, String> idcenterMap) {
		this.idcenterMap = idcenterMap;
	}

}
