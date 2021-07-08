(function($) {
	var enterlist = {
		create : function(last$EleOrFun) {
			return new $.enterlist.EnterList(last$EleOrFun);
		},

		EnterList : function(last$EleOrFun) {
			this.list = new Array();
			this.last$EleOrFun = last$EleOrFun;
			this.add = function($element) {
				this.list[this.list.length] = $element;
				var self = this;
				this.keydownfunc = function(event) {
					var startSetFocus = false;
					if (!event.shiftKey & (13 == event.keyCode || 9 == event.keyCode)) {
						// var list = arguments.callee.enterList.list;
						var list = self.list;
						for ( var i = 0; i < list.length; i++) {
							if (!startSetFocus) {
								if ($element == list[i]) {
									startSetFocus = true;
								}
							} else {
								if (list[i].prop("disabled") == true || list[i].prop("disabled") == "disabled"
										|| list[i].is(":hidden")) {
									continue;
								} else {
									list[i].focus();
									break;
								}
							}

						}
						// 如果$element在队列中并且列表中没有可以设置焦点的控件，考虑找最后一个回车跳转到的控件或执行的方法
						if (startSetFocus & i == list.length) {
							// var last$EleOrFun =
							// arguments.callee.enterList.last$EleOrFun;
							var last$EleOrFun = self.last$EleOrFun;
							if (last$EleOrFun) {
								if (typeof last$EleOrFun == "function") {
									last$EleOrFun.call($element[0], list);
								} else {
									if (last$EleOrFun.prop("disabled") == true
											|| last$EleOrFun.prop("disabled") == "disabled"
											|| last$EleOrFun.is(":hidden")) {
										$element.blur();
									} else {
										 last$EleOrFun.focus();
									}
								}
							} else {// 如果没有可设定焦点的对象，并且没有可跳转的对象或函数则让当前对象失去焦点
								$element.blur();
							}
						}
						return false;
					} else if (event.shiftKey & (13 == event.keyCode || 9 == event.keyCode)) {
						// var list = arguments.callee.enterList.list;
						list = self.list;
						for ( var i = list.length - 1; i >= 0; i--) {
							if (!startSetFocus) {
								if ($element == list[i]) {
									startSetFocus = true;
								}
							} else {
								if (list[i].prop("disabled") == true || list[i].prop("disabled") == "disabled"
										|| list[i].is(":hidden")) {
									continue;
								} else {
									 list[i].focus();
									break;
								}
							}

						}
						return false;
					}
				};
				// this.keydownfunc.enterList = this;
				$element.keydown(this.keydownfunc);
			};
			this.remove = function($element) {
				for ( var i = this.list.length - 1; i >= 0; i--) {
					if ($element == this.list[i]) {
						this.list.splice(i, 1);
					}
				}
			};

		}
	};

	$.extend({
		enterlist : enterlist
	});
}(jQuery));