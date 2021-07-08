/**
 * 界面工具类 银之杰科技股份有限公司 2013-1-24
 * 
 * @author 陈林江
 * @version 1.0.0
 */
var showAlert = true;
var processWait = 100;// 进度条每隔多久进行一次递增（单位毫秒）
var processStepSize = 10; // 进度条每次递增多少像素(此变量暂时没用上)
var processTimeOut = 10000; // 进度条超时时间(单位毫秒)
/**
 * 以下为系统内部使用的全局变量
 */
var serverUrl;
var processInval; // 进度条事件
var waitTime; // 此次进度条已经进行了多久
var message;
/**
 * 自定义提示框
 * 
 * @param strContent
 *            需要提示的内容
 * @param useCheck
 *            是否显示禁用选项
 * @param parentView
 *            在哪个界面进行弹出，如为空，则直接在最外层进行弹出
 */
function wfAlert(strContent, useCheck, parentView, callback) {
	message = strContent;
	if (!showAlert && useCheck) {
		return;
	}
	if (!parentView) {
		parentView = document;
	}
	lockScreen(parentView); // 锁屏
	var strTitle = "提示框";
	var msgw, msgh, bordercolor;
	msgw = 400;// 提示窗口的宽度
	msgh = 100;// 提示窗口的高度
	titleheight = 25; // 提示窗口标题高度
	bordercolor = "#336699";// 提示窗口的边框颜色
	titlecolor = "#99CCFF";// 提示窗口的标题颜色
	var msgObj = parentView.createElement("div");
	msgObj.setAttribute("id", "msgDiv");
	msgObj.setAttribute("align", "center");
	msgObj.style.background = "white";
	msgObj.style.border = "1px solid " + bordercolor;
	msgObj.style.position = "absolute";
	msgObj.style.left = "55%";
	msgObj.style.top = "50%";
	msgObj.style.font = "12px/1.6em Verdana, Geneva, Arial, Helvetica, sans-serif";
	msgObj.style.marginLeft = "-225px";
	msgObj.style.marginTop = -75 + parentView.documentElement.scrollTop + "px";
	msgObj.style.width = msgw + "px";
	msgObj.style.height = msgh + "px";
	msgObj.style.textAlign = "center";
	msgObj.style.lineHeight = "25px";
	msgObj.style.zIndex = "10051";
	var title = parentView.createElement("h4");
	title.setAttribute("id", "msgTitle");
	title.setAttribute("align", "right");
	title.style.margin = "0";
	title.style.padding = "3px";
	title.style.background = bordercolor;
	title.style.filter = "progid:DXImageTransform.Microsoft.Alpha(startX=20, startY=20, finishX=100, finishY=100,style=1,opacity=75,finishOpacity=100);";
	title.style.opacity = "0.75";
	title.style.border = "1px solid " + bordercolor;
	title.style.height = "18px";
	title.style.font = "12px Verdana, Geneva, Arial, Helvetica, sans-serif";
	title.style.color = "white";
	title.style.cursor = "pointer";
	title.title = "点击关闭";
	title.innerHTML = "<table border='0' width='400px' style='vertical-align:top'><tr style='vertical-align:top'><td align='left'><b>"
			+ strTitle
			+ "</b></td><td align='right'><input type='button' id='closeButton'value='关闭'  style='margin-top:-3px'/></td></tr></table></div>";
	var txt = parentView.createElement("div");
	txt.setAttribute("id", "msgTxtDiv");
	var subContent = strContent;
	if (strContent.length > 40) { // 提示语过长时只显示前40位
		subContent = strContent.substring(0, 40) + "...";
		subContent += "<a href='#' style='color:#FF0000' id='showDetail' >详情</a>";
	}
	txt.innerHTML = "<table style='width:100%'><tr style='vertical-align:top'><td id='msgTxt'>"
			+ subContent + "</td></tr></table>";

	var checkText = parentView.createElement("p");
	checkText.innerHTML = "<table border='0' width='100%'><tr><td align='left' width='5%'><input type='checkbox' id='wfCheckBox'></td><td align='left'>不再弹出提示框</td></tr></table>";
	parentView.body.appendChild(msgObj);
	msgObj.appendChild(title);
	msgObj.appendChild(txt);
	if (useCheck) {
		msgObj.appendChild(checkText);
	}
	if (parentView.getElementById("showDetail") != null) {
		parentView.getElementById("showDetail").onclick = function() {
			showMsgDetail(parentView);
		};
	}
	parentView.getElementById("closeButton").onblur = function() {
		closeBlur(parentView);
	};
	parentView.getElementById("closeButton").focus();
	parentView.getElementById("closeButton").onclick = function() {
		closeAlert(parentView, useCheck);
		if (callback) {
			callback();
		}
	};
}

function closeBlur(parentView) {
	if (parentView.getElementById("closeButton") != null) {
		parentView.getElementById("closeButton").focus();
	}
}

/**
 * 关闭弹出框，内部方法
 * 
 * @param parentView
 *            父页面
 * @param useCheck
 *            是否使用禁止弹出的选项
 */
function closeAlert(parentView, useCheck) {
	if (!parentView) {
		parentView = document;
	}
	if (useCheck && parentView.getElementById("wfCheckBox").checked) {
		showAlert = false;
	}
	parentView.getElementById("msgDiv").removeChild(
			parentView.getElementById("msgTitle"));
	parentView.body.removeChild(parentView.getElementById("msgDiv"));
	releaseScreen(parentView); // 释放屏幕
}

/**
 * 打开进度条
 * 
 * @param strTitle
 *            内容
 * @param parentView
 *            父页面
 */
function startProcess(strTitle, parentView) {
	if (!parentView) {
		parentView = document;
	}
	waitTime = 0;
	lockScreen_process(parentView);
	var msgw, msgh, bordercolor;
	msgw = 400;// 提示窗口的宽度
	msgh = 55;// 提示窗口的高度
	titleheight = 25; // 提示窗口标题高度
	bordercolor = "#336699";// 提示窗口的边框颜色
	titlecolor = "#99CCFF";// 提示窗口的标题颜色
	var processDiv = parentView.createElement("div");
	processDiv.setAttribute("id", "processDiv");
	processDiv.setAttribute("align", "center");
	processDiv.setAttribute("overflow", "hidden");
	processDiv.style.background = "white";
	processDiv.style.border = "1px solid " + bordercolor;
	processDiv.style.position = "absolute";
	processDiv.style.left = "55%";
	processDiv.style.top = "50%";
	processDiv.style.font = "12px/1.6em Verdana, Geneva, Arial, Helvetica, sans-serif";
	processDiv.style.marginLeft = "-225px";
	processDiv.style.marginTop = -75 + parentView.documentElement.scrollTop
			+ "px";
	processDiv.style.width = msgw + "px";
	processDiv.style.height = msgh + "px";
	processDiv.style.textAlign = "center";
	processDiv.style.lineHeight = "25px";
	processDiv.style.zIndex = "10041";
	var title = parentView.createElement("h4");
	title.setAttribute("id", "processtitle");
	title.setAttribute("align", "right");
	title.style.margin = "0";
	title.style.padding = "3px";
	title.style.background = bordercolor;
	title.style.filter = "progid:DXImageTransform.Microsoft.Alpha(startX=20, startY=20, finishX=100, finishY=100,style=1,opacity=75,finishOpacity=100);";
	title.style.opacity = "0.75";
	title.style.border = "1px solid " + bordercolor;
	title.style.height = "18px";
	title.style.font = "12px Verdana, Geneva, Arial, Helvetica, sans-serif";
	title.style.color = "white";
	title.style.cursor = "pointer";
	title.title = "请等待...";
	title.innerHTML = "<table border='0' width='100%'><tr><td align='left'><b id='processTitleDesc'>"
			+ strTitle
			+ "</b></td><td id='processClose'  style='display:none'>关闭</td></tr></table></div>";
	var imageDiv = parentView.createElement("div");
	imageDiv.setAttribute("align", "left");
	imageDiv.setAttribute("id", "imgDiv");
	var image = parentView.createElement("img");
	image.setAttribute("src", serverUrl + "common/images/process.jpg");
	image.setAttribute("id", "imageId");
	image.setAttribute("height", "30px");
	imageDiv.appendChild(image);
	parentView.body.appendChild(processDiv);
	processDiv.appendChild(title);
	processDiv.appendChild(imageDiv);
	processDiv.focus();
	parentView.getElementById("processClose").onclick = function() {
		stopProcess(parentView);
	};
	processInval = window.setInterval(function() {
		changeImageSize(parentView);
	}, processWait);
}

/**
 * 显示一个弹出界面
 * 
 * @param url
 *            界面地址，可以使jsp，页也可以是action
 * @param params
 *            需要传递的参数
 * @param titleDesc
 *            页面头描述
 * @param width
 *            页面宽度
 * @param height
 *            页面高度
 * @param parentView
 *            页面所在的父页面
 */
function showPage(url, params, titleDesc, width, height, parentView) {
	if (!parentView) {
		parentView = document;
	}
	lockScreen_showPage(parentView);
	var bordercolor = "#336699";// 提示窗口的边框颜色
	var showPageDiv = parentView.createElement("div");
	showPageDiv.setAttribute("id", "showPageDiv");
	showPageDiv.setAttribute("align", "center");
	showPageDiv.style.background = "white";
	showPageDiv.style.border = "1px solid " + bordercolor;
	showPageDiv.style.position = "absolute";
	showPageDiv.style.left = "50%";
	showPageDiv.style.top = "30%";
	showPageDiv.style.font = "12px/1.6em Verdana, Geneva, Arial, Helvetica, sans-serif";
	showPageDiv.style.marginLeft = "-225px";
	showPageDiv.style.marginTop = -75 + parentView.documentElement.scrollTop
			+ "px";
	showPageDiv.style.width = width + "px";
	showPageDiv.style.height = height + "px";
	showPageDiv.style.textAlign = "center";
	showPageDiv.style.lineHeight = "25px";
	showPageDiv.style.zIndex = "10031";
	var title = parentView.createElement("h4");
	title.setAttribute("id", "showPageTitle");
	title.setAttribute("align", "right");
	title.style.margin = "0";
	title.style.padding = "3px";
	title.style.background = bordercolor;
	title.style.filter = "progid:DXImageTransform.Microsoft.Alpha(startX=20, startY=20, finishX=100, finishY=100,style=1,opacity=75,finishOpacity=100);";
	title.style.opacity = "0.75";
	title.style.border = "1px solid " + bordercolor;
	title.style.height = "18px";
	title.style.font = "12px Verdana, Geneva, Arial, Helvetica, sans-serif";
	title.style.color = "white";
	title.style.cursor = "pointer";
	title.title = titleDesc;
	title.innerHTML = "<table border='0' width='100%'><tr><td width='85%' align='left'><b id='showPageTitleDesc'>"
			+ titleDesc
			+ "</b></td><td id='showPageClose' width='15%'  >关闭</td></tr></table>";
	var pageBody = parentView.createElement("div");
	pageBody.setAttribute("id", "pageBody");
	pageBody.style.width = width + "px";
	pageBody.style.height = height - 25 + "px";
	pageBody.style.position = "absolute";
	pageBody.style.left = "0px";
	$.post(url, params, function(data) {
		showPageDiv.appendChild(title);
		showPageDiv.appendChild(pageBody);
		parentView.body.appendChild(showPageDiv);
		parentView.getElementById("showPageClose").onclick = function() {
			closeShowPage(parentView);
		};
		$("#pageBody").html(data);
	});

}
/**
 * 关闭弹出窗口
 * 
 * @param parentView
 *            父页面
 */
function closeShowPage(parentView) {
	if (!parentView) {
		parentView = document;
	}
	var showPageDiv = parentView.getElementById("showPageDiv");
	var showPageTitle = parentView.getElementById("showPageTitle");
	var pageBody = parentView.getElementById("pageBody");
	showPageDiv.removeChild(showPageTitle);
	showPageDiv.removeChild(pageBody);
	parentView.body.removeChild(showPageDiv);
	releaseScreen_showPage(parentView);
}
/**
 * 关闭弹出窗口
 * 
 * @param parentView
 *            父页面
 */
function closeShowPage_lockPage(parentView) {
	if (!parentView) {
		parentView = document;
	}
	var showPageDiv = parentView.getElementById("showPageDiv");
	var showPageTitle = parentView.getElementById("showPageTitle");
	var pageBody = parentView.getElementById("pageBody");
	showPageDiv.removeChild(showPageTitle);
	showPageDiv.removeChild(pageBody);
	parentView.body.removeChild(showPageDiv);
	releaseScreen_lockPage(parentView);
}

/**
 * 修改进度条描述
 * 
 * @param desc
 *            描述
 * @param parentView
 *            父页面
 */
function changeProcessTitle(desc, parentView) {
	if (!parentView) {
		parentView = document;
	}
	parentView.getElementById("processTitleDesc").innerText = desc;
}

/**
 * 停止进度条
 * 
 * @param parentView
 *            父页面
 */
function stopProcess(parentView) {
	if (!parentView) {
		parentView = document;
	}
	window.clearInterval(processInval);
	if (parentView.getElementById("imageId") == null) {
		return;
	}
	parentView.getElementById("imageId").width = 400;
	window.setTimeout(function() {
		lazyStop(parentView);
	}, 200);
}

/**
 * 延迟停止,视觉上好看点
 * 
 * @param parentView
 *            父页面
 */
function lazyStop(parentView) {
	if (!parentView) {
		parentView = document;
	}
	var processDiv = parentView.getElementById("processDiv");
	if (processDiv == null) { // 已经被停过了，因为这个方法不是线程安全的
		return;
	}
	parentView.getElementById("imgDiv").removeChild(
			parentView.getElementById("imageId"));
	processDiv.removeChild(parentView.getElementById("imgDiv"));
	processDiv.removeChild(parentView.getElementById("processtitle"));
	parentView.body.removeChild(processDiv);
	releaseScreen_process(parentView);
}

/**
 * 锁住页面
 * 
 * @param parentView
 *            父页面，也就是需要锁住的页面
 */
function lockScreen(parentView) {
	if (!parentView) {
		parentView = document;
	}
	var sWidth;
	var sHeight;
	sWidth = document.body.scrollWidth;
	sHeight = document.body.scrollHeight;
	var bgObj = parentView.createElement("div");
	bgObj.setAttribute('id', 'wfbgDiv');
	bgObj.style.position = "absolute";
	bgObj.style.top = "0";
	bgObj.style.background = "#777";
	bgObj.style.filter = "progid:DXImageTransform.Microsoft.Alpha(style=3,opacity=25,finishOpacity=75";
	bgObj.style.opacity = "0.6";
	bgObj.style.left = "0";
	bgObj.style.width = sWidth + "px";
	bgObj.style.height = sHeight + "px";
	bgObj.style.zIndex = "10050";
	parentView.body.appendChild(bgObj);
	if (navigator.userAgent.indexOf("MSIE 6.0") > 0) {
		parentView.body.appendChild(createBgFrame("bgFrame", parentView));
	}
}

/**
 * 释放页面
 * 
 * @param parentView
 *            父页面，也就是需要释放的页面
 */
function releaseScreen(parentView) {
	if (!parentView) {
		parentView = document;
	}
	var bgdiv = parentView.getElementById("wfbgDiv");
	parentView.body.removeChild(bgdiv);
	if (navigator.userAgent.indexOf("MSIE 6.0") > 0) {
		var bgFrame = parentView.getElementById("bgFrame");
		parentView.body.removeChild(bgFrame);
	}
}

/**
 * 锁住页面(进度条专用)
 * 
 * @param parentView
 *            父页面，也就是需要锁住的页面
 */
function lockScreen_process(parentView) {
	if (!parentView) {
		parentView = document;
	}
	var sWidth;
	var sHeight;
	sWidth = document.body.scrollWidth;
	sHeight = document.body.scrollHeight;
	var bgObj = parentView.createElement("div");
	bgObj.setAttribute('id', 'wfbgDiv_process');
	bgObj.style.position = "absolute";
	bgObj.style.top = "0";
	bgObj.style.background = "#777";
	bgObj.style.filter = "progid:DXImageTransform.Microsoft.Alpha(style=3,opacity=25,finishOpacity=75";
	bgObj.style.opacity = "0.6";
	bgObj.style.left = "0";
	bgObj.style.width = sWidth + "px";
	bgObj.style.height = sHeight + "px";
	bgObj.style.zIndex = "10040";
	parentView.body.appendChild(bgObj);
	if (navigator.userAgent.indexOf("MSIE 6.0") > 0) {
		parentView.body
				.appendChild(createBgFrame("bgFrame_process", parentView));
	}
}

function releaseScreen_process(parentView) {
	if (!parentView) {
		parentView = document;
	}
	var bgdiv = parentView.getElementById("wfbgDiv_process");
	parentView.body.removeChild(bgdiv);
	if (navigator.userAgent.indexOf("MSIE 6.0") > 0) {
		var bgFrame = parentView.getElementById("bgFrame_process");
		parentView.body.removeChild(bgFrame);
	}
}

/**
 * 释放页面(弹出框专用)
 * 
 * @param parentView
 *            父页面，也就是需要释放的页面
 */
function releaseScreen_showPage(parentView) {
	if (!parentView) {
		parentView = document;
	}
	var bgdiv = parentView.getElementById("wfbgDiv_showPage");
	parentView.body.removeChild(bgdiv);
	if (navigator.userAgent.indexOf("MSIE 6.0") > 0) {
		var bgFrame = parentView.getElementById("bgFrame_showPage");
		parentView.body.removeChild(bgFrame);
	}
}

/**
 * 锁住页面(弹出框专用)
 * 
 * @param parentView
 *            父页面，也就是需要锁住的页面
 */
function lockScreen_showPage(parentView) {
	if (!parentView) {
		parentView = document;
	}
	var sWidth;
	var sHeight;
	sWidth = document.body.scrollWidth;
	sHeight = document.body.scrollHeight;
	var bgObj = parentView.createElement("div");
	bgObj.setAttribute('id', 'wfbgDiv_showPage');
	bgObj.style.position = "absolute";
	bgObj.style.top = "0";
	bgObj.style.background = "#777";
	bgObj.style.filter = "progid:DXImageTransform.Microsoft.Alpha(style=3,opacity=25,finishOpacity=75";
	bgObj.style.opacity = "0.6";
	bgObj.style.left = "0";
	bgObj.style.width = sWidth + "px";
	bgObj.style.height = sHeight + "px";
	bgObj.style.zIndex = "10030";
	parentView.body.appendChild(bgObj);
	if (navigator.userAgent.indexOf("MSIE 6.0") > 0) {
		parentView.body.appendChild(createBgFrame("bgFrame_showPage",
				parentView));
	}
}

/**
 * 锁住页面(锁定页面专用)
 * 
 * @param parentView
 *            父页面，也就是需要锁住的页面
 */
function lockScreen_lockPage(parentView) {
	if (!parentView) {
		parentView = document;
	}
	var sWidth;
	var sHeight;
	sWidth = document.body.scrollWidth;
	sHeight = document.body.scrollHeight;
	var bgObj = parentView.createElement("div");
	bgObj.setAttribute('id', 'wfbgDiv_lockPage');
	bgObj.style.position = "absolute";
	bgObj.style.top = "0";
	bgObj.style.background = "#777";
	bgObj.style.filter = "progid:DXImageTransform.Microsoft.Alpha(style=3,opacity=90,finishOpacity=90";
	bgObj.style.opacity = "0.9";
	bgObj.style.left = "0";
	bgObj.style.width = sWidth + "px";
	bgObj.style.height = sHeight + "px";
	bgObj.style.zIndex = "10040";
	parentView.body.appendChild(bgObj);
	if (navigator.userAgent.indexOf("MSIE 6.0") > 0) {
		parentView.body.appendChild(createBgFrame("bgFrame_lockPage",
				parentView));
	}
}

/**
 * 释放页面(锁定界面专用)
 * 
 * @param parentView
 *            父页面，也就是需要释放的页面
 */
function releaseScreen_lockPage(parentView) {
	if (!parentView) {
		parentView = document;
	}
	var bgdiv = parentView.getElementById("wfbgDiv_lockPage");
	parentView.body.removeChild(bgdiv);
	if (navigator.userAgent.indexOf("MSIE 6.0") > 0) {
		var bgFrame = parentView.getElementById("bgFrame_lockPage");
		parentView.body.removeChild(bgFrame);
	}
}

/**
 * 内部方法，修改进度条中图片的长度
 * 
 * @param parentView
 *            父页面
 */
function changeImageSize(parentView) {
	if (waitTime > processTimeOut) { // 进度条超时
		window.clearInterval(processInval);
		changeProcessTitle("操作已超时(" + processTimeOut + "ms),你可以继续等待,或点击关闭.",
				parentView);
		parentView.getElementById("processClose").style.display = "block";
		return;
	}
	waitTime += processWait;
	var tempImage = parentView.getElementById("imageId");
	tempImage.width = tempImage.width + (400 - tempImage.width) / 20; // 永远也走不完的进度条
};

/**
 * 显示一个弹出界面(锁屏用)
 * 
 * @param url
 *            界面地址，可以使jsp，页也可以是action
 * @param params
 *            需要传递的参数
 * @param titleDesc
 *            页面头描述
 * @param width
 *            页面宽度
 * @param height
 *            页面高度
 * @param parentView
 *            页面所在的父页面
 */
function showPage_lockPage(url, params, titleDesc, width, height, parentView,
		callBack) {
	if (!parentView) {
		parentView = document;
	}
	lockScreen_lockPage(parentView);
	var bordercolor = "#336699";// 提示窗口的边框颜色
	var showPageDiv = parentView.createElement("div");
	showPageDiv.setAttribute("id", "showPageDiv");
	showPageDiv.setAttribute("align", "center");
	showPageDiv.style.background = "white";
	showPageDiv.style.border = "1px solid " + bordercolor;
	showPageDiv.style.position = "absolute";
	showPageDiv.style.left = "60%";
	showPageDiv.style.top = "50%";
	showPageDiv.style.font = "12px/1.6em Verdana, Geneva, Arial, Helvetica, sans-serif";
	showPageDiv.style.marginLeft = "-225px";
	showPageDiv.style.marginTop = -75 + parentView.documentElement.scrollTop
			+ "px";
	showPageDiv.style.width = width + "px";
	showPageDiv.style.height = height + "px";
	showPageDiv.style.textAlign = "center";
	showPageDiv.style.lineHeight = "25px";
	showPageDiv.style.zIndex = "10041";
	var title = parentView.createElement("h4");
	title.setAttribute("id", "showPageTitle");
	title.setAttribute("align", "right");
	title.style.margin = "0";
	title.style.padding = "3px";
	title.style.background = bordercolor;
	title.style.filter = "progid:DXImageTransform.Microsoft.Alpha(startX=20, startY=20, finishX=100, finishY=100,style=1,opacity=75,finishOpacity=100);";
	title.style.opacity = "0.75";
	title.style.border = "1px solid " + bordercolor;
	title.style.height = "18px";
	title.style.font = "12px Verdana, Geneva, Arial, Helvetica, sans-serif";
	title.style.color = "white";
	title.style.cursor = "pointer";
	title.title = titleDesc;
	title.innerHTML = "<table border='0' width='100%'><tr><td width='85%' align='left'><b id='showPageTitleDesc'>"
			+ titleDesc
			+ "</b></td><td id='showPageClose' width='15%'  >退出</td></tr></table>";
	var pageBody = parentView.createElement("div");
	pageBody.setAttribute("id", "pageBody");
	pageBody.style.width = width + "px";
	pageBody.style.height = height - 25 + "px";
	pageBody.style.position = "absolute";
	pageBody.style.left = "0px";
	$.post(url, params, function(data) {
		showPageDiv.appendChild(title);
		showPageDiv.appendChild(pageBody);
		parentView.body.appendChild(showPageDiv);
		parentView.getElementById("showPageClose").onclick = function() {
			closeShowPage_lockPage(parentView);
			if (callBack) {
				callBack();
			}
		};
		$("#pageBody").html(data);
	});

}

/**
 * 内部方法，显示全部提示信息
 * 
 * @param parentView
 *            父页面
 */
function showMsgDetail(parentView) {
	if (!parentView) {
		parentView = document;
	}
	parentView.getElementById("msgTxt").innerHTML = message;
	if (parentView.getElementById("msgTxt").offsetHeight < 75) {
		parentView.getElementById("msgDiv").style.height = "100px";
	} else {
		parentView.getElementById("msgDiv").style.height = parentView
				.getElementById("msgTxt").offsetHeight
				+ 25 + "px";
	}
};
/**
 * 针对ie6下下拉框不能被div遮住的问题
 * 
 * @param frameId
 *            frame的id
 * @returns
 */
function createBgFrame(frameId, parentView) {
	var frame = parentView.createElement("iframe");
	frame.setAttribute('id', frameId);
	frame.style.position = "absolute";
	frame.style.top = "0px";
	frame.style.left = "0px";
	frame.style.filter = "progid:DXImageTransform.Microsoft.Alpha(style=0,opacity=0)";
	frame.style.width = screen.width + "px";
	frame.style.height = screen.height + "px";
	return frame;
}

/**
 * 操作授权js对象
 * 
 * @returns {OperAuth}
 */
function OperAuth() {

	this.authSuccess = null;

	this.operType = null;

	var tempAuthSuccess = null;

	this.auth = function() {
		tempAuthSuccess = this.authSuccess;
		$.post("operAuth_isNeedAuth.action", "operType=" + this.operType,
				function(result) {
					if (result == "false") { // 不需要授权
						tempAuthSuccess.call();
					} else { // 弹出授权框
						operAuth();
					}
				});
	};

	/**
	 * 打开授权框
	 */
	function operAuth() {
		var bgDiv = document.createElement("div");
		bgDiv.setAttribute('id', 'auth_bgDiv');
		bgDiv.style.position = "absolute";
		bgDiv.style.top = "0px";
		bgDiv.style.left = "0px";
		bgDiv.style.width = "100%";
		bgDiv.style.height = "100%";
		bgDiv.style.background = "#777";
		bgDiv.style.zIndex = "10032";
		bgDiv.style.filter = "progid:DXImageTransform.Microsoft.Alpha(style=3,opacity=25,finishOpacity=75";
		bgDiv.style.opacity = "0.6";
		document.body.appendChild(bgDiv);
		openAuthPage();
	}

	function openAuthPage() {
		var strTitle = "请输入授权人员用户名和密码";
		var msgw, msgh, bordercolor;
		msgw = 250;// 提示窗口的宽度
		msgh = 110;// 提示窗口的高度
		titleheight = 25; // 提示窗口标题高度
		bordercolor = "#336699";// 提示窗口的边框颜色
		titlecolor = "#99CCFF";// 提示窗口的标题颜色
		var authDiv = document.createElement("div");
		authDiv.setAttribute("id", "authDiv");
		authDiv.setAttribute("align", "center");
		authDiv.style.background = "white";
		authDiv.style.border = "1px solid " + bordercolor;
		authDiv.style.position = "absolute";
		authDiv.style.left = "55%";
		authDiv.style.top = "50%";
		authDiv.style.font = "12px/1.6em Verdana, Geneva, Arial, Helvetica, sans-serif";
		authDiv.style.marginLeft = "-225px";
		authDiv.style.marginTop = -75 + document.documentElement.scrollTop
				+ "px";
		authDiv.style.width = msgw + "px";
		authDiv.style.height = msgh + "px";
		authDiv.style.textAlign = "center";
		authDiv.style.lineHeight = "25px";
		authDiv.style.zIndex = "10033";
		var title = document.createElement("h4");
		title.setAttribute("id", "authTitle");
		title.setAttribute("align", "right");
		title.style.margin = "0";
		title.style.padding = "3px";
		title.style.background = bordercolor;
		title.style.filter = "progid:DXImageTransform.Microsoft.Alpha(startX=20, startY=20, finishX=100, finishY=100,style=1,opacity=75,finishOpacity=100);";
		title.style.opacity = "0.75";
		title.style.border = "1px solid " + bordercolor;
		title.style.height = "18px";
		title.style.font = "12px Verdana, Geneva, Arial, Helvetica, sans-serif";
		title.style.color = "white";
		title.style.cursor = "pointer";
		title.title = "点击关闭";
		title.innerHTML = "<table border='0' width='250px' style='vertical-align:top'><tr style='vertical-align:top'><td align='left'><b>"
				+ strTitle
				+ "</b></td><td align='right'><input type='button' id='closeButton_auth'value='关闭'  style='margin-top:-3px'/></td></tr></table></div>";
		var input = document.createElement("div");
		input.style.marginTop = "6px";
		input.innerHTML = "用户名:<input style='width:120px' type='text' id='authName' name='authName'>"
				+ "<br>"
				+ "密&nbsp; &nbsp;码:<input style='width:120px' type='password' id='authPassword' name='authPassword'>"
				+ "<br>"
				+ "<input type='button' value='授权' style='margin-left:155px' id='authButton'/>";

		document.body.appendChild(authDiv);
		authDiv.appendChild(title);
		authDiv.appendChild(input);
		document.getElementById("authDiv").onkeyup = function() {
			authKeyDown();
		};
		document.getElementById("closeButton_auth").onclick = function() {
			closeOperAuth();
		};
		document.getElementById("authButton").onclick = function() {
			document.getElementById("authButton").disabled = "true";
			var userName = document.getElementById("authName").value;
			var password = document.getElementById("authPassword").value;
			if (userName == null || userName.length == 0) {
				wfAlert("请输入用户名");
				document.getElementById("authButton").disabled = "";
				return;
			}
			if (password == null || password.length == 0) {
				wfAlert("请输入密码");
				document.getElementById("authButton").disabled = "";
				return;
			}
			$.post("operAuth_auth.action", "userName=" + userName
					+ "&password=" + password, function(result) {
				if (result == "true") {
					document.getElementById("authButton").disabled = "";
					closeOperAuth();
					tempAuthSuccess.call();
				} else {
					wfAlert(result);
					document.getElementById("authButton").disabled = "";
				}
			});
		};
	}

	function closeOperAuth() {
		var auth_bgDiv = document.getElementById("auth_bgDiv");
		var authDiv = document.getElementById("authDiv");
		document.body.removeChild(authDiv);
		document.body.removeChild(auth_bgDiv);
	}
	
	function authKeyDown(evt){
		if(evt==null){
			evt=window.event;
		}
		if(evt.keyCode==13){
		document.getElementById("authButton").click();
		}
	}

}