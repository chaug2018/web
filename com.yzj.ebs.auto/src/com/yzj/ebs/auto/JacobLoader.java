/**
 * JacobLoader.java
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司
 * 创建:donggenlong 2012-8-18
 */
package com.yzj.ebs.auto;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

/**
 * 加载控件
 * 
 * @author donggenlong
 * @version 1.0
 */
public class JacobLoader {

	private ActiveXComponent activeXComponent;

	public JacobLoader() {
	}

	/**
	 * 加载控件
	 * 
	 * @param vbName
	 * @return
	 * @throws Exception
	 */
	public synchronized boolean load(String vbName) throws Exception {
		try {
			// System.out.println(System.getProperty("java.library.path"));
			activeXComponent = new ActiveXComponent(vbName);

		} catch (Exception e) {
			throw e;
		} catch (Throwable e) {
			throw new Exception(e);
		}
		return activeXComponent != null;
	}

	/**
	 * 调用控件内部方法
	 * 
	 * @param methodName
	 * @param param
	 * @return
	 */
	public synchronized String doMethod(String methodName, Object[] param)
			throws Exception {
		Variant result = null;
		Dispatch dispatch = (Dispatch) activeXComponent.getObject();
		result = (Variant) Dispatch.callN(dispatch, methodName, param);
		return result.toString();
	}

	/**
	 * 资源释放
	 */
	public void release() {
		if(activeXComponent != null){
			activeXComponent.safeRelease();
		}
	}
}
