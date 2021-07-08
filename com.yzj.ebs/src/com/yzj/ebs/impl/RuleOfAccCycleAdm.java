package com.yzj.ebs.impl;

import java.util.ArrayList;
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
 * 账户类型定制 dao层
 * @author j_sun
 * @version 1.0.0
 */
public class RuleOfAccCycleAdm extends BaseService<RuleOfAccCycle> implements IRuleOfAccCycleAdm{
	/**
	 * 查询 账户类型定制规则
	 */
	@SuppressWarnings("unchecked")
	public List<RuleOfAccCycle> queryRuleList(Map<String, String> queryMap,
			PageParam param,boolean flog)throws XDocProcException{
		
		List<RuleOfAccCycle> list =new  ArrayList<RuleOfAccCycle>();
		StringBuffer hql = new StringBuffer();
		hql.append( "from RuleOfAccCycle where 1=1 ");
		if (queryMap != null) {
			for (Map.Entry<String, String> entry : queryMap.entrySet()) {
				if (entry.getValue() != null
						&& entry.getValue().trim().length() != 0) {
					hql.append( " and ").
					append( entry.getKey() ).
					append( "='") .
					append( entry.getValue().trim()).
					append("' ");
				}
			}
		}
		hql.append( "and executeflog=0 order by accCycle asc ,minBal desc");
		try{
			list = (List<RuleOfAccCycle>) dao.find(hql.toString());
		}catch (Exception e) {
			e.printStackTrace();
		}
		if(!flog){//分页查询
			Integer countNumber =list.size();   //得到数据的条数
			int pageSize = param.getPageSize();// 每页显示结果条数
			int totalPage = (int) ((countNumber - 1) / pageSize + 1);// 总页数
			int curPage = param.getCurPage();// 当前要显示的页
			if (curPage > totalPage) {
				curPage = totalPage;
			}
			int firstResult = (curPage - 1) * pageSize;// 分页时显示的第一条记录，默认从0开始
			int lastNumber = countNumber - firstResult;
			param.setFirstResult(firstResult);
			param.setCurPage(curPage);
			param.setTotal((int) countNumber);
			param.setTotalPage(totalPage);
			if((countNumber - firstResult)>pageSize){
				param.setLastResult(firstResult + pageSize);// 当前页显示的最后一条记录
			}else{
				param.setLastResult( countNumber);// 当前页显示的最后一条记录
			}		
			
			if(lastNumber >= pageSize){
				return	list.subList(firstResult, firstResult+pageSize); 
			}else{
				return	list.subList(firstResult, firstResult+lastNumber);
			}
		}else{
			return list;
		}
	}
}
