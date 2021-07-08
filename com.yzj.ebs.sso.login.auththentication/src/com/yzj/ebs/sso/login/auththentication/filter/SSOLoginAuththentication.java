package com.yzj.ebs.sso.login.auththentication.filter;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.yzj.wf.am.security.common.AMSecurityDefine;
import com.yzj.wf.am.security.common.ILoginAuththentication;
import com.yzj.wf.common.WFLogger;
import com.yzj.wf.core.model.po.wrapper.XPeopleInfo;
import com.yzj.wf.core.service.po.IPOService;

/**
 * 创建于:2012-11-22 版权所有(C) 2012 深圳市银之杰科技股份有限公司
 * 
 * 长亮版单点登录过滤器实现类
 * 
 * @author 陈林江
 * @version 1.0.0
 */
public class SSOLoginAuththentication implements ILoginAuththentication {
	private static WFLogger logger = WFLogger
			.getLogger(SSOLoginAuththentication.class);
	private IPOService poService;
	private String logoutUrl;
	private String auththenticationFailUrl;

	@Override
	public XPeopleInfo doLogin(ServletRequest request, ServletResponse response) {
		XPeopleInfo info = null;
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		if (httpRequest.getSession().getAttribute(AMSecurityDefine.XPEOPLEINFO) != null) {// 已进行校验
			try {
				httpResponse.sendRedirect("login.action");
				return null;
			} catch (IOException e) {
				logger.error("进行页面跳转出现错误", e);
			}
		}
		String ticket = httpRequest.getParameter("sunfront_ticket");
		String checkUrl = httpRequest.getParameter("check_url");
		String userId = httpRequest.getParameter("userid");
		try {
			if (ticket == null || "".equals(ticket)) {
				throw new Exception("未获取到登陆令牌");
			} else if (checkUrl == null || "".equals(checkUrl)) {
				throw new Exception("未获取到令牌检查地址");
			} else if (userId == null || "".equals(userId)) {
				throw new Exception("未获取到登陆用户的id信息");
			}
			try {
				String sessionId = httpRequest.getSession().getId();
				String path = httpRequest.getContextPath();
				String logout_Url=httpRequest.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/"+logoutUrl;
				this.checkTicket(checkUrl, ticket, userId, sessionId,logout_Url);
				info = poService.getPeopleInfoByPeopleCode(userId);
				if (info == null) {
					logger.error("未在对账系统中查到代码为:" + userId + "的用户的信息");
					throw new Exception("未在对账系统中查到代码为:" + userId + "的用户的信息");
				} else {
					info.setPwd(null);
				}
			} catch (Exception e) {
				logger.info("登陆未通过:" + e.getMessage(), e);
				throw new Exception("登陆未通过:" + e.getMessage(), e);
			}
		} catch (Exception e) { // 如果登录异常，则获取柜面系统错误信息统一展现网页的地址
			if (checkUrl == null || "".equals(checkUrl)) {
				logger.error("未获取到柜面系统错误信息统一展示地:checkUrl=null");
				try {
					httpResponse.sendRedirect(auththenticationFailUrl + "?errMsg="
							+ URLEncoder.encode(e.getMessage(),"GBK"));
					return null;
				} catch (IOException e1) {
					logger.error("进行页面跳转出现错误", e);
				}
			}
			String[] temp=null;
			if(checkUrl!=null){
		    temp = checkUrl.split("SunIntegrator");
			}
			if (temp == null || temp.length != 2) {
				try {
					httpResponse.sendRedirect(auththenticationFailUrl + "?errMsg="
							+ e.getMessage());
				} catch (IOException e1) {
					logger.error("进行页面跳转出现错误", e);
				}
			} else {//跳转到柜面系统错误信息统一展示界面
				String errUrl = temp[0] + "SunIntegrator/UserLogin";
				try {
					httpResponse.sendRedirect(errUrl + "?errortx="
							+ URLEncoder.encode(e.getMessage(), "GBK"));
				} catch (IOException e1) {
					logger.error("进行页面跳转出现错误", e);
				}
			}
		}
		return info;
	}

	private void checkTicket(String checkUrl, String ticket, String userId,
			String sessionId,String logoutUrl) throws Exception {
		HttpClient httpclient = new DefaultHttpClient();
		try {
			String url = checkUrl + "?userid="
					+ URLEncoder.encode(userId, "GBK") + "&sunfront_ticket="
					+ URLEncoder.encode(ticket, "GBK") + "&sessionid="
					+ URLEncoder.encode(sessionId, "GBK")+"&logout_url="+logoutUrl;
			HttpGet httpRequest = new HttpGet(url); // 设置校验地址
			HttpResponse response = httpclient.execute(httpRequest);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) { // 上传成功
				HttpEntity resEntity = response.getEntity();
				String result = new String(EntityUtils.toByteArray(resEntity),
						"UTF-8");
				if (result != null && result.startsWith("result=success")) {
					return;
				} else if (result != null && result.equals("result=failure")) {
					throw new Exception("令牌校验未通过");
				} else {
					throw new Exception("令牌校验系统返回未约定的结果");
				}

			} else {
				throw new Exception("进行令牌合法性查询出现错误:" + statusCode);
			}

		} catch (Exception e) {
			throw e;
		} finally {
			try {
				httpclient.getConnectionManager().shutdown();
			} catch (Exception ignore) {

			}
		}
	}

	public IPOService getPoService() {
		return poService;
	}

	public void setPoService(IPOService poService) {
		this.poService = poService;
	}

	public String getLogoutUrl() {
		return logoutUrl;
	}

	public void setLogoutUrl(String logoutUrl) {
		this.logoutUrl = logoutUrl;
	}

	public String getAuththenticationFailUrl() {
		return auththenticationFailUrl;
	}

	public void setAuththenticationFailUrl(String auththenticationFailUrl) {
		this.auththenticationFailUrl = auththenticationFailUrl;
	}



}
