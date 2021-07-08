package com.yzj.wf.scan.ui.selection;

import java.util.EventObject;

/**
 * Event object describing a selection change. The source of these
 * events is a selection provider.
 *
 */

public class SelectionEvent extends EventObject {

    
    /**
	 * 
	 */
	private static final long serialVersionUID = -8577071918655142011L;
	
	/**
     * The selection.
     */
    protected transient ISelection selection;

    /**
     * Creates a new event for the given source and selection.
     *
     * @param source the selection provider
     * @param selection the selection
     */
    public SelectionEvent(ISelectionProvider source, ISelection selection) {
        super(source);
        if(selection != null)
        	this.selection = selection;
    }

    /**
     * Returns the selection.
     *
     * @return the selection
     */
    public ISelection getSelection() {
        return selection;
    }

    /**
     * Returns the selection provider that is the source of this event.
     *
     * @return the originating selection provider
     */
    public ISelectionProvider getSelectionProvider() {
        return (ISelectionProvider) getSource();
    }
}
