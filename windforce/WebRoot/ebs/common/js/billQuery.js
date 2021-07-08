
/** 返回上级列表 */
function backQueryBillList() {  
	window.location.href='billQueryAction_queryBillList.action';
}

/** 查询日志 */
function queryLog(docId) {
    top.showPage("queryLog.action?docId=" + docId,"","查询日志",800,500,"");
}

/** 发起删除 */
function deleteBill(docId) {
	var docsetflag = $("#docsetflag").val();
	if(docsetflag == "2") {
		var url = "deleteBill.action?docId=" + docId;
		$("#deleteBillBtn").attr("disabled", true);
		$.post(url, null, 
			function (data, textStatus){
				alert(data.resultMsg); 
				backQueryBillList();
			}, 
			"json"
		); 
	}
	else if(docsetflag != "2"&&docsetflag != "-1") {
		alert("对账未完成，不能删除");
	}
}

/** 关闭弹出窗口 */
function closeTopWindow() {
	$("#topwindow").hide();
}