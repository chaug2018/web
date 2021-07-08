package com.yzj.ebs.auto.tally;

import com.yzj.ebs.auto.ResultBean;
import com.yzj.wf.as.common.IJobRegister;
import com.yzj.wf.as.common.JobLogic;
import com.yzj.wf.as.entity.BusinessInfo;
import com.yzj.wf.bpm.engine.task.IBPMTask;
import com.yzj.ebs.common.IServiceForRemote;
import com.yzj.ebs.common.OperLogModuleDefine;
import com.yzj.ebs.database.DocSet;
import com.yzj.ebs.task.common.TaskConstDef.AppId;
import com.yzj.ebs.task.common.TaskConstDef.TaskResult;

/**
 * 创建于:2012-9-4<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 自动记账逻辑
 * 
 * @author WangXue
 * @edit 秦靖锋
 * @version 1.0.0
 */
public class AutoTally extends JobLogic {

	private  String userId;
	private static final String AutoType = "AUTOTALLY";
	private IJobRegister jobRegister;
	private IServiceForRemote serviceForRemote;

	public void init() throws Exception {
		if (jobRegister != null) {
			jobRegister.addJobLogic(AutoType, this);
		} else {
			throw new Exception("未获取到IJobRegister服务");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yzj.wf.as.common.JobLogic#action()
	 */
	@Override
	public BusinessInfo action() throws Exception {
		try{
			serviceForRemote.debug("开始获取自动记账任务",false);
			IBPMTask task = serviceForRemote.getTask(AppId.AUTO_TALLY, userId);
			if (task != null) {
				String taskId = task.getId();
				long bizKey = Long.valueOf(task.getBusinessKey());
				serviceForRemote.info("1、获取到一笔自动记账任务,taskid:" + taskId + ",bizKey:"
						+ bizKey,String.valueOf(OperLogModuleDefine.autojz));
				serviceForRemote.debug("开始查询docset...",false);
				DocSet docSet = serviceForRemote.queryDocSetByID(bizKey);
				try {
					serviceForRemote.debug("开始记账...",false);
					boolean result = serviceForRemote.tallyDebit(docSet);
					serviceForRemote.info("2、记账结果：" + result,String.valueOf(OperLogModuleDefine.autojz));
					if (result) {
						serviceForRemote.debug("3、记账成功，开始提交任务。。。",String.valueOf(OperLogModuleDefine.autojz));
						serviceForRemote.commitTask(task, docSet, userId,
								TaskResult.SUCCESS);
					} else {
						serviceForRemote.debug("3、记账失败，开始提交任务。。。",String.valueOf(OperLogModuleDefine.autojz));
						return null;
//						serviceForRemote.commitTask(task, docSet, UserId,
//								TaskResult.FAIL);
					}
				} catch (Exception e) {
					e.printStackTrace();
					serviceForRemote.error("自动记账发生异常" + e.getMessage(), e,String.valueOf(OperLogModuleDefine.autojz));
					return null;
//					serviceForRemote.commitTask(task, docSet, UserId,
//							TaskResult.FAIL);
				}
				String branchNo = docSet.getIdBank();
				String voucherNo = docSet.getVoucherNo();
				String credit = docSet.getStrcredit();
				ResultBean resultBean = new ResultBean(bizKey, voucherNo,
						branchNo, credit);
				BusinessInfo businessInfo = new BusinessInfo(AutoType, true,
						resultBean, "");
				return businessInfo;
			} else {
				serviceForRemote.debug("未获取到自动记账任务",false);
				return null;
			}
		}catch(Throwable e){
			serviceForRemote.error("自动记账超时",e,false);
			return null;
		}
	}

	public IJobRegister getJobRegister() {
		return jobRegister;
	}

	public void setJobRegister(IJobRegister jobRegister) {
		this.jobRegister = jobRegister;
	}

	public IServiceForRemote getServiceForRemote() {
		return serviceForRemote;
	}

	public void setServiceForRemote(IServiceForRemote serviceForRemote) {
		this.serviceForRemote = serviceForRemote;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	

}
