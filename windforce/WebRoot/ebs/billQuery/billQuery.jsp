<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<link href="<%=path%>/ebs/common/css/process.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/css.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/dicss.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="<%=path%>/common/js/jquery/jquery-1.7.1.js"></script>
<script language="javascript" src="<%=path%>/ebs/common/js/calendar.js"></script>
<script language="javascript" src="<%=path%>/ebs/common/js/enterToTab.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/orgTree.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/billQuery.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/process.js"></script>

<script type="text/javascript">
	/**查看详情 跳转界面*/
	function view(docId,vouId) {
		if(vouId==null||vouId==""){
			location.href="viewBillDetail.action?docId="+docId;	
		}else{
			location.href="viewBillDetail.action?docId="+docId;	
		}	
	}
	
	/** 查询按钮相关事件 */
	function queryBillList() {
	if(IsDate(document.getElementById("startDate")) && IsDate(document.getElementById("endDate")))
	   {
		var startDate=document.getElementById("startDate").value;
		var endDate=document.getElementById("endDate").value;
		if(startDate!=null&&startDate!=""&&endDate!=null&&endDate!=""){
			if(startDate>endDate){
				alert("开始日期不能晚于结束日期，请确认！");
					viewBillListByPage("1");
				}else{
					viewBillListByPage("1");
				}
			}
			else{
				viewBillListByPage("1");
			}
		}
	}

	/** 票据查询之分页查询*/
	function viewBillListByPage(pageNumber){
		$("#curPage").val(pageNumber);
		$("#billListQueryForm").submit();
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
	
	function MyExportData() {
		var objTable = document.getElementById("billQuery");	
		
		if (objTable.rows.length>2) {
			window.location.href = "billQueryAction_exportData.action";
		}else{
			alert("查询结果无数据");
		}
	}	
	
	$(document).ready(
			function() {
				var idCenter = "<s:property value='idCenter'/>";
				var idBank = "<s:property value='idBank'/>";
				stopProcess();
				initTree(<%=request.getAttribute("orgTree")%>, idCenter,idBank);
			});
</script>
</head>
<body class="baby_in2">
	<input id="moudleName" type="hidden" value="影像流水查询" />
	<div>
		<form id="billListQueryForm" method="post" name="billQueryAction"
			action="billQueryAction_queryBillList.action">
			<div class="nov_moon">
				<div style="width:100%; height:auto; float:left; margin-left:10px; padding:10px 0px 10px 0px;"
					class="border_bottom01">
					<input type="hidden" name="queryParam.curPage" id="curPage" />
					<table width="100%">
						<tr>
							<td width="20%">总 行&nbsp;&nbsp;&nbsp; 
								<select >
									<option value=""> 华融湘江总行</option>
								</select>
							</td>
							<td width="20%">开始日期&nbsp; 
								<input name="queryParam.startDate" type="text"
									onclick="new Calendar().show(this);" title="开始日期"
									class="diinput_text01" id="startDate" maxlength="10"
									value='<s:property value="queryParam.startDate"/>' />
							</td>
							<td width="20%">结束日期&nbsp; 
								<input name="queryParam.endDate" type="text" onclick="new Calendar().show(this);" 
									title="结束日期" class="diinput_text01" id="endDate" maxlength="10"
									value='<s:property value="queryParam.endDate"/>' />
							</td>
							<td width="10%" >
								<input name="btnfind" type="button" class="submit_but09" id="btnfind"
									onclick="queryBillList()" value="查询" />
							</td>
						</tr>
						<tr>
							<td width="20%">分 行&nbsp;&nbsp;&nbsp; 
								<select name="queryParam.idCenter" id="selectIdCenter"
									onchange="changeIdCenter()">
								</select>
							</td>
							<td width="20%">账单编号&nbsp; 
								<input name="queryParam.voucherNo" type="text" class="diinput_text01"
								id="voucherNo" maxlength="30" value='<s:property value="queryParam.voucherNo"/>' />
							</td>

							<td width="20%">业务流水&nbsp; 
								<input name="queryParam.docId" type="text" class="diinput_text01"
								 	id="docId" maxlength="30" value='<s:property value="queryParam.docId"/>' />
							</td>
							<td width="10%">
							</td>
						</tr>
						<tr>
							<td width="20%">网 点&nbsp;&nbsp;&nbsp;
								<select onchange="changeIdBank()" name="queryParam.idBank" id="selectIdBank" >
								</select>
								<input type="hidden" id="idBank" />
							</td>
							<td width="20%">对账日期&nbsp; 
								<input name="queryParam.checkDate" type="text" id="checkDate" maxlength="10"
									onclick="new Calendar().show(this);" class="diinput_text01"
									value='<s:property value="queryParam.checkDate"/>' />
							</td>
							<td width="20%">影像状态&nbsp;
								<s:select list="docFlagMap"  listKey="key" listValue="value"
									headerKey="" headerValue="--请选择--" value="queryParam.docFlag"
									name="queryParam.docFlag" id="docFlag">
								</s:select>	
							</td>
							<td width="10%">
								<input name="export" type="button" class="submit_but09" id="export"
									onclick="MyExportData()" value="导出" />
							</td>
						</tr>
					</table>
				</div>
				<div
					style="width:100%; height:auto; float:left; margin-left:10px; padding:10px 0px 10px 0px;">
					<h1>影像信息列表</h1>

					<table width="100%" border="0" cellpadding="0" cellspacing="0"
						class="myTable" id="billQuery">
						<tr>
							<td width="5%" height="26" align="center" bgcolor="#c76c6f"
								class="font_colors01">序号</td>
							<td width="5%" height="26" align="center" bgcolor="#c76c6f"
								class="font_colors01">流水号</td>
							<td width="10%" align="center" bgcolor="#c76c6f"
								class="font_colors01">账单编号</td>
							<td width="10%" align="center" bgcolor="#c76c6f"
								class="font_colors01">机构号</td>
							<td width="15%" align="center" bgcolor="#c76c6f"
								class="font_colors01">户名</td>
							<td width="10%" align="center" bgcolor="#c76c6f"
								class="font_colors01">对账日期</td>
							<td width="10%" align="center" bgcolor="#c76c6f"
								class="font_colors01">验印状态</td>
							<td width="10%" align="center" bgcolor="#c76c6f"
								class="font_colors01">影像状态</td>
							<td width="15%" align="center" bgcolor="#c76c6f"
								class="font_colors01">删除原因</td>
							<td width="10%" align="center" bgcolor="#c76c6f"
								class="font_colors01">操作</td>
						</tr>
						<tbody id="billList">
							<s:iterator value="billList" var="billDto" status="st">

								<tr <s:if test="#st.index%2 ==0">bgcolor="#CBD6E0"</s:if>
									<s:if test="#st.index%2 ==1">bgcolor="#FFFFFF"</s:if>>
									<td align="center"><s:property
											value="#st.count+queryParam.firstResult" />
									</td>
									<td align="center"><s:property value="#billDto.docId" />
									</td>
									<td align="center"><s:property value="#billDto.voucherNo" />
									</td>
									<td align="center"><s:property value="#billDto.idBank" />
									</td>
									<td align="center"><s:property value="#billDto.accName" />
									</td>
									<td align="center"><s:property value="#billDto.docDate" />
									</td>
									<td align="center"><s:iterator
											value="proveFlagMap.keySet()" id="id">
											<s:if test="#billDto.proveFlag==#id">
												<s:property value="proveFlagMap.get(#id)" />
											</s:if>
										</s:iterator></td>
									<td align="center"><s:iterator value="docFlagMap.keySet()"
											id="id">
											<s:if test="#billDto.docFlag==#id">
												<s:property value="docFlagMap.get(#id)" />
											</s:if>
										</s:iterator></td>
									<td align="center"><s:property value="#billDto.deleteReason" />
									</td>
									<td align="center"><a
										onclick='view("${billDto.docId}","${billDto.voucherNo}")'
										style="color:#33F">查看详情</a>
									</td>

								</tr>

							</s:iterator>
						</tbody>
						<tfoot id="billinfoListPageInfo">
							<tr class="pagelinks">
								<td colspan="4">每页显示 <select name="queryParam.pageSize"
									id="pageSize" style="width:40px;"
									onchange="viewBillListByPage('1')">
										<option value="10"
											<s:if test="queryParam.pageSize==10">selected="selected"</s:if>>10</option>
										<option value="20"
											<s:if test="queryParam.pageSize==20">selected="selected"</s:if>>20</option>
										<option value="50"
											<s:if test="queryParam.pageSize==50">selected="selected"</s:if>>50</option>

								</select>条记录&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;当前显示<s:if
										test="queryParam.total==0">0</s:if> <s:else>
										<s:property value="queryParam.firstResult+1" />
									</s:else>到<s:property value="queryParam.lastResult" />条，共<s:property
										value="queryParam.total" />条</td>

								<td colspan="5" align="right"><s:if
										test="queryParam.curPage==1">
										<a>首页</a>&nbsp;&nbsp;<a>上一页</a>
									</s:if> <s:else>
										<a href="#" onclick="viewBillListByPage('1');">首页</a>&nbsp;&nbsp;<a
											href="#"
											onclick="viewBillListByPage('${queryParam.curPage - 1}');return false;">上一页</a>
									</s:else> <s:if test="queryParam.curPage==queryParam.totalPage">
										<a>下一页</a>&nbsp;&nbsp;<a>尾页</a>
									</s:if> <s:else>
										<a href="#"
											onclick="viewBillListByPage(${queryParam.curPage + 1});return false;">下一页</a>&nbsp;&nbsp;<a
											href="#"
											onclick="viewBillListByPage('${queryParam.totalPage}');return false;">尾页</a>
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
