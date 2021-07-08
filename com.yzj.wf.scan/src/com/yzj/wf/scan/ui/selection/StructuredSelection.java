package com.yzj.wf.scan.ui.selection;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * A concrete implementation of the <code>IStructuredSelection</code> interface,
 * suitable for instantiating.
 * <p>
 * This class is not intended to be subclassed.
 * </p>
 */
public class StructuredSelection implements IStructuredSelection {

    /**
     * The element that make up this structured selection.
     */
    private Object[] elements;
    

    /**
     * The index of the last selected element that makes up this structured selection.
     */
    private int lastElement;
    
    /**
     * The canonical empty selection. This selection should be used instead of
     * <code>null</code>.
     */
    public static final StructuredSelection EMPTY = new StructuredSelection();

    /**
     * Creates a new empty selection.  
     * See also the static field <code>EMPTY</code> which contains an empty selection singleton.
     *
     * @see #EMPTY
     */
    public StructuredSelection() {
    }

    /**
     * Creates a structured selection from the given elements.
     *
     * @param elements an array of elements
     */
    public StructuredSelection(Object[] elements, int lastIndex) {
    	if(elements != null) {
    		this.elements = new Object[elements.length];
    		System.arraycopy(elements, 0, this.elements, 0, elements.length);
    	} else {
    		this.elements = new Object[0];
    	}      
        this.lastElement = lastIndex;
    }

    /**
     * Creates a structured selection containing a single object.
     * The object must not be <code>null</code>.
     *
     * @param element the element
     */
    public StructuredSelection(Object element, int lastIndex) {
        if(element != null)
        	elements = new Object[] { element };
        
        this.lastElement = lastIndex;
    }

    /**
     * Creates a structured selection from the given <code>List</code>. 
     * @param elements list of selected elements
     */
    public StructuredSelection(List<?> elements, int lastIndex) {
        if(elements != null)
        	this.elements = elements.toArray();
        
        this.lastElement = lastIndex;
    }

    /**
     * Creates a structured selection from the given <code>List</code>. 
     * @param elements list of selected elements
     */
    public StructuredSelection(IStructuredSelection elements, Object lastSelectedObj) {
        if(elements != null) {
        	this.elements = elements.toArray();
        	
        	if (lastSelectedObj != null) {
		        for (int i=0; i<elements.size(); i++) {
		        	if (this.elements[i].equals(lastSelectedObj)) {
		        		this.lastElement = i;
		        	}
		        }
        	}
        }
    }
    
    /**
     * Returns whether this structured selection is equal to the given object.
     * Two structured selections are equal iff they contain the same elements
     * in the same order.
     *
     * @param o the other object
     * @return <code>true</code> if they are equal, and <code>false</code> otherwise
     */
    @Override
	public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        //null and other classes
        if (!(o instanceof StructuredSelection)) {
            return false;
        }
        StructuredSelection s2 = (StructuredSelection) o;

        // either or both empty?
        if (isEmpty()) {
            return s2.isEmpty();
        }
        if (s2.isEmpty()) {
            return false;
        }

        //size
        int myLen = elements.length;
        if (myLen != s2.elements.length) {
            return false;
        }
        //element comparison
        for (int i = 0; i < myLen; i++) {
            if (!elements[i].equals(s2.elements[i])) {
                return false;
            }
        }
        return true;
    }

    /* (non-Javadoc)
     * Method declared in IStructuredSelection.
     */
    public Object getFirstElement() {
        return isEmpty() ? null : elements[0];
    }

    /* (non-Javadoc)
     * Method declared in IStructuredSelection.
     */
    public Object getLastElement() {
    	if (elements != null && lastElement > 0 && lastElement < elements.length)
    		return elements[lastElement];
    	
        return isEmpty() ? null : elements[0];
    }

    /* (non-Javadoc)
     * Method declared in ISelection.
     */
    public boolean isEmpty() {
        return elements == null || elements.length == 0;
    }

    /* (non-Javadoc)
     * Method declared in IStructuredSelection.
     */
    public Iterator<?> iterator() {
        return Arrays.asList(elements == null ? new Object[0] : elements)
                .iterator();
    }

    /* (non-Javadoc)
     * Method declared in IStructuredSelection.
     */
    public int size() {
        return elements == null ? 0 : elements.length;
    }

    /* (non-Javadoc)
     * Method declared in IStructuredSelection.
     */
    public Object[] toArray() {
        return elements == null ? new Object[0] : (Object[]) elements.clone();
    }

    /* (non-Javadoc)
     * Method declared in IStructuredSelection.
     */
    public List<?> toList() {
        return Arrays.asList(elements == null ? new Object[0] : elements);
    }

    /**
     * Internal method which returns a string representation of this
     * selection suitable for debug purposes only.
     *
     * @return debug string
     */
    @Override
	public String toString() {
        return isEmpty() ? "<empty_selection>" : toList().toString(); //$NON-NLS-1$
    }
}
