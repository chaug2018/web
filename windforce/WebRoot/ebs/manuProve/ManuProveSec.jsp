<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<style type="text/css">
.baby_in2 #nov_nr .modelname {
	font-size: 24px;
	color: #FF0033;
}
</style>
<head>
<script type="text/javascript">
	var err = "<s:property value='errMsg'/>";
	if (err != null && err.length > 0) {
		alert(err + "!");
		top.refresh();
	}
</script>
<title>账单复验</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=IE8" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3" />
<link href="<%=path%>/ebs/common/css/css.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/dicss.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/process.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="<%=path%>/common/js/jquery/jquery-1.7.1.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/task4prove.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/process.js"></script>

<script language="javascript" type="text/javascript">
	var manuproveJson = <%=request.getAttribute("manuProveJson")%>;
	$(document).ready(function(){
		prepareEnd("secManuProveAction");
		BeginManualSeal(manuproveJson);
	});
</script>
</head>

<body class="baby_in2" LANGUAGE=javascript 
	onunload="EndManualSeal()">
	<input id="moudleName" type="hidden" value="人工复验"/>
	<%@ include file="/ebs/taskviewtool/reasons_div.jsp"%>
	<script type="text/javascript">preparePage("<%=path%>");</script>
	<table>
		<tr>
			<td>
				<OBJECT name="ManuSeal" id='ManuSeal'
					codeBase="ebs/manuProve/SealForBS.cab#Version=-2,0,0,0"
						classid="clsid:0935F84F-7AC0-48E0-9E25-983791FA11A9" width='780' height='650'>
				<PARAM NAME="_ExtentX" VALUE="20638">
				<PARAM NAME="_ExtentY" VALUE="16431">
				</OBJECT>
			</td>
			<td>
				<table>
					<tr>
						<td>
							<input id="submitButton" type="button" class="submit_but09"
							value="验印有效" name="submitButton" onclick="Pass()" />
						</td>
					</tr>
					<tr>
						<td>
							<input id="BtnNotPass" type="button" class="submit_but09" value="验印无效"
								name="BtnNotPass" onclick="NoPass()" />
						</td>
					</tr>
					<tr>
						<td>	
							<input id="toDeleteButton" type="button" class="submit_but09"
							value="删除任务" name="toDeleteButton" onclick="myToDelete()" />
						</td>
					</tr>
					<tr>
						<td>	
							<input id="giveUpButton" type="button" class="submit_but09"
							value="放弃任务" name="giveUpButton" onclick="myGiveUpTask()" />
						</td>
					</tr>
					<tr>
						<td>	
							<input id="BtnChangeYJ" type="button" class="submit_but09"
								value="切换印鉴" name="BtnChangeYJ" onclick="ChangeYJ()" />
								
						</td>
					</tr>
					<tr>
						<td>	
							<input id="BtnChangeAcc" type="button" class="submit_but09"
								value="切换账号" name="BtnChangeAcc" onclick="ChangeAcc()" />
						</td>
					</tr>
					<tr>
						<td>
							<input id="BtnChangeSJ" type="button" class="submit_but09"
								value="切换时间" name="BtnChangeSJ" onclick="changeSJ()" />
						</td>
					</tr>
					<tr>
						<td><br><br>
							<h1>按PageUp和PageDown键预留印鉴翻页<br>按<1><2><3><4>数字键选择印鉴</h1>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</body>
</html>