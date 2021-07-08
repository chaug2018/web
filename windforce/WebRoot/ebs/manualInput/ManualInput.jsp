<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
	+ request.getServerName() + ":" + request.getServerPort()
	+ path + "/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<script type="text/javascript">
		var err = "<s:property value='errMsg'/>";
		if (err != null && err.length > 0) {
			alert("获取任务出现错误");
			top.refresh();
		}
</script>
<title>数据补录</title>
<meta http-equiv="pragma" content="no-cache"/>
<meta http-equiv="cache-control" content="no-cache"/>
<meta http-equiv="expires" content="0"/>    
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3"/>
<meta http-equiv="description" content="This is my page"/>
<link href="<%=path%>/ebs/common/css/css.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/dicss.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/process.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="<%=path%>/common/js/jquery/jquery-1.7.1.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/task.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/process.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/ebs-paramdatabind-1.0.js"></script>
<script type="text/javascript">
	var imgs=null;
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
	<%}}%>
		};		
function myGetBillInfo(){
 	var billNo=document.getElementById("billNo").value;
 	if(billNo==null||billNo.length==0){
 		return;
 	}
 	getBillInfo("manualInputAction_getData.action",
 			"billNo="+billNo);
 }		
		
function mySubmitTask(){
	var accNo=document.getElementById("accNo1").value;
	if(accNo==null||accNo.length==0){//未匹配成功
	  alert("账单未匹配，无法提交任务");
	  return;
	}

	var result1=document.getElementById("result1").value;
	var result2=document.getElementById("result2").value;
	var result3=document.getElementById("result3").value;
	var result4=document.getElementById("result4").value;
	var result5=document.getElementById("result5").value;
	var billNo=document.getElementById("billNo").value;
	var checkResult=result1+result2+result3+result4+result5;
	submitTask("manualInputAction_submitTask.action",
			"checkTaskId="+taskId+"&checkResult="+checkResult+"&billNo="+billNo,
			"manualInputAction_init.action");
}
function myGiveUpTask(){
	giveUpTask("manualInputAction_giveUpTask.action",
		"checkTaskId="+taskId,
		"manualInputAction_init.action");
}
function myToDelete(){
	toDelete("manualInputAction_toDelete.action",
			"checkTaskId="+taskId,
			"manualInputAction_init.action");
}
	$(document).ready(function(){
	bindData();
	prepareEnd();
	//initImageShow(800,800);
	bindImg();
	new ImageShow().init(imgs, 800, 600);
	});
	
</script>

</head>

<body class="baby_in2">
	<%@ include file="/ebs/taskviewtool/processing_div.jsp"%>
	<%@ include file="/ebs/taskviewtool/reasons_div.jsp"%>
	<script type="text/javascript">preparePage("<%=path%>");</script>
<input id="moudleName" type="hidden" value="数据补录"/>
	<div id="nov_nr1">
		<div >
			<form id="myform" name="manualInput" method="post">
				<table width="100%">
					<tr>
						<td width="75%">
						<span style="font-size:16px; color:#b40001">
							<s:if test="reasons!=null">
							【重录理由:	
							<s:iterator value="reasons" var="reason" status="index">
							<s:property value="#index.index+1"/>.<s:property value="#reason"/>&nbsp;
							</s:iterator>
							】
							</s:if>
						</span>
							<div style="width:100%; height:100%; float:left;position:relative; margin-left:0px; padding:0px 0px 0px 0px;"
								class="border_bottom01">
								<%@ include file="/ebs/imgshow/ImageShow.html"%>
							</div>
						</td>
						<td width="25%" valign="top" align="left">
								<ul class="dili">
									<li>业务流水:
										<input name="docId" type="text"
											class="diinput_text01" id="docId" readonly="readonly" />
									</li>
									<li>账单编号:
										<input name="billNo" type="text"
											class="diinput_text01" id="billNo" />
									</li>
									<li>账&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号1:
										<input name="accNo1" type="text" class="diinput_text01" id="accNo1"
											readonly="readonly"/>
									</li>
									<li>余&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;额:
										<input name="accNo_1" type="text"
											class="diinput_text01" id="accNo_1" readonly="readonly"/></li>
									<li>余额结果:
										<select name="result1" id="result1">
											<option value="2">不相符</option>
											<option value="3">相符</option>
									</select>
									</li>
									<li>账&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号2:
										<input name="accNo2" type="text" class="diinput_text01" id="accNo2"
											readonly="readonly"/>
									</li>
									<li>余&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;额:
										<input name="accNo_2" type="text"
											class="diinput_text01" id="accNo_2" readonly="readonly"/>
									</li>
									<li>余额结果:
										<select name="result2" id="result2">
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
										<select name="result3" id="result3">
											<option value="2">不相符</option>
											<option value="3">相符</option>
										</select>
									</li>
									<li>账&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号4:
										<input name="accNo4" type="text" class="diinput_text01" id="accNo4"
											readonly="readonly"/>
									</li>
									<li>余&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;额:
										<input name="accNo_4" type="text" class="diinput_text01" 
											id="accNo_4" readonly="readonly"/></li>
									<li>余额结果:
										<select name="result4" id="result4">
											<option value="2">不相符</option>
											<option value="3">相符</option>
									</select></li>
									<li>账&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号5:
										<input name="accNo5" type="text" class="diinput_text01" id="accNo5"
											readonly="readonly" />
									</li>
									<li>余&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;额:
										<input name="accNo_5" type="text" class="diinput_text01" 
											id="accNo_5" readonly="readonly"/></li>
									<li>余额结果:
										<select name="result5" id="result5">
											<option value="2">不相符</option>
											<option value="3">相符</option>
										</select>
									</li>
								</ul>
								<ul class="dili">
									<li >&nbsp;&nbsp;&nbsp;&nbsp;<input
										id="submitButton" name="submitButton" type="button"
										class="submit_but09" value="提交任务" onclick="mySubmitTask()" />
									</li>
									<li>&nbsp;&nbsp;&nbsp;&nbsp;<input
										id="giveUpButton" name="giveUpButton" type="button"
										class="submit_but09" value="放弃任务" onclick="myGiveUpTask()" />
									</li>
									<li>&nbsp;&nbsp;&nbsp;&nbsp;<input
										id="toDeleteButton" name="toDeleteButton" type="button"
										class="submit_but09" value="发起删除" onclick="myToDelete()" /></li>
								</ul>
							</td>
					</tr>
				</table>
			</form>
		</div>
	</div>
<script type="text/javascript">
	 var bn=document.getElementById("billNo")
	 bn.focus();
     document.documentElement.onkeydown = function(evt){
                var b = !!evt, oEvent = evt || window.event;
                if (oEvent.keyCode == 13) {
                var node = b ? oEvent.target : oEvent.srcElement;
				if(node.name!=null&&node.name=="billNo"){
						myGetBillInfo();
                 }
        }
		if(oEvent.keyCode==112 && oEvent.ctrlKey){//ctrl+F1代表提交任务
			mySubmitTask();
     	}else if(oEvent.keyCode==113 && oEvent.ctrlKey){//ctrl+F2代表放弃任务
      		myGiveUpTask();
      	}else if(oEvent.keyCode==114 && oEvent.ctrlKey){//ctrl+F3代表删除任务
     	 	myToDelete();
   		}
   }

</script>
</body>
</html>
