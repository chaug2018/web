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
public class Trade4006Test {

	
	public static void main(String[] args) {
		Socket socket = null;
		socket = new Socket();
		try {
			socket.connect(new InetSocketAddress("localhost",10000),30000);
			String strInput = "22222222<?xml version=\"1.0\" encoding=\"gbk\"?><ROOT>"
   +"<TRNCD>4006</TRNCD>"
   +"<SRCSYSID>88881</SRCSYSID>"
	+"<PAGECOUNT>10</PAGECOUNT>"+"<RQROWNUM>2</RQROWNUM><ROW><WORKDT>2012-06-30</WORKDT><TRACENO>122222</TRACENO><ACCTNO>233333</ACCTNO><SUBACCTNO>233334</SUBACCTNO></ROW><ROW><WORKDT>2012-06-30</WORKDT><TRACENO>11111</TRACENO><ACCTNO>22222</ACCTNO><SUBACCTNO>22222</SUBACCTNO></ROW><MSGTYPE>dd</MSGTYPE><PAGENO>1</PAGENO><QRYACCTNO1>6214699900000037000</QRYACCTNO1><DOCSTATE>2</DOCSTATE><ORDERBY>1</ORDERBY><BEGDOCDT>2012-06-12</BEGDOCDT><QRYACCTNO>6214699900000037000</QRYACCTNO>"
+"</ROOT>";
			
			byte[] data = strInput.getBytes();
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
				byte[] buffer1 = new byte[111*1024];
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
