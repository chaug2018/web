function ImageShow() {
	var images = null; // 存放图片路径的数组
	var curIndex = 0; // 当前图片下标
	var scale = 1.2; // 缩放比例
	var scaleTime = 0; // 缩放次数负数代表缩小
	var sourceWidth = 0; // 图片原始宽度
	var sourceHeight = 0; // 图片原始高度
	var imageArea = null;

	this.init = function(images_, width_, height_) {
		images = images_;
		sourceWidth = width_;
		sourceHeight = height_;
		imageArea = document.getElementById("imgArea");
		imageArea.onload = justfySize; // 图片加载完了后就调整大小
		imageArea.onerror = loadError;// 加载出错
		setBgDiv();
		createToolBar();
		if (document.all) {
			setStyle4Ie();
		}
		showImage();
		initCache();
	};

	/**
	 * 设置控件背景大小
	 */
	function setBgDiv() {
		var baDiv = document.getElementById("bgDiv");
		baDiv.style.width = sourceWidth + "px";
		baDiv.style.height = sourceHeight + "px";
	}
	;

	/**
	 * 创建工具栏
	 */
	function createToolBar() {
		var elements = document.getElementById("leftPage").children;
		for ( var i = 0; i < elements.length; i++) {
			if (document.all) {
				elements[i].style.filter = "alpha(opacity=0)";
			} else {
				elements[i].style.opacity = "0";
			}
		}
		elements = document.getElementById("rightPage").children;
		for ( var i = 0; i < elements.length; i++) {
			if (document.all) {
				elements[i].style.filter = "alpha(opacity=0)";
			} else {
				elements[i].style.opacity = "0";
			}
		}
		var leftPage = document.getElementById("leftPage");
		leftPage.oncontextmenu = "return false;";
		leftPage.onmousewheel = mouseWheel;
		leftPage.onmouseover = showPreTip;
		leftPage.onmouseout = hidPreTip;
		leftPage.onclick = preImage;
		var rightPage = document.getElementById("rightPage");
		rightPage.onmousewheel = mouseWheel;
		rightPage.onmouseover = showNextTip;
		rightPage.onmouseout = hidNextTip;
		rightPage.onclick = nextImage;
		document.getElementById("smallerButton").onclick = clickSmaller;
		document.getElementById("biggerButton").onclick = clickBigger;
		document.getElementById("clockRoButton").onclick = clockRo;
		document.getElementById("unClockRoButton").onclick = unClockRo;

	}

	function setStyle4Ie() {// ie不太遵循w3c标准，老有各种问题，所以要特殊设置一下
		document.getElementById("bgDiv").style.width = document
				.getElementById("bgDiv").scrollWidth
				+ "px";
		document.getElementById("leftPage").style.filter = "alpha(opacity=0)";
		document.getElementById("rightPage").style.filter = "alpha(opacity=0)";
		document.getElementById("leftPage").style.background = "red";
		document.getElementById("rightPage").style.background = "red";
	}

	/**
	 * 图片预加载
	 */
	function initCache() {
		if (images != null && images.length > 1) {
			for ( var i = 1; i < images.length; i++) {
				var image = new Image();
				image.src = images[i];
			}
		}
	}

	function mouseWheel(evt) {
		if (evt == null) {
			evt = window.event;
		}
		var num = evt.wheelDelta;
		if (num == 0 || num == null) {
			num = evt.detail;
		}
		if (num > 0) {
			bigger();
		} else if (num < 0) {
			smaller();
		}
	}

	/**
	 * 显示下一页的提示
	 */
	function showNextTip(evt) {
		if (evt == null) {
			evt = window.event;
		}
		var element = evt.srcElement;
		if (element.children.length > 1) { // rigthPage
			elements = element.children;
			for ( var i = 0; i < elements.length; i++) {
				if (document.all) {
					elements[i].style.filter = "alpha(opacity=60)";
				} else {
					elements[i].style.opacity = "0.6";
				}
			}
			setTimeout(function() {
				evt.srcElement.title = "点击显示下一张图片";
			}, 300);
		} else if (element.children.length > 0) {// rightArr

		} else { // rightArrImg
			if (document.all) {
				element.parentElement.style.filter = "alpha(opacity=90)";
			} else {
				element.parentElement.style.opacity = "0.9";
			}
		}
	}
	/**
	 * 显示上一页的提示
	 */
	function showPreTip(evt) {
		if (evt == null) {
			evt = window.event;
		}
		var element = evt.srcElement;
		if (element.children.length > 1) {
			elements = element.children;
			for ( var i = 0; i < elements.length; i++) {
				if (document.all) {
					elements[i].style.filter = "alpha(opacity=60)";
				} else {
					elements[i].style.opacity = "0.6";
				}
			}
			window.setTimeout(function() {
				evt.srcElement.title = "点击显示上一张图片";
			}, 300);
		} else if (element.children.length > 0) {// rightArr

		} else {
			if (document.all) {
				element.parentElement.style.filter = "alpha(opacity=90)";
			} else {
				element.parentElement.style.opacity = "0.9";
			}
		}
	}

	/**
	 * 隐藏下一页提示
	 */
	function hidNextTip(evt) {
		if (evt == null) {
			evt = window.event;
		}
		var element = evt.srcElement;
		if (element.children.length > 1) {
			elements = element.children;
			for ( var i = 0; i < elements.length; i++) {
				if (document.all) {
					elements[i].style.filter = "alpha(opacity=0)";
				} else {
					elements[i].style.opacity = "0";
				}
			}
			element.title = "";
		}
	}
	/**
	 * 隐藏上一页提示
	 */
	function hidPreTip(evt) {
		if (evt == null) {
			evt = window.event;
		}
		var element = evt.srcElement;
		if (element.children.length > 1) {
			elements = element.children;
			for ( var i = 0; i < elements.length; i++) {
				if (document.all) {
					elements[i].style.filter = "alpha(opacity=0)";
				} else {
					elements[i].style.opacity = "0";
				}
			}
			element.title = "";
		}
	}

	/**
	 * 显示下一张图片
	 */
	function nextImage() {
		if (curIndex == images.length - 1) {
			alert("已经是最后一张了");
			return;
		}
		curIndex++;
		resetSize();
		showImage();
	}
	;

	/**
	 * 显示上一张图片
	 */
	function preImage() {
		if (curIndex == 0) {
			alert("已经是第一张了");
			return;
		}
		curIndex--;
		resetSize();
		showImage();

	}
	;

	/**
	 * 点击缩小按钮
	 */
	function clickSmaller(evt) {
		if (evt == null) {
			evt = window.event;
		}
		smaller();
		if (evt.stopPropagation) {
			evt.stopPropagation();
		} else {
			evt.cancelBubble = true;
		}
	}

	/**
	 * 点击放大按钮
	 */
	function clickBigger(evt) {
		if (evt == null) {
			evt = window.event;
		}
		bigger();
		if (evt.stopPropagation) {
			evt.stopPropagation();
		} else {
			evt.cancelBubble = true;
		}
	}

	/**
	 * 放大图片
	 */
	function bigger() {
		scaleTime++;
		changeSize();
	}

	/**
	 * 缩小图片
	 */
	function smaller() {
		scaleTime--;
		changeSize();
	}
	;

	/**
	 * 还原图片大小
	 */
	function resetSize() {
		number=0;
		scaleTime = 0;
		changeSize();
	}

	/**
	 * 显示图片
	 */
	function showImage() {
		if (images != null && images.length != 0) {
			showLoading();
			imageArea.src = images[curIndex];
		}
	}
	;

	function showLoading() {
		document.getElementById("loading").style.display = "block";
	}

	function loadingFinish() {
		document.getElementById("loading").style.display = "none";
	}

	/**
	 * 根据图片长宽比例，动态调整大小
	 */
	function justfySize() {
		var width = imageArea.width;
		var height = imageArea.height;
		var a = width / sourceWidth;
		var b = height / sourceHeight;
		if (width >= height) {
			imageArea.style.height = sourceHeight + "px";
			imageArea.style.width = width / b;
			sourceWidth = width / b;
		} else {
			imageArea.style.width = sourceWidth + "px";
			imageArea.style.height = height / a + "px";
			sourceHeight = height / a;
		}
		loadingFinish();
	}

	function loadError() {
		alert("无法加载该图片");
		loadingFinish();
	}

	/**
	 * 图片放大缩小大小
	 */
	function changeSize() {
		var newWidth = sourceWidth;
		var newHeight = sourceHeight;
		for ( var i = scaleTime; i != 0;) {
			if (i > 0) {
				newWidth = newWidth * scale;
				newHeight = newHeight * scale;
				i--;
			} else {
				newWidth = newWidth / scale;
				newHeight = newHeight / scale;
				i++;
			}
		}
		imageArea.style.width = newWidth + "px";
		imageArea.style.height = newHeight + "px";
	}
	
	var number=0;
	/**
	 * 顺时针旋转
	 */
	function clockRo(evt){
		number=number+1;
		if(number==4){
			number=0;
		}
		rotate();
		if (evt == null) {
			evt = window.event;
		}
		if (evt.stopPropagation) {
			evt.stopPropagation();
		} else {
			evt.cancelBubble = true;
		}
	}
	
	/**
	 * 逆时针旋转
	 */
  function unClockRo(evt){
	  number=number-1;
		if(number==-1){
			number=3;
		}
		rotate();
		if (evt == null) {
			evt = window.event;
		}
		if (evt.stopPropagation) {
			evt.stopPropagation();
		} else {
			evt.cancelBubble = true;
		}
	}
	
	function rotate(){
		imageArea.style.filter="progid:DXImageTransform.Microsoft.BasicImage(rotation="+number+")";
		scaleTime = 0;
		changeSize();
		justfySize();
	}

};

