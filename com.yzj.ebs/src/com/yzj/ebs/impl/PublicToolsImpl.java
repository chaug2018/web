package com.yzj.ebs.impl;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzj.ebs.common.BankParam;
import com.yzj.ebs.common.CreditParam;
import com.yzj.ebs.common.IPublicTools;
import com.yzj.ebs.common.IdCenterParam;
import com.yzj.ebs.common.SimpleOrg;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.util.DataLogUtils;
import com.yzj.wf.am.security.common.AMSecurityDefine;
import com.yzj.wf.core.model.po.wrapper.XPeopleInfo;
import com.yzj.wf.pam.common.IParamManager;
import com.yzj.wf.pam.common.ParamException;
import com.yzj.wf.pam.db.BaseParam;
import com.yzj.wf.pam.query.ParamQuery;

/**
 * 创建于:2012-10-8<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 提供一些常用的方法
 * 
 * @author 陈林江
 * @version 1.0
 */
public class PublicToolsImpl implements IPublicTools {

	private DataLogUtils logger;
	private Map<String, CreditParam> creditParams = null;
	private Map<String, IdCenterParam> idCenters = null;
	private Map<String, BankParam> bankParams = null;
	private List<String> deleteReasons = null;
	private List<String> reInputReasons = null;
	private Map<String, String> uReasonMap = null;
	private static SimpleOrg root; // 机构树形结构
	private long root_time=0;
	private long idCenter_time=0;
	private long bankParam_time=0;
	private long creditParams_time=0;
	private long deleteReasons_time=0;
	private long reInputReasons_time=0;
	private long refreshTime=30000;

	private static final String SysbaseValue = "sysbaseValue";
	private static final String Param_Sysbase = "Param_Sysbase";
	private static final String SysbaseID = "sysbaseID";
	private static final String WorkDate = "WORKDATE";
	private static final String description = "description";

	private static final String Param_Credit = "Param_Credit";
	private static final String Param_DeleteReason = "Param_DeleteReason";
	private static final String Param_UReason = "Param_UReason";
	private static final String Param_ReInputReason = "Param_ReInputReason";
	private static final String CreditID = "creditID";
	private static final String CreditValue = "creditValue";
	private static final String IdCenter = "idCenter";
	private static final String Param_IdCenter = "Param_IdCenter";
	private static final String Param_Bank = "Param_Bank";

	private static final String CREDIT_MI_INPT = "CREDIT_MI_INPT";
	private static final String CREDIT_MI_AUTH = "CREDIT_MI_AUTH";
	private static final String CREDIT_MP_FST = "CREDIT_MP_FST";
	private static final String CREDIT_MP_SND = "CREDIT_MP_SND";
	private static final String CREDIT_MP_AUTH = "CREDIT_MP_AUTH";


	private IParamManager paramManager;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public String getCurWorkDate() throws XDocProcException {
		try {
			ParamQuery pq = new ParamQuery();
			pq.addEqPropery(BaseParam.getFiledName("sysbaseID"), WorkDate);
			List<BaseParam> bps = paramManager.getParamByCondition(Param_Sysbase, pq);
			if (bps != null && bps.size() != 0) {
				return bps.get(0).getExtField(SysbaseValue).toString();
			} else {
				throw new XDocProcException("参数表中未配置" + WorkDate + "字段");
			}
		} catch (Exception e) {
			logger.error("查询工作日期失败", e,false);
			throw new XDocProcException("查询工作日期失败:" + e.getMessage());
		}

	}

	/*
	 * 根据sysbasid从param_sysbase表中获取参数
	 */
	public String getSysbaseParam(String sysbaseID) throws XDocProcException {
		try {
			ParamQuery pq = new ParamQuery();
			String pn=BaseParam.getFiledName("sysbaseID");
			pq.addEqPropery(pn, sysbaseID);
			List<BaseParam> bps = paramManager.getParamByCondition(Param_Sysbase, pq);
			if (bps != null && bps.size() != 0) {
				return bps.get(0).getExtField("sysbaseValue").toString();
			} else {
				throw new XDocProcException("参数表中未配置" + sysbaseID + "字段");
			}
		} catch (Exception e) {
			logger.error("查询参数表失败", e,false);
			throw new XDocProcException("查询参数表失败:" + e.getMessage());
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public synchronized CreditParam getCreditParam(String id)
			throws XDocProcException {
		CreditParam result = null;
		List<BaseParam> bps = null;
		if (creditParams == null || creditParams.size() == 0||System.currentTimeMillis()-this.creditParams_time>refreshTime) {
			creditParams_time=System.currentTimeMillis();
			creditParams = new HashMap<String, CreditParam>();
			try {
				bps = paramManager.getAllParamsByGroup(Param_Credit);
			} catch (ParamException e) {
				throw new XDocProcException("获取额度信息参数出现错误:" + e.getMessage());
			}
			this.dealCreditParams(bps);
		}
		result = creditParams.get(id);
		if (result == null) {
			throw new XDocProcException("未获取到id为" + id + "的对账中心对应的额度信息，请检查"
					+ Param_Credit + "参数表");
		}
		return result;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public IdCenterParam getParamIdcenter(String idCenter)
			throws XDocProcException {
		List<BaseParam> bps = null;
		if (idCenters == null || idCenters.size() == 0||System.currentTimeMillis()-idCenter_time>refreshTime) {
			idCenter_time=System.currentTimeMillis();
			idCenters = new HashMap<String, IdCenterParam>();
			try {
				bps = paramManager.getAllParamsByGroup(Param_IdCenter);
			} catch (ParamException e) {
				throw new XDocProcException("获取对账中心参数信息出现错误:" + e.getMessage());
			}
			this.dealIdCenterParam(bps);
		}
		return idCenters.get(idCenter);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Map<String, BankParam> getAllBankParam() throws XDocProcException {
		List<BaseParam> bps = null;
		if (bankParams == null||System.currentTimeMillis()-this.bankParam_time>refreshTime) {
			bankParam_time=System.currentTimeMillis();
			bankParams = new HashMap<String, BankParam>();
			try {
				bps = paramManager.getAllParamsByGroup(Param_Bank);
			} catch (ParamException e) {
				logger.error("获取对账中心参数信息出现错误:", e,false);
				throw new XDocProcException("获取对账中心参数信息出现错误:" + e.getMessage());
			}
			this.dealBankParam(bps);
		}
		return bankParams;
	}

	
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public List<BankParam> getBankParamList(){
		List<BaseParam> bps = null;
			try {
				bps = paramManager.getAllParamsByGroup(Param_Bank+" order by nlevel");
			} catch (ParamException e) {
				logger.error("获取Param_Bank信息出现错误:", e,false);
			}
		return dealBankParamList(bps);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public BankParam getBankParam(String idBank) throws XDocProcException {
		List<BaseParam> bps = null;
		if (bankParams == null || bankParams.size() == 0||System.currentTimeMillis()-this.bankParam_time>refreshTime) {
			bankParam_time=System.currentTimeMillis();
			bankParams = new HashMap<String, BankParam>();
			try {
				bps = paramManager.getAllParamsByGroup(Param_Bank);
			} catch (ParamException e) {
				logger.error("获取机构信息出现错误:", e,false);
				throw new XDocProcException("获取机构信息出现错误:" + e.getMessage());
			}
			this.dealBankParam(bps);
		}
		return bankParams.get(idBank);
	}

	/**
	 * 退票理由参数 Modify by LiuQiFeng
	 */
	public Map<String, String> getUReason() throws XDocProcException {
		List<BaseParam> uReason = new ArrayList<BaseParam>();

		if (uReasonMap == null || uReasonMap.size() == 0) {
			uReasonMap = new HashMap<String, String>();
			try {
				uReason = paramManager.getAllParamsByGroup(Param_UReason);
				if (uReason != null && uReason.size() != 0) {
					for (BaseParam bp : uReason) {
						String proveflag = bp.getExtField("proveflag").toString();
						String uDes = bp.getExtField("description").toString();
						uReasonMap.put(proveflag, uDes);
					}
				} else {
					throw new XDocProcException("未查询验印不过理由参数，请检查"
							+ Param_UReason + "参数表配置是否正确");
				}
			} catch (ParamException e) {
				throw new XDocProcException("获取额度信息参数出现错误:" + e.getMessage());
			}
		}
		return uReasonMap;
	}

	/**
	 * 处理额度参数，将参数表的记录转换成map形式
	 */
	private void dealCreditParams(List<BaseParam> bps) throws XDocProcException {
		if (bps == null || bps.size() == 0) {
			throw new XDocProcException("未获取到额度信息参数，请检查" + Param_Credit + "参数表");
		}
		try {
			for (BaseParam baseParam : bps) {
				String idCenter = baseParam.getExtField(IdCenter).toString();
				String creditType = baseParam.getExtField(CreditID).toString();
				String creditValue_String = baseParam.getExtField(CreditValue)
						.toString();
				long creditValue = Long.valueOf(creditValue_String);
				if (creditParams.get(idCenter) == null) {
					CreditParam creditParam = new CreditParam();
					creditParams.put(idCenter, creditParam);
				}
				if (CREDIT_MI_INPT.equals(creditType)) {
					creditParams.get(idCenter).setManualInput(creditValue);
				} else if (CREDIT_MI_AUTH.equals(creditType)) {
					creditParams.get(idCenter).setManualAuth(creditValue);
				} else if (CREDIT_MP_FST.equals(creditType)) {
					creditParams.get(idCenter).setManualProve_fst(creditValue);
				} else if (CREDIT_MP_SND.equals(creditType)) {
					creditParams.get(idCenter).setManualProve_snd(creditValue);
				} else if (CREDIT_MP_AUTH.equals(creditType)) {
					creditParams.get(idCenter).setManualProve_auth(creditValue);
				}
			}
		} catch (Exception e) {
			throw new XDocProcException("进行额度参数转换时出现错:" + e.getMessage());
		}
	}

	/**
	 * 处理对账中心参数，将其转换为map形式
	 * 
	 * @param list
	 */
	private void dealIdCenterParam(List<BaseParam> bps)
			throws XDocProcException {
		if (bps == null || bps.size() == 0) {
			throw new XDocProcException("未获取到对账中心信息参数，请检查" + Param_IdCenter
					+ "参数表");
		}
		try {
			for (BaseParam baseParam : bps) {
				IdCenterParam center = new IdCenterParam();
				center.setId(baseParam.getId());
				String idCenterNo = null;
				if (baseParam.getExtField("idCenterNo") != null)
					idCenterNo = baseParam.getExtField("idCenterNo").toString();
				center.setIdCenterNo(idCenterNo);
				if (baseParam.getExtField("idCenterName") != null)
					center.setIdCenterName(baseParam
							.getExtField("idCenterName").toString());
				if (baseParam.getExtField("isCheck") != null)
					center.setIsCheck(baseParam.getExtField("isCheck")
							.toString());
				if (baseParam.getExtField("maxSingleCredit_m") != null)
					center.setMaxSingleCredit_m(Float.valueOf(baseParam
							.getExtField("maxSingleCredit_m").toString()));
				if (baseParam.getExtField("maxSingleCredit_b_m") != null)
					center.setMaxSingleCredit_b_m(Float.valueOf(baseParam
							.getExtField("maxSingleCredit_b_m").toString()));
				if (baseParam.getExtField("maxSingleCredit_l_m") != null)
					center.setMaxSingleCredit_l_m(Float.valueOf(baseParam
							.getExtField("maxSingleCredit_l_m").toString()));
				if (baseParam.getExtField("totalAmount_b_m") != null)
					center.setTotalAmount_b_m(Float.valueOf(baseParam
							.getExtField("totalAmount_b_m").toString()));
				if (baseParam.getExtField("totalAmount_l_m") != null)
					center.setTotalAmount_l_m(Float.valueOf(baseParam
							.getExtField("totalAmount_l_m").toString()));
				if (baseParam.getExtField("totalAmount_m") != null)
					center.setTotalAmount_m(Float.valueOf(baseParam
							.getExtField("totalAmount_m").toString()));
				if (baseParam.getExtField("totalTimes_b_m") != null)
					center.setTotalTimes_b_m(Float.valueOf(baseParam
							.getExtField("totalTimes_b_m").toString()));
				if (baseParam.getExtField("totalTimes_l_m") != null)
					center.setTotalTimes_l_m(Float.valueOf(baseParam
							.getExtField("totalTimes_l_m").toString()));
				if (baseParam.getExtField("totalTimes_m") != null)
					center.setTotalTimes_m(Float.valueOf(baseParam
							.getExtField("totalTimes_m").toString()));
				if (baseParam.getExtField("balance_m") != null)
					center.setBalance_m(Float.valueOf(baseParam.getExtField(
							"balance_m").toString()));
				if (baseParam.getExtField("avBalance_m") != null)
					center.setAvBalance_m(Float.valueOf(baseParam.getExtField(
							"avBalance_m").toString()));
				if (baseParam.getExtField("maxSingleCredit_s") != null)
					center.setMaxSingleCredit_s(Float.valueOf(baseParam
							.getExtField("maxSingleCredit_s").toString()));
				if (baseParam.getExtField("maxSingleCredit_l_s") != null)
					center.setMaxSingleCredit_l_s(Float.valueOf(baseParam
							.getExtField("maxSingleCredit_l_s").toString()));
				if (baseParam.getExtField("totalAmount_b_s") != null)
					center.setTotalAmount_b_s(Float.valueOf(baseParam
							.getExtField("totalAmount_b_s").toString()));
				if (baseParam.getExtField("totalAmount_l_s") != null)
					center.setTotalAmount_l_s(Float.valueOf(baseParam
							.getExtField("totalAmount_l_s").toString()));
				if (baseParam.getExtField("totalAmount_s") != null)
					center.setTotalAmount_s(Float.valueOf(baseParam
							.getExtField("totalAmount_s").toString()));
				if (baseParam.getExtField("totalTimes_b_s") != null)
					center.setTotalTimes_b_s(Float.valueOf(baseParam
							.getExtField("totalTimes_b_s").toString()));
				if (baseParam.getExtField("totalTimes_l_s") != null)
					center.setTotalTimes_l_s(Float.valueOf(baseParam
							.getExtField("totalTimes_l_s").toString()));
				if (baseParam.getExtField("totalTimes_s") != null)
					center.setTotalTimes_s(Float.valueOf(baseParam
							.getExtField("totalTimes_s").toString()));
				if (baseParam.getExtField("banlance_s") != null)
					center.setBanlance_s(Float.valueOf(baseParam.getExtField(
							"banlance_s").toString()));
				if (baseParam.getExtField("avBalance_s") != null)
					center.setAvBalance_s(Float.valueOf(baseParam.getExtField(
							"avBalance_s").toString()));
				if (baseParam.getExtField("address") != null)
					center.setAddress(baseParam.getExtField("address")
							.toString());
				if (baseParam.getExtField("zip") != null)
					center.setZip(baseParam.getExtField("zip").toString());
				if (baseParam.getExtField("phone") != null)
					center.setPhone(baseParam.getExtField("phone").toString());
				if (baseParam.getExtField("imageUrl") != null)
					center.setImageUrl(baseParam.getExtField("imageUrl")
							.toString());
				if (baseParam.getExtField("storeUrl") != null)
					center.setStoreUrl(baseParam.getExtField("storeUrl")
							.toString());
				if (baseParam.getExtField("imgSerUser") != null)
					center.setImgSerUser(baseParam.getExtField("imgSerUser")
							.toString());
				if (baseParam.getExtField("imgSerPass") != null)
					center.setImgSerPass(baseParam.getExtField("imgSerPass")
							.toString());
				if (baseParam.getExtField("sealType") != null)
					center.setSealType(baseParam.getExtField("sealType")
							.toString());
				if (baseParam.getExtField("notMatchInputType") != null)
					center.setNotMatchInputType(baseParam.getExtField(
							"notMatchInputType").toString());
				idCenters.put(idCenterNo, center);
			}
		} catch (Exception e) {
			logger.error("进行对账中心参数转换时出现错:", e,false);
			throw new XDocProcException("进行对账中心参数转换时出现错:" + e.getMessage());
		}
	}

	/**
	 * 处理对账中心参数，将其转换为map形式
	 * 
	 * @param list
	 */
	private List<BankParam> dealBankParamList(List<BaseParam> bps){
		if (bps == null || bps.size() == 0) {
			logger.error("未获取到机构信息参数，请检查" + Param_Bank + "参数表",false);
		}
		List<BankParam> list = new ArrayList<BankParam>(); 
		try {
			for (BaseParam baseParam : bps) {
				BankParam bank = new BankParam();
				bank.setId(baseParam.getId());
				if (baseParam.getExtField("idBank") != null)
				bank.setIdBank(baseParam.getExtField("idBank").toString());
				if (baseParam.getExtField("idBranch") != null)
					bank.setIdBranch(baseParam.getExtField("idBranch")
							.toString());
				if (baseParam.getExtField("idCenter") != null)
					bank.setIdCenter(baseParam.getExtField("idCenter")
							.toString());
				if (baseParam.getExtField("orgSid") != null)
					bank.setOrgSid(baseParam.getExtField("orgSid")
							.toString());
				if (baseParam.getExtField("cName") != null)
					bank.setName(baseParam.getExtField("cName").toString());
				if (baseParam.getExtField("nLevel") != null)
					bank.setLevel(Short.valueOf(baseParam.getExtField("nLevel")
							.toString()));
				if (baseParam.getExtField("updateTime") != null)
					bank.setUpdateTime(baseParam.getExtField("updateTime")
							.toString());
				if (baseParam.getExtField("updateOper") != null)
					bank.setUpdateOper(baseParam.getExtField("updateOper")
							.toString());
				list.add(bank);
			}
		} catch (Exception e) {
			logger.error("进行对账中心参数转换时出现错", e,false);
		}
		return list;
	}
	
	/**
	 * 处理对账中心参数，将其转换为map形式
	 * 
	 * @param list
	 */
	private void dealBankParam(List<BaseParam> bps) throws XDocProcException {
		if (bps == null || bps.size() == 0) {
			logger.error("未获取到机构信息参数，请检查" + Param_Bank + "参数表",false);
			throw new XDocProcException("未获取到机构信息参数，请检查" + Param_Bank + "参数表");
		}
		try {
			for (BaseParam baseParam : bps) {
				BankParam bank = new BankParam();
				bank.setId(baseParam.getId());
				if (baseParam.getExtField("idBank") != null)
				bank.setIdBank(baseParam.getExtField("idBank").toString());
				if (baseParam.getExtField("idBranch") != null)
					bank.setIdBranch(baseParam.getExtField("idBranch")
							.toString());
				if (baseParam.getExtField("idCenter") != null)
					bank.setIdCenter(baseParam.getExtField("idCenter")
							.toString());
				if (baseParam.getExtField("orgSid") != null)
					bank.setOrgSid(baseParam.getExtField("orgSid")
							.toString());
				if (baseParam.getExtField("cName") != null)
					bank.setName(baseParam.getExtField("cName").toString());
				if (baseParam.getExtField("nLevel") != null)
					bank.setLevel(Short.valueOf(baseParam.getExtField("nLevel")
							.toString()));
				if (baseParam.getExtField("updateTime") != null)
					bank.setUpdateTime(baseParam.getExtField("updateTime")
							.toString());
				if (baseParam.getExtField("updateOper") != null)
					bank.setUpdateOper(baseParam.getExtField("updateOper")
							.toString());
				bankParams.put(bank.getIdBank(), bank);
			}
		} catch (Exception e) {
			logger.error("进行对账中心参数转换时出现错", e,false);
			throw new XDocProcException("进行对账中心参数转换时出现错");
		}
	}

	public IParamManager getParamManager() {
		return paramManager;
	}

	public void setParamManager(IParamManager paramManager) {
		this.paramManager = paramManager;
	}

	@Override
	public List<String> getDeleteReason() throws XDocProcException {
		List<BaseParam> bps = null;
		if (deleteReasons == null||deleteReasons.size()==0||System.currentTimeMillis()-deleteReasons_time>refreshTime) {
			deleteReasons = new ArrayList<String>();
			try {
				bps = paramManager.getAllParamsByGroup(Param_DeleteReason);
			} catch (ParamException e) {
				logger.error("获取账单删除理由参数信息出现错误:", e,false);
				throw new XDocProcException("获取账单删除理由参数信息出现错误:"
						+ e.getMessage());
			}
			if (bps == null || bps.size() == 0) {
				throw new XDocProcException("票据删除理由表里没有数据");
			} else {
				for (BaseParam baseParam : bps) {
					deleteReasons.add(baseParam.getExtField(description)
							.toString());
				}
			}
		}
		return deleteReasons;
	}

	@Override
	public List<String> getReInputReason() throws XDocProcException {
		List<BaseParam> bps = null;
		if (reInputReasons == null||reInputReasons.size()==0||System.currentTimeMillis()-reInputReasons_time>refreshTime) {
			reInputReasons = new ArrayList<String>();
			try {
				bps = paramManager.getAllParamsByGroup(Param_ReInputReason);
			} catch (ParamException e) {
				logger.error("获取账单重录理由参数信息出现错误:", e,false);
				throw new XDocProcException("获取账单重录理由参数信息出现错误:"
						+ e.getMessage());
			}
			if (bps == null || bps.size() == 0) {
				throw new XDocProcException("票据重录理由表里没有数据");
			} else {
				for (BaseParam baseParam : bps) {
					reInputReasons.add(baseParam.getExtField(description)
							.toString());
				}
			}
		}
		return reInputReasons;
	}
	
	@Override
	public List<BaseParam> getBaseParamsByCondition(ParamQuery pq,
			String tableName) throws XDocProcException {		
		try {
			List<BaseParam> bps = paramManager.getParamByCondition(
					tableName, pq);
			return bps;			
		} catch (Exception e) {
			logger.error("查询参数表"+tableName+"失败", e,false);
			throw new XDocProcException("查询参数表"+tableName+"失败，" + e.getMessage());
		}
		
	}
	


	@Override
	public double getExchangeRate(String currtype) throws XDocProcException {
		ParamQuery pq=new ParamQuery();
	   pq.addEqPropery(BaseParam.getFiledName("currType"), currtype);
	   List<BaseParam> list=this.getBaseParamsByCondition(pq, "Param_Currtype");
	   if(list==null||list.size()==0){
		   throw new XDocProcException("未找到币种"+currtype+"对应的汇率参数");
	   }
	   BaseParam param=list.get(0);
	   double unit=Double.parseDouble((String)param.getExtField("currUnit"));
	   double rate=Double.parseDouble(param.getExtField("exchangeRate")+"");
	   return rate/unit;  
	}

	
	@Override
	public SimpleOrg getCurOrgTree(String orgId) throws XDocProcException {
		// if (root == null||System.currentTimeMillis()-root_time>refreshTime) {
		// root_time=System.currentTimeMillis();
		// List<BankParam> banks = this.getAllBankParamList();
		// // 先获取整个机构的最大深度，避免在最低一层机构时进行无效的循环(如果有一万个网点，这种无效循环的次数在最坏情况下将会是一亿次思密达)
		// // 当然也顺带把总行查出来了
		// int max = 1;
		// for (int i = 0; i < banks.size(); i++) { // 获取总行
		// BankParam bank = banks.get(i);
		// if (bank.getLevel() == 1) { // 总行思密达
		// root = new SimpleOrg(bank.getIdCenter(), bank.getName(), 1);
		// banks.remove(i);
		// i--;
		// }
		// if (bank.getLevel() > max) {
		// max = bank.getLevel();
		// }
		// }
		// if(root==null){
		// throw new XDocProcException("未获取到总行记录，请检查参数表");
		// }
		// this.getChildrenBanks(banks, root, max); //获取总行下面所有的机构
		// }
		// SimpleOrg result=new SimpleOrg("", "", 1);
		// result = this.selectChild(root, result, orgId);
		// if(result==null){
		// throw new XDocProcException("未获取到当前登陆人员所属的机构树信息");
		// }
		// SimpleOrg copy=new SimpleOrg("", "", 1);
		// int curLevel=this.getBankParam(orgId).getLevel();
		// this.copySimpleOrgWithoutParent(result, copy,curLevel);
		// //用json处理双向关联的对象会出现死循环，所以要去掉机构的parent字段
		// copy.setCurLevel(curLevel);
		 // return copy;
		 long time=System.currentTimeMillis();
		 SimpleOrg result=this.getCurSimpleOrg(orgId);
		 logger.info("获取可访问机构树耗时:"+(System.currentTimeMillis()-time)+"ms",false);
		 return result;
	}

	/**
	 * @author 陈林江 2013-5-7
	 * @param orgNo 机构号
	 * @return
	 * @throws XDocProcException
	 */
	private SimpleOrg getCurSimpleOrg(String orgNo) throws XDocProcException {
		List<BankParam> banks = this.getAllBankParamList();
		SimpleOrg root=null;
		Map<String, SimpleOrg> map = new HashMap<String, SimpleOrg>();
		for (BankParam bank : banks) {
			SimpleOrg org = new SimpleOrg(bank.getIdBank(), bank.getName(),
					bank.getLevel());
			org.setParentOrgNo(bank.getIdBranch());
			map.put(bank.getIdBank(), org);
			if(bank.getLevel()==1){
				root=org;
			}
		}
		if(root==null){
			throw new XDocProcException("未找到根机构(级别为1的机构)!");
		}
		for (BankParam bankParam : banks) {
			if(bankParam.getLevel()==1){
				continue;
			}
			SimpleOrg parent = map.get(bankParam.getIdBranch());
			if (parent == null) {
				continue;
			}
			if (parent.getChildren() == null) {
				parent.setChildren(new ArrayList<SimpleOrg>());
			}
			parent.addChild(map.get(bankParam.getIdBank()));
			map.get(bankParam.getIdBank()).setParent(parent);
		}
		SimpleOrg result = map.get(orgNo);
		if (result == null) {
			throw new XDocProcException("未找到机构" + orgNo + "对应的机构树!");
		}
		SimpleOrg temp=result;
		while(temp.getParent()!=null){//将当前机构的兄弟机构及其父辈机构的兄弟机构都去除
			SimpleOrg parent=temp.getParent();
			List<SimpleOrg> list=parent.getChildren();
			for(int i=0;i<list.size();i++){
				SimpleOrg simpleOrg=list.get(i);
				if(simpleOrg==temp){
					parent.getChildren().removeAll(parent.getChildren());
					parent.addChild(simpleOrg);
					temp=parent;
					break;
				}
			}
		}
		Iterator<SimpleOrg> iterator=map.values().iterator();
		while(iterator.hasNext()){  //去除parent属性，否则在用json解析该对象时会出现死循环
			SimpleOrg org=iterator.next();
			org.setParent(null);
			if(org.getLevel()>=result.getLevel()){//将当前人员所属机构以及其下级机构都添加一个空的下级机构，这样在下拉列表里聚可以选择为空了
				org.getChildren().add(0, new SimpleOrg("", "", org.getLevel() + 1));
			}
		}
		return temp;
	}
	
	/**
	 * 递归获取机构的子机构
	 * 
	 * @param banks
	 *            还未找到归宿的机构们
	 * @param cur
	 *            当前机构
	 * @param max
	 *            机构最大深度
	 */
	private void getChildrenBanks(List<BankParam> banks, SimpleOrg cur, int max) {
		for (int i = 0; i < banks.size(); i++) {
			BankParam bank = banks.get(i);
			if (bank.getLevel() == cur.getLevel() + 1) { // 先判断遍历的机构是否可能是当权机构的子机构
				if (bank.getIdBranch().equals(cur.getOrgId())) { // 获取到子机构
					SimpleOrg child = new SimpleOrg(bank.getIdBank(),
							bank.getName(), cur.getLevel() + 1);
					cur.addChild(child);
					child.setParent(cur);
					banks.remove(i); // 除去已被处理的机构
					i--;
					if (cur.getLevel() < max - 1) { // cur.getLevel()+1<max
													// 即遍历到的机构还有可能有子机构
						getChildrenBanks(banks, child, max);
					}
				}
			}
		}
	}
	
	/**
	 * 获取登陆人员可以访问的机构树
	 * @param cur 遍历当前机构
	 * @param result 方法的处理结果
	 * @param orgId 登录人员的机构号
	 */
	private SimpleOrg selectChild(SimpleOrg cur,SimpleOrg result,String orgId)throws XDocProcException{
		
		if(orgId.equals(cur.getOrgId())){ //找到匹配项，剪切掉其兄弟机构，以及其上属机构的兄弟机构
			result.setOrgId(cur.getOrgId());
			result.setOrgName(cur.getOrgName());
			result.setLevel(cur.getLevel());
			result.setChildren(cur.getChildren()); //获取子机构
			while((cur=cur.getParent())!=null){//去掉父机构的兄弟机构
				SimpleOrg parent=new SimpleOrg(cur.getOrgId(), cur.getOrgName(), cur.getLevel());
				result.setParent(parent);
				parent.addChild(result);
				result=result.getParent();
			}
				return result;
		}	
		List<SimpleOrg> orgs=cur.getChildren();
		if(orgs==null||orgs.size()==0){//处理到最底层也未找到匹配项
			return null;
		}
		for(int i=0;i<orgs.size();i++){
			SimpleOrg org=orgs.get(i);
			org=this.selectChild(org,result,orgId);
			if(org!=null){//已匹配
				return org;
			}
		}
		return null;
	}
	
	/**
	 * 去掉机构的parent字段，因为双向关联会导致json解析时出现死循环
	 * @param cur 当前机构
	 * @param copy 需要被复制处理的机构
	 * @param curLevel 当前登录人员所属机构的级别
	 */
	private void copySimpleOrgWithoutParent(SimpleOrg cur,SimpleOrg copy,int curLevel){
		copy.setLevel(cur.getLevel());
		copy.setOrgName(cur.getOrgName());
		copy.setOrgId(cur.getOrgId());
		if(copy.getLevel()>=curLevel){  //如果当前登录人员所属机构的level小于等于当前遍历的机构的level(level越小，机构级别越高)，
			                            //则在当前遍历机构的下面加一个值为空的子机构，这样界面的下拉框里就有一个为空的下拉选项了
			copy.addChild(new SimpleOrg("", "", copy.getLevel()+1));
		}
		List<SimpleOrg> orgs=cur.getChildren();
		for (SimpleOrg simpleOrg : orgs) {
			SimpleOrg child=new SimpleOrg("", "", copy.getLevel()+1);
			copy.addChild(child);
			this.copySimpleOrgWithoutParent(simpleOrg, child,curLevel);
		}
		
	}
	
	
	/**
	 * 获取某机构所能访问所有机构的列表
	 * 
	 * @param orgId
	 * @return
	 */
	private List<BankParam> getAllBankParamList() throws XDocProcException {
		List<BankParam> result = new ArrayList<BankParam>();
		if (bankParams == null || bankParams.size() == 0) {
			this.getBankParam("init"); // 此处只是为了初始化数据
		}
		Set<String> keySet = bankParams.keySet();
		for (String key : keySet) {
			result.add(bankParams.get(key));
		}
		return result;
	}
	@Override
    public XPeopleInfo getCurrLoginPeople()
    {
    	HttpServletRequest request = ServletActionContext.getRequest();
		XPeopleInfo people = (XPeopleInfo) request.getSession().getAttribute(
				AMSecurityDefine.XPEOPLEINFO);
		return people;
    }
	/**
	 * 账户是否对账map
	 * 0 对账  1不对账
	 * @return
	 */
	public Map<String, String> getRefIsCheck (){
		Map<String, String> isCheckMap= new  HashMap<String, String>();
		isCheckMap.put("0","对账");
		isCheckMap.put("1","不对账");
		return isCheckMap;
		
	}

	/**
	 * 是不是特殊帐户map
	 * 0 特殊账户  1普通账户
	 * @return
	 */
	public Map<String, String> getRefIsSpecile (){
		Map<String, String> isSpecile= new  HashMap<String, String>();
		isSpecile.put("1","特殊账户");
		isSpecile.put("0","普通账户");
		return isSpecile;
		
	}
	
	/**
	 * 账户状态map
	 * 0 正常  1 销户 2 长期不动户 3 不动转收益
	 * @return
	 */
	public Map<String, String> getAccState (){
		Map<String, String> accSatae= new  HashMap<String, String>();
		accSatae.put("0","正常");
		accSatae.put("1","长期不动户");
		accSatae.put("2","不动转收益");
		return accSatae;
		
	}
	
	/**
	 * 文件删除
	 * @param fileDelete
	 */
	public void deleteFile(File file){
		try {
			if(file.exists()){
				if(file.isFile()){
					file.delete();
				}else if(file.isDirectory()){
					File[] files = file.listFiles();
					for(File fileTemp:files){
						deleteFile(fileTemp);
					}
				}
				file.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * ajax方式返回信息
	 * @param result
	 */
	public void ajaxResult(String result){
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");  
	    response.setCharacterEncoding("UTF-8");  
	    response.setHeader("Cache-Control","no-cache");  
	    response.setHeader("Pragma","no-cache");  
	    PrintWriter out;  
	    try {  
	        out = response.getWriter();  
	        out.print(result);  
	        out.flush();  
	        out.close();
	    } catch (IOException e) { 
	    	logger.info("ajax返回信息出错", e,false);
	   } 
	}
	
	/**
	 * 获取传递的参数
	 * @return
	 */
	public String getParameter(String param){
		String result="";
		HttpServletRequest request = ServletActionContext.getRequest();
		result=request.getParameter(param);
		return result;
	}
	
	public DataLogUtils getLogger() {
		return logger;
	}

	public void setLogger(DataLogUtils logger) {
		this.logger = logger;
	}
	
	/**
	 * 获取日志类
	 */
	public DataLogUtils getDataLogUtils(){
		return logger;
	}
}
