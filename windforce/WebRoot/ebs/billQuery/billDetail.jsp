<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
	+ request.getServerName() + ":" + request.getServerPort()
	+ path + "/";
%>

<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>影像明细</title>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3" />

<link href="<%=path%>/ebs/common/css/css.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/dicss.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/process.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="<%=path%>/common/js/jquery/jquery-1.7.1.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/billQuery.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/task.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/process.js"></script>

<style type="text/css">
.leftDiv {
	float: left;
	width: 100%;
	height: 600px;
	position: relative;
}

.rightDiv {
	height: 100%;
	top: 0px;
}

.topwindow {
	height: 600px;
	width: 800px;
	position: absolute;
	top: 80px;
	left: 100px;
	overflow: auto;
}
</style>
<script type="text/javascript">
		
/** 返回上级列表 */
function backQueryBillList() {
	window.location.href='billQueryAction_queryBillList.action';
}

function bindImg(){
	imgs = new Array();
	<%
	List<String> imgList = (List<String>)request.getAttribute("imgList");
	if(imgList != null && imgList.size()  >0){
		System.out.println(request.getContextPath());
		for(int i = 0 ; i < imgList.size() ;i ++){
			System.out.println(imgList.get(i));%>
			var curr = <%=i%>;
			imgs[curr] = '<%=imgList.get(i)%>';
	<%
		}
	}
	%>
}

$(document).ready(function() {
	bindImg();
	new ImageShow().init(imgs, 800, 600);
});
</script>
</head>
<body>
	<div id="head">
		<h1>影像明细</h1>
	</div>
	<div id="topwindow" class="topwindow" style="display:none;">
		<div></div>
		<div style="float:left;padding-top: 10px;padding-left: 10px;">日志查询列表</div>
		<div style="float:left;padding-left:620px;padding-top: 10px;">
			<a onclick="closeTopWindow()">关闭</a>
		</div>
		<div id="logList"></div>
	</div>
	<div id="content">
		<table>
			<tr>
				<td style="width:75%">
					<div class="leftDiv" style="position: relative;">
						<%@ include file="/ebs/imgshow/ImageShow.html"%>
					</div>
				</td>
				<td style="width:25%" valign="top">
					<div class="rightDiv">
						<table>
							<tr>
								<td>业务流水：</td>
								<td>
									<s:property value="docSet.docId" />
								</td>
							</tr>
							<tr>
								<td>账单编号：</td>
								<td>
									<s:property value="docSet.voucherNo" />
									<input id="docsetflag" type="hidden"
										value='<s:property value="docSet.docFlag"/>' />
								</td>
							</tr>
							<s:if test="obj.accno_0!=null&& obj.accno_0 !=''">
								<tr>
									<td>账号：</td>
									<td>
										<s:property value="obj.accno_0" />
									</td>
								</tr>
								<tr>
									<td>分账号：</td>
									<td>
										<s:property value="obj.accson_0" />
									</td>
								</tr>
								<tr>
									<td>余额结果：</td>
									<td>
										<s:iterator value="checkMap.keySet()" id="id">
											<s:if test="obj.checkflag_0==#id">
												<s:property value="checkMap.get(#id)" />
											</s:if>
										</s:iterator>
									</td>
								</tr>
							</s:if>
							<s:if test="obj.accno_1!=null&& obj.accno_1 !=''">
								<tr>
									<td>账号：</td>
									<td>
										<s:property value="obj.accno_1" />
									</td>
								</tr>
								<tr>
									<td>分账号：</td>
									<td>
										<s:property value="obj.accson_1" />
									</td>
								</tr>
								<tr>
									<td>余额结果：</td>
									<td>
										<s:iterator value="checkMap.keySet()" id="id">
											<s:if test="obj.checkflag_1==#id">
												<s:property value="checkMap.get(#id)" />
											</s:if>
										</s:iterator>
									</td>
								</tr>
							</s:if>
							<s:if test="obj.accno_2!=null&& obj.accno_2 !=''">
								<tr>
									<td>账号：</td>
									<td>
										<s:property value="obj.accno_2" />
									</td>
								</tr>
								<tr>
									<td>分账号：</td>
									<td>
										<s:property value="obj.accson_2" />
									</td>
								</tr>
								<tr>
									<td>余额结果：</td>
									<td>
										<s:iterator value="checkMap.keySet()" id="id">
											<s:if test="obj.checkflag_2==#id">
												<s:property value="checkMap.get(#id)" />
											</s:if>
										</s:iterator>
									</td>
								</tr>
							</s:if>
							<s:if test="obj.accno_3!=null&& obj.accno_3 !=''">
								<tr>
									<td>账号：</td>
									<td>
										<s:property value="obj.accno_3" />
									</td>
								</tr>
								<tr>
									<td>分账号：</td>
									<td>
										<s:property value="obj.accson_3" />
									</td>
								</tr>
								<tr>
									<td>余额结果：</td>
									<td>
										<s:iterator value="checkMap.keySet()" id="id">
											<s:if test="obj.checkflag_3==#id">
												<s:property value="checkMap.get(#id)" />
											</s:if>
										</s:iterator>
									</td>
								</tr>
							</s:if>
							<s:if test="obj.accno_4!=null&& obj.accno_4 !=''">
								<tr>
									<td>账号：</td>
									<td>
										<s:property value="obj.accno_4" />
									</td>
								</tr>
								<tr>
									<td>分账号：</td>
									<td>
										<s:property value="obj.accson_4" />
									</td>
								</tr>
								<tr>
									<td>余额结果：</td>
									<td>
										<s:iterator value="checkMap.keySet()" id="id">
											<s:if test="obj.checkflag_4==#id">
												<s:property value="checkMap.get(#id)" />
											</s:if>
										</s:iterator>
									</td>
								</tr>
							</s:if>

							<tr>
								<td colspan="2" align="center" style="padding-top: 20px;">
									<input type="button" onclick="backQueryBillList()" class="submit_but09"
										value="返回上级" />
								</td>
							</tr>
							<tr>
								<td colspan="2" align="center">
									<input type="button"
										onclick="queryLog('<s:property value="docSet.docId"/>')"
										class="submit_but09" value="查询日志" />
								</td>
							</tr>
							<tr>
								<td colspan="2" align="center">
									<s:if test="docSet.docFlag==-1">
										<input id="deleteBillBtn" type="button" style="display:none"
											class="submit_but09" value="发起删除" />
									</s:if>
									<s:elseif test="docSet.docFlag!=-1">
										<input id="deleteBillBtn" type="button" style="display:block"
											onclick="deleteBill('<s:property value="docSet.docId"/>')"
											class="submit_but09" value="发起删除" />
									</s:elseif>
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