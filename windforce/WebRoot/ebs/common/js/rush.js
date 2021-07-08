

function fnCheck() 
{ 
	var e = document.getElementsByName( "selectId"); 
	var num = 0; 
	for( var i = 0; i < e.length; i++ ){ 
	if( e[i].checked==true) 
		num++; 
	} 
	if(num < 1 ){ 
	return false; 
	}else{
	return true;}
}
function batchProcess(){
	if(fnCheck()){
		showSon2();	
	}else{
		alert("请选择至少一条要维护的记录");
	}
}

function submitBatchProcess(){
	if(confirm("确认提交修改吗?")){
		startProcess("正在修改...");
		var arrData = new Array();
		var e = document.getElementsByName( "selectId"); 
		var count = 0;
		for( var i = 0; i < e.length; i++ ){ 
			if( e[i].checked==true) {
				arrData[count] = e[i].value; 
				count++;
			}
		} 
		var selectId = arrData.join(",");
		var param = "selectIds="+ selectId;
		param=param+"&rushMethod="+document.getElementById("batchRushMethod_div").value;
		param=param+"&rushResult="+document.getElementById("batchRushResult_div").value;
		param=param+"&rushDesc="+document.getElementById("batchRushDesc_div").value;	
		param=param+"&customResponse="+document.getElementById("batchCustomResponse_div").value;
		$.post("rushAction_batchModify.action", param, function(result){
			dealResult_batchProcess(result);
		});			
}
}
function modifyAcc(data){
	
	var cell=data.parentNode;
	var row=cell.parentNode;
	document.getElementById("tableIndex").value=row.rowIndex;  //当前行下标
	  startProcess("正在从后台获取对账单信息...");	
		$.getJSON("rushAction_getCheckMainDataByIndex.action", "index="+row.rowIndex+"&voucherNo="+row.cells[3].innerHTML, function(checkMainData) {
			if(checkMainData==null){
				alert("未从后台缓存中获取到对应的帐单信息,请尝试刷新界面");
				stopProcess();
			}else{
				stopProcess();
				document.getElementById("customResponse_div").value=checkMainData.customResponse;
				document.getElementById("voucherNo_div").value=checkMainData.voucherNo;
				document.getElementById("accName_div").value=checkMainData.accName;
				var rushMethod=document.getElementById("rushMethod_div");
				 for (var i = 0; i < rushMethod.length; i++) {
					if(checkMainData.rushMethod==""){
						rushMethod.options[0].selected=true;
						break;
					}
			      if (rushMethod.options[i].value == checkMainData.rushMethod){
			    	  rushMethod.options[i].selected=true;
			     	 break;
			      }
				 }
				var rushResult=document.getElementById("rushResult_div");
				 for (var i = 0; i < rushResult.length; i++) {  
					 if(checkMainData.rushResult==""){
						 rushResult.options[0].selected=true;
						 break;
					 }
			        if (rushResult.options[i].value == checkMainData.rushResult){
			        	rushResult.options[i].selected=true;
			       	 break;
			        }
				 }
				 var customResponse=document.getElementById("customResponse_div");
				 for (var i = 0; i < customResponse.length; i++) {  
					 if(checkMainData.customResponse==""){
						 customResponse.options[0].selected=true;
						 break;
					 }
			        if (customResponse.options[i].value == checkMainData.customResponse){
			        	customResponse.options[i].selected=true;
			       	 break;
			        }
				 }
				 document.getElementById("rushDesc_div").value=checkMainData.rushDesc;
				 showSon();
			}
		});
}

/**
 * 提交修改
 */
function submitModify(){
	
	if(document.getElementById("rushMethod_div").value.length == 0){
		alert("请选催收择方式！");
		document.getElementById("rushMethod_div").focus();
	}
	else if(document.getElementById("rushResult_div").value.length == 0){
		alert("请选择处理意见!");
		document.getElementById("rushResult_div").focus();
	}
	else{
			var index=document.getElementById("tableIndex").value;
			var param="index="+index;
			param=param+"&voucherNo="+document.getElementById("voucherNo_div").value;
			param=param+"&rushMethod="+document.getElementById("rushMethod_div").value;
			param=param+"&rushResult="+document.getElementById("rushResult_div").value;
			param=param+"&rushDesc="+document.getElementById("rushDesc_div").value;
			param=param+"&customResponse="+document.getElementById("customResponse_div").value;			
			if(confirm("确认提交修改吗?")){
				startProcess("正在修改...");
				$.post("rushAction_modify.action", param, function(result){
					dealResult_modify(result,tableIndex);
			});	
		}
	}
}



function dealResult_modify(result,tableIndex){
	 if(result!=null&&result.length>0){
		 stopProcess();
		 alert(result);
	 }else{	
		stopProcess();
		closeSon();
		viewRushListByPage(1);
	 }
	}

	function dealResult_batchProcess(result){
	 if(result!=null&&result.length>0){
		 stopProcess();
		 alert(result);
	 }else{
		 stopProcess();
		 closeSon2();
		 viewRushListByPage(1);
	 }
	}
