package com.yzj.ebs.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzj.ebs.common.ICheckMainDataAdm;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.database.AccNoMainData;
import com.yzj.ebs.database.CheckMainData;
import com.yzj.ebs.database.DocSet;
import com.yzj.ebs.util.FinalConstant;

/**
 * 创建于:2012-9-29<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * Basicinfo表操作访问服务接口定义
 * 
 * @author 秦靖锋，施江敏
 * @version 1.0.0
 */
public class CheckMainDataAdm extends BaseService<CheckMainData> implements
		ICheckMainDataAdm {

	//---------------------------------二期报表开发begin-----------------------------------
	
	//验印情况统计(按账号统计)
	@Override
	public List<?> getProveReportListCount(Map<String, String> queryMap,
			PageParam pageParam, boolean isPaged, String selectCount) throws XDocProcException {
		String sql = "";
		String selectSql = "select ";
		if(selectCount!=null && selectCount.equals("countIdCenter")){
			selectSql += "t.idCenter,";
		}
		if(selectCount!=null && selectCount.equals("countIdBank")){
			selectSql += "t.idCenter,t.idBranch,t.idBank,t.bankName,";
		}
		
		selectSql +=  "count(case when t.sendmode!='3' and t.docstate > 1 then a.accno end) as sendCount,"  //对账单发出数
					+ "count(case when t.sendmode!='3' and t.docstate > 1 and t.proveflag ='20' then t.voucherno end) as autoCount,"  //auto验印成功数
					+ "count(case when t.sendmode!='3' and t.docstate > 1 and t.proveflag in ('22','31') then t.voucherno end) as manuCount,"  //manu验印成功数
					+ "count(case when t.sendmode!='3' and t.docstate > 1 and t.proveflag in ('40','41','42','43','44','51') then t.voucherno end) as notPassCount, "//验印不成功数
					+ "count(case when t.sendmode!='3' and t.docstate > 1 and (t.proveflag='0' or t.proveflag is null) then t.voucherno end) as notProve "//尚未验印数
					+ "from autek.ebs_checkmaindata t left join autek.ebs_accnomaindata a on (t.voucherno=a.voucherno) where 1=1 ";
					//+ "from autek.ebs_checkmaindata t,autek.ebs_accnomaindata a where 1=1 and t.voucherno=a.voucherno ";
		if (queryMap != null) {
			for (Map.Entry<String, String> entry : queryMap.entrySet()) {
				if (entry.getValue() != null
						&& entry.getValue().trim().length() != 0) {
					if(entry.getKey() != null && "docDate".equals(entry.getKey())){//日期区间查询
						selectSql += " and " + entry.getValue();
					}else{
						selectSql += " and t." + entry.getKey() + "='" + entry.getValue().trim() + "' ";
					}
				}
			}
		}
		String groupSql = "";
		String orderSql = "";
		if(selectCount!=null && selectCount.equals("countIdCenter")){
			groupSql += " group by t.idcenter" ;
			orderSql = " order by t.idcenter" ;
		}
		if(selectCount!=null && selectCount.equals("countIdBank")){
			groupSql += " group by t.idCenter,t.idBranch,t.idBank,t.bankName " ;
			orderSql = " order by t.idcenter,t.idbank";
		}
		sql = selectSql + groupSql + orderSql;
		System.out.println("验印情况统计sql:"+sql);
		if(isPaged){
			Integer countNumber = dao.findBySql(sql).size();
			int pageSize = pageParam.getPageSize();// 每页显示结果条数
			int totalPage = (int) ((countNumber - 1) / pageSize + 1);// 总页数
			int curPage = pageParam.getCurPage();// 当前要显示的页
			if (curPage > totalPage) {
				curPage = totalPage;
			}
			int firstResult = (curPage - 1) * pageSize;// 分页时显示的第一条记录，默认从0开始
			List<?> list = dao.findBySql(sql, firstResult, pageSize);
			pageParam.setFirstResult(firstResult);
			pageParam.setCurPage(curPage);
			pageParam.setTotal((int) countNumber);
			pageParam.setTotalPage(totalPage);
			pageParam.setLastResult(firstResult + list.size());// 当前页显示的最后一条记录
			return list;
		}else{
			return dao.findBySql(sql);
		}
	}
	
	//验印情况账户明细
	@Override
	public List<?> getProveAccDetailReportList(Map<String, String> queryMap,
			PageParam pageParam, boolean isPaged) throws XDocProcException {
		String sql = "";
		String selectSql = "select t.idCenter,t.idBranch,t.idBank,t.bankName,a.accno,t.accname,"
				+ "count(case when t.sendmode!='3' and t.docstate > 1 then a.accno end) as sendCount,"  //对账单发出数
				+ "count(case when t.sendmode!='3' and t.docstate > 1 and t.proveflag ='20' then t.voucherno end) as autoCount,"  //auto验印成功数
				+ "count(case when t.sendmode!='3' and t.docstate > 1 and t.proveflag in ('22','31') then t.voucherno end) as manuCount,"  //manu验印成功数
				+ "count(case when t.sendmode!='3' and t.docstate > 1 and t.proveflag in ('40','41','42','43','44','51') then t.voucherno end) as proveNotMatchCount, "//验印不成功数
				+ "count(case when t.sendmode!='3' and t.docstate > 1 and (t.proveflag='0' or t.proveflag is null) then t.voucherno end) as notProve "//尚未验印数
				+ "from autek.ebs_checkmaindata t left join autek.ebs_accnomaindata a on (t.voucherno=a.voucherno) where 1=1 ";
		if (queryMap != null) {
			for (Map.Entry<String, String> entry : queryMap.entrySet()) {
				if (entry.getValue() != null
						&& entry.getValue().trim().length() != 0) {
					if(entry.getKey() != null && "docDate".equals(entry.getKey())){//日期区间查询
						selectSql += " and " + entry.getValue();
					}else{
						selectSql += " and t." + entry.getKey() + "='" + entry.getValue().trim() + "' ";
					}
				}
			}
		}
		String groupSql = " group by a.accno,t.accname,t.idCenter,t.idBranch,t.idBank,t.bankName ";
		String orderSql = " order by t.idcenter,t.idbank";

		sql = selectSql + groupSql + orderSql;
		System.out.println("验印情况账户明细sql:"+sql);
		if(isPaged){
			Integer countNumber = dao.findBySql(sql).size();
			int pageSize = pageParam.getPageSize();// 每页显示结果条数
			int totalPage = (int) ((countNumber - 1) / pageSize + 1);// 总页数
			int curPage = pageParam.getCurPage();// 当前要显示的页
			if (curPage > totalPage) {
				curPage = totalPage;
			}
			int firstResult = (curPage - 1) * pageSize;// 分页时显示的第一条记录，默认从0开始
			List<?> list = dao.findBySql(sql, firstResult, pageSize);
			pageParam.setFirstResult(firstResult);
			pageParam.setCurPage(curPage);
			pageParam.setTotal((int) countNumber);
			pageParam.setTotalPage(totalPage);
			pageParam.setLastResult(firstResult + list.size());// 当前页显示的最后一条记录
			return list;
		}else{
			return dao.findBySql(sql);
		}
	}
		
	//按单位进行机构率统计
	@Override
	public List<?> getEbillReportList(Map<String, String> queryMap,
			PageParam pageParam, boolean isPaged, String selectCount)
			throws XDocProcException {
		String sql = "";
		String selectSql = "select ";
		if(selectCount!=null && selectCount.equals("countIdCenter")){
			selectSql += "t1.idCenter, ";
		}
		if(selectCount!=null && selectCount.equals("countIdBank")){
			selectSql += "t1.idCenter,t1.idBank,t1.bankName,";
		}
		selectSql +=  "count(t2.accno) as sendCount," // 总账户数
					+ "count(case when t1.docstate='3' then t2.accno end) as backCount," // 回收数
					+ "count(case when t1.docstate>1 and t2.finalCheckflag in('3','4') then t2.accno end) as checkSuccessCount," // 余额相符数
					+ "count(case when t1.docstate>1 and t2.finalCheckflag in('2','5') then t2.accno end) as checkFailCount, " // 余额不符数
					+ "count(case when t1.docstate>1 and (t2.finalCheckflag='1' or t2.finalCheckflag is null) then t2.accno end) as notCheckCount, " // 尚未核对数
					+ "count(case when t1.docstate>1 and ((t1.proveflag in('20','22','31') and t2.finalCheckflag in('3','4')) " 
					+ "or (t2.sendmode ='3' and t2.finalcheckflag in ('3','4'))) then t2.accno end) as ebillSuccessCount " // 对账成功数
					+ "from autek.ebs_checkmaindata t1 left join autek.ebs_accnomaindata t2 on (t1.voucherno=t2.voucherno) where 1=1 ";
		
		//遍历查询条件
		if (queryMap != null) {
			for (Map.Entry<String, String> entry : queryMap.entrySet()) {
				if (entry.getValue() != null
						&& entry.getValue().trim().length() != 0) {
					if(entry.getKey() != null && "docDate".equals(entry.getKey())){//日期区间查询
						selectSql += " and " + entry.getValue();
					}else if("5".equals(entry.getValue().trim()) 
							&& "sendMode".equals(entry.getKey())){//余额 (邮寄+柜台+网银)
						selectSql += " and t2.sendMode in ('1','2','3') ";
					}else{
						selectSql += " and t2." + entry.getKey() + "='" + entry.getValue().trim() + "' ";
					}
					
				}
			}
		}
		
		String groupSql = "";
		String orderSql = "";
		if(selectCount!=null && selectCount.equals("countIdCenter")){
			groupSql += " group by t1.idCenter ";
			orderSql += " order by t1.idCenter ";
		}
		if(selectCount!=null && selectCount.equals("countIdBank")){
			groupSql += " group by t1.idCenter,t1.idBranch,t1.idBank,t1.bankName " ;
			orderSql = " order by t1.idcenter,t1.idbank";
		}
		sql = selectSql + groupSql +orderSql;
		System.out.println("机构率统计sql:"+sql);
		List<?> list = null;
		if(isPaged){
			Integer countNumber = dao.findBySql(sql).size();
			int pageSize = pageParam.getPageSize();// 每页显示结果条数
			int totalPage = (int) ((countNumber - 1) / pageSize + 1);// 总页数
			int curPage = pageParam.getCurPage();// 当前要显示的页
			if (curPage > totalPage) {
				curPage = totalPage;
			}
			int firstResult = (curPage - 1) * pageSize;// 分页时显示的第一条记录，默认从0开始
			list = dao.findBySql(sql, firstResult, pageSize);
			pageParam.setFirstResult(firstResult);
			pageParam.setCurPage(curPage);
			pageParam.setTotal((int) countNumber);
			pageParam.setTotalPage(totalPage);
			pageParam.setLastResult(firstResult + list.size());// 当前页显示的最后一条记录
			return list;	
		}else{
			list = dao.findBySql(sql);
			return list;
		}
	}
	//---------------------------------二期报表开发end-------------------------------------
		
		
		
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public boolean upDateCheckmaindata(DocSet docSet) throws XDocProcException {
		String szSql = "update  CheckMainData set docState='3', workDate=?,proveFlag=?, frontImagePath=?, backImagePath=?  where voucherNo =?";
		List<Object> params = new ArrayList<Object>();
		params.add(docSet.getWorkDate());
		params.add((docSet.getProveFlag()==null)?"":docSet.getProveFlag().toString());
		params.add(docSet.getFrontImagePath());
		params.add(docSet.getBackImagePath());
		params.add(docSet.getVoucherNo());
		try {
			super.ExecQueryByParam(szSql, params);
			String sql = "update AccNoMainData set finalCheckFlag=checkFlag,matchFlag=(case when checkFlag in ('2','4','5') then 1 else 0 end)  where voucherNo='"
					+ docSet.getVoucherNo() + "'";
			super.ExecQuery(sql);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new XDocProcException("自动记账发生异常" + e.getMessage());
		}

	}

	@Override
	public CheckMainData queryOneByVoucherNo(String voucherno)
			throws XDocProcException {

		CheckMainData checkMainData = null;
		List<?> list = dao
				.findByHql("from CheckMainData c ,AccNoMainData a where a.voucherNo=c.voucherNo and c.voucherNo='"
						+ voucherno.trim() + "'" + " order by a.accNoIndex ");
		//List<CheckMainData> resultList = new ArrayList<CheckMainData>();
		if (list != null && list.size() != 0) {

			for (int i = 0; i < list.size(); i++) {
				Object[] obj = (Object[]) list.get(i);
				if (checkMainData == null) {
					checkMainData = (CheckMainData) obj[0];
					List<AccNoMainData> accNoMainDataList = new ArrayList<AccNoMainData>();
					checkMainData.setAccNoMainDataList(accNoMainDataList);
				}
				AccNoMainData accNoMainData = (AccNoMainData) obj[1];
				checkMainData.getAccNoMainDataList().add(accNoMainData);

			}
			return checkMainData;
		} else {
			return null;
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public CheckMainData updateCheckMainData(CheckMainData checkMainData)
			throws XDocProcException {
		super.update(checkMainData);
		return checkMainData;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CheckMainData> getCheckMainData(Map<String, String> queryMap,
			PageParam param) throws XDocProcException {
		String hql = "from CheckMainData where 1=1 ";
		String queryCount = "select count(*) from CheckMainData where 1=1 ";
		String szBr = "";
		if (queryMap != null) {
			for (Map.Entry<String, String> entry : queryMap.entrySet()) {
				if (entry.getValue() != null
						&& entry.getValue().trim().length() != 0) {
					if (entry.getKey().equals("idBank")
							|| entry.getKey().equals("idBranch")) {
						if (szBr.equals("")) {
							szBr = entry.getKey() + "='" + entry.getValue().trim() 
									+ "'";
						} else {
							szBr = szBr + " and " + entry.getKey() + "='"
									+ entry.getValue().trim()  + "'";
						}
					} else if("5".equals(entry.getValue().trim()) 
							&& "sendMode".equals(entry.getKey())){
						hql += " and sendMode is null ";
						queryCount += " and sendMode is null ";
					} else if("5".equals(entry.getValue().trim()) 
							&& "proveFlag".equals(entry.getKey())){
						hql += "and proveFlag is null ";
						queryCount += "and proveFlag is null ";
					} else {
						hql += " and " + entry.getKey() + "='"
								+ entry.getValue().trim()  + "'";
						queryCount += " and " + entry.getKey().trim()  + "='"
								+ entry.getValue().trim()  + "'";
					}
				}
			}
		}
		if (!szBr.equals("")) {
			hql += " and voucherNo in (select  voucherNo from AccNoMainData where "
					+ szBr + ")";
			queryCount += " and voucherNo in (select  voucherNo from AccNoMainData where "
					+ szBr + ")";
		}
		List<CheckMainData> resultList = (List<CheckMainData>) dao
				.getPageByParamForSql(hql, queryCount, param);
		return resultList;
	}

	@Override
	public List<AccNoMainData> getCheckAccMainData(
			Map<String, String> queryMap, PageParam pageParam, String accNo)
			throws XDocProcException {
		String hql = "from AccNoMainData a,CheckMainData c where a.voucherNo=c.voucherNo ";
		String queryCountHql = "select count(*) from AccNoMainData a,CheckMainData c where a.voucherNo=c.voucherNo ";
		if (queryMap != null) {
			for (Map.Entry<String, String> entry : queryMap.entrySet()) {
				if (entry.getValue() != null
						&& entry.getValue().trim().length() != 0) {
					if (entry.getKey().equals("idBank")
							|| entry.getKey().equals("idCenter")) {
						hql += " and a." + entry.getKey() + "='"
								+ entry.getValue().trim()  + "'";
						queryCountHql += " and a." + entry.getKey() + "='"
								+ entry.getValue().trim()  + "'";
					}else if("faceFlag".equals(entry.getKey())){
						hql += "and a.faceFlag="+entry.getValue();
						queryCountHql += "and a.faceFlag="+entry.getValue();
					}else if("5".equals(entry.getValue().trim()) 
							&& "sendMode".equals(entry.getKey())){
						hql += "and a.sendMode is null ";
						queryCountHql += "and a.sendMode is null ";
					}else if("5".equals(entry.getValue().trim()) 
							&& "proveFlag".equals(entry.getKey())){
						hql += "and c.proveFlag is null ";
						queryCountHql += "and c.proveFlag is null ";
					}else if("checkResult".equals(entry.getKey())){
						if("1".equals(entry.getValue().trim())){//对账成功
							hql += "and ((c.proveFlag in('20','22','31') and a.finalCheckFlag in('3','4')) or (a.sendMode ='3' and a.finalCheckFlag in ('3','4')) ) ";
							queryCountHql += "and ((c.proveFlag in('20','22','31') and a.finalCheckFlag in('3','4')) or (a.sendMode ='3' and a.finalCheckFlag in ('3','4')) ) ";
						}else{//对账不成功
							hql += "and (((a.sendMode !='3' or a.sendMode is null) and (a.finalCheckFlag in ('1','2','5') or a.finalCheckFlag is null or c.proveFlag in ('0','40','41','42','43','44','51') or c.proveFlag is null)) "
								+ "	or (a.sendMode ='3' and (a.finalCheckFlag in ('1','2','5') or a.finalCheckFlag is null))) ";
							queryCountHql += "and (((a.sendMode !='3' or a.sendMode is null) and (a.finalCheckFlag in ('1','2','5') or a.finalCheckFlag is null or c.proveFlag in ('0','40','41','42','43','44','51') or c.proveFlag is null)) "
								+ "	or (a.sendMode ='3' and (a.finalCheckFlag in ('1','2','5') or a.finalCheckFlag is null))) ";
						}
					}else {
						hql += " and c." + entry.getKey() + "='"
								+ entry.getValue().trim()  + "'";
						queryCountHql += " and c." + entry.getKey() + "='"
								+ entry.getValue().trim() + "'";
					}
				}
			}
		}
		if (StringUtils.isNotEmpty(accNo)) {
			hql += " and a.accNo='" + accNo.trim()  + "'";
			queryCountHql += " and a.accNo='" + accNo.trim()  + "'";
		}
		hql += " order by c.voucherNo";

		List<?> list = dao.getPageByParamForSql(hql,queryCountHql,pageParam);
		List<AccNoMainData> resultList = new ArrayList<AccNoMainData>();
		if (list != null && list.size() != 0) {
			for (int i = 0; i < list.size(); i++) {
				Object[] obj = (Object[]) list.get(i);
				AccNoMainData accNoMainData = (AccNoMainData) obj[0];
				CheckMainData checkMainData = (CheckMainData) obj[1];
				accNoMainData.setCheckMainData(checkMainData);
				// 对账结果：余额相符并且验印通过才是对账成功
				//(c.proveflag in('20','22','31') and a.finalCheckflag in('3','4')) or (a.sendmode ='3' and a.finalcheckflag in ('3','4'))
				if ( ("3".equals(accNoMainData.getSendMode()) && ("3".equals(accNoMainData.getFinalCheckFlag()) || "4".equals(accNoMainData.getFinalCheckFlag())))
						|| (("20".equals(checkMainData.getProveFlag()) || "22".equals(checkMainData.getProveFlag()) || "31".equals(checkMainData.getProveFlag()) )
								&& ("3".equals(accNoMainData.getFinalCheckFlag()) || "4" .equals(accNoMainData.getFinalCheckFlag()))) ){
					accNoMainData.setResult("对账成功");
				} else if (((checkMainData.getProveFlag()==null || "0".equals(checkMainData.getProveFlag())) && !"3".equals(accNoMainData.getSendMode()))
							|| (accNoMainData.getFinalCheckFlag()==null || "1".equals(accNoMainData.getFinalCheckFlag()))) {
					accNoMainData.setResult("尚未对账");
				} else {
					accNoMainData.setResult("对账失败");
				}
				resultList.add(accNoMainData);
			}
			return resultList;
		} else {
			return null;
		}
	}

	@Override
	public List<CheckMainData> getAllCheckMainData(Map<String, String> queryMap)
			throws XDocProcException {
		String hql = "from CheckMainData where 1=1 ";
		String szBr = "";
		if (queryMap != null) {
			for (Map.Entry<String, String> entry : queryMap.entrySet()) {
				if (entry.getValue() != null
						&& entry.getValue().trim().length() != 0) {
					if (entry.getKey().equals("idBank")
							|| entry.getKey().equals("idBranch")) {
						if (szBr.equals("")) {
							szBr = entry.getKey() + "='" + entry.getValue().trim() 
									+ "'";
						} else {
							szBr = szBr + " and " + entry.getKey() + "='"
									+ entry.getValue().trim()  + "'";
						}
					} else if("5".equals(entry.getValue().trim()) 
							&& "sendMode".equals(entry.getKey())){
						hql += " and sendMode is null ";
					} else if("5".equals(entry.getValue().trim()) 
							&& "proveFlag".equals(entry.getKey())){
						hql += "and proveFlag is null ";
					} else {
						hql += " and " + entry.getKey() + "='"
								+ entry.getValue().trim()  + "'";
					}
				}
			}
		}
		if (!szBr.equals("")) {
			hql += " and voucherNo in (select  voucherNo from AccNoMainData where "
					+ szBr + ")";
		}
		List<?> list = dao.findByHql(hql);
		List<CheckMainData> resultList = new ArrayList<CheckMainData>();
		if (list != null && list.size() != 0) {
			for (int i = 0; i < list.size(); i++) {
				CheckMainData checkMainData = (CheckMainData) list.get(i);
				resultList.add(checkMainData);
			}
			return resultList;
		} else {
			return null;
		}

	}

	@Override
	public List<AccNoMainData> getAllAccCheckMainData(
			Map<String, String> queryMap, String accNo)
			throws XDocProcException {

		String hql = "from AccNoMainData a, CheckMainData c where a.voucherNo=c.voucherNo ";

		if (queryMap != null) {
			for (Map.Entry<String, String> entry : queryMap.entrySet()) {
				if (entry.getValue() != null
						&& entry.getValue().trim().length() != 0) {
					if("5".equals(entry.getValue().trim()) 
							&& "sendMode".equals(entry.getKey())){
						hql += "and a.sendMode is null ";
					}else if("faceFlag".equals(entry.getKey())){
						hql += "and a.faceFlag="+entry.getValue();
					}else if("5".equals(entry.getValue().trim()) 
							&& "proveFlag".equals(entry.getKey())){
						hql += "and c.proveFlag is null ";
					}else if("checkResult".equals(entry.getKey())){
						if("1".equals(entry.getValue().trim())){//对账成功
							hql += "and ((c.proveFlag in('20','22','31') and a.finalCheckFlag in('3','4')) or (a.sendMode ='3' and a.finalCheckFlag in ('3','4')) ) ";
						}else{//对账不成功
							hql += "and (((a.sendMode !='3' or a.sendMode is null) and (a.finalCheckFlag in ('1','2','5') or a.finalCheckFlag is null or c.proveFlag in ('0','40','41','42','43','44','51') or c.proveFlag is null)) "
								+ "	or (a.sendMode ='3' and (a.finalCheckFlag in ('1','2','5') or a.finalCheckFlag is null))) ";
						}
					}else{
						hql += " and c." + entry.getKey() + "='" + entry.getValue().trim() + "'";
					}
				}
			}
		}

		if (StringUtils.isNotEmpty(accNo)) {
			hql += " and a.accNo='" + accNo.trim() + "'";
		}

		List<?> list = dao.findByHql(hql);

		List<AccNoMainData> resultList = new ArrayList<AccNoMainData>();
		if (list != null && list.size() != 0) {
			for (int i = 0; i < list.size(); i++) {
				Object[] obj = (Object[]) list.get(i);
				AccNoMainData accNoMainData = (AccNoMainData) obj[0];
				CheckMainData checkMainData = (CheckMainData) obj[1];
				accNoMainData.setCheckMainData(checkMainData);
				// 对账结果：余额相符并且验印通过才是对账成功
				//(c.proveflag in('20','22','31') and a.finalCheckflag in('3','4')) or (a.sendmode ='3' and a.finalcheckflag in ('3','4'))
				if ( ("3".equals(accNoMainData.getSendMode()) && ("3".equals(accNoMainData.getFinalCheckFlag()) || "4".equals(accNoMainData.getFinalCheckFlag())))
						|| (("20".equals(checkMainData.getProveFlag()) || "22".equals(checkMainData.getProveFlag()) || "31".equals(checkMainData.getProveFlag()) )
								&& ("3".equals(accNoMainData.getFinalCheckFlag()) || "4" .equals(accNoMainData.getFinalCheckFlag()))) ){
					accNoMainData.setResult("对账成功");
				} else if (((checkMainData.getProveFlag()==null || "0".equals(checkMainData.getProveFlag())) && !"3".equals(accNoMainData.getSendMode()))
							|| (accNoMainData.getFinalCheckFlag()==null || "1".equals(accNoMainData.getFinalCheckFlag()))) {
					accNoMainData.setResult("尚未对账");
				} else {
					accNoMainData.setResult("对账失败");
				}
				resultList.add(accNoMainData);
			}
			
			return resultList;
		} else {
			return null;
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public long getAccSendCount(Map<String, String> queryMap)
			throws XDocProcException {
		String sql = "select "
				+ "count(t1.accno) from autek.ebs_accnomaindata t1,autek.ebs_checkmaindata t2 "
				+ "where t1.voucherno=t2.voucherno and t2.docstate>1";
		// 循环Map取出查询条件
		if (queryMap != null) {
			for (Map.Entry<String, String> entry : queryMap.entrySet()) {
				if (entry.getValue() != null
						&& entry.getValue().trim().length() != 0) {
					sql += " and t2." + entry.getKey() + "='"
							+ entry.getValue().trim()  + "'";
				}
			}
		}
		List list = dao.findBySql(sql);
		String str = list.get(0).toString();
		long countNumber = Integer.parseInt(str);
		return countNumber;
	}
	
	//退信情况统计
	@Override
	public List<?> getUrgeStatisticsResult(Map<String, String> queryMap,
			PageParam pageParam,boolean isPaged) throws XDocProcException {
		String sql = "";
		String selectSql = "select idcenter,idbranch,idbank,bankname,docdate,"
				+ "count(case when docstate>1 and (sendmode in('1','2','4') or sendmode is null) then voucherno end) as totalvouamount,"// 发出账单数
				+ "count(case when docstate>1 and urgeState=1 and (sendmode in('1','2','4') or sendmode is null) then voucherno end) as totaluergeamount,"// 退信总数
				+ "count(case when docstate>1 and urgeState=1 and urgetype=7 and (sendmode in('1','2','4') or sendmode is null) then voucherno end) as rejectedamount,"// 单位拒收
				+ "count(case when docstate>1 and urgeState=1 and urgetype=4 and (sendmode in('1','2','4') or sendmode is null) then voucherno end) as addrchangedamount,"// 原址拆迁
				+ "count(case when docstate>1 and urgeState=1 and urgetype=1 and (sendmode in('1','2','4') or sendmode is null) then voucherno end) as addrunknownamount,"// 地址不详
				+ "count(case when docstate>1 and urgeState=1 and urgetype=5 and (sendmode in('1','2','4') or sendmode is null) then voucherno end) as norecieveramount,"// 投递无人
				+ "count(case when docstate>1 and urgeState=1 and urgetype=3 and (sendmode in('1','2','4') or sendmode is null) then voucherno end) as unitnotexistamount,"// 无此单位
				+ "count(case when docstate>1 and urgeState=1 and urgetype=2 and (sendmode in('1','2','4') or sendmode is null) then voucherno end) as addrnotexistamount,"// 无此地址
				+ "count(case when docstate>1 and urgeState=1 and urgetype=6 and (sendmode in('1','2','4') or sendmode is null) then voucherno end) as noconnectionamount,"// 无法联系
				+ "count(case when docstate>1 and urgeState=1 and urgetype=8 and (sendmode in('1','2','4') or sendmode is null) then voucherno end) as otheramount "// 其他
				+ "from ebs_checkmaindata  ";
		
		String whereSql = "where 1=1 ";
		
		// 循环Map取出查询条件
		if (queryMap != null) {
			for (Map.Entry<String, String> entry : queryMap.entrySet()) {
				if (entry.getValue() != null
						&& entry.getValue().trim().length() != 0) {
					whereSql += " and " + entry.getKey().trim() + "='"
							+ entry.getValue().trim() + "'";
				}
			}
		}
		String groupSql = "group by idcenter,idbranch,idbank,bankname,docdate order by idcenter,idbank";
		sql = selectSql + whereSql + groupSql;
		if(isPaged){
			Integer countNumber = dao.findBySql(sql).size();
			int pageSize = pageParam.getPageSize();// 每页显示结果条数
			int totalPage = (int) ((countNumber - 1) / pageSize + 1);// 总页数
			int curPage = pageParam.getCurPage();// 当前要显示的页
			if (curPage > totalPage) {
				curPage = totalPage;
			}
			int firstResult = (curPage - 1) * pageSize;// 分页时显示的第一条记录，默认从0开始
			List<?> list = dao.findBySql(sql, firstResult, pageSize);
			pageParam.setFirstResult(firstResult);
			pageParam.setCurPage(curPage);
			pageParam.setTotal((int) countNumber);
			pageParam.setTotalPage(totalPage);
			pageParam.setLastResult(firstResult + list.size());// 当前页显示的最后一条记录
			return list;
		}else{
			return dao.findBySql(sql);
		}
	}
	
	//退信情况统计按单位统计
	@Override
	public List<?> getUrgeStatisticsResultCount(Map<String, String> queryMap,
			PageParam pageParam, boolean isPaged, String selectCount)
			throws XDocProcException {
		// TODO Auto-generated method stub
		String sql = "";
		String selectSql = "select ";
		if(selectCount!=null && selectCount.equals("countIdCenter")){
			selectSql += "idCenter, ";
		}
		selectSql += "docdate,"
					+ "count(case when docstate>1 and (sendmode in('1','2','4') or sendmode is null) then voucherno end) as totalvouamount,"// 发出账单数
					+ "count(case when docstate>1 and urgeState=1 and (sendmode in('1','2','4') or sendmode is null) then voucherno end) as totaluergeamount,"// 退信总数
					+ "count(case when docstate>1 and urgeState=1 and urgetype=7 and (sendmode in('1','2','4') or sendmode is null) then voucherno end) as rejectedamount,"// 单位拒收
					+ "count(case when docstate>1 and urgeState=1 and urgetype=4 and (sendmode in('1','2','4') or sendmode is null) then voucherno end) as addrchangedamount,"// 原址拆迁
					+ "count(case when docstate>1 and urgeState=1 and urgetype=1 and (sendmode in('1','2','4') or sendmode is null) then voucherno end) as addrunknownamount,"// 地址不详
					+ "count(case when docstate>1 and urgeState=1 and urgetype=5 and (sendmode in('1','2','4') or sendmode is null) then voucherno end) as norecieveramount,"// 投递无人
					+ "count(case when docstate>1 and urgeState=1 and urgetype=3 and (sendmode in('1','2','4') or sendmode is null) then voucherno end) as unitnotexistamount,"// 无此单位
					+ "count(case when docstate>1 and urgeState=1 and urgetype=2 and (sendmode in('1','2','4') or sendmode is null) then voucherno end) as addrnotexistamount,"// 无此地址
					+ "count(case when docstate>1 and urgeState=1 and urgetype=6 and (sendmode in('1','2','4') or sendmode is null) then voucherno end) as noconnectionamount,"// 无法联系
					+ "count(case when docstate>1 and urgeState=1 and urgetype=8 and (sendmode in('1','2','4') or sendmode is null) then voucherno end) as otheramount "// 其他
					+ "from ebs_checkmaindata  ";
		
//		String whereSql = "where 1=1 and sendMode <> '"+FinalConstant.sendModelMap.get("网银")+"' ";
		String whereSql = "where 1=1 ";
		// 循环Map取出查询条件
		if (queryMap != null) {
			for (Map.Entry<String, String> entry : queryMap.entrySet()) {
				if (entry.getValue() != null
						&& entry.getValue().trim().length() != 0) {
					whereSql += " and " + entry.getKey().trim() + "='"
							+ entry.getValue().trim() + "'";
				}
			}
		}
		
		String groupSql = "group by docdate ";
		String orderSql = " ";
		if(selectCount!=null && selectCount.equals("countIdCenter")){
			groupSql += ",idCenter ";
			orderSql += "order by idCenter";
		}
		sql = selectSql + whereSql + groupSql + orderSql;
		if(isPaged){
			Integer countNumber = dao.findBySql(sql).size();
			int pageSize = pageParam.getPageSize();// 每页显示结果条数
			int totalPage = (int) ((countNumber - 1) / pageSize + 1);// 总页数
			int curPage = pageParam.getCurPage();// 当前要显示的页
			if (curPage > totalPage) {
				curPage = totalPage;
			}
			int firstResult = (curPage - 1) * pageSize;// 分页时显示的第一条记录，默认从0开始
			List<?> list = dao.findBySql(sql, firstResult, pageSize);
			pageParam.setFirstResult(firstResult);
			pageParam.setCurPage(curPage);
			pageParam.setTotal((int) countNumber);
			pageParam.setTotalPage(totalPage);
			pageParam.setLastResult(firstResult + list.size());// 当前页显示的最后一条记录
			return list;
		}else{
			return dao.findBySql(sql);
		}
	}


	@Override
	public List<?> getAllUrgeStatisticsResult(Map<String, String> queryMap)
			throws XDocProcException {
		String sql = "where 1=1 and sendMode <> '3'";
		// 循环Map取出查询条件
		if (queryMap != null) {
			for (Map.Entry<String, String> entry : queryMap.entrySet()) {
				if (entry.getValue() != null
						&& entry.getValue().trim().length() != 0) {
					sql += " and " + entry.getKey().trim() + "='"
							+ entry.getValue().trim() + "'";
				}
			}
		}
		sql = CreateSql.urgeStatisticsSql(sql);

		List<?> list = dao.findBySql(sql);
		return list;
	}

	//催收情况统计
	@Override
	public List<?> getRushStatisticsResult(Map<String, String> queryMap,
			PageParam pageParam,boolean isPaged) throws XDocProcException {
		String sql = "";
		
		String selectSql = "select idcenter,idbranch,idbank,bankname,docdate,"
						+ "count(case when docstate>1 and (sendmode in ('1','2','4') or sendmode is null) then voucherno end) as totalvouamount,"// 发出账单数
						+ "count(case when docstate>1 and rushstate=1 and (sendmode in ('1','2','4') or sendmode is null) then voucherno end) as totalrushamount,"// 催收总数
						+ "count(case when docstate>1 and rushstate=1 and RUSHMETHOD=0 and rushflag=1 and (sendmode in ('1','2','4') or sendmode is null) then voucherno end) as rushedbytelamount,"// 电话催收
						+ "count(case when docstate>1 and rushstate=1 and RUSHMETHOD=1 and rushflag=1 and (sendmode in ('1','2','4') or sendmode is null) then voucherno end) as rushedbyemailamount,"// 邮件催收
						+ "count(case when docstate>1 and rushstate=1 and RUSHMETHOD=2 and rushflag=1 and (sendmode in ('1','2','4') or sendmode is null) then voucherno end) as rushedfacetofaceamount,"// 面对面催收
						+ "count(case when docstate=3 and rushstate=1 and (sendmode in ('1','2','4') or sendmode is null) then voucherno end) as rushsuccessamount "// 催收成功数
						+ "from ebs_checkmaindata ";
		
		String whereSql = "where 1=1 ";
		// 循环Map取出查询条件
		if (queryMap != null) {
			for (Map.Entry<String, String> entry : queryMap.entrySet()) {
				if (entry.getValue() != null
						&& entry.getValue().trim().length() != 0) {
					whereSql += " and " + entry.getKey().trim() + "='"
							+ entry.getValue().trim() + "'";
				}
			}
		}
		
		String groupSql = "group by idcenter,idbranch,idbank,bankname,docdate order by idcenter,idbank ";
		
		sql = selectSql + whereSql + groupSql;
		if(isPaged){
			Integer countNumber = dao.findBySql(sql).size();
			int pageSize = pageParam.getPageSize();
			int totalPage = (int) ((countNumber - 1) / pageSize + 1);
			int curPage = pageParam.getCurPage();
			if (curPage > totalPage) {
				curPage = totalPage;
			}
			int firstResult = (curPage - 1) * pageSize;
			List<?> list = dao.findBySql(sql, firstResult, pageSize);
			pageParam.setFirstResult(firstResult);
			pageParam.setCurPage(curPage);
			pageParam.setTotal((int) countNumber);
			pageParam.setTotalPage(totalPage);
			pageParam.setLastResult(firstResult + list.size());
			return list;
		}else{
			return dao.findBySql(sql);
		}
	}
	
	//催收情况统计(按单位统计)
	@Override
	public List<?> getRushStatisticsResultCount(Map<String, String> queryMap,
			PageParam pageParam, boolean isPaged, String selectCount)
			throws XDocProcException {
		// TODO Auto-generated method stub
		String sql = "";
		
		String selectSql = "select ";
		if(selectCount!=null && selectCount.equals("countIdCenter")){
			selectSql += "idCenter,";
		}
		selectSql += "docdate,"
					+ "count(case when docstate>1 and (sendmode in ('1','2','4') or sendmode is null) then voucherno end) as totalvouamount,"// 发出账单数
					+ "count(case when docstate>1 and rushstate=1 and (sendmode in ('1','2','4') or sendmode is null) then voucherno end) as totalrushamount,"// 催收总数
					+ "count(case when docstate>1 and rushstate=1 and RUSHMETHOD=0 and rushflag=1 and (sendmode in ('1','2','4') or sendmode is null) then voucherno end) as rushedbytelamount,"// 电话催收
					+ "count(case when docstate>1 and rushstate=1 and RUSHMETHOD=1 and rushflag=1 and (sendmode in ('1','2','4') or sendmode is null) then voucherno end) as rushedbyemailamount,"// 邮件催收
					+ "count(case when docstate>1 and rushstate=1 and RUSHMETHOD=2 and rushflag=1 and (sendmode in ('1','2','4') or sendmode is null) then voucherno end) as rushedfacetofaceamount,"// 面对面催收
					+ "count(case when docstate=3 and rushstate=1 and (sendmode in ('1','2','4') or sendmode is null) then voucherno end) as rushsuccessamount "// 催收成功数
					+ "from ebs_checkmaindata ";
		
		String whereSql = "where 1=1 ";
		// 循环Map取出查询条件
		if (queryMap != null) {
			for (Map.Entry<String, String> entry : queryMap.entrySet()) {
				if (entry.getValue() != null
						&& entry.getValue().trim().length() != 0) {
					whereSql += " and " + entry.getKey().trim() + "='"
							+ entry.getValue().trim() + "' ";
				}
			}
		}
		
		String groupSql = "group by docdate ";
		String orderSql = "";
		if(selectCount!=null && selectCount.equals("countIdCenter")){
			groupSql += ",idCenter ";
			orderSql+=" order by idcenter";
		}
		sql = selectSql + whereSql + groupSql + orderSql;
		if(isPaged){
			Integer countNumber = dao.findBySql(sql).size();
			int pageSize = pageParam.getPageSize();
			int totalPage = (int) ((countNumber - 1) / pageSize + 1);
			int curPage = pageParam.getCurPage();
			if (curPage > totalPage) {
				curPage = totalPage;
			}
			int firstResult = (curPage - 1) * pageSize;
			List<?> list = dao.findBySql(sql, firstResult, pageSize);
			pageParam.setFirstResult(firstResult);
			pageParam.setCurPage(curPage);
			pageParam.setTotal((int) countNumber);
			pageParam.setTotalPage(totalPage);
			pageParam.setLastResult(firstResult + list.size());
			return list;
		}else{
			return dao.findBySql(sql);
		}
	}


	@Override
	public List<?> getAllRushStatisticsResult(Map<String, String> queryMap)
			throws XDocProcException {

		String sql = "where 1=1 ";
		// 循环Map取出查询条件
		if (queryMap != null) {
			for (Map.Entry<String, String> entry : queryMap.entrySet()) {
				if (entry.getValue() != null
						&& entry.getValue().trim().length() != 0) {
					sql += " and " + entry.getKey().trim() + "='"
							+ entry.getValue().trim() + "'";
				}
			}
		}
		sql = CreateSql.rushStatisticsSql(sql);
		List<?> list = dao.findBySql(sql);
		return list;

	}

	@SuppressWarnings("rawtypes")
	@Override
	public long getBillSendCount(Map<String, String> queryMap)
			throws XDocProcException {
		String sql = "select count(t.voucherno) from autek.ebs_checkmaindata t where t.docstate>1";
		// 循环Map取出查询条件
		if (queryMap != null) {
			for (Map.Entry<String, String> entry : queryMap.entrySet()) {
				if (entry.getValue() != null
						&& entry.getValue().trim().length() != 0) {
					sql += " and t." + entry.getKey() + "='" + entry.getValue().trim() 
							+ "'";
				}
			}
		}
		List list = dao.findBySql(sql);
		String str = list.get(0).toString();
		long countNumber = Integer.parseInt(str);
		return countNumber;
	}

	//机构对账率统计
	@SuppressWarnings("rawtypes")
	@Override
	public List getEbillAnalyseList(Map<String, String> queryMap,
			PageParam pageParam, boolean isPaged,String selectCount) throws XDocProcException {
		String sql = "select t1.idCenter ,t1.idBank,t1.bankName,t1.docDate,"
				+ "count(case when t1.docstate > 1 then t2.accno end) as sendCount," // 发出的账户数
				+ "count(case when t1.docstate='3' then t2.accno end) as backCount," // 回收数
				+ "count(case when t1.docstate>1 and t1.urgeState=1 then t2.accno end) as retreatCount," // 退信数
				+ "count(case when t1.docstate>1 and (t1.proveflag='20'  or t1.proveflag='22' or t1.proveflag='31') "
				+ "then t2.accno end) as proveMatchCount," // 验印成功数
				+ "count(case when t1.docstate>1 and (t1.proveflag='40' or t1.proveflag='41' or t1.proveflag='42' or t1.proveflag='43' "
				+ "or t1.proveflag='44' or t1.proveflag='51') then t2.accno end) as proveNotMatchCount," // 验印不符数
				+ "count(case when t1.docstate>1 and t1.proveflag='44' then t2.accno end) as wjkCount," // 未建验印库数
				+ "count(case when t2.finalCheckflag='3' or t2.finalCheckflag='4' then t2.accno end) as checkSuccessCount," // 余额相符数
				+ "count(case when t2.finalCheckflag='2' or t2.finalCheckflag='5' then t2.accno end) as checkFailCount, " // 余额不符数
				+ "count(case when t1.docstate>1 and (((t1.proveflag='20' or t1.proveflag='22' or t1.proveflag='31') "
				+ "and (t2.finalCheckflag='3' or t2.finalCheckflag='4')) or  (t2.sendmode ='3' and t2.finalcheckflag in ('3','4'))) then t2.accno end) as ebillSuccessCount " // 对账成功数
				+ "from autek.ebs_checkmaindata t1 left join autek.ebs_accnomaindata t2 on (t1.voucherno=t2.voucherno) " +
				" where 1=1 ";
		
		//遍历查询条件
		if (queryMap != null) {
			for (Map.Entry<String, String> entry : queryMap.entrySet()) {
				if (entry.getValue() != null
						&& entry.getValue().trim().length() != 0) {
					sql += "and t2." + entry.getKey() + "='" + entry.getValue().trim() 
							+ "' ";
				}
			}
		}
		
		sql += "group by t1.idCenter,t1.idBranch,t1.idbank,t1.bankName,t1.docDate order by t1.idCenter";
		
		List<?> list = null;
		if(isPaged){
			Integer countNumber = dao.findBySql(sql).size();
			int pageSize = pageParam.getPageSize();// 每页显示结果条数
			int totalPage = (int) ((countNumber - 1) / pageSize + 1);// 总页数
			int curPage = pageParam.getCurPage();// 当前要显示的页
			if (curPage > totalPage) {
				curPage = totalPage;
			}
			int firstResult = (curPage - 1) * pageSize;// 分页时显示的第一条记录，默认从0开始
			list = dao.findBySql(sql, firstResult, pageSize);
			pageParam.setFirstResult(firstResult);
			pageParam.setCurPage(curPage);
			pageParam.setTotal((int) countNumber);
			pageParam.setTotalPage(totalPage);
			pageParam.setLastResult(firstResult + list.size());// 当前页显示的最后一条记录
			return list;	
		}else{
			list = dao.findBySql(sql);
			return list;
		}
	}
	
	//按单位进行机构率统计
	@Override
	public List<?> getEbillAnalyseListCount(Map<String, String> queryMap,
			PageParam pageParam, boolean isPaged, String selectCount)
			throws XDocProcException {
		// TODO Auto-generated method stub
		String sql = "";
		String selectSql = "select ";
		if(selectCount!=null && selectCount.equals("countIdCenter")){
			selectSql += "t1.idCenter, ";
		}
		selectSql += "t1.docDate,"
					+ "count(case when t1.docstate > 1 then t2.accno end) as sendCount," // 发出的账户数
					+ "count(case when t1.docstate='3' then t2.accno end) as backCount," // 回收数
					+ "count(case when t1.docstate>1 and t1.urgeState=1 then t2.accno end) as retreatCount," // 退信数
					+ "count(case when t1.docstate>1 and (t1.proveflag='20'  or t1.proveflag='22' or t1.proveflag='31') "
					+ "then t2.accno end) as proveMatchCount," // 验印成功数
					+ "count(case when t1.docstate>1 and (t1.proveflag='40' or t1.proveflag='41' or t1.proveflag='42' or t1.proveflag='43' "
					+ "or t1.proveflag='44' or t1.proveflag='51') then t2.accno end) as proveNotMatchCount," // 验印不符数
					+ "count(case when t1.docstate>1 and t1.proveflag='44' then t2.accno end) as wjkCount," // 未建验印库数
					+ "count(case when t2.finalCheckflag='3' or t2.finalCheckflag='4' then t2.accno end) as checkSuccessCount," // 余额相符数
					+ "count(case when t2.finalCheckflag='2' or t2.finalCheckflag='5' then t2.accno end) as checkFailCount, " // 余额不符数
					+ "count(case when t1.docstate>1 and (((t1.proveflag='20' or t1.proveflag='22' or t1.proveflag='31') "
					+ "and (t2.finalCheckflag='3' or t2.finalCheckflag='4')) or  (t2.sendmode ='3' and t2.finalcheckflag in ('3','4'))) then t2.accno end) as ebillSuccessCount " // 对账成功数
					+ "from autek.ebs_checkmaindata t1 left join autek.ebs_accnomaindata t2 on (t1.voucherno=t2.voucherno) " +
					" where 1=1 ";
		
		//遍历查询条件
		if (queryMap != null) {
			for (Map.Entry<String, String> entry : queryMap.entrySet()) {
				if (entry.getValue() != null
						&& entry.getValue().trim().length() != 0) {
					selectSql += "and t2." + entry.getKey() + "='" + entry.getValue().trim() 
								+ "' ";
				}
			}
		}
		
		String groupSql = "group by t1.docDate ";
		String orderSql = "";
		if(selectCount!=null && selectCount.equals("countIdCenter")){
			groupSql += ",t1.idCenter ";
			orderSql += "order by t1.idCenter ";
		}
		
		sql = selectSql + groupSql +orderSql;
		List<?> list = null;
		if(isPaged){
			Integer countNumber = dao.findBySql(sql).size();
			int pageSize = pageParam.getPageSize();// 每页显示结果条数
			int totalPage = (int) ((countNumber - 1) / pageSize + 1);// 总页数
			int curPage = pageParam.getCurPage();// 当前要显示的页
			if (curPage > totalPage) {
				curPage = totalPage;
			}
			int firstResult = (curPage - 1) * pageSize;// 分页时显示的第一条记录，默认从0开始
			list = dao.findBySql(sql, firstResult, pageSize);
			pageParam.setFirstResult(firstResult);
			pageParam.setCurPage(curPage);
			pageParam.setTotal((int) countNumber);
			pageParam.setTotalPage(totalPage);
			pageParam.setLastResult(firstResult + list.size());// 当前页显示的最后一条记录
			return list;	
		}else{
			list = dao.findBySql(sql);
			return list;
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List getAllEbillAnalyseList(Map<String, String> queryMap,
			String orgType, String accNo, String custManager)
			throws XDocProcException {
		String sql = "select t1.idBank,t1.bankName,t1.docDate,"
				+ "count(case when t1.docstate>1 then t1.voucherno end) as sendCount," // 对账单发出数
				+ "count(case when t1.docstate='3' then t1.voucherno end) as backCount," // 回收数
				+ "count(case when t1.docstate>1 and t1.urgeState=1 then t1.voucherno end) as retreatCount," // 退信数
				+ "count(case when t1.docstate>1 and (t1.proveflag='20' or t1.proveflag='21' or t1.proveflag='22' or t1.proveflag='31') "
				+ "then t1.voucherno end) as proveMatchCount," // 验印成功数
				+ "count(case when t1.docstate>1 and (t1.proveflag='40' or t1.proveflag='41' or t1.proveflag='42' or t1.proveflag='43' "
				+ "or t1.proveflag='44' or t1.proveflag='51') then t1.voucherno end) as proveNotMatchCount," // 验印不符数
				+ "count(case when t1.docstate>1 and t1.proveflag='44' then t1.voucherno end) as wjkCount," // 未建库数
				+ "count(case when t2.finalCheckflag='3' or t2.finalCheckflag='4' then t2.accno end) as checkSuccessCount," // 余额不相符数
				+ "count(case when t2.finalCheckflag='2' or t2.finalCheckflag='5' then t2.accno end) as checkFailCount, " // 余额不符数
				+ "count(case when t1.docstate>1 and (((t1.proveflag='20' or t1.proveflag='21' or t1.proveflag='22' or t1.proveflag='31') "
				+ "and (t2.finalCheckflag='3' or t2.finalCheckflag='4')) or  (t2.sendmode ='3' and t2.finalcheckflag in ('3','4'))) then t2.accno end) as ebillSuccessCount " // 对账成功数
				+ " ,t1.idbranch ,t1.idcenter "
				+ "from autek.ebs_checkmaindata t1,autek.ebs_accnomaindata t2";

		if (custManager != null && custManager.trim().length() != 0) {
			sql += ",autek.ebs_custmanager t3 where t3.accno=t2.accno and t3.custmanager='"
					+ custManager + "' ";
		} else {
			sql += " where 1=1";
		}

		sql += " and t1.voucherno=t2.voucherno ";

		if (queryMap != null) {
			for (Map.Entry<String, String> entry : queryMap.entrySet()) {
				if (entry.getValue() != null
						&& entry.getValue().trim().length() != 0) {

					sql += "and t1." + entry.getKey() + "='" + entry.getValue().trim() 
							+ "' ";
				}
			}
		}
		if (accNo != null && accNo.trim().length() != 0) {
			sql += "and t2.accno = '" + accNo + "' ";
		}

		if (orgType.equals("0")) {
			sql += "group by t1.idCenter,t1.idBranch,t1.idbank,t1.bankName,t1.docDate  order by t1.idbranch";
		} else if (orgType.equals("1")) {
			sql += "group by t1.idCenter,t1.idBranch,t1.idbank,t1.bankName,t1.docDate  order by t1.idbank";
		}
		List<?> list = dao.findBySql(sql);
		return list;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List getPartEbillAnalyseList(Map<String, String> queryMap,String docDateStart,String docDateEnd, boolean isPaged) throws XDocProcException {
		String sql = "select t1.idBank,t1.bankName,t1.docDate,"
				+ "count(case when t1.docstate>1 then t1.voucherno end) as sendCount," // 对账单发出数
				+ "count(case when t1.docstate='3' then t1.voucherno end) as backCount," // 回收数
				+ "count(case when t1.docstate>1 and t1.urgeState=1 then t1.voucherno end) as retreatCount," // 退信数
				+ "count(case when t1.docstate>1 and (t1.proveflag='20' or t1.proveflag='21' or t1.proveflag='22' or t1.proveflag='31') "
				+ "then t1.voucherno end) as proveMatchCount," // 验印成功数
				+ "count(case when t1.docstate>1 and (t1.proveflag='40' or t1.proveflag='41' or t1.proveflag='42' or t1.proveflag='43' "
				+ "or t1.proveflag='44' or t1.proveflag='51') then t1.voucherno end) as proveNotMatchCount," // 验印不符数
				+ "count(case when t1.docstate>1 and t1.proveflag='44' then t1.voucherno end) as wjkCount," // 未建库数
				+ "count(case when t2.finalCheckflag='3' or t2.finalCheckflag='4' then t2.accno end) as checkSuccessCount," // 余额不相符数
				+ "count(case when t2.finalCheckflag='2' or t2.finalCheckflag='5' then t2.accno end) as checkFailCount, " // 余额不符数
				+ "count(case when t1.docstate>1 and (((t1.proveflag='20' or t1.proveflag='21' or t1.proveflag='22' or t1.proveflag='31') "
				+ "and (t2.finalCheckflag='3' or t2.finalCheckflag='4')) or  (t2.sendmode ='3' and t2.finalcheckflag in ('3','4'))) then t2.accno end) as ebillSuccessCount " // 对账成功数
				+ " ,t1.idbranch ,t1.idcenter "
				+ "from autek.ebs_checkmaindata t1,autek.ebs_accnomaindata t2 where 1=1 ";

		sql += " and t1.voucherno=t2.voucherno ";
		sql += " and t1.dealDate between '"+ docDateStart +"' and '"+docDateEnd +"'";
		if (queryMap != null) {
			for (Map.Entry<String, String> entry : queryMap.entrySet()) {
				if (entry.getValue() != null
						&& entry.getValue().trim().length() != 0) {

					sql += "and t1." + entry.getKey() + "='" + entry.getValue().trim() 
							+ "' ";
				}
			}
		}
		sql += "group by t1.idCenter,t1.idBranch,t1.idbank,t1.bankName,t1.docDate  order by t1.idbranch";
		List<?> list = dao.findBySql(sql);
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getSealAccno(String voucherno) throws XDocProcException {
		String sql = "select sealaccno,sealMode from ebs_BasicInfo where accNo in (select accNo from ebs_Accnomaindata where voucherNo ='"
				+ voucherno + "')  group by  sealaccno,sealMode ";
		List<Object[]> list = (List<Object[]>) findBySql(sql);
		if(list!=null && list.size()>0){
			return list;
		}else{
			sql = "select sealaccno,sealMode from ebs_BasicInfo where custId in (select custId from ebs_Accnomaindata where voucherNo ='"
					+ voucherno + "')  group by  sealaccno,sealMode ";
			return (List<Object[]>) findBySql(sql);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getCheckMaindata(Map<String, String> queryMap,
			PageParam pageParam, Map<String, String> accQueryMap)
			throws XDocProcException {
		String sql = "select c.voucherNo,c.idCenter,c.bankName,c.accName,c.custId,linkman,c.address,c.phone,c.docState,c.printTimes, "
				+ "c.docDate,c.zip,c.sendmode, case when c.sendaddress is null then c.sendaddress else c.sendaddress || '(' || c.idbank || ')' end as sendaddress  from ebs_checkmaindata c where 1=1 ";
		if (queryMap != null) {
			for (Map.Entry<String, String> entry : queryMap.entrySet()) {
				if (entry.getValue() != null
						&& entry.getValue().trim().length() != 0) {
					// 20130107 账单以分行为主 这里页面查询 支行和网点的条件不应该放到 checkmaindata 中找数据
					if ("idBranch".equals(entry.getKey())||"idBank".equals(entry.getKey())) {
						sql += "and c." + entry.getKey() + "='"+ entry.getValue().trim() + "' ";
					} if("accName".equals(entry.getKey())){
						//2013 10 15添加账户名称 模糊查询
						sql += "and c." + entry.getKey() + " like '%"
								+ entry.getValue().trim() + "%' ";
					}else {
						// 导出模块复选sendmode的四种方式  sunjian
						if ("sendModeType".equals(entry.getKey())) {
							sql += "and c.sendmode in " + "("
									+ entry.getValue().trim() + ") ";
						} else {
							// 其他模块的选择方式
							sql += "and c." + entry.getKey() + "='"
									+ entry.getValue().trim() + "' ";
						}
					}
				}
			}
		}
		if (accQueryMap != null) {
			for (Map.Entry<String, String> entry : accQueryMap.entrySet()) {
				if (entry.getValue() != null
						&& entry.getValue().trim().length() != 0) {
					if("5".equals(entry.getValue().trim()) 
							&& "sendMode".equals(entry.getKey())){
						sql += "and c.sendMode is null ";
					}else{
						sql += "and c." + entry.getKey() + " = '"
								+ entry.getValue().trim() + "' ";
					}
				}
			}
		}
		sql += "  order by c.idCenter,c.idbank,c.custId ";
		Integer countNumber = dao.findBySql(sql).size();
		int pageSize = pageParam.getPageSize();// 每页显示结果条数
		int totalPage = (int) ((countNumber - 1) / pageSize + 1);// 总页数
		int curPage = pageParam.getCurPage();// 当前要显示的页
		if (curPage > totalPage) {
			curPage = totalPage;
		}
		int firstResult = (curPage - 1) * pageSize;// 分页时显示的第一条记录，默认从0开始
		List<Object[]> list = (List<Object[]>) dao.findBySql(sql, firstResult,
				pageSize);
		pageParam.setFirstResult(firstResult);
		pageParam.setCurPage(curPage);
		pageParam.setTotal((int) countNumber);
		pageParam.setTotalPage(totalPage);
		pageParam.setLastResult(firstResult + list.size());// 当前页显示的最后一条记录
		return list;
	}

	@Override
	public void updateCheckmaindataByVoucherno(String voucherno, int printTimes)
			throws XDocProcException {
		super.ExecQuery("update CheckMainData set docState = '2', printTimes = "
				+ printTimes + " where voucherNo ='" + voucherno + "'");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * 根据对账单编号来查询docset表获取图像信息
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public CheckMainData getOneByVoucherNo(String voucherNo)
			throws XDocProcException {
		List<?> list = dao.findByHql("from CheckMainData where voucherNo='"
				+ voucherNo + "'");
		if (list != null && list.size() != 0) {
			return (CheckMainData) list.get(0);
		} else {
			return null;
		}
	}

	@Override
	public List<CheckMainData> getBillinfoQueryData(
			Map<String, String> queryMap, final String queryType,
			PageParam pageParam) throws XDocProcException {
		String sql = "";
		String listsql1 = "select c.voucherno,c.idbank,c.idbranch,c.idcenter,a.accno,c.accname,a.credit,a.docdate,a.currency,c.proveflag,a.checkflag,c.docstate,a.acctype "
				+ "from ebs_checkmaindata c ,ebs_accnomaindata a  where  c.voucherno=a.voucherno ";
		String billinfosql = "select ac.voucherno,ac.idbank,ac.accno,ac.accname,ac.credit,ac.docdate,ac.currency,ac.proveflag,ac.checkflag,ac.docstate"
				+ " from (" + listsql1.trim() + ")  ac ";
		// 将按账单编号查询到的表，作为一个ac表
		String listsql2 = "select c.voucherno,c.idbank,c.idbranch,c.idcenter,a.accno,c.accname,a.credit,a.docdate,a.currency,c.proveflag,a.checkflag,c.docstate ,a.acctype "
				+ " from ebs_checkmaindata c ,(select  * from ebs_accnomaindata a where a.autoid=(select min(autoid) from  ebs_accnomaindata aa where aa.voucherno=a.voucherno)) a  where  c.voucherno=a.voucherno ";
		String basicinfosql = "select ac.voucherno,ac.idbank,ac.accno,ac.accname,ac.credit,ac.docdate,ac.currency,ac.proveflag,ac.checkflag,ac.docstate"
				+ " from (" + listsql2.trim() + ")  ac ";
		// 将按账号信息查询的表，作为一个ac表

		String conditionsql = "where 1=1 ";

		Set<Map.Entry<String, String>> key = queryMap.entrySet();
		Iterator<Map.Entry<String, String>> its = key.iterator();
		while (its.hasNext()) {
			Map.Entry<String, String> entry = (Map.Entry<String, String>) its
					.next();
			if (StringUtils.isNotEmpty(entry.getValue())) {
				conditionsql += "  and ac." + entry.getKey().trim() + "='"
						+ entry.getValue() + "' ";
			}
		}
		if (queryType.equals("2")) {
			sql = billinfosql + conditionsql;
		} else if (queryType.equals("1")) {
			sql = basicinfosql + conditionsql;
		}

		sql += " group by ac.voucherno,ac.idbank,ac.accno,ac.accname,ac.credit,ac.docdate,ac.currency,ac.proveflag,ac.checkflag,ac.docstate " +
				"order by ac.voucherno ";
		Integer countNumber = dao.findBySql(sql).size();
		int pageSize = pageParam.getPageSize();// 每页显示结果条数
		int totalPage = (int) ((countNumber - 1) / pageSize + 1);// 总页数
		int curPage = pageParam.getCurPage();// 当前要显示的页
		if (curPage > totalPage) {
			curPage = totalPage;
		}
		int firstResult = (curPage - 1) * pageSize;// 分页时显示的第一条记录，默认从0开始
		List<?> list = dao.findBySql(sql, firstResult, pageSize);
		pageParam.setFirstResult(firstResult);
		pageParam.setCurPage(curPage);
		pageParam.setTotal((int) countNumber);
		pageParam.setTotalPage(totalPage);
		pageParam.setLastResult(firstResult + list.size());// 当前页显示的最后一条记录
		List<CheckMainData> billList = new ArrayList<CheckMainData>();
		for (int i = 0; i < list.size(); i++) {
			Object[] obj = (Object[]) list.get(i);
			CheckMainData checkMainData = (CheckMainData) obj[0];
			AccNoMainData accNoMainData = (AccNoMainData) obj[1];
			checkMainData.setAccNoMainData(accNoMainData);
			billList.add(checkMainData);
		}
		return billList;
	}
	
	//验印情况统计
	@Override
	public List<?> getProveAnalyseList(Map<String, String> queryMap,
			PageParam pageParam, boolean isPaged) throws XDocProcException {
		// TODO Auto-generated method stub
		String sql = "select t.idCenter,t.idBranch,t.idBank,t.bankName,t.docDate, "
				+ "count(case when (t.sendmode in ('1','2','4') or t.sendmode is null) and t.docstate > 1 then t.voucherno end) as sendCount,"  //对账单发出数
				+ "count(case when (t.sendmode in ('1','2','4') or t.sendmode is null) and t.docstate > 1 and t.proveflag='20'  or t.proveflag='22' or t.proveflag='31' then t.voucherno end) as proveMatchCount,"  //验印成功数
				+ "count(case when (t.sendmode in ('1','2','4') or t.sendmode is null) and t.docstate > 1 and t.proveflag='40' or t.proveflag='41' or t.proveflag='42' or t.proveflag='43' or t.proveflag='44' or t.proveflag='51' then t.voucherno end) as proveNotMatchCount, "//验印不成功数
				+ "count(case when (t.sendmode in ('1','2','4') or t.sendmode is null) and t.docstate > 1 and t.proveflag='0' then t.voucherno end) as notProve "//尚未验印数
				+ "from autek.ebs_checkmaindata t where 1=1  ";
		if (queryMap != null) {
			for (Map.Entry<String, String> entry : queryMap.entrySet()) {
				if (entry.getValue() != null
						&& entry.getValue().trim().length() != 0) {
					sql += " and t." + entry.getKey() + "='" + entry.getValue().trim() 
							+ "' ";
				}
			}
		}
		sql += "group by t.idCenter,t.idBranch,t.idBank,t.bankName,t.docDate order by t.idcenter,t.idbank";
		if(isPaged){
			Integer countNumber = dao.findBySql(sql).size();
			int pageSize = pageParam.getPageSize();// 每页显示结果条数
			int totalPage = (int) ((countNumber - 1) / pageSize + 1);// 总页数
			int curPage = pageParam.getCurPage();// 当前要显示的页
			if (curPage > totalPage) {
				curPage = totalPage;
			}
			int firstResult = (curPage - 1) * pageSize;// 分页时显示的第一条记录，默认从0开始
			List<?> list = dao.findBySql(sql, firstResult, pageSize);
			pageParam.setFirstResult(firstResult);
			pageParam.setCurPage(curPage);
			pageParam.setTotal((int) countNumber);
			pageParam.setTotalPage(totalPage);
			pageParam.setLastResult(firstResult + list.size());// 当前页显示的最后一条记录
			return list;
		}else{
			return dao.findBySql(sql);
		}
	}
	
	//验印情况统计(按单位统计)
	@Override
	public List<?> getProveAnalyseListCount(Map<String, String> queryMap,
			PageParam pageParam, boolean isPaged, String selectCount)
			throws XDocProcException {
		// TODO Auto-generated method stub
		String sql = "";
		String selectSql = "select ";
		if(selectCount!=null && selectCount.equals("countIdCenter")){
			selectSql += "t.idCenter,";
		}
		selectSql += "t.docDate, "
				+ "count(case when (t.sendmode in ('1','2','4') or t.sendmode is null) and t.docstate > 1 then t.voucherno end) as sendCount,"  //对账单发出数
				+ "count(case when (t.sendmode in ('1','2','4') or t.sendmode is null) and t.docstate > 1 and t.proveflag='20'  or t.proveflag='22' or t.proveflag='31' then t.voucherno end) as proveMatchCount,"  //验印成功数
				+ "count(case when (t.sendmode in ('1','2','4') or t.sendmode is null) and t.docstate > 1 and t.proveflag='40' or t.proveflag='41' or t.proveflag='42' or t.proveflag='43' or t.proveflag='44' or t.proveflag='51' then t.voucherno end) as proveNotMatchCount, "//验印不成功数
				+ "count(case when (t.sendmode in ('1','2','4') or t.sendmode is null) and t.docstate > 1 and t.proveflag='0' then t.voucherno end) as notProve "//尚未验印数
				+ "from autek.ebs_checkmaindata t where 1=1  ";
		if (queryMap != null) {
			for (Map.Entry<String, String> entry : queryMap.entrySet()) {
				if (entry.getValue() != null
						&& entry.getValue().trim().length() != 0) {
					selectSql += " and t." + entry.getKey() + "='" + entry.getValue().trim() 
							+ "' ";
				}
			}
		}
		String groupSql = " group by t.docDate ";
		String orderSql = "";
		if(selectCount!=null && selectCount.equals("countIdCenter")){
			groupSql += ",t.idcenter" ;
			orderSql = " order by t.idcenter";
		}
		sql = selectSql + groupSql + orderSql;
		if(isPaged){
			Integer countNumber = dao.findBySql(sql).size();
			int pageSize = pageParam.getPageSize();// 每页显示结果条数
			int totalPage = (int) ((countNumber - 1) / pageSize + 1);// 总页数
			int curPage = pageParam.getCurPage();// 当前要显示的页
			if (curPage > totalPage) {
				curPage = totalPage;
			}
			int firstResult = (curPage - 1) * pageSize;// 分页时显示的第一条记录，默认从0开始
			List<?> list = dao.findBySql(sql, firstResult, pageSize);
			pageParam.setFirstResult(firstResult);
			pageParam.setCurPage(curPage);
			pageParam.setTotal((int) countNumber);
			pageParam.setTotalPage(totalPage);
			pageParam.setLastResult(firstResult + list.size());// 当前页显示的最后一条记录
			return list;
		}else{
			return dao.findBySql(sql);
		}
	}
	
	//获得所有上级机构的名字
	@Override
	public List<?> getAllIdBranchName() throws XDocProcException {
		// TODO Auto-generated method stub
		//String sql = "select b.idbank, b.cname from param_bank b join (select t.idbranch from PARAM_BANK t group by t.idbranch) a on (a.idbranch = b.idbank)";
		String sql = "select b.idbank, b.cname from param_bank b where b.nlevel<3";
		List<?> list = null;
		list = dao.findBySql(sql);
		return list;
	}
	
	/*
	 * 对账单在查询完之后，以查询时选择的发送方式为准，导出账单 sunjian
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<CheckMainData> getExportCheckMainData(Map<String, String> queryMap)throws XDocProcException{
		
		List<CheckMainData> list = new ArrayList<CheckMainData>();;
		StringBuffer hql = new StringBuffer();
		hql.append("from CheckMainData where 1=1 ");
		if (queryMap != null) {
			for (Map.Entry<String, String> entry : queryMap.entrySet()) {
				if (entry.getValue() != null
						&& entry.getValue().trim().length() != 0) {
					if ("idBranch".equals(entry.getKey())||"idBank".equals(entry.getKey())) {
						hql.append("and ").append(entry.getKey())
								.append("=").append(entry.getValue().trim()).append(" ");
					} else {
						if ("sendModeType".equals(entry.getKey())) {
							hql.append("and sendmode in ("+entry.getValue().trim()+")");
							if((entry.getValue().trim()).contains(FinalConstant.sendModelMap.get("邮寄"))){
								hql.append(" and sendAddress is not null and linkMan is not null ");
							}
						} else {
							// 其他模块的选择方式
							hql.append(" and ").
							append(entry.getKey()).append( "=").append(
									entry.getValue().trim()).append(" ");
						}
					}
				}
			}
		}
		hql.append(" order by idcenter");
		list = ((List<CheckMainData>)dao.find(hql.toString()));		
		return list;
	}
	
	/**
	 * 得到对账单中科目号位 活期的明细
	 */	
	@SuppressWarnings("unchecked")
	public List<Object[]> getDeatil(Map<String,String> mapData,String month,boolean isPaged,int firstNum,int pageNum)throws XDocProcException{
	
		String sql= "select t1.voucherno,t2.accno ,t2.workdate,t2.vouno ,t2.abs,t2.credit," +
					" case when t2.dcflag =1 then t2.tracebal  else null end as jf,"+
       				" case when t2.dcflag =2 then t2.tracebal  else null end as df "+
					" from  ebs_checkmaindata t1 "+
  					" left join ebs_accnomaindata t3 on t3.voucherno = t1.voucherno"+
  					"  join ebs_accnodetaildata_"+month +" t2 on t2.accno = t3.accno"+
			        " where t3.subjectno in (select sub.subnoc  from param_subnoc sub where sub.memo like '%活期%') " ;
			        
		String query="";
		if (mapData != null) {
			for (Map.Entry<String, String> entry : mapData.entrySet()) {
				if (entry.getValue() != null && entry.getValue().trim().length() != 0)
					
					if ("sendModeType".equals(entry.getKey())) {
						query +=" and t1.sendmode in ("+entry.getValue()+") ";
						if((entry.getValue().trim()).contains(FinalConstant.sendModelMap.get("邮寄"))){
							query+=" and t1.sendAddress is not null and t1.linkMan is not null";
						}
					}else{
						query+=" and t1."+entry.getKey()+"="+entry.getValue()+"";
					}
			}
		}
		sql +=query+" order by t1.voucherno ,t2.accno,t2.workdate,t2.tracetime,t2.traceno ";
		if(isPaged){
			return (List<Object[]>) dao.findBySql(sql,firstNum,pageNum);
		}else{
			return (List<Object[]>) dao.findBySql(sql);
		}
	}
	
	/**
	 * 得到 所有需要打印对账单的 对账单编号
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> getAllVoucherNo(Map<String,String> queryMap)throws XDocProcException{
		String sql = "select t.voucherno ,t.accname from ebs_checkmaindata t where 1=1 ";
		
		String query="";
		if (queryMap != null) {
			for (Map.Entry<String, String> entry : queryMap.entrySet()) {
				if (entry.getValue() != null && entry.getValue().trim().length() != 0){
						query+=" and "+entry.getKey()+"="+entry.getValue();
					}
			}
		}
		sql+=query+" order by voucherno";
		List<Object[]> result = (List<Object[]>) dao.findBySql(sql);
		return result;
	}
	
	//对账中心业务量统计
	@Override
	public List<?> getBusinessStatisticsResult(Map<String, String> queryMap,
			PageParam pageParam,String docDate,boolean isPaged) throws XDocProcException {
		// TODO Auto-generated method stub
		String sql = "";
		String selectSql = "select b.idcenter,b.idbranch,b.idbank,b.bankname, "
				+ "count(case when a.sendmode = '"+FinalConstant.sendModelMap.get("柜台")+"' then a.accno end) as counteraccount, "// 柜台对账账户数
				+ "count(case when a.sendmode = '"+FinalConstant.sendModelMap.get("邮寄")+"' then a.accno end) as postaccount, " + // 邮寄对账账户数
					"count(case when a.sendmode = '"+FinalConstant.sendModelMap.get("网银")+"' then a.accno end) as netaccount, " +
					"count(case when a.sendmode = '"+FinalConstant.sendModelMap.get("面对面")+"' then a.accno end) as faceaccount, " +
					"count(case when (a.sendmode is null) then a.accno end) as otheraccount, "
				+ "count(b.accno) as idcenterCount, "// 对账中心账户数
				+ "count(a.accno) as idcenteraccount2 "// 应对账账户数
				+ "from ebs_basicinfo b left join " 
				+"(select distinct(c.accno),c.sendmode from ebs_accnomaindata c where 1=1 and c.docdate='"+docDate.trim()+ "') a on(b.accno = a.accno) ";
		
		String whereSql = " where 1=1 ";
		// 循环Map取出查询条件
		if (queryMap != null) {
			for (Map.Entry<String, String> entry : queryMap.entrySet()) {
				if (entry.getValue() != null
						&& entry.getValue().trim().length() != 0) {
					whereSql += " and " +" b."+ entry.getKey().trim() + "='"
							+ entry.getValue().trim() + "' ";
				}
			}
		}
		
		String groupSql = " group by b.idcenter, b.idbranch, b.idbank, b.bankname order by b.idcenter,b.idbank";
		
		sql = selectSql + whereSql + groupSql;
		if(isPaged){
			Integer countNumber = dao.findBySql(sql).size();
			int pageSize = pageParam.getPageSize();
			int totalPage = (int) ((countNumber - 1) / pageSize + 1);
			int curPage = pageParam.getCurPage();
			if (curPage > totalPage) {
				curPage = totalPage;
			}
			int firstResult = (curPage - 1) * pageSize;
			List<?> list = dao.findBySql(sql, firstResult, pageSize);
			pageParam.setFirstResult(firstResult);
			pageParam.setCurPage(curPage);
			pageParam.setTotal((int) countNumber);
			pageParam.setTotalPage(totalPage);
			pageParam.setLastResult(firstResult + list.size());
			return list;
		}else{
			return dao.findBySql(sql);
		}
	}
	
	//对账中心业务量统计(按单位统计)
	@Override
	public List<?> getBusinessStatisticsResultCount(
			Map<String, String> queryMap, PageParam pageParam, String docDate,
			boolean isPaged, String selectCount) throws XDocProcException {
		// TODO Auto-generated method stub
		String sql = "";
		String selectSql = "select  ";
		if(selectCount!=null && selectCount.equals("countIdCenter")){
			selectSql += " b.idCenter,";
		}
		selectSql += "count(case when a.sendmode = '"+FinalConstant.sendModelMap.get("柜台")+"' then a.accno end) as counteraccount, "// 柜台对账账户数
					+ "count(case when a.sendmode = '"+FinalConstant.sendModelMap.get("邮寄")+"' then a.accno end) as postaccount, " + // 邮寄对账账户数
						"count(case when a.sendmode = '"+FinalConstant.sendModelMap.get("网银")+"' then a.accno end) as netaccount, " +
						"count(case when a.sendmode = '"+FinalConstant.sendModelMap.get("面对面")+"' then a.accno end) as faceaccount, " +
						"count(case when (a.sendmode is null) then a.accno end) as otheraccount, "
					+ "count(b.accno) as idcenterCount, "// 对账中心账户数
					+ "count(a.accno) as idcenteraccount2 "// 应对账账户数
					+ "from ebs_basicinfo b left join " 
					+"(select distinct(c.accno),c.sendmode from ebs_accnomaindata c where 1=1 and c.docdate='"+docDate.trim()+ "') a on(b.accno = a.accno) ";
		
		String whereSql = " where 1=1 ";
		// 循环Map取出查询条件
		if (queryMap != null) {
			for (Map.Entry<String, String> entry : queryMap.entrySet()) {
				if (entry.getValue() != null
						&& entry.getValue().trim().length() != 0) {
					whereSql += " and " +" b."+ entry.getKey().trim() + "='"
							+ entry.getValue().trim() + "' ";
				}
			}
		}
		
		String groupSql = " ";
		String	orderSql = " ";
		if(selectCount!=null && selectCount.equals("countIdCenter")){
			groupSql += " group by b.idcenter ";
			orderSql += " order by b.idcenter";
		}
		sql = selectSql + whereSql + groupSql + orderSql;
		if(isPaged){
			Integer countNumber = dao.findBySql(sql).size();
			int pageSize = pageParam.getPageSize();
			int totalPage = (int) ((countNumber - 1) / pageSize + 1);
			int curPage = pageParam.getCurPage();
			if (curPage > totalPage) {
				curPage = totalPage;
			}
			int firstResult = (curPage - 1) * pageSize;
			List<?> list = dao.findBySql(sql, firstResult, pageSize);
			pageParam.setFirstResult(firstResult);
			pageParam.setCurPage(curPage);
			pageParam.setTotal((int) countNumber);
			pageParam.setTotalPage(totalPage);
			pageParam.setLastResult(firstResult + list.size());
			return list;
		}else{
			return dao.findBySql(sql);
		}
	}


	@Override
	public List<?> getAllBusinessStatisticsResult(Map<String, String> queryMap,String docDate)
			throws XDocProcException {
		// TODO Auto-generated method stub
		String sql = "where 1=1 ";
		// 循环Map取出查询条件
		if (queryMap != null) {
			for (Map.Entry<String, String> entry : queryMap.entrySet()) {
				if (entry.getValue() != null
						&& entry.getValue().trim().length() != 0) {
					sql += " and " +"b."+ entry.getKey().trim() + "='"
							+ entry.getValue().trim() + "' ";
				}
			}
		}
		sql = CreateSql.businessStatisticsSql(sql,docDate);
		List<?> list = dao.findBySql(sql);
		return list;
	}

	
	

	

	
}