package com.yzj.ebs.netquery.biz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yzj.ebs.common.IBasicInfoAdm;
import com.yzj.ebs.common.IPublicTools;
import com.yzj.ebs.common.RefTableTools;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.netquery.param.NetResultParam;
import com.yzj.wf.core.model.po.wrapper.XPeopleInfo;

/**
 * 创建于:2013-04-01<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 网银对账情况统计 业务实现
 * 
 * @author 单伟龙
 * @version 1.0.0
 */
public class NetQueryBiz implements INetQueryBiz {

	List<NetResultParam> resultList = new ArrayList<NetResultParam>();
	List<NetResultParam> exportList = new ArrayList<NetResultParam>();
	public NetResultParam result;

	public IBasicInfoAdm basicinfoAdm;
	public IPublicTools tools;
	private Map<String,String> currencyMap = new HashMap<String,String>();

	/**
	 * @return list<NetResultParam>
	 * @param queryMap
	 *            构造查询参数 param 分页参数，entityName 表名，isPaged 是否分页
	 * @throws XDocProcException
	 */
	@Override
	public List<NetResultParam> getNetQueryData(Map<String, String> queryMap,
			PageParam param, String entityName, boolean isPaged)
			throws XDocProcException {
		// TODO Auto-generated method stub

		try {
			XPeopleInfo people = tools.getCurrLoginPeople();
			if (people == null) {
				throw new XDocProcException("会话超时，请重新登录！");
			}
            //清空list，避免多次点击数据累加
			resultList.clear();
			exportList.clear();
			getCurrencyMap();//币种map
			List<?> list;
			if (isPaged) {
				list = null;
				list = basicinfoAdm.getAllData(queryMap, entityName, param);
			} else {
				list = null;
				list = basicinfoAdm.getOneData(queryMap, entityName);
			}

			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
                    String idCenter = "";
					String idBank = "";
					String bankName = "";
					String accNo = "";
					String accSon = "";
					String sendMode = "";
					Double credit = 0.0;
					String docDate = "";
					String currency = "";
					String curr = "";
					String accName = "";
					String checkFlag = "";
					String billcount = "";

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
								bankName = obj[j].toString();
							}
							break;
						case 3:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								accNo = obj[j].toString();
							}
							break;
						case 4:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								accSon = obj[j].toString();
							}
							break;
						case 5:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								sendMode = obj[j].toString();
							}
							break;
						case 6:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								accName = obj[j].toString();
							}
							break;
						case 7:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								docDate = obj[j].toString();
							}
							break;
						case 8:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								curr = obj[j].toString();
								currency = currencyMap.get(curr);
							}
							break;
						case 9:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								credit = Double.parseDouble(obj[j].toString());
							}
							break;
						case 10:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								checkFlag = obj[j].toString();
							}
							break;
						case 11:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								billcount = obj[j].toString();
							}
							break;
						}

					}
					result = new NetResultParam(idCenter,idBank, bankName, accNo,
							accSon, sendMode, accName, docDate, currency,
							credit, checkFlag, billcount);
					resultList.add(result);
					exportList.add(result);

				}
			}
			if (isPaged) {
				return resultList;
			} else {
				return exportList;
			}
		} catch (XDocProcException e) {
			// TODO Auto-generated catch block
			throw new XDocProcException(e.getMessage());
		}

	}

	public List<NetResultParam> getResultList() {
		return resultList;
	}

	public void setResultList(List<NetResultParam> resultList) {
		this.resultList = resultList;
	}

	public List<NetResultParam> getExportList() {
		return exportList;
	}

	public void setExportList(List<NetResultParam> exportList) {
		this.exportList = exportList;
	}

	public NetResultParam getResult() {
		return result;
	}

	public void setResult(NetResultParam result) {
		this.result = result;
	}

	public IBasicInfoAdm getBasicinfoAdm() {
		return basicinfoAdm;
	}

	public void setBasicinfoAdm(IBasicInfoAdm basicinfoAdm) {
		this.basicinfoAdm = basicinfoAdm;
	}

	public IPublicTools getTools() {
		return tools;
	}

	public void setTools(IPublicTools tools) {
		this.tools = tools;
	}

	public Map<String, String> getCurrencyMap() {
		if (null == currencyMap || currencyMap.size() == 0) {
			currencyMap = RefTableTools.valParamCurrtypeMap;
		}
		return currencyMap;
	}

	public void setCurrencyMap(Map<String, String> currencyMap) {
		this.currencyMap = currencyMap;
	}
   
}
