package com.yzj.ebs.task.impl;

/**
 * 创建于:2012-8-22<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 任务节点定位封装
 * 
 * @author WangXue
 * @version 1.0.0
 */
public class AppFlowDef {

	private String processDefKey;
	private String taskName;
	
	public AppFlowDef(String processDefKey, String taskName) {
		super();
		this.processDefKey = processDefKey;
		this.taskName = taskName;
	}

	public String getProcessDefKey() {
		return processDefKey;
	}

	public void setProcessDefKey(String processDefKey) {
		this.processDefKey = processDefKey;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
}
