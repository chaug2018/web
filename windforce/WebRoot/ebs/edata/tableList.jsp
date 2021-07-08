<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
</head>

<body>
	<table width="98%" border="0" cellpadding="0" cellspacing="0"
		id="checkMainDataInfoTable">
		<tr>
			<td width="3%" align="center" bgcolor="#c76c6f" class="font_colors01">序号</td>
			<td width="12%" align="center" bgcolor="#c76c6f" class="font_colors01">账单编号</td>
			<td width="7%" align="center" bgcolor="#c76c6f" class="font_colors01">机构号</td>
			<td width="9%" align="center" bgcolor="#c76c6f" class="font_colors01">机构名称</td>
			<td width="13%" align="center" bgcolor="#c76c6f" class="font_colors01">户名</td>
			<td width="7%" align="center" bgcolor="#c76c6f" class="font_colors01">客户号</td>
			<td width="7%" align="center" bgcolor="#c76c6f" class="font_colors01">联系人</td>
			<td width="6%" align="center" bgcolor="#c76c6f" class="font_colors01">联系地址</td>
			<td width="10%" align="center" bgcolor="#c76c6f" class="font_colors01">联系电话</td>
			<td width="7%" align="center" bgcolor="#c76c6f" class="font_colors01">账单状态</td>
			<td width="9%" align="center" bgcolor="#c76c6f" class="font_colors01">打印次数</td>
		</tr>
		<tbody id="queryList" align="center">
			<s:iterator value="queryBillList" var="queryData" status="st">
				<tr bgcolor='<s:if test="#st.count%2==0">#F4DDDF</s:if>'>
					<td>
						<s:property value="#st.count+accnoMainDataQueryParam.firstResult" />
					</td>
					<td>
						<s:property value="#queryData.voucherNo" />
					</td>
					<td>
						<s:property value="#queryData.idCenter" />
					</td>
					<td>
						<s:property value="#queryData.bankName" />
					</td>
					<td>
						<s:property value="#queryData.accName" />
					</td>
					<td>
						<s:property value="#queryData.custId" />
					</td>
					<td>
						<s:property value="#queryData.linkMan" />
					</td>
					<td>
						<s:property value="#queryData.address" />
					</td>
					<td>
						<s:property value="#queryData.phone" />
					</td>
					<td align="center">
						<s:property value="refDocstateMap.get(#queryData.docState)" />
					</td>
					<td>
						<s:property value="#queryData.printTimes" />
					</td>
				</tr>
			</s:iterator>
		</tbody>
		<tfoot id="accInfoTable">
			<tr class="pagelinks">
				<td colspan="6">
					每页显示
					<select name="accnoMainDataQueryParam.pageSize" id="pageSize"
						style="width:40px;" onchange="viewCheckMainDataListByPage('1')">
						<option value="20"
							<s:if test="accnoMainDataQueryParam.pageSize==20">selected="selected"</s:if>>20</option>
					</select>
					条记录&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;当前显示
					<s:if test="accnoMainDataQueryParam.total==0">0</s:if>
					<s:else>
						<s:property value="accnoMainDataQueryParam.firstResult+1" />
					</s:else>
					到
					<s:property value="accnoMainDataQueryParam.lastResult" />
					条，共
					<s:property value="accnoMainDataQueryParam.total" />
					条
				</td>
				<td colspan="8" align="right">
					<s:if test="accnoMainDataQueryParam.curPage==1">
						<a>首页</a>&nbsp;&nbsp;<a>上一页</a>
					</s:if>
					<s:else>
						<a href="#" onclick="viewCheckMainDataListByPage('1');">首页</a>&nbsp;&nbsp;
									<a href="#"
							onclick="viewCheckMainDataListByPage('${accnoMainDataQueryParam.curPage - 1}');return false;">上一页</a>
					</s:else>
					<s:if
						test="accnoMainDataQueryParam.curPage==accnoMainDataQueryParam.totalPage">
						<a>下一页</a>&nbsp;&nbsp;<a>尾页</a>
					</s:if>
					<s:else>
						<a href="#"
							onclick="viewCheckMainDataListByPage(${accnoMainDataQueryParam.curPage + 1});return false;">下一页</a>&nbsp;&nbsp;<a
							href="#"
							onclick="viewCheckMainDataListByPage('${accnoMainDataQueryParam.totalPage}');return false;">尾页</a>
					</s:else>
				</td>
			</tr>
		</tfoot>
	</table>
</body>
</html>
