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
<title>未达项录入</title>
<meta http-equiv="Content-Type" content="text/html" />

<link href="<%=path%>/ebs/common/css/css.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/dicss.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/process.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="<%=path%>/common/js/jquery/jquery-1.7.1.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/task.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/process.js"></script>
<script language="javascript" src="<%=path%>/ebs/common/js/calendar.js"></script>
<script language="javascript" src="<%=path%>/ebs/common/js/addSort.js"></script>
<script language="javascript" src="<%=path%>/ebs/common/js/tableModify.js"></script>

<script language="javascript" type="text/javascript">
	var err = "<s:property value='errMsg'/>";
	if (err != null && err.length > 0) {
		alert("获取任务出现错误");
		top.refresh();
	}
	var taskId="<s:property value='task.id'/>";

	function mySubmitTask() {
		var arrData = new Array();
		var objTable = document.getElementById("myTable");
		if (objTable.rows.length>1) {
			for ( var i = 0; i < objTable.rows.length; i++) {
				//对核对结果进行判断,没有输入核对结果不能提交
				for ( var j = 0; j < objTable.rows[i].cells.length; j=j+9) {
					if(objTable.rows[i].cells[j].innerText==""){
						alert("请点击编辑,输入核对结果");
						return;
					}
				}
				
				for ( var j = 0; j < objTable.rows[i].cells.length; j++) {
					arrData[i * objTable.rows[i].cells.length + j] = objTable.rows[i].cells[j].innerText;
				}
			}
		}else{
			alert("没有未达项数据");
			return;
		}
		var data = arrData.join(",");
		submitTask("notMatchInputAction_submmitTask.action",
				"checkTaskId="+taskId+"&notMatchData="+ data,
				"notMatchInputAction_init.action");
	}
	
	function myGiveUpTask(){
		giveUpTask("notMatchInputAction_abandonTask.action",
				"checkTaskId="+taskId,
				"notMatchInputAction_init.action");
	}
	function myToDelete(){
		toDelete("notMatchInputAction_sendDelete.action",
				"checkTaskId="+taskId,
				"notMatchInputAction_init.action");
	}	
	
	function bindImg(){
		imgs = new Array();
		<%List<String> imgList = (List<String>)request.getAttribute("imgList");
		if(imgList != null && imgList.size()  >0)
		{
		for(int i = 0 ; i < imgList.size() ;i ++)
		{%>
		var curr = <%=i%>;
		imgs[curr] = '<%=imgList.get(i)%>';
		<%}}%>
	};
	
	$(document).ready(function() {
		prepareEnd();
		bindImg();
		new ImageShow().init(imgs, 800, 600);
	});
</script>
</head>

<body class="baby_in2">
	<input id="moudleName" type="hidden" value="未达录入" />
	<div id="son_div" class="son_div"
		style="background:url('<%=path%>/ebs/common/images/bg2.jpg')">
		<div class="sonpage" id="sonpage">
			<%@ include file="/ebs/back/notMatchInput_div.jsp"%>
		</div>
	</div>
	<%@ include file="/ebs/taskviewtool/processing_div.jsp"%>
	<%@ include file="/ebs/taskviewtool/reasons_div.jsp"%>
	<script type="text/javascript">
		preparePage("${pageContext.request.contextPath}");
	</script>
	<div id="nov_nr1">
		<form id="form1">
			<table width="100%">
				<tr>
					<td width="75%">
						<div style="position:relative;width:100%;height:100%;">
							<%@ include file="/ebs/imgshow/ImageShow.html"%>
						</div>
					</td>
					<td width="25%" valign="middle" align="left"><input id="lineCount" type="hidden"
						value='<s:property value="lineCount"/>' />
						<ul class="dili">
							<li>业务流水:&nbsp; 
								<input name="docSet.docId" type="text" class="diinput_text01" id="docId" 
									readonly="readonly" value='<s:property value="docId"/>' /></li>
							<li>账单编号:&nbsp; 
								<input name="docSet.voucherNo" type="text" class="diinput_text01" id="voucherNo" 
									readonly="readonly" value='<s:property value="voucherNo"/>' />
							</li>
							<li>账&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号:&nbsp; 
								<input name="docSet.accNo" type="text"
									class="diinput_text01" maxlength="32" id="accNo" tabindex="1"
									onkeydown="cgo(this.form,this.name,'')" />
							</li>
							<li>记账日期:&nbsp; 
								<input name="textfield7" type="text" class="diinput_text01" id="traceDate" tabindex="2"
									onclick="new Calendar().show(this);" maxlength="12"
									onkeydown="cgo(this.form,this.name,'')" />
							</li>
							<li>凭证号码:&nbsp; 
							<input name="textfield8" type="text" class="diinput_text01" id="traceNo"
								tabindex="3" maxlength="32" onkeydown="cgo(this.form,this.name,'')" />
							</li>
							<li>摘&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;要:&nbsp; 
								<input name="inputDesc" type="text" class="diinput_text01"
									id="inputDesc" tabindex="4" maxlength="128" 
									onkeydown="cgo(this.form,this.name,'')" />
							</li>
							<li>金&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;额:&nbsp; 
								<input name="textfield10" type="text" class="diinput_text01"
									id="traceCredit" tabindex="5" maxlength="32" onkeydown="cgo(this.form,this.name,'')" />
							</li>
							<li>未达方向:&nbsp;&nbsp;&nbsp; 
								<select name="direction" id="direction" tabindex="6"
									onkeydown="cgo(this.form,this.name,'')" style="color:#333333; font-size:13px; ">
									<option value="-1">--请选择--</option>
									<s:iterator value="refDirectionMap">
										<option value="<s:property value='key'/>">
											<s:property value='value' />
										</option>
									</s:iterator>
								</select>
							</li>
							<li>核对结果:&nbsp;&nbsp;&nbsp; 
								<select name="result" id="result" tabindex="7" onkeydown="if(window.event.keyCode == 13)"
									style="color:#333333; font-size:13px; ">
									<option value="-1">--请选择--</option>
									<s:iterator value="refResultMap">
										<s:if test="key == 4 || key == 5">
											<option value="<s:property value='key'/>">
												<s:property value='value' />
											</option>
										</s:if>
									</s:iterator>
								</select>
							</li>
							<li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<input name="save" type="button" class="submit_but09"
									id="submitModify" value="保存" onclick="addSort()" />
							</li>
							<li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
								<input id="submitButton" name="submitButton" type="button"
									class="submit_but09" value="提交任务" onclick="mySubmitTask()" />
							</li>
							<li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
								<input id="giveUpButton" name="giveUpButton" type="button"
									class="submit_but09" value="放弃任务" onclick="myGiveUpTask()" />
							</li>
							<li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
								<input id="toDeleteButton" id="toDeleteButton" type="button"
									class="submit_but09" value="发起删除" onclick="myToDelete()" />
							</li>
						</ul>
					</td>
				</tr>
				<tr>
				</tr>
			</table>
		</form>
	</div>

	<div
		style="width:100%; height:auto; float:left; margin-left:10px; padding:10px 0px 10px 0px;">
		<h1>
			未达明细列表 &nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp; 工作日期：
			<s:property value="nowDate" />
			&nbsp;&nbsp;&nbsp;&nbsp;
			提示：录入的未达账号必须和纸质账单上打印的账号一致，如：88888801-00001，否则提交任务后无法更新账号的余额结果。

		</h1>
		<table width="1002" cellpadding="0" cellspacing="0" id="myTable">

			<tr>
				<td width="60px" height="26" align="center" bgcolor="#c76c6f"
					class="font_colors01">序号</td>
				<td width="70px" align="center" bgcolor="#c76c6f"
					class="font_colors01">业务流水</td>
				<td width="80px" align="center" bgcolor="#c76c6f"
					class="font_colors01">账单编号</td>
				<td width="90px" align="center" bgcolor="#c76c6f"
					class="font_colors01">账号</td>
				<td width="80px" align="center" bgcolor="#c76c6f"
					class="font_colors01">记账日期</td>
				<td width="80px" align="center" bgcolor="#c76c6f"
					class="font_colors01">金&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;额</td>
				<td width="80px" align="center" bgcolor="#c76c6f"
					class="font_colors01">凭证号码</td>
				<td width="80px" align="center" bgcolor="#c76c6f"
					class="font_colors01">摘&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;要</td>
				<td width="100px" align="center" bgcolor="#c76c6f"
					class="font_colors01">未达方向</td>
				<td width="90px" align="center" bgcolor="#c76c6f"
					class="font_colors01">核对结果</td>
				<td width="80px" align="center" bgcolor="#c76c6f"
					class="font_colors01">操作</td>
			</tr>
			<tbody id="sortList" style="text-align:center;">
				<s:iterator value="notMatchList" var="queryData" status="st">
					<tr bgcolor='<s:if test="#st.count%2==0">#F4DDDF</s:if>'>
						<td align="center" style="border:1px solid #C76C6F"><s:property
								value="#st.count" />
						</td>
						<td style="border:1px solid #C76C6F"><s:property
								value="#queryData.docId" />
						</td>
						<td style="border:1px solid #C76C6F"><s:property
								value="#queryData.voucherNo" />
						</td>
						<td style="border:1px solid #C76C6F"><s:property
								value="#queryData.accNo" />
						</td>
						<td style="border:1px solid #C76C6F"><s:property
								value="#queryData.traceDate" />
						</td>
						<td style="border:1px solid #C76C6F"><s:property
								value="#queryData.strTraceCredit" />
						</td>
						<td style="border:1px solid #C76C6F"><s:property
								value="#queryData.traceNo" />
						</td>
						<td style="border:1px solid #C76C6F"><s:property
								value="#queryData.inputDesc" />
						</td>
						<td style="border:1px solid #C76C6F"><s:iterator
								value="refDirectionMap.keySet()" id="id">
								<s:if test="#queryData.direction==#id">
									<s:property value="refDirectionMap.get(#id)" />
								</s:if>
							</s:iterator></td>
						<td style="border:1px solid #C76C6F"><s:iterator
								value="refResultMap.keySet()" id="id">
								<s:if test="#queryData.checkFlag==#id">
									<s:property value="refResultMap.get(#id)" />
								</s:if>
							</s:iterator></td>
						<td style="border:1px solid #C76C6F"><a
							onclick="modifyNotmatchItem(this)" style="color:#BE333A">编辑</a><font
							color="#BE333A"> |</font> <a onclick="deleteRow(this)"
							style="color:#BE333A"> 删除</a></td>
					</tr>
				</s:iterator>
			</tbody>
		</table>
	</div>
</body>
</html>

