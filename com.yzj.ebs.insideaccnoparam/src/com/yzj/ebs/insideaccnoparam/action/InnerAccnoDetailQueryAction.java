package com.yzj.ebs.insideaccnoparam.action;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.opensymphony.xwork2.ActionSupport;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.database.InnerAccnoDetail;
import com.yzj.ebs.insideaccnoparam.biz.InnerAccnoDetailQueryBiz;
import com.yzj.ebs.insideaccnoparam.pojo.QueryParam;
import com.yzj.ebs.util.FinalConstant;
import com.yzj.wf.common.WFLogger;
import com.yzj.wf.export.common.ExportEntity;
import com.yzj.wf.export.service.DataExporterImpl;

/**
 * 
 * 创建于:2016-2-24<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 内部账户发生额明细查询
 * 
 * @author 
 * @version 1.0.0
 */
public class InnerAccnoDetailQueryAction extends ActionSupport {

	private static final long serialVersionUID = 1L;
	private WFLogger logger = WFLogger.getLogger(InnerAccnoDetailQueryAction.class);
	
	private QueryParam queryParam = new QueryParam();
	List<InnerAccnoDetail> queryList = new ArrayList<InnerAccnoDetail>();
	List<InnerAccnoDetail> exportList = new ArrayList<InnerAccnoDetail>();

	private Map<String, String> refDcFlagMap = new TreeMap<String, String>();

	private InnerAccnoDetailQueryBiz innerAccnoDetailQueryImpl;


	/**
	 * 初始化页面
	 * 
	 * @return
	 * @throws XDocProcException
	 */
	public String init() throws XDocProcException {
		queryList.clear();
		exportList.clear();
		queryParam = new QueryParam();
		refDcFlagMap = FinalConstant.dcflagName;
		
		return "initSuccess";
	}

	/**
	 * 查询发生额明细
	 * 
	 * @return
	 * @throws IOException
	 * @throws XDocProcException
	 */
	public String queryAccnoDetailData() throws IOException, SQLException {
		queryList = this.queryData(true);// 分页查询
		return "initSuccess";
	}

	/**
	 * 导出数据
	 * 
	 * @return
	 * @throws IOException
	 * @throws XDocProcException
	 */
	public String exportData() throws SQLException {

		exportList = this.queryData(false);// 全量查询
		String tableName = "内部账户发生额明细查询";
		ExportEntity entity=new ExportEntity();
		entity.setFileName(tableName);
		entity.setTitle(tableName);
		entity.setDataList(exportList);
		Map<String,String> pro_desc=new LinkedHashMap<String,String>();
		Map<String,Map<String,String>> paramsMap=new HashMap<String,Map<String,String>>();
		
		pro_desc.put("trad_date", "交易日期");
		pro_desc.put("acct", "交易账号");
		pro_desc.put("borrow_lend_sign", "借贷方向");
		pro_desc.put("oppost_acct", "对方账号");
		pro_desc.put("oppost_acct_name", "对方户名");
		pro_desc.put("trad_amt", "发生额");
		pro_desc.put("acct_bal", "实时余额");
		pro_desc.put("host_syst_time", "交易时间");
		pro_desc.put("summy", "摘要");
		paramsMap.put("borrow_lend_sign", refDcFlagMap);
		
		entity.setPro_desc_map(pro_desc);
		entity.setParamsMap(paramsMap);
		try {
			new DataExporterImpl().export(entity);
			return null;
		} catch (Throwable e) {
			logger.error("导出列表失败：" + e.getMessage());
		}
		return null;
	}

	/*
	 * 查询数据
	 */
	private List<InnerAccnoDetail> queryData(boolean isPaged)
			throws SQLException {
		return innerAccnoDetailQueryImpl.getAccnoDetailData(createQueryMap(),queryParam,isPaged);
	}

	/**
	 * 通过QueryParam拼装查询参数Map，作为分页查询方法的参数
	 * 
	 * @return 用于查询的Map
	 */
	private Map<String, String> createQueryMap() {
		Map<String, String> queryMap = new HashMap<String, String>();
		
		String accno = queryParam.getAccno();
		String tracedate = queryParam.getTraceDate();
		
		if (accno != null && accno.trim().length() > 0) {
			queryMap.put("acct", accno.trim());
		}
		queryMap.put("trad_date", tracedate);
		return queryMap;
	}

	public QueryParam getQueryParam() {
		return queryParam;
	}

	public void setQueryParam(QueryParam queryParam) {
		this.queryParam = queryParam;
	}

	public List<InnerAccnoDetail> getQueryList() {
		return queryList;
	}

	public void setQueryList(List<InnerAccnoDetail> queryList) {
		this.queryList = queryList;
	}

	public List<InnerAccnoDetail> getExportList() {
		return exportList;
	}

	public void setExportList(List<InnerAccnoDetail> exportList) {
		this.exportList = exportList;
	}

	public Map<String, String> getRefDcFlagMap() {
		if (null == refDcFlagMap || refDcFlagMap.size() == 0) {
			refDcFlagMap = FinalConstant.dcflagName;
		}
		return refDcFlagMap;
	}

	public void setRefDcFlagMap(Map<String, String> refDcFlagMap) {
		this.refDcFlagMap = refDcFlagMap;
	}

	public InnerAccnoDetailQueryBiz getInnerAccnoDetailQueryImpl() {
		return innerAccnoDetailQueryImpl;
	}

	public void setInnerAccnoDetailQueryImpl(
			InnerAccnoDetailQueryBiz innerAccnoDetailQueryImpl) {
		this.innerAccnoDetailQueryImpl = innerAccnoDetailQueryImpl;
	}


}
