package com.yzj.ebs.rushstatistics.biz;

import java.util.List;
import java.util.Map;

import com.yzj.ebs.rushstatistics.param.RushStatisticsParam;
import com.yzj.ebs.rushstatistics.param.RushStatisticsResultParam;

/**
 * 创建于:2013-04-07<br>
 * 版权所有(C) 2013 深圳市银之杰科技股份有限公司<br>
 * 催收情况统计  业务实现
 * 
 * @author 单伟龙
 * @version 1.0.0
 */
public interface IRushStatisticsBiz {
	/**
	 * 催收情况统计
	 * @param queryMap
	 * @param rushStatisticsParam
	 * @return
	 * isPaged=true为分页查询,isPaged-false为全量查询
	 */
	List<RushStatisticsResultParam> getRushStatisticsResult(Map<String , String> queryMap,RushStatisticsParam rushStatisticsParam,
			boolean isPaged,String selecetCount);

	/**
	 * 获取全量 催收统计数据
	 * @param queryMap
	 * @param rushStatisticsParam
	 * @return
	 */
	List<RushStatisticsResultParam> getAllRushStatisticsResult(Map<String , String> queryMap);

}
