// 前后台数据绑定的方法
/*******************************************************************************
 * options 分为两种类型 1：直接绑定数据，此时根据json串来判断 2.提供参数：实体名，匹配字段 来从后台公共方法中获取数据源自动绑定
 */
function onbindData(opts) {

	var isAutoBind = true;
	for ( var o in opts) {
		if (o == "bindJson") {
			if (opts[o] != null && opts[o] != "") {
				isAutoBind = false;
			}
		}
	}
	if (isAutoBind) {
		// TODO 后续实现后台绑定
	} else {
		for ( var o in opts) {
			if (o == "bindJson") {
				for ( var data in opts["bindJson"]) {
					if (document.getElementById(data) != null) {
						document.getElementById(data).value = opts["bindJson"][data];
					}
				}
			}
		}
	}
};