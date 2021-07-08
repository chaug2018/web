
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
<title>角色管理页面</title>
<link type="text/css" rel="stylesheet" href="<%=path%>/windforce/common/css/core.css" />
<link type="text/css" rel="stylesheet" href="<%=path%>/windforce/common/css/ztree.css" />
<%@ include file="/common/wf_import.jsp"%>
<script type="text/javascript" src="<%=path%>/common/js/jquery/ztree/jquery.ztree.all-3.1.js"></script>
<script type="text/javascript">
$(document).ready(function() {
$.getJSON("<%=path%>/roles_buildAllResourceTree.action",
	function(json) {
		var setting = {
			check : {
				enable : true,
				chkStyle : "checkbox",
				chkboxType : {
					"Y" : "ps",
					"N" : "s"
				}
			},
			callback : {
				onMouseDown : zTreeOnMouseDown
			/*给所有节点添加了鼠标左键按下的监听*/
			}
		};
		var zNodes = json;

		$.fn.zTree.init($("#tree_rolequery"), setting,
				zNodes);
		function zTreeOnMouseDown(event, treeId, treeNode) {

		}
		;
	});
});

// 添加角色
function doAdd() {
	var roleName = document.getElementById("roleName").value;
	var memo = document.getElementById("memo").value;
	if (top.isNull(roleName)) {
		top.wfAlert("角色名称不能为空！");
		return false;
	} else if (!top.isGeneralName(roleName)) {
		top.wfAlert("角色名称含有非法字符！");
		return false;
	} else if (top.isNull(memo)) {
		top.wfAlert("角色描述不能为空！");
		return false;
	} else if (!top.isGeneralName(memo)) {
		top.wfAlert("角色描述含有非法字符！");
		return false;
	} else {
		//遍历树，看哪些节点被选择了。
		var zTree = $.fn.zTree.getZTreeObj("tree_rolequery"), nodes = zTree
				.getChangeCheckedNodes();
		var len = nodes.length;
		// 遍历已选资源
		var selPowers = "";
		if (len > 0) {
			for ( var i = 0; i < len; i++) {
				selPowers += nodes[i].sid + ",";
			}
			selPowers = selPowers.substring(0, selPowers.length - 1);
			document.getElementById("resourceIds").value = selPowers;
		}
		//获取所选岗位
		var roleGroupsCheckBox = document.getElementsByName("roleGroups");
		var roleGroupString = "";
		for ( var i = 0; i < roleGroupsCheckBox.length; i++) {
			if (roleGroupsCheckBox[i].checked) {
				if (roleGroupString.length != 0) {
					roleGroupString = roleGroupString + ",";
				}
				roleGroupString = roleGroupString + (roleGroupsCheckBox[i].value);
			}
		}
		//获取所选互斥角色
		var mutexRolesCheckBox = document.getElementsByName("mutexRoles");
		var mutexRoleString = "";
		for ( var i = 0; i < mutexRolesCheckBox.length; i++) {
			if (mutexRolesCheckBox[i].checked) {
				if (mutexRoleString.length != 0) {
					mutexRoleString = mutexRoleString + ",";
				}
				mutexRoleString = mutexRoleString + (mutexRolesCheckBox[i].value);
			}
		}
		var operAuth=new top.OperAuth();
	    operAuth.operType="addRole";
		operAuth.authSuccess=function(){
			top.startProcess("正在进行输入信息的验证,请稍等...");
			$.post('<%=path%>/roles_validateRoleGroup.action',
						"validateType=add&roleInfo.roleName=" + roleName
								+ "&roleGroupString=" + roleGroupString
								+ "&mutexRoleString=" + mutexRoleString, function(
								data) {
							if (data == "success") {
								top.changeProcessTitle("验证通过,正在提交角色信息...");
								document.forms["addRole"].submit();
							} else {
								top.stopProcess();
								top.wfAlert(data);
							}
						});
		};
		operAuth.auth();
	}
}

	function resetResourcesTree() {
		$.fn.zTree.getZTreeObj("tree_rolequery").checkAllNodes(false);
	}
</script>
</head>
<body
	style="background-color: transparent;background-image: none;width: 760; overflow-x: hidden;overflow-y:auto; ">
	<form name="addRole" action="<%=path%>/roles_doAdd.action"
		method="post">
		<div id="nov_nr" style="width: 100%;height:20%">
			<span class="disblock"> <!-- <img src="<%=path%>/common/images/mo2012.png" />-->
			</span>

			<div class="nov_moon"
				style="width: 100%;height: 100%;  margin-top:0px">
				<table width="100%" border="0" align="center">
					<tr>
						<td width="38%">角色名： <input type="text"
							name="roleInfo.roleName" id="roleName" maxlength="20" />
						</td>
						<td width="62%">描述： <input name="roleInfo.memo" type="text"
							id="memo" size="48" maxlength="50" />
						</td>
					</tr>
					<tr>
						<td colspan="2" valign="top">岗&nbsp;&nbsp;&nbsp;&nbsp;位&nbsp;&nbsp;&nbsp;&nbsp;

							<s:iterator value="allRoleGroupList" id="roleGroup">
								<input type="checkbox" name="roleGroups"
									id="<s:property value='#roleGroup.sid'/>"
									value="<s:property value='#roleGroup.sid'/>" />
								<s:property value='#roleGroup.roleGroupName' />&nbsp;&nbsp;
      </s:iterator></td>
					</tr>
					<tr>
						<td width="100%" colspan="2">互斥角色&nbsp;&nbsp; <s:iterator
								value="roleList" id="role">
								<input type="checkbox" name="mutexRoles"
									id="<s:property value='#role.sid'/>"
									value="<s:property value='#role.sid'/>" />
								<s:property value='#role.roleName' />&nbsp;&nbsp;</s:iterator></td>
					</tr>
					<tr>
						<td>资源列表:</td>
					</tr>
					<tr>
						<td colspan="2"><div class="uoon" style="overflow: auto;">
								<ul id="tree_rolequery" class="ztree">
								</ul>
							</div>
						</td>
					</tr>

					<tr>
						<td colspan="2"><table width="100%" border="0"
								cellspacing="0" cellpadding="0">



								<tr>
									<td colspan="2" valign="top"><input type="button"
										onclick="doAdd();" class="submit_but05" value="保存" />&nbsp;&nbsp;
										<input type="reset" class="submit_but05"
										onclick="resetResourcesTree();" value="重置" />
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
		<input type="hidden" name="resourceIds" id="resourceIds" />
	</form>

	<script type="text/javascript">
		top.stopProcess();
	</script>
</body>
</html>


