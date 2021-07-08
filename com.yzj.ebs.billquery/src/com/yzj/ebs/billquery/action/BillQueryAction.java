package com.yzj.ebs.billquery.action;

import java.io.IOException;
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
import com.yzj.ebs.billquery.biz.IBillQueryBiz;
import com.yzj.ebs.billquery.queryparam.QueryParam;
import com.yzj.ebs.common.IPublicTools;
import com.yzj.ebs.common.IdCenterParam;
import com.yzj.ebs.common.RefTableTools;
import com.yzj.ebs.common.SimpleOrg;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.database.DocLog;
import com.yzj.ebs.database.DocSet;
import com.yzj.wf.am.security.common.AMSecurityDefine;
import com.yzj.wf.common.WFLogger;
import com.yzj.wf.core.model.po.wrapper.XPeopleInfo;
import com.yzj.wf.export.common.ExportEntity;
import com.yzj.wf.export.service.DataExporterImpl;

/**
 * 票据查询相关Action
 * 
 * @author 李海丰 swl
 * @version 1.0
 */
public class BillQueryAction extends ActionSupport {

	private static final long serialVersionUID = -1538744214579790239L;
	private static WFLogger logger = WFLogger.getLogger(BillQueryAction.class);
	private IBillQueryBiz billQuerybiz;
	private QueryParam queryParam;
	private List<DocSet> billList;// 票据列表
	private List<DocLog> logList;// 票据日志列
	private DocSet docSet;
	private IPublicTools publicTools;
	private String errMsg = null;
	private String orgTree;

	private String idCenter = "";
	private String idBranch = "";
	private String idBank = "";
	Object obj = new Object();

	private List<String> imgList = new ArrayList<String>(); // 影像列表
	private TreeMap<String, String> docFlagMap = new TreeMap<String, String>(); // 票据状态
	private TreeMap<String, String> accTypeMap = new TreeMap<String, String>();// 账户类型
	private TreeMap<String, String> proveFlagMap = new TreeMap<String, String>();// 验印状态
	private TreeMap<String, String> checkMap = new TreeMap<String, String>();// 余额状态
	private Map<String, String> pageMap = new HashMap<String, String>();// 单页页数显示
	private Map<String, Object> resultMap = new HashMap<String, Object>();// 票据查询明细页面ajax返回

	/** 初始化页面 */
	public String initPage() {
		errMsg = null;
		queryParam = new QueryParam();
		billList = new ArrayList<DocSet>();

		SimpleOrg org = null;
		idCenter = "";
		idBranch = "";
		idBank = "";
		HttpServletRequest request = ServletActionContext.getRequest();
		XPeopleInfo people = (XPeopleInfo) request.getSession().getAttribute(
				AMSecurityDefine.XPEOPLEINFO);
		try {
			org = publicTools.getCurOrgTree(people.getOrgNo());
		} catch (XDocProcException e) {
			errMsg = "获取当前机构信息列表出现错误";
			logger.error("获取当前机构信息列表出现错误", e);
		}
		JSONObject json = JSONObject.fromObject(org);
		orgTree = json.toString();

		return "initSuccess";
	}

	/***
	 * 查询票据信息列表的方法
	 * 
	 * @return
	 */
	public String queryBillList() {

		String idCenter = queryParam.getIdCenter();
		String idBranch = queryParam.getIdBranch();
		String idBank = queryParam.getIdBank();// 网点号

		this.idCenter = idCenter;
		this.idBranch = idBranch;
		this.idBank = idBank;
		
		try {
			billList = billQuerybiz.queryBillListByPage(queryParam);
		} catch (XDocProcException e) {
			logger.error("查询列表时出现错误", e);
		}
		if (billList != null && billList.size() > 0) {
			return "initSuccess";
		}

		return "initSuccess";
	}

	/**
	 * 导出数据
	 * 
	 * @return
	 * @throws IOException
	 */
	public String exportData() throws IOException {

		List<DocSet> exportList = new ArrayList<DocSet>();
		exportList.clear();

		if (billList == null || billList.size() == 0) {
			return null;
		} else {
			try {
				exportList = billQuerybiz.queryBillList(queryParam);
			} catch (XDocProcException e1) {
				logger.error("导出列表出现错误", e1);
			}
			String tableName = "影像流水查询";
			ExportEntity entity=new ExportEntity();
			entity.setFileName(tableName);
			entity.setTitle(tableName);
			entity.setDataList(exportList);
			Map<String,String> pro_desc=new LinkedHashMap<String,String>();
			Map<String,Map<String,String>> paramsMap=new HashMap<String,Map<String,String>>();
			LinkedHashMap<String, Map<String, String>> tableHeader = new LinkedHashMap<String, Map<String, String>>();
			pro_desc.put("docId", "流水号");
			pro_desc.put("voucherNo", "账单编号");
			pro_desc.put("idBank", "机构号");
			pro_desc.put("accName", "户名");
			pro_desc.put("docDate", "对账日期");
			pro_desc.put("proveFlag", "验印状态");
			pro_desc.put("docFlag", "影像状态");
			pro_desc.put("deleteReason", "删除原因");
			
			paramsMap.put("proveFlag", proveFlagMap);
			paramsMap.put("docFlag", docFlagMap);
			entity.setPro_desc_map(pro_desc);
			entity.setParamsMap(paramsMap);
			try {
				new DataExporterImpl().export(entity);
				return null;
			} catch (Throwable e) {
				logger.error("导出列表失败：" + e.getMessage());
			}
		}
		return null;

	}

	/***
	 * 查看详细信息的方法
	 * 
	 * @return
	 */
	public String viewBillDetail() {

		HttpServletRequest request = ServletActionContext.getRequest();
		String docId = request.getParameter("docId");
		for (int i = 0; i < billList.size(); i++) {
			if (String.valueOf(billList.get(i).getDocId()).equals(docId)) {
				docSet = billList.get(i);
				break;
			}
		}
		imgList.clear();
		if (docSet != null) {
			try {

				IdCenterParam idCenter = publicTools.getParamIdcenter(docSet
						.getIdCenter());
				if (idCenter == null) {
					throw new XDocProcException("无法获取到" + docSet.getIdCenter()
							+ "的机构信息");
				}
				String frontimagepath = idCenter.getImageUrl()
						+ docSet.getFrontImagePath();
				String backimagepath = idCenter.getImageUrl()
						+ docSet.getBackImagePath();
				if (frontimagepath != null
						&& frontimagepath.trim().length() > 0) {
					imgList.add(frontimagepath);
				}
				if (backimagepath != null && backimagepath.trim().length() > 0) {
					imgList.add(backimagepath);
				}
				String[] itemNames = { "accno_0", "accson_0", "checkflag_0",
						"accno_1", "accson_1", "checkflag_1", "accno_2",
						"accson_2", "checkflag_2", "accno_3", "accson_3",
						"checkflag_3", "accno_4", "accson_4", "checkflag_4" };
				docSet.setDescriptionItems(itemNames);
				obj = docSet.getObjDescription();

			} catch (XDocProcException e) {
				e.printStackTrace();
			}
		}
		return "billDetail";

	}

	/** 查询日志 */
	public String queryLog() {
		HttpServletRequest request = ServletActionContext.getRequest();
		String docId = request.getParameter("docId");
		try {
			logList = billQuerybiz.queryDocLogList(docId);
		} catch (XDocProcException e) {
			logger.error("查询日志时出现错误", e);
		}
		return "logList";
	}

	/** 发起删除 */
	public String deleteBill() {
		HttpServletRequest request = ServletActionContext.getRequest();
		String docId = request.getParameter("docId");
		billQuerybiz.deleteBill(docId);
		resultMap.put("resultMsg", "发起删除处理成功");
		return SUCCESS;
	}



	public QueryParam getQueryParam() {
		return queryParam;
	}

	public void setQueryParam(QueryParam queryParam) {
		this.queryParam = queryParam;
	}

	public List<DocSet> getBillList() {
		return billList;
	}

	public void setBillList(List<DocSet> billList) {
		this.billList = billList;
	}

	public IPublicTools getPublicTools() {
		return publicTools;
	}

	public void setPublicTools(IPublicTools publicTools) {
		this.publicTools = publicTools;
	}

	public Map<String, String> getPageMap() {
		return pageMap;
	}

	public void setPageMap(Map<String, String> pageMap) {
		this.pageMap = pageMap;
	}

	public Map<String, Object> getResultMap() {
		return resultMap;
	}

	public void setResultMap(Map<String, Object> resultMap) {
		this.resultMap = resultMap;
	}

	public List<DocLog> getLogList() {
		return logList;
	}

	public void setLogList(List<DocLog> logList) {
		this.logList = logList;
	}

	public DocSet getDocSet() {
		return docSet;
	}

	public void setDocSet(DocSet docSet) {
		this.docSet = docSet;
	}

	public List<String> getImgList() {
		return imgList;
	}

	public void setImgList(List<String> imgList) {
		this.imgList = imgList;
	}

	public TreeMap<String, String> getDocFlagMap() {
		if (null == docFlagMap || docFlagMap.size() == 0) {
			docFlagMap = RefTableTools.ValRefDocflagMap;
		}
		return docFlagMap;
	}

	public void setDocFlagMap(TreeMap<String, String> docFlagMap) {

		this.docFlagMap = docFlagMap;
	}

	public TreeMap<String, String> getAccTypeMap() {
		if (null == accTypeMap || accTypeMap.size() == 0) {
			accTypeMap = RefTableTools.ValRefAcctypeMap;
		}
		return accTypeMap;
	}

	public void setAccTypeMap(TreeMap<String, String> accTypeMap) {
		this.accTypeMap = accTypeMap;
	}

	public TreeMap<String, String> getProveFlagMap() {
		if (proveFlagMap == null || proveFlagMap.size() == 0) {
			proveFlagMap = RefTableTools.ValRefProveflagMap;
		}
		return proveFlagMap;
	}

	public void setProveFlagMap(TreeMap<String, String> proveFlagMap) {
		this.proveFlagMap = proveFlagMap;
	}

	public TreeMap<String, String> getCheckMap() {
		if (checkMap == null || checkMap.size() == 0) {
			checkMap = RefTableTools.ValRefCheckflagMap;
		}
		return checkMap;
	}

	public void setCheckMap(TreeMap<String, String> checkMap) {
		this.checkMap = checkMap;
	}

	public static WFLogger getLogger() {
		return logger;
	}

	public static void setLogger(WFLogger logger) {
		BillQueryAction.logger = logger;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}
	public IBillQueryBiz getBillQuerybiz() {
		return billQuerybiz;
	}

	public void setBillQuerybiz(IBillQueryBiz billQuerybiz) {
		this.billQuerybiz = billQuerybiz;
	}
}