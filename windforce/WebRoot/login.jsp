<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>用户登录</title>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<link href="<%=basePath %>/common/css/login.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
setloginPage();
function setloginPage()
{
if(top.location!=this.location)
{
top.location = this.location;
}
}
</script>
</head>
<body>
	<div class="centerDiv">
		<div align="center">
		用户登录
		</div>
		<form name="loginForm" action="${pageContext.request.contextPath }/j_spring_security_check" method="post">
			<fieldset><div>
					<label style="color:red;width: 200px;">${SPRING_SECURITY_LAST_EXCEPTION}</label></div>
						<div>
							<br /> <label for="j_username">用户代号</label> <input type="text"
								name="j_username" id="j_username" size="30" maxlength="30" /> <br />
						</div>
						<div>
							<label for="password">密码</label> <input type="password"
								name="j_password" id="j_password" size="30" maxlength="30" /> <br />
						</div>
						<div>
							<br />
							<div align="center">
								<input name="submit" type="submit" class="buttom" value="登录" />
								<input name="submit" type="button" class="buttom"
									onclick="document.loginForm.reset()" value="重置" />
							</div>
						</div>
			</fieldset>
		</form>
	</div>
	<div class="hiddenDiv"></div>
</body>
</html>