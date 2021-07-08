package com.yzj.ebs.sso.login.auththentication.action;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.yzj.wf.am.security.common.AMSecurityDefine;

/**
 * 创建于:2012-9-19<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 供外部调用的登出action
 * 
 * @author 陈林江
 * @version 1.0
 */
public class LogoutAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2321901464260452895L;
	private String sunline_front_ticket;
	private String logout_url;
	public String logout(){
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		response.setCharacterEncoding("UTF-8");
//		try {
//			response.getWriter().write("result=success&userid=test");
//		} catch (IOException e) {
//		 
//		}
//		request.getSession().removeAttribute(AMSecurityDefine.XPEOPLEINFO);
		return null;
	}
	public String getSunline_front_ticket() {
		return sunline_front_ticket;
	}
	public void setSunline_front_ticket(String sunline_front_ticket) {
		this.sunline_front_ticket = sunline_front_ticket;
	}
	public String getLogout_url() {
		return logout_url;
	}
	public void setLogout_url(String logout_url) {
		this.logout_url = logout_url;
	}
	
	
}
