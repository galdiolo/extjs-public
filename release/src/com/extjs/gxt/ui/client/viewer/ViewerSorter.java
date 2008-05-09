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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.extjs.gxt.ui.client.util.DefaultComparator;

/**
 * A viewer sorter is used by a <code>Viewer</code> to reorder the elements
 * provided by its content provider.
 */
@SuppressWarnings("unchecked")
public class ViewerSorter<T> {

  public static DefaultComparator DEFAULT_COMPARATOR = new DefaultComparator();

  protected Comparator comparator;

  /**
   * Creates a new instance that uses a <code>DefaultComparator</code>.
   */
  @SuppressWarnings("unchecked")
  public ViewerSorter() {
    comparator = DEFAULT_COMPARATOR;
  }

  /**
   * Creates a new sorter instance with the given comparator.
   * 
   * @param comparator the sorter comparator
   */
  public ViewerSorter(Comparator<T> comparator) {
    this.comparator = comparator;
  }

  /**
   * Returns a negative, zero, or positive number depending on whether the first
   * element is less than, equal to, or greater than the second element.
   * 
   * @param viewer the viewer
   * @param e1 the first element
   * @param e2 the second element
   * @return a negative number if the first element is less than the second
   *         element; the value <code>0</code> if the first element is equal
   *         to the second element; and a positive number if the first element
   *         is greater than the second element
   */
  public int compare(Viewer viewer, T e1, T e2) {
    return comparator.compare(e1, e2);
  }

  /**
   * Sorts the given elements in-place, modifying the given array.
   * 
   * @param viewer the viewer
   * @param elements the elemnts to sort
   */
  public void sort(final Viewer viewer, List<T> elements) {
    Collections.sort(elements, new Comparator<T>() {
      public int compare(T a, T b) {
        return ViewerSorter.this.compare(viewer, a, b);
      }
    });
  }

}
