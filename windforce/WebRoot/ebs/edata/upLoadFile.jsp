<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<link href="<%=path%>/ebs/common/css/css.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/dicss.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/ebs/common/css/process.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="<%=path%>/ebs/common/js/jquery.js"></script>
<script type="text/javascript" src="<%=path%>/ebs/common/js/jquery.form.js"></script>

<script type="text/javascript">

function uploadFile(){
	var dataDate = $("#dataDate").val();
	var button=$("#uploadFileBut");
	button.attr("disabled","true");
	$("#uploadForm").ajaxSubmit({  
	                    	type: "post",
		                    url: "EDataAction_upLoadFile.action" , 
		                    data: "dataDate="+dataDate,
		                    success: function(data){
		                        alert(data);
		                        button.removeAttr("disabled");
		                        $("#uploadForm").resetForm();  
		                    }
		       }); 
	 }

</script>
  
</head>
<body class="nov_moon" style="background:url('/windforce/ebs/common/images/bg_content.jpg')">
	<div style="height:300px ;width:500px;" >
		<input id="dataDate" name="dataDate" style="display: none">
		<center><font size="4">文件上传</font></center>
		<form id="uploadForm" name="uploadForm" method="post" enctype="multipart/form-data">
		 	<table align="center" style="top: 15px;">
		 		<tr>
		 			<td>
		 				机构(UTBLBRCD)：
		 			</td>
					<td>
						<s:file name="utblbrcdFile" label="浏览" id="utblbrcdFile" cssStyle="width: 100px," />
					</td>
				<tr/>
				<tr>
					<td>
		 				人员(KUB_USER)：
		 			</td>
					<td>
						<s:file name="userFile" label="浏览"  id="userFile" cssStyle="width:100px,"  />
					</td>
				</tr>	
				<tr>
					<td>
		 				内部账户(INNERBASICINFO)：
		 			</td>
					<td>
						<s:file name="innerbasicinfoFile" label="浏览"  id="innerbasicinfoFile" cssStyle="width:100px,"  />
					</td>
				</tr>	
				<tr>
					<td>
		 				账户信息(MAINDATA)：
		 			</td>
					<td>
						<s:file name="maindataFile" label="浏览"  id="maindataFile" cssStyle="width:100px," />
					</td>
				</tr>	
				<tr>
					<td>
		 				单位协定存款户(XDCKLIST)：
		 			</td>
					<td>
						<s:file name="xdckFile" label="浏览"  id="xdckFile" cssStyle="width:100px," />
					</td>
				</tr>	
				
				<tr>
					<td>
		 				网银账户(ACCTLIST)：
		 			</td>
					<td>
						<s:file name="acctlistFile" label="浏览"  id="acctlistFile" cssStyle="width:100px," />
					</td>
				</tr>
				<tr>
					<td>
		 				汇率(KNP_EXRT)：
		 			</td>
					<td>
						<s:file name="knpExrtFile" label="浏览"  id="knpExrtFile" cssStyle="width:100px," />
					</td>
				</tr>
				<tr>
					<td>
		 				发生额明细(DEPHIST)：
		 			</td>
					<td>
						<s:file name="dephistFile" label="浏览"  id="dephistFile" cssStyle="width: 100px," />
					</td>
				</tr>
				<tr>
					<td align="right">
						<input type="button" value="上传" id="uploadFileBut" align="right" onclick="uploadFile()" class="submit_but09"/>
					</td>
					<td align="center">
						<input type="button" value="关闭" id="closeBut"  align=right onclick="window.close()" class="submit_but09"/>
					</td>
				</tr>
		 	</table>
		</form>	
	</div>
</body>
</html>
