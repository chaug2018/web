//自定信息提示框
window.customPrompt=function(msg, type){
	if(type != "getdata_fail"){
		top.wfAlert(msg);
	}
};
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

//自定义开始日期/结束日期格式，继承自date类型
var beginDateFormat = window.pageFormat.extend("beginDate", "date"); //继承date格式的方法
beginDateFormat.validate = function(obj, pattern, errormsg) { //重写validate(校验)方法
	if(window.pageFormat.validate(obj, "date")){
		var beginDate = $(obj.parentNode.parentNode).find("input[ref=operateDate][format=beginDate]").val();
		var endDate = $(obj.parentNode.parentNode).find("input[ref=operateDate][format=endDate]").val();
		if(beginDate && endDate && beginDate > endDate){
			promptMsg("起始日期不能大于结束日期","format_error");
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
		var beginDate = $(obj.parentNode.parentNode).find("input[ref=operateDate][format=beginDate]").val();
		var endDate = $(obj.parentNode.parentNode).find("input[ref=operateDate][format=endDate]").val();
		if(beginDate && endDate && beginDate > endDate){
			promptMsg("起始日期不能大于结束日期","format_error");
			return false;
		}
		return true;
	}else{
		return false;
	}
};

function seeDetail(trObj){
	if(trObj && $(trObj).attr("dataindex")){
		var dataObj = resultList[$(trObj).attr("dataindex")];//获得双击的表格行所对应的数据js对象
		generateOperateDiv("query");
		bindDivData("queryOperateDiv", dataObj);
	}
}