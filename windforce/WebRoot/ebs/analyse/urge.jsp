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
<title>退信情况统计</title>
<link href="<%=path%>/ebs/common/css/process.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/css.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/dicss.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="<%=path%>/ebs/common/js/jquery.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/orgTree.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/process.js"></script>
<script language="javascript"  src="<%=path%>/ebs/common/js/calendar.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/accoper.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/radioJudge.js"></script>


<script type="text/javascript">
	function toGetStatistics() {
		if(IsDate(document.getElementById("docDate"))){
			viewStatisticsDetail("1");
		}
	}
	
	function viewStatisticsDetail(pageNumber) {
		if (document.getElementById("docDate").value == "") {
			alert("对账日期不能为空！");
			document.getElementById("docDate").focus();
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
			$("#urgeStatisticsForm").submit();
		}
	}
	
	//回车转Tab
	function cgo(obj, element, method) {
		var e = event ? event : window.event;
		if (e.keyCode == 13) {
			for ( var i = 0; i < obj.length; i++) {
				if (obj[i].name == element) {
					id = i;
				}
			}
			obj[id + 1].focus();
		}
	}
	
	function toExportData() {
		var objTable = document.getElementById("urgeStatisticsList");	
		if (objTable.rows.length>2) {
			window.location.href = 'urgeStatisticsAction_exportData.action';
		}else{
			alert("统计结果列表无数据");
		}
	}
	
	$(document).ready(function() {
    	var idCenter="<s:property value='idCenter'/>";
		var idBank="<s:property value='idBank'/>";
		stopProcess();
		initTree(<%=request.getAttribute("orgTree")%>,idCenter,idBank);
	});
	
</script>

</head>
<body class="baby_in2">
<%@ include file="/ebs/taskviewtool/processing_div.jsp"%>
	<script type="text/javascript">startProcess("正在初始化界面。。。");</script>
	<input id="moudleName" type="hidden" value="退信情况统计"/>
	<form id="urgeStatisticsForm" action="urgeStatisticsAction_getResult.action"
		name="urgeStatistics" method="post">
		<div class="nov_moon">
			<div style="width:100%; height:auto; float:left; margin-left:10px; 
			padding:10px 0px 10px 0px;" class="border_bottom01">
            	<input type="hidden" name="urgeStatisticsQueryParam.curPage" id="curPage" />
				<table width="98%">
				<tr>
					<td width="20%">总&nbsp;&nbsp;&nbsp;&nbsp; 行&nbsp;&nbsp;&nbsp;&nbsp;
						<select >
							<option value=""> 华融湘江总行</option>
						</select>
					</td>
				 	<td width="20%">机 构 号&nbsp;
				 		<input name="urgeStatisticsQueryParam.idBank1" type="text" 
							class="diinput_text01" id="idBank" maxlength="12"
							value='<s:property value="urgeStatisticsQueryParam.idBank1"/>' />
					</td>
					<td width="10%" align="center">
						<input name=queryData type="button" class="submit_but09" id="find" 
							value="统计" onclick="toGetStatistics()" />
					</td>
			    </tr>
				<tr>
					<td width="20%">分&nbsp;&nbsp;&nbsp;&nbsp;	行&nbsp;&nbsp;&nbsp;&nbsp;
						<select name="urgeStatisticsQueryParam.idCenter" 
							id="selectIdCenter" onchange="changeIdCenter()" onclick="judgeCountIdBranch()">
						</select>
					</td>
					<td width="20%">对账日期 
						<input title="对账日期"  name="urgeStatisticsQueryParam.docDate" type="text"
							class="input_text01" id="docDate"  maxlength="10" onclick="new Calendar().show(this);" 
							value='<s:property value="urgeStatisticsQueryParam.docDate"/>'/>
					</td>
					<td width="10%" align="center">
						<input name="btnexport" type="button" class="submit_but09"
						id="btnexport" value="导出" onclick="toExportData()" />
					</td>
				</tr>
				<tr>
					<td width="20%">网&nbsp;&nbsp;&nbsp;&nbsp;点 &nbsp;&nbsp;&nbsp;&nbsp;
						<select	name="urgeStatisticsQueryParam.idBank" id="selectIdBank"  
							 onchange="changeIdBank()" onclick="judgeSelectIdBank()" >
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
				</tr>
				<tr>
					<td>
					</td>
					<td>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<span id="alertErr"name="alertErr" ></span>
					</td>
					<td>
					</td>
				</tr>
			</table>
		</div>
		<div style="width:98%; height:auto; float:left; margin-left:10px; padding:10px 0px 10px 0px;">
			<h1>退信情况统计</h1>
				<table width="100%" border="0" cellpadding="0" cellspacing="0"id="urgeStatisticsList">
					<thead>
						<tr>
						    <td width="5%" height="26" align="center" bgcolor="#c76c6f"	class="font_colors01">序号</td>
						
							<td width="6%" align="center" bgcolor="#c76c6f"	class="font_colors01">分行</td>
							<td width="6%" align="center" bgcolor="#c76c6f"	class="font_colors01">网点</td>
							<td width="12%"align="center" bgcolor="#c76c6f"class="font_colors01">网点名称</td>
							<td width="6%" align="center" bgcolor="#c76c6f"	class="font_colors01">对账<br/>日期</td>
							<td width="6%" align="center" bgcolor="#c76c6f" class="font_colors01">对账单<br/>发出数</td>	
							<td width="6%" align="center" bgcolor="#c76c6f"	class="font_colors01">退信<br/>总数</td>
							<td width="6%" align="center" bgcolor="#c76c6f"	class="font_colors01">单位<br/>拒收数</td>
							<td width="6%" align="center" bgcolor="#c76c6f"	class="font_colors01">原址<br/>拆迁数</td>
							<td width="6%" align="center" bgcolor="#c76c6f"	class="font_colors01">地址<br/>不详数</td>
							<td width="6%" align="center" bgcolor="#c76c6f"	class="font_colors01">投递<br/>无人数</td>
							<td width="6%" align="center" bgcolor="#c76c6f"	class="font_colors01">无此<br/>单位数</td>
							<td width="6%" align="center" bgcolor="#c76c6f"	class="font_colors01">无此<br/>地址数</td>
							<td width="6%" align="center" bgcolor="#c76c6f"	class="font_colors01">无法<br/>联系数</td>
							<td width="6%" align="center" bgcolor="#c76c6f"	class="font_colors01">其他<br/>原因数</td>
							<td width="5%" align="center" bgcolor="#c76c6f"	class="font_colors01">退信率	</td>
						</tr>
					</thead>
					<tbody id="queryList" align="center">
						<s:iterator value="resultList" var="result" status="st">
							<tr  <s:if test="#st.index%2 ==0">bgcolor="#CBD6E0"</s:if>
								<s:if test="#st.index%2 ==1">bgcolor="#FFFFFF"</s:if>>
								<td><s:property value="#st.count+urgeStatisticsQueryParam.firstResult" /></td>
								<td><s:property value="#result.idCenter" /></td>
								<td><s:property value="#result.idBank" /></td>
								<td><s:property value="#result.bankName" /></td>
								<td><s:property value="#result.docDate" /></td>
								<td><s:property value="#result.totalVouAmount" /></td>
								<td><s:property value="#result.totalUrgeAmount" /></td>
								<td><s:property value="#result.rejectedAmount" /></td>
								<td><s:property value="#result.addrChangedAmount" /></td>
								<td><s:property value="#result.addrUnknownAmount" /></td>
								<td><s:property value="#result.noRecieverAmount" /></td>
								<td><s:property value="#result.unitNotExistAmount" /></td>
								<td><s:property value="#result.addrNotExistAmount" /></td>
								<td><s:property value="#result.noConnectionAmount" /></td>
								<td><s:property value="#result.otherAmount" /></td>
								<td><s:property value="#result.urgeRate" /></td>
							</tr>
						</s:iterator>
					</tbody>
					<tfoot id="urgeStatisticsFoot">
						<tr class="pagelinks">
							<td colspan="7">每页显示 
								<select name="urgeStatisticsQueryParam.pageSize" id="pageSize" style="width:40px;"
									onchange="viewStatisticsDetail('1')">
									<option value="10"
										<s:if test="urgeStatisticsQueryParam.pageSize==10">selected="selected"</s:if>>10</option>
									<option value="20"
										<s:if test="urgeStatisticsQueryParam.pageSize==20">selected="selected"</s:if>>20</option>
									<option value="50"
										<s:if test="urgeStatisticsQueryParam.pageSize==50">selected="selected"</s:if>>50</option>
								</select>条记录&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;当前显示
								<s:if test="urgeStatisticsQueryParam.total==0">0</s:if> 
								<s:else>
									<s:property value="urgeStatisticsQueryParam.firstResult+1" />
								</s:else>到<s:property value="urgeStatisticsQueryParam.lastResult" />条，共
								<s:property value="urgeStatisticsQueryParam.total" />条</td>
							<td colspan="12" align="right">
								<s:if test="urgeStatisticsQueryParam.curPage==1">
									<a>首页</a>&nbsp;&nbsp;
									<a>上一页</a>
								</s:if> 
								<s:else>
									<a href="#" onclick="viewStatisticsDetail('1');">首页</a>&nbsp;&nbsp;
									<a href="#"	onclick="viewStatisticsDetail('${urgeStatisticsQueryParam.curPage - 1}');return false;">上一页</a>
								</s:else> 
								<s:if test="urgeStatisticsQueryParam.curPage==urgeStatisticsQueryParam.totalPage">
									<a>下一页</a>&nbsp;&nbsp;
									<a>尾页</a>
								</s:if> 
								<s:else>
									<a href="#" onclick="viewStatisticsDetail('${urgeStatisticsQueryParam.curPage + 1}');return false;">下一页</a>&nbsp;&nbsp;
									<a href="#"	onclick="viewStatisticsDetail('${urgeStatisticsQueryParam.totalPage}');return false;">尾页</a>
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
