<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>内部账户对账</title>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html" />
<link href="<%=path%>/ebs/common/css/process.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/css.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/dicss.css" rel="stylesheet" type="text/css" />
<style>
#son_div {
	position: absolute;
	width: 600px;
	height: 300px;
	margin-left: 50px;
	margin-top: 30px;
	background:url('/windforce/ebs/common/images/bg_content.jpg');
}
</style>
<script language="javascript" src="<%=path%>/ebs/common/js/calendar.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/jquery.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/process.js"></script>


<script type="text/javascript">
	//点取消关闭 窗口时  把 输入的信息清空
	function closeDiv (){
		$("#abs_div").val("");
		//radioCheck("3");
		closeSon();
	}

	//如果之前已经勾对相符或者不相符 则反显
	function radioCheck(data){
		$("input[name='resultFlag']").val([data]);
	}
	
	// 核对界面初始化
	/* function innermodifyAcc(data){
		var cell=data.parentNode;
		var row=cell.parentNode;
		var accno=row.cells[1].innerHTML;
		var credit=row.cells[2].innerHTML;
		var datadate=row.cells[3].innerHTML;
		var abs=row.cells[5].innerHTML;
		var result = row.cells[4].innerHTML;
		
		$("#accno_div").val(accno);
		$("#abs_div").val(credit);
		$("#datadate_div").val(datadate);
			
		if(result == "相符"){
			radioCheck("0");
		}else if(result == "不相符"){
			radioCheck("1");
		}else{
			radioCheck("2");   //置空
		}
		showSon();
	} */
	
	
	function queryinnerData() {
		viewCheckListByPage("1");
	}

	function viewCheckListByPage(pageNumber) {
		if(document.getElementById("datadate").value.length == 0){
			alert("账单日期不能为空！");
			document.getElementById("datadate").focus();
		}else{
			if(IsDate(document.getElementById("datadate")) ){
				var pageSize = $("#select_pageSize").val();
				$("#pageSize").val(pageSize);
				$("#curPage").val(pageNumber);
				$("#insideAccnoCheckForm").submit();
			}else{
				document.getElementById("datadate").focus();
			}
		}
		
	}
	
	//内部账户 配置增加
	/* function ineraddsubmit(){
		var pagenumber = $("#nowPage").val();
		var abs =$("#abs_div").val();
		var result = $("input:checked").val();
		var accno = $("#accno_div").val();
		var postData ="result_div="+result+"&abs_div="+abs+"&accno_div="+accno+"&temp="+new Date();
		if(result == null){
			alert("请勾对相符或者不符选项");
		}else{
			$.ajax({
                type: "POST",
                url: "insideAccnoCheckAction!checkInnerAccno.action",
                data: postData,
                success: function(result) {
                        if(result == "modifySuccess"){
                        	alert("操作成功!");
							viewCheckListByPage(pagenumber);
                        }else {
	                        alert("操作失败!");
                        }
              	  }
        	});	
		}
	} */	
	
	function check_YES(data){
		var pagenumber = $("#nowPage").val();
		var cell=data.parentNode;
		var row=cell.parentNode;
		var accno = row.cells[1].innerHTML;
		var credit = row.cells[2].innerHTML;
		var datadate = row.cells[3].innerHTML;
		var abs="";//清空备注
		if(confirm("确认（账号:"+accno+",余额:"+credit+",日期:"+datadate+"） 相符?")){
			var postData ="result_div=0&abs_div="+abs+"&datadate_div="+datadate+"&accno_div="+accno+"&temp="+new Date();
			$.ajax({
                type: "POST",
                url: "insideAccnoCheckAction!checkInnerAccno.action",
                data: postData,
                success: function(result) {
                        if(result == "modifySuccess"){
                        	alert("操作成功!");
							viewCheckListByPage(pagenumber);
                        }else {
	                        alert("操作失败!");
                        }
              	  }
        	});	
		}
	}
	function check_NO(data){
		var cell=data.parentNode;
		var row=cell.parentNode;
		var accno=row.cells[1].innerHTML;
		var credit=row.cells[2].innerHTML;
		var datadate=row.cells[3].innerHTML;
		var abs=row.cells[5].innerHTML;
		var result = row.cells[4].innerHTML;
		
		$("#accno_div").val(accno);
		$("#datadate_div").val(datadate);
		$("#abs_div").val(credit);
		
		showSon();
	}
	
	//余额调节表 提交
	function creditAdjustSubmit(){
		var pagenumber = $("#nowPage").val();
		var accno = $("#accno_div").val();
		var datadate = $("#datadate_div").val();
		
		var abs="";
		for(var i=1;i<=5;i++){
			var abs_tmp="";
			var datetmp=$("#date"+i).val();
			var detail1 = $("#detail"+i+"1").val();
			var detail2 = $("#detail"+i+"2").val();
			var detail3 = $("#detail"+i+"3").val();
			var detail4 = $("#detail"+i+"4").val();
			
			if(datetmp!=null && datetmp.length>0){
				abs_tmp+="未达明细日期:"+datetmp+",";
			}
			if(detail1!=null && detail1.length>0){
				abs_tmp+="银行已收单位未收款项:"+detail1+",";
			}
			if(detail2!=null && detail2.length>0){
				abs_tmp+="银行已付单位未付款项:"+detail2+",";
			}
			if(detail3!=null && detail3.length>0){
				abs_tmp+="单位已收银行未收款项:"+detail3+",";
			}
			if(detail4!=null && detail4.length>0){
				abs_tmp+="单位已付银行未付款项:"+detail4+",";
			}
			
			
			if(abs_tmp.length>0){
				abs_tmp=abs_tmp.substring(0, abs_tmp.length-1);
				abs_tmp+=" | ";
				abs+=abs_tmp;
			}
			
		}
		
		
		if(abs == null || abs.length==0){
			alert("请填写余额调节表");
		}else{
			var postData ="result_div=1&abs_div="+abs+"&datadate_div="+datadate+"&accno_div="+accno+"&temp="+new Date();
			$.ajax({
                type: "POST",
                url: "insideAccnoCheckAction!checkInnerAccno.action",
                data: postData,
                success: function(result) {
                        if(result == "modifySuccess"){
                        	alert("操作成功!");
							viewCheckListByPage(pagenumber);
                        }else {
	                        alert("操作失败!");
                        }
              	  }
        	});
		}
	}
</script>

</head>
<body class="baby_in2">
	<div id="son_div" class="son_div"
		style="">
		<div class="sonpage" id="sonpage">
			<center>
				<tr>
					<td style="vertical-align: top;" colspan="2">余额调节表</td>
				</tr>
			</center>
		</div>
		<%@ include file="/ebs/insideaccno/innerCheck_div.jsp"%>
	</div>
	
	<div class="nov_moon">
		<div
			style="width:98%; height:auto; float:left; margin-left:10px; padding:10px 0px 10px 0px;"
			class="border_bottom01">
			<form id="insideAccnoCheckForm"
				action="insideAccnoCheckAction!queryInnerAccno.action" name="insideAccnoCheck"
				method="post">
				<table width="100%">
					<tr>
						<td width="30%"><div align="left">
								账&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号&nbsp;&nbsp;&nbsp;
								<input name="innerparam.accno" type="text" class="diinput_text01" maxlength="32" 
									id="accno" value='<s:property value="innerparam.accno"/>' />
							</div>
							<input type="hidden" id="curPage" name="innerparam.curPage" value='<s:property value="innerparam.curPage"/>' />
							<input type="hidden" id="pageSize" name="innerparam.pageSize" value='<s:property value="innerparam.pageSize"/>' />
						</td>
						<td width="30%"><div align="left">
								账单日期&nbsp;&nbsp;&nbsp;
							<input name="innerparam.datadate" type="text" title="账单日期"
							class="diinput_text01" id="datadate" onclick="new Calendar().show(this);"
							value='<s:property value="innerparam.datadate"/>' />
						</td>
						<td width="10%" rowspan="2" align="center">
							<input name="export" type="button" class="submit_but09" id="export"
							value="查询" onclick="queryinnerData()"/></td>
					</tr>
				</table>
			</form>
		</div>
		
		<div style="width:100%; height:auto; float:left; margin-left:10px; padding:10px 0px 10px 0px;">
			<h1>内部账户对账</h1>
			<table width="98%" style="border: 1px solid #c9eaf5;"
				cellpadding="0" cellspacing="0" id="basicInfoTable">
				<tr>		
					<td width="5%" height="26" align="center" bgcolor="#c76c6f"
						class="font_colors01">序号</td>
					<td width="15%" height="26" align="center" bgcolor="#c76c6f"
						class="font_colors01">账号</td>
					<td width="10%" align="center" bgcolor="#c76c6f"
						class="font_colors01">余额</td>
					<td width="10%" align="center" bgcolor="#c76c6f"
						class="font_colors01">账单日期</td>
					<td width="10%" align="center" bgcolor="#c76c6f"
						class="font_colors01">勾兑结果</td>
					<td width="28%" align="center" bgcolor="#c76c6f"
						class="font_colors01">备注</td>
					<td width="10%" align="center" bgcolor="#c76c6f"
						class="font_colors01">复核结果</td>
					<td width="10%" align="center" bgcolor="#c76c6f"
						class="font_colors01">核对</td>
				</tr>
				<tbody id="queryList" align="center">
					<s:iterator value="queryList" var="queryData" status="st">
						<tr bgcolor='<s:if test="#st.count%2==0">#CBD6E0</s:if>'>
							<td align="center" >
								<s:property value="#st.count+innerparam.firstResult" />
							</td>
							<td ><s:property value="#queryData.accNo" /></td>
							<td ><s:property value="#queryData.bal"/></td>	
							<td ><s:property value="#queryData.dataDate"/></td>	
							<td ><s:property value="accordName.get(#queryData.result)"/></td>
							<td ><s:property value="#queryData.abs"/></td>
							<td ><s:property value="recheckMap.get(#queryData.reCheck)"/></td>
							<td >
								<s:if test='#queryData.reCheck!=0'>
									<a onclick="check_YES(this)" style="color:#BE333A">相符</a>&nbsp;/&nbsp;
									<a onclick="check_NO(this)" style="color:#BE333A">不相符</a>
								</s:if>
							</td>
						</tr>
					</s:iterator>
				</tbody>
				<tfoot id="insideListPageInfo">
					<tr class="pagelinks">
						<td colspan="5" align="left">每页显示 <select id="select_pageSize"
							style="width:40px;" onchange="viewCheckListByPage('1')">
								<option value="10"
									<s:if test="innerparam.pageSize==10">selected="selected"</s:if>>10</option>
								<option value="20"
									<s:if test="innerparam.pageSize==20">selected="selected"</s:if>>20</option>
								<option value="50"
									<s:if test="innerparam.pageSize==50">selected="selected"</s:if>>50</option>
						</select>条记录&nbsp;&nbsp;&nbsp;&nbsp;当前显示<s:if test="innerparam.total==0">0</s:if>
							<s:else>
								<s:property value="innerparam.firstResult+1" />
							</s:else>到<s:property value="innerparam.lastResult" />条，共<s:property
								value="innerparam.total" />条</td>
						<td  colspan="3" align="right"><s:if
								test="innerparam.curPage==1">
								<a>首页</a>&nbsp;&nbsp;<a>上一页</a>
							</s:if> <s:else>
								<a href="#" onclick="viewCheckListByPage('1');return false;">首页</a>&nbsp;&nbsp;<a
									href="#"
									onclick="viewCheckListByPage('${innerparam.curPage - 1}');return false;">上一页</a>
							</s:else> <s:if test="innerparam.curPage==innerparam.totalPage">
								<a>下一页</a>&nbsp;&nbsp;<a>尾页</a>
							</s:if> <s:else>
								<a href="#"
									onclick="viewCheckListByPage('${innerparam.curPage + 1}');return false;">下一页</a>&nbsp;&nbsp;<a
									href="#"
									onclick="viewCheckListByPage('${innerparam.totalPage}');return false;">尾页</a>
							</s:else>
								<input type="hidden" id="nowPage" value="${innerparam.curPage}"/>
						</td>
					</tr>
				</tfoot>
			</table>
		</div>
	</div>
</body>
</html>

