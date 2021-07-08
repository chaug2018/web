<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>页面找不到</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<%@ include file="/common/wf_import.jsp"%>
</head>
<script type="text/javascript">
	// 每次页面加载时刷新左任务数据
	top.autoSearch("navigationAutoSearchAction.action");
</script>
<body style="text-align: center;padding-top: 100px;">
	<span class="disblock STYLE1"><img
		src="<%=path%>/common/images/notask.png" /> </span>
	<br />
	<br />
	<br />
	<input type="button" onclick="javascript:top.refresh();"
		class="submit_but04" value="返回首页" />
</body>
</html>
