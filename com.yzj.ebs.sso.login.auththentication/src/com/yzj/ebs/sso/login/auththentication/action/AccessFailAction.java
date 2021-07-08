package com.yzj.ebs.sso.login.auththentication.action;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.yzj.wf.am.security.common.AMSecurityDefine;

/**
 * 创建于:2012-11-23<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 登陆失败action
 * 
 * @author 陈林江
 * @version 1.0
 */
public class AccessFailAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8990422326911816198L;
	private String errMsg;
	public String fail(){
		return "finish";
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
}
