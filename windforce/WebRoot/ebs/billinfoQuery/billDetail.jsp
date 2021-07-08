<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath(); 
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>对账单明细</title>
<meta http-equiv="pragma" content="no-cache"/>
<meta http-equiv="cache-control" content="no-cache"/>
<meta http-equiv="expires" content="0"/>    

<link href="<%=path%>/ebs/common/css/css.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/dicss.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/process.css" rel="stylesheet" type="text/css" />
	
<style type="text/css">
	.leftDiv{float:left; width:67%;top:0px; height:auto;position:relative;}
	.rightDiv{height:100%;top:0px;}
	.topwindow{height:1000px; width:800px; position: absolute; top: 80px; left: 100px; overflow:auto;}
</style>

<script type="text/javascript" src="<%=path%>/ebs/common/js/jquery.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/checkmaindata.js"></script>	
<script type="text/javascript">

    function bindImg(){
	imgs = new Array();
	<%List<String> imgList = (List<String>)request.getAttribute("imgList");
	if(imgList != null && imgList.size()  >0)
	{
		for(int i = 0 ; i < imgList.size() ;i ++)
		{%>
			var curr = <%=i%>;
			imgs[curr] = '<%=imgList.get(i)%>';
		<%}}%>
	};
		
	$(document).ready(function(){
		bindImg();
		new ImageShow().init(imgs, 800, 600);
	});
	
	
	/** 返回上级列表 */
	function backQueryBillList() {
		window.location.href="queryBillinfoData.action";
	}
</script>
</head>
	<body>
		<div id="head">对账单明细</div>
		<div id="topwindow" class="topwindow" style="display:none;">
		<div>
		</div>
			<div style="float:left;padding-left:620px;padding-top: 10px;"><a onclick="closeTopWindow()">关闭</a></div><br>
			<div id="logList"></div>
		</div>
		<div id="content">
		 <table>
		 <tr>
		 <td style="width:75%">
		 <div class="leftDiv" style="position: relative;width:100%;">
			<%@ include file="/ebs/imgshow/ImageShow.html"%>
			</div>
		 </td>
		  <td style="width:25%" valign="top">
		  <div class="rightDiv">
				<table>
					<tr>
						<td width="40%" height="20px">账单编号：&nbsp; 
						</td>
						<td>
							<s:property value="checkMainData.voucherNo"/>	
						</td>
					</tr>
					<tr>
					<td width="30%" height="20px">账户名称：&nbsp; 
						</td>
						<td>
							<s:property value="checkMainData.accName"/>	
						</td>
					</tr>
					<tr>
					<td width="30%" height="20px">对账日期：&nbsp; 
						</td>
						<td>
							<s:property value="checkMainData.docDate"/>	
						</td>
					</tr>
					<tr >
					<td width="30%" height="20px">账单状态：&nbsp; 
						</td>
					<td>
		         	<s:property value="refDocStateMap.get(checkMainData.docState)" />
					</td>	
					</tr>
			
					<tr >
					<td width="30%" height="20px">验印状态：&nbsp; 
						</td>
					<td>
		         	<s:property value="refProveflagMap.get(checkMainData.proveFlag)" />
					</td>	
					</tr>
					<s:iterator value="checkMainData.accNoMainDataList" var="accNoMainData" status="st">
						<tr>
							<td>账     号&nbsp; &nbsp; &nbsp;<s:property value="#st.count"/>：</td>
							<td><s:property value="#accNoMainData.accNo"/></td>
						</tr>
						<tr>
							<td>分 账 号<s:property value="#st.count"/>：</td>
							<td><s:property value="#accNoMainData.accNoSon"/></td>
						</tr>
						<tr>
							<td> 币  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;       种：</td>
							<td><s:property value="#accNoMainData.currency"/></td>
						</tr>
						<tr>
							<td> 余  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;      额：</td>
							<td><s:property value="#accNoMainData.strcredit"/></td>
						</tr>
						<tr>
							<td>余额结果：</td>
							<td>
								<s:iterator value="refCheckflagMap.keySet()" id="id">
									<s:if test="#accNoMainData.finalCheckFlag==#id">
										<s:property value="refCheckflagMap.get(#id)"/>
        							</s:if>
								</s:iterator>
							</td>
						</tr>
						<tr><td>
						..................
						</td></tr>
					</s:iterator>
					<tr>
						<td colspan="2" align="center" style="padding-top: 20px;">
							<input type="button" onclick="backQueryBillList()" value="返回上级"/>
						</td>
					</tr>
				</table>
			</div>
		  </td>
		 </tr>
	 </table>
	</div>
</body>
</html>