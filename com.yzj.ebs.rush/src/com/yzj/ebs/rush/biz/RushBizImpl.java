package com.yzj.ebs.rush.biz;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.yzj.ebs.common.ICheckMainDataAdm;
import com.yzj.ebs.common.ICheckMainDataLogAdm;
import com.yzj.ebs.common.IPublicTools;
import com.yzj.ebs.common.OperLogModuleDefine;
import com.yzj.ebs.common.RefTableTools;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.database.CheckMainData;
import com.yzj.ebs.database.CheckMainDataLog;
import com.yzj.ebs.rush.queryparam.RushQueryParam;
import com.yzj.wf.core.model.po.wrapper.XPeopleInfo;

/**
 * 创建于:2013-04-07<br>
 * 版权所有(C) 2013 深圳市银之杰科技股份有限公司<br>
 * 催收处理 业务实现
 * 
 * @author 单伟龙
 * @version 1.0.0
 */
public class RushBizImpl implements IRushBiz {

	private ICheckMainDataAdm checkMainDataAdm;
	private ICheckMainDataLogAdm checkMainDataLogAdm;
	private IPublicTools tools;

	private static SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	// 以下是维护催收信息时的输入项

	private static String RUSHFLAG_NOTPROCESS = "0";//未处理
	private static String RUSHFLAG_PROCESSED = "1";//已处理
//	private static String RUSHSTATE_NEED = "1";//需催收
	
	private Map<String, String> refRushFlagMap = new TreeMap<String, String>();		//催收标志  0：未处理  1：已处理
	private Map<String,String> refRushMethodMap = new TreeMap<String,String>();	//催收方式 0：电话 1：邮件 2：面对面
	private Map<String,String> refRushResultMap = new TreeMap<String,String>();	//处理意见  0：等待回收 1：再次发出 2：放弃放出
	private TreeMap<String, String> refSendMode = new TreeMap<String, String>();// 发送方式
	private TreeMap<String,String> refCustomResponseMap=new TreeMap<String, String>();//客户回应
	private TreeMap<String,String> refDocStateMap=new TreeMap<String, String>();//账单状态
	/**
	 * 获取分页数据
	 */
	public List<CheckMainData> getCheckMainData(Map<String, String> queryMap,
			RushQueryParam rushQueryParam) throws XDocProcException {
		// TODO Auto-generated method stub
		return checkMainDataAdm.getCheckMainData(queryMap, rushQueryParam);
	}

	/**
	 * 获取全量数据
	 */
	public List<CheckMainData> getAllCheckMainData(Map<String, String> queryMap)
			throws XDocProcException {
		// TODO Auto-generated method stub
		return checkMainDataAdm.getAllCheckMainData(queryMap);
	}

	/**
	 * 单个修改 维护
	 */
	@Override
	public String modifyOne(CheckMainData checkMainData, String index) throws Exception {
		// TODO Auto-generated method stub

		XPeopleInfo people = tools.getCurrLoginPeople();

		CheckMainDataLog log = new CheckMainDataLog();
		log.setOpMode(OperLogModuleDefine.rushProcess);
		log.setChnOpMode(OperLogModuleDefine
				.getModuleName(OperLogModuleDefine.rushProcess));
		log.setIdBank(checkMainData.getIdBank());
		log.setIdBranch(checkMainData.getIdBranch());
		log.setIdCenter(checkMainData.getIdCenter());
		log.setOpCode(people.getPeopleCode());
		log.setVoucherNo(checkMainData.getVoucherNo());
		log.setOpDate(sdf.format(new Date()));
		String desc = this.updateCheckMainData(checkMainData);
		checkMainData.setRushDate(log.getOpDate());
		checkMainData.setRushOperCode(people.getPeopleCode());
		log.setOpDesc(desc);

		try {
			checkMainDataAdm.update(checkMainData); // 更新账单表
			checkMainDataLogAdm.createCheckMainDataLog(log); // 更新日志表
		} catch (XDocProcException e) {
			throw new Exception("更新失败:" + e.getMessage());
		}
		return null;
	}

	/**
	 * 根据页面的传来的一条账单数据  得到一条完整的更新数据
	 * 返回的desc作为  log日志的一个字段
	 * @param checkMainData
	 */
	private String updateCheckMainData(CheckMainData checkMainData) {
		String desc = "催收成功:";
		desc += "催收方式:" + refRushMethodMap.get(checkMainData.getRushMethod());
		/* 如果处理意见为再次发出并且对账单未收回，则更新docState，置为5：再次发出 */
		if (checkMainData.getRushResult().equals("1")
				&& (!checkMainData.getDocState().equals("3"))) {
			desc += ",账单状态:" + checkMainData.getDocState() + "-->5";
			checkMainData.setDocState("5");
		}
		/* 如果处理意见为放弃发出并且对账单未收回，则更新docState，置为6：放弃发出 */
		if (checkMainData.getRushResult().equals("2")
				&& (!checkMainData.getDocState().equals("3"))) {
			desc += ",账单状态:" + checkMainData.getDocState() + "-->6";
			checkMainData.setDocState("6");
		}
		desc += ",催收备注:" + checkMainData.getRushDesc();
		checkMainData.setRushFlag(RUSHFLAG_PROCESSED);
		desc += ",催收状态:" + RUSHFLAG_PROCESSED + "(已处理)";
		desc += ",客户回应:" + checkMainData.getCustomResponse() + "("
				+ refCustomResponseMap.get(checkMainData.getCustomResponse()) + ")";
		return desc;

	}

	public Map<String, String> getRefRushFlagMap() {
		refRushFlagMap = new TreeMap<String, String>();
		refRushFlagMap.put(RUSHFLAG_NOTPROCESS, "未处理");
		refRushFlagMap.put(RUSHFLAG_PROCESSED, "已处理");
		return refRushFlagMap;
	}

	public void setRefRushFlagMap(Map<String, String> refRushFlagMap) {
		this.refRushFlagMap = refRushFlagMap;
	}
	public Map<String, String> getRefRushMethodMap() {
		if (null == refRushMethodMap || refRushMethodMap.size() == 0) {
			refRushMethodMap = RefTableTools.ValRushMethodMap;
		}
		return refRushMethodMap;
	}

	public void setRefRushMethodMap(Map<String, String> refRushMethodMap) {
		this.refRushMethodMap = refRushMethodMap;
	}

	public Map<String, String> getRefRushResultMap() {
		if (null == refRushResultMap || refRushResultMap.size() == 0) {
			refRushResultMap = RefTableTools.ValRushResultMap;
		}
		return refRushResultMap;
	}

	public void setRefRushResultMap(Map<String, String> refRushResultMap) {
		this.refRushResultMap = refRushResultMap;
	}
	public TreeMap<String, String> getRefSendMode() {
		if (null == refSendMode || refSendMode.size() == 0) {
			refSendMode = RefTableTools.ValRefSendModeMap;
		}
		return refSendMode;
	}
	

	public TreeMap<String, String> getRefCustomResponseMap() {
		if(refCustomResponseMap==null||refCustomResponseMap.size()==0){
			refCustomResponseMap=RefTableTools.ValRefCustomResponseMap;
		}
		return refCustomResponseMap;
	}
	

	public TreeMap<String, String> getRefDocStateMap() {
		if(refDocStateMap==null||refDocStateMap.size()==0){
			refDocStateMap=RefTableTools.ValRefDocstateMap;
		}
		return refDocStateMap;
	}

	public void setRefDocStateMap(TreeMap<String, String> refDocStateMap) {
		this.refDocStateMap = refDocStateMap;
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
		RushBizImpl.sdf = sdf;
	}

	/**
	 * 
	 *创建于:2013-1-15<br>
	 *版权所有(C) 2013 深圳市银之杰科技股份有限公司<br>
	 *  临时checkMaindata对象，做界面返回（因为json在处理checkMainData对象的时候会报错，所以搞了个替身）
	 * @author chender
	 * @version 1.0
	 */
	public class TempCheckMainData implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 5384783571032051615L;
		private Long autoId;
		private String idBank;
		private String accName;
		private String voucherNo;
		private String rushMethod;
		private String rushResult;
		private String rushDesc;
		private String rushFlag;
		private String rushDate;
		private String rushOperCode;
		private String customResponse;
		
		
		public String getRushFlag() {
			return rushFlag;
		}
		public void setRushFlag(String rushFlag) {
			this.rushFlag = rushFlag;
		}
		public Long getAutoId() {
			return autoId;
		}
		public void setAutoId(Long autoId) {
			this.autoId = autoId;
		}
		public String getIdBank() {
			return idBank;
		}
		public void setIdBank(String idBank) {
			this.idBank = idBank;
		}
		public String getAccName() {
			return accName;
		}
		public void setAccName(String accName) {
			this.accName = accName;
		}
		public String getVoucherNo() {
			return voucherNo;
		}
		public void setVoucherNo(String voucherNo) {
			this.voucherNo = voucherNo;
		}
		public String getRushMethod() {
			return rushMethod;
		}
		public void setRushMethod(String rushMethod) {
			this.rushMethod = rushMethod;
		}
		public String getRushResult() {
			return rushResult;
		}
		public void setRushResult(String rushResult) {
			this.rushResult = rushResult;
		}
		public String getRushDesc() {
			return rushDesc;
		}
		public void setRushDesc(String rushDesc) {
			this.rushDesc = rushDesc;
		}
		public String getRushDate() {
			return rushDate;
		}
		public void setRushDate(String rushDate) {
			this.rushDate = rushDate;
		}
		public String getRushOperCode() {
			return rushOperCode;
		}
		public void setRushOperCode(String rushOperCode) {
			this.rushOperCode = rushOperCode;
		}
		public String getCustomResponse() {
			return customResponse;
		}
		public void setCustomResponse(String customResponse) {
			this.customResponse = customResponse;
		}
	}

}
