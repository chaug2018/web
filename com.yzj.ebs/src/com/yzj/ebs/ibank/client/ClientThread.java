package com.yzj.ebs.ibank.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import com.yzj.ebs.ibank.server.IBankAdm;
import com.yzj.wf.com.ibank.common.IBankDefine.MapsTemplate;
import com.yzj.wf.com.ibank.common.IBankTemplateFactory;
import com.yzj.wf.com.ibank.common.template.Trade;
import com.yzj.wf.com.ibank.common.template.TradeTemplate;



public class ClientThread implements Runnable {
	private String accno;
	private IBankAdm ibankAdm;
	private ClientToFile clientToFile;
	private IBankTemplateFactory templateFactory;
	public ClientThread(IBankAdm ibankAdm,String accno,IBankTemplateFactory iBankTemplateFactory,ClientToFile clientToFile){
		super();
		this.accno = accno;
		this.ibankAdm = ibankAdm;
		this.clientToFile = clientToFile;
		this.templateFactory = iBankTemplateFactory;
	}
	
	
	
	@Override
	public void run() {
		TradeTemplate tradeTemplate = null;
		InputStream is = null;
		OutputStream os = null;
		Socket socket = null;
		BufferedReader br = null;
		byte[] dataUp = null;
		
		try {
//			TemplateFactoryService templateFactory = new TemplateFactoryService();
			try {
				tradeTemplate = templateFactory.getTradeTemplate(MapsTemplate.CLIENT);
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String hostIp = tradeTemplate.getParamByName("hostip").getValue();
			String hostPort = tradeTemplate.getParamByName("hostport").getValue();
			int portInt = Integer.parseInt(hostPort);
			Trade trade = tradeTemplate.getTradeByID("E001");
			Trade2103 trade2103 = new Trade2103();
			if(trade != null){
				dataUp = trade2103.execTrade(trade, ibankAdm, accno,clientToFile);
			}
			if(dataUp != null){
				socket = new Socket(hostIp,portInt);
				os = socket.getOutputStream();
				os.write(dataUp);
			}
			is = socket.getInputStream();
			byte[] dataDown = new byte[3000];
			int num = 0;
			String dataDownStr = "";
			int len=0;
			while((num = is.read(dataDown))!=-1){
				len = num;//获取字节数组的实际长度
				dataDownStr = dataDownStr + new String(dataDown,0,len,"UTF-8");
			}
//			System.out.println("E001响应:");
//			System.out.println(dataDownStr);
			clientToFile.creatFile(dataDownStr);
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (os != null) {
					os.close();
				}
				if (is != null) {
					is.close();
				}
				if(br != null){
					br.close();
				}
				if (socket != null) {
					socket.close();
				}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	}
}
