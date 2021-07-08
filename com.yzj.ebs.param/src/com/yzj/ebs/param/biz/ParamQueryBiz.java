package com.yzj.ebs.param.biz;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.yzj.ebs.common.IAccnoDetailAdm;
import com.yzj.ebs.common.IPublicTools;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.database.AccNoDetailData;
import com.yzj.ebs.param.queryparam.AccnoDetailQueryParam;
import com.yzj.wf.core.model.po.wrapper.XPeopleInfo;

/**
 * 创建于:2013-04-01<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 发生额明细查询 业务实现
 * 
 * @author 单伟龙
 * @version 1.0.0
 */
public class ParamQueryBiz implements IParamQueryBiz {
	private IAccnoDetailAdm accnoDetailAdm;
	private IPublicTools tools;
	private List<Object[]> queryList = new ArrayList<Object[]>();

	// 查询全量数据方法实现
	@Override
	public List<AccNoDetailData> getAccnoDetailDataByDocDate(
			Map<String, String> queryMap,
			AccnoDetailQueryParam accnoDetailQueryParam, String entityName,
			String workdate, boolean isPaged) throws XDocProcException {
		// TODO Auto-generated method stub
		try {
			queryList.clear();
			XPeopleInfo people = tools.getCurrLoginPeople();
			if (people == null) {
				throw new XDocProcException("会话超时，请重新登录！");
			}
			queryList = accnoDetailAdm.getAccnoDetailDataByDocDate(queryMap,
					accnoDetailQueryParam, entityName, workdate, isPaged);
			return typeChange(queryList);

		} catch (XDocProcException e) {
			// TODO Auto-generated catch block
			throw new XDocProcException(e.getMessage());
		}
	}

	
	/**
	 *  把 list<Object[]>转化成 List<AccNoDetailData>
	 * @return
	 */
	public List<AccNoDetailData> typeChange(List<Object[]> resultList){
		List<AccNoDetailData> accList = new ArrayList<AccNoDetailData>();
		int count = resultList.size();
		for(int i=0;i<count;i++){
			// workdate, tracebal, dcflag, to_accno,to_accname,credit,idbank,traceno
			AccNoDetailData accNo = new AccNoDetailData();
			Object[] ob = resultList.get(i);
			accNo.setWorkDate((String)ob[0]);
			accNo.setTraceBal( Double.valueOf(ob[1].toString()));
			accNo.setDcFlag((String)ob[2]);
			accNo.setTo_Accno((String)ob[3]);
			accNo.setTo_Accname((String)ob[4]);
			accNo.setCredit(Double.valueOf(ob[5].toString()));
			accNo.setIdBank((String)ob[6]);
			accNo.setTraceNo(Long.valueOf(ob[7].toString()));
			accList.add(accNo);
		}
		return accList;
	}
	
	public IAccnoDetailAdm getAccnoDetailAdm() {
		return accnoDetailAdm;
	}

	public void setAccnoDetailAdm(IAccnoDetailAdm accnoDetailAdm) {
		this.accnoDetailAdm = accnoDetailAdm;
	}

	public IPublicTools getTools() {
		return tools;
	}

	public void setTools(IPublicTools tools) {
		this.tools = tools;
	}
	
}