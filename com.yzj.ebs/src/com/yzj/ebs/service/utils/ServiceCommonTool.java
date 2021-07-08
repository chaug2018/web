package com.yzj.ebs.service.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.yzj.ebs.service.common.header.RequestSysHead;
import com.yzj.ebs.service.common.header.ResponseSysHead;


/**
 * 创建于：20120年8月10日<br>
 * 版权所有(C) 2019深圳市银之杰科技股份有限公司<br>
 * webservice服务公共方法类
 * 
 * @author zhengyuan
 * @version 1.0
 */
public class ServiceCommonTool {
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMDDHHMMSS");
	
	/**
	 * 拼裝返回报文头
	 * 
	 * @param requestSysHead 请求报文头信息
	 * @return ResponseSysHead
	 */
	public ResponseSysHead createResponseHead(RequestSysHead requestSysHead) {
		ResponseSysHead responseSysHead = new ResponseSysHead();
		responseSysHead.setServiceCode(requestSysHead.getServiceCode());
		responseSysHead.setServiceScene(requestSysHead.getServiceScene());
		responseSysHead.setConsumerId(requestSysHead.getConsumerId());

		String date = sdf.format(new Date());
		responseSysHead.setTranDate(date.substring(0, 8));
		responseSysHead.setTranTime(date.substring(8, 14));
		return responseSysHead;
	}
}
