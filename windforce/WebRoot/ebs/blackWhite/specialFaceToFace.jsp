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
<title>特殊面对面账户维护</title>
<meta http-equiv="Content-Type" content="text/html" />
<link href="<%=path%>/ebs/common/css/process.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/css.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/dicss.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="<%=path%>/ebs/common/js/jquery.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/orgTree.js"></script>
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

	$("#specialFaceToFaceFormFile").ajaxSubmit({  
	                    	type: "post",
		                    url: "specialFaceToFaceAction_excelBatchInput.action" , 
		                    success: function(data){  
		                       alert(data);
		                       $( "#specialFaceToFaceFormFile").resetForm();  
		                    }
		       });  
	 }

//删除
function deleteInfo(data){
	if(confirm("确认删除此信息！")){
	var cell=data.parentNode;
	var row=cell.parentNode;
	var accno=row.cells[1].innerHTML;
	var docdate=row.cells[2].innerHTML;
    $.ajax({
            type: "POST",
            url: "specialFaceToFaceAction_deleteInfo.action",
            data: "accno="+accno+"&docdate="+docdate,
            success: function(result) {
                    if(result == "deleteSuccess"){
                    	alert("删除成功！");
                    	viewListByPage("${queryParam.curPage}");
                    }else{
                    	alert("删除失败！请稍后再试");
                    }
            }
      });
      
	}
}

function viewListByPage(pageNumber) {
	var obj = document.getElementById("select_pageSize");
	var index = obj.selectedIndex; // 选中索引
	var pageSize = obj.options[index].text; // 选中文本
	
	$("#pageSize").val(pageSize);
	$("#curPage").val(pageNumber);
	$("#specialFaceToFaceForm").submit();
	
}

function toExportData() {
	var objTable = document.getElementById("specialFaceToFaceTable");
	if (objTable.rows.length > 2) {
		window.location.href = "specialFaceToFaceAction_exportData.action";
	} else {
		alert("查询结果列表无数据");
	}
}
	
</script>
</head>

<body class="baby_in2">
	<input id="moudleName" type="hidden" value="特殊面对面账户维护" />
	
	<div class="nov_moon">
		<div style="width:98%; height:auto; float:left; margin-left:10px; padding:10px 0px 10px 0px;"
			class="border_bottom01">
			<form id="specialFaceToFaceForm" action="specialFaceToFaceAction_getQueryData.action" name="getQueryData"
				method="post">
				<table width="100%">
					<tr>
						<td width="20%">对&nbsp;账&nbsp;时&nbsp;间&nbsp;&nbsp; 
							<input name="queryParam.docdate" type="text"
								class="diinput_text01" title='维护时间' id="docdate"
								onfocus="new Calendar().show(this);" maxlength="12"
								value='<s:property value="queryParam.docdate"/>' /> 
							<input type="hidden" id="curPage" name="queryParam.curPage"
								value='<s:property value="queryParam.curPage"/>' /> 
							<input type="hidden" id="pageSize" name="queryParam.pageSize"
								value='<s:property value="queryParam.pageSize"/>' />
						</td>
						<td width="20%">账&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号&nbsp;&nbsp;
								<input name="queryParam.accno" type="text"
									class="diinput_text01" maxlength="32" id="accno"
									value='<s:property value="queryParam.accno"/>' />
						</td>
						<td width="10%" align="center">
							<input name="queryData" type="button" class="submit_but09"
								id="queryData" value="查询" onclick="viewListByPage('1')" />
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
			<form id="specialFaceToFaceFormFile"  name="specialFaceToFaceFormFile" method="post" enctype="multipart/form-data">
				<table width="98%">
					<tr>
						<td width="80%">
							<h1>特殊面对面账户信息列表</h1>
						</td>
						<td>
							<s:file name="upFile" label="FileName:" />
						</td>
						<td width="10%">
							<input type="button" value="上传" class="submit_but09"  onClick="uploadFile()" />
						</td>
						<td width="10%"><a href="specialFaceToFaceAction_downloadTemplete.action"><font
								size="2" color="#7a7c88" >模板下载</font>
						</td>
					</tr>
				</table>
			</form>
			<div>
				<table width="98%" style="border: 1px solid #c9eaf5;"
					cellpadding="0" cellspacing="0" id="specialFaceToFaceTable">
					<tr>
						<td width="8%" align="center" bgcolor="#c76c6f" height="26"
							class="font_colors01">序号</td>
						<td width="30%" align="center" bgcolor="#c76c6f"
							class="font_colors01">账号</td>
						<td width="30%" align="center" bgcolor="#c76c6f"
							class="font_colors01">对账时间</td>
						<td width="30%" align="center" bgcolor="#c76c6f"
							class="font_colors01">操作</td>
					</tr>
					<tbody id="queryList" align="center">
						<s:iterator value="queryList" var="queryData" status="st">
							<tr bgcolor='<s:if test="#st.count%2==0">#F4DDDF</s:if>'>
								<td style="border:1px solid #C76C6F"><s:property value="#st.count+queryParam.firstResult"/></td>
								<td style="border:1px solid #C76C6F"><s:property value="#queryData.accno"/></td>
								<td style="border:1px solid #C76C6F"><s:property value="#queryData.docDate"/></td>
								<td style="border:1px solid #C76C6F">
									<a onclick="deleteInfo(this)" style="color:#BE333A">删除</a>
								</td>
							</tr>
						</s:iterator>
					</tbody>
					<tfoot id="specialFaceToFaceListPageInfo">
						<tr class="pagelinks">
							<td colspan="2">每页显示 <select id="select_pageSize"
								style="width:40px;" onchange="viewListByPage('1')">
									<option value="10"
										<s:if test="queryParam.pageSize==10">selected="selected"</s:if>>10</option>
									<option value="20"
										<s:if test="queryParam.pageSize==20">selected="selected"</s:if>>20</option>
									<option value="50"
										<s:if test="queryParam.pageSize==50">selected="selected"</s:if>>50</option>
							</select>条记录&nbsp;&nbsp;&nbsp;&nbsp;当前显示<s:if test="queryParam.total==0">0</s:if>
								<s:else>
									<s:property value="queryParam.firstResult+1" />
								</s:else>到<s:property value="queryParam.lastResult" />条，共<s:property
									value="queryParam.total" />条</td>
							<td colspan="2" align="right"><s:if
									test="queryParam.curPage==1">
									<a>首页</a>&nbsp;&nbsp;<a>上一页</a>
								</s:if> <s:else>
									<a href="#" onclick="viewListByPage('1');">首页</a>&nbsp;&nbsp;<a
										href="#"
										onclick="viewListByPage('${queryParam.curPage - 1}');return false;">上一页</a>
								</s:else> <s:if test="queryParam.curPage==queryParam.totalPage">
									<a>下一页</a>&nbsp;&nbsp;<a>尾页</a>
								</s:if> <s:else>
									<a href="#"
										onclick="viewListByPage('${queryParam.curPage + 1}');return false;">下一页</a>&nbsp;&nbsp;<a
										href="#"
										onclick="viewListByPage('${queryParam.totalPage}');return false;">尾页</a>
								</s:else>
							</td>
						</tr>
					</tfoot>
				</table>

			</div>

		</div>
	</div>


</body>
</html>
