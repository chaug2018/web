package com.yzj.ebs.report.biz;

import java.util.List;
import java.util.Map;

import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.report.pojo.ActiveAccResult;

public interface IActiveAccReportBiz {

	List<ActiveAccResult> getActiveAccReportList(Map<String,String> queryMap,
		PageParam queryParam,boolean isPaged,String tableName,String workDate);
}
