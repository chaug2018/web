package com.yzj.ebs.seal.analyse.biz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yzj.ebs.common.ICheckMainDataAdm;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.seal.analyse.pojo.AnalyseResult;
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
public class SealAnalyseBiz implements ISealAnalyseBiz{
	
	private List<AnalyseResult> resultList = new ArrayList<AnalyseResult>(); // 统计结果类集合，为了前台显示方便
	private List<AnalyseResult> exportList = new ArrayList<AnalyseResult>(); // 统计结果类集合，为了前台显示方便
	private AnalyseResult result = null;
	private ICheckMainDataAdm checkMainDataAdm;
	private static WFLogger logger = WFLogger.getLogger(SealAnalyseBiz.class);
	private static final java.text.DecimalFormat df = new java.text.DecimalFormat(
			"#.##");
	String errMsg = null;
    /**
     * 验印情况统计业务实现
     */
	@Override
	public List<AnalyseResult> getProveAnalyseList(
			Map<String, String> queryMap, PageParam pageParam, boolean isPaged,String selectCount)
			throws XDocProcException {
		// TODO Auto-generated method stub
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
						}
						break;
					}
				}
				idBranchNameMap.put(idBranch, idBranchName);
			}
			
			//查询数据库
			List<?> list = null;
			if(isPaged){
				if(selectCount!=null && selectCount.equals("countIdBank")){
					list = checkMainDataAdm.getProveAnalyseList(queryMap,
							pageParam,true);
				}else{
					list = checkMainDataAdm.getProveAnalyseListCount(queryMap, pageParam, 
							true, selectCount);
				}
			}else{
				if(selectCount!=null && selectCount.equals("countIdBank")){
					list = checkMainDataAdm.getProveAnalyseList(queryMap,
							pageParam,false);
				}else{
					list = checkMainDataAdm.getProveAnalyseListCount(queryMap, pageParam, 
							false, selectCount);
				}
			}
			
			if(selectCount!=null && selectCount.equals("countIdBank")){
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						String idCenter = "";
						String idBranch = "";
						String idBank = "";
						String bankName = "";
						String docDate = "";
						long sendCount = 0; // 按账单
						long proveMatchCount = 0;
						long proveNotMatchCount = 0;
						long notProve = 0;
						String provePercent = "";
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
									docDate = obj[j].toString();
								}
								break;
							case 5:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									sendCount = Long.parseLong(obj[j].toString());
								}
								break;
							case 6:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									proveMatchCount = Long.parseLong(obj[j]
											.toString());
								}
								break;
							case 7:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									proveNotMatchCount = Long.parseLong(obj[j]
											.toString());
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
						if (sendCount == 0) {
						} else {
							provePercentTmp = (float) proveMatchCount
									/ (float) sendCount * 100;
						}
						provePercent = df.format(provePercentTmp) + "%";
						result = new AnalyseResult(idCenter, idBranch, idBank,
								bankName, docDate, sendCount, proveMatchCount,
								proveNotMatchCount, provePercent,notProve);
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
						String docDate = "";
						long sendCount = 0; // 按账单
						long proveMatchCount = 0;
						long proveNotMatchCount = 0;
						long notProve = 0;
						String provePercent = "";
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
									proveMatchCount = Long.parseLong(obj[j]
											.toString());
								}
								break;
							case 4:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									proveNotMatchCount = Long.parseLong(obj[j]
											.toString());
								}
								break;
							case 5:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									notProve = Long.parseLong(obj[j]
											.toString());
								}
								break;
							}
						}
						idBank = idCenter;
						bankName = idBranchNameMap.get(idCenter);
						float provePercentTmp = 0;
						if (sendCount == 0) {
						} else {
							provePercentTmp = (float) proveMatchCount
									/ (float) sendCount * 100;
						}
						provePercent = df.format(provePercentTmp) + "%";
						result = new AnalyseResult(idCenter, idBranch, idBank,
								bankName, docDate, sendCount, proveMatchCount,
								proveNotMatchCount, provePercent,notProve);
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
						String docDate = "";
						long sendCount = 0; // 按账单
						long proveMatchCount = 0;
						long proveNotMatchCount = 0;
						long notProve = 0;
						String provePercent = "";
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
									proveMatchCount = Long.parseLong(obj[j]
											.toString());
								}
								break;
							case 3:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									proveNotMatchCount = Long.parseLong(obj[j]
											.toString());
								}
								break;
							case 4:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									notProve = Long.parseLong(obj[j]
											.toString());
								}
								break;
							}
						}
						float provePercentTmp = 0;
						if (sendCount == 0) {
						} else {
							provePercentTmp = (float) proveMatchCount
									/ (float) sendCount * 100;
						}
						provePercent = df.format(provePercentTmp) + "%";
						result = new AnalyseResult(idCenter, idBranch, idBank,
								bankName, docDate, sendCount, proveMatchCount,
								proveNotMatchCount, provePercent,notProve);
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
	public List<AnalyseResult> getResultList() {
		return resultList;
	}
	public void setResultList(List<AnalyseResult> resultList) {
		this.resultList = resultList;
	}
	public List<AnalyseResult> getExportList() {
		return exportList;
	}
	public void setExportList(List<AnalyseResult> exportList) {
		this.exportList = exportList;
	}
	public AnalyseResult getResult() {
		return result;
	}
	public void setResult(AnalyseResult result) {
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
		SealAnalyseBiz.logger = logger;
	}
}
