/**
 * 创建于:2013-9-20
 * 版权所有(C) 2013 深圳市银之杰科技股份有限公司
 * @author lixiangjun
 * @version 1.1
 */
package com.yzj.ebs.edata.action;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.opensymphony.xwork2.ActionSupport;
import com.yzj.ebs.common.IPublicTools;
import com.yzj.ebs.common.OperLogModuleDefine;
import com.yzj.ebs.edata.service.NetProcessServer;
import com.yzj.ebs.edata.service.impl.EdataServiceAuto;
import com.yzj.ebs.util.FinalConstant;

/**
 * 数据校验、导入、处理逻辑处理action
 * 
 * @author lixiangjun
 * 
 */
public class EDataAction extends ActionSupport {
	
	private static final long serialVersionUID = 5848224370673573107L;
	
	private IPublicTools tools;
	
	private EdataServiceAuto dataServiceAuto; // 业务处理类
	
	private Map<String,String> acccycleMap;
	private Map<String,String> sendModeMap;
	private Map<String,String> subNoMap;
	private Map<String,String> daysMap;
	private String dataDate;// 数据日期
	private File utblbrcdFile;
	private File userFile;
	private File innerbasicinfoFile;
	private File maindataFile;
	private File acctlistFile;
	private File dephistFile;
	private File knpExrtFile;
	private File xdckFile;
	private String fileType = ".dat";
	//上传的文件路径
	//private String uploadDir = ServletActionContext.getServletContext().getRealPath("\\dataFileDir")+"\\";
	private SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private NetProcessServer netProcessServer;
	
	public static String MSGINFO="";
	/**
	 * 数据处理页面初始化
	 * @return
	 */
	public String edataProcess(){
		MSGINFO="";
		dataDate=null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		daysMap=dataServiceAuto.getDataProcessDate(sdf.format(new Date()));
		return "showEdatePage";
	}
	
	/**
	 * 进入下载页面
	 * @return String
	 */
	public String openUpLoadFile(){
		utblbrcdFile =null;
		userFile=null;
		innerbasicinfoFile=null;
		maindataFile=null;
		xdckFile=null;
		dephistFile=null;
		knpExrtFile=null;
		acctlistFile=null;
		return "openUpLoadPage";
	}
	
	/**
	 * 进入总行对账页面
	 * @return String
	 */
	public String openHBCheck(){
		dataDate=null;
		acccycleMap=FinalConstant.acccycleName;
		sendModeMap=FinalConstant.sendModelName;
		subNoMap=dataServiceAuto.getSubjectNo();
		return "openHBCheck";
	}
	
	
	/**
	 * 文件上传
	 */
	public void upLoadFile(){
		String result = "";
		//得到文件上传的根路径
		String uploadDir =dataServiceAuto.findSysbaseParam("DATAFILEDIR");
		//得到数据文件推送完成结束标示
		String okFlag=dataServiceAuto.findSysbaseParam("OK");
		if(uploadDir!=null&&uploadDir!=""){
			File uploadDirFile = new File(uploadDir);
			if(!uploadDirFile.exists()){
				uploadDirFile.mkdir();
			}
			String year=dataDate.substring(0,4);
			String mouth=dataDate.substring(4,6);
			String day=dataDate.substring(6,8);
			
			//拼接文件上传的路径
			String path =uploadDir+"/"+year+"/"+mouth+"/"+day;
			try {
				List<String> files = new ArrayList<String>();
				Map<String,File> map = new HashMap<String, File>();
				if(utblbrcdFile!=null){
					map.put("UTBLBRCD",utblbrcdFile);
					files.add("UTBLBRCD");
				}
				if(userFile!=null){
					map.put("KUB_USER",userFile);
					files.add("KUB_USER");
				}
				if(innerbasicinfoFile!=null){
					map.put("INNERBASICINFO",innerbasicinfoFile);
					files.add("INNERBASICINFO");
				}
				if(maindataFile!=null){
					map.put("MAINDATA",maindataFile);
					files.add("MAINDATA");
				}
				if(xdckFile!=null){
					map.put("XDCKLIST",xdckFile);
					files.add("XDCKLIST");
				}
				if(acctlistFile!=null){
					map.put("ACCTLIST",acctlistFile);
					files.add("ACCTLIST");
				}
				if(knpExrtFile!=null){
					map.put("KNP_EXRT",knpExrtFile);
					files.add("KNP_EXRT");
				}
				
				if(dephistFile!=null){
					map.put("DEPHIST",dephistFile);
					files.add("DEPHIST");
				}
				
				File dayfileDir= new File(path);
				if(!dayfileDir.exists()){
					dayfileDir.mkdirs();
				}
				
				File okFileTemp = new File(path+"/"+okFlag);
				if(okFileTemp.exists()){
					result="数据文件已存在！";
				}else{
					//文件上传到服务器指定的文件夹下
					for(String fileName:files){
						File fileTemp= map.get(fileName);
						//文件上传到服务器
						boolean flag = fileUpLoad(path,fileName+fileType,fileTemp);
						if(!flag){
							result+=(fileName+" 、");
						}
					}
				}
				
				//检查目录的文件个数是否齐全，如果齐全，则新建一个OK文件作为标示
				String checkFlag = fileCheck(path);
				if(checkFlag==""){
					File okFile = new File(path+"/"+okFlag);
					if(!okFile.exists()){
						okFile.createNewFile();
					}
				}
				//上传操作完成后，将文件清空
				utblbrcdFile =null;
				userFile=null;
				innerbasicinfoFile=null;
				maindataFile=null;
				xdckFile=null;
				dephistFile=null;
				knpExrtFile=null;
				acctlistFile=null;
				
				if(result==""){
					tools.ajaxResult("文件上传成功！");
				}else if(result.contains("、")){
					tools.ajaxResult("文件 "+result.substring(0, result.length()-1)+"上传失败！");
				}else{
					tools.ajaxResult(result);
				}
			} catch (Exception e) {
				tools.getDataLogUtils().error("文件上传异常",e,String.valueOf(OperLogModuleDefine.dataprocess),dataDate);
				tools.ajaxResult("文件上传异常,请重新上传！");
			}finally{
				dataServiceAuto.getDataProcess().getEEdataDao().closeConnProcess();
			}
		}else{
			tools.getDataLogUtils().error("存储目录不存在！",String.valueOf(OperLogModuleDefine.dataprocess),dataDate);
			tools.ajaxResult("存储目录不存在,请在\"系统参数管理\"模块中检测DATAFILEDIR(数据文件存储目录)是否配置！");
		}
	}
	
	/**
	 * 数据文件校验
	 */
	public void dataCheck()
	{
		String uploadDir =dataServiceAuto.findSysbaseParam("DATAFILEDIR");
		String okFlag=dataServiceAuto.findSysbaseParam("OK");
		String year=dataDate.substring(0,4);
		String mouth=dataDate.substring(4,6);
		String day=dataDate.substring(6,8);
		
		String path =uploadDir+"/"+year+"/"+mouth+"/"+day;
    	File fileDir= new File(path);
    	if(!fileDir.exists()){
    		returnProcessInfo("数据日期："+dataDate+", 信息："+dataDate+"的数据文件不存在");
    	}else{
    		File okFile = new File(path+"/"+okFlag);
    		if(okFile.exists()){
    			//对数据文件进行校验
    			tools.getDataLogUtils().info("开始进行数据校验",String.valueOf(OperLogModuleDefine.dataprocess),dataDate);
    			String checkResult = dataServiceAuto.checkDataFile(dataDate);
				if(checkResult=="success"){
					tools.getDataLogUtils().info("数据校验成功",String.valueOf(OperLogModuleDefine.dataprocess),dataDate);
					returnProcessInfo("数据日期："+dataDate+", 信息："+dataDate+"的数据校验成功");
				}
				else{
					tools.getDataLogUtils().info("数据日期："+dataDate+", 信息："+checkResult,String.valueOf(OperLogModuleDefine.dataprocess),dataDate);
					returnProcessInfo("数据日期："+dataDate+", 信息："+checkResult);
				}
        	}else{
        		String flag =fileCheck(path);
        		if(flag!=""){
        			returnProcessInfo("数据日期："+dataDate+", 信息："+dataDate+"的数据 缺失 "+flag+"");
        		}else{
        			returnProcessInfo("数据日期："+dataDate+", 信息："+dataDate+"的数据 缺失 OK 标示文件");
        		}
        	}
    	}
		dataServiceAuto.getDataProcess().getEEdataDao().closeConnProcess();
	}
	
	
	/**
	 * 一键数据导入迁移
	 */
	public void oneKeyDataImport(){
		dataImport();
		dataMove();
	}
	
	
	/**
	 * 数据导入
	 */
	public void dataImport(){
		String uploadDir =dataServiceAuto.findSysbaseParam("DATAFILEDIR");
		String okFlag=dataServiceAuto.findSysbaseParam("OK");
		String year=dataDate.substring(0,4);
		String mouth=dataDate.substring(4,6);
		String day=dataDate.substring(6,8);
		
		String path =uploadDir+"/"+year+"/"+mouth+"/"+day;
		
		File fileDir= new File(path);
    	if(!fileDir.exists()){
    		MSGINFO="数据日期："+dataDate+", 信息："+dataDate+" 的数据文件不存在";
    	}else{
    		File okFile = new File(path+"/"+okFlag);
    		if(okFile.exists()){
    			try {
    				String sql=" ebs_basicinfolog  where opdate='"+dataDate+"' and opdesc='"+OperLogModuleDefine.DATAPROFLAG+"'";
    				Long couFlag=dataServiceAuto.getDataProcess().queryTableCount(sql);
    				if(couFlag==0){
    					//查询最近数据导入的时间，如果当天的数据文件以及导入，则不需要再导入
    					String importdate = dataServiceAuto.findSysbaseParam("IMPORTDATE");
    					sql=" ebs_basicinfolog  where opdate='"+dataDate+"' and opdesc='"+OperLogModuleDefine.DATAIMPSUCCESS+"'";
    					couFlag=dataServiceAuto.getDataProcess().queryTableCount(sql);
    					if(!importdate.equals(dataDate)&&couFlag==0){
    						
    						//将数据平台推送过来的文件导入到数据库中间表中
    						dataServiceAuto.dataImport(dataDate);
    					}else{
    						MSGINFO="数据日期："+dataDate+", 信息："+dataDate+"的数据已经导入，请勿重复导入";
    					}
    				}else{
    					MSGINFO="数据日期："+dataDate+", 信息："+dataDate+"的数据正在处理中,请勿重复处理";
    				}
				} catch (Exception e) {
					MSGINFO="数据日期："+dataDate+", 信息："+"人工数据导入异常,请校验数据文件的正确性后重新导入";
				}finally{
					dataServiceAuto.getDataProcess().getEEdataDao().closeConnProcess();
				}
    		}else{
    			MSGINFO="数据日期："+dataDate+", 信息："+dataDate+"的数据文件不完整，请校验后再执行数据导入";
    		}
    	}
	}

	/**
	 * 数据迁移到正式表
	 */
	public void dataMove(){
		try{
			String importdate = dataServiceAuto.findSysbaseParam("IMPORTDATE");
			if(importdate.equals(dataDate)){
				String sql=" ebs_basicinfolog  where opdate='"+dataDate+"' and opdesc='"+OperLogModuleDefine.DATAPROFLAG+"'";
				Long couFlag=dataServiceAuto.getDataProcess().queryTableCount(sql);
				if(couFlag==0){
					String movedate = dataServiceAuto.findSysbaseParam("MOVEDATE");
					sql=" ebs_basicinfolog  where opdate='"+dataDate+"' and opdesc='"+OperLogModuleDefine.DATAMOVESUCCESS+"'";
					couFlag=dataServiceAuto.getDataProcess().queryTableCount(sql);
					if(!movedate.equals(dataDate)&&couFlag==0){
						tools.getDataLogUtils().info("数据日期："+dataDate+", 信息：开始进行数据迁移",String.valueOf(OperLogModuleDefine.dataprocess),dataDate);
						
						tools.getDataLogUtils().info(OperLogModuleDefine.DATAPROFLAG,String.valueOf(OperLogModuleDefine.dataprocess),dataDate);
						
						//--2期改造修改每天提供的ebs_maindata表中的账号，将子账户虚拟账号设置为总账户+序号的形式----不然出现上线子账户的信息无法匹配上，无法更新
						dataServiceAuto.updateAccnoOfMaindata(dataDate);
						//--2期改造添加修改修改定期子账户账号
						dataServiceAuto.updateAccnoOfBasicinfo(dataDate);
						
						
						//将机构人员信息新增或更新到WF框架中
						dataServiceAuto.disposeUserAndOrg();
						
						//设置协定存款用户的科目号
						dataServiceAuto.updateDxckAccno(dataDate);
						
						//然后将接口表中的账户信息更新到到对应的正式表中
						dataServiceAuto.accInfoFilter(dataDate);
						
						//--2改造修改 针对修改后的定期子户的验印账号要设置为对应的主账号,并设置定期总户不对账
						dataServiceAuto.updateSealAccnoOfBasicForDi(dataDate);
						
						//将状态不正常和所属机构不对账的账户为不对账完成
						dataServiceAuto.updateIscheck(dataDate);
						
						//在临时表中踢出计息的交易数据
						//dataServiceAuto.deleteAccrualDate(dataDate);
						
						//如果日期为每月的一号，则清除账户信息表中的数据
						if(dataDate.subSequence(6,8).equals("01")){
							dataServiceAuto.deletebillDetailByMonth(dataDate.substring(4,6));
						}
						
						//--2期改造修改 更新交易明细中的定期子户的明细账号 将虚拟账号转换为主+序号形式
						dataServiceAuto.updateAccnoOfBillDetail(dataDate);
						//然后将接口表中的账户存款信息更新到对应的正式表中
						dataServiceAuto.billDetailFilter(dataDate);
						
						//将接口表中的内部账户发生额明细迁移到对应的正式表中
						dataServiceAuto.innerAccnoDetailFilter(dataDate);
						
						//--2期改造添加 修改网银开立的验印账号
						dataServiceAuto.updateSealAccnoOfBasicForNet(dataDate);
						//--2期改造设置户名为“同业定期结算性存放”的账户，设置为不对账
						dataServiceAuto.updateIsCheckOfBasicinfo(dataDate);
						
						//--2期间改造 将每天提供的定期子户关系表保存为全量  为后期出问题可查
						dataServiceAuto.insertOrUpdateQLXTPF20(dataDate);
						tools.getDataLogUtils().infoUpdate(OperLogModuleDefine.DATAPROFLAG,OperLogModuleDefine.DATAMOVESUCCESS,null,String.valueOf(OperLogModuleDefine.dataprocess),dataDate);

					}else{
						MSGINFO="数据日期："+dataDate+", 信息："+dataDate+" 的数据文件已迁移到正式表";
					}
				}else{
					MSGINFO="数据日期："+dataDate+", 信息："+dataDate+" 的数据正在处理中,请勿重复处理";
				}
			}else{
				MSGINFO="数据日期："+dataDate+", 信息："+dataDate+" 的数据文件未完成导入，请在数据导入完成后再进场数据迁移";
			}
		}catch (Exception e) {
			MSGINFO="数据日期："+dataDate+", 信息："+dataDate+"的数据迁移异常,"+e.getMessage();
			tools.getDataLogUtils().infoUpdate(OperLogModuleDefine.DATAPROFLAG,MSGINFO,null,String.valueOf(OperLogModuleDefine.dataprocess),dataDate);
		}finally{
			dataServiceAuto.getDataProcess().getEEdataDao().closeConnProcess();
		}
	}
	
	
	/**
	 * 数据处理
	 */
	public void dataProcess(){
		try {
			String movedate = dataServiceAuto.findSysbaseParam("MOVEDATE");
			String year =dataDate.substring(0,4);
			String month =dataDate.substring(4,6);
			String day =dataDate.substring(6,8);
			//如果当天的数据已经完成迁移，才能进行数据处理
	    	if(movedate.equals(dataDate)){
				//如果日期为本月的最后一天，需要生成对账单
				if (dataServiceAuto.isLastDay(dataDate)) {
					//检查数据导入情况
					String imNotFinish = year+month+"当月有数据未导入，请将未导入的数据导入完成后再进行数据处理";
					String imFinish = year+month+"当月数据导入全部完成";
					
					//存在检查数据导入日志记录则更新，没有则插入
					String sql=" ebs_basicinfolog where opdate='"+dataDate+"' and (opdesc='"+imFinish+"' or opdesc='"+imNotFinish+"')";
					Long couFlag=dataServiceAuto.getDataProcess().queryTableCount(sql);
					if(couFlag==0){
						tools.getDataLogUtils().info(imNotFinish,String.valueOf(OperLogModuleDefine.dataprocess),dataDate);
					}
					//统计当月完成数据导入的天数
					sql=" ebs_basicinfolog where substr(opdate,1,6)='"+year+month+"' and opdesc='"+OperLogModuleDefine.DATAMOVESUCCESS+"'";
					couFlag=dataServiceAuto.getDataProcess().queryTableCount(sql);
					int dayNum = Integer.parseInt(day);
					//如果当月每天的数据导入都完成了，则进行数据处理
					if(couFlag==dayNum){
						//更新检查数据导入日志记录
						tools.getDataLogUtils().infoUpdate(imNotFinish,imFinish,null,String.valueOf(OperLogModuleDefine.dataprocess),dataDate);
						sql=" ebs_basicinfolog  where opdate='"+dataDate+"' and opdesc='"+OperLogModuleDefine.DATAPROFLAG+"'";
						couFlag=dataServiceAuto.getDataProcess().queryTableCount(sql);
						if(couFlag==0){
							String processdate = dataServiceAuto.findSysbaseParam("PROCESSDATE");
							sql=" ebs_basicinfolog  where opdate='"+dataDate+"' and opdesc='"+OperLogModuleDefine.DATAPROSUCCESS+"'";
							couFlag=dataServiceAuto.getDataProcess().queryTableCount(sql);
							if(!processdate.equals(dataDate)&&couFlag==0){
								tools.getDataLogUtils().info(OperLogModuleDefine.DATAPROFLAG,String.valueOf(OperLogModuleDefine.dataprocess),dataDate);
								dataServiceAuto.dateProcess(year,month,day,FinalConstant.isHBcheckMap.get("正常"));	
								//给网银发送对账单
								netProcessServer.fetchEbillFromEbill();
								MSGINFO="数据日期："+dataDate+", 信息："+"数据处理完成，处理日期为： "+dataDate;
								tools.getDataLogUtils().infoUpdate(OperLogModuleDefine.DATAPROFLAG,OperLogModuleDefine.DATAPROSUCCESS,null,String.valueOf(OperLogModuleDefine.dataprocess),dataDate);
							}else{
								MSGINFO="数据日期："+dataDate+", 信息：当前月的数据已经处理过了";
							}
						}else{
							MSGINFO="数据日期："+dataDate+", 信息："+dataDate+" 的数据正在处理中,请勿重复处理";
						}
					}else{
						MSGINFO="数据日期："+dataDate+", 信息："+year+month+" 当月有数据未导入，请将未导入的数据导入完成后再进行数据处理";
						//当月有数据未导入，则进行日志记录更新
						tools.getDataLogUtils().infoUpdate(imFinish,imNotFinish,null,String.valueOf(OperLogModuleDefine.dataprocess),dataDate);
					}
				}else{
					MSGINFO="数据日期："+dataDate+", 信息：当前日期不是出账单时间,故无法进行数据处理";
				}
			}else{
				MSGINFO="数据日期："+dataDate+", 信息："+dataDate+" 的数据未迁移成功，请在数据迁移完成后再进行数据处理";
			}
		} catch (Exception e) {
			MSGINFO="数据日期："+dataDate+", 信息："+"数据处理异常,"+e.getMessage();
			tools.getDataLogUtils().infoUpdate(OperLogModuleDefine.DATAPROFLAG,MSGINFO,null,String.valueOf(OperLogModuleDefine.dataprocess),dataDate);
		}
	}
	
	
	
	/**
	 * 执行总行对账
	 */
	public void executeHBCheck(){
		try {
			String sql=" ebs_basicinfolog  where opdate='"+dataDate+"' and opdesc='"+OperLogModuleDefine.DATAPROFLAG+"'";
    		Long couFlag=dataServiceAuto.getDataProcess().queryTableCount(sql);
    		if(couFlag==0){
    			String processdate = dataServiceAuto.findSysbaseParam("PROCESSDATE");
    			sql=" ebs_basicinfolog  where opdate='"+dataDate+"' and opdesc='"+OperLogModuleDefine.DATAPROSUCCESS+"'";
    			couFlag=dataServiceAuto.getDataProcess().queryTableCount(sql);
    			if(!processdate.equals(dataDate)||couFlag==0){
    				
    				tools.getDataLogUtils().info(OperLogModuleDefine.DATAPROFLAG,String.valueOf(OperLogModuleDefine.dataprocess),dataDate);
    				String year= dataDate.substring(0,4);
    				String month = dataDate.substring(4,6);
    				
    				//拼接总行对账条件
    				String detailcountWhere=" ";
    				String basicinfoWhere =" ";
    				String checkRule=tools.getParameter("checkRule");
    				String sendModeSql="";
    				//根据总行对账规则，拼接相应的条件
    				if(checkRule.equals("1")){
    					String sendMode =tools.getParameter("sendMode");
    					if(sendMode.length()>0){
    						sendModeSql="(";
    						String[] str=sendMode.split(",");
    						for(String s:str){
    							sendModeSql +=" info.sendmode ='"+s+"' or";
    							if(s.equals(FinalConstant.sendModelMap.get("面对面"))){
    								sendModeSql+=" info.sendmode='"+FinalConstant.sendModelMap.get("网银")+"' or";
    							}
    						}
    						sendModeSql=(sendModeSql.substring(0, sendModeSql.length()-2)+") ");
    					}
    					if(sendModeSql.length()>0){
    						detailcountWhere = " and "+sendModeSql;
    					}
    					String acccycle =tools.getParameter("acccycle");
    					String acccycleSql="";
    					if(acccycle.length()>0){
    						acccycleSql=" (";
    						String[] str=acccycle.split(",");
    						for(String s:str){
    							acccycleSql +=" acccycle ='"+s+"' or";
    						}
    						acccycleSql=(acccycleSql.substring(0, acccycleSql.length()-2)+") ");
    					}
    					
    					if(acccycleSql.length()>0){
    						basicinfoWhere = " and "+acccycleSql;
    					}
    				}else if(checkRule.equals("2")){
    					String creditB =tools.getParameter("creditB");
    					String creditE =tools.getParameter("creditE");
    					String maxtracebalB =tools.getParameter("maxtracebalB");
    					String maxtracebalE =tools.getParameter("maxtracebalE");
    					String sumtracebalB =tools.getParameter("sumtracebalB");
    					String sumtracebalE =tools.getParameter("sumtracebalE");
    					
    					detailcountWhere = " and credit>=cast('"+creditB+"' as float) and credit <cast('"+creditE+"' as float)  "+
    							"and totalamount_month>=cast('"+sumtracebalB+"' as float) and totalamount_month <cast('"+sumtracebalE+"' as float)  "+
    							"and maxamount_month>=cast('"+maxtracebalB+"' as float) and maxamount_month <cast('"+maxtracebalE+"' as float)  ";
    					
    					String subjectNo =tools.getParameter("subjectNo");
    					String[] str=subjectNo.split(",");
    					String subjectnoSql="";
    					if(subjectNo.length()>0){
    						subjectnoSql="(";
    						for(String s:str){
    							subjectnoSql +="substr(info.subjectno,1,3) ='"+s+"' or";
    						}
    						subjectnoSql=(subjectnoSql.substring(0, subjectnoSql.length()-2)+") ");
    					}
    					basicinfoWhere=" and "+subjectnoSql;
    				}
    				
    				//对网银账号进行处理
    				dataServiceAuto.procOnlineBankAccno(dataDate);
    				
    				//根据发送方式，修改发送地址(面对面和柜台)
    				dataServiceAuto.updateSendAddress(dataDate);
    				
    				//如果发送方式为邮寄，则将发送地址修改为客户地址
    				dataServiceAuto.updateSendAddressByEMS(dataDate);
    				
    				//将当前日期的账户的余额信息更新到账户明细统计表中
    				dataServiceAuto.updateAccCredit(year,month,dataDate);
    				
    				//更新月累计和账户的账户类型的分类
    				dataServiceAuto.accCycleJudge(year,month,dataDate);
    				
    				//修改企业网银的账户类型修改为一类
    				dataServiceAuto.updateOnlineBankAcccycle(year,month,dataDate);
    				
    				//如果对账方式为面对面，则将其账户类型修改为一类
    				dataServiceAuto.updateFaceToFaceAccno(year,month,dataDate);
    				
    				//将满足规则的账户添加到临时对账单表中
    				dataServiceAuto.creatTempBillByBHCheck(dataDate,detailcountWhere,basicinfoWhere);
    				
    				//总行规则如果包含面对面的发送方式，在季点时，将一类账户余额为200W以上的账户也插入到临时对账单中，并修改其发送方式为面对面
    				if(Integer.parseInt(month)%3==0){
    					if(sendModeSql.contains(FinalConstant.sendModelMap.get("面对面"))){
    						dataServiceAuto.updateSendModeByHR(dataDate);
    						dataServiceAuto.disposeOtherAccNo(dataDate,FinalConstant.isHBcheckMap.get("总行"));
    					}
    				}			
    				
    				//对总行临时对账单进行分类并生成对账单编号
    				dataServiceAuto.createTempBill(dataDate,FinalConstant.isHBcheckMap.get("总行"));
    				
    				//生成总行对账单
    				dataServiceAuto.createCheckmaindata(dataDate,FinalConstant.isHBcheckMap.get("总行"));
    				
    				//根据对账单中账户的发送地址信息反向更新账户信息表中对应的发送地址信息
    				dataServiceAuto.updateBasicInfoSendAddress(dataDate,FinalConstant.isHBcheckMap.get("总行"));
    				
    				tools.getDataLogUtils().infoUpdate(OperLogModuleDefine.DATAPROFLAG,OperLogModuleDefine.DATAPROSUCCESS,null,String.valueOf(OperLogModuleDefine.dataprocess),dataDate);
    				
    				tools.ajaxResult("总行对账处理完成,对账日期为"+dataDate+",请去 {对账单信息查询} 模块查询相关数据!");
    			}
    			else{
    				tools.ajaxResult("日期为"+dataDate+"已经对账完成，请勿重复对账！");
    			}
    		}else{
				MSGINFO="数据日期："+dataDate+", 信息："+dataDate+" 的数据正在处理中,请勿重复处理";
			}
		} catch (Exception e) {
			tools.ajaxResult("总行对账异常!"+e.getMessage());
			tools.getDataLogUtils().infoUpdate(OperLogModuleDefine.DATAPROFLAG,"总行对账异常",e,String.valueOf(OperLogModuleDefine.dataprocess),dataDate);
		}finally{
			dataServiceAuto.getDataProcess().getEEdataDao().closeConnProcess();
		}
	}
	
	
	/**
	 * 文件个数校验
	 * @param dir 数据文件的目录
	 * @return
	 */
	public String fileCheck(String dir){
		String flag = "";
		for(String fileName:FinalConstant.FILES_NAME){
			File temp = new File(dir+"/"+fileName+fileType);
			if(!temp.exists()){
				flag +=(fileName+"文件   、");
			}
		}
		if(flag!=""){
			flag= flag.substring(0, flag.length()-1);
		}
		return flag;
	}
	
	/**
	 * 文件上传
	 * @param path 上传路径
	 * @param fileName 文件名字
	 * @param tempfile 需要上传的文件
	 * @return
	 */
	public boolean fileUpLoad(String path,String fileName,File tempfile){
		//如果文件存在，就删除，重新上传
    	BufferedInputStream bis = null;  
    	BufferedOutputStream bos = null;
    	File upLoadfile=null;
    	try {
    		upLoadfile = new File(path+"/"+fileName);
    		if(upLoadfile.exists()){
    			upLoadfile.delete();
    		}
    		FileInputStream fis = new FileInputStream(tempfile);
    		FileOutputStream fos = new FileOutputStream(upLoadfile);
    		bis = new BufferedInputStream(fis);  
    		bos = new BufferedOutputStream(fos); 
    		byte[] b = new byte[1024];  
    		int len = -1;  
    		while((len = bis.read(b)) != -1){  
    			bos.write(b, 0, len);  
    		}
    		fos.flush();
    		bis.close();
    		bos.close(); 
			return true;
		} catch (Exception e) {
			//如果失败的文件存在，删除失败的文件
			if(upLoadfile!=null){
				if(upLoadfile.exists()){
					upLoadfile.delete();
				}
			}
			tools.getDataLogUtils().error(fileName+"上传失败",e,String.valueOf(OperLogModuleDefine.dataprocess),dataDate);
			return false;
		}finally {  
            try {
	            if(bis!=null) {
	            	bis.close();  
	            }
	            if(bos!=null){
	            	bos.close(); 
	            }
            } catch (IOException e) {
            	tools.getDataLogUtils().error("关闭流异常",e,String.valueOf(OperLogModuleDefine.dataprocess),dataDate);
            }  
		}
	}
	
	/**
	 * 将提升信息返回到前台页面 
	 * @param info 需要返回的信息
	 */
	public void returnProcessInfo(String info){
		tools.ajaxResult(sf.format(new Date())+"     "+info+"！");
	}
	
	/**
	 * 得到人工数据处理的提示信息
	 */
	public void getPrintfInfo(){
		if(MSGINFO!=null&&MSGINFO!=""){
			String info=MSGINFO;
			MSGINFO="";
			returnProcessInfo(info);
		}else{
			tools.ajaxResult("");
		}
	}
	
	/**
	 * 总行提示信息
	 */
	public void getHBCheckInfo(){
		tools.ajaxResult("");
	}
	
	public Map<String, String> getAcccycleMap() {
		return acccycleMap;
	}

	public void setAcccycleMap(Map<String, String> acccycleMap) {
		this.acccycleMap = acccycleMap;
	}
	
	public Map<String, String> getSendModeMap() {
		return sendModeMap;
	}

	public void setSendModeMap(Map<String, String> sendModeMap) {
		this.sendModeMap = sendModeMap;
	}
	
	public Map<String, String> getSubNoMap() {
		return subNoMap;
	}

	public void setSubNoMap(Map<String, String> subNoMap) {
		this.subNoMap = subNoMap;
	}

	public File getKnpExrtFile() {
		return knpExrtFile;
	}

	public void setKnpExrtFile(File knpExrtFile) {
		this.knpExrtFile = knpExrtFile;
	}

	public File getAcctlistFile() {
		return acctlistFile;
	}

	public void setAcctlistFile(File acctlistFile) {
		this.acctlistFile = acctlistFile;
	}

	public File getUtblbrcdFile() {
		return utblbrcdFile;
	}

	public void setUtblbrcdFile(File utblbrcdFile) {
		this.utblbrcdFile = utblbrcdFile;
	}

	public File getUserFile() {
		return userFile;
	}

	public void setUserFile(File userFile) {
		this.userFile = userFile;
	}

	public File getInnerbasicinfoFile() {
		return innerbasicinfoFile;
	}

	public void setInnerbasicinfoFile(File innerbasicinfoFile) {
		this.innerbasicinfoFile = innerbasicinfoFile;
	}
	
	public File getMaindataFile() {
		return maindataFile;
	}

	public void setMaindataFile(File maindataFile) {
		this.maindataFile = maindataFile;
	}
	
	public File getXdckFile() {
		return xdckFile;
	}

	public void setXdckFile(File xdckFile) {
		this.xdckFile = xdckFile;
	}

	public File getDephistFile() {
		return dephistFile;
	}

	public void setDephistFile(File dephistFile) {
		this.dephistFile = dephistFile;
	}
	
	public String getDataDate() {
		return dataDate;
	}

	public void setDataDate(String dataDate) {
		this.dataDate = dataDate;
	}
	
	public IPublicTools getTools() {
		return tools;
	}

	public void setTools(IPublicTools tools) {
		this.tools = tools;
	}

	public EdataServiceAuto getDataServiceAuto() {
		return dataServiceAuto;
	}

	public void setDataServiceAuto(EdataServiceAuto dataServiceAuto) {
		this.dataServiceAuto = dataServiceAuto;
	}

	public Map<String, String> getDaysMap() {
		return daysMap;
	}

	public void setDaysMap(Map<String, String> daysMap) {
		this.daysMap = daysMap;
	}

	public NetProcessServer getNetProcessServer() {
		return netProcessServer;
	}

	public void setNetProcessServer(NetProcessServer netProcessServer) {
		this.netProcessServer = netProcessServer;
	}
	
	
}
