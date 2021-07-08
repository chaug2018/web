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
<title>账单交接登记薄</title>
<meta http-equiv="Content-Type" content="text/html" />
<link href="<%=path%>/ebs/common/css/process.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/css.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/dicss.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="<%=path%>/ebs/common/js/jquery.js"></script>
<script language="javascript" src="/windforce/ebs/common/js/calendar.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/orgTree.js"></script>
<script language="javascript" src="<%=path%>/ebs/common/js/enterToTab.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/process.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/exChangeBook.js"></script>


<script type="text/javascript">

	var err = "<s:property value='errMsg'/>";
	if (err != null && err.length > 0) {
		alert(err);
		top.refresh();
	}
	// 设置数字输入框不能输入字母
	function setNumber() {
		if (!(((window.event.keyCode >= 48) && (window.event.keyCode <= 57))
		|| (window.event.keyCode == 13) || (window.event.keyCode == 46)
		|| (window.event.keyCode == 45)) && window.event.keyCode != 8)
		{
			alert("只能输入数值型数据！");
			window.event.keyCode = 0;
		}
		return;
	}
 
</script>
<script type="text/javascript">
	function toExportData() {
		var objTable = document.getElementById("exChangeBookResultTable");
		if (objTable.rows.length > 2) {
			window.location.href = "exChangeBookAction_exportData.action";
		} else {
			alert("统计列表无数据");
		}
	}
	function toExChangeBookResult() {
		viewExChangeBookListByPage("1");
	}
	function viewExChangeBookListByPage(pageNumber) {
		if (document.getElementById("docDate").value.length == 0) {
			alert("对账日期不能为空！");
			document.getElementById("docDate").focus();
		} else {
			if(IsDate(document.getElementById("docDate"))){
				$("#curPage").val(pageNumber);
				$("#exChangeBookForm").submit();
			}else{
				document.getElementById("docDate").focus();
			}
		}
	}
</script>
</head>

<body class="baby_in2">
	<input id="moudleName" type="hidden" value="账单交接登记薄" />
	<%@ include file="/ebs/taskviewtool/processing_div.jsp"%>
	<div class="nov_moon">
		<div style="width:100%; height:auto; float:left; margin-left:10px; padding:10px 0px 10px 0px;"
			class="border_bottom01">
				<div id="son_div" class="son_div" style="background:url('/windforce/ebs/common/images/bg_content.jpg');height: 220px;">
					<div class="sonpage" id="sonpage" align="center">
						<%@ include file="/ebs/exchangebook/exChangeBook_send.jsp"%>
					</div>
				</div>
			<form id="exChangeBookForm" action="exChangeBookAction_queryData.action"
				name="exChangeBook" method="post">
				<table width="98%" >
					<tr>
						<td width="30%">机&nbsp;构&nbsp;&nbsp;号&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input
							name="queryParam.idBank1" id="idBank" class="diinput_text01"
							value='<s:property value="queryParam.idBank1"/>'/>
						</td>
						<td width="30%">对账日期&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input
							name="queryParam.docDate" id="docDate" class="diinput_text01"  title='对账日期'
							value='<s:property value="queryParam.docDate"/>'
							onclick="new Calendar().show(this);" />
						</td>
						<td width="10%" rowspan="2" align="center"><input
							name="queryData" type="button" class="submit_but09"
							id="queryData" value="查询" onclick="toExChangeBookResult()" />
						</td>
						<td width="7%" rowspan="2" align="center"><input
							name="export" type="button" class="submit_but09" id="export"
							value="导出" onclick="toExportData()" />
						</td>
					</tr>
				</table>
			</form>
		</div>
		<div style="width:100%; height:auto; float:left; margin-left:10px; padding:10px 0px 10px 0px;">
		<input type="button" class="submit_but09" value="回收登记" onclick="bookIn('0')" />
		<input type="button" class="submit_but09" value="发送登记" onclick="bookIn('1')" />
		<br />
		<br/>
		<!-- 
		  <ul style="width:762px;" class="img_button2">
	    	<li style="clear:none;list-style: none;display: inline;"><a onclick="bookIn('0');"><img src="/windforce/ebs/common/images/paramsManageico/add.png" width="24px;"height="24px;" /><span style=" size:13px;">回收登记</span></a></li>
	   	 	<li style="clear:none;list-style: none;display: inline;"><a onclick="bookIn('1');"><img src="/windforce/ebs/common/images/paramsManageico/add.png" width="24px;"height="24px;" /><span style=" size:13px;">发送登记</span></a></li>
	      </ul>
		 -->
			<table width="98%" border="0" cellpadding="0" cellspacing="0"
				id="exChangeBookResultTable">
				<thead>
					<tr>						
						
						<td width="13%" height="26" align="center" bgcolor="#c76c6f"
							class="font_colors01">机构号</td>
						<td width="8%" height="26" align="center" bgcolor="#c76c6f"
							class="font_colors01">对账日期</td>
						<td width="8%" height="26" align="center" bgcolor="#c76c6f"
							class="font_colors01">对账渠道</td>
						<td width="8%" height="26" align="center" bgcolor="#c76c6f"
							class="font_colors01">账单编号</td>
						<td width="11%" height="26" align="center" bgcolor="#c76c6f"
							class="font_colors01">单位名称</td>
						<td width="8%" align="center" bgcolor="#c76c6f"
							class="font_colors01">发送日期</td>
							<td width="8%" align="center" bgcolor="#c76c6f"
							class="font_colors01">回收日期</td>
						<td width="8%" align="center" bgcolor="#c76c6f"
							class="font_colors01">备注</td>
						<td width="8%" align="center" bgcolor="#c76c6f"
							class="font_colors01">登记柜员</td>
						<td width="9%" align="center" bgcolor="#c76c6f"
							class="font_colors01">份数</td>
						<td width="9%" align="center" bgcolor="#c76c6f"
							class="font_colors01">登记类型</td>
					</tr>
				</thead>
				<tbody id="analyse" align="center">
					<s:iterator value="resultList" var="result" status="st">
						<tr bgcolor='<s:if test="#st.count%2==0">#F4DDDF</s:if>'>
							<td><s:property value="#result.idBank" /></td>
							<td><s:property value="#result.docDate" /></td>
							<td><s:property value="valRefCheckflagMap.get(#result.sendmode)" /></td>
							<td><s:property value="#result.voucherNo" /></td>
							<td><s:property value="#result.accName" /></td>
							<td><s:property value="#result.sendDate" /></td>
							<td><s:property value="#result.backDate" /></td>
							<td><s:property value="#result.desc" /></td>
							<td><s:property value="#result.opcode" /></td>
							<td><s:property value="#result.letterSum" /></td>
							<td>
							<s:if test="#result.opType==0">回收</s:if>
							<s:if test="#result.opType==1">发送</s:if></td>
						</tr>
					</s:iterator>
				</tbody>
				<tfoot id="ebillFoot">
					<tr class="pagelinks">
						<td colspan="4">每页显示 <select name="queryParam.pageSize"
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
						<td colspan="8" align="right"><s:if
								test="queryParam.curPage==1">
										<a>首页</a>&nbsp;&nbsp;<a>上一页</a>
									</s:if> <s:else>
								<a href="#" onclick="viewAnalyseListByPage('1');">首页</a>&nbsp;&nbsp;<a
									href="#"
									onclick="viewExChangeBookListByPage'${queryParam.curPage - 1}');return false;">上一页</a>
							</s:else> <s:if test="queryParam.curPage==queryParam.totalPage">
										<a>下一页</a>&nbsp;&nbsp;<a>尾页</a>
									</s:if> <s:else>
								<a href="#"
									onclick="viewExChangeBookListByPage'${queryParam.curPage + 1}');return false;">下一页</a>&nbsp;&nbsp;<a
									href="#"
									onclick="viewExChangeBookListByPage'${queryParam.totalPage}');return false;">尾页</a>
							</s:else>
						</td>
					</tr>
				</tfoot>			</table>
			<h2>
				<s:property value="errMsg" />
			</h2>
		</div>
	</div>
	<input type="hidden" name="idBranch" id="idBranch" value="<s:property value='idBranch'/>"/>
	<input type="hidden" name="idCenter" id="idCenter" value="<s:property value='idCenter'/>"/>
	<input type="hidden" name="idBank" id="idBank" value="<s:property value='idBank'/>"/>
	
</body>
</html>
