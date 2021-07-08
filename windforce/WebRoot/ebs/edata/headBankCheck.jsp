<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<title>总行对账</title>

<link href="<%=path%>/ebs/common/css/css.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/process.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/dicss.css" rel="stylesheet" type="text/css" />

<script language="javascript" src="<%=path%>/ebs/common/js/calendar.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/jquery.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/jquery.form.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/tCommon.js"></script>

<script type ="text/javascript">

//执行总行对账
function executeHBCheck(){
	var checkDate = $("#checkDate").val();
	var checkRule =$("#checkRule").val();
	var rule2Where="";
	if(checkRule==""){
		alert("请确定总行对账规则后再执行总行对账！");
		return ;
	}
	else if(checkRule=="2") {
		var creditB =$("#creditB").val();
		var creditE =$("#creditE").val();
		var maxtracebalB =$("#maxtracebalB").val();
		var maxtracebalE =$("#maxtracebalE").val();
		var sumtracebalB =$("#sumtracebalB").val();
		var sumtracebalE =$("#sumtracebalE").val();
		
		if(creditB==""){
			creditB = "0";
		}
		if(creditE==""){
			creditE = "9999999999999";
		}
		if(maxtracebalB==""){
			maxtracebalB = "0";
		}
		if(maxtracebalE==""){
			maxtracebalE = "9999999999999";
		}
		if(sumtracebalB==""){
			sumtracebalB = "0";
		}
		if(sumtracebalE==""){
			sumtracebalE = "9999999999999";
		}
		
		if($.tCommon.numberValidate(creditB)&&$.tCommon.numberValidate(creditE)){
			if(parseFloat(creditB)>parseFloat(creditE)){
				alert("余额的开始金额不能大于结束金额");
				return;
			}
		}
		else{
			alert("请确定余额格式是否正确");
			return ;
		}
		if($.tCommon.numberValidate(maxtracebalB)&&$.tCommon.numberValidate(maxtracebalE)){
			if(parseFloat(maxtracebalB)>parseFloat(maxtracebalE)){
				alert("单笔发生额的开始金额不能大于结束金额");
				return;
			}
		}else{
			alert("请确定单笔发送额格式是否正确");
			return ;
		}
		if($.tCommon.numberValidate(sumtracebalB)&&$.tCommon.numberValidate(sumtracebalE)){
			if(parseFloat(sumtracebalB)>parseFloat(sumtracebalE)){
				alert("累计发生额的开始金额不能大于结束金额");
				return;
			}
		}else{
			alert("请确定累计发生额格式是否正确");
			return ;
		}
		rule2Where="&creditB="+creditB+"&creditE="+creditE
		+"&maxtracebalB="+maxtracebalB+"&maxtracebalE="+maxtracebalE
		+"&sumtracebalB="+sumtracebalB+"&sumtracebalE="+sumtracebalE;
	}

	if(checkDate!=""&&checkDate!=null){
		if(new Date(checkDate)>new Date()){
			alert("输入日期不能大于当前日期!");
			return;
		}
		if(window.confirm("确定要进行总行对账吗？")){
			var str = checkDate.split("-");
			checkDate = str[0]+str[1]+str[2];
			var param="dataDate="+checkDate;
			param+="&checkRule="+checkRule;
			
			if(checkRule=="1"){
				var sendMode="";
				$("input[name='sendMode']:checked").each(function(){
					sendMode+=($(this).val()+",");
				});
				if(sendMode!=""){
					sendMode=sendMode.substring(0,sendMode.length-1);
				}
				param+="&sendMode="+sendMode;
				var acccycle="";
				$("input[name='acccycle']:checked").each(function(){
					acccycle+=($(this).val()+",");
				});
				if(acccycle!=""){
					acccycle=acccycle.substring(0,acccycle.length-1);
				}
				param+="&acccycle="+acccycle;
				
			}else if(checkRule=="2"){
				var subjectNo="";
				$("input[name='subjectNo']:checked").each(function(){
					subjectNo+=($(this).val()+",");
				});
				if(subjectNo!=""){
					subjectNo=subjectNo.substring(0,subjectNo.length-1);
				}
				
				param+="&subjectNo="+subjectNo+rule2Where;
			}
			var button=$("#hbCheckBut");
			button.attr("disabled","true");
			$.ajax({
		        type: "POST",
		        url: "EDataAction_executeHBCheck.action",
		        data: param+"&temp="+new Date(),
		        success: function(result) {
		              	  	alert(result);
		              	    button.removeAttr("disabled");
		       	 	 }
			});
		}
	}else{
		alert("对账截止日期不能为空！");
	}
}

function showRule(){
	var checkRule = $("#checkRule").val();
	if(checkRule=="1"){
		$(".rule1").show();
		$(".rule2").hide();
	}else if(checkRule=="2"){
		$(".rule1").hide();
		$(".rule2").show();
	}else{
		$(".rule1").hide();
		$(".rule2").hide();	
	}
}

function printfInfo(){
	setInterval(function(){
		$.ajax({
	         type: "POST",
	         url: "EDataAction_getHBCheckInfo.action",
	         data: "temp="+new Date(),
	         success: function(result) {
	        	 	 }
		 });
	}, 10000);
}

$(document).ready(function() {
	printfInfo();
})

</script>
 
</head>    
<body class="baby_in2">
	<input id="moudleName" type="hidden" value="总行对账"/>
	<div class="nov_moon">
		<div style="width:98%; height:auto; float:left; margin-left:10px; padding:10px 0px 10px 0px;"
				class="border_bottom01">
			<form id="hBCheckForm" >
			<h1 style="margin-left: 30px">总行对账 </h1>
				<table width="650px;" align="center" border="0" cellpadding="0" cellspacing="8px;">
					<tr>
						<td>对账规则:
						</td>
						<td>
							<s:select label="" cssStyle="width:160px;" id="checkRule" list="#{1:'总行对账规则一',2:'总行对账规则二'}" listKey="key" listValue="value" headerKey="" headerValue="请选择对账规则" onchange="showRule()" ></s:select>
						</td>
					</tr>
					<tr>
						<td>对账截止日期 :</td>
						<td>
						<input type="text" class="diinput_text01" name="checkDate" id="checkDate" 
							 maxlength="10" onclick="new Calendar().show(this);" title="对账日期" />
						</td>
					</tr>
					<tr class="rule1" style="display: none">
						<td>
							发送方式：
						</td>
						<td>
							<s:checkboxlist name="sendMode" label="" list="sendModeMap" listKey="key" listValue="value" ></s:checkboxlist >
						</td>
					</tr>
					<tr class="rule1" style="display: none">
						<td>
							账户类别：
						</td>
						<td>
							<s:checkboxlist name="acccycle" label="" list="acccycleMap" listKey="key" listValue="value"></s:checkboxlist >
						</td>
					</tr>
					
					<tr class="rule2" style="display: none">
						<td>
							时点余额(元):
						</td>
						<td>
							<input id="creditB" name="creditB" class="diinput_text01" style="width:60px;"/>
							至
							<input id="creditE" name="creditE" class="diinput_text01" style="width:100px;"/>
						</td>
					</tr>
					<tr class="rule2" style="display: none">
						<td>
							单笔发生额(元):
						</td>
						<td>
							<input id="maxtracebalB" name="maxtracebalB" class="diinput_text01" style="width:60px;"/>
							至
							<input id="maxtracebalE" name="maxtracebalE" class="diinput_text01" style="width:100px;"/>
						</td>
					<tr class="rule2" style="display: none">
						<td>
							累计发生额(元):
						</td>
						<td>
							<input id="sumtracebalB" name="sumtracebalB" class="diinput_text01" style="width:60px;"/>
							至
							<input id="sumtracebalE" name="sumtracebalE" class="diinput_text01" style="width:100px;"/>
						</td>
					</tr>
					<tr class="rule2" style="display: none">
						<td>
							科目号:
						</td>
						<td>
							<s:checkboxlist name="subjectNo" label="" list="subNoMap" listKey="key" listValue="value"></s:checkboxlist >
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<input type="button" value="对账" id="hbCheckBut" onclick="executeHBCheck()" class="submit_but09"/>
						</td>
					</tr>
				</table>
			</form>
		</div>
	</div>
</body>
</html>
