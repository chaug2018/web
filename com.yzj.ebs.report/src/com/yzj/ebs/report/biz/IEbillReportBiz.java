package com.yzj.ebs.report.biz;

import java.util.List;
import java.util.Map;

import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.report.pojo.EbillResult;

public interface IEbillReportBiz {

	List<EbillResult> getEbillReportList(Map<String,String> queryMap,
		PageParam queryParam,boolean isPaged,String selectCount);
}
