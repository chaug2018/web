<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<base href="<%=basePath%>"/>
<title>参数设置</title>
<meta http-equiv="pragma" content="no-cache"/>
<meta http-equiv="cache-control" content="no-cache"/>
<meta http-equiv="expires" content="0"/>    
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3"/>
<meta http-equiv="description" content="This is my page"/>

<link href="<%=path %>/ebs/common/css/css.css" rel="stylesheet" type="text/css" />
<link href="<%=path %>/ebs/common/css/dicss.css" rel="stylesheet" type="text/css" />
<link href="<%=path %>/ebs/common/css/jquery-css/demo_page.css" rel="stylesheet" type="text/css" />
<link href="<%=path %>/ebs/common/css/jquery-css/demo_table_jui.css" rel="stylesheet" type="text/css" />
<link href="<%=path %>/ebs/common/css/jquery-css/demo_table.css" rel="stylesheet" type="text/css" />		
<link href="<%=path %>/ebs/common/css/jquery-css/zTreeStyle.css" rel="stylesheet" type="text/css"/>
<link href="<%=path %>/common/css/ztree/ztree.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="<%=path%>/ebs/common/js/ebs-paraminput-1.0.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/jquery.js"></script>
<script type="text/javascript" src="<%=path %>/ebs/common/js/calendar.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/jquery.dataTables.js"></script>
<script type="text/javascript" src="<%=path %>/ebs/paramsadmin/js/jquery.ztree.core-3.3.min.js"></script>
<script type="text/javascript">
	var setting = {
			data: {
				simpleData: {
					enable: true
				}
			},
			check: {
				enable: true
			},
	        callback: {
		        onMouseDown: zTreeOnMouseDown /*给所有节点添加了鼠标左键按下的监听*/
	        }
	};

	function zTreeOnMouseDown(event, treeId, treeNode) {
		if (treeNode.tableName!="") {
			document.getElementById("tempCurrTable").value = treeNode.tableName;
        	document.getElementById("paramDetail").src = "<%=path %>/ParamsManagerAction_getReportData.action?reportName="+treeNode.tableName+"&isrefreshTree=";
		}
    };

	$(document).ready(function(){
 		var zNodes=<%=request.getAttribute("initJsonTree")%>; 
		$.fn.zTree.init($("#treeDemo"), setting, zNodes);
	});
</script>
</head>
<body style="height: auto;">
<input id="moudleName" type="hidden" value="参数设置"/>
<div id="nov_nr" >
  <span class="disblock"><img src="<%=path%>/ebs/common/images/paramsManageico/csgl.png" /></span>
  <div class="nov_moon">
	<table>
		<tr>
			<td style="vertical-align:top;">
				<div style="width:200px; float:left; height:auto;">
				<ul style="width:202px;" class="img_button2">
				    <li style=" padding-left:20px;padding-right:20px;padding-top:10px;"><a><img src="<%=path%>/ebs/common/images/paramsManageico/list.png" width="40px;"height="40px;" /><span style=" margin-top:10px; size:13px;">参&nbsp;&nbsp;数&nbsp;&nbsp;列&nbsp;&nbsp;表</span></a></li></ul><span style=" clear:both;display:block; height:20px;"></span>
				<ul id="treeDemo" class="ztree" style=" clear:both; padding:20px; border-top:1px dotted #CC6600;"></ul>
				</div>
			</td>
			<td >
				<input type="hidden" value="" id="tempCurrTable" name="tempCurrTable" />
				<iframe frameborder="0" style="background-color:#ebebe6;overflow:hidden "  src="<%=path %>/ParamsManagerAction_getReportData.action?reportName=Param_Credit" name="paramDetail" id="paramDetail" height="800" width="822px">
				</iframe>
			</td>
		</tr>
	</table>
	</div>
</div>
</body>
</html>