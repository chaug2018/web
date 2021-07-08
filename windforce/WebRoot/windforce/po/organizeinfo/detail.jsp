<%--
  机构信息详细页面                 
@date          2012/04/25         
@author        蒋正秋              
@version       1.0                
--%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.yzj.wf.common.util.StringUtils"%>
<%@page import="com.yzj.wf.core.model.po.PeopleInfo"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
	+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>机构人员管理</title>
<!-- 汉字转拼音JS -->
<link type="text/css" rel="stylesheet" href="<%=path%>/windforce/common/css/core.css" />
<%@ include file="/common/wf_import.jsp"%>
<script type="text/javascript" src="<%=path%>/common/js/pinyinConvert.js"></script>

<script type="text/javascript">
var tempValue = "";
function onAutoTextChange(searchType,firstChar)
{
	var currValue = document.getElementById("autoComplete").value;
	if(tempValue != currValue || searchType == "2")
	{
		var currTrHtml = "";
	<%// 临时变量，用户保存当前用户搜索结果
	StringBuffer tempJsonData = new StringBuffer("");
	List<PeopleInfo> showList = (List<PeopleInfo>)request.getAttribute("userList");
	for(PeopleInfo people : showList)
	{
		tempJsonData.append("{\"peopleName\" :"+"\""+people.getPeopleName()+"\","
	                    +"\"peopleCode\" :"+"\""+people.getPeopleCode()+"\","
	                    +"\"peopleSid\" :"+"\""+people.getSid()+"\","
	                    +"\"peopleGender\" :"+"\""+people.getPeopleGender()+"\"},");
	}
	String initData = "";
	if(!StringUtils.isNullOrBlank(tempJsonData.toString()))
	{
		initData = tempJsonData.toString();
		// 去掉最后一个逗号
		initData = initData.substring(0,initData.length()-1);
	}%>

// 拼装html语句
	var jsonDatas = [<%=initData%>];
	var i = 1;
	var resultCount = 0;
	var trColor = "#CBD6E0";
	$("tr").detach(".gradeX");
	for(people in jsonDatas)
	{
		if(i%2==0)
		{
			trColor = "#CBD6E0";
		}else {
			trColor = "#FFFFFF";
		}
		var isContain = false;
		var startChar = currValue.substring(0,1);
		var strLen = currValue.length;
		if(searchType == "2")
		{
			strLen = 1;
		} else if("本页搜索:姓名、拼音" == currValue)
		{
			strLen = 0;
			startChar="";
		}

		for(var j = 0 ; j < CC2PY(jsonDatas[people]["peopleName"]).length-strLen+1 ; j++)
		{
		// 类型1为自动搜索
			if(searchType == "1")
			{ 
				var py = CC2PY(jsonDatas[people]["peopleName"]);
		 		if(jsonDatas[people]["peopleName"].charAt(j) == startChar)
		  		{
		  
		  			if(jsonDatas[people]["peopleName"].substring(j,j+strLen)==currValue )
		   			{
		   				isContain = true;
		   			}
		  		}
		  		if(py.substring(j,j+strLen).toUpperCase() == currValue.toUpperCase())
		  		{
		   			isContain = true;
		  		}
	  		}
			else 
	  		{
	 	 		// 改变当前点击字母的背景色
		 		 document.getElementById(firstChar).style.backGround = "#FFFFFF";
		  		 var py = CC2PY(jsonDatas[people]["peopleName"].substring(0,1));
		  		// 用户名的首个字的拼音首字母相等，或者英文名的首字母相等时
		 		 if(py.substring(0,1) == firstChar || jsonDatas[people]["peopleName"].substring(0,1).toUpperCase() == firstChar || firstChar== "ALL")
		 	 	 {
		  			isContain = true;
		  		 }
	  		}
		}
	// 如果是点击字母时则判断汉字开头的拼音首字母也要匹配
		if(isContain)
		{
			var userName = jsonDatas[people]["peopleName"];
			var userCode = jsonDatas[people]["peopleCode"];
			var userSid = jsonDatas[people]["peopleSid"];

			var sex = jsonDatas[people]["peopleGender"] == 0 ? "男" : "女";
        	var htmlData = "<tr class=\"gradeX\" bgcolor=\""+trColor+"\" ondblclick=\"javascript:top.userDetail('"+userCode+"');\">"
                                    +"<td onclick=\"setUserDetail(this);\" align=\"center\">"+i+"</td>"
                                    +"<td onclick=\"setUserDetail(this);\" align=\"center\">"+userName+"</td>"
                                    +"<td onclick=\"setUserDetail(this);\" align=\"center\">"+userCode+"</td>"
                                    +"<td onclick=\"setUserDetail(this);\" align=\"center\">"
                                    +sex
                                    +" </td>"
                                    +"<td align=\"center\"><a name=\"addPeople\" id=\"addPeople\" style=\"color:#FF0000\" onclick=\"editUser('"+userSid+"');\">修改</a>" 
                                    +"/<a style=\"color:#FF0000\" onclick=\"delPeople('"+userSid+"','<s:property value='organizeInfo.orgNo'/>')\">删除</a>"
                                    +"/<a style=\"color:#FF0000\" onclick=\"resetPwd('"+userSid+"')\" >重置密码</a>"
                                    +"</td>"
                                    + "</tr>";
           	i++;
           	resultCount++;
           	$("#trAutoComplete").append(htmlData);
		}
		isContain = false;
	}
	$("#userCount").html(resultCount);
  }
	// 保留上一次的值
  tempValue = currValue;
}



// 点击添加用户按钮
function addPeople(){
	var orgId = "<s:property value='organizeInfo.sid'/>";
 	top.startProcess("正在初始化人员新增界面,请稍等...");
 	top.showPage("initAddUserGroup.action",
 			"orgSid="+orgId,"新增人员",530,270);
}

// 点击修改按钮
function editUser(userId)
{
 	top.startProcess("正在初始化人员修改界面,请稍等...");
	top.showPage("editUser_initEditPeople.action","initUserId="+userId,"编辑人员",530,270);
}

// 点击删除按钮
function delPeople(sid,orgId)
{
	// 当前选择的机构
	orgId = "<s:property value='organizeInfo.sid'/>";
	if(sid=="7B92AE0FC4B04DB48F1AFBDB22CD7188"){
	top.wfAlert("超级管理员不能被删除");
	return;
}
if(confirm("确定要删除该人员吗？"))
{
	var operAuth=new top.OperAuth();
	operAuth.operType="deletePeople";
	operAuth.authSuccess=function(){
		top.startProcess("正在提交删除请求,请稍等...");
		top.delPeople("<%=path%>/doDelUser.action?sid="+sid,orgId);
	};
	operAuth.auth();
}

}
// 水印提示
function searchOn(){
	var el = document.getElementById("autoComplete");
    if (el.value == "本页搜索:姓名、拼音")
  	{
   		el.value = "";
    	el.style.color = "";
    }
};

function searchBlur(){
  	var el = document.getElementById("autoComplete");
  	if (el.value == "")
  	{
    	el.value = "本页搜索:姓名、拼音";
    	el.style.color = "gray";
    }
}

// 单击用户列表设置背景颜色
function setUserDetail(td)
{
	var trColor = "#CBD6E0";
	var trs = $("#trAutoComplete tr");
	for(var i = 0 ; i < trs.size();i++)
	{
		if(i%2==0)
		{
			trColor = "#CBD6E0";
		}else {
			trColor = "#FFFFFF";
		}
		$("#trAutoComplete tr").eq(i).css("backgroundColor",trColor);
		$("#trAutoComplete tr").eq(i).css("color","");
	}
	td.parentNode.style.backgroundColor = "#6699FF";
	td.parentNode.style.color = "#FFFFFF";
};

function resetPwd(peopleId)
{
	if(confirm("您确定要重置该用户密码?")){
		$.post("<%=path%>/resetPwdAction.action",
				"userNo="+peopleId, 
				function(data) {
 					top.wfAlert(data);
 		});
 	}
}
 
 function prePage(){
 	if( document.getElementById("prePage").disabled==true){
 		return;
 	}
	  top.startProcess("正在获取人员信息,请稍等...");
	  var curPage=document.getElementById("curPage");
	  $.post("<%=path%>/getPeoplesByPage.action", "curPage="+(parseInt(curPage.innerText)-1), function(data) {
	           $("#peopleTable").html(data);
	            curPage.innerText=parseInt(curPage.innerText)-1;
	            changePageButton();
	            top.stopProcess();
	 });
 }
 
 function nextPage(){
 	if( document.getElementById("nextPage").disabled==true){
 		return;
 	}
	top.startProcess("正在获取人员信息,请稍等...");
 	var curPage=document.getElementById("curPage");
	$.post("<%=path%>/getPeoplesByPage.action", 
			  "curPage="+(parseInt(curPage.innerText)+1), 
			  function(data) {
	          	$("#peopleTable").html(data);
	            curPage.innerText=parseInt(curPage.innerText)+1;
	            changePageButton();
	            top.stopProcess();
	 });
 }
 
 function firstPage(){
  	  if( document.getElementById("firstPage").disabled==true){
 			return;
 	  }
 	  top.startProcess("正在获取人员信息,请稍等...");
  	  var curPage=document.getElementById("curPage");
	  $.post("<%=path%>/getPeoplesByPage.action", 
			  "curPage=1", 
			  function(data) {
	            $("#peopleTable").html(data);
	           	curPage.innerText=1;
	            changePageButton();
	            top.stopProcess();
 	 });
 }
 
 function lastPage(){
  	if( document.getElementById("lastPage").disabled==true){
 		return;
 	}
  	top.startProcess("正在获取人员信息,请稍等...");
 	var curPage=document.getElementById("curPage");
  	$.post("<%=path%>/getPeoplesByPage.action", 
  			"curPage="+"<s:property value="totalPage"/>", 
  			function(data) {
            	$("#peopleTable").html(data);
            	curPage.innerText='<s:property value="totalPage"/>';
            	changePageButton();
            	top.stopProcess();
 	});
 }
 
 function changePageButton(){
   	var curPage=document.getElementById("curPage").innerText;
   	var totalPage="<s:property value="totalPage"/>";
    document.getElementById("firstPage").disabled=true;
    document.getElementById("firstPage").style.color="gray";
    document.getElementById("prePage").disabled=true;
    document.getElementById("prePage").style.color="gray";
    document.getElementById("nextPage").disabled=true;
    document.getElementById("nextPage").style.color="gray";
    document.getElementById("lastPage").disabled=true;
    document.getElementById("lastPage").style.color="gray";
  	if(curPage>1){
	    document.getElementById("firstPage").disabled=false;
	    document.getElementById("firstPage").style.color="#CC0033";
	    document.getElementById("prePage").disabled=false;
	    document.getElementById("prePage").style.color="#CC0033";
    }
    if(curPage<totalPage||curPage.length<totalPage.length){
	    document.getElementById("nextPage").disabled=false;
	    document.getElementById("nextPage").style.color="#CC0033";
	    document.getElementById("lastPage").disabled=false;
	    document.getElementById("lastPage").style.color="#CC0033";
    }
 }
 
 function changePage(){
	  document.getElementById("showPageType").style.visibility="hidden";
	  top.startProcess("正在切换界面,请稍等...");
	  document.getElementById("changePageForm").submit();
 }
 
 function searchPeople(){
  	var peopleName = document.getElementById("peopleName").value;
  	var peopleCode = document.getElementById("peopleCode").value;
  	if(!(top.isNull(peopleName)) && !(top.isGeneralName(peopleName))){
  		top.wfAlert("用户姓名含有非法字符!");
  		return false;
  	} 
    if (!(top.isNull(peopleCode)) && !(top.isAlphaAndDigits(peopleCode))){
  		top.wfAlert("用户代码只能是字母或数字!");
  		return false;
  	}
  	if(document.getElementById("useLikeCheck").checked){
  		document.getElementById("useLike").value="1";
  	}
 	
  	if(document.getElementById("usecontainOrg").checked){
  		document.getElementById("containOrg").value="0";
  	}else{
  		document.getElementById("containOrg").value="1";
  	}
  	
    top.startProcess("正在获取人员信息,请稍等...");
    document.getElementById("queryForm").submit();
 }
 
 function changeImageSize(element,width,height){
  	element.style.width=width;
 	element.style.height=height;
 }
 //导出
 function exportData(sid){
 	window.location.href = "<%=path%>/exportPeopleData.action";
}


</script>
</head>
<body onload="top.stopProcess()"
	style="font-size:12px;background-color: #EBEBE6;width:99%">
	<h2 style="width:50%">
		机构详细信息 (
		<s:if test="showPageType ==1">仅当前机构</s:if>
		<s:else>包含所有下级机构</s:else>
		)
	</h2>
	<form id="changePageForm" action="organizeDetail.action">
		<table width="95%" border="0" align="center" cellpadding="5"
			cellspacing="0">
			<tr>
				<td width="50%" class="border_bottom01">当前机构：
					<s:property value="organizeInfo.orgName" />
				</td>
				<td width="50%" class="border_bottom01">机构代码：
					<s:property value="organizeInfo.orgNo" />
				</td>
			</tr>
			<tr>
				<td class="border_bottom01">机构类型：<s:property
						value="orgType.orgTypeName" />
				</td>
				<td class="border_bottom01">机构共有：<s:if test="showPageType ==1">
						<s:property value="childOrgs" />个直属</s:if>
					<s:else>
						<s:property value="childOrgs-1" />个</s:else>下级机构</td>
			</tr>
			<tr>
				<td class="border_bottom01" height="25px">用户统计：
					<s:property value="peopleCount" />个用户&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<div style="position:absolute; z-index: 200;margin-left:150px;margin-top:-23px;*margin-left:0px;*margin-top:-5px;">
						<img width="25px" height="25px"
							onmousemove="changeImageSize(this,'30px','30px')"
							onmouseout="changeImageSize(this,'25px','25px')" title="新增人员"
							onclick="addPeople();"
							src="<%=path%>/windforce/common/images/po_add.png"></img>
					</div></td>
				<td class="border_bottom01" valign="bottom">统计类型: 
				<select name="showPageType" id="showPageType" style="visibility: hidden"
					onchange="changePage();">
						<option value="1">仅当前机构</option>
						<option value="2">包含所有下级机构</option>
				</select> <input type="hidden" name="sid"
					value="<s:property value='organizeInfo.sid' />" /> <input
					style="display:none" type="text" name="autoKeyWords"
					onblur="searchBlur()" onfocus="searchOn()"
					onkeyup="onAutoTextChange('1','');" id="autoComplete"
					style="margin-left: 60px;color: gray;" value="本页搜索:姓名、拼音" /> <img
					style="display:none" width="18px" height="18px"
					src="<%=path%>/windforce/common/images/po_search.png"
					style="margin-bottom: -4px"></img></td>
			</tr>
		</table>
	</form>
	<div id="ry_boc" style="width:100%;">
		<div style="width:100%; height:36px;">
			<span class="disblock2" style="width:48%;">
			<h2>人员信息（<s:property value="pageSize" />条/页） 
				<a id="firstPage" onclick="firstPage()">首页</a> 
				<a id="prePage" onclick="prePage()">上一页</a> 
				<a id="nextPage" onclick="nextPage()">下一页</a>
				<a id="lastPage" onclick="lastPage()">尾页</a> 
				<label>当前:第</label>
				<label id="curPage">1</label>
				<label>页</label>
			</h2> 
			</span>
			<form action="organizeDetail.action" id="queryForm" method="post">
				<span class="disblock2" style="width:52%; text-align:left; padding-top:5px; vertical-align:middle;*padding-top:0px;*margin-top: -1px;width:50%"> 
					用户姓名:<input type="text" id="peopleName" name="peopleName" value="<s:property value='peopleName'/>" maxlength="10" style="width:50px" /> 
					用户代码:<input type="text" id="peopleCode" name="peopleCode" value="<s:property value='peopleCode'/>" maxlength="10" style="width:50px" /> 
					<input type="checkbox" style="vertical-align:bottom; margin-bottom:3px;_margin-bottom:0px;" id="useLikeCheck" name="useLikeCheck" value="<s:property value='useLike'/>" />
					模糊查询
					<input type="checkbox" style="vertical-align:bottom; margin-bottom:3px;_margin-bottom:0px;" id="usecontainOrg" name="usecontainOrg" ${containOrg == '0'?'checked':''} />
					无岗位查询</br>
					<a style="color:#FF0000" onclick="searchPeople();">查询&nbsp;&nbsp;&nbsp;</a>
					<a style="color:#FF0000" onclick="exportData('<s:property value='#sid'/>');">人员导出</a>
					<%-- 
					<div style="position:absolute; z-index:200;margin-left:320px;margin-top:-21px;*margin-left:0px;*margin-top:2px;">
						<img width="16px" height="16px"
							onmousemove="changeImageSize(this,'20px','20px')"
							onmouseout="changeImageSize(this,'16px','16px')"
							onclick="searchPeople();" title="搜索"
							src="<%=path%>/windforce/common/images/po_search.png"
							style="margin-bottom: -4px"></img>
					</div> 
					 --%>
					<input type="hidden" name="sid" value="<s:property value='organizeInfo.sid' />" /> 
					<input type="hidden" value="1" name="isClickButton" /> 
					<input type="hidden" value="0" id="useLike" name="useLike" /> 
					<input type="hidden" id="containOrg" name="containOrg" value="1" /> 
				</span>
			</form>

		</div>

		<div id="peopleTable"
			style=" width:100%; height:450px; float: left;padding-top: 10px;overflow-y: auto;_padding-top: 0px;">
			<table width="98%" align="left" cellpadding="0" cellspacing="0">
				<tr>
					<td width="10%" height="26" align="center" class="font_colors07">序号</td>
					<td width="20%" align="center" class="font_colors07">用户姓名</td>
					<td width="10%" align="center" class="font_colors07">用户代码</td>
					<td width="16%" align="center" class="font_colors07">用户岗位</td>
					<td width="10%" align="center" class="font_colors07">性别</td>
					<td width="13%" align="center" class="font_colors07">操作</td>
				</tr>
				<tbody id="trAutoComplete">
					<s:iterator value="userList" var="userInfo" status="index">
						<!--  class="gradeX"  这句话别删除，用于自动搜索使用 -->
						<tr class="gradeX"
							<s:if test="#index.index%2 ==0">bgcolor="#CBD6E0"</s:if>
							<s:if test="#index.index%2 ==1">bgcolor="#FFFFFF"</s:if>>
							<td align="center" onclick="setUserDetail(this);">${index.index
								+1 }</td>
							<td align="center" onclick="setUserDetail(this);" style="color: blue;"><a
								onclick="javascript:top.userDetail('<s:property value='#userInfo.peopleCode'/>');">
									<s:property value="#userInfo.peopleName" /> </a></td>
							<td align="center" onclick="setUserDetail(this);"><s:property
									value="#userInfo.peopleCode" /></td>
							<td align="center" onclick="setUserDetail(this);"><s:property
									value="#userInfo.roleGroupStr" /></td>		
							<td align="center" class="center" onclick="setUserDetail(this);"><s:if
									test="#userInfo.peopleGender == 0">男</s:if> <s:if
									test="#userInfo.peopleGender == 1">女</s:if>
							</td>
							<td align="center">
								<s:if test="#userInfo.sid !='7B92AE0FC4B04DB48F1AFBDB22CD7188'">
									<a style="color:#FF0000" onclick="editUser('<s:property value='#userInfo.sid'/>');">修改</a> /
									<a style="color:#FF0000" onclick="delPeople('<s:property value='#userInfo.sid'/>','<s:property value='#userInfo.organizeInfo.orgNo'/>')">删除</a>/ 
								</s:if>
								<a style="color:#FF0000" onclick="resetPwd('<s:property value='#userInfo.sid'/>')" >重置密码</a>
							</td>
						</tr>
					</s:iterator>
				</tbody>
			</table>
		</div>
	</div>
<script type="text/javascript">

	$(document).ready(function(){
		changePageButton();
		var showPageType= document.getElementById("showPageType");
		showPageType.value="<s:property value='showPageType'/>";
		showPageType.style.visibility="visible";
		var useLike="<s:property value='useLike'/>";
		if(useLike=="1"){
			document.getElementById("useLikeCheck").checked=true;
		}
		document.getElementById("peopleTable").style.height=(top.getWindowHeight()-top.getTopSize()-200)+"px";
	});

	var peopleCode=document.getElementById("peopleCode");
     document.documentElement.onkeydown = function(evt){
                var b = !!evt, oEvent = evt || window.event;
                if (oEvent.keyCode == 13) {
                var node = b ? oEvent.target : oEvent.srcElement;
				if(node.name!=null&&node.id=="peopleName"){
					peopleCode.focus();	
                 }else if(node.name!=null&&node.id=="peopleCode"){
                 searchPeople();
                 };
        };		
    };
    
</script>
</body>
</html>


