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

<title>内部账户关联</title>
<meta http-equiv="Content-Type" content="text/html" />
<link href="<%=path%>/ebs/common/css/css.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/dicss.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/process.css" rel="stylesheet" type="text/css" />
<style>
#son_div {
	position: absolute;
	width: 600px;
	height: 200px;
	margin-left: 50px;
	margin-top: 30px;
	background:url('/windforce/ebs/common/images/bg_content.jpg');
}
</style>
<script type="text/javascript" src="<%=path%>/ebs/common/js/jquery.js"></script>
<script language="javascript"  src="<%=path%>/ebs/common/js/enterToTab.js"></script>
<script language="javascript"  src="<%=path%>/ebs/common/js/calendar.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/process.js"></script>


<script type="text/javascript">
//点取消关闭 窗口时  把输入的信息清空
function closeDiv (){
	$("#custId_add").val("");
	$("#accNo_div1").val("");
	closeSon();
}
//删除操作
	function innerdeleteAcc (data){
		var	pagenumber = $("#nowPage").val();
		var cell=data.parentNode;
		var row=cell.parentNode;
		
		var accno = row.cells[1].innerHTML;
		var custId = row.cells[2].innerHTML;
		var recheckCustId = row.cells[3].innerHTML;
		
		var postData ="custId="+custId+"&recheckCustId="+recheckCustId+"&accNo="+accno+"&temp="+new Date();
		if(confirm("确认删除此信息！")){
	        $.ajax({
	                type: "POST",
	                url: "insideAccnoParamAction!deleteInside.action",
	                data: postData,
	                success: function(result) {
	                        if(result == "success"){
	                        	alert("删除成功！");
	                        	viewAccnoListByPage(pagenumber);
	                        }else{
	                        	alert("删除失败，请稍后再试！");
	                        }
	                }
	        });
        }
	}
	
	// 编辑  已废除
	function innermodifyAcc(data){
		var cell=data.parentNode;
		var row=cell.parentNode;
		var accno  =row.cells[2].innerHTML;
		$("#accNo_div1").val(accno);
		$("#accNo_div2").val(accno);
		$("#flog_div").val("modify");
		showSon();
	
	}
	//显示增加窗口
	function addAcountData(){
		$("#flog_div").val("add");
		showSon();
	}
	
	
	function queryAcountData() {
		var query_accNo = $("#accno").val();
		var patrn =/^[0-9]{0,40}$/;
		
		if(!patrn.exec(query_accNo)){
			alert("账号格式不正确,只能是数字类型!");
		}else{
			viewAccnoListByPage("1");
		}
		
	}

	function viewAccnoListByPage(pageNumber) {
		var pageSize = $("#select_pageSize").val();
		$("#pageSize").val(pageSize);
		$("#curPage").val(pageNumber);
		$("#insideAccnoParamForm").submit();
	}
	
	//内部账户 配置增加
	function ineraddsubmit(){
		var	pagenumber = $("#nowPage").val();
		var flog_div =   $("#flog_div").val();
		var custId =   $("#custId_add").val();
		var recheckCustId =   $("#recheckCustId_add").val();
		var accNo_div1 = $("#accNo_div1").val();
		//var accNo_div2 = $("#accNo_div2").val();
		
		var patrn =/^[0-9]{0,40}$/;
		if(!patrn.exec(accNo_div1)){
			alert("账号格式不正确!");
		}else if(accNo_div1==null||accNo_div1==""){
			alert("账号不能为空!");
		}else if(custId==null||custId==""){
			alert("操作员代码不能为空!");
		}else if(recheckCustId==null||recheckCustId==""){
			alert("复核员代码不能为空!");
		}else if(custId==recheckCustId){
			alert("操作员和复核员不能为同一人!");
		}else{
			var postData = "custId="+custId+"&accNo_div1="+accNo_div1+"&flog_div="+flog_div+"&recheckCustId="+recheckCustId+"&temp="+new Date();
			
			$.ajax({
                type: "POST",
                url: "insideAccnoParamAction!addOrModifyInseide.action",
                data: postData,
                success: function(result) {
                        if(result == "1"){
                        	alert("操作成功!");
							viewAccnoListByPage(pagenumber);
                        }else if(result == "0") {
                        	alert("账号不存在!");
                        }else if(result == "2"){
                        	alert("该账号已做关联，不能再做关联!");
                        }else if(result == "4"){
                        	alert("操作员代码不存在!");
                        }else if(result == "5"){
                        	alert("复核员代码不存在!");
                        }else if(result == "6"){
                        	alert("操作员与复核员不在同一机构下!");
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
					<td style="vertical-align: top;" colspan="2">内部账户关联配置</td>
				</tr>
			</center>
		</div>
		<%@ include file="/ebs/insideaccno/iner_div.jsp"%>
	</div>
	
	<div class="nov_moon">
		<div
			style="width:98%; height:auto; float:left; margin-left:10px; padding:10px 0px 10px 0px;"
			class="border_bottom01">
			<form id="insideAccnoParamForm"
				action="insideAccnoParamAction!queryData.action" name="insideAccnoParam"
				method="post">
				<table width="80%">
					<tr>
						<td width="20%">账&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号&nbsp;&nbsp;&nbsp;
							<input name="queryParam.accno" type="text"
									class="diinput_text01" maxlength="32" id="accno"
									value='<s:property value="queryParam.accno"/>' />
							<input type="hidden" id="curPage" name="queryParam.curPage" value='<s:property value="queryParam.curPage"/>' />
							<input type="hidden" id="pageSize" name="queryParam.pageSize" value='<s:property value="queryParam.pageSize"/>' />
						</td>
						<td width="20%">操作员代码&nbsp;&nbsp;&nbsp;
							<input name="queryParam.custId" type="text" 
									class="diinput_text01" maxlength="12" id="custId" 
									value='<s:property value="queryParam.custId"/>' />
						</td>
						<td width="10%" rowspan="2" align="center"><input
							name="export" type="button" class="submit_but09" id="export"
							value="查询" onclick="queryAcountData()"/></td>
					</tr>
					<tr></tr>
					<tr>
					<td width="20%"></td>
					<td width="20%">复核员代码&nbsp;&nbsp;&nbsp;
						<input name="queryParam.recheckCustId" type="text" 
								class="diinput_text01" maxlength="12" id="recheckCustId" 
								value='<s:property value="queryParam.recheckCustId"/>' />
					</td>
					<td width="10%" align="center">
						<input name="addData" type="button" class="submit_but09" id="addData"
							value="添加"  onclick="addAcountData()"/></td>
					</tr>
				</table>
			</form>
		</div>
		
		<div style="width:100%; height:auto; float:left; margin-left:10px; padding:10px 0px 10px 0px;">
			<h1>内部账户关系配置</h1>
			
			<table width="98%" style="border: 1px solid #c9eaf5;"
				cellpadding="0" cellspacing="0" id="basicInfoTable">
				<tr>		
					<td width="10%" height="26" align="center" bgcolor="#c76c6f"
						class="font_colors01">序号</td>
					<td width="20%" align="center" bgcolor="#c76c6f"
						class="font_colors01">账号</td>
					<td width="20%" align="center" bgcolor="#c76c6f"
						class="font_colors01">操作员代码</td>
					<td width="20%" align="center" bgcolor="#c76c6f"
						class="font_colors01">复核员代码</td>
					<td width="20%" align="center" bgcolor="#c76c6f"
						class="font_colors01">操作</td>
				</tr>
				<tbody id="queryList" align="center">
					<s:iterator value="queryList" var="queryData" status="st">
						<tr bgcolor='<s:if test="#st.count%2==0">#CBD6E0</s:if>'>
						
							<td align="center" >
								<s:property value="#st.count+queryParam.firstResult" />
							</td>
							
							<td ><s:property value="#queryData.accNo"/></td>	
							<td ><s:property value="#queryData.custId" /></td>
							<td ><s:property value="#queryData.recheckCustId" /></td>		
							<td >
								<!-- <a onclick="innermodifyAcc(this)" style="color:#BE333A">编辑</a>
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; -->
								<a onclick="innerdeleteAcc(this)" style="color:#BE333A">删除</a>
							</td>
						</tr>
					</s:iterator>
				</tbody>
				<tfoot id="insideListPageInfo">
					<tr class="pagelinks">
						<td colspan="3" align="left">每页显示 <select id="select_pageSize"
							style="width:40px;" onchange="viewAccnoListByPage('1')">
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
						<td  colspan="3" align="right"><s:if
								test="queryParam.curPage==1">
								<a>首页</a>&nbsp;&nbsp;<a>上一页</a>
							</s:if> <s:else>
								<a href="#" onclick="viewAccnoListByPage('1');return false;">首页</a>&nbsp;&nbsp;<a
									href="#"
									onclick="viewAccnoListByPage('${queryParam.curPage - 1}');return false;">上一页</a>
							</s:else> <s:if test="queryParam.curPage==queryParam.totalPage">
								<a>下一页</a>&nbsp;&nbsp;<a>尾页</a>
							</s:if> <s:else>
								<a href="#"
									onclick="viewAccnoListByPage('${queryParam.curPage + 1}');return false;">下一页</a>&nbsp;&nbsp;<a
									href="#"
									onclick="viewAccnoListByPage('${queryParam.totalPage}');return false;">尾页</a>
							</s:else>
								<input type="hidden" id="nowPage" value="${queryParam.curPage}"/>
						</td>
					</tr>
				</tfoot>
			</table>

		</div>
	</div>

</body>
</html>

