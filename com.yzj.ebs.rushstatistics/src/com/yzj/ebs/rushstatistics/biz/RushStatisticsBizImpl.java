package com.yzj.ebs.rushstatistics.biz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yzj.ebs.common.ICheckMainDataAdm;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.rushstatistics.param.RushStatisticsParam;
import com.yzj.ebs.rushstatistics.param.RushStatisticsResultParam;
import com.yzj.ebs.util.FinalConstant;

/**
 * 创建于:2013-04-07<br>
 * 版权所有(C) 2013 深圳市银之杰科技股份有限公司<br>
 * 催收情况统计  业务实现
 * 
 * @author 单伟龙
 * @version 1.0.0
 */
public class RushStatisticsBizImpl implements IRushStatisticsBiz{
	
	private static final java.text.DecimalFormat df = new java.text.DecimalFormat(
			"#.##");
	private List<RushStatisticsResultParam> resultList = new ArrayList<RushStatisticsResultParam>();
	private List<RushStatisticsResultParam> exportList = new ArrayList<RushStatisticsResultParam>();
	private ICheckMainDataAdm checkMainDataAdm;
	private RushStatisticsResultParam result = null;

	String errMsg=null;
	/**
	 * 获取分页数据
	 */
	@Override
	public List<RushStatisticsResultParam> getRushStatisticsResult(Map<String, String> queryMap,
			RushStatisticsParam rushStatisticsParam,boolean isPaged,String selectCount) {
		// TODO Auto-generated method stub
		resultList.clear();
		exportList.clear();
		List<?> list=null;
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
			
			//进行数据库查询
			if(selectCount!=null && selectCount.equals("countIdBank")){
				list = checkMainDataAdm.getRushStatisticsResult(queryMap,
						rushStatisticsParam, isPaged);
			}else{
				list = checkMainDataAdm.getRushStatisticsResultCount(queryMap, 
						rushStatisticsParam, isPaged, selectCount);
			}
			
			//按网点遍历List
			if(selectCount!=null && selectCount.equals("countIdBank")){
				if(list!=null&& list.size()>0){
					for(int i=0;i<list.size();i++){
						String idCenter = "";
						String idBranch = "";
						String idBank = "";
						String bankName = "";
						String docDate="";
						
						long totalAmount=0; // 对账单总数
						long rushedAmount=0; // 已催收账单数
						long successAmount=0; // 催收成功数
						long telAmount=0; // 0.电话催收数
						long emailAmount=0; // 1.邮件催收数
						long faceAmount=0; // 2.面对面催收数
						
						String successRate=""; // 催收成功率
						
						Object[] obj = (Object[]) list.get(i);
						for (int j = 0; j < obj.length; j++){
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
									totalAmount = Long.parseLong(obj[j]
											.toString());
								}
								break;
							case 6:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									rushedAmount = Long.parseLong(obj[j]
											.toString());
								}
								break;
							case 7:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									telAmount = Long.parseLong(obj[j]
											.toString());
								}
								break;
							case 8:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									emailAmount = Long.parseLong(obj[j]
											.toString());
								}
								break;
							case 9:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									faceAmount = Long.parseLong(obj[j]
											.toString());
								}
								break;
							case 10:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									successAmount = Long.parseLong(obj[j]
											.toString());
								}
								break;
							}
						}
						float successRateTmp = 0;
						if(rushedAmount==0){
						}else{
							successRateTmp = (float) successAmount
									/ (float) rushedAmount * 100;
						}
						successRate = df.format(successRateTmp) + "%";
						
						result=new RushStatisticsResultParam( idCenter, idBranch, idBank , bankName, docDate,
								totalAmount, rushedAmount, successAmount, telAmount, emailAmount,
								faceAmount, successRate);
						resultList.add(result);
						exportList.add(result);
					}
				}
			}
			
			//按分行遍历List
			if(selectCount!=null && selectCount.equals("countIdCenter")){
				if(list!=null&& list.size()>0){
					for(int i=0;i<list.size();i++){
						String idCenter = "";
						String idBranch = "";
						String idBank = "";
						String bankName = "";
						String docDate="";
						
						long totalAmount=0; // 对账单总数
						long rushedAmount=0; // 已催收账单数
						long successAmount=0; // 催收成功数
						long telAmount=0; // 0.电话催收数
						long emailAmount=0; // 1.邮件催收数
						long faceAmount=0; // 2.面对面催收数
						
						String successRate=""; // 催收成功率
						
						Object[] obj = (Object[]) list.get(i);
						for (int j = 0; j < obj.length; j++){
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
									totalAmount = Long.parseLong(obj[j]
											.toString());
								}
								break;
							case 3:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									rushedAmount = Long.parseLong(obj[j]
											.toString());
								}
								break;
							case 4:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									telAmount = Long.parseLong(obj[j]
											.toString());
								}
								break;
							case 5:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									emailAmount = Long.parseLong(obj[j]
											.toString());
								}
								break;
							case 6:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									faceAmount = Long.parseLong(obj[j]
											.toString());
								}
								break;
							case 7:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									successAmount = Long.parseLong(obj[j]
											.toString());
								}
								break;
							}
						}
						idBank = idCenter;
						bankName = idBranchNameMap.get(idCenter);
						float successRateTmp = 0;
						if(rushedAmount==0){
						}else{
							successRateTmp = (float) successAmount
									/ (float) rushedAmount * 100;
						}
						successRate = df.format(successRateTmp) + "%";
						
						result=new RushStatisticsResultParam( idCenter, idBranch, idBank , bankName, docDate,
								totalAmount, rushedAmount, successAmount, telAmount, emailAmount,
								faceAmount, successRate);
						resultList.add(result);
						exportList.add(result);
					}
				}
			}
			
			//按总行遍历List
			if(selectCount!=null && selectCount.equals("countIdBranch")){
				if(list!=null&& list.size()>0){
					for(int i=0;i<list.size();i++){
						String idCenter = FinalConstant.ROOTORG;
						String idBranch =  FinalConstant.ROOTORG;
						String idBank =  FinalConstant.ROOTORG;
						String bankName = "华融湘江总行";
						String docDate="";
						
						long totalAmount=0; // 对账单总数
						long rushedAmount=0; // 已催收账单数
						long successAmount=0; // 催收成功数
						long telAmount=0; // 0.电话催收数
						long emailAmount=0; // 1.邮件催收数
						long faceAmount=0; // 2.面对面催收数
						
						String successRate=""; // 催收成功率
						
						Object[] obj = (Object[]) list.get(i);
						for (int j = 0; j < obj.length; j++){
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
									totalAmount = Long.parseLong(obj[j]
											.toString());
								}
								break;
							case 2:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									rushedAmount = Long.parseLong(obj[j]
											.toString());
								}
								break;
							case 3:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									telAmount = Long.parseLong(obj[j]
											.toString());
								}
								break;
							case 4:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									emailAmount = Long.parseLong(obj[j]
											.toString());
								}
								break;
							case 5:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									faceAmount = Long.parseLong(obj[j]
											.toString());
								}
								break;
							case 6:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									successAmount = Long.parseLong(obj[j]
											.toString());
								}
								break;
							}
						}
						float successRateTmp = 0;
						if(rushedAmount==0){
						}else{
							successRateTmp = (float) successAmount
									/ (float) rushedAmount * 100;
						}
						successRate = df.format(successRateTmp) + "%";
						
						result=new RushStatisticsResultParam( idCenter, idBranch, idBank , bankName, docDate,
								totalAmount, rushedAmount, successAmount, telAmount, emailAmount,
								faceAmount, successRate);
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
	public List<RushStatisticsResultParam> getAllRushStatisticsResult(Map<String, String> queryMap) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<RushStatisticsResultParam> getResultList() {
		return resultList;
	}

	public void setResultList(List<RushStatisticsResultParam> resultList) {
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

	public RushStatisticsResultParam getResult() {
		return result;
	}

	public void setResult(RushStatisticsResultParam result) {
		this.result = result;
	}
	
	
	

}
