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
<title>覆盖率统计</title>
<meta http-equiv="Content-Type" content="text/html" />
<link href="<%=path%>/ebs/common/css/process.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/css.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/dicss.css" rel="stylesheet" type="text/css" />

<script language="javascript" src="<%=path%>/ebs/common/js/enterToTab.js"></script>
<script language="javascript" src="<%=path%>/ebs/common/js/calendar.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/jquery.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/orgTree.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/process.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/radioJudge.js"></script>

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
			window.location.href = 'coverReport_exportData.action';
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
		else if(!isLastDay(document.getElementById("beginDocDate").value)){//对账日期不是最后一天
			alert("对账开始日期不是当月最后一天，请重新选择！");
			document.getElementById("beginDocDate").focus();
		}
		else if (document.getElementById("endDocDate").value.length == 0) {
			alert("对账结束日期不能为空！");
			document.getElementById("endDocDate").focus();
		}
		else if(!isLastDay(document.getElementById("endDocDate").value)){//对账日期不是最后一天
			alert("对账结束日期不是当月最后一天，请重新选择！");
			document.getElementById("endDocDate").focus();
		}
		else if(document.getElementById("endDocDate").value<document.getElementById("beginDocDate").value){//对账日期不是最后一天
			alert("对账结束日期必须大于开始日期，请重新选择！");
			document.getElementById("endDocDate").focus();
		}
		else if ((document.getElementById("selectIdCenter").value.length) !== 0
				&& (document.getElementById("countIdBranch").checked == true)) {
			alert("不能进行总行统计");
		}
		else if ((document.getElementById("selectIdBank").value.length)!== 0
				&& (document.getElementById("countIdCenter").checked == true)){
			alert("不能进行分行统计");
		}else {
			$("#curPage").val(pageNumber);
			$("#coverReportForm").submit();
		}
	}
	//判断输入的日期是否是该月的最后一天
	function isLastDay(inputDate){
		var d = new Date(inputDate.replace(/\-/,"/"));
		var nd = new Date(d.getTime()+24*60*60*1000);  //next day
 		return (d.getMonth()!=nd.getMonth());
 	}
	$(document).ready(function() {
		var idCenter = "<s:property value='idCenter'/>";
		var idBank = "<s:property value='idBank'/>";
		stopProcess();
		initTree(<%=request.getAttribute("orgTree")%>, idCenter, idBank);
		
		$("#sendMode").append("<option value='5'>余额 (邮寄+柜台+网银)</option>");
		var sendMode = "<s:property value='queryParam.sendMode'/>";
		if(5==sendMode){
			document.getElementById("sendMode").options[5].selected=true;
		}
	});
</script>
</head>

<body class="baby_in2">
	<input id="moudleName" type="hidden" value="覆盖率统计" />
	<%@ include file="/ebs/taskviewtool/processing_div.jsp"%>
	<script type="text/javascript">startProcess("正在初始化界面。。。");</script>
	<form id="coverReportForm" action="coverReport_analyse.action"
		name="coverReport" method="post">
		<div class="nov_moon">
			<div style="width:100%; height:auto; float:left; margin-left:10px; padding:10px 0px 10px 0px;"
				class="border_bottom01">
				<table width="98%">
					<tr>
						<td width="20%">总&nbsp;&nbsp;&nbsp;&nbsp;行&nbsp;&nbsp;&nbsp;&nbsp;
							<select >
								<option value=""> 华融湘江总行</option>
							</select>
						 	<input type="hidden" id="curPage" name="queryParam.curPage"
								value='<s:property value="queryParam.curPage"/>' />
						</td>
						<td width="20%">机&nbsp;构&nbsp;&nbsp;号&nbsp;&nbsp;&nbsp;
							<input name="queryParam.idBank1" id="idBank" class="diinput_text01" 
								value='<s:property value="queryParam.idBank1"/>' />
						</td>
						<td width="20%">发&nbsp;&nbsp;送&nbsp;&nbsp;方&nbsp;&nbsp;式&nbsp;&nbsp;&nbsp;
								<s:select list="refSendModeMap" listKey="key"
									listValue="value" headerKey=""  headerValue="全部" value="queryParam.sendMode" 
									name="queryParam.sendMode" id="sendMode">
								</s:select>
						</td>
						<td width="10%" align="center">
							<input name="queryData" type="button" class="submit_but09"
							id="queryData" value="统计" onclick="toAnalyseResult()" />
						</td>
					</tr>
					<tr>
						<td width="20%">分&nbsp;&nbsp;&nbsp;&nbsp;行&nbsp;&nbsp;&nbsp;&nbsp;
							<select name="queryParam.idCenter" id="selectIdCenter" 
								onchange="changeIdCenter()" onclick="judgeCountIdBranch()" >
							</select>
						</td>
						<td width="20%">账户类型&nbsp;&nbsp;&nbsp;
								<s:select list="refAccCycleMap" listKey="key"
									listValue="value" headerKey="" headerValue="全部"
									value="queryParam.accCycle" name="queryParam.accCycle" id="accCycle">
								</s:select>
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
						<td width="20%">网&nbsp;&nbsp;&nbsp;&nbsp;点&nbsp;&nbsp;&nbsp;&nbsp;
							<select name="queryParam.idBank" id="selectIdBank" onchange="changeIdBank()" onclick="judgeSelectIdBank()" >
							</select>
						</td>
						<td>
							统计单位&nbsp;&nbsp;
							<input type="radio" id="countIdBranch" name="selectCount" value="countIdBranch" onClick="judgeCountIdBranch()"
							<s:if test="selectCount=='countIdBranch'">checked="checked"</s:if> /> 总行
							<input type="radio" id="countIdCenter" name="selectCount" value="countIdCenter" onClick="judgeCountIdCenter()" 
							<s:if test="selectCount=='countIdCenter'">checked="checked"</s:if> /> 分行
							<input type="radio" id="countIdBank" name="selectCount" value="countIdBank" onClick="judgeCountIdBank()"
							<s:if test="selectCount=='countIdBank'">checked="checked"</s:if> /> 网点
						</td>
						<td>对账结束日期&nbsp;&nbsp;&nbsp; 
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
							<span id="alertErr"name="alertErr" ></span>
						</td>
					</tr>
				</table>
			</div>
			<div
				style="width:98%; height:auto; float:left; margin-left:10px; padding:10px 0px 10px 0px;">
				<h1>覆盖率统计</h1>
				<table width="100%" border="0" cellpadding="0" cellspacing="0"
					id="analyseResultTable">
					<thead>
						<tr>
							<td width="6%" height="26" align="center" bgcolor="#c76c6f"
								class="font_colors01">序号</td>
							<td width="8%" height="26" align="center" bgcolor="#c76c6f"
								class="font_colors01">分行</td>
							<td width="8%" height="26" align="center" bgcolor="#c76c6f"
								class="font_colors01">网点</td>
							<td width="10%" height="26" align="center" bgcolor="#c76c6f"
								class="font_colors01">网点名称</td>
							<td width="10%" height="26" align="center" bgcolor="#c76c6f"
								class="font_colors01">发出的账户数</td>
							<td width="10%" align="center" bgcolor="#c76c6f"
								class="font_colors01">有过成功对账数</td>
							<td width="10%" align="center" bgcolor="#c76c6f"
								class="font_colors01">都未成功对账数</td>
							<s:if test="isThree==1">
								<td width="10%" align="center" bgcolor="#c76c6f"
									class="font_colors01">一次未成功对账数数</td>
								<td width="10%" align="center" bgcolor="#c76c6f"
									class="font_colors01">两次未成功对账数数</td>
								<td width="10%" align="center" bgcolor="#c76c6f"
									class="font_colors01">三次未成功对账数数</td>
							</s:if>
							<td width="10%" align="center" bgcolor="#c76c6f"
								class="font_colors01">覆盖率</td>
	
						</tr>
					</thead>
					<tbody id="analyse" align="center">
						<s:iterator value="resultList" var="result" status="st">
							<tr bgcolor='<s:if test="#st.count%2==0">#F4DDDF</s:if>'>
								<td><s:property
											value="#st.count+queryParam.firstResult" /></td>
								<td><s:property value="#result.idCenter" /></td>
								<td><s:property value="#result.idBank" /></td>
								<td><s:property value="#result.bankName" /></td>
								<td><s:property value="#result.sendCount" /></td>
								<td><s:property value="#result.successCount" /></td>
								<td><s:property value="#result.failCount" /></td>
								<s:if test="isThree==1">
									<td><s:property value="#result.oneFailCount" /></td>
									<td><s:property value="#result.twoFailCount" /></td>
									<td><s:property value="#result.threeFailCount" /></td>
								</s:if>
								<td><s:property value="#result.coverPercent" /></td>
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
							<td colspan="11" align="right">
								<s:if	test="queryParam.curPage==1">
											<a>首页</a>&nbsp;&nbsp;<a>上一页</a>
								</s:if> 
								<s:else>
									<a href="#" onclick="viewAnalyseListByPage('1');">首页</a>&nbsp;&nbsp;
									<a href="#" onclick="viewAnalyseListByPage('${queryParam.curPage - 1}');return false;">上一页</a>
								</s:else> 
								<s:if test="queryParam.curPage==queryParam.totalPage">
									<a>下一页</a>&nbsp;&nbsp;
									<a>尾页</a>
								</s:if> 
								<s:else>
									<a href="#"
										onclick="viewAnalyseListByPage('${queryParam.curPage + 1}');return false;">下一页</a>&nbsp;&nbsp;
									<a
										href="#"
										onclick="viewAnalyseListByPage('${queryParam.totalPage}');return false;">尾页</a>
								</s:else>
							</td>
						</tr>
					</tfoot>			</table>
				<h2>
					<s:property value="errMsg" />
				</h2>
			</div>
		</div>
	</form>
</body>
</html>
