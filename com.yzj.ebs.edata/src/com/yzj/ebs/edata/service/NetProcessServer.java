package com.yzj.ebs.edata.service;


/**
 * 业务层 网银处理接口
 * @author Administrator
 *
 */
public interface NetProcessServer {
	/**
	 * 从网银FTP服务器下载，并处理每天网银传来的对账单结果和调节表
	 * @return
	 */
	public int exchangeResultFormNet();	
	
	/**
	 * 手动触发 
	 * 	从网银FTP服务器下载，并处理每天网银传来的对账单结果和调节表
	 * @param autoId
	 * @param date
	 * @return
	 */
	public int exchangeResultFormNet(String autoId,String date);
	
	/**
	 * 每月月底，生成完对账单，抓取网银对账单，生成文件上传到网银FTP服务器
	 * @return
	 */
	public int fetchEbillFromEbill();
	
	/**
	 * 手动触发
	 * 	每月月底，生成完对账单，抓取网银对账单，生成文件上传到网银FTP服务器
	 * @param autoId
	 * @param date
	 * @return
	 */
	public int fetchEbillFromEbill(String autoId,String date);
	
	/**
	 * 每天抓取对账系统未达处理结果，生成文件，上传到网银FTP服务器
	 * @return
	 */
	public int fetchResultFromEbill();
	
	/**
	 * 手动触发
	 * 	每天抓取对账系统未达处理结果，生成文件，上传到网银FTP服务器
	 * @param autoId
	 * @param date
	 * @return
	 */
	public int fetchResultFromEbill(String autoId,String date);
	

	
}
