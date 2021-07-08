package com.yzj.ebs.operlog.query.action;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.apache.struts2.ServletActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.yzj.ebs.common.IPublicTools;
import com.yzj.ebs.common.OperLogModuleDefine;
import com.yzj.ebs.common.OperLogQueryParam;
import com.yzj.ebs.common.RefTableTools;
import com.yzj.ebs.common.SimpleOrg;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.database.BasicInfo;
import com.yzj.ebs.edata.service.NetProcessServer;
import com.yzj.ebs.operlog.query.biz.IOperLogQueryBiz;
import com.yzj.wf.am.security.common.AMSecurityDefine;
import com.yzj.wf.common.WFLogger;
import com.yzj.wf.core.model.po.wrapper.XPeopleInfo;
import com.yzj.wf.export.common.ExportEntity;
import com.yzj.wf.export.service.DataExporterImpl;

/**
 * 创建于:2013-1-14 版权所有(C) 2012 深圳市银之杰科技股份有限公司
 * 
 * 操作日志查询action
 * 
 * @author 陈林江
 * @version 1.0.0
 */
@SuppressWarnings("rawtypes")
public class OperLogQueryAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static WFLogger logger = WFLogger
			.getLogger(OperLogQueryAction.class);
	private List queryList = new ArrayList();
	private Map<String, String> operLogModuleMap;// 操作日子查询模块map
	private OperLogQueryParam operLogQueryParam;
	private String idCenter;
	private String idBranch;
	private String idBank;
	private IOperLogQueryBiz operLogQueryBiz;

	private String errMsg = null;
	private String orgTree;
	private IPublicTools tools;
	private NetProcessServer netProcessServer;

	/**
	 * 初始化获取页面需要数据
	 */
	public String init() {
		this.clean();
		HttpServletRequest request = ServletActionContext.getRequest();
		XPeopleInfo people = (XPeopleInfo) request.getSession().getAttribute(
				AMSecurityDefine.XPEOPLEINFO);
		operLogQueryParam = new OperLogQueryParam();
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
	 * @return
	 * @throws IOException
	 * @throws XDocProcException
	 */
	public String queryOperLog() throws IOException, XDocProcException {
		errMsg = null;
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		queryList.clear();
			
		operLogQueryParam = processParam(operLogQueryParam);
		queryList = operLogQueryBiz.queryBasicInfoLog(operLogQueryParam,true);
		
		idCenter = operLogQueryParam.getIdCenter();
		idBranch = operLogQueryParam.getIdBranch();
		idBank = operLogQueryParam.getIdBank();
		
//		if(String.valueOf(OperLogModuleDefine.autoOCR).equals(operLogQueryParam.getOperLogModule()) || String.valueOf(OperLogModuleDefine.autoYj).equals(operLogQueryParam.getOperLogModule()) || String.valueOf(OperLogModuleDefine.autojz).equals(operLogQueryParam.getOperLogModule()) ||String.valueOf(OperLogModuleDefine.dataprocess).equals(operLogQueryParam.getOperLogModule())){
//			//反向处理时间
//			operLogQueryParam = processPatamForDate(operLogQueryParam);
//		}
		//反向处理时间
		operLogQueryParam = processPatamForDate(operLogQueryParam);
		
		return "initSuccess";
	}
	
	private OperLogQueryParam processParam(OperLogQueryParam param){
		OperLogQueryParam p = param;
		//如果查询自动模块的 则提出查询条件
		if(String.valueOf(OperLogModuleDefine.autoOCR).equals(operLogQueryParam.getOperLogModule()) || String.valueOf(OperLogModuleDefine.autoYj).equals(operLogQueryParam.getOperLogModule()) || String.valueOf(OperLogModuleDefine.autojz).equals(operLogQueryParam.getOperLogModule()) ||String.valueOf(OperLogModuleDefine.dataprocess).equals(operLogQueryParam.getOperLogModule())){
			operLogQueryParam.setAccName(null);
			operLogQueryParam.setAccNo(null);
			operLogQueryParam.setIdBank(null);
			operLogQueryParam.setIdBank1(null);
			operLogQueryParam.setIdBranch(null);
			operLogQueryParam.setIdCenter(null);
			
		}
		//转换日期
		if(p.getStartTime()!=null || p.getStartTime().length()>0){
			p.setStartTime(p.getStartTime().replaceAll("-", ""));
		}
		if(p.getEndTime()!=null || p.getEndTime().length()>0){
			p.setEndTime(p.getEndTime().replaceAll("-", ""));
		}
		
		return p;
	}
	
	
	/**
	 * 反向处理时间
	 * @param param
	 * @return
	 */
	private OperLogQueryParam processPatamForDate(OperLogQueryParam param){
		
		SimpleDateFormat fromat  =new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat fromatDateFormat  =new SimpleDateFormat("yyyy-MM-dd");
		try{
			if(param.getStartTime()!=null && param.getStartTime().length()>0 ){
				Date date = fromat.parse(param.getStartTime());
				param.setStartTime(fromatDateFormat.format(date));
			}
			if(param.getEndTime()!=null && param.getEndTime().length()>0 ){
				Date date = fromat.parse(param.getEndTime());
				param.setEndTime(fromatDateFormat.format(date));
			}
		}catch(Exception e){
			
		}
		return param;
	}
	
	
	/**
	 * 导出数据
	 * 
	 * @return
	 * @throws IOException
	 */
	public String exportData() throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		List exportList = new ArrayList<BasicInfo>();

		String basicInfoString = "" + OperLogModuleDefine.accModify
				+ OperLogModuleDefine.accSign + OperLogModuleDefine.specialAcc;
		try {
			if (basicInfoString.contains(operLogQueryParam.getOperLogModule())) { // basicInfoLog表
				exportList = operLogQueryBiz.queryBasicInfoLog(
						operLogQueryParam, false);
			} else { // checkMainDataLog表
				exportList = operLogQueryBiz.queryCheckMainDataLog(
						operLogQueryParam, false);
			}
		} catch (Exception e) {
			logger.error("导出列表失败：" + e.getMessage());
			response.getWriter().write("导出列表失败：" + e.getMessage());
		}
		if (exportList.size() == 0 || exportList == null) {
			response.getWriter().write("查询列表为空");
			return null;
		} else {
			String tableName = "操作日志列表";
			ExportEntity entity=new ExportEntity();
			entity.setFileName(tableName);
			entity.setTitle(tableName);
			entity.setDataList(exportList);
			Map<String,String> pro_desc=new LinkedHashMap<String,String>();
			
			pro_desc.put("idCenter", "分行号");
			pro_desc.put("idBranch", "支行号");
			pro_desc.put("idBank", "机构号");
			pro_desc.put("chnOpMode", "模块名称");
			pro_desc.put("opDate", "操作日期");
			pro_desc.put("opCode", "操作员号");
			if (basicInfoString.contains(operLogQueryParam.getOperLogModule())) {
				pro_desc.put("accNo", "账户账号");
				pro_desc.put("accName", "账户名");
			} else {
				pro_desc.put("voucherNo", "账单编号");
			}
			pro_desc.put("opDesc", "操作描述");

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
	
	//重新导出网银对账单
	public String reExportNetEbill(){
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		response.setCharacterEncoding("UTF-8");
		String autoId = request.getParameter("autoId").trim();
		String opDate = request.getParameter("opDate").trim();
		//重新发送对账单
		netProcessServer.fetchEbillFromEbill(autoId, opDate);
		return "initSuccess";
	}
	
	public String reExportNotMatch(){
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		response.setCharacterEncoding("UTF-8");
		String autoId = request.getParameter("autoId").trim();
		String opDate = request.getParameter("opDate").trim();
		//重新发送未达结果
		netProcessServer.fetchResultFromEbill(autoId, opDate);
		return "initSuccess";
	}
	
	public String loadEbillCheck(){
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		response.setCharacterEncoding("UTF-8");
		String autoId = request.getParameter("autoId").trim();
		String opDate = request.getParameter("opDate").trim();
		//重新下载对账结果
		netProcessServer.exchangeResultFormNet(autoId, opDate);
		return "initSuccess";
	}

	private void clean() {
		queryList.clear();
		errMsg = null;
		idCenter = null;
		idBranch = null;
		idBank = null;
	}

	public List getQueryList() {
		return queryList;
	}

	public void setQueryList(List queryList) {
		this.queryList = queryList;
	}

	public OperLogQueryParam getOperLogQueryParam() {
		return operLogQueryParam;
	}

	public void setOperLogQueryParam(OperLogQueryParam operLogQueryParam) {
		this.operLogQueryParam = operLogQueryParam;
	}

	public String getIdCenter() {
		return idCenter;
	}

	public void setIdCenter(String idCenter) {
		this.idCenter = idCenter;
	}

	public String getIdBranch() {
		return idBranch;
	}

	public void setIdBranch(String idBranch) {
		this.idBranch = idBranch;
	}

	public String getIdBank() {
		return idBank;
	}

	public void setIdBank(String idBank) {
		this.idBank = idBank;
	}

	public IOperLogQueryBiz getOperLogQueryBiz() {
		return operLogQueryBiz;
	}

	public void setOperLogQueryBiz(IOperLogQueryBiz operLogQueryBiz) {
		this.operLogQueryBiz = operLogQueryBiz;
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

	public Map<String, String> getOperLogModuleMap() {
		if (operLogModuleMap == null) {
			operLogModuleMap = RefTableTools.ValRefOperLogModuleMap;
		}
		return operLogModuleMap;
	}

	public void setOperLogModuleMap(Map<String, String> operLogModuleMap) {
		this.operLogModuleMap = operLogModuleMap;
	}

	public NetProcessServer getNetProcessServer() {
		return netProcessServer;
	}

	public void setNetProcessServer(NetProcessServer netProcessServer) {
		this.netProcessServer = netProcessServer;
	}

}
