package com.yzj.ebs.notmatch.analyse.biz;

import java.util.List;
import java.util.Map;

import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.notmatch.analyse.pojo.AnalyseResult;

/**
 * 创建于:2013-04-01<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 未达账情况统计 业务实现接口
 * @author 单伟龙
 * @version 1.0.0
 */
public interface INotMatchAnalyseBiz {
	/**
	 * 获取数据
	 * @throws XDocProcException 
	 */
	public List<AnalyseResult> getAnalyseResult(Map<String,String> queryMap,
			PageParam param,boolean isPaged,String selectCount) throws XDocProcException;

}
