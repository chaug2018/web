package com.yzj.ebs.insideaccnoparam.biz;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.database.InnerAccnoMaindata;

/**
 * 创建于:2013-08-16 版权所有(C) 2013 深圳市银之杰科技股份有限公司
 * 内部账户对账单查询核对
 * @author j_sun
 * @version 1.0.0
 */
public interface InsideAccnoCheckBiz {
	/**
	 * 查询 内部账户对账单信息
	 */
	public List<InnerAccnoMaindata> queryAccnoCheck(Map<String,String> queryMap,PageParam param)throws SQLException; 
	
	/**
	 *  核对 内部账户对账单信息
	 */
	public void modifyInnerAccno(Map<String,String> updateMap)throws SQLException; 

}
