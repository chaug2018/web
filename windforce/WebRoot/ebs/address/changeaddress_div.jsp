<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="s" uri="/struts-tags"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>发送地址维护</title>
</head>


<body>
	<table width="100%">
		<tr>
	        <td width="20%px">对账中心&nbsp;&nbsp;&nbsp;
	        	<s:select id="editIdCenter"  name="faceFlug.idCenter"  list="idCenterValueMap" listKey="key" listValue="value" lable=""  >
	  			</s:select>
			</td>
			<td width="20%">发送方式&nbsp;&nbsp;&nbsp;
	  			<s:select id="editSendMode"  name="faceFlug.sendMode"  list="sendModeMap" listKey="key" listValue="value" lable="" headerKey="" headerValue="-请选择-" >
	  			</s:select>
			</td>
		</tr>
		<tr>
			<td width="20%">投递地址&nbsp;&nbsp;&nbsp;
	        	<s:select id="editAddressFlug"  name="faceFlug.addressFlug"  list="addressMap" listKey="key" listValue="value" lable="" headerKey="" headerValue="-请选择-" >
	  			</s:select>
			</td>
		</tr>
		<tr style="height: 60px;">
			<td width="20%" align="center">
				<input name="save" type="button" class="submit_but09" id="submitModify"  value="保存" onclick="saveOrUpdate()" />
			</td>
			<td width="20%" align="center">
				<input name="cancle" type="button" class="submit_but09" id="cancelModify" value="取消" onclick="closeSon()" />
			</td>
		</tr>
	</table>
</body>
</html>
