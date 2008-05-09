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
 * Provides the text and/or icon style for the label of a given element.
 * 
 * <p>
 * This code is based on JFace API from the Eclipse Project.
 * </p>
 */
public abstract class LabelProvider<T> implements BaseLabelProvider<T> {

  /**
   * Returns the text for the label of the given element.
   * 
   * @param element the element for which to provide the label text
   * @return the text string used to label the element, or <code>null</code>
   *         if there is no text label for the given object
   */
  public abstract String getText(T element);

  /**
   * Returns a list of CSS styles to be applied to the element's value.
   * 
   * @param element the element for which to provide the styles
   * @return the styles
   */
  public String getTextStyle(T element) {
    return null;
  }

  /**
   * Returns the icon style for the label of the given element. The style chould
   * be a css class name with a background image specified.
   * 
   * @param element the element for which to provide the label text
   * @return the css class name
   */
  public String getIconStyle(T element) {
    return null;
  }

}
