package com.yzj.ebs.rush.biz;

import java.util.List;
import java.util.Map;

import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.database.CheckMainData;
import com.yzj.ebs.rush.queryparam.RushQueryParam;

/**
 * 创建于:2013-04-07<br>
 * 版权所有(C) 2013 深圳市银之杰科技股份有限公司<br>
 * 催收处理  业务实现
 * 
 * @author 单伟龙
 * @version 1.0.0
 */
public interface IRushBiz {
	/**
	 * 获取分页checkmaindata数据
	 * @throws XDocProcException 
	 */
	List<CheckMainData> getCheckMainData(Map<String,String> queryMap,RushQueryParam rushQueryParam) throws XDocProcException;
	/**
	 * 获取全部checkmaindata数据
	 * @throws XDocProcException 
	 */
	List<CheckMainData> getAllCheckMainData(Map<String, String> queryMap) throws XDocProcException;

	/**
	 * 单个催收处理
	 * @throws Exception 
	 */
	String modifyOne(CheckMainData checkMainData,String index) throws Exception;
	

}
