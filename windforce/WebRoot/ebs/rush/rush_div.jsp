<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>催收处理</title>
</head>

<body>

	<table width="100%">
	<tr><td style=" text-align:center; vertical-align: top;" colspan="2">催收处理</td></tr>
		<tr>
			

			<td width="50%">账单编号 <input name="textVoucherNo" type="text" style="width:140px"
				class="diinput_text01" id="voucherNo_div" disabled="disabled"/>
				<input type="hidden" id="tableIndex" />
				</td>
				
			<td width="50%">账户名称 <input name="textAccName" type="text" style="width:140px"
				class="diinput_text01" id="accName_div" disabled="disabled" /></td>
				
		</tr>
		<tr>
		
		<td width="50%">催收方式 <s:select list="refRushMethodMap" style="width:140px"
					listKey="key" listValue="value" headerKey="" 
					headerValue="" id="rushMethod_div">
				</s:select></td>
			
			<td width="50%">处理意见 <s:select list="refRushResultMap" style="width:140px"
					listKey="key" listValue="value" headerKey=""
					headerValue="" id="rushResult_div">
				</s:select></td>
		</tr>	
		<tr>
			<td width="50%">客户回应 <s:select list="refCustomResponseMap" style="width:140px"
					listKey="key" listValue="value"  id="customResponse_div">
				</s:select></td>
			<td width="50%">&nbsp;备&nbsp;&nbsp;&nbsp;&nbsp;注 &nbsp;&nbsp;<input name="textRushDesc" type="text"
				style="width:140px" id="rushDesc_div"/></td>
		</tr>
	
		<tr>
			<td><input name="save" type="button" class="submit_but09"
				id="submitModify" value="保存" onclick="submitModify()" />
			</td>
			<td><input name="cancle" type="button" class="submit_but09"
				id="cancelModify" value="取消" onclick="closeSon()" />
			</td>
		</tr>

	</table>
</body>
</html>
