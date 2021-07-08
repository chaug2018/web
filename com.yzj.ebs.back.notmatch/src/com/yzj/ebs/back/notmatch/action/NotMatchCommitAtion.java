package com.yzj.ebs.back.notmatch.action;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.yzj.ebs.back.notmatch.biz.INotMatchBiz;
import com.yzj.ebs.common.IPublicTools;
import com.yzj.ebs.common.IdCenterParam;
import com.yzj.ebs.common.RefTableTools;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.database.DocSet;
import com.yzj.ebs.database.NotMatchTable;
import com.yzj.ebs.task.common.IAppTaskAdm;
import com.yzj.ebs.task.common.TaskConstDef.AppId;
import com.yzj.ebs.task.common.TaskConstDef.TaskResult;
import com.yzj.wf.am.security.common.AMSecurityDefine;
import com.yzj.wf.bpm.engine.task.IBPMTask;
import com.yzj.wf.common.WFLogger;
import com.yzj.wf.core.model.po.wrapper.XPeopleInfo;

/**
 * 创建于:2012-9-20<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 未达项录入处理类
 * 
 * @author ShiJiangmin
 * @version 1.0.0
 */
public class NotMatchCommitAtion extends ActionSupport {
	private static final long serialVersionUID = -1937796798142384974L;
	private static SimpleDateFormat matter1 = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private static WFLogger logger = WFLogger
			.getLogger(NotMatchCommitAtion.class);

	private IAppTaskAdm appTaskAdm;
	private IPublicTools publicTools;
	private NotMatchTable firstNotMatch; // 用于显示详细默认信息

	private IBPMTask task;
	private String checkTaskId;
	private String docId;
	private DocSet docSet;
	private String voucherNo;
	private String errMsg;
	private String nowDate; // 工作日期
	private List<String> imgList = new ArrayList<String>();

	private String[] reasons; //

	private List<NotMatchTable> notMatchList = new ArrayList<NotMatchTable>();
	private TreeMap<String, String> refDirectionMap = new TreeMap<String, String>();
	private TreeMap<String, String> refResultMap = new TreeMap<String, String>();
	private String userId;
	private XPeopleInfo userInfo;
	private String idBank;
	private String userCode;
	private static final String NOTMATCHAUTHFLAG_COMMIT = "1"; // 复核通过
	private static final String NOTMATCHAUTHFLAG_BACK = "2"; // 复核退回等待重录
	private static final String NOTMATCHAUTHFLAG_DELETE = "3"; // 复核删除等待确认或重录
	private INotMatchBiz biz;
	
	/**
	 * 初始化界面
	 * 
	 * @return
	 */
	public String init() {

		clean();

		Date dt = new Date();
		SimpleDateFormat matter1 = new SimpleDateFormat("yyyy-MM-dd");
		nowDate = matter1.format(dt).toString();

		userInfo = (XPeopleInfo) ServletActionContext.getRequest().getSession()
				.getAttribute(AMSecurityDefine.XPEOPLEINFO);
		userCode = userInfo.getPeopleCode();
		userId = userInfo.getSid();
		idBank = userInfo.getOrgNo();
		try {
			task = appTaskAdm.getTask(AppId.NOTMATCH_AUTH, userInfo);
			if (task == null) {
				return "noTask";
			} else {
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
				
				if(!frontImg.contains("null") && !backImg.contains("null")){
					imgList.add(frontImg);
					imgList.add(backImg);
				}
				
				notMatchList = biz.getNotMatchListByDocId(docId);
				firstNotMatch = notMatchList.get(0);
				return "initSuccess";
			}
		} catch (XDocProcException e) {
			errMsg = "获取任务出现错误：" + e.getMessage();
			logger.error(errMsg, e);
			return "initSuccess";
		}
	}

	/**
	 * 提交任务
	 * 
	 */
	public String submmitTask() throws IOException {

		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		if (!task.getId().equals(checkTaskId)) {
			response.getWriter().write("err当前提交的任务与后台的任务不相符,可能该是已经被提交");
			return null;
		}
		try {
			for (NotMatchTable notMatchTable : notMatchList) {
				// 审核提交时保持最终状态
				notMatchTable.setFinalCheckFlag(notMatchTable.getCheckFlag());
			}
			// 修改复核相关字段并更新数据库
			updateNotMatchList(NOTMATCHAUTHFLAG_COMMIT);
			biz.updateNotMatchList(getNotMatchList());
			// 更新checkflag字段
			biz.updateAccnoMainData(getNotMatchList(),docSet);
			// 设置未达标志，0：无需做未达 1：需要做未达
			getDocSet().setNeedNotMatch((short) 0);
			getAppTaskAdm().commitTask(task, getDocSet(), getUserInfo(),
					TaskResult.SUCCESS);
		} catch (XDocProcException e) {
			logger.error("提交任务失败", e);
			response.getWriter().write("err提交任务失败:" + e.getMessage());
		}
		clean();
		return null;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	/**
	 * 未达录入时退回录入
	 * 
	 */
	public String sendBack() throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		if (!task.getId().equals(checkTaskId)) {
			response.getWriter().write("err当前提交的任务与后台的任务不相符,可能该是已经被提交");
			return null;
		}
		try {
			// 更新录入状态
			updateNotMatchList(NOTMATCHAUTHFLAG_BACK);
			biz.updateNotMatchList(getNotMatchList());
			// 设置重录理由
			getDocSet().setReInputReasons(getReasons());
			getAppTaskAdm().commitTask(getTask(), getDocSet(), getUserInfo(),
					TaskResult.FAIL);
		} catch (XDocProcException e) {
			logger.error("退回未达项录入失败", e);
			response.getWriter().write("err提交任务失败:" + e.getMessage());
		}
		clean();
		return null;
	}

	/**
	 * 放弃任务
	 * 
	 * @return
	 * @throws IOException
	 */
	public String abandonTask() throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		if (!task.getId().equals(checkTaskId)) {
			response.getWriter().write("err当前提交的任务与后台的任务不相符,可能该是已经被提交");
			return null;
		}
		try {
			appTaskAdm.giveUpTask(task, userInfo);
		} catch (XDocProcException e) {
			logger.error("放弃任务出现错误", e);
			response.getWriter().write("err放弃任务出现错误");
		}
		clean();
		return null;
	}

	/**
	 * 发起删除任务
	 * 
	 * @return
	 * @throws IOException
	 */
	public String sendDelete() throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		if (!task.getId().equals(checkTaskId)) {
			response.getWriter().write("err当前提交的任务与后台的任务不相符,可能该是已经被提交");
			return null;
		}
		try {
			// 更新删除状态
			updateNotMatchList(NOTMATCHAUTHFLAG_DELETE);
			biz.updateNotMatchList(getNotMatchList());
			// 设置删除理由
			getDocSet().setDeleteReasons(getReasons());
			getAppTaskAdm().commitTask(getTask(), getDocSet(), getUserInfo(),
					TaskResult.DELETE);

		} catch (XDocProcException e) {
			logger.error("发起删除失败", e);
			response.getWriter().write("发起删除失败:" + e.getMessage());
		}
		clean();
		return null;
	}

	/**
	 * 在任务提交前记录未达项列表的复核柜员、复核时间以及状态
	 * 
	 * @param authFlag
	 *            复核处理的状态标志
	 */
	public void updateNotMatchList(String authFlag) {
		String opDate = matter1.format(new Date()).toString();
		for (NotMatchTable notMatchTable : notMatchList) {
			// 设置checkDesc
			notMatchTable.setCheckOpCode(userCode);
			notMatchTable.setCheckOpTime(opDate);
			notMatchTable.setAuthFlag(authFlag);
		}

	}

	

	/**
	 * 清空属性
	 */
	public void clean() {
		imgList.clear();
		task = null;
		errMsg = null;
		docId = null;
		docSet = null;
		voucherNo = null;

		nowDate = null;
		notMatchList.clear();
	}

	public static WFLogger getLogger() {
		return logger;
	}

	public static void setLogger(WFLogger logger) {
		NotMatchCommitAtion.logger = logger;
	}

	public IAppTaskAdm getAppTaskAdm() {
		return appTaskAdm;
	}

	public void setAppTaskAdm(IAppTaskAdm appTaskAdm) {
		this.appTaskAdm = appTaskAdm;
	}

	public IPublicTools getPublicTools() {
		return publicTools;
	}

	public void setPublicTools(IPublicTools publicTools) {
		this.publicTools = publicTools;
	}

	public TreeMap<String, String> getRefDirectionMap() {
		if (null == refDirectionMap || refDirectionMap.size() == 0) {
			refDirectionMap = RefTableTools.ValRefDirectionMap;
		}
		return refDirectionMap;
	}

	public void setRefDirectionMap(TreeMap<String, String> refDirectionMap) {
		this.refDirectionMap = refDirectionMap;
	}

	/**
	 * 对未达项余额状态下拉框进行初始化， 只取REF表中两项:余额核对相符（4）、余额核对不符（5）
	 * 
	 * @return 余额状态Map
	 */
	public TreeMap<String, String> getRefResultMap() {
		if (null == refResultMap || refResultMap.size() == 0) {
			refResultMap = RefTableTools.ValRefCheckflagMap;
		}
		return refResultMap;
	}

	public void setRefResultMap(TreeMap<String, String> refResultMap) {
		this.refResultMap = refResultMap;
	}

	public IBPMTask getTask() {
		return task;
	}

	public void setTask(IBPMTask task) {
		this.task = task;
	}

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	public DocSet getDocSet() {
		return docSet;
	}

	public void setDocSet(DocSet docSet) {
		this.docSet = docSet;
	}

	public List<String> getImgList() {
		return imgList;
	}

	public void setImgList(List<String> imgList) {
		this.imgList = imgList;
	}

	public String getVoucherNo() {
		return voucherNo;
	}

	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}

	public List<NotMatchTable> getNotMatchList() {
		return notMatchList;
	}

	public void setNotMatchList(List<NotMatchTable> notMatchList) {
		this.notMatchList = notMatchList;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public NotMatchTable getFirstNotMatch() {
		return firstNotMatch;
	}

	public void setFirstNotMatch(NotMatchTable firstNotMatch) {
		this.firstNotMatch = firstNotMatch;
	}

	public String getNowDate() {
		return nowDate;
	}

	public void setNowDate(String nowDate) {
		this.nowDate = nowDate;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public XPeopleInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(XPeopleInfo userInfo) {
		this.userInfo = userInfo;
	}

	// public String getTaskCount() {
	// return taskCount;
	// }
	//
	// public void setTaskCount(String taskCount) {
	// this.taskCount = taskCount;
	// }

	public String getIdBank() {
		return idBank;
	}

	public void setIdBank(String idBank) {
		this.idBank = idBank;
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
	public INotMatchBiz getBiz() {
		return biz;
	}

	public void setBiz(INotMatchBiz biz) {
		this.biz = biz;
	}

}
