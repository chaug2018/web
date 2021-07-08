package com.yzj.wf.scan.ui.selection;


/**
 * Event object describing a selection change. The source of these
 * events is a selection provider.
 *
 * @see ISelection
 * @see ISelectionProvider
 * @see ISelectionChangedListener
 */
public class SelectionChangedEvent extends SelectionEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1223265056678588167L;

	public SelectionChangedEvent(ISelectionProvider source, ISelection selection) {
		super(source, selection);
	}
}
