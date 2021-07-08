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
<base href="<%=basePath%>" />
<title>机构信息过滤 </title>
<link href="<%=path%>/ebs/common/css/css.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/dicss.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/process.css" rel="stylesheet" type="text/css" />
	
<script type="text/javascript" src="<%=path%>/ebs/common/js/jquery.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/orgTree.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/process.js"></script>

<script type="text/javascript">
/**
 * 数据查询 
 * 重新刷新页面数据
 */
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
	$("#organizaFilterForm").submit();
}

/**
 * 新增一条数据
 */
function addOrganizaFilter(){
	$("#addOrEdit").val("add");
	$("#editBankName").val("");
	$("#editIdBranch").val("");
	$("#editIdCenter").val("");
	$("#editIdBank").val("");
	showSon();
}

/**
 * 根据机构号显示机构信息
 */
function showBankInfo(){
	var button=$("#submitModify");
	var editIdBank =$("#editIdBank").val();
	if(editIdBank.length>0){
		var param="editIdBank="+editIdBank;
		//机构号校验
		$.ajax({
			type: "POST",
	        url: "organizaFilterAction!checkBankIsExist.action",
	        data: param+"&temp="+new Date(),
	        success: function(result){
	        	if(result=="checkSucess"){
 					button.removeAttr("disabled");
 					//获取机构信息，并回显
	        		$.ajax({
	        	         type: "POST",
	        	         url: "organizaFilterAction!findBankInfoByIdBank.action",
	        	         data: param+"&temp="+new Date(),
	        	         dataType:"json",
	        	         success: function(result){
    						$("#editBankName").val(result["editBankName"]);
    						$("#editIdBranch").val(result["editIdBranch"]);
    						$("#editIdCenter").val(result["editIdCenter"]);
	        	         }
	        		});	
	        	 }else if(result=="exist"){
					 alert("此机构号已维护，请勿重复维护");
					 button.attr("disabled","true");
					 $("#editBankName").val("");
					 $("#editIdBranch").val("");
					 $("#editIdCenter").val("");
				 }else if(result=="notFind"){
					 alert("此机构号不存在");
					 button.attr("disabled","true");
					 $("#editBankName").val("");
					 $("#editIdBranch").val("");
					 $("#editIdCenter").val("");
				 }
	         }
	     });
	}
}

/**删除数据**/
function deleteData(data){
	var cell=data.parentNode;
	var row=cell.parentNode;
	var idBank=row.cells[1].innerHTML;
	var param="idBank="+idBank;
	if(confirm("确认要删除数据吗?")){
		$.post("organizaFilterAction!deleteData.action",
				param+"&temp="+new Date(), 
				function(result){
					if(result="deleteSucess"){
						queryListByPage($("#curPage").val());
					}else{
						alert("数据删除异常");
					}
		});	
	}
}


/**
 * 提交修改
 */
function saveOrUpdate(){
	var param="addOrEdit="+$("#addOrEdit").val();
	var editIdBank=$("#editIdBank").val();
	if(editIdBank.length>0){
		param+="&editIdBank="+editIdBank;
		param+="&editBankName="+$("#editBankName").val();
		param+="&editIdBranch="+$("#editIdBranch").val();
		param+="&editIdCenter="+$("#editIdCenter").val();
		
		if(confirm("确认提交数据吗?")){
			$.post("organizaFilterAction!saveData.action",
					param+"&temp="+new Date(), 
					function(result){
						if(result="saveSucess"){
							queryListByPage($("#curPage").val());
						}else{
							alert("保存数据异常");
						}
			});	
		}
	}else{
		alert("机构号不能为空");
	}
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
	<input id="moudleName" type="hidden" value="机构信息过滤" />
	<input id="addOrEdit" name="addOrEdit" type="hidden" />
	<div id="son_div" class="son_div" style="width: 600px; height: 200px;left: 20%;">
		<div class="sonpage" id="sonpage">
			<center>
				<tr>
					<td style="vertical-align: top;" colspan="2">机构过滤维护</td>
				</tr>
			</center>
		</div>
		<%@ include file="/ebs/organizaFilter/organizaFilter_div.jsp"%>
	</div>
	<form id="organizaFilterForm" method="post"
		action="organizaFilterAction!queryData.action" name="organizaFilterForm">
		<div class="nov_moon">
			<div style="width:100%; height:auto; float:left; margin-left:10px; padding:10px 0px 10px 0px;"
				class="border_bottom01">
				<table width="98%">
					<tr>
						<td width="20%">总 行&nbsp;&nbsp;
							<select>
								<option value="">华融湘江总行</option>
							</select>
						</td>
						<td width="20%">分 行&nbsp;&nbsp;&nbsp; 
							<select name="param.idCenter" id="selectIdCenter" 
									onchange="changeIdCenter()">
							</select>
						</td>
						<td width="20%">
							网 &nbsp;&nbsp;&nbsp;点
							<select id="selectIdBank" onchange="changeIdBank()" >
							</select>
						</td>
						<td width="10%">
							<input name="queryData" type="button" class="submit_but09" id="queryData"
								value="查询" onclick="queryListByPage('1')" />
						</td>
					</tr>
					<tr>
						<td width="20%">
							机构号 <input name="param.idBank" type="text" class="diinput_text01" 
								id="idBank" maxlength="12" value='<s:property value="param.idBank"/>' />
						</td>
						<td width="20%">
						</td>
						<td width="20%">
						</td>
						<td width="10%">
							<input name="addData" type="button" class="submit_but09" id="addData"
								value="新增" onclick="addOrganizaFilter()"/>
						</td>
					</tr>
				</table>
			</div>
			<div style="width:100%; height:auto; float:left; margin-left:10px; padding:10px 0px 10px 0px; color: #000000;">
				<h1>查询结果列表</h1>
				<table width="98%" border="0" cellpadding="0" cellspacing="0" id="queryInfo">
					<tr>
						<td width="5%" height="26" align="center" bgcolor="#c76c6f" class="font_colors01">序号</td>
						<td width="15%" align="center" bgcolor="#c76c6f" class="font_colors01">机构号</td>
						<td width="35%" align="center" bgcolor="#c76c6f" class="font_colors01">机构名称</td>
						<td width="15%" align="center" bgcolor="#c76c6f" class="font_colors01">上级机构</td>
						<td width="15%" align="center" bgcolor="#c76c6f" class="font_colors01">对账中心</td>
						<td width="13%" align="center" bgcolor="#c76c6f" class="font_colors01">操作</td>
					</tr>
					<tbody id="queryList" align="center">
						<s:iterator value="queryList" var="queryData" status="st">
							<tr <s:if test="#st.index%2 ==0">bgcolor="#CBD6E0"</s:if>
								<s:if test="#st.index%2 ==1">bgcolor="#FFFFFF"</s:if>>
								<td>
									<s:property value="#st.count+param.firstResult-1" />
								</td>
								<td>
									<s:property value="#queryData.idBank" />
								</td>
								<td>
									<s:property value="#queryData.bankName" />
								</td>
								<td>
									<s:property value="#queryData.idBranch" />
								</td>
								<td>
									<s:property value="#queryData.idCenter" />
								</td>
								<td style="border:1px solid #C76C6F">
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
			</div>
		</div>
		<input type="hidden" id="curPage" name="param.curPage" value='<s:property value="param.curPage"/>' />
	</form>
</body>
</html>
