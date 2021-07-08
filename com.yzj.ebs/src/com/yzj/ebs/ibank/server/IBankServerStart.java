/**
 * 
 */
package com.yzj.ebs.ibank.server;

import org.springframework.beans.BeansException;

import com.yzj.ebs.ibank.server.IBankSocketControl;

/**
 *创建于:2012-5-26<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * TODO
 * @author WangXue
 * @version 1.0.0 
 */
public class IBankServerStart {

	private IBankSocketControl socketControl;
	/**
	 * @param args
	 */
	public void start() {
		TT tt = new TT();
		Thread t = new Thread(tt);
		t.start();
	}
	
	class TT implements Runnable {
		public void run(){
			try {
				socketControl.start();//启动一个socketControl服务
			} catch (BeansException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public IBankSocketControl getSocketControl() {
		return socketControl;
	}

	public void setSocketControl(IBankSocketControl socketControl) {
		this.socketControl = socketControl;
	}
	
	

}
