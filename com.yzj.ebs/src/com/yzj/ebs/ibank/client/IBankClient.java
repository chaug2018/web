package com.yzj.ebs.ibank.client;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.yzj.ebs.ibank.server.IBankAdm;
import com.yzj.wf.com.ibank.common.IBankTemplateFactory;

public class IBankClient {
	private IBankAdm ibankAdm;
	private ClientToFile clientToFile;
	private IBankTemplateFactory templateFactory = null;
	private int POOL_SIZE = 10;
	public void startByAccno(String accno){
		if(accno!=null && !accno.equals("")){
			ClientThread it = new ClientThread(ibankAdm,accno,templateFactory,clientToFile);
//			Thread tt = new Thread(it);
//			tt.start();
			ExecutorService excutorService = Executors.newFixedThreadPool(POOL_SIZE);
			excutorService.execute(it);
		}
	}
	public IBankAdm getIbankAdm() {
		return ibankAdm;
	}
	public void setIbankAdm(IBankAdm ibankAdm) {
		this.ibankAdm = ibankAdm;
	}
	public IBankTemplateFactory getTemplateFactory() {
		return templateFactory;
	}
	public void setTemplateFactory(IBankTemplateFactory templateFactory) {
		this.templateFactory = templateFactory;
	}
	public ClientToFile getClientToFile() {
		return clientToFile;
	}
	public void setClientToFile(ClientToFile clientToFile) {
		this.clientToFile = clientToFile;
	}
	
}