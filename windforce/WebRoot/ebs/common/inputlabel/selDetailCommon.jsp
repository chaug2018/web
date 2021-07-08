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
	function setbgOn(tr) {
		tr.style.backgroundColor = '#FFFFFF';
	}
	function setbgOut(tr) {
		tr.style.backgroundColor = '#e8f2f6';
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
<tr>
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
   <s:if test="#prop.isDetailShow == 1">
				<td width="13%"><s:property value="#prop.propName" />：</td>
				<td width="auto" align="left"><s:if
						test="#prop.propType == @com.yzj.wf.rm.common.RMDefine@VALUEDOMAIN_DATE">
						<input readonly="readonly" x="<s:property value="#prop.cutImgX"/>"
							y="<s:property value="#prop.cutImgY"/>"
							h="<s:property value="#prop.cutImgH"/>"
							w="<s:property value="#prop.cutImgW"/>"
							value='<s:property value="#prop.propValue"/>'
							id='<s:property value="#prop.propPhysicalName"/>'
							name='${prop.propPhysicalName}' type="text" size="15"   />

					</s:if> <s:if
						test="#prop.propType == @com.yzj.wf.rm.common.RMDefine@VALUEDOMAIN_APPOINTCODE">

						<select x="<s:property value="#prop.cutImgX"/>"
							y="<s:property value="#prop.cutImgY"/>"
							h="<s:property value="#prop.cutImgH"/>"
							w="<s:property value="#prop.cutImgW"/>"
							id='<s:property value="#prop.propPhysicalName"/>'
							name='<s:property value="#prop.propPhysicalName"/>'    >
							<option value="">--请选择--</option>
							<s:if test="#prop.infoCode != null && #prop.infoCode != ''">
								<s:iterator var="propCode" value="#prop.infoCode">
									<option value="${propCode.codeKey}">${propCode.codeValue}</option>
								</s:iterator>
							</s:if>
						</select>
						<script>
							document
									.getElementById('<s:property value="#prop.propPhysicalName"/>').value = '<s:property value="#prop.propValue"/>';
						</script>
					</s:if> <s:if
						test="#prop.propType == @com.yzj.wf.rm.common.RMDefine@VALUEDOMAIN_BOOLEAN">
						<select x="<s:property value="#prop.cutImgX"/>"
							y="<s:property value="#prop.cutImgY"/>"
							h="<s:property value="#prop.cutImgH"/>"
							w="<s:property value="#prop.cutImgW"/>"
							id='<s:property value="#prop.propPhysicalName"/>'
							name='<s:property value="#prop.propPhysicalName"/>'    >
							<option value="">--请选择--</option>
							<option value="1">是</option>
							<option value="0">否</option>
						</select>
						<script>
							document
									.getElementById('<s:property value="#prop.propPhysicalName"/>').value = '<s:property value="#prop.propValue"/>';
						</script>
					</s:if> <s:if
						test="#prop.propType == @com.yzj.wf.rm.common.RMDefine@VALUEDOMAIN_FIXTEXT">
						<input x="<s:property value="#prop.cutImgX"/>"
							y="<s:property value="#prop.cutImgY"/>"
							h="<s:property value="#prop.cutImgH"/>"
							w="<s:property value="#prop.cutImgW"/>" readonly="readonly"
							value='<s:property value="#prop.propValue"/>'
							id='<s:property value="#prop.propPhysicalName"/>'
							name='<s:property value="#prop.propPhysicalName"/>' type="text"
							size="15"    />
					</s:if> <s:if
						test='#prop.propType == @com.yzj.wf.rm.common.RMDefine@VALUEDOMAIN_FREETEXT'>
						<input x="<s:property value="#prop.cutImgX"/>"
							y="<s:property value="#prop.cutImgY"/>"
							h="<s:property value="#prop.cutImgH"/>"
							w="<s:property value="#prop.cutImgW"/>" readonly="readonly"
							value='<s:property value="#prop.propValue"/>'
							id='<s:property value="#prop.propPhysicalName"/>'
							name='<s:property value="#prop.propPhysicalName"/>' type="text"
							size="25"    />
					</s:if> <s:if
						test="#prop.propType == @com.yzj.wf.rm.common.RMDefine@VALUEDOMAIN_NUMBER">
						<input x="<s:property value="#prop.cutImgX"/>"
							y="<s:property value="#prop.cutImgY"/>"
							h="<s:property value="#prop.cutImgH"/>"
							w="<s:property value="#prop.cutImgW"/>" readonly="readonly"
							value='<s:property value="#prop.propValue"/>'
							id='<s:property value="#prop.propPhysicalName"/>'
							name='<s:property value="#prop.propPhysicalName"/>'
							onkeypress="setNumber();" type="text" size="15"    />
					</s:if></td>
			</s:if>
  <%} %>
    </s:iterator>
<%} %>
  </tr>
  <%} %>
<tr><td><input type="button" value="退票"/></td></tr>