package com.yzj.ebs.edata.service;

import com.yzj.ebs.edata.common.CheckInformation;
import com.yzj.ebs.edata.common.IDataProcess;
import com.yzj.ebs.edata.common.IDisplay;
import com.yzj.ebs.edata.common.CheckInformation.InfoType;
import com.yzj.ebs.edata.tran.DataHttpService;
import com.yzj.wf.common.WFLogger;

/**
 * 创建于:2012-9-28<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 数据处理实现类
 * 
 * @author Lif
 * @version 1.0.0
 */
public class DataProcessDefault implements IDataProcess {
	private WFLogger logger = WFLogger.getLogger(DataProcessDefault.class);
	private IDisplay display;
	DataHttpService dhs = new DataHttpService();

	@Override
	public void setDisplay(IDisplay display) {
		this.display = display;
	}

	@Override
	public boolean dataDispose(String dataDate,String szmonth) {
		boolean b = true;
		try {
			b = dhs.dataDisposeHttpRequest(dataDate, szmonth);
//			IDataProcessService dps=new DataProcessServiceImpl();
//			b=dps.dataDispose(docdate, szmonth);
		} catch (Exception e) {
			e.printStackTrace();
			b = false;
			logger.error("数据处理时发生异常", e);
			CheckInformation checkInformation = new CheckInformation(
					InfoType.ERROR, "数据处理时发生异常");
			this.display.showMsg(checkInformation);
		}
		return b;
	}

}
