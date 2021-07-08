package com.yzj.ebs.seal.analyse.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.yzj.ebs.common.IPublicTools;
import com.yzj.ebs.common.SimpleOrg;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.seal.analyse.biz.ISealAnalyseBiz;
import com.yzj.ebs.seal.analyse.pojo.AnalyseResult;
import com.yzj.ebs.seal.analyse.pojo.QueryParam;
import com.yzj.wf.common.WFLogger;
import com.yzj.wf.core.model.po.wrapper.XPeopleInfo;
import com.yzj.wf.export.common.ExportEntity;
import com.yzj.wf.export.service.DataExporterImpl;

/**
 * 
 * 创建于:2012-11-15<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 验印情况统计处理类
 * 
 * @author 施江敏 单伟龙
 * @version 1.0.0
 */
public class SealAnalyse extends ActionSupport {

	private static final long serialVersionUID = 3375569970222364029L;

	private QueryParam queryParam;
	private AnalyseResult result; // 统计结果类
	private List<AnalyseResult> resultList = new ArrayList<AnalyseResult>();// 统计结果类集合，为了前台显示方便
	private List<AnalyseResult> exportList = new ArrayList<AnalyseResult>(); // 统计结果类集合，为了前台显示方便

	private String errMsg = "";
	private IPublicTools tools;
	private String orgTree;
	private ISealAnalyseBiz sealAnalyseBizImpl;
	private static WFLogger logger = WFLogger.getLogger(SealAnalyse.class);

	// 查询下拉框的值
	private String idCenter;
	private String idBranch;
	private String idBank;
	private String selectCount;

	/**
	 * 初始化界面
	 * 
	 * @return
	 * @throws XDocProcException
	 */
	public String init() throws XDocProcException {
		errMsg = null;
		queryParam = new QueryParam();
		resultList.clear();
		exportList.clear();
		idCenter = "";
		idBank = "";

		result = null;
		SimpleOrg org = null;
		idCenter = "";
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
	 * 统计数据
	 * 
	 * @return
	 * @throws IOException
	 * @throws XDocProcException
	 */
	public String analyse() throws IOException, XDocProcException {
		resultList = new ArrayList<AnalyseResult>();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		this.idCenter = queryParam.getIdCenter();
		this.idBranch = queryParam.getIdBranch();
		this.idBank = queryParam.getIdBank();

		resultList = this.queryData(true);
		return "initSuccess";
	}

	/**
	 * 导出统计结果
	 * 
	 * @return
	 * @throws IOException
	 * @throws XDocProcException
	 */
	public String exportData() throws IOException, XDocProcException {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		exportList = new ArrayList<AnalyseResult>();
		exportList = this.queryData(false);
		if (exportList.size() == 0 || exportList == null) {
			response.getWriter().write("验印情况统计列表为空");
		} else {
			ExportEntity entity = new ExportEntity();
			entity.setFileName("验印情况统计");
			entity.setTitle("验印情况统计");
			entity.setDataList(exportList);
			Map<String, String> pro_desc = new LinkedHashMap<String, String>();
			pro_desc.put("idCenter", "分行");
			pro_desc.put("idBank", "网点");
			pro_desc.put("bankName", "网点名称");
			pro_desc.put("docDate", "对账日期");
			pro_desc.put("sendCount", "对账单发出数");
			pro_desc.put("proveMatchCount", "验印相符数");
			pro_desc.put("proveNotMatchCount", "验印不符数");
			pro_desc.put("notProve", "尚未验印数");
			pro_desc.put("provePercent", "验印成功率");
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

	/**
	 * 生成查询Map,把查询条件封装成map
	 * 
	 * @return
	 */
	public Map<String, String> createQueryMap() {
		Map<String, String> queryMap = new TreeMap<String, String>();
		String idBank = queryParam.getIdBank();
		String idBank1 = queryParam.getIdBank1();
		String idCenter = queryParam.getIdCenter();
		String idBranch = queryParam.getIdBranch();
		//回显
		this.idCenter = idCenter;
		this.idBranch = idBranch;
		this.idBank = idBank;
		//创建查询Map
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
		queryParam.setIdCenter("");// 清空值
		queryParam.setIdBranch("");// 清空 值
		queryParam.setIdBank("");// 清空值
		String docDateTmp = queryParam.getDocDate();
		String docDate = docDateTmp.substring(0, 4)
				+ docDateTmp.substring(5, 7) + docDateTmp.substring(8);
		queryMap.put("docDate", docDate);
		return queryMap;
	}

	/**
	 * 操作数据库
	 * 
	 * @param isPaged
	 * @return
	 * @throws XDocProcException
	 */
	private List<AnalyseResult> queryData(boolean isPaged)
			throws XDocProcException {

		if (isPaged) {
			resultList = sealAnalyseBizImpl.getProveAnalyseList(
					createQueryMap(), queryParam, true,selectCount);
			return resultList;
		} else {
			exportList = sealAnalyseBizImpl.getProveAnalyseList(
					createQueryMap(), queryParam, false,selectCount);
			return exportList;
		}

	}

	public QueryParam getQueryParam() {
		return queryParam;
	}

	public void setQueryParam(QueryParam queryParam) {
		this.queryParam = queryParam;
	}

	public AnalyseResult getResult() {
		return result;
	}

	public void setResult(AnalyseResult result) {
		this.result = result;
	}

	public List<AnalyseResult> getResultList() {
		return resultList;
	}

	public void setResultList(List<AnalyseResult> resultList) {
		this.resultList = resultList;
	}

	public IPublicTools getTools() {
		return tools;
	}

	public void setTools(IPublicTools tools) {
		this.tools = tools;
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

	public ISealAnalyseBiz getSealAnalyseBizImpl() {
		return sealAnalyseBizImpl;
	}

	public void setSealAnalyseBizImpl(ISealAnalyseBiz sealAnalyseBizImpl) {
		this.sealAnalyseBizImpl = sealAnalyseBizImpl;
	}

	public String getSelectCount() {
		return selectCount;
	}

	public void setSelectCount(String selectCount) {
		this.selectCount = selectCount;
	}

}
