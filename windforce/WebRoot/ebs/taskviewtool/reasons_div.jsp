<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>理由框div</title>
</head>

<body>
	<div class="untreadReason" id="untreadReason">
		<div class="reasons">
			<table bgcolor="#EBEBEB" id="reasonTable" width="100%" border="1">
			</table>
			<table width="100%" border="1" cellpadding="1" cellspacing="0"
				bgcolor="#FFFF99">
				<tr>
					<td>自输理由:<input id="untreadReasonInput" name="untreadReasonInput"
						value="请选择或输入理由信息" onFocus="onFoucs(this)" onBlur="onBlur(this)"
						title="自输理由" style="background:#FFFF99;width:75%;" maxlength="32"></input></td>
					<td><input id="submitDeleteButton" name="submitDeleteButton"
						type="button" value="确定" onclick="submitDelete()"
						style="background:#FFFF99;width:100%; height:110%" /></td>
					<td><input id="cancelButton" name="cancelButton" type="button"
						value="取消" onclick="cancel()"
						style="background:#FFFF99;width:100%; height:110%" /></td>
				</tr>
			</table>
		</div>
	</div>
</body>
</html>
