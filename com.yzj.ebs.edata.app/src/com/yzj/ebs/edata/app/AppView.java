/**
 * 创建于:2012-09-27<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 主界面
 * @author　 lif
 * @version 1.0.1
 */
package com.yzj.ebs.edata.app;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JOptionPane;

import com.yzj.ebs.edata.common.CheckImportResult;
import com.yzj.ebs.edata.common.CheckInformation;
import com.yzj.ebs.edata.common.DownloadFileBySFTP;
import com.yzj.ebs.edata.common.IDisplay;
import com.yzj.ebs.edata.common.IEdataService;
import com.yzj.ebs.edata.common.PublicDefine;
import com.yzj.ebs.edata.common.ReadEdataXml;
import com.yzj.ebs.edata.service.EdataServiceDefault;
import com.yzj.ebs.edata.tran.DataHttpService;

public class AppView extends javax.swing.JApplet implements IDisplay {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3122314267347869519L;
	boolean checkresult = false;
	DataHttpService dhs = new DataHttpService();
	DownloadFileBySFTP ftp = new DownloadFileBySFTP();
	private String ftpFilePath = "edata";

	/**
	 * 
	 */

	/** Initializes the applet Test */
	public void init() {
		try {
			java.awt.EventQueue.invokeAndWait(new Runnable() {
				public void run() {
					initComponents();
				}
			});

//			 String dataServerAdress = this.getParameter("dataServerAdress");
//			 PublicDefine.dataServerAdress = dataServerAdress;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// GEN-BEGIN:initComponents
	// <editor-fold defaultstate="collapsed" desc="Generated Code">
	private void initComponents() {

		jPanel1 = new javax.swing.JPanel();
		datadatelable = new javax.swing.JLabel();
		dataDatetxt = new javax.swing.JTextField();
		checkdatabutton = new javax.swing.JButton();
		importdatabutton = new javax.swing.JButton();
		logLabel = new javax.swing.JLabel();
		logListtxt = new java.awt.TextArea();
		databutton = new javax.swing.JButton();
		datadatelable.setText("\u6570\u636e\u65e5\u671f");
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
		String dateformat = sf.format(new Date());
		dataDatetxt.setText(dateformat);

		checkdatabutton.setText("\u6570\u636e\u6821\u9a8c");
		checkdatabutton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				checkdatabuttonActionPerformed(evt);
			}
		});

		importdatabutton.setText("\u6570\u636e\u5bfc\u5165");
		importdatabutton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				importdatabuttonActionPerformed(evt);
			}
		});

		logLabel.setText("\u65e5\u5fd7");

		databutton.setText("\u6570\u636e\u5904\u7406");
		databutton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				databuttonActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(
				jPanel1);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1Layout
				.setHorizontalGroup(jPanel1Layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								jPanel1Layout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												jPanel1Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(
																logListtxt,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																975,
																Short.MAX_VALUE)
														.addGroup(
																jPanel1Layout
																		.createSequentialGroup()
																		.addGroup(
																				jPanel1Layout
																						.createParallelGroup(
																								javax.swing.GroupLayout.Alignment.LEADING)
																						.addGroup(
																								jPanel1Layout
																										.createSequentialGroup()
																										.addComponent(
																												datadatelable)
																										.addPreferredGap(
																												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																										.addComponent(
																												dataDatetxt,
																												javax.swing.GroupLayout.PREFERRED_SIZE,
																												93,
																												javax.swing.GroupLayout.PREFERRED_SIZE)
																										.addGap(40,
																												40,
																												40)
																										.addComponent(
																												checkdatabutton,
																												javax.swing.GroupLayout.PREFERRED_SIZE,
																												106,
																												javax.swing.GroupLayout.PREFERRED_SIZE)
																										.addGap(34,
																												34,
																												34)
																										.addComponent(
																												importdatabutton,
																												javax.swing.GroupLayout.PREFERRED_SIZE,
																												107,
																												javax.swing.GroupLayout.PREFERRED_SIZE)
																										.addGap(32,
																												32,
																												32)
																										.addComponent(
																												databutton,
																												javax.swing.GroupLayout.PREFERRED_SIZE,
																												99,
																												javax.swing.GroupLayout.PREFERRED_SIZE))
																						.addComponent(
																								logLabel,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								963,
																								Short.MAX_VALUE))
																		.addContainerGap()))));
		jPanel1Layout
				.setVerticalGroup(jPanel1Layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								jPanel1Layout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												jPanel1Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																datadatelable)
														.addComponent(
																dataDatetxt,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(
																checkdatabutton)
														.addComponent(
																importdatabutton)
														.addComponent(
																databutton))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED,
												47, Short.MAX_VALUE)
										.addComponent(
												logLabel,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												17,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												logListtxt,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												388,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addContainerGap()));

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
				getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				layout.createSequentialGroup()
						.addContainerGap()
						.addComponent(jPanel1,
								javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addContainerGap(26, Short.MAX_VALUE)));
		layout.setVerticalGroup(layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				layout.createSequentialGroup()
						.addComponent(jPanel1,
								javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE)));
	}// </editor-fold>
		// GEN-END:initComponents

	/**
	 * 数据处理
	 * 
	 * @param evt
	 */
	private void databuttonActionPerformed(java.awt.event.ActionEvent evt) {
		IEdataService ds = new EdataServiceDefault(ftpFilePath + "\\"
				+ dataDatetxt.getText(), dataDatetxt.getText());
		if (docdatatxtcheck("process")) {
			ds.setDisplay(this);
			// 判断这个月核心有没把数据给全 主表
			try {
				long maindataday = dhs.queryTempDataDateDayHttpRequest(
						"EBS_maindata", dataDatetxt.getText().substring(0, 6));
				long tempaccnodetaildataday = dhs
						.queryTempDataDateDayHttpRequest("EBS_accnodetaildata_"
								+ dataDatetxt.getText().substring(4, 6),
								dataDatetxt.getText().substring(0, 6));
				String mouthday = dataDatetxt.getText().substring(6, 8);
				if (mouthday.equals(String.valueOf(maindataday))
						&& mouthday.equals(String
								.valueOf(tempaccnodetaildataday))) {
					checkresult = ds.dataDispose();
					if (checkresult) {
						javax.swing.JOptionPane.showMessageDialog(
								datadatelable, "数据处理成功");
						checkresult = true;
					}
				} else {
					int cd = javax.swing.JOptionPane.showConfirmDialog(null,
							dataDatetxt.getText() + "这月核心数据未给完，是否继续出账单？",
							"提示信息", JOptionPane.YES_NO_CANCEL_OPTION);
					if (cd == JOptionPane.YES_OPTION) {
						checkresult = ds.dataDispose();
						if (checkresult) {
							javax.swing.JOptionPane.showMessageDialog(
									datadatelable, "数据处理成功");
							checkresult = true;
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// GEN-BEGIN:variables
	// Variables declaration - do not modify
	private javax.swing.JButton checkdatabutton;
	private javax.swing.JTextField dataDatetxt;
	private javax.swing.JButton databutton;
	private javax.swing.JLabel datadatelable;
	private javax.swing.JButton importdatabutton;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JLabel logLabel;
	private java.awt.TextArea logListtxt;

	// End of variables declaration//GEN-END:variables

	/**
	 * 数据导入
	 * 
	 * @param evt
	 */
	private void importdatabuttonActionPerformed(java.awt.event.ActionEvent evt) {
		IEdataService ds = new EdataServiceDefault(ftpFilePath + "\\"
				+ dataDatetxt.getText(), dataDatetxt.getText());
		if (checkresult) {
			if (docdatatxtcheck("import")) {
				ds.setDisplay(this);
				CheckImportResult result = ds.importEdata();
				if (result != null) {
					if (result.failLine == 0) {
						javax.swing.JOptionPane.showMessageDialog(
								datadatelable, "导入成功");
					} else {
						javax.swing.JOptionPane.showMessageDialog(
								datadatelable, "导入错误");
					}
				}
			}
		} else {
			javax.swing.JOptionPane.showMessageDialog(datadatelable,
					"请校验成功再操作导入功能");
		}
	}

	/**
	 * 校验对账日期格式是否正确
	 * 
	 * @return
	 */
	private boolean docdatatxtcheck(String flag) {
		if ("".equals(dataDatetxt.getText()) || dataDatetxt.getText() == null) {
			// 提示请输入日期
			javax.swing.JOptionPane.showMessageDialog(datadatelable, "请输入日期");
			return false;
		} else {
			if (dataDatetxt.getText().length() != 8) {
				// 提示输入日期格式不对
				javax.swing.JOptionPane.showMessageDialog(datadatelable,
						"输入日期格式不对,例如：２０１２０８３１");
				return false;
			}
			if ("process".equals(flag)) {
				// 判断日期是否是一个月最后一天
				String year = dataDatetxt.getText().substring(0, 4);
				String month = dataDatetxt.getText().substring(4, 6);
				String day = dataDatetxt.getText().substring(6, 8);
				Calendar date = Calendar.getInstance();
				int yeari = Integer.parseInt(year);
				int monthi = Integer.parseInt(month);
				if (monthi == 1) {
					monthi = 12;
				}
				date.set(yeari, monthi - 1, 1);
				int maxDayOfMonth = date
						.getActualMaximum(Calendar.DAY_OF_MONTH);
				if (!String.valueOf(maxDayOfMonth).equals(day)) {
					// 提示输入日期不是最后一天
					javax.swing.JOptionPane.showMessageDialog(datadatelable,
							"输入日期不是最后一天");
					return true;
				}
			}
		}
		return true;
	}

	/**
	 * 数据校验
	 * 
	 * @param evt
	 */
	private void checkdatabuttonActionPerformed(java.awt.event.ActionEvent evt) {
		logListtxt.setText("");

		if (docdatatxtcheck("check")) {
			IEdataService ds = new EdataServiceDefault(ftpFilePath + "\\"
					+ dataDatetxt.getText(), dataDatetxt.getText());
			ds.setDisplay(this);
			try {
				
//				 boolean ok = true;
				// 检查核心有没给完
				 boolean ok = ds.checkEdata();
				if (ok) {
					checkresult = true;
				} else {
					javax.swing.JOptionPane.showMessageDialog(datadatelable,
							"校验" + dataDatetxt.getText() + "的数据文件失败，请检查！");
					checkresult = false;
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		AppView mv = new AppView();
		mv.init();
	}

	@Override
	public void showMsg(CheckInformation checkInformation) {
		String strTime;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		strTime = df.format(new Date());
		logListtxt.append(strTime + "      " +  checkInformation.getMsg() + "\r\n");
	}

}