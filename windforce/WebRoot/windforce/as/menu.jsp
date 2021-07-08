<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html >

<html>
<head>
<base href="<%=basePath%>" />
<title>自动调度子系统</title>

<meta http-equiv="content-type" content="text/html; charset=UTF-8" />

<link type="text/css" rel="stylesheet" href="<%=path%>/windforce/common/css/as.css" />
<link type="text/css" rel="stylesheet" href="<%=path%>/windforce/common/css/ztree.css" />
<%@ include file="/common/wf_import.jsp"%>
<script type="text/javascript" src="<%=path%>/common/js/jquery/ztree/jquery.ztree.all-3.1.js"></script>
<script type="text/javascript">
$(document).ready(function() {
$.getJSON("<%=path%>/getInitTaskInfo.action", function(json){
var setting = {
check: {
enable: true
},
view: {
dblClickExpand: false //双击扩展树是否启用。
},
callback: {
onMouseDown: zTreeOnMouseDown, /*给所有节点添加了鼠标左键按下的监听*/
onCheck: onCheck
}
};
//问题：怎么在IE端组织zNodes对象的值？
var zNodes = json;

$.fn.zTree.init($("#tree_orgmerge"), setting, zNodes);
});
});
function onCheck(e, treeId, treeNode) {
//选择了某一个选项。
}
function zTreeOnMouseDown(event, treeId, treeNode) {
//$("#orgName").val(treeNode.name);
};


function startThreads(subType)
{
//遍历树，看哪些节点被选择了。
var zTree = $.fn.zTree.getZTreeObj("tree_orgmerge"),
nodes = zTree.getChangeCheckedNodes();
var len = nodes.length;

if(len < 1)
{
top.wfAlert("请选择线程再操作!");
return;
}
else {
var ids = "";
for (var i=0; i<len; i++) {
 ids += nodes[i].sid+",";
}
// TODO这里还要判断选择的必须为线程
ids = ids.substring(0,ids.length-1);
document.getElementById("startthreads").value = ids;
if(subType == 'start')
{
document.forms["startForm"].action = 'startThreads.action';
$.getJSON("<%=path%>/startThreads.action", function(json) {
					var setting = {
						check : {
							enable : true
						},
						view : {
							dblClickExpand : false
						//双击扩展树是否启用。
						},
						callback : {
							onMouseDown : zTreeOnMouseDown, /*给所有节点添加了鼠标左键按下的监听*/
							onCheck : onCheck
						}
					};
					//问题：怎么在IE端组织zNodes对象的值？
					var zNodes = json;

					$.fn.zTree.init($("#tree_orgmerge"), setting, zNodes);
				});
			} else if (subType == 'stop') {
				document.forms["startForm"].action = 'stopThreads.action';
				document.forms["startForm"].submit();
			}
		}
	}
</script>
</head>
<body>
	<div style="margin-left: 0">
		<input type="button" value="启动" onclick="startThreads('start');" /><input
			type="button" onclick="startThreads('stop');" value="停止" /><input
			type="button" value="打开界面" /><input type="button" value="关闭界面" /><input
			type="button" value="刷新线程" /> <input type="button" value="切换布局" />
	</div>
	<form name="startForm" action="startThreads.action">
		<input id="startthreads" type="hidden" name="threads" />
	</form>
	<ul id="tree_orgmerge" class="ztree"></ul>
</body>
</html>