package com.yzj.ebs.auto;

import java.util.Timer;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.yzj.ebs.auto.datasave.TimerTaskWare;

/**
 * 创建于:2012-9-4<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 自动程序启动器
 * 
 * @author WangXue
 * @version 1.0.0
 */
public class Starter {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String[] local = { "config/bean-pam.xml", "config/bean-as.xml" };
		ApplicationContext appContext = new FileSystemXmlApplicationContext(
				local);
		System.out.println(appContext);
		
		
		//定时器
		Timer timer = new Timer();
		//4个小时扫描
		timer.schedule(new TimerTaskWare(), 0, 14400000);
	}
}
