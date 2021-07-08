package com.yzj.ebs.report.biz.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.yzj.ebs.common.IAccnoMainDataAdm;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.report.biz.ICoverFailReportBiz;
import com.yzj.ebs.report.pojo.CoverFailResult;
import com.yzj.wf.common.WFLogger;

/**
 * 创建于:2013-04-01<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 连续对账未成功账户明细  业务实现
 * 
 * @author 单伟龙
 * @version 1.0.0
 */
public class CoverFailReportBizImpl implements ICoverFailReportBiz{
	
	private static final java.text.DecimalFormat df = new java.text.DecimalFormat("#.##");
	
	private CoverFailResult result; // 统计结果类
	private List<CoverFailResult> resultList = new ArrayList<CoverFailResult>(); // 统计结果类集合，为了前台显示方便
	
	private IAccnoMainDataAdm accnoMainDataAdm; 
	
	private static WFLogger logger = WFLogger.getLogger(CoverFailReportBizImpl.class);

	public List<CoverFailResult> getCoverFailReportList(Map<String, String> queryMap, 
			PageParam queryParam, boolean isPaged) {
		result = null;
		resultList.clear();
		try {
    	   	
			
			List<?> list = accnoMainDataAdm.getCoverFailReportList(queryMap,
					queryParam,isPaged);
			
			if (list != null && list.size() > 0) {
				String idCenter = "";
				String idBank = ""; // 机构号
				String bankName = ""; // 机构名称
				
				String accno = "";
				String accname = ""; // 户名
				String docState = ""; // 账单状态
				String docdate = "";
				String checkFlag = "不符";//对账结果 全是不符的
				String sendmode = ""; // 发送方式
				String accState = ""; // 账户状态
				long notCheckCount = 0; // 未对账次数
				
				
				for (int i = 0; i < list.size(); i++) {
					Object[] obj = (Object[]) list.get(i);
					for (int j = 0; j < obj.length; j++) {
						switch (j) {
						case 0:
							if (obj[j] != null
							&& obj[j].toString().trim().length() > 0) {
								idCenter = obj[j].toString();
							}
							break;
						case 1:
							if (obj[j] != null
							&& obj[j].toString().trim().length() > 0) {
								idBank = obj[j].toString();
							}
							break;
						case 2:
							if (obj[j] != null
							&& obj[j].toString().trim().length() > 0) {
								bankName = obj[j].toString();
							}
							break;
						case 3:
							if (obj[j] != null
							&& obj[j].toString().trim().length() > 0) {
								accno = obj[j].toString();
							}
							break;
						case 4:
							if (obj[j] != null
							&& obj[j].toString().trim().length() > 0) {
								accname = obj[j].toString();
							}
							break;
						
						case 5:
							if (obj[j] != null
							&& obj[j].toString().trim().length() > 0) {
								docState = obj[j].toString();
							}
							break;
						case 6:
							if (obj[j] != null
							&& obj[j].toString().trim().length() > 0) {
								docdate = obj[j].toString();
							}
							break;
						case 7:
							if (obj[j] != null
							&& obj[j].toString().trim().length() > 0) {
								sendmode = obj[j].toString();
							}
							break;
						case 8:
							if (obj[j] != null
							&& obj[j].toString().trim().length() > 0) {
								accState = obj[j].toString();
							}
							break;
						
						case 9:
							if (obj[j] != null
							&& obj[j].toString().trim().length() > 0) {
								notCheckCount = Long.parseLong(obj[j].toString());
							}
							break;
						}
					}
					
					result = new CoverFailResult(idCenter, idBank, bankName, 
							accno, accname, docState, docdate, checkFlag, 
							sendmode, accState, notCheckCount);
					
					resultList.add(result);
				}
			}
		} catch (XDocProcException e) {
			logger.error("对账率统计查询数据库错误", e);
			return null;
		}
		return resultList;
	}

	public CoverFailResult getResult() {
		return result;
	}

	public void setResult(CoverFailResult result) {
		this.result = result;
	}

	public List<CoverFailResult> getResultList() {
		return resultList;
	}

	public void setResultList(List<CoverFailResult> resultList) {
		this.resultList = resultList;
	}

	public IAccnoMainDataAdm getAccnoMainDataAdm() {
		return accnoMainDataAdm;
	}

	public void setAccnoMainDataAdm(IAccnoMainDataAdm accnoMainDataAdm) {
		this.accnoMainDataAdm = accnoMainDataAdm;
	}

	public static java.text.DecimalFormat getDf() {
		return df;
	}
	
}
