package com.yzj.ebs.manuprove.action;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.yzj.ebs.common.IPublicTools;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.wf.common.WFLogger;

/**
 * 获取各种理由的action
 *创建于:2012-10-18<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 
 * @author 陈林江
 * @version 1.0
 */
public class UntreadReasonInitAction extends ActionSupport {
private static WFLogger logger=WFLogger.getLogger(UntreadReasonInitAction.class);
	private IPublicTools tools;
	/**
	 * 退票理由数据初始化
	 */
	private static final long serialVersionUID = -4352318144314329974L;
	
	public String getDeleteReasons()throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		List<String> list = null;
		try{
			 list = tools.getDeleteReason();
		} catch (XDocProcException e) {
			list=new ArrayList<String>();
			list.add("err获取删除理由列表出现错误");
			logger.error("获取删除理由列表出现错误",e);
		}
		 JSONArray json = JSONArray.fromObject( list );
		 OutputStream out = null;
			try {
				out = response.getOutputStream();
				response.setContentType("text/html; charset=UTF-8");
				out.write(json.toString().getBytes("UTF-8"));
				out.flush();
			} catch (Exception e) {
				logger.error("获取json对象出现错误",e);
			} finally {
				if (out != null) {
					out.close();
				}
			}
		return null;
	}
	
	public String getReInputReasons()throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		List<String> list = null;
		try{
			 list = tools.getReInputReason();
		} catch (XDocProcException e) {
			list=new ArrayList<String>();
			list.add("err获取重录理由列表出现错误");
			logger.error("获取重录理由列表出现错误",e);
		}
		 JSONArray json = JSONArray.fromObject( list );
		 OutputStream out = null;
			try {
				out = response.getOutputStream();
				response.setContentType("text/html; charset=UTF-8");
				out.write(json.toString().getBytes("UTF-8"));
				out.flush();
			} catch (Exception e) {
				logger.error("获取json对象出现错误",e);
			} finally {
				if (out != null) {
					out.close();
				}
			}
		return null;
	}
	public IPublicTools getTools() {
		return tools;
	}
	public void setTools(IPublicTools tools) {
		this.tools = tools;
	}

}
