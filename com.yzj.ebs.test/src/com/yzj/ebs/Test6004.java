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
public class Test6004 {

	
	public static void main(String[] args) {
		Socket socket = null;
		socket = new Socket();
		try {
			socket.connect(new InetSocketAddress("10.1.90.144",10000),30000);
			String strInput = "00000329<?xml version=\"1.0\" encoding=\"GBK\"?><ROOT><SRCSYSID>BPB</SRCSYSID><TLRNO>00183</TLRNO><TRNDT> 2012-09-12</TRNDT><VERSION>2.0</VERSION><BRCNO>802011</BRCNO><TRADE_CODE>6004</TRADE_CODE><MSGTYPE>RP</MSGTYPE><TRNCD>6004</TRNCD><CONTRACTNO>SV006</CONTRACTNO><ACCEPTNO>fghjk45678uj</ACCEPTNO><SRCSEQNO>rghjuu8756789</SRCSEQNO></ROOT>";
			
			byte[] data = strInput.getBytes("gbk");
			DataOutputStream oStream = new DataOutputStream(socket
					.getOutputStream());
			ByteArrayInputStream iStream = new ByteArrayInputStream(data);
			byte[] buffer = new byte[2*  1024];
			iStream.read(buffer);
			oStream.write(buffer);
			System.out.println(new String(buffer,"gbk"));
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
					str=new String(buffer1,"gbk");
					System.out.println("原始报文:"+str);
				}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

