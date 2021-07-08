//查询条件字符串前缀
var prefix = "<<[[";
//查询条件字符串分隔符
var separator = "<=>";
//查询条件字符串后缀
var suffix = "]]>>";
//查询条件集合字符串
var requirements="";
//分页：当前页数
var currentPage=1;
//查询结果集
var resultList=null;
//额外一行的查询结果
var extraResult=[];
//内容表格单元格内文字显示的最大长度
var maxCellTextlength = 20;

$(document).ready(function(){
	window.onscroll = function(){ 
	    $(".operateDiv").css("top", (document.body.scrollTop || document.documentElement.scrollTop)+150);
	};
	
	//获取内容表格单元格内文字显示的最大长度
	maxCellTextlength = $("#con_tab").attr("maxCellTextlength");
	if(isNaN(maxCellTextlength)){
		maxCellTextlength = 20;
	}
	
	//对输入要素进行初始化
	$.each($(".requirement input[format],.requirement select[format]"),function(i,reuqireObj){
		window.pageFormat.format(reuqireObj);
	});
	
	//初始化select
	$.each($(".requirement select[dataType!=init]"),function(i,selObj){
		initSelectOptions($(selObj));
	});
	
	//为查询条件添加回车跳转功能
	addFocusByEnterOrTab($("#condition_requirement .requirement input,#condition_requirement .requirement select"),$("#searchButton"));
	
	bindPager();

	//判断是否自动查询数据
	if($("#con_tab").attr("autoquery") == "true"){
		queryData();
	}
	
	//表头绑定排序功能
	if($("#con_tab").attr("sortable") == "click"){
		$("#con_tab .tr_top td").click(function(){
			changeSort(this);
		});
	}else if($("#con_tab").attr("sortable") != "false"){
		$("#con_tab .tr_top td").dblclick(function(){
			changeSort(this);
		});
	}
	
	//点击查询按钮
	$("#searchButton").click(function(){
		currentPage = 1;
		if(generateRequirements()){
			queryData();
		}
	});
	//点击重置按钮
	$("#resetBtn").click(function(){
		$("#condition_requirement .requirement input").val("");
		$("#condition_requirement .requirement select option:first-child").attr("selected","selected");
	});
	
	// 第一页按钮click事件
	$("#first").click(function() {
		if (currentPage < 2){
			return;
		}else{
			currentPage = 1;
		}
		queryData();
	});
	// 上一页按钮click事件
	$("#previous").click(function() {
		if (currentPage < 2){
			return;
		}else{
			currentPage--;
		}
		queryData();
	});
	// 下一页按钮click事件
	$("#next").click(function() {
		var totalPage = parseInt($("#totalPage").text());
		if (currentPage < 1 || currentPage >= totalPage){
			return;
		}else{
			currentPage++;
		}
		queryData();
	});
	// 最后一页按钮click事件
	$("#last").click(function() {
		var totalPage = parseInt($("#totalPage").text());
		if (currentPage < 1 || currentPage >= totalPage){
			return;
		}else{
			currentPage = totalPage;
		}
		queryData();
	});
	// 跳转按钮click事件
	$("#jumpPage").click(function() {
		var totalPage = parseInt($("#totalPage").text());
		var jumpPage = parseInt($("#currentPage").val());
		if (isNaN(jumpPage) || jumpPage < 1 || jumpPage > totalPage){
			$("#currentPage").val(currentPage);
			return;
		}
		currentPage = jumpPage;
		queryData();
	});
	// 导出按钮click事件
	$("#exportBtn").click(function() {
		//生成表头对应属性字符串
		var tableHeadText = "";
		$.each($("#con_tab .tr_top td"), function(i,topCell){
			if($(topCell).attr("ref")){
				tableHeadText += prefix + $(topCell).attr("showname") + separator + $(topCell).attr("ref") + suffix;
			}
		});
		//生成排序字符串
		var serachOrderText = "";
		if($("#con_tab").attr("sortfield")){
			serachOrderText = prefix + $("#con_tab").attr("sortfield") + separator + ("desc" == $("#con_tab").attr("sorttype") ? "desc" : "asc") + suffix;
		}
		//导出文件名
		var exportname = $("#con_tab").attr("exportname");
		if(!exportname){
			exportname = $("#content_name").text();
		}
		if(!exportname){
			exportname = "导出文件";
		}
		
		$("#exportDivHidden").html("");
		$("#exportDivHidden").append("<iframe id='exportHiddenIframe' style='display:none' ></iframe>");
		$("#exportDivHidden").append("<form id='exportForm' action='"+$("#con_tab").attr("exportaction")+"' target='exportHiddenIframe' method='post'>" +
				"<input type='hidden' name='requirementsText' value='"+requirements+"' />" +
				"<input type='hidden' name='serachOrderText' value='"+serachOrderText+"' />" +
				"<input type='hidden' name='tableHeadText' value='"+tableHeadText+"' />" +
				"<input type='hidden' name='exportname' value='"+exportname+"' />" +
				"</form>");
		$("#exportForm").submit();
	});
	
});

//增加一行
function addRow(dataObj){
	var table = document.getElementById("con_tab");
	var topRow = table.rows[0];
	var newRow = table.insertRow(table.rows.length);
	var rowAttributes = topRow.attributes;
	for(var i =0; i < rowAttributes.length; i++){
		if($(topRow).attr(rowAttributes[i].name)){ //屏蔽无效属性
			$(newRow).attr(rowAttributes[i].name, rowAttributes[i].value);
		}
	}
	$(newRow).removeClass("tr_top");

	for(var i =0; i < topRow.cells.length; i++){
		var topCell = topRow.cells[i];
		var newCell = newRow.insertCell(newRow.cells.length);
		var cellAttributes =  topCell.attributes;
		for(var j =0; j < cellAttributes.length; j++){
			if($(topCell).attr(cellAttributes[j].name)){ //屏蔽无效属性
				$(newCell).attr(cellAttributes[j].name, cellAttributes[j].value);
			}
		}
		var text = null;
		if($(topCell).attr("ref")){
			var requirement = $(topCell).children("div[name=hideRequirement]").children("input,select");
			text = findShowValue(dataObj, requirement);
			if(requirement.is("select")){
				if(requirement.children("option[value="+text+"]").length != 0){
					text = requirement.children("option[value="+text+"]").text();
				}
			}
			if(text.length > maxCellTextlength){
				newCell.title = text;
				$(newCell).html(String(text).substr(0, maxCellTextlength-2)+"...");
			}else{
				$(newCell).html(text);
			}
		}else{
			$(newCell).html($(topCell).html());
		}
		if($(newCell).attr("jsTitle")){
			try{
				newCell.title = eval($(newCell).attr("jsTitle"));
			}catch(e){
				console.log("error: " + $(newCell).attr("jsTitle"));
				console.log(e);
			}
		}
	}
	$(document).trigger("custompage_addRow", [newRow]);
	return newRow;
}
//修改指定行
function modifyRow(target,obj){
}
//删除指定行
function deleteRow(target){
}
//清除表格内除表头外所有行
function clearTab(){
	var table = document.getElementById("con_tab");
	for ( var i = table.rows.length - 1 ; i > 0; i--) {
		table.deleteRow(i);
	}
}

//增加操作
function addOperate(obj){
	
	var opertype = $(obj).attr("opertype"); //"add"
	generateOperateDiv(opertype,$(obj).attr("action"));
	var dataObj = false;
	bindDivData(opertype+"OperateDiv", dataObj, $(obj).attr("preaction"));
}
//删除操作
function deleteOperate(obj){
	var opertype = $(obj).attr("opertype"); //"delete"
	generateOperateDiv(opertype,$(obj).attr("action"));
	var dataObj = resultList[$(obj).parent().parent().attr("dataindex")];
	bindDivData(opertype+"OperateDiv", dataObj, $(obj).attr("preaction"));
}
//查看操作
function queryOperate(obj){
	var opertype = $(obj).attr("opertype"); //"query"
	generateOperateDiv(opertype,$(obj).attr("action"));
	var dataObj = resultList[$(obj).parent().parent().attr("dataindex")];
	bindDivData(opertype+"OperateDiv", dataObj, $(obj).attr("preaction"));
}
//修改操作
function modifyOperate(obj){
	var opertype = $(obj).attr("opertype"); //"modify"
	generateOperateDiv(opertype,$(obj).attr("action"));
	var dataObj = resultList[$(obj).parent().parent().attr("dataindex")];
	bindDivData(opertype+"OperateDiv", dataObj, $(obj).attr("preaction"));
}
//弹出窗口操作
function openOperate(obj){
	openDiv($(obj).attr("action"));
}
//自定义javascipt函数操作
function customOperate(thisObj){
	var custom = $(thisObj).attr("action");
//	if(custom.indexOf("(") != -1){
//		var customFunc = custom.substring(0,custom.indexOf("("));
//		eval(customFunc+"=$.proxy("+customFunc+", thisObj)");
//	}
	eval(custom);
}
//弹出带iframe的div, 在其中访问url
function openDiv(url){
	var openDiv = document.getElementById("openOperateDiv");
	if(!openDiv){
		openDiv = document.createElement("div");
		var openIframe = document.createElement("iframe");
		$(openIframe).height(document.body.scrollHeight-50);
		$(openIframe).attr("id","openIframe");
		openDiv.appendChild(openIframe);
		
		var buttons = document.createElement("div");
		$(buttons).attr("class", "buttonsDiv");
		buttons.appendChild(document.createElement("hr"));
		var cancel = document.createElement("input");
		$(cancel).attr("type","button");
		$(cancel).attr("value","返回");
		$(cancel).click(function(){
			$(openDiv).hide();
		});
		buttons.appendChild(cancel);
		openDiv.appendChild(buttons);
		$(openDiv).attr("id","openOperateDiv");
		document.body.appendChild(openDiv);
	}
	$("#openOperateDiv #openIframe").attr("src",url);
	$(openDiv).show();
}
//生成"增删查改"的div
function generateOperateDiv(type, action){
	$(document).trigger("custompage_operatediv_open", [type, action]);
//	if("|add|delete|query|modify|".indexOf("|"+type+"|") == -1){
//		return;
//	}
	var divId = type+"OperateDiv";
	if($("#"+divId).length == 0){
		var operateDiv = document.createElement("div");
		$(operateDiv).attr("id", divId);
		$(operateDiv).attr("class", "operateDiv");
		$.each($("#con_tab .tr_top td"), function(i,topCell){
			if($(topCell).attr("ref")){
				var requirement = document.createElement("div");
				$(requirement).attr("class", "requirement");
				$(requirement).html($(topCell).children("div[name=hideRequirement]").html());

				if(type == "query"){
					$(requirement).children("input,select").attr("disabled", true);
				}else if(type == "delete"){
					$(requirement).hide();
				}
				operateDiv.appendChild(requirement);
			}
		});
		if(type == "delete"){
			var prompt = document.createElement("div");
			$(prompt).attr("class", "promptDiv");
			$(prompt).html("请确认是否删除");
			operateDiv.appendChild(prompt);
		}
		var buttons = document.createElement("div");
		$(buttons).attr("class", "buttonsDiv");
		$(buttons).html("<input type='button' value='确定' onclick=\"submitOperateDiv('"+divId+"')\"/><input type='button' value='取消' onclick=\"hideOperateDiv('"+divId+"')\"/>");

		operateDiv.appendChild(buttons);
		document.body.appendChild(operateDiv);
	}
	$("#"+divId).attr("opertype", type);
	$("#"+divId).attr("action", action);
	$("#"+divId).css("top", (document.body.scrollTop || document.documentElement.scrollTop)+150);
	
	//清空div上输入要素的值
	$("#"+divId+" .requirement input").val("");
	$("#"+divId+" .requirement select option:first-child").attr("selected","selected");
	
	//为操作框div输入框 添加回车跳转功能
	addFocusByEnterOrTab($("#"+divId+" .requirement input,#"+divId+" .requirement select"),$("#"+divId+" .buttonsDiv .submit_ok"));
	$("#"+divId+" .requirement:first input,#"+divId+" .requirement:first select").focus();
}
//"增删查改"操作div点击确定时触发的函数
function submitOperateDiv(divId){
	if($("#"+divId).attr("action") && $("#"+divId).attr("opertype") != "query"){
		var properties = "";
		if(checkRequirement($("#"+divId))){
			return false;
		}
		$.each($("#"+divId+" .requirement input,#"+divId+" .requirement select"),function(i, obj){
			if($.trim(obj.value)){
				properties += prefix + $(obj).attr("ref") + separator + obj.value + suffix;
			}
		});
		$.ajax({
			url:$("#"+divId).attr("action"),
			data:{requirementsText: properties},
			success:function(data){
				$(document).trigger("custompage_operate_success", [divId]);
				hideOperateDiv(divId);
				if(data.resultMsg){
					if(data.resultMsgType){
						promptMsg(data.resultMsg, data.resultMsgType);
					}else{
						promptMsg(data.resultMsg,"operate_success");
					}
				}else{
					promptMsg("操作成功","operate_success");
				}
				queryData();
			},
			error:function(XMLHttpRequest, textStatus, errorThrown){
				$(document).trigger("custompage_operate_fail", [divId]);
				hideOperateDiv(divId);
				promptMsg("操作失败","operate_fail");
			},
			type:"post",
			dataType:"json"
		});
	}else{
		hideOperateDiv(divId);
	}
}
//隐藏"增删查改"操作div
function hideOperateDiv(divId){
	window.shade.hide();
	$("#"+divId).hide();
	$(document).trigger("custompage_operatediv_close", [divId]);
}

//根据配置format属性对显示数据进行处理，并返回
function findShowValue(result, requirement){
	var showText = "";
	if(requirement && requirement.length != 0){
		try{
			showText = window.pageFormat.showValue(result, requirement);
		}catch(e){
			showText = "获取值失败";
			console.log("error: "+requirement.attr("ref"));
			console.log(e);
		}
	}
	showText = showText == null ? "" : showText;
	return showText;
}
//将绑定数据写入指定operateDiv的输入框内，并弹出
function bindDivData(divId, dataObj, preaction){
	if(preaction){
		$.ajax({
			url:preaction,
			success:function(data){
				if(data){
					bindDivData(divId, data);
				}
			},
			error:function(XMLHttpRequest, textStatus, errorThrown){
				promptMsg("初始化页面数据失败","initoperatediv_fail");
			},
			type:"post",
			dataType:"json"
		});
	}else{
		if(dataObj){
			$.each($("#"+divId+" .requirement input"),function(i, obj){
				obj.value = findShowValue(dataObj, $(obj));
			});
			$.each($("#"+divId+" .requirement select"),function(i, obj){
				var tempValue = findShowValue(dataObj, $(obj));
				if($(obj).children("option[value="+tempValue+"]").length != 0){
					obj.value = tempValue;
				}
			});
		}
		window.shade.show();
		$("#"+divId).show();
	}
}
//为额外一行写入绑定数据
function bindExtraData(extraResult){
	$.each($(".extra_row tr td[ref]"), function(i, tdObj){
		var extraRef = $(tdObj).attr("ref");
		if(extraRef){
			var requirement = $(tdObj).children("div[name=hideRequirement]").children("input,select");
			var showText = findShowValue(extraResult, requirement);
			if(requirement.is("select")){
				if(requirement.children("option[value="+showText+"]").length != 0){
					showText = requirement.children("option[value="+showText+"]").text();
				}
			}

			var refLablel = $(tdObj).children("label[id="+extraRef+"]");
			if(refLablel.length == 0){
				$(tdObj).append("<label id='"+extraRef+"'>"+showText+"</label>");
			}else{
				refLablel.text(showText);
			}
		}
	});
}

//根据查询条件部分生成对应的字符串
function generateRequirements(){
	requirements = "";
	if(checkRequirement($("#condition_requirement"))){
		return false;
	}
	$.each($("#condition_requirement .requirement input[ref][ref!=''], #condition_requirement .requirement select[ref][ref!='']"),function(i, obj){
		if($.trim(obj.value)){
			requirements += prefix + obj.name + separator + $(obj).attr("ref") + separator + obj.value + suffix;
		}
	});
	return true;
}

//查询数据
function queryData(){
	var url = $("#con_tab").attr("action");
	if(url){
		var serachOrderText = "";
		if($("#con_tab").attr("sortfield")){
			serachOrderText = prefix + $("#con_tab").attr("sortfield") + separator + ("desc" == $("#con_tab").attr("sorttype") ? "desc" : "asc") + suffix;
		}
		var rownum = $("#con_tab").attr("rownum") ? $("#con_tab").attr("rownum") : 10;
		var data = {
			requirementsText: requirements,
			currentPage: currentPage,
			serachOrderText: serachOrderText,
			rownum: rownum
		};
		$(document).trigger("custompage_query_start");
		$.ajax({
			url:url,
			data:data,
			success:function(data){
				if(data.resultMsg){
					if(data.resultMsgType){
						promptMsg(data.resultMsg, data.resultMsgType);
					}else{
						promptMsg(data.resultMsg, "query_success");
					}
				}
				$(document).trigger("custompage_query_success");
				resultList = data.resultList;
				
				clearTab();
				$.each(data.resultList,function(i, resultData){
					var row = addRow(resultData);
					
					$(row).attr("dataindex", i);
					if((i)%2 == 1){
						$(row).addClass("tr_even trbg");
					}else{
						$(row).addClass("tr_odd trbg");
					}
				});
				
				$(".trbg").mouseover(function(){
					$(this).toggleClass('tr_checked');
					$("#tab_div").css("overflow","hidden");
					$("#tab_div").css("overflow","auto");
				});
				$(".trbg").mouseout(function(){
					$(this).toggleClass('tr_checked');
					$("#tab_div").css("overflow","hidden");
					$("#tab_div").css("overflow","auto");
				});
				
				if (currentPage == 0 && data.totalRows != 0){
					currentPage = 1;
				}
				$("#totalNum").text(data.totalRows);
				$("#totalPage").text(Math.ceil(data.totalRows/rownum));
				bindPager();
				
				$.each($(".extra_row"), function(i,extraRow){
					if($(extraRow).attr("action")){
						$.ajax({
							url:$(extraRow).attr("action"),
							data:{requirementsText: requirements},
							success:function(extraData){
								$(extraRow).attr("dataindex", i);
								extraResult[i] = extraData;
								bindExtraData(extraData);
							},
							error:function(XMLHttpRequest, textStatus, errorThrown){
								promptMsg("查询额外一行数据失败","initextra_fail");
							},
							type:"post",
							dataType:"json"
						});
					}else{
						bindExtraData(data);
					}
				});
			},
			error:function(XMLHttpRequest, textStatus, errorThrown){
				promptMsg("查询失败","query_fail");
				$(document).trigger("custompage_query_fail");
			},
			type:"post",
			dataType:"json"
		});
	}
}
//绑定分页信息
function bindPager(){
	if($("#navigation").length >0){
		$("#currentPage").val(currentPage);
		var totalPage = parseInt($("#totalPage").text());
		document.getElementById("first").style.color = (currentPage == 1 || totalPage <= 1) ? "#777" : "#00f";
		document.getElementById("previous").style.color = (currentPage == 1 || totalPage <= 1) ? "#777" : "#00f";
		document.getElementById("next").style.color = (totalPage <= 1 || totalPage <= currentPage) ? "#777" : "#00f";
		document.getElementById("last").style.color = (totalPage <= 1 || totalPage <= currentPage) ? "#777" : "#00f";
		if (totalPage > 0 && $("#con_tab").attr("exportaction")){
			$("#exportBtn").show();
		}else{
			$("#exportBtn").hide();
		}
	}
}
//修改排序字段或升降序
function changeSort(obj){
	if(document.getElementById("con_tab").rows.length < 2){
		return;
	}
	$(".tr_top td").removeClass("arrows_up arrows_down");
	if(obj && $(obj).attr("ref")){
		if($("#con_tab").attr("sortfield") == $(obj).attr("ref")){
			if($("#con_tab").attr("sorttype") == "asc"){
				$("#con_tab").attr("sorttype", "desc");
				$(obj).addClass("arrows_down");
			}else{
				$("#con_tab").attr("sorttype", "asc");
				$(obj).addClass("arrows_up");
			}
		}else{
			$("#con_tab").attr("sortfield", $(obj).attr("ref"));
			if($("#con_tab").attr("sorttype") == "asc"){
				$(obj).addClass("arrows_up");
			}else if($("#con_tab").attr("sorttype") == "desc"){
				$(obj).addClass("arrows_down");
			}else{
				$("#con_tab").attr("sorttype", "asc");
				$(obj).addClass("arrows_up");
			}
		}
		queryData();
	}
}
//ajax同步获取数据
function getAjaxData(url){
	var ajaxData = null;
	$.ajax({
		url:url,
		success:function(data){
			ajaxData = data;
		},
		error:function(XMLHttpRequest, textStatus, errorThrown){
			promptMsg("获取数据失败","getdata_fail");
		},
		type:"post",
		async:false,
		dataType:"json"
	});
	return ajaxData;
}
//初始化select下拉框的options
function initSelectOptions(selObj){
	var listObj = {};
	try{
		listObj = eval("("+selObj.attr("list")+")");
	}catch(e){
		console.log("error: "+selObj.attr("list"));
		console.log(e);
	}
	var listKey = selObj.attr("listKey");
	var listValue = selObj.attr("listValue");
	var headerKey = selObj.attr("headerKey");
	var headerValue = selObj.attr("headerValue");
	
	for(var x in listObj){ //根据list属性指向的js对象，生成option
		var keyStr = null;
		if(listKey != undefined && listKey != ""&& listObj[x][listKey]){
			keyStr = listObj[x][listKey];
		}else{
			keyStr = x;
		}
		var valueStr = null;
		if(listValue != undefined && listValue != "" && listObj[x][listValue]){
			valueStr = listObj[x][listValue];
		}else{
			valueStr = listObj[x];
		}
		selObj.append("<option value=\""+keyStr+"\">"+valueStr+"</option>");
	}
	if(headerKey != undefined){ //为select赋予默认值
		var optionHeader = selObj.children("option[value="+headerKey+"]");
		headerValue = headerValue == undefined ||  headerValue==""? headerKey : headerValue;
		if(optionHeader.length == 0){
			selObj.prepend("<option value=\""+headerKey+"\" selected=\"selected\">"+headerValue+"</option>");
		}
		selObj.val(headerKey);
	}
}
//校验输入要素的值是否符合对应格式要求
function checkRequirement(requirementsDiv){
	var hasError = false;
	$.each(requirementsDiv.children(".requirement").children("input[ref][ref!=''],select[ref][ref!='']"), function(i, requirement){
		if(requirement.value==""){
			if($(requirement).is("[required=required]")){
				if($(requirement).is("select")){
					promptMsg("请选择"+$(requirement).parent().children("span").text(),"format_error");
				}else{
					promptMsg($(requirement).parent().children("span").text()+"不能为空","format_error");
				}
				hasError = true;
				return false;
			}
			return true;
		}
		if(!window.pageFormat.validate(requirement)){
			hasError = true;
			return false;
		}
	});
	return hasError;
}
//为指定的元素集合添加功能：通过按回车和tab键 移动焦点
function addFocusByEnterOrTab(elements,lastfunc){
	var enterObj = $.enterlist.create(lastfunc);
	$.each(elements, function(i, element){
		enterObj.add($(element));
	});
}