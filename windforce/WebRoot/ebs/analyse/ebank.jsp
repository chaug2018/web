<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<base href="<%=basePath%>" />
<title>网银对账情况统计</title>
<link href="<%=path%>/ebs/common/css/css.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/dicss.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/process.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="<%=path%>/ebs/common/js/jquery.js"></script>
<script language="javascript"  src="<%=path%>/ebs/common/js/calendar.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/process.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/orgTree.js"></script>
<script type="text/javascript">
	function toBillinfoData() {
		if(IsDate(document.getElementById("docDate")))
	   	{
			viewBillListByPage("1");
		}
	}
	
	/** 网银分页查询*/
	function viewBillListByPage(pageNumber){
	    if(document.getElementById("docDate").value.length == 0){
			alert("对账日期不能为空！");
			document.getElementById("docDate").focus();
		}else{
			$("#curPage").val(pageNumber);
			$("#netQueryForm").submit();
		}
	}
	
	//回车转Tab
	function cgo(obj, element, method) {
		var e = event ? event : window.event;
		if (e.keyCode == 13) {
			for ( var i = 0; i < obj.length; i++) {
				if (obj[i].name == element) {
					id = i;
				}
			}
			obj[id + 1].focus();
		}
	}
	
	function toExportData() {
		var objTable = document.getElementById("netQueryTable");	
		if (objTable.rows.length>2) {
			window.location.href = 'netQueryAction_exportData.action';
		}else{
			alert("查询结果无数据");
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
	<%@ include file="/ebs/taskviewtool/processing_div.jsp"%>
	<script type="text/javascript">startProcess("正在初始化界面。。。");</script>
	<input id="moudleName" type="hidden" value="网银对账情况统计"/>
	<div id="nov_nr">
		<form id="netQueryForm" action="netQueryAction_queryNetData.action"
			  name="netQuery" method="post">
			<div class="nov_moon">
				<div style="width:100%; height:auto; float:left; margin-left:15px; padding:10px 0px 10px 0px;"
					class="border_bottom01">
					<table width="98%">
						<tr>
							<td width="20%">总&nbsp;&nbsp;&nbsp;&nbsp;行&nbsp;&nbsp;&nbsp;&nbsp;
								<select>
									<option value=""> 华融湘江总行</option>
								</select>
							</td>
							<td width="20%">机 构 号&nbsp;&nbsp;
								<input name="netQueryParam.idBank1" type="text" 
									   class="diinput_text01" id="idBank"  maxlength="10"
								       value='<s:property value="netQueryParam.idBank1"/>' /> 
								<input type="hidden" id="curPage" name="netQueryParam.curPage" style="width:160px" 
								       value='<s:property value="netQueryParam.curPage"/>' />
							</td>
							<td width="20%">账户账号&nbsp;
								<input name="netQueryParam.accNo" type="text" 
									   class="diinput_text01" id="accNo"  maxlength="32"
									   value='<s:property value="netQueryParam.accNo"/>' />
							</td>
							<td width="10%" align="center">
								<input name="btnfind" type="button" class="submit_but09" id="btnfind"
								onclick="toBillinfoData()" value="统计" />
							</td>

						</tr>
						<tr>
							<td width="20%">分&nbsp;&nbsp;&nbsp;&nbsp;行&nbsp;&nbsp;&nbsp;&nbsp;
								<select name="netQueryParam.idCenter" id="selectIdCenter"
									onchange="changeIdCenter()">
								</select>
							</td>
							<td width="20%">对账日期&nbsp; 
								<input name="netQueryParam.docDate" 
									type="text" onclick="new Calendar().show(this);" title="对账日期"
									class="diinput_text01" id="docDate"  maxlength="10"
									value='<s:property value="netQueryParam.docDate"/>' /></td>
							<td width="20%">余额状态&nbsp;
								<s:select  list="refCheckflagMap"  listKey="key" 
									listValue="value" headerKey="" headerValue="--请选择--"
									value="netQueryParam.checkFlag" name="netQueryParam.checkFlag" id="checkFlag">
								</s:select>
							</td>
							<td width="10%" align="center">
							<input name="btnexport" type="button" class="submit_but09" 
							       id="btnexport" value="导出" onclick="toExportData()" />
							</td>
						</tr>
						<tr>
							<td width="20%">网 &nbsp;&nbsp;&nbsp;&nbsp;点&nbsp;&nbsp;&nbsp; 
								<select name="netQueryParam.idBank" id="selectIdBank"
									 onchange="changeIdBank()">
								</select>
							</td>
						</tr>
					</table>
			</div>
			<div
				style="width:98%; height:auto; float:left; margin-left:10px; padding:10px 0px 10px 0px; color: #000000;">
				<h1>网银对账情况统计</h1>
				<table width="100%" border="0" cellpadding="0" cellspacing="0"
					class="myTable" id="netQueryTable">
					<tr>
						<td width="5%" height="26" align="center" bgcolor="#c76c6f"
							class="font_colors01">序号</td>
						<td width="5%" align="center" bgcolor="#c76c6f"
							class="font_colors01">分行</td>
						<td width="5%" align="center" bgcolor="#c76c6f"
							class="font_colors01">网点</td>
						<td width="10%" align="center" bgcolor="#c76c6f"
							class="font_colors01">网点名称</td>	
						<td width="13%" align="center" bgcolor="#c76c6f"
							class="font_colors01">账号</td>
						<td width="7%" align="center" bgcolor="#c76c6f"
							class="font_colors01">对账方式</td>
						<td width="25%" align="center" bgcolor="#c76c6f"
							class="font_colors01">账号名称</td>
						<td width="5%" align="center" bgcolor="#c76c6f"
							class="font_colors01">对账日期</td>
						<td width="10%" align="center" bgcolor="#c76c6f"
							class="font_colors01">余额</td>
						<td width="15%" align="center" bgcolor="#c76c6f"
							class="font_colors01">对账结果</td>
					</tr>
					<tbody id="resultList">
						<s:iterator value="resultList" var="queryData" status="st">
							<tr <s:if test="#st.index%2 ==0">bgcolor="#CBD6E0" </s:if>
								<s:if test="#st.index%2 ==1">bgcolor="#FFFFFF"</s:if>>
								<td align="center"><s:property value="#st.count+netQueryParam.firstResult" />
								</td>
								<td align="center"><s:property value="#queryData.idCenter" />
								</td>
								<td align="center"><s:property value="#queryData.idBank" />
								</td>
								<td align="center"><s:property value="#queryData.bankName" />
								</td>
								<td align="center"><s:property value="#queryData.accNo" />
								</td>
								<td align="center"><s:property value="refSendModeMap.get(#queryData.sendMode)" />
								</td>
								<td align="center"><s:property value="#queryData.accName" />
								</td>
								<td align="center"><s:property value="#queryData.docDate" />
								</td>
								<td align="center"><s:property value="#queryData.strcredit" />
								</td>
								<td align="center"><s:property value="refCheckflagMap.get(#queryData.checkFlag)" />
								</td>
							</tr>
						</s:iterator>
					</tbody>
					<tfoot id="basicinfoListPageInfo">
						<tr class="pagelinks">
							<td colspan="7" align="left">每页显示
								<select onchange="viewBillListByPage('1')"
										name="netQueryParam.pageSize" id="pageSize"
										style="width:60px;">
									<option value="10"
										<s:if test="netQueryParam.pageSize==10">selected="selected"</s:if>>10</option>
									<option value="20"
										<s:if test="netQueryParam.pageSize==20">selected="selected"</s:if>>20</option>
									<option value="50"
										<s:if test="netQueryParam.pageSize==50">selected="selected"</s:if>>50</option>
								</select>条记录&nbsp;当前显示
								<s:if test="netQueryParam.total==0">0</s:if>
								<s:else>
									<s:property value="netQueryParam.firstResult+1" />
								</s:else>到
								<s:property value="netQueryParam.lastResult" />条，共
								<s:property	value="netQueryParam.total" />条</td>
								<td colspan="6" align="right">
									<s:if
										test="netQueryParam.curPage==1">
											<a>首页</a>&nbsp;&nbsp;<a>上一页</a>
									</s:if> 
									<s:else>
										<a href="#" onclick="viewBillListByPage('1');">首页</a>&nbsp;&nbsp;
										<a href="#"	onclick="viewBillListByPage('${netQueryParam.curPage - 1}');return false;">上一页</a>
									</s:else> 
										<s:if
											test="netQueryParam.curPage==netQueryParam.totalPage">
											<a>下一页</a>&nbsp;&nbsp;<a>尾页</a>
										</s:if> 
										<s:else>
											<a href="#"	onclick="viewBillListByPage(${netQueryParam.curPage + 1});return false;">下一页</a>&nbsp;&nbsp;
											<a href="#" onclick="viewBillListByPage('${netQueryParam.totalPage}');return false;">尾页</a>
										</s:else>
							</td>
						</tr>
					</tfoot>
				</table>			
			</div>
		</div>
		</form>
	</div>
</body>
</html>
