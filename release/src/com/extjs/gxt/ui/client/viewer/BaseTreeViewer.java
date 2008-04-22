/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     GXT - derived implementation
 *******************************************************************************/
package com.extjs.gxt.ui.client.viewer;

/**
 * Defines common methods for tree based viewers.
 * 
 * @see TreeViewer
 * @see TreeTableViewer
 */
public interface BaseTreeViewer {

  /**
   * Adds the given child element to this viewer as a child of the given parent
   * element.
   * 
   * @param parent the parent element
   * @param child the child element
   */
  public void add(Object parent, Object child);

  /**
   * Inserts the given element as a new child element of the given parent
   * element at the given position.
   * 
   * @param parent the parent element
   * @param child the child element
   * @param position the insert position
   */
  public void insert(Object parent, Object child, int position);

  /**
   * Refreshes this viewer completely with information freshly obtained from
   * this viewer's model.
   */
  public void refresh();

  /**
   * Removes the given element from the viewer.
   * 
   * @param element the element to be removed
   */
  public void remove(Object element);

  /**
   * Refreshes labels with information from the viewer's label provider.
   */
  public void update();
  
  /**
   * Refreshes labels with information from the viewer's label provider.
   * 
   * @param elem the element to be updated
   */
  public void update(Object elem);
}
