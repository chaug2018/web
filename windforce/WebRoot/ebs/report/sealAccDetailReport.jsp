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
<base href="<%=basePath%>"/>
<title>验印情况账户明细 </title>
<meta http-equiv="Content-Type" content="text/html" />
<link href="<%=path%>/ebs/common/css/process.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/css.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/dicss.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="<%=path%>/ebs/common/js/jquery.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/orgTree.js"></script>
<script language="javascript" src="<%=path%>/ebs/common/js/enterToTab.js"></script>
<script language="javascript" src="<%=path%>/ebs/common/js/calendar.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/process.js"></script>

<script type="text/javascript">
	var err = "<s:property value='errMsg'/>";
	if (err != null && err.length > 0) {
		alert("初始化服务出现错误");
		top.refresh();
	}
</script>

<script type="text/javascript">
	function toExportData() {
		var objTable = document.getElementById("analyseResultTable");
		if (objTable.rows.length > 2) {
			window.location.href = 'sealAccDetailReport_exportData.action';
		} else {
			alert("统计列表无数据");
		}
	}
	
	function toAnalyseResult() {
		if(IsDate(document.getElementById("beginDocDate")) && 
		   IsDate(document.getElementById("endDocDate"))){
			viewAnalyseListByPage("1");
		}
	}

	function viewAnalyseListByPage(pageNumber) {
		if (document.getElementById("beginDocDate").value.length == 0) {
			alert("对账开始日期不能为空！");
			document.getElementById("beginDocDate").focus();
		}
		else if (document.getElementById("endDocDate").value.length == 0) {
			alert("对账结束日期不能为空！");
			document.getElementById("endDocDate").focus();
		}else {
			$("#curPage").val(pageNumber);
			$("#sealAnalyseForm").submit();
		}

	}
	
	$(document).ready(function() {
		var idCenter = "<s:property value='idCenter'/>";
		var idBank = "<s:property value='idBank'/>";
		stopProcess();
		initTree(<%=request.getAttribute("orgTree")%>, idCenter, idBank);
	});
</script>
</head>

<body class="baby_in2">
	<input id="moudleName" type="hidden" value="验印情况账户明细"/>
	<%@ include file="/ebs/taskviewtool/processing_div.jsp"%>
	<script type="text/javascript">startProcess("正在初始化界面。。。");</script>
	<form id="sealAnalyseForm" action="sealAccDetailReport_analyse.action" name="sealAnalyse" method="post">
		<div class="nov_moon">
			<div style="width:100%; height:auto; float:left; margin-left:10px; padding:10px 0px 10px 0px;"
				class="border_bottom01">
				<table width="98%">
					<tr>
						<td width="20%">总&nbsp;&nbsp;&nbsp;&nbsp;行&nbsp;&nbsp;&nbsp;
							<select >
								<option value=""> 华融湘江总行</option>
							</select>
							<input type="hidden" id="curPage" name="queryParam.curPage"
							value='<s:property value="queryParam.curPage"/>' />
						</td>
						<td width="20%">机&nbsp;&nbsp;&nbsp;&nbsp;构&nbsp;&nbsp;&nbsp;&nbsp;号&nbsp;&nbsp;&nbsp;&nbsp;
							<input name="queryParam.idBank1" id="idBank" class="diinput_text01" maxlength="10"  
							value='<s:property value="queryParam.idBank1"/>' />
						</td>
						
						<td width="10%" align="center">
							<input name="queryData" type="button" class="submit_but09"  
							id="queryData" value="统计" onclick="toAnalyseResult()" />
						</td>
					</tr>
					<tr>
						<td width="20%">分&nbsp;&nbsp;&nbsp;&nbsp;行&nbsp;&nbsp;&nbsp;
							 <select name="queryParam.idCenter" id="selectIdCenter" 
								onchange="changeIdCenter()"  >
							</select>
						</td>
						<td width="20%">对账开始日期&nbsp;&nbsp;&nbsp; 
							<input title="对账开始日期" name="queryParam.beginDocDate" 
							id="beginDocDate" class="diinput_text01"  maxlength="10"
							value='<s:property value="queryParam.beginDocDate"/>'
							onclick="new Calendar().show(this);" />
						</td>
						
						<td width="10%" align="center">
							<input name="export" type="button" class="submit_but09" id="export"
							value="导出" onclick="toExportData()" />
						</td>
					</tr>
					<tr>
						<td width="20%">网&nbsp;&nbsp;&nbsp;&nbsp;点&nbsp;&nbsp;&nbsp;
							<select name="queryParam.idBank" id="selectIdBank" 
								onchange="changeIdBank()"  >
							</select>
						</td>
						<td width="20%">对账结束日期&nbsp;&nbsp;&nbsp; 
							<input title="对账结束日期" name="queryParam.endDocDate" 
							id="endDocDate" class="diinput_text01"  maxlength="10"
							value='<s:property value="queryParam.endDocDate"/>'
							onclick="new Calendar().show(this);" />
						</td>
					</tr>
					<tr>
						<td>
						</td>
						<td>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<span id="alertErr" name="alertErr" ></span>
						</td>
						<td>
						</td>
					</tr>
				</table>
			</div>
			<div style="width:100%; height:auto; float:left; margin-left:10px; padding:10px 0px 10px 0px;">
				<h1>验印情况账户明细</h1>
				<table width="98%" border="0" cellpadding="0" cellspacing="0" id="analyseResultTable">
					<thead>
						<tr>
							<td width="4%" height="26" align="center" bgcolor="#c76c6f"
								class="font_colors01">序号</td>
							<td width="8%" height="26" align="center" bgcolor="#c76c6f"
								class="font_colors01">账号</td>
							<td width="16%" height="26" align="center" bgcolor="#c76c6f"
								class="font_colors01">户名</td>
							<td width="5%" height="26" align="center" bgcolor="#c76c6f"
								class="font_colors01">分行</td>
							<td width="5%" height="26" align="center" bgcolor="#c76c6f"
								class="font_colors01">网点</td>
							<td width="8%" height="26" align="center" bgcolor="#c76c6f"
								class="font_colors01">网点名称</td>
							<td width="6%" height="26" align="center" bgcolor="#c76c6f"
								class="font_colors01">自动验印通过数</td>
							<td width="6%" height="26" align="center" bgcolor="#c76c6f"
								class="font_colors01">手动验印通过数</td>
							<td width="6%" height="26" align="center" bgcolor="#c76c6f"
								class="font_colors01">未通过数</td>
							<td width="6%" height="26" align="center" bgcolor="#c76c6f"
								class="font_colors01">验印总数</td>
							<td width="6%" height="26" align="center" bgcolor="#c76c6f"
								class="font_colors01">尚未验印数</td>
							<td width="6%" height="26" align="center" bgcolor="#c76c6f"
								class="font_colors01">账户数</td>
							<td width="6%" height="26" align="center" bgcolor="#c76c6f"
								class="font_colors01">自动验印成功率</td>
							<td width="6%" height="26" align="center" bgcolor="#c76c6f"
								class="font_colors01">手动验印成功率</td>
						</tr>
					</thead>
					<tbody id="analyse" align="center">
						<s:iterator value="resultList" var="result" status="st">
							<tr bgcolor='<s:if test="#st.count%2==0">#F4DDDF</s:if>'>
								<td><s:property	value="#st.count+queryParam.firstResult" /></td>
								<td><s:property value="#result.accno" /></td>
								<td><s:property value="#result.accname" /></td>
								<td><s:property value="#result.idCenter" /></td>
								<td><s:property value="#result.idBank" /></td>
								<td><s:property value="#result.bankName" /></td>
								<td><s:property value="#result.autoCount" /></td>
								<td><s:property value="#result.manuCount" /></td>
								<td><s:property value="#result.notPassCount" /></td>
								<td><s:property value="#result.proveTotal" /></td>
								<td><s:property value="#result.notProve" /></td>
								<td><s:property value="#result.sendCount" /></td>
								<td><s:property value="#result.autoPercent" /></td>
								<td><s:property value="#result.manuPercent" /></td>
							</tr>
						</s:iterator>
					</tbody>
					<tfoot id="sealFoot">
						<tr class="pagelinks">
							<td colspan="4">每页显示
								<select name="queryParam.pageSize" id="pageSize" style="width:40px;" onchange="viewAnalyseListByPage('1')">
									<option value="10"
										<s:if test="queryParam.pageSize==10">selected="selected"</s:if>>10</option>
									<option value="20"
										<s:if test="queryParam.pageSize==20">selected="selected"</s:if>>20</option>
									<option value="50"
										<s:if test="queryParam.pageSize==50">selected="selected"</s:if>>50</option>
								</select>条记录&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;当前显示
								<s:if test="queryParam.total==0">0</s:if> 
								<s:else>
									<s:property value="queryParam.firstResult+1" />
								</s:else>到
								<s:property value="queryParam.lastResult" />条，共
								<s:property	value="queryParam.total" />条</td>
							<td colspan="8" align="right">
								<s:if test="queryParam.curPage==1">
									<a>首页</a>&nbsp;&nbsp;
									<a>上一页</a>
								</s:if> 
								<s:else>
									<a href="#" onclick="viewAnalyseListByPage('1');return false;">首页</a>&nbsp;&nbsp;
									<a href="#"	onclick="viewAnalyseListByPage('${queryParam.curPage - 1}');return false;">上一页</a>
								</s:else> 
								<s:if test="queryParam.curPage==queryParam.totalPage">
									<a>下一页</a>&nbsp;&nbsp;
									<a>尾页</a>
								</s:if> 
								<s:else>
									<a href="#"	onclick="viewAnalyseListByPage('${queryParam.curPage + 1}');return false;">下一页</a>&nbsp;&nbsp;
									<a href="#" onclick="viewAnalyseListByPage('${queryParam.totalPage}');return false;">尾页</a>
								</s:else>
							 </td>
						</tr>
					</tfoot>
				</table>
				<h2>
					<s:property value="errMsg" />
				</h2>
			</div>
		</div>
	</form>
</body>
</html>
