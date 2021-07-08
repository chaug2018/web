/**
 * 
 */
package com.yzj.wf.ebs.scanapplet.tran;

import java.io.File;
import java.util.ArrayList;
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
import com.yzj.wf.scan.service.IUploadService;
import com.yzj.wf.scan.util.ImageNode;

/**
 * 创建于:2012-8-10<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 影像上传工具类,同城版
 * 
 * @author 陈林江
 * @version 1.0.1
 */
public class UploadService4ebs implements IUploadService {

	

	/**
	 * 上传影像信息
	 * @param nodes 图片节点
	 * @return
	 * @throws Exception
	 */
	public void upload(List<ImageNode> nodes) throws Exception {
		String[] imgs = this.uploadImage(nodes);		
		this.upBizData(imgs);
	}

	/**
	 * 上传影像信息
	 * @param nodes 图片节点
	 * @return
	 * @throws Exception
	 */
	private String[]  uploadImage(List<ImageNode> nodes) throws Exception {
		HttpClient httpclient = new DefaultHttpClient();
		try {
			HttpPost httppost = new HttpPost(AppletParamDefine.imageServerAdress); // 设置影像服务端地址
			MultipartEntity reqEntity = new MultipartEntity();
			for (int i = 0; i < nodes.size(); i++) { // 添加某笔业务的所有影像文件
				FileBody file = new FileBody(nodes.get(i).getFile(), nodes.get(
						i).getFile().getName());
				reqEntity.addPart("data", file);
			}
			httppost.setEntity(reqEntity);
			HttpResponse response = httpclient.execute(httppost);

			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) { // 上传成功
				HttpEntity resEntity = response.getEntity();
				String result = new String(EntityUtils.toByteArray(resEntity),"UTF-8");
				EntityUtils.consume(resEntity);
				if (result != null && result.startsWith("success")) {// 上传影像完毕
					result = result.substring(7);
					String[] imgs=result.split(";");     //影像地址按分号分隔
					if(imgs==null||imgs.length!=nodes.size()+1){
						throw new Exception("上传影像至存储出现数据丢失");
					}
					return imgs;
				}else if(result != null && result.startsWith("fail")){
					throw new Exception(result.substring(4));
				}else{
					throw new Exception("上传影像出现未知错误");
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
	
	/**
	 * 上传业务信息
	 * @param storeId
	 * @throws Exception
	 */
	private void upBizData(String[] imgs)throws Exception{

		HttpClient httpclient = new DefaultHttpClient();
		try {
			HttpPost httppost = new HttpPost(AppletParamDefine.bizServerAdress); // 设置业务服务端地址
			StringBody userId = new StringBody(AppletParamDefine.userId); // 设置用户名
			StringBody orgId = new StringBody(AppletParamDefine.orgId); // 设置机构号
			StringBody type = new StringBody("save"); // 设置操作类型
			MultipartEntity reqEntity = new MultipartEntity();
			reqEntity.addPart("userId", userId);
			reqEntity.addPart("orgId", orgId);
			reqEntity.addPart("type", type);
			for (String img : imgs) {
				StringBody imgBody = new StringBody(img); 
				reqEntity.addPart("imgs", imgBody);
			}
			httppost.setEntity(reqEntity);
			HttpResponse response = httpclient.execute(httppost);

			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) { // 上传成功
				HttpEntity resEntity = response.getEntity();
				String result = new String(EntityUtils.toByteArray(resEntity),"UTF-8");
				EntityUtils.consume(resEntity);
				if (result != null && "success".equals(result)) {// 提交业务完毕
					return;
				}else if(result != null && result.startsWith("fail")){
					throw new Exception(result.substring(4));
				}
			} else {
				throw new Exception("上传业务信息出现错误,可能是网络问题，请重试!");
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
	public static void main(String[] args) {
		UploadService4ebs up=new UploadService4ebs();
		AppletParamDefine.imageServerAdress="http://localhost:8080/storeServer/storeAction.action";
		AppletParamDefine.bizServerAdress="http://localhost:8080/windforce/scanAction.action";
		List<ImageNode> nodes=new ArrayList<ImageNode>();
		ImageNode im=new ImageNode(new File("d:/1.jpg"));
		ImageNode im1=new ImageNode(new File("d:/2.jpg"));
		ImageNode im2=new ImageNode(new File("d:/3.jpg"));
		nodes.add(im);
		nodes.add(im1);
		nodes.add(im2);
		try {
			long now=System.currentTimeMillis();
			for(int i=0;i<1;i++){
			up.upload(nodes);
			}
			System.out.println(System.currentTimeMillis()-now);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
