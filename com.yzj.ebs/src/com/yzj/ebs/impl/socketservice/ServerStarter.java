package com.yzj.ebs.impl.socketservice;

import com.yzj.wf.com.ibank.common.server.IBankControl;
import com.yzj.wf.com.ibank.common.server.IBankControlException;

/**
 * 创建于:2012-11-9<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * TODO
 * 
 * @author Jiangzhengqiu
 * @version 1.0.0
 */
public class ServerStarter {
	private IBankControl bankControl;

	public void init() throws IBankControlException {
		System.out.println("准备启动ibank监听...");
		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					bankControl.start();
				} catch (IBankControlException e) {
					e.printStackTrace();
				}
			}
		});
		t.start();
	}

	public IBankControl getBankControl() {
		return bankControl;
	}

	public void setBankControl(IBankControl bankControl) {
		this.bankControl = bankControl;
	}

}

