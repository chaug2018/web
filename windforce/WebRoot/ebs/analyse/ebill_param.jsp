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
<title>对账有效明细统计</title>
<meta http-equiv="Content-Type" content="text/html" />
<link href="<%=path%>/ebs/common/css/process.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/css.css" rel="stylesheet" type="text/css" />
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
		if (document.getElementById("docDate").value.length == 0) {
			alert("对账日期不能为空！");
			document.getElementById("docDate").focus();
		} 
		var objTable = document.getElementById("analyseResultTable");
		if (objTable.rows.length > 2) {
			window.location.href = 'ebillMatchParamAction_exportData.action';
		} else {
			alert("统计列表无数据");
		}
	}
	
	function toAnalyseResult() {
		if(IsDate(document.getElementById("docDate"))){
			viewAnalyseListByPage("1");
		}
	}
	
	function viewAnalyseListByPage(pageNumber) {
		if (document.getElementById("docDate").value.length == 0) {
			alert("对账日期不能为空！");
			document.getElementById("docDate").focus();
		} else {
			$("#curPage").val(pageNumber);
			$("#ebillMatchAnalyseForm").submit();
		}
	}
	
	$(document).ready(function() {
		var idCenter = "<s:property value='idCenter'/>"
		var idBank = "<s:property value='idBank'/>";
		stopProcess();
		initTree(<%=request.getAttribute("orgTree")%>,idCenter, idBank);
		if ("<s:property value='showList.size()'/>" == 6) {
			document.getElementById("cycle1").innerHTML = "<s:property value='showList.get(5)'/>";
			document.getElementById("cycle2").innerHTML = "<s:property value='showList.get(4)'/>";
			document.getElementById("cycle3").innerHTML = "<s:property value='showList.get(3)'/>";
			document.getElementById("cycle4").innerHTML = "<s:property value='showList.get(2)'/>";
			document.getElementById("cycle5").innerHTML = "<s:property value='showList.get(1)'/>";
			document.getElementById("cycle6").innerHTML = "<s:property value='showList.get(0)'/>";
		}
	});
</script>
</head>

<body class="baby_in2">
	<input id="moudleName" type="hidden" value="对账有效对账明细统计" />
	<%@ include file="/ebs/taskviewtool/processing_div.jsp"%>
	<script type="text/javascript">
		startProcess("正在初始化界面。。。");
	</script>
	<form id="ebillMatchAnalyseForm" action="ebillMatchParamAction_analyse.action"
			name="ebillMatchAnalyse" method="post">
		<div class="nov_moon">
			<div style="width:100%; height:auto; float:left; margin-left:10px; 
				padding:10px 0px 10px 0px;"	class="border_bottom01">
				<table width="98%">
					<tr>
						<td width="20%">总&nbsp;&nbsp;&nbsp;&nbsp;行&nbsp;&nbsp;
							<select>
								<option value="">华融湘江总行</option>
							</select>
							<input type="hidden" id="curPage" name="queryParam.curPage"
								value='<s:property value="queryParam.curPage"/>' />
						</td>
						<td width="20%">机&nbsp;构&nbsp;&nbsp;号&nbsp;&nbsp;
							<input name="queryParam.idBank1" id="idBank" class="diinput_text01" 
							 	value='<s:property value="queryParam.idBank1"/>' />
						</td>
                        <td width="20%">对账渠道&nbsp;&nbsp;
                        	<s:select list="refSendModeMap" listKey="key" 
								listValue="value" headerKey="" headerValue="全部" value="queryParam.sendMode"
								name="queryParam.sendMode" id="sendMode">
						    </s:select>
						</td>
						<td width="10%" align="center">
							<input name="queryData" type="button" class="submit_but09"
							id="queryData" value="统计" onclick="toAnalyseResult()" />
						</td>
					</tr>
					<tr>
						<td width="20%">分&nbsp;&nbsp;&nbsp;&nbsp;行&nbsp;&nbsp;
							<select name="queryParam.idCenter" id="selectIdCenter"
								onchange="changeIdCenter()">
							</select>
						</td>
						<td width="20%">对账日期&nbsp;&nbsp;
							<input name="queryParam.docDate" id="docDate" class="diinput_text01"  title="对账日期"
							value='<s:property value="queryParam.docDate"/>'
							onClick="new Calendar().show(this);" />
						</td>
                        <td width="20%">科&nbsp;目&nbsp;号&nbsp;&nbsp;&nbsp;
							<s:select id="accType" list="refAccTypeMap"  listKey="key"
								listValue="value" headerKey="" headerValue="全部"
								value="queryParam.accType" name="queryParam.accType" >
							</s:select>
						</td>
                        <td width="10%" align="center">
							<input name="export" type="button" class="submit_but09" id="export"
							value="导出" onclick="toExportData()" />
						</td>
					</tr>
					<tr>
						<td width="20%">网&nbsp;&nbsp;&nbsp;点 &nbsp;&nbsp;
							<select  name="queryParam.idBank" id="selectIdBank"  onchange="changeIdBank()">
							</select>
						</td>
						<td width="20%">
						</td>
						<td width="20%">
						</td>
						<td width="10%">
						</td>
					</tr>
				</table>
		
		</div>
		<div style="width:98%; height:auto; float:left; margin-left:10px; padding:10px 0px 10px 0px;">
			<h1>对账有效明细统计</h1>
			<table width="100%" border="0" cellpadding="0" cellspacing="0" id="analyseResultTable">
				<thead>
					<tr>
						<td width="5%" align="center" bgcolor="#c76c6f"
							class="font_colors01">序号</td>
						<td width="6%" align="center" bgcolor="#c76c6f"
							class="font_colors01">分行</td>
						<td width="5%" align="center" bgcolor="#c76c6f"
							class="font_colors01">网点</td>
						<td width="16%" align="center" bgcolor="#c76c6f"
							class="font_colors01">网点名称</td>
						<td width="10%" align="center" bgcolor="#c76c6f"
							class="font_colors01">账号</td>
						<td width="10%" align="center" bgcolor="#c76c6f"
							class="font_colors01">账户名</td>
						<td width="8%" align="center" bgcolor="#c76c6f"
							class="font_colors01"><label id="cycle1">第一周期对账结果</label></td>
						<td width="8%" align="center" bgcolor="#c76c6f"
							class="font_colors01"><label id="cycle2">第二周期对账结果</label></td>
						<td width="8%" align="center" bgcolor="#c76c6f"
							class="font_colors01"><label id="cycle3">第三周期对账结果</label></td>
						<td width="8%" align="center" bgcolor="#c76c6f"
							class="font_colors01"><label id="cycle4">第四周期对账结果</label></td>
						<td width="8%" align="center" bgcolor="#c76c6f"
							class="font_colors01"><label id="cycle5">第五周期对账结果</label></td>
						<td width="8%" align="center" bgcolor="#c76c6f"
							class="font_colors01"><label id="cycle6">第六周期对账结果</label></td>
					</tr>
				</thead>
				<tbody id="analyse" align="center">
					<s:iterator value="resultList" var="result" status="st">
						<tr bgcolor='<s:if test="#st.count%2==0">#F4DDDF</s:if>'>
							<td align="center"><s:property value="#st.count+queryParam.firstResult" /></td>
							<td><s:property value="#result.idCenter" /></td>
							<td><s:property value="#result.idBank" /></td>
							<td><s:property value="#result.bankName" /></td>
							<td><s:property value="#result.accNo" /></td>
							<td><s:property value="#result.accName" /></td>
							<td><s:property value="#result.MatchCount1" /></td>
							<td><s:property value="#result.MatchCount2" /></td>
							<td><s:property value="#result.MatchCount3" /></td>
							<td><s:property value="#result.MatchCount4" /></td>
							<td><s:property value="#result.MatchCount5" /></td>
							<td><s:property value="#result.MatchCount6" /></td>
						</tr>
					</s:iterator>
				</tbody>
				<tfoot id="ebillFoot">
					<tr class="pagelinks">
						<td colspan="6">每页显示
							<select name="queryParam.pageSize" id="pageSize" style="width:40px;"
								onchange="viewAnalyseListByPage('1')">
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
							<s:property	value="queryParam.total" />条
						</td>
						<td colspan="12" align="right">
							<s:if test="queryParam.curPage==1">
								<a>首页&nbsp;&nbsp;
								<a>上一页</a>
							</s:if> 
							<s:else>
								<a href="#" onclick="viewAnalyseListByPage('1');">首页</a>&nbsp;&nbsp;
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
