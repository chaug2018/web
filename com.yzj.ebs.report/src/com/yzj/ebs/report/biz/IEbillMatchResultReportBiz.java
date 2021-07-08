package com.yzj.ebs.report.biz;

import java.util.List;
import java.util.Map;

import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.report.pojo.EbillMatchResultResult;

/**
 * 创建于:2013-04-8<br>
 * 版权所有(C) 2013 深圳市银之杰科技股份有限公司<br>
 * 账户有效对账结果展示
 * @author swl
 * @version 1.0.0
 */
public interface IEbillMatchResultReportBiz {

	/**
	 * 获取半年机构对账有效明细  分页数据
	 * @param queryMap
	 * @param queryParam
	 * @param docDate
	 * @return
	 */
	List<EbillMatchResultResult> getEbillMatchResult(Map<String,String> queryMap,
			PageParam queryParam, boolean isPaged);
	
}
