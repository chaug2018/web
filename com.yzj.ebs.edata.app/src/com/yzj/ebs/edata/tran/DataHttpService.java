/**
 * 创建于:2012-09-24<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * app模拟向后台发请求
 * @author　 lif
 * @version 1.0.1
 */
package com.yzj.ebs.edata.tran;

import java.nio.charset.Charset;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.yzj.ebs.edata.common.PublicDefine;

public class DataHttpService {
	
	
	/**
	 * 执行本地语言SQL语句
	 * @param tableName 表名
	 * @throws Exception
	 */
	public long executeSql(String strSql)
			throws Exception {

		HttpClient httpclient = new DefaultHttpClient();
		try {
			HttpPost httppost = new HttpPost(PublicDefine.dataServerAdress); // 设置后台服务地址
			MultipartEntity reqEntity = new MultipartEntity();
			reqEntity.addPart("tableName", new StringBody("")); // 表名
			reqEntity.addPart("dataDate", new StringBody("")); // 数据日期
			reqEntity.addPart("flag", new StringBody("executeSql")); // 标识调哪个方法
			reqEntity.addPart("dataValues", new StringBody(strSql, Charset.forName(HTTP.UTF_8))); // 　
			httppost.setEntity(reqEntity);
			HttpResponse response = httpclient.execute(httppost);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) { // 成功
				HttpEntity resEntity = response.getEntity();
				String result = EntityUtils.toString(resEntity, "UTF-8");
				EntityUtils.consume(resEntity);
				if (result != null && result.startsWith("success")) { // 数据导入出现异常
					return new Long(result.split(",")[1]);
				}else{
					throw new Exception("执行语句异常!");
				}
			} else {
				throw new Exception("执行语句出现错误,可能是网络问题，请重试!");
			}
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				httpclient.getConnectionManager().shutdown();
			} catch (Exception e) {
				throw new Exception("httpclient服务关闭失败!");
			}
		}
	}
	
	

	/**
	 * 数据导入时，拼8００行数据发到后台去处理
	 * @param tableName 表名
	 * @param dataList 拼的数据集合
	 * @param dataDate 数据日期
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void addTableDataHttpRequest(String tableName, List dataList,String dataDate)
			throws Exception {

		HttpClient httpclient = new DefaultHttpClient();
		try {
			HttpPost httppost = new HttpPost(PublicDefine.dataServerAdress); // 设置后台服务地址
			MultipartEntity reqEntity = new MultipartEntity();
			reqEntity.addPart("tableName", new StringBody(tableName)); // 表名
			reqEntity.addPart("dataDate", new StringBody(dataDate)); // 数据日期
			reqEntity.addPart("flag", new StringBody("addTableDataHttpRequest")); // 标识调哪个方法
			for (int i = 0; i < dataList.size(); i++) {
				reqEntity.addPart("dataValues", new StringBody(dataList.get(i)
						.toString(), Charset.forName(HTTP.UTF_8))); // 　每行数据
			}
			httppost.setEntity(reqEntity);
			HttpResponse response = httpclient.execute(httppost);

			int statusCode = response.getStatusLine().getStatusCode();

			if (statusCode == HttpStatus.SC_OK) { // 成功
				HttpEntity resEntity = response.getEntity();
				String result = EntityUtils.toString(resEntity, "UTF-8");
				EntityUtils.consume(resEntity);
				if (!"success".equals(result)) { // 数据导入出现异常
					throw new Exception("数据导入出现异常!");
				}else{
					return;
				}
			} else {
				throw new Exception("数据导入出现错误,可能是网络问题，请重试!");
			}
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				httpclient.getConnectionManager().shutdown();
			} catch (Exception e) {
				throw new Exception("httpclient服务关闭失败!");
			}
		}
	}
	
	
	
	/**
	 * 数据继续导时，先查临时表一共导了多少条　 把这个请求发到后台查询
	 * @param tableName 表名
	 * @throws Exception
	 */
	public long queryTableDataHttpRequest(String tableName)
			throws Exception {
		long ct=0;
		HttpClient httpclient = new DefaultHttpClient();
		try {
			HttpPost httppost = new HttpPost(PublicDefine.dataServerAdress); // 设置后台服务地址
			MultipartEntity reqEntity = new MultipartEntity();
			reqEntity.addPart("tableName", new StringBody(tableName)); // 表名
			reqEntity.addPart("flag", new StringBody("queryTableDataHttpRequest")); // 标识调哪个方法
			httppost.setEntity(reqEntity);
			HttpResponse response = httpclient.execute(httppost);

			int statusCode = response.getStatusLine().getStatusCode();

			if (statusCode == HttpStatus.SC_OK) { // 成功
				HttpEntity resEntity = response.getEntity();
				String result = EntityUtils.toString(resEntity, "UTF-8");
				EntityUtils.consume(resEntity);
				if (result != null && result.startsWith("success")) {
					String[] suc=result.split(",");
					ct=Integer.valueOf(suc[1]);
				}else{
					throw new Exception(result);
				}
			} else {
				throw new Exception("数据导入出现错误,可能是网络问题，请重试!");
			}
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				httpclient.getConnectionManager().shutdown();
			} catch (Exception e) {
				throw new Exception("httpclient服务关闭失败!");
			}
		}
		return ct;
	}
	/**
	 * 重新导入数据时，先删除表中所有记录
	 * @param tableName 表名
	 * @throws Exception 
	 */
	public void deleteTableDataHttpRequest(String tableName)
			throws Exception {

		HttpClient httpclient = new DefaultHttpClient();
		try {
			HttpPost httppost = new HttpPost(PublicDefine.dataServerAdress); // 设置后台服务地址
			MultipartEntity reqEntity = new MultipartEntity();
			reqEntity.addPart("tableName", new StringBody(tableName)); // 表名
			reqEntity.addPart("flag", new StringBody("deleteTableDataHttpRequest")); // 标识调哪个方法
			httppost.setEntity(reqEntity);
			HttpResponse response = httpclient.execute(httppost);

			int statusCode = response.getStatusLine().getStatusCode();

			if (statusCode == HttpStatus.SC_OK) { // 成功
				HttpEntity resEntity = response.getEntity();
				String result = EntityUtils.toString(resEntity, "UTF-8");
				EntityUtils.consume(resEntity);
				if ("success".equals(result)) { 
					return;
				}else{
					throw new Exception(result);
				}
			} else {
				throw new Exception("数据删除出现错误,可能是网络问题，请重试!");
			}
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				httpclient.getConnectionManager().shutdown();
			} catch (Exception e) {
				throw new Exception("httpclient服务关闭失败!");
			}
		}
	}
	/**
	 * 根据数据日期查主表数据
	 * @param dataDate
	 * @throws Exception
	 */
	public long queryTempTableDataHttpRequest(String tableName,String dataDate)
			throws Exception {
		long ct=0;
		HttpClient httpclient = new DefaultHttpClient();
		try {
			HttpPost httppost = new HttpPost(PublicDefine.dataServerAdress); // 设置后台服务地址
			MultipartEntity reqEntity = new MultipartEntity();
			reqEntity.addPart("dataDate", new StringBody(dataDate)); // 数据日期
			reqEntity.addPart("tableName", new StringBody(tableName)); // 表名
			reqEntity.addPart("flag", new StringBody("queryTempTableDataHttpRequest")); // 标识调哪个方法
			httppost.setEntity(reqEntity);
			HttpResponse response = httpclient.execute(httppost);

			int statusCode = response.getStatusLine().getStatusCode();

			if (statusCode == HttpStatus.SC_OK) { // 成功
				HttpEntity resEntity = response.getEntity();
				String result = EntityUtils.toString(resEntity, "UTF-8");
				EntityUtils.consume(resEntity);
				if (result != null && result.startsWith("success")) {
					String[] suc=result.split(",");
					ct=Integer.valueOf(suc[1]);
				}else{
					throw new Exception(result);
				}
			} else {
				throw new Exception("数据删除出现错误,可能是网络问题，请重试!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				httpclient.getConnectionManager().shutdown();
			} catch (Exception e) {
				throw new Exception("httpclient服务关闭失败!");
			}
		}
		return ct;
	}
	
	
	
	/**
	 * 根据数据日期查主表数据
	 * @param dataDate
	 * @throws Exception
	 */
	public long queryDataStateCountHttpRequest(String strWhere)
			throws Exception {
		long ct=0;
		HttpClient httpclient = new DefaultHttpClient();
		try {
			HttpPost httppost = new HttpPost(PublicDefine.dataServerAdress); // 设置后台服务地址
			MultipartEntity reqEntity = new MultipartEntity();
			reqEntity.addPart("tableName", new StringBody(strWhere)); // 表名
			reqEntity.addPart("flag", new StringBody("queryDataStateCountHttpRequest")); // 标识调哪个方法
			httppost.setEntity(reqEntity);
			HttpResponse response = httpclient.execute(httppost);

			int statusCode = response.getStatusLine().getStatusCode();

			if (statusCode == HttpStatus.SC_OK) { // 成功
				HttpEntity resEntity = response.getEntity();
				String result = EntityUtils.toString(resEntity, "UTF-8");
				EntityUtils.consume(resEntity);
				if (result != null && result.startsWith("success")) {
					String[] suc=result.split(",");
					ct=Integer.valueOf(suc[1]);
				}else{
					throw new Exception(result);
				}
			} else {
				throw new Exception("数据删除出现错误,可能是网络问题，请重试!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				httpclient.getConnectionManager().shutdown();
			} catch (Exception e) {
				throw new Exception("httpclient服务关闭失败!");
			}
		}
		return ct;
	}
	
	
	
	
	
	/**
	 * 数据处理
	 * @param szmonth
	 * @param szmonth
	 * @throws Exception
	 */
	public boolean dataDisposeHttpRequest(String dataDate,String szmonth)
			throws Exception {

		HttpClient httpclient = new DefaultHttpClient();
		try {
			HttpPost httppost = new HttpPost(PublicDefine.dataServerAdress); // 设置后台服务地址
			MultipartEntity reqEntity = new MultipartEntity();
			reqEntity.addPart("flag", new StringBody("dataDisposeHttpRequest")); // 标识调哪个方法
			reqEntity.addPart("dataDate", new StringBody(dataDate)); // 数据日期
			reqEntity.addPart("szmonth", new StringBody(szmonth)); // 月份
			httppost.setEntity(reqEntity);
			HttpResponse response = httpclient.execute(httppost);
			int statusCode = response.getStatusLine().getStatusCode();

			if (statusCode == HttpStatus.SC_OK) { // 成功
				HttpEntity resEntity = response.getEntity();
				String result = EntityUtils.toString(resEntity, "UTF-8");
				EntityUtils.consume(resEntity);
				if ("success".equals(result)) { 
					return true;
				}else{
					return false;
				}
			} else {
				throw new Exception("数据处理出现错误,可能是网络问题，请重试!");
			}
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				httpclient.getConnectionManager().shutdown();
			} catch (Exception e) {
				throw new Exception("httpclient服务关闭失败!");
			}
		}
	}
	
	/**
	 * 每天导数检查有无导入重复数据日期的数据
	 * @param tableName 表名
	 * @throws Exception 
	 */
	public void deleteTableDataByDataDateHttpRequest(String tableName,String dataDate,String dayOrMouth)
			throws Exception {

		HttpClient httpclient = new DefaultHttpClient();
		try {
			HttpPost httppost = new HttpPost(PublicDefine.dataServerAdress); // 设置后台服务地址
			MultipartEntity reqEntity = new MultipartEntity();
			reqEntity.addPart("tableName", new StringBody(tableName)); // 表名
			reqEntity.addPart("dataDate", new StringBody(dataDate)); // 数据日期
			reqEntity.addPart("dayOrMouth", new StringBody(dayOrMouth)); 
			reqEntity.addPart("flag", new StringBody("deleteTableDataByDataDateHttpRequest")); // 标识调哪个方法
			httppost.setEntity(reqEntity);
			HttpResponse response = httpclient.execute(httppost);

			int statusCode = response.getStatusLine().getStatusCode();

			if (statusCode == HttpStatus.SC_OK) { // 成功
				HttpEntity resEntity = response.getEntity();
				String result = EntityUtils.toString(resEntity, "UTF-8");
				EntityUtils.consume(resEntity);
				if ("success".equals(result)) { 
					return;
				}else{
					throw new Exception(result);
				}
			} else {
				throw new Exception("数据删除出现错误,可能是网络问题，请重试!");
			}
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				httpclient.getConnectionManager().shutdown();
			} catch (Exception e) {
				throw new Exception("httpclient服务关闭失败!");
			}
		}
	}
	/**
	 * 数据处理前查找核心给的数据是否给全
	 * @param tableName
	 * @param dataDate
	 * @throws Exception
	 */
	public long queryTempDataDateDayHttpRequest(String tableName,String dataDate)
			throws Exception {
		long ct=0;
		HttpClient httpclient = new DefaultHttpClient();
		try {
			HttpPost httppost = new HttpPost(PublicDefine.dataServerAdress); // 设置后台服务地址
			MultipartEntity reqEntity = new MultipartEntity();
			reqEntity.addPart("dataDate", new StringBody(dataDate)); // 数据日期
			reqEntity.addPart("tableName", new StringBody(tableName)); // 表名
			reqEntity.addPart("flag", new StringBody("queryTempDataDateDayHttpRequest")); // 标识调哪个方法
			httppost.setEntity(reqEntity);
			HttpResponse response = httpclient.execute(httppost);

			int statusCode = response.getStatusLine().getStatusCode();

			if (statusCode == HttpStatus.SC_OK) { // 成功
				HttpEntity resEntity = response.getEntity();
				String result = EntityUtils.toString(resEntity, "UTF-8");
				EntityUtils.consume(resEntity);
				if (result != null && result.startsWith("success")) {
					String[] suc=result.split(",");
					ct=Integer.valueOf(suc[1]);
				}else{
					throw new Exception(result);
				}
			} else {
				throw new Exception("数据处理前查找核心给的数据是否给全出现错误,可能是网络问题，请重试!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				httpclient.getConnectionManager().shutdown();
			} catch (Exception e) {
				throw new Exception("httpclient服务关闭失败!");
			}
		}
		return ct;
	}
	/**
	 * 手工导数要从sftp中下载文件到本地才能导入 
	 * @param filePath 
	 * @param dataDate
	 * @throws Exception 
	 */
	public String downloadFileForSftpHttpRequest(String dataDate)
			throws Exception {
		String sftpIp=null;
		HttpClient httpclient = new DefaultHttpClient();
		try {
			HttpPost httppost = new HttpPost(PublicDefine.dataServerAdress); // 设置后台服务地址
			MultipartEntity reqEntity = new MultipartEntity();
			reqEntity.addPart("dataDate", new StringBody(dataDate)); // 数据日期
			reqEntity.addPart("flag", new StringBody("downloadFileForSftpHttpRequest")); // 标识调哪个方法
			httppost.setEntity(reqEntity);
			HttpResponse response = httpclient.execute(httppost);

			int statusCode = response.getStatusLine().getStatusCode();

			if (statusCode == HttpStatus.SC_OK) { // 成功
				HttpEntity resEntity = response.getEntity();
				String result = EntityUtils.toString(resEntity, "UTF-8");
				EntityUtils.consume(resEntity);
				if (result != null && result.startsWith("success")) {
					String[] suc=result.split(",");
					sftpIp=suc[1];
				}else{
					throw new Exception(result);
				}
			} else {
				throw new Exception("手工导数要从sftp中下载文件到本地出现错误,可能是网络问题，请重试!");
			}
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				httpclient.getConnectionManager().shutdown();
			} catch (Exception e) {
				throw new Exception("httpclient服务关闭失败!");
			}
		}
		return sftpIp;
	}
	
	/**
	 * 导入数据时，更新账户信息
	 * @param dataDate 数据日期
	 * @throws Exception 
	 */
	public boolean updateBasicinfoHttpRequest(String dataDate)
			throws Exception {

		HttpClient httpclient = new DefaultHttpClient();
		try {
			HttpPost httppost = new HttpPost(PublicDefine.dataServerAdress); // 设置后台服务地址
			MultipartEntity reqEntity = new MultipartEntity();
			reqEntity.addPart("dataDate", new StringBody(dataDate)); // 数据日期
			reqEntity.addPart("flag", new StringBody("updateBasicinfoHttpRequest")); // 标识调哪个方法
			httppost.setEntity(reqEntity);
			HttpResponse response = httpclient.execute(httppost);

			int statusCode = response.getStatusLine().getStatusCode();

			if (statusCode == HttpStatus.SC_OK) { // 成功
				HttpEntity resEntity = response.getEntity();
				String result = EntityUtils.toString(resEntity, "UTF-8");
				EntityUtils.consume(resEntity);
				if ("success".equals(result)) { 
					return true;
				}else{
					return false;
				}
			} else {
				throw new Exception("数据删除出现错误,可能是网络问题，请重试!");
			}
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				httpclient.getConnectionManager().shutdown();
			} catch (Exception e) {
				throw new Exception("httpclient服务关闭失败!");
			}
		}
	}
	
	public static void main(String[] args) {
		DataHttpService ds=new DataHttpService();
		PublicDefine.dataServerAdress="http://localhost:8080/windforce/EDataAction_edataProcess.action";
//		List<String> fileDateValuesList = new ArrayList<String>();
//		fileDateValuesList.add("'020115','广西北部湾银行南宁市长湖支行','8000217562','0805012020013328','001','1',' ','广西聚能水电开发有限公司','0','01','0001','一般账户','22000101','3786.5700',' ',' ',' ','','530022','南宁市东葛路86号皓月大厦11楼财务室1107室','','0771-2552825','20120630'");
		try {
//			long l=ds.queryTempDataDateDayHttpRequest("EBS_maindata", "20130415".substring(0, 6));
//			long ld=ds.queryTempDataDateDayHttpRequest("EBS_tempaccnodetaildata", "20130415".substring(0, 6));
//			System.out.println(l+"eee"+ld);
			ds.downloadFileForSftpHttpRequest("20130415");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
