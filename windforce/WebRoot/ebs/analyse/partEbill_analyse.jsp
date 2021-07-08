<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title> 机构对账率统计</title>
<meta http-equiv="Content-Type" content="text/html" />
<link href="<%=path%>/ebs/common/css/css.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/process.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/dicss.css"	rel="stylesheet" type="text/css" />

<script type="text/javascript" src="<%=path%>/ebs/common/js/jquery.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/orgTree.js"></script>
<script language="javascript" src="<%=path%>/ebs/common/js/enterToTab.js"></script>
<script language="javascript" src="<%=path%>/ebs/common/js/calendar.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/process.js"></script>

<script type="text/javascript">
	var err = "<s:property value='errMsg'/>";
	if (err != null && err.length > 0) {
		alert(err);
		top.refresh();
	}
</script>
<script type="text/javascript">
	function toExportData() {
		var objTable = document.getElementById("analyseResultTable");
		if (objTable.rows.length > 2) {
			window.location.href = "partEbillAnalyse_exportData.action";
		} else {
			alert("统计列表无数据");
		}
	}
	function toAnalyseResult() {
		if (IsShortDate(document.getElementById("docDateStart"))&&IsDate(document.getElementById("docDateEnd"))) {
			viewAnalyseListByPage("1");
		}
	}
	function viewAnalyseListByPage(pageNumber) {
		if (document.getElementById("docDateStart").value.length == 0) {
			alert("对账日期不能为空！");
			document.getElementById("docDateStart").focus();
		} else {
			$("#curPage").val(pageNumber);
			$("#ebillAnalyseForm").submit();
		}
	}
	$(document).ready(
		function() {
			var idCenter = "<s:property value='idCenter'/>";
			var idBranch = "<s:property value='idBranch'/>";
			var idBank = "<s:property value='idBank'/>";
			stopProcess();
			initTree(
			<%=request.getAttribute("orgTree")%>,idCenter,idBranch, idBank);
	});
</script>
</head>

<body class="baby_in2">
	<input id="moudleName" type="hidden" value="机构对账率统计" />
	<script type="text/javascript">
		startProcess("正在初始化界面。。。");
	</script>
	<div class="nov_moon">
		<div
			style="width:98%; height:auto; float:left; margin-left:10px; padding:10px 0px 10px 0px;"
			class="border_bottom01">
			<form id="ebillAnalyseForm" action="partEbillAnalyse_analyse.action"
				name="ebillAnalyse" method="post">
				<table width="100%">
					<tr>
						<td width="20%">对账中心&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<select name="queryParam.idCenter"
								id="selectIdCenter" onchange="changeIdCenter()">
							</select>
							<input type="hidden" id="curPage" name="queryParam.curPage"
								value='<s:property value="queryParam.curPage"/>' />
						</td>
						<td width="20%">支&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							行&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<select name="queryParam.idBranch" id="selectIdBranch" 
								onchange="changeIdBranch()">
							</select>
						</td>
						<td width="20%">网
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;点&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<select name="queryParam.idBank" id="selectIdBank" onchange="changeIdBank()">
							</select>
						</td>
						<td width="10%" align="right">
							<input name="queryData" type="button" class="submit_but09"
								id="queryData" value="统计" onclick="toAnalyseResult()" />
						</td>
					</tr>
					<tr>
						<td width="20%">统计起期&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<input name="queryParam.docDateStart"
								id="docDateStart" type="text" maxlength="10"
								value='<s:property value="queryParam.docDateStart"/>' />
						</td>
						<td width="20%">统计止期&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<input name="queryParam.docDateEnd" id="docDateEnd"
								type="text" maxlength="10" title="统计止期"
								value='<s:property value="queryParam.docDateEnd"/>' />
						 </td>
						<td width="20%">
						</td>
						<td width="10%" align="right">
							<input name="export" type="button" class="submit_but09" id="export"
							value="导出" onclick="toExportData()" />
						</td>
					</tr>
				</table>
			</form>
		</div>
		<div
			style="width:100%; height:auto; float:left; margin-left:10px; padding:10px 0px 10px 0px;">
			<h1>统计结果</h1>
			<table width="100%" border="0" cellpadding="0" cellspacing="0"
				id="analyseResultTable">
				<thead>
					<tr>
						<td width="6%" height="26" align="center" bgcolor="#c76c6f"
							class="font_colors01">机构号</td>
						<td width="8%" height="26" align="center" bgcolor="#c76c6f"
							class="font_colors01">机构名称</td>
						<td width="6%" height="26" align="center" bgcolor="#c76c6f"
							class="font_colors01">对账日期</td>
						<td width="6%" height="26" align="center" bgcolor="#c76c6f"
							class="font_colors01">发出数</td>
						<td width="6%" height="26" align="center" bgcolor="#c76c6f"
							class="font_colors01">回收数</td>
						<td width="6%" height="26" align="center" bgcolor="#c76c6f"
							class="font_colors01">回收率</td>
						<td width="6%" align="center" bgcolor="#c76c6f"
							class="font_colors01">退信数</td>
						<td width="6%" align="center" bgcolor="#c76c6f"
							class="font_colors01">验印成功数</td>
						<td width="6%" align="center" bgcolor="#c76c6f"
							class="font_colors01">验印不符数</td>
						<td width="6%" align="center" bgcolor="#c76c6f"
							class="font_colors01">验印成功率</td>
						<td width="6%" align="center" bgcolor="#c76c6f"
							class="font_colors01">验印不符率</td>
						<td width="5%" align="center" bgcolor="#c76c6f"
							class="font_colors01">未建库数</td>
						<td width="6%" align="center" bgcolor="#c76c6f"
							class="font_colors01">余额相符数</td>
						<td width="6%" align="center" bgcolor="#c76c6f"
							class="font_colors01">余额不符数</td>
						<td width="6%" align="center" bgcolor="#c76c6f"
							class="font_colors01">对账成功数</td>
						<td width="6%" align="center" bgcolor="#c76c6f"
							class="font_colors01">对账成功率</td>

					</tr>
				</thead>
				<tbody id="analyse" align="center">
					<s:iterator value="resultList" var="result" status="st">
						<tr bgcolor='<s:if test="#st.count%2==0">#F4DDDF</s:if>'>
							<td><s:property value="#result.idBank" />
							</td>
							<td><s:property value="#result.bankName" />
							</td>
							<td><s:property value="#result.docDate" />
							</td>
							<td><s:property value="#result.sendCount" />
							</td>
							<td><s:property value="#result.backCount" />
							</td>
							<td><s:property value="#result.backPercent" />
							</td>
							<td><s:property value="#result.retreatCount" />
							</td>
							<td><s:property value="#result.proveMatchCount" />
							</td>
							<td><s:property value="#result.proveNotMatchCount" />
							</td>
							<td><s:property value="#result.proveMatchPercent" />
							</td>
							<td><s:property value="#result.proveNotMatchPercent" />
							</td>
							<td><s:property value="#result.wjkCount" />
							</td>
							<td><s:property value="#result.checkSuccessCount" />
							</td>
							<td><s:property value="#result.checkFailCount" />
							</td>
							<td><s:property value="#result.ebillSuccessCount" />
							</td>
							<td><s:property value="#result.checkSuccessPercent" />
							</td>
						</tr>
					</s:iterator>
				</tbody>
				<tfoot id="ebillFoot">
					<tr class="pagelinks">
						<td colspan="6">每页显示 <select name="queryParam.pageSize"
							id="pageSize" style="width:40px;"
							onchange="viewAnalyseListByPage('1')">
								<option value="10"
									<s:if test="queryParam.pageSize==10">selected="selected"</s:if>>10</option>
								<option value="20"
									<s:if test="queryParam.pageSize==20">selected="selected"</s:if>>20</option>
								<option value="50"
									<s:if test="queryParam.pageSize==50">selected="selected"</s:if>>50</option>
						</select>条记录&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;当前显示<s:if
								test="queryParam.total==0">0</s:if> <s:else>
								<s:property value="queryParam.firstResult+1" />
							</s:else>到<s:property value="queryParam.lastResult" />条，共<s:property
								value="queryParam.total" />条</td>
						<td colspan="12" align="right"><s:if
								test="queryParam.curPage==1">
								<a>首页&nbsp;&nbsp;<a>上一页</a>
							</s:if> <s:else>
								<a href="#" onclick="viewAnalyseListByPage('1');">首页</a>&nbsp;&nbsp;<a
									href="#"
									onclick="viewAnalyseListByPage('${queryParam.curPage - 1}');return false;">上一页</a>
							</s:else> <s:if test="queryParam.curPage==queryParam.totalPage">
								<a>下一页</a>&nbsp;&nbsp;<a>尾页</a>
							</s:if> <s:else>
								<a href="#"
									onclick="viewAnalyseListByPage('${queryParam.curPage + 1}');return false;">下一页</a>&nbsp;&nbsp;<a
									href="#"
									onclick="viewAnalyseListByPage('${queryParam.totalPage}');return false;">尾页</a>
							</s:else></td>
					</tr>
				</tfoot>
			</table>
			<h2>
				<s:property value="errMsg" />
			</h2>
		</div>
	</div>
</body>
</html>
