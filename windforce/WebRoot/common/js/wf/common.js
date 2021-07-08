function clearButton() {
	var inputTag = document.getElementsByTagName("input");
	for ( var i = 0; i < inputTag.length; i++) {
        if(inputTag[i].type == "text")
        	{
        	inputTag[i].value = "";
        	}else if(inputTag[i].type == "checkbox")
        		{
        		inputTag[i].checked = false;
        		}
	}
}

/**
 * 用于获取event对象，兼容firefox。
 * 
 * @param func
 *            function对象
 * @return event 对象
 */
function getCurrentEvent(func){
    if (!func) {
        return null;
    }
    var _event = window.event;  
    if(typeof(_event) == 'undefined' || _event == null){
        var _fun = func.caller;  
        if (!_fun) {
            return null;
        }
        _event = _fun.arguments[0];  
        return _event;  
    }
    return _event;  
} 
/**
 * 显示出正在等待的ajax_bar.gif，并定位到传入的元素的正中间。 一般需要在发送ajax请求前执行。
 * 
 * @param element
 *            需要定位在此元素的正中间
 * 
 */
var ajaxBarObj = null;
function showWaitBar(element) {
    if (ajaxBarObj == null) {
        ajaxBarObj = $("<img>",{
            src : "images/ajax_bar.gif",
            style:"position:absolute;z-index:10000;display:none"
        });
        ajaxBarObj.appendTo(document.body);
    } 
    // 定位图片位置为传入对象元素的中央
    var ele = $(element);
    var imgWidth = ajaxBarObj.width();
    var imgHeight = ajaxBarObj.height();
    var eleWidth = ele.width();
    var eleHeight = ele.height();
    var position = ele.offset();
    var eleTop = position.top;
    var eleLeft = position.left;
    var posTop = eleTop + eleHeight/2 - imgHeight/2;
    var posLeft = eleLeft + eleWidth/2 - imgWidth/2;
    ajaxBarObj.css({position:"absolute",top:posTop,left:posLeft});
    ajaxBarObj.show();
}


/**
 * 显示出正在等待的ajax_bar.gif，并定位到传入的元素的正中间。 一般需要在回调方法结束时执行。
 */
function hideWaitBar() {
    if (ajaxBarObj) {
        ajaxBarObj.hide();
    }
}

/**
 * 显示弹出框
 */
var showDialogDiv = null;

function showDialogInfo(url,deWidth,deHeight) {
    // 创建用户窗口
    if (showDialogDiv == null) {
    	showDialogDiv = $("<div>", {
                            "style": "diaplay:none;"
                        }).appendTo("body");
    }
    
    var dialogDiv ;
    var title ;
    var width ;
    var height ;
   
        dialogDiv = showDialogDiv;
        title = "";
        width = deWidth;
        height = deHeight;
    
    // 调用此方法的对象元素
    // var evt = getCurrentEvent(showDialogInfo);
    // 显示等待图标
   // if (evt) {
       // showWaitBar(evt.srcElement||evt.target);
    // }
    var requestPath = url ;

    // *****注意：此处需要先将div的内容设为""，再显示模式对话框，再设置取回的html；否则在IE下窗口大小将不正常
    dialogDiv.html("");
    $.post( requestPath,"",function(data){
        // 隐藏等待图标
        // if (evt) {
            // //hideWaitBar();
        // }
        // 显示模式窗口
        dialogDiv.dialog({
            modal: true,
            resizable: false,
            height: height,
            width: width,
            maxWidth: width,
            maxHeight: height,
            draggable: false,
            title: title,
            // 隐藏所有下拉框，为了兼容IE6。在IE6中，下拉框总是最顶层显示。
            open: function(event, ui) {$("select").hide()},
            close: function(event, ui) {$("select").show()}

        });
        dialogDiv.css("background","url(../../common/images/content_bg.jpg)");
        dialogDiv.css("overflow-x","hidden");
        dialogDiv.html(data);
    });
}
function refresh()
{
	// TODO 系统首页代码，由于现在首页内容为空，暂时用js刷新代替，等以后有了内容则填写服务器路径
	document.getElementById("workFrame").src = '';
	document.getElementById("moduleDesc").innerText="";
	document.getElementById("moduleDesc").title ="";
};
/**
 * 带参数的弹出框
 */
function showWindowDialogForUrl(action,taskName, proDefKey, curPage,width,height)  {
    // 创建用户窗口
    if (showDialogDiv == null) {
    	showDialogDiv = $("<div>", {
                            "style": "diaplay:none;"
                        }).appendTo("body");
    }    
    var dialogDiv;
    var title ;
    dialogDiv = showDialogDiv;
    title = "";
    // *****注意：此处需要先将div的内容设为""，再显示模式对话框，再设置取回的html；否则在IE下窗口大小将不正常
    dialogDiv.html("");
    $.post(
    	action,
    	{"processDefinitionKey":proDefKey,"taskName":taskName, "curPage":curPage},
    	function(data){
	        // 显示模式窗口
	        dialogDiv.dialog({
	            modal: true,
	            resizable: false,
	            height: height,
	            width: width,
	            maxWidth: width,
	            maxHeight: height,
	            draggable: false,
	            title: title,
	            // 隐藏所有下拉框，为了兼容IE6。在IE6中，下拉框总是最顶层显示。
	            open: function(event, ui) {$("select").hide()},
	            close: function(event, ui) {$("select").show()}
	
	        });
        dialogDiv.css("background","url(../../common/images/content_bg.jpg)");
        dialogDiv.css("overflow-x","hidden");
        dialogDiv.html(data);
    });
}