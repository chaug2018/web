<%--
  机构信息详细页面                 
@date          2012/04/25         
@author        蒋正秋              
@version       1.0                
--%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<%@ taglib prefix="s" uri="/struts-tags"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" >
<head>
    <title>机构拆并页面</title>
    
<link rel="stylesheet" type="text/css" href="<%=path%>/windforce/common/css/core.css" />
<%@ include file="/common/wf_import.jsp"%>

<script type="text/javascript">
function splitorg()
{
var currOrg = '<s:property value="organizeInfo.orgNo"/>';
if(currOrg == null || currOrg =="")
{
top.wfAlert("请选择要合并的机构！");
return ;
}
var meger = document.forms[0].elements["peopleInfo.organizeInfo.orgNo"].value;
if(confirm("确定要合并到当前机构吗？"))
{
 $.post("<%=path %>/doSplit.action?currOrgNo="+currOrg+"&mergerOrgNo="+meger, function(data){
 if(data != "0")
 {
 top.wfAlert(data);
 }else {
 window.returnValue = meger;
		window.close();
 }
 });
 }
}
</script>
</head>
<body style ="font-size:12px;background-color: #EBEBE6;overflow-x:hidden; ">
<form class="mws-form" action="http://www.malijuwebshop.com/themes/mws-admin/form_layouts.html">
               	<h3>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;机构详细信息</h3>
<table width="95%" border="0" align="center" cellpadding="5" cellspacing="0">
  <tr>
    <td width="50%" class="border_bottom01">当前机构：<s:property value="organizeInfo.orgName"/></td>
    <td width="50%" class="border_bottom01">机构代码：<s:property value="organizeInfo.orgNo"/></td>
  </tr>
  <tr>
    <td class="border_bottom01">机构类型：<s:property value="organizeInfo.orgType.orgTypeName"/></td>
    <td class="border_bottom01">上级机构：<s:property value="organizeInfo.parentOrg.orgName"/></td>
  </tr>
  <tr>
    <td class="border_bottom01">机构共有：<s:property value="organizeInfo.childOrgs.size"/>个直属下级机构 </td>
    <td class="border_bottom01">机构负责人：</td>
  </tr>
  <tr>
    <td class="border_bottom01">机构内共有：<s:property value="peopleCount"/>个用户</td>
    <td class="border_bottom01">其中被冻结：<s:property value="congOrgs"/>个</td>
  </tr>
 
 
  <tr>
    <td><label>
      
    </label></td>
    <td>&nbsp;</td>
  </tr>
</table>

<div id="ry_boc" style="padding-left: 5px;">将现有机构合并到：<s:select list="currOrgs" name="peopleInfo.organizeInfo.orgNo"  listKey="orgNo" listValue="orgName"></s:select><input type="button" value="保存" onclick="splitorg();"  class="submit_but05"/>&nbsp;&nbsp;<input type="button" value="取消" onclick="javascript:window.close();"  class="submit_but05" />
<div style=" width:650px; height:auto; float: left;">
<span class="disblock2"><h3 style="padding-top: 23px;">被拆除机构的人员信息(共<label id="userCount"><s:property value="organizeInfo.peopleInfos.size()"/></label>项)</h3>
  
</span>
<span class="disblock2" style="text-align:right; padding-top:40px;">
&nbsp;&nbsp;&nbsp;&nbsp;
</span>
</div>
<div style=" width:650px; height:auto; float: left;">
  <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
      <td width="10%" height="26" align="center" bgcolor="#4d657f" class="font_colors01">序号</td>
      <td width="25%" align="center" bgcolor="#4d657f" class="font_colors01">用户姓名</td>
      <td width="25%" align="center" bgcolor="#4d657f" class="font_colors01">用户代码</td>
      <td width="10%" align="center" bgcolor="#4d657f" class="font_colors01">性别</td>
      </tr>
   <tbody id="trAutoComplete">
    <s:iterator value="organizeInfo.peopleInfos" var="userInfo" status="index">
						<tr class="gradeX"
						<s:if test="#index.index%2 ==0">bgcolor="#CBD6E0"</s:if>
						<s:if test="#index.index%2 ==1">bgcolor="#FFFFFF"</s:if>
						
						
						>
							<td align="center">${index.index +1 }</td>
							<td align="center"><s:property value="#userInfo.value.peopleName"/>
							</td >
							<td align="center"><s:property value="#userInfo.value.peopleCode" />
							</td>
							<td align="center" class="center"><s:if test="#userInfo.value.peopleGender == 0">男</s:if>
								<s:if test="#userInfo.value.peopleGender == 1">女</s:if></td>
							
						</tr>
					</s:iterator>
					</tbody>
  </table>
</div>
</div>
</form>
</body>
</html>


