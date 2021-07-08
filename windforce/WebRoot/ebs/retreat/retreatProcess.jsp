<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/ebs/common/js/jquery.js"></script>
<script language="javascript"
	src="${pageContext.request.contextPath}/ebs/common/js/calendar.js"></script>
<script language="javascript"
	src="${pageContext.request.contextPath}/ebs/common/js/enterToTab.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/ebs/common/js/orgTree.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/ebs/common/js/retreatProcess.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/ebs/common/js/process.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/ebs/common/js/task.js"></script>
<link
	href="${pageContext.request.contextPath}/ebs/common/css/process.css"
	rel="stylesheet" type="text/css" />
<title>退信处理</title>
<meta http-equiv="Content-Type" content="text/html" />

<link
	href="${pageContext.request.contextPath}/ebs/common/css/css.css"
	rel="stylesheet" type="text/css" />
<link
	href="${pageContext.request.contextPath}/ebs/common/css/dicss.css"
	rel="stylesheet" type="text/css" />
<script type="text/javascript">
		var err = "<s:property value='errMsg'/>";
		if (err != null && err.length > 0) {
			alert("初始化服务出现错误");
			top.refresh();
		}
</script>
<script type="text/javascript">
 function CheckAll(form) {
		for ( var i = 0; i < form.elements.length; i++) {
			var e = form.elements[i];
			if (e.Name != 'chkAll' && e.disabled == false)
				e.checked = form.chkAll.checked;
		}
	}
	function queryRetreatList() {
		viewRetreathListByPage("1");
	}

	function viewRetreathListByPage(pageNumber) {
			if(IsDate(document.getElementById("docDate"))){
				if(IsDate(document.getElementById("urgeDate"))){
					$("#curPage").val(pageNumber);
					$("#retreatQueryForm").submit();
				}else{
					document.getElementById("urgeDate").focus();
				}
			}else{
				document.getElementById("docDate").focus();
			}
			
	}

	function toExportData() {
		var objTable = document.getElementById("retreatListInfo");
		if (objTable.rows.length > 2) {
			window.location.href = 'retreatProcessAction_exportData.action';
		} else {
			alert("查询结果列表无数据");
		}
	}
	$(document).ready(function(){
	var idCenter="<s:property value='idCenter'/>";
	var idBank="<s:property value='idBank'/>";
	stopProcess();
	initTree(<%=request.getAttribute("orgTree")%>,idCenter,idBank);
	});
	function fnCheck() 
{ 
	var e = document.getElementsByName( "selectId"); 
	var num = 0; 
	for( var i = 0; i < e.length; i++ ){ 
	if( e[i].checked==true) 
		num++; 
	} 
	if(num < 1 ){ 
	return false; 
	}else{
	return true;}
}
function batchProcess(){
	if(fnCheck()){
		showSon2();	
	}else{
		alert("请选择至少一条要维护的记录");
	}
}
</script>

</head>
<body class="baby_in2">
<input id="moudleName" type="hidden" value="退信处理"/>
	<%@ include file="/ebs/taskviewtool/processing_div.jsp"%>
	<script type="text/javascript">startProcess("正在初始化界面。。。");</script>
	<div id="son_div" class="son_div" style="background:url('/windforce/ebs/common/images/bg_content.jpg')">
		<div class="sonpage" id="sonpage">
			<%@ include file="/ebs/retreat/retreatProcess_div.jsp"%>
		</div>
	</div>
	<div id="son2_div" class="son2_div" style="background:url('/windforce/ebs/common/images/bg_content.jpg')">
		<div class="sonpage2" id="sonpage2">
			<%@ include file="/ebs/retreat/batchProcess_div.jsp"%>
		</div>
	</div>
	<form id="retreatQueryForm"
		action="retreatProcessAction_queryRetreatData.action"
		name="retreatQuery" method="post">
		<div class="nov_moon">
			<div
				style="width:98%; height:auto; float:left; margin-left:10px; padding:10px 0px 10px 0px;"
				class="border_bottom01">

				<table width="100%">
					<tr>
						<td>总 行&nbsp;&nbsp;&nbsp; 
								<select style="width:160px" class="input_text01">
									<option value=""> 华融湘江总行</option>
								</select>
							</td>
						<td width="30%">机&nbsp;&nbsp;构&nbsp;&nbsp;号&nbsp; <input
							name="retreatQueryParam.idBank1" id="idBank"  maxlength="10"
							class="diinput_text01" onkeydown="cgo(this.form,this.name,'')"  style="width:160px" 
							value='<s:property value="retreatQueryParam.idBank1"/>' /><input
							type="hidden" id="curPage" name="retreatQueryParam.curPage"
							value='<s:property value="retreatQueryParam.curPage"/>' />
						</td>
						<td width="30%"><div align="left">
								登记日期&nbsp;&nbsp; <input name="retreatQueryParam.urgeDate"  title='登记日期' 
									type="text" class="diinput_text01" id="urgeDate"
									onclick="new Calendar().show(this);"  maxlength="10"  style="width:160px" 
									onkeydown="cgo(this.form,this.name,'')"
									value='<s:property value="retreatQueryParam.urgeDate"/>' />
							</div></td>
						<td width="10%" rowspan="2" align="center"><input
							name="queryData" type="button" class="submit_but09" 
							id="queryData" value="查询" onclick="queryRetreatList()"
							onkeydown="cgo(this.form,this.name,'')" />
						</td>
					</tr>
					<tr>
						<td width="30%">分 行&nbsp;&nbsp;&nbsp; 
								<select
								name="retreatQueryParam.idCenter" id="selectIdCenter"  style="width:160px" 
								class="input_text01" onchange="changeIdCenter()">
								</select>
						</td>
						<td width="30%"><div align="left">
								对账日期&nbsp;&nbsp; <input name="retreatQueryParam.docDate"
									type="text" class="diinput_text01" id="docDate"  maxlength="10"  style="width:160px" 
									onclick="new Calendar().show(this);"   title='对账日期' 
									onkeydown="cgo(this.form,this.name,'')"
									value='<s:property value="retreatQueryParam.docDate"/>' />
							</div></td>
						<td width="30%"><div align="left">
								登记类型&nbsp;&nbsp;
								<s:select onkeydown="cgo(this.form,this.name,'')"
									list="refUrgeTypeMap" class="input_text01" listKey="key"
									listValue="value" headerKey="" headerValue="--请选择--"
									name="retreatQueryParam.urgeType" id="urgeType"
									value="retreatQueryParam.urgeType">
								</s:select>
							</div></td>
<td width="10%"></td>

					</tr>
					<tr>
						<td width="30%">网 点&nbsp;&nbsp;&nbsp; <select
							name="retreatQueryParam.idBank" id="selectIdBank"  style="width:160px" 
							class="input_text01" onkeydown="cgo(this.form,this.name,'')" onchange="changeIdBank()">
						</select>
						</td>
						<td width="30%">账单编号&nbsp;&nbsp; <input
							onkeydown="cgo(this.form,this.name,'')"
							name="retreatQueryParam.voucherNo" type="text"  style="width:160px" 
							class="diinput_text01" id="voucherNo"  maxlength="32"
							value='<s:property value="retreatQueryParam.voucherNo"/>' />
						</td>
						<td width="30%">处理状态&nbsp;&nbsp; <s:select
								list="refUrgeFlagMap" onkeydown="cgo(this.form,this.name,'')"
								class="input_text01" listKey="key" listValue="value"
								name="retreatQueryParam.urgeFlag"
								value="retreatQueryParam.urgeFlag" id="urgeFlag">
							</s:select>
						</td>
						<td width="10%" rowspan="2" align="center"><input 
							name="btnexport" type="button" class="submit_but09"
							id="btnexport" value="导出" onclick="toExportData()" />
						</td>
					</tr>
					<tr>
						<td width="30%">
						</td>
						<td width="30%">
						</td>
						<td width="30%">发送渠道&nbsp;&nbsp; <s:select
								list="refSendMode" onkeydown="cgo(this.form,this.name,'')"
								class="input_text01" listKey="key" listValue="value"
								 headerKey="" headerValue="全部"
								name="retreatQueryParam.sendModeFlag"
								value="retreatQueryParam.sendModeFlag" id="urgeFlag">
							</s:select>
						</td>
					</tr>
				</table>

			</div>
			<div
				style="width:100%; height:auto; float:left; margin-left:10px; padding:10px 0px 10px 0px;">
				
				<table width="98%"><tr>
			<td width="90%"><h1>退信信息列表</h1></td>
			<td width="10%"><input type="button" value="批量处理" onclick="batchProcess()"
				class="submit_but09" /></td>
			</tr></table>
				<table width="98%" border="0" cellpadding="0" cellspacing="0"
					id="retreatListInfo">
					<thead>
						<tr>
						<td width="3%" height="26" align="center" bgcolor="#c76c6f"
							class="font_colors01"><input name="selectall"
							type="checkbox" id="chkAll" onclick="CheckAll(this.form)"
							value="checkbox"></input>
						</td>
							<td width="4%" height="26" align="center" bgcolor="#c76c6f"
								class="font_colors01">序号</td>
							<td width="5%" align="center" bgcolor="#c76c6f"
								class="font_colors01">机构号</td>
							<td width="6%" align="center" bgcolor="#c76c6f"
								class="font_colors01">账单编号</td>
							<td width="10%" align="center" bgcolor="#c76c6f"
								class="font_colors01">账户名称</td>
							<td width="5%" align="center" bgcolor="#c76c6f"
								class="font_colors01">登记日期</td>
							<td width="6%" align="center" bgcolor="#c76c6f"
								class="font_colors01">登记类型</td>
							<td width="9%" align="center" bgcolor="#c76c6f"
								class="font_colors01">摘&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;要</td>
							<td width="8%" align="center" bgcolor="#c76c6f"
								class="font_colors01">处理意见</td>
							<td width="11%" align="center" bgcolor="#c76c6f"
								class="font_colors01">处理备注</td>
							<td width="6%" align="center" bgcolor="#c76c6f"
								class="font_colors01">处理状态</td>
							<td width="6%" align="center" bgcolor="#c76c6f"
								class="font_colors01">已退信次数</td>
								<td width="6%" align="center" bgcolor="#c76c6f"
								class="font_colors01">处理柜员</td>
								<td width="6%" align="center" bgcolor="#c76c6f"
								class="font_colors01">处理日期</td>
								<td width="6%" align="center" bgcolor="#c76c6f"
								class="font_colors01">发送渠道</td>
							<td width="5%" align="center" bgcolor="#c76c6f"
								class="font_colors01">操作</td>
						</tr>
					</thead>


					<tbody id="queryList" align="center">
						<s:iterator value="queryList" var="queryData" status="st">
							<tr bgcolor='<s:if test="#st.count%2==0">#F4DDDF</s:if>'>
							
							<td><input type="checkbox" name="selectId"
									value='<s:property value="#st.count"/>' /></td>
									
								<td><s:property
										value="#st.count+retreatQueryParam.firstResult" /></td>
								<td><s:property value="#queryData.idCenter" /></td>
								<td><s:property value="#queryData.voucherNo" /></td>
								<td><s:property value="#queryData.accName" /></td>
								<td><s:property value="#queryData.urgeDate" /></td>
								<td><s:iterator value="refUrgeTypeMap.keySet()" id="id">
										<s:if test="#queryData.urgeType==#id">
											<s:property value="refUrgeTypeMap.get(#id)" />
										</s:if>
									</s:iterator>
								</td>
								<td><s:property value="#queryData.urgeNote" /></td>
								<td><s:iterator value="refUrgeResultMap.keySet()" id="id">
										<s:if test="#queryData.urgeResult==#id">
											<s:property value="refUrgeResultMap.get(#id)" />
										</s:if>
									</s:iterator>
								</td>
								<td><s:property value="#queryData.urgeDesc" /></td>
								<td><s:iterator value="refUrgeFlagMap.keySet()" id="id">
										<s:if test="#queryData.urgeFlag==#id">
											<s:property value="refUrgeFlagMap.get(#id)" />
										</s:if>
									</s:iterator>
								</td>
								<td><s:property value="#queryData.urgeTimes" /></td>
								<td><s:property value="#queryData.urgePeople1" /></td>
								<td><s:property value="#queryData.urgeDate1" /></td>
								<td><s:property value="refSendMode.get(#queryData.sendMode)" /></td>
								<td>
								<a onclick="modifyAcc(this)" style="color:#BE333A">处理</a>
								</td>
							</tr>
						</s:iterator>
					</tbody>
					<tfoot>
						<tr class="pagelinks">
							<td colspan="5">每页显示 <select
							name="retreatQueryParam.pageSize" id="pageSize"
							style="width:40px;" onchange="viewRetreathListByPage('1')">
								<option value="10"
									<s:if test="retreatQueryParam.pageSize==10">selected="selected"</s:if>>10</option>
								<option value="20"
									<s:if test="retreatQueryParam.pageSize==20">selected="selected"</s:if>>20</option>
								<option value="50"
									<s:if test="retreatQueryParam.pageSize==50">selected="selected"</s:if>>50</option>
						</select>条记录&nbsp;&nbsp;&nbsp;&nbsp;当前显示<s:if test="retreatQueryParam.total==0">0</s:if>
								<s:else>
									<s:property value="retreatQueryParam.firstResult+1" />
								</s:else>到<s:property value="retreatQueryParam.lastResult" />条，共<s:property
									value="retreatQueryParam.total" />条</td>
							<td colspan="10" align="right"><s:if
									test="retreatQueryParam.curPage==1">
										<a>首页</a>&nbsp;&nbsp;<a>上一页</a>
									</s:if> <s:else>
									<a href="#" onclick="viewRetreathListByPage('1');">首页</a>&nbsp;&nbsp;<a
										href="#"
										onclick="viewRetreathListByPage('${retreatQueryParam.curPage - 1}');return false;">上一页</a>
								</s:else> <s:if
									test="retreatQueryParam.curPage==retreatQueryParam.totalPage">
										<a>下一页</a>&nbsp;&nbsp;<a>尾页</a>
									</s:if> <s:else>
									<a href="#"
										onclick="viewRetreathListByPage('${retreatQueryParam.curPage + 1}');return false;">下一页</a>&nbsp;&nbsp;<a
										href="#"
										onclick="viewRetreathListByPage('${retreatQueryParam.totalPage}');return false;">尾页</a>
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
