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
<meta http-equiv="Content-Type" content="text/html" />
<link href="<%=path%>/ebs/common/css/css.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/dicss.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/process.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="<%=path%>/ebs/common/js/orgTree.js"></script>
<script type="text/javascript" src="<%=path%>/common/js/jquery/jquery-1.7.1.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/process.js"></script>
<script language="javascript" src="<%=path%>/ebs/common/js/calendar.js"></script>

<script type="text/javascript">
	function queryAccnoMainDataList(){
		checkbox();
		if(IsDate(document.getElementById("docdate")))
	   	{
			viewAccnoMainDataListByPage("1");
		}
	}
	
	function viewAccnoMainDataListByPage(pageNumber){
		if(document.getElementById("docdate").value.length == 0){
			alert("对账日期不能为空！");
			document.getElementById("docdate").focus();
		}else{
			$("#curPage").val(pageNumber);
			$("#accnoMainDataQueryForm").submit();
		}
	}
	//导出对账单
	function toExportData() {
		var objTable = document.getElementById("accInfoTable");	
		if (objTable.rows.length>2) {
			var str=prompt("请输入加密密码","");    
			if(str){
				window.location.href = "DataExportAction_exportData.action?password="+str;
			}else{
				alert("请输入加密密码");
			}
		}else{
			alert("请先查询！");
		}
	}		
	
	//导出明细
	function toExportDetail() {
		var str=prompt("请输入加密密码","");    
		var objTable = document.getElementById("accInfoTable");	
				if (objTable.rows.length>2) {
			if(str){
				window.location.href = "DataExportAction_exportDetail.action?password="+str;
			}else{
				alert("请输入加密密码！");
			}
		}else{
			alert("请先查询！");
		}
	}		
	
	//回车转Tab
	function cgo(obj, element, method) {
		var e = event ? event : window.event;
		if (e.keyCode == 13) {
			for ( var i = 0; i < obj.length; i++) {
				if (obj[i].name == element) {
					id += i;
				}
			}
		}
	}
	
	//往action传checkbox值
	function checkbox(){
		var cs = document.getElementsByName("xsa");
		var id="all";
		for(var i=0;i<cs.length;i++){
			if(cs[i].checked==true){
				id=id+"+"+(i+1);
			}
		}
		$("#checkresult").val(id);
	}
	
	//checkbox初始化
	function putcheck(check){
		var cs = document.getElementsByName("xsa");
		if(check != "all"){
			var str = new Array();
			str = check.split("+");
			var leng = str.length;
			if(leng>1)
			{
				for(var i=1;i<leng;i++)
				{
					cs[str[i]-1].checked = true;
				}
			}
		}
	}
	
	$(document).ready(function(){
		var check ="<%=request.getAttribute("checkresult")%>";
		putcheck(check);
		$("#checkresult").val(check);
		var idCenter="<s:property value='idCenter'/>";
		var idBank="<s:property value='idBank'/>";
		initTree(<%=request.getAttribute("orgTree")%>,idCenter,idBank);
	});
	
	
	
</script>
</head>

<body class="baby_in2">
	<input id="moudleName" type="hidden" value="数据导出" />
	<form id="accnoMainDataQueryForm" method="post" action="DataExportAction_queryBillinfoData.action"
		name="accnoMainDataQuery">
		<div class="nov_moon">
			<div style="width:98%; height:auto; float:left; margin-left:10px; padding:10px 0px 10px 0px;"
				class="border_bottom01">
				<table width="100%">
					<tr>
						<td width="20%">总 行&nbsp;&nbsp;&nbsp; 
								<select >
									<option value="">华融湘江总行</option>
								</select>
							</td>
						<td width="20%">对账日期 
							<input name="accnoMainDataQueryParam.docDate" type="text" class="diinput_text01" 
								id="docdate" maxlength="10" title="对账日期" onclick="new Calendar().show(this);"
								value='<s:property value="accnoMainDataQueryParam.docDate"/>' />
							<input type="hidden" id="curPage" name="accnoMainDataQueryParam.curPage"
								value='<s:property value="accnoMainDataQueryParam.curPage"/>' />
						</td>
						<td width="20%">机&nbsp;构&nbsp;号
							<input name="accnoMainDataQueryParam.idBank" id="idBank"
								class="diinput_text01" value='<s:property value="accnoMainDataQueryParam.idBank"/>' />
						</td>
						<td width="10%" align="center">
							<input name=queryData type="button" class="submit_but09" id="find" value="查询"
								onclick="queryAccnoMainDataList()" />
						</td>
					</tr>
					<tr>
						<td width="20%">分 行&nbsp;&nbsp;&nbsp; 
							<select name="accnoMainDataQueryParam.idCenter" id="selectIdCenter"
								onchange="changeIdCenter()">
							</select>
						</td>
						<td width="20%">
							对账渠道选择：
						</td>
						<td width="20%">
						</td>
						<td width="10%" align="center">
							<input name="btnexport" type="button" class="submit_but09"
								id="btnexport" value="导出明细" onclick="toExportDetail()" />
						</td>
					</tr>
					<tr>
						<td width="20%">网 点&nbsp;&nbsp;&nbsp; 
							<select id="selectIdBank" onchange="changeIdBank()">
						</select>
						</td>
						<td width="20%">
							<input type="checkbox" name="xsa" id="box1"/>柜台&nbsp;
							<input type="checkbox" name="xsa" id="box2"/>邮寄&nbsp;
							<input type="checkbox" name="xsa" id="box3"/>网银&nbsp;
							<input type="checkbox" name="xsa" id="box4"/>面对面&nbsp;
							<input type="hidden" name="checkresult" id="checkresult"/>
						</td>
						<td width="20%">
						</td>
						<td width="10%" align="center">
							<input name="btnexport" type="button" class="submit_but09"
								id="btnexport" value="导出账单" onclick="toExportData()" />
						</td>
					</tr>
				</table>
			</div>
			<div
				style="width:100%; height:auto; float:left; margin-left:10px; padding:10px 0px 10px 0px; ">
				<h1>查询结果列表</h1>
				<table width="98%" cellpadding="0" cellspacing="0" id="accInfoTable">
					<tr>
						<td width="4%" align="center"  height="26" bgcolor="#c76c6f"
							class="font_colors01">序号</td>
						<td width="10%" align="center" bgcolor="#c76c6f"
							class="font_colors01">账单编号</td>
						<td width="6%" align="center" bgcolor="#c76c6f"
							class="font_colors01">机构号</td>
						<td width="10%" align="center" bgcolor="#c76c6f"
							class="font_colors01">机构名称</td>
						<td width="14%" align="center" bgcolor="#c76c6f"
							class="font_colors01">户名</td>
						<td width="6%" align="center" bgcolor="#c76c6f"
							class="font_colors01">客户号</td>
						<td width="6%" align="center" bgcolor="#c76c6f"
							class="font_colors01">联系人</td>
						<td width="14%" align="center" bgcolor="#c76c6f"
							class="font_colors01">邮寄地址</td>
						<td width="10%" align="center" bgcolor="#c76c6f"
							class="font_colors01">联系电话</td>
						<td width="6%" align="center" bgcolor="#c76c6f"
							class="font_colors01">账单状态</td>
						<td width="6%" align="center" bgcolor="#c76c6f"
							class="font_colors01">发送方式</td>
						<td width="6%" align="center" bgcolor="#c76c6f"
							class="font_colors01">打印次数</td>
					</tr>
					<tbody id="queryList" align="center">
						<s:iterator value="queryBillList" var="queryData" status="st">
							<tr bgcolor='<s:if test="#st.count%2==0">#F4DDDF</s:if>'>
								<td style="border:1px solid #C76C6F"><s:property
										value="#st.count+accnoMainDataQueryParam.firstResult" /></td>
								<td style="border:1px solid #C76C6F" align="center"><s:property value="#queryData.voucherNo" />
								</td>
								<td align="center" style="border:1px solid #C76C6F"><s:property value="#queryData.idCenter" />
								</td>
								<td align="center" style="border:1px solid #C76C6F"><s:property value="#queryData.bankName" />
								</td>
								<td align="center" style="border:1px solid #C76C6F"><s:property value="#queryData.accName" />
								</td>
								<td align="center" style="border:1px solid #C76C6F"><s:property value="#queryData.custId" />
								</td>
								<td align="center" style="border:1px solid #C76C6F"><s:property value="#queryData.linkMan" />
								</td>
								<td align="center" style="border:1px solid #C76C6F"><s:property value="#queryData.sendAddress" />
								</td>
								<td align="center" style="border:1px solid #C76C6F"><s:property value="#queryData.phone" />
								</td>
								<td align="center" style="border:1px solid #C76C6F"><s:property value="refDocstateMap.get(#queryData.docState)" />
								</td>
								<td align="center" style="border:1px solid #C76C6F"><s:property value="#queryData.sendMode" />
								</td>
								<td align="center" style="border:1px solid #C76C6F"><s:property value="#queryData.printTimes" />
								</td>
							</tr>
						</s:iterator>
					</tbody>

					<tfoot id="accInfoTable">
						<tr class="pagelinks">
							<td colspan="6">每页显示 <select
								name="accnoMainDataQueryParam.pageSize" id="pageSize"
								style="width:40px;" onchange="viewAccnoMainDataListByPage('1')">
									<option value="10"
										<s:if test="accnoMainDataQueryParam.pageSize==10">selected="selected"</s:if>>10</option>
									<option value="20"
										<s:if test="accnoMainDataQueryParam.pageSize==20">selected="selected"</s:if>>20</option>
									<option value="50"
										<s:if test="accnoMainDataQueryParam.pageSize==50">selected="selected"</s:if>>50</option>
							</select>条记录&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;当前显示<s:if
									test="accnoMainDataQueryParam.total==0">0</s:if> <s:else>
									<s:property value="accnoMainDataQueryParam.firstResult+1" />
								</s:else>到<s:property value="accnoMainDataQueryParam.lastResult" />条，共<s:property
									value="accnoMainDataQueryParam.total" />条</td>
							<td colspan="6" align="right"><s:if
									test="accnoMainDataQueryParam.curPage==1">
									<a>首页</a>&nbsp;&nbsp;<a>上一页</a>
								</s:if> <s:else>
									<a href="#" onclick="viewAccnoMainDataListByPage('1');">首页</a>&nbsp;&nbsp;<a
										href="#"
										onclick="viewAccnoMainDataListByPage('${accnoMainDataQueryParam.curPage - 1}');return false;">上一页</a>
								</s:else> <s:if
									test="accnoMainDataQueryParam.curPage==accnoMainDataQueryParam.totalPage">
									<a>下一页</a>&nbsp;&nbsp;<a>尾页</a>
								</s:if> <s:else>
									<a href="#"
										onclick="viewAccnoMainDataListByPage(${accnoMainDataQueryParam.curPage + 1});return false;">下一页</a>&nbsp;&nbsp;<a
										href="#"
										onclick="viewAccnoMainDataListByPage('${accnoMainDataQueryParam.totalPage}');return false;">尾页</a>
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
