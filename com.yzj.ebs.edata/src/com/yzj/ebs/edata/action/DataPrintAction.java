/**
 * 创建于:2012-11-10<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * @author lif
 * @version 1.0
 */
package com.yzj.ebs.edata.action;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
import com.yzj.ebs.database.CheckMainData;
import com.yzj.ebs.edata.bean.AccnoMainDataQueryParam;
import com.yzj.ebs.edata.bean.BatchPrintBean;
import com.yzj.ebs.edata.biz.IPrintDataBiz;
import com.yzj.ebs.edata.service.IDataProcessService;
import com.yzj.wf.am.security.common.AMSecurityDefine;
import com.yzj.wf.common.WFLogger;
import com.yzj.wf.core.model.po.wrapper.XPeopleInfo;

/**
 * 对账单打印action
 * 
 * @author lif
 * 
 */
public class DataPrintAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2426853313699193849L;
	private static WFLogger logger = WFLogger.getLogger(DataPrintAction.class);
	private TreeMap<String, String> refCheckflagMap = new TreeMap<String, String>();// 余额状态
	private TreeMap<String, String> refDocstateMap = new TreeMap<String, String>();// 账单状态
	private TreeMap<String, String> refProveflagMap = new TreeMap<String, String>();// 验印状态
	private TreeMap<String, String> refSendMode = new TreeMap<String, String>();// 对账方式
	private TreeMap<String, String> refQueryTypeMap = new TreeMap<String, String>();
	List<CheckMainData> queryBillList = new ArrayList<CheckMainData>(); // 根据条件查询的账单信息
	private AccnoMainDataQueryParam accnoMainDataQueryParam;
	private IPrintDataBiz printDataBiz;
	private String errMsg = null;
	private String orgTree;
	private IPublicTools tools;
	private String idCenter; // 对账中心
	private String idBank; // 网点号
	private List<BatchPrintBean> lstData= new ArrayList<BatchPrintBean>() ; // 账单打印与jas的数据源
	private List<Map<String,String>> detailsData = new ArrayList<Map<String,String>>() ;
	private String selectIds = null; // 批量修改id，对应行号
	private String myFileName; // 打印文件名字
	private String flag;// 临时出账单标识
	private BatchPrintBean total = null;
	private String accNo;
	private String date;
	private IDataProcessService DataProcessServiceImpl; // 业务处理类
	private Map<String,Object> map = new HashMap<String,Object>();
	
	/**
	 * 初始化获取页面需要数据
	 */
	public String init() {
		queryBillList.clear();
		errMsg = null;
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
	 * 查询所有对账单信息
	 * 
	 * @return
	 * @throws IOException
	 * @throws XDocProcException
	 */
	public String queryBillinfoData() throws IOException {
		errMsg = null;
		queryBillList.clear();
		lstData.clear();
		selectIds = "";
		try {
			queryBillList = printDataBiz.queryCheckMainDataInfo(accnoMainDataQueryParam);
		} catch (XDocProcException e) {
			logger.error("查询打印的对账单出错！");
			e.printStackTrace();
		}	
			this.idBank = accnoMainDataQueryParam.getIdBank();
			this.idCenter = accnoMainDataQueryParam.getIdCenter();
		
		return "initSuccess";
	}
	
	/**
	 * 打印对账单数据
	 * 
	 * @return
	 * @throws IOException
	 */
	public String printData() throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		String printType = tools.getParameter("printType");
		logger.info(printType);
		CheckMainData data = (CheckMainData) queryBillList.get(Integer
				.valueOf(selectIds) - 1);
		try {
			lstData=printDataBiz.printData(data,accnoMainDataQueryParam,printType);
			String path = ServletActionContext.getServletContext().getRealPath("/WEB-INF/jasper");
			map.put("SUBREPORT_DIR", path+File.separator);
		} catch (Exception e) {
			e.printStackTrace();
		}
		myFileName = "batchprintinfo";
		return "success";
	}
	
	public String printDetailsData() throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		//String printType = tools.getParameter("printType");
		String firstResult = tools.getParameter("firstResult");
		int firstNum = Integer.parseInt(firstResult);
		String pageNum = tools.getParameter("pageNum");
		int pageSize = Integer.parseInt(pageNum);
		String totalPage = tools.getParameter("totalPage");
		String totalPage1 = tools.getParameter("totalPage1");
		logger.info("firstNum:"+firstNum+","+"pageSize:"+pageSize+","+"totalPage:"+totalPage+","+"totalPage1:"+totalPage1);
		
		CheckMainData data = (CheckMainData) queryBillList.get(Integer
				.valueOf(selectIds) - 1);
		try {
			detailsData=printDataBiz.printDetailsData(data,accnoMainDataQueryParam,firstNum,pageSize);
			String path = ServletActionContext.getServletContext().getRealPath("/WEB-INF/jasper");
			map.put("SUBREPORT_DIR", path+File.separator);
		} catch (Exception e) {
			e.printStackTrace();
		}
		myFileName = "batchprintinfo";
		return "success";
	}
	
	/**
	 * 得到对公明细明细的总数
	 * @return
	 */
	public void getDetailListCount(){
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		CheckMainData data = (CheckMainData) queryBillList.get(Integer
				.valueOf(selectIds) - 1);
		try {
			Map<String,String> queryMap =  new HashMap<String,String>();
			queryMap.put("voucherno", "'"+data.getVoucherNo()+"'");
			List<Object[]> detailList = printDataBiz.getDeatilCount(queryMap, accnoMainDataQueryParam.getDocDate().substring(5 ,7));
			String result = "";
			if(detailList.size()>0){
				result= isPaging(detailList.size());
			}else{
				result = "0";
			}
			tools.ajaxResult(result);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 得到对公明细明细的总数
	 * @return
	 */
	public void getDetailCount(){
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		CheckMainData data = (CheckMainData) queryBillList.get(Integer
				.valueOf(selectIds) - 1);
		try {
			Map<String,String> queryMap =  new HashMap<String,String>();
			queryMap.put("voucherno", "'"+data.getVoucherNo()+"'");
			List<Object[]> detailList = printDataBiz.getDeatilCount(queryMap, accnoMainDataQueryParam.getDocDate().substring(5 ,7));
			String result = "";
			if(detailList.size()>0){
				result= detailList.size()+"";
			}else{
				result = "0";
			}
			tools.ajaxResult(result);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
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
	
	/**
	 * 奇数为真  偶数为假
	 * @param count
	 * @return
	 */
	private  String isPaging(double count){
		String result = "true";
		double h  = count/25;
		h = Math.ceil(h);
		int j = (int) h;
		//偶数为 真
		if(j%2==0){
			result = "false";
		}
		return result;
	}
	
	
	/**
	 * 取得所有对账单的 对账单编号
	 * @return
	 * @throws IOException 
	 */
	public String getAllVoucherNo() throws IOException{
		HttpServletResponse response = ServletActionContext.getResponse();
		String result="";
		try {
			result = printDataBiz.getAllVoucherNo(accnoMainDataQueryParam);
		} catch (XDocProcException e) {
			response.getWriter().write("false");
			logger.error("获取所有打印数据出错!", e);
		}
			response.getWriter().write(result);
		return null;
	}
	
	/**
	 * 查询账单状态，如果是3：已收回  要提示客户是否继续打印
	 */
	public void getDocState(){
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		CheckMainData data = (CheckMainData) queryBillList.get(Integer.valueOf(selectIds) - 1);
		
		String result = "0";
		if(data!=null && "3".equals(data.getDocState())){//已收回
			result = "1";
		}
		tools.ajaxResult(result);
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
		DataPrintAction.logger = logger;
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

	public List<BatchPrintBean> getLstData() {
		return lstData;
	}

	public void setLstData(List<BatchPrintBean> lstData) {
		this.lstData = lstData;
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

	public BatchPrintBean getTotal() {
		return total;
	}

	public void setTotal(BatchPrintBean total) {
		this.total = total;
	}

	public IPrintDataBiz getPrintDataBiz() {
		return printDataBiz;
	}

	public void setPrintDataBiz(IPrintDataBiz printDataBiz) {
		this.printDataBiz = printDataBiz;
	}


	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}

	public List<Map<String, String>> getDetailsData() {
		return detailsData;
	}

	public void setDetailsData(List<Map<String, String>> detailsData) {
		this.detailsData = detailsData;
	}
	
}
