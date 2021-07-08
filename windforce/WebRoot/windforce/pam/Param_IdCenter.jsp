<%@ taglib prefix="cp" uri="/custompage-tags"%>
<%@page language="java" pageEncoding="UTF-8" isELIgnored="false"%>
<%@page import="com.yzj.wf.core.model.po.wrapper.XPeopleInfo"%>
<%@page import="java.lang.String"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>对账中心参数管理</title>
<cp:head />
<script type="text/javascript" src="../windforce/common/js/param_simple.js"></script>
</head>
<%
	XPeopleInfo xp = (XPeopleInfo) session.getAttribute("XPEOPLEINFO");
	String pam_baseparam = (String) session.getAttribute("group");
%>
<body>
	<cp:condition showname="对账中心参数查询">
		<cp:text showname="对账中心" ref="extFields.idCenterNo" maxlength="64"></cp:text>
		<cp:text showname="中心名称" ref="extFields.idCenterName" maxlength="64"></cp:text>
	</cp:condition>
	<cp:content showname="对账中心参数管理">
		<%
			if (null != request.getParameter("edit")
						&& request.getParameter("edit").equals("true")) {
		%>
		<cp:operate showname="增加" opertype="add"
			action="saveParam.action?group=${param.group}"></cp:operate>
		<!-- preaction="initNewObj.action?group=pam_baseparam" -->
		<%
			}
		%>
		<cp:pagetable sortfield="paramName" sorttype="asc" sortable="false"
			rownum="20" action="fillTableContext.action?group=${param.group}"
			autoquery="true">
			<cp:pagecolumn showname="参数序号" ref="id"></cp:pagecolumn>
			<cp:pagecolumn showname="对账中心" ref="extFields.idCenterNo"></cp:pagecolumn>
			<cp:pagecolumn showname="中心名称" ref="extFields.idCenterName"></cp:pagecolumn>
			<cp:pagecolumn showname="是否参与对账" ref="extFields.isCheck" requireType="select"
				list="{'0':'否','1':'是'}"></cp:pagecolumn>

			<cp:pagecolumn showname='累计发生额 （季）（*）' ref="extFields.totalAmount_s"></cp:pagecolumn>
			<cp:pagecolumn showname="余额（季）" ref="extFields.banlance_s"></cp:pagecolumn>
			<cp:pagecolumn showname="累计发生额（百分比）" ref="extFields.totalAmountPercent"></cp:pagecolumn>
			<cp:pagecolumn showname="余额（百分比）" ref="extFields.balancePercent"></cp:pagecolumn>
			<cp:pagecolumn showname="借方累计发生额（月）" ref="extFields.totalAmount_b_m"></cp:pagecolumn>
			<cp:pagecolumn showname="贷方累计发生额（月）" ref="extFields.totalAmount_l_m"></cp:pagecolumn>
			<cp:pagecolumn showname="累计发生额（月）（*）" ref="extFields.totalAmount_m"></cp:pagecolumn>
			<cp:pagecolumn showname="余额（月）" ref="extFields.balance_m"></cp:pagecolumn>
			<cp:pagecolumn showname="最大单笔发生额（月）" ref="extFields.maxSingleCredit_m"></cp:pagecolumn>
			<cp:pagecolumn showname="最大单笔借方发生额（月）" ref="extFields.maxSingleCredit_b_m"></cp:pagecolumn>
			<cp:pagecolumn showname="最大单笔贷方发生额（月）" ref="extFields.maxSingleCredit_l_m"></cp:pagecolumn>
			<cp:pagecolumn showname="对账中心地址" ref="extFields.address"></cp:pagecolumn>
			<cp:pagecolumn showname="邮编" ref="extFields.zip"></cp:pagecolumn>
			<cp:pagecolumn showname="电话" ref="extFields.phone"></cp:pagecolumn>
			<cp:pagecolumn showname="验印模式" ref="extFields.sealType"></cp:pagecolumn>
			<cp:pagecolumn showname="未达录入模式" ref="extFields.notMatchInputType"
				requireType="select" list="{'BEFORE':'之前','AFTER':'之后'}"></cp:pagecolumn>
			<%
				if (null != request.getParameter("edit")
								&& request.getParameter("edit").equals("true")) {
			%>
			<cp:pagecolumn showname="操作" width="80px">
				<cp:operate showname="修改" opertype="modify"
					action="updateParam.action?group=${param.group}"></cp:operate>
				<cp:operate showname="删除" opertype="delete"
					action="deleteParam.action?group=${param.group}"></cp:operate>
			</cp:pagecolumn>
			<%
				}
			%>

		</cp:pagetable>
		<cp:navigation />
	</cp:content>

	<cp:operatediv opertype="add">
		<cp:text showname="参数序号" ref="id"/>
		<cp:text showname="对账中心" ref="extFields.idCenterNo"></cp:text>
		<cp:text showname="中心名称" ref="extFields.idCenterName"></cp:text>
		<cp:select showname="是否参与对账" ref="extFields.isCheck" list="{'0':'否','1':'是'}"></cp:select>
		<cp:text showname='累计发生额（季）（*）' ref="extFields.totalAmount_s"></cp:text>
		<cp:text showname="余额（季）" ref="extFields.banlance_s"></cp:text>
		<cp:text showname="累计发生额（百分比）" ref="extFields.totalAmountPercent"></cp:text>
		<cp:text showname="余额（百分比）" ref="extFields.balancePercent"></cp:text>
		<cp:text showname="借方累计发生额（月）" ref="extFields.totalAmount_b_m"></cp:text>
		<cp:text showname="贷方累计发生额（月）" ref="extFields.totalAmount_l_m"></cp:text>
		<cp:text showname="累计发生额（月）（*）" ref="extFields.totalAmount_m" format=""></cp:text>
		<cp:text showname="余额（月）" ref="extFields.balance_m"></cp:text>
		<cp:text showname="最大单笔发生额（月）" ref="extFields.maxSingleCredit_m"></cp:text>
		<cp:text showname="最大单笔借方发生额（月）" ref="extFields.maxSingleCredit_b_m"></cp:text>
		<cp:text showname="最大单笔贷方发生额（月）" ref="extFields.maxSingleCredit_l_m"></cp:text>
		<cp:text showname="对账中心地址" ref="extFields.address"></cp:text>
		<cp:text showname="邮编" ref="extFields.zip"></cp:text>
		<cp:text showname="电话" ref="extFields.phone"></cp:text>
		<cp:select showname="验印模式" ref="extFields.sealType"
			list="{'1+0':'1+0','1+1':'1+1','2+1':'2+1'}"></cp:select>
		<cp:select showname="未达录入模式" ref="extFields.notMatchInputType"
			list="{'BEFORE':'之前','AFTER':'之后'}"></cp:select>
	</cp:operatediv>

	<cp:operatediv opertype="modify">
		<cp:text showname="参数序号" ref="id"></cp:text>
		<cp:text showname="对账中心" ref="extFields.idCenterNo"></cp:text>
		<cp:text showname="中心名称" ref="extFields.idCenterName"></cp:text>
		<cp:select showname="是否参与对账" ref="extFields.isCheck" list="{'0':'否','1':'是'}"></cp:select>
		<cp:text showname="累计发生额（季）（*）" ref="extFields.totalAmount_s"></cp:text>
		<cp:text showname="余额（季）" ref="extFields.banlance_s"></cp:text>
		<cp:text showname="累计发生额（百分比）" ref="extFields.totalAmountPercent"></cp:text>
		<cp:text showname="余额（百分比）" ref="extFields.balancePercent"></cp:text>
		<cp:text showname="借方累计发生额（月）" ref="extFields.totalAmount_b_m"></cp:text>
		<cp:text showname="贷方累计发生额（月）" ref="extFields.totalAmount_l_m"></cp:text>
		<cp:text showname="累计发生额（月）（*）" ref="extFields.totalAmount_m"></cp:text>
		<cp:text showname="余额（月）" ref="extFields.balance_m"></cp:text>
		<cp:text showname="最大单笔发生额（月）" ref="extFields.maxSingleCredit_m"></cp:text>
		<cp:text showname="最大单笔借方发生额（月）" ref="extFields.maxSingleCredit_b_m"></cp:text>
		<cp:text showname="最大单笔贷方发生额（月）" ref="extFields.maxSingleCredit_l_m"></cp:text>
		<cp:text showname="对账中心地址" ref="extFields.address"></cp:text>
		<cp:text showname="邮编" ref="extFields.zip"></cp:text>
		<cp:text showname="电话" ref="extFields.phone"></cp:text>
		<cp:select showname="验印模式" ref="extFields.sealType"
			list="{'1+0':'1+0','1+1':'1+1','2+1':'2+1'}"></cp:select>
		<cp:select showname="未达录入模式" ref="extFields.notMatchInputType"
			list="{'BEFORE':'之前','AFTER':'之后'}"></cp:select>
	</cp:operatediv>

</body>
</html>