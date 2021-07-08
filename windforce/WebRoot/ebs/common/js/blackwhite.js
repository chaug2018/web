
function modifyAcc(data){
	var cell=data.parentNode;
	var row=cell.parentNode;
	document.getElementById("tableIndex").value=row.rowIndex;  //当前行下标
    startProcess("正在从后台获取账号信息...");	
	$.getJSON("blackWhiteAction_getBasicInfoByIndex.action", 
			"index="+row.rowIndex+"&accNo="+row.cells[1].innerHTML+"&temp="+new Date(), 
			function(basicInfo) {
				if(basicInfo==null){
					alert("未从后台缓存中获取到对应的账号信息,请尝试刷新界面");
					stopProcess();
				}else{
					stopProcess();
					document.getElementById("dataIndex").value=row.cells[0].innerHTML;
					document.getElementById("accNo_div").value=basicInfo.accNo;
					document.getElementById("orgId_div").value=basicInfo.idBank;
					document.getElementById("accName_div").value=basicInfo.accName;
					document.getElementById("adress_div").value=basicInfo.sendAddress;
					document.getElementById("zip_div").value=basicInfo.zip;
					document.getElementById("linkMan_div").value=basicInfo.linkMan;
					document.getElementById("phone_div").value=basicInfo.phone;
					document.getElementById("sealNo_div").value=basicInfo.sealAccNo;
					document.getElementById("sealMode_div").value=basicInfo.sealMode;
					document.getElementById("accnoFlag_div").value=basicInfo.accCycle;
					document.getElementById("isCheck_div").value=basicInfo.isCheck;
	
					var sendTypes=document.getElementById("sendType_div");
					var flag =basicInfo.sendMode;
					if(flag=='4'){		
						//添加 一个 option 面对面
						$("#sendType_div").append("<option value='4'>面对面</option>")
						$("#sendType_div").attr("disabled","disable");
					}else{
						// 删除 一个 option 面对面
						$("#sendType_div").removeAttr("disabled");
						$("#sendType_div option[value='4']").remove();
					}		
					sendTypes.options[0].selected=true;
					 for (var i = 0; i < sendTypes.length; i++) {  	
				        if (sendTypes.options[i].value == basicInfo.sendMode){
				        	sendTypes.options[i].selected=true;
				       	 break;
				        }
				    }
					showSon();
				}
	});
}

/**
 * 提交修改
 */
function submitModify(){

		var tableIndex=document.getElementById("tableIndex").value;
		var param="index="+tableIndex;
		param=param+"&accNo="+document.getElementById("accNo_div").value;
		param=param+"&accName="+document.getElementById("accName_div").value;
		param=param+"&orgId="+document.getElementById("orgId_div").value;
		param=param+"&sendAddress="+document.getElementById("adress_div").value;
		param=param+"&zip="+document.getElementById("zip_div").value;
		param=param+"&linkMan="+document.getElementById("linkMan_div").value;
		param=param+"&phone="+document.getElementById("phone_div").value;
		param=param+"&sealNo="+document.getElementById("sealNo_div").value;
		param=param+"&sendType="+document.getElementById("sendType_div").value;
		param=param+"&accCycle="+document.getElementById("accnoFlag_div").value;
		var sendType = document.getElementById("sendType_div").value;
		if(param.indexOf("=&", 0)>-1 || sendType==""){
			alert("信息输入不完整！");
		}else{
			param=param+"&sealMode="+document.getElementById("sealMode_div").value;
			param=param+"&accnoFlag="+document.getElementById("accnoFlag_div").value;
			param=param+"&isCheck="+document.getElementById("isCheck_div").value;
			if(confirm("确认提交修改吗?")){
				startProcess("正在修改...");
				$.post("blackWhiteAction_submitModify.action", 
						param,
						function(result){
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
	    queryBlackWhite();
	 }
	}

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
function batchUpdateBw(){
	if(fnCheck()){
		showSon2();	
	}else{
		alert("请选择至少一条要维护的记录");
	}
}
function submitBatchModify(){
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
		param=param+"&batchSpecialFlag="+document.getElementById("batchSpecialFlag_div").value;
		$.post("blackWhiteAction_batchModify.action", param, function(result){
			dealResult_batchModify(result,arrData);
	});	
	}
}

function dealResult_batchModify(result,arrData){
	 if(result!=null&&result.length>0){
		 stopProcess();
		 alert(result);
	 }else{	
//		 var table=document.getElementById("basicInfoTable");
//		 for(var i=0;i<arrData.length;i++){
//			 	var tableIndex = arrData[i];
//				var row =table.rows[tableIndex];
//				var specialFlag=document.getElementById("batchSpecialFlag_div");
//				row.cells[11].innerHTML=specialFlag.options[specialFlag.selectedIndex].text;
//				//获取当前日期
//				var signTime = getFormatDateStr(new Date());
//				row.cells[9].innerHTML=signTime;
//		 }
	    stopProcess();
	    closeSon2();
	    queryBlackWhite();
	 }
	}
//获取指定日期“yyyy-MM-dd”格式字符串
function getFormatDateStr(currentDate) {
    var currentDateStr = "";
    var year = currentDate.getFullYear();
    var month = currentDate.getMonth() + 1;
    var day = currentDate.getDate();
   
    currentDateStr += year + "-";
    if (month >= 10) {
        currentDateStr += month + "-";
    } else {
        currentDateStr += "0" + month + "-";
    }
    if (day >= 10) {
        currentDateStr += day + " ";
    } else {
        currentDateStr += "0" + day + " ";
    }
    return currentDateStr;
}