<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<base href="<%=basePath%>" />
<title>操作日志查询</title>

<link href="<%=path%>/ebs/common/css/css.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/modify.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/dicss.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/process.css" rel="stylesheet" type="text/css" />
<script type="text/javascript"src="<%=path%>/ebs/common/js/jquery.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/process.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/orgTree.js"></script>
<script language="javascript" src="<%=path%>/ebs/common/js/calendar.js"></script>

<style type=text/css>
	a:hover {text-decoration: underline;}
</style>

<script type="text/javascript">
	var err = "<s:property value='errMsg'/>";
	if (err != null && err.length > 0) {
		alert("初始化查询服务出现错误");
		top.refresh();
	}
</script>

<script type="text/javascript">
	function queryOperLog() {
		if(IsDate(document.getElementById("txtstartTime")) ){
			if(IsDate(document.getElementById("txtendTime")) ){
                viewBillListByPage("1");
			}else{
				document.getElementById("txtendTime").focus();
			}
		}else{
			document.getElementById("txtstartTime").focus();
		}
	}
	
	function dealChange(){
		var operLogModule=document.getElementById("operLogModule");
		if(operLogModule.value=="0"||operLogModule.value=="1"||operLogModule.value=="2"){
			document.getElementById("voucherNo").disabled=true;
			document.getElementById("voucherNo").value="无效的条件";
			document.getElementById("accNo").disabled=false;
			document.getElementById("accNo").value="";
			document.getElementById("accName").disabled=false;
			document.getElementById("accName").value="";
		}else if(operLogModule.value=="6"||operLogModule.value=="7"||operLogModule.value=="8"||operLogModule.value=="9"){
			document.getElementById("voucherNo").disabled=true;
			document.getElementById("voucherNo").value="无效的条件";
			document.getElementById("accNo").disabled=true;
			document.getElementById("accNo").value="无效的条件";
			document.getElementById("accName").disabled=true;
			document.getElementById("accName").value="无效的条件";
		}else if(operLogModule.value=="13"){
			document.getElementById("voucherNo").disabled=true;
			document.getElementById("voucherNo").value="无效的条件";
			document.getElementById("accNo").disabled=false;
			document.getElementById("accNo").value="";
			document.getElementById("accName").disabled=true;
			document.getElementById("accName").value="无效的条件";
		}else{
			document.getElementById("voucherNo").disabled=false;
			document.getElementById("voucherNo").value="";
			document.getElementById("accNo").disabled=true;
			document.getElementById("accNo").value="无效的条件";
			document.getElementById("accName").disabled=true;
			document.getElementById("accName").value="无效的条件";
		 }
	}
	
	function initchange(){
		var operLogModule=document.getElementById("operLogModule");
		if(operLogModule.value=="0"||operLogModule.value=="1"||operLogModule.value=="2"){
			document.getElementById("voucherNo").disabled=true;
			document.getElementById("voucherNo").value="无效的条件";
		  	document.getElementById("accNo").disabled=false;
		  	document.getElementById("accName").disabled=false;
		}else if(operLogModule.value=="13"){
			document.getElementById("voucherNo").disabled=true;
			document.getElementById("voucherNo").value="无效的条件";
			document.getElementById("accNo").disabled=false;
			document.getElementById("accNo").value="";
			document.getElementById("accName").disabled=true;
			document.getElementById("accName").value="无效的条件";
		}else{
			document.getElementById("voucherNo").disabled=false;
			document.getElementById("accNo").disabled=true;
			document.getElementById("accNo").value="无效的条件";
			document.getElementById("accName").disabled=true;
		  	document.getElementById("accName").value="无效的条件";
		}
	}
	
	function viewBillListByPage(pageNumber){
		if(IsDate(document.getElementById("txtstartTime")) ){
			if(IsDate(document.getElementById("txtendTime")) ){
				$("#curPage").val(pageNumber);
				$("#operLogQueryForm").submit();
			}else{
				document.getElementById("txtendTime").focus();
			}
		}else{
			document.getElementById("txtstartTime").focus();
		}
	}
	
	function toExportData() {
		var objTable = document.getElementById("operLogTable");	
		if (objTable.rows.length>2) {
			window.location.href =  "operLogQueryAction_exportData.action";
		}else{
			alert("查询结果无数据");
		}
	}	
	function showDesc(idNo,desc){
	var string="序号"+idNo+" 操作描述:"+desc;
		document.getElementById("logDetail").innerText=string;
		document.getElementById("hiddenDesc").style.display="block";
	}
	
	function hiddenDesc(){
		document.getElementById("logDetail").innerText="";
		document.getElementById("hiddenDesc").style.display="none";
	}
	
	function submitOnclick(data){
		var cell=data.parentNode;
		var row=cell.parentNode;
		var operLogModule=document.getElementById("operLogModule");
		if(confirm("确定重新执行吗?  序号"+row.cells[0].innerHTML)){
			var param="";
			param=param+"&autoId="+row.cells[4].innerHTML;
			param=param+"&opDate="+row.cells[2].innerHTML;
			//重新发给网银对账单
			if(operLogModule.value=="11"){
			$.post("operLogQueryAction_reExportNetEbill.action", 
					param,
					function(result){
						if(result!=null&&result.length>0){
							viewBillListByPage('${operLogQueryParam.curPage}');
						}	
					});	
			}
			//重新发送未达结果
			if(operLogModule.value=="10"){
				$.post("operLogQueryAction_reExportNotMatch.action", 
						param,
						function(result){
							if(result!=null&&result.length>0){
								viewBillListByPage('${operLogQueryParam.curPage}');
							}	
						});	
			}
			//重新下载对账结果
			if(operLogModule.value=="12"){
				$.post("operLogQueryAction_loadEbillCheck.action", 
						param,
						function(result){
							if(result!=null&&result.length>0){
								viewBillListByPage('${operLogQueryParam.curPage}');
							}	
						});	
			}
		}
	}
	
	$(document).ready(function(){
		var idCenter="<s:property value='idCenter'/>";
		var idBank="<s:property value='idBank'/>";
		stopProcess();
		initTree(<%=request.getAttribute("orgTree")%>,idCenter,idBank);
		
		var operLogModule=document.getElementById("operLogModule");
		if(operLogModule.value=="6"||operLogModule.value=="7"||operLogModule.value=="8"||operLogModule.value=="9"){
			document.getElementById("operLogTable2").style.display="block";
			document.getElementById("operLogTable").style.display="none";
			document.getElementById("operLogTable3").style.display="none";
			document.getElementById("operLogTable4").style.display="none";
			
		}else if(operLogModule.value=="10"||operLogModule.value=="11"||operLogModule.value=="12"){
			document.getElementById("operLogTable3").style.display="block";
			document.getElementById("operLogTable").style.display="none";
			document.getElementById("operLogTable2").style.display="none";
			document.getElementById("operLogTable4").style.display="none";
		}else if(operLogModule.value=="13"){
			document.getElementById("operLogTable4").style.display="block";
			document.getElementById("operLogTable").style.display="none";
			document.getElementById("operLogTable2").style.display="none";
			document.getElementById("operLogTable3").style.display="none";
		}else{
			document.getElementById("operLogTable").style.display="block";
			document.getElementById("operLogTable2").style.display="none";
			document.getElementById("operLogTable3").style.display="none";
			document.getElementById("operLogTable4").style.display="none";
		}
		
		initchange();
	});
</script>
</head>
<body class="baby_in2">
	<script type="text/javascript">startProcess("正在初始化界面。。。");</script>
	<input id="moudleName" type="hidden" value="操作日志查询"/>
	<div id="nov_nr">
		<form id="operLogQueryForm"
			action="operLogQueryAction_queryOperLog.action" name="operLogQuery"
			method="post">
			<div class="nov_moon">
				<div
					style="width:98%; height:auto; float:left; margin-left:15px; padding:10px 0px 10px 0px;"
					class="border_bottom01">
					<table width="98%">
						<tr>
							<td width="20%">总 行&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<select>
									<option value=""> 华融湘江总行</option>
								</select>
							</td>

							<td width="20%">账户账号&nbsp;&nbsp; 
								<input name="operLogQueryParam.accNo" type="text" id="accNo"
									class="diinput_text01"  maxlength="32"
									value='<s:property value="operLogQueryParam.accNo"/>' />
							</td>
							<td width="20%">开始时间 
								<input name="operLogQueryParam.startTime" type="text" 
								onclick="new Calendar().show(this);"
								class="diinput_text01" id="txtstartTime" maxlength="32" title='开始时间'
								value='<s:property value="operLogQueryParam.startTime"/>' />
							</td>
							<td width="10%" align="center">
								<input name="btnfind" type="button" class="submit_but09" id="btnfind"
									onclick="queryOperLog()" value="查询" />
							</td>
						</tr>

						<tr>
							<td width="20%">分 行&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
								<select name="operLogQueryParam.idCenter" id="selectIdCenter"
									onchange="changeIdCenter()">
								</select>
							</td>
							<td width="20%">账户名称&nbsp;&nbsp;
								<input name="operLogQueryParam.accName" type="text"
									class="diinput_text01" id="accName" maxlength="32"
									value='<s:property value="operLogQueryParam.accName"/>' />
							</td>
							<td width="20%">结束时间 
								<input name="operLogQueryParam.endTime"
									type="text" class="diinput_text01" id="txtendTime" title='结束时间 ' 
									onclick="new Calendar().show(this);"maxlength="32"
								value='<s:property value="operLogQueryParam.endTime"/>' />
							</td>

							<td width="10%" align="center"><input name="btnexport"
								type="button" class="submit_but09" id="btnexport" value="导出"
								onclick="toExportData()" />
							</td>
						</tr>
						<tr>
							<td width="20%">网 点&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<select name="operLogQueryParam.idBank" id="selectIdBank"
								onchange="changeIdBank()">
							</select></td>
							<td width="20%">操作员号&nbsp;&nbsp;
								<input name="operLogQueryParam.opCode" type="text"
								class="diinput_text01" maxlength="32"
								value='<s:property value="operLogQueryParam.opCode"/>' />
							</td>
							<td width="20%">机  构 号  
								<input name="operLogQueryParam.idBank1" type="text"
									class="diinput_text01" id="idBank" maxlength="12"
									value='<s:property value="operLogQueryParam.idBank1"/>' />
								<input type="hidden" id="curPage" name="operLogQueryParam.curPage"
									value='<s:property value="operLogQueryParam.curPage"/>' />
							</td>
						</tr>
						<tr>
							<td width="20%">账单编号 
								<input name="operLogQueryParam.voucherNo" type="text"
									class="diinput_text01" maxlength="32" id="voucherNo"
									value='<s:property value="operLogQueryParam.voucherNo"/>' /></td>
							<td width="20%">模块名称 &nbsp;
								<s:select list="operLogModuleMap" listKey="key" listValue="value" headerKey="" 
										value="operLogQueryParam.operLogModule" onchange="dealChange()"
										name="operLogQueryParam.operLogModule" id="operLogModule">
									</s:select>
							</td>
							<td width="20%">操作描述 
								<input name="operLogQueryParam.opDesc" type="text"
									class="diinput_text01" maxlength="32" id="voucherNo"
									value='<s:property value="operLogQueryParam.opDesc"/>' /></td>
						</tr>
					</table>
				</div>
				<div
					style="width:98%; height:auto; float:left; margin-left:10px; padding:10px 0px 10px 0px; color: #000000;">
					<table width="99%">
						<tr>
							<td width="96%" id="logDetail"></td>
							<td width="4%" align="right" valign="bottom"><a href="#" style="display:none" id="hiddenDesc" onclick="hiddenDesc();return false;">隐藏</a></td>
						</tr>
					</table>
					<table width="100%" border="0" cellpadding="0" cellspacing="0"
						class="myTable" id="operLogTable">

						<tr>
							<td width="3%" height="26" align="center" bgcolor="#c76c6f"
								class="font_colors01">序号</td>
							<td width="7%" align="center" bgcolor="#c76c6f"
								class="font_colors01">分行号</td>
							<td width="7%" align="center" bgcolor="#c76c6f"
								class="font_colors01">支行号</td>
							<td width="7%" align="center" bgcolor="#c76c6f"
								class="font_colors01">机构号</td>
							<td width="10%" align="center" bgcolor="#c76c6f"
								class="font_colors01">模块名称</td>
							<td width="10%" align="center" bgcolor="#c76c6f"
								class="font_colors01">操作日期</td>
							<td width="10%" align="center" bgcolor="#c76c6f"
								class="font_colors01">操作员号</td>
							<td width="12%" align="center" bgcolor="#c76c6f"
								class="font_colors01">账单编号</td>
							<td width="12%" align="center" bgcolor="#c76c6f"
								class="font_colors01">账户账号</td>
							<td width="15%" align="center" bgcolor="#c76c6f"
								class="font_colors01">户名</td>
							<td width="6%" align="center" bgcolor="#c76c6f"
								class="font_colors01">操作描述</td>
						</tr>
						<tbody id="queryList">
							<s:iterator value="queryList" var="queryData" status="st">
								<tr <s:if test="#st.index%2 ==0">bgcolor="#CBD6E0"</s:if>
									<s:if test="#st.index%2 ==1">bgcolor="#FFFFFF"</s:if>>
									<td align="center"><s:property
											value="#st.count+operLogQueryParam.firstResult" /></td>
									<td align="center"><s:property value="#queryData.idCenter" /></td>
									<td align="center"><s:property value="#queryData.idBranch" />
									</td>
									<td align="center"><s:property value="#queryData.idBank" />
									</td>
									<td align="center"><s:property
											value="operLogModuleMap.get(#queryData.opMode+'')" />
									</td>
									<td align="center"><s:property value="#queryData.opDate" />
									</td>
									<td align="center"><s:property value="#queryData.opCode" />
									</td>
									<td align="center"><s:property
											value="#queryData.voucherNo" /></td>
									<td align="center"><s:property value="#queryData.accNo" />
									</td>
									<td align="center"><s:property value="#queryData.accName" />
									</td>
									<td align="center"><a href="#" onclick="showDesc('<s:property value="#st.count+operLogQueryParam.firstResult" />','<s:property value="#queryData.opDesc" />'); return false;" >描述</a>
									</td>
								</tr>
							</s:iterator>
						</tbody>
					</table>
					
					<table width="100%" border="0" cellpadding="0" cellspacing="0"
						class="myTable" id="operLogTable2">

						<tr>
							<td width="3%" height="26" align="center" bgcolor="#c76c6f"
								class="font_colors01">序号</td>
							<td width="10%" align="center" bgcolor="#c76c6f"
								class="font_colors01">模块名称</td>
							<td width="10%" align="center" bgcolor="#c76c6f"
								class="font_colors01">操作日期</td>
							<td width="77%" align="center" bgcolor="#c76c6f"
								class="font_colors01">操作描述</td>
						</tr>
						<tbody id="queryList">
							<s:iterator value="queryList" var="queryData" status="st">
								<tr <s:if test="#st.index%2 ==0">bgcolor="#CBD6E0"</s:if>
									<s:if test="#st.index%2 ==1">bgcolor="#FFFFFF"</s:if>>
									<td align="center"><s:property
											value="#st.count+operLogQueryParam.firstResult" /></td>
									<td align="center"><s:property
											value="operLogModuleMap.get(#queryData.opMode+'')" />
									</td>
									<td align="center"><s:property value="#queryData.opDate" />
									</td>
									<td align="left"> <s:property value="#queryData.opDesc" /></td>
								</tr>
							</s:iterator>
						</tbody>
					</table>
					
					<table width="100%" border="0" cellpadding="0" cellspacing="0"
						class="myTable" id="operLogTable3">

						<tr>
							<td width="3%" height="26" align="center" bgcolor="#c76c6f"
								class="font_colors01">序号</td>
							<td width="10%" align="center" bgcolor="#c76c6f"
								class="font_colors01">模块名称</td>
							<td width="10%" align="center" bgcolor="#c76c6f"
								class="font_colors01">操作日期</td>
							<td width="60%" align="center" bgcolor="#c76c6f"
								class="font_colors01">操作描述</td>
							<td width="10%" align="center" bgcolor="#c76c6f"
								class="font_colors01" style="display:none">主键</td>
							<td width="7%" align="center" bgcolor="#c76c6f"
								class="font_colors01">处理方式</td>
						</tr>
						<tbody id="queryList">
							<s:iterator value="queryList" var="queryData" status="st">
								<tr <s:if test="#st.index%2 ==0">bgcolor="#CBD6E0"</s:if>
									<s:if test="#st.index%2 ==1">bgcolor="#FFFFFF"</s:if>>
									<td align="center"><s:property
											value="#st.count+operLogQueryParam.firstResult" /></td>
									<td align="center"><s:property 
											value="operLogModuleMap.get(#queryData.opMode+'')" />
									</td>
									<td align="center"><s:property  value="#queryData.opDate" />
									</td>
									<td align="left"> <s:property  value="#queryData.opDesc" /></td>
									<td align="left" style="display:none" > <s:property  value="#queryData.autoId" /></td>
									<td align="center"> <a onclick="submitOnclick(this)" style="color:#33F">重新执行 </a>
									</td>
								</tr>
							</s:iterator>
						</tbody>
					</table>
					
					<table width="100%" border="0" cellpadding="0" cellspacing="0"
						class="myTable" id="operLogTable4">

						<tr>
							<td width="3%" height="26" align="center" bgcolor="#c76c6f"
								class="font_colors01">序号</td>
							<td width="10%" align="center" bgcolor="#c76c6f"
								class="font_colors01">模块名称</td>
							<td width="20%" align="center" bgcolor="#c76c6f"
								class="font_colors01">账号</td>	
							<td width="10%" align="center" bgcolor="#c76c6f"
								class="font_colors01">操作日期</td>
							<td width="50%" align="center" bgcolor="#c76c6f"
								class="font_colors01">操作描述</td>
							<td width="7%" align="center" bgcolor="#c76c6f"
								class="font_colors01" style="display:none">主键</td>
						</tr>
						<tbody id="queryList">
							<s:iterator value="queryList" var="queryData" status="st">
								<tr <s:if test="#st.index%2 ==0">bgcolor="#CBD6E0"</s:if>
									<s:if test="#st.index%2 ==1">bgcolor="#FFFFFF"</s:if>>
									<td align="center"><s:property value="#st.count+operLogQueryParam.firstResult" />
									</td>
									<td align="center"><s:property value="operLogModuleMap.get(#queryData.opMode+'')" />
									</td>
									<td align="center"><s:property value="#queryData.accNo" />
									</td>
									<td align="center"><s:property value="#queryData.opDate" />
									</td>
									<td align="center"> <s:property value="#queryData.opDesc" />
									</td>
									<td align="left" style="display:none" > <s:property  value="#queryData.autoId" />
									</td>
								</tr>
							</s:iterator>
						</tbody>
					</table>
					
					
						<div id="basicinfoListPageInfo" style="width:1002px; height:auto; float:left; margin-left:10px; padding:10px 0px 10px 0px;" >
							<tr class="pagelinks">
								<td colspan="7" align="left">每页显示 <select
									onchange="viewBillListByPage('1')"
									name="operLogQueryParam.pageSize" id="pageSize"
									style="width:60px;">
										<option value="10"
											<s:if test="operLogQueryParam.pageSize==10">selected="selected"</s:if>>10</option>
										<option value="20"
											<s:if test="operLogQueryParam.pageSize==20">selected="selected"</s:if>>20</option>
										<option value="50"
											<s:if test="operLogQueryParam.pageSize==50">selected="selected"</s:if>>50</option>
								</select>条记录&nbsp当前显示<s:if test="operLogQueryParam.total==0">0</s:if> <s:else>
										<s:property value="operLogQueryParam.firstResult+1" />
									</s:else>到<s:property value="operLogQueryParam.lastResult" />条，共<s:property
										value="operLogQueryParam.total" />条</td>
								<td colspan="6" align="right"><s:if
										test="operLogQueryParam.curPage==1">
										<a>首页</a>&nbsp;&nbsp;<a>上一页</a>
									</s:if> <s:else>
										<a href="#" onclick="viewBillListByPage('1');return false;">首页</a>&nbsp;&nbsp;
									<a href="#"
											onclick="viewBillListByPage('${operLogQueryParam.curPage - 1}');return false;">上一页</a>
									</s:else> <s:if
										test="operLogQueryParam.curPage==operLogQueryParam.totalPage">
										<a>下一页</a>&nbsp;&nbsp;<a>尾页</a>
									</s:if> <s:else>
										<a href="#"
											onclick="viewBillListByPage(${operLogQueryParam.curPage + 1});return false;">下一页</a>&nbsp;&nbsp;<a
											href="#"
											onclick="viewBillListByPage('${operLogQueryParam.totalPage}');return false;">尾页</a>
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
