<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<link href="<%=path%>/ebs/common/css/css.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/dicss.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/process.css" rel="stylesheet" type="text/css" />


<script type="text/javascript" src="<%=path%>/ebs/common/js/orgTree.js"></script>
<script type="text/javascript" src="<%=path%>/common/js/jquery/jquery-1.7.1.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/process.js"></script>
<script language="javascript" src="<%=path%>/ebs/common/js/calendar.js"></script>
<meta http-equiv="Content-Type" content="text/html" />
<script type="text/javascript">
	if("<s:property value='errMsg'/>" != null && "<s:property value='errMsg'/>" != "")
	{
		alert('${errMsg}');
	}
</script>

<script language="javascript" type="text/javascript">
	$(document).ready(function(){
		var idCenter="<s:property value='idCenter'/>";
		var idBank="<s:property value='idBank'/>";
		initTree(<%=request.getAttribute("orgTree")%>,idCenter,idBank);
		var ocx=document.getElementById("BatchPrinOcx");
		stopProcess();
		ocx.Init("BatchPrint.xml");
	});
</script>

<script type="text/javascript">
	function queryAccnoMainDataList(){
		if(IsDate(document.getElementById("docdate"))){
			viewCheckMainDataListByPage("1");
		}
	}
	function viewCheckMainDataListByPage(pageNumber){
		if(document.getElementById("docdate").value.length == 0){
			alert("对账日期不能为空！");
			document.getElementById("docdate").focus();
		}else{
			//对账日期不是最后一天，出临时账单，账号必须输入
//			if(!isLastDay(document.getElementById("docdate").value)){
//				var accNo = document.getElementById("accNo").value;
//				var date = document.getElementById("docdate").value;
//				var para = "accNo="+accNo+"&date="+date;
//				// 判断是否有生成记录,如果已经生成过则不再生成
//				$.post("BatchPrintAction_isCreatedMainData.action", para, function(data) {
//				  if(data == "fail"){
//				 		//alert("此账单已经生成过");
//				  		$("#curPage").val(pageNumber);
//						$("#flag").val("createBill");
//						$("#checkMainDataQueryForm").submit();
//				  } else if("success" == data){
//					if(confirm("对账日期不是最后一天，是否要出临时账单？")){
//						if(accNo.length == 0){
//							alert("账号不能为空！");
//							document.getElementById("accNo").focus();
//						}else{
//							$("#curPage").val(pageNumber);
//							$("#flag").val("createTempBill");
//							//$("#checkMainDataQueryForm").submit();
//							getTableList();
//						}
//					}else{
//						$("#curPage").val(pageNumber);
//						$("#flag").val("createBill");
//						//$("#checkMainDataQueryForm").submit();
//						getTableList();
//					}
//				  }else{
//						alert(data);
//				  }
//			  });
//			} else{
				$("#curPage").val(pageNumber);
				$("#flag").val("createBill");
				$("#checkMainDataQueryForm").submit();
				//getTableList();
//			}
		}
	}
	
	function getTableList(){
		$.post(
			"BatchPrintAction_queryBillinfoData.action",
			$("form").serialize(),
			function(data){
				$("#tableList").html(data);
			}
		);
	}
	
	
	function isLastDay(inputDate){
		var d = new Date(inputDate.replace(/\-/,"/"));
		var nd = new Date(d.getTime()+24*60*60*1000);  //next day
 		return (d.getMonth()!=nd.getMonth());
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
	


function batchPrintBillData(){
	var objTable = document.getElementById("checkMainDataInfoTable");
	var param="<s:property value='accnoMainDataQueryParam'/>";	
	var ocx=document.getElementById("BatchPrinOcx");
	if (objTable.rows.length>2) {
		if(ocx.isPrint==false){
			$.post("BatchPrintAction_printData.action", param, function(data) {
				if(data==""){
					alert("打印完成，请重新查询打印数据以便继续打印");
					return;
				}else{
					ocx.CreateBill(data);
					window.setTimeout(batchPrintBillData(), "1000");
				}
			});
		}else{
			window.setTimeout(batchPrintBillData(), "1000");
		}			
	}else{
		alert("查询结果无数据");
	}
}





</script>
</head>

<body class="baby_in2">
	<input id="moudleName" type="hidden" value="账单打印" />
	<form id="checkMainDataQueryForm" method="post"
		action="BatchPrintAction_queryBillinfoData.action" name="checkMainDataQuery">
		<div class="nov_moon">
			<div
				style="width:98%; height:auto; float:left; margin-left:10px; padding:10px 0px 10px 0px;"
				class="border_bottom01">

				<table width="100%">
					<tr>
						<td width="30%">
							对账中心&nbsp;&nbsp;
							<select name="accnoMainDataQueryParam.idCenter" id="selectIdCenter"
								class="input_text01" style="width:160px" onchange="changeIdCenter()">
							</select>
							<input name="flag" type="hidden" id="flag"></input>
						</td>

						<td width="60%" colspan="2">
							验印状态 &nbsp;
							<s:select list="refProveflagMap" class="input_text01" listKey="key"
								listValue="value" headerKey="" headerValue="--请选择--"
								style="color:#333333; font-size:13px; width:160px;"
								name="accnoMainDataQueryParam.proveFlag" id="proveFlag"
								value="accnoMainDataQueryParam.proveFlag">
							</s:select>
						</td>
						<td width="10%" rowspan="2" align="center">
							<input name=queryData type="button" class="submit_but09" id="find"
								value="查询" onclick="queryAccnoMainDataList()" />
						</td>
					</tr>
					<tr>
						<td width="30%">
							支&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 行&nbsp;&nbsp;&nbsp;
							<select name="accnoMainDataQueryParam.idBranch" id="selectIdBranch"
								class="input_text01" style="width:160px" onchange="changeIdBranch()">
							</select>
						</td>
						<td width="30%">
							机构号&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<input name="accnoMainDataQueryParam.idBank1" id="idBank"
								class="diinput_text01" style="width:160px"
								value='<s:property value="accnoMainDataQueryParam.idBank1"/>' />
						</td>
						<td width="30%">
							对账编号&nbsp;&nbsp;&nbsp;
							<input name="accnoMainDataQueryParam.voucherNo" type="text"
								class="diinput_text01" id="txtvoucherno" style="width:160px"
								value='<s:property value="accnoMainDataQueryParam.voucherNo"/>' />
						</td>
					</tr>
					<tr>
						<td width="30%">
							网 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;点&nbsp;&nbsp;&nbsp;
							<select name="accnoMainDataQueryParam.idBank" id="selectIdBank"
								class="input_text01" style="width:160px" onchange="changeIdBank()">
							</select>
						</td>
						<td width="30%">
							对账日期&nbsp;&nbsp;
							<input name="accnoMainDataQueryParam.docDate" type="text" title="对账日期"
								class="diinput_text01" style="width:160px" id="docdate"
								onclick="new Calendar().show(this);"
								value='<s:property value="accnoMainDataQueryParam.docDate"/>' />
							<input type="hidden" id="curPage" name="accnoMainDataQueryParam.curPage"
								value='<s:property value="accnoMainDataQueryParam.curPage"/>' />

						</td>
						<td width="30%">
							账单状态 &nbsp;&nbsp;
							<s:select list="refDocstateMap" class="input_text01" listKey="key"
								listValue="value" headerKey="" headerValue="--请选择--"
								style="color:#333333; font-size:13px; width:160px;"
								name="accnoMainDataQueryParam.docstateFlag" id="docstateFlag"
								value="accnoMainDataQueryParam.docstateFlag">
							</s:select>
						</td>
						<td width="10%" rowspan="2" align="center">
							<input name="btnprint" type="button" class="submit_but09" id="export"
								value="批量打印" onclick="batchPrintBillData()" />
						</td>
					</tr>
					<tr>
						<td width="30%">
							<div align="left">
								账&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号&nbsp;&nbsp;&nbsp;&nbsp;
								<input name="accnoMainDataQueryParam.accNo" type="text"
									class="input_text01" style="width:160px" id="accNo"
									value='<s:property value="accnoMainDataQueryParam.accNo"/>' />
							</div>
						</td>
						<td width="30%">
							<div align="left">
								客户号&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<input name="accnoMainDataQueryParam.custId" type="text"
									class="diinput_text01" style="width:160px" id="custId"
									value='<s:property value="accnoMainDataQueryParam.custId"/>' />
							</div>
						</td>

						<td width="30%">
							<div align="left">
								对账渠道&nbsp;&nbsp;&nbsp;
								<s:select list="refSendMode" class="input_text01" listKey="key"
									listValue="value" headerKey="" headerValue="--请选择--"
									style="color:#333333; font-size:13px; width:160px;"
									name="accnoMainDataQueryParam.sendMode" id="sendMode"
									value="accnoMainDataQueryParam.sendMode">
								</s:select>
							</div>
						</td>
					</tr>
				</table>
			</div>
			<div
				style="width:100%; height:auto; float:left; margin-left:10px; padding:10px 0px 10px 0px; color: #000000;">
				<div style="position:relative;">
					<h1>查询结果列表</h1>
					<div style="position:absolute;top:-5px;left:760px">
						<object name="BatchPrinOcx" id="BatchPrinOcx"
							codebase="ebs/edata/AutoPrint.cab#Version=1,0,0,2"
							classid="clsid:ADE0CDE6-3A23-4600-949A-A65EA9145764" width="320"
							height="20"> </object>
					</div>
				</div>
				<div id="tableList">
					<table width="98%" border="0" cellpadding="0" cellspacing="0"
						id="checkMainDataInfoTable">
						<tr>
							<td width="3%" align="center" bgcolor="#c76c6f" class="font_colors01" height="26">序号</td>
							<td width="12%" align="center" bgcolor="#c76c6f" class="font_colors01">账单编号</td>
							<td width="7%" align="center" bgcolor="#c76c6f" class="font_colors01">机构号</td>
							<td width="9%" align="center" bgcolor="#c76c6f" class="font_colors01">机构名称</td>
							<td width="13%" align="center" bgcolor="#c76c6f" class="font_colors01">户名</td>
							<td width="7%" align="center" bgcolor="#c76c6f" class="font_colors01">客户号</td>
							<td width="7%" align="center" bgcolor="#c76c6f" class="font_colors01">联系人</td>
							<td width="6%" align="center" bgcolor="#c76c6f" class="font_colors01">联系地址</td>
							<td width="10%" align="center" bgcolor="#c76c6f" class="font_colors01">联系电话</td>
							<td width="7%" align="center" bgcolor="#c76c6f" class="font_colors01">账单状态</td>
							<td width="9%" align="center" bgcolor="#c76c6f" class="font_colors01">打印次数</td>
						</tr>
						<tbody id="queryList" align="center">
							<s:iterator value="queryBillList" var="queryData" status="st">
								<tr bgcolor='<s:if test="#st.count%2==0">#F4DDDF</s:if>'>
									<td>
										<s:property value="#st.count+accnoMainDataQueryParam.firstResult" />
									</td>
									<td>
										<s:property value="#queryData.voucherNo" />
									</td>
									<td>
										<s:property value="#queryData.idCenter" />
									</td>
									<td>
										<s:property value="#queryData.bankName" />
									</td>
									<td>
										<s:property value="#queryData.accName" />
									</td>
									<td>
										<s:property value="#queryData.custId" />
									</td>
									<td>
										<s:property value="#queryData.linkMan" />
									</td>
									<td>
										<s:property value="#queryData.address" />
									</td>
									<td>
										<s:property value="#queryData.phone" />
									</td>
									<td align="center">
										<s:property value="refDocstateMap.get(#queryData.docState)" />
									</td>
									<td>
										<s:property value="#queryData.printTimes" />
									</td>
								</tr>
							</s:iterator>
						</tbody>
						<tfoot id="accInfoTable">
							<tr class="pagelinks">
								<td colspan="6">
									每页显示
									<select name="accnoMainDataQueryParam.pageSize" id="pageSize"
										style="width:40px;" onchange="viewCheckMainDataListByPage('1')">
										<option value="20"
											<s:if test="accnoMainDataQueryParam.pageSize==20">selected="selected"</s:if>>20</option>
									</select>
									条记录&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;当前显示
									<s:if test="accnoMainDataQueryParam.total==0">0</s:if>
									<s:else>
										<s:property value="accnoMainDataQueryParam.firstResult+1" />
									</s:else>
									到
									<s:property value="accnoMainDataQueryParam.lastResult" />
									条，共
									<s:property value="accnoMainDataQueryParam.total" />
									条
								</td>
								<td colspan="8" align="right">
									<s:if test="accnoMainDataQueryParam.curPage==1">
										<a>首页</a>&nbsp;&nbsp;<a>上一页</a>
									</s:if>
									<s:else>
										<a href="#" onclick="viewCheckMainDataListByPage('1');">首页</a>&nbsp;&nbsp;
									<a href="#"
											onclick="viewCheckMainDataListByPage('${accnoMainDataQueryParam.curPage - 1}');return false;">上一页</a>
									</s:else>
									<s:if
										test="accnoMainDataQueryParam.curPage==accnoMainDataQueryParam.totalPage">
										<a>下一页</a>&nbsp;&nbsp;<a>尾页</a>
									</s:if>
									<s:else>
										<a href="#"
											onclick="viewCheckMainDataListByPage(${accnoMainDataQueryParam.curPage + 1});return false;">下一页</a>&nbsp;&nbsp;<a
											href="#"
											onclick="viewCheckMainDataListByPage('${accnoMainDataQueryParam.totalPage}');return false;">尾页</a>
									</s:else>
								</td>
							</tr>
						</tfoot>
					</table>
				</div>
			</div>
		</div>
	</form>
	<!-- <iframe id="tempiframe" ></iframe> -->
</body>
</html>
