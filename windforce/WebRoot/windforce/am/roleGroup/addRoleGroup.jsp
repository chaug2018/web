<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<%--
  增加一个角色组
@date          2012/06/17         
@author       蒋正秋           
@version       1.0                
--%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>角色管理页面</title>
<link type="text/css" rel="stylesheet" href="<%=path%>/windforce/common/css/core.css" />
<link type="text/css" rel="stylesheet" href="<%=path%>/windforce/common/css/ztree.css" />
<%@ include file="/common/wf_import.jsp"%>
<script type="text/javascript" src="<%=path%>/common/js/jquery/ztree/jquery.ztree.all-3.1.js"></script>

<script type="text/javascript">
function doAddRoleGroup(){
	var roleGroupName = document.getElementById("roleGroupName").value;
	var memo = document.getElementById("memo").value;
	if (top.isNull(roleGroupName)) {
		top.wfAlert("岗位名称不能为空！");
		return false;
	} else if (!top.isGeneralName(roleGroupName)) {
		top.wfAlert("岗位名称含有非法字符！");
		return false;
	} else if (top.isNull(memo)) {
		top.wfAlert("岗位描述不能为空！");
		return false;
	} else if (!top.isGeneralName(memo)) {
		top.wfAlert("岗位描述含有非法字符！");
		return false;
	} else {
		var checkBox = document.getElementsByName("roles");
		var string = "";
		for ( var i = 0; i < checkBox.length; i++) {
			if (checkBox[i].checked) {
				if (string.length != 0) {
					string = string + ",";
				}
				string = string + (checkBox[i].value);
			}
		}
		var operAuth=new top.OperAuth();
		operAuth.operType="addRoleGroup";
		operAuth.authSuccess=function(){
			top.startProcess("正在进行输入信息的校验,请稍等...");
			$.post('<%=path%>/rolegroup_validateRoleGroup.action',
						"validateType=add&gourpInfo.roleGroupName=" + roleGroupName
								+ "&rolesString=" + string, function(data) {
							if (data == "success") {
								top.changeProcessTitle("校验通过,正在提交岗位信息,请稍等...");
								document.forms[0].submit();
							} else {
								top.stopProcess();
								top.wfAlert(data);
							}
						});
		};
		operAuth.auth();
	}
}
</script>
</head>
<body
	style="background-color: transparent;background-image: none;width: 760; overflow-x: hidden;overflow-y:auto; ">
	<form name="addRoleGroup" action="<%=path%>/rolegroup_doAdd.action"
		method="post">
		<div id="nov_nr" style="width: 100%;height:20%">
			<div class="nov_moon" style="width: 100%;height: 100%;">
				<table width="100%" border="0" align="center">
					<tr>
						<td width="38%">岗位名： <input type="text"
							name="gourpInfo.roleGroupName" id="roleGroupName" maxlength="20" />
						</td>
						<td width="62%">描述： <input name="gourpInfo.memo" type="text"
							id="memo" size="48" maxlength="50" />
						</td>
					</tr>
					<tr>

						<td colspan="2" height="10"></td>
					</tr>
					<tr>
						<td colspan="2" valign="top" align="left">角&nbsp;&nbsp;&nbsp;&nbsp;色&nbsp;&nbsp;&nbsp;&nbsp;<br />
							<div class="uoon" style="overflow: auto; height: auto; width:80%">
								<!-- TODO这里遍历所有的角色 -->
								<s:iterator id="roleInfo" value="allRoleList">
									<s:if test="#roleInfo.roleState ==  0">

										<input type="checkbox" name="roles"
											id="<s:property value='#roleInfo.sid'/>"
											value="<s:property value='#roleInfo.sid'/>" />
										<s:property value='#roleInfo.roleName' />&nbsp;&nbsp;
   </s:if>
								</s:iterator>

							</div></td>
					</tr>
					<tr>
						<td height="10"></td>
					</tr>
					<tr>
						<td width="100%" colspan="2">适用机构类型&nbsp;&nbsp;
							<div class="uoon" style="overflow: auto; height: auto;width:80%">
								<s:if test="allOrgTypeList.size() < 1">&nbsp;暂无适用机构类型</s:if>
								<s:if test="allOrgTypeList.size() > 0">
									<s:iterator value="allOrgTypeList" id="orgType">
										<input type="checkbox" name="orgTypes"
											id="<s:property value='#orgType.sid'/>"
											value="<s:property value='#orgType.sid'/>" />&nbsp; <s:property
											value="#orgType.orgTypeName" />
									</s:iterator>
								</s:if>

							</div>
						</td>
					</tr>
					<tr>
						<td height="10"></td>
					</tr>
					<tr>
						<td width="100%" colspan="2">当前互斥岗位列表&nbsp;&nbsp;
							<div class="uoon" style="overflow: auto; height: auto; width:80%">



								<s:if test="allRoleGroupList.size() > 0">
									<!-- 这里遍历所有的岗位 -->
									<s:iterator id="org" value="allRoleGroupList">
										<s:if test="#org.roleGroupState ==  0">

											<input type="checkbox" name="mutexGroup"
												id="<s:property value='#org.sid'/>"
												value="<s:property value='#org.sid'/>" />
											<s:property value='#org.roleGroupName' />&nbsp;&nbsp;
   </s:if>
									</s:iterator>
								</s:if>


								<s:if test="allRoleGroupList.size() < 1">&nbsp;暂无互相排斥岗位</s:if>


							</div>
						</td>
					</tr>
					<tr>
						<td colspan="2"><table width="100%" border="0"
								cellspacing="0" cellpadding="0">



								<tr>
									<td colspan="2" valign="top"><input type="button"
										onclick="doAddRoleGroup();" class="submit_but05" value="保存" />&nbsp;&nbsp;<input
										type="reset" class="submit_but05" value="重置" />
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td colspan="2" height="10"></td>
					</tr>
				</table>
			</div>
		</div>
	</form>
	<script type="text/javascript">
		top.stopProcess();
	</script>

</body>
</html>


