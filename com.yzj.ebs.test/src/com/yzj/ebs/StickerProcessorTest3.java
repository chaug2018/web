package com.yzj.ebs;


import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import com.yzj.wf.com.ibank.common.TradeSet;
import com.yzj.wf.com.ibank.common.server.IBankProcess;
import com.yzj.wf.com.ibank.common.server.IBankProcessException;

/**
 * 创建于:2012-11-8<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 签约接口
 * 
 * @author jiangzhengqiu
 * @version 1.0.0
 */
public class StickerProcessorTest3 {

	
	public static void main(String[] args) {
		Socket socket = null;
		socket = new Socket();
		try {
			socket.connect(new InetSocketAddress("localhost",10000),30000);
			String strInput = "33313311<?xml version=\"1.0\" encoding=\"GBK\"?><ROOT><SRCSYSID>BPB</SRCSYSID><TLRNO>00183</TLRNO><TRNDT>20121120</TRNDT><VERSION>2.0</VERSION><BRCNO>802011</BRCNO><TRADE_CODE>6004</TRADE_CODE><MSGTYPE>RP</MSGTYPE><TRNCD>6001</TRNCD><CONTRACTNO>SV006</CONTRACTNO><custid>5000000060</custid><ACCEPTNO>fghjk45678uj</ACCEPTNO><SRCSEQNO>rghjuu8756789</SRCSEQNO></ROOT>";
			
			byte[] data = strInput.getBytes();
			DataOutputStream oStream = new DataOutputStream(socket
					.getOutputStream());
			ByteArrayInputStream iStream = new ByteArrayInputStream(data);
			byte[] buffer = new byte[2*  1024];
			iStream.read(buffer);
			oStream.write(buffer);
			System.out.println(new String(buffer,"utf-8"));
			iStream.close();
			oStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		InputStream inputStream = null;
		try {
			inputStream = socket.getInputStream();
//			BufferedInputStream bufferedInputStream = null;
//			 bufferedInputStream = new BufferedInputStream(inputStream);
			 System.out.println("开始接收报文");
			 BufferedInputStream dis = new BufferedInputStream(inputStream);
				String str=null;
				byte[] buffer1 = new byte[2*1024];
				int len=dis.read(buffer1);
				if(len!=-1){
					str=new String(buffer1,"UTF-8");
					System.out.println("原始报文:"+str);
				}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

