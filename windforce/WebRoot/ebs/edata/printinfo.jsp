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

<title>对账单打印</title>
<meta http-equiv="Content-Type" content="text/html" />
<link href="<%=path%>/ebs/common/css/css.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/dicss.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/process.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="<%=path%>/ebs/common/js/orgTree.js"></script>
<script type="text/javascript" src="<%=path%>/common/js/jquery/jquery-1.7.1.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/process.js"></script>
<script language="javascript"  src="<%=path%>/ebs/common/js/calendar.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/js/jquery.DOMWindow.js"></script>
<script language="javascript" src="<%=path%>/ebs/lodopFile/LodopFuncs.js"></script>

<script type="text/javascript">
	if("<s:property value='errMsg'/>" != null && "<s:property value='errMsg'/>" != "")
	{
		alert("${errMsg}");
	}
</script>

<script type="text/javascript">
    var LODOP; //声明为全局变量 
	function prn1_preview(data) {	
		LODOP.NewPageA();
		LODOP.ADD_PRINT_HTM(40.5,0,555,1802.5,data);
	};

	function unselectall() {
		if (document.chkAll.checked) {
			document.chkAll.checked = document.checkMainDataQueryForm.chkAll.checked & 0;
		}
	}
	function CheckAll(form) {
		for ( var i = 0; i < form.elements.length; i++) {
			var e = form.elements[i];
			if (e.Name != 'chkAll' && e.disabled == false)
				e.checked = form.chkAll.checked;
		}
	}

	function queryAccnoMainDataList(){
		if(IsDate(document.getElementById("docdate")))
	    {
			viewCheckMainDataListByPage("1");
		}
	}
	
	function viewCheckMainDataListByPage(pageNumber){
		if(document.getElementById("docdate").value.length == 0){
			alert("对账日期不能为空！");
			document.getElementById("docdate").focus();
		}else{
					$("#curPage").val(pageNumber);
					$("#flag").val("createBill");
					$("#checkMainDataQueryForm").submit();
			}
	}
	
	function isLastDay(inputDate){
		var d = new Date(inputDate.replace(/\-/,"/"));
		var nd = new Date(d.getTime()+24*60*60*1000);  //next day
 		return (d.getMonth()!=nd.getMonth());
 	}
	
	//回车转Tab
	function cgo(obj, element, method) {
		var e = event ? event : window.event;

		if (e.keyCode == 13) {
			for ( var i = 0; i < obj.length; i++) {
				if (obj[i].name == element) {
					id = i;
				}
			}
			obj[id + 1].focus();
		}
	}
	
	$(document).ready(function(){
		var idCenter="<s:property value='idCenter'/>";
		var idBank="<s:property value='idBank'/>";
		initTree(<%=request.getAttribute("orgTree")%>,idCenter,idBank);
		$("#sendMode").append("<option value='5'>无</option>");
		var sendMode = "<s:property value='accnoMainDataQueryParam.sendMode'/>";
		if(5==sendMode){
			document.getElementById("sendMode").options[5].selected=true;
		}
	});
	
	function batchPrintBillData(){
		var voucherNo = new Array();
		LODOP=getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM')); 
		LODOP.PRINT_INIT("打印控件Lodop功能演示_多页预览");
		//LODOP.SET_PRINT_PAGESIZE(1,2100,2996,""); 
		LODOP.SET_PRINT_PAGESIZE(1,0,0,"A4");
			if(fnCheck()){
			//打印勾选的信息
				var arrData = new Array();
				var e = document.getElementsByName( "selectId"); 
				var count = 0;
				
				for( var i = 0; i < e.length; i++ ){ 
					if( e[i].checked==true) {
						var selectId = e[i].value;
						var param = "selectIds="+ selectId;						
						var isbreak;						
						
						if(count==0){
							//第一条  不做处理
						}else{
							//为第二条再做处理													

							$.ajax({
								cache:false,
								async:false,
		  	         			type: "POST",
		  	         			url: "DataPrintAction_getDetailListCount.action",
		  	         			data: "selectIds="+arrData[count-1]+"&temp="+new Date(),
		  	         			success: function(result){
		  	        	 			if(result =="true"){
	  	        		 				isbreak= "1";
					  	        	 }else if(result=="false"){
					  	        	 	isbreak="2";
					  	        	 }else{
					  	        		 isbreak="3"; //空页
					  	        	 }
		  	        			 }
		  					});	
						}
	  					if(isbreak=="1"){
	  						LODOP.NewPageA();
	  						LODOP.NewPageA();
	  					}else if(isbreak=="2"){
	  						LODOP.NewPageA();
	  						LODOP.NewPageA();
	  						LODOP.NewPageA();
	  					}else if(isbreak=="3"){
	  						LODOP.NewPageA();
	  						LODOP.NewPageA();
	  					}else{
	  						LODOP.NewPageA();
	  					}
	  					
						var currVoucherNo = e[i].title;
						LODOP.ADD_PRINT_URL("2mm",0,"RightMargin:8cm","BottomMargin:0mm", "JR_HTML_batchprint.action?"+param);	//画界面    ireport
						LODOP.ADD_PRINT_BARCODE("220px","350px","400px","42px","Code39",currVoucherNo);   //二维码
						LODOP.ADD_PRINT_IMAGE(775,30,400,400,"<img border='0' transcolor='#FFFFFF' src='<%=basePath %>/common/images/yz.png' />");
						arrData[count] = e[i].value; 
						count++;
					}
				}
				//LODOP.PRINT();      //直接打印
				LODOP.PREVIEW(); //打印预览
				//LODOP.PRINT_DESIGN();  // 打印设置
			}else{
	/*				//打印所有的对账单
				var all = $("#allinfo").val();
				if(confirm("是否全部"+all+" 条打印?")){
				//取得所有的 VoucherNo
			 $.ajax({
	             type: "POST",
	              url: "DataPrintAction!getAllVoucherNo.action",
	              success: function(result) {
	                      if(result == "false"){
	                      	alert("打印失败！请稍后再试");
	                      }else{
	                      	alert(result);
	                      //	voucherNo[] = result.split();
	                      	
	                      }
	             		}
	   			   });
				}
	*/			  
				alert("请选择至少一条打印！");
			}
	}
	
	//重写打印方法，根据打印类型来打印 printType:data,details,dataAndDetails
	function batchPrintByType(printType){
		var voucherNo = new Array();
		LODOP=getLodop(document.getElementById("LODOP_OB"),document.getElementById("LODOP_EM")); 
		LODOP.PRINT_INIT("对账单打印");
		LODOP.SET_PRINT_PAGESIZE(1,0,0,"A4");
			if(fnCheck()){
			//打印勾选的信息
				var arrData = new Array();
				var e = document.getElementsByName("selectId"); 
				var count = 0;
				
				for( var i = 0; i < e.length; i++ ){
					if( e[i].checked==true) {
						var selectId = e[i].value;
						var param = "selectIds="+ selectId;
						
						var isBack="0";//账单状态是否为已收回   0:no  1:yes
						$.ajax({
								cache:false,
								async:false,
		  	         			type: "POST",
		  	         			url: "DataPrintAction_getDocState.action",
		  	         			data: param+"&temp="+new Date(),
		  	         			success: function(result){
		  	        	 			if(result =="1"){
	  	        		 				isBack="1";
	  	        		 				//alert("已收回");
					  	        	}else{
					  	        		//alert("wei收回");
					  	        	}
		  	        			}
		  				});	
		  				
		  				if(isBack=="0" || (isBack=="1" && confirm("账单状态为已收回，确认要继续打印？"))){
							if(printType=="data" || printType=="dataAndDetails"){//打印账单和明细 或者 打印账单
								var isbreak;
							
								if(count==0){
									//第一条  不做处理
								}else{
									//为第二条再做处理													
									$.ajax({
										cache:false,
										async:false,
				  	         			type: "POST",
				  	         			url: "DataPrintAction_getDetailListCount.action",
				  	         			data: "selectIds="+arrData[count-1]+"&temp="+new Date(),
				  	         			success: function(result){
				  	        	 			if(result =="true"){
			  	        		 				isbreak= "1";
							  	        	}else if(result=="false"){
							  	        	 	isbreak="2";
							  	        	}else{
							  	        		isbreak="3"; //空页
							  	        	}
				  	        			 }
				  					});	
								}
			  					if(isbreak=="1"){
			  						LODOP.NewPageA();
			  						LODOP.NewPageA();
			  					}else if(isbreak=="2"){
			  						LODOP.NewPageA();
			  						LODOP.NewPageA();
			  						LODOP.NewPageA();
			  					}else if(isbreak=="3"){
			  						LODOP.NewPageA();
			  						LODOP.NewPageA();
			  					}else{
			  						LODOP.NewPageA();
			  					}
			  					
								var currVoucherNo = e[i].title;
								LODOP.ADD_PRINT_URL("2mm",0,"RightMargin:8cm","BottomMargin:0mm", "JR_HTML_batchprint.action?"+param+"&printType="+ printType);	//画界面    ireport
								LODOP.ADD_PRINT_BARCODE("220px","350px","400px","42px","Code39",currVoucherNo);   //二维码
								LODOP.ADD_PRINT_IMAGE(775,30,400,400,"<img border='0' transcolor='#FFFFFF' src='<%=basePath %>/common/images/yz.png' />");
								arrData[count] = e[i].value; 
								count++;
								
								
								
							}else if(printType=="details"){//打印明细
								$.ajax({
										cache:false,
										async:false,
				  	         			type: "POST",
				  	         			url: "DataPrintAction_getDetailCount.action",
				  	         			data: param+"&temp="+new Date(),
				  	         			success: function(result){
				  	        	 			
				  	        	 			var mxCount = parseInt(result);
			  	        					var totalPage=parseInt((mxCount - 1) / 500 + 1);
			  	        					var totalPage1=parseInt((mxCount - 1) / 250 + 1);
			  	        					//alert(mxCount+",totalPage:"+totalPage+",totalPage1:"+totalPage1);
			  	        					
			  	        					if(mxCount>0){
			  	        						for(var j=1;j<=totalPage;j++){
			  	        							//一次打500条明细
													LODOP.ADD_PRINT_URL("2mm",0,"RightMargin:8cm","BottomMargin:62mm", 
														"MX__HTML__batchprint_subreport2.action?"+param+"&firstResult="+(j-1)*500+"&pageNum=500&totalPage="+totalPage+"&totalPage1="+totalPage1);
													LODOP.PRINT();
			  	        						}
			  	        					}else {
												alert("该账单没有明细");
											}
				  	        			}
				  				});
							}
							
						}
					}
				}
				if(printType=="data" || printType=="dataAndDetails"){
					LODOP.PREVIEW(); //打印预览
				}
			}else{
				alert("请选择至少一条打印！");
			}
	}

	function fnCheck() 
	{ 
		var e = document.getElementsByName("selectId"); 
		var num = 0; 
		for( var i = 0; i < e.length; i++ ){ 
		if( e[i].checked==true) 
			num++; 
		} 
		if(num < 1 ){ 
			return false; 
		}else{
			return true;
		}
	} 
	
	
</script>
</head>

<body class="baby_in2">
	<inputid="moudleName" type="hidden" value="对账单打印" />
	<form id="checkMainDataQueryForm" method="post" action="DataPrintAction_queryBillinfoData.action"
		name="checkMainDataQuery">
		<div class="nov_moon">
			<div style="width:98%; height:auto; float:left; margin-left:10px; padding:10px 0px 10px 0px;"
				class="border_bottom01">
						<input name="flag" type="hidden" id="flag"></input>
				<table width="100%">
					<tr>
						<td width="20%">总 行&nbsp;&nbsp;
							<select>
								<option value=""> 华融湘江总行</option>
							</select>
						</td>
						<td width="20%">验印状态 &nbsp;
							<s:select list="refProveflagMap" listKey="key"
								listValue="value" headerKey="" headerValue="--请选择--"
								name="accnoMainDataQueryParam.proveFlag" 
								id="proveFlag" value="accnoMainDataQueryParam.proveFlag">
							</s:select>
						</td>
						<td width="20%">户&nbsp;&nbsp;&nbsp;&nbsp;名 &nbsp;&nbsp;&nbsp;
							<input name="accnoMainDataQueryParam.accName" type="text"
								class="diinput_text01" id="txtaccName"
								value='<s:property value="accnoMainDataQueryParam.accName"/>' />
						</td>
						<td width="10%" align="center">
							<input name=queryData type="button" class="submit_but09" id="find"
								value="查询" onclick="queryAccnoMainDataList()" />
						</td>
					</tr>
					<tr>
						<td width="20%">分 行&nbsp;&nbsp;
								<select name="accnoMainDataQueryParam.idCenter" id="selectIdCenter"
									onchange="changeIdCenter()">
								</select>
						</td>
						<td width="20%">机&nbsp;&nbsp;构&nbsp;号&nbsp;&nbsp;
							<input name="accnoMainDataQueryParam.idBank" id="idBank"
								class="diinput_text01" value='<s:property value="accnoMainDataQueryParam.idBank"/>' />
						</td>
						<td width="20%">对账编号&nbsp;&nbsp;
							<input name="accnoMainDataQueryParam.voucherNo" type="text"
								class="diinput_text01"id="txtvoucherno"
								value='<s:property value="accnoMainDataQueryParam.voucherNo"/>' />
						</td>
						<td width="10%" align="center">
							<input name="export" type="button" class="submit_but09" id="export"
							value="打印账单和明细" onclick="batchPrintByType('dataAndDetails')" />
						</td>
					</tr>
					<tr>
						<td width="20%">网 点&nbsp;&nbsp;
							<select id="selectIdBank" onchange="changeIdBank()">
							</select>
						</td>
						<td width="20%">对账日期&nbsp;&nbsp;
							<input name="accnoMainDataQueryParam.docDate" type="text" title="对账日期"
								class="diinput_text01" id="docdate"
								onclick="new Calendar().show(this);"
								value='<s:property value="accnoMainDataQueryParam.docDate"/>' />
							<input type="hidden" id="curPage"
								name="accnoMainDataQueryParam.curPage"
								value='<s:property value="accnoMainDataQueryParam.curPage"/>' />
						</td>
						<td width="20%">账单状态 &nbsp;
							<s:select list="refDocstateMap" listKey="key"
								listValue="value" headerKey="" headerValue="--请选择--"
								name="accnoMainDataQueryParam.docstateFlag" id="docstateFlag"
								value="accnoMainDataQueryParam.docstateFlag">
							</s:select>
						</td>
						<td width="10%" align="center">
							<input name="export" type="button" class="submit_but09" id="printData"
							value="打印账单" onclick="batchPrintByType('data')" />
						</td>
					</tr>
					<tr>
						<td width="20%"><%-- 账 号&nbsp;&nbsp;
							<input name="accnoMainDataQueryParam.accNo" type="text"
								class="diinput_text01" id="accNo" 
								value='<s:property value="accnoMainDataQueryParam.accNo"/>' /> --%>
						</td>
						<td width="20%">客&nbsp;&nbsp;户&nbsp;号&nbsp;&nbsp;
							<input name="accnoMainDataQueryParam.custId" type="text"
								class="diinput_text01" id="custId"
								value='<s:property value="accnoMainDataQueryParam.custId"/>' />
						</td>
						<td width="20%">发送方式&nbsp;&nbsp;
								<s:select list="refSendMode" listKey="key" name="accnoMainDataQueryParam.sendMode" id="sendMode"
								listValue="value" headerKey="" headerValue="--请选择--"
								value="accnoMainDataQueryParam.sendMode">
							</s:select>							
						</td>
						<td width="10%" align="center">
							<input name="export" type="button" class="submit_but09" id="details"
							value="打印明细" onclick="batchPrintByType('details')" />
						</td>
					</tr>
				</table>
			</div>
			<div
				style="width:100%; height:auto; float:left; margin-left:10px; padding:10px 0px 10px 0px; color: #000000;">
				<h1>查询结果列表</h1>
				<table width="98%" border="0" cellpadding="0" cellspacing="0"
					id="checkMainDataInfoTable">
					<tr>
						<td width="3%" height="26" align="center" bgcolor="#c76c6f"
							class="font_colors01"><input name="selectall"
							type="checkbox" id="chkAll" onclick="CheckAll(this.form)"
							value="checkbox"></input><input type="hidden"
							id="batchSpecialFlag" name="batchSpecialFlag" /> <input
							type="hidden" id="selectIds" />
						</td>
						<td width="4%" align="center" bgcolor="#c76c6f"
							class="font_colors01">序号</td>
						<td width="8%" align="center" bgcolor="#c76c6f"
							class="font_colors01">账单编号</td>
						<td width="6%" align="center" bgcolor="#c76c6f"
							class="font_colors01">机构号</td>
						<td width="10%" align="center" bgcolor="#c76c6f"
							class="font_colors01">机构名称</td>
						<td width="14%" align="center" bgcolor="#c76c6f"
							class="font_colors01">户名</td>
						<td width="8%" align="center" bgcolor="#c76c6f"
							class="font_colors01">客户号</td>
						<td width="9%" align="center" bgcolor="#c76c6f"
							class="font_colors01">联系人</td>
						<td width="14%" align="center" bgcolor="#c76c6f"
							class="font_colors01">邮寄地址</td>
						<td width="8%" align="center" bgcolor="#c76c6f"
							class="font_colors01">联系电话</td>
						<td width="7%" align="center" bgcolor="#c76c6f"
							class="font_colors01">账单状态</td>
						<td width="7%" align="center" bgcolor="#c76c6f"
							class="font_colors01">打印次数</td>
					</tr>
					<tbody id="queryList" align="center">
						<s:iterator value="queryBillList" var="queryData" status="st">
							<tr bgcolor='<s:if test="#st.count%2==0">#F4DDDF</s:if>'>
								<td><input type="checkbox" name="selectId"
									value='<s:property value="#st.count"/>' title="<s:property value='#queryData.voucherNo' />"/></td>
								<td><s:property
										value="#st.count+accnoMainDataQueryParam.firstResult" />
								</td>
								<td><s:property value="#queryData.voucherNo" /></td>
								<td><s:property value="#queryData.idCenter" /></td>
								<td><s:property value="#queryData.bankName" /></td>
								<td><s:property value="#queryData.accName" /></td>
								<td><s:property value="#queryData.custId" /></td>
								<td><s:property value="#queryData.linkMan" /></td>
								<td><s:property value="#queryData.sendAddress" /></td>
								<td><s:property value="#queryData.phone" /></td>
								<td align="center"><s:property
										value="refDocstateMap.get(#queryData.docState)" /></td>
								<td><s:property value="#queryData.printTimes" /></td>
							</tr>
						</s:iterator>
					</tbody>
					<tfoot id="accInfoTable">
						<tr class="pagelinks">
							<td colspan="6">每页显示 <select
								name="accnoMainDataQueryParam.pageSize" id="pageSize"
								style="width:40px;" onchange="viewCheckMainDataListByPage('1')">
									<option value="10"
										<s:if test="accnoMainDataQueryParam.pageSize==10">selected="selected"</s:if>>10</option>
									<option value="20"
										<s:if test="accnoMainDataQueryParam.pageSize==20">selected="selected"</s:if>>20</option>
									<option value="50"
										<s:if test="accnoMainDataQueryParam.pageSize==50">selected="selected"</s:if>>50</option>
							</select>条记录&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;当前显示<s:if
									test="accnoMainDataQueryParam.total==0">0</s:if> <s:else>
									<s:property value="accnoMainDataQueryParam.firstResult+1" />
								</s:else>到<s:property value="accnoMainDataQueryParam.lastResult" />条，共<s:property
									value="accnoMainDataQueryParam.total" />条</td>
									<input type="hidden" id ="allinfo" value="<s:property value="accnoMainDataQueryParam.total" />"/>
							<td colspan="8" align="right"><s:if
									test="accnoMainDataQueryParam.curPage==1">
									<a>首页</a>&nbsp;&nbsp;<a>上一页</a>
								</s:if> <s:else>
									<a href="#" onclick="viewCheckMainDataListByPage('1');">首页</a>&nbsp;&nbsp;<a
										href="#"
										onclick="viewCheckMainDataListByPage('${accnoMainDataQueryParam.curPage - 1}');return false;">上一页</a>
								</s:else> <s:if
									test="accnoMainDataQueryParam.curPage==accnoMainDataQueryParam.totalPage">
									<a>下一页</a>&nbsp;&nbsp;<a>尾页</a>
								</s:if> <s:else>
									<a href="#"
										onclick="viewCheckMainDataListByPage(${accnoMainDataQueryParam.curPage + 1});return false;">下一页</a>&nbsp;&nbsp;<a
										href="#"
										onclick="viewCheckMainDataListByPage('${accnoMainDataQueryParam.totalPage}');return false;">尾页</a>
								</s:else>
							</td>
						</tr>
					</tfoot>
				</table>

			</div>
		</div>
	</form>
	<!-- <iframe id="tempiframe" ></iframe> -->
</body>
<object id="LODOP_OB"
	classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0>
	<embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0
		pluginspage="<%=path%>/ebs/lodopFile/install_lodop.exe"></embed>

</object>
</html>
