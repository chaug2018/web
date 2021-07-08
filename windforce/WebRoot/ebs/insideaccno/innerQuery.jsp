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
<title>内部账户对账单查询</title>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html" />
<link href="<%=path%>/ebs/common/css/process.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/css.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/dicss.css" rel="stylesheet" type="text/css" />

<script language="javascript" src="<%=path%>/ebs/common/js/calendar.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/jquery.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/process.js"></script>


<script type="text/javascript">
	
	
	function queryinnerData() {
		viewCheckListByPage("1");
	}

	function viewCheckListByPage(pageNumber) {
		if(document.getElementById("begindatadate").value.length == 0){
			alert("起始日期不能为空！");
			document.getElementById("begindatadate").focus();
		}else if(document.getElementById("enddatadate").value.length == 0){
			alert("终止日期不能为空！");
			document.getElementById("enddatadate").focus();
		}else{
			if(IsDate(document.getElementById("begindatadate")) ){
				if(IsDate(document.getElementById("enddatadate")) ){
					var pageSize = $("#select_pageSize").val();
					$("#pageSize").val(pageSize);
					$("#curPage").val(pageNumber);
					$("#insideAccnoQueryForm").submit();
				}else{
					document.getElementById("enddatadate").focus();
				}
			}else{
				document.getElementById("begindatadate").focus();
			}
		}
		
	}
	
	function toExportData() {
		var objTable = document.getElementById("basicInfoTable");
		if (objTable.rows.length > 2) {
			window.location.href = "insideAccnoQueryAction!exportData.action";
		} else {
			alert("统计列表无数据");
		}
	}
</script>

</head>
<body class="baby_in2">
	<div class="nov_moon">
		<div
			style="width:98%; height:auto; float:left; margin-left:10px; padding:10px 0px 10px 0px;"
			class="border_bottom01">
			<form id="insideAccnoQueryForm"
				action="insideAccnoQueryAction!queryInnerAccno.action" name="insideAccnoCheck"
				method="post">
				<table width="100%">
					<tr>
						<td width="30%">账&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号&nbsp;&nbsp;&nbsp;
								<input name="innerparam.accno" type="text" class="diinput_text01" maxlength="32" 
									id="accno" value='<s:property value="innerparam.accno"/>' />
							<input type="hidden" id="curPage" name="innerparam.curPage" value='<s:property value="innerparam.curPage"/>' />
							<input type="hidden" id="pageSize" name="innerparam.pageSize" value='<s:property value="innerparam.pageSize"/>' />
						</td>
						<td width="30%">勾兑结果&nbsp;&nbsp;&nbsp;
							<s:select list="accordName"  listKey="key" listValue="value"
									headerKey="" headerValue="全部" value="innerparam.result"
									name="innerparam.result" id="result">
							</s:select>	
						</td>
						<td width="30%">起始日期&nbsp;&nbsp;&nbsp;
							<input name="innerparam.begindatadate" type="text" title="起始账单日期"
							class="diinput_text01" id="begindatadate" onclick="new Calendar().show(this);"
							value='<s:property value="innerparam.begindatadate"/>' />
						</td>
						
						<td width="10%" align="center">
							<input name="export" type="button" class="submit_but09" id="export"
							value="查询" onclick="queryinnerData()"/>
						</td>
						
					</tr>
					<tr>
						<td width="30%">对账中心号&nbsp;&nbsp;&nbsp;
							<%-- <input name="innerparam.idcenter" type="text" class="diinput_text01" maxlength="12" 
								id="idcenter" value='<s:property value="innerparam.idcenter"/>' /> --%>
							<s:select list="idcenterMap" listKey="key" style="width:165px" 
									listValue="value" headerKey="" headerValue="--请选择--" value="innerparam.idcenter"
									name="innerparam.idcenter" id="idcenter">
							</s:select>
						</td>
						<td width="30%">复核结果&nbsp;&nbsp;&nbsp;
							<s:select list="recheckMap"  listKey="key" listValue="value"
									headerKey="" headerValue="全部" value="innerparam.recheck"
									name="innerparam.recheck" id="recheck">
							</s:select>	
						</td>
						<td width="30%">终止日期&nbsp;&nbsp;&nbsp;
							<input name="innerparam.enddatadate" type="text" title="终止账单日期"
							class="diinput_text01" id="enddatadate" onclick="new Calendar().show(this);"
							value='<s:property value="innerparam.enddatadate"/>' />
						</td>
						<td width="10%" align="center">
							<input name="export" type="button" class="submit_but09" id="export"
							value="导出" onclick="toExportData()" />
						</td>
					</tr>
				</table>
			</form>
		</div>
		
		<div style="width:100%; height:auto; float:left; margin-left:10px; padding:10px 0px 10px 0px;">
			<h1>内部账户对账单查询列表</h1>
			<table width="98%" style="border: 1px solid #c9eaf5;"
				cellpadding="0" cellspacing="0" id="basicInfoTable">
				<tr>		
					<td width="4%" height="26" align="center" bgcolor="#c76c6f"
						class="font_colors01">序号</td>
					<td width="8%" height="26" align="center" bgcolor="#c76c6f"
						class="font_colors01">对账中心号</td>
					<td width="10%" height="26" align="center" bgcolor="#c76c6f"
						class="font_colors01">账号</td>
					<td width="8%" align="center" bgcolor="#c76c6f"
						class="font_colors01">余额</td>
					<td width="8%" align="center" bgcolor="#c76c6f"
						class="font_colors01">账单日期</td>
					<td width="6%" align="center" bgcolor="#c76c6f"
						class="font_colors01">勾兑结果</td>
					<td width="6%" align="center" bgcolor="#c76c6f"
						class="font_colors01">核对柜员号</td>
					<td width="8%" align="center" bgcolor="#c76c6f"
						class="font_colors01">核对日期</td>
					<td width="20%" align="center" bgcolor="#c76c6f"
						class="font_colors01">备注</td>
					<td width="6%" align="center" bgcolor="#c76c6f"
						class="font_colors01">复核结果</td>
					<td width="6%" align="center" bgcolor="#c76c6f"
						class="font_colors01">复核柜员号</td>
					<td width="8%" align="center" bgcolor="#c76c6f"
						class="font_colors01">复核日期</td>
				</tr>
				<tbody id="queryList" align="center">
					<s:iterator value="queryList" var="queryData" status="st">
						<tr bgcolor='<s:if test="#st.count%2==0">#CBD6E0</s:if>'>
							<td align="center" >
								<s:property value="#st.count+innerparam.firstResult" />
							</td>
							<td ><s:property value="#queryData.idCenter" /></td>
							<td ><s:property value="#queryData.accNo" /></td>
							<td ><s:property value="#queryData.bal"/></td>	
							<td ><s:property value="#queryData.dataDate"/></td>	
							<td ><s:property value="accordName.get(#queryData.result)"/></td>
							<td ><s:property value="#queryData.resultPeopleCode"/></td>
							<td ><s:property value="#queryData.resultDate"/></td>
							<td ><s:property value="#queryData.abs"/></td>
							<td ><s:property value="recheckMap.get(#queryData.reCheck)"/></td>
							<td ><s:property value="#queryData.reCheckPeopleCode"/></td>
							<td ><s:property value="#queryData.reCheckDate"/></td>
						</tr>
					</s:iterator>
				</tbody>
				<tfoot id="insideListPageInfo">
					<tr class="pagelinks">
						<td colspan="6" align="left">每页显示 <select id="select_pageSize"
							style="width:40px;" onchange="viewCheckListByPage('1')">
								<option value="10"
									<s:if test="innerparam.pageSize==10">selected="selected"</s:if>>10</option>
								<option value="20"
									<s:if test="innerparam.pageSize==20">selected="selected"</s:if>>20</option>
								<option value="50"
									<s:if test="innerparam.pageSize==50">selected="selected"</s:if>>50</option>
						</select>条记录&nbsp;&nbsp;&nbsp;&nbsp;当前显示<s:if test="innerparam.total==0">0</s:if>
							<s:else>
								<s:property value="innerparam.firstResult+1" />
							</s:else>到<s:property value="innerparam.lastResult" />条，共<s:property
								value="innerparam.total" />条</td>
						<td  colspan="6" align="right"><s:if
								test="innerparam.curPage==1">
								<a>首页</a>&nbsp;&nbsp;<a>上一页</a>
							</s:if> <s:else>
								<a href="#" onclick="viewCheckListByPage('1');return false;">首页</a>&nbsp;&nbsp;<a
									href="#"
									onclick="viewCheckListByPage('${innerparam.curPage - 1}');return false;">上一页</a>
							</s:else> <s:if test="innerparam.curPage==innerparam.totalPage">
								<a>下一页</a>&nbsp;&nbsp;<a>尾页</a>
							</s:if> <s:else>
								<a href="#"
									onclick="viewCheckListByPage('${innerparam.curPage + 1}');return false;">下一页</a>&nbsp;&nbsp;<a
									href="#"
									onclick="viewCheckListByPage('${innerparam.totalPage}');return false;">尾页</a>
							</s:else>
								<input type="hidden" id="nowPage" value="${innerparam.curPage}"/>
						</td>
					</tr>
				</tfoot>
			</table>
		</div>
	</div>
</body>
</html>

