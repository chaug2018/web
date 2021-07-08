package com.yzj.ebs.partebill.biz;

import java.util.List;
import java.util.Map;

import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.partebill.param.AnalyseResult;


/**
 * 创建于:2013-07-22<br>
 * 版权所有(C) 2013 深圳市银之杰科技股份有限公司<br>
 * 增量对账情况  统计 业务实现
 * @author jw
 * @version 1.0.0
 */
public interface IPartEbillAnalyseBiz {

	/**
	 * 获取增量对账情况  统计数据并解析
	 * @param queryMap
	 * @param queryParam
	 * @param docDate
	 * @param isPaged
	 * @return
	 */
	List<AnalyseResult> getPartEbillAnalyseResult(Map<String,String> queryMap,
			PageParam queryParam, String docDateStart, String docDateEnd,boolean isPaged);
}
