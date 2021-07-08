
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<%@ taglib prefix="s" uri="/struts-tags"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%--
  角色管理页面                
@date          2012/06/17         
@author       蒋正秋           
@version       1.0                
--%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>岗位管理页面</title>

<link type="text/css" rel="stylesheet" href="<%=path%>/windforce/common/css/core.css" />
<%@ include file="/common/wf_import.jsp"%>

<script type="text/javascript">
function doDel(operType,operId)
{
var dparameter = "";
var isCheck = "false";
if(operType == "one")
{
dparameter = operId;
}else 
{

var delIds = document.getElementsByName("ids");
for(var i = 0 ; i < delIds.length;i++)
{
if(delIds[i].checked)
{
isCheck = "true";
dparameter+=delIds[i].value+",";
}
}

dparameter = dparameter.substring(0,dparameter.length - 1);
}
if(isCheck == "true" || operType == "one")
{
if(confirm("确定要删除吗？"))
{

var operAuth=new top.OperAuth();
operAuth.operType="deleteRoleGroup";
operAuth.authSuccess=function(){
top.startProcess("正在提交删除请求,请稍等...");
 $.post('<%=path%>/rolegroup_delRoles.action', "delIds="+dparameter, function(data) {
  top.wfAlert(data);
 if(data == "删除成功！")
 {
 top.changeProcessTitle("删除完毕,正在刷新界面...");
 $.post('<%=path%>/rolegroup_ajaxRoleList.action', null, function(data) {
 $("#refreshData").html(data);
 });
 document.getElementById("roleOperate").src="<%=path%>/rolegroup_toDetail.action?detailSid=";
 }else{
 top.stopProcess();
 }
 });
 };
operAuth.auth();
 }
 }else{
 top.wfAlert("请选择要删除的岗位！");
 }
}

function addRoleGroup(){
top.startProcess("正在初始化新增岗位界面,请稍等...");
document.getElementById("roleOperate").src="<%=path%>/rolegroup_initAdd.action";
}
function modifyRoleGroup(sid){
top.startProcess("正在初始化岗位编辑界面,请稍等...");
document.getElementById("roleOperate").src="<%=path%>/rolegroup_initUpdate.action?editId="+sid;
}

// 刷新角色组列表
function refreshRoleGroup()
{
 $.post('<%=path%>/rolegroup_ajaxRoleList.action', null, function(data) {
//document.getElementById("refreshData").innerHTML = data;
$("#refreshData").html(data);
 });
}
</script>
</head>
<body style="margin-right: auto;background-color: transparent;">
	<input id="moudleName" type="hidden" value="岗位管理" />

	<div class="nov_moon">
		<div id="am_left">
			<input type="button" onclick="addRoleGroup();" class="submit_but05"
				value="新增" /><input type="button" onclick="doDel('all','');"
				class="submit_but05" value="删除" />
			<div id="refreshData">
				<table width="100%" border="0" align="center" cellpadding="0"
					cellspacing="0">
					<tr>
						<td width="10%" align="center" class="font_colors07"></td>
						<td width="50%" height="26" align="center" class="font_colors07">岗位名</td>
						<%--    <td width="15%" align="center"  class="font_colors07">状态</td>--%>
						<td width="25%" align="center" class="font_colors07">操作</td>

					</tr>
					<s:iterator id="roleGroup" value="allRoleGroupList" status="index">
						<tr <s:if test="#index.index%2 ==0"></s:if>
							<s:if test="#index.index%2 ==1">bgcolor="#F4DDDF"</s:if>>
							<td align="center" class="border_bottom01">&nbsp; <s:if
									test="#roleGroup.sid != 'supergroup'">
									<input type="checkbox" name="ids"
										value='<s:property value="#roleGroup.sid"/>' />
								</s:if>
							</td>
							<td height="28" align="center" class="border_bottom01">&nbsp;<a
								href="<%=path%>/rolegroup_toDetail.action?detailSid=<s:property value='#roleGroup.sid'/>"
								target="roleOperate"
								onclick="top.startProcess('正在获取岗位信息,请稍等...');"><s:property
										value="#roleGroup.roleGroupName" />
							</a>
							</td>
							<%--    <td align="center" class="border_bottom01" >&nbsp;<s:if test="#roleGroup.roleGroupState == 0">正常</s:if><s:if test="#roleGroup.roleGroupState == 1">冻结</s:if></td>--%>

							<td width="15%" align="left" class="border_bottom01"><input
								type="button" style="width:40px"
								onclick="modifyRoleGroup('<s:property value='#roleGroup.sid'/>');"
								class="submit_but05" value="修改" /> <s:if
									test="#roleGroup.sid != 'supergroup'">
									<input type="button" style="width:40px"
										onclick="doDel('one','<s:property value='#roleGroup.sid'/>');"
										class="submit_but05" value="删除" />
								</s:if></td>
						</tr>
					</s:iterator>
				</table>
			</div>
		</div>
		<div id="main"
			style="position:absolute;left:38%;margin-top: 10px;width:60%;height:90%;float:left">
			<iframe id="roleOperate" name="roleOperate" allowtransparency=true
				frameborder="0"
				src="<%=path%>/rolegroup_toDetail.action?detailSid="
				style="isibility: inherit; background-color:transparent; margin-top:0px; height: 100%;width: 100%;overflow-y: auto;"
				frameborder="0"></iframe>
		</div>
	</div>
</body>
</html>


