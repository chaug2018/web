package com.yzj.ebs.report.biz.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.yzj.ebs.common.ICheckMainDataAdm;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.report.biz.ISealAccDetailReportBiz;
import com.yzj.ebs.report.pojo.SealAccDetailResult;
import com.yzj.wf.common.WFLogger;

/**
 * 创建于:2013-04-07<br>
 * 版权所有(C) 2013 深圳市银之杰科技股份有限公司<br>
 * 验印情况统计  业务实现
 * 
 * @author 单伟龙
 * @version 1.0.0
 */
public class SealAccDetailReportBiz implements ISealAccDetailReportBiz{
	
	private List<SealAccDetailResult> resultList = new ArrayList<SealAccDetailResult>(); // 统计结果类集合，为了前台显示方便
	private List<SealAccDetailResult> exportList = new ArrayList<SealAccDetailResult>(); // 统计结果类集合，为了前台显示方便
	private SealAccDetailResult result = null;
	private ICheckMainDataAdm checkMainDataAdm;
	private static WFLogger logger = WFLogger.getLogger(SealAccDetailReportBiz.class);
	private static final java.text.DecimalFormat df = new java.text.DecimalFormat("#.##");
	String errMsg = null;
	
    /**
     * 验印情况统计业务实现
     */
	@Override
	public List<SealAccDetailResult> getProveAccDetailReportList(
			Map<String, String> queryMap, PageParam pageParam, boolean isPaged)
			throws XDocProcException {
		exportList.clear();
		resultList.clear();
		result = null;
		try {
			List<?> list = checkMainDataAdm.getProveAccDetailReportList(queryMap, pageParam, isPaged);
			
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					String idCenter = "";
					String idBranch = "";
					String idBank = "";
					String bankName = "";
					String accno = "";
					String accname = "";
					
					long autoCount = 0;
					long manuCount = 0;
					long notProve = 0;
					long sendCount = 0; 
				
					long notPassCount = 0;
					long proveTotal = 0;
					
					String autoPercent = "";
					String manuPercent = "";
					
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
								idBranch = obj[j].toString();
							}
							break;
						case 2:
							if (obj[j] != null
							&& obj[j].toString().trim().length() > 0) {
								idBank = obj[j].toString();
							}
							break;
						case 3:
							if (obj[j] != null
							&& obj[j].toString().trim().length() > 0) {
								bankName = obj[j].toString();
							}
							break;
						case 4:
							if (obj[j] != null
							&& obj[j].toString().trim().length() > 0) {
								accno = obj[j].toString();
							}
							break;
						case 5:
							if (obj[j] != null
							&& obj[j].toString().trim().length() > 0) {
								accname = obj[j].toString();
							}
							break;
						case 6:
							if (obj[j] != null
							&& obj[j].toString().trim().length() > 0) {
								sendCount = Long.parseLong(obj[j].toString());
							}
							break;
						case 7:
							if (obj[j] != null
							&& obj[j].toString().trim().length() > 0) {
								autoCount = Long.parseLong(obj[j]
										.toString());
							}
							break;
						case 8:
							if (obj[j] != null
							&& obj[j].toString().trim().length() > 0) {
								manuCount = Long.parseLong(obj[j]
										.toString());
							}
							break;
						case 9:
							if (obj[j] != null
							&& obj[j].toString().trim().length() > 0) {
								notPassCount = Long.parseLong(obj[j]
										.toString());
							}
							break;
						case 10:
							if (obj[j] != null
							&& obj[j].toString().trim().length() > 0) {
								notProve = Long.parseLong(obj[j]
										.toString());
							}
							break;
						}
					}
					float autoPercentTmp = 0;
					float manuPercentTmp = 0;
					
					proveTotal = autoCount + manuCount + notPassCount;
					if (proveTotal == 0) {
					} else {
						autoPercentTmp = (float) autoCount / (float) proveTotal * 100;
						manuPercentTmp = (float) manuCount / (float) proveTotal * 100;
					}
					autoPercent = df.format(autoPercentTmp) + "%";
					manuPercent = df.format(manuPercentTmp) + "%";
					
					result = new SealAccDetailResult(accno, accname, idCenter, idBranch, idBank, bankName, 
							autoCount, manuCount, notPassCount, proveTotal, notProve, sendCount, autoPercent, manuPercent);
					resultList.add(result);
					exportList.add(result);
				}
			}
		
		} catch (XDocProcException e) {
			errMsg = "查询数据库出现错误!";
			logger.error("验印情况统计查询数据库错误", e);
		}
		if(isPaged){
			return resultList;
		}else{
			return exportList;
		}
	}
	public List<SealAccDetailResult> getResultList() {
		return resultList;
	}
	public void setResultList(List<SealAccDetailResult> resultList) {
		this.resultList = resultList;
	}
	public List<SealAccDetailResult> getExportList() {
		return exportList;
	}
	public void setExportList(List<SealAccDetailResult> exportList) {
		this.exportList = exportList;
	}
	public SealAccDetailResult getResult() {
		return result;
	}
	public void setResult(SealAccDetailResult result) {
		this.result = result;
	}

	public ICheckMainDataAdm getCheckMainDataAdm() {
		return checkMainDataAdm;
	}
	public void setCheckMainDataAdm(ICheckMainDataAdm checkMainDataAdm) {
		this.checkMainDataAdm = checkMainDataAdm;
	}
	public static WFLogger getLogger() {
		return logger;
	}
	public static void setLogger(WFLogger logger) {
		SealAccDetailReportBiz.logger = logger;
	}
}
