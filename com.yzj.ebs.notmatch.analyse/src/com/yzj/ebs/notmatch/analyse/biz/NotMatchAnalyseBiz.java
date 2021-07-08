package com.yzj.ebs.notmatch.analyse.biz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yzj.ebs.common.INotMatchTableAdm;
import com.yzj.ebs.common.IPublicTools;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.common.ICheckMainDataAdm;
import com.yzj.ebs.notmatch.analyse.pojo.AnalyseResult;
import com.yzj.ebs.util.FinalConstant;

/**
 * 创建于:2013-04-01<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 未达账情况统计 业务实现
 * 
 * @author 单伟龙
 * @version 1.0.0
 */
public class NotMatchAnalyseBiz implements INotMatchAnalyseBiz {

	private static final java.text.DecimalFormat df = new java.text.DecimalFormat(
			"#.##");
	private List<AnalyseResult> resultList = new ArrayList<AnalyseResult>();// 查询结果集
	private List<AnalyseResult> exportList = new ArrayList<AnalyseResult>();// 导出数据的结果集
	private AnalyseResult result;
	private INotMatchTableAdm notMatchTableAdm;
	public IPublicTools tools;
	private ICheckMainDataAdm checkMainDataAdm;

	@Override
	public List<AnalyseResult> getAnalyseResult(Map<String, String> queryMap,
			PageParam param, boolean isPaged,String selectCount) throws XDocProcException {
		// TODO Auto-generated method stub
		resultList.clear();
		exportList.clear();
		result = null;
		List<?> list = null;
		try {
//			XPeopleInfo people = tools.getCurrLoginPeople();
//			if (people == null) {
//				throw new XDocProcException("会话超时，请重新登录！");
//			}
	
			List<?> idBranchNameList = null;
			Map<String,String> idBranchNameMap = new HashMap<String,String>();
			//获得所有清算中心的名字
			idBranchNameList = notMatchTableAdm.getAllIdBranchName();
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

			// 查询数据库，解析得到结果集
			if(selectCount!=null && selectCount.equals("countIdBank")){
				list = notMatchTableAdm.getAnalyseresults(queryMap, param, isPaged);
			}else{
				list = notMatchTableAdm.getAnalyseresultsCount(queryMap, param, 
						isPaged, selectCount);
			}
			//按网点遍历List
			if(selectCount!=null && selectCount.equals("countIdBank")){
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						String idCenter = "";
						String idBank = "";
						String bankName = "";
						String docDate = "";
						
						long notMatchCount = 0;
						long checkMatchCount = 0;
						long checkNotMatchCount = 0;
						long sendCount = 0;
						String notMatchPercent = "";
						String tunePercent = "";
						
						Object[] obj = (Object[]) list.get(i);
						for (int j = 0; j < obj.length; j++) {
							switch (j) {
							case 0:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									idCenter = obj[j].toString();
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
									notMatchCount = Long.parseLong(obj[j]
											.toString());
								}
								break;
							case 6:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									checkMatchCount = Long.parseLong(obj[j]
											.toString());
								}
								break;
							case 7:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									checkNotMatchCount = Long.parseLong(obj[j]
											.toString());
								}
								break;
							case 8:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									sendCount = Long.parseLong(obj[j].toString());
								}
								break;
							}
						}
						// 未达率
						float notMatchPercentTmp = 0;
						if (sendCount == 0) {
						} else {
							notMatchPercentTmp = (float) notMatchCount
									/ (float) sendCount * 100;
						}
						notMatchPercent = df.format(notMatchPercentTmp) + "%";
						
						// 调平率
						float tunePercentTmp = 0;
						if (notMatchCount != 0) {
							tunePercentTmp = (float) checkMatchCount
									/ (float) notMatchCount * 100;
						}
						tunePercent = df.format(tunePercentTmp) + "%";
						
						result = new AnalyseResult(idCenter,  idBank,
								bankName, docDate, sendCount, notMatchCount,
								checkMatchCount, checkNotMatchCount,
								notMatchPercent, tunePercent);
						exportList.add(result);
						resultList.add(result);
					}
				}
			}
			//按对账中心遍历LIst
			if(selectCount!=null && selectCount.equals("countIdCenter")){
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						String idCenter = "";
						String idBank = "";
						String bankName = "";
						String docDate = "";
						
						long notMatchCount = 0;
						long checkMatchCount = 0;
						long checkNotMatchCount = 0;
						long sendCount = 0;
						String notMatchPercent = "";
						String tunePercent = "";
						
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
									notMatchCount = Long.parseLong(obj[j]
											.toString());
								}
								break;
							case 3:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									checkMatchCount = Long.parseLong(obj[j]
											.toString());
								}
								break;
							case 4:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									checkNotMatchCount = Long.parseLong(obj[j]
											.toString());
								}
								break;
							case 5:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									sendCount = Long.parseLong(obj[j].toString());
								}
								break;
							}
						}
						idBank = idCenter;
						bankName = idBranchNameMap.get(idCenter);
						// 未达率
						float notMatchPercentTmp = 0;
						if (sendCount == 0) {
						} else {
							notMatchPercentTmp = (float) notMatchCount
									/ (float) sendCount * 100;
						}
						notMatchPercent = df.format(notMatchPercentTmp) + "%";
						
						// 调平率
						float tunePercentTmp = 0;
						if (notMatchCount != 0) {
							tunePercentTmp = (float) checkMatchCount
									/ (float) notMatchCount * 100;
						}
						tunePercent = df.format(tunePercentTmp) + "%";
						
						result = new AnalyseResult(idCenter,  idBank,
								bankName, docDate, sendCount, notMatchCount,
								checkMatchCount, checkNotMatchCount,
								notMatchPercent, tunePercent);
						exportList.add(result);
						resultList.add(result);
					}
				}
			}
			//按总行来遍历LIst
			if(selectCount!=null && selectCount.equals("countIdBranch")){
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						String idCenter = FinalConstant.ROOTORG;
						String idBank = FinalConstant.ROOTORG;
						String bankName = "华融湘江总行";
						String docDate = "";
						
						long notMatchCount = 0;
						long checkMatchCount = 0;
						long checkNotMatchCount = 0;
						long sendCount = 0;
						String notMatchPercent = "";
						String tunePercent = "";
						
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
									notMatchCount = Long.parseLong(obj[j]
											.toString());
								}
								break;
							case 2:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									checkMatchCount = Long.parseLong(obj[j]
											.toString());
								}
								break;
							case 3:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									checkNotMatchCount = Long.parseLong(obj[j]
											.toString());
								}
								break;
							case 4:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									sendCount = Long.parseLong(obj[j].toString());
								}
								break;
							}
						}
						// 未达率
						float notMatchPercentTmp = 0;
						if (sendCount == 0) {
						} else {
							notMatchPercentTmp = (float) notMatchCount
									/ (float) sendCount * 100;
						}
						notMatchPercent = df.format(notMatchPercentTmp) + "%";
						
						// 调平率
						float tunePercentTmp = 0;
						if (notMatchCount != 0) {
							tunePercentTmp = (float) checkMatchCount
									/ (float) notMatchCount * 100;
						}
						tunePercent = df.format(tunePercentTmp) + "%";
						
						result = new AnalyseResult(idCenter,  idBank,
								bankName, docDate, sendCount, notMatchCount,
								checkMatchCount, checkNotMatchCount,
								notMatchPercent, tunePercent);
						exportList.add(result);
						resultList.add(result);
					}
				}
			}
			if (isPaged) {
				return resultList;
			} else {
				return exportList;
			}
		} catch (XDocProcException e) {
			// TODO Auto-generated catch block
			throw new XDocProcException(e.getMessage());
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

	public INotMatchTableAdm getNotMatchTableAdm() {
		return notMatchTableAdm;
	}

	public void setNotMatchTableAdm(INotMatchTableAdm notMatchTableAdm) {
		this.notMatchTableAdm = notMatchTableAdm;
	}

	public IPublicTools getTools() {
		return tools;
	}

	public void setTools(IPublicTools tools) {
		this.tools = tools;
	}

	public static java.text.DecimalFormat getDf() {
		return df;
	}

	public ICheckMainDataAdm getCheckMainDataAdm() {
		return checkMainDataAdm;
	}

	public void setCheckMainDataAdm(ICheckMainDataAdm checkMainDataAdm) {
		this.checkMainDataAdm = checkMainDataAdm;
	}
	
}
