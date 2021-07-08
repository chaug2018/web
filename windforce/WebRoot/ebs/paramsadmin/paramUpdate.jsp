<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
    	<title>修改参数</title>

    	<meta http-equiv="pragma" content="no-cache"/>
		<meta http-equiv="cache-control" content="no-cache"/>
		<meta http-equiv="expires" content="0"/>    
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3"/>
		<meta http-equiv="description" content="This is my page"/>
		<link href="<%=path %>/ebs/common/css/css.css" rel="stylesheet" type="text/css" ></link>
		<script type="text/javascript" src="<%=path%>/ebs/common/js/jquery.js"></script>
		<script type="text/javascript" src="<%=path%>/ebs/common/js/jquery.form.js"></script>
		<script type="text/javascript" src="<%=path%>/ebs/common/js/ebs-paramdatabind-1.0.js"></script>
		<script type="text/javascript" src="<%=path%>/ebs/paramsadmin/js/paramUpdate.js"></script>
		<script language="javascript" src="<%=path %>/ebs/common/js/calendar.js"></script>
		<script type="text/javascript">
			$(document).ready(function() {
				var options = {
		    		bindJson: <%=request.getAttribute("docDetail")%>
				};
				onbindData(options);
			});
		</script>
	</head>

	<body style="background: url(<%=path%>/ebs/common/images/bg_content.jpg) center center repeat-x">
		<div>
			<input type="hidden" id="reportName" name="reportName" value="<s:property value='reportName'/>"/>
			<form method="post" id="myForm" name="myForm" >
    			<table id="autoTable" width="650px;" border="0" cellpadding="0" cellspacing="8px;" style="float: left;" >
    				<%@ include file="/ebs/paramsadmin/common/paramCommon.jsp"%>
				</table>
    		</form>
		</div>
	</body>
</html>
