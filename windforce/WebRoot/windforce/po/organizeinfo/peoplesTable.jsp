
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<table width="98%" align="left" cellpadding="0" cellspacing="0">
	<tr>
		<td width="10%" height="26" align="center" class="font_colors07">序号</td>
		<td width="20%" align="center" class="font_colors07">用户姓名</td>
		<td width="10%" align="center" class="font_colors07">用户代码</td>
		<td width="16%" align="center" class="font_colors07">用户岗位</td>
		<td width="10%" align="center" class="font_colors07">性别</td>
		<td width="13%" align="center" class="font_colors07">操作</td>
	</tr>
	<tbody id="trAutoComplete">
		<s:iterator value="userList" var="userInfo" status="index">
			<!--  class="gradeX"  这句话别删除，用于自动搜索使用 -->
			<tr class="gradeX"
				<s:if test="#index.index%2 ==0">bgcolor="#CBD6E0"</s:if>
				<s:if test="#index.index%2 ==1">bgcolor="#FFFFFF"</s:if>>
				<td align="center"
					onclick="setUserDetail(this);">${index.index + startRow+1}</td>
				<td align="center" style="color: blue;"
					onclick="setUserDetail(this);">
					<a onclick="javascript:top.userDetail('<s:property value='#userInfo.peopleCode'/>');">
						<s:property value="#userInfo.peopleName" />
					</a>
				</td>
				<td align="center"
					onclick="setUserDetail(this);"><s:property
						value="#userInfo.peopleCode" /></td>
				<td align="center" onclick="setUserDetail(this);"><s:property
									value="#userInfo.roleGroupStr" /></td>
				<td align="center" class="center"
					onclick="setUserDetail(this);"><s:if
						test="#userInfo.peopleGender == 0">男</s:if> <s:if
						test="#userInfo.peopleGender == 1">女</s:if>
				</td>
				<td align="center">
					<s:if test="#userInfo.sid !='7B92AE0FC4B04DB48F1AFBDB22CD7188'">
						<a style="color:#FF0000" onclick="editUser('<s:property value='#userInfo.sid'/>');">修改</a> /
						<a style="color:#FF0000" onclick="delPeople('<s:property value='#userInfo.sid'/>','<s:property value='#userInfo.organizeInfo.orgNo'/>')">删除</a>/ 
					</s:if>
					<a style="color:#FF0000" onclick="resetPwd('<s:property value='#userInfo.sid'/>')" >重置密码</a>
				</td>
			</tr>
		</s:iterator>
	</tbody>
</table>

