package com.yzj.ebs.ibank.client;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.yzj.ebs.common.IPublicTools;
import com.yzj.wf.com.ibank.common.IBankTemplateFactory;
import com.yzj.wf.com.ibank.common.IBankDefine.MapsTemplate;
import com.yzj.wf.com.ibank.common.template.Trade;

public class ClientToFile {
	private String str;
	private String rootPath;
	private String filePath;
	private IPublicTools publicTools;
	private SimpleDateFormat sdfDay = new SimpleDateFormat("yyyyMMdd");
	private SimpleDateFormat sdfMs = new SimpleDateFormat("HHmmss");
	private IBankTemplateFactory templateFactory = null;
	

//	public StrToFile(String rootPath,String str){
//		this.init();
//		this.rootPath = rootPath;
//		this.str = str;
//	}
	
	
	public synchronized void creatFile(String str){
		this.init();
		if(str!=null && !str.equals("")){
			this.str = str;
			this.rootPath = this.getTracePath();
			if(this.rootPath!=null && !this.rootPath.equals("")){
				this.deleteFile();
				String cp = this.createPath();
				if(cp!=null && !cp.equals("")){
					this.writeToPath();
				
				}
			}
		}
	}
	
	//初始化变量
	private void init(){
		this.str = "";
		this.rootPath = "";
		//初始化filePath路径
		this.filePath = "";
	}
	
	//创建文件路径
	private synchronized String createPath(){
		Date date = new Date();
		String dateStr = sdfDay.format(date);
		String year  = dateStr.substring(0,4);
		String mount = dateStr.substring(4, 6);
		String day  = dateStr.substring(6, 8);
		String time = sdfMs.format(date);
		String TRNCD = this.getTradeCode();
		String SEQNO = this.getSEQNO();
		String RSPCD = this.getRSPCD();
		rootPath = rootPath + "/" + year + "/" + mount + "/" + day;
		if(TRNCD!=null){
			rootPath = rootPath + "/" + TRNCD;
		}else{
			return null;
		}
		if(RSPCD!=null && !RSPCD.equals("")){
			rootPath = rootPath+"/" + "response";
		}else{
			rootPath = rootPath+"/" + "request";
		}
		if(SEQNO!=null){
			filePath = rootPath + "/" + time + "_" + SEQNO;
		}else{
			return null;
		}
		if(RSPCD!=null){
			filePath = filePath + "_" + RSPCD + ".txt";
		}else{
			filePath = filePath + ".txt";
		}
		return "success" ;
	}
	
	//删除3个月前的日志记录
	private synchronized void deleteFile(){
		try {
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMM");
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.set(Calendar.MONTH,cal.get(Calendar.MONTH)-4);
			Date dateFlag = cal.getTime();
			File fileDir = new File(rootPath);
			if(fileDir.isDirectory()){
				//得到所有的年目录
				File[] yearFiles =fileDir.listFiles();
				for(File yearFile:yearFiles){
					String year=yearFile.getName();
					if(year.length()==4){
						if(yearFile.isDirectory()){
							//得到年目录下的所有的月
							File[] mouthFiles =yearFile.listFiles();
							//如果年目录中没有文件，则删除这个年目录
							if(mouthFiles.length==0){
								yearFile.delete();
								continue;
							}
							for(File mouthFile:mouthFiles){
								String month = mouthFile.getName();
								if(month.length()==2){
									Date fileDate = sf.parse(year+month);
									if(fileDate.getTime()<dateFlag.getTime()){
										//删除目录
										publicTools.deleteFile(mouthFile);
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	//向创建的路径中写入文件
	private synchronized void writeToPath(){
		FileOutputStream fos = null ;
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;
		File dirPath = null;
		File dirFile = null;
		
		try{
			dirPath= new File(rootPath);
			if(!dirPath.exists()){
				dirPath.mkdirs();
			}
			
			dirFile = new File(filePath);
			if(dirFile.exists()){
				dirFile.delete();
			}
			try {
				 fos = new FileOutputStream(dirFile);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
			try {
				osw = new OutputStreamWriter(fos, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
			
			bw = new BufferedWriter(osw);
			
	//		try {
	//			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dirFile), "UTF-8"));
	//		} catch (UnsupportedEncodingException e) {
	//			// TODO Auto-generated catch block
	//			e.printStackTrace();
	//		} catch (FileNotFoundException e) {
	//			// TODO Auto-generated catch block
	//			e.printStackTrace();
	//		}
			
			try {
				bw.write(str);
				bw.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
		
		}catch(Exception e) {
		//如果失败的文件存在，删除失败的文件
			if(dirFile!=null){
				if(dirFile.exists()){
					dirFile.delete();
				}
			}
			return;
		}finally {  
			try {
				if (fos != null) {
					fos.close();
				}
				if (osw != null) {
					osw.close();
				}
				if (bw != null) {
					bw.close();
				}
			}catch (IOException e) {
				e.printStackTrace();
				return;
			}  
		}
}
	
	
	
	private String getTradeCode() {
		String tradeCode = null;
		if(str!=null && !str.equals("")){
			if(str.contains("<TRNCD>") && str.contains("</TRNCD>") ){
				String tradeCodeName = "<TRNCD>";
				int tempInt = tradeCodeName.length();
				tradeCode = str.substring(str.indexOf("<TRNCD>")+tempInt, str.indexOf("</TRNCD>"));
			}else if(str.contains("transaction_id") && str.contains("transaction_id")){
				String tradeCodeName = "<transaction_id>";
				int tempInt = tradeCodeName.length();
				tradeCode = str.substring(str.indexOf("<transaction_id>")+tempInt, str.indexOf("</transaction_id>"));
			}
		}
		return tradeCode;
	}
	
	private String getSEQNO() {
		String tradeCode = null;
		if(str!=null && !str.equals("")){
			if(str.contains("<SEQNO>") && str.contains("</SEQNO>") ){
				String tradeCodeName = "<SEQNO>";
				int tempInt = tradeCodeName.length();
				tradeCode = str.substring(str.indexOf("<SEQNO>")+tempInt, str.indexOf("</SEQNO>"));
			}else if(str.contains("<transaction_sn>") && str.contains("</transaction_sn>")){
				String tradeCodeName = "<transaction_sn>";
				int tempInt = tradeCodeName.length();
				tradeCode = str.substring(str.indexOf("<transaction_sn>")+tempInt, str.indexOf("</transaction_sn>"));
			}
		}
		return tradeCode;
	}
	
	private String getRSPCD() {
		String tradeCode = null;
		if(str!=null && !str.equals("")){
			if(str.contains("<RSPCD>") && str.contains("</RSPCD>") ){
				tradeCode = "";
				String tradeCodeName = "<RSPCD>";
				int tempInt = tradeCodeName.length();
				tradeCode = str.substring(str.indexOf("<RSPCD>")+tempInt, str.indexOf("</RSPCD>"));
			}else if(str.contains("<code>") && str.contains("</code>")){
				tradeCode = "";
				String tradeCodeName = "<code>";
				int tempInt = tradeCodeName.length();
				tradeCode = str.substring(str.indexOf("<code>")+tempInt, str.indexOf("</code>"));
			}
		}
		return tradeCode;
	}
	
	private String getTracePath(){
		Trade trade = null;
		String tracepath = null;
		String tradeCode = this.getTradeCode();
		if(tradeCode!=null && !tradeCode.equals("")){
			try {
				trade = templateFactory.getTradeByTemplate(
						MapsTemplate.CLIENT, tradeCode);
				tracepath= trade.getParamValueByName("tracepath");
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
		return tracepath;
	}

	public IPublicTools getPublicTools() {
		return publicTools;
	}

	public void setPublicTools(IPublicTools publicTools) {
		this.publicTools = publicTools;
	}

	public IBankTemplateFactory getTemplateFactory() {
		return templateFactory;
	}

	public void setTemplateFactory(IBankTemplateFactory templateFactory) {
		this.templateFactory = templateFactory;
	}
	

}
