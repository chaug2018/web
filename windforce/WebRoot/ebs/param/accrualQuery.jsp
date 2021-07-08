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
<title>发生额明细查询 </title>

<meta http-equiv="Content-Type" content="text/html" />
<link href="<%=path%>/ebs/common/css/css.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/dicss.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/process.css" rel="stylesheet" type="text/css" />
	
<script type="text/javascript" src="<%=path%>/ebs/common/js/jquery.js"></script>
<script language="javascript" src="<%=path%>/ebs/common/js/calendar.js"></script>
<script language="javascript" src="<%=path%>/ebs/common/js/enterToTab.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/orgTree.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/process.js"></script>

<script type="text/javascript">
	function queryAccnoDetailList(){
		viewAccnoDetailListByPage("1");
	}
	
	function viewAccnoDetailListByPage(pageNumber){
		if(document.getElementById("accNo").value.length == 0){
			alert("账号不能为空！");
			document.getElementById("accNo").focus();
		}else if(document.getElementById("workdate").value.length == 0){
			alert("交易日期不能为空！");
			document.getElementById("workdate").focus();
		}else{
			if(IsDate(document.getElementById("workdate"))){
				$("#curPage").val(pageNumber);
				$("#accnoDetailQueryForm").submit();
			}else{
				document.getElementById("workdate").focus();
			}
		}
	}
	
	function toExportData() {
		var objTable = document.getElementById("accrualQueryInfo");	
		if (objTable.rows.length>2) {
		window.location.href = 'accnoDetailQuery_exportData.action';
		}else{
			alert("查询结果列表无数据");
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
	
	$(document).ready(function(){
		var idCenter="<s:property value='idCenter'/>";
		var idBank="<s:property value='idBank'/>";
		initTree(<%=request.getAttribute("orgTree")%>,idCenter,idBank);
	});
</script>

</head>

<body class="baby_in2">
	<input id="moudleName" type="hidden" value="发生额明细查询" />
	<form id="accnoDetailQueryForm" method="post"
		action="accnoDetailQuery_queryAccnoDetailData.action" name="accnoDetailQuery">
		<div class="nov_moon">
			<div style="width:100%; height:auto; float:left; margin-left:10px; padding:10px 0px 10px 0px;"
				class="border_bottom01">
				<table width="98%">
					<tr>
						<td width="20%">总 行&nbsp;&nbsp;&nbsp; 
								<select>
									<option value=""> 华融湘江总行</option>
								</select>
							</td>
						<td width="20%">
								网 &nbsp;&nbsp;&nbsp;点&nbsp;&nbsp;
							<select onchange="changeIdBank()"  id="selectIdBank">
							</select>
						</td>
						<td width="20%">
							账&nbsp;&nbsp;&nbsp;&nbsp;号&nbsp;&nbsp;
							<input name="accnoDetailQueryParam.accNo" type="text"
								class="diinput_text01" id="accNo" onkeydown="cgo(this.form,this.name,'')" 
								maxlength="32" value='<s:property value="accnoDetailQueryParam.accNo"/>' />
						</td>
						<td width="10%">
							<input name=queryData type="button" class="submit_but09" id="find"
								value="查询" onclick="queryAccnoDetailList()" />
						</td>
					</tr>
					<tr>
						<td width="20%">分 行&nbsp;&nbsp;&nbsp; 
								<select name="accnoDetailQueryParam.idCenter" id="selectIdCenter" onchange="changeIdCenter()">
								</select>
							</td>
						<td width="20%">
							机 构 号&nbsp;
							<input name="accnoDetailQueryParam.idBank" type="text"
								class="diinput_text01" id="idBank" maxlength="12"
								value='<s:property value="accnoDetailQueryParam.idBank"/>' />
						</td>
						<td width="20%">
							交易日期
							<input name="accnoDetailQueryParam.workDate" type="text"
								class="diinput_text01" id="workdate" title='交易日期'
								onclick="new Calendar().show(this);" maxlength="10"
								value='<s:property value="accnoDetailQueryParam.workDate"/>' />
							<input type="hidden" id="curPage" name="accnoDetailQueryParam.curPage"
								class="diinput_text01" value='<s:property value="accnoDetailQueryParam.curPage"/>' />
						</td>
						<td width="10%">
							<input name="btnexport" type="button" class="submit_but09" id="btnexport"
								value="导出" onclick="toExportData()" />
						</td>
					</tr>
				</table>
			</div>
			<div style="width:100%; height:auto; float:left; margin-left:10px; padding:10px 0px 10px 0px; color: #000000;">
				<h1>查询结果列表</h1>
				<table width="98%" border="0" cellpadding="0" cellspacing="0"
					id="accrualQueryInfo">
					<tr>
						<td width="5%" height="26" align="center" bgcolor="#c76c6f" class="font_colors01">序号</td>
						<td width="10%" align="center" bgcolor="#c76c6f" class="font_colors01">发生日期</td>
						<td width="10%" align="center" bgcolor="#c76c6f" class="font_colors01">发生额</td>
						<td width="10%" align="center" bgcolor="#c76c6f" class="font_colors01">借贷方向</td>
						<td width="10%" align="center" bgcolor="#c76c6f" class="font_colors01">对方账号</td>
						<td width="20%" align="center" bgcolor="#c76c6f" class="font_colors01">对方户名</td>
						<td width="10%" align="center" bgcolor="#c76c6f" class="font_colors01">余额</td>
						<td width="13%" align="center" bgcolor="#c76c6f" class="font_colors01">开户行</td>
						<td width="10%" align="center" bgcolor="#c76c6f" class="font_colors01">交易序号</td>
						<!--  
						<td width="12%" align="center" bgcolor="#c76c6f"
							class="font_colors01">核对结果</td>
						-->
					</tr>
					<tbody id="queryList" align="center">
						<s:iterator value="queryList" var="queryData" status="st">
							<tr bgcolor='<s:if test="#st.count%2==0">#F4DDDF</s:if>'>
								<td>
									<s:property value="#st.count+accnoDetailQueryParam.firstResult" />
								</td>
								<td>
									<s:property value="#queryData.workDate" />
								</td>
								<td>
									<s:property value="#queryData.strtraceBal" />
								</td>

								<td align="center">
									<s:property value="docflagMap.get(#queryData.dcFlag)" />
								</td>
								<td>
									<s:property value="#queryData.to_Accno" />
								</td>
								<td>
									<s:property value="#queryData.to_Accname" />
								</td>
								<td>
									<s:property value="#queryData.strcredit" />
								</td>
								<!-- 代理行:网点 -->
								<td>
									<s:property value="#queryData.idBank" />
								</td>
								<td>
									<s:property value="#queryData.traceNo" />
								</td>
								<!-- 
								<td align="center"><s:iterator value="refCheckflagMap.keySet()" id="id">
										<s:if test="#queryData.checkFlag==#id">
											<s:property value="refCheckflagMap.get(#id)" />
										</s:if>
									</s:iterator>
								</td>
								 -->
							</tr>
						</s:iterator>
					</tbody>

					<tfoot id="accnoDetailListPageInfo">
						<tr class="pagelinks">
							<td colspan="4">
								每页显示
								<select name="accnoDetailQueryParam.pageSize" id="pageSize"
									style="width:40px;" onchange="viewAccnoDetailListByPage('1')">
									<option value="10"
										<s:if test="accnoDetailQueryParam.pageSize==10">selected="selected"</s:if>>10</option>
									<option value="20"
										<s:if test="accnoDetailQueryParam.pageSize==20">selected="selected"</s:if>>20</option>
									<option value="50"
										<s:if test="accnoDetailQueryParam.pageSize==50">selected="selected"</s:if>>50</option>
								</select>
								条记录&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;当前显示
								<s:if test="accnoDetailQueryParam.total==0">0</s:if>
								<s:else>
									<s:property value="accnoDetailQueryParam.firstResult+1" />
								</s:else>
								到
								<s:property value="accnoDetailQueryParam.lastResult" />
								条，共
								<s:property value="accnoDetailQueryParam.total" />
								条
							</td>
							<td colspan="8" align="right">
								<s:if test="accnoDetailQueryParam.curPage==1">
									<a>首页</a>&nbsp;&nbsp;<a>上一页</a>
								</s:if>
								<s:else>
									<a href="#" onclick="viewAccnoDetailListByPage('1');">首页</a>&nbsp;&nbsp;<a
										href="#"
										onclick="viewAccnoDetailListByPage('${accnoDetailQueryParam.curPage - 1}');return false;">上一页</a>
								</s:else>
								<s:if
									test="accnoDetailQueryParam.curPage==accnoDetailQueryParam.totalPage">
									<a>下一页</a>&nbsp;&nbsp;<a>尾页</a>
								</s:if>
								<s:else>
									<a href="#"
										onclick="viewAccnoDetailListByPage(${accnoDetailQueryParam.curPage + 1});return false;">下一页</a>&nbsp;&nbsp;<a
										href="#"
										onclick="viewAccnoDetailListByPage('${accnoDetailQueryParam.totalPage}');return false;">尾页</a>
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
