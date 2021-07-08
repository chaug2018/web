<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>余额调节表</title>

<script type="text/javascript">
	$(document).ready(function() {
		//限制输入数字和 . -
	    $("[inputNumber='number']").keypress(function(){
			//0-9:48-57, -:45  .:46  enter:13,只能输入45,46,48-57和13
			if (!(((window.event.keyCode >= 48) && (window.event.keyCode <= 57))
				|| (window.event.keyCode == 13) || (window.event.keyCode == 45) || (window.event.keyCode == 46)))
	  		{
	  			window.event.keyCode = 0;
	  		}
		});
	});
	      
</script>

</head>

<body>
	<table width="100%">
		<tr align="center">
			<td colspan="3">
				账号：<input type="text" style="width:160px;" disabled="true" id="accno_div" value=""/>
			</td>
			<td colspan="2">
				对账日期：<input type="text" style="width:80px;" disabled="true" id="datadate_div" value=""/>
			</td>
		</tr>
		<tr align="center">
			<td colspan="3">
				单位账面余额
			</td>
			<td colspan="2">
				银行账面余额：<input type="text" style="width:80px;" disabled="true" id="abs_div" value=""/>
			</td>
		</tr>
		<tr align="center">
			<td>
				未达明细日期
			</td>
			<td>
				加：银行已收<br/>单位未收款项
			</td>
			<td>
				减：银行已付<br/>单位未付款项
			</td>
			<td>
				加：单位已收<br/>银行未收款项
			</td>
			<td>
				减：单位已付<br/>银行未付款项
			</td>
		</tr>
		<tr align="center">
			<td>
				1、<input type="text" style="width:100px;" inputNumber='number' maxlength="8" id="date1" name="date1" />
			</td>
			<td>
				<input type="text" style="width:100px;" inputNumber='number' maxlength="16" id="detail11" name="detail11" />
			</td>
			<td>
				<input type="text" style="width:100px;" inputNumber='number' maxlength="16" id="detail12" name="detail12" />
			</td>
			<td>
				<input type="text" style="width:100px;" inputNumber='number' maxlength="16" id="detail13" name="detail13" />
			</td>
			<td>
				<input type="text" style="width:100px;" inputNumber='number' maxlength="16" id="detail14" name="detail14" />
			</td>
		</tr>
		<tr align="center">
			<td>
				2、<input type="text" style="width:100px;" inputNumber='number' maxlength="8" id="date2" name="date2" />
			</td>
			<td>
				<input type="text" style="width:100px;" inputNumber='number' maxlength="16" id="detail21" name="detail21" />
			</td>
			<td>
				<input type="text" style="width:100px;" inputNumber='number' maxlength="16" id="detail22" name="detail22" />
			</td>
			<td>
				<input type="text" style="width:100px;" inputNumber='number' maxlength="16" id="detail23" name="detail23" />
			</td>
			<td>
				<input type="text" style="width:100px;" inputNumber='number' maxlength="16" id="detail24" name="detail24" />
			</td>
		</tr>
		<tr align="center">
			<td>
				3、<input type="text" style="width:100px;" inputNumber='number' maxlength="8" id="date3" name="date3" />
			</td>
			<td>
				<input type="text" style="width:100px;" inputNumber='number' maxlength="16" id="detail31" name="detail31" />
			</td>
			<td>
				<input type="text" style="width:100px;" inputNumber='number' maxlength="16" id="detail32" name="detail32" />
			</td>
			<td>
				<input type="text" style="width:100px;" inputNumber='number' maxlength="16" id="detail33" name="detail33" />
			</td>
			<td>
				<input type="text" style="width:100px;" inputNumber='number' maxlength="16" id="detail34" name="detail34" />
			</td>
		</tr>
		<tr align="center">
			<td>
				4、<input type="text" style="width:100px;" inputNumber='number' maxlength="8" id="date4" name="date4" />
			</td>
			<td>
				<input type="text" style="width:100px;" inputNumber='number' maxlength="16" id="detail41" name="detail41" />
			</td>
			<td>
				<input type="text" style="width:100px;" inputNumber='number' maxlength="16" id="detail42" name="detail42" />
			</td>
			<td>
				<input type="text" style="width:100px;" inputNumber='number' maxlength="16" id="detail43" name="detail43" />
			</td>
			<td>
				<input type="text" style="width:100px;" inputNumber='number' maxlength="16" id="detail44" name="detail44" />
			</td>
		</tr>
		<tr align="center">
			<td>
				5、<input type="text" style="width:100px;" inputNumber='number' maxlength="8" id="date5" name="date5" />
			</td>
			<td>
				<input type="text" style="width:100px;" inputNumber='number' maxlength="16" id="detail51" name="detail51" />
			</td>
			<td>
				<input type="text" style="width:100px;" inputNumber='number' maxlength="16" id="detail52" name="detail52" />
			</td>
			<td>
				<input type="text" style="width:100px;" inputNumber='number' maxlength="16" id="detail53" name="detail53" />
			</td>
			<td>
				<input type="text" style="width:100px;" inputNumber='number' maxlength="16" id="detail54" name="detail54" />
			</td>
		</tr>
	</table>
	<br/>
	<tr>
		<center>
			<td><input name="save" type="button" class="submit_but09" id="submitModify"  value="确认" onclick="creditAdjustSubmit()"/></td>
			<td><input name="cancle" type="button" class="submit_but09" id="cancelModify" value="取消"  onclick="closeSon()"/></td>
		</center>	`
	</tr>
</body>
</html>