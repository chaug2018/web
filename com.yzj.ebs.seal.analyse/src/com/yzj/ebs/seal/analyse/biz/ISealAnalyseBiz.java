package com.yzj.ebs.seal.analyse.biz;

import java.util.List;
import java.util.Map;

import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.seal.analyse.pojo.AnalyseResult;

/**
 * 创建于:2013-04-07<br>
 * 版权所有(C) 2013 深圳市银之杰科技股份有限公司<br>
 * 验印情况统计  业务实现
 * 
 * @author 单伟龙
 * @version 1.0.0
 */
public interface ISealAnalyseBiz {

	/**
	 * 验印情况统计 业务实现
	 * @param queryMap
	 * @param pageParam
	 * @param isPaged
	 * @return
	 * @throws XDocProcException
	 */
	List<AnalyseResult> getProveAnalyseList(Map<String, String> queryMap,
			PageParam pageParam, boolean isPaged,String selectCount) throws XDocProcException;
}
