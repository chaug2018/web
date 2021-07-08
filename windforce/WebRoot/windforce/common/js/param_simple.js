$(document).ready(function(){
	window.setInterval(function(){ 
		//queryData();
	}, 3000); 
	
	window.customPrompt=function(msg, type){
		if(type == "query_fail"){
			//top.wfAlert(msg);
		}else if(type == "delete_error"){
			top.wfAlert(msg,false);
		}else if(type == "add_error"){
			top.wfAlert(msg,false);
		}else if(type == "opdate_error"){
			top.wfAlert(msg,false);
		}else if(type == "add_success"){
			top.wfAlert(msg,false);
		}else if(type == "delete_success"){
			top.wfAlert(msg,false);
		}else if(type == "update_success"){
			top.wfAlert(msg,false);
		}else if(type == "update_error"){
			top.wfAlert(msg,false);
		}else if(type == "format_error"){
			top.wfAlert(msg,false);
		}else{
			top.wfAlert(msg,false);
		}
	};
	
});

window.pageFormat.extend("nameCheck",{
	validate : function(obj, pattern, errormsg) {
		if(obj.value && $.trim(obj.value) == ""){
			promptMsg("参数名称不允许为空。","add_error");
			return false ;
		}
		var isExit = getAjaxData("nameCheck.action?group=" + pattern + "&paramName=" + obj.value) ;
		if(null == isExit){
			return false ;
		}else if(isExit){
			promptMsg("参数名称不允许重复。","add_error");
			return false ;
		}
		return true;
	}
});

var type = "String" ;
window.pageFormat.extend("combCheckType",{
	validate : function(obj, pattern, errormsg) {
		type = obj.value;
		return true;
	}
});

window.pageFormat.extend("combCheck",{
	validate : function(obj, pattern, errormsg) {
		var values = obj.value;
		if("String" == type ){
			return true;
		}else if("int" == type){
			var regexp = /^\d+\.?\d*$/; // 默认浮点数
			if (!regexp.test(obj.value)) {
				promptMsg("当前值的类型为数字类型。","format_error");
				return false;
			}
			return true;
		}else if("boolean" == type){
			if("true" == values || "false" == values){
				return true;	
			}else{
				promptMsg("当前值的类型为boolean类型，请输入true或者false。","format_error");
				return false;
			}
		}
		return true;
	}
});

var beginDateFormat = window.pageFormat.extend("beginDate", "date"); //继承date格式的方法
beginDateFormat.validate = function(obj, pattern, errormsg) { //重写validate(校验)方法
	if(window.pageFormat.validate(obj, "date")){
		var beginDate = $(obj.parentNode.parentNode).find("input[ref=activeTime][format=beginDate]").val();
		var endDate = $(obj.parentNode.parentNode).find("input[ref=deactiveTime][format=endDate]").val();
		if(beginDate && endDate && beginDate > endDate){
			promptMsg("失效时间不能小于启用时间","format_error");
			return false;
		}
		return true;
	}else{
		return false;
	}
};
var endDateFormat = window.pageFormat.extend("endDate", "date"); //继承date格式的方法
endDateFormat.validate = function(obj, pattern, errormsg) { //重写validate(校验)方法
	if(window.pageFormat.validate(obj, "date")){
		var beginDate = $(obj.parentNode.parentNode).find("input[ref=activeTime][format=beginDate]").val();
		var endDate = $(obj.parentNode.parentNode).find("input[ref=deactiveTime][format=endDate]").val();
		if(beginDate && endDate && beginDate > endDate){
			promptMsg("失效时间不能小于启用时间","format_error");
			return false;
		}
		return true;
	}else{
		return false;
	}
};


$(document).bind("custompage_addRow", function(e , row){
	if($(row).find("td[ref=paramFlag]").text() == "失效"){
		$(row).find("input[opertype=delete]").attr('disabled',true);
	}
});

//开始查询时，打开遮罩层
$(document).bind("custompage_query_start", function(){
	window.shade.show();
});
//查询成功时，关闭遮罩层
$(document).bind("custompage_query_success", function(){
	window.shade.hide();
});
//查询失败时，关闭遮罩层
$(document).bind("custompage_query_fail", function(){
	window.shade.hide();
});
