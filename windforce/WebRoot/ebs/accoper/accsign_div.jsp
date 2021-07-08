<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>签约维护</title>
</head>

<body >

<table width="100%"  >
	<tr>
    	<td width="180px" >账&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号
		 	 <input name="textfieldaccno" type="text" class="diinput_text02" id="accNo_div" style="width: 190px;" readonly="readonly" />
		 	 <input type="hidden" id="dataIndex"/>
		   	 <input type="hidden" id="tableIndex"/>
   		</td>
   		<td width="180px">机&nbsp;构&nbsp;号&nbsp;
  			<input name="textfieldpointNo" type="text" class="diinput_text02" id="orgId_div" style="width: 190px;"  readonly="readonly"  />
  		</td>
  	</tr>
    <tr>
        <td width="180px">户&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名
  			<input name="textfieldaccname" type="text" class="diinput_text02" style="width: 190px;" id="accName_div" readonly="readonly" />
		</td>
		<td width="180px">联系电话
  			<input name="textfieldlinkphone" type="text" class="diinput_text03" style="width: 190px;" id="phone_div"  maxlength="128"/>
		</td>
    </tr>
    <tr>
    	<td width="180px">邮&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;编
  			<input name="textfieldzip" type="text" class="diinput_text03" style="width: 190px;" id="zip_div"  maxlength="10"/>
		</td>
   		<td width="180px">联&nbsp;系&nbsp;人&nbsp;
  			<input name="textfieldlinkman" type="text" class="diinput_text03" style="width: 190px;" id="linkMan_div"  maxlength="32"/></td>
 	</tr>
  	<tr>
        <td width="180px">地&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;址
  			<input name="textfieldaddress" type="text" class="diinput_text03" style="width: 230px;" id="adress_div" maxlength="256" />
		</td>
        <td width="180px">验印账号
  			<input name="textfieldmobilephone" type="text" class="diinput_text03" style="width: 190px;" id="sealNo_div"  maxlength="32"/>
  		</td>
    </tr>
    <tr>
    	<td width="180px">验印模式<s:select style="width: 190px;"  id="sealMode_div" class="diinput_text01"
									list="sealModeMap"  listKey="key" listValue="value" headerKey="" >
								</s:select>
		</td>	
						 
		<td width="180px">发送方式<s:select style="width: 190px;" id="sendType_div"class="diinput_text01" 
									list="sendTypeMap" listKey="key" listValue="value" headerKey="" >
								</s:select>
		</td>
	</tr>
	<tr>
		<td width="180px">合同号 &nbsp;&nbsp;&nbsp;<input id="contract_div" style="width: 190px;" type="text" class="diinput_text03" id="contractId"  maxlength="32"/>
		</td>
		<td width="180px">对账周期<s:select style="width: 190px;"  class="diinput_text01" id="accCycle_div"
									list="refAccCycleMap"  listKey="key" listValue="value" headerKey="" >
								</s:select>
		</td>	
    </tr>
    <tr>
 		<td width="280px">维护方式<s:select style="width: 190px;" id="modifyType_div" class="diinput_text01" 
									list="modifyTypeMap" listKey="key" listValue="value" headerKey="" >
								</s:select>
		</td>					
    </tr>
</table>
<center>
 <tr>
   	<td><input name="save" type="button" class="submit_but09" id="submitModify"  value="保存" onclick="submitSign()"/><input name="cancle" type="button" class="submit_but09" id="cancelModify" value="取消"  onclick="closeSon()"/></td>
  </tr>
  </center>
</body>
</html>
