package com.yzj.ebs.ruleofacccycle.biz;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.yzj.ebs.common.IRuleOfAccCycleAdm;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.database.RuleOfAccCycle;

/**
 * 
 *创建于:2013-8-23<br>
 *版权所有(C) 2013 深圳市银之杰科技股份有限公司<br>
 * 账户类型定制
 * @author j_sun
 * @version 1.0.0
 */

public class RuleOfAccCycleImpl implements IRuleOfAccCycleBiz{
	private IRuleOfAccCycleAdm ruleOfAccCycleAdm;

	
	
	/**
	 *  增加 账户类型 规则
	 *   把字段ecuteflog 字段设置为0 表示启用该条规则
	 */
	public void addRule (RuleOfAccCycle rule) throws XDocProcException{
		// 把字段cuteflog 字段设置为1 表示启用该条规则
		rule.setExecuteFlog("0");
		ruleOfAccCycleAdm.saveOrUpdate(rule);
	}
	/**
	 *  删除 账户类型 规则
	 *  把字段ecuteflog 字段设置为1 表示废弃该条规则
	 */
	public void deleteRule (RuleOfAccCycle rule)throws XDocProcException{
		// 物理删除
		ruleOfAccCycleAdm.delete(rule);
	}
	/**
	 *  修改 账户类型 规则
	 */
	public void reviseRule (RuleOfAccCycle rule)throws XDocProcException{
		rule.setExecuteFlog("0");
		ruleOfAccCycleAdm.saveOrUpdate(rule);
	}
	/**
	 *  查询 账户类型 规则  isPage 为true全查 false分页
	 */
	public List<RuleOfAccCycle> queryRule(Map<String,String> queryMap,PageParam param, boolean isPaged)
			throws XDocProcException{
		//分页
		return getShow(ruleOfAccCycleAdm.queryRuleList(queryMap, param,isPaged));
		
	}
	
	/**
	 *  在客户端显示过滤操作  如起值为1 止为9999999999 则为""  
	 *  如果 起值为0 止值为999999999  则整个列不显示
	 * @return
	 */
	
	public List<RuleOfAccCycle> getShow(List<RuleOfAccCycle> ruleList)throws XDocProcException{
		List<RuleOfAccCycle> showRule = new ArrayList<RuleOfAccCycle>();
		String max = "9999999999999";
		Iterator<RuleOfAccCycle> iter = ruleList.iterator();
		while(iter.hasNext()){
			RuleOfAccCycle rule = iter.next();
			if(rule.getMaxBal().equals(max)){
				rule.setMaxBal("");
					if(rule.getMinBal().equals("0")){
						rule.setMinBal("");
					}
			}
			if(rule.getOneMaxAccrual().equals(max)){
				rule.setOneMaxAccrual("");
					if(rule.getOneMinAccrual().equals("0")){
						rule.setOneMinAccrual("");
					}
			}
			if(rule.getTotalMaxAccrual().equals(max)){
				rule.setTotalMaxAccrual("");
					if(rule.getTotalMinAccrual().equals("0")){
						rule.setTotalMinAccrual("");
					}
			}
			showRule.add(rule);
		}	
		return showRule;
	}
	
	public IRuleOfAccCycleAdm getRuleOfAccCycleAdm() {
		return ruleOfAccCycleAdm;
	}

	public void setRuleOfAccCycleAdm(IRuleOfAccCycleAdm ruleOfAccCycleAdm) {
		this.ruleOfAccCycleAdm = ruleOfAccCycleAdm;
	}
	
	
	
	
}
