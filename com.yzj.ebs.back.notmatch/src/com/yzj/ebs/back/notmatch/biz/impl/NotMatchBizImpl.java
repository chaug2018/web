/**
 * AccoperBizImpl.java
 * 版权所有(C) 2013 深圳市银之杰科技股份有限公司
 * 创建:Jiangzhengqiu 2013-03-29
 */
package com.yzj.ebs.back.notmatch.biz.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yzj.ebs.back.notmatch.biz.INotMatchBiz;
import com.yzj.ebs.common.IAccnoMainDataAdm;
import com.yzj.ebs.common.IDocSetAdm;
import com.yzj.ebs.common.INotMatchTableAdm;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.database.AccNoMainData;
import com.yzj.ebs.database.DocSet;
import com.yzj.ebs.database.NotMatchTable;

/**
 * 创建于:2013-03-29 版权所有(C) 2013 深圳市银之杰科技股份有限公司
 * 
 * 账户信息业务逻辑统一实现
 * 
 * @author jiangzhengqiu
 * @version 1.0.0
 */
public class NotMatchBizImpl implements INotMatchBiz {

	private IDocSetAdm docSetAdm;
	private INotMatchTableAdm notMatchTableAdm;
	private IAccnoMainDataAdm accnoMainDataAdm;

	/**
	 * 根据流水号查询单个票据
	 * 
	 * @param DocSet
	 *            票据对象
	 * 
	 */
	@Override
	public DocSet queryOneByID(Long docId) throws XDocProcException {
		return docSetAdm.queryOneByID(docId);
	}

	/**
	 * 根据流水号查询未达列表
	 * 
	 * @param List
	 *            <NotMatchTable> 未达列表
	 * 
	 */
	@Override
	public List<NotMatchTable> getNotMatchListByDocId(String docId)
			throws XDocProcException {
		return notMatchTableAdm.getNotMatchListByDocId(docId);
	}

	/**
	 * 更新未达列表
	 * 
	 * 
	 * 
	 */
	@Override
	public void updateNotMatchList(List<NotMatchTable> notMatchList)
			throws XDocProcException {
		notMatchTableAdm.updateNotMatchList(notMatchList);
	}

	/**
	 * 根据账单编号获取余额状态列表
	 * 
	 * @param voucherno
	 *            凭证号 return 账户信息列表
	 */
	@Override
	public List<AccNoMainData> getAccnoMainDataByVoucherNo(String voucherno)
			throws XDocProcException {
		return accnoMainDataAdm.getAccnoMainDataByVoucherNo(voucherno);
	}

	/**
	 * 删除未达列表
	 * 
	 * 
	 * 
	 */
	@Override
	public void deleteNotMatchList(List<NotMatchTable> notMatchList)
			throws XDocProcException {
		notMatchTableAdm.deleteNotMatchList(notMatchList);
	}

	/**
	 * 保存未达录入信息
	 * 
	 * @param notMatchList
	 *            未达列表
	 * 
	 */
	@Override
	public void saveNotMatchItems(List<NotMatchTable> notMatchList)
			throws XDocProcException {
		notMatchTableAdm.saveNotMatchItems(notMatchList);
	}

	/**
	 * 根据条件查询未达列表
	 * 
	 * @param queryMap
	 *            查询条件 param 分页信息 return 未达列表
	 */
	@Override
	public List<NotMatchTable> getNotMatchTableData(
			Map<String, String> queryMap, PageParam param)
			throws XDocProcException {
		return notMatchTableAdm.getNotMatchTableData(queryMap, param);
	}

	/**
	 * 取得所有的未达信息
	 * 
	 * 
	 * return 未达列表
	 */
	@Override
	public List<NotMatchTable> getAllNotMatchData(Map<String, String> queryMap)
			throws XDocProcException {
		return notMatchTableAdm.getAllNotMatchData(queryMap);
	}

	/**
	 * 根据账号及账单编号更新accnomaindata表的checkflag字段
	 * 
	 * @param notMatchList
	 *            未达明细
	 */
	public void updateAccnoMainData(List<NotMatchTable> notMatchList,
			DocSet docSet) throws XDocProcException {
		HashMap<String, String> accnoCheckflagMap = new HashMap<String, String>();
		for (NotMatchTable notMatchTable : notMatchList) {
			if (!accnoCheckflagMap.containsKey(notMatchTable.getAccNo() + "|"
					+ notMatchTable.getCheckFlag())) {
				// 循环未达明细，把账号和余额结果放到MAP中
				accnoCheckflagMap.put(notMatchTable.getAccNo() + "|"
						+ notMatchTable.getCheckFlag(), "");
			}
		}

		// 更新checkflag
		String accno;
		String checkfalg;
		if (!accnoCheckflagMap.isEmpty()) {
			for (Map.Entry<String, String> entry : accnoCheckflagMap.entrySet()) {
				if (entry.getKey() != null
						&& entry.getKey().trim().length() != 0) {
					accno = entry.getKey().split("[|]")[0];
					checkfalg = entry.getKey().split("[|]")[1];
					boolean isRight = true;
					// 如果未达全部为相符时才调节为相符，只要有一笔不符则改成不相符
					for (NotMatchTable notMatchTable : notMatchList) {
						if (accno.equals(notMatchTable.getAccNo())) {
							// 只要有1笔为不相符则视为不符 2:对账不符 5:人工调节不符
							if ("2".equals(notMatchTable.getCheckFlag())
									|| "5".equals(notMatchTable.getCheckFlag())) {
								isRight = false;
							}
						}
					}

					if (isRight) {
						// 全部为相符的情况
						notMatchTableAdm.updateAccnomaindata(accno, "4",
								docSet.getVoucherNo());
					} else {
						notMatchTableAdm.updateAccnomaindata(accno, "5",
								docSet.getVoucherNo());
					}
					isRight = true;
				}
			}
		}
	}

	public IDocSetAdm getDocSetAdm() {
		return docSetAdm;
	}

	public void setDocSetAdm(IDocSetAdm docSetAdm) {
		this.docSetAdm = docSetAdm;
	}

	public INotMatchTableAdm getNotMatchTableAdm() {
		return notMatchTableAdm;
	}

	public void setNotMatchTableAdm(INotMatchTableAdm notMatchTableAdm) {
		this.notMatchTableAdm = notMatchTableAdm;
	}

	public IAccnoMainDataAdm getAccnoMainDataAdm() {
		return accnoMainDataAdm;
	}

	public void setAccnoMainDataAdm(IAccnoMainDataAdm accnoMainDataAdm) {
		this.accnoMainDataAdm = accnoMainDataAdm;
	}
}
