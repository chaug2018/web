package com.yzj.ebs.retreatprocess.biz;

import java.util.List;
import java.util.Map;

import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.database.CheckMainData;
import com.yzj.ebs.retreatprocess.queryparam.RetreatQueryParam;

/**
 * 创建于:2013-04-07<br>
 * 版权所有(C) 2013 深圳市银之杰科技股份有限公司<br>
 * 退信处理 业务实现
 * 
 * @author 单伟龙
 * @version 1.0.0
 */
public interface IRetreatProcessBiz {
	/**
	 * 获取分页checkmaindata数据
	 * @throws XDocProcException 
	 */
	List<CheckMainData> getCheckMainData(Map<String,String> queryMap,RetreatQueryParam retreatQueryParam) throws XDocProcException;
	/**
	 * 退信处理单个处理的实现，批量处理其实就是循环单个index下标
	 */
	public void modifyOne(CheckMainData checkMainData,String index) throws Exception;
	/**
	 * 获取全部checkmaindata数据
	 * @throws XDocProcException 
	 */
	List<CheckMainData> getAllCheckMainData(Map<String, String> queryMap) throws XDocProcException;

}
