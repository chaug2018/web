package com.yzj.ebs.report.biz;

import java.util.List;
import java.util.Map;

import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.report.pojo.FocusResult;

public interface IFocusReportBiz {

	List<FocusResult> getFocusReportList(Map<String,String> queryMap,
		PageParam queryParam,boolean isPaged,String selectCount);
}
