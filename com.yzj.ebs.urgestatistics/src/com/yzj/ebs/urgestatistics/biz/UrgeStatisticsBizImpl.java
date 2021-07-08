package com.yzj.ebs.urgestatistics.biz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yzj.ebs.common.ICheckMainDataAdm;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.urgestatistics.param.UrgeStatisticsQueryParam;
import com.yzj.ebs.urgestatistics.param.UrgeStatisticsResultParam;
import com.yzj.ebs.util.FinalConstant;
import com.yzj.wf.common.WFLogger;

/**
 * 创建于:2013-04-07<br>
 * 版权所有(C) 2013 深圳市银之杰科技股份有限公司<br>
 * 退信情况统计  业务实现
 * 
 * @author 单伟龙
 * @version 1.0.0
 */
public class UrgeStatisticsBizImpl implements IUrgeStatisticsBiz{
	
	private static final java.text.DecimalFormat df = new java.text.DecimalFormat(
			"#.##");
	UrgeStatisticsQueryParam urgeStatisticsQueryParam;
	UrgeStatisticsResultParam result ;

	List<UrgeStatisticsResultParam> resultList = new ArrayList<UrgeStatisticsResultParam>();
	List<UrgeStatisticsResultParam> exportList = new ArrayList<UrgeStatisticsResultParam>();
	
	private ICheckMainDataAdm checkMainDataAdm;
	private static WFLogger logger = WFLogger
			.getLogger(UrgeStatisticsBizImpl.class);

	private String errMsg = "";

	/**
	 * 获取退信分页数据
	 */
	@Override
	public List<UrgeStatisticsResultParam> getUrgeStatisticsResult(
			Map<String, String> queryMap,
			UrgeStatisticsQueryParam urgeStatisticsQueryParam,boolean isPaged,String selectCount) {
		// TODO Auto-generated method stub
		resultList.clear();
		exportList.clear();
		result =null;
		List<?> list = null;
		try{
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
				list = checkMainDataAdm.getUrgeStatisticsResult(
						queryMap, urgeStatisticsQueryParam,isPaged);
			}else{
				list = checkMainDataAdm.getUrgeStatisticsResultCount(queryMap, 
						urgeStatisticsQueryParam, isPaged, selectCount);
			}
			
			if(selectCount!=null && selectCount.equals("countIdBank")){
				if(list!=null&&list.size()>0){
					for(int i=0;i<list.size();i++){
						String idCenter = "";
						String idBranch = "";
						String idBank = "";
						String bankName = "";
						String docDate="";
						
						long totalVouAmount=0; // 账单总数
						long totalUrgeAmount=0; // 退信总数
						long rejectedAmount=0;// 单位拒收
						long addrChangedAmount=0;// 原址拆迁
						long addrUnknownAmount=0;// 地址不详
						long noRecieverAmount=0;// 投递无人
						long unitNotExistAmount=0;// 无此单位
						long addrNotExistAmount=0;// 无此地址
						long noConnectionAmount=0;// 无法联系
						long otherAmount=0;// 其他
						
						String urgeRate="";
						
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
									totalVouAmount = Long.parseLong(obj[j]
											.toString());
								}
								break;
							case 6:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									totalUrgeAmount = Long.parseLong(obj[j]
											.toString());
								}
								break;
							case 7:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									rejectedAmount = Long.parseLong(obj[j]
											.toString());
								}
								break;
							case 8:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									addrChangedAmount = Long.parseLong(obj[j]
											.toString());
								}
								break;
							case 9:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									addrUnknownAmount = Long.parseLong(obj[j]
											.toString());
								}
								break;
							case 10:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									noRecieverAmount = Long.parseLong(obj[j]
											.toString());
								}
								break;
							case 11:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									unitNotExistAmount = Long.parseLong(obj[j]
											.toString());
								}
								break;
							case 12:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									addrNotExistAmount = Long.parseLong(obj[j]
											.toString());
								}
								break;
							case 13:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									noConnectionAmount = Long.parseLong(obj[j]
											.toString());
								}
								break;
							case 14:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									otherAmount = Long.parseLong(obj[j]
											.toString());
								}
								break;	
							}
						}
						float urgeRateTmp = 0;
						if(totalVouAmount==0){
						}else{
							urgeRateTmp = (float) totalUrgeAmount
									/ (float) totalVouAmount * 100;
						}
						urgeRate = df.format(urgeRateTmp) + "%";
						
						result=new UrgeStatisticsResultParam( idCenter,idBranch,idBank,
								bankName,  docDate, totalVouAmount,
								totalUrgeAmount,  rejectedAmount, addrChangedAmount, addrUnknownAmount,
								noRecieverAmount, unitNotExistAmount, addrNotExistAmount, noConnectionAmount,
								otherAmount,urgeRate);
						
						resultList.add(result);
						exportList.add(result);
					}
				}
			}
			
			if(selectCount!=null && selectCount.equals("countIdCenter")){
				if(list!=null&&list.size()>0){
					for(int i=0;i<list.size();i++){
						String idCenter = "";
						String idBranch = "";
						String idBank = "";
						String bankName = "";
						String docDate="";
						
						long totalVouAmount=0; // 账单总数
						long totalUrgeAmount=0; // 退信总数
						long rejectedAmount=0;// 单位拒收
						long addrChangedAmount=0;// 原址拆迁
						long addrUnknownAmount=0;// 地址不详
						long noRecieverAmount=0;// 投递无人
						long unitNotExistAmount=0;// 无此单位
						long addrNotExistAmount=0;// 无此地址
						long noConnectionAmount=0;// 无法联系
						long otherAmount=0;// 其他
						
						String urgeRate="";
						
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
									totalVouAmount = Long.parseLong(obj[j]
											.toString());
								}
								break;
							case 3:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									totalUrgeAmount = Long.parseLong(obj[j]
											.toString());
								}
								break;
							case 4:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									rejectedAmount = Long.parseLong(obj[j]
											.toString());
								}
								break;
							case 5:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									addrChangedAmount = Long.parseLong(obj[j]
											.toString());
								}
								break;
							case 6:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									addrUnknownAmount = Long.parseLong(obj[j]
											.toString());
								}
								break;
							case 7:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									noRecieverAmount = Long.parseLong(obj[j]
											.toString());
								}
								break;
							case 8:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									unitNotExistAmount = Long.parseLong(obj[j]
											.toString());
								}
								break;
							case 9:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									addrNotExistAmount = Long.parseLong(obj[j]
											.toString());
								}
								break;
							case 10:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									noConnectionAmount = Long.parseLong(obj[j]
											.toString());
								}
								break;
							case 11:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									otherAmount = Long.parseLong(obj[j]
											.toString());
								}
								break;	
							}
						}
						idBank = idCenter;
						bankName = idBranchNameMap.get(idCenter);
						float urgeRateTmp = 0;
						if(totalVouAmount==0){
						}else{
							urgeRateTmp = (float) totalUrgeAmount
									/ (float) totalVouAmount * 100;
						}
						urgeRate = df.format(urgeRateTmp) + "%";
						
						result=new UrgeStatisticsResultParam( idCenter,idBranch,idBank,
								bankName,  docDate, totalVouAmount,
								totalUrgeAmount,  rejectedAmount, addrChangedAmount, addrUnknownAmount,
								noRecieverAmount, unitNotExistAmount, addrNotExistAmount, noConnectionAmount,
								otherAmount,urgeRate);
						
						resultList.add(result);
						exportList.add(result);
					}
				}
			}
			
			if(selectCount!=null && selectCount.equals("countIdBranch")){
				if(list!=null&&list.size()>0){
					for(int i=0;i<list.size();i++){
						String idCenter = FinalConstant.ROOTORG;
						String idBranch = FinalConstant.ROOTORG;
						String idBank = FinalConstant.ROOTORG;
						String bankName = "华融湘江总行";
						String docDate="";
						
						long totalVouAmount=0; // 账单总数
						long totalUrgeAmount=0; // 退信总数
						long rejectedAmount=0;// 单位拒收
						long addrChangedAmount=0;// 原址拆迁
						long addrUnknownAmount=0;// 地址不详
						long noRecieverAmount=0;// 投递无人
						long unitNotExistAmount=0;// 无此单位
						long addrNotExistAmount=0;// 无此地址
						long noConnectionAmount=0;// 无法联系
						long otherAmount=0;// 其他
						
						String urgeRate="";
						
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
									totalVouAmount = Long.parseLong(obj[j]
											.toString());
								}
								break;
							case 2:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									totalUrgeAmount = Long.parseLong(obj[j]
											.toString());
								}
								break;
							case 3:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									rejectedAmount = Long.parseLong(obj[j]
											.toString());
								}
								break;
							case 4:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									addrChangedAmount = Long.parseLong(obj[j]
											.toString());
								}
								break;
							case 5:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									addrUnknownAmount = Long.parseLong(obj[j]
											.toString());
								}
								break;
							case 6:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									noRecieverAmount = Long.parseLong(obj[j]
											.toString());
								}
								break;
							case 7:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									unitNotExistAmount = Long.parseLong(obj[j]
											.toString());
								}
								break;
							case 8:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									addrNotExistAmount = Long.parseLong(obj[j]
											.toString());
								}
								break;
							case 9:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									noConnectionAmount = Long.parseLong(obj[j]
											.toString());
								}
								break;
							case 10:
								if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
									otherAmount = Long.parseLong(obj[j]
											.toString());
								}
								break;	
							}
						}
						float urgeRateTmp = 0;
						if(totalVouAmount==0){
						}else{
							urgeRateTmp = (float) totalUrgeAmount
									/ (float) totalVouAmount * 100;
						}
						urgeRate = df.format(urgeRateTmp) + "%";
						
						result=new UrgeStatisticsResultParam( idCenter,idBranch,idBank,
								bankName,  docDate, totalVouAmount,
								totalUrgeAmount,  rejectedAmount, addrChangedAmount, addrUnknownAmount,
								noRecieverAmount, unitNotExistAmount, addrNotExistAmount, noConnectionAmount,
								otherAmount,urgeRate);
						
						resultList.add(result);
						exportList.add(result);
					}
				}
			}
		}catch (XDocProcException e) {
			errMsg = "查询数据库出现错误!";
			logger.error("未达账统计查询数据库错误", e);		
		}
		if(isPaged){
			return resultList;
		}else{
			return exportList;
		}
	}

	/**
	 * 获取退信全量数据
	 */
	@Override
	public List<UrgeStatisticsResultParam> getAllUrgeStatisticsResult(
			Map<String, String> queryMap) {
		// TODO Auto-generated method stub
			return null;	
	}

	public UrgeStatisticsResultParam getResult() {
		return result;
	}

	public void setResult(UrgeStatisticsResultParam result) {
		this.result = result;
	}

	public List<UrgeStatisticsResultParam> getResultList() {
		return resultList;
	}

	public void setResultList(List<UrgeStatisticsResultParam> resultList) {
		this.resultList = resultList;
	}

	public ICheckMainDataAdm getCheckMainDataAdm() {
		return checkMainDataAdm;
	}

	public void setCheckMainDataAdm(ICheckMainDataAdm checkMainDataAdm) {
		this.checkMainDataAdm = checkMainDataAdm;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public List<UrgeStatisticsResultParam> getExportList() {
		return exportList;
	}

	public void setExportList(List<UrgeStatisticsResultParam> exportList) {
		this.exportList = exportList;
	}
	
}
