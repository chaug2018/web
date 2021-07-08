package com.yzj.ebs.param.biz;

import java.util.List;
import java.util.Map;

import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.database.AccNoDetailData;
import com.yzj.ebs.param.queryparam.AccnoDetailQueryParam;

/**
 * 创建于:2013-04-01<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 日志查询模块 业务实现接口
 * 
 * @author 单伟龙
 * @version 1.0.0
 */
public interface IParamQueryBiz {

	/**
	 * 发生额明细查询业务实现接口
	 * @param queryMap 查询map
	 * @param accnoDetailQueryParam  页面参数
	 * @param entityName  表名
	 * @param workdate  工作日期
	 * @param isPaged  是否分页
	 * @return
	 * @throws XDocProcException 
	 */
	public List<AccNoDetailData> getAccnoDetailDataByDocDate(
			Map<String, String> queryMap,
			AccnoDetailQueryParam accnoDetailQueryParam, String entityName,
			String workdate, boolean isPaged) throws XDocProcException;

}
