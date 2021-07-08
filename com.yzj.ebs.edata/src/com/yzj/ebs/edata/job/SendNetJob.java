package com.yzj.ebs.edata.job;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.yzj.ebs.edata.service.NetProcessServer;
import com.yzj.wf.as.common.IJobRegister;
import com.yzj.wf.as.common.JobLogic;
import com.yzj.wf.as.entity.BusinessInfo;
/**
 * 每天晚上将当天对账系统的网银未达调节结果返回给网银系统
 * @author Administrator
 *
 */
public class SendNetJob extends JobLogic {
	private static final String AutoType = "SENDNETJOB";
	private DateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
	private IJobRegister jobRegister;
	private NetProcessServer netServer;
	
	

	public void init() throws Exception {
		if (jobRegister != null) {
			jobRegister.addJobLogic(AutoType, this);
		} else {
			throw new Exception("未获取到IJobRegister服务");
		}
	}
	
	public IJobRegister getJobRegister() {
		return jobRegister;
	}

	public void setJobRegister(IJobRegister jobRegister) {
		this.jobRegister = jobRegister;
	}

	@Override
	public BusinessInfo action() throws Exception {
		String nowDate = formatDate.format(new Date()); // 数据日期
		long time = System.currentTimeMillis();
//		netServer.fetchEbillFromEbill();
		netServer.fetchResultFromEbill();
		BusinessInfo businessInfo=new BusinessInfo(AutoType,true,
				new AutoResult(nowDate,(System.currentTimeMillis() - time)+"",nowDate +"网银账户未达结果抓取流程结束"),nowDate+"网银账户未达结果抓取流程结束");
		return businessInfo;
	}
	
	public NetProcessServer getNetServer() {
		return netServer;
	}

	public void setNetServer(NetProcessServer netServer) {
		this.netServer = netServer;
	}

}
