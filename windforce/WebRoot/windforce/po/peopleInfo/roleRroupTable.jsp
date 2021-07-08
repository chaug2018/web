<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<%--
  机构人员添加页面                 
@date          2012/04/25         
@author        蒋正秋              
@version       1.0                
--%>
<div
	style="height: 130px;overflow-y: auto;background:#FFFFFF; padding-top: 15px; text-align:left; vertical-align: left;">
	<s:iterator value="roleGroupList" var="roleGroup" status="index">
		<span><input type="checkbox" name="userRoles"
			value="<s:property value='#roleGroup.sid'/>" /> <s:property
				value='#roleGroup.roleGroupName' /> </span>
	</s:iterator>
</div>





