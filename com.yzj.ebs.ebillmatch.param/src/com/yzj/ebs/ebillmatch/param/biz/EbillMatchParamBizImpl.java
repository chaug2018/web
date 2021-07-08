package com.yzj.ebs.ebillmatch.param.biz;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.yzj.ebs.common.IAccnoMainDataAdm;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.ebillmatch.param.param.MatchResultParam;
import com.yzj.wf.common.WFLogger;

/**
 * 创建于:2013-04-8<br>
 * 版权所有(C) 2013 深圳市银之杰科技股份有限公司<br>
 * 半年机构对账有效明细 统计 业务实现
 * @author swl
 * @version 1.0.0
 */
public class EbillMatchParamBizImpl implements IEbillMatchParamBiz{
	

	private MatchResultParam result;
	private List<MatchResultParam> resultList = new ArrayList<MatchResultParam>();
	
	private IAccnoMainDataAdm accnoMainDataAdm;
	
	private static WFLogger logger = WFLogger
			.getLogger(EbillMatchParamBizImpl.class);
	/**
	 * 获取数据
	 */
	@Override
	public List<MatchResultParam> getEbillMatchParam(
			Map<String, String> queryMap, PageParam queryParam, String docDate,
			boolean isPaged) {
		// TODO Auto-generated method stub
		result = null;
		resultList.clear();
		try {
			List<?> list = null;
			//true为分页查询
			if (isPaged) {
				list = null;
				list = accnoMainDataAdm.getEbillMatchParam(queryMap,
						(PageParam) queryParam, docDate);
			}else{
				list = null;
				list = accnoMainDataAdm
						.getAllEbillMatchParam(queryMap, docDate);
			}
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					String idCenter = "";
					String idBank = "";
					String bankName = "";
					String accNo = "";
					String accNoSon = "";
					String accName = "";
					String MatchCount1 = "未成功";
					String MatchCount2 = "未成功";
					String MatchCount3 = "未成功";
					String MatchCount4 = "未成功";
					String MatchCount5 = "未成功";
					String MatchCount6 = "未成功";
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
									accNoSon = obj[j].toString();
								}
							}
							break;

						case 5:
							if (obj[j] != null) {
								if (obj[j + 1].toString().equals("0")) {
									MatchCount6 = "";
								} else {
									if (obj[j].toString().trim().length() > 0
											&& obj[j].toString().equals("1")) {
										MatchCount6 = "已成功";
									}
								}
							}
							j++;
							break;
						case 7:
							if (obj[j] != null) {
								if (obj[j + 1].toString().equals("0")) {
									MatchCount5 = "";
								} else {
									if (obj[j].toString().trim().length() > 0
											&& obj[j].toString().equals("1")) {
										MatchCount5 = "已成功";
									}
								}
							}
							j++;
							break;
						case 9:
							if (obj[j] != null) {
								if (obj[j + 1].toString().equals("0")) {
									MatchCount4 = "";
								} else {
									if (obj[j].toString().trim().length() > 0
											&& obj[j].toString().equals("1")) {
										MatchCount4 = "已成功";
									}
								}
							}
							j++;
							break;
						case 11:
							if (obj[j] != null) {
								if (obj[j + 1].toString().equals("0")) {
									MatchCount3 = "";
								} else {
									if (obj[j].toString().trim().length() > 0
											&& obj[j].toString().equals("1")) {
										MatchCount3 = "已成功";
									}
								}
							}
							j++;
							break;
						case 13:
							if (obj[j] != null) {
								if (obj[j + 1].toString().equals("0")) {
									MatchCount2 = "";
								} else {
									if (obj[j].toString().trim().length() > 0
											&& obj[j].toString().equals("1")) {
										MatchCount2 = "已成功";
									}
								}
							}
							j++;
							break;
						case 15:
							if (obj[j] != null) {
								if (obj[j + 1].toString().equals("0")) {
									MatchCount1 = "";
								} else {
									if (obj[j].toString().trim().length() > 0
											&& obj[j].toString().equals("1")) {
										MatchCount1 = "已成功";
									}
								}
							}
							j++;
							break;
						case 17:
							if (obj[j] != null) {
								if (obj[j].toString().trim().length() > 0) {
									accName = obj[j].toString();
								}
							}
							break;
						}
					}

					result = new MatchResultParam(idCenter,idBank, bankName, accNo,
							accNoSon, accName, MatchCount1, MatchCount2,
							MatchCount3, MatchCount4, MatchCount5, MatchCount6);
					resultList.add(result);

				}
			}
		} catch (XDocProcException e) {
			logger.error("对账率统计查询数据库错误", e);
			return null;
		}

		return resultList;
	}
	public MatchResultParam getResult() {
		return result;
	}
	public void setResult(MatchResultParam result) {
		this.result = result;
	}
	public List<MatchResultParam> getResultList() {
		return resultList;
	}
	public void setResultList(List<MatchResultParam> resultList) {
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
		EbillMatchParamBizImpl.logger = logger;
	}

}
