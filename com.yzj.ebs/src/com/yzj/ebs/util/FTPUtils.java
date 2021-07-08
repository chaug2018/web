package com.yzj.ebs.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class FTPUtils {
	private  String host = null;
	private  String username = null;
	private  String password = null;
	private  int port = 0;
	private  ChannelSftp sftp = null;
	private  Session sshSession = null;
	private  Channel channel = null;
	
	
	
	
	/**
	 * 构造函数 
	 * @param host 地址
	 * @param username  用户名
 	 * @param password 密码
	 * @param port 端口号
	 */
	public FTPUtils(String host, String username, String password, int port) {
		this.host = host;
		this.username = username;
		this.password = password;
		this.port = port;
	}

	/**
	 * 上传文件
	 * 	remotePath FTP服务器指定路径 
	 * 	remoteFileName  上传后命的文件名
	 * 	localPath 待上传的本地文件所在路径+文件名
	 * 	OKPath 待上传的本地文件OK所在路径+文件名
	 * @throws SftpException 
	 * @throws FileNotFoundException 
	 */
	public  void upload(Map<String,String> paramMap) throws FileNotFoundException, SftpException{
		//1.连接FTP服务
		connect();
		//2.上传文件
		 String remotePath = paramMap.get("remotePath"); //FTP服务器指定路径  如：/home/dataFile/2013/12/11
		 String remoteFileName = paramMap.get("remoteFileName"); //上传后命的文件名
		 String localPath = paramMap.get("localPath"); //待上传的本地文件所在路径+文件名
		 String OKPath =  paramMap.get("OKPath"); //待上传的本地文件OK所在路径+文件名 D://dd/EBILLOK.dat
			//2.1 创建文件夹
		 createDir(remotePath);
			//2.2 上传文件
		 sftp.put(new FileInputStream(new File(localPath)), remoteFileName);  
			//2.3 创建EBILLOK文件
		 sftp.put(new FileInputStream(new File(OKPath)), "EBILLOK.txt");  
		//3.关闭连接
		disconnect();
	}
	
	/**
	 * 下载文件
	 * 	如果没有OK文件则返回假  其他情况为真
	 * 		remotePath  FTP服务器指定路径
	 * 		resultFileLocalPath 对账单文件的缓存路径（全名）
	 * 		detailFileLocalPath 调节单文件的缓存路径(全名)
	 * @throws SftpException 
	 * @throws FileNotFoundException 
	 */
	public  boolean down(Map<String,String> paramMap) throws SftpException, FileNotFoundException{
		boolean result = true;
		//1.连接FTP服务
		connect();
		//2.下载文件
		 String remotePath = paramMap.get("remotePath"); //FTP服务器指定路径  如：/home/dataFile/2013/12/11
		 String resultFileLocalPath = paramMap.get("resultFileLocalPath"); //对账单文件的缓存路径  // d：//1/a/result.txt
		 String detailFileLocalPath = paramMap.get("detailFileLocalPath"); //调节单文件的缓存路径  // d：//1/a/detail.txt
			//2.1 判断OK文件
		
		try{
			sftp.cd(remotePath);
			String OKPath = paramMap.get("OKFileLocalPath"); //调节单文件的缓存路径  // d：//1/a/detail.txt
			File fileOK= new File(OKPath);
			sftp.get("EBILLOK.txt", new FileOutputStream(fileOK)); 
			
			//2.2 有 就下载
				//2.2.1  下载对账结果
				//2.2.2 下载调节单
			File file = new File(detailFileLocalPath);
			sftp.get("EBILL_DETAIL.txt", new FileOutputStream(file));  
			
			File file2 = new File(resultFileLocalPath);
			sftp.get("EBILL_RESULT.txt", new FileOutputStream(file2)); 
			
		}catch(Exception e){
			result = false;
		}
		
		//3.关闭连接
		disconnect();
		return result;
	}
	
	/**
	 * 连接FTP服务
	 */
	private void connect(){
		 try {  
	            if(sftp != null){  
	                System.out.println("sftp is not null");  
	            }  
	            JSch jsch = new JSch();  
	            sshSession = jsch.getSession(username, host, port);  
	            System.out.println("Session created.");  
	            sshSession.setPassword(password);  
	            Properties sshConfig = new Properties();  
	            sshConfig.put("StrictHostKeyChecking", "no");  
	            sshSession.setConfig(sshConfig);  
	            sshSession.connect();  
	            channel = sshSession.openChannel("sftp");  
	            channel.connect();  
	            sftp = (ChannelSftp) channel;  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
	}
	
	/**
	 * 断开连接
	 */
	private void disconnect(){
		  if(sftp != null){  
	            if(sftp.isConnected()){  
	                sftp.disconnect();  
	                sshSession.disconnect();
	            }
	        }  
	}
	
	 /**
     * 创建目录
     * 		根据filePath进行创建
     * @param filepath
     */
    private void createDir(String filepath){  
    	
        boolean bcreated = false;  
        boolean bparent = false; 
        
        String ppath;
        if(filepath.contains("/")){
        	ppath = filepath.substring(0, filepath.lastIndexOf("/"));
        }else{
        	ppath = filepath.substring(0, filepath.lastIndexOf("\\"));
        } 
        
        try {  
            sftp.cd(ppath);  
            bparent = true;  
        } catch (SftpException e1) {  
            bparent = false;  
        }  
        try {  
            if(bparent){  
                try {  
                    sftp.cd(filepath);  
                    bcreated = true;  
                } catch (Exception e) {  
                    bcreated = false;  
                }  
                if(!bcreated){  
                    sftp.mkdir(filepath);  
                    bcreated = true;  
                }  
            }else{  
                createDir(ppath);  
                sftp.cd(ppath);  
                sftp.mkdir(filepath);  
            }  
        } catch (SftpException e) {  
            System.out.println("mkdir failed :" + filepath);  
            e.printStackTrace();  
        }  
          
        try {  
            sftp.cd(filepath);  
        } catch (SftpException e) {  
            e.printStackTrace();  
            System.out.println("can not cd into :" + filepath);  
        }  
        
    } 
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		FTPUtils ftpUtils = new FTPUtils("192.168.120.193", "gtpwh", "gtpwh", 22);
		
		
		/**
		 *  * 	remotePath FTP服务器指定路径 
	 * 	remoteFileName  上传后命的文件名
	 * 	localPath 待上传的本地文件所在路径+文件名
	 * 	OKPath 待上传的本地文件OK所在路径+文件名
		 */
//1、上传
//		Map<String,String> paramMap = new HashMap<String, String>();
//		paramMap.put("remotePath", "/home/dataFile/a/b/c");
//		paramMap.put("remoteFileName", "EBILL.dat");
//		paramMap.put("localPath", "d://dataTest//EBILL.dat");
//		paramMap.put("OKPath", "d://dataTest//EBILLOK.dat");
//		try {
//			ftpUtils.upload(paramMap);
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (SftpException e) {
//			e.printStackTrace();
//		}
//2.下载
//		Map<String,String> paramMap = new HashMap<String, String>();
//		paramMap.put("remotePath", "/home/dataFile/a/b/c");
//		paramMap.put("resultFileLocalPath", "d://dataTest//EBILL_RESULT.dat");
//		paramMap.put("detailFileLocalPath", "d://dataTest//EBILL_DETAIL.dat");
//		
//		/**
//		 * 	 * 		remotePath  FTP服务器指定路径
//	 * 		resultFileLocalPath 对账单文件的缓存路径（全名）
//	 * 		detailFileLocalPath 调节单文件的缓存路径(全名)
//		 */
//		
//		try {
//			ftpUtils.down(paramMap);
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SftpException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	}

}
