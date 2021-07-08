/**
 * 创建于:2013-9-20
 * 版权所有(C) 2013 深圳市银之杰科技股份有限公司
 * @author lixiangjun
 * @version 1.1
 */
package com.yzj.ebs.edata.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import com.infotech.publiclib.Exception.DaoException;
import com.yzj.ebs.common.IPublicTools;
import com.yzj.ebs.common.OperLogModuleDefine;
import com.yzj.ebs.edata.action.EDataAction;
import com.yzj.ebs.edata.service.DataProcessUtil;
import com.yzj.ebs.util.FinalConstant;

public class EdataServiceAuto {
	private DataCheckAuto dataCheck;
	
	private DataImportAuto dataImport;
	
	private IPublicTools tools;
	
	private DataProcessServiceImpl dataProcess; // 数据处理服务

	public DataProcessUtil dataUtil = new DataProcessUtil();
	
	//区分账户最大发生额和累计发生额不存在的标示 0.0001
	private String nullCreditFlag="0.0001";
	
	
	// 催收处理
	public void rushstateProcess() {
		try {
			String rushdate = tools.getSysbaseParam("RUSHDATE"); // 催收天数
			String sql ="update ebs_checkmaindata set rushstate='1' where ( docstate = '1' or docstate='2' or docstate= '4' or docstate = '5' ) and "
			+ "docdate<= to_char(sysdate-"+ Integer.parseInt(rushdate) + ",'yyyymmdd' )";
			dataProcess.insertOrUpdateDate(sql);
		} catch (Exception e) {
			e.printStackTrace();
			tools.getDataLogUtils().error("更新催收状态发生异常！",String.valueOf(OperLogModuleDefine.rushProcess));
		}
	}
	
	
	
	
	/**                           start      湘江银行数据处理方法                 start                                   **/
	
	/**
	 * 校验数据文件的正确性，主要数据文件个数是否完全，以及文件字段的个数是否正确
	 * @return
	 */
	public String checkDataFile(String dataDate){
		String result ="success";
		BufferedReader read =null;
		try {
			String uploadDir =findSysbaseParam("DATAFILEDIR");
			String okFlag=findSysbaseParam("OK");
			String year=dataDate.substring(0,4);
			String mouth=dataDate.substring(4,6);
			String day=dataDate.substring(6,8);
			
			String path =uploadDir+"/"+year+"/"+mouth+"/"+day;
			File okFile=new File(path+"/"+okFlag);
			//查看文件是否存在
			if(okFile.exists()){
				String splitchar =dataProcess.findSysbaseParam("SPLITCHAR");
				if(splitchar!=null&&!splitchar.equals("")){
					for (int i = 0; i < FinalConstant.FILES_NAME.length; i++) {
						File file = new File(path+"/"+FinalConstant.FILES_NAME[i]+".dat");
						
						int paramNum=FinalConstant.FILES_SIZE[i];
						if(file.exists()){
							read = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
							String strTemp="";
							int num=0;
							while((strTemp = read.readLine())!=null){
								if((strTemp.trim()).length()>0){
									num++;
									//读取一条数据
									List<String> dataStr = dataProcess.analyzeDate(strTemp,splitchar,paramNum);
									if(dataStr.size()!=FinalConstant.FILES_SIZE[i]){
										return FinalConstant.FILES_NAME[i]+".dat文件的数据第"+num+"行数据验证失败";
									}
								}
							}
						}
					}
					return result;
				}else{
					return "数据分隔符未配置,请在\"系统参数管理\"模块中检测SPLITCHAR(数据分隔符)是否配置";
				}
			}else{
				return "数据文件不完整或不存在，请确定数据文件的完整性";
			}
		} catch (Exception e) {
			return "数据校验异常";
		}finally{
			try {
				if(read!=null){
					read.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
	/**
	 * 将接口文件中的数据导入到系统中的临时表中去
	 * 分隔符 str.split("");
	 * 后期如果涉及到币种转换的话，在接口表中对币种进行转换为中文
	 * dataDate 的格式为 20130820
	 * @param dataDate
	 */
	public void dataImport(String dataDate)throws Exception{
		try {
			String uploadDir =findSysbaseParam("DATAFILEDIR");
			String okFlag=findSysbaseParam("OK");
			String year=dataDate.substring(0,4);
			String mouth=dataDate.substring(4,6);
			String day=dataDate.substring(6,8);
			
			String path =uploadDir+"/"+year+"/"+mouth+"/"+day;
			
			//每月一号删除三个月之前的数据文件
			if(day.equals("01")){
				deleteDataFile(uploadDir);
			}
			
			File okFile=new File(path+"/"+okFlag);
			//查看文件是否存在
			if(okFile.exists()){
				clearTempData(dataDate);
				tools.getDataLogUtils().info(OperLogModuleDefine.DATAPROFLAG,String.valueOf(OperLogModuleDefine.dataprocess),dataDate);
				//导数之前，清空接口表的数据
				//数据分隔符
				String splitchar =dataProcess.findSysbaseParam("SPLITCHAR");
				if(splitchar!=null&&!splitchar.equals("")){
					for (int i = 0; i < FinalConstant.FILES_NAME.length; i++) {
						int paramNum=FinalConstant.FILES_SIZE[i];
						File file = new File(path+"/"+FinalConstant.FILES_NAME[i]+".dat");
						if(file.exists()){
							long startLine = 0;
							//查看OK文件中是否记录的数据文件已经导入的行数，此功能为了数据的续传
							BufferedReader read = new BufferedReader(new InputStreamReader(new FileInputStream(okFile)));
							String strTemp="";
							while((strTemp = read.readLine())!=null){
								String[] temp = strTemp.split("=");
								if(temp[0].equals("EBS_"+FinalConstant.FILES_NAME[i])){
									startLine = Long.parseLong(temp[1]);
									break;
								}
							}
							read.close();
							dataImport.insertToOracle(dataDate,path+"/", FinalConstant.FILES_NAME[i], startLine,splitchar,paramNum,okFlag);
						}
					}
					//更新参数表，提示今天的数据已经处导入过了
					getDataProcess().insertOrUpdateDate("update param_sysbase set sysbasevalue ='"+dataDate+"' where sysbaseid ='IMPORTDATE'");
					EDataAction.MSGINFO="数据日期："+dataDate+", 信息：数据导入完成,导入日期为："+dataDate;
					tools.getDataLogUtils().infoUpdate(OperLogModuleDefine.DATAPROFLAG,OperLogModuleDefine.DATAIMPSUCCESS,null,String.valueOf(OperLogModuleDefine.dataprocess),dataDate);
				}
				else{
					tools.getDataLogUtils().infoUpdate(OperLogModuleDefine.DATAPROFLAG,"数据分隔符未配置",null,String.valueOf(OperLogModuleDefine.dataprocess),dataDate);
				}
			}else{
				tools.getDataLogUtils().info("数据日期："+dataDate+", 信息：日期为"+dataDate+"的数据文件未给全或不存在！",String.valueOf(OperLogModuleDefine.dataprocess),dataDate);
			}
		} catch (Exception e) {
			tools.getDataLogUtils().infoUpdate(OperLogModuleDefine.DATAPROFLAG,"数据日期："+dataDate+", 信息：数据导入异常",e,String.valueOf(OperLogModuleDefine.dataprocess),dataDate);
			throw new Exception("数据导入失败，" + e.getMessage());
		}
	}
	
	/**
	 * 处理机构人员信息的数据
	 */
	public void disposeUserAndOrg(){
		dataImport.insertOrgAndPerpleToWf("EBS_UTBLBRCD");// 转移机构信息及人员信息至WF
		dataImport.insertOrgAndPerpleToWf("EBS_KUB_USER");
	}
	
	
	/**
	 * 清空临时接口表数据
	 * @throws DaoException 
	 */
	public void clearTempData(String dataDate) throws Exception{
		try {
			//机构
			String sql ="truncate table EBS_UTBLBRCD";
			dataProcess.insertOrUpdateDate(sql);
			
			//人员
			sql ="truncate table EBS_KUB_USER";
			dataProcess.insertOrUpdateDate(sql);
			
			//内部账户信息
			sql ="truncate table EBS_INNERBASICINFO ";
			dataProcess.insertOrUpdateDate(sql);
			
			//账户信息
			sql="truncate table EBS_MAINDATA";
			dataProcess.insertOrUpdateDate(sql);
			
			//协定存款账户表
			sql ="truncate table EBS_XDCKLIST";
			dataProcess.insertOrUpdateDate(sql);
			
			//网银
			sql ="truncate table EBS_ACCTLIST";
			dataProcess.insertOrUpdateDate(sql);
			
			//汇率
			sql="truncate table EBS_KNP_EXRT";
			dataProcess.insertOrUpdateDate(sql);
			
			//发生额明细
			sql ="truncate table EBS_DEPHIST";
			dataProcess.insertOrUpdateDate(sql);
			
			//--2期改造添加修改修改定期子账户账号
			sql ="truncate table EBS_NETACRELATION";
			dataProcess.insertOrUpdateDate(sql);
			sql ="truncate table EBS_AAPFZ0";
			dataProcess.insertOrUpdateDate(sql);
			
			//内部账户交易明细明细
			sql ="truncate table EBS_INNERACCNODETAIL";
			dataProcess.insertOrUpdateDate(sql);
			
			sql="delete from ebs_basicinfolog where opdate='"+dataDate+"' and chnopmode='数据处理' and opcode='AUTO' ";
			dataProcess.insertOrUpdateDate(sql);
			
			EDataAction.MSGINFO="数据日期："+dataDate+", 信息："+dataDate+", 清空临时接口表数据完成";
		} catch (Exception e) {
			EDataAction.MSGINFO="数据日期："+dataDate+", 信息："+dataDate+", 清空临时接口表数据异常";
			throw new Exception(e.getMessage(), e);
		}finally{
			//关闭数据处理的连接
			dataProcess.EdataDao.closeConnProcess();
		}
	}
	
	/**
	 * 根据日期删除三个月之前数据文件
	 * @param uploadDir 数据文件的根目录
	 */
	public void deleteDataFile(String uploadDir) throws Exception{
		try {
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMM");
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.set(Calendar.MONTH,cal.get(Calendar.MONTH)-4);
			Date dateFlag = cal.getTime();
			File fileDir = new File(uploadDir);
			if(fileDir.isDirectory()){
				//得到所有的年目录
				File[] yearFiles =fileDir.listFiles();
				for(File yearFile:yearFiles){
					String year=yearFile.getName();
					if(year.length()==4){
						if(yearFile.isDirectory()){
							//得到年目录下的所有的月
							File[] mouthFiles =yearFile.listFiles();
							//如果年目录中没有文件，则删除这个年目录
							if(mouthFiles.length==0){
								yearFile.delete();
								continue;
							}
							for(File mouthFile:mouthFiles){
								String day =mouthFile.getName();
								if(day.length()==2){
									Date fileDate = sf.parse(year+day);
									if(fileDate.getTime()<dateFlag.getTime()){
										//删除目录
										tools.deleteFile(mouthFile);
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			tools.getDataLogUtils().error("删除处理完成的数据文件异常",e,String.valueOf(OperLogModuleDefine.dataprocess));
			throw new Exception(e.getMessage(), e);
		}
	}

	/**
	 * 数据处理业务逻辑
	 * @throws DaoException 
	 */
	public void dateProcess(String year,String month,String day,String ishbcheck) throws Exception {
		try {
			//对网银账号进行处理
//			procOnlineBankAccno(year+month+day);
			
			//根据发送方式，修改发送地址
			updateSendAddress(year+month+day);
			
			//如果发送方式为邮寄，则将发送地址修改为客户地址
			updateSendAddressByEMS(year+month+day);
			
			//将账户的余额信息更新到账户明细统计表(ebs_accnodetailcount)中
			updateAccCredit(year,month,year+month+day);
			
			//更新月累计和账户的账户类型的分类
			accCycleJudge(year,month,year+month+day);
			
			//将有发生额明细的企业网银的账户的对账周期设置为一类
			updateOnlineBankAcccycle(year,month,year+month+day);
			
			//如果发送方式为面对面，则将其账户类型修改为一类
			updateFaceToFaceAccno(year,month,year+month+day);
			
			//非人民币账户设置为一类账户
			updateNotCNYAccno(year, month, year+month+day);
			
			//更新账户信息表中的账户类型（特殊的不做更新）
			updateAccBusinessInfo(year,month,year+month+day);
			
			//在季点的时候，将余额大于(包含)200W的一类账户的进行面对面分流标识，并设置面对面对账的月份
			if(Integer.parseInt(month)%3==0){
				billDistributary(year,month,year+month+day);
			}
			
			//将参与对账的账户的数据迁移到临时对账单表中
			disposeNormalAccNo(Integer.parseInt(month),year+month+day);
			
			//将抽查的账户(操作员上传)的信息作为特殊面对面方式出单，转移到临时对账单ebs_accnomaindata中
			disposeSpecialFaceToFaceAccNo(year+month+day);
			
			//将分流的数据状态修改为正常
			updatedisFlag(Integer.parseInt(month),year+month+day);
			
			//根据面对面分流标识，将一类账户余额在200W以上的发送方式临时修改为面对面
			disposeOtherAccNo(year+month+day,ishbcheck);
			
			//临时将新开户的账户的账户类型修改为一类，发送方式修改为面对面
			updateNewAccnoInfo(year+month+day,ishbcheck);
			
			//对临时对账单进行分类并生成对账单编号（针对非网银账户）
			createTempBill(year+month+day,ishbcheck);
			
			//生成网银账号临时对账单编号
			creatTempNetBill(year+month+day,ishbcheck);
			
			//生成对账单
			createCheckmaindata(year+month+day,ishbcheck);
			
			//根据对账单中账户的发送地址信息反向更新账户信息表中对应的发送地址信息
			updateBasicInfoSendAddress(year+month+day,ishbcheck);
			
			//生成内部对账单，将内部账户对账单信息(ebs_innerbasicinfo)更新到对应的正式表中(ebs_inneraccnomaindata)
			updateInnerMaindata(year+month+day);
			
			
		} catch (Exception e) {
			throw new Exception(e.getMessage(), e);
		}finally{
			//关闭数据处理的连接
			dataProcess.EdataDao.closeConnProcess();
		}
	}
	
	
	/**
	 * 设置协定存款账户的科目号
	 * @throws DaoException 
	 */
	public void updateDxckAccno(String dataDate)throws Exception{
		try {
			String sql="merge into ebs_maindata b using (select accno from EBS_XDCKLIST) t " +
					"on(b.accno=t.accno) " +
					" when matched then  " +
					"update set b.subnoc='"+FinalConstant.XDCKSUB+"' ";
			dataProcess.insertOrUpdateDate(sql);
			EDataAction.MSGINFO="数据日期："+dataDate+", 信息：协定存款账户更新完成";
		} catch (Exception e) {
			EDataAction.MSGINFO="数据日期："+dataDate+", 信息：协定存款账户更新异常";
			tools.getDataLogUtils().infoUpdate(OperLogModuleDefine.DATAPROFLAG,EDataAction.MSGINFO,e,String.valueOf(OperLogModuleDefine.dataprocess),dataDate);
			throw new Exception(e.getMessage(), e);
		}
	}
	
	/**
	 * 账户信息转移到正式表
	 * @throws DaoException 
	 */
	public void accInfoFilter(String dataDate) throws Exception{
		
		try {
			/** 
			 * 第一步：将接口表EBS_MAINDATA中账户状态时正常的，并且对应科目号是参与对账的数据转移到正式账户信息表中去，如果正式账户信息表存在此条数据，则做更新，不存在，则新增。
			 */
			String sql ="merge into ebs_basicinfo b " +
					" using (select e.*,p.idbranch,p.idcenter from ebs_maindata e right join param_bank p on p.idbank =e.idbank right join param_subnoc s on substr(e.subnoc,1,3) = s.subnoc where e.accstate = '0' and e.datadate='"+dataDate+"' ) t " +
					" on (b.accno = t.accno) " +
					" when matched then " +
					" update set b.idbank =t.idbank,b.bankname = t.bankname,b.custid =t.custid,b.acctype=t.subnoc,b.accname=t.accname,b.opendate=t.opendate," +
						" b.currency=t.currtype,b.subjectno=substr(t.subnoc,1,3),b.accstate=t.accstate,b.idbranch=t.idbranch,b.idcenter=t.idcenter " +
					" when not matched then " +
					" insert values(BASICINFO_AUTOID.nextval,t.accno,'',t.idbank,t.accname,t.accno,t.currtype," +
						" t.address,t.zip,t.respon,t.phone,t.sendmode,t.opendate,t.subnoc,t.custid,t.accstate,t.idbranch,t.idcenter," +
						" t.bankname,'0','0','',substr(t.subnoc,1,3),t.prodtype,t.proddesc,'','','','','0','','0','0','0',t.address,'')";
			dataProcess.insertOrUpdateDate(sql);
			
			/**
			 * 第二步：	将接口表EBS_MAINDATA中账户状态正常或者不正常，但是在特殊账户表中存成的数据迁移到正式账户信息表中去，如果特殊表中的账户状态为未导入，则做导入操作，如果已存在，在做更新操作，
			 */
			
			sql="merge into ebs_basicinfo b " +
					" using (select p.idbranch,p.idcenter,e.accno,e.idbank,e.bankname,e.custid,e.acctype,e.accname,e.opendate,e.currtype,e.subnoc,e.accstate,e.zip,e.respon,e.phone," +
						" s.isimport,s.acccycle,s.sealmode,s.sendmode,e.prodtype,e.proddesc,e.address " +
						" from ebs_maindata e right join ebs_importspecile s on e.accno=s.accno right join param_bank p on p.idbank =e.idbank  where e.datadate='"+dataDate+"' ) t " +
					" on (b.accno = t.accno ) " +
					" when matched then " +
					" update set b.idbank =t.idbank,b.bankname = t.bankname,b.custid =t.custid,b.acctype=t.subnoc,b.accname=t.accname,b.opendate=t.opendate," +
						" b.currency=t.currtype,b.subjectno=substr(t.subnoc,1,3),b.accstate=t.accstate,b.idbranch=t.idbranch,b.idcenter=t.idcenter " +
					" when not matched then " +
					" insert values(BASICINFO_AUTOID.nextval,t.accno,'',t.idbank,t.accname,t.accno,t.currtype," +
						" t.address,t.zip,t.respon,t.phone,t.sendmode,t.opendate,t.subnoc,t.custid,t.accstate,t.idbranch,t.idcenter," +
						" t.bankname,'0','0',t.acccycle,substr(t.subnoc,1,3),t.prodtype,t.proddesc,'','','','',t.sealmode,'','1','0','0',t.address,'')";
			dataProcess.insertOrUpdateDate(sql);
			
			/**
			 * 第三步：在特殊账户表中将已导入的进入账户信息表的账户的状态修改为已导入
			 */
			sql="merge into ebs_importspecile e "+
					" using (select accno from ebs_basicinfo where isspecile = '1' ) info "+
					" on (info.accno=e.accno) "+
					" when matched then " +
					" update set e.isimport ='1' ";
			dataProcess.insertOrUpdateDate(sql);
			EDataAction.MSGINFO="数据日期："+dataDate+", 信息：账户信息转移到正式表完成";
		} catch (Exception e) {
			EDataAction.MSGINFO="数据日期："+dataDate+", 信息：账户信息转移到正式表异常";
			tools.getDataLogUtils().infoUpdate(OperLogModuleDefine.DATAPROFLAG,EDataAction.MSGINFO,e,String.valueOf(OperLogModuleDefine.dataprocess),dataDate);
			throw new Exception(e.getMessage(), e);
		}
	}
	
	/**
	 * 将内部账户对账单信息(ebs_innerbasicinfo)过滤更新到对应的正式表中(ebs_inneraccnomaindata)
	 * add 20151209 修改内部对账功能模块
	 * @throws DaoException 
	 */
	public void updateInnerMaindata(String dataDate) throws Exception{
		
		try {
			/** 
			 * 将接口表ebs_innerbasicinfo中账号在ebs_inneraccno存在的记录导入到目标对账表ebs_inneraccnomaindata中。
			 */
			String sql = "insert into ebs_inneraccnomaindata(autoId,accno,datadate,bal) " +
						 "select inneraccnomaindata_autoid.nextval,accno,"+dataDate+",bal from ebs_innerbasicinfo " +
						 "where accno in (select accno from ebs_inneraccno) ";
					
			dataProcess.insertOrUpdateDate(sql);
			
			//同步ebs_inneraccnomaindata 的 resultPeopleCode,reCheckPeopleCode,idCenter字段
			sql = "merge into ebs_inneraccnomaindata a using (select t1.insideaccno,t1.insideuser,t1.insiderecheckuser,t2.organizeinfo " 
				+ " from ebs_insideaccnoparam t1,infotech.po_peopleinfo t2 where t1.flog=0 and t2.peoplestate=0 and t1.insideuser=t2.peoplecode) t"
				+ " on ( a.accno=t.insideaccno) "
				+ " when matched then "
				+ " update set a.resultPeopleCode=t.insideuser,a.reCheckPeopleCode=t.insiderecheckuser,a.idCenter=t.organizeinfo"
				+ " where a.datadate='"+dataDate+"'";
			dataProcess.insertOrUpdateDate(sql);
			
			EDataAction.MSGINFO="数据日期："+dataDate+", 信息：内部账户对账单信息转移到正式表ebs_inneraccnomaindata完成";
		} catch (Exception e) {
			EDataAction.MSGINFO="数据日期："+dataDate+", 信息：内部账户对账单信息转移到正式表ebs_inneraccnomaindata完成";
			tools.getDataLogUtils().infoUpdate(OperLogModuleDefine.DATAPROFLAG,EDataAction.MSGINFO,e,String.valueOf(OperLogModuleDefine.dataprocess),dataDate);
			throw new Exception(e.getMessage(), e);
		}
	}
	
	/**
	 * 处理网银账号 
	 * @throws DaoException 
	 */
	public void procOnlineBankAccno(String dataDate) throws Exception{
		try {
			String sql="merge into ebs_basicinfo info using (select accno from ebs_acctlist group by accno) t "+
					" on ( info.accno=t.accno) "+
					" when matched then "+
					" update set info.sendmode ='"+FinalConstant.sendModelMap.get("网银")+"' where info.isspecile ='0' and  info.sendmode is null";
			dataProcess.insertOrUpdateDate(sql);
			EDataAction.MSGINFO="数据日期："+dataDate+", 信息：处理网银账号完成";
		} catch (Exception e) {
			EDataAction.MSGINFO="数据日期："+dataDate+", 信息：处理网银账号异常";
			tools.getDataLogUtils().infoUpdate(OperLogModuleDefine.DATAPROFLAG,EDataAction.MSGINFO,e,String.valueOf(OperLogModuleDefine.dataprocess),dataDate);
			throw new Exception(e.getMessage(), e);
		}
	}
	
	
	/**
	 * 根据发送方式，修改发送地址(只修改面对面和柜台)
	 * @throws DaoException 
	 */
	public void updateSendAddress(String dataDate) throws Exception{
		String[] obj = new String[]{"accno","sendmode","idcenter","idbranch","idbank"};
		String sql="select accno,sendmode,idbranch,idbranch,idbank from ebs_basicinfo " +
				" where  sendmode = '"+FinalConstant.sendModelMap.get("面对面")+"' or sendmode = '"+FinalConstant.sendModelMap.get("柜台")+"' ";
		List<Map<String,Object>>list = dataProcess.queryObjectList(obj,sql);
		for(Map<String,Object> m:list){
			String accno = (String) m.get("accno");
			String sendmode= (String)m.get("sendmode");
			String idbranch = (String)m.get("idbranch"); //分行
			String idbank = (String)m.get("idbank"); //网点
			String address = findAddressBySendMode(accno,idbank,idbranch,sendmode);
			if(address!=null && address!=""){
				sql ="update ebs_basicinfo set sendaddress='"+address+"' where accno= '"+accno+"' ";
				try {
					dataProcess.insertOrUpdateDate(sql);
				} catch (Exception e) {
					tools.getDataLogUtils().error("根据发送方式修改发送地址异常",String.valueOf(OperLogModuleDefine.dataprocess),dataDate);
					throw new Exception(e.getMessage(), e);
				}
			}
		}
	}
	
	
	/**
	 * 根据sendmode、idbranch得到对于的发送地址
	 * @throws DaoException 
	 * @param idbank 机构号
	 * @param idbranch 上级机构
	 * @param sendmode 发送方式
	 */
	public String findAddressBySendMode(String accNo,String idbank,String idbranch,String sendmode ){
		String result="";
		String 	sql ="select addressflug from ebs_faceflug where 1=1 ";
		if(idbranch!=null&&idbranch!=""){
			sql+=" and idcenter = '"+idbranch+"' ";
		}
		if(sendmode!=null&&sendmode!=""){
			sql+=" and sendmode = '"+sendmode+"' ";
		}
		List<Map<String,Object>> listTemp =  dataProcess.queryObjectList(new String[]{"addressflug"},sql);
		if(listTemp.size()>0){
			Map<String,Object> addressFlugMapTemp = listTemp.get(0);
			Object addressflugObj= addressFlugMapTemp.get("addressflug");
			String addressflug="";
			if(addressflugObj!=null){
				addressflug = (String)addressflugObj;
				if(addressflug.equals("1")){
					sql ="select address from param_bank  where idbank = '"+idbank+"' ";
				}else if(addressflug.equals("0")){
					sql ="select address from param_bank  where idbank = '"+idbranch +"' ";
				}
				List<Map<String,Object>> listAddress = dataProcess.queryObjectList(new String[]{"address"},sql);
				if(listAddress.size()>0){
					Map<String,Object> addressMapTemp = listAddress.get(0);
					Object addressObj= addressMapTemp.get("address");
					if(addressObj!=null){
						result = (String)addressObj;
					}
				}
			}
		}else{
			sql ="select address from ebs_basicinfo  where accno = '"+accNo +"' ";
			List<Map<String,Object>> listAddress = dataProcess.queryObjectList(new String[]{"address"},sql);
			if(listAddress.size()>0){
				Map<String,Object> addressMapTemp = listAddress.get(0);
				Object addressObj= addressMapTemp.get("address");
				if(addressObj!=null){
					result = (String)addressObj;
				}
			}
		}
		return result;
	}
	
	/**
	 * 如果账户的发送方式为邮寄，则将其发送地址修改为客户地址
	 * @throws Exception
	 */
	public void updateSendAddressByEMS(String dataDate) throws Exception{
		try {
			String sql="update ebs_basicinfo info set info.sendaddress=info.address where info.sendmode='"+FinalConstant.sendModelMap.get("邮寄")+"'  ";
			dataProcess.insertOrUpdateDate(sql);
			EDataAction.MSGINFO="数据日期："+dataDate+", 信息：将发送方式为邮寄的账户的发送地址修改为客户地址完成";
		} catch (Exception e) {
			EDataAction.MSGINFO="数据日期："+dataDate+", 信息：将发送方式为邮寄的账户的发送地址修改为客户地址异常";
			tools.getDataLogUtils().infoUpdate(OperLogModuleDefine.DATAPROFLAG,EDataAction.MSGINFO,e,String.valueOf(OperLogModuleDefine.dataprocess),dataDate);
			throw new Exception(e.getMessage(), e);
		}
	}
	
	
	/**
	 * 账户的余额新增或更新到账户明细统计表中 只更新在账户明细统计表存在的账户
	 * @param date 日期为 年月
	 * @throws DaoException 
	 */
	public void updateAccCredit(String year,String month,String dataDate) throws Exception{
		try {
			//将数据库存在的本月的账户明细统计数据的账户类型设置为空，用于重新对账户类型进行划分
			String date=year+month;
			String sql ="update ebs_accnodetailcount set acccycle ='' where docdate ='"+date+"' ";
			dataProcess.insertOrUpdateDate(sql);
			
			sql ="merge into ebs_accnodetailcount a using " +
					" (select t.accno,substr(e.subjectno,1,3) as subjectno ,cast(t.bal as float) as credit from ebs_maindata t inner join ebs_basicinfo e on t.accno = e.accno )  b " +
					" on (a.accno = b.accno and a.docdate ='"+date+"') " +
					" when matched then " +
					" update set a.credit = b.credit,a.totalamount_month=cast('"+nullCreditFlag+"' as float),a.maxamount_month=cast('"+nullCreditFlag+"' as float), a.subjectno = b.subjectno "+
					" when not matched then  " +
					" insert values (EBS_ACCNODETAILCOUNT_AUTOID.nextval, b.accno, b.subjectno, '"+date+"', b.credit,cast('"+nullCreditFlag+"' as float),cast('"+nullCreditFlag+"' as float),'')";
			dataProcess.insertOrUpdateDate(sql);
			
			sql="update ebs_accnodetailcount c set c.credit=cast('"+nullCreditFlag+"' as float) where c.credit=0";
			dataProcess.insertOrUpdateDate(sql);
			EDataAction.MSGINFO="数据日期："+dataDate+", 信息：账户余额信息更新完成";
		} catch (Exception e) {
			EDataAction.MSGINFO="数据日期："+dataDate+", 信息：账户余额信息更新异常";
			tools.getDataLogUtils().infoUpdate(OperLogModuleDefine.DATAPROFLAG,EDataAction.MSGINFO,e,String.valueOf(OperLogModuleDefine.dataprocess),dataDate);
			throw new Exception(e.getMessage(), e);
		}
	}
	
	/**
	 * 剔除计息交易明细
	 * @throws DaoException
	 */
	public void deleteAccrualDate(String dataDate)throws Exception{
		try{
			String sql ="delete from  ebs_dephist  where trace_code ='1997' "; 
			dataProcess.insertOrUpdateDate(sql);
			EDataAction.MSGINFO="数据日期："+dataDate+", 信息：剔除计息交易明细完成";
		} catch (Exception e) {
			EDataAction.MSGINFO="数据日期："+dataDate+", 信息：剔除计息交易明细异常";
			tools.getDataLogUtils().infoUpdate(OperLogModuleDefine.DATAPROFLAG,EDataAction.MSGINFO,e,String.valueOf(OperLogModuleDefine.dataprocess),dataDate);
			throw new Exception(e.getMessage(), e);
		}		
	}
	
	/**
	 * 清除账户信息表中的数据
	 * @param month
	 */
	public void deletebillDetailByMonth(String month)throws Exception{
		try{
			String sql ="delete from ebs_accnodetaildata_"+month+"  "; 
			dataProcess.insertOrUpdateDate(sql);
		} catch (Exception e) {
			throw new Exception(e.getMessage(), e);
		}		
	}
	
	
	/**
	 * 存款明细信息过滤
	 * date格式为：20130826
	 * @param date
	 * @throws DaoException 
	 */
	public void billDetailFilter(String dataDate) throws Exception{
		try {
			String index = dataDate.substring(4,6);
			/**
			 * 如果明细表存在导入日志为当前日期的数据，则将其删除
			 */
			String sql ="delete from ebs_accnodetaildata_"+index+" where datadate ='"+dataDate+"' ";
			dataProcess.insertOrUpdateDate(sql);
			
			/** 
			 * 在正式账户信息表中存在的参与对账的账户，将其存款明细从账户存款明细临时表迁移到对应的正式账户存款明细表中去，（存款明细一共为十二张表），
			 */
			sql ="insert into ebs_accnodetaildata_"+index+" (autoid,accno,credit,idbank,idcenter,idbranch,dcflag,workdate,tracebal,vouno," +
				" traceno,currtype,to_accno,to_accname,abs,datadate,importdate,trace_code,tracetime) " +
				" select ACCNODETAILDATA_"+index+"_AUTOID.nextval,e.ac_no,cast(e.bal as float),info.idbank,info.idcenter,info.idbranch,e.io_ind," +
				" e.tx_date, cast(e.tran_amt as float),e.docnum,cast(e.trace_no as number),info.currency,e.to_accno,e.to_accname,e.abs,e.datadate,e.importdate, e.trace_code,e.tracetime "+
				" from ebs_dephist e inner join ebs_basicinfo info on info.accno=e.ac_no where info.ischeck = '0' and e.datadate ='"+dataDate+"' ";
			dataProcess.insertOrUpdateDate(sql);
			
			//对借贷标志进行转换
			
			sql="update ebs_accnodetaildata_"+index+" set dcflag = '"+FinalConstant.dcflagMap.get("双方")+"' where dcflag='B' and datadate ='"+dataDate+"' ";
			dataProcess.insertOrUpdateDate(sql);
			
			sql="update ebs_accnodetaildata_"+index+" set dcflag = '"+FinalConstant.dcflagMap.get("贷")+"' where dcflag='C' and datadate ='"+dataDate+"' ";
			dataProcess.insertOrUpdateDate(sql);
			
			sql="update ebs_accnodetaildata_"+index+" set dcflag = '"+FinalConstant.dcflagMap.get("借")+"' where dcflag='D' and datadate ='"+dataDate+"' ";
			dataProcess.insertOrUpdateDate(sql);
			
		} catch (Exception e) {
			EDataAction.MSGINFO="数据日期："+dataDate+", 信息：存款明细信息过滤异常";
			tools.getDataLogUtils().infoUpdate(OperLogModuleDefine.DATAPROFLAG,EDataAction.MSGINFO,e,String.valueOf(OperLogModuleDefine.dataprocess),dataDate);
			throw new Exception(e.getMessage(), e);
		}
	}
	
	/**
	 * 内部账户明细信息过滤
	 * date格式为：20130826
	 * @param date
	 * @throws DaoException 
	 */
	public void innerAccnoDetailFilter(String dataDate) throws Exception{
		try {
			/**
			 * 如果明细表存在导入日志为当前日期的数据，则将其删除
			 */
			String sql ="delete from ebs_inneraccnodetail_data where datadate ='"+dataDate+"' ";
			dataProcess.insertOrUpdateDate(sql);
			
			
			sql ="insert into ebs_inneraccnodetail_data (AUTOID,trad_date,seri_no,curr_code,acct,oppost_acct,oppost_acct_name,vouch_no," 
							+ "summy,borrow_lend_sign,trad_amt,acct_bal,trad_code,host_syst_time,DATADATE,IMPORTDATE) " +
				" select INNERACCNODETAILDATA_AUTOID.nextval,e.trad_date,e.seri_no,e.curr_code,e.acct,e.oppost_acct,e.oppost_acct_name,e.vouch_no,e.summy," +
				" case when e.borrow_lend_sign='B' then '"+FinalConstant.dcflagMap.get("双方")+"' " +
				" when e.borrow_lend_sign='C' then '"+FinalConstant.dcflagMap.get("贷")+"' " +
				" when e.borrow_lend_sign='D' then '"+FinalConstant.dcflagMap.get("借")+"' " +
				" else '' end," +
				" e.trad_amt,e.acct_bal,e.trad_code,e.host_syst_time,e.DATADATE,e.IMPORTDATE "+
				" from ebs_inneraccnodetail e where e.datadate ='"+dataDate+"' ";
			
			dataProcess.insertOrUpdateDate(sql);
			EDataAction.MSGINFO="数据日期："+dataDate+", 信息：内部账户明细迁移完成";
		} catch (Exception e) {
			EDataAction.MSGINFO="数据日期："+dataDate+", 信息：内部账户明细迁移异常";
			tools.getDataLogUtils().infoUpdate(OperLogModuleDefine.DATAPROFLAG,EDataAction.MSGINFO,e,String.valueOf(OperLogModuleDefine.dataprocess),dataDate);
			throw new Exception(e.getMessage(), e);
		}
	}
	
	/**
	 *  账户对账周期的归类
	 * 	@param date 格式为 201308
	 *  @throws DaoException 
	 */
	public void accCycleJudge(String year,String month,String dataDate) throws Exception{
			try {
				//将本月账户明细的月累计，月最大发生额发生额更新账户明细统计表中,为账户对账周期的判定做数据准备
				String date=year+month;
				String sql ="merge into ebs_accnodetailcount a using " +
						"(select t.accno, max(t.tracebal)as maxtracebal, sum(t.tracebal) as sumtracebal from  EBS_ACCNODETAILDATA_"+month+" t where t.trace_code<>'BBBB' group by t.accno) b " +
						" on (a.accno = b.accno and a.docdate ='"+date+"') " +
						" when matched then " +
						" update set a.totalamount_month = b.sumtracebal, a.maxamount_month=b.maxtracebal " ;
				dataProcess.insertOrUpdateDate(sql);
				
				/**
				 * 对账户明细统计表中的账户进行对账周期的分类 ，账户的对账周期暂存于账户明细统计表中
				 * 账户对账类型定制表 EBS_RULEOFACCCYCLE
				 */
				
				//查询定制的账户类型有几类
				sql ="select acccycle from ebs_ruleofacccycle where executeflog ='0' group by acccycle order by acccycle ";
				List<Map<String,Object>> acccycleList = dataProcess.queryObjectList(new String[]{"acccycle"}, sql);
				
				//账户对账周期的归类
				for(Map<String,Object> accMap:acccycleList){
					updateAcccycle((String)accMap.get("acccycle"),date);
				}
				EDataAction.MSGINFO="数据日期："+dataDate+", 信息：账户对账周期归类完成";
			} catch (Exception e) {
				EDataAction.MSGINFO="数据日期："+dataDate+", 信息：账户对账周期归类异常";
				tools.getDataLogUtils().infoUpdate(OperLogModuleDefine.DATAPROFLAG,EDataAction.MSGINFO,e,String.valueOf(OperLogModuleDefine.dataprocess),dataDate);
				throw new Exception(e.getMessage(), e);
			}	
	}
	
	/**
	 * 更新账户的对账周期
	 * 账户明细统计表中的账户的对账周期同步到账户信息表中去，特殊账户不做同步,和分流的数据不做同步
	 * @param date 例： 201308
	 * @throws DaoException 
	 */
	public void updateAccBusinessInfo(String year,String month,String dataDate) throws Exception{
		try {
			String date=year+month;
			String sql="update ebs_basicinfo set acccycle='' where isspecile ='0' ";
			dataProcess.insertOrUpdateDate(sql);
			sql = "merge into ebs_basicinfo e "+
					" using (select a.accno,a.acccycle from ebs_accnodetailcount a where a.docdate ='"+date+"' ) t "+
					" on (e.accno =t.accno) "+
					" when matched then "+
						" update set e.acccycle = t.acccycle  where e.isspecile ='0' ";
			dataProcess.insertOrUpdateDate(sql);
			EDataAction.MSGINFO="数据日期："+dataDate+", 信息：更新账户的对账周期完成";
		} catch (Exception e) {
			EDataAction.MSGINFO="数据日期："+dataDate+", 信息：更新账户的对账周期异常";
			tools.getDataLogUtils().infoUpdate(OperLogModuleDefine.DATAPROFLAG,EDataAction.MSGINFO,e,String.valueOf(OperLogModuleDefine.dataprocess),dataDate);
			throw new Exception(e.getMessage(), e);
		}
	}
	
	/**
	 * 临时修改新开户的账户的账户类型和发送方式
	 * @param date 例： 201308
	 * @param ishbcheck 是否是总行对账
	 * 
	 * @throws DaoException 
	 */
	public void updateNewAccnoInfo(String dataDate,String ishbcheck) throws Exception{
		try {		
			String sql="select accno,sendmode,idbank,idbranch from ebs_basicinfo where substr(opendate,0,6) ='"+dataDate.substring(0, 6)+"' " +
					" and currency = '"+FinalConstant.currencyMap.get("人民币")+"' ";
			List<Map<String,Object>> list =  dataProcess.queryObjectList(new String[]{"accno","sendmode","idbank","idbranch"}, sql);
			for(Map<String,Object> m:list){
				String accno = (String) m.get("accno");
				String idbranch = (String)m.get("idbranch"); //分行
				String idbank = (String)m.get("idbank"); //网点
				sql = "update ebs_accnomaindata a set a.sendmode ='"+FinalConstant.sendModelMap.get("面对面")+"',a.acccycle='"+FinalConstant.acccycleMap.get("一类账户")+"' " +
						" where a.accno= '"+accno+"'  and a.ishbcheck='"+ishbcheck+"' and docdate = '"+dataDate+"' ";
				dataProcess.insertOrUpdateDate(sql);
				String address = findAddressBySendMode(accno,idbank,idbranch,FinalConstant.sendModelMap.get("面对面"));
				if(address!=""){
					sql="update ebs_accnomaindata a set a.sendaddress= '"+address+"' " +
							" where a.accno= '"+accno+"'  and a.ishbcheck='"+ishbcheck+"' and docdate = '"+dataDate+"' ";
					dataProcess.insertOrUpdateDate(sql);
				}
			}
			EDataAction.MSGINFO="数据日期："+dataDate+", 信息：临时修改新开户的账户的账户类型和发送方式完成";
		} catch (Exception e) {
			EDataAction.MSGINFO="数据日期："+dataDate+", 信息：临时修改新开户的账户的账户类型和发送方式异常";
			tools.getDataLogUtils().infoUpdate(OperLogModuleDefine.DATAPROFLAG,EDataAction.MSGINFO,e,String.valueOf(OperLogModuleDefine.dataprocess),dataDate);
			throw new Exception(e.getMessage(), e);
		}
	}
	
	/**
	 * 修改发送方式为面对面的账户的账户类型为一类
	 * @param date 例： 201308
	 */
	public void updateFaceToFaceAccno(String year,String month,String dataDate) throws Exception{
		try {
			String date=year+month;
			String sql="merge into ebs_accnodetailcount c "
					+" using (select accno from ebs_basicinfo where isspecile ='0' and sendmode ='"+FinalConstant.sendModelMap.get("面对面")+"' ) t "
					+" on (c.accno = t.accno) "
					+" when matched then "
					+" update set c.acccycle ='"+FinalConstant.acccycleMap.get("一类账户")+"' where c.docdate ='"+date+"'";
			dataProcess.insertOrUpdateDate(sql);
			EDataAction.MSGINFO="数据日期："+dataDate+", 信息：修改对账方式为面对面的账户的账户类型为一类完成";
		} catch (Exception e) {
			EDataAction.MSGINFO="数据日期："+dataDate+", 信息：修改对账方式为面对面的账户的账户类型为一类异常";
			tools.getDataLogUtils().infoUpdate(OperLogModuleDefine.DATAPROFLAG,EDataAction.MSGINFO,e,String.valueOf(OperLogModuleDefine.dataprocess),dataDate);
			throw new Exception(e.getMessage(), e);
		}
	}
	
	/**
	 * 修改外币（非人民币）账户的账户类型为一类
	 * @param date 例： 201308
	 */
	public void updateNotCNYAccno(String year,String month,String dataDate) throws Exception{
		try {
			String date=year+month;
			String sql="merge into ebs_accnodetailcount c "
					+" using (select accno from ebs_basicinfo where isspecile ='0' and currency <>'"+FinalConstant.currencyMap.get("人民币")+"' ) t "
					+" on (c.accno = t.accno) "
					+" when matched then "
					+" update set c.acccycle ='"+FinalConstant.acccycleMap.get("一类账户")+"' where c.docdate ='"+date+"'";
			dataProcess.insertOrUpdateDate(sql);
			EDataAction.MSGINFO="数据日期："+dataDate+", 信息：修改外币（非人民币）账户的账户类型为一类完成";
		} catch (Exception e) {
			EDataAction.MSGINFO="数据日期："+dataDate+", 信息：修改外币（非人民币）账户的账户类型为一类异常";
			tools.getDataLogUtils().infoUpdate(OperLogModuleDefine.DATAPROFLAG,EDataAction.MSGINFO,e,String.valueOf(OperLogModuleDefine.dataprocess),dataDate);
			throw new Exception(e.getMessage(), e);
		}
	}
	
	/**
	 * 总行规则如果包含面对面的发送方式，在季点时，将一类账户余额为200W以上的账户也插入到临时对账单中，并修改其发送方式为面对面
	 * @throws Exception
	 * @param date 例： 201308
	 */
	public void updateSendModeByHR(String dataDate) throws Exception{
		try {
			//String creditFlag= "2000000";
			String checkDate = dataDate.substring(0,6);
			String sql="merge into ebs_accnomaindata a "+
					" using (select info.accno,e.credit,e.totalamount_month,e.maxamount_month, "+
					" info.currency, info.sendmode,e.acccycle,info.acctype,info.subjectno,info.opendate, "+
					" info.productno,info.idbank,info.idbranch,info.idcenter,info.bankname,info.custid,info.sendaddress,'"+FinalConstant.isHBcheckMap.get("总行")+"' "+
					" from ( select accno,credit,totalamount_month,maxamount_month,acccycle from ebs_accnodetailcount  where 1=1 and docdate ='"+checkDate+"' and acccycle is not null " +
					" and credit >=(select p.sysbasevalue from PARAM_SYSBASE p where p.sysbaseid='EBILLBYPASSDIT')  and acccycle ='"+FinalConstant.acccycleMap.get("一类账户")+"' ) e "+
					" inner join ebs_basicinfo info on info.accno=e.accno where 1=1 ) t" +
					" on (t.accno=a.accno)"+
					" when matched then " +
					" update set a.sendmode='"+FinalConstant.sendModelMap.get("面对面")+"' where a.docdate='"+dataDate+"' and a.ishbcheck='"+FinalConstant.isHBcheckMap.get("总行")+"' " +
					" when not matched then" +
					" insert values (ACCNOMAINDATA_AUTOID.NEXTVAL,'',t.accno,'','',t.credit,t.totalamount_month,t.maxamount_month," +
					" 0,t.currency,'','','"+FinalConstant.sendModelMap.get("面对面")+"',t.acccycle,'',t.accno,t.subjectno,t.subjectno,'',t.opendate," +
					" '',t.productno,'','','"+dataDate+"','','','',t.idbank,t.idbranch,t.idcenter,t.bankname,t.custid,t.sendaddress,'"+FinalConstant.isHBcheckMap.get("总行")+"','','' )";
			dataProcess.insertOrUpdateDate(sql);
			EDataAction.MSGINFO="总行规则中包含面对面时，将符合要求的一类账户插入临时对账单完成";
		} catch (Exception e) {
			EDataAction.MSGINFO="修改对账方式为面对面的账户的账户类型为一类异常";
			tools.getDataLogUtils().infoUpdate(OperLogModuleDefine.DATAPROFLAG,EDataAction.MSGINFO,e,String.valueOf(OperLogModuleDefine.dataprocess),dataDate);
			throw new Exception(e.getMessage(), e);
		}
	}
	
	
	/**
	 * 将有发生额明细的企业网银的账户的对账周期设置为一类
	 * @param date
	 */
	public void updateOnlineBankAcccycle(String year,String month,String dataDate) throws Exception{
		try {
			String date=year+month;
			String sql ="merge into ebs_accnodetailcount c "
				+" using (select accno from ebs_acctlist group by accno) t "
				+" on (c.accno = t.accno) "
				+" when matched then "
				+" update set c.acccycle ='"+FinalConstant.acccycleMap.get("一类账户")+"' where  c.totalamount_month > cast('"+nullCreditFlag+"' as float)"+" and c.maxamount_month > cast('"+nullCreditFlag+"' as float)"+" and c.docdate ='"+date+"' ";
			dataProcess.insertOrUpdateDate(sql);
			EDataAction.MSGINFO="数据日期："+dataDate+", 信息：将有明细的企业网银的账户类型为一类完成";
		} catch (Exception e) {
			EDataAction.MSGINFO="数据日期："+dataDate+", 信息：将有明细的企业网银的账户类型为一类异常";
			tools.getDataLogUtils().infoUpdate(OperLogModuleDefine.DATAPROFLAG,EDataAction.MSGINFO,e,String.valueOf(OperLogModuleDefine.dataprocess),dataDate);
			throw new Exception(e.getMessage(), e);
		}
	}
	
	
	/**
	 * 账号分流处理 （ 分流标识 distributary）
	 * 在季点的时候，将二类账户和余额大于(包含)200W的一类账户 进行分流标示，并设置账单分流的月份
	 * @param date 例： 201308
	 * @param month 例： 08
	 * @throws DaoException 
	 */
	public void billDistributary(String year,String month,String dataDate) throws Exception{
		//查询出需要分流的账单总数
		try {
			String date=year+month;
			String sql = " EBS_BASICINFO t where t.acccycle = '"+FinalConstant.acccycleMap.get("一类账户") +"' "+  //一类账户
					" and t.ischeck='"+FinalConstant.isCheckMap.get("对账")+"' "+         //账户对账
					" and t.currency = '"+FinalConstant.currencyMap.get("人民币")+"' "+		//币种为人民币
					" and ((t.subjectno in (select s.subnoc from Param_Subnoc s) and t.isspecile = '0') or (t.isspecile = '1')) " +   //参与对账的
					//--2期改造添加 EBILLBYPASSDIT参数  设置分楼金额
					" and t.accno in (select a.accno from ebs_accnodetailcount a where a.credit>=(select p.sysbasevalue from PARAM_SYSBASE p where p.sysbaseid='EBILLBYPASSDIT') and a.docdate= '"+date+"') ";  //余额大于200万
					//" and (t.sendmode<>'"+FinalConstant.sendModelMap.get("网银")+"' or t.sendmode is null)";  //发送方式不为网银的
			
//			String sql =" ebs_accnodetailcount where 1=1 and ( (acccycle ='"+FinalConstant.acccycleMap.get("一类账户")+"' and credit>=2000000)) " 
//					+"and docdate ='"+date+"' "+" and accno in (select b.accno from ebs_basicinfo b where (b.sendmode is null or b.sendmode<>'"+FinalConstant.sendModelMap.get("网银")+"')) ";
			
			int sum = (int) dataProcess.queryTableCount(sql);
			int count = 0;
			if(sum%3==0){
				count = (int) (sum/3);
			}
			else{
				count =  (int) (sum/3)+1;
			}
			//总数大于3才进行分流操作
			if(sum>=3){
				boolean flag =true;
				int begin=0;
				int end=0;
				int mon =Integer.parseInt(month);
				while(flag){
					begin=end;
					end=begin+count;
					if(end>=sum){
						end=sum;
						flag=false;
					}
					sql = "update ebs_basicinfo b set b.distributary='"+(mon%12)+"' where " +
							" b.accno in ( " +
							" select f.accno from ( " +
							" select e.accno,rownum rnum from ( " +
							" select t.accno from EBS_BASICINFO t where t.acccycle = '"+FinalConstant.acccycleMap.get("一类账户")+"' " +
							" and t.ischeck='"+FinalConstant.isCheckMap.get("对账")+"' "+
							" and t.currency = '"+FinalConstant.currencyMap.get("人民币")+"' "+
							" and ((t.subjectno in (select s.subnoc from Param_Subnoc s) and t.isspecile = '0') or (t.isspecile = '1')) " +
							" and t.accno in (select a.accno from ebs_accnodetailcount a where a.credit>=(select p.sysbasevalue from PARAM_SYSBASE p where p.sysbaseid='EBILLBYPASSDIT') and a.docdate='"+date+"')) e " +
							//" and (t.sendmode<>'"+FinalConstant.sendModelMap.get("网银")+"' or t.sendmode is null)) e " +
							" where rownum <= "+end+") f where rnum > "+begin+" )";
					
//					sql ="update ebs_basicinfo ac set ac.distributary='"+(mon%12)+"' where "
//							+" (ac.sendmode is null or ac.sendmode<>'"+FinalConstant.sendModelMap.get("网银")+"') " 
//							+" and ac.accno in ( "+
//							" select accno from (  "+ 
//							" select t.accno,rownum rnum from " +
//							" (select coun.accno from ebs_accnodetailcount coun where 1=1 and ( (coun.acccycle ='"+FinalConstant.acccycleMap.get("一类账户")+"' and coun.credit>=2000000)) and coun.docdate ='"+date+"' " 
//							+" and coun.accno in (select b.accno from ebs_basicinfo b where (b.sendmode is null or b.sendmode<>'"+FinalConstant.sendModelMap.get("网银")+"'))) t  "+                  
//							" where rownum <="+end+" ) where rnum > "+begin+" )";
					
					dataProcess.insertOrUpdateDate(sql);
					mon++;
				}
				EDataAction.MSGINFO="数据日期："+dataDate+", 信息：面对面对账方式分流完成";
			}
		} catch (Exception e) {
			EDataAction.MSGINFO="数据日期："+dataDate+", 信息：账号分流异常";
			tools.getDataLogUtils().infoUpdate(OperLogModuleDefine.DATAPROFLAG,EDataAction.MSGINFO,e,String.valueOf(OperLogModuleDefine.dataprocess),dataDate);
			throw new Exception(e.getMessage(), e);
		}
	}
	
//	/**
//	 * 将本月需要分流的数据转移到临时对账单ebs_accnomaindata中
//	 * @param date
//	 * @param month
//	 * @throws DaoException 
//	 */
//	public void disposeDisAccNo(int month,String dataDate) throws Exception{
//		try {
//			String sql ="insert into ebs_accnomaindata" +
//					"(autoid,accno,credit,totalamount_month,maxamount_month,currency,sendmode,acccycle,sealaccno,acctype," +
//					" subjectno,opendate,docdate,idbank,idbranch,idcenter,bankname,custid,sendaddress,ishbcheck,linkMan ) " +
//					" select ACCNOMAINDATA_AUTOID.NEXTVAL,info.accno,e.credit,e.totalamount_month,e.maxamount_month," +
//					" info.currency, info.sendmode,info.acccycle,info.accno,info.acctype,info.subjectno,info.opendate," +
//					" '"+dataDate+"',info.idbank,info.idbranch,info.idcenter,info.bankname,info.custid,info.sendaddress,'"+FinalConstant.isHBcheckMap.get("正常")+"',info.linkMan " +
//					" from ebs_basicinfo info left join ebs_accnodetailcount e on info.accno = e.accno where  info.distributary ='"+month+"' and e.docdate = '"+dataDate.substring(0,6)+"' and info.acccycle is not null";
//			dataProcess.insertOrUpdateDate(sql);
//			EDataAction.MSGINFO="数据日期："+dataDate+", 信息：处理本月分流账户完成";
//		} catch (Exception e) {
//			EDataAction.MSGINFO="数据日期："+dataDate+", 信息：处理本月分流账户异常";
//			tools.getDataLogUtils().infoUpdate(OperLogModuleDefine.DATAPROFLAG,EDataAction.MSGINFO,e,String.valueOf(OperLogModuleDefine.dataprocess),dataDate);
//			throw new Exception(e.getMessage(), e);
//		}
//	}
	
	
	/**
	 * 对本月需要生成对账数据的的分流账号的分流标识设置为正常
	 * @param month
	 * @throws DaoException 
	 */
	public void updatedisFlag(int month,String dataDate) throws Exception {
		try {
			
			String sql ="update ebs_basicinfo ac set ac.distributary ='0' where ac.distributary='"+month+"'";
			dataProcess.insertOrUpdateDate(sql);
		} catch (Exception e) {
			EDataAction.MSGINFO="数据日期："+dataDate+", 信息：将本月需要生成对账数据的的分流账号的分流标识设置为正常发生异常";
			tools.getDataLogUtils().infoUpdate(OperLogModuleDefine.DATAPROFLAG,EDataAction.MSGINFO,e,String.valueOf(OperLogModuleDefine.dataprocess),dataDate);
			throw new Exception(e.getMessage(), e);
		}
	}
	
	/**
	 * 将非分流的且参与对账的账户的信息转移到临时对账单ebs_accnomaindata中
	 * @param date
	 * @param month
	 * @throws DaoException 
	 */
	public void disposeNormalAccNo(int month,String dataDate) throws Exception{
		try {
			//如果对账单表中存在当前日期的数据，则删除。
			String sql0="delete from ebs_accnomaindata where docdate='"+dataDate+"' and ishbcheck ='"+FinalConstant.isHBcheckMap.get("正常")+"' ";
			dataProcess.insertOrUpdateDate(sql0);
			
			String sql ="insert into ebs_accnomaindata" +
					" (autoid,accno,credit,totalamount_month,maxamount_month,currency,sendmode,acccycle,sealaccno,acctype," +
					" subjectno,opendate,docdate,idbank,idbranch,idcenter,bankname,custid,sendaddress,ishbcheck,linkman,distributary ) " +
					" select ACCNOMAINDATA_AUTOID.NEXTVAL,info.accno,e.credit,e.totalamount_month,e.maxamount_month," +
					" info.currency, info.sendmode,info.acccycle,info.accno,info.acctype,info.subjectno,info.opendate," +
					" '"+dataDate+"',info.idbank,info.idbranch,info.idcenter,info.bankname,info.custid,info.sendaddress,'"+FinalConstant.isHBcheckMap.get("正常")+"',info.linkman,info.distributary " +
					" from ebs_basicinfo info inner join ebs_accnodetailcount e on info.accno = e.accno where info.ischeck ='0' " +
					" and ((info.subjectno in (select s.subnoc from param_subnoc s) and info.isspecile='0') or (info.isspecile='1')) " +
					" and e.docdate = '"+dataDate.substring(0,6)+"'  and (substr(opendate,0,6) ='"+dataDate.substring(0, 6)+"' or info.acccycle ='1' " ;
			//如果本月是季点
			if(month%3==0){
				sql+=" or info.acccycle ='"+FinalConstant.acccycleMap.get("二类账户")+"' ";
			}
			//如果本月是年终
			if(month%6==0){
				sql+=" or info.acccycle ='"+FinalConstant.acccycleMap.get("三类账户")+"' ";
			}
			//如果本月是年终
			if(month%12==0){
				sql+=" or info.acccycle ='"+FinalConstant.acccycleMap.get("四类账户")+"' ";
			}
			sql+=" )";
			dataProcess.insertOrUpdateDate(sql);
			EDataAction.MSGINFO="数据日期："+dataDate+", 信息：参与对账的账户的信息转移到临时对账单中完成";
		} catch (Exception e) {
			EDataAction.MSGINFO="数据日期："+dataDate+", 信息：参与对账的账户的信息转移到临时对账单中异常";
			tools.getDataLogUtils().infoUpdate(OperLogModuleDefine.DATAPROFLAG,EDataAction.MSGINFO,e,String.valueOf(OperLogModuleDefine.dataprocess),dataDate);
			throw new Exception(e.getMessage(), e);
		}
	}
	
	/**
	 * 将抽查的账户(操作员上传)的信息作为特殊面对面方式出单，转移到临时对账单ebs_accnomaindata中
	 * set ebs_accnomaindata faceflag=1,sendmode=4,acccycle=1
	 * @param date
	 * @param month
	 * @throws DaoException 
	 */
	public void disposeSpecialFaceToFaceAccNo(String dataDate) throws Exception{
		try {
			String sql ="merge into ebs_accnomaindata a " +
					" using (select b.accno,d.credit,d.totalamount_month,d.maxamount_month,b.currency,b.acctype," +
					" b.subjectno,b.opendate,b.idbank,b.idbranch,b.idcenter,b.bankname,b.custid,b.sendaddress,b.linkman,b.distributary " +
					" from ebs_basicinfo b inner join ebs_accnodetailcount d on b.accno=d.accno " +
					" inner join ebs_specialfacetoface s on s.accno = d.accno where s.docdate='"+dataDate+"' and d.docdate='"+dataDate.substring(0,6)+"' ) t " +
					" on (a.accno = t.accno and a.docdate='"+dataDate+"') " +
					" when matched then update set a.faceflag = '1', a.sendmode = '4', a.acccycle = '1' " +
					" when not matched then " +
					" insert values(ACCNOMAINDATA_AUTOID.nextval,'',t.accno,'','',t.credit,t.totalamount_month,t.maxamount_month,'',t.currency,'',''," +
					" '4','1','',t.accno,t.acctype,t.subjectno,'',t.opendate,'','','','1','"+dataDate+"',''," +
					" '','',t.idbank,t.idbranch,t.idcenter,t.bankname,t.custid,t.sendaddress,'"+FinalConstant.isHBcheckMap.get("正常")+"',t.linkman,t.distributary)";
			
			//ebs_accnomaindata(AUTOID,VOUCHERNO,ACCNO,ACCNOSON,ACCNOINDEX,CREDIT,TOTALAMOUNT_MONTH,MAXAMOUNT_MONTH,AVBALANCE_MONTH,CURRENCY,CHECKFLAG,FINALCHECKFLAG,
			//					SENDMODE,ACCCYCLE,SIGNFLAG,SEALACCNO,ACCTYPE,SUBJECTNO,KEYFLAG,OPENDATE,SAVELOANFLAG,PRODUCTNO,PRODUCTDESC,FACEFLAG,DOCDATE,SINGLEACCNO,
			//					CURRTYPE,MATCHFLAG,IDBANK,IDBRANCH,IDCENTER,BANKNAME,CUSTID,SENDADDRESS,ISHBCHECK,LINKMAN,DISTRIBUTARY
			
			dataProcess.insertOrUpdateDate(sql);
			EDataAction.MSGINFO="数据日期："+dataDate+", 信息：抽查对账的账户（特殊面对面）的信息转移到临时对账单中完成";
		} catch (Exception e) {
			EDataAction.MSGINFO="数据日期："+dataDate+", 信息：抽查对账的账户（特殊面对面）的信息转移到临时对账单中异常";
			tools.getDataLogUtils().infoUpdate(OperLogModuleDefine.DATAPROFLAG,EDataAction.MSGINFO,e,String.valueOf(OperLogModuleDefine.dataprocess),dataDate);
			throw new Exception(e.getMessage(), e);
		}
	}
	
	/**
	 * 处理特殊要求的用户
	 * 按照面对面分流标识将一类账户余额在200W以上临时修改为面对面 
	 * @param date 例： 201308
	 * @param ishbcheck
	 * @throws DaoException 
	 */
	public void disposeOtherAccNo(String dataDate,String ishbcheck) throws Exception{
		try {
			String month =dataDate.substring(4,6);
			int mon =Integer.parseInt(month);
			//String creditFlag="2000000";
			String sql="select accno,sendmode,idbank,idbranch from ebs_accnomaindata " +
					" where credit >= (select p.sysbasevalue from PARAM_SYSBASE p where p.sysbaseid='EBILLBYPASSDIT') and acccycle ='"+FinalConstant.acccycleMap.get("一类账户")+"' and ishbcheck='"+ishbcheck+"' " +
							" and currency = '"+FinalConstant.currencyMap.get("人民币")+"' "+
							" and docdate = '"+dataDate+"' "+" and distributary = '"+(mon%12)+"' ";
			List<Map<String,Object>> listFace =  dataProcess.queryObjectList(new String[]{"accno","sendmode","idbank","idbranch"}, sql);

			for(Map<String,Object> m:listFace){
				String accno = (String) m.get("accno");
				String idbranch = (String)m.get("idbranch"); //分行
				String idbank = (String)m.get("idbank"); //网点
				sql = "update ebs_accnomaindata a set a.sendmode ='"+FinalConstant.sendModelMap.get("面对面")+"' " +
						" where a.accno= '"+accno+"' and a.ishbcheck='"+ishbcheck+"' and docdate = '"+dataDate+"' ";
				dataProcess.insertOrUpdateDate(sql);
				String address = findAddressBySendMode(accno,idbank,idbranch,FinalConstant.sendModelMap.get("面对面"));
				if(address!=null&address!=""){
					sql="update ebs_accnomaindata a set a.sendaddress= '"+address+"' " +
						" where a.accno= '"+accno+"' and a.ishbcheck='"+ishbcheck+"' and docdate = '"+dataDate+"' ";
					dataProcess.insertOrUpdateDate(sql);
				}
			}
			EDataAction.MSGINFO="数据日期："+dataDate+", 信息：将部分一类账户余额在200W以上的账户的对账方式临时修改为面对面完成";
		}catch (Exception e) {
			tools.getDataLogUtils().infoUpdate(OperLogModuleDefine.DATAPROFLAG,EDataAction.MSGINFO,e,String.valueOf(OperLogModuleDefine.dataprocess),dataDate);
			throw new Exception(e.getMessage(), e);
		}
	}
	
	
	/**
	 * 将网银的账户的发送方式修改为邮寄
	 * @param date 例： 201308
	 * @param ishbcheck
	 * @throws DaoException 
	 */
	public void updateOnlineBankAccNo(String dataDate,String ishbcheck) throws Exception{
		try {
			String sql ="update ebs_accnomaindata a set a.sendmode ='"+FinalConstant.sendModelMap.get("邮寄")+"', " +
					"a.sendaddress=(select info.address from ebs_basicinfo info where info.accno=a.accno) " +
					"where a.ishbcheck='0' and docdate = '"+dataDate+"' and a.sendmode='"+FinalConstant.sendModelMap.get("网银")+"' ";
			dataProcess.insertOrUpdateDate(sql);
			EDataAction.MSGINFO="数据日期："+dataDate+", 信息：临时将网银的账户的发送方式修改为邮寄完成";
		} catch (Exception e) {
			EDataAction.MSGINFO="数据日期："+dataDate+", 信息：临时将网银的账户的发送方式修改为邮寄异常";
			tools.getDataLogUtils().infoUpdate(OperLogModuleDefine.DATAPROFLAG,EDataAction.MSGINFO,e,String.valueOf(OperLogModuleDefine.dataprocess),dataDate);
			throw new Exception(e.getMessage(), e);
		}
	}
	
	/**
	 * 创建非网银账号临时对账单 (一个账单最多存在五个账号)
	 * @param date 格式 20130831
	 * @param isHBcheck 总行对账标示（0,正常,1总行）
	 * @throws DaoException 
	 */
	public void createTempBill(String dataDate,String isHBcheck) throws Exception{
		try {
		
			/**
			 * 对非网银账号按照客户号，发送方式，发送地址，网点号，联系人，进行分组，每组数据按照5条数据一个账单的方式进行账单编号的划分
			 */
			
			String sql="select a.custid,a.sendmode,a.idbank,a.linkman,a.sendaddress " +
					" from ebs_accnomaindata a where 1=1 and (a.sendmode is null or a.sendmode<>'"+FinalConstant.sendModelMap.get("网银")+"') and a.docdate= '"+dataDate+"' and a.isHBcheck= '"+isHBcheck+"' "+
					" group by a.custid,a.sendmode,a.idbank,a.linkman,a.sendaddress ";
			String[] resultPar = new String[]{"custid","sendmode","idbank","linkman","sendaddress"};
			List<Map<String,Object>> list = dataProcess.queryObjectList(new String[]{"custid","sendmode","idbank","linkman","sendaddress"}, sql);
			for(Map<String,Object> m:list){
				sql="select a.accno from ebs_accnomaindata a where 1=1 and a.isHBcheck= '"+isHBcheck+"' and a.docdate= '"+dataDate+"' ";
				for(String str:resultPar){
					String value = (String)m.get(str);
					if(value!=""&&value!=null){
						sql+=" and a."+str+"= '"+value+"' ";
					}else{
						sql+=" and a."+str+" is null ";
					}
				}
				List<Map<String,Object>> accList = dataProcess.queryObjectList(new String[]{"accno"}, sql);
				
				/**
				 * 处理的表 Ebs_Accnomaindata
				 * 一个账单存放5条数据的思路：(5条数据的客户号，发送方式，发送地址，网点号相同)
				 * 取出条件相同的所有数据的账号 ，放到一个list集合中，
				 * 然后对i%5==0的数据分配一个新的账单编号，并且将这个账单编号赋予剩下的四条数据（或者不足四条）
				 */
				
				String voucherno="";
				for(int i=0;i<accList.size();i++){
					String accnoTemp = (String)accList.get(i).get("accno");
					
					//5条数据共用一个对账单编号
					if(i%5==0){
						//对账单编号的生成规则
						String code=" substr('"+dataDate+"',3,4)||idbank||substr(cast(power(10,5)+VOUCHERNO_AUTOID.NEXTVAL as "
								+ "varchar(50)),-5)||substr(cast(DBMS_RANDOM.VALUE(1,'1234567890') as Varchar(50))+'00',2,2)";
						//生成一个账单编号并赋给第一条数据
						sql ="update ebs_accnomaindata e set e.voucherno = "+code+", e.accnoindex ='"+((i%5)+1)+"' "
								+" where e.accno = '"+accnoTemp+"' and e.docdate ='"+dataDate+"'  and e.isHBcheck= '"+isHBcheck+"' ";
						dataProcess.insertOrUpdateDate(sql);
						sql ="select voucherno from ebs_accnomaindata  where accno ='"+accnoTemp+"' and docdate ='"+dataDate+"' and isHBcheck= '"+isHBcheck+"' ";
						List<Map<String,Object>> vouchernoList = dataProcess.queryObjectList( new String[]{"voucherno"}, sql);
						if(vouchernoList.size()==1){
							voucherno = (String)vouchernoList.get(0).get("voucherno");
						}else{
							tools.getDataLogUtils().error("账单编号分配异常",false);
						}
					}else{
						//其他4条数据共用一个账单编号
						sql ="update ebs_accnomaindata e set e.voucherno = '"+voucherno+"', e.accnoindex ='"+((i%5)+1)+"'"
								+" where e.accno = '"+accnoTemp+"' and e.docdate ='"+dataDate+"' and e.isHBcheck= '"+isHBcheck+"' ";
						dataProcess.insertOrUpdateDate(sql);
					}
				}
			}
			EDataAction.MSGINFO="数据日期："+dataDate+", 信息：非网银账号临时对账单生成完成";
			tools.getDataLogUtils().info(EDataAction.MSGINFO,String.valueOf(OperLogModuleDefine.dataprocess),dataDate);
		} catch (Exception e) {
			String sql="delete from ebs_accnomaindata where docdate='"+dataDate+"' and ishbcheck ='"+isHBcheck+"' ";
			dataProcess.insertOrUpdateDate(sql);
			EDataAction.MSGINFO="数据日期："+dataDate+", 信息：非网银账号临时对账单生成异常";
			tools.getDataLogUtils().infoUpdate(OperLogModuleDefine.DATAPROFLAG,EDataAction.MSGINFO,e,String.valueOf(OperLogModuleDefine.dataprocess),dataDate);
			throw new Exception(e.getMessage(), e);
		}
	}
	
	/**
	 * 创建网银账号临时对账单 (一个网银账号生成一个对账单)
	 * @param date 格式 20130831
	 * @param isHBcheck 总行对账标示（0,正常,1总行）
	 * @throws DaoException 
	 */
	public void creatTempNetBill(String dataDate,String isHBcheck) throws Exception {
		try {
			/**
			 * 一个网银账号生成一个对账单
			 */
			
			String sql="select a.accno from ebs_accnomaindata a where 1=1 and a.sendmode='"+FinalConstant.sendModelMap.get("网银")+"' and a.docdate= '"+dataDate+"' and a.isHBcheck= '"+isHBcheck+"' "+
					" group by a.accno ";
			List<Map<String,Object>> list = dataProcess.queryObjectList(new String[]{"accno"}, sql);
			for(Map<String,Object> m:list){
				String accnoTemp = (String)m.get("accno");
					
				//对账单编号的生成规则
				String code=" substr('"+dataDate+"',3,4)||idbank||substr(cast(power(10,5)+VOUCHERNO_AUTOID.NEXTVAL as "
						+ "varchar(50)),-5)||substr(cast(DBMS_RANDOM.VALUE(1,'1234567890') as Varchar(50))+'00',2,2)";
				//生成一个账单编号并赋给一个网银账号
				sql ="update ebs_accnomaindata e set e.voucherno = "+code+", e.accnoindex ='1' "
						+" where e.accno = '"+accnoTemp+"' and e.docdate ='"+dataDate+"'  and e.isHBcheck= '"+isHBcheck+"' ";
				dataProcess.insertOrUpdateDate(sql);
				
				sql ="select voucherno from ebs_accnomaindata  where accno ='"+accnoTemp+"' and docdate ='"+dataDate+"' and isHBcheck= '"+isHBcheck+"' ";
				List<Map<String,Object>> vouchernoList = dataProcess.queryObjectList( new String[]{"voucherno"}, sql);
				if(vouchernoList.size()!=1){
					tools.getDataLogUtils().error("账单编号分配异常",false);
				}
			}
			EDataAction.MSGINFO="数据日期："+dataDate+", 信息：网银账号临时对账单生成完成";
			tools.getDataLogUtils().info(EDataAction.MSGINFO,String.valueOf(OperLogModuleDefine.dataprocess),dataDate);
		} catch (Exception e) {
			String sql="delete from ebs_accnomaindata where docdate='"+dataDate+"' and ishbcheck ='"+isHBcheck+"' ";
			dataProcess.insertOrUpdateDate(sql);
			EDataAction.MSGINFO="数据日期："+dataDate+", 信息：网银账号临时对账单生成异常";
			tools.getDataLogUtils().infoUpdate(OperLogModuleDefine.DATAPROFLAG,EDataAction.MSGINFO,e,String.valueOf(OperLogModuleDefine.dataprocess),dataDate);
			throw new Exception(e.getMessage(), e);
		}
	}
	
	/**
	 * 生成对账单
	 * @param date 格式 20130831
	 * @param isHBcheck 是否是总行对账单
	 * @throws DaoException 
	 */
	public void createCheckmaindata(String dataDate,String isHBcheck) throws Exception{
		/**
		 * 对账单的生成
		 * 将处理完成的临时对账单数据转移到对账单(ebs_checkmaindata)中
		 */
		try {
			//如果对账单表中存在当前日期的数据，则删除。
			String sql="delete from ebs_checkmaindata where docdate='"+dataDate+"' and ishbcheck ='"+isHBcheck+"' ";
			dataProcess.insertOrUpdateDate(sql);
			//将临时对账数据转移到对账单数据表中
			sql="insert into ebs_checkmaindata(autoid,voucherno,senddate,dealdate,printtimes,docstate,"
					+" custid,rushflag,docdate,sendmode,sendaddress,ishbcheck) "
					+" select CHECKMAINDATA_autoid.nextval,b.voucherno,'"+dataDate+"','"+dataDate+"',0,'"+FinalConstant.docStateMap.get("等待打印")+"',"
					+" b.custid,'0','"+dataDate+"',b.sendmode,b.sendaddress, b.ishbcheck "
					+" from (select e.voucherno,e.custid,e.sendmode, e.sendaddress, e.ishbcheck "
						+" from ebs_accnomaindata e where e.docdate ='"+dataDate+"' and e.voucherno is not null and e.ishbcheck ='"+isHBcheck+"' "
						+" group by e.voucherno,e.custid,e.sendmode,e.sendaddress,e.ishbcheck) b ";
			dataProcess.insertOrUpdateDate(sql);
			
			//更新对账单的客户信息
			sql="select voucherno from ebs_checkmaindata where docdate='"+dataDate+"' and ishbcheck ='"+isHBcheck+"' ";
			List<Map<String,Object>> checkMainList = dataProcess.queryObjectList( new String[]{"voucherno"}, sql);
			for(Map<String,Object> vouMap:checkMainList){
				String vouchernoTemp=(String)vouMap.get("voucherno");
				sql="select accno from ebs_accnomaindata where voucherno='"+vouchernoTemp+"' and docdate='"+dataDate+"' and ishbcheck ='"+isHBcheck+"' ";
				List<Map<String,Object>> accNoList = dataProcess.queryObjectList( new String[]{"accno"}, sql);
				for(Map<String,Object> accMap:accNoList){
					String accnoTemp=(String)accMap.get("accno");
					sql="update ebs_checkmaindata c set (c.accname,c.idbank,c.idbranch,c.idcenter,c.bankname,c.address,c.zip,c.linkman,c.phone ) = " +
							"(select accname,idbank,idbranch,idcenter,bankname,address,zip,linkman,phone from ebs_basicinfo where accno='"+accnoTemp+"' ) " +
							" where c.voucherno='"+vouchernoTemp+"' and c.docdate='"+dataDate+"' and c.ishbcheck ='"+isHBcheck+"' ";
					dataProcess.insertOrUpdateDate(sql);
					break;
				}
			}
			//更新参数表，标示数据处理的时间
			getDataProcess().insertOrUpdateDate("update param_sysbase set sysbasevalue ="+dataDate+" where sysbaseid ='PROCESSDATE'");
			EDataAction.MSGINFO="数据日期："+dataDate+", 信息：对账单生成完成，日期："+dataDate;
			tools.getDataLogUtils().info(EDataAction.MSGINFO,String.valueOf(OperLogModuleDefine.dataprocess),dataDate);
		} catch (Exception e) {
			String sql="delete from ebs_checkmaindata where docdate='"+dataDate+"' and ishbcheck ='"+isHBcheck+"' ";
			dataProcess.insertOrUpdateDate(sql);
			EDataAction.MSGINFO="数据日期："+dataDate+", 信息：对账单生成异常";
			tools.getDataLogUtils().infoUpdate(OperLogModuleDefine.DATAPROFLAG,EDataAction.MSGINFO,e,String.valueOf(OperLogModuleDefine.dataprocess),dataDate);
			throw new Exception(e.getMessage(), e);
		}
	}
	
	
	/**
	 * 根据对账单中账户的发送地址信息反向更新账户信息表中对应的发送地址信息
	 * @param date
	 * @param ishbcheck
	 * @throws Exception
	 */
	public void updateBasicInfoSendAddress(String dataDate,String ishbcheck) throws Exception{
		try {
			String sql="merge into ebs_basicinfo c "
				+" using (select accno,sendaddress from ebs_accnomaindata where docdate='"+dataDate+"' and ishbcheck='"+ishbcheck+"') t "
				+" on (c.accno = t.accno) "
				+" when matched then " 
				+" update set c.sendaddress=t.sendaddress";
			dataProcess.insertOrUpdateDate(sql);
			EDataAction.MSGINFO="数据日期："+dataDate+", 信息：更新账户信息表中的发送地址信息完成";
		} catch (Exception e) {
			EDataAction.MSGINFO="数据日期："+dataDate+", 信息：更新账户信息表中的发送地址信息异常";
			tools.getDataLogUtils().infoUpdate(OperLogModuleDefine.DATAPROFLAG,EDataAction.MSGINFO,e,String.valueOf(OperLogModuleDefine.dataprocess),dataDate);
			throw new Exception(e.getMessage(), e);
		}
	}
	
	/**
	 * 将状态不正常和所属机构不对账的账户为不对账完成，这样这类账户就不会再生产对账单了
	 * @throws DaoException 
	 */
	public void updateIscheck(String dataDate) throws Exception{
		try {
			String sql="merge into EBS_BASICINFO b "+
					" using (select accno,accstate from ebs_maindata where accstate<>'0') t "+
					" on (b.accno = t.accno) "+
					" when matched then " +
					" update set b.ischeck='"+FinalConstant.isCheckMap.get("不对账")+"',b.accstate=t.accstate" +",b.remark='自动设置不对账' ";//+
					//		" where b.isspecile='0' ";
			dataProcess.insertOrUpdateDate(sql);
			
			//修改账户状态恢复正常后，设置成对账(modify 2016.9.19)
			sql="update ebs_basicinfo b set b.ischeck='"+FinalConstant.isCheckMap.get("对账")+"',b.remark='' where b.accstate='0' and b.remark='自动设置不对账' ";
			dataProcess.insertOrUpdateDate(sql);
			
			sql="update ebs_basicinfo b set b.ischeck='"+FinalConstant.isCheckMap.get("不对账")+"' " +",b.remark='自动设置不对账' "+
					" where b.idbank in( select idbank from param_organizafilter) ";
			dataProcess.insertOrUpdateDate(sql);
			
			EDataAction.MSGINFO="数据日期："+dataDate+", 信息：将状态不正常和所属机构不对账的账户为不对账完成";
		} catch (Exception e) {
			EDataAction.MSGINFO="数据日期："+dataDate+", 信息：将状态不正常和所属机构不对账的账户为不对账完成异常";
			tools.getDataLogUtils().infoUpdate(OperLogModuleDefine.DATAPROFLAG,EDataAction.MSGINFO,e,String.valueOf(OperLogModuleDefine.dataprocess),dataDate);
			throw new Exception(e.getMessage(), e);
		}
	}
	
	
	/**
	 * 对某一类账户对账周期的判定
	 * @param acccycle：账户类型
	 * @param date 格式为 201308
	 * @throws DaoException 
	 */
	public void updateAcccycle(String acccycle,String date) throws Exception{
		try {
			//根据账户类型（acccycle）的定制规则对所有账户进行账户类型的划分
			String[] resultPar = new String[]{"subjectno","minbal","maxbal","oneminaccrual","onemaxaccrual","totalminaccrual","totalmaxaccrual"};
			String sql = "select e.subjectno,e.minbal,e.maxbal,e.oneminaccrual,e.onemaxaccrual,e.totalminaccrual,e.totalmaxaccrual from  ebs_ruleofacccycle e where e.executeflog = '0' ";
			if(acccycle!=null&&acccycle!=""){
				sql += " and  e.acccycle='"+acccycle+"' ";
			}
			List<Map<String,Object>> list =dataProcess.queryObjectList(resultPar, sql);
			//将符合规则的账户的账户类型设置为对应的账户类型		
			sql ="update ebs_accnodetailcount c set c.acccycle = '"+acccycle+"' where c.acccycle is null and c.docdate='"+date+"' and ( ";
			for(Map<String,Object> m:list){
				String value = (String)m.get("subjectno");
				if(value!=null&&value!=""){
					sql+=" ( c.subjectno ='"+value+"' and ";
				}else {
					sql+=" (";
				}
				value = (String)m.get("minbal");
				if(value!=null&&value!=""){
					sql+=" c.credit>=cast('"+value+"' as float) ";
				}
				value = (String)m.get("maxbal");
				if(value!=null&&value!=""){
					sql+=" and c.credit<cast('"+value+"' as float) ";
				}
				
				if(acccycle.equals("4")){
					//四类账户:余额小于5000，或者(不是并且)月单笔最大发生额为nullCreditFlag，即该月无发生额
					//sql+=" and c.totalamount_month =cast('"+nullCreditFlag+"' as float) "+" and c.maxamount_month=cast('"+nullCreditFlag+"' as float) ";
					sql += " or (c.totalamount_month =cast('"+nullCreditFlag+"' as float) "+" and c.maxamount_month=cast('"+nullCreditFlag+"' as float) ) ";
				}
				else{
					value = (String)m.get("oneminaccrual");
					if(value!=null&&value!=""){
						sql+=" and c.maxamount_month>=cast( '"+value+"' as float) ";;
					}
					value = (String)m.get("onemaxaccrual");
					if(value!=null&&value!=""){
						sql+=" and c.maxamount_month<cast( '"+value+"' as float) ";
					}
					value = (String)m.get("totalminaccrual");
					if(value!=null&&value!=""){
						sql+=" and c.totalamount_month>=cast( '"+value+"' as float) ";
					}
					value = (String)m.get("totalmaxaccrual");
					if(value!=null&&value!=""){
						sql+=" and c.totalamount_month<cast( '"+value+"' as float)  ";
					}
				}
				sql+=") or";
			}
			sql =sql.substring(0, sql.length()-2)+" )";
			dataProcess.insertOrUpdateDate(sql);
		} catch (Exception e) {
			tools.getDataLogUtils().error(acccycle+"类账户判定出错",false);
			throw new Exception(e.getMessage(), e);
			
		}
	}
	
	/**
	 * 查询 PARAM_SYSBASE 表中的参数参数值
	 * @param parma 参数
	 * @return
	 */
	public String findSysbaseParam(String parma){
		return dataProcess.findSysbaseParam(parma);
	}
	
	
	/**
	 * 将满足总行对账规则的账户的数据迁移到临时对账单表中，并标示为总行对账的数据中
	 * @param date 日期
	 * @param detailcountWhere 拼接关于ebs_accnodetailcount的where语句
	 * @param basicinfoWhere 拼接关于ebs_basicinfo的where语句
	 */
	public void creatTempBillByBHCheck(String dataDate,String detailcountWhere,String basicinfoWhere)throws Exception{
		try{
			String checkDate = dataDate.substring(0,6);
			String sql="insert into ebs_accnomaindata (autoid,accno,credit,totalamount_month,maxamount_month,currency,sendmode,acccycle,sealaccno,acctype,"+
					"subjectno,opendate,docdate,idbank,idbranch,idcenter,bankname,custid,sendaddress,ishbcheck,linkman )"+
					"select ACCNOMAINDATA_AUTOID.NEXTVAL,info.accno,e.credit,e.totalamount_month,e.maxamount_month, "+
					"info.currency, info.sendmode,e.acccycle,info.accno,info.acctype,info.subjectno,info.opendate, "+
					"'"+dataDate+"',info.idbank,info.idbranch,info.idcenter,info.bankname,info.custid,info.sendaddress,'"+FinalConstant.isHBcheckMap.get("总行")+"',info.linkman "+
					"from ( select accno,credit,totalamount_month,maxamount_month,acccycle from ebs_accnodetailcount  where 1=1 and docdate ='"+checkDate+"' and acccycle is not null "+detailcountWhere+" ) e "+
					"inner join ebs_basicinfo info on info.accno=e.accno where 1=1 "+basicinfoWhere;
			dataProcess.insertOrUpdateDate(sql);
			tools.getDataLogUtils().info("将满足总行对账规则的账户的数据迁移到临时对账单表中完成",String.valueOf(OperLogModuleDefine.dataprocess),dataDate);
		}catch (Exception e) {
			tools.getDataLogUtils().infoUpdate(OperLogModuleDefine.DATAPROFLAG,"将满足总行对账规则的账户的数据迁移到临时对账单表中异常",e,String.valueOf(OperLogModuleDefine.dataprocess),dataDate);
			throw new Exception(e.getMessage(), e);
		}
	}
	
	
	/**
	 * 获取当天日期的前一天
	 * @param specifiedDay
	 * @return
	 */
	public String getSpecifiedDayBefore(String specifiedDay) {
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day - 1);
		
		String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c
				.getTime());
		return dayBefore;
	}
	
	/**
	 *  判断日期是否是一个月最后一天
	 * @param importDate
	 * @return
	 */
	public boolean isLastDay(String importDate) {
		String year = importDate.substring(0, 4);
		String month = importDate.substring(4, 6);
		String day = importDate.substring(6, 8);
		Calendar date = Calendar.getInstance();
		int yeari = Integer.parseInt(year);
		int monthi = Integer.parseInt(month);
		if (monthi == 1) {
			monthi = 12;
		}
		date.set(yeari, monthi - 1, 1);
		int maxDayOfMonth = date.getActualMaximum(Calendar.DAY_OF_MONTH);
		if (!String.valueOf(maxDayOfMonth).equals(day)) {
			return false;
		}
		return true;
	}
	
	
	/**
	 * 得到所有的科目号
	 * @return
	 */
	public Map<String,String> getSubjectNo(){
		Map<String,String> map = new HashMap<String, String>();
		String sql="select subnoc,memo from param_subnoc ";
		List<Map<String,Object>> list=dataProcess.queryObjectList(new String[]{"subnoc","memo"},sql);
		for(Map<String, Object> m:list){
			String subnoc = (String)m.get("subnoc");
			String memo = (String)m.get("memo");
			map.put(subnoc, memo);
		}
		return map;
	}
	
	
	/**
	 * 得到当前月份数据处理完成的日期
	 * @param dataDate
	 * @return
	 */
	public Map<String,String> getDataProcessDate(String dataDate){
		Map<String,String> dayMap= new TreeMap<String, String>();
		Map<String,String> allDay= getAllDay();
		
		String year=dataDate.substring(0,4);
		String month=dataDate.substring(4,6);
		Integer dayNum = Integer.parseInt(dataDate.substring(6,8))-1;
		for(int i=1;i<=dayNum;i++){
			String day=year+"-"+month+"-"+allDay.get(i+"");
			dayMap.put(day,day);
		}
		String sql="select opdate from ebs_basicinfolog where opdesc='"+OperLogModuleDefine.DATAMOVESUCCESS+"' and chnopmode='数据处理' and opcode='AUTO' and  substr(opdate,0,6)='"+dataDate.substring(0, 6)+"'";
		List<Map<String,Object>> list=dataProcess.queryObjectList(new String[]{"opdate"},sql);
		for(Map<String, Object> m:list){
			String opdate = (String)m.get("opdate");
			String yearTemp = opdate.substring(0,4);
			String monthTemp = opdate.substring(4,6);
			String dayTemp = opdate.substring(6,8);
			if(dayMap.get(dayTemp)==null||dayMap.get(dayTemp).equals("")){
				dayMap.remove(yearTemp+"-"+monthTemp+"-"+dayTemp);
			}
		}
		return dayMap;
	}
	
	
	public Map<String,String> getAllDay(){
		Map<String ,String> dayMap= new TreeMap<String, String>();
		dayMap.put("1","01");
		dayMap.put("2","02");
		dayMap.put("3","03");
		dayMap.put("4","04");
		dayMap.put("5","05");
		dayMap.put("6","06");
		dayMap.put("7","07");
		dayMap.put("8","08");
		dayMap.put("9","09");
		for(int i=10;i<=31;i++){
			dayMap.put(i+"",i+"");
		}
		return dayMap;
	}
	
	/**                           end      湘江银行数据处理业务                 end                                  **/

	
	public DataProcessServiceImpl getDataProcess() {
		return dataProcess;
	}

	public void setDataProcess(DataProcessServiceImpl dataProcess) {
		this.dataProcess = dataProcess;
	}

	public DataImportAuto getDataImport() {
		return dataImport;
	}

	public void setDataImport(DataImportAuto dataImport) {
		this.dataImport = dataImport;
	}

	public DataCheckAuto getDataCheck() {
		return dataCheck;
	}

	public void setDataCheck(DataCheckAuto dataCheck) {
		this.dataCheck = dataCheck;
	}

	public IPublicTools getTools() {
		return tools;
	}

	public void setTools(IPublicTools tools) {
		this.tools = tools;
	}


	/**
	 * --2期改造添加修改修改定期子账户账号
	 * @param dataDate
	 * @throws Exception 
	 */
	public void updateAccnoOfBasicinfo(String dataDate) throws Exception {
		try {
			String sql ="update ebs_basicinfo t "+
					" set t.accno = (select x.accno || '-' || x.accsonnum "+
                    "from ebs_AAPFZ0 x "+
                   "where x.accson = t.accno) "+
                   "where t.accno in "+
                   "(select xx.accson from ebs_AAPFZ0 xx where xx.accson = t.accno) ";
			dataProcess.insertOrUpdateDate(sql);
			EDataAction.MSGINFO="数据日期："+dataDate+", 信息：修改定期子账户信息完成";
		} catch (Exception e) {
			EDataAction.MSGINFO="数据日期："+dataDate+", 信息：修改定期子账户信息异常";
			tools.getDataLogUtils().infoUpdate(OperLogModuleDefine.DATAPROFLAG,EDataAction.MSGINFO,e,String.valueOf(OperLogModuleDefine.dataprocess),dataDate);
			throw new Exception(e.getMessage(), e);
		}
	}

	/**
	 * --2期改造添加 修改网银开立的验印账号
	 * @param dataDate
	 * @throws Exception 
	 */
	public void updateSealAccnoOfBasicForNet(String dataDate) throws Exception {
		try {
			String sql =" update ebs_basicinfo t "+
						" set t.sealaccno = (select x.hqaccno "+
						" from EBS_NETACRELATION x "+
						" where x.dqaccno = t.accno) "+
						" where t.accno in (select xx.dqaccno "+
						" from EBS_NETACRELATION xx "+
						" where xx.dqaccno = t.accno)"; 
			dataProcess.insertOrUpdateDate(sql);
			EDataAction.MSGINFO="数据日期："+dataDate+", 信息：修改网银开立定期账户验印账号完成";
		} catch (Exception e) {
			EDataAction.MSGINFO="数据日期："+dataDate+", 信息：修改网银开立定期账户验印账号异常";
			tools.getDataLogUtils().infoUpdate(OperLogModuleDefine.DATAPROFLAG,EDataAction.MSGINFO,e,String.valueOf(OperLogModuleDefine.dataprocess),dataDate);
			throw new Exception(e.getMessage(), e);
		}
		
	}

	/**
	 * --2期改造修改每天提供的ebs_maindata表中的账号，将子账户虚拟账号设置为总账户+序号的形式----不然出现上线子账户的信息无法匹配上，无法更新
	 * @param dataDate
	 * @throws Exception 
	 */
	public void updateAccnoOfMaindata(String dataDate) throws Exception {
		try {
			String sql ="update ebs_maindata t "+
					" set t.accno = (select x.accno || '-' || x.accsonnum "+
                    "from ebs_AAPFZ0 x "+
                   "where x.accson = t.accno) "+
                   "where t.accno in "+
                   "(select xx.accson from ebs_AAPFZ0 xx where xx.accson = t.accno) ";
			dataProcess.insertOrUpdateDate(sql);
			EDataAction.MSGINFO="数据日期："+dataDate+", 信息：修改账户信息账号完成";
		} catch (Exception e) {
			EDataAction.MSGINFO="数据日期："+dataDate+", 信息：修改账户信息账号异常";
			tools.getDataLogUtils().infoUpdate(OperLogModuleDefine.DATAPROFLAG,EDataAction.MSGINFO,e,String.valueOf(OperLogModuleDefine.dataprocess),dataDate);
			throw new Exception(e.getMessage(), e);
		}
	}

	/**
	 * --2期改造设置户名为“同业定期结算性存放”的账户，设置为不对账
	 * @param dataDate
	 * @throws Exception 
	 */
	public void updateIsCheckOfBasicinfo(String dataDate) throws Exception {
		try {
			String sql ="update ebs_basicinfo t set t.ischeck='"+FinalConstant.isCheckMap.get("不对账")+"' where t.accname='同业定期结算性存放'";
			dataProcess.insertOrUpdateDate(sql);
			EDataAction.MSGINFO="数据日期："+dataDate+", 信息：修改同业定期结算性存放账户为不对账完成";
			
			//更新参数表，提示今天的数据已经处迁移过了
			getDataProcess().insertOrUpdateDate("update PARAM_SYSBASE set SYSBASEVALUE = '"+dataDate+"' where SYSBASEID ='MOVEDATE'");
		} catch (Exception e) {
			EDataAction.MSGINFO="数据日期："+dataDate+", 信息：修改同业定期结算性存放账户为不对账异常";
			tools.getDataLogUtils().infoUpdate(OperLogModuleDefine.DATAPROFLAG,EDataAction.MSGINFO,e,String.valueOf(OperLogModuleDefine.dataprocess),dataDate);
			throw new Exception(e.getMessage(), e);
		}	
	}

	/**
	 * --2改造修改 针对修改后的定期子户的验印账号要设置为对应的主账号（非虚拟账号）,并设置定期总户不对账
	 * @param dataDate
	 * @throws Exception 
	 */
	public void updateSealAccnoOfBasicForDi(String dataDate) throws Exception {
		try {
//			String sql ="update ebs_basicinfo t "+
//						" set t.sealaccno = (select x.accson "+
//                        " from ebs_AAPFZ0 x "+
//                        " where x.accno || '-' || x.accsonnum = t.accno) "+
//                        " where t.accno in "+
//                        " (select xx.accno || '-' || xx.accsonnum "+
//                        " from ebs_AAPFZ0 xx "+
//                        " where xx.accno || '-' || xx.accsonnum = t.accno)";
			String sql ="update ebs_basicinfo t "+
					" set t.sealaccno = (select x.accno "+
                    " from ebs_AAPFZ0 x "+
                    " where x.accno || '-' || x.accsonnum = t.accno) "+
                    " where t.accno like '%-%' ";
			dataProcess.insertOrUpdateDate(sql);
			
			//定期总户不对账
			String s = "update EBS_BASICINFO b set b.ischeck='1',b.remark='定期总户不对账' " +
						" where b.accno in (select distinct accno from ebs_aapfz0)";
			dataProcess.insertOrUpdateDate(s);
			
			EDataAction.MSGINFO="数据日期："+dataDate+", 信息：修改定期子账户的验印账号完成";
		} catch (Exception e) {
			EDataAction.MSGINFO="数据日期："+dataDate+", 信息：修改定期子账户的验印账号异常";
			tools.getDataLogUtils().infoUpdate(OperLogModuleDefine.DATAPROFLAG,EDataAction.MSGINFO,e,String.valueOf(OperLogModuleDefine.dataprocess),dataDate);
			throw new Exception(e.getMessage(), e);
		}
	}
	
	/**
	 * --2期改造修改 更新交易明细中的定期子户的明细账号 将虚拟账号转换为主+序号形式
	 * @param dataDate
	 * @throws Exception 
	 */
	public void updateAccnoOfBillDetail(String dataDate) throws Exception {
		try {
			String sql="update ebs_dephist t "+
						" set t.ac_no = (select x.accno || '-' || x.accsonnum "+
						" from ebs_AAPFZ0 x "+
						" where x.accson = t.ac_no) "+
						" where t.ac_no in "+
						" (select xx.accson from ebs_AAPFZ0 xx where xx.accson = t.ac_no)";
			dataProcess.insertOrUpdateDate(sql);
			EDataAction.MSGINFO="数据日期："+dataDate+", 信息：更新交易明细中的定期子户的明细账号完成";
		} catch (Exception e) {
			EDataAction.MSGINFO="数据日期："+dataDate+", 信息：更新交易明细中的定期子户的明细账号异常";
			tools.getDataLogUtils().infoUpdate(OperLogModuleDefine.DATAPROFLAG,EDataAction.MSGINFO,e,String.valueOf(OperLogModuleDefine.dataprocess),dataDate);
			throw new Exception(e.getMessage(), e);
		}
	}

	/**
	 * --2期间改造 将每天提供的定期子户关系表保存为全量  为后期出问题可查
	 * @param dataDate
	 * @throws Exception 
	 */
	public void insertOrUpdateQLXTPF20(String dataDate) throws Exception {
		try {
			StringBuffer sqlbuff = new StringBuffer();
			sqlbuff.append("  merge into EBS_QLAAPFZ0 temp ");
			sqlbuff.append(" using (select * from EBS_AAPFZ0) tp ");
			sqlbuff.append(" on (temp.ACCSON = tp.ACCSON) ");
			sqlbuff.append(" when matched then ");
			sqlbuff.append(" update ");
			sqlbuff.append(" set temp.ACCNO     = tp.ACCNO, ");
			sqlbuff.append(" temp.ACCNOID   = tp.ACCNOID, ");
			sqlbuff.append(" temp.ACCSONNUM = tp.ACCSONNUM, ");
			sqlbuff.append(" temp.ACCSONID  = tp.ACCSONID, ");
			sqlbuff.append(" temp.ACCSTATE  = tp.ACCSTATE ");
			sqlbuff.append(" when not matched then ");
			sqlbuff.append(" insert ");
			sqlbuff.append(" (temp.ACCNO, ");
			sqlbuff.append(" temp.ACCNOID, ");
			sqlbuff.append(" temp.ACCSONNUM, ");
			sqlbuff.append(" temp.ACCSON, ");
			sqlbuff.append(" temp.ACCSONID, ");
			sqlbuff.append(" temp.ACCSTATE) ");
			sqlbuff.append(" values ");
			sqlbuff.append(" (tp.ACCNO, ");
			sqlbuff.append(" tp.ACCNOID, ");
			sqlbuff.append(" tp.ACCSONNUM, ");
			sqlbuff.append(" tp.ACCSON, ");
			sqlbuff.append(" tp.ACCSONID, ");
			sqlbuff.append(" tp.ACCSTATE) ");
			dataProcess.insertOrUpdateDate(sqlbuff.toString());
			EDataAction.MSGINFO="数据日期："+dataDate+", 信息：保存定期子户关系表完成";
		} catch (Exception e) {
			EDataAction.MSGINFO="数据日期："+dataDate+", 信息：保存定期子户关系表异常";
			tools.getDataLogUtils().infoUpdate(OperLogModuleDefine.DATAPROFLAG,EDataAction.MSGINFO,e,String.valueOf(OperLogModuleDefine.dataprocess),dataDate);
			throw new Exception(e.getMessage(), e);
		}
	}
	
}
