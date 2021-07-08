/**
 * 
 */
package com.yzj.wf.ebs.scanapplet.tran;

import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.yzj.wf.ebs.scanapplet.common.AppletParamDefine;
import com.yzj.wf.scan.paramdefine.ParamDefine;
import com.yzj.wf.scan.service.IUploadService;
import com.yzj.wf.scan.util.ImageNode;

/**
 * 创建于:2012-8-10<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 *  影像上传工具类,同城版
 * @author 陈林江
 * @version 1.0.1
 */
public class UploadService4dps implements IUploadService {

	public  void upload(List<ImageNode> nodes)throws Exception {
		
		HttpClient httpclient = new DefaultHttpClient();
		try {
			HttpPost httppost = new HttpPost(AppletParamDefine.imageServerAdress); // 设置服务端地址
			StringBody comment = new StringBody(ParamDefine.billType); // 设置票据类型
			StringBody userId = new StringBody(AppletParamDefine.userId); // 设置用户名
			MultipartEntity reqEntity = new MultipartEntity();
			reqEntity.addPart("busType", comment);               //当前票据类型
			reqEntity.addPart("userId", userId);  
			for (int i = 0; i < nodes.size(); i++) {      // 添加某笔业务的所有影像文件
				FileBody file = new FileBody(nodes.get(i).getFile(), nodes
						.get(i).getFile().getName());
				reqEntity.addPart("data", file); 
			}
			httppost.setEntity(reqEntity);		
			HttpResponse response = httpclient.execute(httppost);
			
			int statusCode = response.getStatusLine().getStatusCode();
			
			if (statusCode == HttpStatus.SC_OK) { // 上传成功
				HttpEntity resEntity = response.getEntity();
				String result=EntityUtils.toString(resEntity,"UTF-8");
				EntityUtils.consume(resEntity);
				if(!"success".equals(result)){  //服务端存储出现异常
					if("fail001".equals(result)){
						throw new Exception("文件重命名失败!");
					}else if("fail002".equals(result)){
						throw new Exception("服务端调用存储出现异常!");
					}else if(result!=null&&result.startsWith("fail003")){
						if(result.contains("workDate")){
						throw new Exception("当前工作日为空");
						}else if(result.contains("scene")){
						throw new Exception("当前工作场次为空");
						}else{
							throw new Exception("进行业务逻辑处理出现未知错误");
						}
					}else if("fail004".equals(result)){
						throw new Exception("文件传输出现数据丢失!");
					}else{
						throw new Exception("文件上传出现网络异常,可能是请求地址不正确!");
					}			
				}
			} else {
				throw new Exception("上传影像出现错误,可能是网络问题，请重试!");
			}
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				httpclient.getConnectionManager().shutdown();
			} catch (Exception ignore) {

			}
		}
	}

}
