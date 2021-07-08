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
<title>未达项审核</title>
<meta http-equiv="Content-Type" content="text/html" />

<link href="<%=path%>/ebs/common/css/css.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/dicss.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/process.css" rel="stylesheet" type="text/css" />

<script type="text/javascript"  src="<%=path%>/common/js/jquery/jquery-1.7.1.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/task.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/process.js"></script>
<script language="javascript" src="<%=path%>/ebs/common/js/calendar.js"></script>
<script language="javascript" src="<%=path%>/ebs/common/js/addSort.js"></script>

<script type="text/javascript">
	var err = "<s:property value='errMsg'/>";
	if (err != null && err.length > 0) {
		alert("获取任务出现错误");
		top.refresh();
	}
</script>


<script language="javascript" type="text/javascript">
var taskId='<s:property value="task.id"/>';
	function mySubmitTask(){
		submitTask("notMatchCommitAction_submmitTask.action",
				"checkTaskId="+taskId,
				"notMatchCommitAction_init.action");
	}
	
	function myToInput(){
		reInput("notMatchCommitAction_sendBack.action",
				"checkTaskId="+taskId,
				"notMatchCommitAction_init.action");
	}
	function myGiveUpTask(){
		giveUpTask("notMatchCommitAction_abandonTask.action",
				"checkTaskId="+taskId,
				"notMatchCommitAction_init.action");
	}
	function myToDelete(){
		toDelete("notMatchCommitAction_sendDelete.action",
				"checkTaskId="+taskId,
				"notMatchCommitAction_init.action");
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
	
	$(document).ready(function(){
		stopProcess();
    	bindImg();
    	new ImageShow().init(imgs, 800, 600);
	});
	
</script>
</head>


<body class="baby_in2">
<%@ include file="/ebs/taskviewtool/reasons_div.jsp"%>
<script type="text/javascript">startProcess("正在初始化界面。。。");</script>
<input id="moudleName" type="hidden" value="未达审核"/>
<div id="nov_nr1">
	<table>
		<tr>
			<td width="75%">
				<div style="width:100%;position:relative;">
					<%@ include file="/ebs/imgshow/ImageShow.html"%>
				</div>
			</td>
			<td width="25%" valign="bottom" >
			<ul class="dili">
					<li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
					<input id="submitButton" name="submitButton" type="button"
						class="submit_but09" value="提交任务" onclick="mySubmitTask()" />
					</li>
					<li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
						<input id="reInputButton" name="reInputButton" type="button"
							class="submit_but09" value="退回录入" onclick="myToInput()" />
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
		</table>
			<div style="width:100%; height:auto; float:left; margin-left:10px; padding:10px 0px 10px 0px;">
				<h1>未达明细列表
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					工作日期：<s:property value="nowDate"/> &nbsp; &nbsp;&nbsp;&nbsp;&nbsp;  
				<!-- 
					剩余票据：&nbsp;<s:property value="taskCount" />&nbsp;笔
				 -->
					</h1>
				<table width="98%" border="0" cellpadding="0" cellspacing="0"
					id="myTable">
					<thead>
						<tr>
							<td width="6%" height="26" align="center" bgcolor="#c76c6f"
								class="font_colors01">序号</td>
							<td width="7%" align="center" bgcolor="#c76c6f"
								class="font_colors01">业务流水</td>
							<td width="9%" align="center" bgcolor="#c76c6f"
								class="font_colors01">账单编号</td>
							<td width="10%" align="center" bgcolor="#c76c6f"
								class="font_colors01">账号</td>
							<td width="8%" align="center" bgcolor="#c76c6f"
								class="font_colors01">记账日期</td>
							<td width="8%" align="center" bgcolor="#c76c6f"
								class="font_colors01">金&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;额</td>
							<td width="8%" align="center" bgcolor="#c76c6f"
								class="font_colors01">凭证号码</td>
							<td width="10%" align="center" bgcolor="#c76c6f"
								class="font_colors01">摘&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;要</td>
							<td width="10%" align="center" bgcolor="#c76c6f"
								class="font_colors01">未达方向</td>
							<td width="12%" align="center" bgcolor="#c76c6f"
								class="font_colors01">核对结果</td>
						</tr>
					</thead>
					<tbody id="notMatchList" align="center">
						<s:iterator value="notMatchList" var="queryData" status="st">
							<tr bgcolor='<s:if test="#st.count%2==0">#F4DDDF</s:if>'>
								<td><s:property value="#st.count" />
								</td>
								<td><s:property value="#queryData.docId" />
								</td>
								<td><s:property value="#queryData.voucherNo" />
								</td>
								<td><s:property value="#queryData.accNo" />
								</td>
								<td><s:property value="#queryData.traceDate" />
								</td>
								<td><s:property value="#queryData.strTraceCredit" />
								</td>
								<td><s:property value="#queryData.traceNo" />
								</td>
								<td><s:property value="#queryData.inputDesc" />
								</td>
								<td><s:iterator value="refDirectionMap.keySet()" id="id">
										<s:if test="#queryData.direction==#id">
											<s:property value="refDirectionMap.get(#id)" />
										</s:if>
									</s:iterator>
								</td>
								<td><s:iterator value="refResultMap.keySet()" id="id">
										<s:if test="#queryData.checkFlag==#id">
											<s:property value="refResultMap.get(#id)" />
										</s:if>
									</s:iterator>
								</td>
							</tr>
						</s:iterator>
					</tbody>
			</table>
		</div>
</div>
</body>
</html>

