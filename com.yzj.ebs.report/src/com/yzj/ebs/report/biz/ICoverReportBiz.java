package com.yzj.ebs.report.biz;

import java.util.List;
import java.util.Map;

import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.report.pojo.CoverResult;

public interface ICoverReportBiz {

	List<CoverResult> getCoverReportList(Map<String,String> queryMap,
		PageParam queryParam,boolean isPaged,String selectCount,String isThree);
}
