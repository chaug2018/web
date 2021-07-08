package com.yzj.ebs.edata.biz;

import java.io.IOException;
import java.util.List;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.database.CheckMainData;
import com.yzj.ebs.edata.bean.AccnoMainDataQueryParam;

/**
 * 创建于:2013-04-10<br>
 * 版权所有(C) 2013 深圳市银之杰科技股份有限公司<br>
 * 账单打印业务逻辑处理
 * 
 * @author 秦靖锋
 * @version 1.0
 */
public interface IDataExportBiz {

	/***
	 * 查询DetailMainData信息
	 * 
	 * @param accnoMainDataQueryParam
	 *            查询信息对象
	 * @throws Exception
	 */
	public List<CheckMainData> queryAccNoMainDataInfo(
			AccnoMainDataQueryParam accnoMainDataQueryParam)
			throws Exception;

	/***
	 * 封装导出信息
	 * 
	 * @param resultList
	 *            查询结果集 password 输入的密码 accnoMainDataQueryParam 查询信息对象
	 * @throws XDocProcException
	 */
	public List<CheckMainData> makeDataInfo(List<CheckMainData> resultList, String password,
			AccnoMainDataQueryParam accnoMainDataQueryParam) throws IOException;
	/**
	 * 导出明细
	 * @throws Exception 
	 */
	public void exportDetail(String password,String month) throws Exception;
}
