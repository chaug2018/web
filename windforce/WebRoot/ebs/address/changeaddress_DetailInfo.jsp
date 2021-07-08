<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<base href="<%=basePath%>" />
<title>账户发送地址明细列表</title>
<link href="<%=path%>/ebs/common/css/css.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/process.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/dicss.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="<%=path%>/ebs/common/js/jquery.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/orgTree.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/process.js"></script>
</head>
<script type="text/javascript">
//明细数据分页查询
	function queryDetailInfoByPage(pageNumber){
		//获取前台总页数信息
		var totalPage= "<s:property value='detailParam.totalPage'/>";
		if(pageNumber<=0){
			pageNumber=1;
		}else if(pageNumber>totalPage){
			pageNumber=totalPage;
		}
		//如果总页数为0的
		if(totalPage==0){
			pageNumber=1;
		}
		//给前台隐藏按钮（当前页）设值
		$("#detailCurPage").val(pageNumber);
		$("#basicInfoForm").submit();
	}
	
</script>
<body class="nov_moon" style="background:url('/windforce/ebs/common/images/bg_content.jpg');width: 850px;">
	<form id="basicInfoForm" action="changeAddressAction!queryBasicInfo.action?flag=false" name="basicInfoForm" method="post">
		<div class="nov_moon" align="center" style="width: 850px;">
			<table width="98%" cellpadding="0" cellspacing="0" id="basicInfo">
				<thead >
					<tr>
						<td width="5%" height="26" align="center" bgcolor="#c76c6f" class="font_colors01">序号</td>
						<td width="15%" align="center" bgcolor="#c76c6f" class="font_colors01">账号</td>
						<td width="15%" align="center" bgcolor="#c76c6f" class="font_colors01">户名</td>
						<td width="15%" align="center" bgcolor="#c76c6f" class="font_colors01">对账中心机构号</td>
						<td width="15%" align="center" bgcolor="#c76c6f" class="font_colors01">对账中心名称</td>
						<td width="15%" align="center" bgcolor="#c76c6f" class="font_colors01">发送方式</td>
						<td width="18%" align="center" bgcolor="#c76c6f" class="font_colors01">投递地址</td>
					</tr>
				</thead>
				<tbody id="basicInfoList" align="center">
					<s:iterator value="basicInfoList" var="queryData" status="st">
						<tr <s:if test="#st.index%2 ==0">bgcolor="#CBD6E0"</s:if>
							<s:if test="#st.index%2 ==1">bgcolor="#FFFFFF"</s:if>>
							<td><s:property value="#st.count+detailParam.firstResult-1" /></td>
							<td><s:property value="#queryData.accNo" /></td>
							<td><s:property value="#queryData.accName" /></td>
							<td><s:property value="#queryData.idCenter" /></td>
							<td><s:property value="idCenterValueMap.get(#queryData.idCenter)" /></td>
							<td ><s:property value="sendModeMap.get(#queryData.sendMode)" /></td>
							<td ><s:property value="queryData.sendAddress" /></td>
						</tr>
					</s:iterator>
				</tbody>
				<tfoot id="detailPageInfo">
						<tr class="pagelinks">
							<td colspan="5">
								每页显示
								<s:select cssStyle="width:50px;" id="detailParam.pageSize" name="detailParam.pageSize" onchange="queryDetailInfoByPage('1')"
									list="detailParam.pageSizeMap" listKey="key" listValue="value" headerKey="10" headerValue="10" />
								条记录&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								当前第
								<s:property value="detailParam.curPage" />
								页&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								当前显示
								<s:property value="detailParam.firstResult" />
								到
								<s:property value="detailParam.lastResult" />
								条，共
								<s:property value="detailParam.total" />
								条
							</td>
							<td colspan="2" align="right">
								<a onclick="queryDetailInfoByPage('1')">首页</a>&nbsp;&nbsp;
								<a onclick="queryDetailInfoByPage(<s:property value="detailParam.curPage-1"/>)">上一页</a>&nbsp;&nbsp;
								<a onclick="queryDetailInfoByPage(<s:property value="detailParam.curPage+1"/>)">下一页</a>&nbsp;&nbsp;
								<a onclick="queryDetailInfoByPage(<s:property value="detailParam.totalPage"/>)">尾页</a>
							</td>
						</tr>
				</tfoot>
			</table>
			<input type="hidden" id="detailCurPage" name="detailParam.curPage" value='<s:property value="detailParam.curPage"/>' />
		</div>
	</form>
</body>
</html>
