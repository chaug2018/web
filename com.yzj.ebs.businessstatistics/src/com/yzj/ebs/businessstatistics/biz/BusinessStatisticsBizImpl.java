package com.yzj.ebs.businessstatistics.biz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yzj.ebs.businessstatistics.param.BusinessStatisticsParam;
import com.yzj.ebs.businessstatistics.param.BusinessStatisticsResultParam;
import com.yzj.ebs.common.ICheckMainDataAdm;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.util.FinalConstant;

/**
 * 创建于:2013-09-29<br>
 * 版权所有(C) 2013 深圳市银之杰科技股份有限公司<br>
 * 对账中心业务量 业务实现
 * 
 * @author dengwu
 * @version 1.0.0
 */
public class BusinessStatisticsBizImpl implements IBusinessStatisticsBiz {

	private static final java.text.DecimalFormat df = new java.text.DecimalFormat(
			"#.##");
	private List<BusinessStatisticsResultParam> resultList = new ArrayList<BusinessStatisticsResultParam>();
	private List<BusinessStatisticsResultParam> exportList = new ArrayList<BusinessStatisticsResultParam>();
	private ICheckMainDataAdm checkMainDataAdm;
	private BusinessStatisticsResultParam result = null;

	String errMsg = null;

	/**
	 * 获取分页数据
	 */
	@Override
	public List<BusinessStatisticsResultParam> getBusinessStatisticsResult(
			Map<String, String> queryMap,BusinessStatisticsParam businessStatisticsParam, 
			String docDate,boolean isPaged,String selectCount) {
		// TODO Auto-generated method stub
		resultList.clear();
		exportList.clear();
		result = null;
		List<?> list = null;
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
			
			if(selectCount!=null && selectCount.equals("countIdBank")){
				list = checkMainDataAdm.getBusinessStatisticsResult(
						queryMap, businessStatisticsParam, docDate,isPaged);
			}else{
				list = checkMainDataAdm.getBusinessStatisticsResultCount(queryMap, 
						businessStatisticsParam, docDate, isPaged, selectCount);
			}
			
			if(selectCount!=null && selectCount.equals("countIdBank")){
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						String idCenter = "";
						String idBranch = "";
						String idBank = "";
						String bankName = "";
						long counteraccount = 0; // 柜台对账账户数
						long postaccount = 0; // 邮寄对账账户数
						long idcenteracount = 0; // 对账中心账户数
						long shouldacount = 0; // 应对账账户数
						long netAcount = 0;  //网银对账账户数
						long faceAcount = 0;  //面对面对账账户数
						long otherAcount = 0; //其他对账账户数
						String counterPercent =""; //柜台占应对账账户数比例
						String postPercent = ""; //邮寄占应对账账户数比例
						String netPercent = ""; //网银占应对账账户数比例
						String facePercent = ""; //面对面占应对账账户数比例
						String otherPercent = ""; //发送方式为空占应对账账户数比例
						String sPercent = ""; //应对账账户数占对账中心账户数比例
						
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
										counteraccount = Long.parseLong(obj[j]
												.toString());
									}
									break;
								case 5:
									if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
										postaccount = Long.parseLong(obj[j].toString());
									}
									break;
								case 6:
									if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
										netAcount = Long.parseLong(obj[j]
												.toString());
									}
									break;
								case 7:
									if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
										faceAcount = Long.parseLong(obj[j]
												.toString());
									}
									break;
								case 8:
									if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
										otherAcount = Long.parseLong(obj[j]
												.toString());
									}
									break;
								case 9:
									if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
										idcenteracount = Long.parseLong(obj[j]
												.toString());
									}
									break;
								case 10:
									if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
										shouldacount = Long
												.parseLong(obj[j].toString());
									}
									break;
							}
						}
						float counterPercentTmp = 0;
						float postPercentTmp = 0;
						float netPercentTmp = 0;
						float facePercentTmp = 0;
						float otherPercentTmp = 0;
						float sPercentTmp = 0;
						if (shouldacount != 0) {
							counterPercentTmp = (float) counteraccount
									/ (float) shouldacount * 100;
						}
						if (shouldacount != 0) {
							postPercentTmp = (float) postaccount
									/ (float) shouldacount * 100;
						}
						if (shouldacount != 0) {
							netPercentTmp = (float) netAcount
									/ (float) shouldacount * 100;
						}
						if (shouldacount != 0) {
							facePercentTmp = (float) faceAcount
									/ (float) shouldacount * 100;
						}
						if (shouldacount != 0) {
							otherPercentTmp = (float) otherAcount
									/ (float) shouldacount * 100;
						}
						if (shouldacount != 0) {
							sPercentTmp = (float) shouldacount
									/ (float) idcenteracount * 100;
						}
						counterPercent = df.format(counterPercentTmp) + "%";
						postPercent = df.format(postPercentTmp) + "%";
						netPercent = df.format(netPercentTmp) + "%";
						facePercent = df.format(facePercentTmp) + "%";
						otherPercent = df.format(otherPercentTmp) + "%";
						sPercent = df.format(sPercentTmp) + "%";
						
						result = new BusinessStatisticsResultParam(counteraccount,
								postaccount, idcenteracount, shouldacount,
								idCenter, idBranch, idBank, bankName,netAcount,
								faceAcount,otherAcount,counterPercent,postPercent,
								netPercent,facePercent,otherPercent,sPercent);
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
						long counteraccount = 0; // 柜台对账账户数
						long postaccount = 0; // 邮寄对账账户数
						long idcenteracount = 0; // 对账中心账户数
						long shouldacount = 0; // 应对账账户数
						long netAcount = 0;  //网银对账账户数
						long faceAcount = 0;  //面对面对账账户数
						long otherAcount = 0; //其他对账账户数
						String counterPercent =""; //柜台占应对账账户数比例
						String postPercent = ""; //邮寄占应对账账户数比例
						String netPercent = ""; //网银占应对账账户数比例
						String facePercent = ""; //面对面占应对账账户数比例
						String otherPercent = ""; //发送方式为空占应对账账户数比例
						String sPercent = ""; //应对账账户数占对账中心账户数比例
						
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
										counteraccount = Long.parseLong(obj[j]
												.toString());
									}
									break;
								case 2:
									if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
										postaccount = Long.parseLong(obj[j].toString());
									}
									break;
								case 3:
									if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
										netAcount = Long.parseLong(obj[j]
												.toString());
									}
									break;
								case 4:
									if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
										faceAcount = Long.parseLong(obj[j]
												.toString());
									}
									break;
								case 5:
									if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
										otherAcount = Long.parseLong(obj[j]
												.toString());
									}
									break;
								case 6:
									if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
										idcenteracount = Long.parseLong(obj[j]
												.toString());
									}
									break;
								case 7:
									if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
										shouldacount = Long
												.parseLong(obj[j].toString());
									}
									break;
							}
						}
						idBank = idCenter;
						bankName = idBranchNameMap.get(idCenter);
						float counterPercentTmp = 0;
						float postPercentTmp = 0;
						float netPercentTmp = 0;
						float facePercentTmp = 0;
						float otherPercentTmp = 0;
						float sPercentTmp = 0;
						if (shouldacount != 0) {
							counterPercentTmp = (float) counteraccount
									/ (float) shouldacount * 100;
						}
						if (shouldacount != 0) {
							postPercentTmp = (float) postaccount
									/ (float) shouldacount * 100;
						}
						if (shouldacount != 0) {
							netPercentTmp = (float) netAcount
									/ (float) shouldacount * 100;
						}
						if (shouldacount != 0) {
							facePercentTmp = (float) faceAcount
									/ (float) shouldacount * 100;
						}
						if (shouldacount != 0) {
							otherPercentTmp = (float) otherAcount
									/ (float) shouldacount * 100;
						}
						if (shouldacount != 0) {
							sPercentTmp = (float) shouldacount
									/ (float) idcenteracount * 100;
						}
						counterPercent = df.format(counterPercentTmp) + "%";
						postPercent = df.format(postPercentTmp) + "%";
						netPercent = df.format(netPercentTmp) + "%";
						facePercent = df.format(facePercentTmp) + "%";
						otherPercent = df.format(otherPercentTmp) + "%";
						sPercent = df.format(sPercentTmp) + "%";
						
						result = new BusinessStatisticsResultParam(counteraccount,
								postaccount, idcenteracount, shouldacount,
								idCenter, idBranch, idBank, bankName,netAcount,
								faceAcount,otherAcount,counterPercent,postPercent,
								netPercent,facePercent,otherPercent,sPercent);
						resultList.add(result);
						exportList.add(result);
					}
				}
			}
			
			if(selectCount!=null && selectCount.equals("countIdBranch")){
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						String idCenter = FinalConstant.ROOTORG;
						String idBranch = FinalConstant.ROOTORG;
						String idBank = FinalConstant.ROOTORG;
						String bankName = "华融湘江总行";
						long counteraccount = 0; // 柜台对账账户数
						long postaccount = 0; // 邮寄对账账户数
						long idcenteracount = 0; // 对账中心账户数
						long shouldacount = 0; // 应对账账户数
						long netAcount = 0;  //网银对账账户数
						long faceAcount = 0;  //面对面对账账户数
						long otherAcount = 0; //其他对账账户数
						String counterPercent =""; //柜台占应对账账户数比例
						String postPercent = ""; //邮寄占应对账账户数比例
						String netPercent = ""; //网银占应对账账户数比例
						String facePercent = ""; //面对面占应对账账户数比例
						String otherPercent = ""; //发送方式为空占应对账账户数比例
						String sPercent = ""; //应对账账户数占对账中心账户数比例
						
						Object[] obj = (Object[]) list.get(i);
						for (int j = 0; j < obj.length; j++) {
							switch (j) {
								case 0:
									if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
										counteraccount = Long.parseLong(obj[j]
												.toString());
									}
									break;
								case 1:
									if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
										postaccount = Long.parseLong(obj[j].toString());
									}
									break;
								case 2:
									if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
										netAcount = Long.parseLong(obj[j]
												.toString());
									}
									break;
								case 3:
									if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
										faceAcount = Long.parseLong(obj[j]
												.toString());
									}
									break;
								case 4:
									if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
										otherAcount = Long.parseLong(obj[j]
												.toString());
									}
									break;
								case 5:
									if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
										idcenteracount = Long.parseLong(obj[j]
												.toString());
									}
									break;
								case 6:
									if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
										shouldacount = Long
												.parseLong(obj[j].toString());
									}
									break;
							}
						}
						float counterPercentTmp = 0;
						float postPercentTmp = 0;
						float netPercentTmp = 0;
						float facePercentTmp = 0;
						float otherPercentTmp = 0;
						float sPercentTmp = 0;
						if (shouldacount != 0) {
							counterPercentTmp = (float) counteraccount
									/ (float) shouldacount * 100;
						}
						if (shouldacount != 0) {
							postPercentTmp = (float) postaccount
									/ (float) shouldacount * 100;
						}
						if (shouldacount != 0) {
							netPercentTmp = (float) netAcount
									/ (float) shouldacount * 100;
						}
						if (shouldacount != 0) {
							facePercentTmp = (float) faceAcount
									/ (float) shouldacount * 100;
						}
						if (shouldacount != 0) {
							otherPercentTmp = (float) otherAcount
									/ (float) shouldacount * 100;
						}
						if (shouldacount != 0) {
							sPercentTmp = (float) shouldacount
									/ (float) idcenteracount * 100;
						}
						counterPercent = df.format(counterPercentTmp) + "%";
						postPercent = df.format(postPercentTmp) + "%";
						netPercent = df.format(netPercentTmp) + "%";
						facePercent = df.format(facePercentTmp) + "%";
						otherPercent = df.format(otherPercentTmp) + "%";
						sPercent = df.format(sPercentTmp) + "%";
						
						result = new BusinessStatisticsResultParam(counteraccount,
								postaccount, idcenteracount, shouldacount,
								idCenter, idBranch, idBank, bankName,netAcount,
								faceAcount,otherAcount,counterPercent,postPercent,
								netPercent,facePercent,otherPercent,sPercent);
						resultList.add(result);
						exportList.add(result);
					}
				}
			}
		} catch (XDocProcException e) {
			errMsg = "获取催收统计结果失败" + e.getMessage();
		}
		if(isPaged){
			return resultList;
		}else{
			return exportList;
		}
	}

	/**
	 * 获取全量数据
	 */
	@Override
	public List<BusinessStatisticsResultParam> getAllBusinessStatisticsResult(
			Map<String, String> queryMap, String docDate) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<BusinessStatisticsResultParam> getResultList() {
		return resultList;
	}

	public void setResultList(List<BusinessStatisticsResultParam> resultList) {
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

	public BusinessStatisticsResultParam getResult() {
		return result;
	}

	public void setResult(BusinessStatisticsResultParam result) {
		this.result = result;
	}

	public List<BusinessStatisticsResultParam> getExportList() {
		return exportList;
	}

	public void setExportList(List<BusinessStatisticsResultParam> exportList) {
		this.exportList = exportList;
	}

}
