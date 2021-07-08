package com.yzj.ebs.edata.service;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;

import com.yzj.ebs.edata.common.CheckImportResult;
import com.yzj.ebs.edata.common.CheckInformation;
import com.yzj.ebs.edata.common.ICheckMethod;
import com.yzj.ebs.edata.common.IDataCheck;
import com.yzj.ebs.edata.common.IDataImport;
import com.yzj.ebs.edata.common.IDataProcess;
import com.yzj.ebs.edata.common.IDisplay;
import com.yzj.ebs.edata.common.IEdataService;
import com.yzj.ebs.edata.common.PublicDefine;
import com.yzj.ebs.edata.common.CheckInformation.InfoType;
import com.yzj.ebs.edata.service.checkmethod.CheckColumnMethod;
import com.yzj.ebs.edata.tran.DataHttpService;

/**
 * 创建于:2012-9-25<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 数据校验、导入Service管理层默认实现类
 * 
 * @author Lif
 * @version 1.0.0
 */
public class EdataServiceDefault implements IEdataService {
	private IDataCheck dataCheck;
	private IDataImport dataImport;
	private IDataProcess dataDispose;
	private IDisplay display;
	private CheckImportResult result;
	private String filePath;
	private String dataDate;
	private static String[] FILES_NAME = new String[] { "MAINDATA", "DEPHIST",
			"KUB_USER" };// 定义数据处理文件名称前缀

	CheckInformation checkInformation = null; // 提示信息
	private String szmonth;// 1-12月份
	DateFormat formatDate = new SimpleDateFormat("yyyyMMdd");
	DataHttpService dhs = new DataHttpService();

	public EdataServiceDefault(String filePath, String dataDate) {
		this.filePath = filePath;
		this.dataDate = dataDate;
		this.szmonth = dataDate.substring(4, 6);
	}

	@Override
	public void setDisplay(IDisplay display) {
		this.display = display;
	}

	@Override
	public boolean checkEdata() {
		long imporDataNum;// 导入到表中的数据行数
		long stateDataNum;// 数据状态表中的数据行数
		this.dataImport = new DataImportDefault(dataDate);
		this.dataImport.setDisplay(display);
		String selectSql;
		String resultStr = "";
		for (int i = 0; i < FILES_NAME.length; i++) {
			checkInformation = new CheckInformation(InfoType.SUCCESS, "正在校验："
					+ FILES_NAME[i] + "数据文件" + "...");
			this.display.showMsg(checkInformation);
			if (FILES_NAME[i].equals("KUB_USER")) {
				selectSql = "EBS_" + FILES_NAME[i];
			} else {
				selectSql = "EBS_" + FILES_NAME[i] + " where datadate='"
						+ dataDate + "'";
			}
			imporDataNum = this.dataImport.checkLastImportLine(selectSql);

			selectSql = "tablename='EBS_" + FILES_NAME[i] + "' and datadate='"
					+ dataDate + "'";
			stateDataNum = this.dataImport.queryDataStateCount(selectSql);

			if (stateDataNum == imporDataNum && imporDataNum != 0) {
				checkInformation = new CheckInformation(InfoType.SUCCESS,
						"校验成功：" + FILES_NAME[i] + "表校验成功");
				this.display.showMsg(checkInformation);
				resultStr += "0";
			} else {
				checkInformation = new CheckInformation(InfoType.SUCCESS,
						"校验失败：接口表  EBS_" + FILES_NAME[i] + " " + imporDataNum
								+ " 条，数据状态表  EBS_DATASTATE " + stateDataNum
								+ "条");
				this.display.showMsg(checkInformation);
			}
		}
		if (!resultStr.equals("0000")) {
			return  true;
		} else {
			return false;
		}
	}

	@Override
	public CheckImportResult importEdata() {
		this.dataImport = new DataImportDefault(dataDate);
		this.dataImport.setDisplay(display);

		for (int i = 0; i < FILES_NAME.length; i++) {
			// 查找上次有没有导过数据，如果导过导了多少行
			long startLine = 0;
			if ("DEPHIST".equals(FILES_NAME[i].trim())) {// 交易明细表
				startLine = this.dataImport.queryTempTable(
						"EBS_accnodetaildata_" + szmonth, dataDate);
				// 第一次导数据
				if (startLine == 0) {
					checkInformation = new CheckInformation(InfoType.SUCCESS,
							"正在第一次导入  " + dataDate + " 数据...");
					this.display.showMsg(checkInformation);
					String imporSql = "insert into EBS_accnodetaildata_"
							+ dataDate.substring(4, 6)
							+ " (AUTOID,DATADATE,WORKDATE,TRACENO,ACCNO,ACCSON,DCFLAG,TRACEBAL,CREDIT,CURRTYPE,TO_ACCNO,TO_ACCNAME,ABS,vouNo,importdate,CHECKFLAG,traceNoSon)"
							+ "select ACCNODETAILDATA_"
							+ dataDate.substring(4, 6)
							+ "_AUTOID.nextval,dataDate,TX_DATE,TRACE_NO,AC_NO,AC_SEQN,IO_IND,TRAN_AMT,BAL,CURRTYPE,TO_ACCNO,TO_ACCNAME,ABS,DOCNUM,'"
							+ formatDate.format(new Date())
							+ "',1,TRACE_SUB_NO from autek.EBS_DEPHIST where datadate='"
							+ dataDate + "'";
					result = this.dataImport.importData(imporSql);
				} else {
					// 已经导过数据
					int cd = JOptionPane.showConfirmDialog(null,
							"已经导过数据，是否要重新导入？", "提示信息",
							JOptionPane.YES_NO_CANCEL_OPTION);
					if (cd == JOptionPane.YES_OPTION) {
						// 重新导 1 先删 再导
						this.dataImport.deleteTableByDataDate(
								"EBS_accnodetaildata_" + szmonth, dataDate,
								"onlyDay");
						startLine = 0;
						checkInformation = new CheckInformation(
								InfoType.SUCCESS, "正在重新导入 " + dataDate
										+ " 数据...");
						this.display.showMsg(checkInformation);
						String imporSql = "insert into EBS_accnodetaildata_"
								+ dataDate.substring(4, 6)
								+ " (AUTOID,DATADATE,WORKDATE,TRACENO,ACCNO,ACCSON,DCFLAG,TRACEBAL,CREDIT,CURRTYPE,TO_ACCNO,TO_ACCNAME,ABS,vouNo,importdate,CHECKFLAG,traceNoSon,page_Num1,page_Num2)"
								+ "select ACCNODETAILDATA_"
								+ dataDate.substring(4, 6)
								+ "_AUTOID.nextval,dataDate,TX_DATE,TRACE_NO,AC_NO,AC_SEQN,IO_IND,TRAN_AMT,BAL,CURRTYPE,TO_ACCNO,TO_ACCNAME,ABS,DOCNUM,'"
								+ formatDate.format(new Date())
								+ "',1,TRACE_SUB_NO,page_Num1,page_Num2 from autek.EBS_DEPHIST where datadate='"
								+ dataDate + "'";
						result = this.dataImport.importData(imporSql);
					} else if (cd == JOptionPane.NO_OPTION) {
						checkInformation = new CheckInformation(
								InfoType.SUCCESS, "放弃导入 " + dataDate + " 数据...");
						this.display.showMsg(checkInformation);
						result = null;
					}
				}
				if (result != null) {
					if (result.failLine == 0) {
						checkInformation = new CheckInformation(
								InfoType.SUCCESS, "导入 " + dataDate + " 数据完成。");
						this.display.showMsg(checkInformation);
					}
				}
			}

		}
		boolean b;
		try {
			checkInformation = new CheckInformation(InfoType.SUCCESS,
					"正在更新账户信息...");
			this.display.showMsg(checkInformation);
			b = dhs.updateBasicinfoHttpRequest(dataDate);
			if (b) {
				checkInformation = new CheckInformation(InfoType.SUCCESS,
						"更新账户信息成功");
			} else {
				checkInformation = new CheckInformation(InfoType.ERROR,
						"更新账户信息失败");
			}
			this.display.showMsg(checkInformation);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public boolean dataDispose() {
		boolean result = true;
		this.dataImport = new DataImportDefault(dataDate);
		CheckInformation checkInformation = new CheckInformation(
				InfoType.ERROR, "正在数据处理，请稍后...");
		this.display.showMsg(checkInformation);
		this.dataDispose = new DataProcessDefault();
		this.dataDispose.setDisplay(display);
		long maincount = this.dataImport.queryTempTable("EBS_maindata",
				dataDate);
		if (maincount == 0) {
			checkInformation = new CheckInformation(InfoType.ERROR,
					"月末数据未导入，不能数据处理");
			this.display.showMsg(checkInformation);
			return false;
		} else {
			result = this.dataDispose.dataDispose(dataDate, szmonth);
			if (result) {
				// 生成账单完成，把临时发生额明细表删了
				// this.dataImport.deleteTableByDataDate("EBS_tempaccnodetaildata",
				// dataDate, "onlyMouth");
				checkInformation = new CheckInformation(InfoType.ERROR,
						"数据处理完成");
				this.display.showMsg(checkInformation);
			}
		}
		return result;
	}
}
