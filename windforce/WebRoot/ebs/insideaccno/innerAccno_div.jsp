<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>新增内部账户</title>
</head>

<body>
	<table width="100%"  cellpadding="3">
		<tr align="center">
			<td>账号:&nbsp;<input type="text" class="diinput_text01" maxlength="30" id="accNo_div" />
			</td>
		</tr>
	</table>
	<br/>
	<tr>
	<center>
			<td><input name="save" type="button" class="submit_but09" id="submitModify"  value="保存" onclick="inneraddsubmit()"/></td>
			<td><input name="cancle" type="button" class="submit_but09" id="cancelModify" value="取消"  onclick="closeDiv()"/></td>
	</center>	`
	</tr>
</body>
</html>