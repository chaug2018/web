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
<title>账户类型制定</title>
<link href="<%=path%>/ebs/common/css/process.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/css.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/dicss.css" rel="stylesheet" type="text/css" />
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
<script language="javascript" src="<%=path%>/ebs/common/js/enterToTab.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/process.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/ruleofacc.js"></script>


<script type="text/javascript">
	var err = "<s:property value='errMsg'/>";
	if (err != null && err.length > 0) {
		alert("初始化服务出现错误");
		top.refresh();
	}
</script>
<script type="text/javascript">
//回车转TAB
function cgo(obj, element, method) {
		var e = event ? event : window.event;
		if (e.keyCode == 13) {
			for ( var i = 0; i < obj.length; i++) {
				if (obj[i].name == element){
					id = i;
				}
			}
			obj[id + 1].focus();
		}
	}

	//刷新界面
	function refrsh(pageNumber) {
		alert("操作成功！");
		$("#curPage").val(pageNumber);
		$("#ruleOfAccCycleForm").submit();
	}
	
	function queryAcountData() {
		viewRuleListByPage("1");
	}
	//查询
	function viewRuleListByPage(pageNumber) {
		var obj = document.getElementById("select_pageSize");
		var index = obj.selectedIndex; // 选中索引
		var pageSize = obj.options[index].text; // 选中文本
		$("#curPage").val(pageNumber);
		$("#pageSize").val(pageSize);
		$("#ruleOfAccCycleForm").submit();
	}

</script>

</head>

<body class="baby_in2">
	<div id="son_div" class="son_div">
		<div class="sonpage" id="sonpage">
			<center>
				<tr>
					<td style="vertical-align: top;" colspan="2">账户类型定制规则</td>
				</tr>
			</center>
		</div>
		<%@ include file="/ebs/ruleofacccycle/rule_div.jsp"%>
	</div>
	<div class="nov_moon">
		<div style="width:98%; height:auto; float:left; margin-left:10px; padding:10px 0px 10px 0px;"
			class="border_bottom01">
			<form id="ruleOfAccCycleForm" action="ruleOfAccCycleAction!queryData.action" 
				name="ruleOfAccCycleAction"	method="post">
				<table width="90%">
					<tr>
						<td width="3%">
							账户类型:
						</td>
						<td width="8%">
							<s:select list="refAccCycleMap" class="input_text01" listKey="key"
								listValue="value" headerKey="" headerValue="--请选择--"
								style="color:#333333; font-size:13px; width:160px;"
								name="queryParam.accCycle" id="accCycle" value="queryParam.accCycle">
							</s:select>	
							<input type="hidden" id="curPage" name="queryParam.curPage"
									value='<s:property value="queryParam.curPage"/>'/>
							 <input type="hidden" id="pageSize" name="queryParam.pageSize"
									value='<s:property value="queryParam.pageSize"/>'/>
						</td>
						<td width="3%">
							科目号:
						</td>
						<td width="8%">
							<s:select list="ValParamSubnocMap" class="input_text01" listKey="key"
								listValue="value" headerKey="" headerValue="--请选择--"
								style="color:#333333; font-size:13px; width:160px;"
								name="queryParam.subjectNo" id="subjectNo" value="queryParam.subjectNo">
							</s:select>							
						</td>
						<td width="3%">
							<input name="export" type="button" class="submit_but09" id="export"
								value="查询" onclick="queryAcountData()"/>
						</td>
					</tr>
					<tr>
						<td width="3%">
						</td>
						<td width="8%">
						</td>
						<td width="3%">
						</td>
						<td width="8%">
						</td>
						<td width="8%">
							<input name="export" type="button" class="submit_but09" id="export"
								value="添加"  onclick="addrule()"/>
						</td>
					</tr>
				</table>
			</form>
		</div>
		<div style="width:100%; height:auto; float:left; margin-left:10px; padding:10px 0px 10px 0px;">
			<table width="98%">
				<tr>
					<td width="80%"><h1>账户类型定制规则</h1></td>
				</tr>
			</table>
			<form id="ruleOfAccCycleAForm"
				action="ruleOfAccCycleAction!ruleModify.action" name="ruleModify"
				method="post">
				<table width="98%" style="border: 1px solid #c9eaf5;"
					cellpadding="0" cellspacing="0" id="basicInfoTable">
					<thead>
						<tr>
							<td style="border:1px solid #C76C6F" width="10%" align="center" bgcolor="#c76c6f" colspan="1"
								class="font_colors01" >
							</td>
							<td style="border:1px solid #C76C6F" width="10%" align="center" bgcolor="#c76c6f" colspan="1"
								class="font_colors01">
							</td>
							<td style="border:1px solid #C76C6F" width="10%" align="center" bgcolor="#c76c6f" colspan="1"
								class="font_colors01">
							</td>
							<td style="border:1px solid #C76C6F" width="20%" align="center" bgcolor="#c76c6f" colspan="2"
								class="font_colors01">
								<label id="cycle1">余额</label>
							</td>
							<td style="border:1px solid #C76C6F" width="20%" align="center" bgcolor="#c76c6f" colspan="2"
								class="font_colors01">
								<label id="cycle2">单笔发生额</label>
							</td>
							<td style="border:1px solid #C76C6F" width="20%" align="center" bgcolor="#c76c6f" colspan="2"
								class="font_colors01">
								<label id="cycle3">累计发生额</label>
							</td>
							<td style="border:1px solid #C76C6F" width="10%" align="center" bgcolor="#c76c6f" colspan="1"
								class="font_colors01" >
							</td>
						</tr>
						<tr>
						   <td style="border:1px solid #C76C6F" width="10%"  align="center" bgcolor="#c76c6f" 
								class="font_colors01">序号</td>
						   <td style="border:1px solid #C76C6F" width="10%"  align="center" bgcolor="#c76c6f" 
								class="font_colors01">账户类型</td>
						   <td style="border:1px solid #C76C6F" width="10%"  align="center" bgcolor="#c76c6f" 
								class="font_colors01">科目号</td>
						   <td style="border:1px solid #C76C6F" width="10%" align="center" bgcolor="#c76c6f" 
								class="font_colors01">起</td>
						   <td style="border:1px solid #C76C6F" width="10%" align="center" bgcolor="#c76c6f"
								class="font_colors01">止</td>
						   <td style="border:1px solid #C76C6F" width="10%" align="center" bgcolor="#c76c6f" 
								class="font_colors01">起</td>
						   <td style="border:1px solid #C76C6F" width="10%" align="center" bgcolor="#c76c6f"
								class="font_colors01">止</td>
						   <td style="border:1px solid #C76C6F" width="10%" align="center" bgcolor="#c76c6f" 
								class="font_colors01">起</td>
						   <td style="border:1px solid #C76C6F" width="10%" align="center" bgcolor="#c76c6f"
								class="font_colors01">止</td>
							<td style="border:1px solid #C76C6F" width="10%"  align="center" bgcolor="#c76c6f" 
								class="font_colors01">操作</td>
						</tr>
					</thead>
					<tbody id="resultList" align="center">
						<s:iterator value="resultList" var="queryData" status="st">
							<tr bgcolor='<s:if test="#st.count%2==0">#F4DDDF</s:if>'>
							
								<td align="center" style="border:1px solid #C76C6F">
									<s:property value="#st.count+queryParam.firstResult" />
								</td>
								<td style="border:1px solid #C76C6F"><s:property value="refAccCycleMap.get(#queryData.accCycle)" /></td>
								<td style="border:1px solid #C76C6F"><s:property value="ValParamSubnocMap.get(#queryData.subjectNo)" /></td>
								<td style="border:1px solid #C76C6F"><s:property value="#queryData.minBal" /></td>
								<td style="border:1px solid #C76C6F"><s:property value="#queryData.maxBal" /></td>
								<td style="border:1px solid #C76C6F"><s:property value="#queryData.oneMinAccrual" /></td>
								<td style="border:1px solid #C76C6F"><s:property value="#queryData.oneMaxAccrual" /></td>
								<td style="border:1px solid #C76C6F"><s:property value="#queryData.totalMinAccrual" /></td>
								<td style="border:1px solid #C76C6F"><s:property value="#queryData.totalMaxAccrual" /></td>
								<td style="border:1px solid #C76C6F">
									<a onclick="rulemodifyacc(this)" style="color:#BE333A">编辑</a>
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<a onclick="deleteacc(this)" style="color:#BE333A">删除</a>
								</td>
							</tr>
						</s:iterator>
					</tbody>
					<tfoot id="insideListPageInfo">
						<tr class="pagelinks">
							<td colspan="8" align="left">每页显示 <select id="select_pageSize"
								style="width:40px;" onchange="viewRuleListByPage('1')">
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
									<a href="#" onclick="viewRuleListByPage('1');">首页</a>&nbsp;&nbsp;<a
										href="#"
										onclick="viewRuleListByPage('${queryParam.curPage - 1}');return false;">上一页</a>
								</s:else> <s:if test="queryParam.curPage==queryParam.totalPage">
									<a>下一页</a>&nbsp;&nbsp;<a>尾页</a>
								</s:if> <s:else>
									<a href="#"
										onclick="viewRuleListByPage('${queryParam.curPage + 1}');return false;">下一页</a>&nbsp;&nbsp;<a
										href="#"
										onclick="viewRuleListByPage('${queryParam.totalPage}');return false;">尾页</a>
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

