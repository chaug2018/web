package com.yzj.ebs.urgestatistics.biz;

import java.util.List;
import java.util.Map;

import com.yzj.ebs.urgestatistics.param.UrgeStatisticsQueryParam;
import com.yzj.ebs.urgestatistics.param.UrgeStatisticsResultParam;

/**
 * 创建于:2013-04-07<br>
 * 版权所有(C) 2013 深圳市银之杰科技股份有限公司<br>
 * 退信情况统计  业务实现
 * 
 * @author 单伟龙
 * @version 1.0.0
 */
public interface IUrgeStatisticsBiz {
	/**
	 * 
	 * @param queryMap
	 * @param urgeStatisticsQueryParam
	 * @return
	 */
	List<UrgeStatisticsResultParam> getUrgeStatisticsResult(Map<String,String> queryMap, 
			UrgeStatisticsQueryParam urgeStatisticsQueryParam,boolean isPaged,String selectCount);
	
	/**
	 * 
	 * @param queryMap
	 * @param urgeStatisticsQueryParam
	 * @return
	 */
	List<UrgeStatisticsResultParam> getAllUrgeStatisticsResult(Map<String,String> queryMap);

	
}
