<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="s" uri="/struts-tags"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>黑白名单维护</title>
</head>

<body>
<br/>
	<table width="100%"  cellpadding="5"  >
		<tr>
			<td width="80%">全部维护为:<s:select list="refBlackWhiteFlagMap"
					class="diinput_text01" listKey="key" listValue="value" headerKey="" headerValue="全部"
					id="batchSpecialFlag_div">
					<input type="hidden" id="selectIds" />
				</s:select>
			</td>
			
		</tr>
		
		<tr>
			<td><input name="save" type="button" class="submit_but09" id="submitModify"  value="保存" onclick="submitBatchModify()" /></td>

			<td><input name="cancle" type="button" class="submit_but09" id="cancelModify" value="取消" onclick="closeSon2()" /></td>
		</tr>
	</table>
</body>
</html>
