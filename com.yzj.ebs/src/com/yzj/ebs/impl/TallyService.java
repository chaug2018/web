package com.yzj.ebs.impl;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.yzj.ebs.common.ITallyService;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.database.DocSet;
import com.yzj.wf.com.ibank.common.IBank;
import com.yzj.wf.common.WFLogger;



/**
 * 创建于:2012-8-21<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 与行内核心系统接口实现
 * 
 * @author WangXue
 * @version 1.0.0
 */
public class TallyService implements ITallyService {

	private IBank iBank;
	private WFLogger logger = WFLogger.getLogger(this.getClass());
    private String currDataFormat = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.yzj.dps.common.ITallyService#getAccName(com.yzj.dps.db
	 * .DocSet)
	 */
	@Override
	public String getAccName(DocSet docSet,StringBuffer errorMsg) throws XDocProcException {
		Map<String, Object> downParams = new HashMap<String,Object>();
		try {
			//TODO: 记账方法，根据实际情况，目前暂不提供记账至核心。
			return null;
		} catch (Exception e) {
			logger.error("查询户名交易发生异常", e);
			return "核心返回结果异常！";
		}
	}

	@Override
	public boolean tallyDebit(DocSet docSet) throws XDocProcException {
		try {
			// TODO 暂不提供记账至核心。
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("借记记账时发生异常", e);
			return false;
			
		}
	}

	@Override
	public boolean untreadDebit(DocSet docSet) throws XDocProcException {
		try {
			// TODO 暂不提供记账至核心。
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("借记退票时发生异常", e);
			throw new XDocProcException(e);
		}

	}

	@Override
	public boolean undoTallyDebit(DocSet docSet,StringBuffer errorMsg) throws XDocProcException {
		try {
			// TODO 暂不提供记账至核心。
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("借记删除时发生异常", e);
			errorMsg.append("核心返回结果异常！");
			return false;
		}
	}

	@Override
	public boolean checkPayPwd(DocSet docSet,StringBuffer errorMsg) {
		try {
			// TODO 暂不提供记账至核心。
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("密码校验时发生异常", e);
			errorMsg.append("核心返回结果异常！");
			return false;
		}

	}


	public IBank getiBank() {
		return iBank;
	}

	public void setiBank(IBank iBank) {
		this.iBank = iBank;
	}

}
