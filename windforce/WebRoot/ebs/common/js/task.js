
/**
 * 任务界面工具,使用以下方法进行通讯时，若后台出现异常，应返回err+异常信息
 * 
 * @author 陈林江
 */

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
function prepareEnd(){
	stopProcess();	
}

/**
 * 根据账单编号获取账号信息
 */
function getBillInfo(actionPath, param) {
	startProcess("正在获取账号信息。。。");
	$.getJSON(actionPath, param, function(data) {
		var options = {
				bindJson : data
			};
			onbindData(options);
		if(data.errMsg!=null&&data.errMsg.length!=0){
			stopProcess();
			alert(data.errMsg);
			return;
		}	
		if(data.accNo1==null||data.accNo1.length==0){
			alert("未获取到该账单编号对应的信息");
		}
		stopProcess();
	});
};

/**
 * 提交任务
 * 
 * @returns
 */
function submitTask(actionPath, param, nextTaskPath) {
	lockButton();
	startProcess("正在提交任务。。。");
	$.post(actionPath, param, function(result) {
		getNextTask(result, nextTaskPath);
	});
};

/**
 * 重新录入
 * 
 * @returns
 */
function reInput(actionPath, param, nextTaskPath) {
	lockButton();
	getReInputReason(actionPath,param,nextTaskPath);  // 获取删除理由列表
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
function toDelete(actionPath,param,nextTaskPath) {
	lockButton();
	getDeleteReason(actionPath,param,nextTaskPath);  // 获取删除理由列表
};



/**
 * 删除审核不通过
 * 
 * @returns
 */
function deleteUnPass(actionPath, param,nextTaskPath) {
	lockButton();
	startProcess("正在提交任务至发起删除的节点 。。。");
	$.post(actionPath, param, function(result) {
		getNextTask(result, nextTaskPath);
	});
};

function getNextTask(result, nextTaskPath) {
	if (result == null || result.length == 0) {
		changeProcessTitle("正在获取下一笔任务。。。");
		location.href = (nextTaskPath+"?test="+Math.random());  //如果每次跳转的地址相同，浏览器有时会直接从缓存里面拿界面，导致请求到不了后台
//		top.stopProcess();
		return;
	}
	if (result.substring(0, 3) == "err") {
		stopProcess();
		alert(result.substring(3, result.length));
	}else{
		stopProcess();
		alert(result);
	}
	freeButton();
};

/**
 * 锁定按钮
 */
function lockButton() {
	var element = document.getElementById("giveUpButton");
	var element1 = document.getElementById("toDeleteButton");
	var element2 = document.getElementById("submitButton");
	var element3 = document.getElementById("reInputButton");
	var element4 = document.getElementById("deleteUnPassButton");
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
};
/**
 * 获取删除理由
 */
function getDeleteReason(actionPath,param,nextTaskPath){
	$.getJSON("untreadAction_getDeleteReasons.action", param, function(data) {
		openUntreadReason(data,actionPath,nextTaskPath,param);
	});
};

/**
 * 获取重录理由
 */
function getReInputReason(actionPath,param,nextTaskPath){
	$.getJSON("untreadAction_getReInputReasons.action", param, function(data) {
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
	freeButton();
	closeUntreadReason();
}