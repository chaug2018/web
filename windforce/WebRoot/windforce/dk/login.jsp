<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link type="text/css" rel="stylesheet" href="<%=path%>/windforce/common/css/core.css" />
<%@ include file="/common/wf_import.jsp"%>
<title>银企对账</title>
<script type="text/javascript">
setloginPage();
function setloginPage()
{
if(top.location!=this.location)
{
top.location = this.location;
}
}
function modifyPwd()
{

 var winHeight = 300;
	var winWidth = 450;
	var winTop = (window.screen.height - winHeight) / 2;
	var winLeft = (window.screen.width - winWidth) / 2;
	var sFeatures = "dialogHeight:" + winHeight + "px;dialogWidth:" + winWidth
			+ "px;dialogLeft:" + winLeft + "px;dialogTop:" + winTop + "px;";
			var returnValue = window.showModalDialog("<%=path%>/windforce/po/peopleInfo/modifyPwd.jsp?userNo="+""+"&time="
			+ (new Date()).getTime() , null, sFeatures);
			if(returnValue == "success")
			{
				alert("密码已更改！");
			}
}

//监听回车
function onKey(){
	if(window.event.keyCode == 13){
		if(document.activeElement.id == "j_username"){//如果焦点在用户名，按回车焦点设为密码输入框
			document.getElementById("j_password").focus();
			return false;
		} else {//焦点在其他位置，均提交
			checkMultipleLogin();
		}
	}
}
//提交表单
function submitLoginForm(){
		document.forms["loginForm"].submit();
}


function checkMultipleLogin(){
	if(document.getElementById("j_username").value == "") {
		document.getElementById("login_info").innerHTML = "账号不能为空!";
		document.getElementById("j_username").focus();
		return;
	}else if (document.getElementById("j_password").value == "") {
		document.getElementById("login_info").innerHTML = "密码不能为空!";
		document.getElementById("j_password").focus();
		return;
	}
	var user=document.getElementById("j_username").value;
	document.getElementById("login_info").innerHTML = "正在检验当前浏览器上是否存在已登录的用户...";
	$.post("<%=path%>/getPreUser.action","",
						function(preUser) {
							if (preUser != "" && user != preUser) {
								if (preUser.length > 50) {
									wfAlert("查询出现异常");
									document.getElementById("login_info").innerHTML = "";
									return;
								}
								document.getElementById("login_info").innerHTML = "该浏览器上已存在已经登录的其他用户("
										+ preUser + ")!";
								document.getElementById("j_username").focus();
							} else {
								submitLoginForm();
							}
						});

	}
</script>
</head>
<body style="overflow: hidden;background:#d6dee0;" onkeyup="onKey()">
	<form name="loginForm" id="loginForm"
		action="${pageContext.request.contextPath }/j_spring_security_check"
		method="post">
		<div class="login_index">
			<input type="hidden" name="loginType" value="pwdLogin" id="loginType" />
			<table width="800" border="0" align="right" cellpadding="0"
				cellspacing="0">
				<tr>
					<td height="510" colspan="7">&nbsp;</td>
				</tr>
				<tr>
					<td colspan="7" style="color:red;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span
						id="login_info">${SPRING_SECURITY_LAST_EXCEPTION}</span>
					</td>
				</tr>
				<tr>
					<td width="50" align="center" class="font_red">账号</td>
					<td width="150" align="center"><input name="j_username"
						id="j_username" type="text" class="login_text01" />
					</td>
					<td width="50" align="center" class="font_red">密码</td>
					<td width="150" align="center"><input type="password"
						name="j_password" id="j_password" class="login_text01" />
					</td>
					<td width="100" align="center"><input name="login_button"
						type="button" class="submit_login" id="login_button" value=""
						onclick="checkMultipleLogin()" />
					</td>
					<td width="90" align="center" class="font_red"><label
						style="visibility: hidden"> <input type="checkbox"
							name="checkbox" id="checkbox" /> </label></td>
					<td width="190" class="font_red"></td>
				</tr>
			</table>
		</div>
		<div class="login_head">
			<img src="<%=path%>/common/images/hrxj.jpg" />
		</div>
	</form>
	<!-- 清空异常提示信息 -->
	<%
		session.setAttribute("SPRING_SECURITY_LAST_EXCEPTION", "");
	%>
</body>
</html>