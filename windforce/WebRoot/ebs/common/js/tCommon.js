/**
 * $.tCommon函数
 */
(function ($) {
	$.tCommon = $.tCommon||{};
	$.extend($.tCommon,{
		/**
		 * 输入框金额验证
		 */
		numberValidate:function(value){
			var patrn =/^([1-9][0-9]{0,12})|([0]{1})|([0-9]{0,12}).[0-9]{0-3}$/ ;
			if(!patrn.exec(value)){
				return false;
			}else{
				return true;
			}
		}
		
	
	});
})(jQuery);