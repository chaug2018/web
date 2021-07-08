
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
function batchPrintBillData(){
	if(fnCheck()){
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
		$.post("DataPrintAction_printData.action", param, function(result){
		});
	}else{
		alert("请选择至少一条要维护的记录");
	}
}
