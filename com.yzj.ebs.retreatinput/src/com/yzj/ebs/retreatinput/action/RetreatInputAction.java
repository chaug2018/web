package com.yzj.ebs.retreatinput.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.yzj.ebs.common.BankParam;
import com.yzj.ebs.common.IPublicTools;
import com.yzj.ebs.common.RefTableTools;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.database.CheckMainData;
import com.yzj.ebs.retreatinput.biz.IRetreatInputBiz;
import com.yzj.ebs.retreatinput.pojo.BatchParam;
import com.yzj.ebs.retreatinput.pojo.ImportData;
import com.yzj.ebs.retreatinput.pojo.SingleParam;
import com.yzj.wf.am.security.common.AMSecurityDefine;
import com.yzj.wf.common.WFLogger;
import com.yzj.wf.core.model.po.wrapper.XPeopleInfo;

/**
 * 
 * 创建于:2012-10-25<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 退信登记
 * 
 * @author 施江敏  
 * @修订      单伟龙
 * @version 1.0.1
 */
public class RetreatInputAction extends ActionSupport {
	private static final long serialVersionUID = -3203213454474588779L;
	private IPublicTools tools;//工具类
	private IRetreatInputBiz retreatInputBizImpl;
	
	private SingleParam singleParam = new SingleParam();//单笔退信登记输入项
	private List<ImportData> failList = new ArrayList<ImportData>();
	private BatchParam result = new  BatchParam() ;
	
	private TreeMap<String, String> refUrgeTypeMap = new TreeMap<String, String>();
	
	// 上传文件相关
	private XPeopleInfo userInfo; // 人员信息
	private File upFile;

	private static WFLogger logger = WFLogger
			.getLogger(RetreatInputAction.class);
	String msg="";

	private String idCenter;
	private String urgeType;
	private String urgeNote;
	private String voucherNo;
	/**
	 * 初始化界面
	 * 
	 * @return
	 */
	public String init() {
		userInfo = (XPeopleInfo) ServletActionContext.getRequest().getSession()
				.getAttribute(AMSecurityDefine.XPEOPLEINFO);

		this.clean();
		return "initSuccess";
	}

	/**
	 * 逐笔登记退信
	 * 
	 * @return
	 * @throws IOException
	 * @throws XDocProcException 
	 */
	public String singleInput() throws IOException, XDocProcException {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		msg = null;
		CheckMainData checkMaindata = new CheckMainData();
		
		checkMaindata.setVoucherNo(voucherNo);
		checkMaindata.setUrgeNote(urgeNote);
		checkMaindata.setUrgeType(urgeType);
		msg=retreatInputBizImpl.completeSingleInput(checkMaindata);
		response.getWriter().write(msg);
		return null;
		
	}

	/**
	 * 下载批量导入模板
	 * 
	 * @return
	 * @throws IOException
	 */
	public String downloadTemplete() throws IOException {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		String path;
		try {
			path = this.getClass().getClassLoader().getResource("").toURI().getPath();
		} catch (URISyntaxException e1) {
			path="template";
		}
		if(path.contains("classes")){
			path = path.replace("classes", "template");
		}
		String fileName = "退信登记模板.xls";
		String filenamedownload = path + fileName;
		// 设置输出的格式
		fileName = URLEncoder.encode(fileName, "UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.reset();
		response.setContentType("application/x-download");
		response.addHeader("Content-Disposition", "attachment;filename=\""
				+ fileName);
		// 循环取出流中的数据
		OutputStream output = null;
		FileInputStream fis = null;
		try {
			output = response.getOutputStream();
			fis = new FileInputStream(filenamedownload);
			byte[] b = new byte[1024];
			int i = 0;
			while ((i = fis.read(b)) > 0) {
				output.write(b, 0, i);
			}
			output.flush();
		} catch (Exception e) {
			msg = "退信登记模板下载失败";
			logger.error(msg,e);
			return null;
		} finally {
			if (fis != null) {
				fis.close();
				fis = null;
			}
			if (output != null) {
				output.close();
				output = null;
			}
		}
		return null;
	}

	/**
	 * 读取excel文件，批量设置退信状态
	 * 
	 * @return
	 * @throws IOException 
	 * @throws XDocProcException 
	 */
	public String batchInport() throws IOException, XDocProcException {
	
		result=retreatInputBizImpl.completeBatchInput(upFile);
		return "initSuccess";
		
	}
	/**
	 * 初始化的clean方法
	 * @return
	 */
	public void clean(){
		msg = null;
		result=null;
	    urgeType=null;
		urgeNote=null;
		voucherNo=null;
		idCenter="";
	}

	public IPublicTools getTools() {
		return tools;
	}

	public void setTools(IPublicTools tools) {
		this.tools = tools;
	}

	public IRetreatInputBiz getRetreatInputBizImpl() {
		return retreatInputBizImpl;
	}

	public void setRetreatInputBizImpl(IRetreatInputBiz retreatInputBizImpl) {
		this.retreatInputBizImpl = retreatInputBizImpl;
	}

	public static WFLogger getLogger() {
		return logger;
	}

	public static void setLogger(WFLogger logger) {
		RetreatInputAction.logger = logger;
	}

	
	public TreeMap<String, String> getRefUrgeTypeMap() {
		if (null == refUrgeTypeMap || refUrgeTypeMap.size() == 0) {
			refUrgeTypeMap = RefTableTools.ValRefUrgeTypeMap;
		}
		return refUrgeTypeMap;
	}

	public void setRefUrgeTypeMap(TreeMap<String, String> refUrgeTypeMap) {
		this.refUrgeTypeMap = refUrgeTypeMap;
	}

	public SingleParam getSingleParam() {
		return singleParam;
	}

	public void setSingleParam(SingleParam singleParam) {
		this.singleParam = singleParam;
	}

	public List<ImportData> getFailList() {
		return failList;
	}

	public void setFailList(List<ImportData> failList) {
		this.failList = failList;
	}

	public BatchParam getResult() {
		return result;
	}

	public void setResult(BatchParam result) {
		this.result = result;
	}

	public XPeopleInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(XPeopleInfo userInfo) {
		this.userInfo = userInfo;
	}

	public File getUpFile() {
		return upFile;
	}

	public void setUpFile(File upFile) {
		this.upFile = upFile;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getUrgeType() {
		return urgeType;
	}

	public void setUrgeType(String urgeType) {
		this.urgeType = urgeType;
	}

	public String getUrgeNote() {
		return urgeNote;
	}

	public void setUrgeNote(String urgeNote) {
		this.urgeNote = urgeNote;
	}

	public String getVoucherNo() {
		return voucherNo;
	}

	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}

	public String getIdCenter() {
		return idCenter;
	}

	public void setIdCenter(String idCenter) {
		this.idCenter = idCenter;
	}
	
}
