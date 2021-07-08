function modifyNotmatchItem(data){
	//未达录入后   列表详情修改
	var cell=data.parentNode;
	var row=cell.parentNode;
	document.getElementById("tableIndex").value=row.rowIndex;  //当前行下标
	document.getElementById("dataIndex").value=row.cells[0].innerHTML;
	document.getElementById("accNo_div").value=row.cells[3].innerHTML;
	document.getElementById("traceDate_div").value=row.cells[4].innerHTML;
	document.getElementById("traceCredit_div").value=row.cells[5].innerHTML;
	document.getElementById("traceNo_div").value=row.cells[6].innerHTML;
	document.getElementById("inputDesc_div").value=row.cells[7].innerHTML;
	var direction=document.getElementById("direction_div");
	 for (var i = 0; i < direction.length; i++) {  
		if(row.cells[8].innerHTML==""){
			direction.options[0].selected=true;
			break;
		}
        if (direction.options[i].text == row.cells[8].innerHTML||direction.options[i].text+" " == row.cells[8].innerHTML){
        	direction.options[i].selected=true;
       	 break;
        }
    }
	var result=document.getElementById("result_div");
		 for (var i = 0; i < result.length; i++) {  
			if(row.cells[9].innerHTML==""){
				result.options[0].selected=true;
				break;
			}
	        if (result.options[i].text == row.cells[9].innerHTML||result.options[i].text+" " == row.cells[9].innerHTML){
	        	result.options[i].selected=true;
	       	 break;
	        }
	    }
	showSon();
}

function dealResult_modify(){
	var accno = document.getElementById("accNo_div").value;
	var trandate = document.getElementById("traceDate_div").value;
	var tranno = document.getElementById("traceNo_div").value;
	var amttran = document.getElementById("traceCredit_div").value;
	var direction_index = document.getElementById("direction_div");
	var direction = direction_index.options[direction_index.selectedIndex].text;
	var result_index = document.getElementById("result_div");
	var result = result_index.options[result_index.selectedIndex].text;
	//判断是否为空
	if (accno == "") {
		alert("请输入账号！");
		document.getElementById("accNo_div").focus();
		return;
	} else if (!CheckDate(trandate)) {
		document.getElementById("traceDate_div").focus();
		return;
	} else if (tranno == "") {// modify 20160114 加上必输项的控制
		alert("请输入凭证号！");
		document.getElementById("traceNo_div").focus();
		return;
	} else if (amttran == "") {
		alert("请输入发生金额！");
		document.getElementById("traceCredit_div").focus();
		return;
	} else if (direction == "" || direction == "--请选择--") {
		alert("请输入未达方向！");
		document.getElementById("direction_div").focus();
		return;
	} else if (result == "" || result == "--请选择--") {
		alert("请输入核对结果！");
		document.getElementById("result_div").focus();
		return;
	}
	$.post("notMatchInputAction_confirmTask.action", "accNo ="+accno , function(data) {
		var isConfirm = false;
		if("1" == data)
		{
//			if(confirm("该账号在账单中不存在,确定要继续吗?"))
//				{
//				isConfirm = true;
//				}
			isConfirm = true;
	    }
		if(data == "2" || isConfirm)
			{
		var tableIndex = document.getElementById("tableIndex").value;
		var row =document.getElementById("myTable").rows[tableIndex];
		var accNo = document.getElementById("accNo_div");
		row.cells[3].innerHTML=accNo.value;
		var traceDate = document.getElementById("traceDate_div");
		row.cells[4].innerHTML=traceDate.value;
		var traceCredit = document.getElementById("traceCredit_div");
		row.cells[5].innerHTML=traceCredit.value;
		var traceNo = document.getElementById("traceNo_div");
		row.cells[6].innerHTML=traceNo.value;
		var inputDesc = document.getElementById("inputDesc_div");
		row.cells[7].innerHTML=inputDesc.value;
		var direction=document.getElementById("direction_div");
		row.cells[8].innerHTML=direction.options[direction.selectedIndex].text;
		var result=document.getElementById("result_div");
		row.cells[9].innerHTML=result.options[result.selectedIndex].text;
	    stopProcess();
	    closeSon();
			}
	});
}
