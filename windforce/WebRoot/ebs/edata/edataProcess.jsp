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
<title>数据处理</title>
<link href="<%=path%>/ebs/common/css/css.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/dicss.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/process.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=path%>/ebs/common/js/jquery.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/jquery.form.js"></script>
<script language="javascript" src="<%=path%>/ebs/common/js/calendar.js"></script>


<script type="text/javascript">

/**
 * 打开上传文件页面
 */
function opendUpFilePage(){
	var data = $("#checkDate").val();
	if(data!=""&&data!=null){
		if(new Date(data)>new Date()){
			alert("输入日期不能大于当前日期!");
			return;
		}
		var str = data.split("-");
		data = str[0]+str[1]+str[2];
		window.open("EDataAction_openUpLoadFile.action?dataDate="+data,
				 	"数据文件上传","height=340,width=500,top=200,left=400,toolbar=no,menubar=no,scrollbars=no,resizable=no,location=no, status=no");
	}else{
		alert("日期不能为空！");
	}
}

/**
 * 数据校验
 */
function dataCheck(){
	var data = $("#checkDate").val();
	if(data!=""&&data!=null){
		if(new Date(data)>new Date()){
			alert("输入日期不能大于当前日期!");
			return;
		}
		if(window.confirm("确定要进行数据校验吗？")){
			var str = data.split("-");
			data = str[0]+str[1]+str[2];
			showMsg("数据正在校验。。。 请稍等");
			var button=$("#dataCheckBut");
			button.attr("disabled","true");
			$.ajax({
		         type: "POST",
		         url: "EDataAction_dataCheck.action",
		         data: "dataDate="+data+"&temp="+new Date(),
		         success: function(result) {
		        	 		showMsg(result);
		        	 		button.removeAttr("disabled");
		        	 	}
			 });
		}
	}else{
		alert("日期不能为空！");
	}
}
/**
 * 数据导入
 */
function dataImport(){
	var data = $("#checkDate").val();
	if(data!=""&&data!=null){
		if(new Date(data)>new Date()){
			alert("输入日期不能大于当前日期!");
			return;
		}
		if(window.confirm("确定要进行数据导入吗？")){
			var str = data.split("-");
			data = str[0]+str[1]+str[2];
			showMsg("数据正在进行导入。。。 请稍等");
			var button=$("#dataImportBut");
			button.attr("disabled","true");
			$.ajax({
		         type: "POST",
		         url: "EDataAction_dataImport.action",
		         data: "dataDate="+data+"&temp="+new Date(),
		         success: function() {
		        		 	button.removeAttr("disabled");
		        	 	 }
			 });
		}
	}else{
		alert("日期不能为空！");
	}
}

/**
 *数据迁移
 */ 
function dataMove(){
	var data = $("#checkDate").val();
	if(data!=""&&data!=null){
		if(new Date(data)>new Date()){
			alert("输入日期不能大于当前日期!");
			return;
		}
		if(window.confirm("确定要进行数据迁移吗？")){
			var str = data.split("-");
			data = str[0]+str[1]+str[2];
			showMsg("数据正在迁移中。。。 请稍等");
			var button=$("#dataMoveBut");
			button.attr("disabled","true");
			$.ajax({
		         type: "POST",
		         url: "EDataAction_dataMove.action",
		         data: "dataDate="+data+"&temp="+new Date(),
		         success: function() {
		        		 	button.removeAttr("disabled");
		        	 	 }
			 });
		}
	}else{
		alert("日期不能为空！");
	}	
}

/**
 * 数据处理
 */
function dataProcess(){
	var data = $("#checkDate").val();
	if(data!=""&&data!=null){
		if(new Date(data)>new Date()){
			alert("输入日期不能大于当前日期!");
			return;
		}
		if(window.confirm("确定要进行数据处理吗？")){
			var str = data.split("-");
			data = str[0]+str[1]+str[2];
			showMsg("数据正在处理中。。。 请稍等");
			var button=$("#dataProcessBut");
			button.attr("disabled","true");
			$.ajax({
		         type: "POST",
		         url: "EDataAction_dataProcess.action",
		         data: "dataDate="+data+"&temp="+new Date(),
		         success: function() {
		        			button.removeAttr("disabled");
		        	 	 }
			 });
		}
	}else{
		alert("日期不能为空！");
	}
}

function showMsg(info){
	if($("#loginInfodiv").children().size()>20){
		$("#loginInfodiv").children(":first").remove();
	}
	$("#loginInfodiv").last().append("<div><b>"+info+"</b></div>");
}

/**
 * 一键数据导入
 */
function oneKeyDataImport(){
	var data = $("#checkDate").val();
	if(data!=""&&data!=null){
		if(new Date(data)>new Date()){
			alert("输入日期不能大于当前日期!");
			return;
		}
		if(window.confirm("确定要进行数据导入吗？")){
			var str = data.split("-");
			data = str[0]+str[1]+str[2];
			showMsg("数据正在导入中。。。 请稍等");
			var button=$("#oneKeyDataImpBut");
			button.attr("disabled","true");
			$.ajax({
		         type: "POST",
		         url: "EDataAction_oneKeyDataImport.action",
		         data: "dataDate="+data+"&temp="+new Date(),
		         success: function() {
		        			button.removeAttr("disabled");
		        			deleteImpDate();
		        	 	 }
			 });
		}
	}else{
		alert("日期不能为空！");
	}
}

/**
 * 将radio中的数据赋值给日期输入框
 */
function changeDate(){
	var date=$("input[name='notImpDate']:checked").val();
	$("#checkDate").val(date);
}


/**
 * 动态删除已经人工导入完成的日期,未实现
 */
function deleteImpDate(){
	//1、得到当前选择的radio
	var b = $(":radio[checked='true']").get(0);
	//2、删除此radio和后面的Label
	$(b).next("label").remove();
	$(b).remove();
	
}
/**
 * 打印信息
 */
function printfInfo(){
	setInterval(function(){
		$.ajax({
	         type: "POST",
	         url: "EDataAction_getPrintfInfo.action",
	         data: "temp="+new Date(),
	         success: function(result) {
	        	 		if(result.length>0){
		        			showMsg(result);
	        	 		}
	        	 	 }
		 });
	}, 1000);
}

$(document).ready(function() {
	printfInfo();
})


</script>

</head>
<body class="baby_in2">
<input id="moudleName" type="hidden" value="人工数据处理"/>
	<div style="height:550px ;width:1000px; margin-left: 10px"">
		<div style="width: 935px; height: 40px ;margin-top: 40px; margin-left: 20px">
			<table>
				<tr>	
					<td>
						日期:&nbsp;<input type="text" id="checkDate" name="checkDate" style="width:100px" maxlength="10" onclick="new Calendar().show(this);"/> &nbsp;&nbsp;&nbsp;&nbsp;
					</td>
					<td>
						<input type="button" id="dataCheckBut" name="dataCheckBut" value="数据校验" onclick="dataCheck()" class="submit_but09"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</td>
					<td>
						<input type="button" id="oneKeyDataImpBut" name="oneKeyDataImpBut" value="数据导入" onclick="oneKeyDataImport()"  class="submit_but09"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</td>
					
					<!-- 
					<td>
						<input type="button" id="dataImportBut" name="dataImportBut" value="数据导入" onclick="dataImport()"  class="submit_but09"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</td>
					<td>
						<input type="button" id="dataMoveBut" name="dataMoveBut" value="数据迁移" onclick="dataMove()" class="submit_but09"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</td>
					 -->
					<td>
						<input type="button" id="dataProcessBut" name="dataProcessBut" value="数据处理" onclick="dataProcess()"  class="submit_but09"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</td>
					
					<td>
						<input type="button" id="toUpLoad" name="toUpLoad" value="上传文件" onclick="opendUpFilePage()" class="submit_but09"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</td>
				</tr>
			</table>
		</div>
		<h1 style="margin-left: 20px">数据未导入完成的日期： </h1>
		<div id="daysdiv" style="width: 800px; height: 40px ; margin-left: 40px">
			<s:radio name="notImpDate" id="notImpDate" label="" list="daysMap" key="key" value="value" onclick="changeDate()"></s:radio>
		</div>
		<h1 style="margin-left: 20px">日志 </h1>
		<div style="background-color: white; width: 935px; height: 380px ; margin-left: 30px" id="loginInfodiv">
							
		</div>
	</div>
	
</body>
</html>
