package com.yzj.ebs.common;

import java.util.List;
import java.util.Map;

import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.database.DetailMainData;

/**
 *创建于:2012-11-14<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 所有对账不对账账户dao操作接口
 * @author lif
 * @version 1.0
 */
public interface IDetailMainDataAdm extends IBaseService<DetailMainData>  {

	
	/**
	 * 根据对账日期查找账户信息
	 * @param queryMap
	 *            查询条件Map
	 * @return
	 * @throws XDocProcException
	 */
	List<DetailMainData> getDetailMainDataByDocDate(Map<String,String> queryMap,PageParam param)throws XDocProcException;
	/**
	 * 导出数据
	 * @param docdate
	 * @return
	 * @throws XDocProcException
	 */
	public List<DetailMainData> exportTxtDate(Map<String,String> queryMap) throws XDocProcException;
	
	/**
	 *   把账单的发送方式 改为以发送docstate 2 更新发送日期
	 */
	public void updateCheckmaindataByDocdate(String docdate,Map<String,String>mapdata) throws XDocProcException;
	/**
	 *   把对账单的 发送次数+1
	 */
	public void updatePrintTime (Map<String,String>mapdata)throws XDocProcException;
}
