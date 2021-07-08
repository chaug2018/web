package com.yzj.ebs.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import com.yzj.ebs.common.IBasicInfoLogAdm;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.database.BasicInfoLog;
import com.yzj.wf.common.WFLogger;

/**
 * 日志工具类
 * @author Administrator
 *
 */
public class DataLogUtils implements java.io.Serializable {
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1445199609726530057L;
	private static WFLogger logger = WFLogger.getLogger(DataLogUtils.class);
	private IBasicInfoLogAdm ibasicAdm;
	
	
	public List<BasicInfoLog> findBasicInfoLog(BasicInfoLog basicInfoLog){
		return ibasicAdm.findBasicInfoLog(basicInfoLog);
	}
	public void updateBasicInfoLog(BasicInfoLog basicInfoLog) throws XDocProcException{
		ibasicAdm.updateBasicInfoLog(basicInfoLog);
	}
	public BasicInfoLog createBasicInfoLog(BasicInfoLog basicInfoLog)throws XDocProcException{
		return ibasicAdm.createBasicInfoLog(basicInfoLog);
	}
	
	public IBasicInfoLogAdm getIbasicAdm() {
		return ibasicAdm;
	}

	public void setIbasicAdm(IBasicInfoLogAdm ibasicAdm) {
		this.ibasicAdm = ibasicAdm;
	}
	
	/**
	 * 记录系统日志,增加日期参数
	 * @param OPDESC
	 * @param OPMODE
	 * @param opDate
	 */
	public void info(String OPDESC,String OPMODE,String opDate){
		//1、新建BasicInfoLog
		BasicInfoLog basicLog = creatBasicInfoLog(OPDESC,OPMODE,opDate);
		try {
			//2、插入日志表
			ibasicAdm.createBasicInfoLog(basicLog);
		} catch (XDocProcException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 更新系统日志,增加日期参数
	 * @param OLDOPDESC
	 * @param NEWOPDESC
	 * @param t
	 * @param OPMODE
	 * @param opDate
	 */
	public void infoUpdate(String OLDOPDESC,String NEWOPDESC,Throwable t,String OPMODE,String opDate){
		try {
			if(t!=null){
				NEWOPDESC=NEWOPDESC+t.getMessage();
			}
			BasicInfoLog basicLog = new BasicInfoLog();
			basicLog.setAccNo("AAAAAAAA");
			basicLog.setOpMode(Integer.valueOf(OPMODE));
			basicLog.setOpDesc(OLDOPDESC);
			basicLog.setOpCode("AUTO");
			basicLog.setOpDate(opDate);
			List<BasicInfoLog> list= findBasicInfoLog(basicLog);
			if(list.size()>0){
				BasicInfoLog newBasicLog = (BasicInfoLog)list.get(0);
				newBasicLog.setOpDesc(NEWOPDESC);
				updateBasicInfoLog(newBasicLog);
			}
		} catch (XDocProcException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 记录错误日志,增加日期参数
	 * @param OPDESC
	 * @param OPMODE
	 * @param opDate
	 */
	public void error(String OPDESC,String OPMODE,String opDate){
		info(OPDESC,OPMODE,opDate);
	}
	
	/**
	 * 
	 * @param OPDESC 日志说明
	 * @param OPMODE 日志模块
	 */
	public void info(String OPDESC,String OPMODE){
		//1、新建BasicInfoLog
		//设置时间
		SimpleDateFormat fromatDateFormat  =new SimpleDateFormat("yyyyMMdd");
		String  opDate = fromatDateFormat.format(new Date());
		BasicInfoLog basicLog = creatBasicInfoLog(OPDESC,OPMODE,opDate);
		try {
			//2、插入日志表
			ibasicAdm.createBasicInfoLog(basicLog);
		} catch (XDocProcException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 
	 * @param OPDESC 日志说明
	 * @param OPMODE 日志模块
	 */
	public void error(String OPDESC,String OPMODE){
		info(OPDESC, OPMODE);
	}
	
	/**
	 * 
	 * @param OPDESC 日志说明
	 * @param OPMODE 日志模块
	 */
	public void debug(String OPDESC,String OPMODE){
		info(OPDESC, OPMODE);
	}
	
	/**
	 * 
	 * @param OPDESC 日志内容
	 * @param t 异常 
	 * @param OPMODE 日志模块
	 */
	public void info(String OPDESC,Throwable t,String OPMODE){
		OPDESC = OPDESC+t.getMessage();
		info(OPDESC,OPMODE);
	}
	/**
	 * 
	 * @param OPDESC 日志内容
	 * @param t 异常 
	 * @param OPMODE 日志模块
	 */
	public void error(String OPDESC,Throwable t,String OPMODE){
		info(OPDESC,t,OPMODE);
	}
	/**
	 * 
	 * @param OPDESC 日志内容
	 * @param t 异常 
	 * @param OPMODE 日志模块
	 */
	public void debug(String OPDESC,Throwable t,String OPMODE){
		info(OPDESC,t,OPMODE);
	}
	
	/**
	 * 记录系统日志,增加日期参数
	 * @param OPDESC
	 * @param t 异常 
	 * @param OPMODE
	 * @param opDate
	 */
	public void info(String OPDESC,Throwable t,String OPMODE,String opDate){
		OPDESC = OPDESC+t.getMessage();
		info(OPDESC,OPMODE,opDate);
	}
	
	/**
	 * 记录错误日志,增加日期参数
	 * @param OPDESC
	 * @param t 异常 
	 * @param OPMODE
	 * @param opDate
	 */
	public void error(String OPDESC,Throwable t,String OPMODE,String opDate){
		info(OPDESC,t,OPMODE,opDate);
	}
	
	
	/**
	 * 创建一个BasicInfoLog
	 * @param OPDESC 日志内容
	 * @param OPMODE 操作模块
	 * @return
	 */
	public BasicInfoLog creatBasicInfoLog(String OPDESC,String OPMODE,String opDate){
		BasicInfoLog basicLog = new BasicInfoLog();
		
		basicLog.setAccNo("AAAAAAAA");
		basicLog.setOpMode(Integer.valueOf(OPMODE));
		basicLog.setOpDesc(OPDESC);
		basicLog.setOpCode("AUTO");
		basicLog.setOpDate(opDate);
		
		//设置模块中文名称
		switch (Integer.valueOf(OPMODE)) {
			case 6:
				basicLog.setChnOpMode("自动OCR");
				break;
			case 7:
				basicLog.setChnOpMode("自动验印");
				break;
			case 8:
				basicLog.setChnOpMode("自动记账");
				break;
			case 9:
				basicLog.setChnOpMode("数据处理");
				break;
			case 10:
				basicLog.setChnOpMode("上传未达结果");
				break;
			case 11:
				basicLog.setChnOpMode("上传对账单");
				break;
			case 12:
				basicLog.setChnOpMode("对账单结果下载");
				break;
			case 13:
				basicLog.setChnOpMode("网银账户签约");
				break;
			default:
				basicLog.setChnOpMode("数据处理");
				break;
		}
		return basicLog;
	}
	
	
	// 以下是记录文本日志
	public void error(String OPDESC,Throwable t,boolean istoData){
		logger.error(OPDESC,t);	
	}
	public void info(String OPDESC,Throwable t,boolean istoData){
		logger.info(OPDESC,t);	
	}
	public void error(String OPDESC,boolean istoData){
		logger.error(OPDESC);	
	}
	public void info(String OPDESC,boolean istoData){
		logger.info(OPDESC);	
	}
	public void debug(String OPDESC,boolean istoData){
		logger.debug(OPDESC);	
	}
	
	
	/**
	 * 数据处理日志
	 */
	
	
	
}
