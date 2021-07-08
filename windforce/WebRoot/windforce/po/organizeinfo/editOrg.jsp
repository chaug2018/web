<%--
  机构信息修改页面                 
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
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>机构修改页面</title>
<link type="text/css" rel="stylesheet" href="<%=path%>/windforce/common/css/core.css" />
<%@ include file="/common/wf_import.jsp"%>

<script type="text/javascript">
function onSaveOrg(){
var organizeName = document.forms[0].elements["orgInfo.orgName"].value;
var orgNo = document.forms[0].elements["orgInfo.orgNo"].value;
var orgType = document.forms[0].elements["orgInfo.orgType"].value;
var parentCode = document.forms[0].elements["parentOrgNo"].value;
var manageType = document.forms[0].elements["orgInfo.manageType"].checked == true ? 1 : 0;

if(top.isNull(organizeName)){
	top.wfAlert("机构名称不能为空!");
	return false;
}
if(!top.isGeneralName(organizeName)){
	top.wfAlert("机构名称含有非法字符!");
	return false;
}
if(top.isNull(orgNo)){
	top.wfAlert("机构代码不能为空!");
	return false;
}
if(!top.isAlphaAndDigits(orgNo)){
	top.wfAlert("机构代码只能是字母或数字!");
	return false;
}
//added by chenhuang 2013-1-23 当机构类型被修改时，提示用户是否确认操作
var oldOrgType = '<s:property value="orgInfo.orgType"/>';
if(oldOrgType != orgType){
	var orgTypeObj = document.forms[0].elements["orgInfo.orgType"];
	var newOrgTypeName = orgTypeObj.options[orgTypeObj.selectedIndex].text;
	if(!(window.confirm("您确认将该机构机构类型修改为‘"+newOrgTypeName+"’?","提示信息"))){
		return false;
	}
}
var dataParaments = "orgInfo.orgName="+organizeName+"&orgInfo.orgNo="+orgNo+"&orgInfo.orgType="+orgType+"&parentOrgNo="+parentCode+"&orgInfo.manageType="+manageType;
var operAuth=new top.OperAuth();
operAuth.operType="editOrganization";
operAuth.authSuccess=function(){
    top.startProcess("正在提交机构修改,请稍等...");
// 这里提交用ajax目的是为了不刷新页面，好控制iframe层次的显示与隐藏
    $.post('<%=path%>/editOrganize_doEdit.action', dataParaments, function(data) {
	   // TODO action 写验证，验证通过就跳转，不通过就弹出消息
	   // 添加完后隐藏右栏
	   if(data == "1")
	   {
	   // 修改成功设置
	   /* window.returnValue = orgNo;
	   window.close(); */
	   var sid = '<s:property value="sid"/>';
	   top.closeShowPage();
	   top.refreshLeft();
	   top.relDetail(sid);
	   
	   } else if(data == "2")
	   {
	   top.stopProcess();
	   top.wfAlert("修改失败，请联系管理员！");
	   }
	   else {
	   top.stopProcess();
	   top.wfAlert(data);
	   }
	   });
};
 operAuth.auth();
}
function setParentOrg(flag)
{
   // 获取下拉框对象
   var parentOrgs = document.forms[0].elements["parentOrgNo"];
   var currTypeId = document.forms[0].elements["orgInfo.orgType"].value;
   parentOrgs.length = 0;
   var isIE=navigator.userAgent.toUpperCase().indexOf("MSIE")==-1?false:true;
  // 通过ajax过滤父机构
   $.getJSON('<%=path%>/getParentOrg.action?typeId=' + currTypeId, null,
				function(data) {

					// 动态填充下拉框数据
					for (org in data) {
						if (isIE) {
							parentOrgs.add(new Option(data[org].orgName,
									data[org].orgNo));
						} else {
							parentOrgs.add(new Option(data[org].orgName,
									data[org].orgNo), null);
						}
					}
					if(flag){
						parentOrgs.value = '<s:property value="parentOrgNo"/>';
					}
				});
	}
</script>
</head>
<body style="font-size:12px;background-color:#E8EEEB;overflow: hidden;">
	<form class="mws-form" name="myform"
		action="<%=path%>/addOrganize.action">

		<div id="login_bg" style="filter:alpha(opacity:75);opacity:0.75;"></div>
		<div id="login_nov">
			<table >
				<tr>
					<td width="20%" align="center" class="font_colors06">机构名称</td>
					<td width="30%"> <input type="text"
							name="orgInfo.orgName"
							value="<s:property value='orgInfo.orgName'/>"
							class="mws-textinput" maxlength="50"/></td>
					<td width="20%" align="center" class="font_colors06">机构代码</td>
					<td width="30%"><input type="text" name="orgInfo.orgNo" disabled="true"
						value="<s:property value='orgInfo.orgNo' />" class="code_transform" maxlength="16"/>
					</td>
				</tr>
				<tr>
					<td align="center" class="font_colors06">机构类型</td>
					<td><label> <s:select list="initTypeList" style="width:155px;"
								name="orgInfo.orgType" onchange="setParentOrg();" listKey="sid"
								listValue="orgTypeName" disabled="true"></s:select> </label></td>
					<td align="center" class="font_colors06">上级机构</td>
					<td><s:select list="{}" name="parentOrgNo" 
							listKey="orgNo" listValue="orgName"  disabled="true" style="width:155px;"></s:select>
					</td>
				</tr>
				<tr style="display: none">
					<td align="center">&nbsp;</td>
					<td colspan="3" class="font_colors06" ><s:if
							test="orgInfo.manageType == 1">
							<input name="orgInfo.manageType" value="1" type="checkbox"
								checked="checked" />
						</s:if> <s:if test="orgInfo.manageType == 0">
							<input name="orgInfo.manageType" value="1" type="checkbox" />
						</s:if> 自主管理</td>
				</tr>
				<tr>
					<td align="center">&nbsp;</td>
					<td colspan="3" align="right"><input class="submit_but04" type="button"
						onclick="onSaveOrg();" value="保存" /> <input type="reset"
						class="submit_but04" value="重置"
						onclick="setParentOrg(true);" />
					</td>
				</tr>
				<tr>
					<td colspan="4" align="center"><label></label></td>
				</tr>
			</table>
		</div>
	</form>
</body>
<script type="text/javascript">
	//added by chenhuang 20130123
	//初始化上级机构类型
	setParentOrg(true);
	top.stopProcess();
</script>
</html>


