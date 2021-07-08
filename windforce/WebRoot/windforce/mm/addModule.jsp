<%--
  机构信息树展现页面                 
@date          2012/04/25         
@author        蒋正秋              
@version       1.0                
--%>
<%@page import="com.yzj.wf.core.model.mm.common.MMDefine"%>
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
<meta http-equiv="content-type" content="text/html; charset=utf-8"/>
<title>管理导航菜单</title>
<link type="text/css" rel="stylesheet" href="<%=path%>/windforce/common/css/core.css" />
<%@ include file="/common/wf_import.jsp"%>

<script type="text/javascript">
function setInitParent()
{
	var parentValue = '<s:property value="miSid"/>';
	document.forms[0].elements["mi.parentModuleInfo.sid"].value = parentValue;
}
function validate()
{
	var parent = document.forms[0].elements["mi.parentModuleInfo.sid"].value;
	if(parent==null||parent.length==0){
		top.wfAlert("请选择上级模块");
		return;
	}
	var moduleName = document.forms[0].elements["mi.name"].value;
	var url = document.forms[0].elements["mi.entrypoint_url"].value;
	var showname = document.forms[0].elements["mi.showname"].value;
	if(top.isNull(moduleName)){
	 	top.wfAlert("模块名不能为空");
	 	return;
	}
	if (!top.isGeneralName(moduleName)) {
		top.wfAlert("模块名含有非法字符");
	 	return;
	}
	if(top.isNull(url)){
 		top.wfAlert("模块url不能为空");
 		return;
	}
	if(top.isNull(showname)){
 		top.wfAlert("模块显示名称不能为空");
 		return;
	}
	if (!top.isGeneralName(showname)) {
		top.wfAlert("模块显示名含有非法字符");
	 	return;
	}
	var hotKey=document.forms[0].elements["mi.hotkeystr"].value;
		if(top.isNull(hotKey)){
			top.wfAlert("交易代码不能为空!");
			return;
		}
	if(hotKey!=null&&hotKey.length>50){
		top.wfAlert("交易码已超过指定长度");
		return;
	}
	if(hotKey!=null&&hotKey.length>0&&!top.isAlphaAndDigits(hotKey)){
		top.wfAlert("交易代码只能是字母或数字!");
		return;
	}
	var isCheck = true;
	if(moduleName == null || moduleName == ""){
		document.getElementById("moduleNameKey").style.display ="block";
		isCheck = false;
	}else {
		document.getElementById("moduleNameKey").style.display ="none";
	}

	if(url == null || url == "")
	{
		document.getElementById("urlKey").style.display ="block";
		isCheck = false;
	}else{
		document.getElementById("urlKey").style.display ="none";
	}

	if(showname == null || showname == "")
	{
		document.getElementById("shownameKey").style.display ="block";
		isCheck = false;
	}else{
		document.getElementById("shownameKey").style.display ="none";
	}
	if(isCheck)
	{
		if(!document.getElementById("navigation_box").checked&&!document.getElementById("taskview").checked){
		 if(!window.confirm("未选择显示方式,确认提交？","提示")){
		  return;
		 }
	}
	top.startProcess("正在提交新增请求,请稍等...");
	document.forms[0].submit();
	}
}

</script>
</head>
<body onload="setInitParent(),top.stopProcess();"
	style="overflow: hidden;">
	<div id="nov_nr" style="overflow: hidden;">
		<div class="nov_moon" style="width: 100%;height: 100%;margin-top:0px;">
			<form name="myform" method="post"
				action="<%=path%>/modules_doAdd.action">
				<table width="100%" border="0" align="center" cellpadding="0"
					cellspacing="0">
					<tr>
						<td valign="top"><table style="margin-top: 5px;"
								id="moduleOperate">
								<tr>
									<td colspan="2"><s:if
											test="modulerepeat != null && modulerepeat != ''">
											<font color="red"><s:property value="modulerepeat" />
											</font>
										</s:if>
									</td>
								</tr>
								<tr>
									<td>模 &nbsp;块 &nbsp;名 &nbsp;称 ：</td>
									<td width="150px" align="left"><input name="mi.name"
										maxlength="16" value="<s:property value='mi.name'/>" />
									</td>
									<td><label id="moduleNameKey" style="display: none;"><font
											color="red">模块名称不能为空</font>
									</label>
									</td>
								</tr>
								<tr>
									<td>上 &nbsp;级 &nbsp;模&nbsp; 块 ：</td>
									<td width="150px" align="left">
									<s:select list="parentList" name="mi.parentModuleInfo.sid"
										listKey="sid" listValue="showname" style="width:150px;">
									</s:select>
									</td>
								</tr>
								<tr>
									<td>模 &nbsp;块 &nbsp;URL &nbsp;&nbsp;：</td>
									<td width="150px" align="left" colspan="2"><textarea
											name="mi.entrypoint_url" cols="36" rows="3"><s:property value='mi.entrypoint_url' /></textarea><label id="urlKey" style="display: none;"><font
											color="red">模块URL不能为空</font>
									</label>
									</td>
								</tr>
								<tr>
									<td>界面显示名称 ：</td>
									<td width="150px" align="left"><input name="mi.showname"
										maxlength="16" value="<s:property value='mi.showname'/>" />
									</td>
									<td><label id="shownameKey" style="display: none;"><font
											color="red">显示不能为空</font>
									</label>
									</td>
								</tr>
								<tr>
									<td>交易代码 ：</td>
									<td width="150px" align="left"><input name="mi.hotkeystr"
										maxlength="10" value="<s:property value='mi.hotkeystr'/>" />
									</td>
									<td></td>
								</tr>
								<tr>
									<td>显示方式：</td>
									<td><input type="checkbox"
										<s:if test="mi.isNavigation==1">checked="checked"</s:if>
										name="showTypes" id="navigation_box" value="navigation" />
										系统菜单 <input type="checkbox"
										<s:if test="mi.isTaskView==1">checked="checked"</s:if>
										name="showTypes" id="taskview" value="taskview" /> 任务列表</td>
								</tr>
								<tr>
									<td>状&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 态 ：</td>
									<td><select name="mi.state">
											<option value="<%=MMDefine.ModuleState.NORMAL.getValue()%>">运行</option>
											<option value="<%=MMDefine.ModuleState.DEBUG.getValue()%>">调试</option>
											<option value="<%=MMDefine.ModuleState.STOP.getValue()%>">停用</option>
									</select></td>
								</tr>
								<tr>
									<td colspan="2"><input type="button" class="submit_but05"
										value="保存" onclick="validate();" />&nbsp;<input type="reset"
										class="submit_but05" value="重置" />
									</td>
								</tr>
							</table>
						</td>
					</tr>

				</table>
			</form>
		</div>
	</div>
</body>
</html>

