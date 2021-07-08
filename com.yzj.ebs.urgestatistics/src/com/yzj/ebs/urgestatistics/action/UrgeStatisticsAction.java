package com.yzj.ebs.urgestatistics.action;

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
import com.yzj.ebs.common.IPublicTools;
import com.yzj.ebs.common.SimpleOrg;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.urgestatistics.biz.IUrgeStatisticsBiz;
import com.yzj.ebs.urgestatistics.param.UrgeStatisticsQueryParam;
import com.yzj.ebs.urgestatistics.param.UrgeStatisticsResultParam;
import com.yzj.wf.common.WFLogger;
import com.yzj.wf.core.model.po.wrapper.XPeopleInfo;
import com.yzj.wf.export.common.ExportEntity;
import com.yzj.wf.export.service.DataExporterImpl;
/**
 *创建于:2012-12-13<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 对账退信情况统计action
 * @author 单伟龙
 * @version 1.0.0
 *
 */
public class UrgeStatisticsAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4257506420919001148L;
	private static final java.text.DecimalFormat df = new java.text.DecimalFormat(
			"#.##");
	UrgeStatisticsQueryParam urgeStatisticsQueryParam;
	UrgeStatisticsResultParam result ;

	List<UrgeStatisticsResultParam> resultList;

	private IUrgeStatisticsBiz urgeStatisticsBizImpl;
	private IPublicTools tools;
	private static WFLogger logger = WFLogger
			.getLogger(UrgeStatisticsAction.class);

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
		urgeStatisticsQueryParam = new UrgeStatisticsQueryParam();
		errMsg = null;
		resultList=new ArrayList<UrgeStatisticsResultParam>();
		result=null;
		SimpleOrg org = null;
		idCenter="";
		idBranch="";
		idBank="";
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
	 * 获取退信统计数据,并计算退信率
	 * 
	 * @return
	 * @throws XDocProcException 
	 */
	public String getResult() throws XDocProcException {
		this.resultList= new ArrayList<UrgeStatisticsResultParam>();
		errMsg = null;
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		 idBank = urgeStatisticsQueryParam.getIdBank();
		 idCenter = urgeStatisticsQueryParam.getIdCenter();
		 idBranch = urgeStatisticsQueryParam.getIdBranch();

		 resultList = urgeStatisticsBizImpl.getUrgeStatisticsResult( createMap(), 
				 urgeStatisticsQueryParam,true,selectCount);
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
		List<UrgeStatisticsResultParam> exportList = new ArrayList<UrgeStatisticsResultParam>();
		exportList= urgeStatisticsBizImpl.getUrgeStatisticsResult(createMap(), 
				 urgeStatisticsQueryParam,false,selectCount);
		if (exportList.size() == 0 || exportList == null) {
			response.getWriter().write("退信情况统计列表为空");
			return "initSuccess";
		} else {
			ExportEntity entity=new ExportEntity();
			entity.setFileName("退信情况统计");
			entity.setTitle("退信情况统计");
			entity.setDataList(exportList);
			Map<String,String> pro_desc=new LinkedHashMap<String,String>();
			pro_desc.put("idCenter", "分行");	
			pro_desc.put("idBank", "网点");
			pro_desc.put("bankName", "网点名称");
			pro_desc.put("docDate", "对账日期");
			pro_desc.put("totalVouAmount", "账单总数");
			pro_desc.put("totalUrgeAmount", "退信总数");
			pro_desc.put("rejectedAmount", "单位拒收");
			pro_desc.put("addrChangedAmount", "原址拆迁");
			pro_desc.put("addrUnknownAmount", "地址不详");
			pro_desc.put("noRecieverAmount", "投递无人");
			pro_desc.put("unitNotExistAmount", "无此单位");
			pro_desc.put("addrNotExistAmount", "无此地址");
			pro_desc.put("noConnectionAmount", "无法联系");
			pro_desc.put("otherAmount", "其他");
			pro_desc.put("urgeRate", "退信率");
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
			String idBank = urgeStatisticsQueryParam.getIdBank();
			String idCenter = urgeStatisticsQueryParam.getIdCenter();
			String idBranch = urgeStatisticsQueryParam.getIdBranch();
			String idBank1=urgeStatisticsQueryParam.getIdBank1();
			
			this.idCenter = idCenter;
			this.idBranch = idBranch;
			this.idBank  = idBank;

			if (idBank != null && idBank.length() > 0) {
				queryMap.put("idBank", idBank);
				}
		   if (idBank1 != null && idBank1.length() > 0) {
				queryMap.put("idBank", idBank1);
				}
		     if (idBranch != null && idBranch.length() > 0) {
				queryMap.put("idBranch", idBranch);
			}   if (idCenter != null && idCenter.length() > 0) {
				queryMap.put("idCenter", idCenter);
			}
	
			
			String docDateTmp = urgeStatisticsQueryParam.getDocDate();
			String DocDate = docDateTmp.substring(0, 4).trim()
					+ docDateTmp.substring(5, 7).trim() + docDateTmp.substring(8).trim();
			queryMap.put("DocDate", DocDate); 
	
			return queryMap;
		}
	public UrgeStatisticsQueryParam getUrgeStatisticsQueryParam() {
		return urgeStatisticsQueryParam;
	}

	public void setUrgeStatisticsQueryParam(
			UrgeStatisticsQueryParam urgeStatisticsQueryParam) {
		this.urgeStatisticsQueryParam = urgeStatisticsQueryParam;
	}

	public List<UrgeStatisticsResultParam> getResultList() {
		return resultList;
	}

	public void setResultList(List<UrgeStatisticsResultParam> resultList) {
		this.resultList = resultList;
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
		UrgeStatisticsAction.logger = logger;
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

	public static java.text.DecimalFormat getDf() {
		return df;
	}

	public void setResult(UrgeStatisticsResultParam result) {
		this.result = result;
	}

	public IUrgeStatisticsBiz getUrgeStatisticsBizImpl() {
		return urgeStatisticsBizImpl;
	}

	public void setUrgeStatisticsBizImpl(IUrgeStatisticsBiz urgeStatisticsBizImpl) {
		this.urgeStatisticsBizImpl = urgeStatisticsBizImpl;
	}

	public String getSelectCount() {
		return selectCount;
	}

	public void setSelectCount(String selectCount) {
		this.selectCount = selectCount;
	}
	
}
