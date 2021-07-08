package com.yzj.ebs.retreatinput.biz;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.database.CheckMainData;
import com.yzj.ebs.retreatinput.pojo.BatchParam;
import com.yzj.ebs.retreatinput.pojo.ImportData;
import com.yzj.ebs.retreatinput.pojo.SingleParam;

/**
 * 创建于:2013-04-03<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 退信登记  业务实现接口
 * 
 * @author 单伟龙
 * @version 1.0.0
 */
public interface IRetreatInputBiz {
	/**
	 * 逐笔登记业务 实现
	 */

	public String completeSingleInput(CheckMainData info) throws XDocProcException, IOException;
	
	/**
	 * 批量登记业务 实现
	 */
	public BatchParam completeBatchInput(File upFile) throws XDocProcException, IOException;
	
}
