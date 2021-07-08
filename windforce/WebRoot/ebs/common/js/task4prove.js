

/**
 * 陈林江 2012-12-19
 * 验印模块任务专用工具类(获取新任务时不刷新界面)
 * 解决了验印控件无法被div覆盖的问题
 * @author 陈林江
 */
var ManuSeal;
var taskActionPath;
var taskJson;
/**
 * 正在初始化界面
 */
function preparePage(path){
//	initUi(path);   //初始化界面div层
	startProcess("正在初始化界面。。。");	
	top.autoSearch("navigationAutoSearchAction.action");   // 刷新左侧列表	
}

/**
 * 初始化界面完毕
 */
function prepareEnd(actionPath){
	taskActionPath=actionPath;
	ManuSeal=document.getElementById("ManuSeal");
	stopProcess();	
}


/**
 * 提交任务
 * 
 * @returns
 */
function submitTask(actionPath, param, nextTaskPath) {
	lockButton();
	minSeal();
	startProcess("正在提交任务。。。");
	$.post(actionPath, param, function(result) {
		getNextTask(result, nextTaskPath);
	});
};


/**
 * 放弃任务
 * 
 * @returns
 */
function giveUpTask(actionPath, param,nextTaskPath) {	
	lockButton();
	startProcess("正在放弃任务。。。");
	$.post(actionPath, param, function(result) {
		getNextTask(result, nextTaskPath);
	});
};

/**
 * 发起删除
 * 
 * @returns
 */
	lockButton();
	function toDelete(actionPath,param,nextTaskPath) {
	getDeleteReason(actionPath,param,nextTaskPath);  // 获取删除理由列表
};




/**
 * 获取下一笔任务
 * @param result
 * @param nextTaskPath
 */
function getNextTask(result, nextTaskPath) {
	if (result == null || result.length == 0) {
		changeProcessTitle("正在获取下一笔任务。。。");
		var timestamp = new Date().toLocaleString();
		$.getJSON(nextTaskPath, timestamp, function(data){
			showNextTask(data);
		});
		return;
	}
	if (result.substring(0, 3) == "err") {
		maxSeal();
		freeButton();
		stopProcess();
		alert(result.substring(3, result.length));
	}else{
		maxSeal();
		freeButton();
		stopProcess();
		alert(result);
	}
	freeButton();
};

/**
 * 显示下一笔任务
 * @param data
 */
function showNextTask(data){
	if(data==null||data.message==null||data.message==""){
		alert("获取下一笔任务出现错误,请尝试重新进入验印界面");
		top.refresh();
	}else if(data.message=="success"){
		taskJson=data;
		maxSeal();
		ManuProve(data);
		stopProcess();
		freeButton();
	}else if(data.message=="noTask"){
		location.href = ("taskFinish.jsp");
	}
}

/**
 * 初始化验印控件并进行验印
 * @param json
 */
function BeginManualSeal(json) {
	taskJson=json;
	maxSeal();
	initSealForBs(json);
	ManuProve(json);
}

/**
 *开始人工验印
 */
function ManuProve(manuproveJson) {
	ManuSeal.AccList = manuproveJson.accNoList;
	ManuSeal.JudgeImage(manuproveJson.accNo, manuproveJson.voucherNo,
			manuproveJson.operCode, manuproveJson.credit, manuproveJson.docDate,
			manuproveJson.imageFile);
}

/**
 *人工验印通过
 */
function Pass() {
	var strSealLog;
	var strCurSealAccno;
	if (confirm("确认验印通过?")) {
		ManuSeal.PassSeal();
		strSealLog= ManuSeal.GetRecLogStr();
		strCurSealAccno=ManuSeal.AccNo;
		if (ManuSeal.m_JudgeFinish) {
			submitTask(taskActionPath+"_submitTask.action",
					"checkTaskId="+taskJson.taskId+"&operType=pass&sealLogStr="+strSealLog +"&curSealAcc=" +strCurSealAccno, taskActionPath+"_getTaskByAjax.action");

		} else {
			alert("票据验印未完成！");
		}
	}
}

/**
 *人工验印不通过
 */
function NoPass() {
	var strSealLog;
	var strCurSealAcc;
	var strCurUReasonCode;
	var strCurUReason;
	if (confirm("确认验印无效么？")) {
		ManuSeal.InvalidSeal();
		if (ManuSeal.m_JudgeFinish) {
			strSealLog=ManuSeal.GetRecLogStr();
			strCurSealAcc= ManuSeal.AccNo;
			strCurUReasonCode = ManuSeal.FailSelectCode;
			strCurUReason = ManuSeal.FailSelectReason;
			submitTask(taskActionPath+"_submitTask.action","checkTaskId="+taskJson.taskId+"&operType=nopass&sealLogStr=" + strSealLog + "&curSealAcc=" + strCurSealAcc + "&curUReasonCode=" + strCurUReasonCode+  "&curUReason=" + strCurUReason, taskActionPath+"_getTaskByAjax.action");
		} else {
			alert("票据验印未完成！");
		}
	}
}

/**
 *删除业务
 */
function myToDelete() {
		minSeal();
		toDelete(taskActionPath+"_toDelete.action","checkTaskId="+taskJson.taskId,taskActionPath+"_getTaskByAjax.action");
}
function myGiveUpTask(){
		minSeal();
   		giveUpTask(taskActionPath+"_giveUpTask.action","checkTaskId="+taskJson.taskId,taskActionPath+"_getTaskByAjax.action");
}
/**
 *切换账号
 */
function ChangeAcc() {
	if (confirm("确认切换账号么？")) {
		ManuSeal.ChangeAccNo();
	}
}

/**
 *切换印鉴
 */
function ChangeYJ() {
	if (confirm("确认切换印鉴么？")) {
		ManuSeal.ChangeYJ();
	}
}

/**
 *获取日志
 */
function getResult() {
	alert(ManuSeal.GetRecLogStr());
}

/**
 * 验印初始化
 */
function initSealForBs(manuproveJson) {
	//document.getElementById('ManuSeal').focus;
	ManuSeal.SealDbIp = manuproveJson.sealDBIp;
	ManuSeal.SealDbPort =  manuproveJson.sealDBPort;
	ManuSeal.Hydm = manuproveJson.operCode;
	ManuSeal.Channel = "对账";
	ManuSeal.NeedReason = 1;
	/*理由*/
	ManuSeal.FailReason = manuproveJson.uReason;
	ManuSeal.BackReason = "";
	ManuSeal.ChequeUse = 1;
	ManuSeal.CurrencyId = 0;
	ManuSeal.SealMode = 0;
	ManuSeal.ifHaveFailMemo = 0;
	//ManuSeal.SetFocus;
	ManuSeal.DirectSetDevSc(0, 0.8151);
	ManuSeal.DirectSetDevSc(1, 1.13753);
	ManuSeal.Init();
	//alert("初始化Init完成...");

}

/**
 * 页面卸载
 */
function EndManualSeal() {
	ManuSeal.ControlTerminate();
}


/**
 * 锁定按钮
 */
function lockButton() {
	var element = document.getElementById("giveUpButton");
	var element1 = document.getElementById("toDeleteButton");
	var element2 = document.getElementById("submitButton");
	var element3 = document.getElementById("reInputButton");
	var element4 = document.getElementById("deleteUnPassButton");
	var element5 = document.getElementById("BtnNotPass");
	if (element != null) {
		element.disabled = true;
	}
	if (element1 != null) {
		element1.disabled = true;
	}
	if (element2 != null) {
		element2.disabled = true;
	}
	if (element3 != null) {
		element3.disabled = true;
	}
	if (element4 != null) {
		element4.disabled = true;
	}
	if (element5 != null) {
		element5.disabled = true;
	}
};

/**
 * 释放按钮
 */
function freeButton() {
	var element = document.getElementById("giveUpButton");
	var element1 = document.getElementById("toDeleteButton");
	var element2 = document.getElementById("submitButton");
	var element3 = document.getElementById("reInputButton");
	var element4 = document.getElementById("deleteUnPassButton");
	var element5 = document.getElementById("BtnNotPass");
	
	if (element != null) {
		element.disabled = false;
	}
	if (element1 != null) {
		element1.disabled = false;
	}
	if (element2 != null) {
		element2.disabled = false;
	}
	if (element3 != null) {
		element3.disabled = false;
	}
	if (element4 != null) {
		element4.disabled = false;
	}
	if (element5 != null) {
		element5.disabled = false;
	}
};

function minSeal(){
//	alert(ManuSeal.visible)
	ManuSeal.style.visibility="hidden";
//	ManuSeal.width=1;
//	ManuSeal.height=1;
}
function maxSeal(){
	ManuSeal.style.visibility="visible";
//	ManuSeal.width=780;
//	ManuSeal.height=650;
}
/**
 * 获取删除理由
 */
function getDeleteReason(actionPath,param,nextTaskPath){
	$.getJSON("untreadAction_getDeleteReasons.action", param, function(data) {
		openUntreadReason(data,actionPath,nextTaskPath,param);
	});
};


/**
 * 提交删除请求到后台
 */
function submitDelete() {
	var values=document.getElementsByName("reason");
	var untreadReasonInput=document.getElementById("untreadReasonInput");
	var flag=false;
	var taskParam=document.getElementById("params").value;
	var params=taskParam;
	for(var i=0;i<values.length;i++){
		if(values[i].checked){
			params=params+"&reasons="+values[i].value;
			flag=true;
		}
	}
	if(flag==false&&(untreadReasonInput.value==null||untreadReasonInput.value.length==0||untreadReasonInput.value=="请选择或输入理由信息")){   //复选框未勾选且无自输理由
			alert("请选择理由信息或手动输入");
			untreadReasonInput.focus();
			return;
		}
    if(untreadReasonInput.value!=null&&untreadReasonInput.value.length!=0&&untreadReasonInput.value!="请选择或输入理由信息"){
    	if(/['"#$%&\^*]/.test(untreadReasonInput.value)){
    		alert("不允许输入特殊字符");
    		return;
    	}
    	params+="&reasons="+untreadReasonInput.value;
    }
		var nextTaskPath=document.getElementById("nextTaskPath").value;
		var actionPath=document.getElementById("actionPath").value;
		 closeUntreadReason();  //关闭理由选择框
		 startProcess("正在提交请求。。。"); 
		 $.post(actionPath, params, function(result) {
			 getNextTask(result, nextTaskPath); });
		};
/**
 * 判断选择情况，改变自输入理由框的显示情况
 */
function showData(){
	var values=document.getElementsByName("reason");
	var flag=false;
	for(var i=0;i<values.length;i++){
		if(values[i].checked){
			flag=true;
		}
	}
	var untreadReasonInput=document.getElementById("untreadReasonInput");
	if(flag&&untreadReasonInput.value=="请选择或输入理由信息"){
		untreadReasonInput.value="";
	}else if(!flag){
		if(untreadReasonInput.value==""){
		untreadReasonInput.value ="请选择或输入理由信息";
		}
	}	
}	

function cancel(){
	closeUntreadReason();
	maxSeal();
	freeButton();
}

function changeSJ(){
	var str = prompt("请输入对账日期，比如【20130131】","请输入");
	if(str){
		var reg =/\d{8}/; 
		if(reg.test(str)){
			taskJson.docDate=str;
			maxSeal();
			ManuProve(taskJson);
		}else{
			alert("输入对账时间有误!");
			return;
		}
	}
}