/**
 * AccoperBizImpl.java
 * 版权所有(C) 2013 深圳市银之杰科技股份有限公司
 * 创建:Jiangzhengqiu 2013-03-29
 */
package com.yzj.ebs.manuprove.biz.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.yzj.ebs.common.ICheckMainDataAdm;
import com.yzj.ebs.common.IDocSetAdm;
import com.yzj.ebs.common.INewSealAdm;
import com.yzj.ebs.common.IPublicTools;
import com.yzj.ebs.common.ISealLogAdm;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.database.DocSet;
import com.yzj.ebs.database.SealLog;
import com.yzj.ebs.manuprove.action.ManuProveJson;
import com.yzj.ebs.manuprove.biz.IManuProveBiz;

/**
 * 创建于:2013-03-29 版权所有(C) 2013 深圳市银之杰科技股份有限公司
 * 
 * 人工验印业务逻辑统一实现
 * 
 * @author jiangzhengqiu
 * @version 1.0.0
 */
public class ManuProveBizImpl implements IManuProveBiz {
	private IDocSetAdm docSetAdm;
	
	private ICheckMainDataAdm checkMainDataAdm;
	private INewSealAdm newSealAdm;
	private ISealLogAdm seallogAdm;
	private IPublicTools publicTools;

	public DocSet queryOneByID(Long docId) throws XDocProcException
	{
		return docSetAdm.queryOneByID(docId);
	}
	
	/***
	 * 写验印日志
	 */
	public void create(String sealLogStr,DocSet docSet,ManuProveJson manuProveJsonObj) throws Exception{
		Document doc = null;
		if (sealLogStr != null && sealLogStr != "") {
			sealLogStr = sealLogStr.toUpperCase();
			SealLog seallog = new SealLog();
			try {
				doc = DocumentHelper.parseText(sealLogStr);
				Element rootElt = doc.getRootElement(); // 获取根节点
				System.out.println("根节点：" + rootElt.getName()); // 拿到根节点的名称
				Element iter = rootElt.element("SBLOGS"); // 获取根节点下的子节点head
				List listItem = iter.elements("SBLOG");
				Iterator itr = listItem.iterator();

				while (itr.hasNext()) {
					Element itemEle = (Element) itr.next();
					seallog.setAccNo(itemEle.elementTextTrim("ACCNO"));
					seallog.setDocId(docSet.getDocId());
					seallog.setLjyjdm(0);
					seallog.setSealMode(Integer.valueOf(manuProveJsonObj
							.getSealMode()));
					seallog.setOpCode(itemEle.elementTextTrim("OPCODE"));
					seallog.setOpDate(itemEle.elementTextTrim("OPDATE"));
					seallog.setOpTime(itemEle.elementTextTrim("OPTIME"));
					seallog.setResult(Integer.valueOf(itemEle
							.elementTextTrim("OPRESULT")));
					seallog.setVoucherNo(itemEle.elementTextTrim("CHEQUENO"));
					seallog.setYjdm(Long.valueOf(itemEle
							.elementTextTrim("YJDM")));
					seallogAdm.create(seallog);
				}
			} catch (Exception e) {
				String errMsg = "插入验印日志出现错误:";
				throw new Exception(errMsg);
			}
		}
		
	}
	/**
	 * 根据对账单编号获取账号列表
	 * 
	 * @return List<ExChangeBook>
	 * @throws XDocProcException
	 */
	public List<Object[]> getSealAccno(String voucherno) throws XDocProcException
	{
		return checkMainDataAdm.getSealAccno(voucherno);
	}
	
	/**
	 * 获取账号列表
	 * 
	 * @return List<ExChangeBook>
	 * @throws XDocProcException
	 */
	public String getAccNoList(DocSet docSet,ManuProveJson manuProveJsonObj) throws XDocProcException 
	{
		String accnoList = "";
		String billNo = docSet.getVoucherNo(); // 账单编号
		try {
			if (billNo != null && !"".equals(billNo)) {
				List<Object[]> listAccno = checkMainDataAdm.getSealAccno(docSet
						.getVoucherNo());
				// 根据账号存放验印模式
				HashMap<String, String> sealAccno = new HashMap<String, String>();
				String findSql = "";
				String tmpSealaccnoList = "";
				if (listAccno.size() > 0) {
					for (int i = 0; i < listAccno.size(); i++) {
						String sealaccno = listAccno.get(i)[0].toString();
						String sealmode = listAccno.get(i)[1] == null ? "0"
								: listAccno.get(i)[1].toString();// 若验印模式为空则返回0，验印全部预留印鉴
						sealAccno.put(sealaccno, sealmode);
						manuProveJsonObj.setSealMode(sealmode);
						findSql = findSql == "" ? "'" + sealaccno + "'"
								: findSql + ",'" + sealaccno + "'";
						tmpSealaccnoList = tmpSealaccnoList == "" ? sealaccno
								: tmpSealaccnoList + "|" + sealaccno;
					}
					findSql = "select accno from zl where accno in (" + findSql
							+ ")";
					List<Object> listSealAccno = newSealAdm.findBySql(findSql);
					// 循环取出该账单编号下的客户下的所有账户的账号进行验印。
					if (listSealAccno.size() > 0) {
						for (int i = 0; i < listSealAccno.size(); i++) {
							// 传入到人工验印控件的账号格式为 "账号-验印模式"
							String sealaccno = listSealAccno.get(i).toString()
									+ "-"
									+ sealAccno.get(listSealAccno.get(i)
											.toString());
							if (accnoList.equals("")) {
								accnoList = sealaccno;
							} else {
								accnoList = accnoList + "|" + sealaccno;
							}
						}
					} else {
						accnoList = tmpSealaccnoList;
					}
				}
			}
			return accnoList;
		} catch (XDocProcException e) {
			throw new XDocProcException("获取任务出现错误:");
			
		}
	}
	
	/**
	 * 获取验印不过理由参数
	 * 
	 */
	public String getUntreadList() throws Exception {
		Map<String, String> uReasonMap = new HashMap<String, String>();
		String uReasonList = "";
		Object keySet[];
		try {
			uReasonMap = publicTools.getUReason();
			keySet = uReasonMap.keySet().toArray();
			for (Object obj : keySet) {
				uReasonList = uReasonList + obj.toString() + "~"
						+ uReasonMap.get(obj).toString() + "@";
			}
		} catch (XDocProcException e) {
			throw new Exception("获取验印不过理由参数出现错误:");
		}
		return uReasonList;
	}
	
	public IDocSetAdm getDocSetAdm() {
		return docSetAdm;
	}
	public void setDocSetAdm(IDocSetAdm docSetAdm) {
		this.docSetAdm = docSetAdm;
	}
	public ICheckMainDataAdm getCheckMainDataAdm() {
		return checkMainDataAdm;
	}
	public void setCheckMainDataAdm(ICheckMainDataAdm checkMainDataAdm) {
		this.checkMainDataAdm = checkMainDataAdm;
	}
	public INewSealAdm getNewSealAdm() {
		return newSealAdm;
	}
	public void setNewSealAdm(INewSealAdm newSealAdm) {
		this.newSealAdm = newSealAdm;
	}
	public ISealLogAdm getSeallogAdm() {
		return seallogAdm;
	}
	public void setSeallogAdm(ISealLogAdm seallogAdm) {
		this.seallogAdm = seallogAdm;
	}
	public IPublicTools getPublicTools() {
		return publicTools;
	}

	public void setPublicTools(IPublicTools publicTools) {
		this.publicTools = publicTools;
	}

}
