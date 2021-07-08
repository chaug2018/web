package com.yzj.ebs.insideaccnoparam.biz;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.database.InnerAccnoMaindata;

/**
 * 创建于:2013-08-16 版权所有(C) 2013 深圳市银之杰科技股份有限公司
 * 内部账户对账单查询复核
 * @author j_sun
 * @version 1.0.0
 */
public interface InsideAccnoRecheckBiz {
	
	public List<InnerAccnoMaindata> queryAccnoRecheck(Map<String,String> queryMap,PageParam param)throws SQLException; 
	
	public void updateRecheck(Map<String,String> updateMap)throws SQLException; 
	
}
