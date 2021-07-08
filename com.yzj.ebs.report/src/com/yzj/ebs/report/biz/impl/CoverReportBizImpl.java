package com.yzj.ebs.report.biz.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yzj.ebs.common.IAccnoMainDataAdm;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.report.biz.ICoverReportBiz;
import com.yzj.ebs.report.pojo.CoverResult;
import com.yzj.ebs.util.FinalConstant;
import com.yzj.wf.common.WFLogger;

/**
 * 创建于:2013-04-01<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 覆盖率统计  业务实现
 * 
 * @author 单伟龙
 * @version 1.0.0
 */
public class CoverReportBizImpl implements ICoverReportBiz{
	
	private static final java.text.DecimalFormat df = new java.text.DecimalFormat("#.##");
	
	private CoverResult result; // 统计结果类
	private List<CoverResult> resultList = new ArrayList<CoverResult>(); // 统计结果类集合，为了前台显示方便
	
	private IAccnoMainDataAdm accnoMainDataAdm; 
	
	private static WFLogger logger = WFLogger.getLogger(CoverReportBizImpl.class);

	public List<CoverResult> getCoverReportList(
			Map<String, String> queryMap, PageParam queryParam, 
			 boolean isPaged,String selectCount,String isThree) {
		result = null;
		resultList.clear();
		try {
    	   	List<?> idBranchNameList = null;
			Map<String,String> idBranchNameMap = new HashMap<String,String>();
			//获得所有清算中心的名字
			idBranchNameList = accnoMainDataAdm.getAllIdBranchName();
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
			
			List<?> list = accnoMainDataAdm.getCoverReportList(queryMap,
					queryParam,isPaged,selectCount,isThree);
			
			if(selectCount!=null && selectCount.equals("countIdBank")){
				if (list != null && list.size() > 0) {
					String idCenter = "";
					String idBank = ""; // 机构号
					String bankName = ""; // 机构名称
					long sendCount = 0; // 对账单发出数，按账单
					long successCount = 0; // 有过成功对账数，即覆盖数
					long failCount = 0; // 都未成功对账数
					long oneFailCount = 0;	//一次未成功对账数
					long twoFailCount = 0;	//两次未成功对账数
					long threeFailCount = 0;	//三次未成功对账数
					
					String coverPercent = ""; // 覆盖率
					
					
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
									sendCount = Long.parseLong(obj[j].toString());
								}
								break;
							case 4:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									successCount = Long.parseLong(obj[j].toString());
								}
								break;
							
							case 5:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									failCount = Long.parseLong(obj[j].toString());
								}
								break;
							
							}
						}
						
						if("1".equals(isThree)){
							List<?> countList = accnoMainDataAdm.getNotCheckCount(selectCount,idBank);
							if (countList != null && countList.size() > 0) {
								for (int m = 0; m < countList.size(); m++) {
									Object[] o = (Object[]) countList.get(m);
									for (int n = 0; n < o.length; n++) {
										switch (n) {
										case 0:
											if (o[n] != null && o[n].toString().trim().length() > 0) {
												oneFailCount = Long.parseLong(o[n].toString());
											}
											break;
										case 1:
											if (o[n] != null && o[n].toString().trim().length() > 0) {
												twoFailCount = Long.parseLong(o[n].toString());
											}
											break;
										case 2:
											if (o[n] != null && o[n].toString().trim().length() > 0) {
												threeFailCount = Long.parseLong(o[n].toString());
											}
											break;
										}
									}
								}
							}
							
						}
						
						float coverPercentTmp = 0;
						
						if (sendCount == 0) {
						} else {
							coverPercentTmp = (float) successCount / (float) sendCount * 100;
							failCount = sendCount - successCount;
						}
						coverPercent = df.format(coverPercentTmp) + "%";
						
						result = new CoverResult(idCenter, idBank, bankName, 
								sendCount, successCount, failCount, oneFailCount,
								twoFailCount,threeFailCount,coverPercent);
						
						resultList.add(result);
					}
				}
			}
			if(selectCount!=null && selectCount.equals("countIdCenter")){
				if (list != null && list.size() > 0) {
					String idCenter = "";
					String idBank = ""; // 机构号
					String bankName = ""; // 机构名称
					long sendCount = 0; // 对账单发出数，按账单
					long successCount = 0; // 有过成功对账数，即覆盖数
					long failCount = 0; // 都未成功对账数
					long oneFailCount = 0;	//一次未成功对账数
					long twoFailCount = 0;	//两次未成功对账数
					long threeFailCount = 0;	//三次未成功对账数
					String coverPercent = ""; // 覆盖率
					
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
									sendCount = Long.parseLong(obj[j].toString());
								}
								break;
							case 2:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									successCount = Long.parseLong(obj[j].toString());
								}
								break;
							
							case 3:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									failCount = Long.parseLong(obj[j].toString());
								}
								break;
							
							}
						}
						
						if("1".equals(isThree)){
							List<?> countList = accnoMainDataAdm.getNotCheckCount(selectCount,idCenter);
							if (countList != null && countList.size() > 0) {
								for (int m = 0; m < countList.size(); m++) {
									Object[] o = (Object[]) countList.get(m);
									for (int n = 0; n < obj.length; n++) {
										switch (n) {
										case 0:
											if (o[n] != null && o[n].toString().trim().length() > 0) {
												oneFailCount = Long.parseLong(o[n].toString());
											}
											break;
										case 1:
											if (o[n] != null && o[n].toString().trim().length() > 0) {
												twoFailCount = Long.parseLong(o[n].toString());
											}
											break;
										case 2:
											if (o[n] != null && o[n].toString().trim().length() > 0) {
												threeFailCount = Long.parseLong(o[n].toString());
											}
											break;
										}
									}
								}
							}
							
						}
						
						idBank = idCenter;
						bankName = idBranchNameMap.get(idCenter);
						
						float coverPercentTmp = 0;
						
						if (sendCount == 0) {
						} else {
							coverPercentTmp = (float) successCount / (float) sendCount * 100;
							failCount = sendCount - successCount;
						}
						coverPercent = df.format(coverPercentTmp) + "%";
						
						result = new CoverResult(idCenter, idBank, bankName, 
								sendCount, successCount, failCount, oneFailCount,
								twoFailCount,threeFailCount,coverPercent);
						
						resultList.add(result);
					}
				}
			}
			if(selectCount!=null && selectCount.equals("countIdBranch")){
				if (list != null && list.size() > 0) {
					String idCenter = FinalConstant.ROOTORG;
					String idBank = FinalConstant.ROOTORG; // 机构号
					String bankName = "华融湘江总行"; // 机构名称
					long sendCount = 0; // 对账单发出数，按账单
					long successCount = 0; // 有过成功对账数，即覆盖数
					long failCount = 0; // 都未成功对账数
					long oneFailCount = 0;	//一次未成功对账数
					long twoFailCount = 0;	//两次未成功对账数
					long threeFailCount = 0;	//三次未成功对账数
					String coverPercent = ""; // 覆盖率
					
					for (int i = 0; i < list.size(); i++) {
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
									successCount = Long.parseLong(obj[j].toString());
								}
								break;
							
							case 2:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									failCount = Long.parseLong(obj[j].toString());
								}
								break;
							
							}
						}
						
						if("1".equals(isThree)){
							List<?> countList = accnoMainDataAdm.getNotCheckCount("","");
							if (countList != null && countList.size() > 0) {
								for (int m = 0; m < countList.size(); m++) {
									Object[] o = (Object[]) countList.get(m);
									for (int n = 0; n < obj.length; n++) {
										switch (n) {
										case 0:
											if (o[n] != null && o[n].toString().trim().length() > 0) {
												oneFailCount = Long.parseLong(o[n].toString());
											}
											break;
										case 1:
											if (o[n] != null && o[n].toString().trim().length() > 0) {
												twoFailCount = Long.parseLong(o[n].toString());
											}
											break;
										case 2:
											if (o[n] != null && o[n].toString().trim().length() > 0) {
												threeFailCount = Long.parseLong(o[n].toString());
											}
											break;
										}
									}
								}
							}
							
						}
						
						float coverPercentTmp = 0;
						
						if (sendCount == 0) {
						} else {
							coverPercentTmp = (float) successCount / (float) sendCount * 100;
							failCount = sendCount - successCount;
						}
						coverPercent = df.format(coverPercentTmp) + "%";
						
						result = new CoverResult(idCenter, idBank, bankName, 
								sendCount, successCount, failCount, oneFailCount,
								twoFailCount,threeFailCount,coverPercent);
						
						resultList.add(result);
					}
				}
			}
		} catch (XDocProcException e) {
			logger.error("对账率统计查询数据库错误", e);
			return null;
		}
		return resultList;
	}

	public CoverResult getResult() {
		return result;
	}

	public void setResult(CoverResult result) {
		this.result = result;
	}

	public List<CoverResult> getResultList() {
		return resultList;
	}

	public void setResultList(List<CoverResult> resultList) {
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
