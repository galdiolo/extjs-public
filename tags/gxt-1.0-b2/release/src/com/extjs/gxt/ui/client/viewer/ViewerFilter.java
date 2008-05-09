/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Ext GWT - derived implementation
 *******************************************************************************/
package com.extjs.gxt.ui.client.viewer;

import java.util.ArrayList;
import java.util.List;

/**
 * A viewer filter is used by a viewer to extract a subset of elements provided
 * by its content provider.
 * 
 * <p>
 * This code is based on JFace API from the Eclipse Project.
 * </p>
 * 
 * @see ContentProvider
 * @see Viewer
 */
public abstract class ViewerFilter<P, E> {
  
  /**
   * Creates a new viewer filter.
   */
  protected ViewerFilter() {
  }

  /**
   * Filters the given elements for the given viewer. The input array is not
   * modified.
   * <p>
   * The default implementation of this method calls <code>select</code> on
   * each element in the array, and returns only those elements for which
   * <code>select</code> returns <code>true</code>.
   * </p>
   * 
   * @param viewer the viewer
   * @param parent the parent element
   * @param elements the elements to filter
   * @return the filtered elements
   */
  public List<Object> filter(Viewer viewer, P parent, List<E> elements) {
    ArrayList<Object> out = new ArrayList<Object>();
    if (elements != null) {
      for (E e : elements) {
        if (select(viewer, parent, e)) {
          out.add(e);
        }
      }
    }
    return out;
  }

  /**
   * Returns whether the given element makes it through this filter.
   * 
   * @param viewer the viewer
   * @param parentElement the parent element
   * @param element the element
   * @return <code>true</code> if element is included in the filtered set, and
   *         <code>false</code> if excluded
   */
  public abstract boolean select(Viewer viewer, P parentElement, E element);

}
