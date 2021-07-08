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
<title>账户对账状态维护</title>
<link href="<%=path%>/ebs/common/css/css.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/dicss.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/process.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/modify.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=path%>/ebs/common/js/jquery.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/orgTree.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/ischeckmodify.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/process.js"></script>

<script type="text/javascript">
	var err = "<s:property value='errMsg'/>";
	if (err != null && err.length > 0) {
		alert("初始化服务出现错误"); 
		top.refresh();
	}
</script>
<script type="text/javascript">
	function queryAcountData() {
        viewBillListByPage('${acountQueryParam.curPage}');
	}
	
	/** 分页查询*/
	function viewBillListByPage(pageNumber){
		$("#curPage").val(pageNumber);
		$("#accModifyForm").submit();
	}
	
	function toExportData() {
		var objTable = document.getElementById("basicInfoTable");	
		if (objTable.rows.length>2) {
			window.location.href = "ischeckModifyAction_exportData.action";
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
<style>
#son_div {
  position: absolute;
  background:url('/windforce/ebs/common/images/bg_content.jpg');
  width:700px;
  height:270px;
  margin-left:-50px;
  margin-top:-25px;
}
</style>
</head>
<body class="baby_in2">
	<script type="text/javascript">startProcess("正在初始化界面。。。");</script>
	<input id="moudleName" type="hidden" value="账户对账状态维护"/>
	<div id="son_div" class="son_div">
		<div class="sonpage" id="sonpage">
			<center>
			<tr>
				<td style="vertical-align: top;" colspan="2">信息修改</td>
			</tr>
			</center> 
		</div>
		<jsp:include page="/ebs/ischeckmodify/isCheckModify_div.jsp" />
	</div>
	
	<div id="nov_nr" class="nov_nr">
		<form id="accModifyForm"
					action="ischeckModifyAction_queryAccData.action" name="accModify"
					method="post">
		<div class="nov_moon">
			<div
				style="width:98%; height:auto; float:left; margin-left:10px; padding:10px 0px 10px 0px;"
				class="border_bottom01">
					<table width="98%">
						<tr>
							<td width="20%">总 行&nbsp;&nbsp;&nbsp; 
								<select>
									<option value=""> 华融湘江总行</option>
								</select>
							</td>
							<td width="20%">客  户 号 &nbsp; 
								<input name="acountQueryParam.custId" type="text"
									class="diinput_text01" id="txtcustid" maxlength="32" 
									value='<s:property value="acountQueryParam.custId"/>' /> 	
								<input type="hidden" id="curPage" name="acountQueryParam.curPage"
									value='<s:property value="acountQueryParam.curPage"/>' />
							</td>
							<td width="20%">账户账号&nbsp;&nbsp; 
								<input name="acountQueryParam.accNo" type="text" 
								class="diinput_text01" id="txtaccno" maxlength="32"
								value='<s:property value="acountQueryParam.accNo"/>' />
							</td>
							<td width="10%" rowspan="2" align="center">
								<input name="btnfind" type="button" class="submit_but09" id="btnfind"
									onclick="viewBillListByPage('1')" value="查询" />
							</td>
						</tr>
						<tr>
							<td width="20%">分 行&nbsp;&nbsp;&nbsp; 
								<select name="acountQueryParam.idCenter" id="selectIdCenter" 
									onchange="changeIdCenter()">
								</select>
							</td>
							<td width="20%">机  构 号 &nbsp; 
								<input name="acountQueryParam.idBank" type="text"
									class="diinput_text01" id="idBank" maxlength="12"
									value='<s:property value="acountQueryParam.idBank"/>' />
							</td>
							<td width="20%">账户名称&nbsp;&nbsp; 
								<input name="acountQueryParam.accName" type="text"
									class="diinput_text01" id="accName" maxlength="128"
									value='<s:property value="acountQueryParam.accName"/>' />
							</td>
						</tr>
						<tr>
							<td width="20%">网 点&nbsp;&nbsp;&nbsp; 
								<select  id="selectIdBank"
									 onchange="changeIdBank()">
								</select>
							</td>
							<td width="20%">是否对账&nbsp;&nbsp; 									
								<s:select list="refIsCheck" listKey="key"
									listValue="value" headerKey="" headerValue="--请选择--"
									value="acountQueryParam.isCheck" name="acountQueryParam.isCheck" id="isCheck" >
								</s:select>
							</td>
							<td width="20%">账户类型&nbsp;&nbsp; 
								<s:select list="refAccCycleMap" listKey="key"
									listValue="value" headerKey="" headerValue="--请选择--"
									value="acountQueryParam.accCycle" name="acountQueryParam.accCycle" id="acccycle" >
								</s:select>
							</td>
							<td width="10%" align="center">
								
							</td>
						</tr>
					</table>
			</div>
			<div style="width:100%; height:auto; float:left; margin-left:10px; padding:10px 0px 10px 0px; color: #000000;">
				<table width="98%">
				<tr>
					<td>
					<h1>账户信息列表</h1>
					</td>
					<td>
					</td>	
				</tr>
				</table>
				<table width="98%"  cellpadding="0" cellspacing="0"
					class="myTable" id="basicInfoTable">
					<tr>
						<td width="3%" height="26" align="center" bgcolor="#c76c6f"
							class="font_colors01">序号</td>
						<td width="8%" align="center" bgcolor="#c76c6f"
							class="font_colors01">账号</td>
						<td width="4%" align="center" bgcolor="#c76c6f"
							class="font_colors01">机构号</td>
						<td width="10%" align="center" bgcolor="#c76c6f"
							class="font_colors01">户名</td>
						<td width="8%" align="center" bgcolor="#c76c6f"
							class="font_colors01">邮寄地址</td>
						<td width="4%" align="center" bgcolor="#c76c6f"
							class="font_colors01">邮编</td>
						<td width="5%" align="center" bgcolor="#c76c6f"
							class="font_colors01">联系人</td>
						<td width="8%" align="center" bgcolor="#c76c6f"
							class="font_colors01">联系电话</td>
						<td width="4%" align="center" bgcolor="#c76c6f"
							class="font_colors01">客户号</td>
						<td width="8%" align="center" bgcolor="#c76c6f"
							class="font_colors01">验印账号</td>
						<td width="6%" align="center" bgcolor="#c76c6f"
							class="font_colors01">验印模式</td>
						<td width="5%" align="center" bgcolor="#c76c6f"
							class="font_colors01">发送方式</td>
						<td width="5%" align="center" bgcolor="#c76c6f"
							class="font_colors01">账户类型</td>
						<td width="5%" align="center" bgcolor="#c76c6f"
							class="font_colors01">是否对账</td>
						<td width="10%" align="center" bgcolor="#c76c6f"
							class="font_colors01">维护原因</td>
						<td width="5%" align="center" bgcolor="#c76c6f"
							class="font_colors01">操作</td>
					</tr>
					<tbody id="queryList">
						<s:iterator value="queryList" var="queryData" status="st">
							<tr  <s:if test="#st.index%2 ==0">bgcolor="#CBD6E0"</s:if>
						<s:if test="#st.index%2 ==1">bgcolor="#FFFFFF"</s:if>>
								<td align="center" style="border:1px solid #C76C6F"><s:property
										value="#st.count+acountQueryParam.firstResult" />
								</td>
								<td  align="center" style="border:1px solid #C76C6F"><s:property value="#queryData.accNo" />
								</td>
								<td  align="center" style="border:1px solid #C76C6F"><s:property value="#queryData.idBank" />
								</td>
								<td  align="center" style="border:1px solid #C76C6F"><s:property value="#queryData.accName" />
								</td>
								<td  align="center" style="border:1px solid #C76C6F"><s:property value="#queryData.sendAddress" />
								</td>
								<td  align="center" style="border:1px solid #C76C6F"><s:property value="#queryData.zip" />
								</td>
								<td  align="center" style="border:1px solid #C76C6F"><s:property value="#queryData.linkMan" />
								</td>
								<td  align="center" style="border:1px solid #C76C6F"><s:property value="#queryData.phone" />
								</td>
								<td  align="center" style="border:1px solid #C76C6F"><s:property value="#queryData.custId" /></td>
								<td  align="center" style="border:1px solid #C76C6F"><s:property value="#queryData.sealAccNo" /></td>
								<td  align="center" style="border:1px solid #C76C6F"><s:property value="sealModeMap.get(#queryData.sealMode)" />
								</td>
								<td  align="center" style="border:1px solid #C76C6F"><s:property value="sendTypeMap.get(#queryData.sendMode)" />
								</td>		
								<td  align="center" style="border:1px solid #C76C6F"><s:property value="refAccCycleMap.get(#queryData.accCycle)" />
								</td>
								<td  align="center" style="border:1px solid #C76C6F"><s:property value="refIsCheck.get(#queryData.isCheck)" />
								</td>
								<td  align="center" style="border:1px solid #C76C6F"><s:property value="#queryData.remark" />
								</td>						
								<td align="center" style="border:1px solid #C76C6F">
								<a onclick="modifyAcc(this)" style="color:#33F">编辑 </a>
								</td>
							</tr>
						</s:iterator>
					</tbody>
					<tfoot id="basicinfoListPageInfo">
					<tr class="pagelinks">
							<td colspan="7" align="left">每页显示 <select 	onchange="viewBillListByPage('1')"
								name="acountQueryParam.pageSize" id="pageSize"
								style="width:60px;">
									<option value="10"
										<s:if test="acountQueryParam.pageSize==10">selected="selected"</s:if>>10</option>
									<option value="20"
										<s:if test="acountQueryParam.pageSize==20">selected="selected"</s:if>>20</option>
									<option value="50"
										<s:if test="acountQueryParam.pageSize==50">selected="selected"</s:if>>50</option>
							</select>条记录&nbsp当前显示<s:if test="acountQueryParam.total==0">0</s:if>
								<s:else>
									<s:property value="acountQueryParam.firstResult+1" />
								</s:else>到<s:property value="acountQueryParam.lastResult" />条，共<s:property
									value="acountQueryParam.total" />条</td>
							<td colspan="8" align="right">
							<s:if test="acountQueryParam.curPage==1">
										<a>首页</a>&nbsp;&nbsp;<a>上一页</a>
							</s:if> <s:else>
									<a href="#" onclick="viewBillListByPage('1');return false;">首页</a>&nbsp;&nbsp;
									<a href="#"
										onclick="viewBillListByPage('${acountQueryParam.curPage - 1}');return false;">上一页</a>
								</s:else> <s:if
									test="acountQueryParam.curPage==acountQueryParam.totalPage">
										<a>下一页</a>&nbsp;&nbsp;<a>尾页</a>
									</s:if> <s:else>
									<a href="#"
										onclick="viewBillListByPage(${acountQueryParam.curPage + 1});return false;">下一页</a>&nbsp;&nbsp;<a
										href="#"
										onclick="viewBillListByPage('${acountQueryParam.totalPage}');return false;">尾页</a>
								</s:else></td>
						</tr>
					</tfoot>
				</table>
			</div>
		</div>
		</form>
	</div>
</body>
</html>
