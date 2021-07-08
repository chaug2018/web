<%--
  机构人员修改页面                 
@date          2012/04/25         
@author        蒋正秋              
@version       1.0                
--%>
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
<link type="text/css" rel="stylesheet" href="<%=path%>/windforce/common/css/po_mws.css" />
<%@ include file="/common/wf_import.jsp"%>

<script type="text/javascript">
var checkLeval=3; //密码安全级别,默认为3;
function savePwd()
{
var currPwd = document.getElementById("currPwdId").value;
var newPwd = document.getElementById("newPwdId").value;
var confirmPwd = document.getElementById("confrimPwdId").value;
var uid = '<%=request.getParameter("userNo")%>';
var dataParaments=null;
if(uid==null||uid=="null"||uid==""){
	var userCode=document.getElementById("userCode").value;
	if(isNull(userCode)){
		wfAlert("用户名不能为空!");
		return false;
	}else if(!isAlphaAndDigits(userCode)){
		wfAlert("用户名只能是字母或数字!");
		return false;
	}else {
		dataParaments = "userCode="+userCode+"&currPwd="+currPwd+"&newPwd="+newPwd+"&confirmPwd="+confirmPwd;
	}
}else{
	dataParaments = "userNo="+uid+"&currPwd="+currPwd+"&newPwd="+newPwd+"&confirmPwd="+confirmPwd+"&userCode="+""; //清除在主界面进行修改时的遗留数据
}
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

 $.post('<%=path%>/editPwdAction.action', dataParaments, function(data) {
   if(data == "修改成功！"){
   		window.returnValue = "success";
   		window.close();
   }else if(data.indexOf("j_spring_security_check")>0){
   		window.returnValue = "loginout";
   		window.close();
   }else{
   		wfAlert(data);
   }
 });
}



function checkPassword(password){
    var score = 0;
    if (password.length < 6 ) { return -6; }
    score += password.length * 5;
    if (password.match(/(.*[0-9].*[0-9].*[0-9])/)){ score += 5;}
    if (password.match(/(.*[!,@,#,$,%,^,&,*,?,_,~].*[!,@,#,$,%,^,&,*,?,_,~])/)){ score +=10 ;}
    if (password.match(/([a-z].*[A-Z])|([A-Z].*[a-z])/)){ score += 20;}
    if (password.match(/([a-zA-Z])/) && password.match(/([0-9])/)){ score += 20;}
    if (password.match(/([!,@,#,$,%,^,&,*,?,_,~])/) && password.match(/([0-9])/)){ score += 20;}
    if (password.match(/([!,@,#,$,%,^,&,*,?,_,~])/) && password.match(/([a-zA-Z])/)){score += 20;}
    if (password.match(/^\w+$/) || password.match(/^\d+$/) ){ score -= 10;}
    if ( score < 0 ){score = 0;}
    if ( score > 160 ){ score = 160;}
    score=score-(checkLeval-3)*15;
    return score;
     }
    function repeat(len,str){
    var res = "";
    for (var i = 0; i < str.length; i++ ){
        var repeated = true;
        for (var j = 0, max = str.length - i - len; j < len && j < max; j++){
            repeated = repeated && (str.charAt(j + i) == str.charAt(j + i + len));
        }
        if (j < len) repeated = false;
        if (repeated) {
            i += len - 1;
            repeated = false;
        }else{
            res += str.charAt(i);
        }
    }
    return res;
}

function validatePassword(passwordInput){
    var score = checkPassword(passwordInput.value);
    var color_short="#FF0000";
    var color_weak="#FF8C69";
    var color_normal="#FFE4B5";
    var color_good="#CAFF70";
    var color_veryGood="#B3EE3A";
    var password_label = document.getElementById('password_label');
    var text;
    var color=color_short;
	    if(score == -6)    {
	         text= '太短';
	    }else{
	    text = score < 60 ? '弱' : (score < 90 ? '一般' :(score<130?'较好':'很好') );
	    color = score < 60 ? color_weak : (score < 90 ? color_normal :( score<130?color_good:color_veryGood));   
	    }
           
        var width_ = score*1.2+40>200?200:score+40;
        width_=width_<40?40:width_;
        var width=width_+"px";
        password_label.innerHTML = "<span style='width:"+ width +
        ";display:block;overflow:hidden;height:20px;line-height:20px;background:"+ color
        +";'>"+text+"</span>";
}

function submitModify(evt){
if(evt==null){
evt=window.event;
}
if(evt.keyCode==13){
document.getElementById("saveButton").click();
}
}

</script>
</head>
<body style="font-size:12px;background-color:#E8EEEB;overflow: hidden;" onkeyup="submitModify()">


	<div class="mws-panel grid_8">
		<div class="mws-panel-header">
			<span>修改密码</span>
		</div>
		<div class="mws-panel-body">
			<form class="mws-form" name="myform"
				action="<%=path%>/addOrganize.action">

				<div class="mws-form-inline">
					<table>
						<script type="text/javascript">
			                   var userId="<%=request.getParameter("userNo")%>";
							if (userId == null || userId == ""
									|| userId == "null") {
								var string = "<tr><td><div class='mws-form-row'>"
										+ "<label>用户名</label>"
										+ "<div class='mws-form-item small'>"
										+ "<input type='text' id='userCode'  class='mws-textinput' style='width:200px;'/>"
										+ "</div>" + "</div></td></tr>";
								document.write(string);
							}
						</script>
						<tr>
							<td>
								<div class="mws-form-row">
									<label>原密码</label>
									<div class="mws-form-item small">
										<input type="password" id="currPwdId" class="mws-textinput"
											style="width:200px;" />
									</div>
								</div></td>
						</tr>
						<tr>
							<td>
								<div class="mws-form-row">
									<label>新密码</label>
									<div class="mws-form-item small">
										<input type="password" id="newPwdId" name="newPwd"
											onblur="validatePassword(this)"
											onclick="validatePassword(this)"
											onkeyup="validatePassword(this)" maxlength="32"
											class="mws-textinput" style="width:200px;" /> <span
											id="password_label"
											style="width:200px;border:1px solid #F0F0F0"></span>
									</div>
								</div></td>
						</tr>
						<tr>
							<td>
								<div class="mws-form-row">
									<label>确认新密码</label>
									<div class="mws-form-item small">
										<input type="password" id="confrimPwdId" name="confrimPwd"
											maxlength="32" class="mws-textinput" style="width:200px;" />
									</div>
								</div></td>
						</tr>
					</table>
				</div>

				<div class="mws-button-row">
					<input type="button" id="saveButton" value="保存"
						class="mws-button blue" onclick="savePwd();" /> <input
						type="button" value="取消" class="mws-button blue"
						onclick="javascript:window.close();" />

				</div>
			</form>
		</div>
	</div>
</body>
</html>


