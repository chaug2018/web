package com.yzj.ebs.common;

import java.util.List;
import java.util.Map;

import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.database.RuleOfAccCycle;
/**
 * 
 *创建于:2013-8-23<br>
 *版权所有(C) 2013 深圳市银之杰科技股份有限公司<br>
 * 账户类型定制控制类
 * @author j_sun
 * @version 1.0.0
 */
public interface IRuleOfAccCycleAdm extends IBaseService<RuleOfAccCycle>{

	/**
	 *  查询 账户类型定制的 规则
	 */
	List<RuleOfAccCycle> queryRuleList(Map<String, String> queryMap,
			PageParam param,boolean flog)throws XDocProcException;
}
