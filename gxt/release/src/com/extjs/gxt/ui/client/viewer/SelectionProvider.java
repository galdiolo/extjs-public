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
 *     Tom Schindl <tom.schindl@bestsolution.at> - bugfixes in issues: 41
 *******************************************************************************/
package com.extjs.gxt.ui.client.viewer;

/**
 * Interface common to all objects that provide a selection.
 */
public interface SelectionProvider<E> {

  /**
   * Returns the current selection for this provider.
   * 
   * @return the current selection
   */
  public Selection<E> getSelection();

  /**
   * Adds a listener for selection changes in this selection provider. Has no
   * effect if an identical listener is already registered.
   * 
   * @param listener a selection changed listener
   */
  public void addSelectionListener(SelectionChangedListener listener);

  /**
   * Removes the given selection change listener from this selection provider.
   * Has no affect if an identical listener is not registered.
   * 
   * @param listener a selection changed listener
   */
  public void removeSelectionListener(SelectionChangedListener listener);

  /**
   * Sets the current selection for this selection provider.
   * 
   * @param selection the new selection
   */
  public void setSelection(Selection<E> selection);

}
