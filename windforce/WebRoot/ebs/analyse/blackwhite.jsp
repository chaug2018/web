<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<base href="<%=basePath%>" />
<title>特殊账户统计</title></>
<link href="<%=path%>/ebs/common/css/css.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/dicss.css"	rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/process.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="<%=path%>/ebs/common/js/jquery.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/process.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/orgTree.js"></script>
<script language="javascript" src="<%=path%>/ebs/common/js/calendar.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/radioJudge.js"></script>

<script type="text/javascript">
	var err = "<s:property value='errMsg'/>";
	if (err != null && err.length > 0) {
		alert("初始化查询服务出现错误");
		top.refresh();
	}
</script>

<script type="text/javascript">
	function querySpecAccCount() {
		if ((document.getElementById("selectIdCenter").value.length) !== 0
				&& (document.getElementById("countIdBranch").checked == true)) {
			alert("不能进行总行统计");
		}
		else if ((document.getElementById("selectIdBank").value.length)!== 0
				&& (document.getElementById("countIdCenter").checked == true)){
			alert("不能进行分行统计");
		}else {
			viewBillListByPage('1');
		}
	}
	
	/** 票据查询之分页查询*/
	function viewBillListByPage(pageNumber){
		$("#curPage").val(pageNumber);
		$("#blackWhiteAnalyseForm").submit();
	}
	
	function toExportData() {
		var objTable = document.getElementById("accInfoTable");	
		if (objTable.rows.length>2) {
			window.location.href = 'blackWhiteAnalyseAction_exportData.action';
		}else{
			alert("查询结果无数据");
		}
	}	
	
	$(document).ready(function(){
		var idCenter="<s:property value='idCenter'/>";
		var idBank="<s:property value='idBank'/>";
		stopProcess();
		initTree(<%=request.getAttribute("orgTree")%>,idCenter,idBank);
	});
</script>

<body class="baby_in2">
	<input id="moudleName" type="hidden" value="特殊账户统计" />
	<form id="blackWhiteAnalyseForm" method="post" action="blackWhiteAnalyseAction_analyse.action"
		name="accnoMainDataQuery">
		<div class="nov_moon">
			<div style="width:100%; height:auto; float:left; margin-left:10px; padding:10px 0px 10px 0px;"
				class="border_bottom01">
				<table width="98%">
					<tr>
						<td width="20%">总&nbsp;&nbsp;&nbsp;&nbsp; 行&nbsp;&nbsp;&nbsp;&nbsp;
								<select >
									<option value=""> 华融湘江总行</option>
								</select>
						</td>
						<td width="20%">机&nbsp;构&nbsp;号&nbsp;&nbsp;
							<input name="queryParam.idBank1" id="idBank" class="diinput_text01"
								value='<s:property value="queryParam.idBank1"/>' />
						</td>
						<td width="10%" align="center">
							<input name=queryData type="button"	class="submit_but09" 
								id="find" value="统计" onclick="querySpecAccCount()" />
						</td>
					</tr>
					<tr>
						<td width="20%">分 &nbsp;&nbsp;&nbsp;&nbsp;行&nbsp;&nbsp;&nbsp;&nbsp; 
							<select	name="queryParam.idCenter" id="selectIdCenter"  
								 onchange="changeIdCenter()" onclick="judgeCountIdBranch()">
							</select>
						</td>
						<td width="20%">
							统计单位&nbsp;&nbsp;
							<input type="radio" id="countIdBranch" name="selectCount" value="countIdBranch" onClick="judgeCountIdBranch()"
							<s:if test="selectCount=='countIdBranch'">checked="checked"</s:if> /> 总行
							<input type="radio" id="countIdCenter" name="selectCount" value="countIdCenter" onClick="judgeCountIdCenter()" 
							<s:if test="selectCount=='countIdCenter'">checked="checked"</s:if> /> 分行
							<input type="radio" id="countIdBank" name="selectCount" value="countIdBank" onClick="judgeCountIdBank()"
							<s:if test="selectCount=='countIdBank'">checked="checked"</s:if> /> 网点
						</td>
						<td width="10%" align="center" rowspan="2">
							<input name="btnexport" type="button" class="submit_but09"
								id="btnexport" value="导出" onclick="toExportData()" />
						</td>
					</tr>
					<tr>
						<td width="20%">网&nbsp;&nbsp;&nbsp;&nbsp;点&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<select name="queryParam.idBank" id="selectIdBank" 
								onchange="changeIdBank()" onclick="judgeSelectIdBank()" >
							</select>
						</td>
						<td>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<span id="alertErr"name="alertErr" ></span>
						</td>
					</tr>
				</table>
			</div>
			<div style="width:98%; height:auto; float:left; margin-left:10px; padding:10px 0px 10px 0px; ">
				<h1>特殊账户统计</h1>
				<table width="100%"  cellpadding="0" cellspacing="0" id="accInfoTable">
					<tr>
						<td width="10%" height="26" align="center" bgcolor="#c76c6f"
							class="font_colors01">序号</td>
						<td width="10%" align="center" bgcolor="#c76c6f"
							class="font_colors01">分行</td>
						<td width="10%" align="center" bgcolor="#c76c6f"
							class="font_colors01">网点</td>
						<td width="30%" align="center" bgcolor="#c76c6f"
							class="font_colors01">网点名称</td>
						<td width="20%" align="center" bgcolor="#c76c6f"
							class="font_colors01">不对账数</td>
						<td width="20%" align="center" bgcolor="#c76c6f"
							class="font_colors01">对账数</td>
					</tr>
					<tbody id="queryList" align="center">
						<s:iterator value="resultList" var="queryData" status="st">
							<tr bgcolor='<s:if test="#st.count%2==0">#F4DDDF</s:if>'>
								<td style="border:0px solid #C76C6F"><s:property value="#st.count+queryParam.firstResult" />
								</td>
								<td align="center" style="border:0px solid #C76C6F"><s:property value="#queryData.idCenter" />
								</td>
								<td align="center" style="border:0px solid #C76C6F"><s:property value="#queryData.idBank" />
								</td>
								<td align="center" style="border:0px solid #C76C6F"><s:property value="#queryData.bankName" />
								</td>
								<td align="center" style="border:0px solid #C76C6F"><s:property value="#queryData.blackCount" />
								</td>
								<td align="center" style="border:0px solid #C76C6F"><s:property	value="#queryData.whiteCount" />
								</td>
							</tr>
						</s:iterator>
					</tbody>
 					<input type="hidden" id="curPage" name="queryParam.curPage"
						value='<s:property value="queryParam.curPage"/>' />
					<tfoot id="accInfoTable">
						<tr class="pagelinks">
							<td colspan="3">每页显示 
								<select name="queryParam.pageSize" id="pageSize" 
									style="width:40px;" onchange="viewBillListByPage('1')">
									<option value="10"
										<s:if test="queryParam.pageSize==10">selected="selected"</s:if>>10</option>
									<option value="20"
										<s:if test="queryParam.pageSize==20">selected="selected"</s:if>>20</option>
									<option value="50"
										<s:if test="queryParam.pageSize==50">selected="selected"</s:if>>50</option>
								</select>条记录&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;当前显示
								<s:if test="queryParam.total==0">0
								</s:if> 
								<s:else>
									<s:property value="queryParam.firstResult+1" />
								</s:else>到
								<s:property value="queryParam.lastResult" />条，共
								<s:property value="queryParam.total" />条
							</td>
							<td colspan="8" align="right">
								<s:if test="queryParam.curPage==1">
									<a>首页</a>&nbsp;&nbsp;
									<a>上一页</a>
								</s:if> 
								<s:else>
									<a href="#" onclick="viewBillListByPage('1');return false;">首页</a>&nbsp;&nbsp;
									<a href="#"	onclick="viewBillListByPage('${queryParam.curPage - 1}');return false;">上一页</a>
								</s:else> 
								<s:if test="queryParam.curPage==queryParam.totalPage">
									<a>下一页</a>&nbsp;&nbsp;
									<a>尾页</a>
								</s:if> 
								<s:else>
									<a href="#"	onclick="viewBillListByPage(${queryParam.curPage + 1});return false;">下一页</a>&nbsp;&nbsp;
									<a href="#"	onclick="viewBillListByPage('${queryParam.totalPage}');return false;">尾页</a>
								</s:else>
							</td>
						</tr>
					</tfoot>
				</table>
			</div>
		</div>
	</form>
</body>
</html>
