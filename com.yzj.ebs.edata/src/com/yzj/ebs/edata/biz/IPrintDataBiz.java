/**
 * IPrintDataBiz.java
 * 版权所有(C) 2013 深圳市银之杰科技股份有限公司
 * 创建:秦靖锋  2013-03-29
 */
package com.yzj.ebs.edata.biz;

import java.util.List;
import java.util.Map;

import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.database.CheckMainData;
import com.yzj.ebs.edata.bean.AccnoMainDataQueryParam;
import com.yzj.ebs.edata.bean.BatchPrintBean;


/**
 * 创建于:2013-04-10<br>
 * 版权所有(C) 2013 深圳市银之杰科技股份有限公司<br>
 * 账单打印业务逻辑处理
 * 
 * @author 秦靖锋
 * @version 1.0
 */
public interface IPrintDataBiz  {

	/***
	 * 查询CheckMainData信息
	 * @param accnoMainDataQueryParam 账户信息对象
	 * @throws XDocProcException
	 */
	public List<CheckMainData> queryCheckMainDataInfo(
			AccnoMainDataQueryParam accnoMainDataQueryParam)
			throws XDocProcException;
	
	/**
	 * 判断跨机构登记
	 * 
	 * @param AccnoMainDataQueryParam
	 * @return boolean
	 * @throws XDocProcException
	 */
	public boolean judgeOrg(AccnoMainDataQueryParam accnoMainDataQueryParam) throws XDocProcException;
			
	/**
	 * 封装打印对账单数据
	 * @param List<CheckMainData>
	 * @return String
	 * @throws Exception
	 */
	public String batchPrintData(List<CheckMainData> queryBillList) throws Exception;
	
	/**
	 * 封装打印对账单数据
	 * @param CheckMainData
	 * @return String
	 * @throws Exception
	 */
	public List<BatchPrintBean>  printData(CheckMainData data,AccnoMainDataQueryParam param,String printType) throws Exception;
	
	public List<Map<String,String>> printDetailsData(CheckMainData data,AccnoMainDataQueryParam param,int firstNum,int pageNum) throws Exception;
	
	/**
	 * 根据账号和日期到checkmaindata表查询数据
	 * @param AccnoMainDataQueryParam 
	 * @return List<CheckMainData>
	 * @throws XDocProcException
	 */
	public List<CheckMainData> queryCheckMainDataInfoByAccnoAndDocdate(
			AccnoMainDataQueryParam accnoMainDataQueryParam)
			throws XDocProcException;
	
	/**
	 * 得到所有对账单的 对账单编号
	 */
	public String getAllVoucherNo(AccnoMainDataQueryParam Param)throws XDocProcException;
	
	/**
	 * 得到对公明细总数
	 * @param mapData
	 * @param month
	 * @return
	 */
	public List<Object[]> getDeatilCount(Map<String,String> mapData,String month);
	
}
