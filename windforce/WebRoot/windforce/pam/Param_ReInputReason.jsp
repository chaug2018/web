<%@ taglib prefix="cp" uri="/custompage-tags"%>
<%@page language="java" pageEncoding="UTF-8" isELIgnored="false"%>
<%@page import="com.yzj.wf.core.model.po.wrapper.XPeopleInfo"%>
<%@page import="java.lang.String"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>TEST</title>
<cp:head />
<script type="text/javascript" src="../windforce/common/js/param_simple.js"></script>
</head>
<%
XPeopleInfo xp = (XPeopleInfo)session.getAttribute("XPEOPLEINFO");
String pam_baseparam = (String)session.getAttribute("group");
%>
<body>
	<cp:condition showname="退回补录理由参数查询">
		<cp:text showname="理由编号"   ref="extFields.reinputreasonID" maxlength="64"></cp:text>
		<cp:text showname="理由描述"    ref="extFields.description" maxlength="64"></cp:text>
	</cp:condition>
	<cp:content showname="退回补录理由参数管理">
		<%
		if(null != request.getParameter("edit") && request.getParameter("edit").equals("true")){ %>
		<cp:operate showname="增加" opertype="add" action="saveParam.action?group=${param.group}" ></cp:operate>   <!-- preaction="initNewObj.action?group=pam_baseparam" -->
		<%}%>
		<cp:pagetable sortable="false" rownum="20" action="fillTableContext.action?group=${param.group}" autoquery="true">
		<cp:pagecolumn showname="参数序号"  width="120px"  ref="id"></cp:pagecolumn>
		<cp:pagecolumn showname="理由编号"  width="120px"  ref="extFields.reinputreasonID"></cp:pagecolumn>
		<cp:pagecolumn showname="理由描述"  width="120px"  ref="extFields.description"></cp:pagecolumn>
		<%
		if(null != request.getParameter("edit") && request.getParameter("edit").equals("true")){ %>
			<cp:pagecolumn showname="操作" 	width="120px" >
				<cp:operate showname="修改" opertype="modify" action="updateParam.action?group=${param.group}"></cp:operate>
				<cp:operate showname="删除" opertype="delete" action="deleteParam.action?group=${param.group}"></cp:operate>
			</cp:pagecolumn>
		<%}%>
		
		</cp:pagetable>
		<cp:navigation/>
	</cp:content>
	
	<cp:operatediv opertype="add">
		<cp:text showname="参数序号"    ref="id" disabled="true"></cp:text>
		<cp:text showname="理由编号"   requiredLabel="true"  ref="extFields.reinputreasonID"></cp:text>
		<cp:text showname="理由描述"    ref="extFields.description" ></cp:text>
	</cp:operatediv>
	
	<cp:operatediv opertype="modify">
		<cp:text showname="参数序号"    ref="id" disabled="true"></cp:text>
		<cp:text showname="理由编号"   requiredLabel="true"  ref="extFields.reinputreasonID"></cp:text>
		<cp:text showname="理由描述"    ref="extFields.description" ></cp:text>
	</cp:operatediv>
	
</body>
</html>