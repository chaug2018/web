<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<base href="<%=basePath%>" />
<title>人员岗位查询</title></>
<link href="<%=path%>/ebs/common/css/css.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/dicss.css"	rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/process.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="<%=path%>/ebs/common/js/jquery.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/process.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/orgTree.js"></script>

<script type="text/javascript">
	var err = "<s:property value='errMsg'/>";
	if (err != null && err.length > 0) {
		alert("初始化查询服务出现错误");
		top.refresh();
	}
</script>

<script type="text/javascript">
	function submitQuery() {
		viewBillListByPage('1');
	}
	
	/** 票据查询之分页查询*/
	function viewBillListByPage(pageNumber){
		$("#curPage").val(pageNumber);
		$("#queryPeopleRoleForm").submit();
	}
	
	$(document).ready(function(){
		var idCenter="<s:property value='idCenter'/>";
		var idBank="<s:property value='idBank'/>";
		stopProcess();
		initTree(<%=request.getAttribute("orgTree")%>,idCenter,idBank);
	});
	
	function toExportData() {
		var objTable = document.getElementById("peopleRoleTable");	
		if (objTable.rows.length>2) {
			window.location.href = "queryPeopleRoleAction_exportData.action";
		}else{
			alert("查询结果无数据");
		}
	}
</script>

<body class="baby_in2">
	<input id="moudleName" type="hidden" value="人员岗位查询" />
	<form id="queryPeopleRoleForm" method="post" action="queryPeopleRoleAction_query.action"
		name="queryPeopleRoleAction">
		<div class="nov_moon">
			<div style="width:100%; height:auto; float:left; margin-left:10px; padding:10px 0px 10px 0px;"
				class="border_bottom01">
				<table width="98%">
					<tr>
						<td width="20%">总&nbsp;&nbsp;&nbsp;&nbsp; 行&nbsp;&nbsp;&nbsp;&nbsp;
								<select >
									<option value=""> 华融湘江总行</option>
								</select>
						</td>
						<td width="20%">机&nbsp;构&nbsp;号&nbsp;&nbsp;
							<input name="organizeInfo" id="idBank" class="diinput_text01"
								value='<s:property value="idBankCode"/>' />
						</td>
						<td width="10%" align="center">
							<input name=queryData type="button"	class="submit_but09" 
								id="find" value="查询" onclick="submitQuery()" />
						</td>
					</tr>
					<tr>
						<td width="20%">分 &nbsp;&nbsp;&nbsp;&nbsp;行&nbsp;&nbsp;&nbsp;&nbsp; 
							<select	name="queryParam.idCenter" id="selectIdCenter"  
								 onchange="changeIdCenter()" >
							</select>
						</td>
						<td width="20%">姓&nbsp;&nbsp;&nbsp;&nbsp;名&nbsp;&nbsp;&nbsp;
								<input name="queryParam.peopleName" type="text" class="diinput_text01"
									id="txtpeopleName" maxlength="32" value='<s:property value="queryParam.peopleName"/>' />
						</td>
					</tr>
					<tr>
						<td width="20%">网&nbsp;&nbsp;&nbsp;&nbsp;点&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<select name="queryParam.idBank" id="selectIdBank" 
								onchange="changeIdBank()"  >
							</select>
						</td>
						<td width="20%">账&nbsp;&nbsp;&nbsp;&nbsp;号&nbsp;&nbsp;&nbsp; 
								<input name="queryParam.peopleCode" type="text" class="diinput_text01"
									id="txtpeopleCode" maxlength="32" value='<s:property value="queryParam.peopleCode"/>' />
						</td>
						<td width="10%" align="center">
								<input name="btnexport" type="button" class="submit_but09" id="btnexport" value="导出"
								onclick="toExportData()" />
							</td>
					</tr>
				</table>
			</div>
			<div style="width:98%; height:auto; float:left; margin-left:10px; padding:10px 0px 10px 0px; ">
				<h1>人员岗位查询</h1>
				<table width="100%"  border="0" cellpadding="0" cellspacing="0" id="peopleRoleTable">
					<tr>
						<td width="10%" height="26" align="center" bgcolor="#c76c6f"
							class="font_colors01">序号</td>
						<td width="20%" align="center" bgcolor="#c76c6f"
							class="font_colors01">机构号</td>
						<td width="20%" align="center" bgcolor="#c76c6f"
							class="font_colors01">姓名</td>
						<td width="20%" align="center" bgcolor="#c76c6f"
							class="font_colors01">账号</td>
						<td width="30%" align="center" bgcolor="#c76c6f"
							class="font_colors01">岗位</td>
					</tr>
					<tbody id="queryList" align="center">
						<s:iterator value="resultList" var="queryData" status="st">
							<tr <s:if test="#st.index%2 ==0">bgcolor="#CBD6E0"</s:if>
								<s:if test="#st.index%2 ==1">bgcolor="#FFFFFF"</s:if>>
								<td style="border:0px solid #C76C6F"><s:property value="#st.count+queryParam.firstResult" />
								</td>
								<td align="center" style="border:0px solid #C76C6F"><s:property value="#queryData.orgid" />
								</td>
								<td align="center" style="border:0px solid #C76C6F"><s:property value="#queryData.peopleName" />
								</td>
								<td align="center" style="border:0px solid #C76C6F"><s:property value="#queryData.peopleCode" />
								</td>
								<td align="center" style="border:0px solid #C76C6F"><s:property value="#queryData.roleGroupName" />
								</td>
							</tr>
						</s:iterator>
					</tbody>
 					<input type="hidden" id="curPage" name="queryParam.curPage"
						value='<s:property value="queryParam.curPage"/>' />
					<tfoot id="accInfoTable">
						<tr class="pagelinks">
							<td colspan="3">每页显示 
								<select name="queryParam.pageSize" id="pageSize" 
									style="width:40px;" onchange="viewBillListByPage('1')">
									<option value="10"
										<s:if test="queryParam.pageSize==10">selected="selected"</s:if>>10</option>
									<option value="20"
										<s:if test="queryParam.pageSize==20">selected="selected"</s:if>>20</option>
									<option value="50"
										<s:if test="queryParam.pageSize==50">selected="selected"</s:if>>50</option>
								</select>条记录&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;当前显示
								<s:if test="queryParam.total==0">0
								</s:if> 
								<s:else>
									<s:property value="queryParam.firstResult+1" />
								</s:else>到
								<s:property value="queryParam.lastResult" />条，共
								<s:property value="queryParam.total" />条
							</td>
							<td colspan="8" align="right">
								<s:if test="queryParam.curPage==1">
									<a>首页</a>&nbsp;&nbsp;
									<a>上一页</a>
								</s:if> 
								<s:else>
									<a href="#" onclick="viewBillListByPage('1');return false;">首页</a>&nbsp;&nbsp;
									<a href="#"	onclick="viewBillListByPage('${queryParam.curPage - 1}');return false;">上一页</a>
								</s:else> 
								<s:if test="queryParam.curPage==queryParam.totalPage">
									<a>下一页</a>&nbsp;&nbsp;
									<a>尾页</a>
								</s:if> 
								<s:else>
									<a href="#"	onclick="viewBillListByPage(${queryParam.curPage + 1});return false;">下一页</a>&nbsp;&nbsp;
									<a href="#"	onclick="viewBillListByPage('${queryParam.totalPage}');return false;">尾页</a>
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
