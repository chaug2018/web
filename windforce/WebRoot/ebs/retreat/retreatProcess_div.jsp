<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>退信处理</title>
</head>

<body>

	<table width="100%" >
	<tr><td style=" text-align:center; vertical-align: top;" colspan="2">退信处理</td></tr>
		
		<tr>
			<td width="25%">机&nbsp;构&nbsp;号&nbsp;&nbsp; <input name="textIdBank" type="text"
				class="diinput_text01" id="idBank_div" disabled="disabled" style="width:150px"/> 
				<input type="hidden" id="dataIndex" /> 
				<input type="hidden" id="tableIndex" />
			</td>

			<td width="25%">账单编号 <input name="textVoucherNo" type="text"
				class="diinput_text01" id="voucherNo_div" disabled="disabled" style="width:150px"/></td>		
		</tr>	
		<tr>	
			<td width="25%">账户名称 <input name="textAccName" type="text"
				class="diinput_text01" id="accName_div" disabled="disabled" style="width:150px"/></td>
			<td width="25%">登记日期 <input name="textUrgeDate" type="text"
				class="diinput_text01" id="urgeDate_div" disabled="disabled" style="width:150px"/></td>

		</tr>
		<tr>
		<td width="25%">退信次数 <input name="textUrgeTimes" type="text"
				class="diinput_text01" id="urgeTimes_div" disabled="disabled" style="width:150px"/></td>
		
			<td width="25%">登记类型  <input name="textUrgeType" type="text"
				class="diinput_text01" id="urgeType_div" disabled="disabled" style="width:150px"/></td>	
			
			
		</tr>
		<tr>
				<td width="25%">处理意见<s:select list="refUrgeResultMap"
					onkeydown="cgo(this.form,this.name,'')" listKey="key"
					listValue="value" headerKey="" headerValue="" id="urgeResult_div" style="width:150px">
				</s:select></td>
				
				<td width="25%">备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注&nbsp;&nbsp;<input name="textUrgeDesc" type="text"
				id="urgeDesc_div" style="width:150px"   maxlength="128"/></td>
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
