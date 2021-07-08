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
<base href="<%=basePath%>" />
<title>对账有效率统计</title>
<meta http-equiv="Content-Type" content="text/html" />
<link href="<%=path%>/ebs/common/css/css.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/process.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/dicss.css"	rel="stylesheet" type="text/css" />

<script type="text/javascript" src="<%=path%>/ebs/common/js/jquery.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/orgTree.js"></script>
<script language="javascript" src="<%=path%>/ebs/common/js/enterToTab.js"></script>
<script language="javascript" src="<%=path%>/ebs/common/js/calendar.js"></script>
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
		if (document.getElementById("docDate").value.length == 0) {
			alert("对账日期不能为空！");
			document.getElementById("docDate").focus();
		}
		var objTable = document.getElementById("analyseResultTable");
		if (objTable.rows.length > 3) {
			window.location.href = "ebillMatchAction_exportData.action";
		} else {
			alert("统计列表无数据");
		}
	}
	
	function toAnalyseResult() {
	   if(IsDate(document.getElementById("docDate")))
	   {
			viewAnalyseListByPage("1");
		}
	}
	
	function viewAnalyseListByPage(pageNumber) {
		if (document.getElementById("docDate").value.length == 0) {
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
			$("#ebillMatchAnalyseForm").submit();
		}
	}
	
	$(document).ready(
			function() {
				var idCenter = "<s:property value='idCenter'/>";
				var idBank = "<s:property value='idBank'/>";
				stopProcess();
				initTree(<%=request.getAttribute("orgTree")%>, idCenter,idBank);
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
	<input id="moudleName" type="hidden" value="对账有效率统计" />
	<%@ include file="/ebs/taskviewtool/processing_div.jsp"%>
	<script type="text/javascript">startProcess("正在初始化界面。。。");</script>
	<form id="ebillMatchAnalyseForm" action="ebillMatchAction_analyse.action"
		name="ebillMatchAnalyse" method="post">
		<div class="nov_moon">
			<div style="width:100%; height:auto; float:left; margin-left:10px; padding:10px 0px 10px 0px;"
				class="border_bottom01">
					<table width="98%">
						<tr>
							<td width="20%">总&nbsp;&nbsp;行&nbsp;&nbsp;
								<select>
									<option value="">华融湘江总行</option>
								</select>
								<input type="hidden" id="curPage" name="queryParam.curPage"
									value='<s:property value="queryParam.curPage"/>' />
							</td>
							<td width="20%">机&nbsp;构&nbsp;&nbsp;号&nbsp;&nbsp;
								<input name="queryParam.idBank1" id="idBank" class="diinput_text01"  maxlength="10" 
									value='<s:property value="queryParam.idBank1"/>' />
							</td>
							<td width="20%">发送方式&nbsp;&nbsp;
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
							<td width="20%">分&nbsp;&nbsp;行&nbsp;&nbsp;
								<select name="queryParam.idCenter" id="selectIdCenter" 
									onchange="changeIdCenter()" onclick="judgeCountIdBranch()">
								</select>
							</td>
							<td width="20%">对账日期&nbsp;&nbsp;
								<input name="queryParam.docDate" id="docDate" 
									class="diinput_text01"  maxlength="10"
									value='<s:property value="queryParam.docDate"/>'
									onclick="new Calendar().show(this);" title='对账日期'/>
							</td>
							<td width="20%">科&nbsp;目&nbsp;号&nbsp;&nbsp;&nbsp;
								<s:select list="refAccTypeMap" listKey="key"
									listValue="value" headerKey="" 
									headerValue="全部" value="queryParam.accType" 
									name="queryParam.accType" id="accType">
								</s:select>
							</td>
							<td width="10%" align="center">
								<input name="export" type="button" class="submit_but09" id="export"
									value="导出" onclick="toExportData()" />
							</td>
						</tr>
						<tr>
							<td width="20%">网&nbsp;&nbsp;点&nbsp;&nbsp;
								<select	name="queryParam.idBank" id="selectIdBank" 
									onchange="changeIdBank()" onclick="judgeSelectIdBank()">
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
							<td width="20%">账户类型&nbsp;&nbsp; 
								<s:select list="refAccCycleMap" listKey="key"
									listValue="value" headerKey="" headerValue="全部"
									value="queryParam.accCycle" name="queryParam.accCycle" id="accCycle">
								</s:select>
							</td>
							<td width="10%">
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
							<td>
							</td>
						</tr>
					</table>
				
			</div>
			<div style="width:98%; height:auto; float:left; margin-left:10px; padding:10px 0px 10px 0px;">
				<h1>对账有效率统计</h1>
				<table width="100%" border="0"  cellpadding="0" cellspacing="0"
					id="analyseResultTable">
					<thead>
						<tr>
							<td style="border:1px solid #C76C6F" width="8%"  align="center" bgcolor="#c76c6f"  colspan="1"
								class="font_colors01" ></td>
							<td style="border:1px solid #C76C6F" width="8%"  align="center" bgcolor="#c76c6f"  colspan="1"
								class="font_colors01"></td>
							<td style="border:1px solid #C76C6F" width="12%"  align="center" bgcolor="#c76c6f"  colspan="1"
								class="font_colors01"></td>
							<td style="border:1px solid #C76C6F" width="12%"  align="center" bgcolor="#c76c6f"  colspan="3"
								class="font_colors01"><label id="cycle1">第一周期</label></td>
							<td style="border:1px solid #C76C6F" width="12%"  align="center" bgcolor="#c76c6f"   colspan="3"
								class="font_colors01"><label id="cycle2">第二周期</label></td>
							<td style="border:1px solid #C76C6F" width="12%"  align="center" bgcolor="#c76c6f"   colspan="3"
								class="font_colors01"><label id="cycle3">第三周期</label></td>
							<td style="border:1px solid #C76C6F" width="12%"  align="center" bgcolor="#c76c6f"   colspan="3"
								class="font_colors01"><label id="cycle4">第四周期</label></td>
							<td style="border:1px solid #C76C6F" width="12%" align="center" bgcolor="#c76c6f"   colspan="3"
								class="font_colors01"><label id="cycle5">第五周期</label></td>
							<td style="border:1px solid #C76C6F" width="12%" align="center" bgcolor="#c76c6f"  colspan="3"
								class="font_colors01"><label id="cycle6">第六周期</label></td>
						</tr>
						<tr>
						   <td style="border:1px solid #C76C6F" width="8%"  align="center" bgcolor="#c76c6f" 
								class="font_colors01">序号</td>
						   <td style="border:1px solid #C76C6F" width="8%"  align="center" bgcolor="#c76c6f" 
								class="font_colors01">网点</td>
							<td style="border:1px solid #C76C6F" width="12%"  align="center" bgcolor="#c76c6f" 
								class="font_colors01">网点名称</td>
						   <td style="border:1px solid #C76C6F" width="4%" align="center" bgcolor="#c76c6f" 
								class="font_colors01">账户数</td>
							<td style="border:1px solid #C76C6F" width="4%" align="center" bgcolor="#c76c6f"
								class="font_colors01">成功数</td>
							<td style="border:1px solid #C76C6F" width="4%" align="center" bgcolor="#c76c6f"
								class="font_colors01">有效率</td>
							<td style="border:1px solid #C76C6F" width="4%" align="center" bgcolor="#c76c6f"
								class="font_colors01">账户数</td>
							<td style="border:1px solid #C76C6F" localhost:1521:EBS width="4%" align="center" bgcolor="#c76c6f"
								class="font_colors01">成功数</td>
							<td style="border:1px solid #C76C6F" width="4%" align="center" bgcolor="#c76c6f"
								class="font_colors01">有效率</td>
							<td style="border:1px solid #C76C6F" width="4%" align="center" bgcolor="#c76c6f"
								class="font_colors01">账户数</td>
							<td style="border:1px solid #C76C6F" width="4%" align="center" bgcolor="#c76c6f"
								class="font_colors01">成功数</td>
							<td style="border:1px solid #C76C6F" width="4%" align="center" bgcolor="#c76c6f"
								class="font_colors01">有效率</td>
							<td style="border:1px solid #C76C6F" width="4%" align="center" bgcolor="#c76c6f"
								class="font_colors01">账户数</td>
							<td style="border:1px solid #C76C6F" width="4%" align="center" bgcolor="#c76c6f"
								class="font_colors01">成功数</td>
							<td style="border:1px solid #C76C6F" width="4%" align="center" bgcolor="#c76c6f"
								class="font_colors01">有效率</td>
							<td style="border:1px solid #C76C6F" width="4%" align="center" bgcolor="#c76c6f"
								class="font_colors01">账户数</td>
							<td style="border:1px solid #C76C6F" width="4%" align="center" bgcolor="#c76c6f"
								class="font_colors01">成功数</td>
							<td style="border:1px solid #C76C6F" width="4%" align="center" bgcolor="#c76c6f"
								class="font_colors01">有效率</td>
							<td style="border:1px solid #C76C6F" width="4%" align="center" bgcolor="#c76c6f"
								class="font_colors01">账户数</td>
							<td style="border:1px solid #C76C6F" width="4%" align="center" bgcolor="#c76c6f"
								class="font_colors01">成功数</td>
							<td style="border:1px solid #C76C6F" width="4%" align="center" bgcolor="#c76c6f"
								class="font_colors01">有效率</td>
							
						</tr>
					</thead>
					<tbody id="analyse" align="center">
						<s:iterator value="resultList" var="result" status="st">
							<tr bgcolor='<s:if test="#st.count%2==0">#F4DDDF</s:if>'>
							    <td style="border:1px solid #C76C6F" align="center"><s:property	value="#st.count+queryParam.firstResult" /></td>
								<td style="border:1px solid #C76C6F"><s:property value="#result.idBank" /></td>
								<td style="border:1px solid #C76C6F"><s:property value="#result.cname"/></td>
								<td style="border:1px solid #C76C6F"><s:property value="#result.SendCount1" /></td>
								<td style="border:1px solid #C76C6F"><s:property value="#result.MatchCount1" /></td>
								<td style="border:1px solid #C76C6F"><s:property value="#result.fstMatchPercent" /></td>
								<td style="border:1px solid #C76C6F"><s:property value="#result.SendCount2" /></td>
								<td style="border:1px solid #C76C6F"><s:property value="#result.MatchCount2" /></td>
								<td style="border:1px solid #C76C6F"><s:property value="#result.secMatchPercent" /></td>
								<td style="border:1px solid #C76C6F"><s:property value="#result.SendCount3" /></td>
								<td style="border:1px solid #C76C6F"><s:property value="#result.MatchCount3" /></td>
								<td style="border:1px solid #C76C6F"><s:property value="#result.thrMatchPercent" /></td>
								<td style="border:1px solid #C76C6F"><s:property value="#result.SendCount4" /></td>
								<td style="border:1px solid #C76C6F"><s:property value="#result.MatchCount4" /></td>
								<td style="border:1px solid #C76C6F"><s:property value="#result.fouMatchPercent" /></td>
								<td style="border:1px solid #C76C6F"><s:property value="#result.SendCount5" /></td>
								<td style="border:1px solid #C76C6F"><s:property value="#result.MatchCount5" /></td>
								<td style="border:1px solid #C76C6F"><s:property value="#result.fifMatchPercent" /></td>
								<td style="border:1px solid #C76C6F"><s:property value="#result.SendCount6" /></td>
								<td style="border:1px solid #C76C6F"><s:property value="#result.MatchCount6" /></td>
								<td style="border:1px solid #C76C6F"><s:property value="#result.sixMatchPercent" /></td>
							</tr>
						</s:iterator>
					</tbody>
					<tfoot id="ebillFoot">
						<tr class="pagelinks">
							<td colspan="6">每页显示 
								<select name="queryParam.pageSize"
									id="pageSize" style="width:40px;"
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
							<td colspan="15" align="right">
								<s:if
									test="queryParam.curPage==1">
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
									<a href="#"	onclick="viewAnalyseListByPage('${queryParam.totalPage}');return false;">尾页</a>
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
