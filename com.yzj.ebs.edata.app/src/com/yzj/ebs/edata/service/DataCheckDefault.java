package com.yzj.ebs.edata.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.yzj.ebs.edata.common.CheckImportResult;
import com.yzj.ebs.edata.common.CheckInformation;
import com.yzj.ebs.edata.common.ICheckMethod;
import com.yzj.ebs.edata.common.IDataCheck;
import com.yzj.ebs.edata.common.IDisplay;
import com.yzj.ebs.edata.common.ReadEdataXml;
import com.yzj.ebs.edata.common.CheckInformation.InfoType;
import com.yzj.wf.common.WFLogger;

/**
 * 创建于:2012-9-25<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 数据校验默认实现类
 * 
 * @author Lif
 * @version 1.0.0
 */
public class DataCheckDefault implements IDataCheck {
	private WFLogger logger = WFLogger.getLogger(DataCheckDefault.class);

	private String filepath;
	private IDisplay display;
	private List<ICheckMethod> methods = new ArrayList<ICheckMethod>();
	CheckImportResult result = new CheckImportResult();
	ReadEdataXml rex=new ReadEdataXml();
	public DataCheckDefault(String filepath){
		super();
		this.filepath = filepath;
	}

	@Override
	public void setDisplay(IDisplay display) {
		this.display = display;
	}

	@Override
	public void addCheckMethod(ICheckMethod method) {
		this.methods.add(method);
	}

	@Override
	public boolean checkFilePath(){
		// 这个目录是否存在
		logger.info("sftp下载本地路径："+new File(filepath + "\\").getAbsoluteFile());
		if (new File(filepath + "\\").exists()) {
			if (new File(filepath + "\\").isDirectory()) {// 目录下有无文件
				return true;
			} else {
				CheckInformation checkInformation = new CheckInformation(
						InfoType.ERROR, "目录下有无文件");
				this.display.showMsg(checkInformation);
				return false;
			}
		} else {
			CheckInformation checkInformation = new CheckInformation(
					InfoType.ERROR, "目录不存在");
			this.display.showMsg(checkInformation);
			return false;
		}
	}

	@Override
	public CheckImportResult checkFileContent(String fileName) {
		File file = new File(filepath + "\\"
				+ fileName  + ".txt");
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(file), "GBK"));
			String tempString = null;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				result.totalLine++;
				// 显示行号
//				System.out.println("line " + result.totalLine + ": "
//						+ tempString);
				for (ICheckMethod method : methods) {
					// 依次校验规则
					CheckInformation info = method.check(tempString,fileName);
					if (info.getResult()) {
						logger.debug("校验第" + result.totalLine + "行："
								+ tempString + "成功");
					} else {
						logger.debug("校验第" + result.totalLine + "行："
								+ tempString + "失败，原因：" + info.getMsg());
						display.showMsg(info);
						result.failLine++;
						rex.writeErrorLog(filepath, "文件："+fileName+"校验第" + result.totalLine + "行："
								+ tempString);
					}
				}
			}
		} catch (IOException e) {
			logger.error("数据校验时发生异常", e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return result;
	}
}
