<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

</head>

<body>
	<table width="100%" >
	<tr><td style=" text-align:center; vertical-align: top;" colspan="2">未达录入编辑</td></tr>
		<tr>
			<td width="25%">账号
			 <input name="textAccNo" class="diinput_text01" type="text" id="accNo_div" maxlength="32"/> 
				<input type="hidden" id="dataIndex" /> 
				<input type="hidden" id="tableIndex" />
			</td>

			<td width="25%">记账日期 <input name="textTraceDate" type="text"
				 id="traceDate_div" maxlength="12" class="diinput_text01"/>
			</td>
		</tr>	
		<tr>
			<td width="25%">金额 <input name="textTraceCredit" type="text"
				 id="traceCredit_div" maxlength="32" class="diinput_text01"/>
			</td>
			<td width="25%">凭证号码 <input name="textTraceNo" type="text"
				 id="traceNo_div" maxlength="32" class="diinput_text01"/>
			</td>
		</tr>
		<tr>	
			<td width="25%">摘要
				<input name="textInputDesc" type="text" id="inputDesc_div" 
					maxlength="32" class="diinput_text01"/>
			</td>
			<td width="25%">未达方向
				<s:select list="refDirectionMap"
					onkeydown="cgo(this.form,this.name,'')" cssStyle="width: 165px;"
					listKey="key" listValue="value" headerKey="" headerValue="" id="direction_div" >
				</s:select>
			</td>
			</tr>
			<tr>
			<td width="25%">核对结果 
				<select name="result_div" id="result_div" tabindex="7" onkeydown="cgo(this.form,this.name,'')" 
						style="color:#333333; font-size:13px;">
					<option value="-1">--请选择--</option>
					<s:iterator value="refResultMap">
						<s:if test="key == 4 || key == 5">
							<option value="<s:property value='key'/>">
							<s:property value='value' />
						</option>
						</s:if>
					</s:iterator>
				</select>
			</td>
		</tr>
		<tr>
			<td><input name="save" type="button" class="submit_but09"
				id="submitModify" value="保存" onclick="dealResult_modify()" /></td>
			<td><input name="cancle" type="button" class="submit_but09"
				id="cancelModify" value="取消" onclick="closeSon()" /></td>
		</tr>
	</table>
</body>
</html>
