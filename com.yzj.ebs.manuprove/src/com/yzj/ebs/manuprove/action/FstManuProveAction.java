/**
 * 创建于:2012-10-15<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 *
 * @author LiuQF
 * @version 1.0
 */
package com.yzj.ebs.manuprove.action;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.opensymphony.xwork2.ActionSupport;
import com.yzj.ebs.common.IPublicTools;
import com.yzj.ebs.common.IdCenterParam;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.database.DocSet;
import com.yzj.ebs.manuprove.biz.IManuProveBiz;
import com.yzj.ebs.task.common.IAppTaskAdm;
import com.yzj.ebs.task.common.TaskConstDef.AppId;
import com.yzj.ebs.task.common.TaskConstDef.TaskResult;
import com.yzj.wf.am.security.common.AMSecurityDefine;
import com.yzj.wf.bpm.engine.task.IBPMTask;
import com.yzj.wf.common.WFLogger;
import com.yzj.wf.core.model.po.wrapper.XPeopleInfo;

/***
 * 人工初验处理类
 * 
 * @author LiuQF
 * 
 */
public class FstManuProveAction extends ActionSupport {

	private static final long serialVersionUID = 1121842134607719263L;

	private WFLogger logger = WFLogger.getLogger(FstManuProveAction.class);
	private List<String> imgList = new ArrayList<String>();
	private String errMsg;

	private IAppTaskAdm taskAdm;
	private IPublicTools publicTools;
	

	private DocSet docSet;
	private IBPMTask task;
	private String checkTaskId;

	private String userCode;
	private String userId;
	private String idBank;
	private String docId;

	private String sealDBIp;
	private String sealDBPort;
	// 任务提交 “pass” 验印通过，“nopass” 验印不通过，“abandon” 放弃任务，“delete” 删除任务
	private String operType;
	// 验印不过理由（选中的）
	private String curSealAcc;
	private String sealLogStr;
	private String curUReason;
	private String curUReasonCode;
	private String manuProveJson;
	private String[] reasons;

	private ManuProveJson manuProveJsonObj;


	private IManuProveBiz biz;
	/**
	 * 初始化
	 * 
	 */
	public String init() {
		String uReason = null;
		try {
			this.Terminate();
			XPeopleInfo userInfo = (XPeopleInfo) ServletActionContext
					.getRequest().getSession()
					.getAttribute(AMSecurityDefine.XPEOPLEINFO);

			userId = userInfo.getSid();
			userCode = userInfo.getPeopleCode();
			idBank = userInfo.getOrgNo();

			manuProveJsonObj = new ManuProveJson();
			manuProveJsonObj.setOperCode(userCode);

			if (uReason == null || uReason == "") {
				uReason = biz.getUntreadList();
			}
			if (sealDBIp == null || sealDBIp == "") {
				sealDBIp = publicTools.getSysbaseParam("SEALDBIP");

			}
			if (sealDBPort == null || sealDBPort == "") {
				sealDBPort = publicTools.getSysbaseParam("SEALDBPORT");
			}

			manuProveJsonObj.setuReason(uReason);
			manuProveJsonObj.setSealDBIp(sealDBIp);
			manuProveJsonObj.setSealDBPort(sealDBPort);
			return getTask();
		} catch (Exception e) {
			errMsg = "初始化出现错误:";
			logger.error(errMsg, e);
			return "success";
		}

	}

	/**
	 * 获取任务
	 */
	public String getTask() {
		XPeopleInfo userInfo = (XPeopleInfo) ServletActionContext.getRequest()
				.getSession().getAttribute(AMSecurityDefine.XPEOPLEINFO);
		try {
			task = taskAdm.getTask(AppId.MANUALPROVE_FST, userInfo);
			if (task != null) {
				docId = task.getBusinessKey();
				docSet = biz.queryOneByID(Long.valueOf(docId));

				String idCenterNo = docSet.getIdCenter();
				IdCenterParam idCenter = null;
				idCenter = publicTools.getParamIdcenter(idCenterNo);
				if (idCenter == null) {
					throw new XDocProcException("无法获取到机构" + idBank + "所对应的对账中心");
				}
				String frontImg = idCenter.getImageUrl()
						+ docSet.getFrontImagePath();
				String backImg = idCenter.getImageUrl()
						+ docSet.getBackImagePath();
				imgList.add(frontImg);
				imgList.add(backImg);
				manuProveJsonObj.setTaskId(task.getId());
				manuProveJsonObj.setDocDate(docSet.getDocDate());
				manuProveJsonObj.setVoucherNo(docId);
				manuProveJsonObj.setAccNoList(getAccNoList());
				manuProveJsonObj.setImageFile(frontImg);
				manuProveJsonObj.setAccNo(docSet.getSealAccNo());
				manuProveJsonObj.setCredit(docSet.getStrcredit());
				manuProveJsonObj.setAccNo(manuProveJsonObj.getAccNoList()
						.split("[|]")[0]);
			} else {
				errMsg = "没有待处理的初验任务！";
				return "notask";
			}
		} catch (XDocProcException e) {
			errMsg = "获取任务出现错误:" + e.getMessage();
			logger.error(errMsg, e);
			return "success";
		}
		JSONObject a = JSONObject.fromObject(manuProveJsonObj);
		manuProveJson = a.toString();
		return "success";
	}

	public String getTaskByAjax() throws IOException {
		XPeopleInfo userInfo = (XPeopleInfo) ServletActionContext.getRequest()
				.getSession().getAttribute(AMSecurityDefine.XPEOPLEINFO);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		ManuProveJson localManuProveJsonObj = new ManuProveJson();
		try {
			task = taskAdm.getTask(AppId.MANUALPROVE_FST, userInfo);
			if (task != null) {
				docId = task.getBusinessKey();
				docSet = biz.queryOneByID(Long.valueOf(docId));

				String idCenterNo = docSet.getIdCenter();
				IdCenterParam idCenter = null;
				idCenter = publicTools.getParamIdcenter(idCenterNo);
				if (idCenter == null) {
					throw new XDocProcException("无法获取到机构" + idBank + "所对应的对账中心");
				}
				String frontImg = idCenter.getImageUrl()
						+ docSet.getFrontImagePath();
				localManuProveJsonObj.setTaskId(task.getId());
				localManuProveJsonObj.setDocDate(docSet.getDocDate());
				localManuProveJsonObj.setVoucherNo(docId);
				localManuProveJsonObj.setAccNoList(getAccNoList());
				localManuProveJsonObj.setImageFile(frontImg);
				localManuProveJsonObj.setAccNo(docSet.getSealAccNo());
				localManuProveJsonObj.setCredit(docSet.getStrcredit());
				localManuProveJsonObj.setAccNo(localManuProveJsonObj
						.getAccNoList().split("[|]")[0]);
				localManuProveJsonObj.setMessage("success");
			} else {
				localManuProveJsonObj.setMessage("noTask");
			}
		} catch (XDocProcException e) {
			errMsg = "获取任务出现错误:" + e.getMessage();
			logger.error(errMsg, e);
			localManuProveJsonObj.setMessage(errMsg);
		}
		JSONObject json = JSONObject.fromObject(localManuProveJsonObj);
		OutputStream out = null;
		try {
			out = response.getOutputStream();
			response.setContentType("text/html; charset=UTF-8");
			out.write(json.toString().getBytes("UTF-8"));
			out.flush();
		} catch (Exception e) {
			logger.error("获取json对象出现错误", e);
		} finally {
			if (out != null) {
				out.close();
			}
		}
		return null;

	}

	/*
	 * 提交任务
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public String submitTask() throws IOException {
		XPeopleInfo userInfo = (XPeopleInfo) ServletActionContext.getRequest()
				.getSession().getAttribute(AMSecurityDefine.XPEOPLEINFO);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		if (!task.getId().equals(checkTaskId)) {
			response.getWriter().write("err当前提交的任务与后台的任务不相符,可能该是已经被提交");
			return null;
		}
		docSet.setOpCode114(userCode);
		docSet.setProveState(0);
		docSet.setSealAccNo(curSealAcc);
		TaskResult taskresult = TaskResult.SUCCESS;
		if ("pass".equals(operType)) {
			taskresult = TaskResult.SUCCESS;
			docSet.setProveFlag(22);
		} else if ("nopass".equals(operType)) {
			// 设置验印不过理由
			docSet.setProveFlag(Integer.valueOf(curUReasonCode.substring(0,
					curUReasonCode.length() - 1)));
			taskresult = TaskResult.FAIL;
		}
		try {
			insertSealLog();
			taskAdm.commitTask(task, docSet, userInfo, taskresult);
			return null;
		} catch (XDocProcException e) {
			// 在提交失败时需要取消提交
			errMsg = "获取任务出现错误:";
			logger.error(errMsg, e);
			response.getWriter().write("err提交任务失败:" + e.getMessage());
			throw new IOException("提交任务失败");
		}
	}

	/**
	 * 发起删除
	 * 
	 * @return
	 */
	public String toDelete() throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		XPeopleInfo userInfo = (XPeopleInfo) ServletActionContext.getRequest()
				.getSession().getAttribute(AMSecurityDefine.XPEOPLEINFO);
		if (!task.getId().equals(checkTaskId)) {
			response.getWriter().write("err当前提交的任务与后台的任务不相符,可能该是已经被提交");
			return null;
		}
		docSet.setDeleteReasons(reasons);
		try {
			taskAdm.commitTask(task, docSet, userInfo, TaskResult.DELETE);
		} catch (XDocProcException e) {
			logger.error("发起删除失败", e);
			response.getWriter().write("发起删除失败:" + e.getMessage());
		}
		return null;
	}

	/**
	 * 放弃任务
	 * 
	 */
	public String giveUpTask() throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		XPeopleInfo userInfo = (XPeopleInfo) ServletActionContext.getRequest()
				.getSession().getAttribute(AMSecurityDefine.XPEOPLEINFO);
		if (!task.getId().equals(checkTaskId)) {
			response.getWriter().write("err当前提交的任务与后台的任务不相符,可能该是已经被提交");
			return null;
		}
		try {
			taskAdm.giveUpTask(task, userInfo);
		} catch (XDocProcException e) {
			logger.error("放弃任务出现错误", e);
			response.getWriter().write("err放弃任务出现错误");
		}
		return null;
	}

	// 新增验印日志
	private void insertSealLog() throws XDocProcException {
		try {
			biz.create(sealLogStr, docSet, manuProveJsonObj);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 获取账号列表，字符串形式
	 * 
	 */
	private String getAccNoList() {
		try {
			return biz.getAccNoList(docSet, manuProveJsonObj);
		} catch (XDocProcException e) {
			logger.error(e.getMessage());
			return "error";
		}
	}

	

	private void Terminate() {
		imgList.clear();
		task = null;
		errMsg = null;
		docId = null;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public IAppTaskAdm getTaskAdm() {
		return taskAdm;
	}

	public void setTaskAdm(IAppTaskAdm taskAdm) {
		this.taskAdm = taskAdm;
	}

	public String getOperType() {
		return operType;
	}

	public void setOperType(String operType) {
		this.operType = operType;
	}

	public IPublicTools getPublicTools() {
		return publicTools;
	}

	public void setPublicTools(IPublicTools publicTools) {
		this.publicTools = publicTools;
	}

	public String getSealDBIp() {
		return sealDBIp;
	}

	public void setSealDBIp(String sealDBIp) {
		this.sealDBIp = sealDBIp;
	}

	public String getSealDBPort() {
		return sealDBPort;
	}

	public void setSealDBPort(String sealDBPort) {
		this.sealDBPort = sealDBPort;
	}

	public String getCurSealAcc() {
		return curSealAcc;
	}

	public void setCurSealAcc(String curSealAcc) {
		this.curSealAcc = curSealAcc;
	}

	public String getSealLogStr() {
		return sealLogStr;
	}

	public void setSealLogStr(String sealLogStr) {
		this.sealLogStr = sealLogStr;
	}

	public String getCurUReason() {
		return curUReason;
	}

	public void setCurUReason(String curUReason) {
		this.curUReason = curUReason;
	}

	public String getCurUReasonCode() {
		return curUReasonCode;
	}

	public void setCurUReasonCode(String curUReasonCode) {
		this.curUReasonCode = curUReasonCode;
	}

	public ManuProveJson getManuProveJsonObj() {
		return manuProveJsonObj;
	}

	public void setManuProveJsonObj(ManuProveJson manuProveJsonObj) {
		this.manuProveJsonObj = manuProveJsonObj;
	}

	public String getManuProveJson() {
		return manuProveJson;
	}

	public void setManuProveJson(String manuProveJson) {
		this.manuProveJson = manuProveJson;
	}



	public String getCheckTaskId() {
		return checkTaskId;
	}

	public void setCheckTaskId(String checkTaskId) {
		this.checkTaskId = checkTaskId;
	}

	public void setTask(IBPMTask task) {
		this.task = task;
	}

	public String[] getReasons() {
		return reasons;
	}

	public void setReasons(String[] reasons) {
		this.reasons = reasons;
	}
    public IManuProveBiz getBiz() {
		return biz;
	}

	public void setBiz(IManuProveBiz biz) {
		this.biz = biz;
	}
}
