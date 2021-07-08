
function modifyAcc(data){
	var cell=data.parentNode;
	var row=cell.parentNode;
	document.getElementById("tableIndex").value=row.rowIndex;  //当前行下标
	startProcess("正在从后台获取对账单信息...");	
	$.getJSON("retreatProcessAction_getCheckMainDataByIndex.action", "index="+row.rowIndex+"&voucherNo="+row.cells[3].innerHTML, function(checkMainData) {
		if(checkMainData==null){
			alert("未从后台缓存中获取到对应的帐单信息,请尝试刷新界面");
			stopProcess();
		}else{
			stopProcess();
			document.getElementById("idBank_div").value=checkMainData.idBank;
			document.getElementById("voucherNo_div").value=checkMainData.voucherNo;
			document.getElementById("accName_div").value=checkMainData.accName;
			document.getElementById("urgeDate_div").value=checkMainData.urgeDate;
			document.getElementById("urgeType_div").value=checkMainData.urgeType;
			document.getElementById("urgeTimes_div").value=checkMainData.urgeTimes;
			var urgeResult=document.getElementById("urgeResult_div");
			 for (var i = 0; i < urgeResult.length; i++) { 
				if(checkMainData.urgeResult==""){
					urgeResult.options[0].selected=true;
					break;
				}
		        if (urgeResult.options[i].value == checkMainData.urgeResult){
		        	urgeResult.options[i].selected=true;
		       	 break;
		        }
			 }
			 document.getElementById("urgeDesc_div").value=checkMainData.urgeDesc;
			showSon();
		}
	});
}

/**
 * 提交修改
 */
function submitModify(){

	if(document.getElementById("urgeResult_div").value.length == 0){
		alert("请选处理意见！");
		document.getElementById("urgeResult_div").focus();
	}else{
	var dealResult = document.getElementById("urgeResult_div").value;
	var tableIndex=document.getElementById("tableIndex").value;
	var param="index="+tableIndex;
	param=param+"&voucherNo="+document.getElementById("voucherNo_div").value;
	param=param+"&urgeResult="+document.getElementById("urgeResult_div").value;
	param=param+"&urgeDesc="+document.getElementById("urgeDesc_div").value;
	if(document.getElementById("urgeResult_div") != null && dealResult!="")
		{
	if(confirm("确认提交修改吗?")){
		startProcess("正在修改...");
		$.post("retreatProcessAction_modify.action", param, function(result){
			dealResult_modify(result,tableIndex);
	});	
	}
}else
	{
	alert("请选择处理意见");
	}
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
		param=param+"&urgeResult="+document.getElementById("batchurgeResult_div").value;
		param=param+"&urgeDesc="+document.getElementById("batchretreatDesc_div").value;
		$.post("retreatProcessAction_batchModify.action", param, function(result){
			dealResult_batchProcess(result);
		});			
}
}


function dealResult_modify(result,tableIndex){
	 if(result!=null&&result.length>0){
		 stopProcess();
		 alert(result);
	 }else{	
	    stopProcess();
	    closeSon();
	    viewRetreathListByPage(1);
	 }
	}
function dealResult_batchProcess(result){
	 if(result!=null&&result.length>0){
		 stopProcess();
		 alert(result);
	 }else{
		 stopProcess();
		 closeSon2();
		 viewRetreathListByPage(1);
	 }
	}