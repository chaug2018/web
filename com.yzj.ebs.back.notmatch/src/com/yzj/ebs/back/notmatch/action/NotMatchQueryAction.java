package com.yzj.ebs.back.notmatch.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.yzj.ebs.back.notmatch.biz.INotMatchBiz;
import com.yzj.ebs.back.notmatch.queryparam.NotMatchQueryParam;
import com.yzj.ebs.common.INotMatchTableAdm;
import com.yzj.ebs.common.IPublicTools;
import com.yzj.ebs.common.RefTableTools;
import com.yzj.ebs.common.SimpleOrg;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.database.NotMatchTable;
import com.yzj.ebs.util.DataExporter;
import com.yzj.wf.am.security.common.AMSecurityDefine;
import com.yzj.wf.common.WFLogger;
import com.yzj.wf.core.model.po.wrapper.XPeopleInfo;
import com.yzj.wf.export.common.ExportEntity;
import com.yzj.wf.export.service.DataExporterImpl;

/**
 * 
 * 创建于:2012-10-9<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 未达项查询业务处理类
 * 
 * @author ShiJiangmin
 * @version 1.0.0
 */
public class NotMatchQueryAction extends ActionSupport {

	private static final long serialVersionUID = -7460740918714093270L;
	private static final String NOTMATCHAUTHFLAG_COMMIT = "1"; // 复核通过
	private static final String CHECHFLAG_MATCH = "4";
	private static final String CHECHFLAG_NOTMATCH = "5";

	private NotMatchQueryParam notMatchQueryParam;
	private TreeMap<String, String> refDirectionMap = new TreeMap<String, String>();
	private TreeMap<String, String> refResultMap = new TreeMap<String, String>();
	List<NotMatchTable> queryList = new ArrayList<NotMatchTable>();
	private static WFLogger logger = WFLogger
			.getLogger(NotMatchQueryAction.class);
	private Map<String, String> queryMap;
	private String errMsg;
	private String orgTree;
	private IPublicTools tools;
	private INotMatchBiz biz;
	// 查询下拉框的值
	private String idCenter;
	private String idBranch;
	private String idBank;

	/**
	 * 初始化页面需要数据
	 * 
	 * @return
	 */
	public String init() {

		getRefDirectionMap();
		getRefResultMap();
		queryList.clear();
		errMsg = null;
		idCenter = "";
		idBranch = "";
		idBank = "";
		notMatchQueryParam = new NotMatchQueryParam();
		SimpleOrg org = null;
		HttpServletRequest request = ServletActionContext.getRequest();
		XPeopleInfo people = (XPeopleInfo) request.getSession().getAttribute(
				AMSecurityDefine.XPEOPLEINFO);
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
	 * 查询未达项信息
	 * 
	 * @return 符合条件的未达信息列表 queryList
	 * @throws IOException
	 */
	@SuppressWarnings("rawtypes")
	public String queryNotMatchData() throws IOException {
		errMsg = null;
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		queryList.clear();
		queryMap = createQueryMap();
		try {
			List<NotMatchTable> queryListTmp = biz
					.getNotMatchTableData(queryMap, notMatchQueryParam);
			Iterator it = queryListTmp.iterator();
			if (it.hasNext()) {
				for (NotMatchTable notMatchTable : queryListTmp) {
					String traceDateTmp = notMatchTable.getTraceDate();
					String traceDate = traceDateTmp.substring(0, 4) + "-"
							+ traceDateTmp.substring(4, 6) + "-"
							+ traceDateTmp.substring(6);
					notMatchTable.setTraceDate(traceDate);
					queryList.add(notMatchTable);
				}
			}

		} catch (XDocProcException e) {
			errMsg = "未达项查询失败:" + e.getMessage();
			logger.error("未达项查询失败", e);
		}
		return "initSuccess";
	}

	/**
	 * 导出数据
	 * 
	 * @return 未达信息列表
	 * @throws IOException
	 */
	public String exportData() throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		List<NotMatchTable> exportList = new ArrayList<NotMatchTable>();

		if (queryList.size() == 0 || queryList == null) {
			response.getWriter().write("查询列表为空");
			return null;
		} else {
			queryMap = createQueryMap();
			try {
				exportList = biz.getAllNotMatchData(queryMap);
			} catch (XDocProcException e1) {
				logger.error("未达项数据导出时查询数据失败：" + e1.getMessage());
				response.getWriter().write("导出列表失败：" + e1.getMessage());
			}
			String tableName = "未达项明细表";
			ExportEntity entity=new ExportEntity();
			entity.setFileName(tableName);
			entity.setTitle(tableName);
			entity.setDataList(exportList);
			Map<String,String> pro_desc=new LinkedHashMap<String,String>();
			Map<String,Map<String,String>> paramsMap=new HashMap<String,Map<String,String>>();
			pro_desc.put("docId", "流水号");
			pro_desc.put("voucherNo", "账单编号");
			pro_desc.put("idBank", "机构号");
			pro_desc.put("accNo", "账号");
			pro_desc.put("traceDate", "记账日期");
			pro_desc.put("strtraceCredit", "金额");
			pro_desc.put("traceNo", "凭证号码");
			pro_desc.put("inputDesc", "摘要");
			pro_desc.put("direction", "未达方向");
			pro_desc.put("checkFlag", "核对结果");
			
			paramsMap.put("direction", refDirectionMap);
			paramsMap.put("checkFlag", refResultMap);
			
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

	/**
	 * 通过NotMatchQueryParam拼装查询参数Map，作为分页查询方法的参数
	 * 
	 * @return queryMap 用于查询的参数map
	 */
	public Map<String, String> createQueryMap() {
		queryMap = new TreeMap<String, String>();
		String idBank = notMatchQueryParam.getIdBank();
		String idBank1 = notMatchQueryParam.getIdBank1();
		String idCenter = notMatchQueryParam.getIdCenter();
		String idBranch = notMatchQueryParam.getIdBranch();
		this.idCenter = idCenter;
		this.idBranch = idBranch;
		this.idBank = idBank;

		if (idBank != null && idBank.length() > 0) {
			queryMap.put("idBank", idBank);
		}
		if (idBank1 != null && idBank1.length() > 0) {
			queryMap.put("idBank", idBank1);
		}

		if (idBranch != null && idBranch.length() > 0) {
			queryMap.put("idBranch", idBranch);
		}
		if (idCenter != null && idCenter.length() > 0) {
			queryMap.put("idCenter", idCenter);
		}
		queryMap.put("voucherNo", notMatchQueryParam.getVoucherNo());
		queryMap.put("accNo", notMatchQueryParam.getAccNo());
		String docDateTmp = notMatchQueryParam.getDocDate();
		String docDate = docDateTmp.substring(0, 4)
				+ docDateTmp.substring(5, 7) + docDateTmp.substring(8);
		queryMap.put("docDate", docDate);
		queryMap.put("checkFlag", notMatchQueryParam.getResult());
		queryMap.put("direction", notMatchQueryParam.getDirection());
		queryMap.put("authFlag", NOTMATCHAUTHFLAG_COMMIT); // 查询经过复核的未达项数据
		return queryMap;
	}

	public TreeMap<String, String> getRefDirectionMap() {
		if (null == refDirectionMap || refDirectionMap.size() == 0) {
			refDirectionMap = RefTableTools.ValRefDirectionMap;
		}
		return refDirectionMap;
	}

	public void setRefDirectionMap(TreeMap<String, String> refDirectionMap) {
		this.refDirectionMap = refDirectionMap;
	}

	public TreeMap<String, String> getRefResultMap() {
		if (null == refResultMap || refResultMap.size() == 0) {
			refResultMap.put(CHECHFLAG_MATCH, "人工调节余额相符");
			refResultMap.put(CHECHFLAG_NOTMATCH, "人工调节余额不符");
		}
		return refResultMap;
	}

	public void setRefResultMap(TreeMap<String, String> refResultMap) {
		this.refResultMap = refResultMap;
	}

	public List<NotMatchTable> getQueryList() {
		return queryList;
	}

	public void setQueryList(List<NotMatchTable> queryList) {
		this.queryList = queryList;
	}

	public NotMatchQueryParam getNotMatchQueryParam() {
		return notMatchQueryParam;
	}

	public void setNotMatchQueryParam(NotMatchQueryParam notMatchQueryParam) {
		this.notMatchQueryParam = notMatchQueryParam;
	}

	public static WFLogger getLogger() {
		return logger;
	}

	public static void setLogger(WFLogger logger) {
		NotMatchQueryAction.logger = logger;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Map<String, String> getQueryMap() {
		return queryMap;
	}

	public void setQueryMap(Map<String, String> queryMap) {
		this.queryMap = queryMap;
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
	public INotMatchBiz getBiz() {
		return biz;
	}

	public void setBiz(INotMatchBiz biz) {
		this.biz = biz;
	}
	public void setIdBank(String idBank) {
		this.idBank = idBank;
	}

}
