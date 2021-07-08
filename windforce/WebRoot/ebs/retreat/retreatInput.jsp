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
	src="${pageContext.request.contextPath}/ebs/common/js/tab.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/ebs/common/js/process.js"></script>
<link
	href="${pageContext.request.contextPath}/ebs/common/css/process.css"
	rel="stylesheet" type="text/css" />
<title>退信登记</title>
<meta http-equiv="Content-Type" content="text/html" />
<link
	href="${pageContext.request.contextPath}/ebs/common/css/css.css"
	rel="stylesheet" type="text/css" />
<link
	href="${pageContext.request.contextPath}/ebs/common/css/dicss.css"
	rel="stylesheet" type="text/css" />
<style type="text/css">
</style>
<script type="text/javascript">
	function retreatInput() {
		if (document.getElementById("voucherNo").value.length == 0) {
			alert("对账单编号不能为空！");
			document.getElementById("voucherNo").focus();
		} else if (document.getElementById("urgeType").value.length == 0) {
			alert("请选择退信原因！");
			document.getElementById("urgeType").focus();
		} else {
		    var param = "voucherNo=";
			param = param + document.getElementById("voucherNo").value;
			param = param + "&urgeNote="
					+ document.getElementById("urgeNote").value;
			param = param + "&urgeType="
					+ document.getElementById("urgeType").value;

			startProcess("正在登记...");
			
			$.post("retreatInputAction_singleInput.action", param, function(
					result) {
				dealResult_modify(result);
			});

		}
	}
	function dealResult_modify(result) {
		document.getElementById("voucherNo").value = "";
		document.getElementById("urgeNote").value = "";
		document.getElementById("urgeType").value = "";
		stopProcess();
		alert(result);
	}
	//上传文件	
	function fileupload() {
		var filepath = "";
		filepath = document.getElementById("File_New").value;//获得文件路径
		if (filepath.length != 0) {
			startProcess("正在登记...");
			sendFileToServer(filepath);
			stopProcess();
		} else {
			alert("请选择要上传的文件！");
		}
	}
	$(document).ready(
			function() {			
				stopProcess();
			});
</script>

</head>
<body class="baby_in2" >
<input id="moudleName" type="hidden" value="退信登记"/>
	<%@ include file="/ebs/taskviewtool/processing_div.jsp"%>
	<script type="text/javascript">startProcess("正在初始化界面。。。");</script>
	
	<div id="tabs0" style="height: 150px">
		<ul class="menu0" id="menu0">
			<li onclick="setTab(0,0)" class="hover"><font size="3px" color="#E12E2E"><b>批量登记</b>
			</font>
			</li>
			<li onclick="setTab(0,1)"><font size="3px" color="#E12E2E"><b>逐笔登记</b>
			</font>
			</li>
			
		</ul>
		<div class="main" id="main0">
			<ul class="block">
				<li><br /> <br /> <br /> <s:form
						action="retreatInputAction_batchInport.action" method="POST"
						enctype="multipart/form-data" >
						&nbsp;&nbsp;
						<s:file name="upFile" label="FileName:" />
						<s:submit value="批量导入" align="left" />
						&nbsp;&nbsp;&nbsp;&nbsp;<a
							href="retreatInputAction_downloadTemplete.action"><font
							size="2" color="#7a7c88">模板下载</font>
						</a>
					 <br /> <br /> &nbsp;&nbsp;<font color="#c76c6f"><s:property
							value="result.msg" />
				</font>
				</s:form>
				</li>
			</ul>
			<ul>
				<li><br /> <br /> <br />
					<table width="100%">
						<tr>
							<td width="27%">&nbsp;&nbsp;退信原因&nbsp;&nbsp;<s:select
									list="refUrgeTypeMap" listKey="key" listValue="value"
									headerKey="" headerValue="--请选择--"
									name="retreatQueryParam.urgeType" style="color:#333333; font-size:13px; width:125px;"
									value="retreatQueryParam.urgeType" id="urgeType" onkeydown="cgo(this.form,this.name,'')">
								</s:select></td>
							<td width="27%">对账单编号：<input type="text"  maxlength="32"
								name="retreatQueryParam.voucherNo" class="diinput_text01"
								id="voucherNo" size="20" onkeydown="cgo(this.form,this.name,'')"></input></td>
							<td width="22%">备注：<input type="text"  maxlength="56"
								class="diinput_text01" name="retreatQueryParam.urgeNote"
								id="urgeNote" onkeydown="cgo(this.form,this.name,'')"></input>
							</td>
							<td width="11%"><input name="input" type="button"
								style="width:100px; height:24px; background: url(${pageContext.request.contextPath}/ebs/common/images/iocn_10.png) no-repeat; border:0px;"
								id="find" value="确认登记" onclick="retreatInput();" /></td>
						</tr>
					</table>
				</li>
			</ul>
			<ul>
				<li></li>
			</ul>
			<ul>
				<li></li>
			</ul>
		</div>
	</div>
	<div
		style="width:1002px;height:400px;overflow-y: scroll; float:left; margin-left:10px; padding:10px 0px 10px 0px;">
		<h2>
			成功导入条数：
			<s:property value="result.successCount" />
			条&nbsp;&nbsp;&nbsp;导入失败条数：
			<s:property value="result.failCount" />
			条
		</h2>
		<h1>导入失败信息列表</h1>
		<table width="900" border="0" cellpadding="0" cellspacing="0"
			id="retreatListInfo">
			<thead>
				<tr>
					<td width="100" height="26" align="center" bgcolor="#c76c6f"
						class="font_colors01">序号</td>
					<td width="300" align="center" bgcolor="#c76c6f"
						class="font_colors01">账单编号</td>
					<td width="500" align="center" bgcolor="#c76c6f"
						class="font_colors01">失败原因</td>
				</tr>
			</thead>
			<tbody id="result" align="center">
				<s:iterator value="result.failList" var="failData" status="st">
					<tr
						bgcolor='<s:if test="#st.count%2==0">#F4DDDF</s:if><s:else>#EBEBE6</s:else>'>
						<td class="font_colors06"><s:property value="#st.count" />
						</td>
						<td class="font_colors06"><s:property
								value="#failData.voucherNo" />
						</td>
						<td class="font_colors06"><s:property
								value="#failData.failReason" />
						</td>
					</tr>
				</s:iterator>
			</tbody>
		</table>
	</div>
</body>
</html>
