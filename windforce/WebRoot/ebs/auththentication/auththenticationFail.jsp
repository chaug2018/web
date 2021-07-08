<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="java.net.URLEncoder" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>登陆授权失败</title>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String localErr=null;
	String errUrl=null;
	String errMsg= request.getParameter("errMsg");
	if(errMsg!=null){
	 	errMsg=new String(errMsg.getBytes("ISO8859_1"),"gb2312");
	}
%>
</head>
<body>
<p>登陆授权失败,且未获取到柜面系统错误信息统一展示界面</p>
<p>授权失败原因为:"<%=errMsg%>"</p>
</body>
</html>