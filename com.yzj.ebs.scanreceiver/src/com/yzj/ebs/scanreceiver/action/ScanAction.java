/**
 * 
 */
package com.yzj.ebs.scanreceiver.action;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.yzj.ebs.common.BankParam;
import com.yzj.ebs.common.IPublicTools;
import com.yzj.ebs.common.IdCenterParam;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.database.DocSet;
import com.yzj.ebs.task.common.IAppTaskAdm;
import com.yzj.wf.am.security.common.AMSecurityDefine;
import com.yzj.wf.common.WFLogger;
import com.yzj.wf.core.model.po.wrapper.XPeopleInfo;

/**
 * 创建于:2012-9-19<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 扫描模块后台业务逻辑处理action
 * 
 * @author 陈林江
 * @version 1.0.0
 */
public class ScanAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3852318144314329974L;

	private static WFLogger logger = WFLogger.getLogger(ScanAction.class);

	private static SimpleDateFormat sdfYMD = new SimpleDateFormat("yyyy-MM-dd");

	private String userId;
	private String orgId;

	private List<String> imgs; // 影像url列表
	private String bizServerAdress; // 当前action的url，初始化的时候传入到applet中
	private String imageServerAdress; // 影像接收服务url，初始化的时候传入到applet中
	private String errMsg; // 初始化时的错误信息
	private String type; // 操作类型，"save"代表保存，"init"或空都代表初始化,

	private IAppTaskAdm taskAdm;
	private IPublicTools publicTools;

	/**
	 * 对请求处理
	 * 
	 * @return
	 * @throws IOException
	 */
	public String init_save() throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		XPeopleInfo people = (XPeopleInfo) request.getSession().getAttribute(
				AMSecurityDefine.XPEOPLEINFO);
		response.setCharacterEncoding("UTF-8");
		errMsg=null;
		if (!"save".equals(type)) { // 初始化请求
			IdCenterParam pc = null;   //对账中心信息
			BankParam bp = null;        //网点信息
			if (people == null) { // 未登陆
				response.sendRedirect(request.getContextPath()
						+ "/windforce/dk/login.jsp");
				return null;
			} else {
				userId=people.getPeopleCode();
				orgId = people.getOrgNo();
				try {
					bp = publicTools.getBankParam(orgId);
					if (bp != null) {
						pc = publicTools.getParamIdcenter(bp.getIdCenter());
					}
				} catch (XDocProcException e) {
					errMsg = "查询操作人员对应的对账中心信息出现错误";
					logger.error(errMsg,e);
					return "show"; // 展示扫描界面
				}
				if (pc == null) {
					errMsg = "对不起,查不到你所属的对帐中心的相关信息，无法进行扫描操作";
					logger.warn("未获取到该人员所属的对账中心的信息");
					return "show"; // 展示扫描界面
				}
			}
			try {
				errMsg = null; // 清空历史错误
				this.initParams(pc); // 初始化必要参数
			} catch (Exception e) {
				errMsg = "初始化参数出现错误";
				logger.error(errMsg);
			}
			return "show"; // 展示扫描界面
		} else {// 以下为接收扫描信息时的处理逻辑
			type = null; // 先清空type信息
			if (imgs.size() != 3) {
				response.getWriter().write(
						"fail当前业务传入的图片数量(" + imgs.size() + ")不为2");
				return null;
			}
			try {
				DocSet docSet = this.createDocSet(imgs);
				XPeopleInfo people_temp=new XPeopleInfo();
				people_temp.setSid(userId);
				people_temp.setPeopleCode(userId);
				people_temp.setOrgNo(orgId);
				taskAdm.createTaskFromScan(docSet, people_temp);
			} catch (XDocProcException e) {
				response.getWriter().write("fail创建任务失败:" + e.getMessage());
				return null;
			}
			response.getWriter().write("success");
		}

		return null;
	}

	private DocSet createDocSet(List<String> imgs) throws XDocProcException {
		BankParam bp=publicTools.getBankParam(orgId);
		IdCenterParam pc=publicTools.getParamIdcenter(bp.getIdCenter());
		Date date = new Date();
		DocSet docSet = new DocSet();
		docSet.setCredit(0.0);
		docSet.setNeedNotMatch((short)0);     //默认不需要做未达，在发现有未达项时再将次字段改为1
		docSet.setIdCenter(pc.getIdCenterNo());
		docSet.setIdBranch(bp.getIdBranch());
		docSet.setIdBank(bp.getIdBank());
		docSet.setWorkDate(sdfYMD.format(date));
		docSet.setFrontImagePath(imgs.get(0));
		docSet.setBackImagePath(imgs.get(1));
		docSet.setStoreId(imgs.get(2));
		docSet.setDocTypeId(1);
		docSet.setOpCode100(userId); // 扫描柜员id号
		docSet.setIsFree(1);
		docSet.setCallTimes(0);
		return docSet;
	}

	/**
	 * 初始化参数
	 * @param 对账中心号
	 */
	private void initParams(IdCenterParam idCenter) throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		bizServerAdress = request.getRequestURL().toString();
		imageServerAdress = idCenter.getStoreUrl()+"storeAction.action";
		logger.info("初始化扫描界面参数完毕...");
	}

	public String getServerUrl() {
		return bizServerAdress;
	}

	public void setServerUrl(String serverUrl) {
		this.bizServerAdress = serverUrl;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getBizServerAdress() {
		return bizServerAdress;
	}

	public void setBizServerAdress(String bizServerAdress) {
		this.bizServerAdress = bizServerAdress;
	}

	public String getImageServerAdress() {
		return imageServerAdress;
	}

	public void setImageServerAdress(String imageServerAdress) {
		this.imageServerAdress = imageServerAdress;
	}

	public List<String> getImgs() {
		return imgs;
	}

	public void setImgs(List<String> imgs) {
		this.imgs = imgs;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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
}
