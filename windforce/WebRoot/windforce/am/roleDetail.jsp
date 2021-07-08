<%@page import="com.yzj.wf.core.model.po.common.PODefine"%>
<%@page import="com.yzj.wf.core.model.am.common.AMDefine"%>
<%@page import="com.yzj.wf.core.model.am.RoleGroupInfo"%>
<%@page import="com.yzj.wf.core.model.am.RoleInfo"%>
<%@page import="org.apache.struts2.ServletActionContext"%>
<%@page import="com.yzj.wf.core.model.po.OrgType"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
	+ request.getServerName() + ":" + request.getServerPort()
	+ path + "/";
%>
<%@ taglib prefix="s" uri="/struts-tags"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%--
  角色信息详细              
@date          2012/06/17         
@author       蒋正秋           
@version       1.0                
--%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>角色管理页面</title>
<link type="text/css" rel="stylesheet" href="<%=path%>/windforce/common/css/ztree_ext.css" />
<link type="text/css" rel="stylesheet" href="<%=path%>/windforce/common/css/core.css" />
<%@ include file="/common/wf_import.jsp"%>
<script type="text/javascript" src="<%=path%>/common/js/jquery/ztree/jquery.ztree.all-3.1.js"></script>
<script type="text/javascript">
$(document).ready(function() {
$.getJSON("<%=path%>/roles_buildPowerTree.action", function(json) {
			var setting = {
				callback : {
					onMouseDown : zTreeOnMouseDown
				/*给所有节点添加了鼠标左键按下的监听*/
				}
			};
			var zNodes = json;

			$.fn.zTree.init($("#tree_rolequery"), setting, zNodes);
			function zTreeOnMouseDown(event, treeId, treeNode) {

			}
			;
		});
	});
	var isrefresh = '<s:property value="refreshTree"/>';
	if (isrefresh == "true") {
		top.refreshRoleList();
	}
</script>
</head>
<body style="background-color: transparent;background-image: none;overflow-x: hidden;overflow-y:auto;">
		<div id="nov_nr"  style="width: 100%;height: 100%;margin-top:0px">
		<div class="nov_moon" style="width: 100%;height: 100%;  margin-top:0px">
			<table width="100%" border="0" align="center">

				<tr>
					<td width="28%">角&nbsp;色&nbsp;名： <s:property value="detail.roleName" />
					</td>
					<td width="72%">描述： <s:property value="detail.memo" />
					</td>
				</tr>
				<tr>
					<td colspan="2" valign="top">岗&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;位：
						<label>
						<s:if test="detail.roleGroupInfos.size() < 1">&nbsp;暂无</s:if>
						<s:if test="detail.roleGroupInfos.size() > 0">
						 <s:iterator id="roleGroup"
								value="detail.roleGroupInfos">
								<s:if test="#roleGroup.value.roleGroupState ==  0">
									<s:property
											value="#roleGroup.value.roleGroupName" />
									&nbsp;&nbsp;
      </s:if>
							</s:iterator> 
							</s:if>
							</label>
					</td>
				</tr>

				<tr>
					<td width="100%" colspan="2">互斥角色： <label>
							<s:if test="detail.mutexRoleInfos.size() < 1">&nbsp;暂无</s:if>
							<s:if test="detail.mutexRoleInfos.size() > 0">
								<s:iterator value="detail.mutexRoleInfos" id="mutex">
      &nbsp;<a
										href="<%=path%>/roles_roleDetail.action?detailSid=<s:property value='#mutex.value.sid'/>"><s:property
											value="#mutex.value.roleName" />
									</a>
								</s:iterator>
							</s:if> </label>
					</td>
				</tr>
				<tr>
					<td height="10"></td>
				</tr>
				<tr>
					<td>资源列表:</td>
				</tr>
				<tr>
					<td colspan="2"><div class="uoon" style="overflow: auto;">
							<s:if test="detail.powerInfos.size() < 1">
							该角色暂时无资源！
							</s:if>
							<s:if test="detail.powerInfos.size() > 0">
								<ul id="tree_rolequery" class="ztree">
								</ul>
							</s:if>
						</div>
					</td>
				</tr>

				<tr>
					<td colspan="2"><table width="100%" border="0" cellspacing="0"
							cellpadding="0">



							<tr>
								<td colspan="2" valign="top"></td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td colspan="2" height="10"></td>
				</tr>
			</table>
			</div>
		</div>
<script type="text/javascript">top.stopProcess();</script>
</body>
</html>


