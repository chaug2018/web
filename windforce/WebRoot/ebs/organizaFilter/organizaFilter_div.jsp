<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="s" uri="/struts-tags"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>机构过滤维护</title>
</head>


<body>
	<table width="100%">
		<tr>
	        <td width="20%px">机构号&nbsp;&nbsp;&nbsp;
	        	<s:textfield  id="editIdBank" cssClass="diinput_text01" name="organizaFilter.idBank" onblur="showBankInfo()" label="" ></s:textfield>
			</td>
			<td width="20%">机构名称
	  			<s:textfield id="editBankName" cssClass="diinput_text01" name="organizaFilter.bankName" label="" readonly="true" >
	        	</s:textfield>
			</td>
		</tr>
		<tr>
			<td width="20%">上级机构
	        	<s:textfield id="editIdBranch" cssClass="diinput_text01" name="organizaFilter.idBranch" label="" readonly="true">
	        	</s:textfield>
			</td>
			<td width="20%">对账中心
	  			<s:textfield id="editIdCenter" cssClass="diinput_text01" name="organizaFilter.idCenter" label="" readonly="true">
	        	</s:textfield>
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
