package com.yzj.ebs.ibank.server;

import java.io.IOException;
import java.net.MalformedURLException;

import com.yzj.wf.com.ibank.common.IBankDefine.MapsTemplate;
import com.yzj.wf.com.ibank.common.IBankTemplateFactory;
import com.yzj.wf.com.ibank.common.template.Trade;
import com.yzj.wf.com.ibank.common.template.TradeTemplate;
import com.yzj.wf.com.ibank.templatefactory.XMLParser;

/**
 * 加载模板，获取交易信息
 * @author HeWeiLong
 *
 */
public class TemplateFactoryService implements IBankTemplateFactory {

	  private TradeTemplate clientTradeTemplate;
	  private TradeTemplate serverTradeTemplate;
	  private String templateFileUrlServer;
	  private String templateFileUrlClient;
	  
	@Override
	public Trade getTradeByTemplate(MapsTemplate templateType, String tradeCode)
			throws CloneNotSupportedException {
		Trade trade = getTradeTemplate(templateType).getTradeByID(tradeCode);
		return trade;
	}
	
	@Override
	public TradeTemplate getTradeTemplate(MapsTemplate templateType)
			throws CloneNotSupportedException {
//		String templateFileUrlServer = "D:\\home\\IBankServerMapsTemplate.xml";
//		String templateFileUrlClient = "D:\\home\\IBankClientMapsTemplate.xml";
	
		TradeTemplate tradeTemplate = new TradeTemplate();
		XMLParser xmlParser;
		//客户端
		if(templateType.getValue()==MapsTemplate.CLIENT.getValue()){
			try {
				xmlParser = new XMLParser(templateFileUrlClient);
				tradeTemplate = xmlParser.getTradeTemplate();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if(templateType.getValue()==MapsTemplate.SERVER.getValue()){//服务端
			try {
				xmlParser = new XMLParser(templateFileUrlServer);
				tradeTemplate = xmlParser.getTradeTemplate();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return tradeTemplate;
	}

	public TradeTemplate getClientTradeTemplate() {
		return clientTradeTemplate;
	}

	public void setClientTradeTemplate(TradeTemplate clientTradeTemplate) {
		this.clientTradeTemplate = clientTradeTemplate;
	}

	public TradeTemplate getServerTradeTemplate() {
		return serverTradeTemplate;
	}

	public void setServerTradeTemplate(TradeTemplate serverTradeTemplate) {
		this.serverTradeTemplate = serverTradeTemplate;
	}

	public String getTemplateFileUrlServer() {
		return templateFileUrlServer;
	}

	public void setTemplateFileUrlServer(String templateFileUrlServer) {
		this.templateFileUrlServer = templateFileUrlServer;
	}

	public String getTemplateFileUrlClient() {
		return templateFileUrlClient;
	}

	public void setTemplateFileUrlClient(String templateFileUrlClient) {
		this.templateFileUrlClient = templateFileUrlClient;
	}
	
	
}
