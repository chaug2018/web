<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"/>
<html>
  <head>
    <base href="<%=basePath%>"/>
    
    <title>My JSP 'paramDetail.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache"/>
	<meta http-equiv="cache-control" content="no-cache"/>
	<meta http-equiv="expires" content="0"/>    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3"/>
	<meta http-equiv="description" content="This is my page"/>
<link href="<%=path %>/ebs/common/css/jquery-css/demo_page.css" rel="stylesheet" type="text/css" />
<link href="<%=path %>/ebs/common/css/jquery-css/demo_table_jui.css" rel="stylesheet" type="text/css" />
<link href="<%=path %>/ebs/common/css/jquery-css/demo_table.css" rel="stylesheet" type="text/css" />			
<link href="<%=path %>/ebs/common/css/css.css" rel="stylesheet" type="text/css" />
<link href="<%=path %>/ebs/common/css/dicss.css" rel="stylesheet" type="text/css" />
<link href="<%=path %>/ebs/common/css/tipTip.css" rel="stylesheet" type="text/css"/>

<style type="text/css">
.middle {
text-align: center;
}
</style>
<script type="text/javascript" src="<%=path%>/ebs/common/js/jquery.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/jquery.dataTables.js"></script>
<script type="text/javascript" src="<%=path %>/ebs/paramsadmin/js/paramInquiry.js"></script>
<script language="javascript" src="<%=path %>/ebs/common/js/calendar.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/jquery.form.js"></script>
<script type="text/javascript" src="<%=path %>/ebs/paramsadmin/js/paramQuery.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	$(":submit").addClass("submit_but05");
	$(":reset").addClass("submit_but05");
	$("#autoTable tr:last td").attr("colspan","4").addClass("middle");

});
function initAddParam(){
	var tablename=$("#reportName").val();
	//top.showd("ParamsManagerAction_initAddParams.action?reportName="+tablename,660,350);
	var winHeight = 350;
	var winWidth = 660;
	var winTop = (window.screen.height - winHeight) / 2;
	var winLeft = (window.screen.width - winWidth) / 2;
	var sFeatures = "dialogHeight:" + winHeight + "px;dialogWidth:" + winWidth
			+ "px;dialogLeft:" + winLeft + "px;dialogTop:" + winTop + "px;";
	var returnValue = window.showModalDialog("ParamsManagerAction_initAddParams.action?reportName="+tablename,null,sFeatures);
	if(returnValue){
		// 刷新界面
		top.paramframeForward("ParamsManagerAction_getReportData.action?reportName="+document.getElementById("reportName").value);
	}
}



</script>
  </head>
  
  <body style="background-color: #ebebe6;overflow-x: hidden;">
    <input type="hidden" id="reportName" name="reportName" value="<s:property value='reportName'/>"/>
    <input type="hidden" id="powerName" name="currPowerName" value="<s:property value='reportName'/>"/>
	<input type="hidden" id="xmlName" name="currXmlName" value="<s:property value='reportName'/>.xml"/>
  <div style="width:auto; height:auto; float:left; margin-left:10px; padding:10px 0px 10px 0px;" class="border_bottom01">
  <ul style="width:762px; padding:0px; margin:0px;" class="img_button2">
    <li style=" margin-bottom:5px;"><a><img src="<%=path%>/ebs/common/images/paramsManageico/info.png" width="24px;"height="24px;" /><span style="size:13px;">参&nbsp;数&nbsp;信&nbsp;息</span></a></li></ul>
    <div style="clear:both; width:802px; overflow:hidden; border-top:1px dotted #CC6600;">
    <form method="post" id="myForm" name="myForm" >
    <table id="autoTable" width="650px;" border="0" cellpadding="0" cellspacing="8px;" style="float: left;" >
        <%@ include file="/ebs/common/inputlabel/selConditionCommon.jsp"%>
    </table>
    </form>
    <span><img style="float:left; margin-top:9px; margin-left:20px;" src="<%=path%>/ebs/common/images/paramsManageico/manager.png" /></span>
    </div>
  </div>
   <s:if test="reportName != 'Param_IdCenter'">
  <div id="div_img_ccgl">
  <ul style="width:762px;" class="img_button2">
    <li style="clear:none;"><a onclick="initAddParam()"><img src="<%=path%>/ebs/common/images/paramsManageico/add.png" width="24px;"height="24px;" /><span style=" size:13px;">新增参数</span></a></li>
    </ul></div>
    </s:if>
  <div  style=" float:left; margin-left:10px; width:808px; padding:5px 0px 10px 0px; overflow-x: scroll;overflow-y: hidden;">
    <h1><s:property value='paramName'/></h1>
    <table width="802px" cellpadding="0" cellspacing="0" border="0" class="display" id="example1" >
       </table>
  </div>
  </body>
</html>
