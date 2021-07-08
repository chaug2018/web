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
<title>机构权限管理子系统</title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3" />
<meta http-equiv="description" content="This is my page" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<link type="text/css" rel="stylesheet" href="<%=path%>/windforce/common/css/core.css" />
<%@ include file="/common/wf_import.jsp"%>

<script type="text/javascript">
// 初始化下拉框数据
      $.post('<%=path%>/initAddOrganize_initAddOrganize.action', null, function(data) {});
// 点击单个机构信息
function frameForward(url,sid){
main_right.location.href =url; // main_right 是指主页面下的iframe id.
if(right.location.href.indexOf('editOrganize_editInit.action') > 0)
{
editOrg("<%=path%>/editOrganize_editInit.action?orgNo="+sid);
}
}
// 点击添加机构按钮
function right_addOrg(url){

right.location.href = url;
expendRight();
}
// 添加完机构后刷新左菜单
function refreshLeft()
{
left.location.href = "<%=path%>/windforce/po/organizeinfo/organize_left.jsp";
}
// 点击修改机构按钮
function editOrg(url)
{
right.location.href = url;
expendRight();
}
// 点击删除机构按钮
function delOrg(url)
{
 $.post(url, null, function(data) {
    top.wfAlert(data);
    if(data == "删除成功!")
    {
    // 刷新左页面
    left.location.href = "<%=path%>/windforce/po/organizeinfo/organize_left.jsp";
    main_right.location.href =  "<%=path%>/organizeDetail.action?sid=";
    }
 });
}
function relDetail(currId)
{
main_right.location.href = "<%=path%>/organizeDetail.action?sid="+currId;
//main_right.location.href = "<%=path%>/organizeDetail.action?sid="+currId;
}

// 点击添加用户按钮
function right_addPeople(url){

right.location.href = url;
expendRight();
}
// 属性用户列表
function refreshUserList()
{
main_right.location.href = "<%=path%>/userList.action";
}
// 点击修改用户按钮
function editUser(url){
expendRight();
right.location.href = url;
}


	// 点击删除人员按钮
function delPeople(url,orId)
{
 $.post(url, null, function(data) {
    top.wfAlert(data);
    // 刷新用户列表
    main_right.location.href =  "<%=path%>/userList.action?orgId="
					+ orId;
			drawRight();
		});
	}
	// 点击用户详细
	function right_peopleDetail(url) {

		right.location.href = url;
		expendRight();
	}
	$(document).ready(function() {
	 
	});
</script>
</head>
<body style="overflow: hidden;" class="body_background">
	<input id="moudleName" type="hidden" value="机构人员管理" />
	<div id="nov_nr_son" style="width:100%">
		<div id="ry_noc" style="margin-right: 5px;">
			<iframe frameborder="0" id="left" name="left"
				src="<%=path%>/windforce/po/organizeinfo/organize_left.jsp"
				style="width:100%; height: 100%; background-color: #EBEBE6;"></iframe>
		</div>
		<div id="ry_boc">
			<iframe frameborder="0" id="main_right" name="main_right"
				src="<%=path%>/organizeDetail.action?sid="
				style="width:100%;background-color: #EBEBE6; height: 100%; isibility: inherit; ">
			</iframe>
		</div>
	</div>
	<script type="text/javascript">
		document.getElementById("ry_noc").style.height = top.getWindowHeight()
				- top.getTopSize() - 5 + "px";
		document.getElementById("ry_boc").style.height = top.getWindowHeight()
				- top.getTopSize() - 5 + "px";
	</script>
</body>
</html>