package com.yzj.ebs.edata.job;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.yzj.ebs.edata.service.NetProcessServer;
import com.yzj.wf.as.common.IJobRegister;
import com.yzj.wf.as.common.JobLogic;
import com.yzj.wf.as.entity.BusinessInfo;
/**
 * 每天晚上接受网银传来的  对账单结果和对账单调节表
 * @author Administrator
 *
 */
public class AcceptNetJob extends JobLogic {
	private static final String AutoType = "ACCEPTNETJOB";
	private IJobRegister jobRegister;
	private DateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
	private NetProcessServer netServer;
	
	public void init() throws Exception {
		if (jobRegister != null) {
			jobRegister.addJobLogic(AutoType, this);
		} else {
			throw new Exception("未获取到IJobRegister服务");
		}
	}

	@Override
	public BusinessInfo action() throws Exception {
		String nowDate = formatDate.format(new Date()); // 数据日期
		long time = System.currentTimeMillis();
		netServer.exchangeResultFormNet();
		BusinessInfo businessInfo=new BusinessInfo(AutoType,true,
				new AutoResult(nowDate,(System.currentTimeMillis() - time)+"",nowDate +"处理网银发来的对账单结果和调节表流程结束"),nowDate+"处理网银发来的对账单结果和调节表流程结束");
		return businessInfo;
	}
	
	public NetProcessServer getNetServer() {
		return netServer;
	}

	public void setNetServer(NetProcessServer netServer) {
		this.netServer = netServer;
	}

	public IJobRegister getJobRegister() {
		return jobRegister;
	}

	public void setJobRegister(IJobRegister jobRegister) {
		this.jobRegister = jobRegister;
	}

}
