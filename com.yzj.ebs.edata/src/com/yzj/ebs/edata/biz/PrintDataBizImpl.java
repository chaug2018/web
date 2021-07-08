/**
 * PrintDataBizImpl.java
 * 版权所有(C) 2013 深圳市银之杰科技股份有限公司
 * 创建:秦靖锋  2013-03-29
 */
package com.yzj.ebs.edata.biz;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.ServletActionContext;
import com.infotech.publiclib.Exception.DaoException;
import com.yzj.ebs.common.BankParam;
import com.yzj.ebs.common.IAccnoDetailAdm;
import com.yzj.ebs.common.IAccnoMainDataAdm;
import com.yzj.ebs.common.ICheckMainDataAdm;
import com.yzj.ebs.common.IParamBank;
import com.yzj.ebs.common.IPublicTools;

import com.yzj.ebs.common.RefTableTools;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.database.AccNoDetailData;
import com.yzj.ebs.database.AccNoMainData;
import com.yzj.ebs.database.CheckMainData;
import com.yzj.ebs.database.temp.hbm.EbsMarginData;
import com.yzj.ebs.edata.bean.AccnoMainDataQueryParam;
import com.yzj.ebs.edata.bean.BatchPrintBean;

import com.yzj.ebs.util.UtilBase;
import com.yzj.wf.am.security.common.AMSecurityDefine;
import com.yzj.wf.common.WFLogger;
import com.yzj.wf.core.model.po.wrapper.XPeopleInfo;

/**
 * 创建于:2013-03-29 版权所有(C) 2013 深圳市银之杰科技股份有限公司
 * 
 * 打印
 * 
 * @author 秦靖锋
 * @version 1.0.0
 */
public class PrintDataBizImpl implements IPrintDataBiz {

	/**
	 * 自动生成序列
	 */
	private IPublicTools tools;
	private ICheckMainDataAdm checkMainDataAdm;
	private IAccnoMainDataAdm accnoMainDataAdm;
	private IAccnoDetailAdm accnoDetailAdm;
	private IParamBank  paramBank;
	public BatchPrintBean total = null;
	private static WFLogger logger = WFLogger.getLogger(PrintDataBizImpl.class);
	private RefTableTools refTableTools;

	public List<BatchPrintBean> printData(CheckMainData data,AccnoMainDataQueryParam param,String printType) throws Exception {
		List<BatchPrintBean> lstData = new ArrayList<BatchPrintBean>();
		Map<String,String> queryMap =  new HashMap<String,String>();
		queryMap.put("voucherno", "'"+data.getVoucherNo()+"'");
		try {
			List<Object[]> detailList = null;
			if("dataAndDetails".equals(printType)){
				//得到全部明细
				detailList = checkMainDataAdm.getDeatil(queryMap, param.getDocDate().substring(5 ,7),false,0,0);	
			}else if("data".equals(printType)){
				//只打印对账单，不打明细
			}
			
			lstData = getPrintList(data,getChangeType(detailList));
			int printNum = 1;
			if (data.getPrintTimes() != null) {
				printNum = data.getPrintTimes().intValue() + 1;
			}
			checkMainDataAdm.updateCheckmaindataByVoucherno(
					data.getVoucherNo(), printNum);
		} catch (XDocProcException e) {
			e.printStackTrace();
			logger.error("打印账单后更改账单状态为等待回收出现错误", e);
		}

		return lstData;
	}
	
	public List<Map<String,String>>  printDetailsData(CheckMainData data,AccnoMainDataQueryParam param,int firstNum,int pageNum) throws Exception{
		List<Map<String,String>> detailsData = new ArrayList<Map<String,String>>() ;
		Map<String,String> queryMap =  new HashMap<String,String>();
		queryMap.put("voucherno", "'"+data.getVoucherNo()+"'");
		try {
			//得到分页明细
			List<Object[]> detailList = checkMainDataAdm.getDeatil(queryMap, param.getDocDate().substring(5 ,7),true,firstNum,pageNum);	
			detailsData=getChangeType(detailList);
			
		} catch (XDocProcException e) {
			e.printStackTrace();
			logger.error("打印账单后更改账单状态为等待回收出现错误", e);
		}

		return detailsData;
	}
	/**
	 * 返回打印的源数据列表
	 * 
	 * @return
	 * @throws DaoException
	 */
	public List<BatchPrintBean> getPrintList(CheckMainData data,List<Map<String,String>> detailList)
			throws XDocProcException {
		/** 账单信息：账单编号、对账日期、客户号、户名、邮政编码、地址、联系人、联系电话 */
		
		List<AccNoMainData> docList = new ArrayList<AccNoMainData>();
		List<BatchPrintBean> lstData = new ArrayList<BatchPrintBean>();
		total = new BatchPrintBean();
		total.setVoucherno(data.getVoucherNo());
		String date = data.getDocDate().substring(0, 4) + "-"
				+ data.getDocDate().substring(4, 6) + "-"
				+ data.getDocDate().substring(6, 8);
		total.setDocdate(date);
		total.setCustomerid(data.getCustId());
		
		if (data.getZip() != null && !"null".equals(data.getZip())) {
			total.setZip("邮政编码：" + data.getZip());
		} else {
			total.setZip("邮政编码：");
		}
		if (data.getSendAddress() != null && !"null".equals(data.getSendAddress())) {
			total.setAddress("邮寄地址：" + data.getSendAddress());
		} else {
			total.setAddress("邮寄地址：");
		}
		if (data.getLinkMan() != null && !"null".equals(data.getLinkMan())) {
			total.setLinkman("联系人：" + data.getLinkMan());
		} else {
			total.setLinkman("联系人：");
		}
		if (data.getPhone() != null && !"null".equals(data.getPhone())) {
			total.setPhone("客户电话：" + data.getPhone());
		} else {
			total.setPhone("客户电话：");
		}
		if (data.getAccName() != null && !"null".equals(data.getAccName())) {
			total.setAccname("客户名称：" + data.getAccName());
		} else {
			total.setAccname("客户名称：");
		}

		// 对账中心电话 地址
		BankParam cp = paramBank.getBankParam(data.getIdCenter());
		
		if (cp.getPhone() != null && !"null".equals(cp.getPhone())) {
			total.setCenterphone("电话：" + cp.getPhone());
		} else {
			total.setCenterphone("电话：");
		}
		if (cp.getAddress() != null && !"null".equals(cp.getAddress())) {
			total.setCenteraddress("地址：" + cp.getAddress());
		} else {
			total.setCenteraddress("地址：");
		}
		if (cp.getcName() != null && !"null".equals(cp.getcName())) {
			total.setIdCenterName("分行 ：" + cp.getcName());
		} else {
			total.setIdCenterName("分行 ：");
		}

		// 对账单发送地址
		if (data.getSendAddress() != null && !"null".equals(data.getSendAddress())) {
			total.setSendAddress("发送地址：" + data.getSendAddress());
		} else {
			total.setSendAddress("发送地址：");
		}
	
		docList = accnoMainDataAdm.getAccnoMainDataByVoucherNo(data
				.getVoucherNo());
		for (int j = 0; j < docList.size(); j++) {
			AccNoMainData doc = (AccNoMainData) docList.get(j);
			
			Map<String,String> subMap = RefTableTools.ValParamSubnocMap;
			Map<String,String> sealModeMap = RefTableTools.ValRefSealModeMap;
			Map<String, String> currType = new HashMap<String, String>();
			try {
				currType = refTableTools.getParamSub("param_currtype");
			} catch (DaoException e) {
				e.printStackTrace();
			}
			/** 账号信息：账号、账户类型、开户行名、余额、币种 */
			if (j == 0) {
				if (doc.getAccNo() != null) {
					total.setAccno1(doc.getAccNo());
					//add 20151222 打印模板增加验印模式
					String sealmode = accnoMainDataAdm.getSealmodeByAccNo(doc.getAccNo());
					String chnSealmode=(sealModeMap.get(sealmode)==null)?"":sealModeMap.get(sealmode);
					total.setSealmode(chnSealmode);
				}
				if (doc.getSubjectNo() != null) {
					total.setSubjectNo1(subMap.get(doc.getSubjectNo()));
				}
				if (doc.getIdBank() != null) {
					total.setIdbank1(doc.getIdBank());
				}
				if (doc.getBankName() != null) {
					total.setBankname1(doc.getBankName());
				}
				if (doc.getCredit() != null) {
					total.setCredit1(UtilBase.formatString(doc.getCredit()));
				}
				if (doc.getCurrency() != null) {
					total.setCurrtypeCN1(currType.get(doc.getCurrency()));
				}
			}
			if (j == 1) {
				if (doc.getAccNo() != null) {
					total.setAccno2(doc.getAccNo());
				}
				if (doc.getSubjectNo() != null) {
					total.setSubjectNo2(subMap.get(doc.getSubjectNo()));
				}
				if (doc.getIdBank() != null) {
					total.setIdbank2(doc.getIdBank());
				}
				if (doc.getBankName() != null) {
					total.setBankname2(doc.getBankName());
				}
				if (doc.getCredit() != null) {
					total.setCredit2(UtilBase.formatString(doc.getCredit()));
				}
				if (doc.getCurrency() != null) {
					total.setCurrtypeCN2(currType.get(doc.getCurrency()));
				}
			}
			if (j == 2) {
				if (doc.getAccNo() != null) {
					total.setAccno3(doc.getAccNo());
				}
				if (doc.getSubjectNo() != null) {
					total.setSubjectNo3(subMap.get(doc.getSubjectNo()));
				}
				if (doc.getIdBank() != null) {
					total.setIdbank3(doc.getIdBank());
				}
				if (doc.getIdBank() != null) {
					total.setBankname3(doc.getIdBank());
				}
				if (doc.getCredit() != null) {
					total.setCredit3(UtilBase.formatString(doc.getCredit()));
				}
				if (doc.getCurrency() != null) {
					total.setCurrtypeCN3(currType.get(doc.getCurrency()));
				}
			}
			if (j == 3) {
				if (doc.getAccNo() != null) {
					total.setAccno4(doc.getAccNo());
				}
				if (doc.getSubjectNo() != null) {
					total.setSubjectNo4(subMap.get(doc.getSubjectNo()));
				}
				if (doc.getIdBank() != null) {
					total.setIdbank4(doc.getIdBank());
				}
				if (doc.getBankName() != null) {
					total.setBankname4(doc.getBankName());
				}
				if (doc.getCredit() != null) {
					total.setCredit4(UtilBase.formatString(doc.getCredit()));
				}
				if (doc.getCurrency() != null) {
					total.setCurrtypeCN4(currType.get(doc.getCurrency()));
				}
			}
			if (j == 4) {
				if (doc.getAccNo() != null) {
					total.setAccno5(doc.getAccNo());
				}
				if (doc.getSubjectNo() != null) {
					total.setSubjectNo5(subMap.get(doc.getSubjectNo()));
				}
				if (doc.getIdBank() != null) {
					total.setIdbank5(doc.getIdBank());
				}
				if (doc.getBankName() != null) {
					total.setBankname5(doc.getBankName());
				}
				if (doc.getCredit() != null) {
					total.setCredit5(UtilBase.formatString(doc.getCredit()));
				}
				if (doc.getCurrency() != null) {
					total.setCurrtypeCN5(currType.get(doc.getCurrency()));
				}
			}
		}	
		
		if(detailList!=null && detailList.size()>0){			
			total.setAccDetails(detailList);
//			if(detailList.size()==1){
//				if("".equals(detailList.get(0).get("credit")) && "".equals(detailList.get(0).get("accno")) && "".equals(detailList.get(0).get("workdate")) && "".equals(detailList.get(0).get("vouno")) && "".equals(detailList.get(0).get("abs"))){
//					total.setAccDetails(null);
//				}
//			}else{
//				total.setAccDetails(detailList);
//			}
		}
		lstData.add(total);
		return lstData;
	}

	public List<CheckMainData> queryCheckMainDataInfoByAccnoAndDocdate(
			AccnoMainDataQueryParam accnoMainDataQueryParam)
			throws XDocProcException {
		Map<String, String> mapdata = new HashMap<String, String>();

		Map<String, String> accQueryMap = new HashMap<String, String>();
		accQueryMap.put("accNo", accnoMainDataQueryParam.getAccNo().trim());
		String docdate = accnoMainDataQueryParam.getDocDate();// 对账日期

		if (docdate != null || !"".equals(docdate)) {
			mapdata.put("docDate", docdate.replace("-", ""));
		}
		List<CheckMainData> resultList = new ArrayList<CheckMainData>();

		// 按账号查账单信息
		List<Object[]> list = checkMainDataAdm.getCheckMaindata(mapdata,
				accnoMainDataQueryParam, accQueryMap);
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				CheckMainData cmd = new CheckMainData();
				if (list.get(i)[0] == null) {
					cmd.setVoucherNo("");
				} else {
					cmd.setVoucherNo(String.valueOf(list.get(i)[0] + ""));
				}
				if (list.get(i)[1] == null) {
					cmd.setIdBank("");
				} else {
					cmd.setIdBank(String.valueOf(list.get(i)[1] + ""));
				}
				if (list.get(i)[2] == null) {
					cmd.setBankName("");
				} else {
					cmd.setBankName(String.valueOf(list.get(i)[2] + ""));
				}
				if (list.get(i)[3] == null) {
					cmd.setAccName("");
				} else {
					cmd.setAccName(String.valueOf(list.get(i)[3] + ""));
				}
				if (list.get(i)[4] == null) {
					cmd.setCustId("");
				} else {
					cmd.setCustId(String.valueOf(list.get(i)[4] + ""));
				}
				if (list.get(i)[5] == null) {
					cmd.setLinkMan("");
				} else {
					cmd.setLinkMan(String.valueOf(list.get(i)[5] + ""));
				}
				if (list.get(i)[6] == null) {
					cmd.setAddress("");
				} else {
					cmd.setAddress(String.valueOf(list.get(i)[6] + ""));
				}
				if (list.get(i)[7] == null) {
					cmd.setPhone("");
				} else {
					cmd.setPhone(String.valueOf(list.get(i)[7] + ""));
				}
				cmd.setDocState(String.valueOf(list.get(i)[8] + ""));
				if (list.get(i)[9] != null) {
					cmd.setPrintTimes(new Integer(String.valueOf(list.get(i)[9]
							+ "")).intValue());
				} else {
					cmd.setPrintTimes(0);
				}
				cmd.setDocDate(String.valueOf(list.get(i)[10] + ""));
				if (list.get(i)[11] == null) {
					cmd.setZip("");
				} else {
					cmd.setZip(String.valueOf(list.get(i)[11] + ""));
				}
				resultList.add(cmd);
			}
		}
		return resultList;
	}

	/**
	 * 打印对账单数据 主张单数据以|#|分隔，主张单字段以|@|分隔，明细数据以|^|分隔，明细字段以|*|分隔
	 * 
	 * @return
	 * @throws IOException
	 */
	public String batchPrintData(List<CheckMainData> queryBillList)
			throws Exception {
		String returnStr = "";
		if (queryBillList.size() > 0) {
			for (int i = 0; i < queryBillList.size(); i++) {
				CheckMainData data = queryBillList.get(i);
				try {
					BatchPrintBean batchPrintBean = getBatchPrintList(data);
					int printNum = 1;
					if (data.getPrintTimes() != null) {
						printNum = data.getPrintTimes().intValue() + 1;
					}
					checkMainDataAdm.updateCheckmaindataByVoucherno(
							data.getVoucherNo(), printNum);
					// 主数据
					if (returnStr == "") {
						returnStr = getPrintDataByStr(batchPrintBean);
					} else {
						returnStr = returnStr + "|#|"
								+ getPrintDataByStr(batchPrintBean);
					}

					// 明细数据 ,
					String detailByStr = getPrintDetailByStr(batchPrintBean);
					returnStr = returnStr + "|@|" + detailByStr;

				} catch (XDocProcException e) {
					e.printStackTrace();
					logger.error("打印账单后更改账单状态为等待回收出现错误", e);
				}
			}
		}
		return returnStr;

	}

	/**
	 * 返回打印的源数据列表
	 * 
	 * @return
	 * @throws DaoException
	 */
	public BatchPrintBean getBatchPrintList(CheckMainData data)
			throws XDocProcException {
		/** 账单信息：账单编号、对账日期、客户号、户名、邮政编码、地址、联系人、联系电话 */
	
		total = new BatchPrintBean();
		total.setVoucherno(data.getVoucherNo());
		String date = data.getDocDate().substring(0, 4) + "-"
				+ data.getDocDate().substring(4, 6) + "-"
				+ data.getDocDate().substring(6, 8);
		total.setDocdate(date);
		total.setCustomerid(data.getCustId());

		if (data.getZip() != null && !"null".equals(data.getZip())) {
			total.setZip(data.getZip());
		} else {
			total.setZip("");
		}
		if (data.getAddress() != null && !"null".equals(data.getAddress())) {
			total.setAddress(data.getAddress());
		} else {
			total.setAddress("");
		}
		if (data.getLinkMan() != null && !"null".equals(data.getLinkMan())) {
			total.setLinkman(data.getLinkMan());
		} else {
			total.setLinkman("");
		}
		if (data.getPhone() != null && !"null".equals(data.getPhone())) {
			total.setPhone(data.getPhone());
		} else {
			total.setPhone("");
		}
		if (data.getAccName() != null && !"null".equals(data.getAccName())) {
			total.setAccname( data.getAccName());
		} else {
			total.setAccname("");
		}
		//对账中心电话 地址
		BankParam cp = paramBank.getBankParam(data.getIdCenter());
		
		if (cp.getPhone() != null && !"null".equals(cp.getPhone())) {
			total.setCenterphone("电话：" + cp.getPhone());
		} else {
			total.setCenterphone("电话：");
		}
		if (cp.getAddress() != null && !"null".equals(cp.getAddress())) {
			total.setCenteraddress("地址：" + cp.getAddress());
		} else {
			total.setCenteraddress("地址：");
		}

		//发送地址
		if (data.getSendAddress() != null && !"null".equals(data.getSendAddress())) {
			total.setSendAddress(data.getSendAddress());
		} else {
			total.setSendAddress("");
		}	
		List<AccNoMainData> docList = accnoMainDataAdm
				.getAccnoMainDataByVoucherNo(data.getVoucherNo());     //根据对账单编号得到 其下面的1-5个 明细信息
		for (int j = 0; j < docList.size(); j++) {
			AccNoMainData doc = (AccNoMainData) docList.get(j);
			Map<String,String> subMap = RefTableTools.ValParamSubnocMap;
			/** 账号信息：账号、账户类型、开户行名、余额、币种 */
			if (j == 0) {
				if (doc.getAccNo() != null) {
					total.setAccno1(doc.getAccNo());
				}
				if (doc.getSubjectNo() != null) {
					total.setSubjectNo1(subMap.get(doc.getSubjectNo()));
				}
				if (doc.getIdBank() != null) {
					total.setIdbank1(doc.getIdBank());
				}
				if (doc.getBankName() != null) {
					total.setBankname1(doc.getBankName());
				}
				if (doc.getCredit() != null) {
					total.setCredit1(UtilBase.formatString(doc.getCredit()));
				}
				if (doc.getCurrency() != null) {
					total.setCurrtypeCN1(doc.getCurrency());
				}
			}
			if (j == 1) {
				if (doc.getAccNo() != null) {
					total.setAccno2(doc.getAccNo());
				}
				if (doc.getSubjectNo() != null) {
					total.setSubjectNo2(subMap.get(doc.getSubjectNo()));
				}
				if (doc.getIdBank() != null) {
					total.setIdbank2(doc.getIdBank());
				}
				if (doc.getBankName() != null) {
					total.setBankname2(doc.getBankName());
				}
				if (doc.getCredit() != null) {
					total.setCredit2(UtilBase.formatString(doc.getCredit()));
				}
				if (doc.getCurrency() != null) {
					total.setCurrtypeCN2(doc.getCurrency());
				}
			}
			if (j == 2) {
				if (doc.getAccNo() != null) {
					total.setAccno3(doc.getAccNo());
				}
				if (doc.getSubjectNo() != null) {
					total.setSubjectNo3(subMap.get(doc.getSubjectNo()));
				}
				if (doc.getIdBank() != null) {
					total.setIdbank3(doc.getIdBank());
				}
				if (doc.getBankName() != null) {
					total.setBankname3(doc.getBankName());
				}
				if (doc.getCredit() != null) {
					total.setCredit3(UtilBase.formatString(doc.getCredit()));
				}
				if (doc.getCurrency() != null) {
					total.setCurrtypeCN3(doc.getCurrency());
				}
			}
			if (j == 3) {
				if (doc.getAccNo() != null) {
					total.setAccno4(doc.getAccNo());
				}
				if (doc.getSubjectNo() != null) {
					total.setSubjectNo4(subMap.get(doc.getSubjectNo()));
				}
				if (doc.getIdBank() != null) {
					total.setIdbank4(doc.getIdBank());
				}
				if (doc.getBankName() != null) {
					total.setBankname4(doc.getBankName());
				}
				if (doc.getCredit() != null) {
					total.setCredit4(UtilBase.formatString(doc.getCredit()));
				}
				if (doc.getCurrency() != null) {
					total.setCurrtypeCN4(doc.getCurrency());
				}
			}
			if (j == 4) {
				if (doc.getAccNo() != null) {
					total.setAccno5(doc.getAccNo());
				}
				if (doc.getSubjectNo() != null) {
					total.setSubjectNo5(subMap.get(doc.getSubjectNo()));
				}
				if (doc.getIdBank() != null) {
					total.setIdbank5(doc.getIdBank());
				}
				if (doc.getBankName() != null) {
					total.setBankname5(doc.getBankName());
				}
				if (doc.getCredit() != null) {
					total.setCredit5(UtilBase.formatString(doc.getCredit()));
				}
				if (doc.getCurrency() != null) {
					total.setCurrtypeCN5(doc.getCurrency());
				}
			}
		}
		return total;
	}

	// 明细数据以|^|分隔，明细字段以|*|分隔
	private String getPrintDetailByStr(BatchPrintBean data) {
		String returnStr = "";
		String strAccno = "";
		String strAccname = "";
		String strBiaotou = "序号|*|交易日期|*|借方发生额|*|贷方发生额|*|余额|*|摘要|^|";
		String strJieshu = "-----|*|-----------|*|-----------------|*|---打印结束---|*|-------------------|*|----------------|^|";
		String tempAccno = "";
		String temp = "";
		String marginAccno = "";
		String strDocdate = data.getDocdate().substring(0, 4) + "年"
				+ data.getDocdate().substring(5, 7) + "月"
				+ data.getDocdate().substring(8) + "日";
		int bishu = 0;
		// 先查询明细数据
		List<AccNoDetailData> listAccNoDetailData = new ArrayList<AccNoDetailData>();
		try {
			listAccNoDetailData = accnoDetailAdm.getAccnoDetailDataByVoucherno(
					data.getVoucherno(), data.getDocdate());
		} catch (XDocProcException e) {
			e.printStackTrace();
			logger.error("打印账单时生成明细数据发生错误", e);
		}
		if (listAccNoDetailData.size() > 0) {
			for (int i = 0; i < listAccNoDetailData.size(); i++) {

				if (i == 0) {
					tempAccno = listAccNoDetailData.get(i).getAccNo();
				}
				// 查询结果已经按账号排序，如果下一笔明细的账号不等于前一笔明细的账号，则需要打印表头
				if (!tempAccno.equals(listAccNoDetailData.get(i).getAccNo())) {
					strAccname = "single|*|" + data.getAccname() + "|^|";
					strAccno = "single|*|账号：" + tempAccno + "   币种:人民币   截止日期："
							+ strDocdate + "   共发生" + bishu + "笔明细|^|";
					temp = strAccname + strAccno + strBiaotou + temp
							+ strJieshu;
					returnStr = returnStr + temp;
					bishu = 1;
					temp = bishu + "|*|"
							+ listAccNoDetailData.get(i).getWorkDate() + "|*|"
							+ listAccNoDetailData.get(i).getStrtraceDBal()
							+ "|*|"
							+ listAccNoDetailData.get(i).getStrTraceCBal()
							+ "|*|" + listAccNoDetailData.get(i).getStrcredit()
							+ "|*|" + listAccNoDetailData.get(i).getAbs()
							+ "|^|";
					tempAccno = listAccNoDetailData.get(i).getAccNo();
				} else if (i == listAccNoDetailData.size() - 1) {
					bishu += 1;
					temp = temp + bishu + "|*|"
							+ listAccNoDetailData.get(i).getWorkDate() + "|*|"
							+ listAccNoDetailData.get(i).getStrtraceDBal()
							+ "|*|"
							+ listAccNoDetailData.get(i).getStrTraceCBal()
							+ "|*|" + listAccNoDetailData.get(i).getStrcredit()
							+ "|*|" + listAccNoDetailData.get(i).getAbs()
							+ "|^|";
					strAccname = "single|*|" + data.getAccname() + "|^|";
					strAccno = "single|*|账号：" + tempAccno + "   币种:人民币   截止日期："
							+ strDocdate + "   共发生" + bishu + "笔明细|^|";
					temp = strAccname + strAccno + strBiaotou + temp
							+ strJieshu;
					returnStr = returnStr + temp;
					temp = "";
				} else {
					bishu += 1;
					temp = temp + bishu + "|*|"
							+ listAccNoDetailData.get(i).getWorkDate() + "|*|"
							+ listAccNoDetailData.get(i).getStrtraceDBal()
							+ "|*|"
							+ listAccNoDetailData.get(i).getStrTraceCBal()
							+ "|*|" + listAccNoDetailData.get(i).getStrcredit()
							+ "|*|" + listAccNoDetailData.get(i).getAbs()
							+ "|^|";
				}
			}

		}
		// 再查询保证金和定期数据
		List<EbsMarginData> listMarginData = new ArrayList<EbsMarginData>();
		try {
			listMarginData = accnoDetailAdm.getMarginDataByVoucherno(data
					.getVoucherno());
		} catch (XDocProcException e) {
			e.printStackTrace();
			logger.error("打印账单时生成明细数据发生错误", e);
		}
		bishu = 0;
		strBiaotou = "序号|*|开户日期|*|开户机构|*|账号尾数|*|余额|*|期限|^|";
		if (listMarginData.size() > 0) {
			for (int i = 0; i < listMarginData.size(); i++) {
				// 活期保证金打印余额后还需打印明细
				if (listMarginData.get(i).getSubnoc().substring(0, 6)
						.equals("251101")) {
					if (marginAccno.equals("")) {
						marginAccno = "'" + listMarginData.get(i).getAccno()
								+ "'";
					} else {
						marginAccno = marginAccno + ",'"
								+ listMarginData.get(i).getAccno() + "'";
					}
				}

				if (i == 0) {
					tempAccno = listMarginData.get(i).getAcctype();
				}
				// 查询结果已经按账户类型排序，如果下一数据的账户类型不等于前一笔明细的，则需要打印表头
				if (!tempAccno.equals(listMarginData.get(i).getAcctype())) {
					strAccname = "single|*|" + data.getAccname() + "|^|";
					if (tempAccno.equals("2")) {
						strAccno = "single|*|定期汇总                币种:人民币   截止日期："
								+ strDocdate + "   共" + bishu + "笔明细|^|";
					} else {
						strAccno = "single|*|保证金汇总            币种:人民币   截止日期："
								+ strDocdate + "   共" + bishu + "笔明细|^|";
					}
					temp = strAccname + strAccno + strBiaotou + temp
							+ strJieshu;
					returnStr = returnStr + temp;
					tempAccno = listMarginData.get(i).getAcctype();
					bishu = 1;
					temp =  bishu + "|*|"
							+ listMarginData.get(i).getOpendate() + "|*|"
							+ listMarginData.get(i).getAccno().substring(0, 9)
							+ "|*|"
							+ listMarginData.get(i).getAccno().substring(9)
							+ "|*|" + listMarginData.get(i).getStrcredit()
							+ "|*|" + listMarginData.get(i).getTimelimit()
							+ "|^|";
				} else if (i == listMarginData.size() - 1) {
					bishu += 1;
					temp = temp + bishu + "|*|"
							+ listMarginData.get(i).getOpendate() + "|*|"
							+ listMarginData.get(i).getAccno().substring(0, 9)
							+ "|*|"
							+ listMarginData.get(i).getAccno().substring(9)
							+ "|*|" + listMarginData.get(i).getStrcredit()
							+ "|*|" + listMarginData.get(i).getTimelimit()
							+ "|^|";
					strAccname = "single|*|" + data.getAccname() + "|^|";
					if (tempAccno.equals("2")) {
						strAccno = "single|*|定期汇总                币种:人民币   截止日期："
								+ strDocdate + "   共" + bishu + "笔|^|";
					} else {
						strAccno = "single|*|保证金汇总           币种:人民币   截止日期："
								+ strDocdate + "   共" + bishu + "笔|^|";
					}
					temp = strAccname + strAccno + strBiaotou + temp
							+ strJieshu;
					returnStr = returnStr + temp;
					temp = "";
				} else {
					bishu += 1;
					temp = temp + bishu + "|*|"
							+ listMarginData.get(i).getOpendate() + "|*|"
							+ listMarginData.get(i).getAccno().substring(0, 9)
							+ "|*|"
							+ listMarginData.get(i).getAccno().substring(9)
							+ "|*|" + listMarginData.get(i).getStrcredit()
							+ "|*|" + listMarginData.get(i).getTimelimit()
							+ "|^|";
				}
			}

		}

		// 最后查询保证金活期明细数据
		if (marginAccno.equals("")) {
			return returnStr;
		}
		try {
			String queryWhre = "accNo in (" + marginAccno + ")";
			String entityName = "ebs_AccnoDetailData_"
					+ data.getVoucherno().substring(2, 4);
			listAccNoDetailData = new ArrayList<AccNoDetailData>();
			listAccNoDetailData = accnoDetailAdm.getAccnoDetailDataByWhere(
					queryWhre, entityName);
		} catch (XDocProcException e) {
			e.printStackTrace();
			logger.error("打印账单时生成明细数据发生错误", e);
		}
		bishu = 0;
		strBiaotou = "序号|*|交易日期|*|借方发生额|*|贷方发生额|*|余额|*|摘要|^|";
		if (listAccNoDetailData.size() > 0) {
			returnStr = returnStr + "single|*|以下为活期保证金明细：|^|";
			for (int i = 0; i < listAccNoDetailData.size(); i++) {
				if (i == 0) {
					tempAccno = listAccNoDetailData.get(i).getAccNo();
				}
				// 查询结果已经按账号排序，如果下一笔明细的账号不等于前一笔明细的账号，则需要打印表头
				if (!tempAccno.equals(listAccNoDetailData.get(i).getAccNo())) {
					strAccname = "single|*|" + data.getAccname() + "|^|";
					strAccno = "single|*|账号：" + tempAccno + "   币种:人民币   截止日期："
							+ strDocdate + "   共发生" + bishu + "笔明细|^|";
					temp = strAccname + strAccno + strBiaotou + temp
							+ strJieshu;
					returnStr = returnStr + temp;
					bishu = 1;
					temp = bishu + "|*|"
							+ listAccNoDetailData.get(i).getWorkDate() + "|*|"
							+ listAccNoDetailData.get(i).getStrtraceDBal()
							+ "|*|"
							+ listAccNoDetailData.get(i).getStrTraceCBal()
							+ "|*|" + listAccNoDetailData.get(i).getStrcredit()
							+ "|*|" + listAccNoDetailData.get(i).getAbs()
							+ "|^|";
					tempAccno = listAccNoDetailData.get(i).getAccNo();
				} else if (i == listAccNoDetailData.size() - 1) {
					bishu += 1;
					temp = temp + bishu + "|*|"
							+ listAccNoDetailData.get(i).getWorkDate() + "|*|"
							+ listAccNoDetailData.get(i).getStrtraceDBal()
							+ "|*|"
							+ listAccNoDetailData.get(i).getStrTraceCBal()
							+ "|*|" + listAccNoDetailData.get(i).getStrcredit()
							+ "|*|" + listAccNoDetailData.get(i).getAbs()
							+ "|^|";
					strAccname = "single|*|" + data.getAccname() + "|^|";
					strAccno = "single|*|账号：" + tempAccno + "   币种:人民币   截止日期："
							+ strDocdate + "   共发生" + bishu + "笔明细|^|";
					temp = strAccname + strAccno + strBiaotou + temp
							+ strJieshu;
					returnStr = returnStr + temp;
					temp = "";
				} else {
					bishu += 1;
					temp = temp + bishu + "|*|"
							+ listAccNoDetailData.get(i).getWorkDate() + "|*|"
							+ listAccNoDetailData.get(i).getStrtraceDBal()
							+ "|*|"
							+ listAccNoDetailData.get(i).getStrTraceCBal()
							+ "|*|" + listAccNoDetailData.get(i).getStrcredit()
							+ "|*|" + listAccNoDetailData.get(i).getAbs()
							+ "|^|";
				}
			}

		}
		return returnStr;
	}

	// 字段间以|@|分隔，换行以|#|分隔，字段顺序查看BatchPrint.xml-><VoucherCfg>/<DataDefine>/<Fields>节点字段顺序
	private String getPrintDataByStr(BatchPrintBean data) {
		String returnStr;
		returnStr = data.getDocdate();
		returnStr = returnStr + "|@|" + data.getVoucherno();
		returnStr = returnStr + "|@|" + data.getAccname();
		returnStr = returnStr + "|@|" + data.getZip();
		returnStr = returnStr + "|@|" + data.getAddress();
		returnStr = returnStr + "|@|" + data.getPhone();
		returnStr = returnStr + "|@|" + data.getLinkman();
		returnStr = returnStr + "|@|" + data.getCenterphone();
		returnStr = returnStr + "|@|" + data.getCenteraddress();
		returnStr = returnStr + "|@|" + data.getSendAddress();
		returnStr = returnStr + "|@|" + data.getAccno1();
		returnStr = returnStr + "|@|" + data.getCredit1();
		returnStr = returnStr + "|@|" + data.getCurrtypeCN1();
		returnStr = returnStr + "|@|" + data.getSubjectNo1();
		returnStr = returnStr + "|@|" + data.getBankname1();
		returnStr = returnStr + "|@|" + data.getAccno2();
		returnStr = returnStr + "|@|" + data.getCredit2();
		returnStr = returnStr + "|@|" + data.getCurrtypeCN2();
		returnStr = returnStr + "|@|" + data.getSubjectNo2();
		returnStr = returnStr + "|@|" + data.getBankname2();
		returnStr = returnStr + "|@|" + data.getAccno3();
		returnStr = returnStr + "|@|" + data.getCredit3();
		returnStr = returnStr + "|@|" + data.getCurrtypeCN3();
		returnStr = returnStr + "|@|" + data.getSubjectNo3();
		returnStr = returnStr + "|@|" + data.getBankname3();
		returnStr = returnStr + "|@|" + data.getAccno4();
		returnStr = returnStr + "|@|" + data.getCredit4();
		returnStr = returnStr + "|@|" + data.getCurrtypeCN4();
		returnStr = returnStr + "|@|" + data.getSubjectNo4();
		returnStr = returnStr + "|@|" + data.getBankname4();
		returnStr = returnStr + "|@|" + data.getAccno5();
		returnStr = returnStr + "|@|" + data.getCredit5();
		returnStr = returnStr + "|@|" + data.getCurrtypeCN5();
		returnStr = returnStr + "|@|" + data.getSubjectNo5();
		returnStr = returnStr + "|@|" + data.getBankname5();
		return returnStr;
	}

	public boolean judgeOrg(AccnoMainDataQueryParam accnoMainDataQueryParam)
			throws XDocProcException {
		// 判断跨机构登记
		@SuppressWarnings("unchecked")
		List<String> list = (List<String>) accnoMainDataAdm
				.findBySql("select IDBANK from EBS_MAINDATA where accno='"
						+ accnoMainDataQueryParam.getAccNo()
						+ "' and datadate='"
						+ accnoMainDataQueryParam.getDocDate().replace("-", "")
						+ "'");
		boolean isAllowCreate = false;
		if (list != null && list.size() > 0) {
			HttpServletRequest request = ServletActionContext.getRequest();
			XPeopleInfo people = (XPeopleInfo) request.getSession()
					.getAttribute(AMSecurityDefine.XPEOPLEINFO);
			BankParam loginParam = tools.getBankParam(people.getOrgNo());
			String idbank = list.get(0);
			BankParam bp = tools.getBankParam(loginParam.getIdBank());
			Short st = bp.getLevel();

			BankParam maindatabank = tools.getBankParam(idbank);
			if (st == 2) {
				if (maindatabank.getIdCenter().equals(loginParam.getIdCenter())) {
					isAllowCreate = true;
				}
			} else if (st == 3) {
				if (maindatabank.getIdBranch().equals(loginParam.getIdBranch())) {
					isAllowCreate = true;
				}
			} else if (st == 4) {
				if (idbank.equals(loginParam.getIdBank())) {
					isAllowCreate = true;
				}
			}
			// 如果当前登录人员是总行FR0001，就可以出
			if (st == 1) {
				isAllowCreate = true;
			}
		}
		return isAllowCreate;
	}

	@Override
	public List<CheckMainData> queryCheckMainDataInfo(
			AccnoMainDataQueryParam accnoMainDataQueryParam)
			throws XDocProcException {
		
		Map<String, String> mapdata = new HashMap<String, String>();
		Map<String, String> accQueryMap = new HashMap<String, String>();
		String docdate = accnoMainDataQueryParam.getDocDate();// 对账日期
		String voucherno = accnoMainDataQueryParam.getVoucherNo();// 账单编号

		String proveflag = accnoMainDataQueryParam.getProveFlag();
		String docstateflag = accnoMainDataQueryParam.getDocstateFlag();

		String idCenter = accnoMainDataQueryParam.getIdCenter();
	
		String sendMode = accnoMainDataQueryParam.getSendMode();
		String custId = accnoMainDataQueryParam.getCustId();
		//String accNo = accnoMainDataQueryParam.getAccNo();//checkmaindata没有accno字段
		
		//2013 10 15添加账户名称 模糊查询
		String accName = accnoMainDataQueryParam.getAccName();

		if (docdate != null || !"".equals(docdate)) {
			mapdata.put("docDate", docdate.replace("-", ""));
		}
		if (sendMode != null || !"".equals(sendMode)) {
			accQueryMap.put("sendMode", sendMode);
		}
		mapdata.put("voucherNo", voucherno);
		//accQueryMap.put("accNo", accNo);
		mapdata.put("proveFlag", proveflag);
		mapdata.put("docState", docstateflag);
		mapdata.put("idCenter", idCenter);
	
		mapdata.put("custId", custId);
		
		//2013 10 15添加账户名称 模糊查询
		mapdata.put("accName", accName);

		if (accnoMainDataQueryParam.getIdBank() != null
				&& accnoMainDataQueryParam.getIdBank().length() > 0) {
			mapdata.put("idBank", accnoMainDataQueryParam.getIdBank());
		}
		List<CheckMainData> resultList = new ArrayList<CheckMainData>();

		// 按账号查账单信息
		List<Object[]> list = checkMainDataAdm.getCheckMaindata(mapdata,
				accnoMainDataQueryParam, accQueryMap);
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				CheckMainData cmd = new CheckMainData();
				if (list.get(i)[0] == null) {
					cmd.setVoucherNo("");
				} else {
					cmd.setVoucherNo(String.valueOf(list.get(i)[0] + ""));
				}
				if (list.get(i)[1] == null) {
					cmd.setIdCenter("");
				} else {
					cmd.setIdCenter(String.valueOf(list.get(i)[1] + ""));
				}
				if (list.get(i)[2] == null) {
					cmd.setBankName("");
				} else {
					cmd.setBankName(String.valueOf(list.get(i)[2] + ""));
				}
				if (list.get(i)[3] == null) {
					cmd.setAccName("");
				} else {
					cmd.setAccName(String.valueOf(list.get(i)[3] + ""));
				}
				if (list.get(i)[4] == null) {
					cmd.setCustId("");
				} else {
					cmd.setCustId(String.valueOf(list.get(i)[4] + ""));
				}
				if (list.get(i)[5] == null) {
					cmd.setLinkMan("");
				} else {
					cmd.setLinkMan(String.valueOf(list.get(i)[5] + ""));
				}
				if (list.get(i)[6] == null) {
					cmd.setAddress("");
				} else {
					cmd.setAddress(String.valueOf(list.get(i)[6] + ""));
				}
				if (list.get(i)[7] == null) {
					cmd.setPhone("");
				} else {
					cmd.setPhone(String.valueOf(list.get(i)[7] + ""));
				}
				cmd.setDocState(String.valueOf(list.get(i)[8] + ""));
				if (list.get(i)[9] != null) {
					cmd.setPrintTimes(new Integer(String.valueOf(list.get(i)[9]
							+ "")).intValue());
				} else {
					cmd.setPrintTimes(0);
				}
				cmd.setDocDate(String.valueOf(list.get(i)[10] + ""));
				if (list.get(i)[11] == null) {
					cmd.setZip("");
				} else {
					cmd.setZip(String.valueOf(list.get(i)[11] + ""));
				}
				if (list.get(i)[13] == null) {
					cmd.setSendAddress("");
				} else {
					cmd.setSendAddress(String.valueOf(list.get(i)[13] + ""));
				}
				resultList.add(cmd);
			}
		}
		return resultList;
	}

	/**
	 * 得到所有对账单的 对账单编号
	 */
	public String getAllVoucherNo(AccnoMainDataQueryParam Param)throws XDocProcException{
		List<Object[]> queryList = new ArrayList<Object[]>();
		queryList = checkMainDataAdm.getAllVoucherNo(getQueryMap(Param));
		StringBuffer str = new StringBuffer();
		Iterator<Object[]> it =queryList.iterator();
		while(it.hasNext()){
			Object[] ob= (Object[]) it.next();
			str.append(ob[0]);
			str.append("*");
		}
		return str.toString();
	}
	
	/**
	 * g 根据 AccnoMainDataQueryParam 得到查询的map
	 * @return
	 */
	public Map<String ,String>getQueryMap(AccnoMainDataQueryParam accnoMainDataQueryParam){
		
		Map<String, String> mapdata = new HashMap<String, String>();	
		String docdate = accnoMainDataQueryParam.getDocDate();// 对账日期
		String sendMode = accnoMainDataQueryParam.getSendMode();
		if (docdate != null || !"".equals(docdate)) {
			mapdata.put("docDate", docdate.replace("-", ""));
		}
		if (sendMode != null || !"".equals(sendMode)) {
			mapdata.put("sendMode", sendMode);
		}
		mapdata.put("voucherNo",  accnoMainDataQueryParam.getVoucherNo());
		mapdata.put("accNo",accnoMainDataQueryParam.getAccNo());
		mapdata.put("proveFlag", accnoMainDataQueryParam.getProveFlag());
		mapdata.put("docState", accnoMainDataQueryParam.getDocstateFlag());
		mapdata.put("idCenter",accnoMainDataQueryParam.getIdCenter());
		mapdata.put("custId",accnoMainDataQueryParam.getCustId());

		if (accnoMainDataQueryParam.getIdBank() != null
				&& accnoMainDataQueryParam.getIdBank().length() > 0) {
			mapdata.put("idBank", accnoMainDataQueryParam.getIdBank());
		}
		return mapdata;
	}
	
	/**
	 * 把  List<Object> 转成 List<accDetailsBean>
	 * @return
	 */
	public List<Map<String,String>> getChangeType(List<Object[]> list){
		if(list==null){
			return null;
		}else{
			List<Map<String,String>>detailList =new ArrayList<Map<String,String>>();
			Iterator<Object[]> it =list.iterator();
			while(it.hasNext()){
				Object[] ob= (Object[]) it.next();
				Map<String,String> dataMap = new HashMap<String,String>();
				if(ob[0]==null){
					dataMap.put("voucherNo","");
				}else{
					dataMap.put("voucherNo",(String)ob[0]);
				}
				if(ob[1]==null){
					dataMap.put("accno","");
				}else{
					dataMap.put("accno",(String)ob[1]);
				}
				if(ob[2]==null){
					dataMap.put("workdate","");
				}else{
					dataMap.put("workdate",(String)ob[2]);
				}
				if(ob[3]==null){
					dataMap.put("vouno","");
				}else{
					dataMap.put("vouno",(String)ob[3]);
				}
				if(ob[4]==null){
					dataMap.put("abs","");
				}else{
					dataMap.put("abs",(String)ob[4]);
				}
				if(ob[5]==null){
					dataMap.put("credit","");
				}else{
					dataMap.put("credit",UtilBase.formatString(Double.valueOf(String.valueOf(ob[5]))));
				}
				if(ob[6]==null){
					dataMap.put("jf","");
				}else{
					dataMap.put("jf",UtilBase.formatString(Double.valueOf(String.valueOf(ob[6]))));
				}
				if(ob[7]==null){
					dataMap.put("df","");
				}else{
					dataMap.put("df",UtilBase.formatString(Double.valueOf(String.valueOf(ob[7]))));
				}
				
				detailList.add(dataMap);
			}
			return detailList;
		}
	}
	
	@Override
	public List<Object[]> getDeatilCount(Map<String, String> mapData,
			String month) {
		List<Object[]> detailList = new ArrayList<Object[]>();
		try {
			//得到全部明细
			detailList = checkMainDataAdm.getDeatil(mapData, month,false,0,0);
		} catch (XDocProcException e) {
			e.printStackTrace();
			logger.error("查询对公明细出错", e);
		}

		return detailList;
	}
	
	public IPublicTools getTools() {
		return tools;
	}

	public void setTools(IPublicTools tools) {
		this.tools = tools;
	}

	public ICheckMainDataAdm getCheckMainDataAdm() {
		return checkMainDataAdm;
	}

	public void setCheckMainDataAdm(ICheckMainDataAdm checkMainDataAdm) {
		this.checkMainDataAdm = checkMainDataAdm;
	}

	public IAccnoMainDataAdm getAccnoMainDataAdm() {
		return accnoMainDataAdm;
	}

	public void setAccnoMainDataAdm(IAccnoMainDataAdm accnoMainDataAdm) {
		this.accnoMainDataAdm = accnoMainDataAdm;
	}

	public IAccnoDetailAdm getAccnoDetailAdm() {
		return accnoDetailAdm;
	}

	public void setAccnoDetailAdm(IAccnoDetailAdm accnoDetailAdm) {
		this.accnoDetailAdm = accnoDetailAdm;
	}

	public IParamBank getParamBank() {
		return paramBank;
	}

	public void setParamBank(IParamBank paramBank) {
		this.paramBank = paramBank;
	}

	public RefTableTools getRefTableTools() {
		return refTableTools;
	}

	public void setRefTableTools(RefTableTools refTableTools) {
		this.refTableTools = refTableTools;
	}

}
