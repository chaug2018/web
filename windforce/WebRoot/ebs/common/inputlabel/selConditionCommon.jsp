<%--
               业务数据展现表格通用组件                
@date          2012/11/17         
@author        蒋正秋              
@version       1.0                
--%>
<%@page import="com.opensymphony.xwork2.ognl.OgnlValueStack"%>
<%@page import="com.yzj.wf.common.db.ReportProps"%>
<%@page import="java.lang.reflect.Method"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
  <script>
function setbgOn(tr)
{
tr.style.backgroundColor= '#FFFFFF';
}
function setbgOut(tr)
{
tr.style.backgroundColor= '#e8f2f6';
}
// 设置数字输入框不能输入字母
function setNumber() {
	if (!(((window.event.keyCode >= 48) && (window.event.keyCode <= 57))

	|| (window.event.keyCode == 13) || (window.event.keyCode == 46)

	|| (window.event.keyCode == 45)))

	{
		alert("只能输入数值型数据！");
		window.event.keyCode = 0;

	}
	return;
}
</script>
<!-- 页面排版，获取最大行号和最大列号 -->
<%
List<ReportProps> list = (List<ReportProps>)request.getAttribute("propList");
Integer maxRow = 0;
if(list != null && list.size() > 0)
{
for(int i = 0 ; i < list.size();i ++)
{
if(list.get(i).getSelRowNum() > maxRow)
{
maxRow = list.get(i).getSelRowNum();
}
}
}
 %>
 <%for(int row  = 1 ; row <= maxRow ; row++) {%>
<tr style="font-size: 12px;">
  <!-- 获取最大列，然后遍历布局该列 -->
  <%int maxColumn = 0;
  for(int c = 0 ; c < list.size();c++)
  {
  if(list.get(c).getSelRowNum() == row)
  {
    if(list.get(c).getSelColumnNum() > maxColumn)
    {
      maxColumn = list.get(c).getSelColumnNum();
    }
  }
  }
  // 遍历该行所有列，按照配置文件的值来布局
  for(int s = 1;s <= maxColumn;s++)
  {
   %>
   <s:iterator value="propList" var="prop" status="status">
   <%
OgnlValueStack stack = (OgnlValueStack)request.getAttribute("struts.valueStack");
ReportProps obj=(ReportProps)stack.findValue("prop");
if(row == obj.getSelRowNum() && s == obj.getSelColumnNum()) {%>
   <s:if test="#prop.isContentShow == 1">
  <s:if test="#prop.isConditionShow == 1">
  
    <td  width="10%"><s:property value="#prop.propName"/>：</td>
   <td  width="auto">
    <s:if test="#prop.propType == @com.yzj.ebs.paramsadmin.common.ParamDefine@VALUEDOMAIN_DATE">
                     <input readonly="readonly" id='<s:property value="#prop.propPhysicalName"/>' name='${prop.propPhysicalName}' match="ge" type="text" size="15" onclick="new Calendar().show(this);"/>
                   ~ 
                     <input readonly="readonly" id='<s:property value="#prop.propPhysicalName"/>' name='${prop.propPhysicalName}' match="le" type="text" size="15" onclick="new Calendar().show(this);"/> 
                   </s:if>
                   <s:if test="#prop.propType == @com.yzj.ebs.paramsadmin.common.ParamDefine@VALUEDOMAIN_DATETIME">
                     起: <input readonly="readonly" id='<s:property value="#prop.propPhysicalName"/>' name='${prop.propPhysicalName}' type="text" match="ge" size="15" onclick="new Calendar().show(this);"/>
                     <select name="${prop.propPhysicalName}startHour">
                     <option value="">-选择-</option>
                      <%for(int i = 0 ; i < 24; i++ ){ %>
                     <%if(i < 10) { %>
                     <option value="0<%=i %>">0<%=i %></option>
                     <%} else { %>
                     <option value="<%=i %>"><%=i %></option>
                     <%} %>
                     <%} %>
                     </select>时 
                     <select name="${prop.propPhysicalName}startMinute">
                     <option value="">-选择-</option>
                     <%for(int i = 0 ; i < 60; i++ ){ %>
                     <%if(i < 10) { %>
                     <option value="0<%=i %>">0<%=i %></option>
                     <%} else { %>
                     <option value="<%=i %>"><%=i %></option>
                     <%} %>
                     <%} %>
                     </select>分
                          <select name="${prop.propPhysicalName}startSecond">
                          <option value="">-选择-</option>
                     <%for(int i = 0 ; i < 60; i++ ){ %>
                     <%if(i < 10) { %>
                     <option value="0<%=i %>">0<%=i %></option>
                     <%} else { %>
                     <option value="<%=i %>"><%=i %></option>
                     <%} %>
                     <%} %>
                     </select>秒<br/> 至: <input readonly="readonly" id='<s:property value="#prop.propPhysicalName"/>' name='${prop.propPhysicalName}' match="le" type="text" size="15" onclick="new Calendar().show(this);"/> <select name="${prop.propPhysicalName}endHour">
                       <option value="">-选择-</option>
                      <%for(int i = 0 ; i < 24; i++ ){ %>
                     <%if(i < 10) { %>
                     <option value="0<%=i %>">0<%=i %></option>
                     <%} else { %>
                     <option value="<%=i %>"><%=i %></option>
                     <%} %>
                     <%} %>
                     </select>时 
                     <select name="${prop.propPhysicalName}endMinute">
                     <option value="">-选择-</option>
                     <%for(int i = 0 ; i < 60; i++ ){ %>
                     <%if(i < 10) { %>
                     <option value="0<%=i %>">0<%=i %></option>
                     <%} else { %>
                     <option value="<%=i %>"><%=i %></option>
                     <%} %>
                     <%} %>
                     </select>分
                          <select name="${prop.propPhysicalName}endSecond">
                          <option value="">-选择-</option>
                     <%for(int i = 0 ; i < 60; i++ ){ %>
                     <%if(i < 10) { %>
                     <option value="0<%=i %>">0<%=i %></option>
                     <%} else { %>
                     <option value="<%=i %>"><%=i %></option>
                     <%} %>
                     <%} %>
                     </select>秒
                   </s:if>
                   <s:if test="#prop.propType == @com.yzj.ebs.paramsadmin.common.ParamDefine@VALUEDOMAIN_TIME">
                     <select name="${prop.propPhysicalName}startHour">
                     <option value="">-选择-</option>
                      <%for(int i = 0 ; i < 24; i++ ){ %>
                     <%if(i < 10) { %>
                     <option value="0<%=i %>">0<%=i %></option>
                     <%} else { %>
                     <option value="<%=i %>"><%=i %></option>
                     <%} %>
                     <%} %>
                     </select>时 
                     <select name="${prop.propPhysicalName}startMinute">
                     <option value="">-选择-</option>
                     <%for(int i = 0 ; i < 60; i++ ){ %>
                     <%if(i < 10) { %>
                     <option value="0<%=i %>">0<%=i %></option>
                     <%} else { %>
                     <option value="<%=i %>"><%=i %></option>
                     <%} %>
                     <%} %>
                     </select>分
                          <select name="${prop.propPhysicalName}startSecond">
                          <option value="">-选择-</option>
                     <%for(int i = 0 ; i < 60; i++ ){ %>
                     <%if(i < 10) { %>
                     <option value="0<%=i %>">0<%=i %></option>
                     <%} else { %>
                     <option value="<%=i %>"><%=i %></option>
                     <%} %>
                     <%} %>
                     </select>秒
                     ~ 
                       <select name="${prop.propPhysicalName}endHour">
                       <option value="">-选择-</option>
                      <%for(int i = 0 ; i < 24; i++ ){ %>
                     <%if(i < 10) { %>
                     <option value="0<%=i %>">0<%=i %></option>
                     <%} else { %>
                     <option value="<%=i %>"><%=i %></option>
                     <%} %>
                     <%} %>
                     </select>时 
                     <select name="${prop.propPhysicalName}endMinute">
                     <option value="">-选择-</option>
                     <%for(int i = 0 ; i < 60; i++ ){ %>
                     <%if(i < 10) { %>
                     <option value="0<%=i %>">0<%=i %></option>
                     <%} else { %>
                     <option value="<%=i %>"><%=i %></option>
                     <%} %>
                     <%} %>
                     </select>分
                          <select name="${prop.propPhysicalName}endSecond">
                          <option value="">-选择-</option>
                     <%for(int i = 0 ; i < 60; i++ ){ %>
                     <%if(i < 10) { %>
                     <option value="0<%=i %>">0<%=i %></option>
                     <%} else { %>
                     <option value="<%=i %>"><%=i %></option>
                     <%} %>
                     <%} %>
                     </select>秒
                   </s:if>
                   <s:if test="#prop.propType == @com.yzj.ebs.paramsadmin.common.ParamDefine@VALUEDOMAIN_APPOINTCODE">
                  
                     <select title="<s:property value='#prop.propName'/>" id='<s:property value="#prop.propPhysicalName"/>' name='<s:property value="#prop.propPhysicalName"/>' match="equal">
                      <option value="">--请选择--</option>
                      <s:if test="#prop.propCodes != null">
                      <s:iterator var="propCode" value="#prop.propCodes">
                      <option value="<s:property value='#propCode.key'/>"><s:property value='#propCode.value'/></option>
                      </s:iterator>
                      </s:if>
                     </select>
                   </s:if>
                   <s:if test="#prop.propType == @com.yzj.ebs.paramsadmin.common.ParamDefine@VALUEDOMAIN_BOOLEAN">
                     <select id='<s:property value="#prop.propPhysicalName"/>' name='<s:property value="#prop.propPhysicalName"/>' match="equal">
                      <option value="">--请选择--</option>
                      <option value="1">是</option>
                      <option value="0">否</option>
                     </select>
                   </s:if>
                    <s:if test="#prop.propType == @com.yzj.ebs.paramsadmin.common.ParamDefine@VALUEDOMAIN_FIXTEXT">
                      <input id='<s:property value="#prop.propPhysicalName"/>' name='<s:property value="#prop.propPhysicalName"/>' type="text" size="15"  match="like"/>
                   </s:if>
                    <s:if test='#prop.propType == @com.yzj.ebs.paramsadmin.common.ParamDefine@VALUEDOMAIN_FREETEXT'>
                      <input id ='<s:property value="#prop.propPhysicalName"/>' name='<s:property value="#prop.propPhysicalName"/>' type="text" size="25" match="like"/>
                   </s:if>
                   <s:if test="#prop.propType == @com.yzj.ebs.paramsadmin.common.ParamDefine@VALUEDOMAIN_NUMBER || #prop.propType == @com.yzj.ebs.paramsadmin.common.ParamDefine@VALUEDOMAIN_LONG">
                      <input id='<s:property value="#prop.propPhysicalName"/>' name='<s:property value="#prop.propPhysicalName"/>' onkeypress="setNumber();" type="text" size="15" match="equal"/>
                   </s:if>
                    <s:if test="#prop.propType == @com.yzj.ebs.paramsadmin.common.ParamDefine@VALUEDOMAIN_DOUBLE || #prop.propType == @com.yzj.ebs.paramsadmin.common.ParamDefine@VALUEDOMAIN_FLOAT">
                      <input title="<s:property value='#prop.propName'/>" id='<s:property value="#prop.propPhysicalName"/>' name='<s:property value="#prop.propPhysicalName"/>' onblur="return isFloat(this);" type="text" size="15" match="equal" checkType='${prop.checkType}'/>
                   </s:if>
                   </td>
  
  </s:if>
  </s:if>
  <%} %>
    </s:iterator>
<%} %>
  </tr>
  <%} %>
  <tr><td colspan="2"><input type="submit" class="submit_but09" value="查询" name="submitButton" /> <input class="submit_but09" type="reset" value="重置" name="resetButton "/></td></tr>