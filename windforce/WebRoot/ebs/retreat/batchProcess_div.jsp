<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="s" uri="/struts-tags"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>批量催收处理</title>
</head>

<body>
	<table width="100%" >
	<tr><td style=" text-align:center; vertical-align: top;" colspan="2">批量退信处理</td></tr>
		<tr>
			
			<td width="40%">处理意见 <s:select list="refUrgeResultMap"
					onkeydown="cgo(this.form,this.name,'')" listKey="key"
					listValue="value" headerKey="" headerValue="" id="batchurgeResult_div" style="width:150px">
				</s:select></td>
		<td width="40%">&nbsp;备&nbsp;&nbsp;&nbsp;&nbsp;注 &nbsp;&nbsp;<input name="textRushDesc" type="text"
				style="width:100px" id="batchretreatDesc_div"/></td>
			
		</tr>
		<tr>
			<td><input name="save" type="button" class="submit_but09" id="submitBatchModify"  value="保存" onclick="submitBatchProcess()" /></td>

			<td><input name="cancle" type="button" class="submit_but09" id="cancelBatchModify" value="取消" onclick="closeSon2()" /></td>
		</tr>
	</table>
</body>
</html>
