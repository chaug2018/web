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
<title>账户签约登记</title>
<link href="<%=path%>/ebs/common/css/css.css"rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/dicss.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/process.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/modify.css" rel="stylesheet" type="text/css" />
<style>
#son_div {
	position: absolute;
	width: 700px;
	height: 230px;
	margin-left: 50px;
	margin-top: 30px;
	background:url('/windforce/ebs/common/images/bg_content.jpg');
}
</style>
<script type="text/javascript" src="<%=path%>/ebs/common/js/jquery.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/orgTree.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/accoper.js"></script>
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
		$("#curPage").val(1);
		$("#accModifyForm").submit();
	}
	
	/** 票据查询之分页查询*/
	function viewBillListByPage(pageNumber){
		$("#curPage").val(pageNumber);
		$("#accModifyForm").submit();
	}
	
	function toExportData() {
		var objTable = document.getElementById("basicInfoTable");	
		if (objTable.rows.length>2) {
			window.location.href = "accSignAction_exportData.action";
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
<script type="text/javascript">startProcess("正在初始化界面。。。");</script>
<input id="moudleName" type="hidden" value="账户签约登记"/>

<div id="son_div" class="son_div">
	<div class="sonpage" id="sonpage">
	<center>
		<tr>
			<td style="vertical-align: top;" colspan="2">账户签约</td>
		</tr>
	</center>
	 </div>
	<jsp:include page="/ebs/accoper/accsign_div.jsp" />
    
 </div>
	<div id="nov_nr" class="nov_nr">
		<form id="accModifyForm" action="accSignAction_queryAccData.action" name="accModify"
				method="post">
		<div class="nov_moon">
			<div style="width:98%; height:auto; float:left; margin-left:10px; padding:10px 0px 10px 0px;"
				class="border_bottom01">		
					<table width="98%" >
						<tr>
							<td width="20%">总&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;行&nbsp;&nbsp;&nbsp; 
								<select>
									<option value="">华融湘江总行</option>
								</select>
							</td>
							<td width="20%">合  同     号&nbsp;&nbsp; 
								<input name="acountQueryParam.contractNo" type="text"
									class="diinput_text01" id="txtcontactno" maxlength="32"
									value='<s:property value="acountQueryParam.contractNo"/>' /> 
								<input type="hidden" id="curPage" name="acountQueryParam.curPage"
									value='<s:property value="acountQueryParam.curPage"/>' />
							</td>
							<td width="20%">账户账号&nbsp;&nbsp; 
								<input name="acountQueryParam.accNo" type="text" class="diinput_text01"
								id="txtaccno" maxlength="32" value='<s:property value="acountQueryParam.accNo"/>' />
							</td>
							<td width="10%" align="center">
							<input name="btnfind" type="button" class="submit_but09" id="btnfind" 
								onclick="queryAcountData()" value="查询" />
							</td>
						</tr>
						<tr>
							<td width="20%">分&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;行&nbsp;&nbsp;&nbsp; 
								<select name="acountQueryParam.idBranch"  id="selectIdCenter" onchange="changeIdCenter()">
								</select>
							</td>
							<td width="20%">机  构 号 &nbsp; 
								<input name="acountQueryParam.idBank" type="text"
									class="diinput_text01" id="idBank" maxlength="12"
									value='<s:property value="acountQueryParam.idBank"/>' />
							</td>
							<td width="20%">账户名称&nbsp;&nbsp;
								<input name="acountQueryParam.accName" type="text"
									class="diinput_text01" id="accName"  maxlength="128"
									value='<s:property value="acountQueryParam.accName"/>' />
							</td>
							<td width="10%">
							</td>
						</tr>
						<tr>
							<td width="20%">网&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;点&nbsp;&nbsp;&nbsp; 
								<select id="selectIdBank" onchange="changeIdBank()">
							</select>
							</td>
							<td width="20%">客  户 号 &nbsp; 
								<input name="acountQueryParam.custId" type="text"
									class="diinput_text01" id="txtcustid" maxlength="32"
									value='<s:property value="acountQueryParam.custId"/>' />
							</td>
							<td width="20%">对账周期&nbsp;&nbsp; 
								<s:select class="diinput_text01" style="width:165px" id="accCycle"
									name="acountQueryParam.accCycle" list="refAccCycleMap" listKey="key"
									listValue="value" headerKey="" headerValue="--请选择--"
									value="acountQueryParam.accCycle"  >
								</s:select>
							</td>
							<td width="10%" align="center">
								<input name="btnexport" type="button" class="submit_but09" 
									id="btnexport" value="导出" onclick="toExportData()"/>
							</td>
						</tr>
						<tr>
							<td width="20%">签约状态&nbsp;&nbsp; 
								<s:select list="contactFlagMap" listKey="key"
									listValue="value" headerKey="" headerValue="--请选择--"
									value="acountQueryParam.signFlag" name="acountQueryParam.signFlag" id="signFlag">
								</s:select>
							</td>
							<!-- 屏蔽掉验印模式
							<td width="21%">验印模式&nbsp;&nbsp; <s:select
									list="sealModeMap" class="diinput_text01" listKey="key"
									listValue="value" headerKey="" headerValue="--请选择--"
									value="acountQueryParam.sealMode"
									name="acountQueryParam.sealMode" id="sealMode">
								</s:select>
							</td>
							 -->
							<td width="20%">
							</td>
							<td width="20%">
							</td>
							<td width="10%" align="center">
								<input name="btnexport" id="btnexport"
									type="button" class="submit_but09" value="新增签约" onclick="addSign()"/>
							</td>
						</tr>
					</table>
			</div>
			<div
				style="width:100%; height:auto; float:left; margin-left:10px; padding:10px 0px 10px 0px; color: #000000;">
				<h1>账户信息列表</h1>
				<table width="98%"  cellpadding="0"  cellspacing="0"
					class="myTable" id="basicInfoTable" >
					<tr>
						<td width="3%" height="26" align="center" bgcolor="#c76c6f"
							class="font_colors01">序号</td>
						<td width="10%" align="center" bgcolor="#c76c6f"
							class="font_colors01">账号</td>
						<td width="5%" align="center" bgcolor="#c76c6f"
							class="font_colors01">机构号</td>
						<td width="10%" align="center" bgcolor="#c76c6f"
							class="font_colors01">户名</td>
						<td width="8%" align="center" bgcolor="#c76c6f"
							class="font_colors01">地址</td>
						<td width="5%" align="center" bgcolor="#c76c6f"
							class="font_colors01">邮编</td>
						<td width="5%" align="center" bgcolor="#c76c6f"
							class="font_colors01">联系人</td>
						<td width="5%" align="center" bgcolor="#c76c6f"
							class="font_colors01">联系电话</td>	
						<td width="8%" align="center" bgcolor="#c76c6f"
							class="font_colors01">验印账号</td>
							<td width="7%" align="center" bgcolor="#c76c6f"
							class="font_colors01">签约状态</td>
							<td width="6%" align="center" bgcolor="#c76c6f"
							class="font_colors01">验印模式</td>
							<td width="60" align="center" bgcolor="#c76c6f"
							class="font_colors01">对账周期</td>
							<td width="7%" align="center" bgcolor="#c76c6f"
							class="font_colors01">客户号</td>
							<td width="7%" align="center" bgcolor="#c76c6f"
							class="font_colors01">合同号</td>
						<td width="10%" align="center" bgcolor="#c76c6f"
							class="font_colors01">操作</td>
						<td width="20%" align="center" bgcolor="#c76c6f"
							class="font_colors01" style="display:none"></td>
					</tr>
					<tbody id="queryList">
						<s:iterator value="queryList" var="queryData" status="st">
							<tr <s:if test="#st.index%2 ==0">bgcolor="#F4DDDF"</s:if>
						<s:if test="#st.index%2 ==1">bgcolor="#FFFFFF"</s:if>>
								<td align="center" style="border:1px solid #C76C6F">
									<s:property value="#st.count+acountQueryParam.firstResult" />
								</td>
								<td align="center" style="border:1px solid #C76C6F">
									<s:property value="#queryData.accNo" />
								</td>
								<td align="center" style="border:1px solid #C76C6F">
									<s:property value="#queryData.idBank" />
								</td>
								<td align="center" style="border:1px solid #C76C6F">
									<s:property value="#queryData.accName" />
								</td>
								<td align="center" style="border:1px solid #C76C6F">
									<s:property value="#queryData.address" />
								</td>
								<td align="center" style="border:1px solid #C76C6F">
									<s:property value="#queryData.zip" />
								</td>
								<td align="center" style="border:1px solid #C76C6F">
									<s:property value="#queryData.linkMan" />
								</td>
								<td align="center" style="border:1px solid #C76C6F">
									<s:property value="#queryData.phone" />
								</td>
								<td align="center" style="border:1px solid #C76C6F"><s:property value="#queryData.sealAccNo" /></td>
								<td align="center" style="border:1px solid #C76C6F">
									<s:property value="contactFlagMap.get(#queryData.signFlag)" />
								</td>
								<td align="center" style="border:1px solid #C76C6F">
									<s:property value="sealModeMap.get(#queryData.sealMode)" />
								</td>
								<td  align="center" style="border:1px solid #C76C6F">
									<s:property value="refAccCycleMap.get(#queryData.accCycle)" />
								</td>
								<td  align="center" style="border:1px solid #C76C6F">
									<s:property value="#queryData.custId" /></td>
								<td  align="center" style="border:1px solid #C76C6F">
									<s:property value="#queryData.signContractNo" />
								</td>
								<td align="center"  style="border:1px solid #C76C6F">
								<s:if test="#queryData.signFlag==\"0\"">
									<a onclick="sign(this)" style="color:#FF0000">签约 </a>
									</s:if>
									<s:elseif test="#queryData.signFlag==\"1\"">
									<a onclick="sign(this)" style="color:#FF0000">修改 </a>
									</s:elseif>
								</td>
								<td  align="center" style="border:1px solid #C76C6F;display:none">
									<s:property value="sendTypeMap.get(#queryData.sendMode)" />
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
							<td colspan="8" align="right"><s:if
									test="acountQueryParam.curPage==1">
										<a>首页</a></a>&nbsp;&nbsp;<a>上一页</a>
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
