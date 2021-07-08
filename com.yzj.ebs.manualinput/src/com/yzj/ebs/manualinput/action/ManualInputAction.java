package com.yzj.ebs.manualinput.action;

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
import com.yzj.ebs.database.AccNoMainData;
import com.yzj.ebs.database.CheckMainData;
import com.yzj.ebs.database.DocSet;
import com.yzj.ebs.manualinput.biz.IManualBiz;
import com.yzj.ebs.manualinput.param.ManualInputInfo;
import com.yzj.ebs.task.common.IAppTaskAdm;
import com.yzj.ebs.task.common.TaskConstDef.AppId;
import com.yzj.ebs.task.common.TaskConstDef.TaskResult;
import com.yzj.wf.am.security.common.AMSecurityDefine;
import com.yzj.wf.bpm.engine.task.IBPMTask;
import com.yzj.wf.common.WFLogger;
import com.yzj.wf.core.model.po.wrapper.XPeopleInfo;

/**
 * 创建于:2012-9-19<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 数据补录action
 * 
 * @author 陈林江
 * @version 1.0
 */
public class ManualInputAction extends ActionSupport {

	private WFLogger logger = WFLogger.getLogger(ManualInputAction.class);

	private static final long serialVersionUID = -7358905128405053152L;

	private IAppTaskAdm taskAdm;
	private IPublicTools publicTools;
	private IManualBiz biz;

	private String[] reasons;
	private DocSet docSet;
	private IBPMTask task;
	private String errMsg;
	private String inputInfo;
	private String billNo; // 账单编号，二维码识别失败时的账号查询
	private String taskId;
	private String checkTaskId;
	private String docId;
	private String checkResult;
	private List<String> imgList = new ArrayList<String>(); // 影像列表
	private List<AccNoMainData> accNolist = new ArrayList<AccNoMainData>();
	private String shortcutId;
	private String billNoText;

	/***
	 * 初始化列表
	 */
	public String init() {
		this.clean();
		XPeopleInfo userInfo = (XPeopleInfo) ServletActionContext.getRequest()
				.getSession().getAttribute(AMSecurityDefine.XPEOPLEINFO);

		String idBank = userInfo.getOrgNo();
		ManualInputInfo info = new ManualInputInfo();
		try {
			task = taskAdm.getTask(AppId.MANUAL_INPUT, userInfo);
			if (task == null) {
				return "noTask";
			} else {
				taskId = task.getId();
				docId = task.getBusinessKey();
				docSet = biz.queryOneByID(Long.valueOf(docId));
				if (docSet == null) {
					throw new XDocProcException("未获取到流程任务中指定的业务记录");
				}
				reasons = docSet.getReInputReasons();
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
				String billNo = docSet.getVoucherNo(); // 账单编号
				if (billNo != null && !"".equals(billNo)
						&& (reasons == null || reasons.length == 0)) {
					accNolist = biz.getAccnoMainDataByVoucherNo(docSet
							.getVoucherNo());
					double totalCredit = 0;
					for (int i = 0; i < accNolist.size(); i++) {
						AccNoMainData data = accNolist.get(i);
						if (data.getCredit() != null) {
							totalCredit += data.getCredit();
						}
					}
					docSet.setCredit(totalCredit);
					info = biz.getManualInputInfo(accNolist, info);
					info.setBillNo(billNo);
				}
				info.setDocId(docId);
				JSONObject json = JSONObject.fromObject(info);
				inputInfo = json.toString();
				return "initSuccess";
			}
		} catch (XDocProcException e) {
			errMsg = "获取任务出现错误:";
			logger.error(errMsg, e);
			return "initSuccess";
		}
	}

	/**
	 * 提交任务
	 * 
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public String submitTask() throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		XPeopleInfo userInfo = (XPeopleInfo) ServletActionContext.getRequest()
				.getSession().getAttribute(AMSecurityDefine.XPEOPLEINFO);
		if (!task.getId().equals(checkTaskId)) {
			response.getWriter().write("err当前提交的任务与后台的任务不相符,可能该是已经被提交");
			return null;
		}
		try {
			boolean needNotmatch = false;
			for (int i = 0; i < accNolist.size(); i++) {
				if (i == 5) {
					break;
				}
				AccNoMainData data = accNolist.get(i);
				String checkFlag = checkResult.substring(i, i + 1);
				data.setCheckFlag(checkFlag);
				if ("2".equals(checkFlag)) { // 有不相符的
					needNotmatch = true;
				}
				// 设置Description字段
				try {
					docSet.setDescItem("accno_" + i, data.getAccNo());
					docSet.setDescItem("cridet_" + i, String.valueOf(data.getCredit()));
					docSet.setDescItem("checkflag_" + i, data.getCheckFlag());
				} catch (Exception e) {
					response.getWriter().write("err更新任务信息出现异常");
					return null;
				}
			}
			if (needNotmatch) { // 设置需要做未达的标志
				docSet.setNeedNotMatch((short) 1);
			}
			CheckMainData data = biz.getOneByVoucherNo(billNo);
			docSet.setDocDate(data.getDocDate());
			docSet.setAccName(data.getAccName());
			docSet.setBankName(data.getBankName());
			docSet.setVoucherNo(billNo);
			docSet.setOpCode111(userInfo.getPeopleCode());
			biz.updateCheckMainDataByDocSet(data, docSet);
			biz.batchUpdate(accNolist);

			taskAdm.commitTask(task, docSet, userInfo, TaskResult.SUCCESS);
		} catch (XDocProcException e) {
			response.getWriter().write("err提交任务失败:" + e.getMessage());
			return null;
		}
		this.clean();
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

	/**
	 * 发起删除
	 * 
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
	 * 查询账号信息
	 * 
	 */
	public String getData() throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		ManualInputInfo info = new ManualInputInfo();
		// TODO guoyj
		try {
			accNolist = biz.getAccnoMainDataByVoucherNo(billNo);
			if (accNolist != null && accNolist.size() > 0) {
				CheckMainData data = biz.getOneByVoucherNo(billNo);
				if (data != null) {
					if (data.getIdCenter().equals(docSet.getIdCenter())) {

						double totalCredit = 0;
						for (int i = 0; i < accNolist.size(); i++) {
							AccNoMainData accNoMaindata = accNolist.get(i);
							if (accNoMaindata.getCredit() != null) {
								totalCredit += accNoMaindata.getCredit();
							}
						}
						docSet.setCredit(totalCredit);

						info = biz.getManualInputInfo(accNolist, info);
						docSet.setIdBank(data.getIdBank());
						docSet.setIdBranch(data.getIdBranch());
					} else {
						logger.warn("该账单编号(" + billNo
								+ ")对应的数据不属于本对账中心，无法进行补录操作");
						info.setErrMsg("该账单编号对应的数据不属于本对账中心，无法进行补录操作");
					}
				}

			}
			info.setBillNo(billNo);
			info.setDocId(docId);
			JSONObject json = JSONObject.fromObject(info);
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
		} catch (XDocProcException e) {
			logger.error("查询该账单编号对应的账号信息出现异常", e);
			response.getWriter().write("err查询该账单编号对应的账号信息出现异常");
			return null;
		}
	}


	private void clean() {
		imgList.clear();
		task = null;
		errMsg = null;
		inputInfo = null;
		docId = null;
		checkResult = null;
		accNolist.clear();
		reasons = null;
		billNo = null;
	}

	public List<String> getImgList() {
		return imgList;
	}

	public void setImgList(List<String> imgList) {
		this.imgList = imgList;
	}

	public String getInputInfo() {
		return inputInfo;
	}

	public void setInputInfo(String inputInfo) {
		this.inputInfo = inputInfo;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public IAppTaskAdm getTaskAdm() {
		return taskAdm;
	}

	public void setTaskAdm(IAppTaskAdm taskAdm) {
		this.taskAdm = taskAdm;
	}

	public IPublicTools getPublicTools() {
		return publicTools;
	}

	public void setPublicTools(IPublicTools publicTools) {
		this.publicTools = publicTools;
	}

	public DocSet getDocSet() {
		return docSet;
	}

	public void setDocSet(DocSet docSet) {
		this.docSet = docSet;
	}

	public IBPMTask getTask() {
		return task;
	}

	public void setTask(IBPMTask task) {
		this.task = task;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	public String getCheckResult() {
		return checkResult;
	}

	public void setCheckResult(String checkResult) {
		this.checkResult = checkResult;
	}

	public String[] getReasons() {
		return reasons;
	}

	public void setReasons(String[] reasons) {
		this.reasons = reasons;
	}

	public String getCheckTaskId() {
		return checkTaskId;
	}

	public void setCheckTaskId(String checkTaskId) {
		this.checkTaskId = checkTaskId;
	}

	public IManualBiz getBiz() {
		return biz;
	}

	public void setBiz(IManualBiz biz) {
		this.biz = biz;
	}

}
