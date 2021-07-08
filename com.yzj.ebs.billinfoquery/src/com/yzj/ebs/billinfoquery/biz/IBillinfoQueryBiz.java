package com.yzj.ebs.billinfoquery.biz;

import java.util.List;
import java.util.Map;

import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.database.CheckMainData;

/**
 * 创建于:2013-04-01<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 账单信息查询  业务实现
 * 
 * @author 单伟龙
 * @version 1.0.0
 */
public interface IBillinfoQueryBiz {

	/**
	 * 查询账单数据
	 * @param queryMap 条件map
	 * @param billinfoQueryParam 分页信息
	 * @param accNo     账号
	 * @param queryType 查询类型
	 * @return list<?> 泛型的数据
	 * @throws XDocProcException 
	 */
   List<?> queryData(Map<String,String> queryMap,
		PageParam billinfoQueryParam, String accNo,String queryType) throws XDocProcException;
   /**
    * 查询全量数据
    * @param queryMap
    * @param accNo
    * @param queryType
    * @return list<?>
    * @throws XDocProcException
    */
   List<?> queryAllData(Map<String,String> queryMap,
			 String accNo,String queryType) throws XDocProcException;
   /**
    * 根据账单编号查询详细记录
    * @param voucherNo
    * @return
 * @throws XDocProcException 
    */
   CheckMainData queryOneByVoucherNo(String voucherNo) throws XDocProcException;

}
