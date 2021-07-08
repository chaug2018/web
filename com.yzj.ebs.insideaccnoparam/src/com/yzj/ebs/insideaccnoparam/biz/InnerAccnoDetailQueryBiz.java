package com.yzj.ebs.insideaccnoparam.biz;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.yzj.ebs.database.InnerAccnoDetail;
import com.yzj.ebs.insideaccnoparam.pojo.QueryParam;

public interface InnerAccnoDetailQueryBiz {

	/**
	 * 内部账户发生额明细查询业务实现接口
	 * @param queryMap 查询map
	 * @param queryParam  页面参数
	 * @param isPaged  是否分页
	 * @return
	 * @throws SQLException 
	 */
	public List<InnerAccnoDetail> getAccnoDetailData(Map<String, String> queryMap,
			QueryParam queryParam, boolean isPaged) throws SQLException;

}
