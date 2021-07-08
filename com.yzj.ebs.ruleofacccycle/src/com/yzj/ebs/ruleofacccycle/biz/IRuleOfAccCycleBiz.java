package com.yzj.ebs.ruleofacccycle.biz;

import java.util.List;
import java.util.Map;

import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.database.RuleOfAccCycle;

public interface IRuleOfAccCycleBiz {
	
	/**
	 *  增加 账户类型 规则
	 *  把字段cuteflog 字段设置为1 表示启用该条规则 
	 */
	void addRule (RuleOfAccCycle rule) throws XDocProcException;
	/**
	 *  删除 账户类型 规则
	 *  把字段cuteflog 字段设置为-1 表示废弃该条规则
	 */
	void deleteRule (RuleOfAccCycle rule)throws XDocProcException;
	/**
	 *  修改 账户类型 规则
	 */
	void reviseRule (RuleOfAccCycle rule)throws XDocProcException;
	/**
	 *  查询 账户类型 规则
	 */
	List<RuleOfAccCycle> queryRule(Map<String,String> queryMap,PageParam param, boolean isPaged)throws XDocProcException;
	/**
	 *  在客户端显示过滤操作  如起值为1 止为9999999999 则为""  
	 *  如果 起值为0 止值为999999999  则整个列不显示
	 * @return
	 */
	public List<RuleOfAccCycle> getShow(List<RuleOfAccCycle> ruleList)throws XDocProcException;
	
}
