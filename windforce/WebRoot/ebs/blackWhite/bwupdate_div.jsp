<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>特殊账户维护</title>
</head>

<body>
	<table width="100%" border="0" cellpadding="2"  style="float: left;"  >
			<tr>
		    	<td width="280px" >账&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号&nbsp;&nbsp;&nbsp;
				  <input name="textfieldaccno" type="text" class="diinput_text02"  style="width: 190px; color: grey;" id="accNo_div"  disabled="disabled" />
				  <input type="hidden" id="dataIndex"/>
				  <input type="hidden" id="tableIndex"/>
			  </td>
		        <td width="280px">机&nbsp;构&nbsp;号&nbsp;&nbsp;&nbsp;
		  			<input name="textfieldidBank" type="text"  style="width: 190px; color: grey;" class="diinput_text02" id="orgId_div"  disabled="disabled"  /></td>
			</tr>
		    <tr>
		    	<td width="280px">户&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名&nbsp;&nbsp;&nbsp;
		 			 <input name="textfieldAccName" type="text"  style="width: 190px;" class="diinput_text02" id="accName_div" disabled="disabled"/>
		  		</td>
				<td width="280px">联系电话&nbsp;&nbsp;
		  			<input name="textfieldPhone" type="text"  style="width: 190px;" class="diinput_text03" id="phone_div" maxlength="128" />
		  		</td>
		  	</tr>
		    <tr>
		    	<td width="280px">邮&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;编&nbsp;&nbsp;&nbsp;
					<input name="textfieldzip" type="text"  style="width: 190px;" class="diinput_text03" id="zip_div"  maxlength="10" />
		  		</td>
		  	 	<td width="280px">联&nbsp;系&nbsp;人&nbsp;&nbsp;&nbsp;
		  			<input name="textfieldLinkMan" type="text"  style="width: 190px;" class="diinput_text03" id="linkMan_div" maxlength="32"/>
		  		</td>
		    </tr>
		    <tr>    
		    	<td width="280px">地&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;址&nbsp;&nbsp;&nbsp;
		  			<input name="textfieldAddress" type="text"  style="width: 190px;" class="diinput_text03" id="adress_div"/>
		  		</td>
		  		
		        <td width="280px">验印账号&nbsp;&nbsp;
		  			<input name="textfieldmobilephone" type="text"  style="width: 190px;" class="diinput_text03" id="sealNo_div"  disabled="disabled"/>
		  		</td>
		  		 
		  	</tr>
		    <tr>
		 		<td width="280px">发送方式&nbsp;&nbsp;
		 			<s:select style="width: 190px;" id="sendType_div"
							   list="sendTypeMap" listKey="key" listValue="value" headerKey="" headerValue="--请选择--">
					</s:select>
				</td>
		
				<td width="280px">验印模式&nbsp;&nbsp;
					<s:select style="width: 190px;" id="sealMode_div"
							  list="sealModeMap" listKey="key" listValue="value" headerKey="" headerValue="--请选择--" >
					</s:select>
				</td>		
		    </tr>
		    <tr>
		 		<td width="25%">账户类型&nbsp;&nbsp; 
		 			<s:select list="refAccCycleMap" style="width:190px;" id="accnoFlag_div"
		 					  listKey="key" listValue="value"  headerKey="" headerValue="--请选择--" >
					</s:select>
				</td>	
						
				<td width="25%">是否对账&nbsp;&nbsp; <s:select style="width:190px;" id="isCheck_div"
					        list="refIsCheck"  listKey="key" listValue="value"  headerKey="" headerValue="--请选择--" disabled="true">
						</s:select>
				</td>	
		    </tr>
	</table>

	<br/>
	<br/>
	<center>
			<td><input name="save" type="button" class="submit_but09" id="submitModify"  value="保存" onclick="submitModify()"/></td>
			<td><input name="cancle" type="button" class="submit_but09" id="cancelModify" value="取消"  onclick="closeSon()"/></td>
	</center>
	
</body>
</html>
