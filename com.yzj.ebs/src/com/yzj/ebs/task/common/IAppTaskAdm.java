package com.yzj.ebs.task.common;

import java.util.List;

import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.database.DocSet;
import com.yzj.ebs.task.common.TaskConstDef.TaskResult;
import com.yzj.wf.bpm.engine.task.IBPMTask;
import com.yzj.wf.core.model.po.wrapper.XPeopleInfo;

/**
 * 
 * 创建于:2012-9-25<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 回收流程任务相关接口
 * 
 * @author 陈林江
 * @version 1.0
 */
public interface IAppTaskAdm {

	/**
	 * 从扫描创建任务
	 * 
	 * @param docset
	 *            业务对象
	 * @param people
	 *            人员
	 * @param extParams
	 *            可选扩展参数
	 * @throws XDocProcException
	 *             异常
	 */
	Object createTaskFromScan(DocSet docset, XPeopleInfo people, Object... extParams)
			throws XDocProcException;
	
	/**
	 * 从网银创建任务
	 * 
	 * @param docset
	 *            业务对象
	 * @param people
	 *            人员
	 * @param extParams
	 *            可选扩展参数
	 * @throws XDocProcException
	 *             异常
	 */
	Object createTaskFromEbank(DocSet docset,XPeopleInfo people, Object... extParams)
			throws XDocProcException;

	/**
	 * 获取任务
	 * 
	 * @param appId
	 *            应用程序标识
	 * @param people
	 *            用户
	 * @param extParams
	 *            可选扩展参数
	 * @return 任务对象
	 * @throws XDocProcException
	 *             异常
	 */
	IBPMTask getTask(String appId, XPeopleInfo people, Object... extParams)
			throws XDocProcException;

	/**
	 * 获取任务，只获取空闲任务
	 * 
	 * @param appId
	 *            应用程序标识
	 * @param people
	 *            用户
	 * @param extParams
	 *            可选扩展参数
	 * @return 任务对象
	 * @throws XDocProcException
	 *             异常
	 */
	IBPMTask getFreeTask(String appId, XPeopleInfo people, Object... extParams)
			throws XDocProcException;

	/**
	 * 提交任务
	 * 
	 * @param taskInfo
	 *            任务对象
	 * @param ywInfo
	 *            业务信息对象
	 * @param people
	 *            用户
	 * @param taskResult
	 *            任务处理结果
	 * @param extParams
	 *            可选扩展参数
	 * @throws XDocProcException
	 *             异常
	 */
	void commitTask(IBPMTask taskInfo, DocSet docset, XPeopleInfo people,
			TaskResult taskResult, Object... extParams)
			throws XDocProcException;

	/**
	 * 取消任务，取消任务时，任务锁定状态将不解除，否则获取下一笔时有几率获取到上一笔任务
	 * 
	 * @param taskInfo
	 *            任务对象
	 * @param docset
	 *            业务信息对象
	 * @param people
	 *            用户
	 * @param taskResult
	 *            任务处理结果
	 * @param extParams
	 *            可选扩展参数
	 * @throws XDocProcException
	 *             异常
	 */
	IBPMTask cancelAndGetTask(IBPMTask taskInfo, DocSet docset, XPeopleInfo people,
			TaskResult taskResult, Object... extParams)
			throws XDocProcException;

	/**
	 * 批量查询任务列表，注意：本方法并不会锁定任务
	 * 
	 * @param appId
	 *            应用程序标识
	 * @param people
	 *            用户
	 * @param firstResult
	 *            分页查询时的第一条记录索引,<b>注意：如果maxResults设置为-1则会忽略本参数的值，不再分页查询</b>
	 * @param maxResults
	 *            分页查询时每页记录条数，<b>注意：如果设置为-1则会忽略firstResult参数值，并且不再分页查询</b>
	 * @return 查询到的任务信息
	 * @throws XDocProcException
	 *             异常
	 */
	List<IBPMTask> queryTaskList(String appId, XPeopleInfo people, int firstResult,
			int maxResults) throws XDocProcException;

	/**
	 * 锁定任务
	 * 
	 * @param appId
	 *            应用程序标识
	 * @param taskInfo
	 *            任务实例
	 * @param userId
	 *            用户名称
	 * @throws XDocProcException
	 *             异常
	 */
	void lockTask(IBPMTask taskInfo, String userId) throws XDocProcException;

	/**
	 * 解锁任务
	 * 
	 * @param appId
	 *            应用程序标识
	 * @param taskInfo
	 *            任务实例
	 * @param userId
	 *            用户名称
	 * @throws XDocProcException
	 *             异常
	 */
	void unLockTask(IBPMTask taskInfo, String userId) throws XDocProcException;

	/**
	 * 批量锁定任务
	 * 
	 * @param taskInfos
	 *            待锁定的任务列表
	 * @param userId
	 *            用户ID
	 * @return 已锁定的任务列表
	 * @throws XDocProcException
	 *             异常
	 */
	List<IBPMTask> lockTaskList(List<IBPMTask> taskInfos, String userId)
			throws XDocProcException;

	/**
	 * 批量解锁任务
	 * 
	 * @param taskInfos
	 *            待解锁的任务列表
	 * @param userId
	 *            用户ID
	 * @return 解锁成功的任务列表
	 * @throws XDocProcException
	 *             异常
	 */
	List<IBPMTask> unLockTaskList(List<IBPMTask> taskInfos, String userId)
			throws XDocProcException;

	/**
	 * 放弃任务
	 * 
	 * @param taskInfo
	 *            任务信息
	 * @param people
	 *            操作人员
	 * @throws XDocProcException
	 */
	void giveUpTask(IBPMTask taskInfo, XPeopleInfo people) throws XDocProcException;
}
