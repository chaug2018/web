<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>回收登记</title>

</head>
<body>
	<table >
		<tr style="width: 300px;">
			<td style=" text-align:center; vertical-align: top;" colspan="4">账单登记</td>
		</tr>
		
		<tr style="width: 200px;">
			<td style="width: 60px;">
				对账日期
			</td>
			<td style="width: 100px;">
				<input readonly="readonly" name="queryParam.docDate" type="text" class="diinput_text01"  onclick="new Calendar().show(this);" 
					id="queryParam.docDate" readonly="readonly" />
			</td>
			<td style="width: 60px;" align="center">
				对账单编号
			</td>
			<td style="width: 100px;"> 
				<input name="queryParam.voucherNo" type="text" class="diinput_text01" id="queryParam.voucherNo" maxlength="32"/>
			</td>
		</tr>
		
		<tr style="width: 300px;">
			<td style="width: 60px;">
			对账渠道
			</td>
			<td style="width: 100px;">
					<select name="queryParam.sendmode" id="queryParam.sendmode" class="diinput_text01">
					<s:iterator value="valRefCheckflagMap" id="mode">
						<option value="<s:property value='#mode.key'/>"><s:property value="#mode.value"/></option>
					</s:iterator>
					</select>
			</td>
			<td style="width: 60px;" align="center">
					备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注
			</td>
			<td style="width: 100px;">
				<input name="queryParam.desc" type="text" class="diinput_text01" id="queryParam.desc"/>
			</td>
		</tr>
					
		<tr id="call1" style="width: 300px; ">
			<td style="width: 60px;">
				签收日期
			</td>
			<td style="width: 100px;">
				<input name="queryParam.backDate" onclick="new Calendar().show(this);" type="text" class="diinput_text01"
					id="queryParam.backDate" readonly="readonly" />
			</td>
			<td style="width: 60px;" align="center">
					份&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;数
			</td>
			<td style="width: 100px;"> 
				<input name="queryParam.letterSum" type="text" class="diinput_text01" id="queryParam.letterSum" onkeypress="setNumber();" />
			</td>
		</tr>
		
		<tr id="send1" style="width: 300px;" >
			<td style="width: 60px;">
				单位名称
			</td>
			<td style="width: 100px;"> 
					<input name="queryParam.accName" type="text" class="diinput_text01" id="queryParam.accName"  maxlength="128"/>
			</td>
			<td style="width: 60px;" align="center">
				发&nbsp;送&nbsp;日&nbsp;期
			</td>
			<td style="width: 100px;">
				<input readonly="readonly" name="queryParam.sendDate" type="text" onclick="new Calendar().show(this);"
					class="diinput_text01" id="queryParam.sendDate"  />
			</td>
		</tr>
		
        <tr style="width: 300px;">
			<td style="width: 75px;">
				<input type="hidden" name="operType" id="operType"/>
			</td>
			<td style="width: 75px;">
				<input name="ok" type="button" class="submit_but09" id="cancelModify" value="确定"  onclick="call()"/>
			</td>
			<td style="width: 75px;">	
				<input name="close" type="button" class="submit_but09" id="cancelModify" value="取消"  onclick="closeSon()"/>
			</td>
			<td style="width: 75px;">
			</td>
	 	</tr>
	</table>
</body>
</html>