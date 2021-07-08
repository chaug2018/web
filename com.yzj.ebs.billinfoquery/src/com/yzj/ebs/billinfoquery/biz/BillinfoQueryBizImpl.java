package com.yzj.ebs.billinfoquery.biz;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.yzj.ebs.common.ICheckMainDataAdm;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.database.AccNoMainData;
import com.yzj.ebs.database.CheckMainData;

/**
 * 创建于:2013-04-01<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 账单信息查询  业务实现
 * 
 * @author 单伟龙
 * @version 1.0.0
 */
public class BillinfoQueryBizImpl implements IBillinfoQueryBiz {
	
	public ICheckMainDataAdm checkMainDataAdm;
	
	private List<CheckMainData> queryBillList ;
	private List<AccNoMainData> resultList ;
	/**
	 * 得到分页查询数据
	 */
	@Override
	public List<?> queryData(Map<String, String> queryMap,
			PageParam billinfoQueryParam, String accNo, String queryType) throws XDocProcException {
		resultList = new ArrayList<AccNoMainData>();
		queryBillList = new ArrayList<CheckMainData>();
		// 按帳號查詢的時候
		if (queryType.equals("2")) {
			resultList = checkMainDataAdm.getCheckAccMainData(queryMap,
							billinfoQueryParam, accNo);
			return resultList;
		}else {//按账单查询
			if(queryMap.containsKey("checkResult"))
				queryMap.remove("checkResult");
			if(queryMap.containsKey("faceFlag"))
				queryMap.remove("faceFlag");
			
			queryBillList = checkMainDataAdm.getCheckMainData(queryMap,
							 billinfoQueryParam);
			return queryBillList;
			}
	}
	/**
	 * 得到全量数据
	 */
	@Override
	public List<?> queryAllData(Map<String, String> queryMap, String accNo,
			String queryType) throws XDocProcException {
		resultList = new ArrayList<AccNoMainData>();
		queryBillList = new ArrayList<CheckMainData>();
		// 按帳號查詢的時候
		if (queryType.equals("2")) {
			resultList = checkMainDataAdm.getAllAccCheckMainData(queryMap, accNo);
			return resultList;
		}else {//按账单查询
			if(queryMap.containsKey("checkResult"))
				queryMap.remove("checkResult");
			if(queryMap.containsKey("faceFlag"))
				queryMap.remove("faceFlag");
			
			queryBillList =  checkMainDataAdm.getAllCheckMainData(queryMap);
			return queryBillList;
		}
	}
	@Override
	public CheckMainData queryOneByVoucherNo(String voucherNo) throws XDocProcException {
	
		return checkMainDataAdm.queryOneByVoucherNo(voucherNo);
	}
	public ICheckMainDataAdm getCheckMainDataAdm() {
		return checkMainDataAdm;
	}
	public void setCheckMainDataAdm(ICheckMainDataAdm checkMainDataAdm) {
		this.checkMainDataAdm = checkMainDataAdm;
	}
	public List<CheckMainData> getQueryBillList() {
		return queryBillList;
	}
	public void setQueryBillList(List<CheckMainData> queryBillList) {
		this.queryBillList = queryBillList;
	}
	public List<AccNoMainData> getResultList() {
		return resultList;
	}
	public void setResultList(List<AccNoMainData> resultList) {
		this.resultList = resultList;
	}

}
