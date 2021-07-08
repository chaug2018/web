<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<table cellpadding="0" cellspacing="0" border="1" style="padding-top: 10px;padding-left: 10px;padding-bottom: 20px;border: 1px solid #c9eaf5;"  >
	<thead>
		<tr>
			<td width="40px" align="center">序号</td>
			<td width="80px" align="center">流水号</td>
			<td width="100px" align="center">操作柜员</td>
			<td width="100px" align="center">操作日期</td>
			<td width="400px" align="center">操作描述</td>
			<td width="100px" align="center">流程名称</td>
		</tr>
	</thead>
	<tbody id="logList"  >
		<s:iterator value="logList" var="logDto" status="st">
			<tr>
				<td align="center"><s:property value="#st.count"/></td>
				<td align="center"><s:property value="#logDto.docId"/></td>
				<td align="center"><s:property value="#logDto.opCode"/></td>
				<td align="center"><s:property value="#logDto.opDate"/></td>
				<td align="left"><s:property value="#logDto.logDesc"/></td>
				<td align="center"><s:property value="#logDto.taskName"/></td>
			</tr>
		</s:iterator>
	</tbody>
</table>