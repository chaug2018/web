package com.yzj.ebs.edata.service.checkmethod;

import com.yzj.ebs.edata.common.CheckInformation;
import com.yzj.ebs.edata.common.ICheckMethod;
import com.yzj.ebs.edata.common.PublicDefine;
import com.yzj.ebs.edata.common.CheckInformation.InfoType;

/**
 * 创建于:2012-9-25<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 校验文件列数
 * 
 * @author Lif
 * @version 1.0.0
 */
public class CheckColumnMethod implements ICheckMethod {
	private String separator;

	public CheckColumnMethod(String separator) {
		super();
		this.separator = separator;
	}
	@Override
	public CheckInformation check(String content,String fileName) {
		int separatorNum=0;
		if("maindata".equals(fileName)){//.split("_")[0])
			separatorNum=PublicDefine.maindataSeparatorlen;
		}else if("dephist".equals(fileName)){
			separatorNum=PublicDefine.dephistSeparatorlen;
		}else if("utblbrcd".equals(fileName)){
			separatorNum=PublicDefine.utblbrcdSeparatorlen;
		}else if("kub_user".equals(fileName)){
			separatorNum=PublicDefine.userSeparatorlen;
		}else if("knp_exrt".equals(fileName)){
			separatorNum=PublicDefine.exrtSeparatorlen;
		}else if("atchus".equals(fileName)){
			separatorNum=PublicDefine.atchusSeparatorlen;
		}else if("OK".equals(fileName)){
			separatorNum=PublicDefine.okSeparatorlen;
		}
		
		CheckInformation checkInformation;
		int len = content.length()
				- content.replace(separator, "").length();
		boolean result = len == separatorNum;
		if (result == false) {
			checkInformation=new CheckInformation(InfoType.ERROR,"文件："+fileName+"列数不一致:"+content);
		}else{
			checkInformation=new CheckInformation(InfoType.SUCCESS,"列数一致");
		}
		checkInformation.setResult(result);
		return checkInformation;
	}

	

}
