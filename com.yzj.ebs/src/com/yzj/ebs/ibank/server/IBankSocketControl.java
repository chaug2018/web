package com.yzj.ebs.ibank.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBException;

import com.yzj.ebs.common.IPublicTools;
import com.yzj.ebs.ibank.server.IBankAssembleService;
import com.yzj.ebs.ibank.client.IBankClient;
import com.yzj.ebs.ibank.server.Trade2100;
import com.yzj.wf.com.ibank.common.IBankAssembleException;
import com.yzj.wf.com.ibank.common.IBankFix;
import com.yzj.wf.com.ibank.common.IBankRevise;
import com.yzj.wf.com.ibank.common.IBankTemplateFactory;
import com.yzj.wf.com.ibank.common.IBankDefine.MapsTemplate;
import com.yzj.wf.com.ibank.common.IBankDefine.TradeService;
import com.yzj.wf.com.ibank.common.server.IBankControl;
import com.yzj.wf.com.ibank.common.server.IBankControlException;
import com.yzj.wf.com.ibank.common.server.IBankProcessException;
import com.yzj.wf.com.ibank.common.server.IBankServer;
import com.yzj.wf.com.ibank.common.server.IBankServerException;
import com.yzj.wf.com.ibank.common.server.IBankServerTradeCodeGetter;
import com.yzj.wf.com.ibank.common.server.ThreadPoolException;
import com.yzj.wf.com.ibank.common.server.ThreadPoolRunnable;
import com.yzj.wf.com.ibank.common.server.ThreadPoolDefine.Key;
import com.yzj.wf.com.ibank.common.template.Trade;
import com.yzj.wf.com.ibank.servicefactory.ServiceFactory;
import com.yzj.wf.common.WFLogger;

/**
 * 功能: IBank服务端控制组件, 基于SOCKET<br>
 * 版权所有(C) 2009 深圳市银之杰科技股份有限公司<br>
 * 创建 2009-10-25 BeiJing<br>
 * 
 * @author rain<br>
 * @version 1.0<br>
 */
public class IBankSocketControl implements IBankControl {
	private com.yzj.wf.common.WFLogger logger = WFLogger.getLogger(getClass());

	private String port; // 监听端口
	private ServiceFactory serviceFactory;
	private IBankRevise reviseService;
	private IBankFix fixService;

	private ServerSocket serverSocket = null;
	private Boolean isStop = true; // 控制器默认停止运行

	private IBankServer server = null; // 服务组件
	private IBankTemplateFactory templateFactory = null;

	private IBankServerTradeCodeGetter tradeCodeGetter = null;
	private IBankAdm ibankAdm;
	private IBankClient iBankClient;
	private StrToFile strToFile;
	private IPublicTools publicTools;

	public void start() throws IBankControlException {
		if (isStop) {
			try {
				init();

				server.start();

				isStop = false;

			} catch (IBankServerException e2) {
				logger.error("ibank server exception");
				throw new IBankControlException("ibank server start exception",
						e2);
			}

			try {
				serverSocket = new ServerSocket();

				serverSocket
						.bind(new InetSocketAddress(Integer.parseInt(port)));
				logger.info("ibank Server listen on port:" + port);
				// System.out.println("server listen on port:"+port);

			} catch (NumberFormatException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e) {
				logger.error("I/O operate exception:" + e.getMessage());
				throw new IBankControlException("I/O operate exception", e);
			}
			// logger.info("****socket service start listen****");

			while (!isStop) {
				final Socket socket;
				try {
					socket = serverSocket.accept();
				} catch (IOException e) {
					logger.error("I/O operate exception:" + e.getMessage());
					throw new IBankControlException("I/O operate exception", e);
				}
				server.execute(new ThreadPoolRunnable() {
					// 工作任务内部类
					@SuppressWarnings("synthetic-access")
					public void runIt(Map<Key, String> thData)
							throws ThreadPoolException {
						Socket taskSocket = socket;
						OutputStream os = null;
						InputStream is = null;
						Trade trade = null;
						BufferedReader br = null;

						try {
							os = taskSocket.getOutputStream();
							is = taskSocket.getInputStream();
							br= new BufferedReader(new InputStreamReader(is, "UTF-8"));
							char[] dataDownc = new char[1024]; //对方 -> 我方
							byte[] dataUp = null; // 我方 -> 对方
							int num = 0;
							String dataDownStr = "";
							//阻塞式的接收流
							num = br.read(dataDownc);
							//将字符数组转换为字符串
							dataDownStr = new String(dataDownc,0,num);
							//System.out.println("服务器接收：");
							//System.out.println(dataDownStr);
							//记载日志
							strToFile.creatFile(dataDownStr);
							String tradeCode = tradeCodeGetter
									.getTradeCode(dataDownStr);
							thData.put(Key.ITEM_INFO, dataDownStr); // 设置接收到的信息串

							logger.info("trade code: " + tradeCode
									+ " find the trade");
							logger.debug("Rec dataDown:" + dataDownStr);
							// System.out.println("TRACE:(rec:)"+dataDownStr);

							try {
								trade = templateFactory.getTradeByTemplate(
										MapsTemplate.SERVER, tradeCode);

								if (trade == null) {
									logger.error("trade template service doesn't exist by trade code");
									throw new ThreadPoolException(
											"trade template service doesn't exist by trade code");
								}
							} catch (Exception e) {
								logger.error(
										"trade template service doesn't exist by trade code exception",
										e);
								throw new ThreadPoolException(
										"trade template service doesn't exist by trade code exception");
							}

							
							IBankAssembleService assembleImp = new IBankAssembleService();
							//解析报文
							try {
								assembleImp.downAssemble(trade, dataDownStr);
							} catch (IBankAssembleException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							//获取解析后带上传value的报文
							Trade tradeAfterAss  = assembleImp.getTrade();
							//根据交易代码执行具体的交易
							if(Integer.parseInt(tradeCode)==2100){
								Trade2100 trade2100 = new Trade2100();
								try {
									dataUp = trade2100.execTrade(tradeAfterAss,ibankAdm,strToFile);
								} catch (IBankProcessException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (CloneNotSupportedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							if(Integer.parseInt(tradeCode)==2101){
								Trade2101 trade2101 = new Trade2101();
								try {
									dataUp = trade2101.execTrade(tradeAfterAss,ibankAdm,
											strToFile,iBankClient,publicTools);
								} catch (IBankProcessException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (CloneNotSupportedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							if(Integer.parseInt(tradeCode)==2102) {
								Trade2102 trade2102 = new Trade2102();
								try {
									dataUp = trade2102.queryVouchernoList(dataDownStr,ibankAdm,strToFile);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							os.write(dataUp);
							os.flush();
							//System.out.println("服务器发送：");
							//System.out.println(new String(dataUp, "UTF-8"));
						} catch (IOException e) {
							logger.error("I/O operate exception:"
									+ e.getMessage());
							throw new ThreadPoolException(
									"I/O operate exception" + e.getMessage(), e);
							
						}catch (Exception e) {
							logger.error(e.getMessage());
							throw new ThreadPoolException(e.getMessage());
						} finally {
							try {
								if (os != null) {
									os.close();
								}
								if (is != null) {
									is.close();
								}
								if (br != null) {
									br.close();
								}
								if (taskSocket != null) {
									taskSocket.close();
								}
							} catch (IOException e) {
								logger.error("clean resource exception:"
										+ e.getMessage());
								throw new ThreadPoolException(
										"clean resource exception", e);
							}
						}
					}

					public Map<Key, String> getInitData() {
						// 初始化信息MAP
						return new HashMap<Key, String>();
					}
				});
			}
		}
	}

	public void stop() {
		if (!isStop) {
			server.stop();
			isStop = true;
			if (serverSocket != null) {
				serverSocket = null;
			}
			logger.info("****socket service stop listen****");
		}
	}

	/**
	 * 由配置文件初始化相关参数和服务标识
	 * 
	 * @throws IBankControlException
	 *             IBankControlException异常
	 */
	private void init() throws IBankControlException {

		if (port == null || port.trim().equals("")) {
			throw new IBankControlException("Listen port doesn't exist!");
		}

		// 查找服务组件
		server = (IBankServer) serviceFactory.getService(IBankServer.class,
				TradeService.IBANKSERVER.getValue());
		if (server == null) {
			throw new IBankControlException(
					"ibankserver service doesn't exist!");
		}
		// 查找交易代码获取器服务
		tradeCodeGetter = (IBankServerTradeCodeGetter) serviceFactory
				.getService(IBankServerTradeCodeGetter.class,
						TradeService.IBANKTRADECODEGETTER.getValue());
		if (tradeCodeGetter == null) {
			throw new IBankControlException(
					"tradeCodeGetter service doesn't exist!");
		}
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public IBankTemplateFactory getTemplateFactory() {
		return templateFactory;
	}

	public void setTemplateFactory(IBankTemplateFactory templateFactory) {
		this.templateFactory = templateFactory;
	}

	public ServiceFactory getServiceFactory() {
		return serviceFactory;
	}

	public void setServiceFactory(ServiceFactory serviceFactory) {
		this.serviceFactory = serviceFactory;
	}

	public IBankRevise getReviseService() {
		return reviseService;
	}

	public void setReviseService(IBankRevise reviseService) {
		this.reviseService = reviseService;
	}

	public IBankFix getFixService() {
		return fixService;
	}

	public void setFixService(IBankFix fixService) {
		this.fixService = fixService;
	}

	public IBankAdm getIbankAdm() {
		return ibankAdm;
	}

	public void setIbankAdm(IBankAdm ibankAdm) {
		this.ibankAdm = ibankAdm;
	}

	public IBankClient getiBankClient() {
		return iBankClient;
	}

	public void setiBankClient(IBankClient iBankClient) {
		this.iBankClient = iBankClient;
	}

	public StrToFile getStrToFile() {
		return strToFile;
	}

	public void setStrToFile(StrToFile strToFile) {
		this.strToFile = strToFile;
	}

	public IPublicTools getPublicTools() {
		return publicTools;
	}

	public void setPublicTools(IPublicTools publicTools) {
		this.publicTools = publicTools;
	}
	
}
