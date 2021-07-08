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
<script type="text/javascript">
	var err = "<s:property value='errMsg'/>";
	if (err != null && err.length > 0) {
		alert("获取任务出现错误");
		top.refresh();
	}
</script>
<title>录入审核</title>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3" />
<meta http-equiv="description" content="This is my page" />
<link href="<%=path%>/ebs/common/css/css.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/dicss.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/process.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="<%=path%>/common/js/jquery/jquery-1.7.1.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/ebs-paramdatabind-1.0.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/task.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/process.js"></script>

<script type="text/javascript">
	var taskId="<s:property value="task.id"/>";
	function bindData(){
		var options = {
		    bindJson: <%=request.getAttribute("inputInfo")%>
	  	};
		onbindData(options);
	};
	
	function bindImg(){
		imgs = new Array();
		<%List<String> imgList = (List<String>)request.getAttribute("imgList");
		if(imgList != null && imgList.size()  >0)
		{
			for(int i = 0 ; i < imgList.size() ;i ++)
			{%>
				var curr = <%=i%>;
				imgs[curr] = '<%=imgList.get(i)%>';
		  <%}
		}%>
	};
		
	function mySubmitTask(){
		var accNo=document.getElementById("accNo1").value;
		if(accNo==null||accNo.length==0){//未匹配成功
		  alert("人工录入未成功,请重新进行录入");
		  return;
	}
		
	submitTask("manualAuthAction_submitTask.action",
			"checkTaskId="+taskId,
			"manualAuthAction_init.action");
	}
	
	function myReInput(){
		reInput("manualAuthAction_reInput.action",
			 "checkTaskId="+taskId,
			 "manualAuthAction_init.action");
	}
	
	function myGiveUpTask(){
		giveUpTask("manualAuthAction_giveUpTask.action",
			"checkTaskId="+taskId,
			"manualAuthAction_init.action");
	}
	function myToDelete(){
		toDelete("manualAuthAction_toDelete.action",
				"checkTaskId="+taskId,
				"manualAuthAction_init.action");
	}
	
	$(document).ready(function(){
		bindData();
		prepareEnd();
		bindImg();
		new ImageShow().init(imgs, 800, 600);
	});
	
</script>

</head>

<body class="baby_in2">
	<%@ include file="/ebs/taskviewtool/reasons_div.jsp"%>
	<script type="text/javascript">preparePage("<%=path%>");</script>
	<input id="moudleName" type="hidden" value="录入审核" />
	<div id="nov_nr1">
		<div class="nov_moon">
			<form id="myform" name="manualInput" method="post">
				<table width="100%">
					<tr>
						<td width="75%">
							<div style="width:100%; position:relative; height:100%; float:left; margin-left:0px; padding:0px 0px 0px 0px;"
								class="border_bottom01">
								<%@ include file="/ebs/imgshow/ImageShow.html"%>
							</div>
						</td>
						<td width="25%" valign="top" align="left">
							<div style="float:left; margin-left:10px; padding:10px 0px 10px 0px;">
								<ul class="dili">
									<li>业务流水:
										<input name="docId" type="text"
											class="diinput_text01" id="docId" readonly="readonly" />
									</li>
									<li>账单编号:
										<input name="billNo" type="text"
											readonly="readonly" class="diinput_text01" id="billNo" />
									</li>
									<li>账&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号1:
										<input name="accNo1" type="text" class="diinput_text01" id="accNo1"
											readonly="readonly" />
									</li>
									<li>余&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;额:
										<input name="accNo_1" type="text"
											class="diinput_text01" id="accNo_1" readonly="readonly"/>
									</li>
									<li>余额结果:
										<select name="result1" id="result1" disabled="disabled">
											<option value="2">不相符</option>
											<option value="3">相符</option>
										</select>
									</li>
									<li>账&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号2:
										<input name="accNo2" type="text" class="diinput_text01" id="accNo2"
											readonly="readonly" />
									</li>
									<li>余&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;额:
										<input name="accNo_2" type="text"
											class="diinput_text01" id="accNo_2" readonly="readonly" />
									</li>
									<li>余额结果:
										<select name="result2" id="result2" disabled="disabled" >
											<option value="2">不相符</option>
											<option value="3">相符</option>
									</select>
									</li>
									<li>账&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号3:
										<input name="accNo3" type="text" class="diinput_text01" id="accNo3"
											readonly="readonly" />
									</li>
									<li>余&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;额:
										<input name="accNo_3" type="text"
											class="diinput_text01" id="accNo_3" readonly="readonly"/>
									</li>
									<li>余额结果:
										 <select name="result3" id="result3" disabled="disabled">
												<option value="2">不相符</option>
												<option value="3">相符</option>
										</select>
									</li>
									<li>账&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号4:
										<input name="accNo4" type="text" class="diinput_text01" id="accNo4"
											readonly="readonly" />
									</li>
									<li>余&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;额:
										<input name="accNo_4" type="text"
											class="diinput_text01" id="accNo_4" readonly="readonly"/>
									</li>
									<li>余额结果:
										<select name="result4" id="result4" disabled="disabled">
												<option value="2">不相符</option>
												<option value="3">相符</option>
										</select>
									</li>
									<li>账&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号5: 
										<input name="accNo5" type="text" class="diinput_text01" id="accNo5"
											readonly="readonly" />
									</li>
									<li>余&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;额:
										<input name="accNo_5" type="text"
											class="diinput_text01" id="accNo_5" readonly="readonly"/>
									</li>
									<li>余额结果:
										<select name="result5" id="result5" disabled="disabled" >
												<option value="2">不相符</option>
												<option value="3">相符</option>
										</select>
									</li>
								</ul>
								<ul class="dili">
									<li>&nbsp;&nbsp;&nbsp;&nbsp;
										<input id="submitButton" name="submitButton" type="button" class="submit_but09"
											value="提交任务" onclick="mySubmitTask()"/>
									</li>
									<li>&nbsp;&nbsp;&nbsp;&nbsp;
										<input id="reInputButton" name="reInputButton" type="button" class="submit_but09"
											value="重新录入" onclick="myReInput()" />
									</li>
									<li>&nbsp;&nbsp;&nbsp;&nbsp;
										<input id="giveUpButton" name="giveUpButton" type="button" 
											class="submit_but09" value="放弃任务" onclick="myGiveUpTask()" />
									</li>
									<li>&nbsp;&nbsp;&nbsp;&nbsp;
										<input id="toDeleteButton" name="toDeleteButton" type="button" 
										class="submit_but09" value="发起删除" onclick="myToDelete()" />
									</li>
								</ul>
							</div>
						</td>
					</tr>
				</table>
			</form>
		</div>
	</div>
</body>
</html>
