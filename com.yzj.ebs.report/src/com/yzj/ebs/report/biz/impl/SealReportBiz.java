package com.yzj.ebs.report.biz.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yzj.ebs.common.ICheckMainDataAdm;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.report.biz.ISealReportBiz;
import com.yzj.ebs.report.pojo.SealResult;
import com.yzj.ebs.util.FinalConstant;
import com.yzj.wf.common.WFLogger;

/**
 * 创建于:2013-04-07<br>
 * 版权所有(C) 2013 深圳市银之杰科技股份有限公司<br>
 * 验印情况统计  业务实现
 * 
 * @author 单伟龙
 * @version 1.0.0
 */
public class SealReportBiz implements ISealReportBiz{
	
	private List<SealResult> resultList = new ArrayList<SealResult>(); // 统计结果类集合，为了前台显示方便
	private List<SealResult> exportList = new ArrayList<SealResult>(); // 统计结果类集合，为了前台显示方便
	private SealResult result = null;
	private ICheckMainDataAdm checkMainDataAdm;
	private static WFLogger logger = WFLogger.getLogger(SealReportBiz.class);
	private static final java.text.DecimalFormat df = new java.text.DecimalFormat(
			"#.##");
	String errMsg = null;
    /**
     * 验印情况统计业务实现
     */
	@Override
	public List<SealResult> getProveReportList(
			Map<String, String> queryMap, PageParam pageParam, boolean isPaged,String selectCount)
			throws XDocProcException {
		exportList.clear();
		resultList.clear();
		result = null;
		try {
			List<?> idBranchNameList = null;
			Map<String,String> idBranchNameMap = new HashMap<String,String>();
			//获得所有清算中心的名字
			idBranchNameList = checkMainDataAdm.getAllIdBranchName();
			for (int i = 0; i < idBranchNameList.size(); i++) {
				String idBranch = "";
				String idBranchName = "";
				Object[] obj = (Object[]) idBranchNameList.get(i);
				for (int j = 0; j < obj.length; j++) {
					switch (j) {
					case 0:
						if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
							idBranch = obj[j].toString();
						}
						break;
					case 1:
						if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
							idBranchName = obj[j].toString();
							idBranchName = idBranchName.replace("清算中心", "");
						}
						break;
					}
				}
				idBranchNameMap.put(idBranch, idBranchName);
			}
			
			//查询数据库
			List<?> list = checkMainDataAdm.getProveReportListCount(queryMap, pageParam, isPaged, selectCount);
		
			if(selectCount!=null && selectCount.equals("countIdBank")){
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						String idCenter = "";
						String idBranch = "";
						String idBank = "";
						String bankName = "";
						
						long sendCount = 0; 	//账户数
						long proveTotal = 0; 	//验印总数
						long autoCount = 0;		//自动验印通过数	
						long manuCount = 0;		//手动验印通过数
						long notPassCount = 0;	//未通过数
						
						long notProve = 0;	//未验印数
						String provePercent = "";
						String autoPercent = "";
						
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
									sendCount = Long.parseLong(obj[j].toString());
								}
								break;
							case 5:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									autoCount = Long.parseLong(obj[j].toString());
								}
								break;
							case 6:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									manuCount = Long.parseLong(obj[j].toString());
								}
								break;
							case 7:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									notPassCount = Long.parseLong(obj[j].toString());
								}
								break;
							case 8:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									notProve = Long.parseLong(obj[j]
											.toString());
								}
								break;
							}
						}
						
						float provePercentTmp = 0;
						float autoPercentTmp = 0;
						proveTotal = autoCount + manuCount + notPassCount;//sendCount - notProve;
						if (proveTotal == 0) {
						} else {
							provePercentTmp = (float) (autoCount+manuCount) / (float) proveTotal * 100;
							autoPercentTmp= (float) autoCount / (float) proveTotal * 100;
						}
						
						provePercent = df.format(provePercentTmp) + "%";
						autoPercent = df.format(autoPercentTmp) + "%";
						result = new SealResult(idCenter, idBranch, idBank, bankName, 
								autoCount, manuCount, notPassCount, sendCount, proveTotal,
								notProve, provePercent, autoPercent);
								
						resultList.add(result);
						exportList.add(result);
					}
				} 
			}
			
			if(selectCount!=null && selectCount.equals("countIdCenter")){
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						String idCenter = "";
						String idBranch = "";
						String idBank = "";
						String bankName = "";

						long sendCount = 0; 	//账户数
						long proveTotal = 0; 	//验印总数
						long autoCount = 0;		//自动验印通过数	
						long manuCount = 0;		//手动验印通过数
						long notPassCount = 0;	//未通过数
						
						long notProve = 0;	//未验印数
						String provePercent = "";
						String autoPercent = "";
						
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
									sendCount = Long.parseLong(obj[j].toString());
								}
								break;
							case 2:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									autoCount = Long.parseLong(obj[j].toString());
								}
								break;
							case 3:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									manuCount = Long.parseLong(obj[j].toString());
								}
								break;
							case 4:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									notPassCount = Long.parseLong(obj[j].toString());
								}
								break;
							case 5:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									notProve = Long.parseLong(obj[j].toString());
								}
								break;
							}
						}
						idBank = idCenter;
						bankName = idBranchNameMap.get(idCenter);
						
						float provePercentTmp = 0;
						float autoPercentTmp = 0;
						proveTotal = autoCount + manuCount + notPassCount;
						if (proveTotal == 0) {
						} else {
							provePercentTmp = (float) (autoCount+manuCount) / (float) proveTotal * 100;
							autoPercentTmp= (float) autoCount / (float) proveTotal * 100;
						}
						
						provePercent = df.format(provePercentTmp) + "%";
						autoPercent = df.format(autoPercentTmp) + "%";
						result = new SealResult(idCenter, idBranch, idBank, bankName, 
								autoCount, manuCount, notPassCount, sendCount, proveTotal,
								notProve, provePercent, autoPercent);
						
						resultList.add(result);
						exportList.add(result);
					}
				} 
			}
			
			if(selectCount!=null && selectCount.equals("countIdBranch")) {
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						String idCenter = FinalConstant.ROOTORG;
						String idBranch = FinalConstant.ROOTORG;
						String idBank = FinalConstant.ROOTORG;
						String bankName = "华融湘江总行";

						long sendCount = 0; 	//账户数
						long proveTotal = 0; 	//验印总数
						long autoCount = 0;		//自动验印通过数	
						long manuCount = 0;		//手动验印通过数
						long notPassCount = 0;	//未通过数
						
						long notProve = 0;	//未验印数
						String provePercent = "";
						String autoPercent = "";
						
						Object[] obj = (Object[]) list.get(i);
						for (int j = 0; j < obj.length; j++) {
							switch (j) {
							case 0:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									sendCount = Long.parseLong(obj[j].toString());
								}
								break;
							case 1:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									autoCount = Long.parseLong(obj[j].toString());
								}
								break;
							case 2:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									manuCount = Long.parseLong(obj[j].toString());
								}
								break;
							case 3:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									notPassCount = Long.parseLong(obj[j].toString());
								}
								break;
							case 4:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									notProve = Long.parseLong(obj[j].toString());
								}
								break;
							}
						}
						
						float provePercentTmp = 0;
						float autoPercentTmp = 0;
						proveTotal = autoCount + manuCount + notPassCount;
						if (proveTotal == 0) {
						} else {
							provePercentTmp = (float) (autoCount+manuCount) / (float) proveTotal * 100;
							autoPercentTmp= (float) autoCount / (float) proveTotal * 100;
						}
						
						provePercent = df.format(provePercentTmp) + "%";
						autoPercent = df.format(autoPercentTmp) + "%";
						result = new SealResult(idCenter, idBranch, idBank, bankName, 
								autoCount, manuCount, notPassCount, sendCount, proveTotal,
								notProve, provePercent, autoPercent);
						
						resultList.add(result);
						exportList.add(result);
					}
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
	public List<SealResult> getResultList() {
		return resultList;
	}
	public void setResultList(List<SealResult> resultList) {
		this.resultList = resultList;
	}
	public List<SealResult> getExportList() {
		return exportList;
	}
	public void setExportList(List<SealResult> exportList) {
		this.exportList = exportList;
	}
	public SealResult getResult() {
		return result;
	}
	public void setResult(SealResult result) {
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
		SealReportBiz.logger = logger;
	}
}
