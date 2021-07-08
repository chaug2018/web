package com.yzj.ebs.report.biz.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yzj.ebs.common.IBasicInfoAdm;
import com.yzj.ebs.common.ICheckMainDataAdm;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.report.biz.IFocusReportBiz;
import com.yzj.ebs.report.pojo.FocusResult;
import com.yzj.ebs.util.FinalConstant;
import com.yzj.wf.common.WFLogger;

/**
 * 创建于:2013-04-01<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 对账集中情况统计  业务实现
 * 
 * @author 单伟龙
 * @version 1.0.0
 */
public class FocusReportBizImpl implements IFocusReportBiz{
	
	private static final java.text.DecimalFormat df = new java.text.DecimalFormat("#.##");
	
	private FocusResult result; // 统计结果类
	private List<FocusResult> resultList = new ArrayList<FocusResult>(); // 统计结果类集合，为了前台显示方便
	
	private IBasicInfoAdm basicinfoAdm; 
	
	private static WFLogger logger = WFLogger.getLogger(FocusReportBizImpl.class);

	public List<FocusResult> getFocusReportList(
			Map<String, String> queryMap, PageParam queryParam, 
			 boolean isPaged,String selectCount) {
		result = null;
		resultList.clear();
		try {
    	   	List<?> idBranchNameList = null;
			Map<String,String> idBranchNameMap = new HashMap<String,String>();
			//获得所有清算中心的名字
			idBranchNameList = basicinfoAdm.getAllIdBranchName();
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
			
			List<?> list = basicinfoAdm.getFocusReportList(queryMap,
					queryParam,isPaged,selectCount);
			
			if(selectCount!=null && selectCount.equals("countIdBank")){
				if (list != null && list.size() > 0) {
					String idCenter = "";
					String idBank = ""; // 机构号
					String bankName = ""; // 机构名称
					
					long mailCount = 0;		//邮寄对账账户数
					long netCount = 0;		//网银对账账户数
					long faceCount = 0;	//面对面对账账户数
					long counterCount = 0;	//柜台对账账户数
					long otherCount = 0;	//其它对账账户数
					long checkCount = 0;	//需对账账户数
					String focusPercent = "";	//集中度
					
					
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
									mailCount = Long.parseLong(obj[j].toString());
								}
								break;
							case 4:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									netCount = Long.parseLong(obj[j].toString());
								}
								break;
							
							case 5:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									faceCount = Long.parseLong(obj[j].toString());
								}
								break;
							case 6:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									counterCount = Long.parseLong(obj[j].toString());
								}
								break;
							case 7:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									otherCount = Long.parseLong(obj[j].toString());
								}
								break;
							case 8:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									checkCount = Long.parseLong(obj[j].toString());
								}
								break;
							}
						}
						float focusPercentTmp = 0;
						
						if (checkCount == 0) {
						} else {
							focusPercentTmp = (float) (mailCount+netCount+faceCount) / (float) checkCount * 100;
						}
						focusPercent = df.format(focusPercentTmp) + "%";
						
						result = new FocusResult(idCenter, idBank, bankName, 
								mailCount, netCount, faceCount, counterCount, 
								otherCount, checkCount, focusPercent);
						
						resultList.add(result);
					}
				}
			}
			if(selectCount!=null && selectCount.equals("countIdCenter")){
				if (list != null && list.size() > 0) {
					String idCenter = "";
					String idBank = ""; // 机构号
					String bankName = ""; // 机构名称
					
					long mailCount = 0;		//邮寄对账账户数
					long netCount = 0;		//网银对账账户数
					long faceCount = 0;	//面对面对账账户数
					long counterCount = 0;	//柜台对账账户数
					long otherCount = 0;	//其它对账账户数
					long checkCount = 0;	//需对账账户数
					String focusPercent = "";	//集中度
					
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
									mailCount = Long.parseLong(obj[j].toString());
								}
								break;
							case 2:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									netCount = Long.parseLong(obj[j].toString());
								}
								break;
							
							case 3:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									faceCount = Long.parseLong(obj[j].toString());
								}
								break;
							case 4:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									counterCount = Long.parseLong(obj[j].toString());
								}
								break;
							case 5:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									otherCount = Long.parseLong(obj[j].toString());
								}
								break;
							case 6:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									checkCount = Long.parseLong(obj[j].toString());
								}
								break;
							}
						}
						idBank = idCenter;
						bankName = idBranchNameMap.get(idCenter);
						
						float focusPercentTmp = 0;
						
						if (checkCount == 0) {
						} else {
							focusPercentTmp = (float) (mailCount+netCount+faceCount) / (float) checkCount * 100;
						}
						focusPercent = df.format(focusPercentTmp) + "%";
						
						result = new FocusResult(idCenter, idBank, bankName, 
								mailCount, netCount, faceCount, counterCount, 
								otherCount, checkCount, focusPercent);
						
						resultList.add(result);
					}
				}
			}
			if(selectCount!=null && selectCount.equals("countIdBranch")){
				if (list != null && list.size() > 0) {
					String idCenter = FinalConstant.ROOTORG;
					String idBank = FinalConstant.ROOTORG; // 机构号
					String bankName = "华融湘江总行"; // 机构名称

					long mailCount = 0;		//邮寄对账账户数
					long netCount = 0;		//网银对账账户数
					long faceCount = 0;	//面对面对账账户数
					long counterCount = 0;	//柜台对账账户数
					long otherCount = 0;	//其它对账账户数
					long checkCount = 0;	//需对账账户数
					String focusPercent = "";	//集中度
					
					for (int i = 0; i < list.size(); i++) {
						Object[] obj = (Object[]) list.get(i);
						for (int j = 0; j < obj.length; j++) {
							switch (j) {
							case 0:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									mailCount = Long.parseLong(obj[j].toString());
								}
								break;
							case 1:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									netCount = Long.parseLong(obj[j].toString());
								}
								break;
							
							case 2:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									faceCount = Long.parseLong(obj[j].toString());
								}
								break;
							case 3:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									counterCount = Long.parseLong(obj[j].toString());
								}
								break;
							case 4:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									otherCount = Long.parseLong(obj[j].toString());
								}
								break;
							case 5:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									checkCount = Long.parseLong(obj[j].toString());
								}
								break;
								
							}
						}
						float focusPercentTmp = 0;
						
						if (checkCount == 0) {
						} else {
							focusPercentTmp = (float) (mailCount+netCount+faceCount) / (float) checkCount * 100;
						}
						focusPercent = df.format(focusPercentTmp) + "%";
						
						result = new FocusResult(idCenter, idBank, bankName, 
								mailCount, netCount, faceCount, counterCount, 
								otherCount, checkCount, focusPercent);
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

	public FocusResult getResult() {
		return result;
	}

	public void setResult(FocusResult result) {
		this.result = result;
	}

	public List<FocusResult> getResultList() {
		return resultList;
	}

	public void setResultList(List<FocusResult> resultList) {
		this.resultList = resultList;
	}

	public IBasicInfoAdm getBasicinfoAdm() {
		return basicinfoAdm;
	}

	public void setBasicinfoAdm(IBasicInfoAdm basicinfoAdm) {
		this.basicinfoAdm = basicinfoAdm;
	}

	public static java.text.DecimalFormat getDf() {
		return df;
	}

}
