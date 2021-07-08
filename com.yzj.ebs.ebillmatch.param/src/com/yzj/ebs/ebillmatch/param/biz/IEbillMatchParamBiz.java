package com.yzj.ebs.ebillmatch.param.biz;

import java.util.List;
import java.util.Map;

import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.ebillmatch.param.param.MatchResultParam;

/**
 * 创建于:2013-04-8<br>
 * 版权所有(C) 2013 深圳市银之杰科技股份有限公司<br>
 * 半年机构对账有效明细 统计 业务实现
 * @author swl
 * @version 1.0.0
 */
public interface IEbillMatchParamBiz {

	/**
	 * 获取半年机构对账有效明细  分页数据
	 * @param queryMap
	 * @param queryParam
	 * @param docDate
	 * @return
	 */
	List<MatchResultParam> getEbillMatchParam(Map<String,String> queryMap,
			PageParam queryParam, String docDate,boolean isPaged);
	
}
