/**
 * 页面元素检查工具类 银之杰科技股份有限公司
 * 
 * @Date 2012-1-24
 * @author 陈皇
 * @version 1.0.0
 */

/**
 * 判断该字符串是否为空
 * 
 * @param str
 * @returns {Boolean}
 */
function isNull(str) {
	var trimStr = $.trim(str);
	if (trimStr.length == 0) {
		return true;
	} else {
		for ( var i = 0; i < trimStr.length; i++) {
			if (trimStr.charAt(i) != " ")
				return false;
		}
		return true;
	}
}

/**
 * 判断该字符串是否为英文字符
 * 
 * @param str
 * @returns {Boolean}
 */
function isCharacter(str) {
	if (isNull(str)) {
		return false;
	} else {
		for ( var i = 0; i < str.length; i++) {
			var u = str.charCodeAt(i);
			if ((u > 0) && (u < 128))
				continue;
			else
				return false;
		}
	}
	return true;
}

/**
 * 判断该字符是否为字母
 * 
 * @param str
 * @returns {Boolean}
 */
function isAlpha(str) {
	if (isNull(str)) {
		return false;
	} else {
		for ( var i = 0; i < str.length; i++) {
			var mychar = str.charAt(i);
			if ((mychar < 'a' || mychar > 'z')
					&& (mychar < 'A' || mychar > 'Z'))
				return false;
		}
	}
	return true;
}

/**
 * 判断该字符串是否为数字
 * 
 * @param str
 * @returns {Boolean}
 */
function isDigits(str) {
	if (isNull(str)) {
		return false;
	} else {
		for ( var i = 0; i < str.length; i++) {
			var mychar = str.charAt(i);
			if (mychar < "0" || mychar > "9")
				return false;
		}
	}
	return true;
}

/**
 * 判断是否为中文
 * 
 * @param str
 * @returns {Boolean}
 */
function isChinese(str) {
	if (isNull(str)) {
		return false;
	}
	var regex = /[\u4e00-\u9fa5]/;
	for ( var i = 0; i < str.length; i++) {
		if (!regex.test(str.charAt(i))) {
			return false;
		}
	}
	return true;
}

/**
 * 判断是否为邮箱地址
 * 
 * @param str
 * @returns {Boolean}
 */
function isEmail(str){
	var emailRegex = /^(\w+)([\-+.][\w]+)*@(\w[\-\w]*\.){1,5}([A-Za-z]){2,4}$/;
    return emailRegex.test(str);
}

/**
 * 判断是否为字符和数字
 * 
 * @param str
 * @returns {Boolean}
 */
function isAlphaAndDigits(str){
	if(isNull(str)) {
		return false;
	} else {
		var addressRegex = /[a-zA-Z\d]/;
	    for (var i = 0; i < str.length; i++) {
	        if (!addressRegex.test(str.charAt(i))) {
	            return false;
	        }
	    }
	    return true;
    }
}

/**
 * 验证是否为通用名称：中文、字母、数字、-、_、空白、(、)、[、]
 * 
 * @param str
 * @returns {Boolean}
 */
function isGeneralName(str){
	if(isNull(str)) {
		return false;
	} else {
		var nameRegex = /[ \(\)\[\]_\-a-zA-Z\u4e00-\u9fa5\d]/;
	    for (var i = 0; i < str.length; i++) {
	        if (!nameRegex.test(str.charAt(i))) {
	            return false;
	        }
	    }
	    return true;
    }
}

/**
 * 验证是否为通用密码：字母、数字、.、_、-
 * 
 * @param str
 * @returns {Boolean}
 */
function isGeneralPwd(str){
	if(isNull(str)) {
		return false;
	} else {
		var pwdRegex = /[\._\-a-zA-Z\d]/;
	    for (var i = 0; i < str.length; i++) {
	        if (!pwdRegex.test(str.charAt(i))) {
	            return false;
	        }
	    }
	    return true;
    }
}