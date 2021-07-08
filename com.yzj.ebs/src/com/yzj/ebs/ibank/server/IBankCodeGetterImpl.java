package com.yzj.ebs.ibank.server;

import com.yzj.wf.com.ibank.common.server.IBankServerTradeCodeGetter;


public class IBankCodeGetterImpl implements IBankServerTradeCodeGetter{

	/**
	 * 获取上传报文中的交易码
	 */
	@Override
	public String getTradeCode(String dataDownStr) {
		String tradeCode = null;
		if(dataDownStr.contains("<TRNCD>") && dataDownStr.contains("</TRNCD>") ){
			String tradeCodeName = "<TRNCD>";
			int tempInt = tradeCodeName.length();
			tradeCode = dataDownStr.substring(dataDownStr.indexOf("<TRNCD>")+tempInt, dataDownStr.indexOf("</TRNCD>"));
		}else if(dataDownStr.contains("transaction_id") && dataDownStr.contains("transaction_id")){
			String tradeCodeName = "<transaction_id>";
			int tempInt = tradeCodeName.length();
			tradeCode = dataDownStr.substring(dataDownStr.indexOf("<transaction_id>")+tempInt, dataDownStr.indexOf("</transaction_id>"));
		}
		return tradeCode;
	}
//
//	public static void main(String[] args){
//		String temp = "<Root><SRCSEQNO>000000001</SRCSEQNO><SRCSYSID>WEBEB</SRCSYSID><MSGTYPE>RP</MSGTYPE><TRNCD>4001</TRNCD><TRNDT>20131029</TRNDT><QRYACCTNO>78003012010010055933</QRYACCTNO><CUSTNO>312</CUSTNO></Root>";
//		IBankCodeGetterImpl imp = new IBankCodeGetterImpl();
//		String tradeCode = imp.getTradeCode(temp);
//		System.out.println("交易码为："+tradeCode);
//	}
}
