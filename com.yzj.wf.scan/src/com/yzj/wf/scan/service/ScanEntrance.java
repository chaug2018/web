package com.yzj.wf.scan.service;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import javax.swing.JPanel;

import org.apache.log4j.BasicConfigurator;

import com.yzj.wf.scan.action.SaveImageAction;
import com.yzj.wf.scan.appletparam.CDealAppletParamImpl;
import com.yzj.wf.scan.mainview.BatchScanView;
import com.yzj.wf.scan.paramdefine.ParamDefine;
import com.yzj.wf.scan.util.ImageUtility;
import com.yzj.wf.scan.util.JIThumbnailCache;
import com.yzj.wf.scan.util.ReadConfig;
import com.yzj.wf.scan.util.SysCacheContext;

/**
 * 创建于:2012-10-22<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 扫描服务调用入口
 * 
 * @author 陈林江
 * @version 1.0
 */
public class ScanEntrance {
	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(ScanEntrance.class);

	private File propertiesFile;

	private ReadConfig readConfig = null;

	/**
	 * 
	 * @param language 语言种类
	 * @param isAutoFormat 是否自动格式化
	 * @param isDelete 是否在上传完毕后删除图片及图片列表
	 * @param billTypes 业务类型，用逗号分隔，如1,支票,2,汇票
	 */
	public ScanEntrance(String language,boolean isAutoFormat,boolean isDelete,String billTypes,IUploadService upService)throws Exception {
		try{
		if(language!=null&&!"".equals(language)){
		ParamDefine.initLanguage(language);
		}
		ParamDefine.autoFormat=isAutoFormat;
		ParamDefine.isDelete=isDelete;
		try {
			ParamDefine.initBusTypes(billTypes);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("传入的业务参数不正确,请尝试刷新网页...");
		}
		BasicConfigurator.configure();
		}catch (Exception e){
			throw new Exception (ParamDefine.initFail+e.getMessage());
		}
		SaveImageAction.getInstance().setUpService(upService);
	}
	
	/**
	 * 
	 * @param languageFile 语言类型文件
	 * @param isAutoFormat 是否自动格式化
	 * @param isDelete 是否在上传完毕后删除图片及图片列表
	 * @param billTypes 业务类型，用逗号分隔，如1,支票,2,汇票
	 */
	public ScanEntrance(File languageFile,boolean isAutoFormat,boolean isDelete,String billTypes,IUploadService upService)throws Exception {

		try{
		if(languageFile!=null&&languageFile.exists()){
		ParamDefine.initLanguage(languageFile);
		}else{
			System.out.println("提示:传入的语言种类文件为空或不存在:");
			if(languageFile!=null){
				System.out.print(languageFile.getAbsolutePath());
			}
		}
		ParamDefine.autoFormat=isAutoFormat;
		ParamDefine.isDelete=isDelete;
		try {
			ParamDefine.initBusTypes(billTypes);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("传入的业务参数不正确,请尝试刷新网页...");
		}
		BasicConfigurator.configure();
		}catch (Exception e){
			throw new Exception (ParamDefine.initFail+e.getMessage());
		}
		SaveImageAction.getInstance().setUpService(upService);
	
	}

	/**
	 * 初始化扫描服务
	 */
	public JPanel startScanService() throws Exception {
		try{
		initLocalFile();
		writeJai_imageio();
		return initData();
		}catch(Exception e){
			throw new Exception (ParamDefine.initFail+e.getMessage());
		}
	}
	
	/**
	 * 关闭扫描服务
	 */
	public void stopScanService(){
		JIThumbnailCache.getInstanse().close();
	}

	/**
	 * 初始化本地文件
	 */
	private void initLocalFile() throws Exception {
		String userdir = System.getProperty("user.home");
		File parent = new File(userdir, "yinzhijie");
		String classname = getClass().getName();
		String filename = classname.substring(0, classname.lastIndexOf('.'))
				+ ".properties.txt";
		try {
			try {
				parent.mkdirs();
				propertiesFile = new File(parent.getAbsolutePath(), filename);
			} catch (Exception e) {
				log.warn("创建目录失败：" + parent.getAbsolutePath(), e);
				propertiesFile = new File(filename);
			}

			if (readConfig == null) {
				readConfig = new ReadConfig();
			}

			if (propertiesFile.exists()) {
				Properties properties = new Properties();
				properties.load(new FileInputStream(propertiesFile));
				readConfig.setProperties(properties);
			}
			log.info("properties file: " + propertiesFile.getAbsolutePath());
			log.info("java home:" + System.getProperty("java.home"));
		} catch (Exception e) {
			log.error("加载本地文件失败，原因为【" + e.getMessage() + "】", e);
			throw new Exception("加载本地文件失败，原因为【" + e.getMessage() + "】");
		}
	}

	/**
	 * 负制jai的jar包
	 * 
	 * @throws Exception
	 */
	private void writeJai_imageio() throws Exception {
		String javaHome = System.getProperty("java.home");
		// 复制jai_imageio.jar
		File imageioFile = new File(javaHome + File.separator + "lib"
				+ File.separator + "ext" + File.separator + "jai_imageio.jar");
		if (!imageioFile.exists()) {
			log.info("write jai_imageio," + imageioFile.getAbsolutePath());
			ImageUtility.initFileAddr(this.getClass(), "jai_imageio.jar",
					imageioFile);
		}
	}

	/**
	 * 初始化必要数据
	 * 
	 */
	private JPanel initData() throws Exception {
		// 清空
		JIThumbnailCache.init(readConfig);
		// 初始获取applet数据接口
		final CDealAppletParamImpl dealAppletParamImpl = new CDealAppletParamImpl();
		final SysCacheContext sysCacheContext = new SysCacheContext();
		sysCacheContext.setDealAppletParam(dealAppletParamImpl);
		sysCacheContext.setReadconfig(readConfig);
		JPanel panel=this.initScanDataAndGUI(sysCacheContext);
		return panel;
	}

	private JPanel initScanDataAndGUI(SysCacheContext sysCacheContext)
			throws Exception {
		BatchScanView view = new BatchScanView(sysCacheContext, readConfig);
		return view.getJPanel();
	}

}
