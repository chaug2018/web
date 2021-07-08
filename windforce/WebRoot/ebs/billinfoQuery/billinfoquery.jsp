<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>对账单信息查询</title>
<link href="<%=path%>/ebs/common/css/css.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/dicss.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/process.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="<%=path%>/ebs/common/js/jquery.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/orgTree.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/checkmaindata.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/process.js"></script>
<script language="javascript" src="<%=path%>/ebs/common/js/calendar.js"></script>
<script language="javascript" src="<%=path%>/ebs/common/js/enterToTab.js"></script>
<script type="text/javascript">

	function toBillinfoData() {
		viewAccnoDetailListByPage("1");
	}
	
	function viewAccnoDetailListByPage(pageNumber){
		 if(document.getElementById("docdate").value.length == 0){
			alert("对账日期不能为空！");
			document.getElementById("docdate").focus();
		}else{
			if(IsDate(document.getElementById("docdate")) ){
				$("#curPage").val(pageNumber);
				$("#billinfoQueryForm").submit();
			}else{
				document.getElementById("docdate").focus();
			}
		}
	}
	
	function dealChange(){
	    var accNo=document.getElementById("accNo").value;
	    var queryType=document.getElementById("queryType").value;
	    if(queryType=="1"){
	    	document.getElementById("accNo").disabled=true;
	    	document.getElementById("accNo").value="无效的条件";
	    	document.getElementById("checkResult").disabled=true;
	    	document.getElementById("checkResult").value="";
	    	document.getElementById("faceFlag").disabled=true;
	    	document.getElementById("faceFlag").value="";
	    }else{
	    	document.getElementById("accNo").disabled=false;
	    	document.getElementById("accNo").value="";
	    	document.getElementById("checkResult").disabled=false;
	    	document.getElementById("faceFlag").disabled=false;
	    }
	}
	
	function initChange(){
        var accNo=document.getElementById("accNo").value;
	    var queryType=document.getElementById("queryType").value;
        if(queryType=="1"){
	   		document.getElementById("accNo").disabled=true;
	    	document.getElementById("accNo").value="无效的条件";
	    	document.getElementById("checkResult").disabled=true;
	    	document.getElementById("checkResult").value="";
	    	document.getElementById("faceFlag").disabled=true;
	    	document.getElementById("faceFlag").value="";
	    }else{
	    	document.getElementById("accNo").disabled=false;
	    	document.getElementById("checkResult").disabled=false;
	    	document.getElementById("faceFlag").disabled=false;
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
		var objTable1 = document.getElementById("queryBillList");	
		var objTable2 = document.getElementById("resultList");
		if (objTable1.rows.length>0) {
			window.location.href = "billinfoQueryAction_exportData.action";
		}else if(objTable2.rows.length>0){
		   window.location.href = "billinfoQueryAction_exportData.action";
		}
		else{
			alert("查询结果无数据");
		}
	}	
	$(document).ready(
			function() {
				var idCenter = "<s:property value='idCenter'/>";
				var idBank = "<s:property value='idBank'/>";
				stopProcess();
				initTree(<%=request.getAttribute("orgTree")%>, idCenter,idBank);
				var queryType=document.getElementById("queryType").value;
				if(queryType=="1" ){
		 			document.getElementById("accNoQuery").style.display="none";
		 			document.getElementById("voucherNoQuery").style.display="block";
				}
			initChange();
			
			$("#sendMode").append("<option value='5'>无</option>");
			var sendMode = "<s:property value='billinfoQueryParam.sendMode'/>";
			if(5==sendMode){
				document.getElementById("sendMode").options[5].selected=true;
			}
			
			$("#proveFlag").append("<option value='5'>无</option>");
			var proveFlag = "<s:property value='billinfoQueryParam.proveFlag'/>";
			if(5==proveFlag){
				document.getElementById("proveFlag").options[11].selected=true;
			}
	});
</script>
</head>
<body class="baby_in2" >
<script type="text/javascript">startProcess("正在初始化界面。。。");</script>
<input id="moudleName" type="hidden" value="对账单信息查询"/>
	<div>	
	<form id="billinfoQueryForm" method="post" action="billinfoQueryAction_queryBillinfoData.action" name="billinfoQuery">
		<div class="nov_moon">
			<div style="width:100%; height:auto; float:left; margin-left:10px; padding:10px 0px 10px 0px;"
				class="border_bottom01">
                <input type="hidden" name="billinfoQueryParam.curPage" id="curPage" />
					<table width="98%">
						<tr>
							<td width="20%">总 行&nbsp;&nbsp;&nbsp; 
								<select>
									<option value="">华融湘江总行</option>
								</select>
							</td>
							<td width="20%">机&nbsp;&nbsp;构&nbsp;号&nbsp;
								<input type="text" name="billinfoQueryParam.idBank" class="diinput_text01" id="idBank" maxlength="12"
									value='<s:property value="billinfoQueryParam.idBank"/>' />
							</td>
							<td width="20%">账单编号&nbsp; 
								<input name="billinfoQueryParam.voucherNo" type="text" class="diinput_text01" id="voucherNo" 
									maxlength="30" value='<s:property value="billinfoQueryParam.voucherNo"/>' />
							</td>
							<td width="10%" align="center">
								<input name="btnfind" type="button" class="submit_but09" id="btnfind"
								onclick="toBillinfoData()" value="查询" />
							</td>
						</tr>
						<tr>
							<td width="20%">分 行&nbsp;&nbsp;&nbsp; 
								<select name="billinfoQueryParam.idCenter"  id="selectIdCenter" onchange="changeIdCenter()">
								</select>
							</td>
							<td width="20%">对账日期&nbsp; 
								<input name="billinfoQueryParam.docDate" type="text" onclick="new Calendar().show(this);"  
									class="diinput_text01" id="docdate" maxlength="10" title='对账日期' 
									value='<s:property value="billinfoQueryParam.docDate"/>' />
							</td>
							<td width="20%">验印状态&nbsp;
								<s:select list="refProveflagMap" listKey="key"
									listValue="value" headerKey="" headerValue="全部"
									value="billinfoQueryParam.proveFlag" 
									name="billinfoQueryParam.proveFlag" id="proveFlag">
								</s:select>
							</td>
							<td width="10%">
							</td>
						</tr>
						<tr>
							<td width="20%">网  点&nbsp;&nbsp;&nbsp; 
								<select onchange="changeIdBank()" id="selectIdBank" >
								</select>
							</td>
							<td width="20%">账户账号&nbsp; 
								<input name="billinfoQueryParam.accNo" type="text"
										class="diinput_text01" id="accNo" maxlength="128"
										value='<s:property value="billinfoQueryParam.accNo"/>' />
							</td>
							<td width="20%">查询方式&nbsp; 
								<s:select list="refQueryTypeMap" listKey="key"
									listValue="value" headerKey="" value="billinfoQueryParam.queryType"  onchange="dealChange()"
									name="billinfoQueryParam.queryType" id="queryType">
								</s:select>
							</td>	
							<td width="10%" align="center">
								<input name="export" type="button" class="submit_but09" id="export"
									onclick="toExportData()" value="导出" />
							</td>
						</tr>
						<tr>
							<td width="20%">是否特殊面对面&nbsp;
								<s:select list="faceFlagMap" listKey="key" style="width:165px" 
									listValue="value" headerKey="" headerValue="--请选择--" value="billinfoQueryParam.faceFlag"
									name="billinfoQueryParam.faceFlag" id="faceFlag">
								</s:select>
							</td>
							<td width="20%">对账结果&nbsp; 
								<s:select list="checkResultMap" listKey="key" style="width:165px" 
									listValue="value" headerKey="" headerValue="--请选择--" value="billinfoQueryParam.checkResult"
									name="billinfoQueryParam.checkResult" id="checkResult">
								</s:select>
							</td>
							<td width="20%">发送方式&nbsp; 
								<s:select list="refSendModeMap" listKey="key" style="width:165px" 
									listValue="value" headerKey="" headerValue="--请选择--" value="billinfoQueryParam.sendMode"
									name="billinfoQueryParam.sendMode" id="sendMode">
								</s:select>
							</td>
						</tr>
					</table>
			</div>
			<div
				style="width:100%; height:auto; float:left; margin-left:10px; padding:10px 0px 10px 0px;">
				<h1>对账单信息列表</h1>
					<table width="100%" border="0" cellpadding="0" cellspacing="0"
					class="myTable" id="accNoQuery"  style="display:block;">
					<tr>
						<td width="3%" height="26" align="center" bgcolor="#c76c6f"
							class="font_colors01">序号</td>
						<td width="8%" align="center" bgcolor="#c76c6f"
							class="font_colors01">对账单编号</td>
						<td width="5%" align="center" bgcolor="#c76c6f"
							class="font_colors01">机构号</td>
						<td width="8%" align="center" bgcolor="#c76c6f"
							class="font_colors01">账号</td>
						<td width="12%" align="center" bgcolor="#c76c6f"
							class="font_colors01">户名</td>
						<td width="8%" align="center" bgcolor="#c76c6f"
							class="font_colors01">余额</td>
						<td width="5%" align="center" bgcolor="#c76c6f"
							class="font_colors01">对账日期</td>
						<td width="5%" align="center" bgcolor="#c76c6f"
							class="font_colors01">币种</td>
						<td width="6%" align="center" bgcolor="#c76c6f"
							class="font_colors01">验印状态</td>
						<td width="6%" align="center" bgcolor="#c76c6f"
							class="font_colors01">余额结果</td>
						<td width="6%" align="center" bgcolor="#c76c6f"
							class="font_colors01">账单状态</td>
						<td width="6%" align="center" bgcolor="#c76c6f"
							class="font_colors01">账户类型</td>
						<td width="6%" align="center" bgcolor="#c76c6f"
							class="font_colors01">回收日期</td>
						<td width="5%" align="center" bgcolor="#c76c6f"
							class="font_colors01">对账成功</td>
						<td width="5%" align="center" bgcolor="#c76c6f"
							class="font_colors01">发送方式</td>
						<td width="5%" align="center" bgcolor="#c76c6f"
							class="font_colors01">操作</td>
					</tr>
					<tbody id="resultList">
						<s:iterator value="resultList" var="queryData"  status="st">
							
							<tr 
							<s:if test="#st.index%2 ==0">bgcolor="#CBD6E0"</s:if>
							<s:if test="#st.index%2 ==1">bgcolor="#FFFFFF"</s:if>
							>
								<td align="center"><s:property value="#st.count+billinfoQueryParam.firstResult" /></td>
								<td align="center"><s:property value="#queryData.checkMainData.voucherNo" /></td>
								<td align="center"><s:property value="#queryData.idBank" /></td>
								<td align="center"><s:property value="#queryData.accNo" /></td>
								<td align="center"><s:property value="#queryData.checkMainData.accName" /></td>
								<td align="center"><s:property value="#queryData.strcredit" /></td>
								<td align="center"><s:property value="#queryData.checkMainData.docDate" /></td>
								<td align="center">
									<s:if test='currTypeMap.get(#queryData.currency)==null || null ==""'>
										<s:property value="#queryData.currency" />
									</s:if>
									<s:else>
										<s:property value="currTypeMap.get(#queryData.currency)" />
									</s:else>
								</td>
								<td align="center">
								<s:property value="refProveflagMap.get(#queryData.checkMainData.proveFlag)" />
								</td>
								<td align="center">
								<s:property value="refCheckflagMap.get(#queryData.finalCheckFlag)" />
								</td>
								<td align="center">
								<s:property value="refDocStateMap.get(#queryData.checkMainData.docState)" />
								</td>
								<td align="center">
								<s:property value="refAccCycleMap.get(#queryData.accCycle)" />
								</td>
								<td align="center"><s:property value="#queryData.checkMainData.workDate" /></td>
								<td  align="center">
									<s:property value="#queryData.result" />
								</td>
								<td  align="center">
									<s:property value="refSendModeMap.get(#queryData.sendMode)" />
								</td>
								<td align="center"><a href='viewBillinfoDetail.action?voucherNo=<s:property value="#queryData.voucherNo"/>'>查看详情</a></td>
							</tr>
						</s:iterator>
					</tbody>
				
				</table>
				<table width="100%" border="0" cellpadding="0" cellspacing="0"
					class="myTable" id="voucherNoQuery" style="display:none;" >
					<tr>
						<td width="6%" height="26" align="center" bgcolor="#c76c6f"
							class="font_colors01">序号</td>
						<td width="12%" align="center" bgcolor="#c76c6f"
							class="font_colors01">对账单编号</td>
						<td width="8%" align="center" bgcolor="#c76c6f"
							class="font_colors01">机构号</td>
						<td width="20%" align="center" bgcolor="#c76c6f"
							class="font_colors01">户名</td>
						<td width="8%" align="center" bgcolor="#c76c6f"
							class="font_colors01">对账日期</td>
						<td width="8%" align="center" bgcolor="#c76c6f"
							class="font_colors01">验印状态</td>
						<td width="8%" align="center" bgcolor="#c76c6f"
							class="font_colors01">账单状态</td>
						<td width="8%" align="center" bgcolor="#c76c6f"
							class="font_colors01">回收日期</td>
						<td width="8%" align="center" bgcolor="#c76c6f"
							class="font_colors01">发送方式</td>
						<td width="10%" align="center" bgcolor="#c76c6f"
							class="font_colors01">操作</td>
					</tr>
					<tbody id="queryBillList">
						<s:iterator value="queryBillList" var="queryData" status="st">
							
							<tr <s:if test="#st.index%2 ==0">bgcolor="#CBD6E0"</s:if>
						<s:if test="#st.index%2 ==1">bgcolor="#FFFFFF"</s:if>>
								<td align="center"><s:property
										value="#st.count+billinfoQueryParam.firstResult" /></td>
								<td align="center"><s:property value="#queryData.voucherNo" /></td>
								<td align="center"><s:property value="#queryData.idBank" /></td>
								<td align="center"><s:property value="#queryData.accName" /></td>
								<td align="center"><s:property value="#queryData.docDate" /></td>
								<td align="center">
								<s:property value="refProveflagMap.get(#queryData.proveFlag)" />
								</td>
								<td align="center">
								<s:property value="refDocStateMap.get(#queryData.docState)" />
								</td>
								<td align="center"><s:property value="#queryData.workDate" /></td>
								<td  align="center">
									<s:property value="refSendModeMap.get(#queryData.sendMode)" />
								</td>
								<td align="center"><a href='viewBillinfoDetail.action?voucherNo=<s:property value="#queryData.voucherNo"/>'>查看详情</a></td>
							</tr>
						
						</s:iterator>
					</tbody>
				</table>	
				<div style="width:1002px; height:auto; float:left; margin-left:10px; padding:10px 0px 10px 0px;">
					<tr  class="pagelinks">
					<td colspan="3">每页显示 <select
							name="billinfoQueryParam.pageSize" id="pageSize"
							style="width:40px;" onchange="viewAccnoDetailListByPage('1')">
								<option value="10"
									<s:if test="billinfoQueryParam.pageSize==10">selected="selected"</s:if>>10</option>
								<option value="20"
									<s:if test="billinfoQueryParam.pageSize==20">selected="selected"</s:if>>20</option>
								<option value="50"
									<s:if test="billinfoQueryParam.pageSize==50">selected="selected"</s:if>>50</option>
						
						</select>条记录&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;当前显示<s:if
								test="billinfoQueryParam.total==0">0</s:if> <s:else>
								<s:property value="billinfoQueryParam.firstResult+1" />
							</s:else>到<s:property value="billinfoQueryParam.lastResult" />条，共<s:property
								value="billinfoQueryParam.total" />条</td>
						
						<td colspan="5" align="right"><s:if
								test="billinfoQueryParam.curPage==1">
										<a>首页</a>&nbsp;&nbsp;<a>上一页</a>
									</s:if> <s:else>
								<a href="#" onclick="viewAccnoDetailListByPage('1');">首页</a>&nbsp;&nbsp;<a
									href="#"
									onclick="viewAccnoDetailListByPage('${billinfoQueryParam.curPage - 1}');return false;">上一页</a>
							</s:else> <s:if test="billinfoQueryParam.curPage==billinfoQueryParam.totalPage">
										<a>下一页</a>&nbsp;&nbsp;<a>尾页</a>
									</s:if> <s:else>
								<a href="#"
									onclick="viewAccnoDetailListByPage(${billinfoQueryParam.curPage + 1});return false;">下一页</a>&nbsp;&nbsp;<a
									href="#"
									onclick="viewAccnoDetailListByPage('${billinfoQueryParam.totalPage}');return false;">尾页</a>
							</s:else>
						</td>
					</tr>
				</div>		
			</div>
		</div>
	</form>
	</div>
</body>
</html>
