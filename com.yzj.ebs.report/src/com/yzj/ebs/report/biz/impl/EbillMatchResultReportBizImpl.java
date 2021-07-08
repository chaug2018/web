package com.yzj.ebs.report.biz.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.yzj.ebs.common.IAccnoMainDataAdm;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.report.biz.IEbillMatchResultReportBiz;
import com.yzj.ebs.report.pojo.EbillMatchResultResult;
import com.yzj.wf.common.WFLogger;

/**
 * 创建于:2013-04-8<br>
 * 版权所有(C) 2013 深圳市银之杰科技股份有限公司<br>
 * 账户有效对账结果展示  业务实现
 * @author swl
 * @version 1.0.0
 */
public class EbillMatchResultReportBizImpl implements IEbillMatchResultReportBiz{
	

	private EbillMatchResultResult result;
	private List<EbillMatchResultResult> resultList = new ArrayList<EbillMatchResultResult>();
	
	private IAccnoMainDataAdm accnoMainDataAdm;
	
	private static WFLogger logger = WFLogger.getLogger(EbillMatchResultReportBizImpl.class);
	
	@Override
	public List<EbillMatchResultResult> getEbillMatchResult(
			Map<String, String> queryMap, PageParam queryParam, boolean isPaged) {
		result = null;
		resultList.clear();
		try {
			List<?> list = accnoMainDataAdm.getEbillMatchResult(queryMap,queryParam,isPaged);

			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					String idCenter = "";
					String idBank = "";
					String bankName = "";
					String accNo = "";
					String finalcheckflag="";//对账结果
					String proveflag="";//验印结果
					String succflag="";//成功与否:勾兑结果为相符+验印结果为正常+流程结束
					String docdate="";
					
					Object[] obj = (Object[]) list.get(i);
					for (int j = 0; j < obj.length; j++) {
						switch (j) {
						case 0:
							if (obj[j] != null) {
								if (obj[j].toString().trim().length() > 0) {
									idCenter = obj[j].toString();
								}
							}
							break;
						case 1:
							if (obj[j] != null) {
								if (obj[j].toString().trim().length() > 0) {
									idBank = obj[j].toString();
								}
							}
							break;
						case 2:
							if (obj[j] != null) {
								if (obj[j].toString().trim().length() > 0) {
									bankName = obj[j].toString();
								}
							}
							break;
						case 3:
							if (obj[j] != null) {
								if (obj[j].toString().trim().length() > 0) {
									accNo = obj[j].toString();
								}
							}
							break;
						case 4:
							if (obj[j] != null) {
								if (obj[j].toString().trim().length() > 0) {
									finalcheckflag = obj[j].toString();
								}
							}
							break;
						case 5:
							if (obj[j] != null) {
								if (obj[j].toString().trim().length() > 0) {
									proveflag = obj[j].toString();
								}
							}
							break;
						case 6:
							if (obj[j] != null) {
								if (obj[j].toString().trim().length() > 0) {
									docdate = obj[j].toString();
								}
							}
							break;
						case 7:
							if (obj[j] != null) {
								if (obj[j].toString().trim().length() > 0) {
									succflag = obj[j].toString();
								}
							}
							break;
						}
					}

					result = new EbillMatchResultResult(idCenter, idBank, bankName, accNo, 
							finalcheckflag, proveflag, succflag, docdate);
					resultList.add(result);

				}
			}
		} catch (XDocProcException e) {
			logger.error("对账率统计查询数据库错误", e);
			return null;
		}

		return resultList;
	}
	public EbillMatchResultResult getResult() {
		return result;
	}
	public void setResult(EbillMatchResultResult result) {
		this.result = result;
	}
	public List<EbillMatchResultResult> getResultList() {
		return resultList;
	}
	public void setResultList(List<EbillMatchResultResult> resultList) {
		this.resultList = resultList;
	}
	public IAccnoMainDataAdm getAccnoMainDataAdm() {
		return accnoMainDataAdm;
	}
	public void setAccnoMainDataAdm(IAccnoMainDataAdm accnoMainDataAdm) {
		this.accnoMainDataAdm = accnoMainDataAdm;
	}
	public static WFLogger getLogger() {
		return logger;
	}
	public static void setLogger(WFLogger logger) {
		EbillMatchResultReportBizImpl.logger = logger;
	}

}
