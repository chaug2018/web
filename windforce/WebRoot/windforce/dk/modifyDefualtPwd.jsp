
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<%@ taglib prefix="s" uri="/struts-tags"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">


<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>修改密码</title>
<script type="text/javascript">
var checkLeval=3; //密码安全级别,默认为3;
function savePwd()
{
var currPwd = document.getElementById("currPwdId").value;
var newPwd = document.getElementById("newPwdId").value;
var confirmPwd = document.getElementById("confrimPwdId").value;
var uid = '<%=request.getParameter("userSid")%>';
var dataParaments=null;
dataParaments = "userNo="+uid+"&currPwd="+currPwd+"&newPwd="+newPwd+"&confirmPwd="+confirmPwd+"&userCode="+""; //清除在主界面进行修改时的遗留数据
if(isNull(currPwd)){
	wfAlert("原始密码不能为空!");
	return false;
}
if(isNull(newPwd)){
	wfAlert("新密码不能为空!");
	return false;
}
if(isNull(confirmPwd)){
	wfAlert("确认密码不能为空!");
	return false;
}
if(newPwd.length<6){
 wfAlert("密码长度不能小于6");
 return;
}
if(checkPassword(newPwd)<60){
   wfAlert("密码强度不够");
   return;
  }
if(!isGeneralPwd(currPwd)){
	wfAlert("原始密码只能是字母(a-z)、数字(0-9)、中划线(-)、下划线(_)和点(.)");
	return false;
}
if(!isGeneralPwd(newPwd)){
	wfAlert("新密码只能是字母(a-z)、数字(0-9)、中划线(-)、下划线(_)和点(.)");
	return false;
}
if(!isGeneralPwd(confirmPwd)){
	wfAlert("确认密码只能是字母(a-z)、数字(0-9)、中划线(-)、下划线(_)和点(.)");
	return false;
}

 $.post('<%=path%>/editPwdAction.action', dataParaments,
				function(data) {
					if (data != "修改成功！") {
						wfAlert(data);
					} else {
						wfAlert("密码修改成功", false, null, function() {
							closeShowPage_lockPage();
						});
					}
				});
	}

	function checkPassword(password) {
		var score = 0;
		if (password.length < 6) {
			return -6;
		}
		score += password.length * 5;
		if (password.match(/(.*[0-9].*[0-9].*[0-9])/)) {
			score += 5;
		}
		if (password
				.match(/(.*[!,@,#,$,%,^,&,*,?,_,~].*[!,@,#,$,%,^,&,*,?,_,~])/)) {
			score += 10;
		}
		if (password.match(/([a-z].*[A-Z])|([A-Z].*[a-z])/)) {
			score += 20;
		}
		if (password.match(/([a-zA-Z])/) && password.match(/([0-9])/)) {
			score += 20;
		}
		if (password.match(/([!,@,#,$,%,^,&,*,?,_,~])/)
				&& password.match(/([0-9])/)) {
			score += 20;
		}
		if (password.match(/([!,@,#,$,%,^,&,*,?,_,~])/)
				&& password.match(/([a-zA-Z])/)) {
			score += 20;
		}
		if (password.match(/^\w+$/) || password.match(/^\d+$/)) {
			score -= 10;
		}
		if (score < 0) {
			score = 0;
		}
		if (score > 160) {
			score = 160;
		}
		score = score - (checkLeval - 3) * 15;
		return score;
	}

	function validatePassword(passwordInput) {
		var score = checkPassword(passwordInput.value);
		var color_short = "#FF0000";
		var color_weak = "#FF8C69";
		var color_normal = "#FFE4B5";
		var color_good = "#CAFF70";
		var color_veryGood = "#B3EE3A";
		var password_label = document.getElementById('password_label');
		var text;
		var color = color_short;
		if (score == -6) {
			text = '太短';
		} else {
			text = score < 60 ? '弱' : (score < 90 ? '一般' : (score < 130 ? '较好'
					: '很好'));
			color = score < 60 ? color_weak : (score < 90 ? color_normal
					: (score < 130 ? color_good : color_veryGood));
		}

		var width_ = score - 12 > 123 ? 123 : score - 12;
		width_ = width_ < 30 ? 30 : width_;
		var width = width_ + "px";
		password_label.innerHTML = "<span style='width:"+ width +
        ";display:block;overflow:hidden;height:15px;line-height:15px;background:"+ color
        +";'>"
				+ text + "</span>";
	}
	function keyUp() {
		var oEvent = window.event;
		if (oEvent.keyCode == 13) {
			savePwd();
		}
	}
</script>
</head>
<body>
	<div align="left" style="position:absolute;left:0px;">
		&nbsp;&nbsp;原始密码&nbsp;&nbsp;&nbsp;<input type="password"
			id="currPwdId" style="width:170px;" /> <br />
		&nbsp;&nbsp;新设密码&nbsp;&nbsp;&nbsp;<input type="password" id="newPwdId"
			name="newPwd" onblur="validatePassword(this)"
			onclick="validatePassword(this)" onkeydown="validatePassword(this)"
			maxlength="32" style="width:170px;" /> <br />
		&nbsp;&nbsp;确认密码&nbsp;&nbsp;&nbsp;<input type="password"
			id="confrimPwdId" name="confrimPwd" maxlength="32"
			style="width:170px;" /> &nbsp;&nbsp;密码强度&nbsp;&nbsp;&nbsp; <span
			id="password_label"
			style="width:150px;position: absolute;margin-top:3px;margin-left:-4px;"></span>
		<br />
		<div style="left:205px;margin-top:-25px;position: absolute;">
			<input type="button" value="确定" onclick="savePwd()" />
		</div>
	</div>
	<script type="text/javascript">
		var currPwdId = document.getElementById("currPwdId");
		var newPwdId = document.getElementById("newPwdId");
		var confrimPwdId = document.getElementById("confrimPwdId");
		validatePassword(newPwdId);
		currPwdId.onkeyup = keyUp;
		newPwdId.onkeyup = keyUp;
		confrimPwdId.onkeyup = keyUp;
	</script>
</body>
</html>


