
function deleteAcc(data){
	var cell=data.parentNode;
	var row=cell.parentNode;
	var accno=row.cells[1].innerHTML;
	if(confirm("确认删除吗?")){
		startProcess("正在删除...");
		$.post("accModifyAction_delete.action", 
				"accNo="+accno+"&index="+row.rowIndex, 
				function(result){
					dealResult_delete(result,row);
		});	
	}
}

function addSign(){
	document.getElementById("accNo_div").value="";
	document.getElementById("orgId_div").value="";
	document.getElementById("accName_div").value="";
	document.getElementById("adress_div").value="";
	document.getElementById("zip_div").value="";
	document.getElementById("linkMan_div").value="";
	document.getElementById("phone_div").value="";
	document.getElementById("sealNo_div").value="";
	document.getElementById("contract_div").value="";
	document.getElementById("accNo_div").readOnly=false;
	document.getElementById("orgId_div").readOnly=false;
	document.getElementById("accName_div").readOnly=false;
	showSon();
}

function modifyAcc(data){
	var cell=data.parentNode;
	var row=cell.parentNode;
	document.getElementById("tableIndex").value=row.rowIndex;  //当前行下标
    startProcess("正在从后台获取账号信息...");	
	$.getJSON("accModifyAction_getBasicInfoByIndex.action", 
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
//						//$("#sendType_div").append("<option value='4'>面对面</option>")
//						//$("#sendType_div").attr("disabled","disable");
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
 * 签约
 * @param data
 */
function sign(data){
	var cell=data.parentNode;
	var row=cell.parentNode;
	document.getElementById("accNo_div").readOnly=true;
	document.getElementById("orgId_div").readOnly=true;
	document.getElementById("accName_div").readOnly=true;
	document.getElementById("tableIndex").value=row.rowIndex;  //当前行下标	
    startProcess("正在从后台获取账号信息...");	
	$.getJSON("accSignAction_getBasicInfoByIndex.action", "index="+row.rowIndex+"&temp="+new Date()+"&accNo="+row.cells[1].innerHTML,
			function(basicInfo) {
		
		if(basicInfo==null){
			alert("未从后台缓存中获取到对应的账号信息,请尝试刷新界面");
			stopProcess();
		}else{
			stopProcess();
			document.getElementById("accNo_div").value=basicInfo.accNo;
			document.getElementById("orgId_div").value=basicInfo.idBank;
			document.getElementById("accName_div").value=basicInfo.accName;
			document.getElementById("adress_div").value=basicInfo.address;
			document.getElementById("zip_div").value=basicInfo.zip;
			document.getElementById("linkMan_div").value=basicInfo.linkMan;
			document.getElementById("phone_div").value=basicInfo.phone;
			document.getElementById("sealNo_div").value=basicInfo.sealAccNo;
			document.getElementById("contract_div").value=basicInfo.signContractNo;
			 var sealModes=document.getElementById("sealMode_div");//屏蔽掉验印模式
			 sealModes.options[0].selected=true;
			 for (var i = 0; i < sealModes.length; i++) {  
				
		         if (sealModes.options[i].value == basicInfo.sealMode){
		        	 sealModes.options[i].selected=true;
		        	 break;
		         }
		     }
			 var accCycles=document.getElementById("accCycle_div");
			 accCycles.options[0].selected=true;
			 for (var i = 0; i < accCycles.length; i++) {  
				
		         if (accCycles.options[i].value == basicInfo.accCycle){
		        	 accCycles.options[i].selected=true;
		        	 break;
		         }
		     }
			 var sendTypes=document.getElementById("sendType_div");
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
	//var index=document.getElementById("dataIndex").value;
	var param="index="+tableIndex;
	param=param+"&sendAddress="+document.getElementById("adress_div").value;
	param=param+"&zip="+document.getElementById("zip_div").value;
	param=param+"&linkMan="+document.getElementById("linkMan_div").value;
	param=param+"&phone="+document.getElementById("phone_div").value;
	param=param+"&sendType="+document.getElementById("sendType_div").value;
	var sendType = document.getElementById("sendType_div").value;
	var accNo=document.getElementById("accNo_div").value;
	if(param.indexOf("=&", 0)>-1 || sendType==""){
		alert("信息输入不完整！");
	}else{
		param=param+"&accNo="+document.getElementById("accNo_div").value;
		param=param+"&accName="+document.getElementById("accName_div").value;
		param=param+"&orgId="+document.getElementById("orgId_div").value;
		param=param+"&sealNo="+document.getElementById("sealNo_div").value;
		param=param+"&sealMode="+document.getElementById("sealMode_div").value;
		param=param+"&accnoFlag="+document.getElementById("accnoFlag_div").value;
		param=param+"&isCheck="+document.getElementById("isCheck_div").value;
		var confirmMsg="";
		var sendmode;
		$.getJSON("accModifyAction_getBasicInfoByIndex.action", 
				"index="+tableIndex+"&accNo="+accNo+"&temp="+new Date(), 
				function(basicInfo) {
					if(basicInfo==null){
						alert("未从后台缓存中获取到对应的账号信息,请尝试刷新界面");
						stopProcess();
					}else{
						sendmode=basicInfo.sendMode;
						if('3'==sendmode){
							confirmMsg="发送方式由网银修改为";
							var flag = document.getElementById("sendType_div").value;
							if('3'==flag){
								confirmMsg="";
							}else{
								if('1'==flag){
									confirmMsg = confirmMsg+"柜台,";
								}
								if('2'==flag){
									confirmMsg = confirmMsg+"邮寄,";
								}
							}
						}
						if(confirm(confirmMsg+"确认提交修改吗?")){
							startProcess("正在修改...");
							$.post("accModifyAction_modify.action", 
									param,
									function(result){
										dealResult_modify(result,tableIndex);
									});	
						}
					}
				});
	}
}


/**
 * 提交签约信息
 */
function submitSign(){
	var tableIndex=document.getElementById("tableIndex").value;
	//var index=document.getElementById("dataIndex").value;
	if(document.getElementById("accNo_div").readOnly==false){
		tableIndex="1";//新增签约时其实这个字段是没有用的，但是没值的话后续逻辑会认为是输入不完全，所以设置为1
	}
	var param="index="+tableIndex;
	param=param+"&accNo="+document.getElementById("accNo_div").value;
	param=param+"&accName="+document.getElementById("accName_div").value;
	param=param+"&orgId="+document.getElementById("orgId_div").value;
	param=param+"&address="+document.getElementById("adress_div").value;
	param=param+"&zip="+document.getElementById("zip_div").value;
	param=param+"&linkMan="+document.getElementById("linkMan_div").value;
	param=param+"&phone="+document.getElementById("phone_div").value;
	param=param+"&sealNo="+document.getElementById("sealNo_div").value;
	param=param+"&sendType="+document.getElementById("sendType_div").value;
	param=param+"&sealMode="+document.getElementById("sealMode_div").value;
	param=param+"&accCycle="+document.getElementById("accCycle_div").value;
	param=param+"&contractNo="+document.getElementById("contract_div").value;
	param=param+"&modifyType="+document.getElementById("modifyType_div").value;

	
	if(param.indexOf("=&", 0)>-1){
		alert("信息输入不完整");
		return;
	}
	if(document.getElementById("sendType_div").value == "0" )
		{
		alert("发送方式必须指定！");
	    return;
		}
	if(document.getElementById("accNo_div").readOnly==true){
	if(confirm("确认提交修改吗?")){
		startProcess("正在修改...");
		$.post("accSignAction_modify.action", param, function(result){
			dealResult_sign(result,tableIndex);
	});	
	}
	}else{
		if(confirm("确认新增吗?")){
			startProcess("正在新增...");
			$.post("accSignAction_add.action", param, function(result){
				dealResult_add(result);
		});	
	}
}
}


function dealResult_delete(result,row){
 if(result!=null&&result.length>0){
	 stopProcess();
	 alert(result);
 }else{	
//  var index=row.rowIndex;
//  row.parentNode.deleteRow(index-1);
//   stopProcess();
	 queryAcountData();
 }
}

function dealResult_modify(result,tableIndex){
	 if(result!=null&&result.length>0){
		 stopProcess();
		 alert(result);
	 }else{	
//		var row =document.getElementById("basicInfoTable").rows[tableIndex];
//		row.cells[4].innerHTML=document.getElementById("adress_div").value;
//		row.cells[5].innerHTML=document.getElementById("zip_div").value;
//		row.cells[6].innerHTML=document.getElementById("linkMan_div").value;
//		row.cells[7].innerHTML=document.getElementById("phone_div").value;
//		row.cells[8].innerHTML=document.getElementById("sealNo_div").value;
//		var sendType=document.getElementById("sendType_div");
//		var faceType=document.getElementById("faceType_div");
//		row.cells[11].innerHTML=sendType.options[sendType.selectedIndex].text;
//		row.cells[12].innerHTML=faceType.options[faceType.selectedIndex].text;
		 queryAcountData();
//	    stopProcess();
//	    closeSon();
	 }
	}

function dealResult_sign(result,tableIndex){
	 if(result!=null&&result.length>0){
		 stopProcess();
		 alert(result);
	 }else{	
//		var row =document.getElementById("basicInfoTable").rows[tableIndex];
//		row.cells[4].innerHTML=document.getElementById("adress_div").value;
//		row.cells[5].innerHTML=document.getElementById("zip_div").value;
//		row.cells[6].innerHTML=document.getElementById("linkMan_div").value;
//		row.cells[7].innerHTML=document.getElementById("phone_div").value;
//		row.cells[9].innerHTML=document.getElementById("sealNo_div").value;
//		row.cells[10].innerHTML="已签约";
////		var sendType=document.getElementById("sendType_div");
////		var faceType=document.getElementById("faceType_div");
//		var sealMode=document.getElementById("sealMode_div");
//		var accCycle=document.getElementById("accCycle_div");
//		var contractId=document.getElementById("contractId").value;
//		row.cells[11].innerHTML=sealMode.options[sealMode.selectedIndex].text;
//		row.cells[12].innerHTML=accCycle.options[accCycle.selectedIndex].text;
//		row.cells[13].innerHTML=contractId;
//		row.cells[14].innerHTML="<a onclick='sign(this)' style='color:#FF0000'>修改 </a>";
//	    stopProcess();
//	    closeSon();
	    queryAcountData();
	 }
	}

function dealResult_add(result){
	 stopProcess();
	 if(result!=null&&result.length>0){
		 alert(result);
		 return;
	 }else{
		 closeSon();
	 }
	 queryAcountData();
	}