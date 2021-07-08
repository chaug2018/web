package com.yzj.ebs.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yzj.ebs.common.IAccnoMainDataAdm;
import com.yzj.ebs.common.ICheckMainDataAdm;
import com.yzj.ebs.common.IDocSetAdm;
import com.yzj.ebs.common.INotMatchTableAdm;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.database.AccNoMainData;
import com.yzj.ebs.database.CheckMainData;
import com.yzj.ebs.database.DocSet;
import com.yzj.ebs.database.NotMatchTable;
import com.yzj.ebs.task.common.IAppTaskAdm;
import com.yzj.wf.core.model.po.wrapper.XPeopleInfo;

/**
 * 本工具主要用于处理从网银FTP下载下来的对账结果和余额不符结果
 * 1、对账结果：
 * 		(1)核对结果相符：更新2个表中的3个字段；
 * 		(2)核对结果不符：创建任务到未达录入；
 * 2、余额不符结果：插入记录到ebs_notMatchTable表
 * @author heweilong
 * @date 2014-01-06
 *
 */
public class WebBankUtil {

	private IAppTaskAdm taskAdm;
	private INotMatchTableAdm notMatchTableAdm;
	private ICheckMainDataAdm checkMainDataAdm;
	private IAccnoMainDataAdm accNoMainDataAdm;
	private IDocSetAdm docSetAdm ;
	
	/**
	 * 处理传入的对账结果集和不符明细结果集
	 * 1、对账结果明细处理思路：
	 * 		(1)余额相符：修改checkMainData和AccNoMainData记录：
	 * 		(2)余额不符：创建DocSet，再根据DocSet创建工作流；
	 * 2、不符明细处理思路：
	 * 		(1)一个结果创建一条NotMatchTable记录
	 * @param voucherResultMapList ：对账结果集
	 * @param notMatchDetailMapList ：不符明细结果集
	 * @return
	 */
	public void proVoucherResulte(List< Map<String,String> > voucherResultMapList ,List<Map<String,String>> notMatchDetailMapList){
		
		//对账结果处理
		if(voucherResultMapList.size() != 0){
			for(int i=0;i<voucherResultMapList.size();i++){
				proVoucherResultMap(voucherResultMapList.get(i));
			}
		}
		//不符结果处理
		if(notMatchDetailMapList.size() != 0){
			for(int i=0;i<notMatchDetailMapList.size();i++){
				NotMatchTable noMatchTable;
				try {
					noMatchTable = preInsert(notMatchDetailMapList.get(i));
					insertNotMatchDetail(noMatchTable);
				} catch (XDocProcException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * 
	 * <key,value>:<voucherNo,123456>
	 * 根据传入的对账结果更新ebs_checkMainData表中对应的记录
	 * 1、核对相符处理：更新checkMaindata表和ebs_accnomaindata表中对应的记录	--->后期网银的对账单改造为一对一，即一个账号一张对账单
	 * 		（1）更新checkMaindata表的哪几个字段呢？ 对账单状态docstate=3(已完成)
	 * 		（2）更新accnoMaindata表的哪几个字段呢？最终余额状态finalCheckFlag、matchFlag未达标记、
	 * 2、核对不符处理：到未达处理
	 * @return
	 */
	private void proVoucherResultMap(Map<String,String> voucherResultMap){
		String result = voucherResultMap.get("RESULT1");
		String voucherNo = "";
		if(result.equals("1")){//相符，修改记录：checkMainData+AccNoMainDdata
			for(String key : voucherResultMap.keySet()){
				if(key.equals("VOUCHERNO")){
					voucherNo = voucherResultMap.get(key);
				}
			}
			//执行核对成功处理逻辑
			try {
				//更新CheckMainData表记录
				CheckMainData checkMainData = checkMainDataAdm.getOneByVoucherNo(voucherNo);
				checkMainData.setDocState("3");
				checkMainDataAdm.update(checkMainData);
				
				//更新AccNoMainData表记录
				List<AccNoMainData> accNoMainDataList = accNoMainDataAdm.getAccnoMainDataByVoucherNo(voucherNo);
				for(AccNoMainData accNoMainData : accNoMainDataList){
					accNoMainData.setFinalCheckFlag("3");//对账相符
					accNoMainData.setMatchFlag( new Integer(0));//金额一致
				}
				//批量更新
				accNoMainDataAdm.batchUpdate(accNoMainDataList);
			} catch (XDocProcException e) {
				e.printStackTrace();
			}
		}else{//不符，创建未达任务：docset+task
			try {
				for(String key : voucherResultMap.keySet()){
					if(key.equals("VOUCHERNO")){
						voucherNo = voucherResultMap.get(key);
					}
				}
				createTaskFromEBank(createDocSet(voucherNo));
			} catch (XDocProcException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 插入不符明细前的预处理：将传过来的Map转化为NotMatchTable对象
	 * @param notMatchDetailMap
	 * @return
	 */
	private NotMatchTable preInsert(Map<String,String> notMatchDetailMap)throws XDocProcException{
		NotMatchTable notMatchTable = new NotMatchTable();
		if(!notMatchDetailMap.get("ACCNO").equals("\\N")){//账号不为空
			notMatchTable.setAccNo(notMatchDetailMap.get("ACCNO"));//设置账号
//			AccNoMainData accNoMainData = accNoMainDataAdm.getAccnoMainDataByAccno(notMatchDetailMap.get("ACCNO"));
//			notMatchTable.setVoucherNo(accNoMainData.getVoucherNo());//根据账号查找到对应的对账单号
//			DocSet docset = docSetAdm.findEBankDocSetByVoucherNo(accNoMainData.getVoucherNo());
			notMatchTable.setVoucherNo(notMatchDetailMap.get("VOUCHERNO"));//设置对账单编号
			DocSet docset = docSetAdm.findEBankDocSetByVoucherNo(notMatchDetailMap.get("VOUCHERNO"));//根据对账单编号查找docset
			if(docset != null){//设置DocID，初始化未达录入界面时需要
				notMatchTable.setDocId(docset.getDocId());
			}
		}
		
		//填充DocID字段，因为从网银对账结果创建的未达录入任务初始化时要依据DocID来查找对应的notMatchTable结果集；
		
		notMatchTable.setTraceDate(notMatchDetailMap.get("TRACEDATE"));
		notMatchTable.setTraceNo(notMatchDetailMap.get("TRACENO"));
		notMatchTable.setInputDesc(notMatchDetailMap.get("MEMO"));
		//填充交易金额、未达方向
		if(! notMatchDetailMap.get("CREDIT1").equals("\\N")){//金额1不为空：银行已到账，单位尚未到账（借方）
			notMatchTable.setDirection("0");
			notMatchTable.setTraceCredit(new Double(Double.parseDouble(notMatchDetailMap.get("CREDIT1"))));
		}else if(! notMatchDetailMap.get("CREDIT2").equals("\\N")){//金额2不为空：银行已到账，单位尚未到账（贷方）
			notMatchTable.setDirection("1");
			notMatchTable.setTraceCredit(new Double(Double.parseDouble(notMatchDetailMap.get("CREDIT2"))));
		}else if(! notMatchDetailMap.get("CREDIT3").equals("\\N")){//金额3不为空：单位已到账，银行尚未到账（借方）
			notMatchTable.setDirection("2");
			notMatchTable.setTraceCredit(new Double(Double.parseDouble(notMatchDetailMap.get("CREDIT3"))));
		}else if(! notMatchDetailMap.get("CREDIT4").equals("\\N")){//金额4不为空：单位已到账，银行尚未到账（贷方）
			notMatchTable.setDirection("3");
			notMatchTable.setTraceCredit(new Double(Double.parseDouble(notMatchDetailMap.get("CREDIT4"))));
		}
		notMatchTable.setFinalCheckFlag(notMatchDetailMap.get("RESULT"));//最终核对结果：1表示人工调节余额相符；2表示人工调节余额不符。

		return notMatchTable;
	}
	
	
	/**
	 * 创建工作流记录：
	 * @param docSet : 待创建工作流的记录
	 * @return ：返回经过修改后的DocSet记录
	 */
	private Object createTaskFromEBank(DocSet docSet){
		XPeopleInfo people_temp=new XPeopleInfo();
		//不可为空，否则创建任务会报错：经测试，此柜员号与创建的任务范围无关，仅与该任务对应的账号所在机构有关。故统一用super用户来创建		
		people_temp.setSid("7B92AE0FC4B04DB48F1AFBDB22CD7188");//super
		people_temp.setPeopleCode("super");//super
		people_temp.setOrgNo("1000000000");//super
		try {
			docSet = (DocSet) taskAdm.createTaskFromEbank(docSet, people_temp, "EBill");
		} catch (XDocProcException e) {
			e.printStackTrace();
		}
		return docSet;
	} 
	/**
	 * 生成DocSet对象，供工作流使用
	 * @return ：docset对象
	 * @throws XDocProcException
	 */
	private DocSet createDocSet(String voucherNo) throws XDocProcException{
		DocSet docSet = new DocSet();
		
		Date date = new Date();
		//必须赋值机构信息，否则创建任务会失败
		List<AccNoMainData> accNoMainDataList = accNoMainDataAdm.getAccnoMainDataByVoucherNo(voucherNo);
		if(accNoMainDataList.size()!= 0){
			AccNoMainData accNoMainData = accNoMainDataList.get(0);
			docSet.setIdCenter(accNoMainData.getIdCenter());
			docSet.setIdBranch(accNoMainData.getIdBranch());
			docSet.setIdBank(accNoMainData.getIdBank());
		}
		docSet.setVoucherNo(voucherNo);//必须填充对账单编号，因为初始化未达录入界面时回去查找该值
		docSet.setCredit(0.0);					//金额
		docSet.setNeedNotMatch((short)1);     //1:创建任务至未达
		SimpleDateFormat sdfYMD = new SimpleDateFormat("yyyy-MM-dd");
		docSet.setWorkDate(sdfYMD.format(date));	//日期

		docSet.setDocTypeId(1);						//票据类型
		docSet.setOpCode100("ebill"); 				// 扫描柜员id号
		docSet.setIsFree(1);						//锁定标记
		docSet.setDocFlag(new Integer(3));			//回收流程状态：3为等待自动对账
		docSet.setCallTimes(0);						//调用次数
		docSet.setProveFlag(0);						//验印标记
		
		return docSet;
	}

	/**
	 * 调用NotMatchTableAdm服务插入余额不符记录
	 * @param notMatchTableList
	 */
	private void insertNotMatchDetail(NotMatchTable notMatchTable){
		try {
			notMatchTableAdm.saveOrUpdate(notMatchTable);
		} catch (XDocProcException e) {
			e.printStackTrace();
		}
	}
	
	public IAppTaskAdm getTaskAdm() {
		return taskAdm;
	}

	public void setTaskAdm(IAppTaskAdm taskAdm) {
		this.taskAdm = taskAdm;
	}

	public INotMatchTableAdm getNotMatchTableAdm() {
		return notMatchTableAdm;
	}

	public void setNotMatchTableAdm(INotMatchTableAdm notMatchTableAdm) {
		this.notMatchTableAdm = notMatchTableAdm;
	}

	public ICheckMainDataAdm getCheckMainDataAdm() {
		return checkMainDataAdm;
	}

	public void setCheckMainDataAdm(ICheckMainDataAdm checkMainDataAdm) {
		this.checkMainDataAdm = checkMainDataAdm;
	}

	public IAccnoMainDataAdm getAccNoMainDataAdm() {
		return accNoMainDataAdm;
	}

	public void setAccNoMainDataAdm(IAccnoMainDataAdm accNoMainDataAdm) {
		this.accNoMainDataAdm = accNoMainDataAdm;
	}
	
	public IDocSetAdm getDocSetAdm() {
		return docSetAdm;
	}
	public void setDocSetAdm(IDocSetAdm docSetAdm) {
		this.docSetAdm = docSetAdm;
	}
	
//	public static void main(String[] args){
//		List<Map<String,String>> testList = new ArrayList<Map<String,String>>(); 
//		System.out.println(testList.size());
//	}
	
	
	/**
	 * 初始方法，测试用
	 * 1、测试对账单为空的情况；
	 * 2、测试单笔对账单结果；
	 * 3、测试多笔对账单结果；
	 */
//	public void init(){
//		List<Map<String,String>> voucherResultMapList = new ArrayList<Map<String,String>>();
//		
//		Map<String, String> voucherResultMap = new HashMap<String,String>();
//		voucherResultMap.put("VOUCHERNO", "130988083066572");//对账单号
//		voucherResultMap.put("DOCDATE", "20130930");//对账日期
//		voucherResultMap.put("ACCNO1", "78003012010010045294");//账号
//		voucherResultMap.put("RESULT1", "2");
//		voucherResultMapList.add(voucherResultMap);
//		
//		List<Map<String,String>> notMatchDetailMapList = new ArrayList<Map<String,String>>();
//		Map<String,String> notMatchDetailMap = new HashMap<String,String>();
//		notMatchDetailMap.put("ACCNO", "78003012010010045294");
//		notMatchDetailMap.put("TRACEDATE", "20130913");
//		notMatchDetailMap.put("TRACENO", "18874747453");
//		notMatchDetailMap.put("MEMO", "");
//		notMatchDetailMap.put("CREDIT1", "8000");
//		notMatchDetailMap.put("RESULT", "1");
//		
//		notMatchDetailMapList.add(notMatchDetailMap);
//		proVoucherResulte(voucherResultMapList,notMatchDetailMapList);
//	}
}
