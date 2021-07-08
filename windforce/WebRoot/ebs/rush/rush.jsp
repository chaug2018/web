<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/common/js/jquery/jquery-1.7.1.js"></script>
<script language="javascript"
	src="${pageContext.request.contextPath}/ebs/common/js/calendar.js"></script>
<script language="javascript"
	src="${pageContext.request.contextPath}/ebs/common/js/enterToTab.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/ebs/common/js/orgTree.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/ebs/common/js/rush.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/ebs/common/js/process.js"></script>
<link
	href="${pageContext.request.contextPath}/ebs/common/css/process.css"
	rel="stylesheet" type="text/css" />
<title>账单催收</title>
<meta http-equiv="Content-Type" content="text/html" />

<link href="${pageContext.request.contextPath}/ebs/common/css/css.css"
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
	function queryRushList() {
	if(IsDate(document.getElementById("docDate")))
	   {
		viewRushListByPage("1");
		}
	}
    function CheckAll(form) {
		for ( var i = 0; i < form.elements.length; i++) {
			var e = form.elements[i];
			if (e.Name != 'chkAll' && e.disabled == false)
				e.checked = form.chkAll.checked;
		}
	}
	function viewRushListByPage(pageNumber) {
	if(document.getElementById("docDate").value.length == 0){
			alert("对账日期不能为空！");
			document.getElementById("docDate").focus();
		}else {
			$("#curPage").val(pageNumber);
			$("#rushQueryForm").submit();
		}
	}

	function toExportData() {
		var objTable = document.getElementById("rushListInfo");	
		if (objTable.rows.length>2) {
		window.location.href = 'rushAction_exportData.action';
		}else{
			alert("查询结果列表无数据");
		}
	}
	$(document).ready(function(){
	var idCenter="<s:property value='idCenter'/>";
	var idBank="<s:property value='idBank'/>";
	stopProcess();
	initTree(<%=request.getAttribute("orgTree")%>,idCenter,idBank);
	});
</script>

</head>
<body class="baby_in2">
<input id="moudleName" type="hidden" value="催收处理"/>
<%@ include file="/ebs/taskviewtool/processing_div.jsp"%>
	<script type="text/javascript">startProcess("正在初始化界面。。。");</script>
	<div id="son_div" class="son_div"   style="background:url('/windforce/ebs/common/images/bg_content.jpg')">
		<div class="sonpage" id="sonpage">
			<%@ include file="/ebs/rush/rush_div.jsp"%>
		</div>
	</div>
	<div id="son2_div" class="son2_div" style="background:url('/windforce/ebs/common/images/bg2.jpg')">
		<div class="sonpage2" id="sonpage2">
			<%@ include file="/ebs/rush/batchProcess_div.jsp"%>
		</div>
	</div>
	<form id="rushQueryForm"
		action="rushAction_queryRushData.action"
		name="rushQuery" method="post">
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
							name="rushQueryParam.idBank1" id="idBank"  maxlength="10"
							class="diinput_text01"  type="text"  style="width:160px" 
							value='<s:property value="rushQueryParam.idBank1"/>' />
						<input type="hidden" id="curPage" name="rushQueryParam.curPage"
								value='<s:property value="netQueryParam.curPage"/>' />
						</td>
						<td width="30%"><div align="left">
								催收方式&nbsp;&nbsp;
								<s:select
									list="refRushMethodMap" class="input_text01" listKey="key"
									listValue="value" headerKey="" headerValue="--请选择--"
									style="color:#333333; font-size:13px; width:160px;"
									name="rushQueryParam.rushMethod" id="rushMethod"
									value="rushQueryParam.rushMethod">
								</s:select>
							</div></td>
						<td width="10%" rowspan="2" align="center"><input
							name="queryData" type="button" class="submit_but09"
							id="queryData" value="查询" onclick="queryRushList()"
							 />
						</td>
					</tr>
					<tr>
						<td width="30%">分 行&nbsp;&nbsp;&nbsp; 
								<select
								name="rushQueryParam.idCenter" id="selectIdCenter"  style="width:160px" 
								class="input_text01" onchange="changeIdCenter()">
								</select>
						</td>
						<td width="30%"><div align="left">
								账单编号&nbsp;&nbsp; <input name="rushQueryParam.voucherNo"
									type="text" class="diinput_text01" id="voucherNo"
									 maxlength="32"  style="width:160px" 
									value='<s:property value="rushQueryParam.voucherNo"/>' />
							</div></td>
						<td width="30%">
							<div align="left">
								处理标志&nbsp;&nbsp;
								<s:select
									list="refRushFlagMap" class="input_text01" listKey="key"
									listValue="value"
									style="color:#333333; font-size:13px; width:160px;"
									name="rushQueryParam.rushFlag" id="rushFlag"
									value="rushQueryParam.rushFlag">
								</s:select>
							</div>
						</td>
						<td width="10%"></td>
					</tr>
					<tr>
						<td width="30%">网 点&nbsp;&nbsp;&nbsp; <select
							name="rushQueryParam.idBank" id="selectIdBank"
							class="input_text01"  onchange="changeIdBank()"  style="width:160px" >
						</select>
						</td>
						<td width="30%">对账日期&nbsp;&nbsp; <input
							onclick="new Calendar().show(this);"  maxlength="10"
							name="rushQueryParam.docDate" type="text" title="对账日期"
							class="diinput_text01" id="docDate" 
							value='<s:property value="rushQueryParam.docDate"/>'   style="width:160px" />
						</td>
						<td width="30%">发送渠道&nbsp;&nbsp; <s:select
								list="refSendMode" onkeydown="cgo(this.form,this.name,'')"
								class="input_text01" listKey="key" listValue="value"
								 headerKey="" headerValue="全部"
								style="color:#333333; font-size:13px; width:160px;"
								name="rushQueryParam.sendModeFlag"
								value="rushQueryParam.sendModeFlag" id="urgeFlag">
							</s:select></td>
						<td width="10%" rowspan="2" align="center"><input
							name="btnexport" type="button" class="submit_but09"
							id="btnexport" value="导出" onclick="toExportData()"   />
						</td>
						<td width="10%"></td>
					</tr>
				</table>

			</div>
			<div
				style="width:100%; height:auto; float:left; margin-left:10px; padding:10px 0px 10px 0px;">
			<table width="98%"><tr>
			<td width="90%"><h1>催收信息列表</h1></td>
			<td width="10%"><input type="button" value="批量处理" onclick="batchProcess()"
				class="submit_but09" /></td>
			</tr></table>
				<table width="100%" border="0" cellpadding="0" cellspacing="0"
					id="rushListInfo">
					<thead>
						<tr>
						<td width="3%" height="26" align="center" bgcolor="#c76c6f"
							class="font_colors01"><input name="selectall"
							type="checkbox" id="chkAll" onclick="CheckAll(this.form)"
							value="checkbox"></input>
						</td>
							<td width="5%" height="26" align="center" bgcolor="#c76c6f"
								class="font_colors01">序号</td>
							<td width="5%" align="center" bgcolor="#c76c6f"
								class="font_colors01">机构号</td>
							<td width="10%" align="center" bgcolor="#c76c6f"
								class="font_colors01">账单编号</td>
							<td width="15%" align="center" bgcolor="#c76c6f"
								class="font_colors01">账户名称</td>
							<td width="5%" align="center" bgcolor="#c76c6f"
								class="font_colors01">账单状态</td>
							<td width="6%" align="center" bgcolor="#c76c6f"
								class="font_colors01">催收日期</td>
							<td width="7%" align="center" bgcolor="#c76c6f"
								class="font_colors01">催收柜员</td>
							<td width="5%" align="center" bgcolor="#c76c6f"
								class="font_colors01">客户回应</td>
							<td width="7%" align="center" bgcolor="#c76c6f"
								class="font_colors01">催收方式</td>
							<td width="6%" align="center" bgcolor="#c76c6f"
								class="font_colors01">处理意见</td>
							<td width="9%" align="center" bgcolor="#c76c6f"
								class="font_colors01">备注</td>
							<td width="6%" align="center" bgcolor="#c76c6f"
								class="font_colors01">处理标志</td>
								<td width="6%" align="center" bgcolor="#c76c6f"
								class="font_colors01">发送渠道</td>
							<td width="7%" align="center" bgcolor="#c76c6f"
								class="font_colors01">操作</td>
						</tr>
					</thead>


					<tbody id="queryList" align="center">
						<s:iterator value="queryList" var="queryData" status="st">
							<tr bgcolor='<s:if test="#st.count%2==0">#F4DDDF</s:if>'>
							<td><input type="checkbox" name="selectId"
									value='<s:property value="#st.count"/>' /></td>
								<td align="center"><s:property
										value="#st.count+rushQueryParam.firstResult" /></td>
								<td align="center"><s:property value="#queryData.idCenter" /></td>
								<td align="center"><s:property value="#queryData.voucherNo" /></td>
								<td align="center"><s:property value="#queryData.accName" /></td>
								<td align="center"><s:property
										value="refDocStateMap.get(#queryData.docState)" /></td>
								<td align="center"><s:property value="#queryData.rushDate" /></td>
								<td align="center"><s:property value="#queryData.rushOperCode" /></td>
								<td align="center"><s:property
										value="refCustomResponseMap.get(#queryData.customResponse)" /></td>
								
								<td align="center"><s:iterator value="refRushMethodMap.keySet()" id="id">
										<s:if test="#queryData.rushMethod==#id">
											<s:property value="refRushMethodMap.get(#id)" />
										</s:if>
									</s:iterator>
								</td>
								<td align="center"><s:iterator value="refRushResultMap.keySet()" id="id">
										<s:if test="#queryData.rushResult==#id">
											<s:property value="refRushResultMap.get(#id)" />
										</s:if>
									</s:iterator>
								</td>
								<td align="center"><s:property value="#queryData.rushDesc" /></td>
								<td align="center"><s:iterator value="refRushFlagMap.keySet()" id="id">
										<s:if test="#queryData.rushFlag==#id">
											<s:property value="refRushFlagMap.get(#id)" />
										</s:if>
									</s:iterator>
								</td>
								<td><s:property value="refSendMode.get(#queryData.sendMode)" /></td>		
								<td align="center"><a onclick="modifyAcc(this)" style="color:#BE333A"  id="mybtn">
								处理</a>
								</td>
							</tr>
						</s:iterator>
					</tbody>
					<tfoot>
						<tr class="pagelinks">
							<td colspan="6">每页显示 
								<select name="rushQueryParam.pageSize" id="pageSize"
									style="width:40px;" onchange="viewRushListByPage('1')">
									<option value="10"
										<s:if test="rushQueryParam.pageSize==10">selected="selected"</s:if>>10</option>
									<option value="20"
										<s:if test="rushQueryParam.pageSize==20">selected="selected"</s:if>>20</option>
									<option value="50"
										<s:if test="rushQueryParam.pageSize==50">selected="selected"</s:if>>50</option>
								</select>条记录&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;当前显示<s:if test="rushQueryParam.total==0">0</s:if>
								<s:else>
									<s:property value="rushQueryParam.firstResult+1" />
								</s:else>到<s:property value="rushQueryParam.lastResult" />条，共<s:property
									value="rushQueryParam.total" />条</td>
							<td colspan="9" align="right"><s:if
									test="rushQueryParam.curPage==1">
										<a>首页</a>&nbsp;&nbsp;<a>上一页</a>
									</s:if> <s:else>
									<a href="#" onclick="viewRushListByPage('1');">首页</a>&nbsp;&nbsp;<a
										href="#"
										onclick="viewRushListByPage('${rushQueryParam.curPage - 1}');return false;">上一页</a>
								</s:else> <s:if
									test="rushQueryParam.curPage==rushQueryParam.totalPage">
										<a>下一页</a>&nbsp;&nbsp;<a>尾页</a>
									</s:if> <s:else>
									<a href="#"
										onclick="viewRushListByPage('${rushQueryParam.curPage + 1}');return false;">下一页</a>&nbsp;&nbsp;<a
										href="#"
										onclick="viewRushListByPage('${rushQueryParam.totalPage}');return false;">尾页</a>
								</s:else></td>
						</tr>
					</tfoot>
				</table>
			</div>
		</div>
	</form>
</body>
</html>
