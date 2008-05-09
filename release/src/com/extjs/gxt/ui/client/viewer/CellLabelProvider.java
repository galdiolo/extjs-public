/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     GXT - derived implementation
 *******************************************************************************/

package com.extjs.gxt.ui.client.viewer;

/**
 * The <code>CellLabelProvider</code> is an implementation of a label provider
 * for structured viewers.
 */
public class CellLabelProvider<E> implements BaseLabelProvider<E> {

  /**
   * Returns the cell's text.
   * 
   * @param element the element
   * @return the text
   */
  public String getText(E element) {
    return element.toString();
  }

  /**
   * Returns the cell's icon style.
   * 
   * @param element the element
   * @return the icon style
   */
  public String getIconStyle(E element) {
    return null;
  }

  /**
   * Returns the cell's text style.
   * 
   * @param element the element
   * @return the text style
   */
  public String getTextStyle(E element) {
    return null;
  }

  /**
   * Update the label for cell.
   * 
   * @param cell the viewer cell
   */
  public void update(ViewerCell<E> cell) {
    cell.setText(getText(cell.getElement()));
    cell.setIconStyle(getIconStyle(cell.getElement()));
    cell.setTextStyle(getTextStyle(cell.getElement()));
  }

}
