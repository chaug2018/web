<%--
               业务数据展现表格通用组件                
@date          2010/11/17         
@author        蒋正秋              
@version       1.0                
--%>
<%@page import="java.lang.reflect.Method"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<table width="100%" border="0" align="left" cellpadding="0"
	cellspacing="0" class="cont_table" id="bizDataTable">
	<s:if test="toPages != null && toPages.size() > 0">
		<tr>
			<s:iterator id="showName" value="showNames">
				<th width="50px" width="auto"
					style="background: url(<%=path%>/windforce/common/images/as_table_bg.gif) repeat-x;"><s:property
						value="#showName.value" /></th>
			</s:iterator>
		</tr>
		<s:iterator id="showValue" value="toPages">
			<tr
				<s:if test="#showValue['result']=='失败'">style="background-color: #FF3366;"</s:if>
				align="left" class="cont_table_tr">

				<s:iterator id="showName" value="showNames">

					<td width="50" align="left" style="line-height: 18px;"><s:property
							value="#showValue[#showName.key]" /></td>

				</s:iterator>
			</tr>
		</s:iterator>
	</s:if>
	<s:if test="toPages == null || toPages.size() < 1">
		<tr align="left" class="cont_table_tr" onmouseover="setbgOn(this);"
			onmouseout="setbgOut(this);">
			<td width="50" style="line-height: 18px;" align="left">没有监听到业务处理信息</td>
		</tr>
	</s:if>
</table>