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
<title>内部账户维护</title>
<meta http-equiv="Content-Type" content="text/html" />
<link href="<%=path%>/ebs/common/css/process.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/css.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/dicss.css" rel="stylesheet" type="text/css" />
<style>
#son_div {
	position: absolute;
	width: 400px;
	height: 200px;
	margin-left: 50px;
	margin-top: 30px;
	background:url('/windforce/ebs/common/images/bg_content.jpg');
}
</style>
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

//删除
function deleteInfo(data){
	var cell=data.parentNode;
	var row=cell.parentNode;
	var accno=row.cells[1].innerHTML;
	
	if(confirm("确认删除账号:"+accno+" 该条数据?")){
	    $.ajax({
	            type: "POST",
	            url: "innerAccnoAction_deleteInfo.action",
	            data: "accno="+accno,
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
	$("#innerAccnoForm").submit();
	
}

function toExportData() {
	var objTable = document.getElementById("innerAccnoTable");
	if (objTable.rows.length > 2) {
		window.location.href = "innerAccnoAction_exportData.action";
	} else {
		alert("查询结果列表无数据");
	}
}
	function addAccnoData(){
		showSon();
	}
	
	function closeDiv (){
		$("#accNo_div").val("");
		closeSon();
	}
	
	//内部账户 配置增加
	function inneraddsubmit(){
		var accNo_div = $("#accNo_div").val();
		
		var patrn =/^[0-9]{0,30}$/;
		if(!patrn.exec(accNo_div)){
			alert("账号格式不正确!");
		}else if(accNo_div==null||accNo_div==""){
			alert("账号不能为空!");
		}else{
			var postData = "accNo_div="+accNo_div+"&temp="+new Date();
			
			$.ajax({
                type: "POST",
                url: "innerAccnoAction_addAccno.action",
                data: postData,
                success: function(result) {
                        if(result == "1"){
                        	alert("新增成功!");
							viewListByPage("${queryParam.curPage}");
                        }else if(result == "2"){
                        	alert("该账号已存在!");
                        }else{
                        	alert("数据库出错,请稍后再试!");
                        }
              	}
        	});
		}
	}
	
</script>
</head>

<body class="baby_in2">
	<div id="son_div" class="son_div">
		<div class="sonpage" id="sonpage">
			<center>
				<tr>
					<td style="vertical-align: top;" colspan="2">新增内部账户</td>
				</tr>
			</center>
		</div>
		<%@ include file="/ebs/insideaccno/innerAccno_div.jsp"%>
	</div>
	
	<div class="nov_moon">
		<div style="width:98%; height:auto; float:left; margin-left:10px; padding:10px 0px 10px 0px;"
			class="border_bottom01">
			<form id="innerAccnoForm" action="innerAccnoAction_getQueryData.action" name="getQueryData"
				method="post">
				<table width="100%">
					<tr>
						<td width="20%" align="center">账&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号&nbsp;&nbsp;
								<input name="queryParam.accno" type="text"
									class="diinput_text01" maxlength="32" id="accno"
									value='<s:property value="queryParam.accno"/>' />
								<input type="hidden" id="curPage" name="queryParam.curPage"
									value='<s:property value="queryParam.curPage"/>' /> 
								<input type="hidden" id="pageSize" name="queryParam.pageSize"
									value='<s:property value="queryParam.pageSize"/>' />
						</td>
						<td width="20%">
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
					<tr>
						<td width="20%"></td>
						<td width="20%"></td>
						<td width="10%" align="center">
							<input name="addData" type="button" class="submit_but09" id="addData"
								value="添加"  onclick="addAccnoData()" />
						</td>
						<td width="10%"></td>
					</tr>
				</table>
			</form>
		</div>
		<div style="width:100%; height:auto; float:left; margin-left:10px; padding:10px 0px 10px 0px;">
			
			<table width="98%">
				<tr>
					<td width="80%">
						<h1>内部账户信息列表</h1>
					</td>
					
				</tr>
			</table>
			<div>
				<table width="98%" style="border: 1px solid #c9eaf5;"
					cellpadding="0" cellspacing="0" id="innerAccnoTable">
					<tr>
						<td width="8%" align="center" bgcolor="#c76c6f" height="26"
							class="font_colors01">序号</td>
						<td width="30%" align="center" bgcolor="#c76c6f"
							class="font_colors01">账号</td>
						<td width="20%" align="center" bgcolor="#c76c6f"
							class="font_colors01">录入日期</td>
						<td width="20%" align="center" bgcolor="#c76c6f"
							class="font_colors01">录入柜员</td>
						<td width="20%" align="center" bgcolor="#c76c6f"
							class="font_colors01">操作</td>
					</tr>
					<tbody id="queryList" align="center">
						<s:iterator value="queryList" var="queryData" status="st">
							<tr bgcolor='<s:if test="#st.count%2==0">#F4DDDF</s:if>'>
								<td style="border:1px solid #C76C6F"><s:property value="#st.count+queryParam.firstResult"/></td>
								<td style="border:1px solid #C76C6F"><s:property value="#queryData.accNo"/></td>
								<td style="border:1px solid #C76C6F"><s:property value="#queryData.inputDate"/></td>
								<td style="border:1px solid #C76C6F"><s:property value="#queryData.inputPeopleCode"/></td>
								<td style="border:1px solid #C76C6F">
									<a onclick="deleteInfo(this)" style="color:#BE333A">删除</a>
								</td>
							</tr>
						</s:iterator>
					</tbody>
					<tfoot id="innerAccnoListPageInfo">
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
							<td colspan="3" align="right"><s:if
									test="queryParam.curPage==1">
									<a>首页</a>&nbsp;&nbsp;<a>上一页</a>
								</s:if> <s:else>
									<a href="#" onclick="viewListByPage('1');return false;">首页</a>&nbsp;&nbsp;<a
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
