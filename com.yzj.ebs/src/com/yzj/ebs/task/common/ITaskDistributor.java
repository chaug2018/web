/**
 * 
 */
package com.yzj.ebs.task.common;

import java.util.List;

/**
 * 
 * 创建于:2012-12-11<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 自定义任务分配器接口
 * @author 陈林江
 * @version 1.0
 */
public interface ITaskDistributor {

	/**
	 * 获取人工录入的任务处理人员
	 * @param businessKey 业务主键
	 * @return 人员集合
	 * @throws Exception
	 */
	List<String> getManualInputUsers(String businessKey)throws Exception;
	
	/**
	 * 获取录入审核的任务处理人员
	 * @param businessKey 业务主键
	 * @return 人员集合
	 * @throws Exception
	 */
	List<String> getManualAuthUsers(String businessKey)throws Exception;
	
	/**
	 * 获取人工初验的任务处理人员
	 * @param businessKey 业务主键
	 * @return 人员集合
	 * @throws Exception
	 */
	List<String> getProveFstUsers(String businessKey)throws Exception;
	
	/**
	 * 获取人工复验的任务处理人员
	 * @param businessKey 业务主键
	 * @return 人员集合
	 * @throws Exception
	 */
	List<String> getProveSndUsers(String businessKey)throws Exception;
	
	/**
	 * 获取主管验印的任务处理人员
	 * @param businessKey 业务主键
	 * @return 人员集合
	 * @throws Exception
	 */
	List<String> getProveAuthUsers(String businessKey)throws Exception;
	
	/**
	 * 获取未达录入的任务处理人员
	 * @param businessKey 业务主键
	 * @return 人员集合
	 * @throws Exception
	 */
	List<String> getNotMatchInputUsers(String businessKey)throws Exception;
	
	/**
	 * 获取未达审核的任务处理人员
	 * @param businessKey 业务主键
	 * @return 人员集合
	 * @throws Exception
	 */
	List<String> getNotMatchAuthUsers(String businessKey)throws Exception;
	
	/**
	 * 获取删除审核的任务处理人员
	 * @param businessKey 业务主键
	 * @return 人员集合
	 * @throws Exception
	 */
	List<String> getDeleteAuthUsers(String businessKey)throws Exception;
	
}
