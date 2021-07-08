/**
 * GenericUtils.java
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司
 * 创建:Jiangzhengqiu 2012-04-10
 */
package com.yzj.ebs.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/***
 * 工具类 利用反射机制类操作object对象
 * 
 * @author Jiangzhengqiu
 * 
 */
@SuppressWarnings("rawtypes")
public class GenericUtils {
	// 工具类，提供一些通用的转换
	private GenericUtils() {
	}

	
	public static Class getSuperClassGenricType(Class clazz) {
		return getSuperClassGenricType(clazz, 0);
	}

	public static Class getSuperClassGenricType(Class clazz, int index) {

		Type genType = clazz.getGenericSuperclass();

		if (!(genType instanceof ParameterizedType)) {
			return Object.class;
		}

		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

		if (index >= params.length || index < 0) {
			return Object.class;
		}
		if (!(params[index] instanceof Class)) {
			return Object.class;
		}
		return (Class) params[index];
	}
}
