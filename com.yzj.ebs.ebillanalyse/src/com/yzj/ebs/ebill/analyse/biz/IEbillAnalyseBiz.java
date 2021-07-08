package com.yzj.ebs.ebill.analyse.biz;

import java.util.List;
import java.util.Map;

import com.yzj.ebs.common.param.PageParam;

import com.yzj.ebs.ebill.analyse.pojo.AnalyseResult;
/**
 * 创建于:2013-04-01<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 机构对账率统计  业务实现
 * 
 * @author 单伟龙
 * @version 1.0.0
 */
public interface IEbillAnalyseBiz {

	/**
	 * 得到数据
	 */
	List<AnalyseResult> getEbillAnalyseList(Map<String,String> queryMap,
		PageParam queryParam,boolean isPaged,String selectCount);
}
