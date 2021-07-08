/**
 * BatchScanView.java
 * 版权所有(C) 2011 深圳市银之杰科技股份有限公司
 * 创建:LiuQiangQiang 2012-2-14
 */
package com.yzj.wf.scan.mainview;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.tree.DefaultTreeModel;

import com.yzj.wf.scan.appletparam.IDealAppletParam;
import com.yzj.wf.scan.gui.batchtree.BatchTree;
import com.yzj.wf.scan.gui.batchtree.BatchTreeNode;
import com.yzj.wf.scan.init.ScanButtonAction;
import com.yzj.wf.scan.init.ScanToolBarPanel;
import com.yzj.wf.scan.panel.BatchListPane;
import com.yzj.wf.scan.tree.CustomTreeNode;
import com.yzj.wf.scan.util.AbstractNodeList;
import com.yzj.wf.scan.util.IAppletPanel;
import com.yzj.wf.scan.util.ReadConfig;
import com.yzj.wf.scan.util.SysCacheContext;
import com.yzj.wf.scan.util.ImageStoreDef.AppletParamKey;

/**
 * 批量扫描
 * 
 * @author LiuQiangQiang
 * @version 1.0.0
 */
public class BatchScanView extends JFrame implements IAppletPanel {

	private static final long serialVersionUID = -1633204781876729067L;

	private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(BatchScanView.class);

	private SysCacheContext sysCacheContext;

	private ReadConfig readConfig;

	private JPanel jpanel;

	private AbstractNodeList abstractNodeList;

	private ScanToolBarPanel toolBarPanel;


	private BatchTree batchTree;

	private boolean parseflag;

	public BatchScanView(SysCacheContext sysCacheContext, ReadConfig readConfig) {
		try {
			if (sysCacheContext == null)
				throw new NullPointerException("SysCacheContext is null");
			if (readConfig == null)
				throw new NullPointerException("ReadConfig is null");
			this.sysCacheContext = sysCacheContext;
			this.readConfig = readConfig;

			System.setSecurityManager(null);
			initGUI();
		} catch (Exception e) {
			logger.error("BatchScanView initialize is error", e);
		}
	}

	private void initGUI() throws Exception {
		jpanel = new JPanel();
		jpanel.setLayout(new BorderLayout());
		jpanel.setBackground(Color.WHITE);

		toolBarPanel = new ScanToolBarPanel(ScanButtonAction.initTools(), this);


		abstractNodeList = new BatchListPane(this);

		batchTree = new BatchTree(new DefaultTreeModel(getRootNode()), this);
		
		JScrollPane jScrollPane = new JScrollPane(batchTree);
		jScrollPane.setMinimumSize(new Dimension(180, 100));

		JSplitPane jSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jScrollPane, abstractNodeList);
		jSplitPane.setContinuousLayout(true);
		jSplitPane.setOneTouchExpandable(true);
		jSplitPane.setDividerSize(10);

		jpanel.add(toolBarPanel, BorderLayout.NORTH);

		jpanel.add(jSplitPane, BorderLayout.CENTER);

		batchTree.addSelectionChangedListener(abstractNodeList);

		toolBarPanel.addSelectionChangedListener(batchTree);

		toolBarPanel.addSelectionChangedListener(abstractNodeList);

		pack();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yinzhijie.util.IAppletPanel#getJPanel()
	 */
	
	public JPanel getJPanel() {
		return jpanel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yinzhijie.util.IAppletPanel#prepareImageNodeList()
	 */
	
	public void prepareImageNodeList() {
	}

	public SysCacheContext getSysCacheContext() {
		return sysCacheContext;
	}

	public BatchTreeNode getRootNode() {
		BatchTreeNode node = new BatchTreeNode();
		return node;
	}

	public CustomTreeNode getCustomTreeRootNode() throws Exception {
		if (!parseflag) {
			IDealAppletParam dealAppletParam = sysCacheContext.getDealAppletParam();
			dealAppletParam.getTreeNodeByParamName(sysCacheContext.getRootNode(), AppletParamKey.PURVIEW, sysCacheContext.getOrders());
			parseflag = true;
		}
		return sysCacheContext.getRootNode();
	}


	public ReadConfig getReadConfig() {
		return readConfig;
	}

	public BatchTree getBatchTree() {
		return batchTree;
	}

	public AbstractNodeList getAbstractNodeList() {
		return abstractNodeList;
	}

	public ScanToolBarPanel getToolBarPanel() {
		return toolBarPanel;
	}
}
