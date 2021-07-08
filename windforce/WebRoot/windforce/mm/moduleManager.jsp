<%--
  机构信息树展现页面                 
@date          2012/04/25         
@author        蒋正秋              
@version       1.0                
--%>
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
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>管理导航菜单</title>
<link type="text/css" rel="stylesheet" href="<%=path%>/windforce/common/css/mm.css" />
<link type="text/css" rel="stylesheet" href="<%=path%>/windforce/common/css/ztree.css" />
<link type="text/css" rel="stylesheet" href="<%=path%>/windforce/common/css/core.css" />
<%@ include file="/common/wf_import.jsp"%>
<script type="text/javascript" src="<%=path%>/common/js/jquery/ztree/jquery.ztree.all-3.1.js"></script>
<script type="text/javascript">
function onloadTree(){
$.getJSON("<%=path%>/modulesInitAction.action", function(json){
        var setting = {
        check: {
		enable: true,
		chkStyle: "checkbox",
		chkboxType: { "Y": "s", "N": "s" }
		    },
	     callback: {
		        onMouseDown: zTreeOnMouseDown /*给所有节点添加了鼠标左键按下的监听*/
	        }
        };
		var zNodes =json;
		zTree=$.fn.zTree.init($("#tree_orgquery"), setting, zNodes);
		function zTreeOnMouseDown(event, treeId, treeNode) {
          //  $("#orgName").val(treeNode.name);	
			if(treeNode==null||(event.button!=1&&event.button!=0)){  //若点击复选框时treeNode为null
			return;
			}
			zTree.selectNode(treeNode);
          document.getElementById("tempCurrId").value = treeNode.sid;
          top.startProcess("正在获取模块详细信息,请稍等...");
         document.getElementById("moduleOperate").src = "<%=path%>/modules_getModuleDetail.action?miSid="+treeNode.sid+"&isrefreshTree=";
         	        
        };
		
		$(document).ready(function($) { 
	        $("#btn_ok").click(function() { 
			    var str = $("#slt_orgtype option:selected").val();				
				String.prototype.Trim = function() {//声明一个原型函数，去掉本字符串的所有空格回车符。
	                return this.replace(/(^\s*)|(\s*$)|(\n)/g, ""); 
                };
				str = str.Trim();			
				if(str == "请选择机构类型：") {
					top.wfAlert(str);
				}   
				if($("#chx_isself").attr("checked") == true) {
					top.wfAlert("选择了自主管理！");
				}       
				//测试，循环创建select的option. 
				for(var i = 0; i < 3; i++) {
                    $("#slt_orgup").append("<option>上级机构</option>"); 					
				}				 
	        });
        });	
		
		$(document).ready(function($) { 
	        $("#btn_cancel").click(function() { 
                top.wfAlert("cancel"); 
	        });
        });	
  
		$(document).ready(function($) { 
	        $("#slt_orgtype").change(function() { 
                top.wfAlert("机构类型被选择！"); 
	        });
        });	
				});	
}
function addModule()
{
var currId = document.getElementById("tempCurrId").value;
top.startProcess("正在初始化模块新增界面,请稍等...");
document.getElementById("moduleOperate").src = "<%=path%>/modules_InitAdd.action?miSid="+currId;
}
// 修改模块
function updateModule(){
	var currId = document.getElementById("tempCurrId").value;
	if(currId !="00000000000000000000000000000000")
	{
		if(currId == null || currId == "")
		{
		top.wfAlert("请选择要修改的模块！");
		}else
		{
		top.startProcess("正在初始化模块修改界面,请稍等...");
		document.getElementById("moduleOperate").src= "<%=path%>/modules_initUpdate.action?miSid="
						+ currId;
		}
	} else {
		top.wfAlert("您好，根模块不能修改！");
	}
}
	// 执行删除
function doDelete() {
var zTree = $.fn.zTree.getZTreeObj("tree_orgquery"), nodes = zTree
				.getCheckedNodes();
		var len = nodes.length;
		var ids = "";
		if (len < 1) {
			top.wfAlert("请勾选要删除的模块！");
			return;
		}
	if (confirm("确定要删除所选模块吗？(若删除父模块，所有子模块都会被删除)")) {
			for ( var s = 0; s < nodes.length; s++) {
			if(nodes[s].children){
				checkAllchildren(zTree,nodes[s]);
				}
			}
			nodes = zTree.getCheckedNodes();
			for ( var s = 0; s < nodes.length; s++) {
				ids += nodes[s].sid + ",";
			}
			top.startProcess("正在提交删除请求,请稍等...");
			// 请求删除
			$.post("<%=path%>/modules_doDelete.action?reIds="+ids, function(data){
			if(data == "success")
			{
			top.changeProcessTitle("删除完毕,正在刷新界面...");
			top.wfAlert("删除成功！");
			onloadTree();
			document.getElementById("moduleOperate").src = "<%=path%>/modules_getModuleDetail.action";
								} else if (data.substring(0, 4) == "warn") {
									top.stopProcess();
									top.wfAlert(data.substring(4));
								} else {
									top.stopProcess();
									top.wfAlert("删除失败！请联系管理员。");
								}
							});
		}
	}

	function checkAllchildren(ztree, node) {
		ztree.checkNode(node, true);
		if (node.children && node.children.length > 0) {
			for ( var i = 0; i < node.children.length; i++) {
				checkAllchildren(ztree, node.children[i]);
			}
		}
	}

	$(document).ready(
			function() {
				var size = 80;
				document.getElementById("moduleOperate").style.height = (top
						.getWindowHeight()
						- top.getTopSize() - size)
						+ "px";
				document.getElementById("tree_orgquery").style.height = (top
						.getWindowHeight()
						- top.getTopSize() - size - 20)
						+ "px"; //做成树有滚动条减掉20就好了，
				document.getElementById("nov_nr").style.height = (top
						.getWindowHeight()
						- top.getTopSize() - size)
						+ "px";
				document.getElementById("uoon").style.height = (top
						.getWindowHeight()
						- top.getTopSize() - size)
						+ "px";
			});
</script>
</head>
<body class="body_background" onload="onloadTree();">
	<input id="moudleName" type="hidden" value="模块管理" />
	<div id="nov_nr" style="margin-top: 5px;">

		<div class="nov_moon"
			style="background-color: transparent;width:100%;height:100%;">
			<table width="100%" border="0" align="center" cellpadding="0"
				cellspacing="0">

				<tr>
					<td width="20%"><input type="button" onclick="addModule();"
						class="submit_but05" value="新增" />&nbsp;<input type="button"
						class="submit_but05" value="修改" onclick="updateModule();" />&nbsp;<input
						type="button" class="submit_but05" value="删除"
						onclick="doDelete();" />&nbsp;
						<div id="uoon" class="uoon"
							style="overflow-x:hidden;overflow-y: auto; overflow-x:auto; height: 95%;width:95%;border: 4px solid #A4B5C9;">
							<ul id="tree_orgquery" class="ztree" style="margin-top:0px"></ul>
						</div> <input name="temp" type="hidden" id="tempCurrId" /></td>
					<td valign="top" width="59%">&nbsp;<iframe frameborder="0"
							id="moduleOperate" name="moduleOperate"
							src="<%=path%>/modules_getModuleDetail.action?miSid="
							style=" width: 95%; height: 100%; margin-top: 25px;background-color: transparent;overflow: hidden;" />
					</td>
				</tr>
			</table>
		</div>

	</div>

</body>
</html>

