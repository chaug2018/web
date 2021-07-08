package com.yzj.ebs.insideaccnoparam.biz;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.database.InnerAccnoMaindata;

public interface InsideAccnoQueryBiz {
	
	public List<InnerAccnoMaindata> queryAccnolist(Map<String,String> queryMap,PageParam param,boolean isPage) throws SQLException; 
	
}
