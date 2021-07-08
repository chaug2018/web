/**
 * @package WF
 * @author chenhuang
 * @desc WF框架页面缓存对象
 * @date 2013-2-22
 * @version 1.0
 */
/**
 * 缓存操作对象
 */
WFCache = {
	/**
	 * 当前版本
	 */
	version : '1.0.0',

	/**
	 * 获取命名空间
	 * 
	 * @param cacheSpace
	 * @returns {WF.CacheSpace}
	 */
	getCacheSpace : function(cacheSpace) {
		if (arguments.length == 1) {
			return new WF.CacheSpace(cacheSpace);
		}
	},
	/**
	 * 创建一个空Cache块
	 * 
	 * @returns {PageCache}
	 */
	createPageCache : function() {
		return new WF.PageCache();
	},
	/**
	 * 按命名空间存储Cache
	 * 
	 * @param key
	 * @param value
	 */
	putPageCache : function(key, value) {
		if (key instanceof WF.CacheSpace && value instanceof WF.PageCache) {
			WFCacheData[key.getCacheSpace()] = value;
		}
	},
	/**
	 * 获取PageCache
	 * 
	 * @param key
	 * @returns {PageCache}
	 */
	getPageCache : function(key) {
		if (key instanceof WF.CacheSpace) {
			return WFCacheData[key.getCacheSpace()];
		} else {
			return null;
		}
	},
	/**
	 * 根据命名空间删除对象数据
	 * 
	 * @param key
	 */
	removePageCache : function(key) {
		if (key instanceof WF.CacheSpace) {
			delete WFCacheData[key.getCacheSpace()];
		}
	},
	/**
	 * 预览所有PageCache
	 */
	previewPageCache : function() {
		OpenWindow = window
				.open(
						"",
						"页面缓存列表",
						"height=300, width=400,toolbar=no,scrollbars=yes,menubar=no,location=no,status=no");
		var cacheDataHtml = "<ul>";
		for ( var cacheKey in WFCacheData) {
			cacheDataHtml += "<li><b>" + cacheKey + "</b><ul>";
			var pageCache = WFCacheData[cacheKey];
			var keys = pageCache.keys();
			for ( var i = 0; i < keys.length; i++) {
				cacheDataHtml += "<li>" + $.toJSON(keys[i]) + " = "
						+ $.toJSON(pageCache.get(keys[i])) + "</li>";
			}
			cacheDataHtml += "<br></ul></li>";
		}
		cacheDataHtml += "</ul>";
		// 写成一行
		OpenWindow.document.write("<TITLE>前端缓存数据列表</TITLE>");
		OpenWindow.document.write("<BODY BGCOLOR=#ffffff>");
		OpenWindow.document.write(cacheDataHtml);
		OpenWindow.document.write("</BODY>");
		OpenWindow.document.write("</HTML>");
		OpenWindow.document.close();
	},
	/**
	 * 刷新缓存数据
	 */
	refreshPageCache : function(cacheSpacekey) {
		if (arguments.length == 1) {
			$.post(WFCacheManager.getWebContext() + "/refreshPageCache.action",
					"cacheSpacekey=" + cacheSpacekey, function(cache) {
						if (cache != "命名空间有误,页面缓存刷新失败!") {
							var data = $.parseJSON(cache);
							for ( var cacheSpaceKey in data) {
								// 获取命名空间
								var cacheSpace = WFCache
										.getCacheSpace(cacheSpaceKey);
								// 创建新的缓存存储对象
								var pageCache = WFCache.createPageCache();

								var tempCache = data[cacheSpaceKey];
								for ( var key in tempCache) {
									pageCache.put(key, tempCache[key]);
								}
								// 移除原有缓存数据
								WFCache.removePageCache(cacheSpace);
								// 设想新的缓存数据
								WFCache.putPageCache(cacheSpace, pageCache);
							}
							// 刷新预览界面
							var previewCacheSpace = WFCache
									.getCacheSpace("WF_CORE_CACHE_SPACE");
							var defaultCache = WFCache
									.getPageCache(previewCacheSpace);
							if (defaultCache.get("SYS_RUN_MODE") == "1") {// 系统为调试模式
								WFCache.previewPageCache();
							}
						} else {
							alert(cache);
						}
					});
		}
	}

};
/**
 * 缓存管理类，暂不对外开放
 */
WFCacheManager = {
	/**
	 * 加载页面缓存数据
	 */
	loadPageCache : function() {
		$.ajaxSettings.async = false;// 设置为同步请求
		$.getJSON(WFCacheManager.getWebContext()
				+ "/loadPageCache.action", function(data) {
			for ( var cacheSpaceKey in data) {
				// 获取命名空间
				var cacheSpace = WFCache.getCacheSpace(cacheSpaceKey);
				// 根据命名空间获取缓存
				var pageCache = WFCache.getPageCache(cacheSpace);
				// 判断当前命名空间缓存是否存在，若不存在，创建创建一个新缓存
				if (!(pageCache instanceof WF.PageCache)) {
					pageCache = WFCache.createPageCache();
				}
				var tempCache = data[cacheSpaceKey];
				for ( var key in tempCache) {
					pageCache.put(key, tempCache[key]);
				}
				WFCache.putPageCache(cacheSpace, pageCache);
			}
		});
		// 判断当前系统运行状态，是否需要预览缓存
		var previewCacheSpace = WFCache.getCacheSpace("WF_CORE_CACHE_SPACE");
		var defaultCache = WFCache.getPageCache(previewCacheSpace);
		if (defaultCache.get("SYS_RUN_MODE") == "1") {// 系统为调试模式
			WFCache.previewPageCache();
		}
	},
	/**
	 * 获取web容器context
	 */
	getWebContext : function() {
		var pathName = document.location.pathname;
		var contextLastIndex = pathName.substr(1).indexOf("/");
		var context = pathName.substr(0, contextLastIndex + 1);
		return context;
	}
};

/**
 * 缓存数据存储
 */
WFCacheData = {};

if (typeof WF == "undefined") {
	WF = {};
}
/**
 * PageCache:存储页面缓存数据,相当于一个HashMap
 */
WF.PageCache = function() {

	/**
	 * 数据存储对象
	 */
	this.data = new Object();

	/**
	 * put方法
	 */
	this.put = function(key, value) {
		if (!this.containsKey(key)) {
			this.data[key] = value;
		}
	};

	/**
	 * get方法
	 */
	this.get = function(key) {
		return this.containsKey(key) ? this.data[key] : null;
	};

	/**
	 * remove方法
	 */
	this.remove = function(key) {
		if (this.containsKey(key) && (delete this.data[key])) {
		}
	};

	/**
	 * 是否包含Key
	 */
	this.containsKey = function(key) {
		return (key in this.data);
	};

	/**
	 * 是否包含value
	 */
	this.containsValue = function(value) {
		for ( var key in this.data) {
			if (this.data[key] == value) {
				return true;
			}
		}
		return false;
	};

	/**
	 * 所有value值
	 */
	this.values = function() {
		var tempValues = new Array();
		for ( var key in this.data) {
			tempValues.push(this.data[key]);
		}
		return tempValues;
	};

	/**
	 * 所有key值
	 */
	this.keys = function() {
		var tempKeys = new Array();
		for ( var key in this.data) {
			tempKeys.push(key);
		}
		return tempKeys;
	};

	/**
	 * Map大小
	 */
	this.size = function() {
		var count = 0;
		for (key in this.data) {
			count++;
		}
		return count;
	};

	/**
	 * 清空Map
	 */
	this.clear = function() {
		this.data = new Object();
	};

	/**
	 * 转为json字符串
	 */
	this.toJSON = function() {
		try {
			return $.toJSON(this.data);
		} catch (err) {
			return null;
		}
		;
	};
	/**
	 * 转为json对象
	 * 
	 * @returns
	 */
	this.parseJSON = function() {
		try {
			return $.parseJSON(this.toJSON());
		} catch (err) {
			return null;
		}
		;
	};
};

/**
 * CacheSpace:页面命名空间对象
 */
WF.CacheSpace = function(cacheSpace) {
	this.cacheSpace = cacheSpace;

	this.getCacheSpace = function() {
		return this.cacheSpace;
	};
};