<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>账户类型规则增加</title>
</head>

<body>
	<table width="100%"  cellpadding="3">
		<tr>
			<td width="25%">
				账户类型:&nbsp;&nbsp;&nbsp;&nbsp;
					<s:select
						list="refAccCycleMap" class="input_text01" listKey="key"
						listValue="value" headerKey="" headerValue="--请选择--"
						style="color:#333333; font-size:13px; width:160px;"
						name="queryParam.accCycle" id="acc_div"
						value="queryParam.accCycle">
					</s:select>	
				<input type="hidden" id="dataIndex" /> 
				<input type="hidden" id="tableIndex" />
				<input type="hidden" id="modifyOrAdd"/>
			</td>
 
			<td width="25%">
			 科目号:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<s:select
						list="ValParamSubnocMap" class="input_text01" listKey="key"
						listValue="value" headerKey="" headerValue="--请选择--"
						style="color:#333333; font-size:13px; width:160px;"
						name="queryParam.subjectNo" id="sub_div"
						value="queryParam.subjectNo">
					</s:select>	
			</td>
		</tr>
		<tr>
			<td width="25%">
			余额 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<input name="queryParam.minBal"  style="width:90px" type="text" class="diinput_text01" id="minBal_div"  />
				至
				<input name="queryParam.maxBal"  style="width:90px" type="text" class="diinput_text01" id="maxBal_div" />
			</td>
			<td width="25%">
					单笔发生额 &nbsp;
				<input name="queryParam.oneMinAccrual"  style="width:90px" type="text" class="diinput_text01" id="oneMinAccrual_div" />
				至
				<input name="queryParam.oneMaxAccrual"  style="width:90px" type="text" class="diinput_text01" id="oneMaxAccrual_div"  />
			</td>
		</tr>
		<tr>
			<td width="25%">
			累计发生额
				<input name="queryParam.totalMinAccrual"  style="width:90px" type="text" class="diinput_text01" id="totalMinAccrual_div"  />
				至
				<input name="queryParam.totalMaxAccrual"  style="width:90px" type="text" class="diinput_text01" id="otalMaxAccrual_div" />
			</td>
			<td width="25%">
				
			</td>	
		</tr>		
	</table>
	<br/>
	<tr>
	<center>
			<td><input name="save" type="button" class="submit_but09" id="submitModify"  value="保存" onclick="addsubmit()"/></td>
			<td><input name="cancle" type="button" class="submit_but09" id="cancelModify" value="取消"  onclick="closeSon()"/></td>
	</center>	`
	</tr>
</body>
</html>