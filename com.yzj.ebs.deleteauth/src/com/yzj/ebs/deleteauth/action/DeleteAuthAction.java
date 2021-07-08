package com.yzj.ebs.deleteauth.action;

import java.io.IOException;
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
import com.yzj.ebs.database.DocSet;
import com.yzj.ebs.database.NotMatchTable;
import com.yzj.ebs.deleteauth.biz.IDeleteAuthBiz;
import com.yzj.ebs.deleteauth.service.BillInfo;
import com.yzj.ebs.task.common.IAppTaskAdm;
import com.yzj.ebs.task.common.TaskConstDef;
import com.yzj.ebs.task.common.TaskConstDef.AppId;
import com.yzj.ebs.task.common.TaskConstDef.ProcessVariableKey;
import com.yzj.ebs.task.common.TaskConstDef.TaskResult;
import com.yzj.ebs.task.impl.AppTaskAdm;
import com.yzj.wf.am.security.common.AMSecurityDefine;
import com.yzj.wf.bpm.engine.task.IBPMTask;
import com.yzj.wf.common.WFLogger;
import com.yzj.wf.core.model.po.wrapper.XPeopleInfo;

/**
 * 创建于:2012-10-15<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 删除审核action
 * 
 * @author 陈林江
 * @version 1.0
 */
public class DeleteAuthAction extends ActionSupport {

	private WFLogger logger = WFLogger.getLogger(DeleteAuthAction.class);

	private static final long serialVersionUID = -7358905128405053152L;

	//private IDocSetAdm docSetAdm;
	private IAppTaskAdm taskAdm;
	//private IAccnoMainDataAdm accnoMainDataAdm;;
	private IPublicTools publicTools;
	//private INotMatchTableAdm notMatchAdm;

	private DocSet docSet;
	private IBPMTask task;
	private String checkTaskId;
	private String errMsg;
	private String inputInfo;
	private String billNo; 
	private String taskId;
	private String docId;
	private String[] reasons;
	private String preNode;
	private List<String> imgList = new ArrayList<String>(); // 影像列表
	private List<AccNoMainData> accNolist=new ArrayList<AccNoMainData>();
	private IDeleteAuthBiz biz;

	// 初始化需要的参数
	public String init() {
		this.clean();
		XPeopleInfo userInfo = (XPeopleInfo) ServletActionContext.getRequest()
				.getSession().getAttribute(AMSecurityDefine.XPEOPLEINFO);

		String idBank = userInfo.getOrgNo();
		BillInfo info=new BillInfo();
		try {
			// 随机获取一笔任务
			task = taskAdm.getTask(AppId.DELETE_AUTH, userInfo);
			if (task == null) {
				return "noTask";
			} else {
				// 获取当前节点的名称
				preNode=task.getVariable(ProcessVariableKey.PRENODE).toString();
				if(preNode!=null&&preNode.length()>0){
					preNode=AppTaskAdm.preNode_taskNameMap.get(preNode);
				}
				taskId = task.getId();
				docId = task.getBusinessKey();
				docSet = biz.queryOneByID(Long.valueOf(docId));
				if(docSet==null){
					throw new XDocProcException("未获取到流程任务中指定的业务记录");
				}
				// 获取删除理由
				reasons=docSet.getDeleteReasons();
				String idCenterNo = docSet.getIdCenter();
				IdCenterParam idCenter = null;
				idCenter = publicTools.getParamIdcenter(idCenterNo);
				if (idCenter == null) {
					throw new XDocProcException("无法获取到机构" + idBank + "所对应的对账中心");
				}
				// 获取当前账单的图像
				String frontImg = idCenter.getImageUrl()
						+ docSet.getFrontImagePath();
				String backImg = idCenter.getImageUrl()
						+ docSet.getBackImagePath();
				imgList.add(frontImg);
				imgList.add(backImg);
				String billNo = docSet.getVoucherNo(); // 账单编号
				if (billNo != null && !"".equals(billNo)) {
					info = biz.getManualInputInfo(docSet); 
				}
				info.setBillNo(billNo);
				info.setDocId(docId);
				JSONObject json = JSONObject.fromObject(info);
				inputInfo = json.toString();
				return "initSuccess";
			}
		} catch (XDocProcException e) {
			errMsg = "获取任务出现错误:";
			logger.error(errMsg,e);
			return "initSuccess";
		}
	}

	/**
	 * 提交任务
	 * 
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public String submitTask() throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		XPeopleInfo userInfo = (XPeopleInfo) ServletActionContext.getRequest()
				.getSession().getAttribute(AMSecurityDefine.XPEOPLEINFO);
		if(!task.getId().equals(checkTaskId)){
			response.getWriter().write("err当前提交的任务与后台的任务不相符,可能该是已经被提交");	
			return null;
		}
		try {
			// 如果当前节点是未达审核
			if(TaskConstDef.PreNodeTaskName.NOTMATCHAUTH.equals(preNode)){
				biz.deleteNotMatchList(docSet.getDocId()+"");
			}
			taskAdm.commitTask(task, docSet,  userInfo, TaskResult.SUCCESS);
		} catch (XDocProcException e) {
			response.getWriter().write("err提交任务失败:"+e.getMessage());
			throw new IOException("提交任务失败");
		}
		this.clean();
		return null;
	}
	
	/**
	 * 返回至发起删除的节点
	 * 
	 * @return
	 */
	public String turnBack() throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		XPeopleInfo userInfo = (XPeopleInfo) ServletActionContext.getRequest()
				.getSession().getAttribute(AMSecurityDefine.XPEOPLEINFO);
		if(!task.getId().equals(checkTaskId)){
			response.getWriter().write("err当前提交的任务与后台的任务不相符,可能该是已经被提交");	
			return null;
		}
		try {
			taskAdm.commitTask(task, docSet, userInfo, TaskResult.FAIL);
		} catch (XDocProcException e) {
			response.getWriter().write("err提交任务失败:"+e.getMessage());
			return null;
		}
		this.clean();
		return null;
	}

	/**
	 * 放弃任务
	 * 
	 * @return
	 */
	public String giveUpTask() throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		XPeopleInfo userInfo = (XPeopleInfo) ServletActionContext.getRequest()
				.getSession().getAttribute(AMSecurityDefine.XPEOPLEINFO);
		if(!task.getId().equals(checkTaskId)){
			response.getWriter().write("err当前提交的任务与后台的任务不相符,可能该是已经被提交");	
			return null;
		}
		try {
			taskAdm.giveUpTask(task, userInfo);
		} catch (XDocProcException e) {
			logger.error("放弃任务出现错误",e);
			response.getWriter().write("err放弃任务出现错误");
		}
		return null;
	}

    // 清空属性
	private void clean() {
		imgList.clear();
		task=null;
		errMsg = null;
		inputInfo = null;
		docId = null;
		accNolist.clear();
		reasons=null;
		preNode=null;
		reasons=null;
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

	public String[] getReasons() {
		return reasons;
	}

	public void setReasons(String[] reasons) {
		this.reasons = reasons;
	}

	public String getPreNode() {
		return preNode;
	}

	public void setPreNode(String preNode) {
		this.preNode = preNode;
	}

	public String getCheckTaskId() {
		return checkTaskId;
	}

	public void setCheckTaskId(String checkTaskId) {
		this.checkTaskId = checkTaskId;
	}
	public IDeleteAuthBiz getBiz() {
		return biz;
	}

	public void setBiz(IDeleteAuthBiz biz) {
		this.biz = biz;
	}

}
