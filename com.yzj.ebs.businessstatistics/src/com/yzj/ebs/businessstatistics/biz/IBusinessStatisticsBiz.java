package com.yzj.ebs.businessstatistics.biz;

import java.util.List;
import java.util.Map;

import com.yzj.ebs.businessstatistics.param.BusinessStatisticsParam;
import com.yzj.ebs.businessstatistics.param.BusinessStatisticsResultParam;

/**
 * 创建于:2013-09-29<br>
 * 版权所有(C) 2013 深圳市银之杰科技股份有限公司<br>
 * 业务量 统计业务实现
 * 
 * @author dengwu
 * @version 1.0.0
 */
public interface IBusinessStatisticsBiz {
	/**
	 * 对账中心业务量统计
	 * @param queryMap
	 * @param rushStatisticsParam
	 * @return
	 */
	List<BusinessStatisticsResultParam> getBusinessStatisticsResult(Map<String , String> queryMap,
			BusinessStatisticsParam rushStatisticsParam,String docDate,boolean isPaged,String selectCount);

	/**
	 * 获取全量 对账中心业务量数据
	 * @param queryMap
	 * @param rushStatisticsParam
	 * @return
	 */
	List<BusinessStatisticsResultParam> getAllBusinessStatisticsResult(Map<String , String> queryMap,String docDate);

}
