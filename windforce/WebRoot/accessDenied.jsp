<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
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

<title>My JSP 'accessDenied.jsp' starting page</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
</head>
<body>
	<h1 align="center" style="padding-top:100px; color: red">出错了</h1>
	<div align="center">
		<h2>你无权访问，访问被拒绝!</h2><br>
		<h2>
			<span id="time" style="color: red;">5</span>秒钟自动退出登录！点击<a
				style="color: blue;"
				href="${pageContext.request.contextPath}/j_spring_security_logout">退出</a>
		</h2>
	</div>
</body>
<script type="text/javascript">
	function loginOut() {
		var time = document.getElementById("time");
		if (time.innerHTML == 0) {
			window.location.href = "${pageContext.request.contextPath}/j_spring_security_logout";
			return false;
		}
		time.innerHTML = time.innerHTML * 1 - 1;
	}
	window.setInterval("loginOut();", 1000);
</script>
</html>
