<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<base href="<%=basePath%>" />

<title>流程监控</title>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<meta http-equiv="keywords" content="流程监控" />
<meta http-equiv="description" content="流程监控 " />
<link type="text/css" rel="stylesheet" href="<%=path%>/windforce/common/css/pm.css" />
<link type="text/css" rel="stylesheet" href="<%=path%>/windforce/common/css/core.css" />
<%@ include file="/common/wf_import.jsp"%>
<script type="text/javascript" src="<%=path%>/windforce/common/js/pm.js"></script>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
</head>
<body class="baby_in2" id="aaa">
	<div id="nov_nr">
		<span class="disblock"><img
			src="<%=path%>/windforce/common/images/pm_lcjk.png" />
		</span> <input type="hidden" id="processDefinitionKey"
			value="${processDefinitionKey}" />
		<div class="nov_moon" id="headera">
			<div
				style="width:1002px; height:auto; float:left; margin-left:10px; padding:10px 0px 10px 0px;"
				class="border_bottom01">
				<div class="box" style="width:800px; float:left;">
					<div class="tagMenu" style="overflow:hidden; ">
						<ul class="menu" style="float:left;">
							<c:forEach items="${processDefList}" var="processItem">
								<li onclick="changeProcessTab('${processItem.key}', this)">${processItem.processDisplayName}</li>
							</c:forEach>
							<li style="float:right;"><span>				
							<input id="refreshButton"  type="button" value="刷新" style="width:38px; height:23px;" onclick="refreshProcessMonitorData()" />
							</span>
							</li>
						</ul>
					</div>
					<div class="content">
						<div class="layout">
							<table id="processInfoTable" width="790">
								<tr>
									<td width="40%" align="center">任务环节名称</td>
									<td width="30%" align="center">锁定任务数</td>
									<td width="30%" align="center">空闲任务数</td>
								</tr>
								<tbody id="procCountInfo">
								</tbody>
							</table>
						</div>
					</div>
				</div>
<!-- 				<div> -->
<!-- 					<img id="processImage" alt="流程示意图" width="810px;" /> -->
<!-- 					<input -->
<!-- 						id="rootUrl" value="<%=path%>" type="hidden" /> -->
<!-- 				</div> -->
			</div>
		</div>
	</div>
</body>
</html>