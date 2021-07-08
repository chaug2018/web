<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@page import="com.yzj.wf.common.WFException"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
WFException exception=(WFException)request.getAttribute("javax.servlet.error.exception");
String errMsg=exception.getMessage();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>解除锁定</title>
<script type="text/javascript">
</script>
</head>
 <body style="text-align: center;padding-top: 100px;">
    <img src="<%=path%>/common/images/500.jpg"/><br/><br/>
    <span>错误信息:<%=errMsg %></span><br/>
   <input type="button"  onclick="javascript:top.refresh();" class="submit_but04" value="返回首页"/>
  </body>
</html>