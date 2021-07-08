package com.yzj.ebs.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 
 *创建于:2012-9-26
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司
 * 获取各种格式日期类
 * @author pwx
 * @version 1.0.0
 */
public class UtilBase {

	/**
	 * 日期格式为：如：20120926
	 * @return 日期字符串
	 */
	public static String getNowDate() {
		Date newdate = new Date();
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
		String dateformat = sf.format(newdate);
		return dateformat;
	}
	
	
	/**
	 * 获取当前日期，格式为：如：2012-09-26 16:14:55
	 * @return 日期字符串
	 */
	public static String getNowTime() {
		Date newdate = new Date();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateformat = sf.format(newdate);
		return dateformat;
	}
	/**
	 * 获取当前日期，格式为：如：20120926161455
	 * @return 日期字符串
	 */
	public static String getTime() {
		Date newdate = new Date();
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateformat = sf.format(newdate);
		return dateformat;
	}
	/**
	 * 获取当前日期，格式为：如：20120926041600956
	 * @return 日期字符串
	 */
	public static String getTimeSSS() {
		Date newdate = new Date();
		SimpleDateFormat sf=new SimpleDateFormat("yyyyMMddhhmmssSSS");
		String dateformat = sf.format(newdate);
		return dateformat;
	}
	
	// 将Double类型转换为String类型
	public static String formatString(Double retStr) {
		// 小数格式化，引号中的0.000表示保留小数点后三位（第四位四舍五入）
		DecimalFormat df = new DecimalFormat("#,##0.00");
		String retString = df.format(retStr);
		return retString;
	}

		public String getMothLastDate(String date) {
			
			Calendar cal = Calendar.getInstance();
			// 不加下面2行，就是取当前时间前一个月的第一天及最后一天
			cal.set(Calendar.YEAR, Integer.parseInt(date.substring(0, 4)));
			cal.set(Calendar.MONTH, Integer.parseInt(date.substring(4, 6)));
			cal.set(Calendar.DAY_OF_MONTH, 0);
			Date lastDate = cal.getTime();
			cal.set(Calendar.DAY_OF_MONTH, -1);
			cal.add(Calendar.DAY_OF_MONTH, 1);
			lastDate = cal.getTime();
			
			return getTime(lastDate) ;
		}
		public static String getTime(Date newdate ) {
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
			String dateformat = sf.format(newdate);
			return dateformat;
		}
		
}


