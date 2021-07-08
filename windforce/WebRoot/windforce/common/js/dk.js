/**
 * 桌面子系统相关js方法 银之杰科技股份有限公司 2012-1-24
 * 
 * @author 陈林江
 * @version 1.0.0
 */
var showNotice = true; // 是否在离开界面时弹出提示
var windowWidth = 1279; // 浏览器宽度
var windowHeight = 685; // 浏览器高度
var topSize = 35; // logo栏高度
var leftSize_min = 12;
var leftSize_max = 190;
var leftSize = leftSize_max; // 左侧菜单栏高度
function getStyle(o, k) {
	var _k = k.replace(/-[a-z]/g, function(a) {
		return a.substr(1, 1).toUpperCase();
	});
	var v = o.style[_k];
	if (!v) {
		if (document.defaultView && document.defaultView.getComputedStyle) {
			var c = document.defaultView.getComputedStyle(o, null);
			v = c ? c.getPropertyValue(_k) : null;
		} else if (o.currentStyle) {
			v = o.currentStyle[_k];
		}
	}
	return v;
}

function hideLeft() {
	var objLeft = document.getElementById("left_loginov");
	var objLogin = document.getElementById("left_login");
	var currLeft = parseInt(getStyle(objLeft, "left"));
	if (currLeft > -187) {
		objLeft.style.left = (currLeft - 8) + "px";
		objLogin.style.left = (currLeft - 8) + "px";
		window.setTimeout("hideLeft()", 5);
	}

}

function showLeft() {
	var objLeft = document.getElementById("left_loginov");
	var objLogin = document.getElementById("left_login");
	var currLeft = parseInt(getStyle(objLeft, "left"));
	if (currLeft < 0) {
		objLeft.style.left = (currLeft + 8) + "px";
		objLogin.style.left = (currLeft + 8) + "px";
		window.setTimeout("showLeft()", 5);
	}
}

function setMenu() {
	var imgsrc = document.getElementById("extendImg").src;
	// 收缩菜单栏
	if (imgsrc.substr(imgsrc.length - 13) == "dk_toLeft.jpg") {
		leftSize = leftSize_min;
		document.getElementById("extendImg").src = imgsrc.replace(
				"dk_toLeft.jpg", "dk_toRight.png");
		document.getElementById("extendImg").style.width = 17 + "px";
		document.getElementById("extendImg").style.height = 88 + "px";
		document.getElementById("extendImg").style.paddingLeft = 12 + "px";
		hideLeft();
		// 右侧拉宽
		document.getElementById("workFrame").style.width = getWindowWidth()
				- getLeftSize() + "px";
		document.getElementById("workFrame").style.height = getWindowHeight()
				- getTopSize() + "px";
		document.getElementById("workFrame").style.paddingLeft = leftSize_min
				+ "px";
		// 展开菜单栏
	} else {
		leftSize = leftSize_max;
		document.getElementById("extendImg").src = imgsrc.replace(
				"dk_toRight.png", "dk_toLeft.jpg");
		document.getElementById("extendImg").style.width = 9 + "px";
		document.getElementById("extendImg").style.height = 104 + "px";
		document.getElementById("extendImg").style.paddingLeft = 12 + "px";

		showLeft();

		document.getElementById("workFrame").style.paddingLeft = getLeftSize()
				+ "px";
		document.getElementById("workFrame").style.height = getWindowHeight()
				- getTopSize() + "px";
		document.getElementById("workFrame").style.width = (getWindowWidth() - getLeftSize())
				+ "px";

	}
}
$(document)
		.ready(
				function() {
					bindFirstMenu();
					var previewCacheSpace = WFCache
							.getCacheSpace("WF_CORE_CACHE_SPACE");
					var defaultCache = WFCache.getPageCache(previewCacheSpace);
					var isBlockTaskMenu = defaultCache
							.get("IS_BLOCK_TASK_MENU");
					document.getElementById("left_login").style.height = getWindowHeight()
							- getTopSize() + "px";
					if (isBlockTaskMenu == "unblock") {
						document.getElementById("login_cai").style.height = (getWindowHeight()
								- getTopSize() - 15)
								* 0.65 + "px";
						document.getElementById("login_ren").style.height = (getWindowHeight()
								- getTopSize() - 15)
								* 0.3 + "px";
						autoSearch(); // 界面打开时就进行一次任务栏的刷新
						window.setInterval(autoSearch, 10000);
					} else {
						document.getElementById("login_cai").style.height = (getWindowHeight()
								- getTopSize() - 30)
								+ "px";
						document.getElementById("login_ren").style.display = "none";
					}
					document.getElementById("nov_nr").style.height = getWindowHeight()
							+ "px";

				});
function bindFirstMenu() {
	$("#li.firstMenu").unbind("click");
	$("li.firstMenu")
			.click(
					function() {
						if (typeof ($(this).next("li").get(0)) != "undefined"
								&& $(this).next("li:first").attr("class") != "firstMenu") {
							$(this).next("li:first").slideToggle();

						} else {
							initFrame($(this).attr("name"));
							workFrame.location.href = $(this).attr("id");
						}

					});
	$("#li.cc02 a").unbind("click");
	$("li.cc02 a").click(function() {
		var currClass = $(this).parent().attr("class");
		// 为了控制点击子节点时不能触发父节点点击事件
		$(this).parent().find("ul:first").slideToggle();
		if (currClass == "cc02") {
			$(this).parent().attr("class", "cc01");
		} else if (currClass == "cc01") {
			$(this).parent().attr("class", "cc02");
		} else if (currClass == "cc03") {
			$(this).parent().attr("class", "cc04");
		} else if (currClass == "cc04") {
			$(this).parent().attr("class", "cc03");
		}
	});
}

/**
 * 点击左侧的可跳转的模块时触发的事件
 */
function initFrame(moduleName) {
	var tempModuleName = moduleName;
	if (moduleName.length > 10) {
		tempModuleName = moduleName.substring(0, 6) + "...";
	}
	document.getElementById("moduleDesc").innerText = "当前模块:  "
			+ tempModuleName;
	document.getElementById("moduleDesc").title = moduleName;
	// startProcess("正在初始化界面,请稍等...");
}
// 双击任务栏
function dbClick() {
	var login_ren = document.getElementById("login_ren");
	var login_cai = document.getElementById("login_cai");
	var taskul = document.getElementById("taskul");

	if (login_ren.offsetHeight < 250) {
		login_ren.style.height = (getWindowHeight() - getTopSize() - 15) * 0.40
				+ "px";
		login_cai.style.height = (getWindowHeight() - getTopSize() - 15) * 0.55
				+ "px";
		taskul.style.height = (getWindowHeight() - getTopSize() - 15) * 0.40
				- 39 + "px";
	} else {
		login_ren.style.height = (getWindowHeight() - getTopSize() - 15) * 0.20
				+ "px";
		login_cai.style.height = (getWindowHeight() - getTopSize() - 15) * 0.75
				+ "px";
		taskul.style.height = (getWindowHeight() - getTopSize() - 15) * 0.20
				- 39 + "px";
	}
}

var isKeyDown = false;
var preY = 0;

function mouseDown() {
	// 若为IE6，则不执行该代码
	if (!(($.browser.msie) && ($.browser.version == "6.0"))) {
		var evt = window.event;
		if (evt.button != 2) {
			return;
		}
		var midLine = document.getElementById("midLine");
		midLine.style.cursor = "pointer";
		preY = evt.screenY;
		isKeyDown = true;
	}
}

function mouseOver() {
	var midLine = document.getElementById("midLine");
	midLine.style.cursor = "n-resize";
}
function mouseMove() {
	var evt = window.event;
	if (!isKeyDown) {
		return;
	}
	if (evt.button != 2) {
		return;
	}
	var login_ren = document.getElementById("login_ren");
	var login_cai = document.getElementById("login_cai");
	var taskul = document.getElementById("taskul");
	var increY = evt.screenY - preY; // 纵坐标增长量
	if (login_ren.offsetHeight > 400 && increY < 0) {
		return;
	}
	if (login_ren.offsetHeight < 150 && increY > 0) {
		return;
	}
	login_ren.style.height = login_ren.offsetHeight - increY + "px";
	taskul.style.height = taskul.offsetHeight - increY + "px";
	login_cai.style.height = 610 - login_ren.offsetHeight + increY + "px";
	preY = evt.screenY;
	if (evt.stopPropagation) {
		evt.stopPropagation();
	} else {
		evt.cancelBubble = true;
	}
}
function mouseOut() {
	var midLine = document.getElementById("midLine");
	midLine.style.cursor = "pointer";
	isKeyDown = false;
}
function mouseUp() {
	var midLine = document.getElementById("midLine");
	midLine.style.cursor = "n-resize";
	isKeyDown = false;
}

// 自动检索导航信息
function autoSearch(url) {
	var temp = document.getElementById("autoComplete");
	if (temp == null) {
		return;
	}
	var currValue = temp.value;
	if (currValue == null || currValue == "" || currValue == "搜索交易代码") {
		$.getJSON("navigationAutoSearchAction.action?timestamp="+ new Date().getTime(), "hostKey=", function(
				data) {
			$("#taskul").html("");
			$("#taskul").html(data["taskView"]);
		});
	} else {

		$.getJSON("navigationAutoSearchAction.action?timestamp="+ new Date().getTime(), "hostKey=" + currValue,
				function(data) {
					$("#taskul").html("");
					$("#taskul").html(data["taskView"]);
				});
	}

}
// 自动检索导航信息
function autoSearchMenu(url) {
	var currValue = document.getElementById("autoComplete").value;

	if (currValue == null || currValue == "" || currValue == "搜索交易代码") {
		$.getJSON(url, "hostKey=", function(data) {
			$("#autoSearchParent").html("");
			$("#autoSearchParent").html(data["navigation"]);
			bindFirstMenu();
		});
	} else {

		$.getJSON(url, "hostKey=" + currValue, function(data) {
			$("#autoSearchParent").html("");
			$("#autoSearchParent").html(data["navigation"]);
			bindFirstMenu();
		});
	}

}

function lockIndexPage() {
	showPage_lockPage("windforce/dk/freePage.jsp", "", "请输入密码进行解锁", 200, 60,
			null, function() {
				WFUnload.clear();
				window.location.href = "j_spring_security_logout";
			});
}

function changeDefaultPassword(sid) {
	showPage_lockPage("windforce/dk/modifyDefualtPwd.jsp?userSid=" + sid, "",
			"你的密码为默认密码，请修改!", 260, 132, null, function() {
				WFUnload.clear();
				window.location.href = "j_spring_security_logout";
			});
}

function reLoad() {
	showNotice = false;// 通过调用内部方法进行的界面刷新不弹出提示框
	document.location.href = "login.action";
}
// 弹出屏蔽层
function showd(url, width, height) {
	showDialogInfo(url, width, height);
}
// 刷新右侧iframe
function refreshRight(url) {
	workFrame.location.href = url;
}
// 敲回车搜索
function onFunctionKey(evt) {
	evt = (evt) ? evt : ((window.event) ? window.event : "");
	keyCode = evt.keyCode ? evt.keyCode
			: (evt.which ? evt.which : evt.charCode);
	if (keyCode == 13) {
		searchBlur();
		autoSearchMenu('navigationAutoSearchAction.action');
	}
}

// 带参数的弹出屏蔽层
function showWindowForUrl(action, taskName, proDefKey, curPage, width, height) {
	showWindowDialogForUrl(action, taskName, proDefKey, curPage, width, height);
}
function onscrollTo() {

	window.scrollTo(0, 85);
}

function getWindowWidth() {
	return window.screen.availWidth > 1170 ? window.screen.availWidth-10 : 1170;
}
function getWindowHeight() {
	return window.screen.availHeight - 120;
}
function getTopSize() {
	return topSize;
}
function getLeftSize() {
	return leftSize;
}