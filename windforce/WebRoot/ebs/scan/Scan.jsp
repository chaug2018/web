<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
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
<title>影像采集</title>
    <meta http-equiv="pragma" content="no-cache"/>
	<meta http-equiv="cache-control" content="no-cache"/>
	<meta http-equiv="expires" content="0"/>    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3"/>
	<meta http-equiv="description" content="This is my page"/>
<script type="text/javascript"
	src="<%=path%>/common/js/jquery/jquery-1.7.1.js"></script>
<script type="text/javascript"
	src="<%=path%>/ebs/common/js/process.js"></script>
<link href="<%=path%>/ebs/common/css/process.css" rel="stylesheet"
	type="text/css" />
<script type="text/javascript">
		var err = "<s:property value='errMsg'/>";
		if (err != null && err.length > 0) {
			alert("初始化业务参数出现错误");
			top.refresh();
		}
</script>
<script type="text/javascript">
	function stop(){
	//document.all.scanApplet.style.visibility= "visible "; 
		stopProcess();
	}
</script>
</head>
<body>
<input id="moudleName" type="hidden" value="影像采集"/>
<%@ include file="/ebs/taskviewtool/processing_div.jsp"%>
<script type="text/javascript">startProcess("正在初始化界面。。。");</script>

  <div class="scanApplet" style= "width:100%;height:100%" >

	<object style="WIDTH: 100%; HEIGHT: 100%" id="scanApplet" name="scanApplet" 
						classid=clsid:CAFEEFAC-0016-0000-0000-ABCDEFFEDCBA>
						<param name="codebase" value="." />
						<param name="code" value="com.yzj.wf.ebs.scanapplet.applet.MainApp" />
						<param name="archive" value="applet/Scan.jar" />
						<param name="name" value="scanApplet" />
						<param name="type" value="application/x-java-applet;version=1.6" />
						<param name="scriptable" value="true" />
						<param name="mayscript" value="true" />
						<!-- 以下为自定义参数 -->
						<param name=bizServerAdress value="<s:property value='bizServerAdress'/>"></param>
						<param name=imageServerAdress value="<s:property value='imageServerAdress'/>"></param>
						<param name=userId value="<s:property value='userId'/>"></param>
						<param name=orgId value="<s:property value='orgId'/>"></param>
						<param name=busTypes value="1,对账单"></param>
						<param name=autoFormat value="1"></param>
						<param name=language value="Scan_ch_ebs"></param>
						
						<applet style="WIDTH: 100%; HEIGHT: 100%" id="scanApplet"
			name="scanApplet" code="com.yzj.wf.ebs.scanapplet.applet.MainApp.class"
			archive="applet/Scan.jar">
			<!-- 以下为自定义参数 -->
			<param name=bizServerAdress
				value="<s:property value='bizServerAdress'/>"></param>
			<param name=imageServerAdress
				value="<s:property value='imageServerAdress'/>"></param>
			<param name=userId value="<s:property value='userId'/>"></param>
			<param name=orgId value="<s:property value='orgId'/>"></param>
			<param name=busTypes value="1,对账单"></param>
			<param name=autoFormat value="1"></param>
			<param name=language value="Scan_ch_ebs"></param>
		</applet>
					</object>
					
	
	
	</div>
	<script type="text/javascript">
			var applet = document.getElementById("scanApplet");
			applet.style.height =top.getWindowHeight()-top.getTopSize()-50+"px";
			applet.width = "100%";
			setTimeout("stop()","1000");		
	</script>
</body>
</html>


