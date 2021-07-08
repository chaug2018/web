package com.yzj.ebs.report.biz.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.yzj.ebs.common.IAccnoMainDataAdm;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.report.biz.IActiveAccReportBiz;
import com.yzj.ebs.report.pojo.ActiveAccResult;
import com.yzj.wf.common.WFLogger;

/**
 * 创建于:2013-04-01<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 活跃账户情况统计  业务实现
 * 
 * @author 单伟龙
 * @version 1.0.0
 */
public class ActiveAccReportBizImpl implements IActiveAccReportBiz{
	
	private static final java.text.DecimalFormat df = new java.text.DecimalFormat("#.##");
	
	private ActiveAccResult result; // 统计结果类
	private List<ActiveAccResult> resultList = new ArrayList<ActiveAccResult>(); // 统计结果类集合，为了前台显示方便
	
	private IAccnoMainDataAdm accnoMainDataAdm; 
	
	private static WFLogger logger = WFLogger.getLogger(ActiveAccReportBizImpl.class);

	public List<ActiveAccResult> getActiveAccReportList(
			Map<String, String> queryMap, PageParam queryParam, 
			 boolean isPaged,String tableName,String workDate) {
		result = null;
		resultList.clear();
		try {
			
			List<?> list = accnoMainDataAdm.getActiveAccReportList(queryMap,
					queryParam,isPaged,tableName,workDate);
			
			
			if (list != null && list.size() > 0) {
				String idCenter = "";
				String idBank = ""; // 机构号
				String bankName = ""; // 机构名称
				String accno = "";
				String accname = "";
				long creditCount = 0;	//发生额明细数
				
				
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
								creditCount = Long.parseLong(obj[j].toString());
							}
							break;
						}
					}
					result = new ActiveAccResult(idCenter, idBank, bankName, accno,accname, creditCount);
					
					resultList.add(result);
				}
			}
		} catch (XDocProcException e) {
			logger.error("对账率统计查询数据库错误", e);
			return null;
		}
		return resultList;
	}

	
	public ActiveAccResult getResult() {
		return result;
	}


	public void setResult(ActiveAccResult result) {
		this.result = result;
	}


	public List<ActiveAccResult> getResultList() {
		return resultList;
	}


	public void setResultList(List<ActiveAccResult> resultList) {
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
