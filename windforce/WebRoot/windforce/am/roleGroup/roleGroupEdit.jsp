
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
  角色编辑页面                
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
function doUpdate()
{
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
		var groupName = document.getElementById("roleGroupName").value;
		var groupSid = '<s:property value="gourpInfo.sid"/>';
		//获取所有角色
		var checkBoxRoles = document.getElementsByName("roles");
		var roles = "";
		for ( var i = 0; i < checkBoxRoles.length; i++) {
			if (checkBoxRoles[i].checked) {
				if (roles.length != 0) {
					roles = roles + ",";
				}
				roles = roles + (checkBoxRoles[i].value);
			}
		}
		//获取所有使用机构类型
		var checkBoxOrgTypes = document.getElementsByName("orgTypes");
		var orgTypeStr = "";
		for(var i = 0; i < checkBoxOrgTypes.length; i++){
			if (checkBoxOrgTypes[i].checked) {
				if (orgTypeStr.length != 0) {
					orgTypeStr = orgTypeStr + ",";
				}
				orgTypeStr = orgTypeStr + (checkBoxOrgTypes[i].value);
			}
		}
		//获取所有互斥岗位
		var checkBoxMutexGroups = document.getElementsByName("mutexGroup");
		var mutexGroupStr = "";
		for(var i = 0; i < checkBoxMutexGroups.length; i++){
			if (checkBoxMutexGroups[i].checked) {
				if (mutexGroupStr.length != 0) {
					mutexGroupStr = mutexGroupStr + ",";
				}
				mutexGroupStr = mutexGroupStr + (checkBoxMutexGroups[i].value);
			}
		}
		var operAuth=new top.OperAuth();
		operAuth.operType="editRoleGroup";
		operAuth.authSuccess=function(){
			top.startProcess("正在对输入的信息进行校验,请稍等...");
			$.post('<%=path%>/rolegroup_validateRoleGroup.action',
						"validateType=edit&gourpInfo.roleGroupName=" + groupName
								+ "&gourpInfo.sid=" + groupSid + "&rolesString="
								+ roles + "&mutexGroupStr=" + mutexGroupStr
								+ "&orgTypeStr=" + orgTypeStr, function(data) {
							if (data == "success") {
								top.changeProcessTitle("校验通过,正在刷新界面...");
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
	style="background-color: transparent;background-image: none;width: 760; height: auto;overflow-x: hidden;overflow-y:auto; ">
	<form name="doUpdateRoleGroup"
		action="<%=path%>/rolegroup_doUpdate.action" method="post">
		<div id="nov_nr" style="width: 100%;height:20%">
			<span class="disblock">
			</span>
			<div class="nov_moon" style="width: 100%;height: 100%;">
				<table width="100%" border="0" align="center">
					<tr>
						<td width="38%">岗位名： <s:textfield
								name="gourpInfo.roleGroupName" id="roleGroupName" maxlength="20" />
						</td>
						<td width="62%">描述： <s:textfield name="gourpInfo.memo"
								id="memo" size="48" maxlength="50" /></td>
					</tr>
					<tr>
						<td colspan="2" height="10"></td>
					</tr>
					<tr>
						<td colspan="2" valign="top" align="left">角&nbsp;&nbsp;&nbsp;&nbsp;色&nbsp;&nbsp;&nbsp;&nbsp;<br />
							<div class="uoon" style="overflow: auto; height: auto;width:80%">
							<table>
								<!-- TODO这里遍历所有的角色 -->
								<s:iterator id="roleInfo" value="allRoleList" status="index">
								<s:if test="#index.index%4==0">
								<tr>
								</s:if>
							 	   <td>
									<s:if test="#roleInfo.roleState ==  0">
										<s:if test="gourpInfo.roleInfos.get(#roleInfo.sid) == null">
											<input type="checkbox" name="roles"
												id="<s:property value='#roleInfo.sid'/>"
												value="<s:property value='#roleInfo.sid'/>" />
										</s:if>
										<s:if test="gourpInfo.roleInfos.get(#roleInfo.sid) != null">
											<input type="checkbox" checked="checked" name="roles"
												id="<s:property value='#roleInfo.sid'/>"
												value="<s:property value='#roleInfo.sid'/>" />
										</s:if>
										<s:property value='#roleInfo.roleName' />&nbsp;&nbsp;
   									</s:if>
   									</td>
   							   <s:if test="#index.index%4==3">
								</tr>
								</s:if>
								</s:iterator>
								</table>
							</div>
						</td>
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
										<s:if test="gourpInfo.orgTypes.get(#orgType.sid) == null">
											<input type="checkbox" name="orgTypes"
												id="<s:property value='#orgType.sid'/>"
												value="<s:property value='#orgType.sid'/>" />&nbsp;
            							</s:if>
										<s:if test="gourpInfo.orgTypes.get(#orgType.sid) != null">
											<input type="checkbox" checked="checked" name="orgTypes"
												id="<s:property value='#orgType.sid'/>"
												value="<s:property value='#orgType.sid'/>" />&nbsp;
            							</s:if>
										<!--       <a href="<%=path%>/roles_roleDetail.action?detailSid=<s:property value='#orgType.sid'/>"> -->
										<s:property value="#orgType.orgTypeName" />
										<!--       </a> -->
									</s:iterator>
								</s:if>

							</div></td>
					</tr>
					<tr>
						<td height="10"></td>
					</tr>
					<tr>
						<td width="100%" colspan="2">当前互斥岗位列表&nbsp;&nbsp;
							<div class="uoon" style="overflow: auto; height: auto; width:80%">
							<table width="100%">
								<s:if test="allRoleGroupList.size() > 0">
									<!-- 这里遍历所有的岗位 -->
									<s:iterator id="org" value="allRoleGroupList" status="index">
										<s:if test="#org.roleGroupState ==  0">		
											<s:if test="#index.index%4==0">
								         	  <tr>
									        </s:if>		 
								        <td>       
											<s:if
												test="gourpInfo.mutexRoleGouptInfos.get(#org.sid) != null">
												<input type="checkbox" checked="checked" name="mutexGroup"
													id="<s:property value='#org.sid'/>"
													value="<s:property value='#org.sid'/>" />
											</s:if>
											<s:if
												test="gourpInfo.mutexRoleGouptInfos.get(#org.sid) == null">
												<s:if test="#org.sid != gourpInfo.sid">
													<input type="checkbox" name="mutexGroup"
														id="<s:property value='#org.sid'/>"
														value="<s:property value='#org.sid'/>" />
												</s:if>
												<s:else>
												<input type="checkbox" name="mutexGroup" disabled="disabled"
														id="<s:property value='#org.sid'/>"
														value="<s:property value='#org.sid'/>" />
												</s:else>
											</s:if>
										
												<s:property value='#org.roleGroupName' />&nbsp;&nbsp;
										
																			
											</td>
											<s:if test="#index.index%4==3">
							         			<tr>
								            </s:if>
										</s:if>
									</s:iterator>
								</s:if>
								<s:if test="allRoleGroupList.size() < 2">&nbsp;暂无可编辑的互相排斥岗位 </s:if>
							</table></div></td>
					</tr>
					<tr>
						<td colspan="2"><table width="100%" border="0"
								cellspacing="0" cellpadding="0">
								<tr>
									<td colspan="2" valign="top"><input type="button"
										onclick="doUpdate();" class="submit_but05" value="保存" />&nbsp;&nbsp;<input
										type="reset" class="submit_but05" value="重置" /></td>
								</tr>
							</table></td>
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


