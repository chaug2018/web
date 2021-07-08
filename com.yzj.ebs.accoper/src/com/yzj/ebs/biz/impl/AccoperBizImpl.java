/**
 * AccoperBizImpl.java
 * 版权所有(C) 2013 深圳市银之杰科技股份有限公司
 * 创建:Jiangzhengqiu 2013-03-29
 */
package com.yzj.ebs.biz.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.yzj.ebs.biz.IAccModifyBiz;
import com.yzj.ebs.common.IBasicInfoAdm;
import com.yzj.ebs.common.IBasicInfoLogAdm;
import com.yzj.ebs.common.INewSealAdm;
import com.yzj.ebs.common.IPublicTools;
import com.yzj.ebs.common.OperLogModuleDefine;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.database.BasicInfo;
import com.yzj.ebs.database.BasicInfoLog;
import com.yzj.wf.core.model.po.wrapper.XPeopleInfo;

/**
 * 创建于:2013-03-29 版权所有(C) 2013 深圳市银之杰科技股份有限公司
 * 
 * 账户信息业务逻辑统一实现
 * 
 * @author jiangzhengqiu
 * @version 1.0.0
 */
public class AccoperBizImpl implements IAccModifyBiz {

	/**
	 * 自动生成序列
	 */
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private IBasicInfoAdm basicInfoAdm;
	private IBasicInfoLogAdm basicInfoLogAdm;
	private INewSealAdm newSealAdm;
	private IPublicTools tools;

	/***
	 * 删除账户信息业务逻辑
	 * 
	 * @param info
	 *            账户信息对象
	 * @throws XDocProcException
	 */
	public void delete(BasicInfo info) throws XDocProcException {
		try {
			XPeopleInfo people = tools.getCurrLoginPeople();
			if (people == null) {
				throw new XDocProcException("会话超时，请重新登录！");
			}
			try {
				basicInfoAdm.delete(info);
				BasicInfoLog infoLog = new BasicInfoLog();
				infoLog.setOpCode(people.getPeopleCode());
				infoLog.setAccNo(info.getAccNo());
				infoLog.setAccName(info.getAccName());
				infoLog.setOpDate(sdf.format(new Date()));
				infoLog.setIdBank(info.getIdBank());
				infoLog.setIdBranch(info.getIdBranch());
				infoLog.setIdCenter(info.getIdCenter());
				infoLog.setOpMode(OperLogModuleDefine.accModify);
				infoLog.setChnOpMode(OperLogModuleDefine
						.getModuleName(OperLogModuleDefine.accModify));
				infoLog.setOpDesc("删除账户成功! 账户:" + info.getAccNo() + ";子账号:"
						+ info.getAccSon());
			} catch (XDocProcException e) {
				throw new XDocProcException(e.getMessage());
			}
		} catch (XDocProcException e1) {
			throw new XDocProcException(e1.getMessage());
		}
	}

	/***
	 * 判断验印账号与签约账号的客户号是否一致
	 * 
	 * @return 是否一致
	 */
	public boolean isTheSame(String accNo, String sealAccno)
			throws XDocProcException {
		boolean sameCustid = false;
		// 判断验印账号的客户号必须跟签约账号一致
		String querySql = "select count(CUSTID)  from EBS_BASICINFO where accno = '"
				+ accNo
				+ "' and "
				+ "CUSTID =(select CUSTID from  EBS_BASICINFO where accno = '"
				+ sealAccno + "' group by CUSTID) group by CUSTID";
		try {

			List<?> queryCount = basicInfoAdm.findBySql(querySql);
			// 如果验印账号与签约账号的客户号不一致则查询出的结果集大于0
			if (queryCount != null && queryCount.size() > 0) {
				Integer count = Integer.valueOf(queryCount.get(0).toString());
				if (count <= 0) {
					sameCustid = true;
				}
			} else {
				sameCustid = true;
			}
		} catch (XDocProcException e1) {
			throw new XDocProcException(e1.getMessage());
		}
		return sameCustid;
	}

	/***
	 * 判断账号是否存在印鉴
	 * 
	 * @return 是否存在印鉴
	 * @throws XDocProcException
	 */
	public boolean isExistSeal(String accNo) throws XDocProcException {
		// 如果一致的话判断验印账号在印鉴库是否存在
		String isExistSql = "select accno from zl where accno  = '" + accNo
				+ "'";
		try {
			List<?> queryCount = newSealAdm.findBySql(isExistSql);
			if (queryCount == null || queryCount.size() < 1) {
				return true;
			}
		} catch (XDocProcException e1) {
			throw new XDocProcException(e1.getMessage());
		}
		return false;
	}

	/***
	 * 修改账户信息
	 * 
	 * @return void
	 * @throws XDocProcException
	 */
	public void modify(BasicInfo info, String desc) throws XDocProcException {
		XPeopleInfo people = tools.getCurrLoginPeople();
		BasicInfoLog infoLog = new BasicInfoLog();
		infoLog.setOpCode(people.getPeopleCode());
		infoLog.setAccNo(info.getAccNo());
		infoLog.setAccName(info.getAccName());
		infoLog.setOpDate(sdf.format(new Date()));
		infoLog.setIdBank(info.getIdBank());
		infoLog.setIdBranch(info.getIdBranch());
		infoLog.setIdCenter(info.getIdCenter());
		infoLog.setOpMode(OperLogModuleDefine.accModify);
		infoLog.setChnOpMode(OperLogModuleDefine
				.getModuleName(OperLogModuleDefine.accModify));
		infoLog.setOpDesc(desc);
		
		try {
			basicInfoAdm.updateBasicInfo(info);
			basicInfoLogAdm.create(infoLog);    //日志记录
		} catch (XDocProcException e) {
			throw new XDocProcException("修改账户信息失败");
		}
	}

	/***
	 * 新增一个客户信息
	 * 
	 * @throws XDocProcException
	 */
	public void create(BasicInfo info, String desc) throws XDocProcException {
		XPeopleInfo people = tools.getCurrLoginPeople();
		BasicInfoLog infoLog = new BasicInfoLog();
		infoLog.setOpCode(people.getPeopleCode());
		infoLog.setAccNo(info.getAccNo());
		infoLog.setAccName(info.getAccName());
		infoLog.setOpDate(sdf.format(new Date()));
		infoLog.setIdBank(info.getIdBank());
		infoLog.setIdBranch(info.getIdBranch());
		infoLog.setIdCenter(info.getIdCenter());
		infoLog.setOpMode(OperLogModuleDefine.accSign);
		infoLog.setChnOpMode(OperLogModuleDefine
				.getModuleName(OperLogModuleDefine.accSign));
		desc = "新增签约账户,账号:" + info.getAccNo() + ",户名:" + info.getAccName()
				+ ",合同号:" + info.getSignContractNo() + desc;
		infoLog.setOpDesc(desc);
		try {
			basicInfoAdm.create(info);
			basicInfoLogAdm.create(infoLog);
		} catch (XDocProcException e) {
			throw new XDocProcException(e.getMessage());
		}
	}

	/***
	 * 根據帳號查詢帳戶信息
	 * 
	 * @throws XDocProcException
	 */
	public BasicInfo getOneByAccNo(String accNo) throws XDocProcException {
		return basicInfoAdm.getOneByAccNo(accNo);
	}

	/***
	 * 查询账户信息
	 * 
	 * @return 账户信息列表
	 */
	public List<BasicInfo> getBasicinfoData(Map<String, String> queryMap,
			PageParam param) throws XDocProcException {
		return basicInfoAdm.getBasicinfoData(queryMap, (PageParam) param);
	}
	
	/***
	 * 查询网银账户信息
	 * 
	 * @return 账户信息列表
	 */
	public List<BasicInfo> getAcctListData(Map<String, String> queryMap,
			PageParam param) throws XDocProcException {
		List<BasicInfo> resultList = new ArrayList<BasicInfo>();
		resultList.clear();
		List<?> list=null;
		try {
			list = basicInfoAdm.getAcctListData(queryMap, param);
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					BasicInfo bas = new BasicInfo();
					Object[] obj = (Object[]) list.get(i);
					for (int j = 0; j < obj.length; j++) {
						switch (j) {
						case 0:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								bas.setAutoId( Long.parseLong(obj[j]
										.toString())) ;
							}
							break;
						case 1:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								bas.setAccNo(obj[j].toString());
							}
							break;
						case 2:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								bas.setAccSon(obj[j].toString());
							}
							break;
						case 3:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								bas.setIdBank(obj[j].toString());
							}
							break;
						case 4:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								bas.setAccName(obj[j].toString());
							}
							break;
						case 5:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								bas.setSealAccNo(obj[j].toString());
							}
							break;
						case 6:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								bas.setCurrency(obj[j].toString());
							}
							break;
						case 7:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								bas.setAddress(obj[j].toString());
							}
							break;
						case 8:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								bas.setZip(obj[j].toString());
							}
							break;
						case 9:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								bas.setLinkMan(obj[j].toString());
							}
							break;
						case 10:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								bas.setPhone(obj[j].toString());
							}
							break;
						case 11:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								bas.setSendMode(obj[j].toString());
							}
							break;
						case 12:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								bas.setOpenDate(obj[j].toString());
							}
							break;
						case 13:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								bas.setAccType(obj[j].toString());
							}
							break;
						case 14:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								bas.setCustId(obj[j].toString());
							}
							break;
						case 15:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								bas.setAccState(obj[j].toString());
							}
							break;
						case 16:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								bas.setIdBranch(obj[j].toString());
							}
							break;
						case 17:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								bas.setIdCenter(obj[j].toString());
							}
							break;
						case 18:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								bas.setBankName(obj[j].toString());
							}
							break;
						case 19:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								bas.setFaceFlag(obj[j].toString());
							}
							break;
						case 20:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								bas.setSpecialFlag(obj[j].toString());
							}
							break;
						case 21:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								bas.setAccCycle(obj[j].toString());
							}
							break;
						case 22:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								bas.setSubjectNo(obj[j].toString());
							}
							break;
						case 23:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								bas.setProductNo(obj[j].toString());
							}
							break;
						case 24:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								bas.setProductDesc(obj[j].toString());
							}
							break;
						case 25:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								bas.setSignFlag(obj[j].toString());
							}
							break;
						case 26:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								bas.setSignTime(obj[j].toString());
							}
							break;
						case 27:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								bas.setSignOpCode(obj[j].toString());
							}
							break;
						case 28:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								bas.setSignContractNo(obj[j].toString());
							}
							break;
						case 29:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								bas.setSealMode(obj[j].toString());
							}
							break;
						case 30:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								bas.setSingleAccno(obj[j].toString());
							}
							break;
						case 31:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								bas.setIsSpecile(obj[j].toString());
							}
							break;
						case 32:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								bas.setIsCheck(obj[j].toString());
							}
							break;
						case 33:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								bas.setDistributary(obj[j].toString());
							}
							break;
						case 34:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								bas.setSendAddress(obj[j].toString());
							}
							break;
						}
					}
					resultList.add(bas);
				}
			}
		} catch (XDocProcException e1) {
			// TODO: handle exception
			throw new XDocProcException(e1.getMessage());
		}
	return resultList;
	}
	
	/***
	 * 查询所有账户
	 * 
	 * @return 账户信息列表
	 */
	public List<BasicInfo> getAllBasicInfo(Map<String, String> queryMap)
			throws XDocProcException {
		return basicInfoAdm.getAllBasicInfo(queryMap);
	}
	
	/***
	 * 查询所有网银账户
	 * 
	 * @return 账户信息列表
	 */
	public List<BasicInfo> getAllAcctList(Map<String, String> queryMap)
			throws XDocProcException {
		List<BasicInfo> resultList = new ArrayList<BasicInfo>();
		resultList.clear();
		List<?> list=null;
		try {
			list = basicInfoAdm.getAllAcctList(queryMap);
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					BasicInfo bas = new BasicInfo();
					Object[] obj = (Object[]) list.get(i);
					for (int j = 0; j < obj.length; j++) {
						switch (j) {
						case 0:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								bas.setAutoId( Long.parseLong(obj[j]
										.toString())) ;
							}
							break;
						case 1:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								bas.setAccNo(obj[j].toString());
							}
							break;
						case 2:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								bas.setAccSon(obj[j].toString());
							}
							break;
						case 3:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								bas.setIdBank(obj[j].toString());
							}
							break;
						case 4:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								bas.setAccName(obj[j].toString());
							}
							break;
						case 5:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								bas.setSealAccNo(obj[j].toString());
							}
							break;
						case 6:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								bas.setCurrency(obj[j].toString());
							}
							break;
						case 7:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								bas.setAddress(obj[j].toString());
							}
							break;
						case 8:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								bas.setZip(obj[j].toString());
							}
							break;
						case 9:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								bas.setLinkMan(obj[j].toString());
							}
							break;
						case 10:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								bas.setPhone(obj[j].toString());
							}
							break;
						case 11:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								bas.setSendMode(obj[j].toString());
							}
							break;
						case 12:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								bas.setOpenDate(obj[j].toString());
							}
							break;
						case 13:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								bas.setAccType(obj[j].toString());
							}
							break;
						case 14:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								bas.setCustId(obj[j].toString());
							}
							break;
						case 15:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								bas.setAccState(obj[j].toString());
							}
							break;
						case 16:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								bas.setIdBranch(obj[j].toString());
							}
							break;
						case 17:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								bas.setIdCenter(obj[j].toString());
							}
							break;
						case 18:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								bas.setBankName(obj[j].toString());
							}
							break;
						case 19:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								bas.setFaceFlag(obj[j].toString());
							}
							break;
						case 20:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								bas.setSpecialFlag(obj[j].toString());
							}
							break;
						case 21:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								bas.setAccCycle(obj[j].toString());
							}
							break;
						case 22:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								bas.setSubjectNo(obj[j].toString());
							}
							break;
						case 23:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								bas.setProductNo(obj[j].toString());
							}
							break;
						case 24:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								bas.setProductDesc(obj[j].toString());
							}
							break;
						case 25:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								bas.setSignFlag(obj[j].toString());
							}
							break;
						case 26:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								bas.setSignTime(obj[j].toString());
							}
							break;
						case 27:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								bas.setSignOpCode(obj[j].toString());
							}
							break;
						case 28:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								bas.setSignContractNo(obj[j].toString());
							}
							break;
						case 29:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								bas.setSealMode(obj[j].toString());
							}
							break;
						case 30:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								bas.setSingleAccno(obj[j].toString());
							}
							break;
						case 31:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								bas.setIsSpecile(obj[j].toString());
							}
							break;
						case 32:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								bas.setIsCheck(obj[j].toString());
							}
							break;
						case 33:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								bas.setDistributary(obj[j].toString());
							}
							break;
						case 34:
							if (obj[j] != null
									&& obj[j].toString().trim().length() > 0) {
								bas.setSendAddress(obj[j].toString());
							}
							break;
						}
					}
					resultList.add(bas);
				}
			}
		} catch (XDocProcException e1) {
			// TODO: handle exception
			throw new XDocProcException(e1.getMessage());
		}
	return resultList;
	}

	public IBasicInfoAdm getBasicInfoAdm() {
		return basicInfoAdm;
	}

	public void setBasicInfoAdm(IBasicInfoAdm basicInfoAdm) {
		this.basicInfoAdm = basicInfoAdm;
	}

	public IBasicInfoLogAdm getBasicInfoLogAdm() {
		return basicInfoLogAdm;
	}

	public void setBasicInfoLogAdm(IBasicInfoLogAdm basicInfoLogAdm) {
		this.basicInfoLogAdm = basicInfoLogAdm;
	}

	public INewSealAdm getNewSealAdm() {
		return newSealAdm;
	}

	public void setNewSealAdm(INewSealAdm newSealAdm) {
		this.newSealAdm = newSealAdm;
	}

	public IPublicTools getTools() {
		return tools;
	}

	public void setTools(IPublicTools tools) {
		this.tools = tools;
	}

	

}
