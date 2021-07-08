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
<title>未达项查询</title>
<meta http-equiv="Content-Type" content="text/html" />

<link href="<%=path%>/ebs/common/css/css.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/dicss.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/process.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="<%=path%>/common/js/jquery/jquery-1.7.1.js"></script>
<script language="javascript" src="<%=path%>/ebs/common/js/calendar.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/orgTree.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/process.js"></script>

<script type="text/javascript">
		var err = "<s:property value='errMsg'/>";
		if (err != null && err.length > 0) {
			alert("初始化查询服务出现错误");
			top.refresh();
		}
</script>
<script type="text/javascript">
	function queryNotMatchList() {
		if(IsDate(document.getElementById("docdate")))
		{
			viewnotMatchListByPage("1");
	    }
	}

	function viewnotMatchListByPage(pageNumber) {
		if (document.getElementById("docdate").value.length == 0) {
			alert("对账日期不能为空！");
			document.getElementById("docdate").focus();
		} else {
			$("#curPage").val(pageNumber);
			$("#notMatchQueryForm").submit();
		}
	}

	function toExportData() {
		var objTable = document.getElementById("myTable");
		if (objTable.rows.length > 2) {
			window.location.href = "notMatchQueryAction_exportData.action";
		} else {
			alert("没有未达项数据");
		}
	}
	$(document).ready(
			function() {
				var idCenter = "<s:property value='idCenter'/>";
				var idBank = "<s:property value='idBank'/>";
				stopProcess();
				initTree( <%=request.getAttribute("orgTree")%>, idCenter, idBank);
			});
</script>

</head>
<body class="baby_in2">
<input id="moudleName" type="hidden" value="未达查询"/>
	<script type="text/javascript">startProcess("正在初始化界面。。。");</script>
	<form id="notMatchQueryForm" action="notMatchQueryAction_queryNotMatchData.action"
		name="notMatchQuery" method="post">
		<div class="nov_moon">
			<div style="width:100%; height:auto; float:left; margin-left:10px; padding:10px 0px 10px 0px;"
				class="border_bottom01">

				<table width="100%">
					<tr>
						<td width="20%">总 行&nbsp;&nbsp;&nbsp; 
								<select>
									<option value=""> 华融湘江总行</option>
								</select>
						</td>
						<td width="20%">机&nbsp;&nbsp;构&nbsp;&nbsp;号&nbsp; 
							<input name="notMatchQueryParam.idBank1" id="idBank" maxlength="12"
								class="diinput_text01" onkeydown="cgo(this.form,this.name,'')"
								value='<s:property value="notMatchQueryParam.idBank1"/>' />
							<input type="hidden" id="curPage" name="notMatchQueryParam.curPage"
								value='<s:property value="notMatchQueryParam.curPage"/>' />
						</td>
						<td width="20%">
								对账日期&nbsp;&nbsp; 
								<input style="color:#333333; font-size:13px;"
									name="notMatchQueryParam.docDate" type="text" title="对账日期"
									class="diinput_text01" id="docdate" maxlength="12"
									onclick="new Calendar().show(this);" onkeydown="cgo(this.form,this.name,'')"
									value='<s:property value="notMatchQueryParam.docDate"/>' />
						</td>
						<td width="10%" >
							<input name="queryData" type="button" class="submit_but09"
								id="queryData" value="查询" onclick="queryNotMatchList()"
								onkeydown="cgo(this.form,this.name,'')" />
						</td>
					</tr>
					<tr>
						<td width="20%">分 行&nbsp;&nbsp;&nbsp; 
								<select name="notMatchQueryParam.idCenter" id="selectIdCenter" onchange="changeIdCenter()">
								</select>
							</td>
						<td width="20%">账单编号&nbsp;&nbsp; 
							<input onkeydown="cgo(this.form,this.name,'')" name="notMatchQueryParam.voucherNo" 
								type="text" class="diinput_text01" id="voucherNo" maxlength="32"
								value='<s:property value="notMatchQueryParam.voucherNo"/>' />
						</td>
						<td width="20%">
							余额状态&nbsp;&nbsp;
							<s:select style="color:#333333;" cssStyle="width:165px;"  list="refResultMap" listValue="value" headerKey="" headerValue="--请选择--"
							 	listKey="key" onkeydown="cgo(this.form,this.name,'')" 
								name="notMatchQueryParam.result" id="result" value="notMatchQueryParam.result" />
						</td>
						<td width="10%" >
						</td>
					</tr>
					<tr>
						<td width="20%">网 点&nbsp;&nbsp;&nbsp; 
							<select name="notMatchQueryParam.idBank" id="selectIdBank"
								onkeydown="cgo(this.form,this.name,'')"onchange="changeIdBank()">
							</select>
						</td>
						<td width="20%">
							账&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号&nbsp;&nbsp; 
							<input name="notMatchQueryParam.accNo" onkeydown="cgo(this.form,this.name,'')" 
								type="text" class="diinput_text01" id="accno" maxlength="32"
								value='<s:property value="notMatchQueryParam.accNo"/>' />
						</td>
						<td width="20%">未达方向&nbsp;&nbsp; 
							<s:select list="refDirectionMap" onkeydown="cgo(this.form,this.name,'')"
								cssStyle="width:165px;" listKey="key" listValue="value"
								headerKey="" headerValue="--请选择--" style="color:#333333; font-size:13px;"
								name="notMatchQueryParam.direction"
								value="notMatchQueryParam.direction" id="direction">
							</s:select>
						</td>
						<td width="10%">
							<input name="btnexport" type="button" class="submit_but09"
							id="btnexport" value="导出" onclick="toExportData()" />
						</td>
					</tr>
				</table>
			</div>
			<div style="width:100%; height:auto; float:left; margin-left:10px; padding:10px 0px 10px 0px;">
				<h1>未达信息列表</h1>
				<table width="98%" border="0" cellpadding="0" cellspacing="0" id="myTable">
					<thead>
						<tr>
							<td width="4%" height="26" align="center" bgcolor="#c76c6f"
								class="font_colors01">序号</td>
							<td width="5%" align="center" bgcolor="#c76c6f"
								class="font_colors01">业务流水</td>
							<td width="10%" align="center" bgcolor="#c76c6f"
								class="font_colors01">账单编号</td>
							<td width="6%" align="center" bgcolor="#c76c6f"
								class="font_colors01">机构号</td>
							<td width="10%" align="center" bgcolor="#c76c6f"
								class="font_colors01">账号</td>
							<td width="8%" align="center" bgcolor="#c76c6f"
								class="font_colors01">记账日期</td>
							<td width="10%" align="center" bgcolor="#c76c6f"
								class="font_colors01">金&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;额</td>
							<td width="15%" align="center" bgcolor="#c76c6f"
								class="font_colors01">凭证号码</td>
							<td width="15%" align="center" bgcolor="#c76c6f"
								class="font_colors01">摘&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;要</td>
							<td width="10%" align="center" bgcolor="#c76c6f"
								class="font_colors01">未达方向</td>
							<td width="10%" align="center" bgcolor="#c76c6f"
								class="font_colors01">核对结果</td>
						</tr>
					</thead>
					

					<tbody id="queryList" align="center">
						<s:iterator value="queryList" var="queryData" status="st">
							<tr bgcolor='<s:if test="#st.count%2==0">#F4DDDF</s:if>'>
								<td><s:property value="#st.count+notMatchQueryParam.firstResult" /></td>
								<td><s:property value="#queryData.docId" /></td>
								<td><s:property value="#queryData.voucherNo" /></td>
								<td><s:property value="#queryData.idBank" /></td>
								<td><s:property value="#queryData.accNo" /></td>
								<td><s:property value="#queryData.traceDate" /></td>
								<td><s:property value="#queryData.strtraceCredit" /></td>
								<td><s:property value="#queryData.traceNo" /></td>
								<td><s:property value="#queryData.inputDesc" /></td>
								<td><s:iterator value="refDirectionMap.keySet()" id="id">
										<s:if test="#queryData.direction==#id">
											<s:property value="refDirectionMap.get(#id)" />
										</s:if>
									</s:iterator>
								</td>
								<td><s:iterator value="refResultMap.keySet()" id="id">
										<s:if test="#queryData.checkFlag==#id">
											<s:property value="refResultMap.get(#id)" />
										</s:if>
									</s:iterator>
								</td>
							</tr>
						</s:iterator>
					</tbody>
					<tfoot id="notMatchListPageInfo">
						<tr class="pagelinks">
							<td colspan="5">每页显示 <select name="notMatchQueryParam.pageSize" id="pageSize"
								style="width:40px;" onchange="viewnotMatchListByPage('1')">
									<option value="10"
										<s:if test="notMatchQueryParam.pageSize==10">selected="selected"</s:if>>10</option>
									<option value="20"
										<s:if test="notMatchQueryParam.pageSize==20">selected="selected"</s:if>>20</option>
									<option value="50"
										<s:if test="notMatchQueryParam.pageSize==50">selected="selected"</s:if>>50</option>
							</select>条记录&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;当前显示<s:if
									test="notMatchQueryParam.total==0">0</s:if> <s:else>
									<s:property value="notMatchQueryParam.firstResult+1" />
								</s:else>到<s:property value="notMatchQueryParam.lastResult" />条，共<s:property
									value="notMatchQueryParam.total" />条</td>
							<td colspan="8" align="right"><s:if
									test="notMatchQueryParam.curPage==1">
										<a>首页</a>&nbsp;&nbsp;<a>上一页</a>
									</s:if> <s:else>
									<a href="#" onclick="viewnotMatchListByPage('1');">首页</a>&nbsp;&nbsp;<a
										href="#"
										onclick="viewnotMatchListByPage('${notMatchQueryParam.curPage - 1}');return false;">上一页</a>
								</s:else> <s:if
									test="notMatchQueryParam.curPage==notMatchQueryParam.totalPage">
										<a>下一页</a>&nbsp;&nbsp;<a>尾页</a>
									</s:if> <s:else>
									<a href="#"
										onclick="viewnotMatchListByPage('${notMatchQueryParam.curPage + 1}');return false;">下一页</a>&nbsp;&nbsp;<a
										href="#"
										onclick="viewnotMatchListByPage('${notMatchQueryParam.totalPage}');return false;">尾页</a>
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
