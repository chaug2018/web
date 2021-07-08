package com.yzj.wf.scan.init;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

import com.yzj.wf.scan.actionbutton.ButtonObject;
import com.yzj.wf.scan.mainview.BatchScanView;
import com.yzj.wf.scan.ui.selection.ISelection;
import com.yzj.wf.scan.ui.selection.ISelectionChangedListener;
import com.yzj.wf.scan.ui.selection.ISelectionProvider;
import com.yzj.wf.scan.ui.selection.SelectionChangedEvent;
import com.yzj.wf.scan.ui.selection.StructuredSelection;
import com.yzj.wf.scan.util.IAppletPanel;
import com.yzj.wf.scan.util.IConfirmAction;
import com.yzj.wf.scan.util.ImageIconFactory;
import com.yzj.wf.scan.util.ToolBarPanel;

/**
 * 
 *创建于:2012-8-16<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 初始化工具栏
 * @author 陈林江
 * @version 1.0.1
 */
public class ScanToolBarPanel extends ToolBarPanel {

	private static final long serialVersionUID = 2262564356865573170L;

	private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ScanToolBarPanel.class);

	private JToolBar toolBar;

	private List<ButtonObject> scanButtonList = null;

	private Vector<ISelectionChangedListener> selectionChangeListener;

	private ScanButtonControl scanButton;

	private BatchScanView view;

	private Font font = new Font("Default", 0, 10);

	public ScanToolBarPanel(List<ButtonObject> scanButtonList, BatchScanView view) {
		super();
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.view = view;
		logger.info("start ScanTooLBarPanel GUI");
		selectionChangeListener = new Vector<ISelectionChangedListener>(4);
		setLayout(new BorderLayout());
		this.scanButtonList = scanButtonList;
		scanButton = new ScanButtonControl(this, view);
		creatToolBar();
		setButton(view.getJPanel());
		add(toolBar);
		logger.info("End ScanTooLBarPanel GUI");
	}

	/**
	 * @return
	 */
	private void creatToolBar() {
		if (toolBar == null) {
			toolBar = new JToolBar();
			toolBar.setBorder(new LineBorder(Color.black, 1, false));
			toolBar.setMargin(new Insets(0, 0, 0, 0));
			toolBar.add(scanButton.getDriver_Button());
			toolBar.add(scanButton.getSet_Button());
			toolBar.add(scanButton.getScan_Button());
			toolBar.add(scanButton.getInsert_Button());
			toolBar.add(scanButton.getBusSet_Button());
		}
	}

	@SuppressWarnings("serial")
	private void setButton(JComponent component) {
		if (toolBar != null && scanButtonList != null) {
			for (final ButtonObject scanObject : scanButtonList) {
				JButton button = new JButton();
				button.setIcon(ImageIconFactory.getImageIcon(scanObject.getIconAddr()));
				button.setFocusable(false);
				button.setMaximumSize(new Dimension(65, 100));
				button.setText(scanObject.getButtonName());
				button.setToolTipText(scanObject.getToolTipText());
				button.setHorizontalTextPosition(SwingConstants.CENTER);
				button.setVerticalTextPosition(SwingConstants.BOTTOM);
				button.setFont(font);
				Action action = new AbstractAction() {
					public void actionPerformed(final ActionEvent event) {
						operatorType(scanObject);
					}
				};
				button.addActionListener(action);
				component.registerKeyboardAction(action, KeyStroke.getKeyStroke(scanObject.getKeyEvent(), scanObject.getModifiers()), JComponent.WHEN_IN_FOCUSED_WINDOW);
				toolBar.add(button);
			}
		}
	}

	private void operatorType(ButtonObject scanObject) {
		try {
			IConfirmAction action = scanObject.getAction();
			if (action == null) {
				logger.debug("获取处理类对象为NULL！");
				return;
			}
			action.setToolBarPanel(this);
			boolean flag = action.confirm();
			if (!flag) {
				logger.debug("操作失败！");
				return;
			}
			flag = action.isNotifyListener();
			if (flag) {
				// 获取选择项
				ISelection selection = action.getStructuredSelection();
				if (selection == null)
					selection = getSelection();
				// 通知监听者
				toolBarPanelChanged(ScanToolBarPanel.this, selection);
			}
		} catch (Exception e) {
			logger.error("操作类型：" + scanObject.getButtonName() + " 失败！原因为：" + e.getMessage(),e);
		}
	}

	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		if (!selectionChangeListener.contains(listener)) {
			this.selectionChangeListener.add(listener);
		}
	}

	public String getProviderId() {
		return null;
	}

	public ISelection getSelection() {
		ISelection selection = new StructuredSelection();
		return selection;
	}

	public void removeSelectionChangedListener(ISelectionChangedListener listener) {
		if (listener != null)
			this.selectionChangeListener.remove(listener);
	}

	public void setSelection(ISelection selection) {
		
	}

	@Override
	public synchronized void toolBarPanelChanged(ISelectionProvider source, ISelection selection) {
		Object[] seListeners;
		seListeners = this.selectionChangeListener.toArray();
		SelectionChangedEvent scEvent = new SelectionChangedEvent(this, selection);
		for (int i = 0; i < seListeners.length; i++) {
			((ISelectionChangedListener) seListeners[i]).selectionChanged(scEvent);
		}
	}
	

	public BatchScanView getView() {
		return view;
	}

	public void freshTempDir() {
		scanButton.creatTempDir();
	}

	public IAppletPanel getAppletPanel() {
		return view;
	}
	
}
