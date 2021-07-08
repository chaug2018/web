/**
 * 创建于:2013-9-20
 * 版权所有(C) 2013 深圳市银之杰科技股份有限公司
 * @author lixiangjun
 * @version 1.1
 */
package com.yzj.ebs.edata.job;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.yzj.ebs.common.IPublicTools;
import com.yzj.ebs.common.OperLogModuleDefine;
import com.yzj.ebs.edata.service.DataProcessUtil;
import com.yzj.ebs.edata.service.NetProcessServer;
import com.yzj.ebs.edata.service.impl.EdataServiceAuto;
import com.yzj.ebs.util.FinalConstant;
import com.yzj.wf.as.common.IJobRegister;
import com.yzj.wf.as.common.JobLogic;
import com.yzj.wf.as.entity.BusinessInfo;

public class AutoImportData extends JobLogic {
	private static final String AutoType = "AUTOIMPORT";
	
	private DateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
	
	private IJobRegister jobRegister;
	
	private EdataServiceAuto dataServiceAuto;

	private IPublicTools tools;

	public DataProcessUtil dataUtil = new DataProcessUtil();
	
	private NetProcessServer netProcessServer;

	public void init() throws Exception {
		if (jobRegister != null) {
			jobRegister.addJobLogic(AutoType, this);
		} else {
			throw new Exception("未获取到IJobRegister服务");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yzj.wf.as.common.JobLogic#action()
	 */
	@Override
	public BusinessInfo action() throws Exception {
		long time = System.currentTimeMillis();
		String nowDate = formatDate.format(new Date()); // 数据日期
		String[] dateTemp = dataServiceAuto.getSpecifiedDayBefore(nowDate).split("-");
		String year =dateTemp[0];
		String month =dateTemp[1];
		String day =dateTemp[2];
		
		//date格式为  20130831
		String dataDate =year+month+day;
		
		try {
			/**
			 * 催收处理
			 */
			dataServiceAuto.rushstateProcess();
			
			/**
			 * 湘江银行数据处理开始
			 **/
			
			/**     
			 * 数据导入     
			**/
			//将数据平台推送到服务器上的文件的数据导入到系统的接口 表中
			String importdate = dataServiceAuto.findSysbaseParam("IMPORTDATE");
			String sql=" ebs_basicinfolog  where opdate='"+dataDate+"' and opdesc='"+OperLogModuleDefine.DATAPROFLAG+"'";
			Long couFlag=dataServiceAuto.getDataProcess().queryTableCount(sql);
			//日期为dataDate的数据必须未导入，否则不能进行数据导入，避免重复数据导入
			if(couFlag==0){
				//查询最近数据导入的时间，如果当天的数据文件以及导入，则不需要再导入
				sql=" ebs_basicinfolog  where opdate='"+dataDate+"' and opdesc='"+OperLogModuleDefine.DATAIMPSUCCESS+"'";
				couFlag=dataServiceAuto.getDataProcess().queryTableCount(sql);
				if(!importdate.equals(dataDate)&&couFlag==0){
					
					String checkResult = dataServiceAuto.checkDataFile(dataDate);
					if(checkResult=="success"){
						
						dataServiceAuto.dataImport(dataDate);
					}
					else{
						tools.getDataLogUtils().info("数据日期:"+dataDate+", 信息："+checkResult,String.valueOf(OperLogModuleDefine.dataprocess),dataDate);
					}
				}
			}
			
			/**
			 * 数据迁移
			 */
			sql=" ebs_basicinfolog  where opdate='"+dataDate+"' and opdesc='"+OperLogModuleDefine.DATAPROFLAG+"'";
			couFlag=dataServiceAuto.getDataProcess().queryTableCount(sql);
			String movedate = dataServiceAuto.findSysbaseParam("MOVEDATE");
			if(couFlag==0){
				//数据迁移到正式表中 当天的数据必须导入后才能继续数据迁移
				importdate = dataServiceAuto.findSysbaseParam("IMPORTDATE");
				if(importdate.equals(dataDate)){
					sql=" ebs_basicinfolog  where opdate='"+dataDate+"' and opdesc='"+OperLogModuleDefine.DATAMOVESUCCESS+"'";
					couFlag=dataServiceAuto.getDataProcess().queryTableCount(sql);
					//日期为dataDate的数据必须未迁移完成，否则不能进行数据迁移，避免重复数据迁移
					if(!movedate.equals(dataDate)&&couFlag==0){
						moveData(month, day, dataDate);
					}
				}else{
					tools.getDataLogUtils().info("数据日期："+dataDate+", 信息：日期为"+dataDate+"的数据未导入",false);
				}
			}
			
			/**     
			 * 数据处理    
			 **/
			
			//如果日期为本月的最后一天，需要生成对账单
			if (dataServiceAuto.isLastDay(dataDate)) {
				String processdate = dataServiceAuto.findSysbaseParam("PROCESSDATE");
				movedate = dataServiceAuto.findSysbaseParam("MOVEDATE");
				sql=" ebs_basicinfolog  where opdate='"+dataDate+"' and opdesc='"+OperLogModuleDefine.DATAPROFLAG+"'";
				couFlag=dataServiceAuto.getDataProcess().queryTableCount(sql);
				if(couFlag==0){
					if(movedate.equals(dataDate)){
						sql=" ebs_basicinfolog  where opdate='"+dataDate+"' and opdesc='"+OperLogModuleDefine.DATAPROSUCCESS+"'";
						couFlag=dataServiceAuto.getDataProcess().queryTableCount(sql);
						//本月的数据未进行过数据处理，才能进行数据处理，避免数据重复处理
						if(!processdate.equals(dataDate)&&couFlag==0){
							//检查数据导入情况
							String imNotFinish = year+month+"当月有数据未导入，请将未导入的数据导入完成后再进行数据处理";
							String imFinish = year+month+"当月数据导入全部完成";
							
							//存在检查数据导入日志记录则更新，没有则插入
							sql=" ebs_basicinfolog where opdate='"+dataDate+"' and (opdesc='"+imFinish+"' or opdesc='"+imNotFinish+"')";
							couFlag=dataServiceAuto.getDataProcess().queryTableCount(sql);
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
								
								
								tools.getDataLogUtils().info("开始进行数据处理",String.valueOf(OperLogModuleDefine.dataprocess),dataDate);
								
								tools.getDataLogUtils().info(OperLogModuleDefine.DATAPROFLAG,String.valueOf(OperLogModuleDefine.dataprocess),dataDate);
								
								dataServiceAuto.dateProcess(year,month,day,FinalConstant.isHBcheckMap.get("正常"));
								//自动给网银发送对账单
								netProcessServer.fetchEbillFromEbill();
								
								tools.getDataLogUtils().infoUpdate(OperLogModuleDefine.DATAPROFLAG,OperLogModuleDefine.DATAPROSUCCESS,null,String.valueOf(OperLogModuleDefine.dataprocess),dataDate);
							}else{
								//当月有数据未导入，则进行日志记录更新
								tools.getDataLogUtils().infoUpdate(imFinish,imNotFinish,null,String.valueOf(OperLogModuleDefine.dataprocess),dataDate);
							}
						}
					}else{
						tools.getDataLogUtils().info("数据日期:"+dataDate+", 信息："+dataDate+"的数据未迁移完成",String.valueOf(OperLogModuleDefine.dataprocess),dataDate);
					}
				}
			}
			
			/**
			 * 湘江银行数据处理结束
			 **/
			
			BusinessInfo businessInfo=new BusinessInfo(AutoType,true,
					new AutoResult(nowDate,(System.currentTimeMillis() - time)+"",nowDate +"自动数据处理流程结束"),nowDate+"自动数据处理流程结束");
			return businessInfo;
		} catch (Exception e) {
			tools.getDataLogUtils().infoUpdate(OperLogModuleDefine.DATAPROFLAG,"自动数据处理流程异常,请人工进行处理,日期为："+nowDate,e,String.valueOf(OperLogModuleDefine.dataprocess),dataDate);
			e.printStackTrace();
			return new BusinessInfo(AutoType,true,
					new AutoResult(nowDate,(System.currentTimeMillis() - time)+"",nowDate +"自动数据处理流程异常,"),nowDate+"自动数据处理流程失败,");
		}finally{
			dataServiceAuto.getDataProcess().getEEdataDao().closeConnProcess();
		}
	}
	
	/**
	 * 迁移方法
	 * @param month
	 * @param day
	 * @param dataDate
	 * @throws Exception
	 */
	public void moveData(String month, String day, String dataDate)
			throws Exception {
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
		if(day.equals("01")){
			dataServiceAuto.deletebillDetailByMonth(month);
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
	}
	
	
	public IJobRegister getJobRegister() {
		return jobRegister;
	}

	public void setJobRegister(IJobRegister jobRegister) {
		this.jobRegister = jobRegister;
	}

	public EdataServiceAuto getDataServiceAuto() {
		return dataServiceAuto;
	}

	public void setDataServiceAuto(EdataServiceAuto dataServiceAuto) {
		this.dataServiceAuto = dataServiceAuto;
	}

	public IPublicTools getTools() {
		return tools;
	}

	public void setTools(IPublicTools tools) {
		this.tools = tools;
	}

	public NetProcessServer getNetProcessServer() {
		return netProcessServer;
	}

	public void setNetProcessServer(NetProcessServer netProcessServer) {
		this.netProcessServer = netProcessServer;
	}
	
}
