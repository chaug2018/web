package com.yzj.ebs.report.biz.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yzj.ebs.common.ICheckMainDataAdm;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.report.biz.IEbillReportBiz;
import com.yzj.ebs.report.pojo.EbillResult;
import com.yzj.ebs.util.FinalConstant;
import com.yzj.wf.common.WFLogger;

/**
 * 创建于:2013-04-01<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 机构对账成功率统计  业务实现
 * 
 * @author 单伟龙
 * @version 1.0.0
 */
public class EbillReportBizImpl implements IEbillReportBiz{
	
	private static final java.text.DecimalFormat df = new java.text.DecimalFormat("#.##");
	
	private EbillResult result; // 统计结果类
	private List<EbillResult> resultList = new ArrayList<EbillResult>(); // 统计结果类集合，为了前台显示方便
	
	private ICheckMainDataAdm checkMainDataAdm; 
	
	private static WFLogger logger = WFLogger.getLogger(EbillReportBizImpl.class);

	public List<EbillResult> getEbillReportList(
			Map<String, String> queryMap, PageParam queryParam, 
			 boolean isPaged,String selectCount) {
		result = null;
		resultList.clear();
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
			
			List<?> list = checkMainDataAdm.getEbillReportList(queryMap,
					queryParam,isPaged,selectCount);
			
			if(selectCount!=null && selectCount.equals("countIdBank")){
				if (list != null && list.size() > 0) {
					String idCenter = "";
					String idBank = ""; // 机构号
					String bankName = ""; // 机构名称
					long sendCount = 0; // 对账单发出数，按账单
					long backCount = 0; // 回收数

					long checkSuccessCount = 0; // 余额相符数
					long checkFailCount = 0; // 余额不相符数
					long notCheckCount = 0; // 尚未核对数
					long ebillSuccessCount = 0; // 对账成功数
					
					String checkSuccessPercent = "";// 对账成功率
					String backPercent = ""; // 回收率
					
					
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
									backCount = Long.parseLong(obj[j].toString());
								}
								break;
							
							case 5:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									checkSuccessCount = Long.parseLong(obj[j]
											.toString());
								}
								break;
							case 6:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									checkFailCount = Long.parseLong(obj[j]
											.toString());
								}
								break;
							case 7:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									notCheckCount = Long.parseLong(obj[j]
											.toString());
								}
								break;
							case 8:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									ebillSuccessCount = Long.parseLong(obj[j]
											.toString());
								}
								break;
							}
						}
						float checkSuccessPercentTmp = 0;
						float backPercentTmp = 0;
						
						if (sendCount == 0) {
						} else {
							checkSuccessPercentTmp = (float) ebillSuccessCount / (float) sendCount * 100;
							backPercentTmp = (float) backCount / (float) sendCount * 100;
						}
						checkSuccessPercent = df.format(checkSuccessPercentTmp) + "%";
						backPercent = df.format(backPercentTmp) + "%";
						
						result = new EbillResult(idCenter, idBank, bankName, 
								sendCount, backCount, checkSuccessCount, 
								checkFailCount, notCheckCount, ebillSuccessCount, 
								checkSuccessPercent, backPercent);
						
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
					long backCount = 0; // 回收数
					
					long checkSuccessCount = 0; // 余额相符数
					long checkFailCount = 0; // 余额不相符数
					long notCheckCount = 0; // 尚未核对数
					long ebillSuccessCount = 0; // 对账成功数
					
					String checkSuccessPercent = "";// 对账成功率
					String backPercent = ""; // 回收率
					
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
									backCount = Long.parseLong(obj[j].toString());
								}
								break;
							
							case 3:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									checkSuccessCount = Long.parseLong(obj[j].toString());
								}
								break;
							case 4:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									checkFailCount = Long.parseLong(obj[j].toString());
								}
								break;
							case 5:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									notCheckCount = Long.parseLong(obj[j].toString());
								}
								break;
							case 6:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									ebillSuccessCount = Long.parseLong(obj[j].toString());
								}
								break;
							}
						}
						idBank = idCenter;
						bankName = idBranchNameMap.get(idCenter);
						
						float checkSuccessPercentTmp = 0;
						float backPercentTmp = 0;
						
						if (sendCount == 0) {
						} else {
							checkSuccessPercentTmp = (float) ebillSuccessCount / (float) sendCount * 100;
							backPercentTmp = (float) backCount / (float) sendCount * 100;
						}
						checkSuccessPercent = df.format(checkSuccessPercentTmp) + "%";
						backPercent = df.format(backPercentTmp) + "%";
						
						result = new EbillResult(idCenter, idBank, bankName, 
								sendCount, backCount, checkSuccessCount, 
								checkFailCount, notCheckCount, ebillSuccessCount, 
								checkSuccessPercent, backPercent);
						
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
					long backCount = 0; // 回收数
					
					long checkSuccessCount = 0; // 余额相符数
					long checkFailCount = 0; // 余额不相符数
					long notCheckCount = 0; // 尚未核对数
					long ebillSuccessCount = 0; // 对账成功数
					
					String checkSuccessPercent = "";// 对账成功率
					String backPercent = ""; // 回收率
					
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
									backCount = Long.parseLong(obj[j].toString());
								}
								break;
							
							case 2:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									checkSuccessCount = Long.parseLong(obj[j].toString());
								}
								break;
							case 3:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									checkFailCount = Long.parseLong(obj[j].toString());
								}
								break;
							case 4:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									notCheckCount = Long.parseLong(obj[j].toString());
								}
								break;
							case 5:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									ebillSuccessCount = Long.parseLong(obj[j].toString());
								}
								break;
								
							}
						}
						float checkSuccessPercentTmp = 0;
						float backPercentTmp = 0;
						
						if (sendCount == 0) {
						} else {
							checkSuccessPercentTmp = (float) ebillSuccessCount / (float) sendCount * 100;
							backPercentTmp = (float) backCount / (float) sendCount * 100;
						}
						checkSuccessPercent = df.format(checkSuccessPercentTmp) + "%";
						backPercent = df.format(backPercentTmp) + "%";
						
						result = new EbillResult(idCenter, idBank, bankName, 
								sendCount, backCount, checkSuccessCount, 
								checkFailCount, notCheckCount, ebillSuccessCount, 
								checkSuccessPercent, backPercent);
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

	public EbillResult getResult() {
		return result;
	}

	public void setResult(EbillResult result) {
		this.result = result;
	}

	public List<EbillResult> getResultList() {
		return resultList;
	}

	public void setResultList(List<EbillResult> resultList) {
		this.resultList = resultList;
	}

	public ICheckMainDataAdm getCheckMainDataAdm() {
		return checkMainDataAdm;
	}

	public void setCheckMainDataAdm(ICheckMainDataAdm checkMainDataAdm) {
		this.checkMainDataAdm = checkMainDataAdm;
	}

	public static java.text.DecimalFormat getDf() {
		return df;
	}
	
	

}
