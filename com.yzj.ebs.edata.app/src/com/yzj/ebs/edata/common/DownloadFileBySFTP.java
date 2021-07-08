package com.yzj.ebs.edata.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.yzj.common.file.service.FileOperatorService;
import com.yzj.common.file.service.IFileOperator;
import com.yzj.wf.common.WFLogger;

/**
 * 创建于:2013-01-04<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * @author lif
 * @version 1.0
 */
public class DownloadFileBySFTP {
	private static WFLogger logger = WFLogger.getLogger(DownloadFileBySFTP.class);
	private static String[] FILES_NAME = new String[] { "OK","maindata", "dephist",
		"utblbrcd","knp_exrt","kub_user","atchus"};// 定义数据处理文件名称前缀

	/**
	 * 去sftp服务上拿数据文件
	 * 
	 * @return
	 */
	public boolean ftpDownload(String sftpIp,String dataDate,String ftpFilePath) {
		boolean b = true;
		String fname = null;
		try {
			String filepath = ftpFilePath + "\\";
			if (createFile(filepath, dataDate)) {
				// 去param_idcenter表中获取ftp路径，用户名密码
				IFileOperator fo = new FileOperatorService();
				InputStream in = null;
				for (int i = 0; i < FILES_NAME.length; i++) {
					// 从sftp服务器中下载文件
					fname = FILES_NAME[i] + ".txt";
					in = fo.get(sftpIp + "/" + dataDate + "/" + FILES_NAME[i]
							+ ".txt");
					// 放到本地这个目录下
					fo.put(in, filepath + "/" + dataDate + "/" + FILES_NAME[i]
							+ ".txt");
					fo.closeInputStream(in);
				}
			}
		} catch (Exception e) {
			logger.error("文件不存在或连接ftp异常！" + sftpIp + "/" + dataDate + "/"
					+ fname);
			b = false;
		}
		return b;
	}
	/**
	 * 从sftp 中下载的文件放到本目录下
	 * 
	 * @param filePath
	 * @param dataDate
	 * @return
	 */
	private static boolean createFile(String filePath, String dataDate) {
		try {
			File file = new File(filePath+"\\"+dataDate);
			System.out.println("*********"+file.getAbsolutePath());
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
	 * 导入前检查核心数据是否给出
	 * 
	 * @param fileAllPath
	 * @param dataDate
	 * @return
	 */
	public boolean checkSftpFileIsOK(String ftpFilePath,String dataDate) {
		boolean ok = false;
		// 当天导数目录找法：ftp目录+系统时间
		File file = new File(ftpFilePath + "\\" + dataDate
				+ "\\" + "OK.txt");
		BufferedReader reader = null;
		// 判断这个点导数文件核心有没有给过来
		if (file.exists()) {
			try {
				String[] okStr;// 拿到导数状态的内容：导数日期和导数状态
				reader = new BufferedReader(new InputStreamReader(
						new FileInputStream(file), "GBK"));
				String tempString = null;
				while ((tempString = reader.readLine()) != null) {
					okStr = tempString.split(PublicDefine.columnSeparator,
							tempString.length());
					// 1 先拿导数状态判断，如果等于1 才可以校验导入 ：每天定到每天的某个点
					// 取系统时间跟导数状态的时间比较
					String impDate = okStr[0];
					String impState = okStr[1];
					if (dataDate.equals(impDate.replace("-", "")) && "1".equals(impState)) {
						ok = true;
					} else {
						logger.info("核心数据还没给完，不能导入！");
						ok = false;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				ok = false;
			}
		} else {
			logger.warn("核心还未给出当天导数文件！");
			ok = false;
		}
		return ok;
	}
}
