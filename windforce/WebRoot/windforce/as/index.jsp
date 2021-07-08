<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<base href="<%=basePath%>" />

<title>自动阵列管理</title>

<meta http-equiv="content-type" content="text/html; charset=UTF-8" />

<link type="text/css" rel="stylesheet" href="<%=path%>/windforce/common/css/ztree.css" />
<link type="text/css" rel="stylesheet" href="<%=path%>/windforce/common/css/core.css" />
<link type="text/css" rel="stylesheet" href="<%=path%>/windforce/common/css/as.css" />
<%@ include file="/common/wf_import.jsp"%>
<script type="text/javascript" src="<%=path%>/windforce/common/js/as-jquery.ztree.core-extend.3.1.js"></script>
<script type="text/javascript" src="<%=path%>/common/js/jquery/ztree/jquery.ztree.excheck-3.1.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	$("#start").bind("click",function(){
		startThreads('start','startThreads.action');
	});
	$("#stop").bind("click",function(){
		startThreads('stop','stopThreads.action');
	});
});
</script>
</head>
<body style="background-color: transparent;">
	<span class="disblock">
	<img src="<%=path%>/windforce/common/images/as_zddujk.png" /> 
	</span>
	<div class="nov_moon">
		<div id="top">
			<h2 style="background: none;">自动任务列表</h2>
			<div class=split></div>
			<div id="topTags">
				<ul id="topmenus">
				</ul>
			</div>
		</div>
		<div id="main">
			<div style="margin-left: 10px; margin-top: 10px;">
				<ul id="changceManager">
					<li><a id="start"><img
							src="<%=path%>/windforce/common/images/as_start.png" width="24"
							height="24" /><span class="imgtext">启动</span> </a></li>
					<li><a id="stop"><img
							src="<%=path%>/windforce/common/images/as_stop.png" width="24"
							height="24" /><span class="imgtext">停止</span> </a></li>
					<!-- <li><a id="stop"><img
								src="<%=path%>/windforce/common/images/as_refresh.png" width="24" height="24" /><span
								class="imgtext">刷新</span> </a>
						</li> -->
				</ul>
			</div>
			<form name="startForm" action="startThreads.action">
				<input id="startthreads" type="hidden" name="threads" />
			</form>
			<ul id="tree_orgmerge" class="ztree" style="border:none;width:430px;">
			</ul>
			<div class=split></div>
			<div id="content">
				<div id="welcome" class="content" style="display: block;">
					<div align="center">
						<p>
							<strong>自动阵列业务展现</strong>
						</p>
					</div>
				</div>
			</div>
		</div>
		<div id="footer">版权所有 © 深圳市银之杰科技股份有限公司</div>
	</div>
	<script type="text/javascript">
// 定时刷新内容变量 start
var id;
var interval = 1000;
var conIndex ;
var conName;
var threadId = 0;
// end
var contentIndex = 0;
var leftMenu;
initTreeList();
function initTreeList()
{
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
	},
	async :{enable : true}
	};
	//问题：怎么在IE端组织zNodes对象的值？
	var zNodes = json;
	
	$.fn.zTree.init($("#tree_orgmerge"), setting, zNodes);
	 leftMenu = $("li:has(.level1)").size();
	 // 添加对应的内容模块
	 for(var c = 0 ; c < leftMenu ;c++)
	 {
	 
	 var moduleIndex = "<div id='c"+c+"' class=\"content\">"+c+"</div>";
	 $("#content").append(moduleIndex);
	 }
	});
};

function setBizData()
{
// TODO 这里要根据模块ID判断限制具体的业务模块数据
//var lengthData = $("#"+"c"+conIndex+" table tr").size();
// 控制最大长度显示200条

if(conIndex != -1)
{
$.post("<%=path%>/showBizDatas.action?taskName="+conName, function(data){
if(document.getElementById("c"+conIndex)){
document.getElementById("c"+conIndex).innerHTML = data;
document.getElementById("c"+conIndex).scrollTop = document.getElementById("c"+conIndex).scrollHeight;
}
//document.getElementById("c"+conIndex).style.bottom =(document.documentElement.scrollTop+(document.documentElement.clientHeight-document.getElementById("c"+conIndex).offsetHeight)/2)+"px";

});
}
}
function onCheck(e, treeId, treeNode) {

}
function zTreeOnMouseDown(event, treeId, treeNode) {
	if(treeNode != null) {
		//选择了某一个选项。
		var id = treeNode.tId;
		// 索引
		var sindex = $("#"+id).attr("name") ;
		// 名称
		var treeName = treeNode.name;
		//截取超长的字符串
		if(treeName != "" && treeName.length > 5) {
			treeName = treeName.substring(0,5)+"..";
		}
		if(sindex != "" && sindex != null) {
			treeNode.name = treeNode.name.replace("界面未开启","界面已开启");
			var zTree = $.fn.zTree.getZTreeObj("tree_orgmerge");
			zTree.updateNode(treeNode);
			if(document.getElementById("p"+sindex)==null){
				openNew(sindex,treeName);
			} else {
				// 如果是已经打开的菜单则只需要控制业务数据面板显示就行了
				clearContent();
				document.getElementById("c"+sindex).style.display="block";
				clearStyle();
				document.getElementById("p"+sindex).style.background='url(<%=path%>/windforce/common/images/as_tabbg1.gif)';
			}
			// 2012-06-12 请求模块信息时添加主机信息
			var hostInfo = treeNode.getParentNode().sid;
			var requestInfo = hostInfo+":"+treeNode.sid;
			$.post("<%=path%>/showBizDatas.action?taskName=" + requestInfo,
				function(data) {
					document.getElementById("c" + sindex).innerHTML = data;
					conIndex = sindex;
					conName = requestInfo;
					window.clearInterval(setBizData);
					if (threadId < 1) {
						threadId = window.setInterval(setBizData, interval);
					}
			});
		}
	}
};

function startThreads(subType, url) {
	//遍历树，看哪些节点被选择了。
	var zTree = $.fn.zTree.getZTreeObj("tree_orgmerge"), nodes = zTree
			.getChangeCheckedNodes();
	var len = nodes.length;

	if (len < 1) {
		top.wfAlert("请选择线程再操作!");
		return;
	} else {
		var hosts = {};
		for ( var i = 0; i < len; i++) {
			// 先获取host配置,用json对象来判断是否有重复

			if ($("#" + nodes[i].tId).attr("class") == "level2") {
				var currRoot = nodes[i].getParentNode().getParentNode().sid;
				if (hosts[currRoot] != null && hosts[currRoot] != "") {
					hosts[currRoot] = hosts[currRoot] + nodes[i].sid
							+ ",";
				} else {
					hosts[currRoot] = nodes[i].sid + ",";
				}
			}
		}
		var returnChar = "";
		for (key in hosts) {
			returnChar += key + ":"
					+ hosts[key].substring(0, hosts[key].length - 1)
					+ "|";
		}

		// TODO这里还要判断选择的必须为线程
		returnChar = returnChar.substring(0, returnChar.length - 1);

		url = url + "?threads=" + returnChar;
		$.getJSON("<%=path%>/"+url, function(json){
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
	
	}
}
</script>
<script type="text/javascript">
var menu;//顶部菜单容器
var tags;//顶部菜单
window.onload=function(){
	function $(id){
		return document.getElementById(id);
	}
	menu=$("topTags").getElementsByTagName("ul")[0];
   	tags=menu.getElementsByTagName("li");//顶部菜单
};
//增加或删除标签
function openNew(id,name){
	var tagMenu=document.createElement("li");
	tagMenu.id="p"+id;
	tagMenu.style.background='url(<%=path%>/windforce/common/images/as_tabbg1.gif)';
	tagMenu.innerHTML=name+"&nbsp;&nbsp;"+"<img src='<%=path%>/windforce/common/images/as_off.gif' style='vertical-align:middle'/>";
	document.getElementById("welcome").style.display="none";
	clearContent();
	document.getElementById("c"+id).style.display="block";
	clearStyle();
	tagMenu.style.background='url(<%=path%>/windforce/common/images/as_tabbg1.gif)';

	//标签点击事件
	tagMenu.onclick=function(evt){
		clearStyle();
		tagMenu.style.background='url(<%=path%>/windforce/common/images/as_tabbg1.gif)';
		clearContent();
		document.getElementById("c"+id).style.display="block";
		var hostInfo = $("#tree_orgmerge li[name="+id+"]").attr("hostAndTName");
		// 设置当前刷屏数据ID
		conIndex = id;
		conName = hostInfo;
	};
	
	//标签内关闭图片点击事件
	tagMenu.getElementsByTagName("img")[0].onclick = function(evt){
		evt= (evt) ? evt:((window.event)?window.event:null);
		if(evt.stopPropagation){//取消opera和Safari冒泡行为;
			evt.stopPropagation();
		} 
		var currId = $("#topmenus li:first").attr("id");
		var currOpPage = this.parentNode.id;
		this.parentNode.parentNode.removeChild(tagMenu);//删除当前标签
		//设置如果关闭一个标签时，让最后一个标签得到焦点
		// 根据P的ID获取索引---这里是关闭一个标签后显示第一个标签与内容
		var pIndex = currId.substr(1);
		if(tags.length-1>=0){
			clearStyle();
			tags[tags.length-1].style.background='url(<%=path%>/windforce/common/images/as_tabbg1.gif)';
	
			clearContent();
	
			var hostInfo = $("#tree_orgmerge li[name="+pIndex+"]").attr("hostAndTName");
			document.getElementById("c"+pIndex).style.display = "block";
			window.clearInterval(setBizData);
	
			// 设置当前刷得数据ID
			conIndex = pIndex;
			conName = hostInfo;
	 	} else {
			conIndex = -1;
			window.clearInterval(setBizData);
			clearContent();
			document.getElementById("welcome").style.display="block";
	   	}
		// 关闭界面后改变状态
		var zTree = $.fn.zTree.getZTreeObj("tree_orgmerge");
		var hostInfo = $("#tree_orgmerge li[name="+currOpPage.substr(1)+"]").attr("id");
		var node = zTree.getNodeByTId(hostInfo);
		node.name = node.name.replace("界面已开启","界面未开启");
		zTree.updateNode(node);
	};
	menu.appendChild(tagMenu);
}

//清除标签样式
function clearStyle(){
	for(var i=0;i<tags.length;i++){
		menu.getElementsByTagName("li")[i].style.background='url(<%=path%>/windforce/common/images/as_tabbg2.gif)';
	}
}
//清除内容
function clearContent() {
	for (var i=0; i < leftMenu; i++) {
		document.getElementById("c" + i).style.display = "none";
	}
}
// 定时刷新左边树
function refreshLeftTree() {
	initTreeList();
}
window.setInterval(refreshLeftTree, 1000 * 60);
function setbgOn(tr) {
	tr.style.backgroundColor = '#FFFFFF';
}
function setbgOut(tr) {
	tr.style.backgroundColor = '#e8f2f6';
}
</script>
</body>
</html>