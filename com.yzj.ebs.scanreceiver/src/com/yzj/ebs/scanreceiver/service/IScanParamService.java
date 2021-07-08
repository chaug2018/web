package com.yzj.ebs.scanreceiver.service;

import com.yzj.ebs.common.XDocProcException;

/**
 *创建于:2012-9-19<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 为扫描功能提供的参数服务接口
 * @author 陈林江
 * @version 1.0
 */
public interface IScanParamService {

	String getStorePath(String orgNo)throws XDocProcException ;
}
