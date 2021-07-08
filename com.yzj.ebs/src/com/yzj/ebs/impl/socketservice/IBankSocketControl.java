package com.yzj.ebs.impl.socketservice;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.yzj.wf.com.ibank.common.IBankAssemble;
import com.yzj.wf.com.ibank.common.IBankAssembleException;
import com.yzj.wf.com.ibank.common.IBankFix;
import com.yzj.wf.com.ibank.common.IBankFixException;
import com.yzj.wf.com.ibank.common.IBankRevise;
import com.yzj.wf.com.ibank.common.IBankReviseException;
import com.yzj.wf.com.ibank.common.IBankTemplateFactory;
import com.yzj.wf.com.ibank.common.ITallyFlowListException;
import com.yzj.wf.com.ibank.common.ITallyFlowListService;
import com.yzj.wf.com.ibank.common.TradeSet;
import com.yzj.wf.com.ibank.common.IBankDefine.DataFlowDirection;
import com.yzj.wf.com.ibank.common.IBankDefine.MapsTemplate;
import com.yzj.wf.com.ibank.common.IBankDefine.TradeDebug;
import com.yzj.wf.com.ibank.common.IBankDefine.TradeService;
import com.yzj.wf.com.ibank.common.IBankDefine.TradeTrace;
import com.yzj.wf.com.ibank.common.server.IBankControl;
import com.yzj.wf.com.ibank.common.server.IBankControlException;
import com.yzj.wf.com.ibank.common.server.IBankProcess;
import com.yzj.wf.com.ibank.common.server.IBankProcessException;
import com.yzj.wf.com.ibank.common.server.IBankServer;
import com.yzj.wf.com.ibank.common.server.IBankServerException;
import com.yzj.wf.com.ibank.common.server.IBankServerTradeCodeGetter;
import com.yzj.wf.com.ibank.common.server.ThreadPoolException;
import com.yzj.wf.com.ibank.common.server.ThreadPoolRunnable;
import com.yzj.wf.com.ibank.common.server.ThreadPoolDefine.Key;
import com.yzj.wf.com.ibank.common.template.Trade;
import com.yzj.wf.com.ibank.servicefactory.ServiceFactory;
import com.yzj.wf.com.ibank.toolkit.tools.TraceHelper;
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
	private com.yzj.wf.common.WFLogger logger = WFLogger
			.getLogger(getClass());

	private String port; // 监听端口
	private ServiceFactory serviceFactory;
	private IBankRevise reviseService;
	private IBankFix fixService;

	// private String templateServiceIdentity = null; //模板服务标识
	// private String tradeCodeGetterIdentity; //交易码获取器服务标识
	// private String ibankServerIdentity; //服务组件标识

	private ServerSocket serverSocket = null;
	private Boolean isStop = true; // 控制器默认停止运行

	private IBankServer server = null; // 服务组件
	private IBankTemplateFactory templateFactory = null;

	private IBankServerTradeCodeGetter tradeCodeGetter = null;
	private SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat sdfMs = new SimpleDateFormat("HHmmssSSS");

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
				// serverSocket.setReuseAddress(true); //绑定端口之前设置选项
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

						ITallyFlowListService tallyFlowListService = null;
						// IBankRevise reviseService;
						IBankAssemble assembleService = null;
						// IBankExtern externService = null;
						IBankProcess processService = null;

						try {
							os = taskSocket.getOutputStream();
							is = taskSocket.getInputStream();

							byte[] dataDown = new byte[3000]; // 对方 -> 我方
							byte[] dataUp = null; // 我方 -> 对方

							int len = is.read(dataDown);
							if (len < 4) {
								// logger.info("connect -> interrupt...");
								return;
							}
							is.read(dataDown);
							String dataDownStr = new String(dataDown, 0, len); // 将字节数组转为字符串
							String tradeCode = tradeCodeGetter
									.getTradeCode(dataDownStr);
							TradeSet tradeSet = new TradeSet(tradeCode); // 客户端交易对象
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

							String tracepath = trade
									.getParamValueByName("tracepath");
							boolean isTraceFile = false;
							String filename = "";
							if (tracepath != null && trade.getTrace() == TradeTrace.TRACE_RECORD.getValue()) {
								isTraceFile = true;
								Date date = new Date();
								String dateStr = sdfDay.format(date);
								String tradeId = trade.getId();
								tracepath = tracepath + "/" + dateStr + "/" + tradeId + "/";
								String time = sdfMs.format(date);
								String uuid = UUID.randomUUID().toString().replace("-", "").toUpperCase();
								filename = tracepath + time + "_" + uuid + ".txt";
							}

							if (trade.getDebug() == TradeDebug.DEBUG_TEST
									.getValue()) {
								tradeSet.setBusinessId(1);
							}

							tallyFlowListService = (ITallyFlowListService) serviceFactory
									.getService(
											ITallyFlowListService.class,
											trade.getParamValueByName(TradeService.TALLYFLOWLIST
													.getValue()));
							assembleService = (IBankAssemble) serviceFactory.getService(
									IBankAssemble.class,
									trade.getParamValueByName(TradeService.IBANKASSEMBLE
											.getValue()));
							processService = (IBankProcess) serviceFactory.getService(
									IBankProcess.class,// "IBANKPROCESS_SERVICE");
									trade.getParamValueByName(TradeService.IBANKPROCESS
											.getValue()));

							try {
								if (assembleService != null) {
									assembleService.downAssemble(trade,
											dataDown);
								} else {
									logger.error("down assemble service doesn't exist");
								}

								if (fixService != null) {
									fixService.downFix(trade);
									if (isTraceFile) {
										TraceHelper.writeToFile_DOWN_EXTERN(filename, trade, true);
									}
								} else {
									logger.error("Down fix service doesn't exist!");
								}

								if (reviseService != null) {
									reviseService.downRevise(tradeSet, trade);
								} else {
									logger.error("Down Revise service doesn't exist!");
								}

								if (tallyFlowListService != null) {
									tallyFlowListService.writeTallyFlowList(
											trade, tradeSet,
											DataFlowDirection.DOWN_FLOW);
								} else {
									logger.error("Down tallyFlowList service doesn't exist");
								}
							} catch (IBankAssembleException e1) {
								throw new ThreadPoolException(
										"down assemble service exception"
												+ e1.getMessage());
							} catch (IBankFixException e2) {
								throw new ThreadPoolException(
										"down fix service exception"
												+ e2.getMessage());
							} catch (IBankReviseException e3) {
								throw new ThreadPoolException(
										"down revise service exception"
												+ e3.getMessage());
							} catch (ITallyFlowListException e4) {
								throw new ThreadPoolException(
										"write tallyflowlist error"
												+ e4.getMessage());
							}catch (Exception e) {
								e.printStackTrace();
							}
							
							try {
								tradeSet = processService.execTrade(tradeSet);
							} catch (IBankProcessException e) {
								logger.error("process server process exception");
								throw new ThreadPoolException(
										"server process exception"
												+ e.getMessage());
							}

							logger.debug("start up upExecute service(here->there)");
							try {
								if (reviseService != null) {
									reviseService.upRevise(trade, tradeSet);
								} else {
									logger.info("up revise service doesn't exist");
								}
								if (tallyFlowListService != null) {
									tallyFlowListService.writeTallyFlowList(
											trade, tradeSet,
											DataFlowDirection.UP_FLOW);

								} else {
									logger.info("up tallyflowlist service doesn't exist!");
								}
								if (fixService != null) {
									fixService.upFix(trade);
								} else {
									logger.info("up assemble service doesn't exist");
								}
								if (assembleService != null) {
									dataUp = assembleService.upAssemble(trade);
									if (isTraceFile) {
										StringBuffer sb = new StringBuffer();
										sb.append(System.getProperty("line.separator"));
										TraceHelper.writeToFile(filename, sb.toString(), true);
										
										TraceHelper.writeToFile_UP_EXTERN(filename, trade, true);
										sb = new StringBuffer();
										sb.append(System.getProperty("line.separator"));
										sb.append("UpAssemble:  #");
										sb.append(new String(dataUp));
										sb.append("#");
										sb.append(System.getProperty("line.separator"));
										TraceHelper.writeToFile(filename, sb.toString(), true);
									}
								} else {
									logger.info("up assemble service doesn't exist");
								}

							} catch (IBankReviseException e) {
								logger.error("up revise service exception");
								throw new ThreadPoolException(
										"up revise service exception"
												+ e.getMessage());
							} catch (ITallyFlowListException e2) {
								logger.error("write tallyFlowList error");
								throw new ThreadPoolException(
										"write tallyflowlist error"
												+ e2.getMessage());
							} catch (IBankFixException e3) {
								logger.error("up fix service exception");
								throw new ThreadPoolException(
										"up fix service exception"
												+ e3.getMessage());
							} catch (IBankAssembleException e4) {
								logger.error("up assemble service exception");
								throw new ThreadPoolException(
										"up assemble service exception"
												+ e4.getMessage());
							}catch (Exception e) {
								e.printStackTrace();
							}
							os.write(dataUp);
                            System.out.println(new String(dataUp,"utf-8"));
						} catch (IOException e) {
							logger.error("I/O operate exception:"
									+ e.getMessage());
							throw new ThreadPoolException(
									"I/O operate exception" + e.getMessage(), e);
						} finally {
							try {
								if (os != null) {
									os.close();
								}
								if (is != null) {
									is.close();
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
}
