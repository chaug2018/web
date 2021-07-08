<%--
  机构信息树展现页面                 
@date          2012/04/25         
@author        蒋正秋   ,chenhuang
@version       1.0                
--%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
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

<link rel="stylesheet" type="text/css" href="<%=path%>/windforce/common/css/core.css" />
<link rel="stylesheet" type="text/css" href="<%=path%>/windforce/common/css/org.css" />
<link rel="stylesheet" type="text/css" href="<%=path%>/windforce/common/css/ztree_ext.css" />
<%@ include file="/common/wf_import.jsp"%>
<script type="text/javascript" src="<%=path%>/common/js/jquery/ztree/jquery.ztree.all-3.1.js"></script>

<title>管理导航菜单</title>
<script type="text/javascript">
var jsonData ;
var tempValue = "";
var tipMessage="请输入机构名称";
var allOrgs;
$(document).ready(function() {
$.getJSON("<%=path%>/getOrganizeList.action", "temp="+new Date(), function(json){
        var setting = {
	        callback: {
		        onMouseDown: zTreeOnMouseDown /*给所有节点添加了鼠标左键按下的监听*/
	        }
        };
        allOrgs=json;
		var zNodes =json;
		zTree=$.fn.zTree.init($("#tree_orgquery"), setting, zNodes);
		function zTreeOnMouseDown(event, treeId, treeNode) {
		if(treeNode==null||(event.button!=1&&event.button!=0)){
		return;
		}
		zTree.selectNode(treeNode);
		//  top是调用主页的指针，iframe跳转必须由主页来分发
		document.getElementById("selectedId").value = treeNode.sid;
		top.startProcess("正在初始化机构详细信息,请稍等...");
		top.frameForward("<%=path%>/organizeDetail.action?sid="+treeNode.sid+"&showPageType=1",treeNode.sid);
        };
		});
		
});			
// 点击添加按钮
function addOrg(){
       var currOrgId = document.getElementById("selectedId").value;
       //added by chenhuang 2013-1-23,判断是否选中初始父机构
		if(currOrgId == "")
		{
		top.wfAlert("请选中一个初始父机构！");
		 return false;
		} 
		top.startProcess("正在初始化机构新增界面,请稍等...");
      // 获取当前机构ID，用于添加页面初始化信息联动选中
      // 现判断是否为网点(网点为最基础机构，不能再添加子机构)
      $.post("<%=path%>/initAddOrganize_addFlag.action?sid="+currOrgId+"&requestType=operFlag", function(data){
 
      if("true" == data)
      {
       // top.changeProcessTitle("校验通过,正在初始化机构新增界面...");
       top.showPage("initAddOrganize_toAdd.action","sid="+currOrgId,"新增机构",510,125);
    	
      }else{
      top.stopProcess();
      top.wfAlert("此机构不能再创建子机构！");
      }
});
}
// 机构拆并
function splitOrg()
{
  var currOrgId = document.getElementById("selectedId").value;
      // 获取当前机构ID，用于添加页面初始化信息联动选中
      // 现判断是否为网点(网点为最基础机构，不能再添加子机构)
      $.post("<%=path%>/splitOrg_splitFlag.action?orgNo="+currOrgId, function(data){
       if("1" == data)
       {
       if(confirm("该机构暂无人员是否直接删除？"))
       {
       top.delOrg("<%=path%>/deleteOrganize.action?orgNo="+currOrgId);
       }
       }else if("2" == data)
       {
       top.wfAlert("该类型的机构不能拆并！");
       }else if("4" == data){
        top.wfAlert("该机构无兄弟机构，无法进行拆并操作！");
       }else if("3" == data)
       {
       var winHeight = 700;
	var winWidth = 650;
	var winTop = (window.screen.height - winHeight) / 2;
	var winLeft = (window.screen.width - winWidth) / 2;
	var sFeatures = "dialogHeight:" + winHeight + "px;dialogWidth:" + winWidth
			+ "px;dialogLeft:" + winLeft + "px;dialogTop:" + winTop + "px;";
			var returnValue = window.showModalDialog("<%=path%>/splitOrg_toSplit.action?orgNo="+currOrgId+"&time=" + (new Date()).getTime() , null, sFeatures);
			if(returnValue != "fail" && returnValue != "undefined" && returnValue != null)
			{
			document.getElementById("selectedId").value = returnValue;
		    top.frameForward("<%=path%>/organizeDetail.action?sid="+returnValue,returnValue);
		    // 刷新做菜单
		    top.refreshLeft();
			}
       }
      
});
}
// 点击修改按钮
function editOrg()
{
// 当前选择的机构
var currId = document.getElementById("selectedId").value;
// 如果没有选择的话则提示选择一条机构
if(currId == "")
{
 top.wfAlert("请选择要修改的机构！");
} else if(currId=="1000000000"){
 top.wfAlert("总行不能修改！");
}else 
{      top.startProcess("正在初始化机构编辑界面,请稍等...");
       top.showPage("editOrganize_editInit.action","sid="+currId,"机构修改 ",510,125);
}
}
// 点击删除按钮
function delOrg()
{

	// 当前选择的机构
	var currId = document.getElementById("selectedId").value;
	if(currId == "") {
		top.wfAlert("请选择一个机构再删除!");
	} else if(currId=="1000000000"){
		 top.wfAlert("总行不能删除！");
	} else {
		if(confirm("确定要删除该机构吗？")) {
			var operAuth=new top.OperAuth();
			operAuth.operType="deleteOrganization";
			operAuth.authSuccess=function(){
				top.startProcess("正在提交删除请求,请稍等...");
				top.delOrg("<%=path%>/deleteOrganize.action?sid="+currId);
			};	
			operAuth.auth();
		}
	}
}
function searchOn(){
var el = document.getElementById("autoComplete");
  if (el.value == tipMessage)
  {
    el.value = "";
    el.style.color = "";
    }
};
  function searchBlur(){
  var el = document.getElementById("autoComplete");
  if (el.value == "")
  {
    el.value = tipMessage;
    el.style.color = "gray";
    }
}
	// 自动检索
	function autoSearch()
	{
	var keyword = document.getElementById("autoComplete").value;
	if(tempValue != keyword)
	{
	var dataParaments = "completeWords="+keyword;
	$.post('<%=path%>/autoSearch.action', dataParaments, function(json) {
	var setting = {
	        callback: {
		        onMouseDown: zTreeOnMouseDown /*给所有节点添加了鼠标左键按下的监听*/
	        }
        };
		var zNodes =json;
		
		$.fn.zTree.init($("#tree_orgquery"), setting, zNodes);
				function zTreeOnMouseDown(event, treeId, treeNode) {
				if(treeNode==null){
				return;
				}
		//  top是调用主页的指针，iframe跳转必须由主页来分发
		document.getElementById("selectedId").value = treeNode.sid;
		top.frameForward("<%=path%>/organizeDetail.action?sid="+treeNode.sid,treeNode.sid);
        };
	},"json");
	}
	// 保留上一次的值
	tempValue = keyword;
	}
	
	function initZtree(json){
	var setting = {
	        callback: {
		        onMouseDown: zTreeOnMouseDown /*给所有节点添加了鼠标左键按下的监听*/
	        }
        };
		var zNodes =json;	
		$.fn.zTree.init($("#tree_orgquery"), setting, zNodes);
				function zTreeOnMouseDown(event, treeId, treeNode) {
				if(treeNode==null||(event.button!=1&&event.button!=0)){
					return;
					}
		zTree.selectNode(treeNode);
		top.startProcess("正在初始化机构详细信息,请稍等...");
		document.getElementById("selectedId").value = treeNode.sid;
		top.frameForward("<%=path%>/organizeDetail.action?sid="+treeNode.sid,treeNode.sid);
	 }
	}
	
	function resetQuery(){
	 document.getElementById("autoComplete").value=tipMessage;
	  initZtree(allOrgs);
	}
	
	
	function searchOrgs(){
	 var key=document.getElementById("autoComplete").value;
	 if(key==tipMessage){
	 	//initZtree(allOrgs);
	 	top.wfAlert("请输入机构名称或者机构号！");
	 	return;
	 }
	 if(searchType == 1 && !(top.isNull(key)) && !(top.isGeneralName(key))){
	 	top.wfAlert("机构名称含有非法字符!");
	 	return;
	 }
	 if(searchType == 2 && !(top.isNull(key)) && !(top.isAlphaAndDigits(key))){
	 	top.wfAlert("机构号只能是字母或数字!");
	 	return;
	 }
	var results=new Array();
	
	for(var i=0;i<allOrgs.length;i++){
	var temp_org=Clone(allOrgs[i]);
	var result= dealChildren(null,temp_org,key,false);
	if(result!=null){
	 results[results.length]=result;
	}
	}	
	initZtree(results);
	}
	
	function dealChildren(parentNode,child,key,isParentMatch){
	 var value;
	 var isMatch=false;
	 if(searchType==1){
	  value=child.name;
	 }else{
	  value=child.orgNo;
	 }
	  if((searchType==1&&value.indexOf(key)>-1&&value.indexOf(key)<value.indexOf("("))||searchType==2&&value.indexOf(key)>-1){
	  isMatch=true;
	  isParentMatch=true;
	  child.icon="<%=path%>/windforce/common/images/po_queryResult.png";
	  if(parentNode!=null){
	  parentNode.open=true;
	  }
	  }
	  var childrenResult = new Array();
	  var children=child.children;
	  var childResult;
	  if(children!=null){	
	  for(var i=0;i<children.length;i++){
	   childResult =dealChildren(child,children[i],key,isParentMatch);
	   if(childResult!=null){
	   childrenResult[childrenResult.length]=childResult;
	   if(child.open){
	     if(parentNode!=null){
	  	parentNode.open=true;
	  }
	   }
	   }
	  }
	  }
	  if(childrenResult!=null&&childrenResult.length>0){
	   child.children=childrenResult;
	  }else{
	  child.children=null;
	  if(!isMatch&&!isParentMatch){
	  return null;
	  }
	  }
	  return child;
	}
	
function Clone(obj){
    var objClone;
    if (obj.constructor == Object){
        objClone = new obj.constructor(); 
    }else{
        objClone = new obj.constructor(obj.valueOf()); 
    }
    for(var key in obj){
        if ( objClone[key] != obj[key] ){ 
            if ( typeof(obj[key]) == 'object' ){ 
                objClone[key] =Clone(obj[key]);
            }else{
                objClone[key] = obj[key];
            }
        }
    }
    objClone.toString = obj.toString;
    objClone.valueOf = obj.valueOf;
    return objClone; 
} 
	function showSelect(){
	 var select=document.getElementById("queryTypeSelect");
	 select.style.display="block";
	}
	function hideSelect(){
	 var select=document.getElementById("queryTypeSelect");
	 select.style.display="none";
	}
	
	var searchType=1;  //1代表按机构名搜索，2代表按机构号搜索
	function selectType(type){
	searchType=type;
	 var select=document.getElementById("queryTypeSelect");
	  var img=document.getElementById("searchTypeImg");
	  select.style.display="none";
	  img.src="<%=path%>/windforce/common/images/po_show_search_type.png";
	  if(type==1){
	   tipMessage="请输入机构名称";
	  }else{
	  tipMessage="请输入机构号";
	  }
	  document.getElementById("autoComplete").value=tipMessage;
	}
	
	function showBg(item){	
	item.style.background="#4876FF";	
	}
	
	function clearBg(item){
	item.style.background="";
	}
	</script>
</head>
<body style="background-color: #EBEBE6;width:100%">
	<span class="disblock margin_top01" style="padding-left:4px"><input
		class="submit_but08" onclick="addOrg();" type="button" value="添加" /><input
		class="submit_but08" type="button" onclick="editOrg();" value="修改" /><input
		class="submit_but08" type="button" onclick="delOrg();" value="删除" />
		<!--  added by chenhuang 2013-1-24 1.0版本屏蔽该功能
		<input class="submit_but08" type="button" value="拆并" onclick="splitOrg();" />&nbsp;
		--> </span>

	<div style="position:absolute;padding-left:4px;z-index: 100;">
		<input id="autoComplete" type="text"
			style="margin-right: 0px;color: gray;width:145px;" value="请输入机构名称"
			onblur="searchBlur()" onfocus="searchOn()" maxlength="50" />
	</div>
	<div style="position:absolute;margin-left:134px;z-index: 101;">
		<table>
			<tr>
				<td><img id="searchTypeImg" title="设置搜索类型" width="16px"
					height="16px" style="padding-top:3px;" onmouseout="hideSelect()"
					onmouseover="showSelect()"
					src="../../common/images/po_show_search_type.png" /></td>
				<td>
					<a>
						<img onclick="searchOrgs()" width="18px" height="18px" title="搜索" style="padding-top:1px;padding-left:2px"
						src="../../common/images/po_search.png" />
					</a>
				</td>
				<td>
					<a>
						<img onclick="resetQuery()" width="18px" height="18px" title="重置" style="padding-top:1px;padding-left:2px"
							src="../../common/images/po_resetQuery.png" />
					</a>
				</td>
			</tr>
		</table>
	</div>
	<div id="queryTypeSelect" onmouseout="hideSelect()"
		onmouseover="showSelect()"
		style="position:absolute;margin-left:90px;margin-top:20px;width:105px;height:auto;z-index:101;background:#FFFFFF;display:none;">
		<ul style="margin-top:4px;">
			<li onclick="selectType(1)" onmousemove="showBg(this);"
				onmouseout="clearBg(this)"
				style="text-align:right; list-style-type:none;">按机构名进行查询&nbsp;</li>
			<li onclick="selectType(2)" onmousemove="showBg(this);"
				onmouseout="clearBg(this)"
				style="text-align:right; list-style-type:none;">按机构号进行查询&nbsp;</li>
		</ul>
	</div>

	<div class="cc_ul"
		style="position:absolute;margin-top:15px;z-index:100;">
		<ul id="tree_orgquery" class="ztree" style="width:100%">
		</ul>
	</div>
	<input type="hidden" id="selectedId" />
	<script type="text/javascript">
document.getElementById("tree_orgquery").style.height = top.getWindowHeight()-top.getTopSize()-80 + "px";
</script>
</body>
</html>

