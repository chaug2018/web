package com.yzj.ebs.rushstatistics.action;

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
import com.yzj.ebs.rushstatistics.biz.IRushStatisticsBiz;
import com.yzj.ebs.rushstatistics.param.RushStatisticsParam;
import com.yzj.ebs.rushstatistics.param.RushStatisticsResultParam;
import com.yzj.wf.common.WFLogger;
import com.yzj.wf.core.model.po.wrapper.XPeopleInfo;
import com.yzj.wf.export.common.ExportEntity;
import com.yzj.wf.export.service.DataExporterImpl;
/**
 * 创建于:2012-11-19<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 催收统计action
 * @author 单伟龙
 * @version 1.0.0
 *
 */
public class RushStatisticsAction extends ActionSupport {

	/**
	 * @author 单伟龙
	 * @todo 对账催收统计报表
	 * @version 1.0.0
	 * 
	 */
	private static final long serialVersionUID = -1543579678632816032L;
	

	private RushStatisticsParam rushStatisticsParam;
	private List<RushStatisticsResultParam> resultList ;

	private IRushStatisticsBiz rushStatisticsBizImpl;
	private IPublicTools tools;
	private static WFLogger logger = WFLogger
			.getLogger(RushStatisticsAction.class);

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
		rushStatisticsParam = new RushStatisticsParam();
		resultList= new ArrayList<RushStatisticsResultParam>();
		errMsg = null;
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
	 * 获取催收统计数据，并计算催收成功率
	 * 
	 * @return
	 */
	public String getResult() {
		errMsg = null;
	    this.resultList=new ArrayList<RushStatisticsResultParam>();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		 idBank = rushStatisticsParam.getIdBank();
		 idCenter = rushStatisticsParam.getIdCenter();
		 idBranch = rushStatisticsParam.getIdBranch();
		 
		 resultList = rushStatisticsBizImpl.getRushStatisticsResult(createMap(), 
				 rushStatisticsParam,true,selectCount);
			
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
		List<RushStatisticsResultParam> exportList = new ArrayList<RushStatisticsResultParam>();
		exportList=rushStatisticsBizImpl.getRushStatisticsResult(createMap(), 
				 rushStatisticsParam,false,selectCount);
		if (exportList.size() == 0 || exportList == null) {
			response.getWriter().write("催收情况统计列表为空");
			return "initSuccess";
		} else {
			ExportEntity entity=new ExportEntity();
			entity.setFileName("催收情况统计");
			entity.setTitle("催收情况统计");
			entity.setDataList(exportList);
			Map<String,String> pro_desc=new LinkedHashMap<String, String>();
			pro_desc.put("idCenter", "分行");
			pro_desc.put("idBank", "网点");
			pro_desc.put("bankName", "网点名称");
			pro_desc.put("docDate", "对账日期");
			pro_desc.put("totalAmount", "对账单数量");
			pro_desc.put("rushedAmount", "应催收总数");
			pro_desc.put("successAmount", "催收成功数");
			pro_desc.put("telAmount", "电话催收数");
			pro_desc.put("emailAmount", "邮件催收数");
			pro_desc.put("faceAmount", "面对面催收数");
			pro_desc.put("successRate", "催收成功率");
			
			
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
			String idBank = rushStatisticsParam.getIdBank();
			String idCenter = rushStatisticsParam.getIdCenter();
			String idBranch = rushStatisticsParam.getIdBranch();
			String idBank1=rushStatisticsParam.getIdBank1();
			
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
			String docDateTmp = rushStatisticsParam.getDocDate();
			String DocDate = docDateTmp.substring(0, 4).trim()
					+ docDateTmp.substring(5, 7).trim() + docDateTmp.substring(8).trim();
			queryMap.put("DocDate", DocDate); 
			return queryMap;
		}
	
	public IRushStatisticsBiz getRushStatisticsBizImpl() {
			return rushStatisticsBizImpl;
		}

	public void setRushStatisticsBizImpl(IRushStatisticsBiz rushStatisticsBizImpl) {
			this.rushStatisticsBizImpl = rushStatisticsBizImpl;
		}

	public RushStatisticsParam getRushStatisticsParam() {
		return rushStatisticsParam;
	}
	public void setRushStatisticsParam(RushStatisticsParam rushStatisticsParam) {
		this.rushStatisticsParam = rushStatisticsParam;
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
		RushStatisticsAction.logger = logger;
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

	public List<RushStatisticsResultParam> getResultList() {
		return resultList;
	}

	public void setResultList(List<RushStatisticsResultParam> resultList) {
		this.resultList = resultList;
	}

	public String getSelectCount() {
		return selectCount;
	}

	public void setSelectCount(String selectCount) {
		this.selectCount = selectCount;
	}
	
}
