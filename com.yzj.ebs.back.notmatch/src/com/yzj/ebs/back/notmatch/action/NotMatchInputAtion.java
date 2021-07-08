package com.yzj.ebs.back.notmatch.action;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.yzj.ebs.back.notmatch.biz.INotMatchBiz;
import com.yzj.ebs.common.INotMatchTableAdm;
import com.yzj.ebs.common.IPublicTools;
import com.yzj.ebs.common.IdCenterParam;
import com.yzj.ebs.common.RefTableTools;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.database.AccNoMainData;
import com.yzj.ebs.database.DocSet;
import com.yzj.ebs.database.NotMatchTable;
import com.yzj.ebs.impl.NotMatchTableAdm;
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
public class NotMatchInputAtion extends ActionSupport {

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	private static final long serialVersionUID = 6593622800146015898L;
	private static final String NOTMATCHAUTHFLAG_INPUTED = "0"; // 复核结果标志，录入完成未复核
	private static final SimpleDateFormat matter1 = new SimpleDateFormat(
			"yyyy-MM-dd");

	private IAppTaskAdm appTaskAdm;
	private IPublicTools publicTools;
	private INotMatchTableAdm notMatchTableAdm;

	private static WFLogger logger = WFLogger
			.getLogger(NotMatchInputAtion.class);
	private XPeopleInfo userInfo; // 人员信息
	private String userCode;
	private String userId; // 人员编号
	private String idBank; // 行号
	private String idBranch; // 上级管理行
	private String idCenterNo; // 对账中心
	private String bankName; // 机构名称
	private String voucherNo; // 账单编号
	private String docDate; // 对账日期
	private DocSet docSet; // 业务流水
	private String docId; // 业务流水号
	private String nowDate; // 工作日期
	private String[] reasons;
	private String accNo;

	private String notMatchData;// 对账单不符项数据,每条记录以","分隔

	private List<NotMatchTable> notMatchList = new ArrayList<NotMatchTable>(); // 未达项列表
	private List<String> imgList = new ArrayList<String>();;// 图片列表

	private IBPMTask task; // 当前任务
	private String checkTaskId;
	protected TreeMap<String, String> refDirectionMap = new TreeMap<String, String>();
	protected TreeMap<String, String> refResultMap = new TreeMap<String, String>();
	private String errMsg;
	private String taskcount;
	private String authInfo;
	private int lineCount = 1;
	private List<NotMatchTable> notMatchList_his = new ArrayList<NotMatchTable>(); // 历史未达列表，非退回任务则为空
	private INotMatchBiz biz;
	

	Map<String, String> accnoMap = new HashMap<String, String>();

	/**
	 * 初始化界面
	 * 
	 * @return
	 * @throws IOException
	 */
	public String init() {
		clean();
		// 初始化一些页面信息
		getRefDirectionMap();
		getRefResultMap();
		idBank = ""; // 行号
		idBranch = ""; // 上级管理行
		idCenterNo = ""; // 对账中心
		nowDate = matter1.format(new Date()).toString();
		userInfo = (XPeopleInfo) ServletActionContext.getRequest().getSession()
				.getAttribute(AMSecurityDefine.XPEOPLEINFO);
		userId = userInfo.getSid();
		userCode = userInfo.getPeopleCode();
		idBank = userInfo.getOrgNo();
		String checkDesc; // 复核备注
		String checkOpCode; // 复核人员
		String opTime; // 复核时间

		accnoMap.clear();

		try {

			task = appTaskAdm.getTask(AppId.NOTMATCH_INPUT, userInfo);
			if (task == null) {
				return "noTask";
			} else {
				docId = task.getBusinessKey();
				docSet = biz.queryOneByID(Long.valueOf(docId));
				billinfoInit();
				// 根据账单编号找其对应的账号
				List<AccNoMainData> docList = biz
						.getAccnoMainDataByVoucherNo(voucherNo);
				if (docList != null && docList.size() > 0) {
					for (AccNoMainData accNoMainData : docList) {
						accnoMap.put(accNoMainData.getAccNo(),
								accNoMainData.getAccNo());
						accnoMap.put(accNoMainData.getSingleAccno(),
								accNoMainData.getSingleAccno());
					}
				}
				
				/*
				 * 根据docID获取当前回收对账单的不符明细：专用于获取网银对账结果后创建的未达录入任务
				 */
				notMatchList = notMatchTableAdm.getNotMatchListByDocId(docId);

				IdCenterParam idCenter = null;
				if (idCenterNo == null || "".equals(idCenterNo)) { // 匹配不成功
					idCenterNo = publicTools.getBankParam(idBank).getIdCenter();
					idCenter = publicTools.getParamIdcenter(idCenterNo);
				} else {
					idCenter = publicTools.getParamIdcenter(idCenterNo);
				}
				if (idCenter == null) {
					throw new XDocProcException("无法获取到机构" + idBank + "所对应的对账中心");
				}
				docSet.setIdCenter(idCenter.getIdCenterNo());

				String frontImg = idCenter.getImageUrl()
						+ docSet.getFrontImagePath();
				String backImg = idCenter.getImageUrl()
						+ docSet.getBackImagePath();

				if(!frontImg.contains("null") && !backImg.contains("null")){
					imgList.add(frontImg);
					imgList.add(backImg);
				}

				// 根据docId查询notMatchTable，确认该笔任务是否来自删除审核退回或未达审核退回
				if (!isTaskBack(docId)) {
					authInfo = null;
					return "initSuccess";
				} else {
					checkDesc = docSet.getReInputReason();
					if (checkDesc == null || checkDesc.trim().length() == 0) {
						checkDesc = "无";
					}
					checkOpCode = notMatchList.get(0).getCheckOpCode();
					opTime = notMatchList.get(0).getCheckOpTime();
					authInfo = "该笔任务为退回任务 > 复核柜员： [" + checkOpCode
							+ "] 复核时间： [" + opTime + "] 备注： [" + checkDesc
							+ "]";
					return "initSuccess";
				}

			}
		} catch (XDocProcException e) {
			setErrMsg("获取任务出现错误:" + e.getMessage());
			logger.error(errMsg, e);
			return null;
		}
	}

	public String confirmTask() throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		if (StringUtils.isEmpty(accNo) || accnoMap.get(accNo.trim()) == null) {
			response.getWriter().write("1");
			return null;
		} else {
			response.getWriter().write("2");
			return null;
		}
	}

	/**
	 * 提交任务
	 * 
	 * @return
	 * @throws IOException
	 */
	public String submmitTask() throws IOException {

		notMatchList.clear();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		if (task == null && !task.getId().equals(checkTaskId)) {
			response.getWriter().write("err当前提交的任务与后台的任务不相符,可能该是已经被提交");
			return null;
		}
		String[] strs = this.getNotMatchData().split(",");
		try {
			for (int i = 11; i < strs.length; i = i + 11) {
				int sindex = 0;
				for (int j = 11; j < strs.length; j = j + 11) {
					// 判断是否录入重复的流水号
					if (strs[i + 6].trim().equals(strs[j + 6].trim())
							&& strs[i + 2].trim().equals(strs[j + 2].trim())) {
						sindex++;
						if (sindex >= 2) {
							try {
								response.getWriter().write("请不要录入相同的流水号！");
								return null;
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
			// 对于退回的未达录入任务先删除历史数据
			if (getNotMatchList_his().iterator().hasNext()) {
				biz.deleteNotMatchList(getNotMatchList_his());
			}
			if (notMatchListCreate(strs).size() > 0) {
				biz.saveNotMatchItems(getNotMatchList());
			}
			getAppTaskAdm().commitTask(getTask(), getDocSet(), getUserInfo(),
					TaskResult.SUCCESS);
		} catch (XDocProcException e) {
			logger.error("提交任务失败", e);
			response.getWriter().write("err提交任务失败: 请重新登录再试！");
		}

		this.clean();
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
		XPeopleInfo userInfo = (XPeopleInfo) ServletActionContext.getRequest()
				.getSession().getAttribute(AMSecurityDefine.XPEOPLEINFO);
		if (!task.getId().equals(checkTaskId)) {
			response.getWriter().write("err当前提交的任务与后台的任务不相符,可能该是已经被提交");
			return null;
		}
		docSet.setDeleteReasons(reasons);
		try {
			appTaskAdm.commitTask(task, docSet, userInfo, TaskResult.DELETE);
		} catch (XDocProcException e) {
			logger.error("发起删除失败", e);
			response.getWriter().write("发起删除失败:" + e.getMessage());
		}
		clean();
		return null;
	}

	/**
	 * 清空页面信息
	 */
	public void clean() {

		notMatchList.clear();
		imgList.clear();
		task = null;
		errMsg = null;
		docId = null;
		voucherNo = null;
		notMatchData = null;
	}

	/**
	 * 根据docset获取当前任务的对账单信息
	 */
	public void billinfoInit() {

		idBank = docSet.getIdBank();
		idBranch = docSet.getIdBranch();
		idCenterNo = docSet.getIdCenter();
		bankName = docSet.getBankName();
		voucherNo = docSet.getVoucherNo();
	}

	/**
	 * 在任务提交之前解析页面传入的字符串，整理成一个notMatchList
	 * 
	 * @param str
	 *            页面传入的未达项数据字符串数组
	 * @return 解析结果notMatchList
	 * @throws XDocProcException
	 */
	public List<NotMatchTable> notMatchListCreate(String[] strs)
			throws XDocProcException {
		for (int i = 11; i < strs.length; i = i + 11) {
			NotMatchTable notMatchItem = new NotMatchTable();
			for (NotMatchTable nmt : notMatchList_his) {
				// 如果能从历史数据中取到未达信息则先出事后其对象，避免覆盖页面上没有但数据存在的属性，如：对账日期
				if (docSet.getVoucherNo().equals(nmt.getVoucherNo())
						&& strs[i + 6].trim().equals(nmt.getTraceNo())) {
					notMatchItem = nmt;
				}
			}
			notMatchItem.setInputOpCode(userCode); // 录入柜员号

			Date dt = new Date();// 页面显示当前日期
			SimpleDateFormat matter1 = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			String inputTime = matter1.format(dt).toString(); // 录入日期
			notMatchItem.setInputOpTime(inputTime);

			notMatchItem.setDocId(docSet.getDocId());// 业务流水
			notMatchItem.setVoucherNo(docSet.getVoucherNo());// 对账单编号
			notMatchItem.setAccNo(strs[i + 3].trim());// 账号
			String year = strs[i + 4].trim().substring(0, 4); // 记账日期
			String month = strs[i + 4].trim().substring(5, 7);
			String day = strs[i + 4].trim().substring(8);
			notMatchItem.setTraceDate(year + month + day);
			notMatchItem.setTraceCredit(Double.parseDouble(strs[i + 5].trim()));// 发生额
			notMatchItem.setTraceNo(strs[i + 6].trim());// 凭证号
			notMatchItem.setInputDesc(strs[i + 7].trim());// 摘要

			Set<Map.Entry<String, String>> directionSet = refDirectionMap
					.entrySet();
			for (Iterator<Map.Entry<String, String>> it = directionSet
					.iterator(); it.hasNext();) {
				Map.Entry<String, String> entry = (Map.Entry<String, String>) it
						.next();
				if (strs[i + 8].trim().equals(entry.getValue())) {
					notMatchItem.setDirection(entry.getKey());
					break;
				}
			}

			Set<Map.Entry<String, String>> resultSet = refResultMap.entrySet();
			for (Iterator<Map.Entry<String, String>> it = resultSet.iterator(); it
					.hasNext();) {
				Map.Entry<String, String> entry = (Map.Entry<String, String>) it
						.next();
				if (strs[i + 9].trim().equals(entry.getValue())) {
					notMatchItem.setCheckFlag(entry.getKey());
					break;
				}
			}
			// 如果不为空则为网银过来的未达账单，此时不需要赋值，直接从数据库取出来即可
			if (StringUtils.isEmpty(notMatchItem.getDocDate())) {
				notMatchItem.setDocDate(docSet.getDocDate());
			}
			notMatchItem.setIdBank(docSet.getIdBank());
			notMatchItem.setIdBranch(docSet.getIdBranch());
			notMatchItem.setIdCenter(docSet.getIdCenter());
			notMatchItem.setBankName(docSet.getBankName());
			notMatchItem.setAuthFlag(NOTMATCHAUTHFLAG_INPUTED);

			notMatchList.add(notMatchItem);
		}
		return notMatchList;
	}

	/**
	 * 根据docId查询notMatchTable，确认该笔任务是否来自删除审核退回或未达审核退回
	 * 如果未达项列表中有数据，则表示该笔任务为退回，设置notMatchList并且返回值true，否则返回false
	 * 
	 * @return
	 * @throws XDocProcException
	 */
	@SuppressWarnings("rawtypes")
	public boolean isTaskBack(String docId) throws XDocProcException {
		List<NotMatchTable> list = biz
				.getNotMatchListByDocId(docId);
		this.notMatchList_his = biz.getNotMatchListByDocId(docId);
		Iterator it = list.iterator();
		if (it.hasNext()) {
			this.lineCount = list.size() + 1; // 设置未达项列表行号
			for (int i = 0; i < list.size(); i++) {
				String traceDateTmp = list.get(i).getTraceDate();
				if (StringUtils.isNotEmpty(traceDateTmp)
						&& traceDateTmp.split("-").length < 3) {
					String traceDate = traceDateTmp.substring(0, 4) + "-"
							+ traceDateTmp.substring(4, 6) + "-"
							+ traceDateTmp.substring(6);
					list.get(i).setTraceDate(traceDate);
				}
			}
			this.notMatchList = list;
			return true;
		} else {
			return false;
		}
	}

	public TreeMap<String, String> getRefDirectionMap() {
		if (null == refDirectionMap || refDirectionMap.size() == 0) {
			refDirectionMap = RefTableTools.ValRefDirectionMap;
		}
		return refDirectionMap;
	}

	public TreeMap<String, String> getRefResultMap() {
		if (null == refResultMap || refResultMap.size() == 0) {
			refResultMap = RefTableTools.ValRefCheckflagMap;
		}
		return refResultMap;
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
	
	public INotMatchTableAdm getNotMatchTableAdm() {
		return notMatchTableAdm;
	}

	public void setNotMatchTableAdm(INotMatchTableAdm notMatchTableAdm) {
		this.notMatchTableAdm = notMatchTableAdm;
	}

	public XPeopleInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(XPeopleInfo userInfo) {
		this.userInfo = userInfo;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getIdBank() {
		return idBank;
	}

	public void setIdBank(String idBank) {
		this.idBank = idBank;
	}

	public String getIdBranch() {
		return idBranch;
	}

	public void setIdBranch(String idBranch) {
		this.idBranch = idBranch;
	}

	public String getIdCenterNo() {
		return idCenterNo;
	}

	public void setIdCenterNo(String idCenterNo) {
		this.idCenterNo = idCenterNo;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getVoucherNo() {
		return voucherNo;
	}

	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}

	public String getDocDate() {
		return docDate;
	}

	public void setDocDate(String docDate) {
		this.docDate = docDate;
	}

	public DocSet getDocSet() {
		return docSet;
	}

	public void setDocSet(DocSet docSet) {
		this.docSet = docSet;
	}

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	public String getNowDate() {
		return nowDate;
	}

	public void setNowDate(String nowDate) {
		this.nowDate = nowDate;
	}

	public String getNotMatchData() {
		return notMatchData;
	}

	public void setNotMatchData(String notMatchData) {
		this.notMatchData = notMatchData;
	}

	public List<NotMatchTable> getNotMatchList() {
		return notMatchList;
	}

	public void setNotMatchList(List<NotMatchTable> notMatchList) {
		this.notMatchList = notMatchList;
	}

	public List<String> getImgList() {
		return imgList;
	}

	public void setImgList(List<String> imgList) {
		this.imgList = imgList;
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

	public String getTaskcount() {
		return taskcount;
	}

	public void setTaskcount(String taskcount) {
		this.taskcount = taskcount;
	}

	public void setRefDirectionMap(TreeMap<String, String> refDirectionMap) {
		this.refDirectionMap = refDirectionMap;
	}

	public void setRefResultMap(TreeMap<String, String> refResultMap) {
		this.refResultMap = refResultMap;
	}

	public String getAuthInfo() {
		return authInfo;
	}

	public void setAuthInfo(String authInfo) {
		this.authInfo = authInfo;
	}

	public int getLineCount() {
		return lineCount;
	}

	public void setLineCount(int lineCount) {
		this.lineCount = lineCount;
	}

	public String[] getReasons() {
		return reasons;
	}

	public void setReasons(String[] reasons) {
		this.reasons = reasons;
	}

	public List<NotMatchTable> getNotMatchList_his() {
		return notMatchList_his;
	}

	public void setNotMatchList_his(List<NotMatchTable> notMatchList_his) {
		this.notMatchList_his = notMatchList_his;
	}

	public String getCheckTaskId() {
		return checkTaskId;
	}

	public void setCheckTaskId(String checkTaskId) {
		this.checkTaskId = checkTaskId;
	}

	

	public Map<String, String> getAccnoMap() {
		return accnoMap;
	}

	public void setAccnoMap(Map<String, String> accnoMap) {
		this.accnoMap = accnoMap;
	}

	public String getAccNo() {
		return accNo;
	}

	public void setAccNo(String accNo) {
		this.accNo = accNo;
	}
	public INotMatchBiz getBiz() {
		return biz;
	}

	public void setBiz(INotMatchBiz biz) {
		this.biz = biz;
	}
}
