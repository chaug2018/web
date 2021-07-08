/* Chinese initialisation for the jQuery UI date picker plugin. */
/* Written by Cloudream (cloudream@gmail.com). */
$.datepicker.regional['zh-CN'] = {
	closeText : '关闭',
	prevText : '&#x3c;上月',
	nextText : '下月&#x3e;',
	currentText : '今天',
	monthNames : [ '一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月' ],
	monthNamesShort : [ '一', '二', '三', '四', '五', '六', '七', '八', '九', '十', '十一', '十二' ],
	dayNames : [ '星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六' ],
	dayNamesShort : [ '周日', '周一', '周二', '周三', '周四', '周五', '周六' ],
	dayNamesMin : [ '日', '一', '二', '三', '四', '五', '六' ],
	weekHeader : '周',
	dateFormat : 'yy-mm-dd',
	firstDay : 1,
	isRTL : false,
	showMonthAfterYear : true,
	yearSuffix : '年'
};
$.datepicker.setDefaults($.datepicker.regional['zh-CN']);

// 定义弹出提示信息方式，默认alert形式，若不同系统有自己的实现方式，可通过实现自定义函数window.customPrompt来实现
window.promptMsg = function(msg, type) {
	if (window.customPrompt) {
		window.customPrompt(msg, type);
	} else {
		alert(msg);
	}
};
// 提供修改遮罩层实现的接口(声明window.customShade，并必须拥有show,hide方法)
window.shade = {
	show : function(){
		if (window.customShade && window.customShade.show && window.customShade.hide) {
			window.customShade.show();
		} else {
			$("#fade").show();
			$("#fade").height(document.body.scrollHeight || document.documentElement.scrollHeight);
		}
	},
	hide : function(){
		if (window.customShade && window.customShade.show && window.customShade.hide) {
			window.customShade.hide();
		} else {
			$("#fade").hide();
		}
	}
};

// 定义各种格式，对数据进行校验，或转换数据用于页面显示
window.pageFormat = {
	// 用于扩展各种校验格式或转换数据
	extend : function(name, obj) {
		if (typeof obj == "string") { //传入字符串时，将新添的格式化对象属性引用于字符串指向的已有格式
			var temp = {};
			if (this.formats[obj]) {
				temp.validate = this.formats[obj].validate;
				temp.showValue = this.formats[obj].showValue;
				temp.format = this.formats[obj].format;
			}
			obj = temp;
		}
		this.formats[name] = obj;
		return obj;
	},
	// 校验，验证指定值是否符合对应格式要求
	validate : function(requirement, formatName, pattern) {
		if (formatName == undefined) {
			formatName = $(requirement).attr("format");
		}
		if (pattern == undefined) {
			pattern = $(requirement).attr("pattern");
		}
		if (this.formats[formatName] && this.formats[formatName].validate) {
			var errormsg = $(requirement).attr("errormsg");
			if (!errormsg) {
				errormsg = $(requirement).parent().children("span").text() + "格式错误";
			}
			return this.formats[formatName].validate(requirement, pattern, errormsg);
		}
		return true;
	},
	// 取值，对指定值按照对应格式进行处理后返回
	showValue : function(dataObj, requirement, formatName, pattern) {
		if (formatName == undefined) {
			formatName = $(requirement).attr("format");
		}
		if (pattern == undefined) {
			pattern = $(requirement).attr("pattern");
		}
		if (formatName && this.formats[formatName] && this.formats[formatName].showValue) {
			return this.formats[formatName].showValue(dataObj, $(requirement).attr("ref"), pattern);
		}
		var sValue = eval("dataObj." + $(requirement).attr("ref"));
		if(typeof sValue == "undefined"){
			sValue = "";
		}
		return sValue;
	},
	// 按格式初始化，对指定html对象照对应格式进行初始化处理
	format : function(requirement, formatName, pattern) {
		if (formatName == undefined) {
			formatName = $(requirement).attr("format");
		}
		if (pattern == undefined) {
			pattern = $(requirement).attr("pattern");
		}
		if (this.formats[formatName] && this.formats[formatName].format) {
			return this.formats[formatName].format(requirement, pattern);
		}
		return true;
	},
	formats : {
		regular : { // 正则表达式
			/**
			 * 校验，验证指定值是否符合对应格式要求
			 * @param obj			对应输入要素
			 * @param pattern		匹配模式
			 * @param errormsg	错误提示信息
			 */
			validate : function(obj, pattern, errormsg) {
				var regexp = null;
				try{
					regexp = eval(pattern);
					if(typeof regexp.test != "function"){
						promptMsg("正则表达式书写错误："+pattern,"format_error");
						return false;
					}
				}catch(e){
					promptMsg("正则表达式书写错误："+pattern,"format_error");
					console.log(e);
					return false;
				}
				if (!regexp.test(obj.value)) {
					promptMsg(errormsg,"format_error");
					return false;
				}
				return true;
			}
		},
		math : { // 数学运算
			/**
			 * 取值，对指定值按照对应格式进行处理后返回
			 * @param obj			数据对象
			 * @param ref			引用字符串
			 * @param pattern		匹配模式
			 */
			showValue : function(dataObj, ref, pattern) {
				var source = ref.replace(/([\w]+)/g, "dataObj.${1}");
				try{
					source = eval(source);
				}catch(e){
					console.log(e);
				}
				return source;
			}
		},
		javascript : { // 纯粹的javascript代码
			/**
			 * 校验，验证指定值是否符合对应格式要求
			 * @param obj			对应输入要素
			 * @param pattern		匹配模式
			 * @param errormsg	错误提示信息
			 */
			validate : function(obj, pattern, errormsg) {
				try{
					if(eval(pattern)){
						return true;
					}
				}catch(e){
					console.log(e);
				}
				promptMsg(errormsg,"format_error");
				return false;
			},
			/**
			 * 取值，对指定值按照对应格式进行处理后返回
			 * @param obj			数据对象
			 * @param ref			引用字符串
			 * @param pattern		匹配模式
			 */
			showValue : function(dataObj, ref, pattern) {
				var source = null;
				try{
					source = eval(ref);
				}catch(e){
					source = ref;
					console.log(e);
				}
				return source;
			},
			/**
			 * 格式化，对指定html对象照对应格式进行初始化处理
			 * @param obj			对应输入要素
			 * @param pattern		匹配模式
			 */
			format : function(obj, pattern) {
				eval(obj);
			}
		},
		number : { // 数字
			validate : function(obj, pattern, errormsg) {
				var regexp = /^\d+\.?\d*$/; // 默认浮点数
				if (pattern == "int") { // 整数
					regexp = /^\d+$/;
				}
				if (!regexp.test(obj.value)) {
					promptMsg(errormsg,"format_error");
					return false;
				}
				return true;
			}
		},
		date : { // 日期，例: "yy-mm-dd" 对应 "2012-12-21"，"y-mm-dd" 对应 "12-12-21"
			validate : function(obj, pattern, errormsg) {
				if (!pattern) {
					pattern = "yy-mm-dd";
				}
				if (pattern.replace(/y/g, "01").length != obj.value.length) {
					promptMsg(errormsg,"format_error");
					return false;
				}
				try {
					$.datepicker.parseDate(pattern, obj.value);
				} catch (e) {
					promptMsg(errormsg,"format_error");
					return false;
				}
				return true;
			},
			format : function(dateInput, pattern) {
				// 为日期类型输入要素绑定jqueryUI日期控件
				if (!pattern) {
					pattern = "yy-mm-dd";
				}
				var option = {
					dateFormat : pattern,
					changeYear : true
				};
				$(dateInput).datepicker(option);
			}
		},
		combox : { // 绑定自动完成功能的组合下拉框，需要以select下拉框为来源
			format : function(selObj, pattern) {
				if (pattern == "text") {
					$(selObj).combobox({
						hasTriangle : false
					});
				} else {
					$(selObj).combobox({
						inputWidth : "85%"
					});
				}
			}
		}
	}
};