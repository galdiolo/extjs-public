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

import com.extjs.gxt.ui.client.widget.Component;

/**
 * The <code>ViewerCell</code> is the representation of a cell.
 */
public class ViewerCell<E> {

  E element;
  Component item;
  private int columnIndex;
  private String columnId;
  private String text;
  private String iconStyle;
  private String textStyle;
  private String toolTipText;

  /**
   * Returns the column's id.
   * 
   * @return the column id
   */
  public String getColumnId() {
    return columnId;
  }

  /**
   * Returns the cell's colum index.
   * 
   * @return the column index
   */
  public int getColumnIndex() {
    return columnIndex;
  }

  /**
   * Returns the cell's element.
   * 
   * @return the element
   */
  public E getElement() {
    return element;
  }

  public String getIconStyle() {
    return iconStyle;
  }

  /**
   * Returns the row component.
   * 
   * @return the row component
   */
  public Component getItem() {
    return item;
  }

  /**
   * Returns the cell's text.
   * 
   * @return the text
   */
  public String getText() {
    return text;
  }

  /**
   * Returns the text style.
   * 
   * @return the text style
   */
  public String getTextStyle() {
    return textStyle;
  }

  /**
   * Returns the cell's tool tip text.
   * 
   * @return the tool tip text
   */
  public String getToolTipText() {
    return toolTipText;
  }

  /**
   * Sets the icon style.
   * 
   * @param iconStyle the icon style
   */
  public void setIconStyle(String iconStyle) {
    this.iconStyle = iconStyle;
  }

  /**
   * Sets the cell's text.
   * 
   * @param text the text
   */
  public void setText(String text) {
    this.text = text;
  }

  /**
   * Sets the cell's text style.
   * 
   * @param textStyle the text style
   */
  public void setTextStyle(String textStyle) {
    this.textStyle = textStyle;
  }

  /**
   * Sets the cell's tool tip text.
   * 
   * @param toolTipText the tool tip text
   */
  public void setToolTipText(String toolTipText) {
    this.toolTipText = toolTipText;
  }

  void reset(E element, Component item, int columnIndex, String columnId) {
    this.element = element;
    this.columnIndex = columnIndex;
    this.columnId = columnId;
    this.item = item;
    text = null;
    textStyle = null;
    toolTipText = null;
  }

}
