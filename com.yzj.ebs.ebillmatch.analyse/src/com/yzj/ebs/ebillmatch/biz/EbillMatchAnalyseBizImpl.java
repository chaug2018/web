package com.yzj.ebs.ebillmatch.biz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yzj.ebs.common.IAccnoMainDataAdm;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.ebillmatch.param.ResultParam;
import com.yzj.ebs.util.FinalConstant;
import com.yzj.wf.common.WFLogger;

/**
 * 创建于:2013-04-8<br>
 * 版权所有(C) 2013 深圳市银之杰科技股份有限公司<br>
 * 半年机构对账有效率  统计 业务实现
 * @author swl
 * @version 1.0.0
 */
public class EbillMatchAnalyseBizImpl implements IEbillMatchAnalyseBiz{
	
	private static final java.text.DecimalFormat df = new java.text.DecimalFormat(
			"#.##");

	private ResultParam result;
	private List<ResultParam> resultList = new ArrayList<ResultParam>();
	
	private IAccnoMainDataAdm accnoMainDataAdm;
	
	private static WFLogger logger = WFLogger.getLogger(EbillMatchAnalyseBizImpl.class);
	@Override
	public List<ResultParam> getEbillMatchAnalyseResult(
			Map<String, String> queryMap, PageParam queryParam, 
			String docDate,boolean isPaged,String selectCount) {
		// TODO Auto-generated method stub
		resultList.clear();
		result=null;
		try {
			List<?> idBranchNameList = null;
			Map<String,String> idBranchNameMap = new HashMap<String,String>();
			//获得所有清算中心的名字
			idBranchNameList = accnoMainDataAdm.getAllIdBranchName();
			for (int i = 0; i < idBranchNameList.size(); i++) {
				String idBranch = "";
				String idBranchName = "";
				Object[] obj = (Object[]) idBranchNameList.get(i);
				for (int j = 0; j < obj.length; j++) {
					switch (j) {
					case 0:
						if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
							idBranch = obj[j].toString();
						}
						break;
					case 1:
						if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
							idBranchName = obj[j].toString();
						}
						break;
					}
				}
				idBranchNameMap.put(idBranch, idBranchName);
			}
				
			List<?> list = null;
			// isPaged为按条件分页查询，否则为查询所有（导出时）
			//如果selectCount等于countIdBank，则以网点为单位进行统计, 否则以清算中心或者总行为单位进行统计
			if (isPaged) {
				if(selectCount!= null && selectCount.equals("countIdBank")){
					list=null;
					list = accnoMainDataAdm.getEbillMatchAnalyseResult(queryMap,
							(PageParam) queryParam, docDate);
				}else{
					list=null;
					list = accnoMainDataAdm.getEbillMatchAnalyseResultCount(queryMap,
							(PageParam) queryParam, docDate,selectCount);
				}
				
			} else {
				if(selectCount!= null && selectCount.equals("countIdBank")){
					list=null;
					list = accnoMainDataAdm.getAllEbillMatchAnalyseResult(queryMap,
							docDate);
				}else{
					list=null;
					list = accnoMainDataAdm.getAllEbillMatchAnalyseResultCount(queryMap,
							 docDate,selectCount);
				}
			}
			if (list != null && list.size() > 0) {
				if(selectCount!= null && selectCount.equals("countIdBank")){
					String idCenter = "";
					String idBank = "";
					String cName = "";

					long SendCount1 = 0;
					long MatchCount1 = 0;
					long SendCount2 = 0;
					long MatchCount2 = 0;
					long SendCount3 = 0;
					long MatchCount3 = 0;
					long SendCount4 = 0;
					long MatchCount4 = 0;
					long SendCount5 = 0;
					long MatchCount5 = 0;
					long SendCount6 = 0;
					long MatchCount6 = 0;

					String fstMatchPercent = "";
					String secMatchPercent = "";
					String thrMatchPercent = "";
					String fouMatchPercent = "";
					String fifMatchPercent = "";
					String sixMatchPercent = "";
					for (int i = 0; i < list.size(); i++) {
						Object[] obj = (Object[]) list.get(i);
						for (int j = 0; j < obj.length; j++) {
							switch (j) {
							case 0:
								if (obj[j] != null
										&& obj[j].toString().trim().length() > 0) {
									idCenter = obj[j].toString();
								}
								break;
							case 1:
								if (obj[j] != null
										&& obj[j].toString().trim().length() > 0) {
									idBank = obj[j].toString();
								}
								break;
							case 2:
								if (obj[j] != null
										&& obj[j].toString().trim().length() > 0) {
									SendCount6 = Long.parseLong(obj[j].toString());
								}
								break;
							case 3:
								if (obj[j] != null
										&& obj[j].toString().trim().length() > 0) {
									MatchCount6 = Long.parseLong(obj[j].toString());
								}
								break;
							case 4:
								if (obj[j] != null
										&& obj[j].toString().trim().length() > 0) {
									SendCount5 = Long.parseLong(obj[j].toString());
								}
								break;
							case 5:
								if (obj[j] != null
										&& obj[j].toString().trim().length() > 0) {
									MatchCount5 = Long.parseLong(obj[j].toString());
								}
								break;
							case 6:
								if (obj[j] != null
										&& obj[j].toString().trim().length() > 0) {
									SendCount4 = Long.parseLong(obj[j].toString());
								}
								break;
							case 7:
								if (obj[j] != null
										&& obj[j].toString().trim().length() > 0) {
									MatchCount4 = Long.parseLong(obj[j].toString());
								}
								break;
							case 8:
								if (obj[j] != null
										&& obj[j].toString().trim().length() > 0) {
									SendCount3 = Long.parseLong(obj[j].toString());
								}
								break;
							case 9:
								if (obj[j] != null
										&& obj[j].toString().trim().length() > 0) {
									MatchCount3 = Long.parseLong(obj[j].toString());
								}
								break;
							case 10:
								if (obj[j] != null
										&& obj[j].toString().trim().length() > 0) {
									SendCount2 = Long.parseLong(obj[j].toString());
								}
								break;
							case 11:
								if (obj[j] != null
										&& obj[j].toString().trim().length() > 0) {
									MatchCount2 = Long.parseLong(obj[j].toString());
								}
								break;
							case 12:
								if (obj[j] != null
										&& obj[j].toString().trim().length() > 0) {
									SendCount1 = Long.parseLong(obj[j].toString());
								}
								break;
							case 13:
								if (obj[j] != null
										&& obj[j].toString().trim().length() > 0) {
									MatchCount1 = Long.parseLong(obj[j].toString());
								}
								break;
							case 14:
								if (obj[j] != null
										&& obj[j].toString().trim().length() > 0) {
									cName = obj[j].toString();
								}
								break;
							}
						}
						float fstMatchPercentTmp = 0;
						float secMatchPercentTmp = 0;
						float thrMatchPercentTmp = 0;
						float fouMatchPercentTmp = 0;
						float fifMatchPercentTmp = 0;
						float sixMatchPercentTmp = 0;
						if (SendCount1 != 0) {
							fstMatchPercentTmp = (float) MatchCount1
									/ (float) SendCount1 * 100;
						}
						if (SendCount2 != 0) {
							secMatchPercentTmp = (float) MatchCount2
									/ (float) SendCount2 * 100;
						}
						if (SendCount3 != 0) {
							thrMatchPercentTmp = (float) MatchCount3
									/ (float) SendCount3 * 100;
						}
						if (SendCount4 != 0) {
							fouMatchPercentTmp = (float) MatchCount4
									/ (float) SendCount4 * 100;
						}
						if (SendCount5 != 0) {
							fifMatchPercentTmp = (float) MatchCount5
									/ (float) SendCount5 * 100;
						}
						if (SendCount6 != 0) {
							sixMatchPercentTmp = (float) MatchCount6
									/ (float) SendCount6 * 100;
						}
						fstMatchPercent = df.format(fstMatchPercentTmp) + "%";
						secMatchPercent = df.format(secMatchPercentTmp) + "%";
						thrMatchPercent = df.format(thrMatchPercentTmp) + "%";
						fouMatchPercent = df.format(fouMatchPercentTmp) + "%";
						fifMatchPercent = df.format(fifMatchPercentTmp) + "%";
						sixMatchPercent = df.format(sixMatchPercentTmp) + "%";
						result = new ResultParam(idCenter,idBank, cName, SendCount1,
								MatchCount1, SendCount2, MatchCount2, SendCount3,
								MatchCount3, SendCount4, MatchCount4, SendCount5,
								MatchCount5, SendCount6, MatchCount6,
								fstMatchPercent, secMatchPercent, thrMatchPercent,
								fouMatchPercent, fifMatchPercent, sixMatchPercent);
						resultList.add(result);
						
					}
				}
				if(selectCount!= null && selectCount.equals("countIdCenter")){
					String idCenter = "";
					String idBank = "";
					String cName = "";

					long SendCount1 = 0;
					long MatchCount1 = 0;
					long SendCount2 = 0;
					long MatchCount2 = 0;
					long SendCount3 = 0;
					long MatchCount3 = 0;
					long SendCount4 = 0;
					long MatchCount4 = 0;
					long SendCount5 = 0;
					long MatchCount5 = 0;
					long SendCount6 = 0;
					long MatchCount6 = 0;

					String fstMatchPercent = "";
					String secMatchPercent = "";
					String thrMatchPercent = "";
					String fouMatchPercent = "";
					String fifMatchPercent = "";
					String sixMatchPercent = "";
					for (int i = 0; i < list.size(); i++) {
						Object[] obj = (Object[]) list.get(i);
						for (int j = 0; j < obj.length; j++) {
							switch (j) {
							case 0:
								if (obj[j] != null
										&& obj[j].toString().trim().length() > 0) {
									idBank = obj[j].toString();
								}
								break;
							case 1:
								if (obj[j] != null
										&& obj[j].toString().trim().length() > 0) {
									SendCount6 = Long.parseLong(obj[j].toString());
								}
								break;
							case 2:
								if (obj[j] != null
										&& obj[j].toString().trim().length() > 0) {
									MatchCount6 = Long.parseLong(obj[j].toString());
								}
								break;
							case 3:
								if (obj[j] != null
										&& obj[j].toString().trim().length() > 0) {
									SendCount5 = Long.parseLong(obj[j].toString());
								}
								break;
							case 4:
								if (obj[j] != null
										&& obj[j].toString().trim().length() > 0) {
									MatchCount5 = Long.parseLong(obj[j].toString());
								}
								break;
							case 5:
								if (obj[j] != null
										&& obj[j].toString().trim().length() > 0) {
									SendCount4 = Long.parseLong(obj[j].toString());
								}
								break;
							case 6:
								if (obj[j] != null
										&& obj[j].toString().trim().length() > 0) {
									MatchCount4 = Long.parseLong(obj[j].toString());
								}
								break;
							case 7:
								if (obj[j] != null
										&& obj[j].toString().trim().length() > 0) {
									SendCount3 = Long.parseLong(obj[j].toString());
								}
								break;
							case 8:
								if (obj[j] != null
										&& obj[j].toString().trim().length() > 0) {
									MatchCount3 = Long.parseLong(obj[j].toString());
								}
								break;
							case 9:
								if (obj[j] != null
										&& obj[j].toString().trim().length() > 0) {
									SendCount2 = Long.parseLong(obj[j].toString());
								}
								break;
							case 10:
								if (obj[j] != null
										&& obj[j].toString().trim().length() > 0) {
									MatchCount2 = Long.parseLong(obj[j].toString());
								}
								break;
							case 11:
								if (obj[j] != null
										&& obj[j].toString().trim().length() > 0) {
									SendCount1 = Long.parseLong(obj[j].toString());
								}
								break;
							case 12:
								if (obj[j] != null
										&& obj[j].toString().trim().length() > 0) {
									MatchCount1 = Long.parseLong(obj[j].toString());
								}
								break;
							}
						}
						cName = idBranchNameMap.get(idBank);
						idCenter = idBank;
						float fstMatchPercentTmp = 0;
						float secMatchPercentTmp = 0;
						float thrMatchPercentTmp = 0;
						float fouMatchPercentTmp = 0;
						float fifMatchPercentTmp = 0;
						float sixMatchPercentTmp = 0;
						if (SendCount1 != 0) {
							fstMatchPercentTmp = (float) MatchCount1
									/ (float) SendCount1 * 100;
						}
						if (SendCount2 != 0) {
							secMatchPercentTmp = (float) MatchCount2
									/ (float) SendCount2 * 100;
						}
						if (SendCount3 != 0) {
							thrMatchPercentTmp = (float) MatchCount3
									/ (float) SendCount3 * 100;
						}
						if (SendCount4 != 0) {
							fouMatchPercentTmp = (float) MatchCount4
									/ (float) SendCount4 * 100;
						}
						if (SendCount5 != 0) {
							fifMatchPercentTmp = (float) MatchCount5
									/ (float) SendCount5 * 100;
						}
						if (SendCount6 != 0) {
							sixMatchPercentTmp = (float) MatchCount6
									/ (float) SendCount6 * 100;
						}
						fstMatchPercent = df.format(fstMatchPercentTmp) + "%";
						secMatchPercent = df.format(secMatchPercentTmp) + "%";
						thrMatchPercent = df.format(thrMatchPercentTmp) + "%";
						fouMatchPercent = df.format(fouMatchPercentTmp) + "%";
						fifMatchPercent = df.format(fifMatchPercentTmp) + "%";
						sixMatchPercent = df.format(sixMatchPercentTmp) + "%";
						result = new ResultParam(idCenter,idBank, cName, SendCount1,
								MatchCount1, SendCount2, MatchCount2, SendCount3,
								MatchCount3, SendCount4, MatchCount4, SendCount5,
								MatchCount5, SendCount6, MatchCount6,
								fstMatchPercent, secMatchPercent, thrMatchPercent,
								fouMatchPercent, fifMatchPercent, sixMatchPercent);
						resultList.add(result);
						
					}
				}
				if(selectCount!= null && selectCount.equals("countIdBranch")){
					String idCenter = FinalConstant.ROOTORG;
					String idBank = FinalConstant.ROOTORG;
					String cName = "华融湘江总行";

					long SendCount1 = 0;
					long MatchCount1 = 0;
					long SendCount2 = 0;
					long MatchCount2 = 0;
					long SendCount3 = 0;
					long MatchCount3 = 0;
					long SendCount4 = 0;
					long MatchCount4 = 0;
					long SendCount5 = 0;
					long MatchCount5 = 0;
					long SendCount6 = 0;
					long MatchCount6 = 0;

					String fstMatchPercent = "";
					String secMatchPercent = "";
					String thrMatchPercent = "";
					String fouMatchPercent = "";
					String fifMatchPercent = "";
					String sixMatchPercent = "";
					for (int i = 0; i < list.size(); i++) {
						Object[] obj = (Object[]) list.get(i);
						for (int j = 0; j < obj.length; j++) {
							switch (j) {
							case 0:
								if (obj[j] != null
										&& obj[j].toString().trim().length() > 0) {
									SendCount6 = Long.parseLong(obj[j].toString());
								}
								break;
							case 1:
								if (obj[j] != null
										&& obj[j].toString().trim().length() > 0) {
									MatchCount6 = Long.parseLong(obj[j].toString());
								}
								break;
							case 2:
								if (obj[j] != null
										&& obj[j].toString().trim().length() > 0) {
									SendCount5 = Long.parseLong(obj[j].toString());
								}
								break;
							case 3:
								if (obj[j] != null
										&& obj[j].toString().trim().length() > 0) {
									MatchCount5 = Long.parseLong(obj[j].toString());
								}
								break;
							case 4:
								if (obj[j] != null
										&& obj[j].toString().trim().length() > 0) {
									SendCount4 = Long.parseLong(obj[j].toString());
								}
								break;
							case 5:
								if (obj[j] != null
										&& obj[j].toString().trim().length() > 0) {
									MatchCount4 = Long.parseLong(obj[j].toString());
								}
								break;
							case 6:
								if (obj[j] != null
										&& obj[j].toString().trim().length() > 0) {
									SendCount3 = Long.parseLong(obj[j].toString());
								}
								break;
							case 7:
								if (obj[j] != null
										&& obj[j].toString().trim().length() > 0) {
									MatchCount3 = Long.parseLong(obj[j].toString());
								}
								break;
							case 8:
								if (obj[j] != null
										&& obj[j].toString().trim().length() > 0) {
									SendCount2 = Long.parseLong(obj[j].toString());
								}
								break;
							case 9:
								if (obj[j] != null
										&& obj[j].toString().trim().length() > 0) {
									MatchCount2 = Long.parseLong(obj[j].toString());
								}
								break;
							case 10:
								if (obj[j] != null
										&& obj[j].toString().trim().length() > 0) {
									SendCount1 = Long.parseLong(obj[j].toString());
								}
								break;
							case 11:
								if (obj[j] != null
										&& obj[j].toString().trim().length() > 0) {
									MatchCount1 = Long.parseLong(obj[j].toString());
								}
								break;
							}
						}
						float fstMatchPercentTmp = 0;
						float secMatchPercentTmp = 0;
						float thrMatchPercentTmp = 0;
						float fouMatchPercentTmp = 0;
						float fifMatchPercentTmp = 0;
						float sixMatchPercentTmp = 0;
						if (SendCount1 != 0) {
							fstMatchPercentTmp = (float) MatchCount1
									/ (float) SendCount1 * 100;
						}
						if (SendCount2 != 0) {
							secMatchPercentTmp = (float) MatchCount2
									/ (float) SendCount2 * 100;
						}
						if (SendCount3 != 0) {
							thrMatchPercentTmp = (float) MatchCount3
									/ (float) SendCount3 * 100;
						}
						if (SendCount4 != 0) {
							fouMatchPercentTmp = (float) MatchCount4
									/ (float) SendCount4 * 100;
						}
						if (SendCount5 != 0) {
							fifMatchPercentTmp = (float) MatchCount5
									/ (float) SendCount5 * 100;
						}
						if (SendCount6 != 0) {
							sixMatchPercentTmp = (float) MatchCount6
									/ (float) SendCount6 * 100;
						}
						fstMatchPercent = df.format(fstMatchPercentTmp) + "%";
						secMatchPercent = df.format(secMatchPercentTmp) + "%";
						thrMatchPercent = df.format(thrMatchPercentTmp) + "%";
						fouMatchPercent = df.format(fouMatchPercentTmp) + "%";
						fifMatchPercent = df.format(fifMatchPercentTmp) + "%";
						sixMatchPercent = df.format(sixMatchPercentTmp) + "%";
						result = new ResultParam(idCenter,idBank, cName, SendCount1,
								MatchCount1, SendCount2, MatchCount2, SendCount3,
								MatchCount3, SendCount4, MatchCount4, SendCount5,
								MatchCount5, SendCount6, MatchCount6,
								fstMatchPercent, secMatchPercent, thrMatchPercent,
								fouMatchPercent, fifMatchPercent, sixMatchPercent);
						resultList.add(result);
						
					}
				}
				
			}
		} catch (XDocProcException e) {
			logger.error("对账率统计查询数据库错误", e);
			return null;
		}

		return resultList;
	}
	public ResultParam getResult() {
		return result;
	}
	public void setResult(ResultParam result) {
		this.result = result;
	}
	public List<ResultParam> getResultList() {
		return resultList;
	}
	public void setResultList(List<ResultParam> resultList) {
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
		EbillMatchAnalyseBizImpl.logger = logger;
	}
	public static java.text.DecimalFormat getDf() {
		return df;
	}
	
}
