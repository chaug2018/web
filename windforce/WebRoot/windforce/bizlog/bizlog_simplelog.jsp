<%@ taglib prefix="cp" uri="/custompage-tags"%>
<%@page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<cp:head />
<script type="text/javascript" src="windforce/common/js/bizlog_simplelog.js"></script>
</head>
<body>
	<cp:condition showname="查询业务日志">
		<cp:select showname="业务系统" ref="sysId" list="getAjaxData('logDataSource.action?paramName=sysId')"
			listKey="sysId" listValue="memo" headerKey="" headerValue="全部" format="combox"></cp:select>
		<cp:text showname="业务流水号" ref="bizId" format="number" pattern="int" errormsg="业务流水号请输入数字"></cp:text>
		<cp:select showname="操作类型" ref="operateType" list="options.operateType" dataType="init" headerKey="" headerValue="全部"></cp:select>
		<cp:text showname="操作人代码" ref="operatorCode" format="regular" pattern="/^[0-9a-z_]+$/i"></cp:text>
		<cp:text showname="机构号" ref="operatorOrgNo"></cp:text>
		<cp:text showname="账号" ref="accNo" format="regular" pattern="/^[0-9a-z_]{9,18}$/i"></cp:text>
		<cp:text showname="金额" ref="credit" format="number"></cp:text>
		<cp:text showname="起始日期" ref="operateDate" condtype="ge" format="beginDate" required="true"></cp:text>
		<cp:text showname="结束日期" ref="operateDate" condtype="le" format="endDate" required="true"></cp:text>
		<cp:text showname="备注" ref="memo" condtype="like"></cp:text>
		<cp:select showname="数据来源" ref="his" list="{'current':'当前数据','historical':'历史数据'}"></cp:select>
	</cp:condition>
	<cp:content showname="日志列表">
	<!-- 
		<cp:operate showname="插入当前日志(测试用)" opertype="custom" action="batchw(100,'current')"></cp:operate>
		<cp:operate showname="插入历史日志(测试用)" opertype="custom" action="batchw(100,'history')"></cp:operate>
	 -->
		<cp:pagetable rownum="10" action="businessLog!queryData.action" exportaction="businessLog!exportData.action"
			ondblclick="seeDetail(this)">
			<cp:pagecolumn showname="业务系统" ref="sysId" requireType="select"
				list="getAjaxData('logDataSource.action?paramName=sysId')" listKey="sysId" listValue="memo"></cp:pagecolumn>
			<cp:pagecolumn showname="业务流水号" ref="bizId"></cp:pagecolumn>
			<cp:pagecolumn showname="操作类型" ref="operateType" requireType="select" list="options.operateType" dataType="init"></cp:pagecolumn>
			<cp:pagecolumn showname="操作人代码" ref="operatorCode"></cp:pagecolumn>
			<cp:pagecolumn showname="操作日期" ref="operateDate"></cp:pagecolumn>
			<cp:pagecolumn showname="操作时间" ref="operateTime"></cp:pagecolumn>
			<cp:pagecolumn showname="机构号" ref="operatorOrgNo"></cp:pagecolumn>
			<cp:pagecolumn showname="账号" ref="accNo"></cp:pagecolumn>
			<cp:pagecolumn showname="金额" ref="credit"></cp:pagecolumn>
			<cp:pagecolumn showname="备注" ref="memo"></cp:pagecolumn>
		</cp:pagetable>
		<cp:extrarow action="businessLog!findOperateRows.action">
			<cp:pagecolumn showname="所查询数据来源下操作类型为'操作0'的日志总数：" ref="totalRows"></cp:pagecolumn>
		</cp:extrarow>
		<cp:navigation />
	</cp:content>
</body>
</html>