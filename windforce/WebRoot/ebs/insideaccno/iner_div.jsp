<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>内部账户配置</title>
</head>

<body>
	<table width="100%"  cellpadding="3">
		<tr align="center">
			<td>
				账&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号&nbsp;<input type="text" class="diinput_text01" maxlength="32" id="accNo_div1" />
				<input type="hidden" id="accNo_div2" value=""/>
			</td>
		</tr>
		<tr align="center">
			<td>
				操作员代码&nbsp;<input type="text" class="diinput_text01" maxlength="12" id="custId_add" name="custId_add" />
					<input type="hidden" id="flog_div" value=""/>
			</td>		
		</tr>
		<tr align="center">
			<td>
				复核员代码&nbsp;<input type="text" class="diinput_text01" maxlength="12" id="recheckCustId_add" name="recheckCustId_add" />
			</td>		
		</tr>
		
	</table>
	<br/>
	<tr>
	<center>
			<td><input name="save" type="button" class="submit_but09" id="submitModify"  value="保存" onclick="ineraddsubmit()"/></td>
			<td><input name="cancle" type="button" class="submit_but09" id="cancelModify" value="取消"  onclick="closeDiv()"/></td>
	</center>	`
	</tr>
</body>
</html>