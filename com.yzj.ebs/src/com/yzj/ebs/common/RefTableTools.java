package com.yzj.ebs.common;

import com.infotech.publiclib.Exception.DaoException;
import com.yzj.ebs.database.dao.UniversalDao;
import com.yzj.ebs.util.EbillUtil;
import com.yzj.wf.pam.common.IParamManager;
import com.yzj.wf.pam.common.ParamException;
import com.yzj.wf.pam.db.BaseParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 
 * 创建于:2012-9-25<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 为所有的ref表做键值转换
 * 
 * @author pengwenxuan
 * @version 1.0.0
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
//TODO:
public class RefTableTools {
	private IParamManager paramManager;
	private UniversalDao universalDao;
	List<BaseParam> bps = null;
	public static TreeMap<String, Integer> RefAccCycleMap = null;
	public static TreeMap<String, Integer> RefCheckflagMap = null;
	public static TreeMap<String, Integer> RefDirectionMap = null;
	public static TreeMap<String, Integer> RefDocflagMap = null;
	public static TreeMap<String, Integer> RefDocstateMap = null;
	public static TreeMap<String, Integer> RefFaceflagMap = null;
	public static TreeMap<String, Integer> RefKeyflagMap = null;
	public static TreeMap<String, Integer> RefKeytypeMap = null;
	public static TreeMap<String, Integer> RefMatchflagMap = null;
	public static TreeMap<String, Integer> RefNoticeFlagMap = null;
	public static TreeMap<String, Integer> RefProveflagMap = null;
	public static TreeMap<String, Integer> RefProvestateMap = null;
	public static TreeMap<String, Integer> RefSealModeMap = null;
	public static TreeMap<String, Integer> RefSignflagMap = null;
	public static TreeMap<String, Integer> RefStsMap = null;
	public static TreeMap<String, Integer> RefSendModeMap = null;
	
//	public static TreeMap<String, Integer> RefQuerytypeMap = null;
	
	public static TreeMap<Integer, String> Sym_RefAccCycleMap = null;
	public static TreeMap<Integer, String> Sym_RefCheckflagMap = null;
	public static TreeMap<Integer, String> Sym_RefDirectionMap = null;
	public static TreeMap<Integer, String> Sym_RefDocflagMap = null;
	public static TreeMap<Integer, String> Sym_RefDocstateMap = null;
	public static TreeMap<Integer, String> Sym_RefFaceflagMap = null;
	public static TreeMap<Integer, String> Sym_RefKeyflagMap = null;
	public static TreeMap<Integer, String> Sym_RefKeytypeMap = null;
	public static TreeMap<Integer, String> Sym_RefMatchflagMap = null;
	public static TreeMap<Integer, String> Sym_RefNoticeFlagMap = null;
	public static TreeMap<Integer, String> Sym_RefProveflagMap = null;
	public static TreeMap<Integer, String> Sym_RefProvestateMap = null;
	public static TreeMap<Integer, String> Sym_RefReproveStateMap = null;
	public static TreeMap<Integer, String> Sym_RefSealModeMap = null;
	public static TreeMap<Integer, String> Sym_RefSignflagMap = null;
	public static TreeMap<Integer, String> Sym_RefStsMap = null;
	public static TreeMap<Integer, String> Sym_RefSendModeMap = null;

//	public static TreeMap<Integer, String> Sym_RefQuerytypeMap=null;
	
	public static TreeMap<String, String> TxtRefAccCycleMap = null;
	public static TreeMap<String, String> TxtRefAcctypeMap = null;
	public static TreeMap<String, String> TxtRefCheckflagMap = null;
	public static TreeMap<String, String> TxtRefDirectionMap = null;
	public static TreeMap<String, String> TxtRefDocflagMap = null;
	public static TreeMap<String, String> TxtRefDocstateMap = null;
	public static TreeMap<String, String> TxtRefFaceflagMap = null;
	public static TreeMap<String, String> TxtRefKeyflagMap = null;
	public static TreeMap<String, String> TxtRefKeytypeMap = null;
	public static TreeMap<String, String> TxtRefMatchflagMap = null;
	public static TreeMap<String, String> TxtRefNoticeFlagMap = null;
	public static TreeMap<String, String> TxtRefProveflagMap = null;
	public static TreeMap<String, String> TxtRefProvestateMap = null;
	public static TreeMap<String, String> TxtRefSealModeMap = null;
	public static TreeMap<String, String> TxtRefSignflagMap = null;
	public static TreeMap<String, String> TxtRefStsMap = null;
	public static TreeMap<String, String> TxtRefSendModeMap = null;
	
//	public static TreeMap<String, String> TxtRefQuerytypeMap=null;
	
	public static TreeMap<String, String> ValRefAccCycleMap = null;
	public static TreeMap<String, String> ValRefAcctypeMap = null;
	public static TreeMap<String, String> ValRefCheckflagMap = null;
	public static TreeMap<String, String> ValRefDirectionMap = null;
	public static TreeMap<String, String> ValRefDocflagMap = null;
	public static TreeMap<String, String> ValRefDocstateMap = null;
	public static TreeMap<String, String> ValRefFaceflagMap = null;
	public static TreeMap<String, String> ValRefKeyflagMap = null;
	public static TreeMap<String, String> ValRefKeytypeMap = null;
	public static TreeMap<String, String> ValRefMatchflagMap = null;
	public static TreeMap<String, String> ValRefNoticeFlagMap = null;
	public static TreeMap<String, String> ValRefProveflagMap = null;
	public static TreeMap<String, String> ValRefProvestateMap = null;
	public static TreeMap<String, String> ValRefSealModeMap = null;
	public static TreeMap<String, String> ValRefSignflagMap = null;
	public static TreeMap<String, String> ValRefStsMap = null;
	public static TreeMap<String, String> ValRefSendModeMap = null;
	public static TreeMap<String, String> ValNotMatchAuthflagMap = null;
	public static TreeMap<String, String> ValSpecialFlagMap = null;
	public static TreeMap<String, String> ValRushStateMap = null;
	public static TreeMap<String, String> ValRushMethodMap = null;
	public static TreeMap<String, String> ValRushResultMap = null;
	public static TreeMap<String, String> ValRefUrgeTypeMap = null;
	public static TreeMap<String, String> ValRefOperLogModuleMap = null;
	public static TreeMap<String, String> ValRefCustomResponseMap = null;
	public static TreeMap<String, String> ValParamSubnocMap = null;
	public static TreeMap<String, String> valParamCurrtypeMap = null;
	
//	public static TreeMap<String, String> ValRefQuerytypeMap=null;
	
	public void init() {
		try {
			RefAccCycleMap = getRefDescInt("ebs_Ref_AccCycle");
			RefCheckflagMap = getRefDescInt("ebs_Ref_Checkflag");
			RefDirectionMap = getRefDescInt("ebs_Ref_Direction");
			RefDocflagMap = getRefDescInt("ebs_Ref_Docflag");
			RefDocstateMap = getRefDescInt("ebs_Ref_Docstate");
			RefFaceflagMap = getRefDescInt("ebs_Ref_Faceflag");
			RefKeyflagMap = getRefDescInt("ebs_Ref_Keyflag");
			RefKeytypeMap = getRefDescInt("ebs_Ref_Keytype");
			RefMatchflagMap = getRefDescInt("ebs_Ref_Matchflag");
			RefNoticeFlagMap = getRefDescInt("ebs_Ref_NoticeFlag");
			RefProveflagMap = getRefDescInt("ebs_Ref_Proveflag");
			RefProvestateMap = getRefDescInt("ebs_Ref_Provestate");
			RefSealModeMap = getRefDescInt("ebs_Ref_SealMode");
			RefSignflagMap = getRefDescInt("ebs_Ref_Signflag");
			RefStsMap = getRefDescInt("ebs_Ref_Sts");
			RefSendModeMap = getRefDescInt("ebs_Ref_SendMode");
		
//			RefQuerytypeMap=getRefDescInt("ebs_Ref_Querytype");
			

			Sym_RefAccCycleMap = EbillUtil.getSymmetryMap_SI2IS(RefAccCycleMap);
			Sym_RefCheckflagMap = EbillUtil
					.getSymmetryMap_SI2IS(RefCheckflagMap);
			Sym_RefDirectionMap = EbillUtil
					.getSymmetryMap_SI2IS(RefDirectionMap);
			Sym_RefDocflagMap = EbillUtil.getSymmetryMap_SI2IS(RefDocflagMap);
			Sym_RefDocstateMap = EbillUtil.getSymmetryMap_SI2IS(RefDocstateMap);
			Sym_RefFaceflagMap = EbillUtil.getSymmetryMap_SI2IS(RefFaceflagMap);
			Sym_RefKeyflagMap = EbillUtil.getSymmetryMap_SI2IS(RefKeyflagMap);
			Sym_RefKeytypeMap = EbillUtil.getSymmetryMap_SI2IS(RefKeytypeMap);
			Sym_RefMatchflagMap = EbillUtil
					.getSymmetryMap_SI2IS(RefMatchflagMap);
			Sym_RefNoticeFlagMap = EbillUtil
					.getSymmetryMap_SI2IS(RefNoticeFlagMap);
			Sym_RefProveflagMap = EbillUtil
					.getSymmetryMap_SI2IS(RefProveflagMap);
			Sym_RefProvestateMap = EbillUtil
					.getSymmetryMap_SI2IS(RefProvestateMap);
			Sym_RefSealModeMap = EbillUtil.getSymmetryMap_SI2IS(RefSealModeMap);
			Sym_RefSignflagMap = EbillUtil.getSymmetryMap_SI2IS(RefSignflagMap);
			Sym_RefStsMap = EbillUtil.getSymmetryMap_SI2IS(RefStsMap);
			Sym_RefSendModeMap = EbillUtil.getSymmetryMap_SI2IS(RefSendModeMap);
	//		Sym_RefQuerytypeMap = EbillUtil.getSymmetryMap_SI2IS(RefQuerytypeMap);
			
			
			TxtRefAccCycleMap = getRefDesc("ebs_Ref_AccCycle");
			TxtRefAcctypeMap = getRefDesc("ebs_Ref_Acctype");
			TxtRefCheckflagMap = getRefDesc("ebs_Ref_Checkflag");
			TxtRefDirectionMap = getRefDesc("ebs_Ref_Direction");
			TxtRefDocflagMap = getRefDesc("ebs_Ref_Docflag");
			TxtRefDocstateMap = getRefDesc("ebs_Ref_Docstate");
			TxtRefFaceflagMap = getRefDesc("ebs_Ref_Faceflag");
			TxtRefKeyflagMap = getRefDesc("ebs_Ref_Keyflag");
			TxtRefKeytypeMap = getRefDesc("ebs_Ref_Keytype");
			TxtRefMatchflagMap = getRefDesc("ebs_Ref_Matchflag");
			TxtRefNoticeFlagMap = getRefDesc("ebs_Ref_NoticeFlag");
			TxtRefProveflagMap = getRefDesc("ebs_Ref_Proveflag");
			TxtRefProvestateMap = getRefDesc("ebs_Ref_Provestate");
			TxtRefSealModeMap = getRefDesc("ebs_Ref_SealMode");
			TxtRefSignflagMap = getRefDesc("ebs_Ref_Signflag");
			TxtRefStsMap = getRefDesc("ebs_Ref_Sts");
			TxtRefSendModeMap = getRefDesc("ebs_Ref_SendMode");
	//		TxtRefQuerytypeMap = getRefDesc("ebs_Ref_Querytype");
			
			

			ValRefAccCycleMap = getRefDescId("ebs_Ref_AccCycle");
			ValRefAcctypeMap = getRefDescId("ebs_Ref_Acctype");
			ValRefCheckflagMap = getRefDescId("ebs_Ref_Checkflag");
			ValRefDirectionMap = getRefDescId("ebs_Ref_Direction");
			ValRefDocflagMap = getRefDescId("ebs_Ref_Docflag");
			ValRefDocstateMap = getRefDescId("ebs_Ref_Docstate");
			ValRefFaceflagMap = getRefDescId("ebs_Ref_Faceflag");

			ValRefKeyflagMap = getRefDescId("ebs_Ref_Keyflag");
			ValRefKeytypeMap = getRefDescId("ebs_Ref_Keytype");
			ValRefMatchflagMap = getRefDescId("ebs_Ref_Matchflag");
			ValRefNoticeFlagMap = getRefDescId("ebs_Ref_NoticeFlag");
			ValRefProveflagMap = getRefDescId("ebs_Ref_Proveflag");
			ValRefProvestateMap = getRefDescId("ebs_Ref_Provestate");
			ValRefSealModeMap = getRefDescId("ebs_Ref_SealMode");
			ValRefSignflagMap = getRefDescId("ebs_Ref_Signflag");
			ValRefStsMap = getRefDescId("ebs_Ref_Sts");
			ValRefSendModeMap = getRefDescId("ebs_Ref_SendMode");
			ValNotMatchAuthflagMap = getRefDescId("ebs_Ref_NotMatchAuthflag");
			ValSpecialFlagMap = getRefDescId("ebs_Ref_Specialflag");
			ValRushStateMap = getRefDescId("ebs_Ref_RushState");
			ValRushMethodMap = getRefDescId("ebs_Ref_RushMethod");
			ValRushResultMap = getRefDescId("ebs_Ref_RushResult");
			ValRefUrgeTypeMap = getRefDescId("ebs_Ref_UrgeType");
			ValRefOperLogModuleMap = getRefDescId("ebs_Ref_OperLogModule");
			ValRefCustomResponseMap = getRefDescId("ebs_Ref_customResponse");
	//		ValRefQuerytypeMap = getRefDescId("ebs_Ref_Querytype");
			
			//得到科目号Map
			ValParamSubnocMap =  getParamDescId("param_subnoc");
			valParamCurrtypeMap = getParamSub ("param_currtype");
			
		} catch (DaoException e) {
			e.printStackTrace();
		}
	}


	public TreeMap<String, String> getRefDesc(String tablename)
			throws DaoException {
		TreeMap<String, String> itemap = new TreeMap<String, String>();
		try {
			bps = paramManager.getAllParamsByGroup(tablename);
		} catch (ParamException e) {
			throw new DaoException("获取对账中心参数信息出现错误:" + e.getMessage());
		}
		try {
			for (BaseParam baseParam : bps) {
				String refid = baseParam.getExtField("refid").toString();
				String refdesc = baseParam.getExtField("refdesc").toString();
				itemap.put(refdesc, refid);
			}
		} catch (Exception e) {
			throw new DaoException("进行对账中心参数转换时出现错:" + e.getMessage());
		}
		return itemap;
	}

	public TreeMap<String, String> getRefDescId(String tablename)
			throws DaoException {
		TreeMap itemMap = new TreeMap();
		try {
			bps = paramManager.getAllParamsByGroup(tablename);
		} catch (ParamException e) {
			throw new DaoException("获取对账中心参数信息出现错误:" + e.getMessage());
		}
		try {
			for (BaseParam baseParam : bps) {
				String refid = baseParam.getExtField("refid").toString();
				String refdesc = baseParam.getExtField("refdesc").toString();
				itemMap.put(refid,refdesc);
			}
		} catch (Exception e) {
			throw new DaoException("进行对账中心参数转换时出现错:" + e.getMessage());
		}
		return itemMap;
	}

	public TreeMap<String, Integer> getRefDescInt(String tablename)
			throws DaoException {
		TreeMap<String, Integer> itemap = new TreeMap<String, Integer>();
		try {
			bps = paramManager.getAllParamsByGroup(tablename);
		} catch (ParamException e) {
			throw new DaoException("获取对账中心参数信息出现错误:" + e.getMessage());
		}
		try {
			for (BaseParam baseParam : bps) {
				Integer refid = new Integer(baseParam.getExtField("refid")
						.toString());
				String refdesc = baseParam.getExtField("refdesc").toString();
				itemap.put(refdesc, refid);
			}
		} catch (Exception e) {
			throw new DaoException("进行对账中心参数转换时出现错:" + e.getMessage());
		}
		return itemap;
	}

	public TreeMap<Integer, String> getRefDescInt_A(String tablename)
			throws DaoException {
		TreeMap<Integer, String> itemap = new TreeMap<Integer, String>();
		try {
			bps = paramManager.getAllParamsByGroup(tablename);
		} catch (ParamException e) {
			throw new DaoException("获取对账中心参数信息出现错误:" + e.getMessage());
		}
		try {
			for (BaseParam baseParam : bps) {
				Integer refid = new Integer(baseParam.getExtField("refid")
						.toString());
				String refdesc = baseParam.getExtField("refdesc").toString();
				itemap.put(refid, refdesc);
			}
		} catch (Exception e) {
			throw new DaoException("进行对账中心参数转换时出现错:" + e.getMessage());
		}
		return itemap;
	}

	public TreeMap<String, Short> getRefDescShort(String tablename)
			throws DaoException {
		Short refId = (short) 0;
		TreeMap<String, Short> itemap = new TreeMap<String, Short>();
		try {
			bps = paramManager.getAllParamsByGroup(tablename);
		} catch (ParamException e) {
			throw new DaoException("获取对账中心参数信息出现错误:" + e.getMessage());
		}
		try {
			for (BaseParam baseParam : bps) {
				String temp = baseParam.getExtField("refid").toString();
				refId = Short.valueOf(Short.parseShort(temp));
				String refdesc = baseParam.getExtField("refdesc").toString();

				refId = Short.valueOf(Short.parseShort(temp));
				itemap.put(refdesc, refId);

			}
		} catch (Exception e) {
			throw new DaoException("进行对账中心参数转换时出现错:" + e.getMessage());
		}
		return itemap;

	}

	public TreeMap<String, Byte> getRefDescByte(String tablename)
			throws DaoException {
		Byte refId = (short) 0;
		TreeMap<String, Byte> itemap = new TreeMap<String, Byte>();
		try {
			bps = paramManager.getAllParamsByGroup(tablename);
		} catch (ParamException e) {
			throw new DaoException("获取对账中心参数信息出现错误:" + e.getMessage());
		}
		try {
			for (BaseParam baseParam : bps) {
				String temp = baseParam.getExtField("refid").toString();
				refId = Byte.valueOf(Byte.parseByte(temp));
				String refdesc = baseParam.getExtField("refdesc").toString();

				itemap.put(refdesc, refId);
			}
		} catch (Exception e) {
			throw new DaoException("进行对账中心参数转换时出现错:" + e.getMessage());
		}
		return itemap;
	}
	
	//得到 科目号MAP
	public TreeMap<String, String> getParamDescId(String tablename)throws DaoException{
		String str = "select t.subnoc,t.memo from param_subnoc t ";
		
			List<Object> list = new ArrayList<Object>();
			try {
				list = universalDao.findBySql(str);
			} catch (XDocProcException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			TreeMap<String,String> listMap = new TreeMap<String,String>();
			int leng = list.size();
			for(int i =0;i< leng;i++){
				Object[] o = (Object[]) list.get(i);
				listMap.put((String)o[0],(String)o[1]);
			}
		return listMap;
	}
	
	//得到币种 
	public TreeMap<String, String> getParamSub(String tablename)throws DaoException{
		String str = "select t.currtype,t.chnname from param_currtype t ";
		List<Object> list = new ArrayList<Object>();
		try {
			list = universalDao.findBySql(str);
		} catch (XDocProcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TreeMap<String,String> listMap = new TreeMap<String,String>();
		int leng = list.size();
		for(int i =0;i< leng;i++){
			Object[] o = (Object[]) list.get(i);
			listMap.put((String)o[0],(String)o[1]);
		}
		return listMap;
	}
	

	public IParamManager getParamManager() {
		return paramManager;
	}

	public void setParamManager(IParamManager paramManager) {
		this.paramManager = paramManager;
	}

	public UniversalDao getUniversalDao() {
		return universalDao;
	}

	public void setUniversalDao(UniversalDao universalDao) {
		this.universalDao = universalDao;
	}

	public List<BaseParam> getBps() {
		return bps;
	}

	public void setBps(List<BaseParam> bps) {
		this.bps = bps;
	}

}
