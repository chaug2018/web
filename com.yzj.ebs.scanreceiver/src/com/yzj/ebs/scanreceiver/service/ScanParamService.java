package com.yzj.ebs.scanreceiver.service;

import java.util.Map;

import com.yzj.ebs.common.XDocProcException;

/**
 *创建于:2012-9-19<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 扫描参数服务实现类
 * @author 陈林江
 * @version 1.0
 */
public class ScanParamService implements IScanParamService {
	
	private static Map<String,String> params;

	/* (non-Javadoc)
	 * @see com.yinzhijie.ebs.scanreceiver.service.IScanParamService#getStorePath(java.lang.String)
	 */
	@Override
	public String getStorePath(String orgNo)throws XDocProcException {
		if(params==null){
			params=this.getParams();
		}
		String result=params.get(orgNo);
		if(result==null){
			throw new XDocProcException("未获取到机构号:"+orgNo+"对应的影像服务地址");
		}
		return result;
	}
	
	private Map<String,String> getParams(){
		return null;
	}

}
