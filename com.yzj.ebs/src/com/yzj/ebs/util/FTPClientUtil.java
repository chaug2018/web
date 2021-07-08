package com.yzj.ebs.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Map;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPSClient;


/**
 * 用Apache commons.net.ftp包来实现FTP客户端的一些调用
 * @author 贺伟龙
 * @date 2013-12-30
 *
 */
public class FTPClientUtil {
	private FTPSClient ftpClient;
	
	/*
	 * 构造
	 */
	public FTPClientUtil(){
		ftpClient = new FTPSClient();
	}
	
	public FTPClientUtil(FTPSClient ftpClient){
		this.ftpClient = ftpClient;
	}
	
	/**
	 * 根据给出的主机、端口、用户名和密码连接、登陆FTP服务器
	 * @param host ： FTP服务器所在主机名，IP地址也可以
	 * @param port ： FTP服务器监听端口，默认为21；
	 * @param userName ： FTP服务器上分派给客户端登陆用的用户名；
	 * @param password ： FTP服务器上分派给客户端登陆用的密码；
	 * @return	： 登陆成功与失败的标签
	 */
	private boolean connectToServer(String host,int port,String userName,String password)throws Exception{
		boolean connectedOK = false ; 
			ftpClient.connect(host, port);
			connectedOK = ftpClient.login(userName, password);
		return connectedOK;
	}
	
	/**
	 * 根据传入的参数集上传文件到FTP服务器
	 * 参数集：
	 * host ：FTP服务器IP地址
	 * port ：FTP服务器监听端口
	 * userName ： 登陆FTP服务器的用户名；
	 * password ： 登陆FTP服务器的密码；
	 * remotePath ：FTP服务器指定路径
	 * remoteFileName ：上传后命的文件名
	 * localPath : 待上传的本地文件所在路径+文件名
	 * @return	upLoadOK ：返回上传成功与否的标签
	 */
	public boolean upLoadFileToFTP (Map<String,String> paramMap)throws Exception{
		boolean upLoadOK = false;
		/*
		 * 1、连接FTP；
		 * 2、上传前判断：路径；
		 * 3、上传文件：业务文件+OK标志文件；
		 * 4、关闭连接；
		 */
		boolean connectOK = connectToServer(paramMap.get("host"),Integer.parseInt(paramMap.get("port")),paramMap.get("userName"),paramMap.get("password"));
		if(connectOK){
			//判断、上传、再关闭；
			File file = new File(paramMap.get("localPath"));
			File okFile = new File("OK.dat");
			FileInputStream input;
				input = new FileInputStream(file);
				boolean isDirectory = ftpClient.changeWorkingDirectory(paramMap.get("remotePath"));
				if(isDirectory == false){//不存在，先创建，确认创建路径成功后再上传
					isDirectory = ftpClient.makeDirectory(paramMap.get("remotePath"));
					if(isDirectory){//创建路径成功，上传文件
						upLoadOK = ftpClient.storeFile(paramMap.get("remoteFileName"), input);
						if(upLoadOK){//上传成功，生成OK标志文件
							input = new FileInputStream(okFile);
							upLoadOK = ftpClient.storeFile("OK.dat", input);
						}
					}
				}else{//路径存在，直接上传文件
					upLoadOK = ftpClient.storeFile(paramMap.get("remoteFileName"), input);
					if(upLoadOK){//上传成功，生成OK标志文件
						input = new FileInputStream(okFile);
						upLoadOK = ftpClient.storeFile("OK.dat", input);
					}
				}
				//退出并关闭连接
				ftpClient.quit();
				ftpClient.disconnect();
		}
		return upLoadOK;
	}
	
	/**
	 * 根据传入的参数集从FTP服务器下载文件
	 * 参数共8个：
	 * host ：FTP服务器IP地址
	 * port ：FTP服务器监听端口
	 * userName ： 登陆FTP服务器的用户名；
	 * password ： 登陆FTP服务器的密码；
	 * remotePath ：指定的远程路径
	 * remoteFileName ：指定的远程文件名
	 * localPath ：指定的本地路径+本地文件名；
	 * @return downOK ：下载成功与否的标记
	 */
	public boolean downFileFromFTP(Map<String,String> paramMap)throws Exception{
		boolean downOK = false;
		/*
		 * 1、连接并登陆FTP服务器
		 * 2、切换到指定的工作路径；
		 * 3、判断指定路径是否有指定文件以及OK标志文件，并下载；
		 * 4、退出登陆并断开连接；
		 */
		boolean connectOK = connectToServer(paramMap.get("host"),Integer.parseInt(paramMap.get("port")),paramMap.get("userName"),paramMap.get("password"));
		if(connectOK){//连接登陆成功
				OutputStream os = new FileOutputStream(paramMap.get("localPath"));
				boolean isExist = ftpClient.changeWorkingDirectory(paramMap.get("remotePath"));
				if(isExist){//路径存在，判断是否有指定文件
					FTPFile[] files = ftpClient.listFiles();
					boolean isFileExist = false;
					for(FTPFile file : files){
						isFileExist = file.getName().equals(paramMap.get("remoteFileName"));
						if(isFileExist){//业务文件存在，先判断是否有OK标志文件，再下载
							for(FTPFile okFile : files){
								if(okFile.getName().equals("OK.dat")){//有OK标志文件再下载
									downOK = ftpClient.retrieveFile(paramMap.get("remoteFileName"), os);
								}else{//没有就不下载，并返回下载失败的标签；
									downOK = false;
								}
							}
						}
					}
				}else{//不存在此路径
					downOK = false;
				}
				//退出并关闭连接
				ftpClient.quit();
				ftpClient.disconnect();
		}
		return downOK;
	}
	
	/**
	 * 根据传入的参数判断指定路径下是否存在OK.dat标志文件
	 * @param paramMap : 参数集
	 	host ： FTP服务器所在主机名，IP地址也可以
	  	port ： FTP服务器监听端口，默认为21；
	  	userName ： FTP服务器上分派给客户端登陆用的用户名；
	  	password ： FTP服务器上分派给客户端登陆用的密码；
	 	pathname ：要查找的路径
	 * @return
	 */
	public boolean isOKFileExist(Map<String,String> paramMap)throws Exception{
		boolean connectOK = connectToServer(paramMap.get("host"),Integer.parseInt(paramMap.get("port")),paramMap.get("userName"),paramMap.get("password"));
		boolean fileExist = false ;
		boolean pathEist = false;
		if(connectOK){
				pathEist = ftpClient.changeWorkingDirectory(paramMap.get("pathname"));
				if(pathEist){//路径存在
					FTPFile[] ftpFiles = ftpClient.listFiles();
					for(FTPFile okFile : ftpFiles){
						if(okFile.getName().equals("OK.dat")){
							fileExist = true;
						}
					}
				}
		}
		return fileExist;
	}
	
	
	
	public FTPClient getFtpClient() {
		return ftpClient;
	}

	public void setFtpClient(FTPSClient ftpClient) {
		this.ftpClient = ftpClient;
	}

//	public static void main(String[] args){
//		FTPClientUtil ftpUtil = new FTPClientUtil();
//		Map<String,String> paramMap = new HashMap<String,String>(); 
//		paramMap.put("host", "127.0.0.1");
//		paramMap.put("port", "21");
//		paramMap.put("userName", "infotech");
//		paramMap.put("password", "flyvideo");
//		paramMap.put("remotePath", "FTP/2014/1/7");
//		paramMap.put("remoteFileName", "EBill.dat");
//		paramMap.put("localPath", "1.dat");
//		paramMap.put("pathname", "FTP/2014/1/7");
		
		
//		boolean connectOK = ftpUtil.connectToServer(paramMap.get("host"), Integer.parseInt(paramMap.get("port")), paramMap.get("userName"), paramMap.get("password"));
//		System.out.println("连接成功？ ："+connectOK);
//		boolean upLoadOK = ftpUtil.upLoadFileToFTP(paramMap);
//		System.out.println("上传成功？："+ upLoadOK );
//		boolean downFileOK = ftpUtil.downFileFromFTP(paramMap);
//		System.out.println("下载成功？："+ downFileOK );
//		boolean isOKFile = ftpUtil.isOKFileExist(paramMap);
//		System.out.println("有OK.dat文件么？："+ isOKFile );
//	}
}
