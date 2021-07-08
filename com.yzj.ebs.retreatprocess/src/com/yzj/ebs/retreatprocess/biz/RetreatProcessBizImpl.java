package com.yzj.ebs.retreatprocess.biz;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.yzj.ebs.common.ICheckMainDataAdm;
import com.yzj.ebs.common.ICheckMainDataLogAdm;
import com.yzj.ebs.common.IPublicTools;
import com.yzj.ebs.common.OperLogModuleDefine;
import com.yzj.ebs.common.RefTableTools;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.database.CheckMainData;
import com.yzj.ebs.database.CheckMainDataLog;
import com.yzj.ebs.retreatprocess.queryparam.RetreatQueryParam;
import com.yzj.wf.core.model.po.wrapper.XPeopleInfo;

/**
 * 创建于:2013-04-07<br>
 * 版权所有(C) 2013 深圳市银之杰科技股份有限公司<br>
 * 退信处理  业务实现
 * 
 * @author 单伟龙
 * @version 1.0.0
 */
public class RetreatProcessBizImpl implements IRetreatProcessBiz{
	
	private ICheckMainDataAdm checkMainDataAdm;
	private ICheckMainDataLogAdm checkMainDataLogAdm;
	private IPublicTools tools;
	
	private static SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	private static final SimpleDateFormat matter1 = new SimpleDateFormat("yyyyMMdd");
	
	// 以下是处理意见所需要的字段
	private String urgeResult="";
	private String userId="";
	
	private static String URGEFLAG_NOTPROCESS = "0"; // 退信未处理
	private static String URGEFLAG_PROCESSED = "1"; // 退信已处理
  //private static String URGESTATE_NOTINPUT = "0"; // 退信未登记
  //private static String URGESTATE_INPUTED = "1"; // 退信已登记

	private Map<String, String> refUrgeTypeMap = new TreeMap<String, String>();	//处理类型
	private Map<String, String> refUrgeFlagMap = new TreeMap<String, String>(); // 处理标志 0:未处理 1:已处理
	private Map<String,String> refUrgeResultMap = new TreeMap<String,String>();	//处理意见  0：再次发出 1：放弃放出
	private TreeMap<String, String> refSendMode = new TreeMap<String, String>();// 发送方式
	/**
	 * @throws XDocProcException 
	 * 
	 */
	@Override
	public List<CheckMainData> getCheckMainData(Map<String, String> queryMap,
			RetreatQueryParam retreatQueryParam) throws XDocProcException {
		// TODO Auto-generated method stub
		
		return checkMainDataAdm.getCheckMainData(queryMap,retreatQueryParam);
	}
	/**
	 * @throws XDocProcException 
	 * 
	 */
	@Override
	public List<CheckMainData> getAllCheckMainData(Map<String, String> queryMap) throws XDocProcException {
		// TODO Auto-generated method stub
		
		return checkMainDataAdm.getAllCheckMainData(queryMap);
	}
	/**
	 * 退信处理单个实现
	 */
	@Override
	public void modifyOne(CheckMainData checkMainData,String index) throws Exception {
		// TODO Auto-generated method stub
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		XPeopleInfo people = tools.getCurrLoginPeople();
		userId = people.getPeopleCode();
		response.setCharacterEncoding("UTF-8");
		CheckMainDataLog log=new CheckMainDataLog();
		log.setOpMode(OperLogModuleDefine.reTreatProcess);
		log.setChnOpMode(OperLogModuleDefine.getModuleName(OperLogModuleDefine.reTreatProcess));
		log.setIdBank(checkMainData.getIdBank());
		log.setIdBranch(checkMainData.getIdBranch());
		log.setIdCenter(checkMainData.getIdCenter());
		log.setOpCode(people.getPeopleCode());
		log.setVoucherNo(checkMainData.getVoucherNo());//更改了，退信批量处理的账单编号未写入表中的问题
		log.setOpDate(sdf.format(new Date()));
		String desc = this.updateChenkMianData(checkMainData);
		log.setOpDesc(desc);
		try{
		checkMainDataAdm.update(checkMainData);
		checkMainDataLogAdm.createCheckMainDataLog(log);
	} catch (XDocProcException e) {
		throw new Exception("更新失败:"+e.getMessage());
	}
	}
	
	/**
	 * 处理退信
	 * 
	 * @param checkMainData
	 */
	private String updateChenkMianData(CheckMainData checkMainData) {
		String desc="退信处理成功!";
		desc+="描述:"+checkMainData.getUrgeDesc();
		desc+=",处理意见:"+checkMainData.getUrgeResult()+"("+refUrgeResultMap.get(checkMainData.getUrgeResult())+")";
		/*如果处理意见为再次发出并且对账单未收回，则更新docState，置为5：再次发出*/
		if(urgeResult.equals("0")&&(!checkMainData.getDocState() .equals("3"))){	
			desc+=",账单状态:"+checkMainData.getDocState()+"-->5";
			checkMainData.setDocState("5");
		}
		/*如果处理意见为放弃发出并且对账单未收回，则更新docState，置为6：放弃发出*/
		if(urgeResult.equals("1")&&(!checkMainData.getDocState() .equals("3"))){
			desc+=",账单状态:"+checkMainData.getDocState()+"-->6";
			checkMainData.setDocState("6");
		}
		checkMainData.setUrgeFlag(URGEFLAG_PROCESSED);
		desc+=",处理标识:"+URGEFLAG_PROCESSED+"(退信已处理)";
		String inputTime = matter1.format(new Date()).toString();
		checkMainData.setUrgeDate1(inputTime);
		checkMainData.setUrgePeople1(userId);
		return desc;
	}

	

	public Map<String, String> getRefUrgeTypeMap() {
		if(null==refUrgeTypeMap || refUrgeTypeMap.size()==0){
			refUrgeTypeMap = RefTableTools.ValRefUrgeTypeMap;
		}
		return refUrgeTypeMap;
	}

	public void setRefUrgeTypeMap(Map<String, String> refUrgeTypeMap) {
		this.refUrgeTypeMap = refUrgeTypeMap;
	}

	public Map<String, String> getRefUrgeFlagMap() {
		refUrgeFlagMap.put(URGEFLAG_NOTPROCESS, "未处理");
		refUrgeFlagMap.put(URGEFLAG_PROCESSED, "已处理");
		return refUrgeFlagMap;
	}

	public void setRefUrgeFlagMap(Map<String, String> refUrgeFlagMap) {
		this.refUrgeFlagMap = refUrgeFlagMap;
	}
	
	public Map<String, String> getRefUrgeResultMap() {
		refUrgeResultMap = new TreeMap<String,String>();
		refUrgeResultMap.put("0", "再次发出");
		refUrgeResultMap.put("1", "放弃发出");
		return refUrgeResultMap;
	}

	public void setRefUrgeResultMap(Map<String, String> refUrgeResultMap) {
		this.refUrgeResultMap = refUrgeResultMap;
	}

	public TreeMap<String, String> getRefSendMode() {
		if (null == refSendMode || refSendMode.size() == 0) {
			refSendMode = RefTableTools.ValRefSendModeMap;
		}
		return refSendMode;
	}

	public void setRefSendMode(TreeMap<String, String> refSendMode) {
		this.refSendMode = refSendMode;
	}
	public ICheckMainDataAdm getCheckMainDataAdm() {
		return checkMainDataAdm;
	}
	public void setCheckMainDataAdm(ICheckMainDataAdm checkMainDataAdm) {
		this.checkMainDataAdm = checkMainDataAdm;
	}
	public ICheckMainDataLogAdm getCheckMainDataLogAdm() {
		return checkMainDataLogAdm;
	}
	public void setCheckMainDataLogAdm(ICheckMainDataLogAdm checkMainDataLogAdm) {
		this.checkMainDataLogAdm = checkMainDataLogAdm;
	}
	public IPublicTools getTools() {
		return tools;
	}
	public void setTools(IPublicTools tools) {
		this.tools = tools;
	}
	public static SimpleDateFormat getSdf() {
		return sdf;
	}
	public static void setSdf(SimpleDateFormat sdf) {
		RetreatProcessBizImpl.sdf = sdf;
	}
	public static SimpleDateFormat getMatter1() {
		return matter1;
	}

}
