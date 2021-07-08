// 票据查询的js文件 
function detail(id) {
	location.href = "docset_detail.action?docId=" + id;
}


//查询表头回调函数，返回json字符串
function queryData(json, parameter) {
	
	// 替换经办人代码
	setOpCodes(json);
	//替换返回来的参数用中文代替
	changeToChinese(json);
	//在最后一列增加一列
	setEndColumn(json);
	
	var defaultUrl="docset_queryDocListJson.action?reportName=DocSet&queryForm="
		+ parameter + "&date=" + (new Date()).getTime();
	var flag=$("#docFlag").val();
	if ((flag=="1")||(flag=="101")) {
		var defaultUrl1="docset_queryDocListJson.action?reportName=TmpScan&queryForm="
			+ parameter + "&date=" + (new Date()).getTime();
		// 此处是获取表格的表头信息
		$.getJSON("queryDataTable_createHeadCol.action?powerName=TmpScan&xmlName=QueryTmpScan.xml",
						function(json) {
			//替换返回来的参数用中文代替
			changeToChinese(json);
			$("#example1_wrapper").remove();
			if ($("#firstTable").html()=="") {
				$("#firstTable").append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"example1\">"+
			       "</table>");
			}
			bindData("example2",defaultUrl1,json);
		});
	}else {
		$("#example2_wrapper").remove();
		if ($("#secondTable").html()=="") {
			$("#secondTable").append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"example2\">"+
		       "</table>");
		}
		bindData("example1",defaultUrl,json);
	}
}
$(document).ready(function() {
					// 此处是获取表格的表头信息
					$.getJSON("queryDataTable_createHeadCol.action?powerName=DocSet&xmlName=QueryDocSet.xml",
									function(json) {
										// 查询业务数据
										queryData(json, "");
									});
				});


function setEndColumn(json){
	// ----------end-------------
	// 添加自定义的表头信息
	// 下面注释块是将一列添加在表格的最后一列，
	//-------start---------
	json[json.length] = {
		sTitle : "操作",

		fnRender : function(obj) {

			var sReturn = "<a href='docset_detail.action?docId="
					+ obj.aData["docID"] + "'>详细</a>";
			return sReturn;
		},
		"aTargets" : [ 0 ]
	};
	
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
// 经办柜员由于是在map里面，此处将map对象转换成普通对象显示在表格控件里面
function setOpCodes(json)
{
	for ( var header in json) {

		json[header] ={
				sTitle : json[header]["sTitle"],
				sClass:"center",
				mDataProp:json[header]["mDataProp"],
				codeInfo:json[header]["codeInfo"],
				fnRender : function(obj) {
					var sReturn = obj.aData[this.mDataProp];
					if(this.mDataProp == "opCodes")
						{
					 sReturn = obj.aData[ "opCodes" ];
					if(sReturn != null && sReturn !="undefined" )
						{
					 sReturn =sReturn["opCode_12"];
						}
					
						}
					return sReturn;
		     }
			};
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
	$("#"+id)
	.dataTable(
			{	
				"bDestroy" : true,
				"bFilter": false,
				"bProcessing": true,
				"sPaginationType" : "full_numbers",
				"iDisplayLength":10,
				 "bServerSide":true,//打开服务器模式，这个是最重要的</font>
                 "bLengthChange":false, //关闭每页显示多少条数据
                 "fnServerData":retrieveData,//自定义数据获取函数
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
// 服务器分页函数
//自定义数据获取函数
function retrieveData( sSource, aoData, fnCallback ) {
	var postData = "";
	
	for(var data in aoData)
		{
			postData = postData+aoData[data]["name"]+"="+aoData[data]["value"]+"&";
		}
        $.ajax( {
                "type": "POST",
                "url": sSource,
                "dataType": "json",
                "data": postData,
                "success": function(resp) {
                        fnCallback(resp);
                }
        });
}