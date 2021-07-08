
//查询表头回调函数，返回json字符串
function queryData(json, parameter) {
	//替换返回来的参数用中文代替
	changeToChinese(json);
	var powerName =$("#reportName").val();
	var defaultUrl="ParamsManagerAction_getTableData.action?reportName="+powerName+"&queryForm="
		+ parameter + "&date=" + (new Date()).getTime();
	bindData("example1",defaultUrl,json);

}
$(document).ready(queryDataBegin);

function queryDataBegin(){
	var powerName =$("#reportName").val();
	// 此处是获取表格的表头信息
	$.getJSON("queryDataTable_createHeadCol.action?powerName="+powerName+"&xmlName="+powerName+".xml",
	function(json) {
		// 查询业务数据
		queryData(json, "");
	});
}

function deleteParam(id){
	var powerName =$("#reportName").val();
	if(confirm('是否删除该参数?')){
		$.post("ParamsManagerAction_deleteParams.action","autoid="+id,function(data){
			top.paramframeForward("ParamsManagerAction_getReportData.action?reportName="+powerName);	
			
		});
	}
}
function setEndColumn(json){
	// ----------end-------------
	// 添加自定义的表头信息
	// 下面注释块是将一列添加在表格的最后一列，
	//-------start---------
	json[json.length] = {
		sTitle : "操作",

		fnRender : function(obj) {
			//var sReturn = "<input type='button' class='submit_but08' onclick=\"javascript:top.showd('ParamsManagerAction_initUpdateParams.action?autoid=" + obj.aData["id"] + "',660,350)\" value='修改'/>  <input type='button' class='submit_but08' onclick=\"deleteParam('"+ obj.aData["id"] +"');\" value='删除'>";
			var sReturn = "<input type='button' class='submit_but08' onclick=\"initUpdateParam('"+ obj.aData["id"] +"');\" value='修改'/>  <input type='button' class='submit_but08' onclick=\"deleteParam('"+ obj.aData["id"] +"');\" value='删除'>";
			return sReturn;
		},
		"aTargets" : [ 0 ]
		
		
		
	};
	
}

function initUpdateParam(id){
	var tablename=$("#reportName").val();
	var winHeight = 350;
	var winWidth = 660;
	var winTop = (window.screen.height - winHeight) / 2;
	var winLeft = (window.screen.width - winWidth) / 2;
	var sFeatures = "dialogHeight:" + winHeight + "px;dialogWidth:" + winWidth
			+ "px;dialogLeft:" + winLeft + "px;dialogTop:" + winTop + "px;";
	var returnValue = window.showModalDialog("ParamsManagerAction_initUpdateParams.action?autoid=" + id,null,sFeatures);
	if(returnValue){
		// 刷新界面
		top.paramframeForward("ParamsManagerAction_getReportData.action?reportName="+document.getElementById("reportName").value);
	}
}

function setFirstColumn(json){
	//-----------end-------------

// 下面注释块是将一列添加在表格的第一列，上面json[json.length]是将自定义列添加在最后一列
//  ----start----
 json.unshift( {
sTitle : "全选",

 fnRender : function(obj) {
		  
 var sReturn = "<a href='docset_detail.action?docId="
 +obj.aData["docID"]+"'>详细</a>";
 return sReturn;
 },"aTargets": [ 0 ]
});	
}

function changeToChinese(json){
	// 如有需要替换代码则需要遍历以下代码 {mDataProp=workDate, codeInfo={32=支票}, sTitle=工作日期,sClass=center}
	// ----------start-----------
	for ( var header in json) {
		// 绑定列数据读取方法,在读取的时候替换需要显示的代码值 如：票据类型，0:支票，1:凭证
		if(json[header]["codeInfo"] != null && json[header]["codeInfo"] != "undefined")
		{
			
			json[header] ={
				sTitle : json[header]["sTitle"],
				sClass:"center",
				mDataProp:json[header]["mDataProp"],
				index:header,
				codeInfo:json[header]["codeInfo"],
				fnRender : function(obj) {
					var sReturn = obj.aData[ this.mDataProp ];
		     		if(this.codeInfo != null && this.codeInfo !="undefined" )
		     		{
		     			sReturn =this.codeInfo[sReturn];
		     		}
		     		return sReturn;
				}
			};
	
		}
       
	}
	
}
// 设置数字输入框不能输入字母
function setNumber() {
	if (!(((window.event.keyCode >= 48) && (window.event.keyCode <= 57))

	|| (window.event.keyCode == 13) || (window.event.keyCode == 46)

	|| (window.event.keyCode == 45)))

	{
		alert("只能输入数值型数据！");
		window.event.keyCode = 0;

	}
	return;
}

function bindData(id,defaultUrl,json){
	// ----------end-------------
	// 添加自定义的表头信息
	// 下面注释块是将一列添加在表格的最后一列，
	//-------start---------
	setEndColumn(json);
	$("#"+id)
	.dataTable(
			{	
				"bDestroy" : true,
				"sPaginationType" : "full_numbers",
				// "bFilter": false ,//开关，是否启用客户端过滤器
				"sAjaxSource" :defaultUrl ,// 此URL是获取数据源
				"aoColumns" : json,
				"oLanguage" : {
					"sProcessing" : "正在加载中......",
					"sLengthMenu" : "每页显示 _MENU_ 条记录",
					"sZeroRecords" : "对不起，查询不到相关数据！",
					"sEmptyTable" : "表中无数据存在！",
					"sInfo" : "当前显示 _START_ 到 _END_ 条，共 _TOTAL_ 条记录",
					"sInfoFiltered" : "数据表中共为 _MAX_ 条记录",
					"sSearch" : "搜索",
					"oPaginate" : {
						"sFirst" : "首页",
						"sPrevious" : "上一页",
						"sNext" : "下一页",
						"sLast" : "末页"
					}
				}
			});
	
}
