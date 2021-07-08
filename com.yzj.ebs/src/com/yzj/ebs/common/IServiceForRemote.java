package com.yzj.ebs.common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import com.yzj.ebs.database.AccNoMainData;
import com.yzj.ebs.database.BasicInfoLog;
import com.yzj.ebs.database.CheckMainData;
import com.yzj.ebs.database.DocSet;
import com.yzj.ebs.task.common.TaskConstDef.TaskResult;
import com.yzj.ebs.util.DataLogUtils;
import com.yzj.wf.bpm.engine.task.IBPMTask;

/**
 * 创建于:2012-8-24<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 远程服务接口，为远端程序（如自动程序）提供一系列方法
 * 
 * @author WangXue
 * @version 1.0.0
 */
public interface IServiceForRemote extends Remote {

	/**
	 * 获取任务
	 * 
	 * @param appId
	 *            应用程序标识
	 * @param userId
	 *            用户ID
	 * @param extParams
	 *            可选扩展参数
	 * @return 任务对象
	 * @throws XDocProcException
	 *             异常
	 */
	IBPMTask getTask(String appId, String userId, Object... extParams)
			throws XDocProcException, RemoteException;

	/**
	 * 提交任务
	 * 
	 * @param taskInfo
	 *            任务对象
	 * @param ywInfo
	 *            业务信息对象
	 * @param userId
	 *            用户ID
	 * @param taskResult
	 *            任务处理结果
	 * @param extParams
	 *            可选扩展参数
	 * @throws XDocProcException
	 *             异常
	 */
	void commitTask(IBPMTask taskInfo, DocSet ywInfo, String userId,
			TaskResult taskResult, Object... extParams)
			throws XDocProcException, RemoteException;

	/**
	 * 根据DocId查询docset
	 * 
	 * @param docId
	 *            主键docid
	 * @return 查询结果
	 * @throws XDocProcException
	 *             异常
	 */
	DocSet queryDocSetByID(Long docId) throws XDocProcException,
			RemoteException;

	/**
	 * 借记记账
	 * 
	 * @param docSet
	 *            业务信息
	 * @return 交易结果，成功返回true
	 * @throws XDocProcException
	 *             异常
	 */
	boolean tallyDebit(DocSet docSet) throws XDocProcException, RemoteException;

	/**
	 * 
	 * 根据账单编号查询checkmaindata
	 * 
	 * @param voucherno
	 *            voucherno账单编号
	 * @return 查询结果
	 * @throws XDocProcException
	 *             自定义异常
	 */
	public CheckMainData queryCheckMainData(String voucherno)
			throws XDocProcException, RemoteException;

	/**
	 * 批量更新账号明细
	 * 
	 * @param list
	 *            需要更新的对象集合
	 * 
	 * @return
	 * @throws XDocProcException
	 */
	public void batchUpdateAccNoMainData(List<AccNoMainData> list)
			throws XDocProcException, RemoteException;

	/**
	 * 根据账单编号获取账号明细
	 * 
	 * @param voucherno
	 *            账单编号
	 * @return
	 * @throws XDocProcException
	 */
	public List<AccNoMainData> getAccnoMainDataByVoucherNo(String voucherno)
			throws XDocProcException, RemoteException;

	/**
	 * 
	 * 根据账单编号查询BasicInfo,供自动验印使用
	 * 
	 * @param voucherno
	 *            voucherno账单编号
	 * @return 查询结果
	 * @throws XDocProcException
	 *             自定义异常
	 */
	public List<Object[]> getSealAccno(String voucherno)
			throws XDocProcException, Exception, RemoteException;

	/**
	 * 
	 * 查询账号在newseal库中是否存在
	 * 
	 * @param sql
	 *            sql查询语句
	 * @return 查询结果
	 * @throws XDocProcException
	 *             自定义异常
	 */
	public List<Object> checkSealAccno(String sql) throws XDocProcException,
			Exception, RemoteException;

	/**
	 * 根据币种得到和人民币的汇率
	 * 
	 * @param currtype
	 *            币种
	 * @return 汇率
	 */
	double getExchangeRate(String currtype) throws RemoteException;
	
	
	public void error(String OPDESC,Throwable t,boolean istoData) throws RemoteException;
	public void info(String OPDESC,Throwable t,boolean istoData) throws RemoteException;
	public void error(String OPDESC,boolean istoData) throws RemoteException;
	public void info(String OPDESC,boolean istoData) throws RemoteException;
	public void debug(String OPDESC,boolean istoData) throws RemoteException;
	
	/**
	 * 
	 * @param OPDESC 日志说明
	 * @param OPMODE 日志模块
	 */
	public void error(String OPDESC,String OPMODE) throws RemoteException;
	/**
	 * 
	 * @param OPDESC 日志说明
	 * @param OPMODE 日志模块
	 */
	public void info(String OPDESC,String OPMODE) throws RemoteException;
	/**
	 * 
	 * @param OPDESC 日志说明
	 * @param OPMODE 日志模块
	 */
	public void debug(String OPDESC,String OPMODE) throws RemoteException;
	
	/**
	 * 
	 * @param OPDESC 日志内容
	 * @param t 异常 
	 * @param OPMODE 日志模块
	 */
	public void info(String OPDESC,Throwable t,String OPMODE) throws RemoteException;
	/**
	 * 
	 * @param OPDESC 日志内容
	 * @param t 异常 
	 * @param OPMODE 日志模块
	 */
	public void error(String OPDESC,Throwable t,String OPMODE) throws RemoteException;
	/**
	 * 
	 * @param OPDESC 日志内容
	 * @param t 异常 
	 * @param OPMODE 日志模块
	 */
	public void debug(String OPDESC,Throwable t,String OPMODE) throws RemoteException;
}
