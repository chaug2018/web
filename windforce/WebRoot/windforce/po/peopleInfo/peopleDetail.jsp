


<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<%@ taglib prefix="s" uri="/struts-tags"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%--
  机构人员详细页面                 
@date          2012/04/25         
@author        蒋正秋              
@version       1.0                
--%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>添加修改页面</title>
<link type="text/css" rel="stylesheet"
	href="<%=path%>/windforce/common/css/core.css" />
<%@ include file="/common/wf_import.jsp"%>
<script type="text/javascript">
// 重置密码
function resetPwd()
{
	if(confirm("您确定要重置该用户密码?")){
		var currId = "<s:property value='peopleInfo.sid'/>";
		 $.post('<%=path%>/resetPwdAction.action', "userNo=" + currId,
					function(data) {
						wfAlert(data);
					});
		}
	}
</script>
</head>
<body>
<div style="background-color: #E8EEEB ;height: 100%">
	<table cellspacing="0" width="500px">
		<tr>
			<td width="20%" class="border_bottom01">用户代码：</td>
			<td width="80%" align="left" class="border_bottom01"><s:property
					value="peopleInfo.peopleCode" /></td>
		</tr>
		<tr>
			<td width="20%" class="border_bottom01">用户姓名：</td>
			<td width="80%" align="left" class="border_bottom01"><s:property
					value="peopleInfo.peopleName" /></td>
		</tr>
		<tr>
			<td width="20%" class="border_bottom01">性别：</td>
			<td width="80%" align="left" class="border_bottom01"><s:if
					test="peopleInfo.peopleGender == 0">男</s:if> <s:if
					test="peopleInfo.peopleGender == 1">女</s:if></td>
		</tr>
		<tr>
			<td width="20%" class="border_bottom01">所属机构：</td>
			<td width="80%" align="left" class="border_bottom01"><s:property
					value="organizeInfo.orgName" /></td>
		</tr>
		<tr>
			<td width="20%" class="border_bottom01">岗位列表：</td>
			<td width="80%" align="left" class="border_bottom01"><s:iterator
					value="peopleRoleGroupList" var="roleGroup" status="index">
					<s:property value='#roleGroup.roleGroupName' />
					<s:if test="#index.index != 0 && #index.index%3 == 0">
						<br />
					</s:if>
				</s:iterator></td>
		</tr>
	</table>
	<div align="right">
		<input type="button" value="重置密码" class="submit_but04"
			onclick="resetPwd()" />&nbsp;&nbsp;
	</div>
</div>
</body>
</html>


