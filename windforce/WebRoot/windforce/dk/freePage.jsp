<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
  <%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>解除锁定</title>
<%@ include file="/common/wf_import.jsp"%>
<script type="text/javascript">
var errCount=0;
function keyDown(element){
       var oEvent = window.event;
         if (oEvent.keyCode == 13) {
			if(element.value!=""){
			$.post("<%=path%>/checkPassword.action","password="+element.value,function(data){
			if(data=="true"){
			 closeShowPage_lockPage();
			}else if(data=="false"){
			 errCount++;
			 if(errCount>=3){
			 wfAlert("密码连续输入错误3次，将退出系统!",false,null,function(){
			 WFUnload.clear();
			 window.location.href ="${pageContext.request.contextPath }/j_spring_security_logout";
			 });
			}else{
			wfAlert("密码输入错误!",false,null,function(){
			document.getElementById("password").focus();
			});
			}
			}else{
			 wfAlert("当前用户已经被登出!",false,null,function(){
			 WFUnload.clear();
			 window.location.href ="${pageContext.request.contextPath }/j_spring_security_logout";
			 });
			}
			});
			}
         };	
}
</script>
</head>
<body>
<table>
<tr>
<td>密码:<input type="password" id="password" onkeydown="keyDown(this)"></input></td>
</tr>
<tr>

</tr>
</table>
<script type="text/javascript">
document.getElementById("password").focus();
</script>
</body>
</html>