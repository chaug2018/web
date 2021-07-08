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
<title>发送方式参数维护</title>
<link href="<%=path%>/ebs/common/css/css.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/process.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/dicss.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="<%=path%>/ebs/common/js/jquery.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/orgTree.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/process.js"></script>

<script type="text/javascript">
		var err = "<s:property value='errMsg'/>";
		if (err != null && err.length > 0) {
			alert("初始化服务出现错误"); 
			top.refresh();
		}
</script>
<script type="text/javascript">

	//打开新增页面
	function addData(){
		//进入编辑界面操作：1、设置对账中心和发送方式两个下拉框为可编辑；2、清空3个下拉框的值；3、设置"新增"标签；4、显示内嵌页面
		$("#editIdCenter").removeAttr("disabled");
		$("#editSendMode").removeAttr("disabled");
		$("#editIdCenter").val("");
		$("#editSendMode").val("");
		$("#editAddressFlug").val("");
		$("#addOrEdit").val("add");
		showSon();
	}
	
	//保存数据
	function saveOrUpdate(){
		//获取选中信息：
		var addOrEdit = $("#addOrEdit").val(); 
		var idCenter = $("#editIdCenter").val();
		var sendMode = $("#editSendMode").val();
		var sendAddress = $("#editAddressFlug").val();
		var flag = "true";
		var param = "addOrEdit="+addOrEdit+"&idCenter="+idCenter+"&sendMode="+sendMode+"&sendAddress="+sendAddress;
		if(addOrEdit=="add"){
			//下拉框未选值提示
			if(idCenter==""){
				alert("请选择分行");
				return false;
			}
			if(sendMode==""){
				alert("请选择发送方式");
				return false;
			}
			if(sendAddress==""){
				alert("请选择投递地址");
				return false;
			}
			$.ajax({
	  	         type: "POST",
	  	         url: "changeAddressAction!ifExist.action",
	  	         data: param+"&temp="+new Date(),
	  	         success: function(result){
	  	        	 if(result =="exist"){
  	        		 	alert("此投递地址已存在，请勿重复增加");
	  	        	 }else{
	  	        		saveDate(param);
	  	        	 }
	  	         }
	  		});	
		}else if(addOrEdit=="edit"){
			saveDate(param);
		}
	}
	
	/**
	* 提交数据
	**/
	function saveDate(param){
		$.ajax({
			 type: "POST",
			 url: "changeAddressAction!addAddressData.action",
			 data: param+"&temp="+new Date(),
			 success: function(result){
				 if(result=="addSuccess"){
					queryListByPage("<s:property value='param.curPage'/>");
				 }else{
					 alert("新增数据异常");
				 }
			 }
		 });
	}
	
	
	//删除一条数据
	function deleteData(data){
		var cell=data.parentNode;
		var row=cell.parentNode;
		var idCenter=row.cells[1].innerHTML;
		var sendMode=row.cells[3].innerHTML;
		var sendAddress = row.cells[4].innerHTML;
		var param="idCenter="+idCenter+"&sendMode="+sendMode+"&sendAddress="+sendAddress;
		if(confirm("确认要删除数据吗?")){
			$.post("changeAddressAction!deleteData.action",
					param+"&temp="+new Date(), 
					function(result){
						if(result="deleteSucess"){
							queryListByPage("<s:property value='param.curPage'/>");
						}else{
							alert("数据删除异常");
						}
			});	
		}
	}
	
	//修改一条记录
	function updateData(data){
		var cell=data.parentNode;
		var row=cell.parentNode;
		var idCenter=row.cells[1].innerHTML;
		var sendMode=row.cells[3].innerHTML;
		var sendAddress = row.cells[4].innerHTML;
		$("#editIdCenter").val(idCenter);
		if(sendMode=="柜台"){
			sendMode = "1";
		}else if(sendMode == "面对面"){
			sendMode = "4";
		}
		if(sendAddress=="分行"){
			sendAddress = "0";
		}else if(sendAddress == "网点"){
			sendAddress = "1";
		}
		$("#editSendMode").val(sendMode);
		$("#editAddressFlug").val(sendAddress);
		//设置编辑标签
		$("#addOrEdit").val("edit");
		$("#editIdCenter").attr("disabled","disabled");
		$("#editSendMode").attr("disabled","disabled");
		showSon();
	}
	
	//数据分页查询
	function queryListByPage(pageNumber){
	var totalPage= "<s:property value='param.totalPage'/>";
	if(pageNumber<=0){
		pageNumber=1;
	}else if(pageNumber>totalPage){
		pageNumber=totalPage;
	}
	//如果总页数为0的
	if(totalPage==0){
		pageNumber=1;
	}
	$("#curPage").val(pageNumber);
	$("#changeAddressForm").submit();
	}
	
	//查询分行发送方式的详细地址
	function queryDetailInfo(data){
		/**
		1、获取改行信息：分行行号idCenter、发送方式sendMode
		2、将参数发送至后台指定的Action地址
			2.1、获取传送的参数；
			2.2、根据参数调用BasicInfoAdm中的分页查询方法getBasicinfoData，获取BasicInfo的结果集；
		3、显示获取到的记录到jsp页面；
			3.1、增加新的JSP页面：changeaddress_DetailInfo.jsp；
			3.2、struts-ebs.xml中配置Action方法执行后跳转的指定JSP名字：<result name="queryDetailInfo">/ebs/address/changeaddress_DetailInfo.jsp</result>
		4、JSP：
			4.1、布局：table：账号(AccNo)、账户名(AccNoName)、对账中心(idCenter)、发送方式(sendMode)、发送地址(sendAddress)
		5、分页处理：
			5.1、定义独立的分页参数对象；
			5.2、定义独立的分页查询方法；
			5.3、后台修改分页参数的属性；
		*/
		var cell=data.parentNode;
		var row=cell.parentNode;
		var idCenter=row.cells[1].innerHTML;
		var sendModeStr=row.cells[3].innerHTML;
		var sendAddress = row.cells[4].innerHTML;
		if(sendModeStr=="柜台"){
			sendMode="1";
		}else if(sendModeStr=="面对面"){
			sendMode="4";
		}
		//给二层页面中的隐藏按钮赋值
		$("#detailIDCenter").val(idCenter);
		$("#detailSendMode").val(sendMode);
		
		//是否该初始化明细页分页参数的标签
		//传参给后台
		var param = "idCenter="+idCenter+"&sendMode="+sendMode+"&flag=true";
		/**
		channelmode=yes|no|1|0 是否使用剧院模式显示窗口。默认为 no。 
		directories=yes|no|1|0 是否添加目录按钮。默认为 yes。 
		fullscreen=yes|no|1|0 是否使用全屏模式显示浏览器。默认是 no。处于全屏模式的窗口必须同时处于剧院模式。 
		height=pixels 窗口文档显示区的高度。以像素计。 
		left=pixels 窗口的 x 坐标。以像素计。 
		location=yes|no|1|0 是否显示地址字段。默认是 yes。 
		menubar=yes|no|1|0 是否显示菜单栏。默认是 yes。 
		resizable=yes|no|1|0 窗口是否可调节尺寸。默认是 yes。 
		scrollbars=yes|no|1|0 是否显示滚动条。默认是 yes。 
		status=yes|no|1|0 是否添加状态栏。默认是 yes。 
		titlebar=yes|no|1|0 是否显示标题栏。默认是 yes。 
		toolbar=yes|no|1|0 是否显示浏览器的工具栏。默认是 yes。 
		top=pixels 窗口的 y 坐标。 
		width=pixels 窗口的文档显示区的宽度。以像素计。 
		*/
		window.open("changeAddressAction!queryBasicInfo.action?"+param,
					"账户发送方式明细列表",
					"height=400,width=850,top=200,left=400,directories=no,location=no,menubar=no,resizable=no,scrollbars=yes,status=no,titlebar=no,toolbar=no");
	}
	
	$(document).ready(function(){
		var idCenter="<s:property value='param.idCenter'/>";
		var idBank="<s:property value='param.idBank'/>";
		var orgTree=${orgTree};
		initTree(orgTree,idCenter,idBank);
	});
</script>

</head>
<body class="baby_in2">
	<input id="moudleName" type="hidden" value="投递地址维护" />
	<!-- 用于区别新增和编辑的隐藏按钮 -->
	<input id="addOrEdit" name="addOrEdit" type="hidden" />
	<!-- 内嵌页面：用于新增和编辑 -->
	<div id="son_div" class="son_div" style="width: 600px; height: 200px;left: 20%;">
		<div class="sonpage" id="sonpage">
			<center>
				<tr>
					<td style="vertical-align: top;" colspan="2">柜面和面对面投递地址维护</td>
				</tr>
			</center>
		</div>
		<%@ include file="/ebs/address/changeaddress_div.jsp"%>
	</div>
	<!-- 用于提交的表单 -->
	<form id="changeAddressForm" action="changeAddressAction!queryData.action" name="changeAddressForm" method="post">
		<div class="nov_moon">
			<div style="width:98%; height:auto; float:left; margin-left:10px; padding:10px 0px 10px 0px;" class="border_bottom01">
				<table width="98%">
					<tr>
						<td>
							总行 
						</td>
						<td>
							<s:textfield label="" value="华融湘江银行总行"></s:textfield>
						</td>
						<td>
							分 行
						</td>
						<td>
							<select id="selectIdCenter" name="param.idCenter" >	
							</select >					
						</td>
						<td>
							发送方式：		
						</td>
						<td>
							<s:select id="sendMode"  name="param.sendMode" list="sendModeMap" listKey="key"
								listValue="value" lable="" headerKey="" headerValue="-请选择-" ></s:select>
						</td>	
						<td>
							<input type="button" class="submit_but09" 
								onclick="queryListByPage('1')" value="查询" />
						</td>
					</tr>
					<tr>
						<td>
						</td>
						<td>
						</td>
						<td>
						</td>
						<td>
						</td>
						<td>
						</td>
						<td>
						</td>
						<td>
							<input type="button" class="submit_but09" 
								onclick="addData()" value="新增" />
						</td>	
					</tr>
				</table>
			</div>
			<div style="width:100%; height:auto; float:left; margin-left:10px; padding:10px 0px 10px 0px; color: #000000;">
			<h1>查询结果列表</h1>
				<table width="98%" cellpadding="0" cellspacing="0" id="faceFlugTable">
					<thead >
						<tr>
							<td width="5%" height="26" align="center" bgcolor="#c76c6f" class="font_colors01">序号</td>
							<td width="20%" align="center" bgcolor="#c76c6f" class="font_colors01">对账中心机构号</td>
							<td width="20%" align="center" bgcolor="#c76c6f" class="font_colors01">对账中心名称</td>
							<td width="15%" align="center" bgcolor="#c76c6f" class="font_colors01">发送方式</td>
							<td width="20%" align="center" bgcolor="#c76c6f" class="font_colors01">投递地址</td>
							<td width="18%" align="center" bgcolor="#c76c6f" class="font_colors01">操作</td>
						</tr>
					</thead>
					<tbody id="faceFlugList" align="center">
						<s:iterator value="faceFlugList" var="queryData" status="st">
							<tr <s:if test="#st.index%2 ==0">bgcolor="#CBD6E0"</s:if>
								<s:if test="#st.index%2 ==1">bgcolor="#FFFFFF"</s:if>>
								<td><s:property value="#st.count+param.firstResult-1" /></td>
								<td><s:property value="#queryData.idCenter" /></td>
								<td><s:property value="idCenterValueMap.get(#queryData.idCenter)" /></td>
								<td ><s:property value="sendModeMap.get(#queryData.sendMode)" /></td>
								<td ><s:property value="addressMap.get(#queryData.addressFlug)" /></td>
								<td style="border:1px solid #C76C6F">
									<a onclick="queryDetailInfo(this)" style="color:#BE333A">查看</a>&nbsp;&nbsp;&nbsp;
									<a onclick="updateData(this)" style="color:#BE333A">编辑</a>&nbsp;&nbsp;&nbsp;
									<a onclick="deleteData(this)" style="color:#BE333A">删除</a>
								</td>
							</tr>
						</s:iterator>
					</tbody>
					<tfoot id="pageInfo">
							<tr class="pagelinks">
								<td colspan="4">
									每页显示
									<s:select cssStyle="width:50px;" id="param.pageSize" name="param.pageSize" onchange="queryListByPage('1')"
										list="param.pageSizeMap" listKey="key" listValue="value" headerKey="10" headerValue="10" />
									条记录&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									当前第
									<s:property value="param.curPage" />
									页&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									当前显示
									<s:property value="param.firstResult" />
									到
									<s:property value="param.lastResult" />
									条，共
									<s:property value="param.total" />
									条
								</td>
								<td colspan="2" align="right">
									<a onclick="queryListByPage('1')">首页</a>&nbsp;&nbsp;
									<a onclick="queryListByPage(<s:property value="param.curPage-1"/>)">上一页</a>&nbsp;&nbsp;
									<a onclick="queryListByPage(<s:property value="param.curPage+1"/>)">下一页</a>&nbsp;&nbsp;
									<a onclick="queryListByPage(<s:property value="param.totalPage"/>)">尾页</a>
								</td>
							</tr>
					</tfoot>
				</table>
				<div id="display_div" style="display: none">
					<input type="hidden" id="curPage" name="param.curPage" value='<s:property value="param.curPage"/>' />
					<select id="selectIdBank"></select>
				 	<input id="idBank" type="text"/>
				</div>
			</div>
		</div>
	</form>
</body>
</html>
