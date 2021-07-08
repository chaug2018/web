package com.yzj.ebs.ebillmatch.biz;

import java.util.List;
import java.util.Map;

import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.ebillmatch.param.ResultParam;

/**
 * 创建于:2013-04-8<br>
 * 版权所有(C) 2013 深圳市银之杰科技股份有限公司<br>
 * 半年机构对账有效率  统计 业务实现
 * @author swl
 * @version 1.0.0
 */
public interface IEbillMatchAnalyseBiz {

	/**
	 * 获取半年机构对账有效率  统计数据并解析
	 * @param queryMap
	 * @param queryParam
	 * @param docDate
	 * @param isPaged
	 * @return
	 */
	List<ResultParam> getEbillMatchAnalyseResult(Map<String,String> queryMap,
			PageParam queryParam, String docDate,boolean isPaged,String selectCount);
}
