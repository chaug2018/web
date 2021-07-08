package com.yzj.ebs.edata.service;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

import com.yzj.common.file.service.FileOperatorService;
import com.yzj.common.file.service.IFileOperator;
import com.yzj.ebs.common.BankParam;
import com.yzj.ebs.common.IPublicTools;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.edata.action.EDataAction;
import com.yzj.ebs.edata.bean.BillBean;
import com.yzj.ebs.edata.dao.impl.WFDataDao;
import com.yzj.ebs.util.FinalConstant;
import com.yzj.wf.common.WFLogger;
import com.yzj.wf.common.util.SIDCreator;
import com.yzj.wf.core.model.po.OrgType;
import com.yzj.wf.core.model.po.OrganizeInfo;
import com.yzj.wf.core.model.po.PeopleInfo;
import com.yzj.wf.core.model.po.common.PODefine;
import com.yzj.wf.core.service.po.IOrganizeInfoAdm;
import com.yzj.wf.core.service.po.IPeopleInfoAdm;

/**
 * 创建于:20101-01<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 人员机构同步到wf框架中公用方法、连接sftp下载文件
 * 
 * @author Lif
 * @version 1.0.0
 */
public class DataProcessUtil {
	private static WFLogger logger = WFLogger.getLogger(DataProcessUtil.class);
	/***
	 * 湘江银行机构类型ID
	 */
	public static final String orgTypeLevel1 = "65A2D56EDB3E4C14AF8858AF2C80188C";
	/***
	 * 总行机构类型ID
	 */
	public static final String orgTypeLevel2 = "D51E99BE443892600D643F09F63F_001";
	/***
	 * 清算中心机构类型ID
	 */
	public static final String orgTypeLevel3 = "DB009F5340209EF5DDEE83AEC206_002";
	/***
	 * 支行机构类型ID
	 */
	public static final String orgTypeLevel4 = "49E4BD36442189FAA2EF4CC1EF2F_004";
	
	public String ftpFilePath = "edata";
	


	/**
	 * 将核心给出的机构和人员信息放入wf框架中
	 * 
	 * @param tableName
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public void insertOrgAndPerpleToWf(IDataProcessService dataProcess,
			IPublicTools tools, IOrganizeInfoAdm biz,
			IPeopleInfoAdm peopleInfoAdm, String tableName,WFDataDao wfDataDao) {
		try {
			//先把机构信息导入到wf框架中
			if ("EBS_UTBLBRCD".equals(tableName)) {
				//机构迁移到正式表
				boolean b = dataProcess.processParamBankData();
				if (b) {
					Map<String, BankParam> bankParam=tools.getAllBankParam();
					//得到按照级别排序的BankParam List
					List<BankParam> bankParamList=tools.getBankParamList();
					List<OrganizeInfo> list = new ArrayList<OrganizeInfo>();
					for (BankParam bank :bankParamList) {
						
						OrganizeInfo o = new OrganizeInfo();
						o.setOrgNo(bank.getIdBank());
						o.setOrgName(bank.getName());
						o.setOrgLevel(bank.getLevel().intValue() - 1);
						// Level=1 虚拟机构上级为空，不然在wf框架中会出现死循环
						if (bank.getLevel().intValue() == 1) {
							o.setParentOrg("");
						} else {
							o.setParentOrg(bank.getIdBranch());
						}
						o.setSid(bank.getOrgSid());
//						if (bank.getLevel().intValue() == 1) {
//							o.setSid(PODefine.ROOTORGANIZESID);
//							// 更新param_bank orgsid=PODefine.ROOTORGANIZESID
//							dataProcess.insertOrUpdateDate("update param_bank set orgsid='"
//											+ PODefine.ROOTORGANIZESID + "' where nlevel=1");
//						} else {
//							o.setSid(bank.getOrgSid());
//						}
						OrgType orgType = new OrgType();
						orgType.setOrgTypeState((short) 0);
						// 这里改成配置性 机构类型: 1:湘江银行 2:总行 3:清算中心 4:支行
						if (0 == o.getOrgLevel()) {
							orgType.setSid(orgTypeLevel1);
						} else if (1 == o.getOrgLevel()) {
							orgType.setSid(orgTypeLevel2);
						} else if (2 == o.getOrgLevel()) {
							orgType.setSid(orgTypeLevel3);
						} else if (3 == o.getOrgLevel()) {
							orgType.setSid(orgTypeLevel4);
						}
						o.setOrgState((short) 0);
						o.setOrgType(orgType.getSid());
						list.add(o);
					}
					
					//得到数据库存在的所有的机构信息
					List<OrganizeInfo> allOrgs = biz.getAllOrganizeInfos(true);
					Map<String, OrganizeInfo> orgsMap = new HashMap<String, OrganizeInfo>();
					for (OrganizeInfo organizeInfo : allOrgs) {
						orgsMap.put(organizeInfo.getOrgNo(), organizeInfo);
					}
					for (int i = 0; i < list.size(); i++) {
						OrganizeInfo parent = list.get(i);
						//如果是支行的话，直接跳出循环，支行没有子机构
						if (parent.getOrgLevel() == 2) {
							continue;
						}
						String parentOrgNo = parent.getOrgNo();
						
						if(orgsMap.get(parentOrgNo)==null){
							if (parent.getParentOrg() == null|| "".equals(parent.getParentOrg())) {
								OrganizeInfo orgInfo = biz.findOrganizeInfoByOrgNo(FinalConstant.ROOTORG, false);
								orgInfo.setOrgNo(parent.getOrgNo());
								orgInfo.setOrgLevel(0);
								orgInfo.setOrgName(parent.getOrgName());
								biz.saveOrUpdate(orgInfo);
							}
						}
						
						for (int j = 0; j < list.size(); j++) {
							OrganizeInfo child = list.get(j);
							if (parentOrgNo.equals(child.getParentOrg())) {
								child.setParentOrg(parentOrgNo);
								//如果在机构表中不存在这个机构
								if (orgsMap.get(child.getOrgNo()) == null) {
									//查询这个机构的父机构在机构表中是否存在
									OrganizeInfo os = biz.findOrganizeInfoByOrgNo(parentOrgNo, false);
									//如果父机构不存在,直接跳出本次循环
									if (os == null) {
										continue;
									} else {
										child.setOrgPath(this.getOrgPath(child.getOrgNo(), bankParam));
										biz.create(child);
									}
								} else {
									//如果存在，则更新机构信息
									OrganizeInfo os = orgsMap.get(child.getOrgNo());
									os.setOrgLevel(child.getOrgLevel());
									//os.setOrgName(child.getOrgName());  --2期改造  不更新机构名称 因机构名称很不很管理 手动管理
									os.setParentOrg(child.getParentOrg());
									os.setOrgPath(this.getOrgPath(child.getOrgNo(), bankParam));
									biz.saveOrUpdate(os);
								}
							}
						}
					}
					logger.info(EDataAction.MSGINFO);
				} else {
					logger.info("机构导入到正式表中出现异常");
					
				}
				String updateParamBankName = "merge into autek.param_bank b using (select orgno,orgname from infotech.po_organizeinfo ) o "
											+ " on (b.idbank=o.orgno) when matched then update set b.cname=o.orgname";
				wfDataDao.insertOrUpdateDate(updateParamBankName);
			}
			// 如果是人员信息，就单独发请求去后台调用wf封装的接口添加到wf框架中
			if ("EBS_KUB_USER".equals(tableName)) {
				List<BillBean> userList = dataProcess.queryUsers();
				if (userList != null && userList.size() > 0) {
					Map<String, OrganizeInfo> tempMap = new HashMap<String, OrganizeInfo>();
					for (Iterator iterator = userList.iterator(); iterator
							.hasNext();) {
						BillBean billBean = (BillBean) iterator.next();
						PeopleInfo people = new PeopleInfo();
						// 人员代码，人员名称，所属机构
						people.setPeopleCode(billBean.getPeoplecode());
						people.setPeopleName(billBean.getPeoplename());
						people.setPeopleGender(new Byte(billBean.getSex()));

						// 封装用户信息
						String userSid = SIDCreator.getUUID();
						// 用户性别，密码，状态，默认桌面先暂时设置初始化值
						people.setPwd(new Md5PasswordEncoder().encodePassword(
								PODefine.USER_INITPWD, userSid));
						people.setPeopleState(PODefine.STATE_NORMAL);
						people.setPeopleGender(people.getPeopleGender());
						people.setDefaultDesktop((byte) 0);
						people.setSid(userSid);
						// 封装机构信息
						OrganizeInfo oinfo = null;
						oinfo = tempMap.get(billBean.getOrgno());
						if (oinfo == null) {
							oinfo = biz.findOrganizeInfoByOrgNo(
									billBean.getOrgno(), false);
						}
						if (oinfo != null) {
							tempMap.put(billBean.getOrgno(), oinfo);
							people.setOrganizeInfo(oinfo.getSid());
							List<PeopleInfo> peoList = peopleInfoAdm
									.findPeopleInfoByPeopleCode(
											billBean.getPeoplecode(), false);
							// 添加人员时要保证机构信息存在和人员不存在情况下才插数据到wf人员信息表中
							if (peoList == null) {
								peopleInfoAdm.create(people);
							} else {
								for (Iterator iterator2 = peoList.iterator(); iterator2
										.hasNext();) {
									PeopleInfo peopleInfo = (PeopleInfo) iterator2
											.next();
									
									//2期改造 将机构发生变更的人员对应岗位删除
									if(!people.getOrganizeInfo().equals(peopleInfo.getOrganizeInfo())){
										//如果机构不一致，则删除人员的岗位信息
										wfDataDao.insertOrUpdateDate("delete from am_rolegrouppeople t where t.peoplesid='"+peopleInfo.getSid()+"'");
									}
									
									peopleInfo.setPeopleName(people
											.getPeopleName());
									peopleInfo.setOrganizeInfo(people
											.getOrganizeInfo());
									peopleInfoAdm.update(peopleInfo);
								}
							}
						}
					}
					logger.info("人员迁移到到wf框架中完成");
				}else{
					logger.info("没有查询到需要迁移的人员信息");
				}
			}
		} catch (Exception e) {
			logger.error("柜员机构信息迁移到wf框架中发生异常",e);
		}
	}

	/**
	 * 找出机构树，结构为|上级|中级|下级|
	 * 
	 * @return
	 */
	public String getOrgPath(String org, Map<String, BankParam> bankParam) {
		BankParam subBankParam;
		String path = "|" + org + "|";
		// 找出本机构的机构信息
		subBankParam = bankParam.get(org);
		// Bank_Param表中 1虚拟机构，2总行，3分行，4网点
		if (subBankParam.getLevel() == 1) {
		}
		if (subBankParam.getLevel() == 2) {
			path = "|" + subBankParam.getIdBranch() + path;
		}
		if (subBankParam.getLevel() == 3) {
			path = "|" + subBankParam.getIdBranch() + path;
			subBankParam = bankParam.get(subBankParam.getIdBranch());
			path = "|" + subBankParam.getIdBranch() + path;
		}
		if (subBankParam.getLevel() == 4) {
			path = "|" + subBankParam.getIdBranch() + path;
			subBankParam = bankParam.get(subBankParam.getIdBranch());
			path = "|" + subBankParam.getIdBranch() + path;
			subBankParam = bankParam.get(subBankParam.getIdBranch());
			path = "|" + subBankParam.getIdBranch() + path;
		}
		return path;
	}

	/**
	 * 去sftp服务上拿数据文件
	 * 
	 * @return
	 */
	public boolean ftpDownload(IPublicTools tools, String dataDate) {
		boolean b = true;
		String sftpIp = null;
		String fname = null;
		IFileOperator fo = new FileOperatorService();
		InputStream in = null;
		try {
			sftpIp = tools.getSysbaseParam("SFTP");
			String filepath = ftpFilePath + "\\";
			if (createFile(filepath, dataDate)) {
				// 去param_idcenter表中获取ftp路径，用户名密码
				// IFileOperator fo = new FileOperatorService();
				// InputStream in = null;
				for (int i = 0; i < FinalConstant.FILES_NAME.length; i++) {
					// 从sftp服务器中下载文件
					fname = FinalConstant.FILES_NAME[i] + ".txt";
					in = fo.get(sftpIp + "/" + dataDate + "/" + FinalConstant.FILES_NAME[i]
							+ ".txt");
					// 放到本地这个目录下
					fo.put(in, filepath + "/" + dataDate + "/" + FinalConstant.FILES_NAME[i]
							+ ".txt");
					fo.closeInputStream(in);
				}
			}
		} catch (Exception e) {
			fo.closeInputStream(in);
			logger.error("文件不存在或连接ftp异常！" + sftpIp + "/" + dataDate + "/"
					+ fname);
			b = false;
		}
		return b;
	}

	public String getSftpPath(IPublicTools tools) {
		String sftpIp = null;
		try {
			sftpIp = tools.getSysbaseParam("SFTP");
		} catch (XDocProcException e) {
			logger.error("获取sftp路径失败");
			e.printStackTrace();
		}
		return sftpIp;
	}
	
	/**
	 * 从sftp 中下载的文件放到本目录下
	 * 
	 * @param filePath
	 * @param dataDate
	 * @return
	 */
	public static boolean createFile(String filePath, String dataDate) {
		try {
			File file = new File(filePath + "\\" + dataDate);
			if (!file.exists()) {
				file.mkdirs();
			}
			return true;
		} catch (Exception e) {
			logger.error("本地文件夹创建异常！", e);
			return false;
		}
	}

	/**
	 * 每天导入数据文件后要删除这个文件夹 根据路径删除指定的目录或文件，无论存在与否
	 * 
	 * @param dataDate
	 *            要删除的目录或文件
	 * @return 删除成功返回 true，否则返回 false。
	 */
	public boolean DeleteFolder(String dataDate) {
		boolean flag = false;
		String path = ftpFilePath + "\\" + dataDate.replace("-", "");
		File file = new File(path);
		// 判断目录或文件是否存在
		if (!file.exists()) { // 不存在返回 false
			return flag;
		} else {
			// 判断是否为文件
			if (file.isFile()) { // 为文件时调用删除文件方法
				return deleteFile(path);
			} else { // 为目录时调用删除目录方法
				return deleteDirectory(path);
			}
		}
	}

	/**
	 * 删除单个文件
	 * 
	 * @param sPath
	 *            被删除文件的文件名
	 * @return 单个文件删除成功返回true，否则返回false
	 */
	public boolean deleteFile(String sPath) {
		boolean flag = false;
		File file = new File(sPath);
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists()) {
			file.delete();
			flag = true;
		}
		return flag;
	}

	/**
	 * 删除目录（文件夹）以及目录下的文件
	 * 
	 * @param sPath
	 *            被删除目录的文件路径
	 * @return 目录删除成功返回true，否则返回false
	 */
	public boolean deleteDirectory(String sPath) {
		boolean flag;
		// 如果sPath不以文件分隔符结尾，自动添加文件分隔符
		if (!sPath.endsWith(File.separator)) {
			sPath = sPath + File.separator;
		}
		File dirFile = new File(sPath);
		// 如果dir对应的文件不存在，或者不是一个目录，则退出
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return false;
		}
		flag = true;
		// 删除文件夹下的所有文件(包括子目录)
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除子文件
			if (files[i].isFile()) {
				flag = deleteFile(files[i].getAbsolutePath());
				if (!flag)
					break;
			} // 删除子目录
			else {
				flag = deleteDirectory(files[i].getAbsolutePath());
				if (!flag)
					break;
			}
		}
		if (!flag)
			return false;
		// 删除当前目录
		if (dirFile.delete()) {
			return true;
		} else {
			return false;
		}
	}
	
}