package com.yzj.wf.scan.ui.selection;

/**
 * Interface common to all objects that provide a selection.
 *
 * @see ISelection
 * @see SelectionChangedEvent
 */
public interface ISelectionProvider {
	
    /**
     * Returns the id for this provider.
     * 
     * @return the provider id
     */
	public String getProviderId();
	
    /**
     * Adds a listener for selection changes in this selection provider.
     * Has no effect if an identical listener is already registered.
     *
     * @param listener a selection changed listener
     */
    public void addSelectionChangedListener(ISelectionChangedListener listener);

    /**
     * Returns the current selection for this provider.
     * 
     * @return the current selection
     */
    public ISelection getSelection();

    /**
     * Removes the given selection change listener from this selection provider.
     * Has no affect if an identical listener is not registered.
     *
     * @param listener a selection changed listener
     */
    public void removeSelectionChangedListener(ISelectionChangedListener listener);

    /**
     * Sets the current selection for this selection provider.
     *
     * @param selection the new selection
     */
    public void setSelection(ISelection selection);
}
