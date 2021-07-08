package com.yzj.ebs.businessstatistics.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.yzj.ebs.businessstatistics.biz.IBusinessStatisticsBiz;
import com.yzj.ebs.businessstatistics.param.BusinessStatisticsParam;
import com.yzj.ebs.businessstatistics.param.BusinessStatisticsResultParam;
import com.yzj.ebs.common.IPublicTools;
import com.yzj.ebs.common.SimpleOrg;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.wf.common.WFLogger;
import com.yzj.wf.core.model.po.wrapper.XPeopleInfo;
import com.yzj.wf.export.common.ExportEntity;
import com.yzj.wf.export.service.DataExporterImpl;

/**
 * 创建于:2013-09-27<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 对账中心业务量统计action
 * 
 * @author dengwu
 * @version 1.0.0
 * 
 */
public class BusinessStatisticsAction extends ActionSupport {

	private static final long serialVersionUID = -1543579678632816032L;

	private BusinessStatisticsParam businessStatisticsParam;
	private List<BusinessStatisticsResultParam> resultList;
	private IBusinessStatisticsBiz businessStatisticsBizImpl;
	private IPublicTools tools;
	private static WFLogger logger = WFLogger
			.getLogger(BusinessStatisticsAction.class);

	private String errMsg;
	private String orgTree;

	// 查询下拉框的值
	private String idCenter;
	private String idBranch;
	private String idBank;
	private String selectCount = "";

	/**
	 * 初始化界面
	 * 
	 * @return
	 * @throws XDocProcException
	 */
	public String init() throws XDocProcException {
		businessStatisticsParam = new BusinessStatisticsParam();
		resultList = new ArrayList<BusinessStatisticsResultParam>();
		errMsg = null;
		SimpleOrg org = null;
		idCenter = "";
		idBranch = "";
		idBank = "";
		selectCount = "countIdBank";
		XPeopleInfo people = tools.getCurrLoginPeople();
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
	 * 获取催收统计数据，并计算催收成功率
	 * 
	 * @return
	 */
	public String getResult() {
		errMsg = null;
		this.resultList = new ArrayList<BusinessStatisticsResultParam>();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		idBank = businessStatisticsParam.getIdBank();
		idCenter = businessStatisticsParam.getIdCenter();
		idBranch = businessStatisticsParam.getIdBranch();
		String docDateTmp = businessStatisticsParam.getDocDate();
		String DocDate = docDateTmp.substring(0, 4).trim()
				+ docDateTmp.substring(5, 7).trim()
				+ docDateTmp.substring(8).trim();

		resultList = businessStatisticsBizImpl.getBusinessStatisticsResult(
				createMap(), businessStatisticsParam, DocDate,true,selectCount);

		return "initSuccess";
	}

	/**
	 * 导出统计结果
	 * 
	 * @return
	 * @throws IOException
	 */
	public String exportData() throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		List<BusinessStatisticsResultParam> exportList = new ArrayList<BusinessStatisticsResultParam>();
		String docDateTmp = businessStatisticsParam.getDocDate();
		String DocDate = docDateTmp.substring(0, 4).trim()
				+ docDateTmp.substring(5, 7).trim()
				+ docDateTmp.substring(8).trim();
		exportList = businessStatisticsBizImpl.getBusinessStatisticsResult(
				createMap(), businessStatisticsParam, DocDate,false,selectCount);
		if (exportList.size() == 0 || exportList == null) {
			response.getWriter().write("对账中心业务量统计列表为空");
			// tools.ajaxResult("对账中心业务量统计列表为空");
			return "initSuccess";
		} else {
			ExportEntity entity = new ExportEntity();
			entity.setFileName("对账中心业务量统计");
			entity.setTitle("对账中心业务量统计");
			entity.setDataList(exportList);
			Map<String, String> pro_desc = new LinkedHashMap<String, String>();
			pro_desc.put("idCenter", "分行");
			pro_desc.put("idBank", "网点");
			pro_desc.put("bankName", "网点名称");
			pro_desc.put("counterAccount", "柜台对账账户数");
			pro_desc.put("counterPercent", "占应对账账户数比例");
			pro_desc.put("postAccount", "邮寄对账账户数");
			pro_desc.put("postPercent", "占应对账账户数比例");
			pro_desc.put("netAcount", "网银对账账户数");
			pro_desc.put("netPercent", "占应对账账户数比例");
			pro_desc.put("faceAcount", "面对面对账账户数");
			pro_desc.put("facePercent", "占应对账账户数比例");
			pro_desc.put("otherAcount", "其它对账账户数");
			pro_desc.put("otherPercent", "占应对账账户数比例");
			pro_desc.put("shouldAcount", "应对账账户数");
			pro_desc.put("idCenterAcount", "对账中心账户数");

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

	/*
	 * @todo 完成查询条件的queryMap
	 */
	private Map<String, String> createMap() {
		// TODO Auto-generated method stub
		Map<String, String> queryMap = new HashMap<String, String>();
		String idBank = businessStatisticsParam.getIdBank();
		String idCenter = businessStatisticsParam.getIdCenter();
		String idBranch = businessStatisticsParam.getIdBranch();
		String idBank1 = businessStatisticsParam.getIdBank1();

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

		return queryMap;
	}

	public BusinessStatisticsParam getBusinessStatisticsParam() {
		return businessStatisticsParam;
	}

	public void setBusinessStatisticsParam(
			BusinessStatisticsParam businessStatisticsParam) {
		this.businessStatisticsParam = businessStatisticsParam;
	}

	public IBusinessStatisticsBiz getBusinessStatisticsBizImpl() {
		return businessStatisticsBizImpl;
	}

	public void setBusinessStatisticsBizImpl(
			IBusinessStatisticsBiz businessStatisticsBizImpl) {
		this.businessStatisticsBizImpl = businessStatisticsBizImpl;
	}

	public IPublicTools getTools() {
		return tools;
	}

	public void setTools(IPublicTools tools) {
		this.tools = tools;
	}

	public static WFLogger getLogger() {
		return logger;
	}

	public static void setLogger(WFLogger logger) {
		BusinessStatisticsAction.logger = logger;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
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

	public List<BusinessStatisticsResultParam> getResultList() {
		return resultList;
	}

	public void setResultList(List<BusinessStatisticsResultParam> resultList) {
		this.resultList = resultList;
	}

	public String getSelectCount() {
		return selectCount;
	}

	public void setSelectCount(String selectCount) {
		this.selectCount = selectCount;
	}
	
}
