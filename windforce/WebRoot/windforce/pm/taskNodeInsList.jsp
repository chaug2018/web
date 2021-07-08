<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>任务实例列表</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="任务实例列表">
<meta http-equiv="description" content="This is my page">
<link type="text/css" rel="stylesheet" href="<%=path%>/windforce/common/css/pm.css" />
<link type="text/css" rel="stylesheet" href="<%=path%>/windforce/common/css/core.css" />
<%@ include file="/common/wf_import.jsp"%>
<script type="text/javascript" src="<%=path%>/windforce/common/js/pm.js"></script>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
</head>

<body>
	<div id="list_content">
		<input type="hidden" value="${processDefinitionKey}"
			id="processDefinitionKey"> <input type="hidden"
			value="${taskName}" id="taskName"> <input type="hidden"
			value="${curPage}" id="curPage"> <input type="hidden"
			value="${pageFlag}" id="pageFlag"> <input type="hidden"
			value="${currentUserId}" id="currentUserId">
		<center>
			<h2 style="size:12px;margin-top: 7px;">
				<c:if test="${pageFlag == 'L'}">锁定任务列表</c:if>
				<c:if test="${pageFlag == 'F'}">空闲任务列表</c:if>
			</h2>
			<br>
			<div id="tools" style="margin-right: 10px;">
				<c:if test="${pageFlag == 'F'}">
					<span id="lockTasks"><img width="16px;" height="16px;"
						src="<%=path%>/windforce/common/images/pm_lock.png" />锁&nbsp;&nbsp;&nbsp;定</span>
				</c:if>
				<c:if test="${pageFlag == 'L'}">
					<span id="unLockTasks"><img width="16px;" height="16px;"
						src="<%=path%>/windforce/common/images/pm_unlock.png" />解&nbsp;&nbsp;&nbsp;锁</span>
				</c:if>
			</div>
			<table width="100%">
				<tr>
					<td width="40%" align="center"><input type="checkbox"
						id="allselect" value="all">全选</td>
					<td width="30%" align="center">业务主键</td>
					<td width="30%" align="center">锁定人</td>
				</tr>
				<c:forEach items="${taskInstanceList}" var="taskInstanceItem">
					<tr>
						<td align="center"><input type="checkbox" name="taskInsIds"
							value="${taskInstanceItem.id}"></td>
						<td align="center">${taskInstanceItem.businessKey}</td>
						<td id="td_peopleCode_${taskInstanceItem.id}">${taskInstanceItem.peopleCode}</td>
					</tr>
				</c:forEach>
			</table>
			<div style="float: right;margin-right: 10px;margin-top:7px;">
				&nbsp;第${curPage}&nbsp;页&nbsp;&nbsp;共&nbsp;${totalPage}&nbsp;页&nbsp;
				<c:if test="${curPage == 1}">
					<span>首页</span>&nbsp;<span>上一页</span>&nbsp;</c:if>
				<c:if test="${curPage != 1}">
					<a href="#"
						onclick="viewTaskNodeInsList('${taskName}','${processDefinitionKey}', '1','${pageFlag}');return false;">首页</a>&nbsp;<a
						href="#"
						onclick="viewTaskNodeInsList('${taskName}','${processDefinitionKey}', '${lastPage}','${pageFlag}');return false;">上一页</a>&nbsp;</c:if>
				<c:if test="${curPage == totalPage}">
					<span>下一页&nbsp;</span>
					<span>尾页&nbsp;</span>
				</c:if>
				<c:if test="${curPage != totalPage}">
					<a href="#"
						onclick="viewTaskNodeInsList('${taskName}','${processDefinitionKey}', '${nextPage}','${pageFlag}');return false;">下一页&nbsp;</a>
					<a href="#"
						onclick="viewTaskNodeInsList('${taskName}','${processDefinitionKey}', '${totalPage}','${pageFlag}');return false;">尾页&nbsp;</a>
				</c:if>
			</div>
		</center>
	</div>
	<script type="text/javascript">
	 top.stopProcess();
	</script>
</body>
</html>