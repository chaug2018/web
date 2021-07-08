package com.yzj.ebs;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * 创建于:2012-9-4<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 自动程序启动器
 * 
 * @author WangXue
 * @version 1.0.0
 */
public class Addds {

	/**
	 * @param args
	 */

public static void main(String[] args) {
	String[] local = { "config/bean-pam.xml", "config/bean-as.xml" };
	ApplicationContext appContext = new FileSystemXmlApplicationContext(
			local);
	System.out.println(appContext);
}

}
