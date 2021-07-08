package com.yzj.ebs.task.impl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzj.ebs.common.CreditParam;
import com.yzj.ebs.common.IDocLogAdm;
import com.yzj.ebs.common.IDocSetAdm;
import com.yzj.ebs.common.IPublicTools;
import com.yzj.ebs.common.IdCenterParam;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.database.DocLog;
import com.yzj.ebs.database.DocSet;
import com.yzj.ebs.task.common.IAppTaskAdm;
import com.yzj.ebs.task.common.TaskConstDef.NotMatchResult;
import com.yzj.ebs.task.common.TaskConstDef.PreNodeTaskName;
import com.yzj.ebs.task.common.TaskConstDef.PreNodeVar;
import com.yzj.ebs.task.common.TaskConstDef.ProcessDefKey;
import com.yzj.ebs.task.common.TaskConstDef.ProcessVariableKey;
import com.yzj.ebs.task.common.TaskConstDef.TaskResult;
import com.yzj.ebs.task.common.TaskConstDef.TaskSource;
import com.yzj.ebs.util.FileUtil;
import com.yzj.wf.bpm.engine.identity.BPMUser;
import com.yzj.wf.bpm.engine.runtime.IBPMRuntimeManager;
import com.yzj.wf.bpm.engine.task.IBPMTask;
import com.yzj.wf.bpm.engine.task.IBPMTaskManager;
import com.yzj.wf.common.WFLogger;
import com.yzj.wf.common.util.ConditionParamDefine;
import com.yzj.wf.core.model.po.wrapper.XPeopleInfo;
import com.yzj.wf.pam.common.IParamManager;
import com.yzj.wf.pam.db.BaseParam;

/**
 * 
 * 创建于:2012-9-25<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 回城流程任务接口实现
 * 
 * @author 陈林江
 * @version 1.0
 */
public class AppTaskAdm implements IAppTaskAdm {
	private static final String ParamTableName_ModuleFlow = "Param_ModuleFlow";
	private static final String ParamField_AppId = "appId";
	private static final String ParamField_ProcessDefKey = "processDefKey";
	private static final String ParamField_TaskName = "taskName";
	private static final String TaskTagKey_ProcessDefKey = "processDefKey";
	private static final String StartTaskNodeName = "开始";

	private SimpleDateFormat sdfYMD = new SimpleDateFormat("yyyy-MM-dd");
	// private SimpleDateFormat sdfHMS = new SimpleDateFormat("HH:mm:ss");
	private String flowExtFilePath_recovery = "xml-config/FlowExt_RECOVERY.xml";

	private IDocSetAdm docSetAdm;
	private IDocLogAdm docLogAdm;
	private IParamManager paramManager;
	private IBPMRuntimeManager runtimeManager;

	/**
	 * 回收流程任务管理器
	 */
	private IBPMTaskManager taskManager;
	/**
	 * 公共服务工具
	 */
	private IPublicTools publicTools;

	/**
	 * 流程扩展配置
	 */
	private ConditionParamDefine flowExtConfig;

	/**
	 * appid与TaskName的映射,K:appid,V:任务的列表,一个appid可能对应多个流程上的多个任务节点
	 */
	private Map<String, List<AppFlowDef>> appId_TaskNameMap;

	/**
	 * taskNam和流程变量中的preNode映射关系表
	 */
	private static Map<String, String> taskName_preNodeMap;
	public static Map<String, String> preNode_taskNameMap;
	public static Map<String,String> idCenterMap=new HashMap<String, String>();

	private WFLogger logger = WFLogger.getLogger(this.getClass());

	public void init() throws XDocProcException {
		try {
			// 初始化appid和任务节点的对应关系
			appId_TaskNameMap = new HashMap<String, List<AppFlowDef>>();
			List<BaseParam> params = paramManager
					.getAllParamsByGroup(ParamTableName_ModuleFlow);
			for (BaseParam param : params) {
				String appId = param.getExtField(ParamField_AppId).toString();
				String processDefKey = param.getExtField(
						ParamField_ProcessDefKey).toString();
				String taskName = param.getExtField(ParamField_TaskName)
						.toString();
				AppFlowDef appFlowDef = new AppFlowDef(processDefKey, taskName);
				if (appId_TaskNameMap.containsKey(appId)) {
					appId_TaskNameMap.get(appId).add(appFlowDef);
				} else {
					List<AppFlowDef> list = new ArrayList<AppFlowDef>();
					list.add(appFlowDef);
					appId_TaskNameMap.put(appId, list);
				}
			}

			/**
			 * 初始化任务名称与preNode的对应关系
			 */
			taskName_preNodeMap = new HashMap<String, String>();
			preNode_taskNameMap = new HashMap<String, String>();
			taskName_preNodeMap.put(PreNodeTaskName.MANUALAUTH,
					PreNodeVar.MANUALAUTH);
			taskName_preNodeMap.put(PreNodeTaskName.MANUALINPUT,
					PreNodeVar.MANUALINPUT);
			taskName_preNodeMap.put(PreNodeTaskName.NOTMATCHAUTH,
					PreNodeVar.NOTMATCHAUTH);
			taskName_preNodeMap.put(PreNodeTaskName.NOTMATCHINPUT,
					PreNodeVar.NOTMATCHINPUT);
			taskName_preNodeMap.put(PreNodeTaskName.SEALAUTH,
					PreNodeVar.SEALAUTH);
			taskName_preNodeMap
					.put(PreNodeTaskName.SEALFST, PreNodeVar.SEALFST);
			taskName_preNodeMap
					.put(PreNodeTaskName.SEALSND, PreNodeVar.SEALSND);

			preNode_taskNameMap.put(PreNodeVar.MANUALAUTH,
					PreNodeTaskName.MANUALAUTH);
			preNode_taskNameMap.put(PreNodeVar.MANUALINPUT,
					PreNodeTaskName.MANUALINPUT);
			preNode_taskNameMap.put(PreNodeVar.NOTMATCHAUTH,
					PreNodeTaskName.NOTMATCHAUTH);
			preNode_taskNameMap.put(PreNodeVar.NOTMATCHINPUT,
					PreNodeTaskName.NOTMATCHINPUT);
			preNode_taskNameMap.put(PreNodeVar.SEALAUTH,
					PreNodeTaskName.SEALAUTH);
			preNode_taskNameMap
					.put(PreNodeVar.SEALFST, PreNodeTaskName.SEALFST);
			preNode_taskNameMap
					.put(PreNodeVar.SEALSND, PreNodeTaskName.SEALSND);

			// 读取docFlag和logInfo配置信息
			flowExtConfig = new ConditionParamDefine();
			File file = FileUtil.getFileByPath(flowExtFilePath_recovery);
			if (file == null){
				throw new Exception("文件不存在:" + flowExtFilePath_recovery);
			}else if(!file.exists()){
				throw new Exception("文件不存在:" + file.getAbsolutePath());
			}else{
				flowExtConfig.init(file);
			}				
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new XDocProcException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yzj.dps.task.common.IAppTaskAdm#createTask(java.lang.Object,
	 * java.lang.String, java.lang.Object[])
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Object createTaskFromScan(DocSet docSet, XPeopleInfo people, Object... extParams)
			throws XDocProcException {
		try {
			String idCenter=publicTools.getBankParam(people.getOrgNo()).getIdCenter();
			Map<String, Object> variableMap = getInitVariableMap();
			this.addVariableMap(variableMap, docSet.getIdCenter());
			variableMap.put(ProcessVariableKey.CREDIT, docSet.getCredit());
			variableMap.put(ProcessVariableKey.TASKSOURCE, TaskSource.SCAN);
			variableMap.put(ProcessVariableKey.ORGID, idCenter);
			StringBuffer logInfo = new StringBuffer();
			Integer docFlag = getDocFlagAndLogInfo(StartTaskNodeName,
					variableMap, logInfo);
			docSet.setDocFlag(docFlag);
			docSet = docSetAdm.saveOrUpdate(docSet);
			variableMap.put(ProcessVariableKey.BUSINESSKEY, docSet.getDocId()+"");
			writeDocLog(docSet, StartTaskNodeName,
					logInfo.toString(), people.getPeopleCode());
			String bizKey = String.valueOf(docSet.getDocId());
			BPMUser user = new BPMUser(people.getSid(),idCenter);
			runtimeManager.startProcessInstanceById(
					ProcessDefKey.EBS_RECOVERY, bizKey,user, variableMap);
			return docSet;
		} catch (Exception e) {
			logger.error("创建" + ProcessDefKey.EBS_RECOVERY + "流程的任务失败", e);
			throw new XDocProcException("创建流程实例失败:" + e.getMessage());
		}

	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yzj.dps.task.common.IAppTaskAdm#createTask(java.lang.Object,
	 * java.lang.String, java.lang.Object[])
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Object createTaskFromEbank(DocSet docSet, XPeopleInfo people, Object... extParams)
			throws XDocProcException {
		String bizKey = null;
		try {		
			Map<String, Object> variableMap = getInitVariableMap();
			this.addVariableMap(variableMap, docSet.getIdCenter());
			variableMap.put(ProcessVariableKey.CREDIT, docSet.getCredit());
			variableMap.put(ProcessVariableKey.TASKSOURCE, TaskSource.EBANK);
			variableMap.put(ProcessVariableKey.ORGID, docSet.getIdCenter());
			if (docSet.getNeedNotMatch() == 0) { // 不需要做未达
				variableMap.put(ProcessVariableKey.NOTMATCHRESULT,
						NotMatchResult.FINISH);
			} else { // 需要做未达
				variableMap.put(ProcessVariableKey.NOTMATCHRESULT,
						NotMatchResult.UNFINISH);
			}
			StringBuffer logInfo = new StringBuffer();
			Integer docFlag = getDocFlagAndLogInfo(StartTaskNodeName,
					variableMap, logInfo);
			docSet.setDocFlag(docFlag);
			docSet = docSetAdm.saveOrUpdate(docSet);
			bizKey=String.valueOf(docSet.getDocId());
			variableMap.put(ProcessVariableKey.BUSINESSKEY, docSet.getDocId()+"");
			writeDocLog(docSet, StartTaskNodeName,
					logInfo.toString(), people.getPeopleCode());
			idCenterMap.put(bizKey, docSet.getIdCenter());
			String idCenter=publicTools.getBankParam(people.getOrgNo()).getIdCenter();
			BPMUser user = new BPMUser(people.getSid(),idCenter);
			runtimeManager.startProcessInstanceById(
					ProcessDefKey.EBS_RECOVERY, bizKey, user,variableMap);
			return docSet;
		} catch (Exception e) {
			logger.error("创建" + ProcessDefKey.EBS_RECOVERY + "流程的任务失败", e);
			throw new XDocProcException("创建流程实例失败:" + e.getMessage());
		}finally{
			idCenterMap.remove(bizKey);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yzj.dps.task.common.IAppTaskAdm#getTask(java.lang.String,
	 * java.lang.String, java.lang.Object[])
	 */
	@SuppressWarnings("deprecation")
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public IBPMTask getTask(String appId, XPeopleInfo people,Object... extParams)
			throws XDocProcException {
		List<AppFlowDef> appFlowDefs = appId_TaskNameMap.get(appId);
		if (appFlowDefs == null) {
			throw new XDocProcException("未知的appId" + appId + ",请检查配置是否正确");
		}
		String idCenter=null;
		if(people.getOrgNo()!=null){//自动程序无机构号
			idCenter=publicTools.getBankParam(people.getOrgNo()).getIdCenter();
		}
		IBPMTask result = null;
		BPMUser user = new BPMUser(people.getSid(),idCenter);
		for (AppFlowDef appFlowDef : appFlowDefs) {
			try {
				String processDefKey = appFlowDef.getProcessDefKey();
				String taskName = appFlowDef.getTaskName();
				// // 优先获取未锁定的任务，没有未锁定的任务则获取锁定的任务
				result = taskManager.acquireTask(taskName, user);
				if (result != null) {
					result.setTagByName(TaskTagKey_ProcessDefKey, processDefKey);
					break;
				} else {
					List<IBPMTask> tasks = taskManager.getLockedTaskList(
							taskName, user);
					if (tasks != null && tasks.size() != 0) {
						result = tasks.get(0);
						result.setTagByName(TaskTagKey_ProcessDefKey,
								processDefKey);
						break;
					}
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				throw new XDocProcException("获取任务失败:" + e.getMessage());
			}
		}
		if (result != null) {
			logger.debug("获取任务成功，bizKey:" + result.getBusinessKey());
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yzj.dps.task.common.IAppTaskAdm#getFreeTask(java.lang.String,
	 * java.lang.String, java.lang.Object[])
	 */
	@SuppressWarnings("deprecation")
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public IBPMTask getFreeTask(String appId, XPeopleInfo people,
			Object... extParams) throws XDocProcException {
		List<AppFlowDef> appFlowDefs = appId_TaskNameMap.get(appId);
		if (appFlowDefs == null) {
			throw new XDocProcException("未知的appId" + appId + ",请检查配置是否正确");
		}
		IBPMTask result = null;
		for (AppFlowDef appFlowDef : appFlowDefs) {
			try {
				String processDefKey = appFlowDef.getProcessDefKey();
				String taskName = appFlowDef.getTaskName();
				String idCenter=null;
				if(people.getOrgNo()!=null){//自动程序无机构号
					idCenter=publicTools.getBankParam(people.getOrgNo()).getIdCenter();
				}
				BPMUser user = new BPMUser(people.getSid(),idCenter);
				result = taskManager.acquireTask(taskName, user);
				if (result != null) {
					result.setTagByName(TaskTagKey_ProcessDefKey, processDefKey);
					break;
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				throw new XDocProcException("获取任务失败:" + e.getMessage());
			}
		}
		if (result != null) {
			logger.debug("获取任务成功，bizKey:" + result.getBusinessKey());
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yzj.dps.task.common.IAppTaskAdm#commitTask(com.yzj.wf
	 * .bpm.engine.task.IBPMTask, java.lang.Object, java.lang.String,
	 * java.lang.String, java.lang.Object[])
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void commitTask(IBPMTask taskInfo, DocSet docSet, XPeopleInfo people,
			TaskResult taskResult, Object... extParams)
			throws XDocProcException {
		try {
			Map<String, Object> variables = taskInfo.getVariables();
			variables.put(ProcessVariableKey.RESULT, taskResult.getValue());
			variables.put(ProcessVariableKey.BUSINESSKEY, docSet.getDocId()+"");
			if (docSet.getNeedNotMatch() == 0) { // 不需要做未达
				variables.put(ProcessVariableKey.NOTMATCHRESULT,
						NotMatchResult.FINISH);
			} else { // 需要做未达
				variables.put(ProcessVariableKey.NOTMATCHRESULT,
						NotMatchResult.UNFINISH);
			}
			String preNode = taskName_preNodeMap.get(taskInfo.getName());
			if (preNode != null) { // 在删除审核节点提交任务的时候不需要设置preNode流程变量值
				variables.put(ProcessVariableKey.PRENODE, preNode);
			}
			variables.put(ProcessVariableKey.CREDIT, docSet.getCredit()); // 更新金额
			StringBuffer logInfoOut = new StringBuffer();
			// 根据配置获取docflag以及日志信息
			Integer docFlag = getDocFlagAndLogInfo(taskInfo.getName(),
					variables, logInfoOut);
			docSet.setDocFlag(docFlag);
			docSetAdm.update(docSet);
			writeDocLog(docSet, taskInfo.getName(),
					logInfoOut.toString(), people.getPeopleCode());
			String idCenter=null;
			if(people.getOrgNo()!=null){//自动程序无机构号
				idCenter=publicTools.getBankParam(people.getOrgNo()).getIdCenter();
			}
			BPMUser user = new BPMUser(people.getSid(),idCenter);
			taskManager.commitTask(taskInfo.getName(), taskInfo.getId(), user,
					variables);
		} catch (Exception e) {
			e.printStackTrace();
			throw new XDocProcException("提交任务失败:" + e.getMessage());
		}
	}

	@Override
	public IBPMTask cancelAndGetTask(IBPMTask taskInfo, DocSet docset,
			XPeopleInfo people, TaskResult taskResult, Object... extParams)
			throws XDocProcException {
		String idCenter=null;
		if(people.getOrgNo()!=null){//自动程序无机构号
			idCenter=publicTools.getBankParam(people.getOrgNo()).getIdCenter();
		}
		BPMUser user = new BPMUser(people.getSid(),idCenter);
		IBPMTask newTask = taskManager.acquireTask(taskInfo.getName(), user);
		if (newTask == null) {
			logger.warn("该环节已没有空闲任务");
			return taskInfo;
		} else {
			String taskName = taskInfo.getName();
			String taskId = taskInfo.getId();
			taskManager.unLockTask(taskName, taskId, user);
			return newTask;
		}

	}

	@Override
	public void giveUpTask(IBPMTask taskInfo, XPeopleInfo people)
			throws XDocProcException {//放弃任务时不做任何处理
//		BPMUser user = new BPMUser(userId);
//		String taskName = taskInfo.getName();
//		String taskId = taskInfo.getId();
//		taskManager.unLockTask(taskName, taskId, user);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yzj.dps.task.common.IAppTaskAdm#queryTaskList(java.lang.String ,
	 * java.lang.String, int, int)
	 */
	@SuppressWarnings("deprecation")
	@Override
	public List<IBPMTask> queryTaskList(String appId, XPeopleInfo people,
			int firstResult, int maxResults) throws XDocProcException {
		List<AppFlowDef> appFlowDefs = appId_TaskNameMap.get(appId);
		if (appFlowDefs == null) {
			throw new XDocProcException("未知的appId" + appId + ",请检查配置是否正确");
		}
		try {
			List<IBPMTask> result = new ArrayList<IBPMTask>();
			for (AppFlowDef appFlowDef : appFlowDefs) {
				String idCenter=null;
				if(people.getOrgNo()!=null){//自动程序无机构号
				idCenter=publicTools.getBankParam(people.getOrgNo()).getIdCenter();
				}
				BPMUser user = new BPMUser(people.getSid(),idCenter);
				List<IBPMTask> lockedTasks = taskManager.getLockedTaskList(
						appFlowDef.getTaskName(), user);
				for (IBPMTask task : lockedTasks) {
					task.setTagByName(TaskTagKey_ProcessDefKey,
							appFlowDef.getProcessDefKey());
					result.add(task);
				}
				List<IBPMTask> tmpList = taskManager.getFreeTaskList(
						appFlowDef.getTaskName(), user, 0, -1);
				for (IBPMTask task : tmpList) {
					task.setTagByName(TaskTagKey_ProcessDefKey,
							appFlowDef.getProcessDefKey());
					result.add(task);
				}
			}
			return result;

		} catch (Exception e) {
			throw new XDocProcException("查询任务列表是出现异常" + e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yzj.dps.task.common.IAppTaskAdm#lockTask(com.yzj.wf.bpm
	 * .engine.task.IBPMTask, java.lang.String)
	 */
	@Override
	public void lockTask(IBPMTask taskInfo, String userId)
			throws XDocProcException {
		@SuppressWarnings("deprecation")
		String processDefKey = taskInfo.getTagByName(TaskTagKey_ProcessDefKey);
		if (taskManager == null) {
			throw new XDocProcException("未找到锁定该任务需要的任务管理器,流程Key:"
					+ processDefKey);
		}
		try {
			BPMUser user = new BPMUser(userId);
			taskManager.lockTask(taskInfo.getName(), taskInfo.getId(), user);
		} catch (Exception e) {
			logger.warn("锁定任务时发生异常," + e.getMessage());
			throw new XDocProcException("锁定任务时发生异常," + e.getMessage());
		}
	}

	@Override
	public List<IBPMTask> lockTaskList(List<IBPMTask> taskInfos, String userId)
			throws XDocProcException {
		List<IBPMTask> lockedTasks = new ArrayList<IBPMTask>();
		for (IBPMTask taskInfo : taskInfos) {
			@SuppressWarnings("deprecation")
			String processDefKey = taskInfo
					.getTagByName(TaskTagKey_ProcessDefKey);
			if (taskManager == null) {
				throw new XDocProcException("未找到锁定该任务需要的任务管理器,流程Key:"
						+ processDefKey);
			}
			try {
				BPMUser user = new BPMUser(userId);
				taskManager
						.lockTask(taskInfo.getName(), taskInfo.getId(), user);
				lockedTasks.add(taskInfo);
			} catch (Exception e) {
				logger.warn("批量锁定任务时出现异常," + e.getMessage());
			}
		}
		return lockedTasks;
	}

	@Override
	public void unLockTask(IBPMTask taskInfo, String userId)
			throws XDocProcException {
		@SuppressWarnings("deprecation")
		String processDefKey = taskInfo.getTagByName(TaskTagKey_ProcessDefKey);
		if (taskManager == null) {
			throw new XDocProcException("未找到解锁该任务需要的任务管理器,流程Key:"
					+ processDefKey);
		}
		try {
			BPMUser user = new BPMUser(userId);
			taskManager.unLockTask(taskInfo.getName(), taskInfo.getId(), user);
		} catch (Exception e) {
			logger.warn("解锁任务时发生异常," + e.getMessage());
			throw new XDocProcException("解锁任务时发生异常," + e.getMessage());
		}
	}

	@Override
	public List<IBPMTask> unLockTaskList(List<IBPMTask> taskInfos, String userId)
			throws XDocProcException {
		List<IBPMTask> unLockTasks = new ArrayList<IBPMTask>();
		for (IBPMTask taskInfo : taskInfos) {
			try {
				BPMUser user = new BPMUser(userId);
				taskManager.unLockTask(taskInfo.getName(), taskInfo.getId(),
						user);
				unLockTasks.add(taskInfo);
			} catch (Exception e) {
				logger.warn("批量解锁任务时出现异常," + e.getMessage());
			}
		}
		return unLockTasks;
	}

	/**
	 * 获取对账中心额度信息
	 * 
	 * @return
	 */
	private Map<String, Object> getInitVariableMap() throws XDocProcException {
		Map<String, Object> variableMap = new HashMap<String, Object>();
		String[] flowVariableKeys = ProcessVariableKey.getAllValues();
		for (String key : flowVariableKeys) {
			variableMap.put(key, null);
		}
		return variableMap;
	}

	/**
	 * 向流程变量map中新增值
	 * 
	 * @param variableMap
	 *            流程变量map
	 * @param idCenter
	 *            对账中心号
	 * @return
	 */
	private Map<String, Object> addVariableMap(Map<String, Object> variableMap,
			String idCenter) throws XDocProcException {
		// if (idCenter == null || "".equals(idCenter)) { //
		// docSet表里还没有idCenter字段
		// return variableMap;
		// }
		// if (variableMap.get(ProcessVariableKey.CREDIT_MI_INPT) != null) {
		// return variableMap;
		// }
		IdCenterParam pic = publicTools.getParamIdcenter(idCenter);
		CreditParam credit = publicTools.getCreditParam(idCenter);
		variableMap.put(ProcessVariableKey.CREDIT_MI_INPT, // 人工录入额度
				credit.getManualInput());
		variableMap.put(ProcessVariableKey.CREDIT_MI_AUTH, // 录入审核额度
				credit.getManualAuth());
		variableMap.put(ProcessVariableKey.CREDIT_MP_FST, // 人工验印额度
				credit.getManualProve_fst());
		variableMap.put(ProcessVariableKey.CREDIT_MP_SND, // 人工复验额度
				credit.getManualProve_snd());
		variableMap.put(ProcessVariableKey.CREDIT_MP_AUTH, // 主管验印额度
				credit.getManualProve_auth());
		variableMap.put(ProcessVariableKey.SEALTYPE, pic.getSealType()); // 验印模式
		variableMap.put(ProcessVariableKey.NOTMATCHINPUTTYPE,
				pic.getNotMatchInputType()); // 未达录入模式
		return variableMap;
	}

	public IPublicTools getPublicTools() {
		return publicTools;
	}

	public void setPublicTools(IPublicTools publicTools) {
		this.publicTools = publicTools;
	}

	private Integer getDocFlagAndLogInfo(String taskName,
			Map<String, Object> variables, StringBuffer logInfoOut)
			throws XDocProcException {
		try {
			String result = flowExtConfig.getParamByMap(taskName, variables);
			String[] docflagAndloginfo = result.split("[|]");
			if (docflagAndloginfo == null || docflagAndloginfo.length != 2) {
				throw new XDocProcException("流程扩展配置不正确，请检查配置！");
			}
			logInfoOut.append(docflagAndloginfo[1]);
			return Integer.valueOf(docflagAndloginfo[0]);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取DocFlag及日志信息时出错" + e.getMessage());
			throw new XDocProcException(e);
		}
	}

	private void writeDocLog(DocSet docSet, String taskName, String logInfo,
			String userCode) throws XDocProcException {
		DocLog docLog = new DocLog();
		docLog.setDocId(docSet.getDocId());
		docLog.setTaskName(taskName);
		docLog.setLogDesc(logInfo);
		docLog.setIdBank(docSet.getIdBank());
		docLog.setBankName(docSet.getBankName());
		docLog.setVoucherNo(docSet.getVoucherNo());
		docLog.setOpCode(userCode);
		Date date = new Date();
		docLog.setOpDate(sdfYMD.format(date));
		docLogAdm.create(docLog);
		logger.info("写业务日志成功");
	}

	public IBPMRuntimeManager getRuntimeManager() {
		return runtimeManager;
	}

	public void setRuntimeManager(IBPMRuntimeManager runtimeManager) {
		this.runtimeManager = runtimeManager;
	}

	public IDocSetAdm getDocSetAdm() {
		return docSetAdm;
	}

	public void setDocSetAdm(IDocSetAdm docSetAdm) {
		this.docSetAdm = docSetAdm;
	}

	public IParamManager getParamManager() {
		return paramManager;
	}

	public void setParamManager(IParamManager paramManager) {
		this.paramManager = paramManager;
	}

	public IDocLogAdm getDocLogAdm() {
		return docLogAdm;
	}

	public void setDocLogAdm(IDocLogAdm docLogAdm) {
		this.docLogAdm = docLogAdm;
	}

	public String getFlowExtFilePath_cheque() {
		return flowExtFilePath_recovery;
	}

	public void setFlowExtFilePath_cheque(String flowExtFilePath_cheque) {
		this.flowExtFilePath_recovery = flowExtFilePath_cheque;
	}

	public IBPMTaskManager getTaskManager() {
		return taskManager;
	}

	public void setTaskManager(IBPMTaskManager taskManager) {
		this.taskManager = taskManager;
	}

}
