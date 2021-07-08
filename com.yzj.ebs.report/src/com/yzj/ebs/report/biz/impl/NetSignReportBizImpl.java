package com.yzj.ebs.report.biz.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yzj.ebs.common.IBasicInfoAdm;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.report.biz.INetSignReportBiz;
import com.yzj.ebs.report.pojo.NetSignResult;
import com.yzj.ebs.util.FinalConstant;
import com.yzj.wf.common.WFLogger;

/**
 * 创建于:2013-04-01<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 网银对账签约率统计  业务实现
 * 
 * @author 单伟龙
 * @version 1.0.0
 */
public class NetSignReportBizImpl implements INetSignReportBiz{
	
	private static final java.text.DecimalFormat df = new java.text.DecimalFormat("#.##");
	
	private NetSignResult result; // 统计结果类
	private List<NetSignResult> resultList = new ArrayList<NetSignResult>(); // 统计结果类集合，为了前台显示方便
	
	private IBasicInfoAdm basicinfoAdm; 
	
	private static WFLogger logger = WFLogger.getLogger(NetSignReportBizImpl.class);

	public List<NetSignResult> getNetSignReportList(
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
			
			List<?> list = basicinfoAdm.getNetSignReportList(queryMap,
					queryParam,isPaged,selectCount);
			
			if(selectCount!=null && selectCount.equals("countIdBank")){
				if (list != null && list.size() > 0) {
					String idCenter = "";
					String idBank = ""; // 机构号
					String bankName = ""; // 机构名称
					
					long accCount = 0;		//总账户数
					long netCount = 0;		//网银开户数
					long netSignCount = 0;	//网银对账签约数=自助签约数+柜面签约数
					long autoSignCount = 0;	//自助签约数
					long counterSignCount = 0;	//柜面签约数
					String netSignPercent = "";	//网银签约率
					
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
									accCount = Long.parseLong(obj[j].toString());
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
									netSignCount = Long.parseLong(obj[j].toString());
								}
								break;
							case 6:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									autoSignCount = Long.parseLong(obj[j].toString());
								}
								break;
							case 7:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									counterSignCount = Long.parseLong(obj[j].toString());
								}
								break;
							
							}
						}
						float netSignPercentTmp = 0;
						
						if (netCount == 0) {
						} else {
							netSignPercentTmp = (float) netSignCount / (float) netCount * 100;
						}
						netSignPercent = df.format(netSignPercentTmp) + "%";
						
						result = new NetSignResult(idCenter, idBank, bankName, 
								accCount, netCount, netSignCount, autoSignCount, 
								counterSignCount, netSignPercent);
						
						resultList.add(result);
					}
				}
			}
			if(selectCount!=null && selectCount.equals("countIdCenter")){
				if (list != null && list.size() > 0) {
					String idCenter = "";
					String idBank = ""; // 机构号
					String bankName = ""; // 机构名称
					
					long accCount = 0;		//总账户数
					long netCount = 0;		//网银开户数
					long netSignCount = 0;	//网银对账签约数=自助签约数+柜面签约数
					long autoSignCount = 0;	//自助签约数
					long counterSignCount = 0;	//柜面签约数
					String netSignPercent = "";	//网银签约率
					
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
									accCount = Long.parseLong(obj[j].toString());
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
									netSignCount = Long.parseLong(obj[j].toString());
								}
								break;
							case 4:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									autoSignCount = Long.parseLong(obj[j].toString());
								}
								break;
							case 5:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									counterSignCount = Long.parseLong(obj[j].toString());
								}
								break;
							}
						}
						idBank = idCenter;
						bankName = idBranchNameMap.get(idCenter);

						float netSignPercentTmp = 0;
						
						if (netCount == 0) {
						} else {
							netSignPercentTmp = (float) netSignCount / (float) netCount * 100;
						}
						netSignPercent = df.format(netSignPercentTmp) + "%";
						
						result = new NetSignResult(idCenter, idBank, bankName, 
								accCount, netCount, netSignCount, autoSignCount, 
								counterSignCount, netSignPercent);
						
						resultList.add(result);
					}
				}
			}
			if(selectCount!=null && selectCount.equals("countIdBranch")){
				if (list != null && list.size() > 0) {
					String idCenter = FinalConstant.ROOTORG;
					String idBank = FinalConstant.ROOTORG; // 机构号
					String bankName = "华融湘江总行"; // 机构名称

					long accCount = 0;		//总账户数
					long netCount = 0;		//网银开户数
					long netSignCount = 0;	//网银对账签约数=自助签约数+柜面签约数
					long autoSignCount = 0;	//自助签约数
					long counterSignCount = 0;	//柜面签约数
					String netSignPercent = "";	//网银签约率
					
					for (int i = 0; i < list.size(); i++) {
						Object[] obj = (Object[]) list.get(i);
						for (int j = 0; j < obj.length; j++) {
							switch (j) {
							case 0:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									accCount = Long.parseLong(obj[j].toString());
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
									netSignCount = Long.parseLong(obj[j].toString());
								}
								break;
							case 3:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									autoSignCount = Long.parseLong(obj[j].toString());
								}
								break;
							case 4:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									counterSignCount = Long.parseLong(obj[j].toString());
								}
								break;
								
							}
						}
						float netSignPercentTmp = 0;
						
						if (netCount == 0) {
						} else {
							netSignPercentTmp = (float) netSignCount / (float) netCount * 100;
						}
						netSignPercent = df.format(netSignPercentTmp) + "%";
						
						result = new NetSignResult(idCenter, idBank, bankName, 
								accCount, netCount, netSignCount, autoSignCount, 
								counterSignCount, netSignPercent);
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

	public NetSignResult getResult() {
		return result;
	}

	public void setResult(NetSignResult result) {
		this.result = result;
	}

	public List<NetSignResult> getResultList() {
		return resultList;
	}

	public void setResultList(List<NetSignResult> resultList) {
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
