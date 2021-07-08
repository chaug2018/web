/**
 * 创建于:2013-04-03<br>
 * 版权所有(C) 2013 深圳市银之杰科技股份有限公司<br>
 * @author qinjingfeng
 * @version 1.0
 */
package com.yzj.ebs.edata.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
import com.yzj.ebs.database.CheckMainData;
import com.yzj.ebs.edata.bean.AccnoMainDataQueryParam;
import com.yzj.ebs.edata.biz.IPrintDataBiz;
import com.yzj.ebs.edata.service.IDataProcessService;
import com.yzj.wf.am.security.common.AMSecurityDefine;
import com.yzj.wf.common.WFLogger;
import com.yzj.wf.core.model.po.wrapper.XPeopleInfo;

/**
 * 账单批打action
 * 
 * @author qinjingfeng
 * 
 */
public class BatchPrintAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2426853313699193849L;
	private static WFLogger logger = WFLogger.getLogger(BatchPrintAction.class);
	private TreeMap<String, String> refCheckflagMap = new TreeMap<String, String>();// 余额状态
	private TreeMap<String, String> refDocstateMap = new TreeMap<String, String>();// 账单状态
	private TreeMap<String, String> refProveflagMap = new TreeMap<String, String>();// 验印状态
	private TreeMap<String, String> refSendMode = new TreeMap<String, String>();// 对账方式
	private TreeMap<String, String> refQueryTypeMap = new TreeMap<String, String>();
	private TreeMap<String, String> refAccTypeMap = new TreeMap<String, String>();
	List<CheckMainData> queryBillList = new ArrayList<CheckMainData>(); // 根据条件查询的账单信息
	private AccnoMainDataQueryParam accnoMainDataQueryParam;
	private IPrintDataBiz printDataBiz;
	private String errMsg = null;
	private String orgTree;
	private IPublicTools tools;
	private String idCenter; // 对账中心
	private String idBank; // 网点号
	private String selectIds = null; // 批量修改id，对应行号
	private String myFileName; // 打印文件名字
	private String flag;// 临时出账单标识
	private String accNo;
	private String date;
	private IDataProcessService DataProcessServiceImpl; // 业务处理类

	/**
	 * 初始化获取页面需要数据
	 */
	public String init() {
		queryBillList.clear();
		errMsg = null;
		idCenter = "";
		idCenter = "";
		idBank = "";
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
	 * 分页查询对账单信息
	 * 
	 * @return
	 * @throws IOException
	 * @throws XDocProcException
	
	public String queryBillinfoData() throws IOException {
		errMsg = null;
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		queryBillList.clear();
		selectIds = "";
		try {
			// 对账日期不是月末最后一天就是根据账号出临时账单
			if ("createTempBill".equals(flag)) {
				try {
					if (StringUtils.isNotEmpty(accnoMainDataQueryParam
							.getAccNo())
							&& StringUtils.isNotEmpty(accnoMainDataQueryParam
									.getDocDate())) {
						boolean isAllowCreate = false;
						isAllowCreate = printDataBiz
								.judgeOrg(accnoMainDataQueryParam);

						if (isAllowCreate) {
							DataProcessServiceImpl.tempDataDispose(
									accnoMainDataQueryParam.getAccNo(),
									accnoMainDataQueryParam.getDocDate());
						} else {
							errMsg = "不能跨机构生成！";
							return "initSuccess";
						}
					}
					queryBillList = printDataBiz
							.queryCheckMainDataInfo(accnoMainDataQueryParam);
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("出临时账单出现错误", e);
				}
			} else {
				queryBillList = printDataBiz
						.queryCheckMainDataInfo(accnoMainDataQueryParam);
			}
			this.idBank = accnoMainDataQueryParam.getIdBank();
			this.idCenter = accnoMainDataQueryParam.getIdCenter();
		} catch (XDocProcException e) {
			errMsg = "查询账单信息出现错误";
			logger.error("查询账单信息出现错误", e);
		}
		return "initSuccess";
	}
	 */



	/**
	 * 分页打印对账单数据
	 * 
	 * @return
	 * @throws IOException
	 */
	public String printData() throws IOException {
		try {
			if (accnoMainDataQueryParam.getCurPage() > accnoMainDataQueryParam
					.getTotalPage()) {
				queryBillList = new ArrayList<CheckMainData>();
			} else {
				queryBillList = printDataBiz
						.queryCheckMainDataInfo(accnoMainDataQueryParam);
				accnoMainDataQueryParam.setCurPage(accnoMainDataQueryParam
						.getCurPage() + 1);// 为下次循环做准备
			}
		} catch (XDocProcException e) {
			errMsg = "查询账单信息出现错误";
			logger.error("查询账单信息出现错误", e);
		}
		String returnStr = "";
		try {
			returnStr = printDataBiz.batchPrintData(queryBillList);
		} catch (Exception e) {
			errMsg = "封装账单信息出现错误";
			logger.error("封装账单信息出现错误", e);
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(returnStr);
		return null;
	}


	/***
	 * 判断临时账单是否生成过
	 * 
	 * @return
	 */
	public String isCreatedMainData() {
		String result = "success";
		try {
			accnoMainDataQueryParam.setAccNo(accNo);
			accnoMainDataQueryParam.setDocDate(date);
			queryBillList = printDataBiz
					.queryCheckMainDataInfoByAccnoAndDocdate(accnoMainDataQueryParam);
			if (queryBillList != null && queryBillList.size() > 0) {
				result = "fail";
			}
		} catch (XDocProcException e) {
			result = "查询账单时出现异常";
			logger.error("查询是否生成临时账单时出现异常:" + e.getMessage());
			e.printStackTrace();
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}


	public AccnoMainDataQueryParam getAccnoMainDataQueryParam() {
		return accnoMainDataQueryParam;
	}

	public void setAccnoMainDataQueryParam(
			AccnoMainDataQueryParam accnoMainDataQueryParam) {
		this.accnoMainDataQueryParam = accnoMainDataQueryParam;
	}

	public static WFLogger getLogger() {
		return logger;
	}

	public static void setLogger(WFLogger logger) {
		BatchPrintAction.logger = logger;
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

	public TreeMap<String, String> getRefDocstateMap() {

		if (null == refDocstateMap || refDocstateMap.size() == 0) {
			refDocstateMap = RefTableTools.ValRefDocstateMap;
		}
		return refDocstateMap;
	}

	public void setRefDocstateMap(TreeMap<String, String> refDocstateMap) {

		this.refDocstateMap = refDocstateMap;
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

	public void setQueryBillList(List<CheckMainData> QueryBillList) {
		this.queryBillList = QueryBillList;
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

	public String getSelectIds() {
		return selectIds;
	}

	public void setSelectIds(String selectIds) {
		this.selectIds = selectIds;
	}

	public String getMyFileName() {
		return myFileName;
	}

	public void setMyFileName(String myFileName) {
		this.myFileName = myFileName;
	}

	public TreeMap<String, String> getRefAccTypeMap() {
		if (null == refAccTypeMap || refAccTypeMap.size() == 0) {
			refAccTypeMap = RefTableTools.ValRefAcctypeMap;
		}
		return refAccTypeMap;
	}

	public void setRefAccTypeMap(TreeMap<String, String> refAccTypeMap) {
		this.refAccTypeMap = refAccTypeMap;
	}

	public String getIdCenter() {
		return idCenter;
	}

	public void setIdCenter(String idCenter) {
		this.idCenter = idCenter;
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

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public IDataProcessService getDataProcessServiceImpl() {
		return DataProcessServiceImpl;
	}

	public void setDataProcessServiceImpl(
			IDataProcessService dataProcessServiceImpl) {
		DataProcessServiceImpl = dataProcessServiceImpl;
	}

	public String getAccNo() {
		return accNo;
	}

	public void setAccNo(String accNo) {
		this.accNo = accNo;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public TreeMap<String, String> getRefSendMode() {
		if (null == refSendMode || refSendMode.size() == 0) {
			refSendMode = RefTableTools.ValRefSendModeMap;
		}
		return refSendMode;
	}

	public void setRefSendMode(TreeMap<String, String> refSendMode) {
		this.refSendMode = refSendMode;
	}

	public IPrintDataBiz getPrintDataBiz() {
		return printDataBiz;
	}

	public void setPrintDataBiz(IPrintDataBiz printDataBiz) {
		this.printDataBiz = printDataBiz;
	}

}
