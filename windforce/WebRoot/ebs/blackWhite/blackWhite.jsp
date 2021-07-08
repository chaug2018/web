<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>特殊账户维护</title>
<meta http-equiv="Content-Type" content="text/html" />
<link href="<%=path%>/ebs/common/css/process.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/css.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/dicss.css" rel="stylesheet" type="text/css" />
<style>
#son_div {
	position: absolute;
	width: 700px;
	height: 250px;
	margin-left: -50px;
	margin-top: -25px;
	background:url('/windforce/ebs/common/images/bg_content.jpg');
}
</style>
<script type="text/javascript" src="<%=path%>/ebs/common/js/jquery.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/orgTree.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/blackwhite.js"></script>
<script language="javascript" src="<%=path%>/ebs/common/js/enterToTab.js"></script>
<script language="javascript" src="<%=path%>/ebs/common/js/calendar.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/process.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/jquery.form.js"></script>
<script type="text/javascript">
	var err = "<s:property value='errMsg'/>";
	if (err != null && err.length > 0) {
		alert("初始化服务出现错误");
		top.refresh();
	}
</script>
<script type="text/javascript">
// ajax form上传文件
function uploadFile(){

	$("#blackWhiteFormFile").ajaxSubmit({  
	                    	type: "post",
		                    url: "blackWhiteAction_excelBatchInput.action" , 
		                    success: function(data){  
		                       alert(data);
		                       $( "#blackWhiteFormFile").resetForm();  
		                    }
		       });  
	 }

//删除
function deleteInfor(data){
	if(confirm("确认删除此信息！")){
	var cell=data.parentNode;
	var row=cell.parentNode;
	var i=row.cells[1].innerHTML;
    $.ajax({
            type: "POST",
            url: "blackWhiteAction_deleteInfor.action",
            data: "postData="+i,
            success: function(result) {
                    if(result == "deleteSuccess"){
                    }else{
                    	alert("删除失败！请稍后再试");
                    }
            }
      });
      deleteRefrsh("${bwQueryParam.curPage}");
	}
}
//删除成功后刷新信息
	function deleteRefrsh(pageNumber) {
		alert("删除成功！");
		var obj = document.getElementById("select_pageSize");
		var index = obj.selectedIndex; // 选中索引
		var pageSize = obj.options[index].text; // 选中文本
		if (IsDate(document.getElementById("signTime"))) {
			$("#pageSize").val(pageSize);
			$("#curPage").val(pageNumber);
			$("#blackWhiteForm").submit();
		} else {
			document.getElementById("signTime").focus();
		}
	}

	function queryBlackWhite() {
		viewBlackWhiteListByPage('${bwQueryParam.curPage}');
	}

	function viewBlackWhiteListByPage(pageNumber) {
		var obj = document.getElementById("select_pageSize");
		var index = obj.selectedIndex; // 选中索引
		var pageSize = obj.options[index].text; // 选中文本
		if (IsDate(document.getElementById("signTime"))) {
			$("#pageSize").val(pageSize);
			$("#curPage").val(pageNumber);
			$("#blackWhiteForm").submit();
		} else {
			document.getElementById("signTime").focus();
		}
	}

	function toExportData() {
		var objTable = document.getElementById("basicInfoTable");
		if (objTable.rows.length > 2) {
			window.location.href = 'blackWhiteAction_exportData.action';
		} else {
			alert("查询结果列表无数据");
		}
	}
	
	$(document).ready(
		function() {
			var idCenter = "<s:property value='idCenter'/>";
			var idBank = "<s:property value='idBank'/>";
			initTree(<%=request.getAttribute("orgTree")%>, idCenter, idBank);
		});
</script>
</head>

<body class="baby_in2">
	<input id="moudleName" type="hidden" value="特殊账户维护" />
	<div id="son_div" class="son_div">
		<div class="sonpage" id="sonpage">
			<center>
				<tr>
					<td style="vertical-align: top;" colspan="2">特殊账户维护</td>
				</tr>
			</center>
		</div>
		<%@ include file="/ebs/blackWhite/bwupdate_div.jsp"%>
	</div>
	<div class="nov_moon">
		<div style="width:98%; height:auto; float:left; margin-left:10px; padding:10px 0px 10px 0px;"
			class="border_bottom01">
			<form id="blackWhiteForm" action="blackWhiteAction_getQueryData.action" name="blackWhiteQurry"
				method="post">
				<table width="100%">
					<tr>
						<td width="20%">总 行&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;
								<select>
									<option value=""> 华融湘江总行</option>
								</select>
						</td>
						<td width="20%">维&nbsp;护&nbsp;时&nbsp;间&nbsp;&nbsp; 
							<input name="bwQueryParam.signTime" type="text"
								class="diinput_text01" title='维护时间' id="signTime"
								onfocus="new Calendar().show(this);" maxlength="12"
								value='<s:property value="bwQueryParam.signTime"/>' /> 
							<input type="hidden" id="curPage" name="bwQueryParam.curPage"
								value='<s:property value="bwQueryParam.curPage"/>' /> 
							<input type="hidden" id="pageSize" name="bwQueryParam.pageSize"
								value='<s:property value="bwQueryParam.pageSize"/>' />
						</td>
						<td width="20%">账&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号&nbsp;&nbsp;
								<input name="bwQueryParam.accNo" type="text"
									class="diinput_text01" maxlength="32" id="accNo"
									value='<s:property value="bwQueryParam.accNo"/>' />
						</td>
						<td width="10%" align="center">
							<input name="queryData" type="button" class="submit_but09"
								id="queryData" value="查询" onclick="viewBlackWhiteListByPage('1')" />
						</td>
					</tr>
					<tr>
						<td width="20%">分 行&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;
								<select name="bwQueryParam.idCenter" id="selectIdCenter"
									onchange="changeIdCenter()">
								</select>
						</td>
						<td width="20%">机&nbsp;&nbsp;&nbsp;构&nbsp;&nbsp;&nbsp;号&nbsp;&nbsp;
							<input name="bwQueryParam.idBank" id="idBank"
								class="diinput_text01" maxlength="12"
								value='<s:property value="bwQueryParam.idBank"/>' />
						</td>
						<td width="20%">账&nbsp;户&nbsp;名&nbsp;称&nbsp;&nbsp; 
							<input name="bwQueryParam.accName" id="accName" class="diinput_text01"
								maxlength="128" value='<s:property value="bwQueryParam.accName"/>' />
						</td>
					</tr>
					<tr>
						<td width="20%">网 点&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; 
							<select id="selectIdBank" onchange="changeIdBank()">
							</select>
						</td>
						<td width="20%">客&nbsp;&nbsp;&nbsp;户&nbsp;&nbsp;&nbsp;号&nbsp;&nbsp;
							<input name="bwQueryParam.custId" type="text"
								class="diinput_text01" maxlength="32" id="custId"
								value='<s:property value="bwQueryParam.custId"/>' />
						</td>
						<td width="20%">账&nbsp;户&nbsp;类&nbsp;型&nbsp;&nbsp; 
							<s:select list="refAccCycleMap" listKey="key"
								listValue="value" headerKey="" headerValue="--请选择--"
								name="bwQueryParam.accCycle" id="accCycle"
								value="bwQueryParam.accCycle">
							</s:select>
						</td>
						<td width="10%" align="center">
							<input name="export" type="button" class="submit_but09" id="export"
							value="导出" onclick="toExportData()" />
						</td>
					</tr>
					<tr>
						<td width="20%">是否对账&nbsp;&nbsp;
							<s:select list="refIsCheck" listKey="key" listValue="value"  
								headerKey="" headerValue="--请选择--" id="isCheck_div" name="bwQueryParam.ischeck">
							</s:select>
						</td>
					</tr>
				</table>
			</form>
		</div>
		<div style="width:100%; height:auto; float:left; margin-left:10px; padding:10px 0px 10px 0px;">
				<form id="blackWhiteFormFile"  name="blackWhiteFormFile" method="post" enctype="multipart/form-data">
				<table width="98%">
					<tr>
						<td width="80%">
							<h1>特殊账户信息列表</h1>
						</td>
						<td>
							<s:file name="upFile" label="FileName:" />
						</td>
						<td width="10%">
							<input type="button" value="上传" class="submit_but09"  onClick="uploadFile()" />
						</td>
						<td width="10%"><a href="blackWhiteAction_downloadTemplete.action"><font
								size="2" color="#7a7c88" >模板下载</font>
						</td>
					</tr>
				</table>
			</form>
			<form id="batchUpdateForm"
				action="blackWhiteAction_batchModify.action" name="updateBwList"
				method="post">
				<table width="98%" style="border: 1px solid #c9eaf5;"
					cellpadding="0" cellspacing="0" id="basicInfoTable">
					<tr>
						<td width="4%" align="center" bgcolor="#c76c6f" height="26"
							class="font_colors01">序号</td>
						<td width="12%" align="center" bgcolor="#c76c6f"
							class="font_colors01">账号</td>
						<td width="5%" align="center" bgcolor="#c76c6f"
							class="font_colors01">机构号</td>
						<td width="12%" align="center" bgcolor="#c76c6f"
							class="font_colors01">账户名称</td>
						<td width="10%" align="center" bgcolor="#c76c6f"
							class="font_colors01">邮寄地址</td>
						<td width="7%" align="center" bgcolor="#c76c6f"
							class="font_colors01">邮编</td>
						<td width="7%" align="center" bgcolor="#c76c6f"
							class="font_colors01">联系电话</td>
						<td width="6%" align="center" bgcolor="#c76c6f"
							class="font_colors01">联系人</td>
						<td width="7%" align="center" bgcolor="#c76c6f"
							class="font_colors01">维护时间</td>
						<td width="8%" align="center" bgcolor="#c76c6f"
							class="font_colors01">客户号</td>
						<td width="7%" align="center" bgcolor="#c76c6f"
							class="font_colors01">是否对账</td>
						<td width="7%" align="center" bgcolor="#c76c6f"
							class="font_colors01">账户类型</td>
						<td width="7%" align="center" bgcolor="#c76c6f"
							class="font_colors01">操作</td>
					</tr>
					<tbody id="queryList" align="center">
						<s:iterator value="queryList" var="queryData" status="st">
							<tr bgcolor='<s:if test="#st.count%2==0">#F4DDDF</s:if>'>
								<td style="border:1px solid #C76C6F"><s:property
										value="#st.count+bwQueryParam.firstResult" /></td>
								<td style="border:1px solid #C76C6F"><s:property
										value="#queryData.accNo" />
								</td>
								<td style="border:1px solid #C76C6F"><s:property
										value="#queryData.idBank" />
								</td>
								<td style="border:1px solid #C76C6F"><s:property
										value="#queryData.accName" />
								</td>
								<td style="border:1px solid #C76C6F"><s:property
										value="#queryData.sendAddress" />
								</td>
								<td style="border:1px solid #C76C6F"><s:property
										value="#queryData.zip" />
								</td>
								<td style="border:1px solid #C76C6F"><s:property
										value="#queryData.phone" />
								</td>
								<td style="border:1px solid #C76C6F"><s:property
										value="#queryData.linkMan" />
								</td>
								<td style="border:1px solid #C76C6F"><s:property
										value="#queryData.signTime" />
								</td>
								<td style="border:1px solid #C76C6F"><s:property
										value="#queryData.custId" />
								</td> 
								<td style="border:1px solid #C76C6F">
									<s:property value="refIsCheck.get(#queryData.isCheck)" />
								</td>
								<td style="border:1px solid #C76C6F">
									<s:iterator value="refAccCycleMap.keySet()" id="id">
										<s:if test="#queryData.accCycle==#id">
											<s:property value="refAccCycleMap.get(#id)" />
										</s:if>
									</s:iterator>
								</td>
								<td style="border:1px solid #C76C6F">
									<a onclick="modifyAcc(this)" style="color:#BE333A">编辑</a>&nbsp;&nbsp;
									<a onclick="deleteInfor(this)" style="color:#BE333A">删除</a>
								</td>
							</tr>
						</s:iterator>
					</tbody>
					<tfoot id="blackWhiteListPageInfo">
						<tr class="pagelinks">
							<td colspan="7">每页显示 <select id="select_pageSize"
								style="width:40px;" onchange="viewBlackWhiteListByPage('1')">
									<option value="10"
										<s:if test="bwQueryParam.pageSize==10">selected="selected"</s:if>>10</option>
									<option value="20"
										<s:if test="bwQueryParam.pageSize==20">selected="selected"</s:if>>20</option>
									<option value="50"
										<s:if test="bwQueryParam.pageSize==50">selected="selected"</s:if>>50</option>
							</select>条记录&nbsp;&nbsp;&nbsp;&nbsp;当前显示<s:if test="bwQueryParam.total==0">0</s:if>
								<s:else>
									<s:property value="bwQueryParam.firstResult+1" />
								</s:else>到<s:property value="bwQueryParam.lastResult" />条，共<s:property
									value="bwQueryParam.total" />条</td>
							<td colspan="12" align="right"><s:if
									test="bwQueryParam.curPage==1">
									<a>首页</a>&nbsp;&nbsp;<a>上一页</a>
								</s:if> <s:else>
									<a href="#" onclick="viewBlackWhiteListByPage('1');">首页</a>&nbsp;&nbsp;<a
										href="#"
										onclick="viewBlackWhiteListByPage('${bwQueryParam.curPage - 1}');return false;">上一页</a>
								</s:else> <s:if test="bwQueryParam.curPage==bwQueryParam.totalPage">
									<a>下一页</a>&nbsp;&nbsp;<a>尾页</a>
								</s:if> <s:else>
									<a href="#"
										onclick="viewBlackWhiteListByPage('${bwQueryParam.curPage + 1}');return false;">下一页</a>&nbsp;&nbsp;<a
										href="#"
										onclick="viewBlackWhiteListByPage('${bwQueryParam.totalPage}');return false;">尾页</a>
								</s:else>
							</td>
						</tr>
					</tfoot>
				</table>

			</form>

		</div>
	</div>


</body>
</html>
