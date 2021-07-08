package com.yzj.ebs.ebill.analyse.biz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yzj.ebs.common.ICheckMainDataAdm;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.ebill.analyse.pojo.AnalyseResult;
import com.yzj.ebs.util.FinalConstant;
import com.yzj.wf.common.WFLogger;

/**
 * 创建于:2013-04-01<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 机构对账率统计  业务实现
 * 
 * @author 单伟龙
 * @version 1.0.0
 */
public class EbillAnalyseBizImpl implements IEbillAnalyseBiz{
	
	private static final java.text.DecimalFormat df = new java.text.DecimalFormat(
			"#.##");
	
	private AnalyseResult result; // 统计结果类
	private List<AnalyseResult> resultList = new ArrayList<AnalyseResult>(); // 统计结果类集合，为了前台显示方便
	
	private ICheckMainDataAdm checkMainDataAdm; 
	
	private static WFLogger logger = WFLogger.getLogger(EbillAnalyseBizImpl.class);

	public List<AnalyseResult> getEbillAnalyseList(
			Map<String, String> queryMap, PageParam queryParam, 
			 boolean isPaged,String selectCount) {
		// TODO Auto-generated method stub
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
						}
						break;
					}
				}
				idBranchNameMap.put(idBranch, idBranchName);
			}
			
			List<?> list = null;
			if(selectCount!=null && selectCount.equals("countIdBank")){
				list = checkMainDataAdm.getEbillAnalyseList(queryMap,
						queryParam,isPaged,selectCount);
			}else{
				list = checkMainDataAdm.getEbillAnalyseListCount(queryMap,
						queryParam, isPaged, selectCount);
			}
			
			if(selectCount!=null && selectCount.equals("countIdBank")){
				if (list != null && list.size() > 0) {
					String idCenter = "";
					String docDate = ""; // 对账日期
					String idBank = ""; // 机构号
					String bankName = ""; // 机构名称
					long sendCount = 0; // 对账单发出数，按账单
					long retreatCount = 0; // 退信数
					long backCount = 0; // 回收数
					long proveMatchCount = 0; // 验印相符数
					long proveNotMatchCount = 0; // 验印不符数
					long wjkCount = 0; // 未建库数
					long checkSuccessCount = 0; // 余额相符数
					long checkFailCount = 0; // 余额不相符数
					long ebillSuccessCount = 0; // 对账成功数
					
					String checkSuccessPercent = "";// 对账成功率
					String backPercent = ""; // 回收率
					String proveMatchPercent = ""; // 验印成功率
					String proveNotMatchPercent = ""; // 验印不符率
					
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
									docDate = obj[j].toString();
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
									backCount = Long
											.parseLong(obj[j].toString());
								}
								break;
							case 6:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									retreatCount = Long.parseLong(obj[j].toString());
								}
								break;
							case 7:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									proveMatchCount = Long.parseLong(obj[j]
											.toString());
								}
								break;
							case 8:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									proveNotMatchCount = Long.parseLong(obj[j]
											.toString());
								}
								break;
							case 9:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									wjkCount = Long.parseLong(obj[j].toString());
								}
								break;
							case 10:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									checkSuccessCount = Long.parseLong(obj[j]
											.toString());
								}
								break;
							case 11:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									checkFailCount = Long.parseLong(obj[j]
											.toString());
								}
								break;
							case 12:
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
						float proveMatchPercentTmp = 0;
						float proveNotMatchPercentTmp = 0;
						
						if (sendCount == 0) {
						} else {
							checkSuccessPercentTmp = (float) ebillSuccessCount
									/ (float) sendCount * 100;
							backPercentTmp = (float) backCount / (float) sendCount
									* 100;
							proveMatchPercentTmp = (float) proveMatchCount
									/ (float) sendCount * 100;
							proveNotMatchPercentTmp = (float) proveNotMatchCount
									/ (float) sendCount * 100;
							
						}
						checkSuccessPercent = df.format(checkSuccessPercentTmp) + "%";
						backPercent = df.format(backPercentTmp) + "%";
						proveMatchPercent = df.format(proveMatchPercentTmp) + "%";
						proveNotMatchPercent = df.format(proveNotMatchPercentTmp)
								+ "%";
						
						result = new AnalyseResult(idCenter,docDate, idBank, bankName,
								sendCount, retreatCount, backCount,
								proveMatchCount, proveNotMatchCount, wjkCount,
								checkSuccessCount, checkFailCount,ebillSuccessCount,
								checkSuccessPercent, backPercent,
								proveMatchPercent, proveNotMatchPercent);
						resultList.add(result);
					}
				}
			}
			if(selectCount!=null && selectCount.equals("countIdCenter")){
				if (list != null && list.size() > 0) {
					String idCenter = "";
					String docDate = ""; // 对账日期
					String idBank = ""; // 机构号
					String bankName = ""; // 机构名称
					long sendCount = 0; // 对账单发出数，按账单
					long retreatCount = 0; // 退信数
					long backCount = 0; // 回收数
					long proveMatchCount = 0; // 验印相符数
					long proveNotMatchCount = 0; // 验印不符数
					long wjkCount = 0; // 未建库数
					long checkSuccessCount = 0; // 余额相符数
					long checkFailCount = 0; // 余额不相符数
					long ebillSuccessCount = 0; // 对账成功数
					
					String checkSuccessPercent = "";// 对账成功率
					String backPercent = ""; // 回收率
					String proveMatchPercent = ""; // 验印成功率
					String proveNotMatchPercent = ""; // 验印不符率
					
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
									docDate = obj[j].toString();
								}
								break;
							case 2:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									sendCount = Long.parseLong(obj[j].toString());
								}
								break;
							case 3:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									backCount = Long
											.parseLong(obj[j].toString());
								}
								break;
							case 4:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									retreatCount = Long.parseLong(obj[j].toString());
								}
								break;
							case 5:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									proveMatchCount = Long.parseLong(obj[j]
											.toString());
								}
								break;
							case 6:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									proveNotMatchCount = Long.parseLong(obj[j]
											.toString());
								}
								break;
							case 7:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									wjkCount = Long.parseLong(obj[j].toString());
								}
								break;
							case 8:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									checkSuccessCount = Long.parseLong(obj[j]
											.toString());
								}
								break;
							case 9:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									checkFailCount = Long.parseLong(obj[j]
											.toString());
								}
								break;
							case 10:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									ebillSuccessCount = Long.parseLong(obj[j]
											.toString());
								}
								break;
							}
						}
						idBank = idCenter;
						bankName = idBranchNameMap.get(idCenter);
						float checkSuccessPercentTmp = 0;
						float backPercentTmp = 0;
						float proveMatchPercentTmp = 0;
						float proveNotMatchPercentTmp = 0;
						
						if (sendCount == 0) {
						} else {
							checkSuccessPercentTmp = (float) ebillSuccessCount
									/ (float) sendCount * 100;
							backPercentTmp = (float) backCount / (float) sendCount
									* 100;
							proveMatchPercentTmp = (float) proveMatchCount
									/ (float) sendCount * 100;
							proveNotMatchPercentTmp = (float) proveNotMatchCount
									/ (float) sendCount * 100;
							
						}
						checkSuccessPercent = df.format(checkSuccessPercentTmp) + "%";
						backPercent = df.format(backPercentTmp) + "%";
						proveMatchPercent = df.format(proveMatchPercentTmp) + "%";
						proveNotMatchPercent = df.format(proveNotMatchPercentTmp)
								+ "%";
						
						result = new AnalyseResult(idCenter,docDate, idBank, bankName,
								sendCount, retreatCount, backCount,
								proveMatchCount, proveNotMatchCount, wjkCount,
								checkSuccessCount, checkFailCount,ebillSuccessCount,
								checkSuccessPercent, backPercent,
								proveMatchPercent, proveNotMatchPercent);
						resultList.add(result);
					}
				}
			}
			if(selectCount!=null && selectCount.equals("countIdBranch")){
				if (list != null && list.size() > 0) {
					String idCenter = FinalConstant.ROOTORG;
					String docDate = ""; // 对账日期
					String idBank = FinalConstant.ROOTORG; // 机构号
					String bankName = "华融湘江总行"; // 机构名称
					long sendCount = 0; // 对账单发出数，按账单
					long retreatCount = 0; // 退信数
					long backCount = 0; // 回收数
					long proveMatchCount = 0; // 验印相符数
					long proveNotMatchCount = 0; // 验印不符数
					long wjkCount = 0; // 未建库数
					long checkSuccessCount = 0; // 余额相符数
					long checkFailCount = 0; // 余额不相符数
					long ebillSuccessCount = 0; // 对账成功数
					
					String checkSuccessPercent = "";// 对账成功率
					String backPercent = ""; // 回收率
					String proveMatchPercent = ""; // 验印成功率
					String proveNotMatchPercent = ""; // 验印不符率
					
					for (int i = 0; i < list.size(); i++) {
						Object[] obj = (Object[]) list.get(i);
						for (int j = 0; j < obj.length; j++) {
							switch (j) {
							case 0:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									docDate = obj[j].toString();
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
									backCount = Long
											.parseLong(obj[j].toString());
								}
								break;
							case 3:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									retreatCount = Long.parseLong(obj[j].toString());
								}
								break;
							case 4:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									proveMatchCount = Long.parseLong(obj[j]
											.toString());
								}
								break;
							case 5:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									proveNotMatchCount = Long.parseLong(obj[j]
											.toString());
								}
								break;
							case 6:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									wjkCount = Long.parseLong(obj[j].toString());
								}
								break;
							case 7:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									checkSuccessCount = Long.parseLong(obj[j]
											.toString());
								}
								break;
							case 8:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									checkFailCount = Long.parseLong(obj[j]
											.toString());
								}
								break;
							case 9:
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
						float proveMatchPercentTmp = 0;
						float proveNotMatchPercentTmp = 0;
						
						if (sendCount == 0) {
						} else {
							checkSuccessPercentTmp = (float) ebillSuccessCount
									/ (float) sendCount * 100;
							backPercentTmp = (float) backCount / (float) sendCount
									* 100;
							proveMatchPercentTmp = (float) proveMatchCount
									/ (float) sendCount * 100;
							proveNotMatchPercentTmp = (float) proveNotMatchCount
									/ (float) sendCount * 100;
							
						}
						checkSuccessPercent = df.format(checkSuccessPercentTmp) + "%";
						backPercent = df.format(backPercentTmp) + "%";
						proveMatchPercent = df.format(proveMatchPercentTmp) + "%";
						proveNotMatchPercent = df.format(proveNotMatchPercentTmp)
								+ "%";
						
						result = new AnalyseResult(idCenter,docDate, idBank, bankName,
								sendCount, retreatCount, backCount,
								proveMatchCount, proveNotMatchCount, wjkCount,
								checkSuccessCount, checkFailCount,ebillSuccessCount,
								checkSuccessPercent, backPercent,
								proveMatchPercent, proveNotMatchPercent);
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
