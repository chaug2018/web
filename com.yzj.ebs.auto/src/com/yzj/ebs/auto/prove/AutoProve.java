package com.yzj.ebs.auto.prove;

import java.util.HashMap;
import java.util.List;

import com.yzj.ebs.auto.DownImage;
import com.yzj.ebs.auto.JacobLoader;
import com.yzj.ebs.auto.ResultBean;
import com.yzj.ebs.auto.Synchronizer;
import com.yzj.ebs.auto.verifydata.DeleteFileUtils;
import com.yzj.ebs.common.IServiceForRemote;
import com.yzj.ebs.common.OperLogModuleDefine;
import com.yzj.ebs.database.DocSet;
import com.yzj.ebs.task.common.TaskConstDef.AppId;
import com.yzj.ebs.task.common.TaskConstDef.TaskResult;
import com.yzj.wf.as.common.IJobRegister;
import com.yzj.wf.as.common.JobLogic;
import com.yzj.wf.as.entity.BusinessInfo;
import com.yzj.wf.bpm.engine.task.IBPMTask;
import com.yzj.wf.pam.common.IParamManager;

/**
 * 创建于:2012-8-18<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 自动验印
 * 
 * @author 秦靖锋， WangXue
 * @version 1.0.0
 */
public class AutoProve extends JobLogic {

	private String userId;
	private static final String AutoType = "AUTOPROVE";
	private static final String ControlName = "SealForAuto.CSealCtrl";
	private static final String MethodName = "SealByImage";
	private static final String ParamGroupName = "DOWNPARAM";
	private static final String ParamName_ip = "ip";
	private static final String ParamName_port = "port";
	private static final String ParamName_imageLocalPath = "imageLocalPath";
	private static final String ParamName_imageServer = "imageServer";

	private IJobRegister jobRegister;
	private IServiceForRemote serviceForRemote;
	private IParamManager paramManager;
	private DownImage downImage;

	public void init() throws Exception {	
		if (jobRegister != null) {
			jobRegister.addJobLogic("AUTOPROVE", this);
			String ip = paramManager
					.getParamByName(ParamGroupName, ParamName_ip)
					.getParamValue().toString();
			String port = paramManager
					.getParamByName(ParamGroupName, ParamName_port)
					.getParamValue().toString();
			String imageLocalPath = paramManager
					.getParamByName(ParamGroupName, ParamName_imageLocalPath)
					.getParamValue().toString();
			String imageServer = paramManager
					.getParamByName(ParamGroupName, ParamName_imageServer)
					.getParamValue().toString();
			downImage = new DownImage(ip, Integer.valueOf(port),
					imageLocalPath, imageServer);
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
		try {
			serviceForRemote.debug("开始获取验印任务...",false);
			IBPMTask task = serviceForRemote.getTask(AppId.AUTO_PROVE, userId);
			JacobLoader loader = null;
			boolean result = false;
			String strFileName = null;
			if (task != null) {
				synchronized (Synchronizer.simpleSynchronizer) {
					String taskId = task.getId();
					long bizKey = Long.valueOf(task.getBusinessKey());
					serviceForRemote.info("1、获取到一笔自动验印任务,taskid:" + taskId + ",bizKey:"
							+ bizKey,String.valueOf(OperLogModuleDefine.autoYj));
					serviceForRemote.debug("开始查询docset...",false);
					DocSet docSet = serviceForRemote.queryDocSetByID(bizKey);
					String branchNo = docSet.getIdBank();
					String voucherNo = docSet.getVoucherNo();
					String credit = docSet.getStrcredit();
					try {
						String imageUrl = docSet.getFrontImagePath();
						serviceForRemote.debug("开始下载影像...",false);
						strFileName = downImage.downImage(imageUrl);
						// 根据账单编号去查出该客户号下的所有账户
						serviceForRemote.debug("开始获取验印账号...",false);
						List<Object[]> listAccno = serviceForRemote
								.getSealAccno(docSet.getVoucherNo());
						// 组装下查询结果，记录验印模式，再到验印库去查询是否有记录
						// 把账号及验印模式丢到MAP中
						HashMap<String, String> sealAccno = new HashMap<String, String>();
						String findSql = "";
						if (listAccno.size() > 0) {
							for (int i = 0; i < listAccno.size(); i++) {
								String sealaccno = listAccno.get(i)[0]
										.toString();
								String sealmode = listAccno.get(i)[1] == null ? "0"
										: listAccno.get(i)[1].toString();// 若验印模式为空则返回0，验印全部预留印鉴
								sealAccno.put(sealaccno, sealmode);
								
								docSet.setSealAccNo(sealaccno);
								
								findSql = findSql == "" ? "'" + sealaccno + "'"
										: findSql + ",'" + sealaccno + "'";
							}
							findSql = "select accno from zl where accno in ("
									+ findSql + ")";
							serviceForRemote.debug("2、开始从验印库获取账号信息...",String.valueOf(OperLogModuleDefine.autoYj));
							List<Object> listSealAccno = serviceForRemote
									.checkSealAccno(findSql);
							Object[] param;
							loader = new JacobLoader();
							if (loader.load(ControlName)) {
								// 循环listBasicInfo，取出该账单编号下的客户下的所有账户的账号进行验印。
								if (listSealAccno.size() > 0) {
									serviceForRemote.info("3、开始进行自动验印...",String.valueOf(OperLogModuleDefine.autoYj));
									for (int i = 0; i < listSealAccno.size(); i++) {
										String sealaccno = listSealAccno.get(i)
												.toString();
										// 图像绝对路径，账号，操作员代码，金额，任务号,验印模式,凭证号，凭证日期
										param = new Object[] {
												strFileName.trim(), sealaccno,
												"Auto", credit, "1",
												sealAccno.get(sealaccno), 1 };
										String value = loader.doMethod(
												MethodName, param);
										if (value.equals("1")) {
											result = true;
										}
										docSet.setSealAccNo(sealaccno);
										if (result) {
											break;// 验印成功后退出
										}
									}
								}
								if (result) {
									docSet.setProveFlag(20);
									serviceForRemote.error("验印成功，开始提交任务。。。",false);
									serviceForRemote.commitTask(task, docSet,
											userId, TaskResult.SUCCESS);
									serviceForRemote.info("4、自动验印通过,taskid:" + taskId
											+ ",bizKey:" + bizKey,String.valueOf(OperLogModuleDefine.autoYj));
								} else {
									serviceForRemote.error("验印失败，开始提交任务。。。",false);
									serviceForRemote.commitTask(task, docSet,
											userId, TaskResult.FAIL);
									serviceForRemote.info("4、自动验印未通过,taskid:" + taskId
											+ ",bizKey:" + bizKey,String.valueOf(OperLogModuleDefine.autoYj));
								}
							} else {
								serviceForRemote.error("加载自动验印控件失败:" + ControlName,String.valueOf(OperLogModuleDefine.autoYj));
								throw new Exception("加载自动验印控件失败:" + ControlName);
							}
						} else {// 找不到账号时，也直接验印失败
							serviceForRemote.error("验印失败，开始提交任务。。。",false);
							serviceForRemote.commitTask(task, docSet, userId,
									TaskResult.FAIL);
							serviceForRemote.info("未找到验印账号，自动验印结果为不通过,taskid:" + taskId
									+ ",bizKey:" + bizKey,String.valueOf(OperLogModuleDefine.autoYj));
						}
						final ResultBean resultBean = new ResultBean(bizKey,
								voucherNo, branchNo, credit);
						// 封装转给页面的对象
						final BusinessInfo businessInfo = new BusinessInfo(
								AutoType, result, resultBean, "");
						return businessInfo;
					} catch (Exception e1) {// 异常时直接算为验印不通过
						serviceForRemote.error("发生异常" + e1.getMessage(),e1,String.valueOf(OperLogModuleDefine.autoYj));
						serviceForRemote.error("验印失败，开始提交任务。。。",false);
						serviceForRemote.commitTask(task, docSet, userId,
								TaskResult.FAIL);
						final ResultBean resultBean = new ResultBean(bizKey,
								voucherNo, branchNo, credit);
						// 封装转给页面的对象
						final BusinessInfo businessInfo = new BusinessInfo(
								AutoType, false, resultBean, "");
						return businessInfo;
					} finally {
						if (loader != null) {
							loader.release();
						}
						if(strFileName!=null){
							DeleteFileUtils.DeleteFile(strFileName);
						}
					}
				}

			} else {
				serviceForRemote.debug("未获取到自动验印任务",false);
				return null;
			}
		} catch (Throwable e) {
			serviceForRemote.error("自动验印超时", e,false);
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

	public IParamManager getParamManager() {
		return paramManager;
	}

	public void setParamManager(IParamManager paramManager) {
		this.paramManager = paramManager;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
