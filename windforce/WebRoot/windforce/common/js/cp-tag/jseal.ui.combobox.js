(function($) {
	$.extend($.ui.autocomplete, {
		filter : function(array, term) {
			var matcher = new RegExp("^"+$.ui.autocomplete.escapeRegex(term), "i");
			return $.grep(array, function(value) {
				var pinyin = getMatchPinyin(value,true);
				for(var x in pinyin){
					if(matcher.test(pinyin[x])){
						return true;
					}
				}
				return false;
			});
		}
	});
	
	
	$.widget( "ui.combobox", {
		options: {
			wrapperWidth:null,//模拟的select的总长度，为空默认与替换的select长度相同(该属性长度单位请勿使用'%')
			inputWidth:null,//模拟的select的input长度，为空默认使用combobox.css内定义长度("80%")
			hasTriangle:true//是否需要模拟按钮
		},
		
		input:null,
		
		_create: function() {
			var self = this;
			var select = this.element.hide();
			var wrapperWidth = this.options.wrapperWidth ? this.options.wrapperWidth : select.outerWidth();
			var wrapperHeight = select.outerHeight();
			
			var wrapper = this.wrapper = $("<span>").insertAfter(select).width(wrapperWidth).height(wrapperHeight);
			wrapper.addClass("ui-combobox");
			var input = $("<input>").appendTo(wrapper).val(select.val() ? select.children(":selected").text() : "");
			input.addClass("ui-combobox-input").height(wrapperHeight);
			if(this.options.inputWidth){
				input.width(this.options.inputWidth);
			}

			input.autocomplete({
				delay: 0,
				minLength: 0,
				source: function(request, response) {
					var matcher = new RegExp("^"+$.ui.autocomplete.escapeRegex(request.term), "i" );
					response(select.children("option").map(function() {
						var text = $(this).text();
						if(this.value){
							if(!request.term){
								return {value: text,option: this};
							}
							var pinyin = getMatchPinyin(text, true);
							for(var x in pinyin){
								if(matcher.test(pinyin[x])){
									return {value: text,option: this};
								}
							}
						}
					}) );
				},
				select: function(event, ui) {
					ui.item.option.selected = true;
					self._trigger("selected", event);
				},
				change: function(event, ui) {
					if (!ui.item) {
						var matcher = new RegExp( "^" + $.ui.autocomplete.escapeRegex($(this).val()) + "$", "i" );
						var valid = false;
						select.children("option").each(function() {
							if ($(this).text().match(matcher)) {
								this.selected = valid = true;
								return false;
							}
						});
						if (!valid) {
							$(this).val("");
							select.val("");
							input.data("autocomplete" ).term = "";
							return false;
						}
					}
				}
			});
			input.data("autocomplete").menu.element.css("font-size", input.css("font-size"));

			this.input = input;
			
			if(this.options.hasTriangle){
				var triangle = $("<a>").attr("tabIndex", -1).appendTo(wrapper).button({icons: {primary: "ui-icon-triangle-1-s"},text: false});
				triangle.addClass("ui-combobox-toggle");
				triangle.width(wrapper.width() - input.outerWidth(true) -2).height(wrapperHeight-2).css("top",input.position().top);
				triangle.click(function() {
					if ( input.autocomplete("widget").is( ":visible" ) ) {
						input.autocomplete("close");
						return false;
					}
					$(this).blur();
					input.autocomplete("search", "");
					input.focus();
				});
			}
		},
		
		reset:function(index){//用于重置combobox，参数为空时，第一个option为默认选项
			this.element.get(0).selectedIndex=(index ? index : 0);
			this.input.val(this.element.val() ? this.element.children(":selected").text() : "");
		},

		destroy: function() {
			this.wrapper.remove();
			this.element.show();
			$.Widget.prototype.destroy.call(this);
		}
	});
})(jQuery);