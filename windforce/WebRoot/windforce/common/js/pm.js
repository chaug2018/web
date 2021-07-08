 $(document).ready(function(){	 
	displayFirstProcess();
	
	$("#allselect").click(function(){		
		var allSelectValue = $("#allselect").val();		
		if('all' == allSelectValue) {//全选
			$("#allselect").val("none");
			$("[name = taskInsIds]:checkbox").attr("checked", true); 
			
		} else {
			$("#allselect").val("all");
			$("[name = taskInsIds]:checkbox").attr("checked", false);
		}
    });
	
	$("[name = taskInsIds]:checkbox").click(function(){
		var isSelect = $(this).attr("checked"); 
		if(isSelect != 'checked') {
			$("#allselect").val("all");
			$("#allselect").attr("checked", false);
		}
	});

	/** 任务锁定 */
	$("#lockTasks").click(function(){
		var taskInsIds = "";
		var ck_val =[];    
	      $('input[name="taskInsIds"]:checked').each(function(){
	    	  ck_val.push($(this).val());
	    	  taskInsIds += "taskInsIds=" + $(this).val() + "&";    
	    });  
	    taskInsIds = taskInsIds.substring(0,taskInsIds.length-1);
		if(ck_val.length > 0) {
			var url = "lockTaskInstance.action?" + taskInsIds;//请求的 url参数
			var processDefinitionKey = $("#processDefinitionKey").val();//流程定义key
			var taskName = $("#taskName").val();//任务环节名称
			var curPage = $("#curPage").val();//当前页
			$.post(url, null, 
			    function (data, textStatus){
					top.wfAlert(data.resultMsg); 
					top.closeShowPage();
					viewUnlockedTaskNodeInsList(taskName, processDefinitionKey, curPage);//刷新空闲任务列表
				}, 
				"json"
			); 
		} else {
			top.wfAlert("请选中需要锁定的任务！");
		}
	});
	
	/** 任务解除锁定 */
	$("#unLockTasks").click(function(){
		var taskInsIds = "";
		var ck_val =[];    
		var currentUserId = $("#currentUserId").val();
		var validFlag = "Y";
		var msg = "";
	      $('input[name="taskInsIds"]:checked').each(function(){
	    	  var taskInsId = $(this).val();
	    	  var taskInsIdUserId = $("#td_peopleCode_" + taskInsId).text();
	    	  if(currentUserId != taskInsIdUserId) {
	    		  validFlag = "N";
	    		  msg += taskInsIdUserId + ",";
	    	  }
	    	  ck_val.push(taskInsId);
	    	  taskInsIds += "taskInsIds=" + $(this).val() + "&";    
	    });  
	    if(validFlag == 'N') {
	    	msg = msg.substring(0, msg.length - 1);
	    	top.wfAlert("对不起，您不能解锁 " + msg + " 的任务！");
	    	return false;
	    }
	    taskInsIds = taskInsIds.substring(0,taskInsIds.length-1);
		if(ck_val.length > 0) {
			var url = "unLockTaskInstance.action?" + taskInsIds;//请求的 url参数
			var processDefinitionKey = $("#processDefinitionKey").val();//流程定义key
			var taskName = $("#taskName").val();//任务环节名称
			var curPage = $("#curPage").val();//当前页
			
			$.post(url, null, 
			    function (data, textStatus){ 
					top.wfAlert(data.resultMsg); 
					 top.closeShowPage();
					viewLockedTaskNodeInsList(taskName, processDefinitionKey, curPage);//刷新列表页面
				}, 
				"json"
			); 
		} else {
			top.wfAlert("请选中需要锁定的任务！");
		}
	});
 });
	
 /** 流程tab页点击事件 */
 function changeProcessTab(processPrimaryKey, obj) {
	$(obj).addClass("current").siblings().removeClass("current");
	diplayProcessCountInfo(processPrimaryKey);	
 }
 
 /** 获取流程定义的相关信息并组织成表格形式 */
 function diplayProcessCountInfo(processPrimaryKey) {
	 var action = "queryCountInfoByProDefKey.action";
	 $("#processDefinitionKey").val(processPrimaryKey);
	 top.startProcess("正在刷新流程数据...");
	 $.post(
		    	action,
		    	{"processDefinitionKey":processPrimaryKey},
		    	function(data){
		    		var table = $("#procCountInfo");
		    		table.children("tr").remove();
		    		var taskNodeListArray = data.bpmTaskNodeList;
		    		for(var i = 0; i < taskNodeListArray.length; i++) {
		    			var tr=$("<tr></tr>");
		    	        tr.appendTo(table);
		    	        
		    			var taskName = taskNodeListArray[i].taskName;		    			
		    			var taskDisplayName = taskNodeListArray[i].taskDisplayName;
		    			var td = $("<td align='center'>" + taskDisplayName + "</td>");
		    	        td.appendTo(tr);
		    			
		    			var isLockedCount = taskNodeListArray[i].isLockedCount;
		    			var td = $("<td align='center'></td>");
		    			var astr = '<a href="#" onclick="' + "viewLockedTaskNodeInsList('" + taskName + "','" + processPrimaryKey + "','1');return false;" + '"' + ">" + isLockedCount + "</a>";
		    			var a = $(astr);
		    			a.appendTo(td);
		    			td.appendTo(tr);
		    			
		    			var unAssignedTasks = taskNodeListArray[i].unAssignedTasks;
		    			var td=$("<td align='center'></td>");
		    			var astr = '<a href="#" onclick="' + "viewUnlockedTaskNodeInsList('" + taskName + "','" + processPrimaryKey + "','1');return false;" + '"' + ">" + unAssignedTasks + "</a>";
		    			var a = $(astr);
		    			a.appendTo(td);
		    	        td.appendTo(tr);
		    		}
		    		$('#processInfoTable tr:even').filter(':eq(0)').addClass('even');
		    		$('#processInfoTable tr:even').filter(':gt(0)').addClass('odd');
		    		top.stopProcess();
		    	}
		    );
	 displayProcessImage(processPrimaryKey);
 }
 

 /** 显示流程示意图 */
function displayProcessImage(processPrimaryKey) {
	var rootUrl = $("#rootUrl").val();
	var imgUrl = rootUrl + "/common/images/pm_" + processPrimaryKey + '.png';
	$("#processImage").attr("src", imgUrl);
	$("#imageDisplayArea").show();
	$("#processImage").error(
		function() {
			var nopicUrl = rootUrl + "/windforce/common/images/pm_nopic.png"; 
			$("#processImage").attr("src", nopicUrl).attr("alt", "该流程暂无流程示意图");
		});
}

 /** 默认显示第一个流程的相关内容*/
 function displayFirstProcess(){
	 $("ul.menu li:first-child").addClass("current");
	 $("ul.menu li:first-child").trigger("onclick");
	 
 }
 
 function viewTaskNodeInsList(taskName, proDefKey, curPage,pageFlag) {
	if(pageFlag=="L"){
		top.closeShowPage();
		viewLockedTaskNodeInsList(taskName, proDefKey, curPage);
	}else{
		top.closeShowPage();
		viewUnlockedTaskNodeInsList(taskName, proDefKey, curPage);
	}
 }
 
 /** 点击任务节点名称连接，显示该节点下的所有任务实例明细列表 */
 function viewLockedTaskNodeInsList(taskName, proDefKey, curPage) {
	 top.startProcess("正在获取数据...");
	 var action = "lockedTtaskNodeInsList.action";	
	// top.showWindowForUrl(action,taskName, proDefKey, curPage,660,500);
	 top.showPage(action, "taskName="+taskName+"&curPage="+curPage, "锁定任务列表", 660, 500, null);
 }
 
 /** 点击任务节点名称连接，显示该节点下的所有任务实例明细列表 */
 function viewUnlockedTaskNodeInsList(taskName, proDefKey, curPage) {
	 top.startProcess("正在获取数据...");
	 var action = "unlockedTtaskNodeInsList.action";
	 //var params = "{'processDefinitionKey':" + proDefKey + ",'taskName':" + taskName + ",'curPage':" + curPage + "}";
//     top.showWindowForUrl(action,taskName, proDefKey, curPage,660,500);
     top.showPage(action, "taskName="+taskName+"&curPage="+curPage, "空闲任务列表", 660, 500, null);
 }
 
 /** 刷新按钮相关事件*/
 function refreshProcessMonitorData() {
	 var button=document.getElementById("refreshButton");
	 if(button.disabled){
		 return;
	 }
	 button.disabled=true;
	 number=10;
	 changeText();  //最多10秒刷新一次
	 var processPrimaryKey = $("#processDefinitionKey").val();
	 diplayProcessCountInfo(processPrimaryKey);
 }
 
 var number=0;
 function changeText(){
	 if(number==0){
		 document.getElementById("refreshButton").disabled=false;
		 document.getElementById("refreshButton").value="刷新";
		 return;
	 }
	 document.getElementById("refreshButton").value=number;
	 number=number-1;
	 setTimeout(changeText, 1000);
 }