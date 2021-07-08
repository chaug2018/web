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
<link href="<%=path%>/ebs/common/css/process.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/css.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/dicss.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="<%=path%>/ebs/common/js/jquery.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/orgTree.js"></script>
<script language="javascript" src="<%=path%>/ebs/common/js/enterToTab.js"></script>
<script language="javascript" src="<%=path%>/ebs/common/js/calendar.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/process.js"></script>
<title>特殊账户过滤查询</title>
<meta http-equiv="Content-Type" content="text/html" />

<script type="text/javascript">

	function queryImport() {
		viewSpecileListByPage("1");
	}
//查询
	function viewSpecileListByPage(pageNumber) {
		var obj = document.getElementById("select_pageSize");
		var index = obj.selectedIndex; // 选中索引
		var pageSize = obj.options[index].text; // 选中文本
		$("#curPage").val(pageNumber);
		$("#pageSize").val(pageSize);
		$("#inputSpecileForm").submit();
	}


</script>
</head>

<body class="baby_in2">
<div class="nov_moon">
	<div style="width:98%; height:auto; float:left; margin-left:10px; padding:10px 0px 10px 0px;"
			class="border_bottom01">
		<form id="inputSpecileForm" action="inputQueryAction!queryData.action" name="inputSpecileForm" method="post">
			<table width="100%">
				<tr>
					<td>
						账 号
					</td>
					<td >
						<input type="text" class="diinput_text01"  name="inputParam.accno"/>
						<input type="hidden" id="curPage" name="inputParam.curPage"
							value='<s:property value="inputParam.curPage"/>' /> 
						<input type="hidden" id="pageSize" name="inputParam.pageSize"
							value='<s:property value="inputParam.pageSize"/>' />
					</td>
					<td>
						日 期
					</td>
					<td > 
						<input name="inputParam.date" type="text" class="diinput_text01" title='维护时间' id="signTime"
							onfocus="new Calendar().show(this);" maxlength="12"
							value='<s:property value="inputParam.date"/>'/> 
					</td>
				</tr>
				<tr > 
					<td>
						是否导入
					</td>
					<td >
						<s:select list="refImportName" listKey="key"
									listValue="value" headerKey="" headerValue="--请选择--"
									name="inputParam.flag" id="flag">
						</s:select>
					</td>
					<td>
						账户类型
					</td>
					<td >
						<s:select list="refAccCycleMap" listKey="key"
									listValue="value" headerKey="" headerValue="--请选择--"
									name="inputParam.accCycle" id="accCycle">
						</s:select>
					</td>
					<td width="10%" rowspan="2" align="center">
					<input name="queryData" type="button" class="submit_but09" id="queryData" 
						value="查询" onclick="queryImport()" />
					</td>
				</tr>
			</table>
		</form>	
	</div>
	<div style="width:100%; height:auto; float:left; margin-left:10px; padding:10px 0px 10px 0px;">
		<table width="98%"  cellpadding="0" cellspacing="0" id="basicInfoTable">
					<td>
						<h1>特殊账户</h1>
					</td>
					<tr>
						<td width="10%" height="26" align="center" bgcolor="#c76c6f"
							class="font_colors01">序号
						</td>
						<td width="20%" align="center" bgcolor="#c76c6f"
							class="font_colors01">账号
						</td>
						<td width="15%" align="center" bgcolor="#c76c6f"
							class="font_colors01">账户类型
						</td>
						<td width="20%" align="center" bgcolor="#c76c6f"
							class="font_colors01">验印模式
						</td>
						<td width="10%" align="center" bgcolor="#c76c6f"
							class="font_colors01">发送方式
						</td>
						<td width="15%" align="center" bgcolor="#c76c6f"
							class="font_colors01">日期
						</td>
						<td width="10%" align="center" bgcolor="#c76c6f"
							class="font_colors01">是否导入
						</td>
					</tr>
				<tbody id="result" align="center">
						<s:iterator value="result" var="queryData" status="st">
							<tr bgcolor='<s:if test="#st.count%2==0">#F4DDDF</s:if>'>
								<td style="border:1px solid #C76C6F"><s:property value="#st.count+inputParam.firstResult" /></td>
								<td style="border:1px solid #C76C6F"><s:property value="#queryData.accno" />
								<td style="border:1px solid #C76C6F"><s:property value="refAccCycleMap.get(#queryData.accCycle)" />
								<td style="border:1px solid #C76C6F"><s:property value="sealModeMap.get(#queryData.sealMode)" />
								<td style="border:1px solid #C76C6F"><s:property value="sendTypeMap.get(#queryData.sendMode)" />
								<td style="border:1px solid #C76C6F"><s:property value="#queryData.docDate" />
								<td style="border:1px solid #C76C6F"><s:property value="refImportName.get(#queryData.isImport)" />
							</tr>
						</s:iterator>
				</tbody>
				<tfoot id="blackWhiteListPageInfo">
						<tr class="pagelinks">
							<td colspan="4">每页显示 <select id="select_pageSize"
								style="width:40px;" onchange="viewSpecileListByPage('1')">
									<option value="10"
										<s:if test="inputParam.pageSize==10">selected="selected"</s:if>>10</option>
									<option value="20"
										<s:if test="inputParam.pageSize==20">selected="selected"</s:if>>20</option>
									<option value="50"
										<s:if test="inputParam.pageSize==50">selected="selected"</s:if>>50</option>
							</select>条记录&nbsp;&nbsp;&nbsp;&nbsp;当前显示<s:if test="inputParam.total==0">0</s:if>
								<s:else>
									<s:property value="inputParam.firstResult+1" />
								</s:else>到<s:property value="inputParam.lastResult" />条，共<s:property
									value="inputParam.total" />条</td>
							<td colspan="12" align="right"><s:if
									test="inputParam.curPage==1">
									<a>首页</a>&nbsp;&nbsp;<a>上一页</a>
								</s:if> <s:else>
									<a href="#" onclick="viewSpecileListByPage('1');">首页</a>&nbsp;&nbsp;<a
										href="#"
										onclick="viewSpecileListByPage('${inputParam.curPage - 1}');return false;">上一页</a>
								</s:else> <s:if test="inputParam.curPage==inputParam.totalPage">
									<a>下一页</a>&nbsp;&nbsp;<a>尾页</a>
								</s:if> <s:else>
									<a href="#"
										onclick="viewSpecileListByPage('${inputParam.curPage + 1}');return false;">下一页</a>&nbsp;&nbsp;<a
										href="#"
										onclick="viewSpecileListByPage('${inputParam.totalPage}');return false;">尾页</a>
								</s:else>
							</td>
						</tr>
				</tfoot>
		</table>
	</div>
</div>
</body>
</html>

