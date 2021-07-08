package com.yzj.ebs.manualinput.biz.impl;

import java.text.DecimalFormat;
import java.util.List;

import com.yzj.ebs.common.IAccnoMainDataAdm;
import com.yzj.ebs.common.ICheckMainDataAdm;
import com.yzj.ebs.common.IDocSetAdm;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.database.AccNoMainData;
import com.yzj.ebs.database.CheckMainData;
import com.yzj.ebs.database.DocSet;
import com.yzj.ebs.manualinput.biz.IManualBiz;
import com.yzj.ebs.manualinput.param.ManualAuthInfo;
import com.yzj.ebs.manualinput.param.ManualInputInfo;
/**
 * 
 *创建于:2013-4-11 <br>
 *版权所有(C) 2013 深圳市银之杰科技股份有限公司<br>
 * 数据补录及数据复核逻辑操作实现类
 * @author 施江敏
 * @version 1.0.0
 */
public class ManualBizImpl implements IManualBiz {
	private IAccnoMainDataAdm accnoMainDataAdm;
	private ICheckMainDataAdm checkMainDataAdm;
	private IDocSetAdm docSetAdm;
	
	@Override
	public void batchUpdate(List<AccNoMainData> accNolist) throws XDocProcException {
		accnoMainDataAdm.batchUpdate(accNolist);
		
	}
	
	public IAccnoMainDataAdm getAccnoMainDataAdm() {
		return accnoMainDataAdm;
	}
	@Override
	public List<AccNoMainData> getAccnoMainDataByVoucherNo(String voucherNo) throws XDocProcException {
		return accnoMainDataAdm.getAccnoMainDataByVoucherNo(voucherNo);
	}
	public ICheckMainDataAdm getCheckMainDataAdm() {
		return checkMainDataAdm;
	}
	public IDocSetAdm getDocSetAdm() {
		return docSetAdm;
	}
	@Override
	public ManualInputInfo getManualInputInfo(List<AccNoMainData> accNolist,
			ManualInputInfo info) {
		DecimalFormat df = new DecimalFormat("0.00"); 
		for (int i = 0; i < accNolist.size(); i++) {
			AccNoMainData data = accNolist.get(i);
			if (i == 0) {
				info.setAccNo1(data.getAccNo());
				info.setAccNo_1(df.format(data.getCredit()));
				if (data.getCheckFlag() == null
						|| (!"2".equals(data.getCheckFlag()) && !"3"
								.equals(data.getCheckFlag()))) {
					info.setResult1("2");
				} else {
					info.setResult1(data.getCheckFlag());
				}
			} else if (i == 1) {
				info.setAccNo2(data.getAccNo());
				info.setAccNo_2(df.format(data.getCredit()));
				if (data.getCheckFlag() == null
						|| (!"2".equals(data.getCheckFlag()) && !"3"
								.equals(data.getCheckFlag()))) {
					info.setResult2("2");
				} else {
					info.setResult2(data.getCheckFlag());
				}
			} else if (i == 2) {
				info.setAccNo3(data.getAccNo());
				info.setAccNo_3(df.format(data.getCredit()));
				if (data.getCheckFlag() == null
						|| (!"2".equals(data.getCheckFlag()) && !"3"
								.equals(data.getCheckFlag()))) {
					info.setResult3("2");
				} else {
					info.setResult3(data.getCheckFlag());
				}
			} else if (i == 3) {
				info.setAccNo4(data.getAccNo());
				info.setAccNo_4(df.format(data.getCredit()));
				if (data.getCheckFlag() == null
						|| (!"2".equals(data.getCheckFlag()) && !"3"
								.equals(data.getCheckFlag()))) {
					info.setResult4("2");
				} else {
					info.setResult4(data.getCheckFlag());
				}
			} else if (i == 4) {
				info.setAccNo5(data.getAccNo());
				info.setAccNo_5(df.format(data.getCredit()));
				if (data.getCheckFlag() == null
						|| (!"2".equals(data.getCheckFlag()) && !"3"
								.equals(data.getCheckFlag()))) {
					info.setResult5("2");
				} else {
					info.setResult5(data.getCheckFlag());
				}
			}
		}
		return info;
	}
	@Override
	public ManualAuthInfo getManualAuthInfo(List<AccNoMainData> accNolist,
			ManualAuthInfo info) {
		DecimalFormat df = new DecimalFormat("0.00"); 
		for (int i = 0; i < accNolist.size(); i++) {
			AccNoMainData data = accNolist.get(i);
			if (i == 0) {
				info.setAccNo1(data.getAccNo());
				info.setAccNo_1(df.format(data.getCredit()));
				info.setResult1(data.getCheckFlag());
			} else if (i == 1) {
				info.setAccNo2(data.getAccNo());
				info.setAccNo_2(df.format(data.getCredit()));
				info.setResult2(data.getCheckFlag());
			} else if (i == 2) {
				info.setAccNo3(data.getAccNo());
				info.setAccNo_3(df.format(data.getCredit()));
				info.setResult3(data.getCheckFlag());
			} else if (i == 3) {
				info.setAccNo4(data.getAccNo());
				info.setAccNo_4(df.format(data.getCredit()));
				info.setResult4(data.getCheckFlag());
			} else if (i == 4) {
				info.setAccNo5(data.getAccNo());
				info.setAccNo_5(df.format(data.getCredit()));
				info.setResult5(data.getCheckFlag());
			}
		}
		return info;
	
	}
	@Override
	public CheckMainData getOneByVoucherNo(String billNo) throws XDocProcException {
		return checkMainDataAdm.getOneByVoucherNo(billNo);
	}

	@Override
	public DocSet queryOneByID(Long docId) throws XDocProcException {
		return docSetAdm.queryOneByID(docId);
	}

	public void setAccnoMainDataAdm(IAccnoMainDataAdm accnoMainDataAdm) {
		this.accnoMainDataAdm = accnoMainDataAdm;
	}

	public void setCheckMainDataAdm(ICheckMainDataAdm checkMainDataAdm) {
		this.checkMainDataAdm = checkMainDataAdm;
	}

	public void setDocSetAdm(IDocSetAdm docSetAdm) {
		this.docSetAdm = docSetAdm;
	}

	@Override
	public void updateCheckMainDataByDocSet(CheckMainData data,DocSet docSet) throws XDocProcException {
		data.setStoreId(docSet.getStoreId());// 将存储id写入CheckMainData
		data.setFrontImagePath(docSet.getFrontImagePath());
		checkMainDataAdm.update(data);
	}

}
