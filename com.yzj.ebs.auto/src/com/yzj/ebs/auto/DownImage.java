package com.yzj.ebs.auto;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * 创建于:2012-8-24<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 图片下载器
 * 
 * @author donggenlong WangXue
 * @version 1.0.0
 */
public class DownImage {
	private String ip;
	private int port;
	private String tmpImagePath;
	private String tmpImageServer;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	public DownImage(String ip, int port, String tmpImagePath,
			String tmpImageServer) {
		super();
		this.ip = ip;
		this.port = port;
		this.tmpImagePath = tmpImagePath;
		this.tmpImageServer=tmpImageServer;
	}

	/**
	 * 根据图片相对路径下载图片
	 * 
	 * @param imagePathURL
	 *            图片相对路径
	 * @return 返回图片的本地绝对路径
	 */
	@SuppressWarnings("deprecation")
	public String downImage(String imagePathURL) throws Exception {
		HttpClient httpClient = new DefaultHttpClient();
		HttpHost httpHost = new HttpHost(ip, port);
		HttpGet httpGet = new HttpGet("/" +tmpImageServer +"/" + imagePathURL);

		HttpResponse httpResponse = httpClient.execute(httpHost, httpGet);
		if (HttpStatus.SC_OK == httpResponse.getStatusLine().getStatusCode()) {
			HttpEntity httpEntity = httpResponse.getEntity();
			File path = new File(tmpImagePath + File.separator
					+ sdf.format(new Date()));
			if (!path.exists()) {
				path.mkdirs();
			}
			
			//删除昨天的图像文件，实践证明只能删除前一天的数据，否则会出现文件正在使用的异常，但此方案还是不科学
			String deletepath = tmpImagePath + File.separator
					+ getSpecifiedDayBefore(sdf.format(new Date()));
			DeleteFolder(deletepath)	;		
			// 图片文件名，在目录下已时分秒毫秒为图片名
			String fileName = UUID.randomUUID().toString().replace("-", "")
					.toUpperCase();
			File storeImage = new File(path + File.separator + fileName
					+ ".jpg");
			FileOutputStream fileOutputStream = new FileOutputStream(storeImage);
			InputStream inputStream = httpEntity.getContent();
			byte[] b = new byte[1024];
			int i = 0;
			while ((i = inputStream.read(b)) != -1) {
				fileOutputStream.write(b, 0, i);
			}
			fileOutputStream.flush();
			fileOutputStream.close();
			if (httpEntity != null)
				httpEntity.consumeContent();
			return storeImage.getAbsolutePath();
		} else {
			throw new Exception("连接http服务器失败");
		}
	}
	
	
    /**
     *  根据路径删除指定的目录或文件，无论存在与否
     *@param sPath  要删除的目录或文件
     *@return 删除成功返回 true，否则返回 false。
     */
    public boolean DeleteFolder(String sPath) {
    	boolean flag = false;
    	File  file = new File(sPath);
        // 判断目录或文件是否存在
        if (!file.exists()) {  // 不存在返回 false
            return flag;
        } else {
            // 判断是否为文件
            if (file.isFile()) {  // 为文件时调用删除文件方法
                return deleteFile(sPath);
            } else {  // 为目录时调用删除目录方法
                return deleteDirectory(sPath);
            }
        }
    }
    
    /**
     * 删除目录（文件夹）以及目录下的文件
     * @param   sPath 被删除目录的文件路径
     * @return  目录删除成功返回true，否则返回false
     */
    public boolean deleteDirectory(String sPath) {
    	
        //如果sPath不以文件分隔符结尾，自动添加文件分隔符
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        File dirFile = new File(sPath);
        //如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        boolean flag = true;
        //删除文件夹下的所有文件(包括子目录)
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            //删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) break;
            } //删除子目录
            else {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) break;
            }
        }
        if (!flag) return false;
        //删除当前目录
        if (dirFile.delete()) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * 删除单个文件
     * @param   sPath    被删除文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public boolean deleteFile(String sPath) {
        
    	boolean flag = false;
    	File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }
	
	 /**
		 * 获取当天日期的前一天
		 * 
		 * @param specifiedDay
		 * @return
		 */
		public String getSpecifiedDayBefore(String specifiedDay) {
			Calendar c = Calendar.getInstance();
			Date date = null;
			try {
				date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			c.setTime(date);
			int day = c.get(Calendar.DATE);
			c.set(Calendar.DATE, day - 1);

			String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c
					.getTime());
			return dayBefore;
		}
}
