/**
 * 创建于:2012-11-08<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * @author lif
 * @version 1.0
 */
package com.yzj.ebs.edata.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import javax.servlet.http.HttpServletRequest;
import net.sf.json.JSONObject;
import org.apache.struts2.ServletActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.yzj.ebs.common.IPublicTools;
import com.yzj.ebs.common.RefTableTools;
import com.yzj.ebs.common.SimpleOrg;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.database.CheckMainData;
import com.yzj.ebs.edata.bean.AccnoMainDataQueryParam;
import com.yzj.ebs.edata.biz.IDataExportBiz;
import com.yzj.wf.am.security.common.AMSecurityDefine;
import com.yzj.wf.common.WFLogger;
import com.yzj.wf.core.model.po.wrapper.XPeopleInfo;

/**
 * 数据导出action
 * 
 * @author lif
 * 
 */
public class DataExportAction extends ActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 324380654216998105L;
	private static WFLogger logger = WFLogger.getLogger(DataExportAction.class);
	private TreeMap<String, String> refCheckflagMap = new TreeMap<String, String>();
	private TreeMap<String, String> refProveflagMap = new TreeMap<String, String>();
	private TreeMap<String, String> refQueryTypeMap = new TreeMap<String, String>();
	private TreeMap<String, String> refAccCycleMap = new TreeMap<String, String>();
	private TreeMap<String, String> refSendModeMap = new TreeMap<String, String>();
	private TreeMap<String, String> refDocstateMap = new TreeMap<String, String>();// 账单状态
	private List<CheckMainData> queryBillList = new ArrayList<CheckMainData>();
	private AccnoMainDataQueryParam accnoMainDataQueryParam;
	private String errMsg = null;
	private String orgTree;
	private IPublicTools tools;
	private String password;// 密钥
	private String idCenter; // 清算中心
	private String idBank; // 支行号
	private String checkresult;

	private List<CheckMainData> resultList = new ArrayList<CheckMainData>();
	private IDataExportBiz dataExportBiz;

	/**
	 * 初始化获取页面需要数据
	 */
	public String init() {
		queryBillList.clear();
		errMsg = null;
		idCenter = "";
		idBank = "";
		checkresult = "all";
		accnoMainDataQueryParam = new AccnoMainDataQueryParam();
		HttpServletRequest request = ServletActionContext.getRequest();
		XPeopleInfo people = (XPeopleInfo) request.getSession().getAttribute(
				AMSecurityDefine.XPEOPLEINFO);
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
	public String queryBillinfoData() throws Exception {
		errMsg = null;
		queryBillList.clear();
		// 账单发送方式
		accnoMainDataQueryParam.setSendMode(checkresult);
		try {
			queryBillList = dataExportBiz.queryAccNoMainDataInfo(accnoMainDataQueryParam);
			this.idBank = accnoMainDataQueryParam.getIdBank();
			this.idCenter = accnoMainDataQueryParam.getIdCenter();
		} catch (Exception e) {
			errMsg = "查询账单信息出现错误";
			logger.error("查询账单信息出现错误", e);
		}
		return "initSuccess";
	}

	/**
	 * 对账单导出数据
	 * 
	 * @return
	 * @throws IOException
	 */
	public String exportData() throws IOException {
		accnoMainDataQueryParam.setSendMode(checkresult);
		resultList = dataExportBiz.makeDataInfo(resultList, password,accnoMainDataQueryParam);
		if (resultList == null || resultList.size() == 0) {
			logger.info("下载数据为空");
		}
		return "initSuccess";
	}
	
	/**
	 * 明细导出 
	 * @return
	 */
	public String exportDetail() throws Exception {
		try {
			String month = accnoMainDataQueryParam.getDocDate().substring(5,7);
			dataExportBiz.exportDetail(password,month);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "initSuccess";
	}

	public static WFLogger getLogger() {
		return logger;
	}

	public static void setLogger(WFLogger logger) {
		DataExportAction.logger = logger;
	}

	public TreeMap<String, String> getRefCheckflagMap() {
		if (null == refCheckflagMap || refCheckflagMap.size() == 0) {
			refCheckflagMap = RefTableTools.ValRefCheckflagMap;
		}
		return refCheckflagMap;
	}

	public void setRefCheckflagMap(TreeMap<String, String> refCheckflagMap) {
		this.refCheckflagMap = refCheckflagMap;
	}
	public TreeMap<String, String> getRefProveflagMap() {
		if (null == refProveflagMap || refProveflagMap.size() == 0) {
			refProveflagMap = RefTableTools.ValRefProveflagMap;
		}
		return refProveflagMap;
	}

	public void setRefProveflagMap(TreeMap<String, String> refProveflagMap) {
		this.refProveflagMap = refProveflagMap;
	}

	public TreeMap<String, String> getRefQueryTypeMap() {
		if (null == refQueryTypeMap || refQueryTypeMap.size() == 0) {
			// refQueryTypeMap = RefTableTools.ValRefQuerytypeMap;
		}
		return refQueryTypeMap;
	}

	public void setRefQueryTypeMap(TreeMap<String, String> refQueryTypeMap) {
		this.refQueryTypeMap = refQueryTypeMap;
	}

	public List<CheckMainData> getQueryBillList() {
		return queryBillList;
	}

	public void setQueryBillList(List<CheckMainData> queryBillList) {
		this.queryBillList = queryBillList;
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

	/**
	 * @return the refAccCycleMap
	 */
	public TreeMap<String, String> getRefAccCycleMap() {
		if (null == refAccCycleMap || refAccCycleMap.size() == 0) {
			refAccCycleMap = RefTableTools.ValRefAccCycleMap;
		}
		return refAccCycleMap;
	}

	public void setRefAccCycleMap(TreeMap<String, String> refAccCycleMap) {
		this.refAccCycleMap = refAccCycleMap;
	}

	public AccnoMainDataQueryParam getAccnoMainDataQueryParam() {
		return accnoMainDataQueryParam;
	}

	public void setAccnoMainDataQueryParam(
			AccnoMainDataQueryParam accnoMainDataQueryParam) {
		this.accnoMainDataQueryParam = accnoMainDataQueryParam;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public TreeMap<String, String> getRefSendModeMap() {
		return refSendModeMap;
	}

	public void setRefSendModeMap(TreeMap<String, String> refSendModeMap) {
		this.refSendModeMap = refSendModeMap;
	}
	
	public TreeMap<String, String> getRefDocstateMap() {
		if (null == refDocstateMap || refDocstateMap.size() == 0) {
			refDocstateMap = RefTableTools.ValRefDocstateMap;
		}
		return refDocstateMap;
	}
	public void setRefDocstateMap(TreeMap<String, String> refDocstateMap) {
		this.refDocstateMap = refDocstateMap;
	}
	public IDataExportBiz getDataExportBiz() {
		return dataExportBiz;
	}

	public void setDataExportBiz(IDataExportBiz dataExportBiz) {
		this.dataExportBiz = dataExportBiz;
	}

	public String getCheckresult() {
		return checkresult;
	}

	public void setCheckresult(String checkresult) {
		this.checkresult = checkresult;
	}

	public String getIdCenter() {
		return idCenter;
	}

	public void setIdCenter(String idCenter) {
		this.idCenter = idCenter;
	}

}
